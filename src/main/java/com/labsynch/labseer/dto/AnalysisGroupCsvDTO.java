package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;

public class AnalysisGroupCsvDTO {

	
	private static final Logger logger = LoggerFactory.getLogger(AnalysisGroupCsvDTO.class);

	public AnalysisGroupCsvDTO(){
		//empty constructor
	}

	//	valueType	valueKind	numericValue	stringValue	dateValue	clobValue	urlValue	fileValue	codeValue	valueUnit	valueOperator	publicData	stateType	stateKind	stateID	analysisGroupID	experimentID	experimentVersion	lsTransaction	recordedBy	codeName
	//	stringValue	Rendering Hint	NULL	4 parameter D-R	NULL	NULL	NULL	NULL	NULL	NULL	NULL	FALSE	data	results	1	1	1007	1	86	smeyer	AG-1
	//	stringValue	Rendering Hint	NULL	4 parameter D-R	NULL	NULL	NULL	NULL	NULL	NULL	NULL	FALSE	data	results	5	2	1007	1	86	smeyer	AG-2
	//	valueType,valueKind,numericValue,stringValue,dateValue,clobValue,urlValue,fileValue,codeType,codeKind,codeValue,unitKind,unitType,operatorKind,operatorType,publicData,stateType,stateKind,tempStateId,stateId,id,tempId,parentId,tempParentId,lsTransaction,recordedBy,codeName,lsType,lsKind

	private Long experimentID;
	private String experimentCodeName;
	private Integer experimentVersion;

	private String analysisGroupID;	
	private Long id;	
	private Integer version;
	private String lsType;
	private String lsKind;
	private String codeName;

	private Long stateID;	
	private String stateType;
	private String stateKind;

	private Long valueId;	
	private String valueType;
	private String valueKind;
	private String codeOrigin;
	private String codeType;
	private String codeKind;
	private String stringValue;
	protected String codeValue;
	private String fileValue;
	private String urlValue;
	private Date dateValue;
	private String clobValue;
	private String valueOperator;
	private BigDecimal numericValue;
	private Integer sigFigs;
	private BigDecimal uncertainty;
	private Integer numberOfReplicates;
	private String uncertaintyType;
	private String valueUnit;
	private Double concentration;
	private String concUnit;
	private String comments;
	private boolean ignored;
	private Long lsTransaction;
	private Date recordedDate;
	private String recordedBy;
	private Date modifiedDate;
	private String modifiedBy;
	private boolean publicData;

	public void setLsTransaction(String lsTransaction) {
		if (lsTransaction.equalsIgnoreCase("NULL")){
			this.lsTransaction = null;
		} else {
		this.lsTransaction = Long.valueOf(lsTransaction);
	}
		}

	public void setExperimentVersion(String experimentVersion) {
		if (experimentVersion.equalsIgnoreCase("NULL")){
			this.experimentVersion = null;
		} else {
		this.experimentVersion = Integer.valueOf(experimentVersion);
		}
	}

	public void setExperimentID(String experimentID) {
		if (experimentID.equalsIgnoreCase("NULL")){
			this.experimentID = null;
		} else {
			this.experimentID = Long.valueOf(experimentID);
		}
	}

	public void setStateID(String stateID) {
		this.stateID = Long.valueOf(stateID);
	}

	public void setPublicData(String publicData) {
		if (publicData.equalsIgnoreCase("FALSE")){
			this.publicData = false;
		} else {
			this.publicData = true;
		}
	}

	public void setDateValue(String dateValue) throws ParseException {
		if (dateValue.equalsIgnoreCase("NULL")){
			this.dateValue = null;
		} else {
			DateFormat df = new SimpleDateFormat("mm/dd/yyyy");
			this.dateValue = df.parse(dateValue);
		}
	}

	public void setNumericValue(String numericValue) {
		if (numericValue.equalsIgnoreCase("NULL")){
			this.numericValue = null;
		} else {
			this.numericValue = new BigDecimal(numericValue);
		}
	}


	public static String[] getColumns(){
		String[] headerColumns = new String[] {
				"id", 
				"protocolId",
				"experimentId",
				"experimentCodeName",
				"experimentName",
				"lsType",
				"lsKind",
				"testedLot",
				"result"};

		return headerColumns;

	}

	public static CellProcessor[] getProcessors() {
		final CellProcessor[] processors = new CellProcessor[] { 
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),

				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),
				new Optional(),

