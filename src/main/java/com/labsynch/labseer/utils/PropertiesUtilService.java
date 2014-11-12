package com.labsynch.labseer.utils;

import org.springframework.stereotype.Service;

@Service
public interface PropertiesUtilService {

	Integer getBatchSize();

	Integer getFetchSize();

	Boolean getUniqueExperimentName();

	String getHostPath();

	String getAuthStrategy();

	String getClientPath();

	String getSecurityProperties();

	Boolean getUniqueProtocolName();

	boolean getCreateKindsOnTheFly();

	
}