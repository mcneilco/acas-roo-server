package com.labsynch.labseer.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertiesUtilServiceImpl implements PropertiesUtilService{
	
	String clientPath;

	@Value("${client.path}")
	public void setClientPath(String clientPath) {
	    this.clientPath = clientPath;
	}

	@Override
	public String getClientPath() {
	    return this.clientPath;
	}
	
	String hostPath;

	@Value("${client.service.persistence.fullpath}")
	public void setHostPath(String hostPath) {
	    this.hostPath = hostPath;
	}

	@Override
	public String getHostPath() {
	    return this.hostPath;
	}

	String authStrategy;

	@Value("${server.security.authstrategy}")
	public void setAuthStrategy(String authStrategy) {
	    this.authStrategy = authStrategy;
	}

	@Override
	public String getAuthStrategy() {
	    return this.authStrategy;
	}
	
	String securityProperties;

	@Value("${server.security.properties.path}")
	public void setSecurityProperties(String securityProperties) {
	    this.securityProperties = securityProperties;
	}

	@Override
	public String getSecurityProperties() {
	    return this.securityProperties;
	}
	
	String batchSize;

	@Value("${acas.batchSize}")
	public void setBatchSize(String batchSize) {
	    this.batchSize = batchSize;
	}

	@Override
	public Integer getBatchSize() {
	    return Integer.parseInt(this.batchSize);
	}

	String fetchSize;

	@Value("${acas.fetchSize}")
	public void setFetchSize(String fetchSize) {
	    this.fetchSize = fetchSize;
	}

	@Override
	public Integer getFetchSize() {
	    return Integer.parseInt(this.fetchSize);
	}
	
	String uniqueExperimentName;

	@Value("${uniqueExperimentName}")
	public void setUniqueExperimentName(String uniqueExperimentName) {
	    this.uniqueExperimentName = uniqueExperimentName;
	}
	
	@Override
	public Boolean getUniqueExperimentName() {
	    return Boolean.parseBoolean(this.uniqueExperimentName);
	}
	
	String uniqueProtocolName;

	@Value("${uniqueProtocolName}")
	public void setUniqueProtocolName(String uniqueProtocolName) {
	    this.uniqueExperimentName = uniqueProtocolName;
	}
	
	@Override
	public Boolean getUniqueProtocolName() {
	    return Boolean.parseBoolean(this.uniqueProtocolName);
	}
	
	String autoCreateKinds;

	@Value("${autoCreateKinds}")
	public void setAutoCreateKinds(String autoCreateKinds) {
	    this.uniqueExperimentName = autoCreateKinds;
	}
	
	@Override
	public Boolean getAutoCreateKinds() {
	    return Boolean.parseBoolean(this.autoCreateKinds);
	}
	
	String guiPath;

	@Value("${acas.gui.fullpath}")
	public void setGuiPath(String guiPath) {
	    this.guiPath = guiPath;
	}
	
	@Override
	public String getGuiPath() {
	    return this.guiPath;
	}
}