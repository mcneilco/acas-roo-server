package com.labsynch.labseer.domain;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "CONTAINER_KIND_PKSEQ", finders={"findContainerKindsByKindNameEqualsAndLsType"})
@RooJson
public class ContainerKind {

	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "ls_type")
	private ContainerType lsType;

	@NotNull
	@Size(max = 255)
	private String kindName;

	@Column(unique = true)
	@Size(max = 255)
	private String lsTypeAndKind;

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new ContainerKind().entityManager;
		if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	@Transactional
	public void persist() {
		if (this.entityManager == null) this.entityManager = entityManager();
		this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
		this.entityManager.persist(this);
	}

	@Transactional
	public ContainerKind merge() {
		if (this.entityManager == null) this.entityManager = entityManager();
		this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
		ContainerKind merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

}
