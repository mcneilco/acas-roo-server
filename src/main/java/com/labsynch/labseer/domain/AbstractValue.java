package com.labsynch.labseer.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;

@Configurable
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

@Transactional
public abstract class AbstractValue {

	@NotNull
	@Size(max = 64)
	private String lsType;

	@NotNull
	@Size(max = 255)
	private String lsKind;

	@Size(max = 350)
	private String lsTypeAndKind;

	@Size(max = 255)
	private String codeOrigin;

	@Size(max = 255)
	private String codeType;

	@Size(max = 255)
	private String codeKind;

	@Size(max = 350)
	private String codeTypeAndKind;

	@Size(max = 255)
	protected String stringValue;

	@Size(max = 255)
	protected String codeValue;

	@Size(max = 512)
	private String fileValue;

	@Size(max = 2000)
	private String urlValue;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Date dateValue;

	@Column(columnDefinition = "text")
	@Basic(fetch = FetchType.LAZY)
	private String clobValue;

	@Basic(fetch = FetchType.LAZY)
	private byte[] blobValue;

	@Size(max = 25)
	private String operatorType;

	@Size(max = 10)
	private String operatorKind;

	@Size(max = 50)
	private String operatorTypeAndKind;

	@Column(precision = 38, scale = 18)
	private BigDecimal numericValue;

	private Integer sigFigs;

	@Column(precision = 38, scale = 18)
	private BigDecimal uncertainty;

	private Integer numberOfReplicates;

	@Size(max = 255)
	private String uncertaintyType;

	@Size(max = 25)
	private String unitType;

	@Size(max = 25)
	private String unitKind;

	@Size(max = 55)
	private String unitTypeAndKind;

	private Double concentration;

	@Size(max = 25)
	private String concUnit;

	@Size(max = 512)
	private String comments;

	@NotNull
	private boolean ignored;

	@NotNull
	private boolean deleted;

	private Long lsTransaction;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Date recordedDate;

	@NotNull
	@Size(max = 255)
	private String recordedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Date modifiedDate;

	@Size(max = 255)
	private String modifiedBy;

	@NotNull
	private boolean publicData;

	@Id
	@SequenceGenerator(name = "abstractValueGen", sequenceName = "VALUE_PKSEQ", allocationSize = 50)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "abstractValueGen")
	@Column(name = "id")
	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLsTypeAndKind(String lsTypeAndKind) {
		this.lsTypeAndKind = lsTypeAndKind;
	}

	public void setOperatorTypeAndKind(String operatorTypeAndKind) {
		this.operatorTypeAndKind = operatorTypeAndKind;
	}

	public void setUnitTypeAndKind(String unitTypeAndKind) {
		this.unitTypeAndKind = unitTypeAndKind;
	}

	public void setNumericValue(BigDecimal numericValue) {
		this.numericValue = numericValue;
	}

	public boolean getIgnored() {
		return this.ignored;
	}

	public boolean getPublicData() {
		return this.publicData;
	}

	public boolean getDeleted() {
		return this.deleted;
	}

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new AbstractValue() {
		}.entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	@Transactional
	public void persist() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.setLsTypeAndKind(new StringBuilder().append(this.lsType).append("_").append(this.lsKind).toString());
		this.setCodeTypeAndKind(new StringBuilder().append(this.codeType).append("_").append(this.codeKind).toString());
		this.setUnitTypeAndKind(new StringBuilder().append(this.unitType).append("_").append(this.unitKind).toString());
		this.setOperatorTypeAndKind(
				new StringBuilder().append(this.operatorType).append("_").append(this.operatorKind).toString());
		if (this.recordedDate == null)
			this.recordedDate = new Date();
		this.entityManager.persist(this);
	}

	@Transactional
	public AbstractValue merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.setLsTypeAndKind(new StringBuilder().append(this.lsType).append("_").append(this.lsKind).toString());
		this.setCodeTypeAndKind(new StringBuilder().append(this.codeType).append("_").append(this.codeKind).toString());
		this.setUnitTypeAndKind(new StringBuilder().append(this.unitType).append("_").append(this.unitKind).toString());
		this.setOperatorTypeAndKind(
				new StringBuilder().append(this.operatorType).append("_").append(this.operatorKind).toString());
		this.modifiedDate = new Date();
		AbstractValue merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public static String[] getColumns() {
		String[] headerColumns = new String[] {
				"id",
				"lsType",
				"lsKind",
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
		// 31 columns
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

				new Optional()

		};

		return processors;
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

	public String getCodeTypeAndKind() {
		return this.codeTypeAndKind;
	}

	public void setCodeTypeAndKind(String codeTypeAndKind) {
		this.codeTypeAndKind = codeTypeAndKind;
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

	public String getOperatorTypeAndKind() {
		return this.operatorTypeAndKind;
	}

	public BigDecimal getNumericValue() {
		return this.numericValue;
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

	public String getUnitTypeAndKind() {
		return this.unitTypeAndKind;
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

	public boolean isDeleted() {
		return this.deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
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

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("lsType", "lsKind",
			"lsTypeAndKind", "codeOrigin", "codeType", "codeKind", "codeTypeAndKind", "stringValue", "codeValue",
			"fileValue", "urlValue", "dateValue", "clobValue", "blobValue", "operatorType", "operatorKind",
			"operatorTypeAndKind", "numericValue", "sigFigs", "uncertainty", "numberOfReplicates", "uncertaintyType",
			"unitType", "unitKind", "unitTypeAndKind", "concentration", "concUnit", "comments", "ignored", "deleted",
			"lsTransaction", "recordedDate", "recordedBy", "modifiedDate", "modifiedBy", "publicData", "id",
			"entityManager");

	public static long countAbstractValues() {
		return entityManager().createQuery("SELECT COUNT(o) FROM AbstractValue o", Long.class).getSingleResult();
	}

	public static List<AbstractValue> findAllAbstractValues() {
		return entityManager().createQuery("SELECT o FROM AbstractValue o", AbstractValue.class).getResultList();
	}

	public static List<AbstractValue> findAllAbstractValues(String sortFieldName, String sortOrder) {
		String jpaQuery = "SELECT o FROM AbstractValue o";
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				jpaQuery = jpaQuery + " " + sortOrder;
			}
		}
		return entityManager().createQuery(jpaQuery, AbstractValue.class).getResultList();
	}

	public static AbstractValue findAbstractValue(Long id) {
		if (id == null)
			return null;
		return entityManager().find(AbstractValue.class, id);
	}

	public static List<AbstractValue> findAbstractValueEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM AbstractValue o", AbstractValue.class)
				.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

	public static List<AbstractValue> findAbstractValueEntries(int firstResult, int maxResults, String sortFieldName,
			String sortOrder) {
		String jpaQuery = "SELECT o FROM AbstractValue o";
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				jpaQuery = jpaQuery + " " + sortOrder;
			}
		}
		return entityManager().createQuery(jpaQuery, AbstractValue.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}

	@Transactional
	public void remove() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			AbstractValue attached = AbstractValue.findAbstractValue(this.id);
			this.entityManager.remove(attached);
		}
	}

	@Transactional
	public void flush() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.flush();
	}

	@Transactional
	public void clear() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.clear();
	}

	@Version
	@Column(name = "version")
	private Integer version;

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
