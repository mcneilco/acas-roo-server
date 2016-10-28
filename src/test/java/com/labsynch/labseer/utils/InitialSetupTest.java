package com.labsynch.labseer.utils;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.labseer.domain.ThingKind;
import com.labsynch.labseer.domain.ThingType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Configurable
public class InitialSetupTest {

	private static final Logger logger = LoggerFactory.getLogger(InitialSetupTest.class);

	@Test
	public void Setup_Test_1() throws IOException{
		logger.debug("run initial setup");
		InitialSetup.setupFixtures();
	}

	//@Test
	public void Setup_Test_2() throws IOException{
		logger.debug("run initial setup");
		//thingTypes
		if (ThingType.countThingTypes() < 1){
			//logger.info("Setting new thing types");
			String propertyName = "thingTypes";
			String json = SetupPropertiesUtils.getProperties(propertyName);
			//logger.info("input json " + json);

			Collection<ThingType> thingTypes = ThingType.fromJsonArrayToThingTypes(json);
			for (ThingType thingType : thingTypes){
				thingType.persist();
				//logger.info(thingType.toJson());
			}
		}
		
		if (ThingKind.countThingKinds() < 1){
			String propertyName = "thingKinds";
			String json = SetupPropertiesUtils.getProperties(propertyName);
			//logger.info("input json " + json);

			Collection<ThingKind> thingKinds = ThingKind.fromJsonArrayToThingKinds(json);
			for (ThingKind thingKind : thingKinds){
				logger.info(thingKind.toJson());
				List<ThingType> dbLsTypes = ThingType.findThingTypesByTypeNameEquals(thingKind.getLsType().getTypeName()).getResultList();
				if (dbLsTypes.size() == 1){
					thingKind.setLsType(dbLsTypes.get(0));
					thingKind.persist();
					logger.info(thingKind.toJson());
				} else {
					logger.error("did not get expected results: " + dbLsTypes.size());
				}
			}
			
		}
		
	}



}
