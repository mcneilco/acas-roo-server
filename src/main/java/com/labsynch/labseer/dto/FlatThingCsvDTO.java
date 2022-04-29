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


public class FlatThingCsvDTO {

	private static final Logger logger = LoggerFactory.getLogger(FlatThingCsvDTO.class);

	public FlatThingCsvDTO(){
		//empty constructor
	}


	//	valueType	valueKind	numericValue	stringValue	dateValue	clobValue	urlValue	fileValue	codeValue	valueUnit	valueOperator	publicData	stateType	stateKind	stateID	analysisGroupID	experimentID	experimentVersion	lsTransaction	recordedBy	codeName
	//	stringValue	Rendering Hint	NULL	4 parameter D-R	NULL	NULL	NULL	NULL	NULL	NULL	NULL	FALSE	data	results	1	1	1007	1	86	smeyer	AG-1
	//	stringValue	Rendering Hint	NULL	4 parameter D-R	NULL	NULL	NULL	NULL	NULL	NULL	NULL	FALSE	data	results	5	2	1007	1	86	smeyer	AG-2
	//	valueType,valueKind,numericValue,stringValue,dateValue,clobValue,urlValue,
	//	fileValue,codeType,codeKind,codeValue,unitKind,unitType,operatorKind,operatorType,
	//	publicData,stateType,stateKind,tempStateId,stateId,id,tempId,parentId,tempParentId,
	//	lsTransaction,recordedBy,codeName,lsType,lsKind


	private Long parentId;
	private String tempParentId;

	private Long id;	
	private String tempId;	
	private String codeName;
	private String lsType;
	private String lsKind;

	private Long stateId;	
	private String tempStateId;	
	private String stateType;
	private String stateKind;

	private Long valueId;
	private String tempValueId;	
	private String valueType;
	private String valueKind;
	private String codeOrigin;
	private String codeType;
	private String codeKind;
	protected String codeValue;
	private String stringValue;
	private String fileValue;
	private String urlValue;
	private Date dateValue;
	private String clobValue;
	private String operatorType;
	private String operatorKind;
	private BigDecimal numericValue;
	private Integer sigFigs;
	private BigDecimal uncertainty;
	private Integer numberOfReplicates;
	private String uncertaintyType;
	private String unitType;
	private String unitKind;
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

	public void setCodeName(String codeName) {
		if (codeName.equalsIgnoreCase("NULL")){
			this.codeName = null;
		} else {
			this.codeName = codeName;
		}
	}
	
	public void setParentId(String parentId) {
		if (parentId.equalsIgnoreCase("NULL")){
			this.parentId = null;
		} else {
			this.parentId = Long.valueOf(parentId);
		}
	}

	public void setStateId(String stateId) {
		this.stateId = Long.valueOf(stateId);
	}

	public void setPublicData(String publicData) {
		if (publicData.equalsIgnoreCase("FALSE")){
			this.publicData = false;
		} else {
			this.publicData = true;
		}
	}
	
	public void setIgnored(String ignored) {
		if (ignored.equalsIgnoreCase("FALSE")){
			this.ignored = false;
		} else {
			this.ignored = true;
		}
	}

