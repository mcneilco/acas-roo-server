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
@RooIntegrationTest(entity = ProtocolLabel.class)
public class ProtocolLabelIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    ProtocolLabelDataOnDemand dod;

	@Test
    public void testCountProtocolLabels() {
        Assert.assertNotNull("Data on demand for 'ProtocolLabel' failed to initialize correctly", dod.getRandomProtocolLabel());
        long count = ProtocolLabel.countProtocolLabels();
        Assert.assertTrue("Counter for 'ProtocolLabel' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindProtocolLabel() {
        ProtocolLabel obj = dod.getRandomProtocolLabel();
        Assert.assertNotNull("Data on demand for 'ProtocolLabel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProtocolLabel' failed to provide an identifier", id);
        obj = ProtocolLabel.findProtocolLabel(id);
        Assert.assertNotNull("Find method for 'ProtocolLabel' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'ProtocolLabel' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllProtocolLabels() {
        Assert.assertNotNull("Data on demand for 'ProtocolLabel' failed to initialize correctly", dod.getRandomProtocolLabel());
        long count = ProtocolLabel.countProtocolLabels();
        Assert.assertTrue("Too expensive to perform a find all test for 'ProtocolLabel', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<ProtocolLabel> result = ProtocolLabel.findAllProtocolLabels();
        Assert.assertNotNull("Find all method for 'ProtocolLabel' illegally returned null", result);
        Assert.assertTrue("Find all method for 'ProtocolLabel' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindProtocolLabelEntries() {
        Assert.assertNotNull("Data on demand for 'ProtocolLabel' failed to initialize correctly", dod.getRandomProtocolLabel());
        long count = ProtocolLabel.countProtocolLabels();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<ProtocolLabel> result = ProtocolLabel.findProtocolLabelEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'ProtocolLabel' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'ProtocolLabel' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        ProtocolLabel obj = dod.getRandomProtocolLabel();
        Assert.assertNotNull("Data on demand for 'ProtocolLabel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProtocolLabel' failed to provide an identifier", id);
        obj = ProtocolLabel.findProtocolLabel(id);
        Assert.assertNotNull("Find method for 'ProtocolLabel' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyProtocolLabel(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'ProtocolLabel' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        ProtocolLabel obj = dod.getRandomProtocolLabel();
        Assert.assertNotNull("Data on demand for 'ProtocolLabel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProtocolLabel' failed to provide an identifier", id);
        obj = ProtocolLabel.findProtocolLabel(id);
        boolean modified =  dod.modifyProtocolLabel(obj);
        Integer currentVersion = obj.getVersion();
        ProtocolLabel merged = (ProtocolLabel)obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'ProtocolLabel' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'ProtocolLabel' failed to initialize correctly", dod.getRandomProtocolLabel());
        ProtocolLabel obj = dod.getNewTransientProtocolLabel(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'ProtocolLabel' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'ProtocolLabel' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'ProtocolLabel' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        ProtocolLabel obj = dod.getRandomProtocolLabel();
        Assert.assertNotNull("Data on demand for 'ProtocolLabel' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'ProtocolLabel' failed to provide an identifier", id);
        obj = ProtocolLabel.findProtocolLabel(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'ProtocolLabel' with identifier '" + id + "'", ProtocolLabel.findProtocolLabel(id));
    }
}
