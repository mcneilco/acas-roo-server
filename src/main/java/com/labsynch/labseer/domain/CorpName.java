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
import javax.persistence.Version;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable

public class CorpName {

    @Size(max = 50)
    private String parentCorpName;

    @Size(max = 50)
    private String comment;

    private Boolean ignore;

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
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

    public String getParentCorpName() {
        return this.parentCorpName;
    }

    public void setParentCorpName(String parentCorpName) {
        this.parentCorpName = parentCorpName;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getIgnore() {
        return this.ignore;
    }

    public void setIgnore(Boolean ignore) {
        this.ignore = ignore;
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static CorpName fromJsonToCorpName(String json) {
        return new JSONDeserializer<CorpName>()
                .use(null, CorpName.class).deserialize(json);
    }

    public static String toJsonArray(Collection<CorpName> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<CorpName> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<CorpName> fromJsonArrayToCorpNames(String json) {
        return new JSONDeserializer<List<CorpName>>()
                .use("values", CorpName.class).deserialize(json);
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("parentCorpName", "comment",
            "ignore");

    public static final EntityManager entityManager() {
        EntityManager em = new CorpName().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countCorpNames() {
        return entityManager().createQuery("SELECT COUNT(o) FROM CorpName o", Long.class).getSingleResult();
    }

    public static List<CorpName> findAllCorpNames() {
        return entityManager().createQuery("SELECT o FROM CorpName o", CorpName.class).getResultList();
    }

    public static List<CorpName> findAllCorpNames(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM CorpName o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, CorpName.class).getResultList();
    }

    public static CorpName findCorpName(Long id) {
        if (id == null)
            return null;
        return entityManager().find(CorpName.class, id);
    }

    public static List<CorpName> findCorpNameEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM CorpName o", CorpName.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public static List<CorpName> findCorpNameEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM CorpName o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, CorpName.class).setFirstResult(firstResult)
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
            CorpName attached = CorpName.findCorpName(this.id);
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
    public CorpName merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        CorpName merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
