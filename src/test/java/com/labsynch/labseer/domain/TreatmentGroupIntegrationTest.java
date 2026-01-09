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
public class TreatmentGroupIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    TreatmentGroupDataOnDemand dod;

    @Test
    public void testCountTreatmentGroups() {
        Assert.assertNotNull("Data on demand for 'TreatmentGroup' failed to initialize correctly",
                dod.getRandomTreatmentGroup());
        long count = TreatmentGroup.countTreatmentGroups();
        Assert.assertTrue("Counter for 'TreatmentGroup' incorrectly reported there were no entries", count > 0);
    }

    @Test
    public void testFindTreatmentGroup() {
        TreatmentGroup obj = dod.getRandomTreatmentGroup();
        Assert.assertNotNull("Data on demand for 'TreatmentGroup' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'TreatmentGroup' failed to provide an identifier", id);
        obj = TreatmentGroup.findTreatmentGroup(id);
        Assert.assertNotNull("Find method for 'TreatmentGroup' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'TreatmentGroup' returned the incorrect identifier", id, obj.getId());
    }

    @Test
    public void testFindAllTreatmentGroups() {
        Assert.assertNotNull("Data on demand for 'TreatmentGroup' failed to initialize correctly",
                dod.getRandomTreatmentGroup());
        long count = TreatmentGroup.countTreatmentGroups();
        Assert.assertTrue("Too expensive to perform a find all test for 'TreatmentGroup', as there are " + count
                + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
                count < 250);
        List<TreatmentGroup> result = TreatmentGroup.findAllTreatmentGroups();
        Assert.assertNotNull("Find all method for 'TreatmentGroup' illegally returned null", result);
        Assert.assertTrue("Find all method for 'TreatmentGroup' failed to return any data", result.size() > 0);
    }

    @Test
    public void testFindTreatmentGroupEntries() {
        Assert.assertNotNull("Data on demand for 'TreatmentGroup' failed to initialize correctly",
                dod.getRandomTreatmentGroup());
        long count = TreatmentGroup.countTreatmentGroups();
        if (count > 20)
            count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<TreatmentGroup> result = TreatmentGroup.findTreatmentGroupEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'TreatmentGroup' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'TreatmentGroup' returned an incorrect number of entries", count,
                result.size());
    }

    @Test
    public void testFlush() {
        TreatmentGroup obj = dod.getRandomTreatmentGroup();
        Assert.assertNotNull("Data on demand for 'TreatmentGroup' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'TreatmentGroup' failed to provide an identifier", id);
        obj = TreatmentGroup.findTreatmentGroup(id);
        Assert.assertNotNull("Find method for 'TreatmentGroup' illegally returned null for id '" + id + "'", obj);
        boolean modified = dod.modifyTreatmentGroup(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'TreatmentGroup' failed to increment on flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testMergeUpdate() {
        TreatmentGroup obj = dod.getRandomTreatmentGroup();
        Assert.assertNotNull("Data on demand for 'TreatmentGroup' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'TreatmentGroup' failed to provide an identifier", id);
        obj = TreatmentGroup.findTreatmentGroup(id);
        boolean modified = dod.modifyTreatmentGroup(obj);
        Integer currentVersion = obj.getVersion();
        TreatmentGroup merged = (TreatmentGroup) obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(),
                id);
        Assert.assertTrue("Version for 'TreatmentGroup' failed to increment on merge and flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'TreatmentGroup' failed to initialize correctly",
                dod.getRandomTreatmentGroup());
        TreatmentGroup obj = dod.getNewTransientTreatmentGroup(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'TreatmentGroup' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'TreatmentGroup' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'TreatmentGroup' identifier to no longer be null", obj.getId());
    }

    @Test
    public void testRemove() {
        TreatmentGroup obj = dod.getRandomTreatmentGroup();
        Assert.assertNotNull("Data on demand for 'TreatmentGroup' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'TreatmentGroup' failed to provide an identifier", id);
        obj = TreatmentGroup.findTreatmentGroup(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'TreatmentGroup' with identifier '" + id + "'",
                TreatmentGroup.findTreatmentGroup(id));
    }
}
