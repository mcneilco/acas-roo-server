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
public class OperatorTypeIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    OperatorTypeDataOnDemand dod;

	@Test
    public void testCountOperatorTypes() {
        Assert.assertNotNull("Data on demand for 'OperatorType' failed to initialize correctly", dod.getRandomOperatorType());
        long count = OperatorType.countOperatorTypes();
        Assert.assertTrue("Counter for 'OperatorType' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindOperatorType() {
        OperatorType obj = dod.getRandomOperatorType();
        Assert.assertNotNull("Data on demand for 'OperatorType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'OperatorType' failed to provide an identifier", id);
        obj = OperatorType.findOperatorType(id);
        Assert.assertNotNull("Find method for 'OperatorType' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'OperatorType' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllOperatorTypes() {
        Assert.assertNotNull("Data on demand for 'OperatorType' failed to initialize correctly", dod.getRandomOperatorType());
        long count = OperatorType.countOperatorTypes();
        Assert.assertTrue("Too expensive to perform a find all test for 'OperatorType', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<OperatorType> result = OperatorType.findAllOperatorTypes();
        Assert.assertNotNull("Find all method for 'OperatorType' illegally returned null", result);
        Assert.assertTrue("Find all method for 'OperatorType' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindOperatorTypeEntries() {
        Assert.assertNotNull("Data on demand for 'OperatorType' failed to initialize correctly", dod.getRandomOperatorType());
        long count = OperatorType.countOperatorTypes();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<OperatorType> result = OperatorType.findOperatorTypeEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'OperatorType' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'OperatorType' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        OperatorType obj = dod.getRandomOperatorType();
        Assert.assertNotNull("Data on demand for 'OperatorType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'OperatorType' failed to provide an identifier", id);
        obj = OperatorType.findOperatorType(id);
        Assert.assertNotNull("Find method for 'OperatorType' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyOperatorType(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'OperatorType' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        OperatorType obj = dod.getRandomOperatorType();
        Assert.assertNotNull("Data on demand for 'OperatorType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'OperatorType' failed to provide an identifier", id);
        obj = OperatorType.findOperatorType(id);
        boolean modified =  dod.modifyOperatorType(obj);
        Integer currentVersion = obj.getVersion();
        OperatorType merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'OperatorType' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'OperatorType' failed to initialize correctly", dod.getRandomOperatorType());
        OperatorType obj = dod.getNewTransientOperatorType(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'OperatorType' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'OperatorType' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'OperatorType' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        OperatorType obj = dod.getRandomOperatorType();
        Assert.assertNotNull("Data on demand for 'OperatorType' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'OperatorType' failed to provide an identifier", id);
        obj = OperatorType.findOperatorType(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'OperatorType' with identifier '" + id + "'", OperatorType.findOperatorType(id));
    }
}
