package com.labsynch.labseer.dto;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.TreatmentGroupState;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class TreatmentGroupValueDTO {
	
	
    public TreatmentGroupValueDTO(TreatmentGroupValue TreatmentGroupValue) {
    	this.setTreatmentGroupId(TreatmentGroupValue.getTreatmentGroupId());
    	
    	this.setLsState(TreatmentGroupValue.getLsState());
    	
    	this.setId(TreatmentGroupValue.getId());
    	this.setLsType(TreatmentGroupValue.getLsType());
    	this.setLsKind(TreatmentGroupValue.getLsKind());
    	this.setLsTypeAndKind(TreatmentGroupValue.getLsTypeAndKind());
    	
		this.setStringValue(TreatmentGroupValue.getStringValue());
		this.setCodeValue(TreatmentGroupValue.getCodeValue());
		this.setFileValue(TreatmentGroupValue.getFileValue());
		this.setUrlValue(TreatmentGroupValue.getUrlValue());
		this.setDateValue(TreatmentGroupValue.getDateValue());
		this.setClobValue(TreatmentGroupValue.getClobValue());
		this.setBlobValue(TreatmentGroupValue.getBlobValue());
		this.setNumericValue(TreatmentGroupValue.getNumericValue());
		
		this.setSigFigs(TreatmentGroupValue.getSigFigs());
		this.setUncertainty(TreatmentGroupValue.getUncertainty());
		this.setNumberOfReplicates(TreatmentGroupValue.getNumberOfReplicates());
		this.setUncertaintyType(TreatmentGroupValue.getUncertaintyType());
		
		this.setComments(TreatmentGroupValue.getComments());
		this.setIgnored(TreatmentGroupValue.isIgnored());
		this.setModifiedDate(TreatmentGroupValue.getModifiedDate());
		this.setRecordedDate(TreatmentGroupValue.getRecordedDate());
		this.setPublicData(TreatmentGroupValue.isPublicData());

		this.setCodeOrigin(TreatmentGroupValue.getCodeOrigin());
		this.setCodeType(TreatmentGroupValue.getCodeType());
		this.setCodeKind(TreatmentGroupValue.getCodeKind());
		this.setOperatorType(TreatmentGroupValue.getOperatorType());
		this.setOperatorKind(TreatmentGroupValue.getOperatorKind());
		this.setUnitType(TreatmentGroupValue.getUnitType());
		this.setUnitKind(TreatmentGroupValue.getUnitKind());

		this.setLsTransaction(TreatmentGroupValue.getLsTransaction());
    	this.setRecordedBy(TreatmentGroupValue.getRecordedBy());
		this.setModifiedBy(TreatmentGroupValue.getModifiedBy());
		this.setStateId(TreatmentGroupValue.getStateId());	
		this.setStateKind(TreatmentGroupValue.getStateKind());	
		this.setStateType(TreatmentGroupValue.getStateType());
    }
    
    private Long analysisGroupId;
    private Long treatmentGroupId;
	
    private TreatmentGroupState lsState;
	
	private Long id;
    private String lsType;
	private String lsKind;
	private String lsTypeAndKind;
	
	private String stringValue;
	private String codeValue;
	private String fileValue;
	private String urlValue;
	private Date dateValue;
	private String clobValue;
    private byte[] blobValue;
    private BigDecimal numericValue;
    
	private Integer sigFigs;
	private BigDecimal uncertainty;
	private Integer numberOfReplicates;
	private String uncertaintyType;
	
	private String comments;
	private boolean ignored;
	private Date recordedDate;
	private Date modifiedDate;
	private boolean publicData;
	
	private String codeOrigin;
	private String codeType;
	private String codeKind;
    private String operatorType;
	private String operatorKind;
    private String unitType;
	private String unitKind;
	private Long lsTransaction;
	private String recordedBy;
	private String modifiedBy;
    private Long stateId;
	private String stateType;
	private String stateKind;
	
	private BigDecimal concentration;
	private String concUnit;
	
	public boolean getIgnored() {
		return this.isIgnored();
	}
	
	public boolean getPublicData() {
		return this.isPublicData();
	}


	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static TreatmentGroupValueDTO fromJsonToTreatmentGroupValueDTO(String json) {
        return new JSONDeserializer<TreatmentGroupValueDTO>()
        .use(null, TreatmentGroupValueDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<TreatmentGroupValueDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<TreatmentGroupValueDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<TreatmentGroupValueDTO> fromJsonArrayToTreatmentGroes(String json) {
        return new JSONDeserializer<List<TreatmentGroupValueDTO>>()
        .use("values", TreatmentGroupValueDTO.class).deserialize(json);
    }

	public Long getAnalysisGroupId() {
        return this.analysisGroupId;
    }

	public void setAnalysisGroupId(Long analysisGroupId) {
        this.analysisGroupId = analysisGroupId;
    }

	public Long getTreatmentGroupId() {
        return this.treatmentGroupId;
    }

	public void setTreatmentGroupId(Long treatmentGroupId) {
        this.treatmentGroupId = treatmentGroupId;
    }

	public TreatmentGroupState getLsState() {
        return this.lsState;
    }

	public void setLsState(TreatmentGroupState lsState) {
        this.lsState = lsState;
    }

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
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

	public String getLsTypeAndKind() {
        return this.lsTypeAndKind;
    }

	public void setLsTypeAndKind(String lsTypeAndKind) {
        this.lsTypeAndKind = lsTypeAndKind;
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

	public byte[] getBlobValue() {
        return this.blobValue;
    }

	public void setBlobValue(byte[] blobValue) {
        this.blobValue = blobValue;
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

	public Date getRecordedDate() {
        return this.recordedDate;
    }

	public void setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }

	public Date getModifiedDate() {
        return this.modifiedDate;
    }

	public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

	public boolean isPublicData() {
        return this.publicData;
    }

	public void setPublicData(boolean publicData) {
        this.publicData = publicData;
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

	public Long getLsTransaction() {
        return this.lsTransaction;
    }

	public void setLsTransaction(Long lsTransaction) {
        this.lsTransaction = lsTransaction;
    }

	public String getRecordedBy() {
        return this.recordedBy;
    }

	public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

	public String getModifiedBy() {
        return this.modifiedBy;
    }

	public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

	public Long getStateId() {
        return this.stateId;
    }

	public void setStateId(Long stateId) {
        this.stateId = stateId;
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

	public BigDecimal getConcentration() {
        return this.concentration;
    }

	public void setConcentration(BigDecimal concentration) {
        this.concentration = concentration;
    }

	public String getConcUnit() {
        return this.concUnit;
    }

	public void setConcUnit(String concUnit) {
        this.concUnit = concUnit;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}

