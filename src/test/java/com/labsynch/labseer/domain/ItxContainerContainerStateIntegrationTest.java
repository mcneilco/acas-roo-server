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
public class ItxContainerContainerStateIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    ItxContainerContainerStateDataOnDemand dod;

    @Test
    public void testCountItxContainerContainerStates() {
        Assert.assertNotNull("Data on demand for 'ItxContainerContainerState' failed to initialize correctly",
                dod.getRandomItxContainerContainerState());
        long count = ItxContainerContainerState.countItxContainerContainerStates();
        Assert.assertTrue("Counter for 'ItxContainerContainerState' incorrectly reported there were no entries",
                count > 0);
    }

    @Test
    public void testFindItxContainerContainerState() {
        ItxContainerContainerState obj = dod.getRandomItxContainerContainerState();
        Assert.assertNotNull("Data on demand for 'ItxContainerContainerState' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItxContainerContainerState' failed to provide an identifier", id);
        obj = ItxContainerContainerState.findItxContainerContainerState(id);
        Assert.assertNotNull("Find method for 'ItxContainerContainerState' illegally returned null for id '" + id + "'",
                obj);
        Assert.assertEquals("Find method for 'ItxContainerContainerState' returned the incorrect identifier", id,
                obj.getId());
    }

    @Test
    public void testFindAllItxContainerContainerStates() {
        Assert.assertNotNull("Data on demand for 'ItxContainerContainerState' failed to initialize correctly",
                dod.getRandomItxContainerContainerState());
        long count = ItxContainerContainerState.countItxContainerContainerStates();
        Assert.assertTrue("Too expensive to perform a find all test for 'ItxContainerContainerState', as there are "
                + count
                + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
                count < 250);
        List<ItxContainerContainerState> result = ItxContainerContainerState.findAllItxContainerContainerStates();
        Assert.assertNotNull("Find all method for 'ItxContainerContainerState' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ItxContainerContainerState' failed to return any data",
                result.size() > 0);
    }

    @Test
    public void testFindItxContainerContainerStateEntries() {
        Assert.assertNotNull("Data on demand for 'ItxContainerContainerState' failed to initialize correctly",
                dod.getRandomItxContainerContainerState());
        long count = ItxContainerContainerState.countItxContainerContainerStates();
        if (count > 20)
            count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ItxContainerContainerState> result = ItxContainerContainerState
                .findItxContainerContainerStateEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ItxContainerContainerState' illegally returned null", result);
        Assert.assertEquals(
                "Find entries method for 'ItxContainerContainerState' returned an incorrect number of entries", count,
                result.size());
    }

    @Test
    public void testFlush() {
        ItxContainerContainerState obj = dod.getRandomItxContainerContainerState();
        Assert.assertNotNull("Data on demand for 'ItxContainerContainerState' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItxContainerContainerState' failed to provide an identifier", id);
        obj = ItxContainerContainerState.findItxContainerContainerState(id);
        Assert.assertNotNull("Find method for 'ItxContainerContainerState' illegally returned null for id '" + id + "'",
                obj);
        boolean modified = dod.modifyItxContainerContainerState(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ItxContainerContainerState' failed to increment on flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testMergeUpdate() {
        ItxContainerContainerState obj = dod.getRandomItxContainerContainerState();
        Assert.assertNotNull("Data on demand for 'ItxContainerContainerState' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItxContainerContainerState' failed to provide an identifier", id);
        obj = ItxContainerContainerState.findItxContainerContainerState(id);
        boolean modified = dod.modifyItxContainerContainerState(obj);
        Integer currentVersion = obj.getVersion();
        ItxContainerContainerState merged = (ItxContainerContainerState) obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(),
                id);
        Assert.assertTrue("Version for 'ItxContainerContainerState' failed to increment on merge and flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'ItxContainerContainerState' failed to initialize correctly",
                dod.getRandomItxContainerContainerState());
        ItxContainerContainerState obj = dod.getNewTransientItxContainerContainerState(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ItxContainerContainerState' failed to provide a new transient entity",
                obj);
        Assert.assertNull("Expected 'ItxContainerContainerState' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'ItxContainerContainerState' identifier to no longer be null", obj.getId());
    }

    @Test
    public void testRemove() {
        ItxContainerContainerState obj = dod.getRandomItxContainerContainerState();
        Assert.assertNotNull("Data on demand for 'ItxContainerContainerState' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItxContainerContainerState' failed to provide an identifier", id);
        obj = ItxContainerContainerState.findItxContainerContainerState(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ItxContainerContainerState' with identifier '" + id + "'",
                ItxContainerContainerState.findItxContainerContainerState(id));
    }
}
