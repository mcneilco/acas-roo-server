package com.labsynch.labseer.dto;

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

import com.labsynch.labseer.domain.ExperimentValue;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class StateValueCsvDTO {

	private static final Logger logger = LoggerFactory.getLogger(StateValueCsvDTO.class);

	public StateValueCsvDTO() {
		// empty constructor
	}

	public StateValueCsvDTO(ExperimentValue entityValue) {
		this.setStateId(entityValue.getLsState().getId());
		this.setId(entityValue.getId());
		this.setLsType(entityValue.getLsType());
		this.setLsKind(entityValue.getLsKind());
		this.setCodeValue(entityValue.getCodeValue());
		this.setCodeType(entityValue.getCodeType());
		this.setCodeKind(entityValue.getCodeKind());
		this.setCodeOrigin(entityValue.getCodeOrigin());

		this.setStringValue(entityValue.getStringValue());
		this.setFileValue(entityValue.getFileValue());
		this.setUrlValue(entityValue.getUrlValue());
		this.setDateValue(entityValue.getDateValue());
		this.setClobValue(entityValue.getClobValue());

		this.setOperatorType(entityValue.getOperatorType());
		this.setOperatorKind(entityValue.getOperatorKind());
		this.setNumericValue(entityValue.getNumericValue());
		this.setSigFigs(entityValue.getSigFigs());
		this.setUncertainty(entityValue.getUncertainty());
		this.setUncertaintyType(entityValue.getUncertaintyType());
		this.setUnitType(entityValue.getUnitType());
		this.setUnitKind(entityValue.getUnitKind());
		this.setNumberOfReplicates(entityValue.getNumberOfReplicates());

		this.setComments(entityValue.getComments());
		this.setIgnored(entityValue.isIgnored());
		this.setLsTransaction(entityValue.getLsTransaction());

		this.setRecordedDate(entityValue.getRecordedDate());
		this.setRecordedBy(entityValue.getRecordedBy());
		this.setModifiedDate(entityValue.getModifiedDate());
		this.setModifiedBy(entityValue.getModifiedBy());
		this.setPublicData(entityValue.isPublicData());
	}

	private Long stateId;

	private Long id;
	private String lsType;
	private String lsKind;
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

	private String comments;
	private boolean ignored;
	private Long lsTransaction;
	private Date recordedDate;
	private String recordedBy;
	private Date modifiedDate;
	private String modifiedBy;
	private boolean publicData;

	// 29 fields

	public boolean getIgnored() {
		return this.ignored;
	}

	public boolean getPublicData() {
		return this.publicData;
	}

	public void setLsTransaction(String lsTransaction) {
		if (lsTransaction.equalsIgnoreCase("NULL")) {
			this.lsTransaction = null;
		} else {
			this.lsTransaction = Long.valueOf(lsTransaction);
		}
	}

	public void setStateId(String stateId) {
		this.stateId = Long.valueOf(stateId);
	}

	public void setPublicData(String publicData) {
		if (publicData.equalsIgnoreCase("FALSE")) {
			this.publicData = false;
		} else {
			this.publicData = true;
		}
	}

	public void setIgnored(String ignored) {
		if (ignored.equalsIgnoreCase("FALSE")) {
			this.ignored = false;
		} else {
			this.ignored = true;
		}
	}

	public void setDateValue(String dateValue) throws ParseException {
		if (dateValue.equalsIgnoreCase("NULL")) {
			this.dateValue = null;
		} else {
			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				this.dateValue = df.parse(dateValue);
			} catch (Exception e) {
				try {
					this.dateValue = new Date(Long.parseLong(dateValue));
				} catch (Exception e2) {
					DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
					this.dateValue = df.parse(dateValue);
				}

			}
		}
	}

	public void setNumericValue(String numericValue) {
		if (numericValue.equalsIgnoreCase("NULL")) {
			this.numericValue = null;
		} else {
			this.numericValue = new BigDecimal(numericValue);
		}
	}

	public void setUncertainty(String uncertainty) {
		if (uncertainty.equalsIgnoreCase("NULL")) {
			this.uncertainty = null;
		} else {
			this.uncertainty = new BigDecimal(uncertainty);
		}
	}

	public void setId(String id) {
		if (id.equalsIgnoreCase("NULL")) {
			this.id = null;
		} else {
			this.id = Long.valueOf(id);
		}
	}

	public void setSigFigs(String sigFigs) {
		if (sigFigs.equalsIgnoreCase("NULL")) {
			this.sigFigs = null;
		} else {
			this.sigFigs = Integer.valueOf(sigFigs);
		}
	}

	public void setNumberOfReplicates(String numberOfReplicates) {
		if (numberOfReplicates.equalsIgnoreCase("NULL")) {
			this.numberOfReplicates = null;
		} else {
			this.numberOfReplicates = Integer.valueOf(numberOfReplicates);
		}
	}

	public static String[] getColumns() {
		String[] headerColumns = new String[] {
				"stateId",

				"id",
				"lsType",
				"lsKind",
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
				"comments",
				"ignored",
				"lsTransaction",
				"recordedDate",
				"recordedBy",
				"modifiedDate",
				"modifiedBy",
				"publicData"
		};

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
				new Optional()

		};

		return processors;
	}

	public Long getStateId() {
		return this.stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
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

	public static StateValueCsvDTO fromJsonToStateValueCsvDTO(String json) {
		return new JSONDeserializer<StateValueCsvDTO>()
				.use(null, StateValueCsvDTO.class).deserialize(json);
	}

	public static String toJsonArray(Collection<StateValueCsvDTO> collection) {
		return new JSONSerializer()
				.exclude("*.class").serialize(collection);
	}

	public static String toJsonArray(Collection<StateValueCsvDTO> collection, String[] fields) {
		return new JSONSerializer()
				.include(fields).exclude("*.class").serialize(collection);
	}

	public static Collection<StateValueCsvDTO> fromJsonArrayToStateValueCsvDTO(String json) {
		return new JSONDeserializer<List<StateValueCsvDTO>>()
				.use("values", StateValueCsvDTO.class).deserialize(json);
	}
}
