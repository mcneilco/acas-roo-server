package com.labsynch.labseer.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "STATE_PKSEQ", inheritanceType = "TABLE_PER_CLASS")
@RooJson
@Transactional
public abstract class AbstractState {

	@NotNull
	@Size(max = 255)
	private String recordedBy;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Date recordedDate;

	@Size(max = 255)
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Date modifiedDate;

	@NotNull
	@Size(max = 64)
	private String lsType;

	@NotNull
	@Size(max = 255)
	private String lsKind;

	@Size(max = 255)
    @org.hibernate.annotations.Index(name="_TK_IDX")
	private String lsTypeAndKind;

	@Size(max = 512)
	private String comments;

	@NotNull
	@org.hibernate.annotations.Index(name="_IGN_IDX")
	private boolean ignored;
	
	@NotNull
	private boolean deleted;

	@org.hibernate.annotations.Index(name="_TRXN_IDX")
	private Long lsTransaction;

	@Id
	@SequenceGenerator(name = "abstractStateGen", sequenceName = "STATE_PKSEQ", allocationSize=20)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "abstractStateGen")
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
		EntityManager em = new AbstractState() {
		}.entityManager;
		if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	@Transactional
	public void persist() {
		if (this.entityManager == null) this.entityManager = entityManager();
		this.setLsTypeAndKind(new StringBuilder().append(this.lsType).append("_").append(this.lsKind).toString());
		if (this.recordedDate == null) this.recordedDate = new Date();
		this.entityManager.persist(this);
	}

	@Transactional
	public AbstractState merge() {
		if (this.entityManager == null) this.entityManager = entityManager();
		this.setLsTypeAndKind(new StringBuilder().append(this.lsType).append("_").append(this.lsKind).toString());
		this.modifiedDate = new Date();
		AbstractState merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

}
