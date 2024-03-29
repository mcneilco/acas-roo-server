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
public class ExperimentIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    ExperimentDataOnDemand dod;

    @Test
    public void testCountExperiments() {
        Assert.assertNotNull("Data on demand for 'Experiment' failed to initialize correctly",
                dod.getRandomExperiment());
        long count = Experiment.countExperiments();
        Assert.assertTrue("Counter for 'Experiment' incorrectly reported there were no entries", count > 0);
    }

    @Test
    public void testFindExperiment() {
        Experiment obj = dod.getRandomExperiment();
        Assert.assertNotNull("Data on demand for 'Experiment' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Experiment' failed to provide an identifier", id);
        obj = Experiment.findExperiment(id);
        Assert.assertNotNull("Find method for 'Experiment' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Experiment' returned the incorrect identifier", id, obj.getId());
    }

    @Test
    public void testFindAllExperiments() {
        Assert.assertNotNull("Data on demand for 'Experiment' failed to initialize correctly",
                dod.getRandomExperiment());
        long count = Experiment.countExperiments();
        Assert.assertTrue("Too expensive to perform a find all test for 'Experiment', as there are " + count
                + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
                count < 250);
        List<Experiment> result = Experiment.findAllExperiments();
        Assert.assertNotNull("Find all method for 'Experiment' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Experiment' failed to return any data", result.size() > 0);
    }

    @Test
    public void testFindExperimentEntries() {
        Assert.assertNotNull("Data on demand for 'Experiment' failed to initialize correctly",
                dod.getRandomExperiment());
        long count = Experiment.countExperiments();
        if (count > 20)
            count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Experiment> result = Experiment.findExperimentEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Experiment' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Experiment' returned an incorrect number of entries", count,
                result.size());
    }

    @Test
    public void testFlush() {
        Experiment obj = dod.getRandomExperiment();
        Assert.assertNotNull("Data on demand for 'Experiment' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Experiment' failed to provide an identifier", id);
        obj = Experiment.findExperiment(id);
        Assert.assertNotNull("Find method for 'Experiment' illegally returned null for id '" + id + "'", obj);
        boolean modified = dod.modifyExperiment(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Experiment' failed to increment on flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testMergeUpdate() {
        Experiment obj = dod.getRandomExperiment();
        Assert.assertNotNull("Data on demand for 'Experiment' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Experiment' failed to provide an identifier", id);
        obj = Experiment.findExperiment(id);
        boolean modified = dod.modifyExperiment(obj);
        Integer currentVersion = obj.getVersion();
        Experiment merged = (Experiment) obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(),
                id);
        Assert.assertTrue("Version for 'Experiment' failed to increment on merge and flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'Experiment' failed to initialize correctly",
                dod.getRandomExperiment());
        Experiment obj = dod.getNewTransientExperiment(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Experiment' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Experiment' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'Experiment' identifier to no longer be null", obj.getId());
    }

    @Test
    public void testRemove() {
        Experiment obj = dod.getRandomExperiment();
        Assert.assertNotNull("Data on demand for 'Experiment' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Experiment' failed to provide an identifier", id);
        obj = Experiment.findExperiment(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Experiment' with identifier '" + id + "'", Experiment.findExperiment(id));
    }
}
