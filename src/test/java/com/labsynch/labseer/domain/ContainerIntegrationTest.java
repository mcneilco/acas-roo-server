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
public class ContainerIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    ContainerDataOnDemand dod;

    @Test
    public void testCountContainers() {
        Assert.assertNotNull("Data on demand for 'Container' failed to initialize correctly", dod.getRandomContainer());
        long count = Container.countContainers();
        Assert.assertTrue("Counter for 'Container' incorrectly reported there were no entries", count > 0);
    }

    @Test
    public void testFindContainer() {
        Container obj = dod.getRandomContainer();
        Assert.assertNotNull("Data on demand for 'Container' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Container' failed to provide an identifier", id);
        obj = Container.findContainer(id);
        Assert.assertNotNull("Find method for 'Container' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Container' returned the incorrect identifier", id, obj.getId());
    }

    @Test
    public void testFindAllContainers() {
        Assert.assertNotNull("Data on demand for 'Container' failed to initialize correctly", dod.getRandomContainer());
        long count = Container.countContainers();
        Assert.assertTrue("Too expensive to perform a find all test for 'Container', as there are " + count
                + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
                count < 250);
        List<Container> result = Container.findAllContainers();
        Assert.assertNotNull("Find all method for 'Container' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Container' failed to return any data", result.size() > 0);
    }

    @Test
    public void testFindContainerEntries() {
        Assert.assertNotNull("Data on demand for 'Container' failed to initialize correctly", dod.getRandomContainer());
        long count = Container.countContainers();
        if (count > 20)
            count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Container> result = Container.findContainerEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Container' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Container' returned an incorrect number of entries", count,
                result.size());
    }

    @Test
    public void testFlush() {
        Container obj = dod.getRandomContainer();
        Assert.assertNotNull("Data on demand for 'Container' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Container' failed to provide an identifier", id);
        obj = Container.findContainer(id);
        Assert.assertNotNull("Find method for 'Container' illegally returned null for id '" + id + "'", obj);
        boolean modified = dod.modifyContainer(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Container' failed to increment on flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testMergeUpdate() {
        Container obj = dod.getRandomContainer();
        Assert.assertNotNull("Data on demand for 'Container' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Container' failed to provide an identifier", id);
        obj = Container.findContainer(id);
        boolean modified = dod.modifyContainer(obj);
        Integer currentVersion = obj.getVersion();
        Container merged = (Container) obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(),
                id);
        Assert.assertTrue("Version for 'Container' failed to increment on merge and flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'Container' failed to initialize correctly", dod.getRandomContainer());
        Container obj = dod.getNewTransientContainer(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Container' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Container' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'Container' identifier to no longer be null", obj.getId());
    }

    @Test
    public void testRemove() {
        Container obj = dod.getRandomContainer();
        Assert.assertNotNull("Data on demand for 'Container' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Container' failed to provide an identifier", id);
        obj = Container.findContainer(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Container' with identifier '" + id + "'", Container.findContainer(id));
    }
}
