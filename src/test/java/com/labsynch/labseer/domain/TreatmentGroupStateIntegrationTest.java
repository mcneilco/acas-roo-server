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
public class TreatmentGroupStateIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    TreatmentGroupStateDataOnDemand dod;

	@Test
    public void testCountTreatmentGroupStates() {
        Assert.assertNotNull("Data on demand for 'TreatmentGroupState' failed to initialize correctly", dod.getRandomTreatmentGroupState());
        long count = TreatmentGroupState.countTreatmentGroupStates();
        Assert.assertTrue("Counter for 'TreatmentGroupState' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindTreatmentGroupState() {
        TreatmentGroupState obj = dod.getRandomTreatmentGroupState();
        Assert.assertNotNull("Data on demand for 'TreatmentGroupState' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'TreatmentGroupState' failed to provide an identifier", id);
        obj = TreatmentGroupState.findTreatmentGroupState(id);
        Assert.assertNotNull("Find method for 'TreatmentGroupState' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'TreatmentGroupState' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllTreatmentGroupStates() {
        Assert.assertNotNull("Data on demand for 'TreatmentGroupState' failed to initialize correctly", dod.getRandomTreatmentGroupState());
        long count = TreatmentGroupState.countTreatmentGroupStates();
        Assert.assertTrue("Too expensive to perform a find all test for 'TreatmentGroupState', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<TreatmentGroupState> result = TreatmentGroupState.findAllTreatmentGroupStates();
        Assert.assertNotNull("Find all method for 'TreatmentGroupState' illegally returned null", result);
        Assert.assertTrue("Find all method for 'TreatmentGroupState' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindTreatmentGroupStateEntries() {
        Assert.assertNotNull("Data on demand for 'TreatmentGroupState' failed to initialize correctly", dod.getRandomTreatmentGroupState());
        long count = TreatmentGroupState.countTreatmentGroupStates();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<TreatmentGroupState> result = TreatmentGroupState.findTreatmentGroupStateEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'TreatmentGroupState' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'TreatmentGroupState' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        TreatmentGroupState obj = dod.getRandomTreatmentGroupState();
        Assert.assertNotNull("Data on demand for 'TreatmentGroupState' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'TreatmentGroupState' failed to provide an identifier", id);
        obj = TreatmentGroupState.findTreatmentGroupState(id);
        Assert.assertNotNull("Find method for 'TreatmentGroupState' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyTreatmentGroupState(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'TreatmentGroupState' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        TreatmentGroupState obj = dod.getRandomTreatmentGroupState();
        Assert.assertNotNull("Data on demand for 'TreatmentGroupState' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'TreatmentGroupState' failed to provide an identifier", id);
        obj = TreatmentGroupState.findTreatmentGroupState(id);
        boolean modified =  dod.modifyTreatmentGroupState(obj);
        Integer currentVersion = obj.getVersion();
        TreatmentGroupState merged = (TreatmentGroupState)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'TreatmentGroupState' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'TreatmentGroupState' failed to initialize correctly", dod.getRandomTreatmentGroupState());
        TreatmentGroupState obj = dod.getNewTransientTreatmentGroupState(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'TreatmentGroupState' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'TreatmentGroupState' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'TreatmentGroupState' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        TreatmentGroupState obj = dod.getRandomTreatmentGroupState();
        Assert.assertNotNull("Data on demand for 'TreatmentGroupState' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'TreatmentGroupState' failed to provide an identifier", id);
        obj = TreatmentGroupState.findTreatmentGroupState(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'TreatmentGroupState' with identifier '" + id + "'", TreatmentGroupState.findTreatmentGroupState(id));
    }
}
