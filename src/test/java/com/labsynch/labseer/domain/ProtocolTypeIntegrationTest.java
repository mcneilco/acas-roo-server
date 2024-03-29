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
public class ProtocolTypeIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    ProtocolTypeDataOnDemand dod;

    @Test
    public void testCountProtocolTypes() {
        Assert.assertNotNull("Data on demand for 'ProtocolType' failed to initialize correctly",
                dod.getRandomProtocolType());
        long count = ProtocolType.countProtocolTypes();
        Assert.assertTrue("Counter for 'ProtocolType' incorrectly reported there were no entries", count > 0);
    }

    @Test
    public void testFindProtocolType() {
        ProtocolType obj = dod.getRandomProtocolType();
        Assert.assertNotNull("Data on demand for 'ProtocolType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProtocolType' failed to provide an identifier", id);
        obj = ProtocolType.findProtocolType(id);
        Assert.assertNotNull("Find method for 'ProtocolType' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ProtocolType' returned the incorrect identifier", id, obj.getId());
    }

    @Test
    public void testFindAllProtocolTypes() {
        Assert.assertNotNull("Data on demand for 'ProtocolType' failed to initialize correctly",
                dod.getRandomProtocolType());
        long count = ProtocolType.countProtocolTypes();
        Assert.assertTrue("Too expensive to perform a find all test for 'ProtocolType', as there are " + count
                + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
                count < 250);
        List<ProtocolType> result = ProtocolType.findAllProtocolTypes();
        Assert.assertNotNull("Find all method for 'ProtocolType' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ProtocolType' failed to return any data", result.size() > 0);
    }

    @Test
    public void testFindProtocolTypeEntries() {
        Assert.assertNotNull("Data on demand for 'ProtocolType' failed to initialize correctly",
                dod.getRandomProtocolType());
        long count = ProtocolType.countProtocolTypes();
        if (count > 20)
            count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ProtocolType> result = ProtocolType.findProtocolTypeEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ProtocolType' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ProtocolType' returned an incorrect number of entries", count,
                result.size());
    }

    @Test
    public void testFlush() {
        ProtocolType obj = dod.getRandomProtocolType();
        Assert.assertNotNull("Data on demand for 'ProtocolType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProtocolType' failed to provide an identifier", id);
        obj = ProtocolType.findProtocolType(id);
        Assert.assertNotNull("Find method for 'ProtocolType' illegally returned null for id '" + id + "'", obj);
        boolean modified = dod.modifyProtocolType(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ProtocolType' failed to increment on flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testMergeUpdate() {
        ProtocolType obj = dod.getRandomProtocolType();
        Assert.assertNotNull("Data on demand for 'ProtocolType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProtocolType' failed to provide an identifier", id);
        obj = ProtocolType.findProtocolType(id);
        boolean modified = dod.modifyProtocolType(obj);
        Integer currentVersion = obj.getVersion();
        ProtocolType merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(),
                id);
        Assert.assertTrue("Version for 'ProtocolType' failed to increment on merge and flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'ProtocolType' failed to initialize correctly",
                dod.getRandomProtocolType());
        ProtocolType obj = dod.getNewTransientProtocolType(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ProtocolType' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ProtocolType' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'ProtocolType' identifier to no longer be null", obj.getId());
    }

    @Test
    public void testRemove() {
        ProtocolType obj = dod.getRandomProtocolType();
        Assert.assertNotNull("Data on demand for 'ProtocolType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProtocolType' failed to provide an identifier", id);
        obj = ProtocolType.findProtocolType(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ProtocolType' with identifier '" + id + "'",
                ProtocolType.findProtocolType(id));
    }
}
