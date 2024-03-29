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
public class ContainerValueIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    ContainerValueDataOnDemand dod;

    @Test
    public void testCountContainerValues() {
        Assert.assertNotNull("Data on demand for 'ContainerValue' failed to initialize correctly",
                dod.getRandomContainerValue());
        long count = ContainerValue.countContainerValues();
        Assert.assertTrue("Counter for 'ContainerValue' incorrectly reported there were no entries", count > 0);
    }

    @Test
    public void testFindContainerValue() {
        ContainerValue obj = dod.getRandomContainerValue();
        Assert.assertNotNull("Data on demand for 'ContainerValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ContainerValue' failed to provide an identifier", id);
        obj = ContainerValue.findContainerValue(id);
        Assert.assertNotNull("Find method for 'ContainerValue' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ContainerValue' returned the incorrect identifier", id, obj.getId());
    }

    @Test
    public void testFindAllContainerValues() {
        Assert.assertNotNull("Data on demand for 'ContainerValue' failed to initialize correctly",
                dod.getRandomContainerValue());
        long count = ContainerValue.countContainerValues();
        Assert.assertTrue("Too expensive to perform a find all test for 'ContainerValue', as there are " + count
                + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
                count < 250);
        List<ContainerValue> result = ContainerValue.findAllContainerValues();
        Assert.assertNotNull("Find all method for 'ContainerValue' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ContainerValue' failed to return any data", result.size() > 0);
    }

    @Test
    public void testFindContainerValueEntries() {
        Assert.assertNotNull("Data on demand for 'ContainerValue' failed to initialize correctly",
                dod.getRandomContainerValue());
        long count = ContainerValue.countContainerValues();
        if (count > 20)
            count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ContainerValue> result = ContainerValue.findContainerValueEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ContainerValue' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ContainerValue' returned an incorrect number of entries", count,
                result.size());
    }

    @Test
    public void testFlush() {
        ContainerValue obj = dod.getRandomContainerValue();
        Assert.assertNotNull("Data on demand for 'ContainerValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ContainerValue' failed to provide an identifier", id);
        obj = ContainerValue.findContainerValue(id);
        Assert.assertNotNull("Find method for 'ContainerValue' illegally returned null for id '" + id + "'", obj);
        boolean modified = dod.modifyContainerValue(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ContainerValue' failed to increment on flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testMergeUpdate() {
        ContainerValue obj = dod.getRandomContainerValue();
        Assert.assertNotNull("Data on demand for 'ContainerValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ContainerValue' failed to provide an identifier", id);
        obj = ContainerValue.findContainerValue(id);
        boolean modified = dod.modifyContainerValue(obj);
        Integer currentVersion = obj.getVersion();
        ContainerValue merged = (ContainerValue) obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(),
                id);
        Assert.assertTrue("Version for 'ContainerValue' failed to increment on merge and flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'ContainerValue' failed to initialize correctly",
                dod.getRandomContainerValue());
        ContainerValue obj = dod.getNewTransientContainerValue(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ContainerValue' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ContainerValue' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'ContainerValue' identifier to no longer be null", obj.getId());
    }

    @Test
    public void testRemove() {
        ContainerValue obj = dod.getRandomContainerValue();
        Assert.assertNotNull("Data on demand for 'ContainerValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ContainerValue' failed to provide an identifier", id);
        obj = ContainerValue.findContainerValue(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ContainerValue' with identifier '" + id + "'",
                ContainerValue.findContainerValue(id));
    }
}
