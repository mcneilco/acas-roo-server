package com.labsynch.labseer.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class ReadSeuratUsersFileTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ReadSeuratUsersFileTest.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	
	@Test
	public void Read_Test_1() {
		logger.info("here is the current authStrategy: " + propertiesUtilService.getAuthStrategy());
	}
	
	@Test
	public void Read_Test_3() throws IOException {
		logger.info("here is the current path to the security properties file: " + propertiesUtilService.getSecurityProperties());

		Properties properties = new Properties();
		FileInputStream propertyStream = new FileInputStream(propertiesUtilService.getSecurityProperties()); 

		properties.load(propertyStream);
		propertyStream.close();
		
		logger.info(properties.getProperty("bob"));
		logger.info(properties.getProperty("mcneilco"));
		logger.info(properties.getProperty("huang"));
		
		String passwordString = properties.getProperty("huang");
		List<String> passwordList = new ArrayList<String>();
		String[] parsedEntry = passwordString.split("\\{SHA\\}|,");
		for (String word : parsedEntry){
			logger.info("parsed word: " + word);
			passwordList.add(word);
		}
		
		if (passwordString.contains("{SHA}")){
			logger.info("the entry starts with {SHA}");
			logger.info("here is the encrypted password: " + passwordList.get(1));
		} else {
			logger.info("the entry is a simple password");
			logger.info("here is the simple password: " + passwordList.get(0));
		}
				
	}
	


	
}
