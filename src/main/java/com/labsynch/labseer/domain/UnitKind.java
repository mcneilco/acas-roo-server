package com.labsynch.labseer.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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

@Entity
@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "UNITS_KIND_PKSEQ", finders={"findUnitKindsByKindNameEqualsAndLsType"})
@RooJson
public class UnitKind {

    @NotNull
    @ManyToOne
	@JoinColumn(name = "ls_type")
    private UnitType lsType;

    @NotNull
    @Size(max = 64)
    private String kindName;

    @Column(unique = true)
    @Size(max = 255)
    private String lsTypeAndKind;

	@Id
    @SequenceGenerator(name = "unitKindGen", sequenceName = "UNITS_KIND_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "unitKindGen")
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
        EntityManager em = new UnitKind().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countUnitKinds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM UnitKind o", Long.class).getSingleResult();
    }

	public static List<UnitKind> findAllUnitKinds() {
        return entityManager().createQuery("SELECT o FROM UnitKind o", UnitKind.class).getResultList();
    }

	public static UnitKind findUnitKind(Long id) {
        if (id == null) return null;
        return entityManager().find(UnitKind.class, id);
    }

	public static List<UnitKind> findUnitKindEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM UnitKind o", UnitKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.lsType = UnitType.findUnitType(this.getLsType().getId());
		this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            UnitKind attached = UnitKind.findUnitKind(this.id);
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
    public UnitKind merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
		this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
        UnitKind merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
