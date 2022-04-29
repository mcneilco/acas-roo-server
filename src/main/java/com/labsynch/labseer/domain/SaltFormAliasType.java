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

@Configurable
@Entity

public class SaltFormAliasType {

    @Size(max = 255)
    private String typeName;

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

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

    @PersistenceContext
    transient EntityManager entityManager;

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("typeName");

    public static final EntityManager entityManager() {
        EntityManager em = new SaltFormAliasType().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countSaltFormAliasTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SaltFormAliasType o", Long.class).getSingleResult();
    }

    public static List<SaltFormAliasType> findAllSaltFormAliasTypes() {
        return entityManager().createQuery("SELECT o FROM SaltFormAliasType o", SaltFormAliasType.class)
                .getResultList();
    }

    public static List<SaltFormAliasType> findAllSaltFormAliasTypes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM SaltFormAliasType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, SaltFormAliasType.class).getResultList();
    }

    public static SaltFormAliasType findSaltFormAliasType(Long id) {
        if (id == null)
            return null;
        return entityManager().find(SaltFormAliasType.class, id);
    }

    public static List<SaltFormAliasType> findSaltFormAliasTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SaltFormAliasType o", SaltFormAliasType.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<SaltFormAliasType> findSaltFormAliasTypeEntries(int firstResult, int maxResults,
            String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM SaltFormAliasType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, SaltFormAliasType.class).setFirstResult(firstResult)
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
            SaltFormAliasType attached = SaltFormAliasType.findSaltFormAliasType(this.id);
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
    public SaltFormAliasType merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        SaltFormAliasType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static SaltFormAliasType fromJsonToSaltFormAliasType(String json) {
        return new JSONDeserializer<SaltFormAliasType>()
                .use(null, SaltFormAliasType.class).deserialize(json);
    }

    public static String toJsonArray(Collection<SaltFormAliasType> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<SaltFormAliasType> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<SaltFormAliasType> fromJsonArrayToSaltFormAliasTypes(String json) {
        return new JSONDeserializer<List<SaltFormAliasType>>()
                .use("values", SaltFormAliasType.class).deserialize(json);
    }
}
