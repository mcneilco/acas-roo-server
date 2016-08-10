package com.labsynch.labseer.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;


@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "INTERACTION_KIND_PKSEQ", finders={"findInteractionKindsByKindNameEqualsAndLsType"})
@RooJson
public class InteractionKind {
	
    @NotNull
    @ManyToOne
    private InteractionType lsType;

    @NotNull
    @Size(max = 255)
    private String kindName;

    @Column(unique = true)
    @Size(max = 255)
    private String lsTypeAndKind;

	@Id
    @SequenceGenerator(name = "interactionKindGen", sequenceName = "INTERACTION_KIND_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "interactionKindGen")
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

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

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new InteractionKind().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countInteractionKinds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM InteractionKind o", Long.class).getSingleResult();
    }

	public static List<InteractionKind> findAllInteractionKinds() {
        return entityManager().createQuery("SELECT o FROM InteractionKind o", InteractionKind.class).getResultList();
    }

	public static InteractionKind findInteractionKind(Long id_) {
        if (id_ == null) return null;
        return entityManager().find(InteractionKind.class, id_);
    }

	public static List<InteractionKind> findInteractionKindEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM InteractionKind o", InteractionKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }


	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
		this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            InteractionKind attached = InteractionKind.findInteractionKind(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public InteractionKind merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
		this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
        InteractionKind merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
