package com.labsynch.labseer.dto;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class DoseReponseParamsDTO {

	private static final Logger logger = LoggerFactory.getLogger(DoseReponseParamsDTO.class);

	public DoseReponseParamsDTO() {
		// empty constructor
	}

	public DoseReponseParamsDTO(
			Long id,
			Integer version,
			String agCodeName,
			String curveId,
			String lsType,
			String lsKind,
			String stringValue,
			String codeValue,
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
			boolean publicData) {

		this.id = id;
		this.lsType = lsType;
		this.lsKind = lsKind;
		this.numericValue = numericValue;
		this.stringValue = stringValue;
		this.codeValue = codeValue;
		this.clobValue = clobValue;

		// private String operatorType;
		// private String operatorKind;
		// private Integer sigFigs;
		// private BigDecimal uncertainty;
		// private Integer numberOfReplicates;
		// private String uncertaintyType;
		// private String unitType;
		// private String unitKind;
		// private String comments;
		// private boolean ignored;
		// private Long lsTransaction;
		// private boolean publicData;
	}

	private Long id;
	private Integer version;
	private Long Stateid;
	private String agCodeName;
	private String curveId;
	private String lsType;
	private String lsKind;
	private String stringValue;
	private String codeValue;
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

	public String toJson() {
		return new JSONSerializer()
				.exclude("*.class").serialize(this);
	}

	public String toJson(String[] fields) {
		return new JSONSerializer()
				.include(fields).exclude("*.class").serialize(this);
	}

	public static DoseReponseParamsDTO fromJsonToDoseReponseParamsDTO(String json) {
		return new JSONDeserializer<DoseReponseParamsDTO>()
				.use(null, DoseReponseParamsDTO.class).deserialize(json);
	}

	public static String toJsonArray(Collection<DoseReponseParamsDTO> collection) {
		return new JSONSerializer()
				.exclude("*.class").serialize(collection);
	}

	public static String toJsonArray(Collection<DoseReponseParamsDTO> collection, String[] fields) {
		return new JSONSerializer()
				.include(fields).exclude("*.class").serialize(collection);
	}

	public static Collection<DoseReponseParamsDTO> fromJsonArrayToDoseRepoes(String json) {
		return new JSONDeserializer<List<DoseReponseParamsDTO>>()
				.use("values", DoseReponseParamsDTO.class).deserialize(json);
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
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

	public Long getStateid() {
		return this.Stateid;
	}

	public void setStateid(Long Stateid) {
		this.Stateid = Stateid;
	}

	public String getAgCodeName() {
		return this.agCodeName;
	}

	public void setAgCodeName(String agCodeName) {
		this.agCodeName = agCodeName;
	}

	public String getCurveId() {
		return this.curveId;
	}

	public void setCurveId(String curveId) {
		this.curveId = curveId;
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
}
