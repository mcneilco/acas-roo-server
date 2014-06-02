package com.labsynch.labseer.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetupPropertiesUtils {

	private static final Logger logger = LoggerFactory.getLogger(InitialSetup.class);

	private static String setupPropertiesFileName = "initialsetup.properties";
//	private static String setupPropertiesFileName = "node_config.js";

	public static String getProperties(String propertyName) throws IOException {
		logger.debug("PROP Reader");
		Properties properties = new Properties();
		InputStream propertyStream = SetupPropertiesUtils.class.getClassLoader().getResourceAsStream(setupPropertiesFileName);

		properties.load(propertyStream);
		propertyStream.close();

		return properties.getProperty(propertyName);
	}
}
