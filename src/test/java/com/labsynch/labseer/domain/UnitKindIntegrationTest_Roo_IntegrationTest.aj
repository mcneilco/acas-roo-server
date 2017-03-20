// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.UnitKind;
import com.labsynch.labseer.domain.UnitKindDataOnDemand;
import com.labsynch.labseer.domain.UnitKindIntegrationTest;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect UnitKindIntegrationTest_Roo_IntegrationTest {
    
    declare @type: UnitKindIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: UnitKindIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: UnitKindIntegrationTest: @Transactional;
    
    @Autowired
    UnitKindDataOnDemand UnitKindIntegrationTest.dod;
    
    @Test
    public void UnitKindIntegrationTest.testCountUnitKinds() {
        Assert.assertNotNull("Data on demand for 'UnitKind' failed to initialize correctly", dod.getRandomUnitKind());
        long count = UnitKind.countUnitKinds();
        Assert.assertTrue("Counter for 'UnitKind' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void UnitKindIntegrationTest.testFindUnitKind() {
        UnitKind obj = dod.getRandomUnitKind();
        Assert.assertNotNull("Data on demand for 'UnitKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UnitKind' failed to provide an identifier", id);
        obj = UnitKind.findUnitKind(id);
        Assert.assertNotNull("Find method for 'UnitKind' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'UnitKind' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void UnitKindIntegrationTest.testFindAllUnitKinds() {
        Assert.assertNotNull("Data on demand for 'UnitKind' failed to initialize correctly", dod.getRandomUnitKind());
        long count = UnitKind.countUnitKinds();
        Assert.assertTrue("Too expensive to perform a find all test for 'UnitKind', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<UnitKind> result = UnitKind.findAllUnitKinds();
        Assert.assertNotNull("Find all method for 'UnitKind' illegally returned null", result);
        Assert.assertTrue("Find all method for 'UnitKind' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void UnitKindIntegrationTest.testFindUnitKindEntries() {
        Assert.assertNotNull("Data on demand for 'UnitKind' failed to initialize correctly", dod.getRandomUnitKind());
        long count = UnitKind.countUnitKinds();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<UnitKind> result = UnitKind.findUnitKindEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'UnitKind' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'UnitKind' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void UnitKindIntegrationTest.testFlush() {
        UnitKind obj = dod.getRandomUnitKind();
        Assert.assertNotNull("Data on demand for 'UnitKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UnitKind' failed to provide an identifier", id);
        obj = UnitKind.findUnitKind(id);
        Assert.assertNotNull("Find method for 'UnitKind' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyUnitKind(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'UnitKind' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void UnitKindIntegrationTest.testMergeUpdate() {
        UnitKind obj = dod.getRandomUnitKind();
        Assert.assertNotNull("Data on demand for 'UnitKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UnitKind' failed to provide an identifier", id);
        obj = UnitKind.findUnitKind(id);
        boolean modified =  dod.modifyUnitKind(obj);
        Integer currentVersion = obj.getVersion();
        UnitKind merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'UnitKind' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void UnitKindIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'UnitKind' failed to initialize correctly", dod.getRandomUnitKind());
        UnitKind obj = dod.getNewTransientUnitKind(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'UnitKind' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'UnitKind' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'UnitKind' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void UnitKindIntegrationTest.testRemove() {
        UnitKind obj = dod.getRandomUnitKind();
        Assert.assertNotNull("Data on demand for 'UnitKind' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'UnitKind' failed to provide an identifier", id);
        obj = UnitKind.findUnitKind(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'UnitKind' with identifier '" + id + "'", UnitKind.findUnitKind(id));
    }
    
}