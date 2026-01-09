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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Transactional
@Configurable
public class UnitTypeIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    UnitTypeDataOnDemand dod;

    @Test
    public void testCountUnitTypes() {
        Assert.assertNotNull("Data on demand for 'UnitType' failed to initialize correctly", dod.getRandomUnitType());
        long count = UnitType.countUnitTypes();
        Assert.assertTrue("Counter for 'UnitType' incorrectly reported there were no entries", count > 0);
    }

    @Test
    public void testFindUnitType() {
        UnitType obj = dod.getRandomUnitType();
        Assert.assertNotNull("Data on demand for 'UnitType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UnitType' failed to provide an identifier", id);
        obj = UnitType.findUnitType(id);
        Assert.assertNotNull("Find method for 'UnitType' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'UnitType' returned the incorrect identifier", id, obj.getId());
    }

    @Test
    public void testFindAllUnitTypes() {
        Assert.assertNotNull("Data on demand for 'UnitType' failed to initialize correctly", dod.getRandomUnitType());
        long count = UnitType.countUnitTypes();
        Assert.assertTrue("Too expensive to perform a find all test for 'UnitType', as there are " + count
                + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
                count < 250);
        List<UnitType> result = UnitType.findAllUnitTypes();
        Assert.assertNotNull("Find all method for 'UnitType' illegally returned null", result);
        Assert.assertTrue("Find all method for 'UnitType' failed to return any data", result.size() > 0);
    }

    @Test
    public void testFindUnitTypeEntries() {
        Assert.assertNotNull("Data on demand for 'UnitType' failed to initialize correctly", dod.getRandomUnitType());
        long count = UnitType.countUnitTypes();
        if (count > 20)
            count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<UnitType> result = UnitType.findUnitTypeEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'UnitType' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'UnitType' returned an incorrect number of entries", count,
                result.size());
    }

    @Test
    public void testFlush() {
        UnitType obj = dod.getRandomUnitType();
        Assert.assertNotNull("Data on demand for 'UnitType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UnitType' failed to provide an identifier", id);
        obj = UnitType.findUnitType(id);
        Assert.assertNotNull("Find method for 'UnitType' illegally returned null for id '" + id + "'", obj);
        boolean modified = dod.modifyUnitType(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'UnitType' failed to increment on flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testMergeUpdate() {
        UnitType obj = dod.getRandomUnitType();
        Assert.assertNotNull("Data on demand for 'UnitType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UnitType' failed to provide an identifier", id);
        obj = UnitType.findUnitType(id);
        boolean modified = dod.modifyUnitType(obj);
        Integer currentVersion = obj.getVersion();
        UnitType merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(),
                id);
        Assert.assertTrue("Version for 'UnitType' failed to increment on merge and flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'UnitType' failed to initialize correctly", dod.getRandomUnitType());
        UnitType obj = dod.getNewTransientUnitType(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'UnitType' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'UnitType' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'UnitType' identifier to no longer be null", obj.getId());
    }

    @Test
    public void testRemove() {
        UnitType obj = dod.getRandomUnitType();
        Assert.assertNotNull("Data on demand for 'UnitType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UnitType' failed to provide an identifier", id);
        obj = UnitType.findUnitType(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'UnitType' with identifier '" + id + "'", UnitType.findUnitType(id));
    }
}