				new Optional()
		};

		return processors;
	}



	public Long getExperimentID() {
        return this.experimentID;
    }

	public void setExperimentID(Long experimentID) {
        this.experimentID = experimentID;
    }

	public String getExperimentCodeName() {
        return this.experimentCodeName;
    }

	public void setExperimentCodeName(String experimentCodeName) {
        this.experimentCodeName = experimentCodeName;
    }

	public Integer getExperimentVersion() {
        return this.experimentVersion;
    }

	public void setExperimentVersion(Integer experimentVersion) {
        this.experimentVersion = experimentVersion;
    }

	public String getAnalysisGroupID() {
        return this.analysisGroupID;
    }

	public void setAnalysisGroupID(String analysisGroupID) {
        this.analysisGroupID = analysisGroupID;
    }

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	public String getLsType() {
        return this.lsType;
    }

	public void setLsType(String lsType) {
        this.lsType = lsType;
    }

	public String getLsKind() {
        return this.lsKind;
    }

	public void setLsKind(String lsKind) {
        this.lsKind = lsKind;
    }

	public String getCodeName() {
        return this.codeName;
    }

	public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

	public Long getStateID() {
        return this.stateID;
    }

	public void setStateID(Long stateID) {
        this.stateID = stateID;
    }

	public String getStateType() {
        return this.stateType;
    }

	public void setStateType(String stateType) {
        this.stateType = stateType;
    }

	public String getStateKind() {
        return this.stateKind;
    }

	public void setStateKind(String stateKind) {
        this.stateKind = stateKind;
    }

	public Long getValueId() {
        return this.valueId;
    }

	public void setValueId(Long valueId) {
        this.valueId = valueId;
    }

	public String getValueType() {
        return this.valueType;
    }

	public void setValueType(String valueType) {
        this.valueType = valueType;
    }

	public String getValueKind() {
        return this.valueKind;
    }

	public void setValueKind(String valueKind) {
        this.valueKind = valueKind;
    }

	public String getCodeOrigin() {
        return this.codeOrigin;
    }

	public void setCodeOrigin(String codeOrigin) {
        this.codeOrigin = codeOrigin;
    }

	public String getCodeType() {
        return this.codeType;
    }

	public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

	public String getCodeKind() {
        return this.codeKind;
    }

	public void setCodeKind(String codeKind) {
        this.codeKind = codeKind;
    }

	public String getStringValue() {
        return this.stringValue;
    }

	public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

	public String getCodeValue() {
        return this.codeValue;
    }

	public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

	public String getFileValue() {
        return this.fileValue;
    }

	public void setFileValue(String fileValue) {
        this.fileValue = fileValue;
    }

	public String getUrlValue() {
        return this.urlValue;
    }

	public void setUrlValue(String urlValue) {
        this.urlValue = urlValue;
    }

	public Date getDateValue() {
        return this.dateValue;
    }

	public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

	public String getClobValue() {
        return this.clobValue;
    }

	public void setClobValue(String clobValue) {
        this.clobValue = clobValue;
    }

	public String getValueOperator() {
        return this.valueOperator;
    }

	public void setValueOperator(String valueOperator) {
        this.valueOperator = valueOperator;
    }

	public BigDecimal getNumericValue() {
        return this.numericValue;
    }

	public void setNumericValue(BigDecimal numericValue) {
        this.numericValue = numericValue;
    }

	public Integer getSigFigs() {
        return this.sigFigs;
    }

	public void setSigFigs(Integer sigFigs) {
        this.sigFigs = sigFigs;
    }

	public BigDecimal getUncertainty() {
        return this.uncertainty;
    }

	public void setUncertainty(BigDecimal uncertainty) {
        this.uncertainty = uncertainty;
    }

	public Integer getNumberOfReplicates() {
        return this.numberOfReplicates;
    }

	public void setNumberOfReplicates(Integer numberOfReplicates) {
        this.numberOfReplicates = numberOfReplicates;
    }

	public String getUncertaintyType() {
        return this.uncertaintyType;
    }

	public void setUncertaintyType(String uncertaintyType) {
        this.uncertaintyType = uncertaintyType;
    }

	public String getValueUnit() {
        return this.valueUnit;
    }

	public void setValueUnit(String valueUnit) {
        this.valueUnit = valueUnit;
    }

	public Double getConcentration() {
        return this.concentration;
    }

	public void setConcentration(Double concentration) {
        this.concentration = concentration;
    }

	public String getConcUnit() {
        return this.concUnit;
    }

	public void setConcUnit(String concUnit) {
        this.concUnit = concUnit;
    }

	public String getComments() {
        return this.comments;
    }

	public void setComments(String comments) {
        this.comments = comments;
    }

	public boolean isIgnored() {
        return this.ignored;
    }

	public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

	public Long getLsTransaction() {
        return this.lsTransaction;
    }

	public void setLsTransaction(Long lsTransaction) {
        this.lsTransaction = lsTransaction;
    }

	public Date getRecordedDate() {
        return this.recordedDate;
    }

	public void setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }

	public String getRecordedBy() {
        return this.recordedBy;
    }

	public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

	public Date getModifiedDate() {
        return this.modifiedDate;
    }

	public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

	public String getModifiedBy() {
        return this.modifiedBy;
    }

	public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

	public boolean isPublicData() {
        return this.publicData;
    }

	public void setPublicData(boolean publicData) {
        this.publicData = publicData;
    }

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

	public static AnalysisGroupCsvDTO fromJsonToAnalysisGroupCsvDTO(String json) {
        return new JSONDeserializer<AnalysisGroupCsvDTO>()
        .use(null, AnalysisGroupCsvDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<AnalysisGroupCsvDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<AnalysisGroupCsvDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<AnalysisGroupCsvDTO> fromJsonArrayToAnalysisGroes(String json) {
        return new JSONDeserializer<List<AnalysisGroupCsvDTO>>()
        .use("values", AnalysisGroupCsvDTO.class).deserialize(json);
    }
}


