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
public class InteractionTypeIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    InteractionTypeDataOnDemand dod;

	@Test
    public void testCountInteractionTypes() {
        Assert.assertNotNull("Data on demand for 'InteractionType' failed to initialize correctly", dod.getRandomInteractionType());
        long count = InteractionType.countInteractionTypes();
        Assert.assertTrue("Counter for 'InteractionType' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindInteractionType() {
        InteractionType obj = dod.getRandomInteractionType();
        Assert.assertNotNull("Data on demand for 'InteractionType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'InteractionType' failed to provide an identifier", id);
        obj = InteractionType.findInteractionType(id);
        Assert.assertNotNull("Find method for 'InteractionType' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'InteractionType' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllInteractionTypes() {
        Assert.assertNotNull("Data on demand for 'InteractionType' failed to initialize correctly", dod.getRandomInteractionType());
        long count = InteractionType.countInteractionTypes();
        Assert.assertTrue("Too expensive to perform a find all test for 'InteractionType', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<InteractionType> result = InteractionType.findAllInteractionTypes();
        Assert.assertNotNull("Find all method for 'InteractionType' illegally returned null", result);
        Assert.assertTrue("Find all method for 'InteractionType' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindInteractionTypeEntries() {
        Assert.assertNotNull("Data on demand for 'InteractionType' failed to initialize correctly", dod.getRandomInteractionType());
        long count = InteractionType.countInteractionTypes();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<InteractionType> result = InteractionType.findInteractionTypeEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'InteractionType' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'InteractionType' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        InteractionType obj = dod.getRandomInteractionType();
        Assert.assertNotNull("Data on demand for 'InteractionType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'InteractionType' failed to provide an identifier", id);
        obj = InteractionType.findInteractionType(id);
        Assert.assertNotNull("Find method for 'InteractionType' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyInteractionType(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'InteractionType' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        InteractionType obj = dod.getRandomInteractionType();
        Assert.assertNotNull("Data on demand for 'InteractionType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'InteractionType' failed to provide an identifier", id);
        obj = InteractionType.findInteractionType(id);
        boolean modified =  dod.modifyInteractionType(obj);
        Integer currentVersion = obj.getVersion();
        InteractionType merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'InteractionType' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'InteractionType' failed to initialize correctly", dod.getRandomInteractionType());
        InteractionType obj = dod.getNewTransientInteractionType(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'InteractionType' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'InteractionType' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'InteractionType' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        InteractionType obj = dod.getRandomInteractionType();
        Assert.assertNotNull("Data on demand for 'InteractionType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'InteractionType' failed to provide an identifier", id);
        obj = InteractionType.findInteractionType(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'InteractionType' with identifier '" + id + "'", InteractionType.findInteractionType(id));
    }
}
