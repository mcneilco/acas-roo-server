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
public class ThingTypeIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    ThingTypeDataOnDemand dod;

	@Test
    public void testCountThingTypes() {
        Assert.assertNotNull("Data on demand for 'ThingType' failed to initialize correctly", dod.getRandomThingType());
        long count = ThingType.countThingTypes();
        Assert.assertTrue("Counter for 'ThingType' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindThingType() {
        ThingType obj = dod.getRandomThingType();
        Assert.assertNotNull("Data on demand for 'ThingType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ThingType' failed to provide an identifier", id);
        obj = ThingType.findThingType(id);
        Assert.assertNotNull("Find method for 'ThingType' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ThingType' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllThingTypes() {
        Assert.assertNotNull("Data on demand for 'ThingType' failed to initialize correctly", dod.getRandomThingType());
        long count = ThingType.countThingTypes();
        Assert.assertTrue("Too expensive to perform a find all test for 'ThingType', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<ThingType> result = ThingType.findAllThingTypes();
        Assert.assertNotNull("Find all method for 'ThingType' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ThingType' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindThingTypeEntries() {
        Assert.assertNotNull("Data on demand for 'ThingType' failed to initialize correctly", dod.getRandomThingType());
        long count = ThingType.countThingTypes();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ThingType> result = ThingType.findThingTypeEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ThingType' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ThingType' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        ThingType obj = dod.getRandomThingType();
        Assert.assertNotNull("Data on demand for 'ThingType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ThingType' failed to provide an identifier", id);
        obj = ThingType.findThingType(id);
        Assert.assertNotNull("Find method for 'ThingType' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyThingType(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ThingType' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        ThingType obj = dod.getRandomThingType();
        Assert.assertNotNull("Data on demand for 'ThingType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ThingType' failed to provide an identifier", id);
        obj = ThingType.findThingType(id);
        boolean modified =  dod.modifyThingType(obj);
        Integer currentVersion = obj.getVersion();
        ThingType merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'ThingType' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'ThingType' failed to initialize correctly", dod.getRandomThingType());
        ThingType obj = dod.getNewTransientThingType(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ThingType' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ThingType' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'ThingType' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        ThingType obj = dod.getRandomThingType();
        Assert.assertNotNull("Data on demand for 'ThingType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ThingType' failed to provide an identifier", id);
        obj = ThingType.findThingType(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ThingType' with identifier '" + id + "'", ThingType.findThingType(id));
    }
}
