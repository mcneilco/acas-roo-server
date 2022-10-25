package com.labsynch.labseer.domain;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity

public class StandardizationSettings {

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date modifiedDate;

    /**
     */
    private Boolean needsStandardization;

    private String reasons;

    public String getReasons() {
        return reasons;
    }

    public void setReasons(String reasons) {
        this.reasons = reasons;
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

    public static StandardizationSettings fromJsonToStandardizationSettings(String json) {
        return new JSONDeserializer<StandardizationSettings>()
                .use(null, StandardizationSettings.class).deserialize(json);
    }

    public static String toJsonArray(Collection<StandardizationSettings> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<StandardizationSettings> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<StandardizationSettings> fromJsonArrayToStandardizationSettingses(String json) {
        return new JSONDeserializer<List<StandardizationSettings>>()
                .use("values", StandardizationSettings.class).deserialize(json);
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("modifiedDate",
            "needsStandardization");

    public static final EntityManager entityManager() {
        EntityManager em = new StandardizationSettings().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countStandardizationSettingses() {
        return entityManager().createQuery("SELECT COUNT(o) FROM StandardizationSettings o", Long.class)
                .getSingleResult();
    }

    public static List<StandardizationSettings> findAllStandardizationSettingses() {
        return entityManager().createQuery("SELECT o FROM StandardizationSettings o", StandardizationSettings.class)
                .getResultList();
    }

    public static List<StandardizationSettings> findAllStandardizationSettingses(String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM StandardizationSettings o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, StandardizationSettings.class).getResultList();
    }

    public static StandardizationSettings findStandardizationSettings(Long id) {
        if (id == null)
            return null;
        return entityManager().find(StandardizationSettings.class, id);
    }

    public static List<StandardizationSettings> findStandardizationSettingsEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM StandardizationSettings o", StandardizationSettings.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<StandardizationSettings> findStandardizationSettingsEntries(int firstResult, int maxResults,
            String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM StandardizationSettings o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, StandardizationSettings.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            StandardizationSettings attached = StandardizationSettings.findStandardizationSettings(this.id);
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
    public StandardizationSettings merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        StandardizationSettings merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public static Long countFindStandardizationSettingsesByNeedsStandardization(Boolean needsStandardization) {
        if (needsStandardization == null)
            throw new IllegalArgumentException("The needsStandardization argument is required");
        EntityManager em = StandardizationSettings.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM StandardizationSettings AS o WHERE o.needsStandardization = :needsStandardization",
                Long.class);
        q.setParameter("needsStandardization", needsStandardization);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<StandardizationSettings> findStandardizationSettingsesByNeedsStandardization(
            Boolean needsStandardization) {
        if (needsStandardization == null)
            throw new IllegalArgumentException("The needsStandardization argument is required");
        EntityManager em = StandardizationSettings.entityManager();
        TypedQuery<StandardizationSettings> q = em.createQuery(
                "SELECT o FROM StandardizationSettings AS o WHERE o.needsStandardization = :needsStandardization",
                StandardizationSettings.class);
        q.setParameter("needsStandardization", needsStandardization);
        return q;
    }

    public static TypedQuery<StandardizationSettings> findStandardizationSettingsesByNeedsStandardization(
            Boolean needsStandardization, String sortFieldName, String sortOrder) {
        if (needsStandardization == null)
            throw new IllegalArgumentException("The needsStandardization argument is required");
        EntityManager em = StandardizationSettings.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM StandardizationSettings AS o WHERE o.needsStandardization = :needsStandardization");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<StandardizationSettings> q = em.createQuery(queryBuilder.toString(), StandardizationSettings.class);
        q.setParameter("needsStandardization", needsStandardization);
        return q;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public Date getModifiedDate() {
        return this.modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Boolean getNeedsStandardization() {
        return this.needsStandardization;
    }

    public void setNeedsStandardization(Boolean needsStandardization) {
        this.needsStandardization = needsStandardization;
    }
}