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
public class ItxContainerContainerIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    ItxContainerContainerDataOnDemand dod;

	@Test
    public void testCountItxContainerContainers() {
        Assert.assertNotNull("Data on demand for 'ItxContainerContainer' failed to initialize correctly", dod.getRandomItxContainerContainer());
        long count = ItxContainerContainer.countItxContainerContainers();
        Assert.assertTrue("Counter for 'ItxContainerContainer' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindItxContainerContainer() {
        ItxContainerContainer obj = dod.getRandomItxContainerContainer();
        Assert.assertNotNull("Data on demand for 'ItxContainerContainer' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItxContainerContainer' failed to provide an identifier", id);
        obj = ItxContainerContainer.findItxContainerContainer(id);
        Assert.assertNotNull("Find method for 'ItxContainerContainer' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ItxContainerContainer' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllItxContainerContainers() {
        Assert.assertNotNull("Data on demand for 'ItxContainerContainer' failed to initialize correctly", dod.getRandomItxContainerContainer());
        long count = ItxContainerContainer.countItxContainerContainers();
        Assert.assertTrue("Too expensive to perform a find all test for 'ItxContainerContainer', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<ItxContainerContainer> result = ItxContainerContainer.findAllItxContainerContainers();
        Assert.assertNotNull("Find all method for 'ItxContainerContainer' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ItxContainerContainer' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindItxContainerContainerEntries() {
        Assert.assertNotNull("Data on demand for 'ItxContainerContainer' failed to initialize correctly", dod.getRandomItxContainerContainer());
        long count = ItxContainerContainer.countItxContainerContainers();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ItxContainerContainer> result = ItxContainerContainer.findItxContainerContainerEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ItxContainerContainer' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ItxContainerContainer' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        ItxContainerContainer obj = dod.getRandomItxContainerContainer();
        Assert.assertNotNull("Data on demand for 'ItxContainerContainer' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItxContainerContainer' failed to provide an identifier", id);
        obj = ItxContainerContainer.findItxContainerContainer(id);
        Assert.assertNotNull("Find method for 'ItxContainerContainer' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyItxContainerContainer(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ItxContainerContainer' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        ItxContainerContainer obj = dod.getRandomItxContainerContainer();
        Assert.assertNotNull("Data on demand for 'ItxContainerContainer' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItxContainerContainer' failed to provide an identifier", id);
        obj = ItxContainerContainer.findItxContainerContainer(id);
        boolean modified =  dod.modifyItxContainerContainer(obj);
        Integer currentVersion = obj.getVersion();
        ItxContainerContainer merged = (ItxContainerContainer)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'ItxContainerContainer' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'ItxContainerContainer' failed to initialize correctly", dod.getRandomItxContainerContainer());
        ItxContainerContainer obj = dod.getNewTransientItxContainerContainer(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ItxContainerContainer' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ItxContainerContainer' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'ItxContainerContainer' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        ItxContainerContainer obj = dod.getRandomItxContainerContainer();
        Assert.assertNotNull("Data on demand for 'ItxContainerContainer' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItxContainerContainer' failed to provide an identifier", id);
        obj = ItxContainerContainer.findItxContainerContainer(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ItxContainerContainer' with identifier '" + id + "'", ItxContainerContainer.findItxContainerContainer(id));
    }
}
