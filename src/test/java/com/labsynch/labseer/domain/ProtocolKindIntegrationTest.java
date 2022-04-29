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

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Transactional
public class ProtocolKindIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    ProtocolKindDataOnDemand dod;

    @Test
    public void testCountProtocolKinds() {
        Assert.assertNotNull("Data on demand for 'ProtocolKind' failed to initialize correctly",
                dod.getRandomProtocolKind());
        long count = ProtocolKind.countProtocolKinds();
        Assert.assertTrue("Counter for 'ProtocolKind' incorrectly reported there were no entries", count > 0);
    }

    @Test
    public void testFindProtocolKind() {
        ProtocolKind obj = dod.getRandomProtocolKind();
        Assert.assertNotNull("Data on demand for 'ProtocolKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProtocolKind' failed to provide an identifier", id);
        obj = ProtocolKind.findProtocolKind(id);
        Assert.assertNotNull("Find method for 'ProtocolKind' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ProtocolKind' returned the incorrect identifier", id, obj.getId());
    }

    @Test
    public void testFindAllProtocolKinds() {
        Assert.assertNotNull("Data on demand for 'ProtocolKind' failed to initialize correctly",
                dod.getRandomProtocolKind());
        long count = ProtocolKind.countProtocolKinds();
        Assert.assertTrue("Too expensive to perform a find all test for 'ProtocolKind', as there are " + count
                + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
                count < 250);
        List<ProtocolKind> result = ProtocolKind.findAllProtocolKinds();
        Assert.assertNotNull("Find all method for 'ProtocolKind' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ProtocolKind' failed to return any data", result.size() > 0);
    }

    @Test
    public void testFindProtocolKindEntries() {
        Assert.assertNotNull("Data on demand for 'ProtocolKind' failed to initialize correctly",
                dod.getRandomProtocolKind());
        long count = ProtocolKind.countProtocolKinds();
        if (count > 20)
            count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ProtocolKind> result = ProtocolKind.findProtocolKindEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ProtocolKind' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ProtocolKind' returned an incorrect number of entries", count,
                result.size());
    }

    @Test
    public void testFlush() {
        ProtocolKind obj = dod.getRandomProtocolKind();
        Assert.assertNotNull("Data on demand for 'ProtocolKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProtocolKind' failed to provide an identifier", id);
        obj = ProtocolKind.findProtocolKind(id);
        Assert.assertNotNull("Find method for 'ProtocolKind' illegally returned null for id '" + id + "'", obj);
        boolean modified = dod.modifyProtocolKind(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ProtocolKind' failed to increment on flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testMergeUpdate() {
        ProtocolKind obj = dod.getRandomProtocolKind();
        Assert.assertNotNull("Data on demand for 'ProtocolKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProtocolKind' failed to provide an identifier", id);
        obj = ProtocolKind.findProtocolKind(id);
        boolean modified = dod.modifyProtocolKind(obj);
        Integer currentVersion = obj.getVersion();
        ProtocolKind merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(),
                id);
        Assert.assertTrue("Version for 'ProtocolKind' failed to increment on merge and flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'ProtocolKind' failed to initialize correctly",
                dod.getRandomProtocolKind());
        ProtocolKind obj = dod.getNewTransientProtocolKind(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ProtocolKind' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ProtocolKind' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'ProtocolKind' identifier to no longer be null", obj.getId());
    }

    @Test
    public void testRemove() {
        ProtocolKind obj = dod.getRandomProtocolKind();
        Assert.assertNotNull("Data on demand for 'ProtocolKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProtocolKind' failed to provide an identifier", id);
        obj = ProtocolKind.findProtocolKind(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ProtocolKind' with identifier '" + id + "'",
                ProtocolKind.findProtocolKind(id));
    }
}
