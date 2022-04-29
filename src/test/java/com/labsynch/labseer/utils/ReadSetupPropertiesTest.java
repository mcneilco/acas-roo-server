package com.labsynch.labseer.utils;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml" })
@Configurable
public class ReadSetupPropertiesTest {

	private static final Logger logger = LoggerFactory.getLogger(ReadSetupPropertiesTest.class);

	@Test
	public void Setup_Test_1() throws IOException {
		logger.debug("run initial setup");
		String propertyName = "thingTypes";
		// String propertyName =
		// "exports.serverConfigurationParams.configuration.username";
		logger.info("TESTING property " + SetupPropertiesUtils.getProperties(propertyName));
	}

}
