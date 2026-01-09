package com.labsynch.labseer.domain;

import java.util.Iterator;
import java.util.List;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

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
public class ExperimentTypeIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    ExperimentTypeDataOnDemand dod;

    @Test
    public void testCountExperimentTypes() {
        Assert.assertNotNull("Data on demand for 'ExperimentType' failed to initialize correctly",
                dod.getRandomExperimentType());
        long count = ExperimentType.countExperimentTypes();
        Assert.assertTrue("Counter for 'ExperimentType' incorrectly reported there were no entries", count > 0);
    }

    @Test
    public void testFindExperimentType() {
        ExperimentType obj = dod.getRandomExperimentType();
        Assert.assertNotNull("Data on demand for 'ExperimentType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ExperimentType' failed to provide an identifier", id);
        obj = ExperimentType.findExperimentType(id);
        Assert.assertNotNull("Find method for 'ExperimentType' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ExperimentType' returned the incorrect identifier", id, obj.getId());
    }

    @Test
    public void testFindAllExperimentTypes() {
        Assert.assertNotNull("Data on demand for 'ExperimentType' failed to initialize correctly",
                dod.getRandomExperimentType());
        long count = ExperimentType.countExperimentTypes();
        Assert.assertTrue("Too expensive to perform a find all test for 'ExperimentType', as there are " + count
                + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
                count < 250);
        List<ExperimentType> result = ExperimentType.findAllExperimentTypes();
        Assert.assertNotNull("Find all method for 'ExperimentType' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ExperimentType' failed to return any data", result.size() > 0);
    }

    @Test
    public void testFindExperimentTypeEntries() {
        Assert.assertNotNull("Data on demand for 'ExperimentType' failed to initialize correctly",
                dod.getRandomExperimentType());
        long count = ExperimentType.countExperimentTypes();
        if (count > 20)
            count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ExperimentType> result = ExperimentType.findExperimentTypeEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ExperimentType' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ExperimentType' returned an incorrect number of entries", count,
                result.size());
    }

    @Test
    public void testFlush() {
        ExperimentType obj = dod.getRandomExperimentType();
        Assert.assertNotNull("Data on demand for 'ExperimentType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ExperimentType' failed to provide an identifier", id);
        obj = ExperimentType.findExperimentType(id);
        Assert.assertNotNull("Find method for 'ExperimentType' illegally returned null for id '" + id + "'", obj);
        boolean modified = dod.modifyExperimentType(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ExperimentType' failed to increment on flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testMergeUpdate() {
        ExperimentType obj = dod.getRandomExperimentType();
        Assert.assertNotNull("Data on demand for 'ExperimentType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ExperimentType' failed to provide an identifier", id);
        obj = ExperimentType.findExperimentType(id);
        boolean modified = dod.modifyExperimentType(obj);
        Integer currentVersion = obj.getVersion();
        ExperimentType merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(),
                id);
        Assert.assertTrue("Version for 'ExperimentType' failed to increment on merge and flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'ExperimentType' failed to initialize correctly",
                dod.getRandomExperimentType());
        ExperimentType obj = dod.getNewTransientExperimentType(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ExperimentType' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ExperimentType' identifier to be null", obj.getId());
        try {
            obj.persist();
        } catch (final ConstraintViolationException e) {
            final StringBuilder msg = new StringBuilder();
            for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                final ConstraintViolation<?> cv = iter.next();
                msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath())
                        .append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue())
                        .append(")").append("]");
            }
            throw new IllegalStateException(msg.toString(), e);
        }
        obj.flush();
        Assert.assertNotNull("Expected 'ExperimentType' identifier to no longer be null", obj.getId());
    }

    @Test
    public void testRemove() {
        ExperimentType obj = dod.getRandomExperimentType();
        Assert.assertNotNull("Data on demand for 'ExperimentType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ExperimentType' failed to provide an identifier", id);
        obj = ExperimentType.findExperimentType(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ExperimentType' with identifier '" + id + "'",
                ExperimentType.findExperimentType(id));
    }
}
