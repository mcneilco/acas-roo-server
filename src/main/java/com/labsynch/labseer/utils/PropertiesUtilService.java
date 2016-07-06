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

	Boolean getAutoCreateKinds();

	String getClientFullPath();

	boolean getUniqueLsThingName();
	
	boolean getEnableSwagger();

	String getAcaslicenseFile();
	
	boolean getSyncLdapAuthRoles();

	String getAcasAdminRole();

	String getAcasUserRole();

	int getContainerInventorySearchMaxResult();

	
}