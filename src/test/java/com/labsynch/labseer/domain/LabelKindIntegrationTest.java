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
public class LabelKindIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    LabelKindDataOnDemand dod;

    @Test
    public void testCountLabelKinds() {
        Assert.assertNotNull("Data on demand for 'LabelKind' failed to initialize correctly", dod.getRandomLabelKind());
        long count = LabelKind.countLabelKinds();
        Assert.assertTrue("Counter for 'LabelKind' incorrectly reported there were no entries", count > 0);
    }

    @Test
    public void testFindLabelKind() {
        LabelKind obj = dod.getRandomLabelKind();
        Assert.assertNotNull("Data on demand for 'LabelKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LabelKind' failed to provide an identifier", id);
        obj = LabelKind.findLabelKind(id);
        Assert.assertNotNull("Find method for 'LabelKind' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'LabelKind' returned the incorrect identifier", id, obj.getId());
    }

    @Test
    public void testFindAllLabelKinds() {
        Assert.assertNotNull("Data on demand for 'LabelKind' failed to initialize correctly", dod.getRandomLabelKind());
        long count = LabelKind.countLabelKinds();
        Assert.assertTrue("Too expensive to perform a find all test for 'LabelKind', as there are " + count
                + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
                count < 250);
        List<LabelKind> result = LabelKind.findAllLabelKinds();
        Assert.assertNotNull("Find all method for 'LabelKind' illegally returned null", result);
        Assert.assertTrue("Find all method for 'LabelKind' failed to return any data", result.size() > 0);
    }

    @Test
    public void testFindLabelKindEntries() {
        Assert.assertNotNull("Data on demand for 'LabelKind' failed to initialize correctly", dod.getRandomLabelKind());
        long count = LabelKind.countLabelKinds();
        if (count > 20)
            count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<LabelKind> result = LabelKind.findLabelKindEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'LabelKind' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'LabelKind' returned an incorrect number of entries", count,
                result.size());
    }

    @Test
    public void testFlush() {
        LabelKind obj = dod.getRandomLabelKind();
        Assert.assertNotNull("Data on demand for 'LabelKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LabelKind' failed to provide an identifier", id);
        obj = LabelKind.findLabelKind(id);
        Assert.assertNotNull("Find method for 'LabelKind' illegally returned null for id '" + id + "'", obj);
        boolean modified = dod.modifyLabelKind(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'LabelKind' failed to increment on flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testMergeUpdate() {
        LabelKind obj = dod.getRandomLabelKind();
        Assert.assertNotNull("Data on demand for 'LabelKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LabelKind' failed to provide an identifier", id);
        obj = LabelKind.findLabelKind(id);
        boolean modified = dod.modifyLabelKind(obj);
        Integer currentVersion = obj.getVersion();
        LabelKind merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(),
                id);
        Assert.assertTrue("Version for 'LabelKind' failed to increment on merge and flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'LabelKind' failed to initialize correctly", dod.getRandomLabelKind());
        LabelKind obj = dod.getNewTransientLabelKind(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'LabelKind' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'LabelKind' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'LabelKind' identifier to no longer be null", obj.getId());
    }

    @Test
    public void testRemove() {
        LabelKind obj = dod.getRandomLabelKind();
        Assert.assertNotNull("Data on demand for 'LabelKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LabelKind' failed to provide an identifier", id);
        obj = LabelKind.findLabelKind(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'LabelKind' with identifier '" + id + "'", LabelKind.findLabelKind(id));
    }
}
