

package com.labsynch.labseer.service;

import java.util.HashSet;
import java.util.Set;

import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.utils.PropertiesUtilService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class LsThingServicePutTests {

	private static final Logger logger = LoggerFactory.getLogger(LsThingServicePutTests.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
	@Autowired
	private LsThingService lsThingService;
	
	@Transactional
	@Test
	public void lsThingNestedPutTest2() {
		
		LsThing queryThing = LsThing.findLsThing(8L);

		LsThing results = lsThingService.updateLsThing(queryThing);
		
		logger.info(results.toJsonWithNestedFull());

	}
	
	@Transactional
//	@Test
	public void getThingsTest() {
		
		LsThing queryThing = LsThing.findLsThing(8L);

		Set<ItxLsThingLsThing> firstLsThings = new HashSet<ItxLsThingLsThing>();
		firstLsThings.addAll(ItxLsThingLsThing.findItxLsThingLsThingsByFirstLsThing(queryThing).getResultList());
		logger.debug("found number of first interactions: " + firstLsThings.size());
		
		Set<ItxLsThingLsThing> secondLsThings = new HashSet<ItxLsThingLsThing>();
		secondLsThings.addAll(ItxLsThingLsThing.findItxLsThingLsThingsBySecondLsThing(queryThing).getResultList());
		logger.debug("found number of second interactions: " + secondLsThings.size());

	}
	
	@Transactional
//	@Test
	public void getThingsItxTest() {
		
		LsThing queryThing = LsThing.findLsThing(8L);

		Set<ItxLsThingLsThing> firstLsThings = queryThing.getFirstLsThings();
		logger.debug("found number of first interactions: " + firstLsThings.size());
		
		Set<ItxLsThingLsThing> secondLsThings = queryThing.getSecondLsThings();
		logger.debug("found number of second interactions: " + secondLsThings.size());

	}
	
}
