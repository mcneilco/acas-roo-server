package com.labsynch.labseer.dto.configuration;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class ServerSettingsConfigDTO {

    private String corpPrefix;

    private String corpSeparator;
    
    private int numberCorpDigits;
    
    private int startingCorpNumber;
        
    private String batchSeparator;
    
    private String saltSeparator;

    private String notebookSavePath;
    
    private String exactMatchDef;
    
    private long maxSearchTime;

    private int maxSearchResults;
    
    private int formatBatchDigits;
    
    private boolean fancyCorpNumberFormat;
    
    private String corpParentFormat;

    private String corpBatchFormat;
    
    private boolean appendSaltCodeToLotName;
    
    private String noSaltCode;
    
    private boolean uniqueNotebook;

    private boolean unitTestDB;

    private boolean initalDBLoad;
    
    private boolean projectRestrictions;
    
    private boolean compoundInventory;
    
    private boolean disableTubeCreationIfNoBarcode;

    private String jchemVersion;
    
    private String databaseType;
    
    private boolean standardizeStructure;

    private boolean useExternalStandardizerConfig;
    
    private String standardizerConfigFilePath;

    private boolean usePredefinedList;
    
    private boolean newUserIsChemist;
    
    private boolean orderSelectLists;
    
    private boolean checkACASDependenciesByContainerCode;
    
    private boolean registerNoStructureCompoundsAsUniqueParents;


	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static ServerSettingsConfigDTO fromJsonToServerSettingsConfigDTO(String json) {
        return new JSONDeserializer<ServerSettingsConfigDTO>()
        .use(null, ServerSettingsConfigDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ServerSettingsConfigDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ServerSettingsConfigDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ServerSettingsConfigDTO> fromJsonArrayToServerSettingsCoes(String json) {
        return new JSONDeserializer<List<ServerSettingsConfigDTO>>()
        .use("values", ServerSettingsConfigDTO.class).deserialize(json);
    }

	public String getCorpPrefix() {
        return this.corpPrefix;
    }

	public void setCorpPrefix(String corpPrefix) {
        this.corpPrefix = corpPrefix;
    }

	public String getCorpSeparator() {
        return this.corpSeparator;
    }

	public void setCorpSeparator(String corpSeparator) {
        this.corpSeparator = corpSeparator;
    }

	public int getNumberCorpDigits() {
        return this.numberCorpDigits;
    }

	public void setNumberCorpDigits(int numberCorpDigits) {
        this.numberCorpDigits = numberCorpDigits;
    }

	public int getStartingCorpNumber() {
        return this.startingCorpNumber;
    }

	public void setStartingCorpNumber(int startingCorpNumber) {
        this.startingCorpNumber = startingCorpNumber;
    }

	public String getBatchSeparator() {
        return this.batchSeparator;
    }

	public void setBatchSeparator(String batchSeparator) {
        this.batchSeparator = batchSeparator;
    }

	public String getSaltSeparator() {
        return this.saltSeparator;
    }

	public void setSaltSeparator(String saltSeparator) {
        this.saltSeparator = saltSeparator;
    }

	public String getNotebookSavePath() {
        return this.notebookSavePath;
    }

	public void setNotebookSavePath(String notebookSavePath) {
        this.notebookSavePath = notebookSavePath;
    }

	public String getExactMatchDef() {
        return this.exactMatchDef;
    }

	public void setExactMatchDef(String exactMatchDef) {
        this.exactMatchDef = exactMatchDef;
    }

	public long getMaxSearchTime() {
        return this.maxSearchTime;
    }

	public void setMaxSearchTime(long maxSearchTime) {
        this.maxSearchTime = maxSearchTime;
    }

	public int getMaxSearchResults() {
        return this.maxSearchResults;
    }

	public void setMaxSearchResults(int maxSearchResults) {
        this.maxSearchResults = maxSearchResults;
    }

	public int getFormatBatchDigits() {
        return this.formatBatchDigits;
    }

	public void setFormatBatchDigits(int formatBatchDigits) {
        this.formatBatchDigits = formatBatchDigits;
    }

	public boolean isFancyCorpNumberFormat() {
        return this.fancyCorpNumberFormat;
    }

	public void setFancyCorpNumberFormat(boolean fancyCorpNumberFormat) {
        this.fancyCorpNumberFormat = fancyCorpNumberFormat;
    }

	public String getCorpParentFormat() {
        return this.corpParentFormat;
    }

	public void setCorpParentFormat(String corpParentFormat) {
        this.corpParentFormat = corpParentFormat;
    }

	public String getCorpBatchFormat() {
        return this.corpBatchFormat;
    }

	public void setCorpBatchFormat(String corpBatchFormat) {
        this.corpBatchFormat = corpBatchFormat;
    }

	public boolean isAppendSaltCodeToLotName() {
        return this.appendSaltCodeToLotName;
    }

	public void setAppendSaltCodeToLotName(boolean appendSaltCodeToLotName) {
        this.appendSaltCodeToLotName = appendSaltCodeToLotName;
    }

	public String getNoSaltCode() {
        return this.noSaltCode;
    }

	public void setNoSaltCode(String noSaltCode) {
        this.noSaltCode = noSaltCode;
    }

	public boolean isUniqueNotebook() {
        return this.uniqueNotebook;
    }

	public void setUniqueNotebook(boolean uniqueNotebook) {
        this.uniqueNotebook = uniqueNotebook;
    }

	public boolean isUnitTestDB() {
        return this.unitTestDB;
    }

	public void setUnitTestDB(boolean unitTestDB) {
        this.unitTestDB = unitTestDB;
    }

	public boolean isInitalDBLoad() {
        return this.initalDBLoad;
    }

	public void setInitalDBLoad(boolean initalDBLoad) {
        this.initalDBLoad = initalDBLoad;
    }

	public boolean isProjectRestrictions() {
        return this.projectRestrictions;
    }

	public void setProjectRestrictions(boolean projectRestrictions) {
        this.projectRestrictions = projectRestrictions;
    }

	public boolean isCompoundInventory() {
        return this.compoundInventory;
    }

	public void setCompoundInventory(boolean compoundInventory) {
        this.compoundInventory = compoundInventory;
    }

	public boolean isDisableTubeCreationIfNoBarcode() {
        return this.disableTubeCreationIfNoBarcode;
    }

	public void setDisableTubeCreationIfNoBarcode(boolean disableTubeCreationIfNoBarcode) {
        this.disableTubeCreationIfNoBarcode = disableTubeCreationIfNoBarcode;
    }

	public String getJchemVersion() {
        return this.jchemVersion;
    }

	public void setJchemVersion(String jchemVersion) {
        this.jchemVersion = jchemVersion;
    }

	public String getDatabaseType() {
        return this.databaseType;
    }

	public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

	public boolean isStandardizeStructure() {
        return this.standardizeStructure;
    }

	public void setStandardizeStructure(boolean standardizeStructure) {
        this.standardizeStructure = standardizeStructure;
    }

	public boolean isUseExternalStandardizerConfig() {
        return this.useExternalStandardizerConfig;
    }

	public void setUseExternalStandardizerConfig(boolean useExternalStandardizerConfig) {
        this.useExternalStandardizerConfig = useExternalStandardizerConfig;
    }

	public String getStandardizerConfigFilePath() {
        return this.standardizerConfigFilePath;
    }

	public void setStandardizerConfigFilePath(String standardizerConfigFilePath) {
        this.standardizerConfigFilePath = standardizerConfigFilePath;
    }

	public boolean isUsePredefinedList() {
        return this.usePredefinedList;
    }

	public void setUsePredefinedList(boolean usePredefinedList) {
        this.usePredefinedList = usePredefinedList;
    }

	public boolean isNewUserIsChemist() {
        return this.newUserIsChemist;
    }

	public void setNewUserIsChemist(boolean newUserIsChemist) {
        this.newUserIsChemist = newUserIsChemist;
    }

	public boolean isOrderSelectLists() {
        return this.orderSelectLists;
    }

	public void setOrderSelectLists(boolean orderSelectLists) {
        this.orderSelectLists = orderSelectLists;
    }

	public boolean isCheckACASDependenciesByContainerCode() {
        return this.checkACASDependenciesByContainerCode;
    }

	public void setCheckACASDependenciesByContainerCode(boolean checkACASDependenciesByContainerCode) {
        this.checkACASDependenciesByContainerCode = checkACASDependenciesByContainerCode;
    }

	public boolean getRegisterNoStructureCompoundsAsUniqueParents() {
        return this.registerNoStructureCompoundsAsUniqueParents;
    }

	public void setRegisterNoStructureCompoundsAsUniqueParents(boolean registerNoStructureCompoundsAsUniqueParents) {
        this.registerNoStructureCompoundsAsUniqueParents = registerNoStructureCompoundsAsUniqueParents;
    }
}
