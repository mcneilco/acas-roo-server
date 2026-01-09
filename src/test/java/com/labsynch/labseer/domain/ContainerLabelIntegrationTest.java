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
public class ContainerLabelIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    ContainerLabelDataOnDemand dod;

    @Test
    public void testCountContainerLabels() {
        Assert.assertNotNull("Data on demand for 'ContainerLabel' failed to initialize correctly",
                dod.getRandomContainerLabel());
        long count = ContainerLabel.countContainerLabels();
        Assert.assertTrue("Counter for 'ContainerLabel' incorrectly reported there were no entries", count > 0);
    }

    @Test
    public void testFindContainerLabel() {
        ContainerLabel obj = dod.getRandomContainerLabel();
        Assert.assertNotNull("Data on demand for 'ContainerLabel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ContainerLabel' failed to provide an identifier", id);
        obj = ContainerLabel.findContainerLabel(id);
        Assert.assertNotNull("Find method for 'ContainerLabel' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ContainerLabel' returned the incorrect identifier", id, obj.getId());
    }

    @Test
    public void testFindAllContainerLabels() {
        Assert.assertNotNull("Data on demand for 'ContainerLabel' failed to initialize correctly",
                dod.getRandomContainerLabel());
        long count = ContainerLabel.countContainerLabels();
        Assert.assertTrue("Too expensive to perform a find all test for 'ContainerLabel', as there are " + count
                + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
                count < 250);
        List<ContainerLabel> result = ContainerLabel.findAllContainerLabels();
        Assert.assertNotNull("Find all method for 'ContainerLabel' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ContainerLabel' failed to return any data", result.size() > 0);
    }

    @Test
    public void testFindContainerLabelEntries() {
        Assert.assertNotNull("Data on demand for 'ContainerLabel' failed to initialize correctly",
                dod.getRandomContainerLabel());
        long count = ContainerLabel.countContainerLabels();
        if (count > 20)
            count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ContainerLabel> result = ContainerLabel.findContainerLabelEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ContainerLabel' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ContainerLabel' returned an incorrect number of entries", count,
                result.size());
    }

    @Test
    public void testFlush() {
        ContainerLabel obj = dod.getRandomContainerLabel();
        Assert.assertNotNull("Data on demand for 'ContainerLabel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ContainerLabel' failed to provide an identifier", id);
        obj = ContainerLabel.findContainerLabel(id);
        Assert.assertNotNull("Find method for 'ContainerLabel' illegally returned null for id '" + id + "'", obj);
        boolean modified = dod.modifyContainerLabel(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ContainerLabel' failed to increment on flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testMergeUpdate() {
        ContainerLabel obj = dod.getRandomContainerLabel();
        Assert.assertNotNull("Data on demand for 'ContainerLabel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ContainerLabel' failed to provide an identifier", id);
        obj = ContainerLabel.findContainerLabel(id);
        boolean modified = dod.modifyContainerLabel(obj);
        Integer currentVersion = obj.getVersion();
        ContainerLabel merged = (ContainerLabel) obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(),
                id);
        Assert.assertTrue("Version for 'ContainerLabel' failed to increment on merge and flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'ContainerLabel' failed to initialize correctly",
                dod.getRandomContainerLabel());
        ContainerLabel obj = dod.getNewTransientContainerLabel(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ContainerLabel' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ContainerLabel' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'ContainerLabel' identifier to no longer be null", obj.getId());
    }

    @Test
    public void testRemove() {
        ContainerLabel obj = dod.getRandomContainerLabel();
        Assert.assertNotNull("Data on demand for 'ContainerLabel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ContainerLabel' failed to provide an identifier", id);
        obj = ContainerLabel.findContainerLabel(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ContainerLabel' with identifier '" + id + "'",
                ContainerLabel.findContainerLabel(id));
    }
}
