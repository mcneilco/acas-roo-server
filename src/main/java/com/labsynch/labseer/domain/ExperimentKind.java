package com.labsynch.labseer.domain;

import java.util.Collection;
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
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity

public class ExperimentKind {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ls_type")
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
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countExperimentKinds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ExperimentKind o", Long.class).getSingleResult();
    }

    public static List<com.labsynch.labseer.domain.ExperimentKind> findAllExperimentKinds() {
        return entityManager().createQuery("SELECT o FROM ExperimentKind o", ExperimentKind.class).getResultList();
    }

    public static com.labsynch.labseer.domain.ExperimentKind findExperimentKind(Long id) {
        if (id == null)
            return null;
        return entityManager().find(ExperimentKind.class, id);
    }

    public static List<com.labsynch.labseer.domain.ExperimentKind> findExperimentKindEntries(int firstResult,
            int maxResults) {
        return entityManager().createQuery("SELECT o FROM ExperimentKind o", ExperimentKind.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.lsType = ExperimentType.findExperimentType(this.getLsType().getId());
        this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_')
                .append(this.getKindName()).toString();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            ExperimentKind attached = ExperimentKind.findExperimentKind(this.id);
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

    @Transactional
    public com.labsynch.labseer.domain.ExperimentKind merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_')
                .append(this.getKindName()).toString();
        ExperimentKind merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("lsType", "kindName",
            "lsTypeAndKind", "id", "version", "entityManager");

    public static List<ExperimentKind> findAllExperimentKinds(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ExperimentKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ExperimentKind.class).getResultList();
    }

    public static List<ExperimentKind> findExperimentKindEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM ExperimentKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ExperimentKind.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public ExperimentType getLsType() {
        return this.lsType;
    }

    public void setLsType(ExperimentType lsType) {
        this.lsType = lsType;
    }

    public String getKindName() {
        return this.kindName;
    }

    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    public String getLsTypeAndKind() {
        return this.lsTypeAndKind;
    }

    public void setLsTypeAndKind(String lsTypeAndKind) {
        this.lsTypeAndKind = lsTypeAndKind;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static Long countFindExperimentKindsByLsTypeEqualsAndKindNameEquals(ExperimentType lsType, String kindName) {
        if (lsType == null)
            throw new IllegalArgumentException("The lsType argument is required");
        if (kindName == null || kindName.length() == 0)
            throw new IllegalArgumentException("The kindName argument is required");
        EntityManager em = ExperimentKind.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM ExperimentKind AS o WHERE o.lsType = :lsType  AND o.kindName = :kindName",
                Long.class);
        q.setParameter("lsType", lsType);
        q.setParameter("kindName", kindName);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<ExperimentKind> findExperimentKindsByLsTypeEqualsAndKindNameEquals(ExperimentType lsType,
            String kindName) {
        if (lsType == null)
            throw new IllegalArgumentException("The lsType argument is required");
        if (kindName == null || kindName.length() == 0)
            throw new IllegalArgumentException("The kindName argument is required");
        EntityManager em = ExperimentKind.entityManager();
        TypedQuery<ExperimentKind> q = em.createQuery(
                "SELECT o FROM ExperimentKind AS o WHERE o.lsType = :lsType  AND o.kindName = :kindName",
                ExperimentKind.class);
        q.setParameter("lsType", lsType);
        q.setParameter("kindName", kindName);
        return q;
    }

    public static TypedQuery<ExperimentKind> findExperimentKindsByLsTypeEqualsAndKindNameEquals(ExperimentType lsType,
            String kindName, String sortFieldName, String sortOrder) {
        if (lsType == null)
            throw new IllegalArgumentException("The lsType argument is required");
        if (kindName == null || kindName.length() == 0)
            throw new IllegalArgumentException("The kindName argument is required");
        EntityManager em = ExperimentKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM ExperimentKind AS o WHERE o.lsType = :lsType  AND o.kindName = :kindName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ExperimentKind> q = em.createQuery(queryBuilder.toString(), ExperimentKind.class);
        q.setParameter("lsType", lsType);
        q.setParameter("kindName", kindName);
        return q;
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static ExperimentKind fromJsonToExperimentKind(String json) {
        return new JSONDeserializer<ExperimentKind>()
                .use(null, ExperimentKind.class).deserialize(json);
    }

    public static String toJsonArray(Collection<ExperimentKind> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<ExperimentKind> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<ExperimentKind> fromJsonArrayToExperimentKinds(String json) {
        return new JSONDeserializer<List<ExperimentKind>>()
                .use("values", ExperimentKind.class).deserialize(json);
    }
}
