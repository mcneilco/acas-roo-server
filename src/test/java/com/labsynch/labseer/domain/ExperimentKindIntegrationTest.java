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
public class ExperimentKindIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    ExperimentKindDataOnDemand dod;

	@Test
    public void testCountExperimentKinds() {
        Assert.assertNotNull("Data on demand for 'ExperimentKind' failed to initialize correctly", dod.getRandomExperimentKind());
        long count = ExperimentKind.countExperimentKinds();
        Assert.assertTrue("Counter for 'ExperimentKind' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindExperimentKind() {
        ExperimentKind obj = dod.getRandomExperimentKind();
        Assert.assertNotNull("Data on demand for 'ExperimentKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ExperimentKind' failed to provide an identifier", id);
        obj = ExperimentKind.findExperimentKind(id);
        Assert.assertNotNull("Find method for 'ExperimentKind' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ExperimentKind' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllExperimentKinds() {
        Assert.assertNotNull("Data on demand for 'ExperimentKind' failed to initialize correctly", dod.getRandomExperimentKind());
        long count = ExperimentKind.countExperimentKinds();
        Assert.assertTrue("Too expensive to perform a find all test for 'ExperimentKind', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<ExperimentKind> result = ExperimentKind.findAllExperimentKinds();
        Assert.assertNotNull("Find all method for 'ExperimentKind' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ExperimentKind' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindExperimentKindEntries() {
        Assert.assertNotNull("Data on demand for 'ExperimentKind' failed to initialize correctly", dod.getRandomExperimentKind());
        long count = ExperimentKind.countExperimentKinds();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ExperimentKind> result = ExperimentKind.findExperimentKindEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ExperimentKind' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ExperimentKind' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        ExperimentKind obj = dod.getRandomExperimentKind();
        Assert.assertNotNull("Data on demand for 'ExperimentKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ExperimentKind' failed to provide an identifier", id);
        obj = ExperimentKind.findExperimentKind(id);
        Assert.assertNotNull("Find method for 'ExperimentKind' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyExperimentKind(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ExperimentKind' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        ExperimentKind obj = dod.getRandomExperimentKind();
        Assert.assertNotNull("Data on demand for 'ExperimentKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ExperimentKind' failed to provide an identifier", id);
        obj = ExperimentKind.findExperimentKind(id);
        boolean modified =  dod.modifyExperimentKind(obj);
        Integer currentVersion = obj.getVersion();
        ExperimentKind merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'ExperimentKind' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'ExperimentKind' failed to initialize correctly", dod.getRandomExperimentKind());
        ExperimentKind obj = dod.getNewTransientExperimentKind(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ExperimentKind' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ExperimentKind' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'ExperimentKind' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        ExperimentKind obj = dod.getRandomExperimentKind();
        Assert.assertNotNull("Data on demand for 'ExperimentKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ExperimentKind' failed to provide an identifier", id);
        obj = ExperimentKind.findExperimentKind(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ExperimentKind' with identifier '" + id + "'", ExperimentKind.findExperimentKind(id));
    }
}
