package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class AnalysisGroupValueBaseDTO {

	private static final Logger logger = LoggerFactory.getLogger(AnalysisGroupValueBaseDTO.class);

	public AnalysisGroupValueBaseDTO(){
		//empty constructor
	}

	public AnalysisGroupValueBaseDTO(
			Long id, 
			Long stateId,
			String agCodeName,
			String lsType,
			String lsKind,
			 String stringValue,
			 String codeValue,
			 String fileValue,
			 String urlValue,
			 Date dateValue,
		     String clobValue,
			 String operatorType,
			 String operatorKind,
			 BigDecimal numericValue,
			 Integer sigFigs,
			 BigDecimal uncertainty,
			 Integer numberOfReplicates,
			 String uncertaintyType,
			 String unitType,
			 String unitKind,
			 String comments,
			 boolean ignored,
			 Long lsTransaction,
			 boolean publicData
			
			){

		this.id = id;
		this.stateId = stateId;
		this.agCodeName = agCodeName;
		this.lsType = lsType;
		this.lsKind = lsKind;
		this.numericValue = numericValue;
		this.stringValue = stringValue;
		this.codeValue = codeValue;		
		this.fileValue = fileValue;
		this.urlValue = urlValue;
		this.dateValue = dateValue;
	    this.clobValue = clobValue;
		this.operatorType = operatorType;
		this.operatorKind = operatorKind;
		this.sigFigs = sigFigs;
		this.uncertainty = uncertainty;
		this.numberOfReplicates = numberOfReplicates;
		this.uncertaintyType = uncertaintyType;
		this.unitType = unitType;
		this.unitKind = unitKind;
		this.comments = unitKind;
		this.ignored = ignored;
		this.lsTransaction = lsTransaction;
		this.publicData = publicData;	
		}
	
	private Long id;
	private Long stateId;
	private String agCodeName;
	private String lsType;
	private String lsKind;
	private String stringValue;
	protected String codeValue;
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
	private String comments;
	private boolean ignored;
	private Long lsTransaction;
	private boolean publicData;
	

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Long getStateId() {
        return this.stateId;
    }

	public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

	public String getAgCodeName() {
        return this.agCodeName;
    }

	public void setAgCodeName(String agCodeName) {
        this.agCodeName = agCodeName;
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

	public static AnalysisGroupValueBaseDTO fromJsonToAnalysisGroupValueBaseDTO(String json) {
        return new JSONDeserializer<AnalysisGroupValueBaseDTO>()
        .use(null, AnalysisGroupValueBaseDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<AnalysisGroupValueBaseDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<AnalysisGroupValueBaseDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<AnalysisGroupValueBaseDTO> fromJsonArrayToAnalysisGroes(String json) {
        return new JSONDeserializer<List<AnalysisGroupValueBaseDTO>>()
        .use("values", AnalysisGroupValueBaseDTO.class).deserialize(json);
    }
}


