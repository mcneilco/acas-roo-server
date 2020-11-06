package com.labsynch.labseer.utils;

import org.springframework.stereotype.Service;

@Service
public interface PropertiesFileService {

	String getProperty(String propertFilePath, String propertyName);

	String getUsernameProperty(String propertFilePath, String userName);



	
}