	public void setDateValue(String dateValue) throws ParseException {
		if (dateValue.equalsIgnoreCase("NULL")){
			this.dateValue = null;
		} else {
			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				this.dateValue = df.parse(dateValue);
			} catch(Exception e) {
				try {
					this.dateValue = new Date(Long.parseLong(dateValue));
				} catch(Exception e2) {
					DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
					this.dateValue = df.parse(dateValue);
				}
				
			}
		}
	}

	public void setNumericValue(String numericValue) {
		if (numericValue.equalsIgnoreCase("NULL")){
			this.numericValue = null;
		} else {
			this.numericValue = new BigDecimal(numericValue);
		}
	}

	public void setUncertainty(String uncertainty) {
		if (uncertainty.equalsIgnoreCase("NULL")){
			this.uncertainty = null;
		} else {
			this.uncertainty = new BigDecimal(uncertainty);
		}
	}
	
	public void setConcentration(String concentration) {
		if (concentration.equalsIgnoreCase("NULL")){
			this.concentration = null;
		} else {
			this.concentration = new Double(concentration);
		}
	}
	
	public void setId(String id) {
		if (id.equalsIgnoreCase("NULL")){
			this.id = null;
		} else {
			this.id = Long.valueOf(id);
		}
	}
	
	public void setValueId(String valueId) {
		if (valueId.equalsIgnoreCase("NULL")){
			this.valueId = null;
		} else {
			this.valueId = Long.valueOf(valueId);
		}
	}
	
	public void setSigFigs(String sigFigs) {
		if (sigFigs.equalsIgnoreCase("NULL")){
			this.sigFigs = null;
		} else {
			this.sigFigs = Integer.valueOf(sigFigs);
		}
	}

	public void setNumberOfReplicates(String numberOfReplicates) {
		if (numberOfReplicates.equalsIgnoreCase("NULL")){
			this.numberOfReplicates = null;
		} else {
			this.numberOfReplicates = Integer.valueOf(numberOfReplicates);
		}
	}
	
	
	public static String[] getColumns(){
		String[] headerColumns = new String[] {
				"parentId", 
				"tempParentId",
				"id",
				"tempId",
				"codeName",
				"lsType",
				"lsKind",
				"stateId",
				"tempStateId",
				"stateType",
				
				"stateKind",
				"tempValueId",
				"valueType",
				"valueKind",
				"codeOrigin",
				"codeType",
				"codeKind",
				"codeValue",
				"stringValue",
				"fileValue",
				"urlValue",
				
				"dateValue",
				"clobValue",
				"operatorType",
				"operatorKind",
				"numericValue",
				"sigFigs",
				"uncertainty",
				"numberOfReplicates",
				"uncertaintyType",
				"unitType",
				
				"unitKind",
				"concentration",
				"concUnit",
				"comments",
				//				"ignored",
				"lsTransaction",
				//				"recordedDate",
				"recordedBy",
				//				"modifiedDate",
				//				"modifiedBy",
				"publicData"
				// 37 values
		};

		//		valueType,valueKind,numericValue,stringValue,dateValue,clobValue,urlValue,
		//		fileValue,codeType,codeKind,codeValue,unitKind,unitType,operatorKind,operatorType,
		//		publicData,stateType,stateKind,tempStateId,stateId,id,tempId,parentId,tempParentId,
		//		lsTransaction,recordedBy,codeName,lsType,lsKind

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



	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static FlatThingCsvDTO fromJsonToFlatThingCsvDTO(String json) {
        return new JSONDeserializer<FlatThingCsvDTO>()
        .use(null, FlatThingCsvDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<FlatThingCsvDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<FlatThingCsvDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<FlatThingCsvDTO> fromJsonArrayToFlatThingCsvDTO(String json) {
        return new JSONDeserializer<List<FlatThingCsvDTO>>()
        .use("values", FlatThingCsvDTO.class).deserialize(json);
    }

	public Long getParentId() {
        return this.parentId;
    }

	public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

	public String getTempParentId() {
        return this.tempParentId;
    }

	public void setTempParentId(String tempParentId) {
        this.tempParentId = tempParentId;
    }

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public String getTempId() {
        return this.tempId;
    }

	public void setTempId(String tempId) {
        this.tempId = tempId;
    }

	public String getCodeName() {
        return this.codeName;
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

	public Long getStateId() {
        return this.stateId;
    }

	public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

	public String getTempStateId() {
        return this.tempStateId;
    }

	public void setTempStateId(String tempStateId) {
        this.tempStateId = tempStateId;
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

	public String getTempValueId() {
        return this.tempValueId;
    }

	public void setTempValueId(String tempValueId) {
        this.tempValueId = tempValueId;
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

	public String getCodeValue() {
        return this.codeValue;
    }

	public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

	public String getStringValue() {
        return this.stringValue;
    }

	public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
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

	public String getOperatorType() {
        return this.operatorType;
    }

	public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

	public String getOperatorKind() {
        return this.operatorKind;
    }

	public void setOperatorKind(String operatorKind) {
        this.operatorKind = operatorKind;
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

	public String getUnitType() {
        return this.unitType;
    }

	public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

	public String getUnitKind() {
        return this.unitKind;
    }

	public void setUnitKind(String unitKind) {
        this.unitKind = unitKind;
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
}


