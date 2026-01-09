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

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Transactional
public class LsTransactionIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

    @Autowired
    LsTransactionDataOnDemand dod;

    @Test
    public void testCountLsTransactions() {
        Assert.assertNotNull("Data on demand for 'LsTransaction' failed to initialize correctly",
                dod.getRandomLsTransaction());
        long count = LsTransaction.countLsTransactions();
        Assert.assertTrue("Counter for 'LsTransaction' incorrectly reported there were no entries", count > 0);
    }

    @Test
    public void testFindLsTransaction() {
        LsTransaction obj = dod.getRandomLsTransaction();
        Assert.assertNotNull("Data on demand for 'LsTransaction' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LsTransaction' failed to provide an identifier", id);
        obj = LsTransaction.findLsTransaction(id);
        Assert.assertNotNull("Find method for 'LsTransaction' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'LsTransaction' returned the incorrect identifier", id, obj.getId());
    }

    @Test
    public void testFindAllLsTransactions() {
        Assert.assertNotNull("Data on demand for 'LsTransaction' failed to initialize correctly",
                dod.getRandomLsTransaction());
        long count = LsTransaction.countLsTransactions();
        Assert.assertTrue("Too expensive to perform a find all test for 'LsTransaction', as there are " + count
                + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test",
                count < 250);
        List<LsTransaction> result = LsTransaction.findAllLsTransactions();
        Assert.assertNotNull("Find all method for 'LsTransaction' illegally returned null", result);
        Assert.assertTrue("Find all method for 'LsTransaction' failed to return any data", result.size() > 0);
    }

    @Test
    public void testFindLsTransactionEntries() {
        Assert.assertNotNull("Data on demand for 'LsTransaction' failed to initialize correctly",
                dod.getRandomLsTransaction());
        long count = LsTransaction.countLsTransactions();
        if (count > 20)
            count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<LsTransaction> result = LsTransaction.findLsTransactionEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'LsTransaction' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'LsTransaction' returned an incorrect number of entries", count,
                result.size());
    }

    @Test
    public void testFlush() {
        LsTransaction obj = dod.getRandomLsTransaction();
        Assert.assertNotNull("Data on demand for 'LsTransaction' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LsTransaction' failed to provide an identifier", id);
        obj = LsTransaction.findLsTransaction(id);
        Assert.assertNotNull("Find method for 'LsTransaction' illegally returned null for id '" + id + "'", obj);
        boolean modified = dod.modifyLsTransaction(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'LsTransaction' failed to increment on flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testMergeUpdate() {
        LsTransaction obj = dod.getRandomLsTransaction();
        Assert.assertNotNull("Data on demand for 'LsTransaction' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LsTransaction' failed to provide an identifier", id);
        obj = LsTransaction.findLsTransaction(id);
        boolean modified = dod.modifyLsTransaction(obj);
        Integer currentVersion = obj.getVersion();
        LsTransaction merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(),
                id);
        Assert.assertTrue("Version for 'LsTransaction' failed to increment on merge and flush directive",
                (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

    @Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'LsTransaction' failed to initialize correctly",
                dod.getRandomLsTransaction());
        LsTransaction obj = dod.getNewTransientLsTransaction(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'LsTransaction' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'LsTransaction' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'LsTransaction' identifier to no longer be null", obj.getId());
    }

    @Test
    public void testRemove() {
        LsTransaction obj = dod.getRandomLsTransaction();
        Assert.assertNotNull("Data on demand for 'LsTransaction' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'LsTransaction' failed to provide an identifier", id);
        obj = LsTransaction.findLsTransaction(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'LsTransaction' with identifier '" + id + "'",
                LsTransaction.findLsTransaction(id));
    }
}
