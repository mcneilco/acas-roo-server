package com.labsynch.labseer.utils;

import java.util.List;

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

	Boolean getAutoCreateKinds();

	String getClientFullPath();

	boolean getUniqueLsThingName();

	List<String> getComponentKindList();
	
	List<String> getAssemblyKindList();

	
}