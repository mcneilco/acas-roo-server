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
public class ThingPageIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    ThingPageDataOnDemand dod;

	@Test
    public void testCountThingPages() {
        Assert.assertNotNull("Data on demand for 'ThingPage' failed to initialize correctly", dod.getRandomThingPage());
        long count = ThingPage.countThingPages();
        Assert.assertTrue("Counter for 'ThingPage' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindThingPage() {
        ThingPage obj = dod.getRandomThingPage();
        Assert.assertNotNull("Data on demand for 'ThingPage' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ThingPage' failed to provide an identifier", id);
        obj = ThingPage.findThingPage(id);
        Assert.assertNotNull("Find method for 'ThingPage' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ThingPage' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllThingPages() {
        Assert.assertNotNull("Data on demand for 'ThingPage' failed to initialize correctly", dod.getRandomThingPage());
        long count = ThingPage.countThingPages();
        Assert.assertTrue("Too expensive to perform a find all test for 'ThingPage', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<ThingPage> result = ThingPage.findAllThingPages();
        Assert.assertNotNull("Find all method for 'ThingPage' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ThingPage' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindThingPageEntries() {
        Assert.assertNotNull("Data on demand for 'ThingPage' failed to initialize correctly", dod.getRandomThingPage());
        long count = ThingPage.countThingPages();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ThingPage> result = ThingPage.findThingPageEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ThingPage' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ThingPage' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        ThingPage obj = dod.getRandomThingPage();
        Assert.assertNotNull("Data on demand for 'ThingPage' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ThingPage' failed to provide an identifier", id);
        obj = ThingPage.findThingPage(id);
        Assert.assertNotNull("Find method for 'ThingPage' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyThingPage(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ThingPage' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        ThingPage obj = dod.getRandomThingPage();
        Assert.assertNotNull("Data on demand for 'ThingPage' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ThingPage' failed to provide an identifier", id);
        obj = ThingPage.findThingPage(id);
        boolean modified =  dod.modifyThingPage(obj);
        Integer currentVersion = obj.getVersion();
        ThingPage merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'ThingPage' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'ThingPage' failed to initialize correctly", dod.getRandomThingPage());
        ThingPage obj = dod.getNewTransientThingPage(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ThingPage' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ThingPage' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'ThingPage' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        ThingPage obj = dod.getRandomThingPage();
        Assert.assertNotNull("Data on demand for 'ThingPage' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ThingPage' failed to provide an identifier", id);
        obj = ThingPage.findThingPage(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ThingPage' with identifier '" + id + "'", ThingPage.findThingPage(id));
    }
}
