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
public class ExperimentLabelIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    ExperimentLabelDataOnDemand dod;

    @Test
    public void testCountExperimentLabels() {
        Assert.assertNotNull("Data on demand for 'ExperimentLabel' failed to initialize correctly",
                dod.getRandomExperimentLabel());
        long count = ExperimentLabel.countExperimentLabels();
        Assert.assertTrue("Counter for 'ExperimentLabel' incorrectly reported there were no entries", count > 0);
    }

    @Test
    public void testFindExperimentLabel() {
        ExperimentLabel obj = dod.getRandomExperimentLabel();
        Assert.assertNotNull("Data on demand for 'ExperimentLabel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ExperimentLabel' failed to provide an identifier", id);
        obj = ExperimentLabel.findExperimentLabel(id);
        Assert.assertNotNull("Find method for 'ExperimentLabel' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ExperimentLabel' returned the incorrect identifier", id, obj.getId());
    }

    @Test
    public void testFindAllExperimentLabels() {
        Assert.assertNotNull("Data on demand for 'ExperimentLabel' failed to initialize correctly",
                dod.getRandomExperimentLabel());
        long count = ExperimentLabel.countExperimentLabels();
        Assert.assertTrue("Too expensive to perform a find all test for 'ExperimentLabel', as there are " + count
                + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
                count < 250);
        List<ExperimentLabel> result = ExperimentLabel.findAllExperimentLabels();
        Assert.assertNotNull("Find all method for 'ExperimentLabel' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ExperimentLabel' failed to return any data", result.size() > 0);
    }

    @Test
    public void testFindExperimentLabelEntries() {
        Assert.assertNotNull("Data on demand for 'ExperimentLabel' failed to initialize correctly",
                dod.getRandomExperimentLabel());
        long count = ExperimentLabel.countExperimentLabels();
        if (count > 20)
            count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ExperimentLabel> result = ExperimentLabel.findExperimentLabelEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ExperimentLabel' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ExperimentLabel' returned an incorrect number of entries", count,
                result.size());
    }

    @Test
    public void testFlush() {
        ExperimentLabel obj = dod.getRandomExperimentLabel();
        Assert.assertNotNull("Data on demand for 'ExperimentLabel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ExperimentLabel' failed to provide an identifier", id);
        obj = ExperimentLabel.findExperimentLabel(id);
        Assert.assertNotNull("Find method for 'ExperimentLabel' illegally returned null for id '" + id + "'", obj);
        boolean modified = dod.modifyExperimentLabel(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ExperimentLabel' failed to increment on flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testMergeUpdate() {
        ExperimentLabel obj = dod.getRandomExperimentLabel();
        Assert.assertNotNull("Data on demand for 'ExperimentLabel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ExperimentLabel' failed to provide an identifier", id);
        obj = ExperimentLabel.findExperimentLabel(id);
        boolean modified = dod.modifyExperimentLabel(obj);
        Integer currentVersion = obj.getVersion();
        ExperimentLabel merged = (ExperimentLabel) obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(),
                id);
        Assert.assertTrue("Version for 'ExperimentLabel' failed to increment on merge and flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'ExperimentLabel' failed to initialize correctly",
                dod.getRandomExperimentLabel());
        ExperimentLabel obj = dod.getNewTransientExperimentLabel(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ExperimentLabel' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ExperimentLabel' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'ExperimentLabel' identifier to no longer be null", obj.getId());
    }

    @Test
    public void testRemove() {
        ExperimentLabel obj = dod.getRandomExperimentLabel();
        Assert.assertNotNull("Data on demand for 'ExperimentLabel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ExperimentLabel' failed to provide an identifier", id);
        obj = ExperimentLabel.findExperimentLabel(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ExperimentLabel' with identifier '" + id + "'",
                ExperimentLabel.findExperimentLabel(id));
    }
}
