package com.labsynch.labseer.domain;

import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity

public class PreDef_CorpName {

    private static final Logger logger = LoggerFactory.getLogger(PreDef_CorpName.class);

    @org.hibernate.annotations.Index(name = "PreDef_CorpNumber_IDX")
    private long corpNumber;

    @Size(max = 64)
    @org.hibernate.annotations.Index(name = "PreDef_CorpName_IDX")
    private String corpName;

    @Size(max = 255)
    private String comment;

    @org.hibernate.annotations.Index(name = "PreDef_Used_IDX")
    private Boolean used;

    @org.hibernate.annotations.Index(name = "PreDef_Skip_IDX")
    private Boolean skip;

    public static PreDef_CorpName findNextCorpName() {
        EntityManager em = PreDef_CorpName.entityManager();
        TypedQuery<PreDef_CorpName> q = em.createQuery(
                "SELECT o FROM PreDef_CorpName AS o WHERE o.skip = false AND o.used = false ORDER BY o asc",
                PreDef_CorpName.class);
        q.setMaxResults(1);
        return q.getSingleResult();

    }

    public long getCorpNumber() {
        return this.corpNumber;
    }

    public void setCorpNumber(long corpNumber) {
        this.corpNumber = corpNumber;
    }

    public String getCorpName() {
        return this.corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getUsed() {
        return this.used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public Boolean getSkip() {
        return this.skip;
    }

    public void setSkip(Boolean skip) {
        this.skip = skip;
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static PreDef_CorpName fromJsonToPreDef_CorpName(String json) {
        return new JSONDeserializer<PreDef_CorpName>()
                .use(null, PreDef_CorpName.class).deserialize(json);
    }

    public static String toJsonArray(Collection<PreDef_CorpName> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<PreDef_CorpName> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<PreDef_CorpName> fromJsonArrayToPreDef_CorpNames(String json) {
        return new JSONDeserializer<List<PreDef_CorpName>>()
                .use("values", PreDef_CorpName.class).deserialize(json);
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

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "corpNumber",
            "corpName", "comment", "used", "skip");

    public static final EntityManager entityManager() {
        EntityManager em = new PreDef_CorpName().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countPreDef_CorpNames() {
        return entityManager().createQuery("SELECT COUNT(o) FROM PreDef_CorpName o", Long.class).getSingleResult();
    }

    public static List<PreDef_CorpName> findAllPreDef_CorpNames() {
        return entityManager().createQuery("SELECT o FROM PreDef_CorpName o", PreDef_CorpName.class).getResultList();
    }

    public static List<PreDef_CorpName> findAllPreDef_CorpNames(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM PreDef_CorpName o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, PreDef_CorpName.class).getResultList();
    }

    public static PreDef_CorpName findPreDef_CorpName(Long id) {
        if (id == null)
            return null;
        return entityManager().find(PreDef_CorpName.class, id);
    }

    public static List<PreDef_CorpName> findPreDef_CorpNameEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM PreDef_CorpName o", PreDef_CorpName.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<PreDef_CorpName> findPreDef_CorpNameEntries(int firstResult, int maxResults,
            String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM PreDef_CorpName o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, PreDef_CorpName.class).setFirstResult(firstResult)
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
            PreDef_CorpName attached = PreDef_CorpName.findPreDef_CorpName(this.id);
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
    public PreDef_CorpName merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        PreDef_CorpName merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
