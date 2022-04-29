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
public class ValueKindIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    ValueKindDataOnDemand dod;

    @Test
    public void testCountValueKinds() {
        Assert.assertNotNull("Data on demand for 'ValueKind' failed to initialize correctly", dod.getRandomValueKind());
        long count = ValueKind.countValueKinds();
        Assert.assertTrue("Counter for 'ValueKind' incorrectly reported there were no entries", count > 0);
    }

    @Test
    public void testFindValueKind() {
        ValueKind obj = dod.getRandomValueKind();
        Assert.assertNotNull("Data on demand for 'ValueKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ValueKind' failed to provide an identifier", id);
        obj = ValueKind.findValueKind(id);
        Assert.assertNotNull("Find method for 'ValueKind' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ValueKind' returned the incorrect identifier", id, obj.getId());
    }

    @Test
    public void testFindAllValueKinds() {
        Assert.assertNotNull("Data on demand for 'ValueKind' failed to initialize correctly", dod.getRandomValueKind());
        long count = ValueKind.countValueKinds();
        Assert.assertTrue("Too expensive to perform a find all test for 'ValueKind', as there are " + count
                + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
                count < 250);
        List<ValueKind> result = ValueKind.findAllValueKinds();
        Assert.assertNotNull("Find all method for 'ValueKind' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ValueKind' failed to return any data", result.size() > 0);
    }

    @Test
    public void testFindValueKindEntries() {
        Assert.assertNotNull("Data on demand for 'ValueKind' failed to initialize correctly", dod.getRandomValueKind());
        long count = ValueKind.countValueKinds();
        if (count > 20)
            count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ValueKind> result = ValueKind.findValueKindEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ValueKind' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ValueKind' returned an incorrect number of entries", count,
                result.size());
    }

    @Test
    public void testFlush() {
        ValueKind obj = dod.getRandomValueKind();
        Assert.assertNotNull("Data on demand for 'ValueKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ValueKind' failed to provide an identifier", id);
        obj = ValueKind.findValueKind(id);
        Assert.assertNotNull("Find method for 'ValueKind' illegally returned null for id '" + id + "'", obj);
        boolean modified = dod.modifyValueKind(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ValueKind' failed to increment on flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testMergeUpdate() {
        ValueKind obj = dod.getRandomValueKind();
        Assert.assertNotNull("Data on demand for 'ValueKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ValueKind' failed to provide an identifier", id);
        obj = ValueKind.findValueKind(id);
        boolean modified = dod.modifyValueKind(obj);
        Integer currentVersion = obj.getVersion();
        ValueKind merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(),
                id);
        Assert.assertTrue("Version for 'ValueKind' failed to increment on merge and flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'ValueKind' failed to initialize correctly", dod.getRandomValueKind());
        ValueKind obj = dod.getNewTransientValueKind(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ValueKind' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ValueKind' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'ValueKind' identifier to no longer be null", obj.getId());
    }

    @Test
    public void testRemove() {
        ValueKind obj = dod.getRandomValueKind();
        Assert.assertNotNull("Data on demand for 'ValueKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ValueKind' failed to provide an identifier", id);
        obj = ValueKind.findValueKind(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ValueKind' with identifier '" + id + "'", ValueKind.findValueKind(id));
    }
}
