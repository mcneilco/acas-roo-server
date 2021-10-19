package com.labsynch.labseer.utils;

import java.util.Collection;

import com.labsynch.labseer.dto.SimpleBulkLoadPropertyDTO;

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

	boolean getRestrictExperiments();

	boolean getEnableProjectRoles();

	String getChemistryPackage();

	String getRootLocationLabel();

	String getTrashLocationLabel();

	String getBenchesLocationLabel();

	String getEmailFromAddress();

	String getAcasURL();

	String getAcasAppURL();
	
	Boolean getLotCalledBatch();
	
	Boolean getUseExactMass();
	
	String getCorpPrefix();
	
	String getCorpSeparator();
	
	String getSaltSeparator();
	
	String getBatchSeparator();
	
	Integer getNumberCorpDigits();
	
	Integer getFormatBatchDigits();
	
	Integer getStartingCorpNumber();
	
	Boolean getFancyCorpNumberFormat();
	
	String getCorpParentFormat();
	
	String getCorpBatchFormat();
	
	Boolean getAppendSaltCodeToLotName();

	Boolean getSaltBeforeLot();
	
	String getNoSaltCode();
	
	Boolean getUsePredefinedList();
	
	Boolean getUniqueNotebook();
	
	String getNotebookSavePath();
	
	String getExactMatchDef();
	
	Integer getMaxSearchTime();
	
	Integer getMaxSearchResults();
	
	Boolean getProjectRestrictions();
	
	Boolean getCompoundInventory();
	
	Boolean getDisableTubeCreationIfNoBarcode();
	
	Boolean getCheckACASDependenciesByContainerCode();
	
	Boolean getUseExternalStandardizerConfig();
	
	String getStandardizerConfigFilePath();
	
	Boolean getOrderSelectLists();
	
	Boolean getRegisterNoStructureCompoundsAsUniqueParents();
	
	Boolean getUseProjectRoles();
	
	Collection<SimpleBulkLoadPropertyDTO> getDbProperties();
	
}