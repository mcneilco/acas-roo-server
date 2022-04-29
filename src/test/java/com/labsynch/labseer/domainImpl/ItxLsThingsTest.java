package com.labsynch.labseer.domainImpl;

import java.util.List;

import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.LsThing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class ItxLsThingsTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ItxLsThingsTest.class);
	
	@Test
	@Transactional
    public void getSingleItxLsThingTest1() {
		Long itxId = 216082L;
		ItxLsThingLsThing itxLs = ItxLsThingLsThing.findItxLsThingLsThing(itxId);
		logger.info(itxLs.toJson());
	
	}
	
	@Test
	@Transactional
    public void getItxLsThingTest2() {
		Long thingId = 216221L;
		LsThing lsThing = LsThing.findLsThing(thingId);
		List<ItxLsThingLsThing> itxLsSet = ItxLsThingLsThing.findItxLsThingLsThingsByLsTypeEqualsAndLsKindEqualsAndFirstLsThingEquals("supports", "reference_gene", lsThing).getResultList();
		logger.info("############ Set of interactions ##########");
		logger.info(ItxLsThingLsThing.toJsonArray(itxLsSet));
	
	}
	
}
