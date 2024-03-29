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
public class ThingPageArchiveIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    ThingPageArchiveDataOnDemand dod;

    @Test
    public void testCountThingPageArchives() {
        Assert.assertNotNull("Data on demand for 'ThingPageArchive' failed to initialize correctly",
                dod.getRandomThingPageArchive());
        long count = ThingPageArchive.countThingPageArchives();
        Assert.assertTrue("Counter for 'ThingPageArchive' incorrectly reported there were no entries", count > 0);
    }

    @Test
    public void testFindThingPageArchive() {
        ThingPageArchive obj = dod.getRandomThingPageArchive();
        Assert.assertNotNull("Data on demand for 'ThingPageArchive' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ThingPageArchive' failed to provide an identifier", id);
        obj = ThingPageArchive.findThingPageArchive(id);
        Assert.assertNotNull("Find method for 'ThingPageArchive' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ThingPageArchive' returned the incorrect identifier", id, obj.getId());
    }

    @Test
    public void testFindAllThingPageArchives() {
        Assert.assertNotNull("Data on demand for 'ThingPageArchive' failed to initialize correctly",
                dod.getRandomThingPageArchive());
        long count = ThingPageArchive.countThingPageArchives();
        Assert.assertTrue("Too expensive to perform a find all test for 'ThingPageArchive', as there are " + count
                + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
                count < 250);
        List<ThingPageArchive> result = ThingPageArchive.findAllThingPageArchives();
        Assert.assertNotNull("Find all method for 'ThingPageArchive' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ThingPageArchive' failed to return any data", result.size() > 0);
    }

    @Test
    public void testFindThingPageArchiveEntries() {
        Assert.assertNotNull("Data on demand for 'ThingPageArchive' failed to initialize correctly",
                dod.getRandomThingPageArchive());
        long count = ThingPageArchive.countThingPageArchives();
        if (count > 20)
            count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ThingPageArchive> result = ThingPageArchive.findThingPageArchiveEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ThingPageArchive' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ThingPageArchive' returned an incorrect number of entries", count,
                result.size());
    }

    @Test
    public void testFlush() {
        ThingPageArchive obj = dod.getRandomThingPageArchive();
        Assert.assertNotNull("Data on demand for 'ThingPageArchive' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ThingPageArchive' failed to provide an identifier", id);
        obj = ThingPageArchive.findThingPageArchive(id);
        Assert.assertNotNull("Find method for 'ThingPageArchive' illegally returned null for id '" + id + "'", obj);
        boolean modified = dod.modifyThingPageArchive(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ThingPageArchive' failed to increment on flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testMergeUpdate() {
        ThingPageArchive obj = dod.getRandomThingPageArchive();
        Assert.assertNotNull("Data on demand for 'ThingPageArchive' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ThingPageArchive' failed to provide an identifier", id);
        obj = ThingPageArchive.findThingPageArchive(id);
        boolean modified = dod.modifyThingPageArchive(obj);
        Integer currentVersion = obj.getVersion();
        ThingPageArchive merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(),
                id);
        Assert.assertTrue("Version for 'ThingPageArchive' failed to increment on merge and flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'ThingPageArchive' failed to initialize correctly",
                dod.getRandomThingPageArchive());
        ThingPageArchive obj = dod.getNewTransientThingPageArchive(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ThingPageArchive' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ThingPageArchive' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'ThingPageArchive' identifier to no longer be null", obj.getId());
    }

    @Test
    public void testRemove() {
        ThingPageArchive obj = dod.getRandomThingPageArchive();
        Assert.assertNotNull("Data on demand for 'ThingPageArchive' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ThingPageArchive' failed to provide an identifier", id);
        obj = ThingPageArchive.findThingPageArchive(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ThingPageArchive' with identifier '" + id + "'",
                ThingPageArchive.findThingPageArchive(id));
    }
}
