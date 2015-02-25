package com.labsynch.labseer.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@RooJpaActiveRecord(sequenceName = "EXPERIMENT_KIND_PKSEQ", finders={"findExperimentKindsByLsTypeEqualsAndKindNameEquals"})
@RooJson
public class ExperimentKind {

    @NotNull
    @ManyToOne
    private ExperimentType lsType;

    @NotNull
    @Size(max = 255)
    private String kindName;

    @Size(max = 255)
    @Column(unique = true)
    private String lsTypeAndKind;

    @Id
    @SequenceGenerator(name = "experimentKindGen", sequenceName = "EXPERIMENT_KIND_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "experimentKindGen")
    @Column(name = "id")
    private Long id;

    @Version
    @Column(name = "version")
    private Integer version;

    @PersistenceContext
    transient EntityManager entityManager;

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

    public static final EntityManager entityManager() {
        EntityManager em = new ExperimentKind().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countExperimentKinds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ExperimentKind o", Long.class).getSingleResult();
    }

    public static List<com.labsynch.labseer.domain.ExperimentKind> findAllExperimentKinds() {
        return entityManager().createQuery("SELECT o FROM ExperimentKind o", ExperimentKind.class).getResultList();
    }

    public static com.labsynch.labseer.domain.ExperimentKind findExperimentKind(Long id) {
        if (id == null) return null;
        return entityManager().find(ExperimentKind.class, id);
    }

    public static List<com.labsynch.labseer.domain.ExperimentKind> findExperimentKindEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ExperimentKind o", ExperimentKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.lsType = ExperimentType.findExperimentType(this.getLsType().getId());
        this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            ExperimentKind attached = ExperimentKind.findExperimentKind(this.id);
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
    public com.labsynch.labseer.domain.ExperimentKind merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
        ExperimentKind merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
