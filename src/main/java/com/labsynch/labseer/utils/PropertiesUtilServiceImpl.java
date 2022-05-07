package com.labsynch.labseer.utils;

import java.util.Collection;

import com.labsynch.labseer.dto.SimpleBulkLoadPropertyDTO;
import com.labsynch.labseer.service.ChemStructureService.SearchType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertiesUtilServiceImpl implements PropertiesUtilService {

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

	int containerInventorySearchMaxResult;

	@Value("${client.containerInventory.search.maxResult}")
	public void setContainerInventorySearchMaxResult(int containerInventorySearchMaxResult) {
		this.containerInventorySearchMaxResult = containerInventorySearchMaxResult;
	}

	@Override
	public int getContainerInventorySearchMaxResult() {
		return this.containerInventorySearchMaxResult;
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

	@Value("${server.acas.batchSize}")
	public void setBatchSize(String batchSize) {
		this.batchSize = batchSize;
	}

	@Override
	public Integer getBatchSize() {
		return Integer.parseInt(this.batchSize);
	}

	String fetchSize;

	@Value("${server.acas.fetchSize}")
	public void setFetchSize(String fetchSize) {
		this.fetchSize = fetchSize;
	}

	@Override
	public Integer getFetchSize() {
		return Integer.parseInt(this.fetchSize);
	}

	String uniqueExperimentName;

	@Value("${server.uniqueExperimentName}")
	public void setUniqueExperimentName(String uniqueExperimentName) {
		this.uniqueExperimentName = uniqueExperimentName;
	}

	@Override
	public Boolean getUniqueExperimentName() {
		return Boolean.parseBoolean(this.uniqueExperimentName);
	}

	Boolean uniqueProtocolName;

	@Value("${server.uniqueProtocolName}")
	public void setUniqueProtocolName(Boolean uniqueProtocolName) {
		this.uniqueProtocolName = Boolean.parseBoolean(this.uniqueExperimentName);
	}

	@Override
	public Boolean getUniqueProtocolName() {
		return this.uniqueProtocolName;
	}

	String autoCreateKinds;

	@Value("${server.autoCreateKinds}")
	public void setAutoCreateKinds(String autoCreateKinds) {
		this.autoCreateKinds = autoCreateKinds;
	}

	@Override
	public Boolean getAutoCreateKinds() {
		return Boolean.parseBoolean(this.autoCreateKinds);
	}

	String clientFullPath;

	@Value("${client.fullpath}")
	public void setClientFullPath(String clientFullPath) {
		this.clientFullPath = clientFullPath;
	}

	@Override
	public String getClientFullPath() {
		return this.clientFullPath;
	}

	String uniqueLsThingName;

	@Value("${server.uniqueLsThingName}")
	public void setUniqueLsThingName(String uniqueLsThingName) {
		this.uniqueLsThingName = uniqueLsThingName;
	}

	@Override
	public boolean getUniqueLsThingName() {
		return Boolean.parseBoolean(this.uniqueLsThingName);
	}

	String enableSwagger;

	@Value("${server.enableSwagger}")
	public void setEnableSwagger(String enableSwagger) {
		this.enableSwagger = enableSwagger;
	}

	@Override
	public boolean getEnableSwagger() {
		return Boolean.parseBoolean(this.enableSwagger);
	}

	String acasLicenseFile;

	@Value("${server.acas.license}")
	public void setAcaslicenseFile(String acasLicenseFile) {
		this.acasLicenseFile = acasLicenseFile;
	}

	@Override
	public String getAcaslicenseFile() {
		return this.acasLicenseFile;
	}

	String syncLdapAuthRoles;

	@Value("${server.security.syncLdapAuthRoles}")
	public void setSyncLdapAuthRoles(String syncLdapAuthRoles) {
		this.syncLdapAuthRoles = syncLdapAuthRoles;
	}

	@Override
	public boolean getSyncLdapAuthRoles() {
		return Boolean.parseBoolean(this.syncLdapAuthRoles);
	}

	String restrictExperiments;

	@Value("${server.service.projects.restrictExperiments}")
	public void setRestrictExperiments(String restrictExperiments) {
		this.restrictExperiments = restrictExperiments;
	}

	@Override
	public boolean getRestrictExperiments() {
		return Boolean.parseBoolean(this.restrictExperiments);
	}

	String acasUserRole;

	@Value("${client.roles.acas.userRole}")
	public void setAcasUserRole(String acasUserRole) {
		this.acasUserRole = acasUserRole;
	}

	@Override
	public String getAcasUserRole() {
		return this.acasUserRole;
	}

	String acasAdminRole;

	@Value("${client.roles.acas.adminRole}")
	public void setAcasAdminRole(String acasAdminRole) {
		this.acasAdminRole = acasAdminRole;
	}

	@Override
	public String getAcasAdminRole() {
		return this.acasAdminRole;
	}

	String enableProjectRoles;

	@Value("${server.project.roles.enable}")
	public void setEnableProjectRoles(String enableProjectRoles) {
		this.enableProjectRoles = enableProjectRoles;
	}

	@Override
	public boolean getEnableProjectRoles() {
		return Boolean.parseBoolean(this.enableProjectRoles);
	}

	// server.chemistry.package
	String chemistryPackage;

	@Value("${server.chemistry.package}")
	public void setChemistryPackage(String chemistryPackage) {
		this.chemistryPackage = chemistryPackage;
	}

	@Override
	public String getChemistryPackage() {
		return this.chemistryPackage;
	}

	String rootLocationLabel;

	@Value("${client.compoundInventory.rootLocationLabel}")
	public void setRootLocationLabel(String rootLocationLabel) {
		this.rootLocationLabel = rootLocationLabel;
		if (this.rootLocationLabel.startsWith("${"))
			this.rootLocationLabel = null;
	}

	@Override
	public String getRootLocationLabel() {
		return this.rootLocationLabel;
	}

	String trashLocationLabel;

	@Value("${client.compoundInventory.trashLocationLabel}")
	public void setTrashLocationLabel(String trashLocationLabel) {
		this.trashLocationLabel = trashLocationLabel;
		if (this.trashLocationLabel.startsWith("${"))
			this.trashLocationLabel = null;
	}

	@Override
	public String getTrashLocationLabel() {
		return this.trashLocationLabel;
	}

	String benchesLocationLabel;

	@Value("${client.compoundInventory.benchesLocationLabel}")
	public void setBenchesLocationLabel(String benchesLocationLabel) {
		this.benchesLocationLabel = benchesLocationLabel;
		if (this.benchesLocationLabel.startsWith("${"))
			this.benchesLocationLabel = null;
	}

	@Override
	public String getBenchesLocationLabel() {
		return this.benchesLocationLabel;
	}

	String emailFromAddress;

	@Value("${server.support.smtp.from}")
	public void setEmailFromAddress(String emailFromAddress) {
		this.emailFromAddress = emailFromAddress;
		if (this.emailFromAddress.startsWith("${"))
			this.emailFromAddress = null;
	}

	@Override
	public String getEmailFromAddress() {
		return this.emailFromAddress;
	}

	// CMPD REG PROPERTIES
	String acasURL;

	@Value("${client.cmpdreg.serverConnection.acasURL}")
	public void setAcasURL(String acasURL) {
		this.acasURL = acasURL;
		if (this.acasURL.startsWith("${")) {
			acasURL = null;
		} else {
			this.acasURL = acasURL;
		}
		;
	}

	@Override
	public String getAcasURL() {
		return this.acasURL;
	}

	String acasAppURL;

	@Value("${client.cmpdreg.serverConnection.acasAppURL}")
	public void setAcasAppURL(String acasAppURL) {
		this.acasAppURL = acasAppURL;
		if (this.acasAppURL.startsWith("${")) {
			acasAppURL = null;
		} else {
			this.acasAppURL = acasAppURL;
		}
		;
	}

	@Override
	public String getAcasAppURL() {
		return this.acasAppURL;
	}

	Boolean lotCalledBatch;

	@Value("${client.cmpdreg.metaLot.lotCalledBatch}")
	public void setLotCalledBatch(String lotCalledBatch) {
		if (lotCalledBatch.startsWith("${")) {
			lotCalledBatch = null;
		} else {
			this.lotCalledBatch = Boolean.parseBoolean(lotCalledBatch);
		}
		;
	}

	@Override
	public Boolean getLotCalledBatch() {
		return this.lotCalledBatch;
	}

	Boolean useExactMass;

	@Value("${client.cmpdreg.metaLot.useExactMass}")
	public void setUseExactMass(String useExactMass) {
		if (useExactMass.startsWith("${")) {
			useExactMass = null;
		} else {
			this.useExactMass = Boolean.parseBoolean(useExactMass);
		}
		;
	}

	@Override
	public Boolean getUseExactMass() {
		return this.useExactMass;
	}

	String corpPrefix;

	@Value("${client.cmpdreg.serverSettings.corpPrefix}")
	public void setCorpPrefix(String corpPrefix) {
		this.corpPrefix = corpPrefix;
		if (this.corpPrefix.startsWith("${")) {
			corpPrefix = null;
		} else {
			this.corpPrefix = corpPrefix;
		}
		;
	}

	@Override
	public String getCorpPrefix() {
		return this.corpPrefix;
	}

	String corpSeparator;

	@Value("${client.cmpdreg.serverSettings.corpSeparator}")
	public void setCorpSeparator(String corpSeparator) {
		this.corpSeparator = corpSeparator;
		if (this.corpSeparator.startsWith("${")) {
			corpSeparator = null;
		} else {
			this.corpSeparator = corpSeparator;
		}
		;
	}

	@Override
	public String getCorpSeparator() {
		return this.corpSeparator;
	}

	String saltSeparator;

	@Value("${client.cmpdreg.serverSettings.saltSeparator}")
	public void setSaltSeparator(String saltSeparator) {
		this.saltSeparator = saltSeparator;
		if (this.saltSeparator.startsWith("${")) {
			saltSeparator = null;
		} else {
			this.saltSeparator = saltSeparator;
		}
		;
	}

	@Override
	public String getSaltSeparator() {
		return this.saltSeparator;
	}

	String batchSeparator;

	@Value("${client.cmpdreg.serverSettings.batchSeparator}")
	public void setBatchSeparator(String batchSeparator) {
		this.batchSeparator = batchSeparator;
		if (this.batchSeparator.startsWith("${")) {
			batchSeparator = null;
		} else {
			this.batchSeparator = batchSeparator;
		}
		;
	}

	@Override
	public String getBatchSeparator() {
		return this.batchSeparator;
	}

	int numberCorpDigits;

	@Value("${client.cmpdreg.serverSettings.numberCorpDigits}")
	public void setNumberCorpDigits(int numberCorpDigits) {
		this.numberCorpDigits = numberCorpDigits;
	}

	@Override
	public Integer getNumberCorpDigits() {
		return this.numberCorpDigits;
	}

	Integer formatBatchDigits;

	@Value("${client.cmpdreg.serverSettings.formatBatchDigits}")
	public void setFormatBatchDigits(Integer formatBatchDigits) {
		this.formatBatchDigits = formatBatchDigits;
	}

	@Override
	public Integer getFormatBatchDigits() {
		return this.formatBatchDigits;
	}

	Integer startingCorpNumber;

	@Value("${client.cmpdreg.serverSettings.startingCorpNumber}")
	public void setStartingCorpNumber(Integer startingCorpNumber) {
		this.startingCorpNumber = startingCorpNumber;
	}

	@Override
	public Integer getStartingCorpNumber() {
		return this.startingCorpNumber;
	}

	Boolean fancyCorpNumberFormat;

	@Value("${client.cmpdreg.serverSettings.fancyCorpNumberFormat}")
	public void setFancyCorpNumberFormat(String fancyCorpNumberFormat) {
		if (fancyCorpNumberFormat.startsWith("${")) {
			fancyCorpNumberFormat = null;
		} else {
			this.fancyCorpNumberFormat = Boolean.parseBoolean(fancyCorpNumberFormat);
		}
		;
	}

	@Override
	public Boolean getFancyCorpNumberFormat() {
		return this.fancyCorpNumberFormat;
	}

	String corpParentFormat;

	@Value("${client.cmpdreg.serverSettings.corpParentFormat}")
	public void setCorpParentFormat(String corpParentFormat) {
		this.corpParentFormat = corpParentFormat;
		if (this.corpParentFormat.startsWith("${")) {
			corpParentFormat = null;
		} else {
			this.corpParentFormat = corpParentFormat;
		}
		;
	}

	@Override
	public String getCorpParentFormat() {
		return this.corpParentFormat;
	}

	String corpBatchFormat;

	@Value("${client.cmpdreg.serverSettings.corpBatchFormat}")
	public void setCorpBatchFormat(String corpBatchFormat) {
		this.corpBatchFormat = corpBatchFormat;
		if (this.corpBatchFormat.startsWith("${")) {
			corpBatchFormat = null;
		} else {
			this.corpBatchFormat = corpBatchFormat;
		}
		;
	}

	@Override
	public String getCorpBatchFormat() {
		return this.corpBatchFormat;
	}

	Boolean appendSaltCodeToLotName;

	@Value("${client.cmpdreg.serverSettings.appendSaltCodeToLotName}")
	public void setAppendSaltCodeToLotName(String appendSaltCodeToLotName) {
		if (appendSaltCodeToLotName.startsWith("${")) {
			appendSaltCodeToLotName = null;
		} else {
			this.appendSaltCodeToLotName = Boolean.parseBoolean(appendSaltCodeToLotName);
		}
		;
	}

	@Override
	public Boolean getAppendSaltCodeToLotName() {
		return this.appendSaltCodeToLotName;
	}

	Boolean saltBeforeLot;

	@Value("${client.cmpdreg.metaLot.saltBeforeLot}")
	public void setSaltBeforeLot(String saltBeforeLot) {
		if (saltBeforeLot.startsWith("${")) {
			saltBeforeLot = null;
		} else {
			this.saltBeforeLot = Boolean.parseBoolean(saltBeforeLot);
		}
		;
	}

	@Override
	public Boolean getSaltBeforeLot() {
		return this.saltBeforeLot;
	}

	String noSaltCode;

	@Value("${client.cmpdreg.serverSettings.noSaltCode}")
	public void setNoSaltCode(String noSaltCode) {
		this.noSaltCode = noSaltCode;
		if (this.noSaltCode.startsWith("${")) {
			noSaltCode = null;
		} else {
			this.noSaltCode = noSaltCode;
		}
		;
	}

	@Override
	public String getNoSaltCode() {
		return this.noSaltCode;
	}

	Boolean usePredefinedList;

	@Value("${client.cmpdreg.serverSettings.usePredefinedList}")
	public void setUsePredefinedList(String usePredefinedList) {
		if (usePredefinedList.startsWith("${")) {
			usePredefinedList = null;
		} else {
			this.usePredefinedList = Boolean.parseBoolean(usePredefinedList);
		}
		;
	}

	@Override
	public Boolean getUsePredefinedList() {
		return this.usePredefinedList;
	}

	Boolean uniqueNotebook;

	@Value("${client.cmpdreg.serverSettings.uniqueNotebook}")
	public void setUniqueNotebook(String uniqueNotebook) {
		if (uniqueNotebook.startsWith("${")) {
			uniqueNotebook = null;
		} else {
			this.uniqueNotebook = Boolean.parseBoolean(uniqueNotebook);
		}
		;
	}

	@Override
	public Boolean getUniqueNotebook() {
		return this.uniqueNotebook;
	}

	String notebookSavePath;

	@Value("${client.cmpdreg.serverSettings.notebookSavePath}")
	public void setNotebookSavePath(String notebookSavePath) {
		this.notebookSavePath = notebookSavePath;
		if (this.notebookSavePath.startsWith("${")) {
			notebookSavePath = null;
		} else {
			this.notebookSavePath = notebookSavePath;
		}
		;
	}

	@Override
	public String getNotebookSavePath() {
		return this.notebookSavePath;
	}

	SearchType exactMatchDef;

	@Value("${client.cmpdreg.serverSettings.exactMatchDef}")
	public void setExactMatchDef(String exactMatchDef) {
		this.exactMatchDef = SearchType.getIfPresent(exactMatchDef).orElse(SearchType.DUPLICATE_TAUTOMER);
	}

	@Override
	public SearchType getExactMatchDef() {
		return this.exactMatchDef;
	}

	Integer maxSearchTime;

	@Value("${client.cmpdreg.serverSettings.maxSearchTime}")
	public void setMaxSearchTime(Integer maxSearchTime) {
		this.maxSearchTime = maxSearchTime;
	}

	@Override
	public Integer getMaxSearchTime() {
		return this.maxSearchTime;
	}

	Integer maxSearchResults;

	@Value("${client.cmpdreg.serverSettings.maxSearchResults}")
	public void setMaxSearchResults(Integer maxSearchResults) {
		this.maxSearchResults = maxSearchResults;
	}

	@Override
	public Integer getMaxSearchResults() {
		return this.maxSearchResults;
	}

	Boolean projectRestrictions;

	@Value("${client.cmpdreg.serverSettings.projectRestrictions}")
	public void setProjectRestrictions(String projectRestrictions) {
		if (projectRestrictions.startsWith("${")) {
			projectRestrictions = null;
		} else {
			this.projectRestrictions = Boolean.parseBoolean(projectRestrictions);
		}
		;
	}

	@Override
	public Boolean getProjectRestrictions() {
		return this.projectRestrictions;
	}

	Boolean compoundInventory;

	@Value("${client.cmpdreg.serverSettings.compoundInventory}")
	public void setCompoundInventory(String compoundInventory) {
		if (compoundInventory.startsWith("${")) {
			compoundInventory = null;
		} else {
			this.compoundInventory = Boolean.parseBoolean(compoundInventory);
		}
		;
	}

	@Override
	public Boolean getCompoundInventory() {
		return this.compoundInventory;
	}

	Boolean disableTubeCreationIfNoBarcode;

	@Value("${client.cmpdreg.serverSettings.disableTubeCreationIfNoBarcode}")
	public void setDisableTubeCreationIfNoBarcode(String disableTubeCreationIfNoBarcode) {
		if (disableTubeCreationIfNoBarcode.startsWith("${")) {
			disableTubeCreationIfNoBarcode = null;
		} else {
			this.disableTubeCreationIfNoBarcode = Boolean.parseBoolean(disableTubeCreationIfNoBarcode);
		}
		;
	}

	@Override
	public Boolean getDisableTubeCreationIfNoBarcode() {
		return this.disableTubeCreationIfNoBarcode;
	}

	Boolean checkACASDependenciesByContainerCode;

	@Value("${client.cmpdreg.serverSettings.checkACASDependenciesByContainerCode}")
	public void setCheckACASDependenciesByContainerCode(String checkACASDependenciesByContainerCode) {
		if (checkACASDependenciesByContainerCode.startsWith("${")) {
			checkACASDependenciesByContainerCode = null;
		} else {
			this.checkACASDependenciesByContainerCode = Boolean.parseBoolean(checkACASDependenciesByContainerCode);
		}
		;
	}

	@Override
	public Boolean getCheckACASDependenciesByContainerCode() {
		return this.checkACASDependenciesByContainerCode;
	}

	Boolean useExternalStandardizerConfig;

	@Value("${client.cmpdreg.serverSettings.useExternalStandardizerConfig}")
	public void setUseExternalStandardizerConfig(String useExternalStandardizerConfig) {
		if (useExternalStandardizerConfig.startsWith("${")) {
			useExternalStandardizerConfig = null;
		} else {
			this.useExternalStandardizerConfig = Boolean.parseBoolean(useExternalStandardizerConfig);
		}
		;
	}

	@Override
	public Boolean getUseExternalStandardizerConfig() {
		return this.useExternalStandardizerConfig;
	}

	String standardizerConfigFilePath;

	@Value("${client.cmpdreg.serverSettings.standardizerConfigFilePath}")
	public void setStandardizerConfigFilePath(String standardizerConfigFilePath) {
		this.standardizerConfigFilePath = standardizerConfigFilePath;
		if (this.standardizerConfigFilePath.startsWith("${")) {
			standardizerConfigFilePath = null;
		} else {
			this.standardizerConfigFilePath = standardizerConfigFilePath;
		}
		;
	}

	@Override
	public String getStandardizerConfigFilePath() {
		return this.standardizerConfigFilePath;
	}

	Boolean orderSelectLists;

	@Value("${client.cmpdreg.serverSettings.orderSelectLists}")
	public void setOrderSelectLists(String orderSelectLists) {
		if (orderSelectLists.startsWith("${")) {
			orderSelectLists = null;
		} else {
			this.orderSelectLists = Boolean.parseBoolean(orderSelectLists);
		}
		;
	}

	@Override
	public Boolean getOrderSelectLists() {
		return this.orderSelectLists;
	}

	Boolean registerNoStructureCompoundsAsUniqueParents;

	@Value("${client.cmpdreg.serverSettings.registerNoStructureCompoundsAsUniqueParents}")
	public void setRegisterNoStructureCompoundsAsUniqueParents(String registerNoStructureCompoundsAsUniqueParents) {
		if (registerNoStructureCompoundsAsUniqueParents.startsWith("${")) {
			registerNoStructureCompoundsAsUniqueParents = null;
		} else {
			this.registerNoStructureCompoundsAsUniqueParents = Boolean
					.parseBoolean(registerNoStructureCompoundsAsUniqueParents);
		}
		;
	}

	@Override
	public Boolean getRegisterNoStructureCompoundsAsUniqueParents() {
		return this.registerNoStructureCompoundsAsUniqueParents;
	}

	Boolean useProjectRoles;

	@Value("${client.cmpdreg.bulkLoadSettings.useProjectRoles}")
	public void setUseProjectRoles(String useProjectRoles) {
		if (useProjectRoles.startsWith("${")) {
			useProjectRoles = null;
		} else {
			this.useProjectRoles = Boolean.parseBoolean(useProjectRoles);
		}
		;
	}

	@Override
	public Boolean getUseProjectRoles() {
		return this.useProjectRoles;
	}

	Collection<SimpleBulkLoadPropertyDTO> dbProperties;

	@Value("${client.cmpdreg.bulkLoadSettings.dbProperties}")
	public void setDbProperties(String dbProperties) {
		this.dbProperties = SimpleBulkLoadPropertyDTO.fromJsonArrayToSimpleBulkLoadProes(dbProperties);
	}

	@Override
	public Collection<SimpleBulkLoadPropertyDTO> getDbProperties() {
		return this.dbProperties;
	}

	String preprocessorSettings;

	@Value("${client.cmpdreg.serverSettings.liveDesign.preprocessorSettings}")
	public void setPreprocessorSettings(String preprocessorSettings) {
		this.preprocessorSettings = preprocessorSettings;
	}

	@Override
	public String getPreprocessorSettings() {
		return this.preprocessorSettings;
	}

	int standardizationBatchSize;

	@Value("${server.acas.standardizationBatchSize}")
	public void setStandardizationBatchSize(String standardizationBatchSize) {
		if (standardizationBatchSize.startsWith("${")) {
			this.standardizationBatchSize = 1000;
		} else {
			this.standardizationBatchSize = Integer.parseInt(standardizationBatchSize);
		}
		;
	}

	@Override
	public Integer getStandardizationBatchSize() {
		return this.standardizationBatchSize;
	}

	int externalStructureProcessingBatchSize;

	@Value("${server.acas.externalStructureProcessingBatchSize}")
	public void setExternalStructureProcessingBatchSize(String externalStructureProcessingBatchSize) {
		if (externalStructureProcessingBatchSize.startsWith("${")) {
			this.externalStructureProcessingBatchSize = 100;
		} else {
			this.externalStructureProcessingBatchSize = Integer.parseInt(externalStructureProcessingBatchSize);
		}
		;
	}

	@Override
	public Integer getExternalStructureProcessingBatchSize() {
		return this.externalStructureProcessingBatchSize;
	}

	Boolean allowDuplicateParentAliases;

	@Value("${server.service.external.preferred.batchid.allowParentAliasLotNames}")
	public void setAllowDuplicateParentAliases(String allowDuplicateParentAliases) {
		if (allowDuplicateParentAliases.startsWith("${")) {
			this.allowDuplicateParentAliases = true;
		} else {
			this.allowDuplicateParentAliases = Boolean.parseBoolean(allowDuplicateParentAliases);
		}
	}

	@Override
	public Boolean getAllowDuplicateParentAliases() {
		return this.allowDuplicateParentAliases;
	}

}