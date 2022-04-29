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
public class ItxSubjectContainerIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    ItxSubjectContainerDataOnDemand dod;

    @Test
    public void testCountItxSubjectContainers() {
        Assert.assertNotNull("Data on demand for 'ItxSubjectContainer' failed to initialize correctly",
                dod.getRandomItxSubjectContainer());
        long count = ItxSubjectContainer.countItxSubjectContainers();
        Assert.assertTrue("Counter for 'ItxSubjectContainer' incorrectly reported there were no entries", count > 0);
    }

    @Test
    public void testFindItxSubjectContainer() {
        ItxSubjectContainer obj = dod.getRandomItxSubjectContainer();
        Assert.assertNotNull("Data on demand for 'ItxSubjectContainer' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItxSubjectContainer' failed to provide an identifier", id);
        obj = ItxSubjectContainer.findItxSubjectContainer(id);
        Assert.assertNotNull("Find method for 'ItxSubjectContainer' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ItxSubjectContainer' returned the incorrect identifier", id, obj.getId());
    }

    @Test
    public void testFindAllItxSubjectContainers() {
        Assert.assertNotNull("Data on demand for 'ItxSubjectContainer' failed to initialize correctly",
                dod.getRandomItxSubjectContainer());
        long count = ItxSubjectContainer.countItxSubjectContainers();
        Assert.assertTrue("Too expensive to perform a find all test for 'ItxSubjectContainer', as there are " + count
                + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
                count < 250);
        List<ItxSubjectContainer> result = ItxSubjectContainer.findAllItxSubjectContainers();
        Assert.assertNotNull("Find all method for 'ItxSubjectContainer' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ItxSubjectContainer' failed to return any data", result.size() > 0);
    }

    @Test
    public void testFindItxSubjectContainerEntries() {
        Assert.assertNotNull("Data on demand for 'ItxSubjectContainer' failed to initialize correctly",
                dod.getRandomItxSubjectContainer());
        long count = ItxSubjectContainer.countItxSubjectContainers();
        if (count > 20)
            count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ItxSubjectContainer> result = ItxSubjectContainer.findItxSubjectContainerEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ItxSubjectContainer' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ItxSubjectContainer' returned an incorrect number of entries",
                count, result.size());
    }

    @Test
    public void testFlush() {
        ItxSubjectContainer obj = dod.getRandomItxSubjectContainer();
        Assert.assertNotNull("Data on demand for 'ItxSubjectContainer' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItxSubjectContainer' failed to provide an identifier", id);
        obj = ItxSubjectContainer.findItxSubjectContainer(id);
        Assert.assertNotNull("Find method for 'ItxSubjectContainer' illegally returned null for id '" + id + "'", obj);
        boolean modified = dod.modifyItxSubjectContainer(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ItxSubjectContainer' failed to increment on flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testMergeUpdate() {
        ItxSubjectContainer obj = dod.getRandomItxSubjectContainer();
        Assert.assertNotNull("Data on demand for 'ItxSubjectContainer' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItxSubjectContainer' failed to provide an identifier", id);
        obj = ItxSubjectContainer.findItxSubjectContainer(id);
        boolean modified = dod.modifyItxSubjectContainer(obj);
        Integer currentVersion = obj.getVersion();
        ItxSubjectContainer merged = (ItxSubjectContainer) obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(),
                id);
        Assert.assertTrue("Version for 'ItxSubjectContainer' failed to increment on merge and flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'ItxSubjectContainer' failed to initialize correctly",
                dod.getRandomItxSubjectContainer());
        ItxSubjectContainer obj = dod.getNewTransientItxSubjectContainer(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ItxSubjectContainer' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ItxSubjectContainer' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'ItxSubjectContainer' identifier to no longer be null", obj.getId());
    }

    @Test
    public void testRemove() {
        ItxSubjectContainer obj = dod.getRandomItxSubjectContainer();
        Assert.assertNotNull("Data on demand for 'ItxSubjectContainer' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ItxSubjectContainer' failed to provide an identifier", id);
        obj = ItxSubjectContainer.findItxSubjectContainer(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ItxSubjectContainer' with identifier '" + id + "'",
                ItxSubjectContainer.findItxSubjectContainer(id));
    }
}
