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
public class SubjectStateIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    SubjectStateDataOnDemand dod;

	@Test
    public void testCountSubjectStates() {
        Assert.assertNotNull("Data on demand for 'SubjectState' failed to initialize correctly", dod.getRandomSubjectState());
        long count = SubjectState.countSubjectStates();
        Assert.assertTrue("Counter for 'SubjectState' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindSubjectState() {
        SubjectState obj = dod.getRandomSubjectState();
        Assert.assertNotNull("Data on demand for 'SubjectState' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SubjectState' failed to provide an identifier", id);
        obj = SubjectState.findSubjectState(id);
        Assert.assertNotNull("Find method for 'SubjectState' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'SubjectState' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllSubjectStates() {
        Assert.assertNotNull("Data on demand for 'SubjectState' failed to initialize correctly", dod.getRandomSubjectState());
        long count = SubjectState.countSubjectStates();
        Assert.assertTrue("Too expensive to perform a find all test for 'SubjectState', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<SubjectState> result = SubjectState.findAllSubjectStates();
        Assert.assertNotNull("Find all method for 'SubjectState' illegally returned null", result);
        Assert.assertTrue("Find all method for 'SubjectState' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindSubjectStateEntries() {
        Assert.assertNotNull("Data on demand for 'SubjectState' failed to initialize correctly", dod.getRandomSubjectState());
        long count = SubjectState.countSubjectStates();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<SubjectState> result = SubjectState.findSubjectStateEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'SubjectState' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'SubjectState' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        SubjectState obj = dod.getRandomSubjectState();
        Assert.assertNotNull("Data on demand for 'SubjectState' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SubjectState' failed to provide an identifier", id);
        obj = SubjectState.findSubjectState(id);
        Assert.assertNotNull("Find method for 'SubjectState' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifySubjectState(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'SubjectState' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        SubjectState obj = dod.getRandomSubjectState();
        Assert.assertNotNull("Data on demand for 'SubjectState' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SubjectState' failed to provide an identifier", id);
        obj = SubjectState.findSubjectState(id);
        boolean modified =  dod.modifySubjectState(obj);
        Integer currentVersion = obj.getVersion();
        SubjectState merged = (SubjectState)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'SubjectState' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'SubjectState' failed to initialize correctly", dod.getRandomSubjectState());
        SubjectState obj = dod.getNewTransientSubjectState(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'SubjectState' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'SubjectState' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'SubjectState' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        SubjectState obj = dod.getRandomSubjectState();
        Assert.assertNotNull("Data on demand for 'SubjectState' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SubjectState' failed to provide an identifier", id);
        obj = SubjectState.findSubjectState(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'SubjectState' with identifier '" + id + "'", SubjectState.findSubjectState(id));
    }
}
