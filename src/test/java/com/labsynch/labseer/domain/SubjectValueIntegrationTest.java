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
public class SubjectValueIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    SubjectValueDataOnDemand dod;

	@Test
    public void testCountSubjectValues() {
        Assert.assertNotNull("Data on demand for 'SubjectValue' failed to initialize correctly", dod.getRandomSubjectValue());
        long count = SubjectValue.countSubjectValues();
        Assert.assertTrue("Counter for 'SubjectValue' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindSubjectValue() {
        SubjectValue obj = dod.getRandomSubjectValue();
        Assert.assertNotNull("Data on demand for 'SubjectValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SubjectValue' failed to provide an identifier", id);
        obj = SubjectValue.findSubjectValue(id);
        Assert.assertNotNull("Find method for 'SubjectValue' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'SubjectValue' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllSubjectValues() {
        Assert.assertNotNull("Data on demand for 'SubjectValue' failed to initialize correctly", dod.getRandomSubjectValue());
        long count = SubjectValue.countSubjectValues();
        Assert.assertTrue("Too expensive to perform a find all test for 'SubjectValue', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<SubjectValue> result = SubjectValue.findAllSubjectValues();
        Assert.assertNotNull("Find all method for 'SubjectValue' illegally returned null", result);
        Assert.assertTrue("Find all method for 'SubjectValue' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindSubjectValueEntries() {
        Assert.assertNotNull("Data on demand for 'SubjectValue' failed to initialize correctly", dod.getRandomSubjectValue());
        long count = SubjectValue.countSubjectValues();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<SubjectValue> result = SubjectValue.findSubjectValueEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'SubjectValue' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'SubjectValue' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        SubjectValue obj = dod.getRandomSubjectValue();
        Assert.assertNotNull("Data on demand for 'SubjectValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SubjectValue' failed to provide an identifier", id);
        obj = SubjectValue.findSubjectValue(id);
        Assert.assertNotNull("Find method for 'SubjectValue' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifySubjectValue(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'SubjectValue' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        SubjectValue obj = dod.getRandomSubjectValue();
        Assert.assertNotNull("Data on demand for 'SubjectValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SubjectValue' failed to provide an identifier", id);
        obj = SubjectValue.findSubjectValue(id);
        boolean modified =  dod.modifySubjectValue(obj);
        Integer currentVersion = obj.getVersion();
        SubjectValue merged = (SubjectValue)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'SubjectValue' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'SubjectValue' failed to initialize correctly", dod.getRandomSubjectValue());
        SubjectValue obj = dod.getNewTransientSubjectValue(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'SubjectValue' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'SubjectValue' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'SubjectValue' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        SubjectValue obj = dod.getRandomSubjectValue();
        Assert.assertNotNull("Data on demand for 'SubjectValue' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'SubjectValue' failed to provide an identifier", id);
        obj = SubjectValue.findSubjectValue(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'SubjectValue' with identifier '" + id + "'", SubjectValue.findSubjectValue(id));
    }
}
