package com.labsynch.labseer.domain;

import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Transactional
public class ExperimentValueIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    ExperimentValueDataOnDemand dod;

	@Test
    public void testCountExperimentValues() {
        Assert.assertNotNull("Data on demand for 'ExperimentValue' failed to initialize correctly", dod.getRandomExperimentValue());
        long count = ExperimentValue.countExperimentValues();
        Assert.assertTrue("Counter for 'ExperimentValue' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindExperimentValue() {
        ExperimentValue obj = dod.getRandomExperimentValue();
        Assert.assertNotNull("Data on demand for 'ExperimentValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ExperimentValue' failed to provide an identifier", id);
        obj = ExperimentValue.findExperimentValue(id);
        Assert.assertNotNull("Find method for 'ExperimentValue' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ExperimentValue' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllExperimentValues() {
        Assert.assertNotNull("Data on demand for 'ExperimentValue' failed to initialize correctly", dod.getRandomExperimentValue());
        long count = ExperimentValue.countExperimentValues();
        Assert.assertTrue("Too expensive to perform a find all test for 'ExperimentValue', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<ExperimentValue> result = ExperimentValue.findAllExperimentValues();
        Assert.assertNotNull("Find all method for 'ExperimentValue' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ExperimentValue' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindExperimentValueEntries() {
        Assert.assertNotNull("Data on demand for 'ExperimentValue' failed to initialize correctly", dod.getRandomExperimentValue());
        long count = ExperimentValue.countExperimentValues();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ExperimentValue> result = ExperimentValue.findExperimentValueEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ExperimentValue' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ExperimentValue' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        ExperimentValue obj = dod.getRandomExperimentValue();
        Assert.assertNotNull("Data on demand for 'ExperimentValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ExperimentValue' failed to provide an identifier", id);
        obj = ExperimentValue.findExperimentValue(id);
        Assert.assertNotNull("Find method for 'ExperimentValue' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyExperimentValue(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ExperimentValue' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        ExperimentValue obj = dod.getRandomExperimentValue();
        Assert.assertNotNull("Data on demand for 'ExperimentValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ExperimentValue' failed to provide an identifier", id);
        obj = ExperimentValue.findExperimentValue(id);
        boolean modified =  dod.modifyExperimentValue(obj);
        Integer currentVersion = obj.getVersion();
        ExperimentValue merged = (ExperimentValue)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'ExperimentValue' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'ExperimentValue' failed to initialize correctly", dod.getRandomExperimentValue());
        ExperimentValue obj = dod.getNewTransientExperimentValue(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ExperimentValue' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ExperimentValue' identifier to be null", obj.getId());
        try {
            obj.persist();
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        obj.flush();
        Assert.assertNotNull("Expected 'ExperimentValue' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        ExperimentValue obj = dod.getRandomExperimentValue();
        Assert.assertNotNull("Data on demand for 'ExperimentValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ExperimentValue' failed to provide an identifier", id);
        obj = ExperimentValue.findExperimentValue(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ExperimentValue' with identifier '" + id + "'", ExperimentValue.findExperimentValue(id));
    }
}
