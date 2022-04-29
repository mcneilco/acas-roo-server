package com.labsynch.labseer.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PropertiesFileServiceImpl implements PropertiesFileService {

	private static final Logger logger = LoggerFactory.getLogger(PropertiesFileServiceImpl.class);

	@Override
	public String getProperty(String propertFilePath, String propertyName) {

		Properties properties = new Properties();
		FileInputStream propertyStream;
		try {
			propertyStream = new FileInputStream(propertFilePath);
			properties.load(propertyStream);
			propertyStream.close();

		} catch (FileNotFoundException e) {
			logger.error("ERROR: file not found");
		} catch (IOException e) {
			logger.error("ERROR: IO exception");

		}

		String property = properties.getProperty(propertyName);

		return property;

	}

	@Override
	public String getUsernameProperty(String propertFilePath, String userName) {

		Properties properties = new Properties();
		FileInputStream propertyStream;
		try {
			propertyStream = new FileInputStream(propertFilePath);
			properties.load(propertyStream);
			propertyStream.close();

		} catch (FileNotFoundException e) {
			logger.error("ERROR: file not found");
		} catch (IOException e) {
			logger.error("ERROR: IO exception");
		}

		String property = properties.getProperty(userName);

		if (property != null && property.length() > 0) {
			return userName;
		} else {
			return null;
		}

	}

}