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
public class ContainerKindIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    ContainerKindDataOnDemand dod;

    @Test
    public void testCountContainerKinds() {
        Assert.assertNotNull("Data on demand for 'ContainerKind' failed to initialize correctly",
                dod.getRandomContainerKind());
        long count = ContainerKind.countContainerKinds();
        Assert.assertTrue("Counter for 'ContainerKind' incorrectly reported there were no entries", count > 0);
    }

    @Test
    public void testFindContainerKind() {
        ContainerKind obj = dod.getRandomContainerKind();
        Assert.assertNotNull("Data on demand for 'ContainerKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ContainerKind' failed to provide an identifier", id);
        obj = ContainerKind.findContainerKind(id);
        Assert.assertNotNull("Find method for 'ContainerKind' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ContainerKind' returned the incorrect identifier", id, obj.getId());
    }

    @Test
    public void testFindAllContainerKinds() {
        Assert.assertNotNull("Data on demand for 'ContainerKind' failed to initialize correctly",
                dod.getRandomContainerKind());
        long count = ContainerKind.countContainerKinds();
        Assert.assertTrue("Too expensive to perform a find all test for 'ContainerKind', as there are " + count
                + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
                count < 250);
        List<ContainerKind> result = ContainerKind.findAllContainerKinds();
        Assert.assertNotNull("Find all method for 'ContainerKind' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ContainerKind' failed to return any data", result.size() > 0);
    }

    @Test
    public void testFindContainerKindEntries() {
        Assert.assertNotNull("Data on demand for 'ContainerKind' failed to initialize correctly",
                dod.getRandomContainerKind());
        long count = ContainerKind.countContainerKinds();
        if (count > 20)
            count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ContainerKind> result = ContainerKind.findContainerKindEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ContainerKind' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ContainerKind' returned an incorrect number of entries", count,
                result.size());
    }

    @Test
    public void testFlush() {
        ContainerKind obj = dod.getRandomContainerKind();
        Assert.assertNotNull("Data on demand for 'ContainerKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ContainerKind' failed to provide an identifier", id);
        obj = ContainerKind.findContainerKind(id);
        Assert.assertNotNull("Find method for 'ContainerKind' illegally returned null for id '" + id + "'", obj);
        boolean modified = dod.modifyContainerKind(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ContainerKind' failed to increment on flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testMergeUpdate() {
        ContainerKind obj = dod.getRandomContainerKind();
        Assert.assertNotNull("Data on demand for 'ContainerKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ContainerKind' failed to provide an identifier", id);
        obj = ContainerKind.findContainerKind(id);
        boolean modified = dod.modifyContainerKind(obj);
        Integer currentVersion = obj.getVersion();
        ContainerKind merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(),
                id);
        Assert.assertTrue("Version for 'ContainerKind' failed to increment on merge and flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'ContainerKind' failed to initialize correctly",
                dod.getRandomContainerKind());
        ContainerKind obj = dod.getNewTransientContainerKind(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ContainerKind' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ContainerKind' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'ContainerKind' identifier to no longer be null", obj.getId());
    }

    @Test
    public void testRemove() {
        ContainerKind obj = dod.getRandomContainerKind();
        Assert.assertNotNull("Data on demand for 'ContainerKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ContainerKind' failed to provide an identifier", id);
        obj = ContainerKind.findContainerKind(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ContainerKind' with identifier '" + id + "'",
                ContainerKind.findContainerKind(id));
    }
}
