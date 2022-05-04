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

public class OperatorKind {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ls_type")
    private OperatorType lsType;

    @NotNull
    @Size(max = 64)
    private String kindName;

    @Column(unique = true)
    @Size(max = 255)
    private String lsTypeAndKind;

    @Id
    @SequenceGenerator(name = "operatorKindGen", sequenceName = "OPERATOR_KIND_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "operatorKindGen")
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
        EntityManager em = new OperatorKind().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countOperatorKinds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM OperatorKind o", Long.class).getSingleResult();
    }

    public static List<OperatorKind> findAllOperatorKinds() {
        return entityManager().createQuery("SELECT o FROM OperatorKind o", OperatorKind.class).getResultList();
    }

    public static OperatorKind findOperatorKind(Long id) {
        if (id == null)
            return null;
        return entityManager().find(OperatorKind.class, id);
    }

    public static List<OperatorKind> findOperatorKindEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM OperatorKind o", OperatorKind.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.lsType = OperatorType.findOperatorType(this.getLsType().getId());
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
            OperatorKind attached = OperatorKind.findOperatorKind(this.id);
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
    public OperatorKind merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_')
                .append(this.getKindName()).toString();
        OperatorKind merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public static Long countFindOperatorKindsByKindNameEqualsAndLsType(String kindName, OperatorType lsType) {
        if (kindName == null || kindName.length() == 0)
            throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = OperatorKind.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM OperatorKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType",
                Long.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<OperatorKind> findOperatorKindsByKindNameEqualsAndLsType(String kindName,
            OperatorType lsType) {
        if (kindName == null || kindName.length() == 0)
            throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = OperatorKind.entityManager();
        TypedQuery<OperatorKind> q = em.createQuery(
                "SELECT o FROM OperatorKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType",
                OperatorKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }

    public static TypedQuery<OperatorKind> findOperatorKindsByKindNameEqualsAndLsType(String kindName,
            OperatorType lsType, String sortFieldName, String sortOrder) {
        if (kindName == null || kindName.length() == 0)
            throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = OperatorKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM OperatorKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<OperatorKind> q = em.createQuery(queryBuilder.toString(), OperatorKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }

    public OperatorType getLsType() {
        return this.lsType;
    }

    public void setLsType(OperatorType lsType) {
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

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("lsType", "kindName",
            "lsTypeAndKind", "id", "version", "entityManager");

    public static List<OperatorKind> findAllOperatorKinds(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM OperatorKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, OperatorKind.class).getResultList();
    }

    public static List<OperatorKind> findOperatorKindEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM OperatorKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, OperatorKind.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
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

    public static OperatorKind fromJsonToOperatorKind(String json) {
        return new JSONDeserializer<OperatorKind>()
                .use(null, OperatorKind.class).deserialize(json);
    }

    public static String toJsonArray(Collection<OperatorKind> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<OperatorKind> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<OperatorKind> fromJsonArrayToOperatorKinds(String json) {
        return new JSONDeserializer<List<OperatorKind>>()
                .use("values", OperatorKind.class).deserialize(json);
    }
}
