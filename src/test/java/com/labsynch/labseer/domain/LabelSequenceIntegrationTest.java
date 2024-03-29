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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Transactional
@Configurable
public class LabelSequenceIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    LabelSequenceDataOnDemand dod;

    @Test
    public void testCountLabelSequences() {
        Assert.assertNotNull("Data on demand for 'LabelSequence' failed to initialize correctly",
                dod.getRandomLabelSequence());
        long count = LabelSequence.countLabelSequences();
        Assert.assertTrue("Counter for 'LabelSequence' incorrectly reported there were no entries", count > 0);
    }

    @Test
    public void testFindLabelSequence() {
        LabelSequence obj = dod.getRandomLabelSequence();
        Assert.assertNotNull("Data on demand for 'LabelSequence' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LabelSequence' failed to provide an identifier", id);
        obj = LabelSequence.findLabelSequence(id);
        Assert.assertNotNull("Find method for 'LabelSequence' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'LabelSequence' returned the incorrect identifier", id, obj.getId());
    }

    @Test
    public void testFindAllLabelSequences() {
        Assert.assertNotNull("Data on demand for 'LabelSequence' failed to initialize correctly",
                dod.getRandomLabelSequence());
        long count = LabelSequence.countLabelSequences();
        Assert.assertTrue("Too expensive to perform a find all test for 'LabelSequence', as there are " + count
                + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
                count < 250);
        List<LabelSequence> result = LabelSequence.findAllLabelSequences();
        Assert.assertNotNull("Find all method for 'LabelSequence' illegally returned null", result);
        Assert.assertTrue("Find all method for 'LabelSequence' failed to return any data", result.size() > 0);
    }

    @Test
    public void testFindLabelSequenceEntries() {
        Assert.assertNotNull("Data on demand for 'LabelSequence' failed to initialize correctly",
                dod.getRandomLabelSequence());
        long count = LabelSequence.countLabelSequences();
        if (count > 20)
            count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<LabelSequence> result = LabelSequence.findLabelSequenceEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'LabelSequence' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'LabelSequence' returned an incorrect number of entries", count,
                result.size());
    }

    @Test
    public void testFlush() {
        LabelSequence obj = dod.getRandomLabelSequence();
        Assert.assertNotNull("Data on demand for 'LabelSequence' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LabelSequence' failed to provide an identifier", id);
        obj = LabelSequence.findLabelSequence(id);
        Assert.assertNotNull("Find method for 'LabelSequence' illegally returned null for id '" + id + "'", obj);
        boolean modified = dod.modifyLabelSequence(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'LabelSequence' failed to increment on flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testMergeUpdate() {
        LabelSequence obj = dod.getRandomLabelSequence();
        Assert.assertNotNull("Data on demand for 'LabelSequence' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LabelSequence' failed to provide an identifier", id);
        obj = LabelSequence.findLabelSequence(id);
        boolean modified = dod.modifyLabelSequence(obj);
        Integer currentVersion = obj.getVersion();
        LabelSequence merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(),
                id);
        Assert.assertTrue("Version for 'LabelSequence' failed to increment on merge and flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'LabelSequence' failed to initialize correctly",
                dod.getRandomLabelSequence());
        LabelSequence obj = dod.getNewTransientLabelSequence(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'LabelSequence' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'LabelSequence' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'LabelSequence' identifier to no longer be null", obj.getId());
    }

    @Test
    public void testRemove() {
        LabelSequence obj = dod.getRandomLabelSequence();
        Assert.assertNotNull("Data on demand for 'LabelSequence' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LabelSequence' failed to provide an identifier", id);
        obj = LabelSequence.findLabelSequence(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'LabelSequence' with identifier '" + id + "'",
                LabelSequence.findLabelSequence(id));
    }
}
