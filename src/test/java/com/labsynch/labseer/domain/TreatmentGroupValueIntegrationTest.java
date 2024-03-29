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
public class TreatmentGroupValueIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    TreatmentGroupValueDataOnDemand dod;

    @Test
    public void testCountTreatmentGroupValues() {
        Assert.assertNotNull("Data on demand for 'TreatmentGroupValue' failed to initialize correctly",
                dod.getRandomTreatmentGroupValue());
        long count = TreatmentGroupValue.countTreatmentGroupValues();
        Assert.assertTrue("Counter for 'TreatmentGroupValue' incorrectly reported there were no entries", count > 0);
    }

    @Test
    public void testFindTreatmentGroupValue() {
        TreatmentGroupValue obj = dod.getRandomTreatmentGroupValue();
        Assert.assertNotNull("Data on demand for 'TreatmentGroupValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'TreatmentGroupValue' failed to provide an identifier", id);
        obj = TreatmentGroupValue.findTreatmentGroupValue(id);
        Assert.assertNotNull("Find method for 'TreatmentGroupValue' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'TreatmentGroupValue' returned the incorrect identifier", id, obj.getId());
    }

    @Test
    public void testFindAllTreatmentGroupValues() {
        Assert.assertNotNull("Data on demand for 'TreatmentGroupValue' failed to initialize correctly",
                dod.getRandomTreatmentGroupValue());
        long count = TreatmentGroupValue.countTreatmentGroupValues();
        Assert.assertTrue("Too expensive to perform a find all test for 'TreatmentGroupValue', as there are " + count
                + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
                count < 250);
        List<TreatmentGroupValue> result = TreatmentGroupValue.findAllTreatmentGroupValues();
        Assert.assertNotNull("Find all method for 'TreatmentGroupValue' illegally returned null", result);
        Assert.assertTrue("Find all method for 'TreatmentGroupValue' failed to return any data", result.size() > 0);
    }

    @Test
    public void testFindTreatmentGroupValueEntries() {
        Assert.assertNotNull("Data on demand for 'TreatmentGroupValue' failed to initialize correctly",
                dod.getRandomTreatmentGroupValue());
        long count = TreatmentGroupValue.countTreatmentGroupValues();
        if (count > 20)
            count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<TreatmentGroupValue> result = TreatmentGroupValue.findTreatmentGroupValueEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'TreatmentGroupValue' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'TreatmentGroupValue' returned an incorrect number of entries",
                count, result.size());
    }

    @Test
    public void testFlush() {
        TreatmentGroupValue obj = dod.getRandomTreatmentGroupValue();
        Assert.assertNotNull("Data on demand for 'TreatmentGroupValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'TreatmentGroupValue' failed to provide an identifier", id);
        obj = TreatmentGroupValue.findTreatmentGroupValue(id);
        Assert.assertNotNull("Find method for 'TreatmentGroupValue' illegally returned null for id '" + id + "'", obj);
        boolean modified = dod.modifyTreatmentGroupValue(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'TreatmentGroupValue' failed to increment on flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testMergeUpdate() {
        TreatmentGroupValue obj = dod.getRandomTreatmentGroupValue();
        Assert.assertNotNull("Data on demand for 'TreatmentGroupValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'TreatmentGroupValue' failed to provide an identifier", id);
        obj = TreatmentGroupValue.findTreatmentGroupValue(id);
        boolean modified = dod.modifyTreatmentGroupValue(obj);
        Integer currentVersion = obj.getVersion();
        TreatmentGroupValue merged = (TreatmentGroupValue) obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(),
                id);
        Assert.assertTrue("Version for 'TreatmentGroupValue' failed to increment on merge and flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'TreatmentGroupValue' failed to initialize correctly",
                dod.getRandomTreatmentGroupValue());
        TreatmentGroupValue obj = dod.getNewTransientTreatmentGroupValue(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'TreatmentGroupValue' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'TreatmentGroupValue' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'TreatmentGroupValue' identifier to no longer be null", obj.getId());
    }

    @Test
    public void testRemove() {
        TreatmentGroupValue obj = dod.getRandomTreatmentGroupValue();
        Assert.assertNotNull("Data on demand for 'TreatmentGroupValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'TreatmentGroupValue' failed to provide an identifier", id);
        obj = TreatmentGroupValue.findTreatmentGroupValue(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'TreatmentGroupValue' with identifier '" + id + "'",
                TreatmentGroupValue.findTreatmentGroupValue(id));
    }
}
