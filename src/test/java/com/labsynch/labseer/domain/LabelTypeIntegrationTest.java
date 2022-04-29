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
import org.springframework.roo.addon.test.RooIntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Transactional
@RooIntegrationTest(entity = LabelType.class)
public class LabelTypeIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    LabelTypeDataOnDemand dod;

	@Test
    public void testCountLabelTypes() {
        Assert.assertNotNull("Data on demand for 'LabelType' failed to initialize correctly", dod.getRandomLabelType());
        long count = LabelType.countLabelTypes();
        Assert.assertTrue("Counter for 'LabelType' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindLabelType() {
        LabelType obj = dod.getRandomLabelType();
        Assert.assertNotNull("Data on demand for 'LabelType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LabelType' failed to provide an identifier", id);
        obj = LabelType.findLabelType(id);
        Assert.assertNotNull("Find method for 'LabelType' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'LabelType' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllLabelTypes() {
        Assert.assertNotNull("Data on demand for 'LabelType' failed to initialize correctly", dod.getRandomLabelType());
        long count = LabelType.countLabelTypes();
        Assert.assertTrue("Too expensive to perform a find all test for 'LabelType', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<LabelType> result = LabelType.findAllLabelTypes();
        Assert.assertNotNull("Find all method for 'LabelType' illegally returned null", result);
        Assert.assertTrue("Find all method for 'LabelType' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindLabelTypeEntries() {
        Assert.assertNotNull("Data on demand for 'LabelType' failed to initialize correctly", dod.getRandomLabelType());
        long count = LabelType.countLabelTypes();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<LabelType> result = LabelType.findLabelTypeEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'LabelType' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'LabelType' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        LabelType obj = dod.getRandomLabelType();
        Assert.assertNotNull("Data on demand for 'LabelType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LabelType' failed to provide an identifier", id);
        obj = LabelType.findLabelType(id);
        Assert.assertNotNull("Find method for 'LabelType' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyLabelType(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'LabelType' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        LabelType obj = dod.getRandomLabelType();
        Assert.assertNotNull("Data on demand for 'LabelType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LabelType' failed to provide an identifier", id);
        obj = LabelType.findLabelType(id);
        boolean modified =  dod.modifyLabelType(obj);
        Integer currentVersion = obj.getVersion();
        LabelType merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'LabelType' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'LabelType' failed to initialize correctly", dod.getRandomLabelType());
        LabelType obj = dod.getNewTransientLabelType(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'LabelType' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'LabelType' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'LabelType' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        LabelType obj = dod.getRandomLabelType();
        Assert.assertNotNull("Data on demand for 'LabelType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LabelType' failed to provide an identifier", id);
        obj = LabelType.findLabelType(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'LabelType' with identifier '" + id + "'", LabelType.findLabelType(id));
    }
}
