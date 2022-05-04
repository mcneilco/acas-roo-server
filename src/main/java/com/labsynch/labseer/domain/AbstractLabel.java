package com.labsynch.labseer.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
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

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Configurable

@Transactional
public abstract class AbstractLabel {

	// @Column(unique = true)
	@NotNull
	@Size(max = 255)
	private String labelText;

	@NotNull
	@Size(max = 255)
	private String recordedBy;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Date recordedDate;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Date modifiedDate;

	private boolean physicallyLabled;

	@Size(max = 255)
	private String imageFile;

	@NotNull
	@Size(max = 64)
	private String lsType;

	@NotNull
	@Size(max = 255)
	private String lsKind;

	@Size(max = 255)
	private String lsTypeAndKind;

	@NotNull
	private boolean preferred;

	@NotNull
	private boolean ignored;

	@NotNull
	private boolean deleted;

	private Long lsTransaction;

	@Id
	@SequenceGenerator(name = "abstractLabelGen", sequenceName = "LABEL_PKSEQ", allocationSize = 20)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "abstractLabelGen")
	@Column(name = "id")
	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new AbstractLabel() {
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
		if (this.recordedDate == null)
			this.recordedDate = new Date();
		this.entityManager.persist(this);
	}

	@Transactional
	public AbstractLabel merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		AbstractLabel merged = this.entityManager.merge(this);
		this.setLsTypeAndKind(new StringBuilder().append(this.lsType).append("_").append(this.lsKind).toString());
		this.modifiedDate = new Date();
		this.entityManager.flush();
		return merged;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
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

	public String getLabelText() {
		return this.labelText;
	}

	public void setLabelText(String labelText) {
		this.labelText = labelText;
	}

	public String getRecordedBy() {
		return this.recordedBy;
	}

	public void setRecordedBy(String recordedBy) {
		this.recordedBy = recordedBy;
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

	public boolean isPhysicallyLabled() {
		return this.physicallyLabled;
	}

	public void setPhysicallyLabled(boolean physicallyLabled) {
		this.physicallyLabled = physicallyLabled;
	}

	public String getImageFile() {
		return this.imageFile;
	}

	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
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

	public boolean isPreferred() {
		return this.preferred;
	}

	public void setPreferred(boolean preferred) {
		this.preferred = preferred;
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

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("labelText", "recordedBy",
			"recordedDate", "modifiedDate", "physicallyLabled", "imageFile", "lsType", "lsKind", "lsTypeAndKind",
			"preferred", "ignored", "deleted", "lsTransaction", "id", "entityManager");

	public static long countAbstractLabels() {
		return entityManager().createQuery("SELECT COUNT(o) FROM AbstractLabel o", Long.class).getSingleResult();
	}

	public static List<AbstractLabel> findAllAbstractLabels() {
		return entityManager().createQuery("SELECT o FROM AbstractLabel o", AbstractLabel.class).getResultList();
	}

	public static List<AbstractLabel> findAllAbstractLabels(String sortFieldName, String sortOrder) {
		String jpaQuery = "SELECT o FROM AbstractLabel o";
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				jpaQuery = jpaQuery + " " + sortOrder;
			}
		}
		return entityManager().createQuery(jpaQuery, AbstractLabel.class).getResultList();
	}

	public static AbstractLabel findAbstractLabel(Long id) {
		if (id == null)
			return null;
		return entityManager().find(AbstractLabel.class, id);
	}

	public static List<AbstractLabel> findAbstractLabelEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM AbstractLabel o", AbstractLabel.class)
				.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

	public static List<AbstractLabel> findAbstractLabelEntries(int firstResult, int maxResults, String sortFieldName,
			String sortOrder) {
		String jpaQuery = "SELECT o FROM AbstractLabel o";
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				jpaQuery = jpaQuery + " " + sortOrder;
			}
		}
		return entityManager().createQuery(jpaQuery, AbstractLabel.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}

	@Transactional
	public void remove() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			AbstractLabel attached = AbstractLabel.findAbstractLabel(this.id);
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
}
