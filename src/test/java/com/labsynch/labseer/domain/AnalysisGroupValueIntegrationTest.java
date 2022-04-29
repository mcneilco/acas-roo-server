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
public class AnalysisGroupValueIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    AnalysisGroupValueDataOnDemand dod;

	@Test
    public void testCountAnalysisGroupValues() {
        Assert.assertNotNull("Data on demand for 'AnalysisGroupValue' failed to initialize correctly", dod.getRandomAnalysisGroupValue());
        long count = AnalysisGroupValue.countAnalysisGroupValues();
        Assert.assertTrue("Counter for 'AnalysisGroupValue' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindAnalysisGroupValue() {
        AnalysisGroupValue obj = dod.getRandomAnalysisGroupValue();
        Assert.assertNotNull("Data on demand for 'AnalysisGroupValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AnalysisGroupValue' failed to provide an identifier", id);
        obj = AnalysisGroupValue.findAnalysisGroupValue(id);
        Assert.assertNotNull("Find method for 'AnalysisGroupValue' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'AnalysisGroupValue' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllAnalysisGroupValues() {
        Assert.assertNotNull("Data on demand for 'AnalysisGroupValue' failed to initialize correctly", dod.getRandomAnalysisGroupValue());
        long count = AnalysisGroupValue.countAnalysisGroupValues();
        Assert.assertTrue("Too expensive to perform a find all test for 'AnalysisGroupValue', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<AnalysisGroupValue> result = AnalysisGroupValue.findAllAnalysisGroupValues();
        Assert.assertNotNull("Find all method for 'AnalysisGroupValue' illegally returned null", result);
        Assert.assertTrue("Find all method for 'AnalysisGroupValue' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindAnalysisGroupValueEntries() {
        Assert.assertNotNull("Data on demand for 'AnalysisGroupValue' failed to initialize correctly", dod.getRandomAnalysisGroupValue());
        long count = AnalysisGroupValue.countAnalysisGroupValues();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<AnalysisGroupValue> result = AnalysisGroupValue.findAnalysisGroupValueEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'AnalysisGroupValue' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'AnalysisGroupValue' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        AnalysisGroupValue obj = dod.getRandomAnalysisGroupValue();
        Assert.assertNotNull("Data on demand for 'AnalysisGroupValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AnalysisGroupValue' failed to provide an identifier", id);
        obj = AnalysisGroupValue.findAnalysisGroupValue(id);
        Assert.assertNotNull("Find method for 'AnalysisGroupValue' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyAnalysisGroupValue(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'AnalysisGroupValue' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        AnalysisGroupValue obj = dod.getRandomAnalysisGroupValue();
        Assert.assertNotNull("Data on demand for 'AnalysisGroupValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AnalysisGroupValue' failed to provide an identifier", id);
        obj = AnalysisGroupValue.findAnalysisGroupValue(id);
        boolean modified =  dod.modifyAnalysisGroupValue(obj);
        Integer currentVersion = obj.getVersion();
        AnalysisGroupValue merged = (AnalysisGroupValue)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'AnalysisGroupValue' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'AnalysisGroupValue' failed to initialize correctly", dod.getRandomAnalysisGroupValue());
        AnalysisGroupValue obj = dod.getNewTransientAnalysisGroupValue(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'AnalysisGroupValue' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'AnalysisGroupValue' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'AnalysisGroupValue' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        AnalysisGroupValue obj = dod.getRandomAnalysisGroupValue();
        Assert.assertNotNull("Data on demand for 'AnalysisGroupValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'AnalysisGroupValue' failed to provide an identifier", id);
        obj = AnalysisGroupValue.findAnalysisGroupValue(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'AnalysisGroupValue' with identifier '" + id + "'", AnalysisGroupValue.findAnalysisGroupValue(id));
    }
}
