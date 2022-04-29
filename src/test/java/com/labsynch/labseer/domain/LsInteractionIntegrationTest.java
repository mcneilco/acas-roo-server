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
import org.springframework.roo.addon.test.RooIntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Transactional
@Configurable
@RooIntegrationTest(entity = LsInteraction.class)
public class LsInteractionIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    LsInteractionDataOnDemand dod;

	@Test
    public void testCountLsInteractions() {
        Assert.assertNotNull("Data on demand for 'LsInteraction' failed to initialize correctly", dod.getRandomLsInteraction());
        long count = LsInteraction.countLsInteractions();
        Assert.assertTrue("Counter for 'LsInteraction' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindLsInteraction() {
        LsInteraction obj = dod.getRandomLsInteraction();
        Assert.assertNotNull("Data on demand for 'LsInteraction' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LsInteraction' failed to provide an identifier", id);
        obj = LsInteraction.findLsInteraction(id);
        Assert.assertNotNull("Find method for 'LsInteraction' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'LsInteraction' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllLsInteractions() {
        Assert.assertNotNull("Data on demand for 'LsInteraction' failed to initialize correctly", dod.getRandomLsInteraction());
        long count = LsInteraction.countLsInteractions();
        Assert.assertTrue("Too expensive to perform a find all test for 'LsInteraction', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<LsInteraction> result = LsInteraction.findAllLsInteractions();
        Assert.assertNotNull("Find all method for 'LsInteraction' illegally returned null", result);
        Assert.assertTrue("Find all method for 'LsInteraction' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindLsInteractionEntries() {
        Assert.assertNotNull("Data on demand for 'LsInteraction' failed to initialize correctly", dod.getRandomLsInteraction());
        long count = LsInteraction.countLsInteractions();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<LsInteraction> result = LsInteraction.findLsInteractionEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'LsInteraction' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'LsInteraction' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        LsInteraction obj = dod.getRandomLsInteraction();
        Assert.assertNotNull("Data on demand for 'LsInteraction' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LsInteraction' failed to provide an identifier", id);
        obj = LsInteraction.findLsInteraction(id);
        Assert.assertNotNull("Find method for 'LsInteraction' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyLsInteraction(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'LsInteraction' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        LsInteraction obj = dod.getRandomLsInteraction();
        Assert.assertNotNull("Data on demand for 'LsInteraction' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LsInteraction' failed to provide an identifier", id);
        obj = LsInteraction.findLsInteraction(id);
        boolean modified =  dod.modifyLsInteraction(obj);
        Integer currentVersion = obj.getVersion();
        LsInteraction merged = (LsInteraction)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'LsInteraction' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'LsInteraction' failed to initialize correctly", dod.getRandomLsInteraction());
        LsInteraction obj = dod.getNewTransientLsInteraction(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'LsInteraction' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'LsInteraction' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'LsInteraction' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        LsInteraction obj = dod.getRandomLsInteraction();
        Assert.assertNotNull("Data on demand for 'LsInteraction' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LsInteraction' failed to provide an identifier", id);
        obj = LsInteraction.findLsInteraction(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'LsInteraction' with identifier '" + id + "'", LsInteraction.findLsInteraction(id));
    }
}
