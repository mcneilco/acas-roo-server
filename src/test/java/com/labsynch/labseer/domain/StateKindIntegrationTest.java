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

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Transactional
@RooIntegrationTest(entity = StateKind.class)
public class StateKindIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    StateKindDataOnDemand dod;

	@Test
    public void testCountStateKinds() {
        Assert.assertNotNull("Data on demand for 'StateKind' failed to initialize correctly", dod.getRandomStateKind());
        long count = StateKind.countStateKinds();
        Assert.assertTrue("Counter for 'StateKind' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindStateKind() {
        StateKind obj = dod.getRandomStateKind();
        Assert.assertNotNull("Data on demand for 'StateKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'StateKind' failed to provide an identifier", id);
        obj = StateKind.findStateKind(id);
        Assert.assertNotNull("Find method for 'StateKind' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'StateKind' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllStateKinds() {
        Assert.assertNotNull("Data on demand for 'StateKind' failed to initialize correctly", dod.getRandomStateKind());
        long count = StateKind.countStateKinds();
        Assert.assertTrue("Too expensive to perform a find all test for 'StateKind', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<StateKind> result = StateKind.findAllStateKinds();
        Assert.assertNotNull("Find all method for 'StateKind' illegally returned null", result);
        Assert.assertTrue("Find all method for 'StateKind' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindStateKindEntries() {
        Assert.assertNotNull("Data on demand for 'StateKind' failed to initialize correctly", dod.getRandomStateKind());
        long count = StateKind.countStateKinds();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<StateKind> result = StateKind.findStateKindEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'StateKind' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'StateKind' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        StateKind obj = dod.getRandomStateKind();
        Assert.assertNotNull("Data on demand for 'StateKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'StateKind' failed to provide an identifier", id);
        obj = StateKind.findStateKind(id);
        Assert.assertNotNull("Find method for 'StateKind' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyStateKind(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'StateKind' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        StateKind obj = dod.getRandomStateKind();
        Assert.assertNotNull("Data on demand for 'StateKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'StateKind' failed to provide an identifier", id);
        obj = StateKind.findStateKind(id);
        boolean modified =  dod.modifyStateKind(obj);
        Integer currentVersion = obj.getVersion();
        StateKind merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'StateKind' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'StateKind' failed to initialize correctly", dod.getRandomStateKind());
        StateKind obj = dod.getNewTransientStateKind(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'StateKind' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'StateKind' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'StateKind' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        StateKind obj = dod.getRandomStateKind();
        Assert.assertNotNull("Data on demand for 'StateKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'StateKind' failed to provide an identifier", id);
        obj = StateKind.findStateKind(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'StateKind' with identifier '" + id + "'", StateKind.findStateKind(id));
    }
}
