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
public class ProtocolStateIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    ProtocolStateDataOnDemand dod;

    @Test
    public void testCountProtocolStates() {
        Assert.assertNotNull("Data on demand for 'ProtocolState' failed to initialize correctly",
                dod.getRandomProtocolState());
        long count = ProtocolState.countProtocolStates();
        Assert.assertTrue("Counter for 'ProtocolState' incorrectly reported there were no entries", count > 0);
    }

    @Test
    public void testFindProtocolState() {
        ProtocolState obj = dod.getRandomProtocolState();
        Assert.assertNotNull("Data on demand for 'ProtocolState' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProtocolState' failed to provide an identifier", id);
        obj = ProtocolState.findProtocolState(id);
        Assert.assertNotNull("Find method for 'ProtocolState' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ProtocolState' returned the incorrect identifier", id, obj.getId());
    }

    @Test
    public void testFindAllProtocolStates() {
        Assert.assertNotNull("Data on demand for 'ProtocolState' failed to initialize correctly",
                dod.getRandomProtocolState());
        long count = ProtocolState.countProtocolStates();
        Assert.assertTrue("Too expensive to perform a find all test for 'ProtocolState', as there are " + count
                + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
                count < 250);
        List<ProtocolState> result = ProtocolState.findAllProtocolStates();
        Assert.assertNotNull("Find all method for 'ProtocolState' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ProtocolState' failed to return any data", result.size() > 0);
    }

    @Test
    public void testFindProtocolStateEntries() {
        Assert.assertNotNull("Data on demand for 'ProtocolState' failed to initialize correctly",
                dod.getRandomProtocolState());
        long count = ProtocolState.countProtocolStates();
        if (count > 20)
            count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ProtocolState> result = ProtocolState.findProtocolStateEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ProtocolState' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ProtocolState' returned an incorrect number of entries", count,
                result.size());
    }

    @Test
    public void testFlush() {
        ProtocolState obj = dod.getRandomProtocolState();
        Assert.assertNotNull("Data on demand for 'ProtocolState' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProtocolState' failed to provide an identifier", id);
        obj = ProtocolState.findProtocolState(id);
        Assert.assertNotNull("Find method for 'ProtocolState' illegally returned null for id '" + id + "'", obj);
        boolean modified = dod.modifyProtocolState(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ProtocolState' failed to increment on flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testMergeUpdate() {
        ProtocolState obj = dod.getRandomProtocolState();
        Assert.assertNotNull("Data on demand for 'ProtocolState' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProtocolState' failed to provide an identifier", id);
        obj = ProtocolState.findProtocolState(id);
        boolean modified = dod.modifyProtocolState(obj);
        Integer currentVersion = obj.getVersion();
        ProtocolState merged = (ProtocolState) obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(),
                id);
        Assert.assertTrue("Version for 'ProtocolState' failed to increment on merge and flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'ProtocolState' failed to initialize correctly",
                dod.getRandomProtocolState());
        ProtocolState obj = dod.getNewTransientProtocolState(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ProtocolState' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ProtocolState' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'ProtocolState' identifier to no longer be null", obj.getId());
    }

    @Test
    public void testRemove() {
        ProtocolState obj = dod.getRandomProtocolState();
        Assert.assertNotNull("Data on demand for 'ProtocolState' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProtocolState' failed to provide an identifier", id);
        obj = ProtocolState.findProtocolState(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ProtocolState' with identifier '" + id + "'",
                ProtocolState.findProtocolState(id));
    }
}
