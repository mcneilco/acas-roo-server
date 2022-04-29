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
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable

public class UncertaintyKind {

    @NotNull
    @Column(unique = true)
    @Size(max = 255)
    @JoinColumn(name = "ls_type")
    private String kindName;

    @Id
    @SequenceGenerator(name = "uncertaintyKindGen", sequenceName = "UNCERTAINTY_KIND_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uncertaintyKindGen")
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

    public String getKindName() {
        return this.kindName;
    }

    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("kindName");

    public static final EntityManager entityManager() {
        EntityManager em = new UncertaintyKind().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countUncertaintyKinds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM UncertaintyKind o", Long.class).getSingleResult();
    }

    public static List<UncertaintyKind> findAllUncertaintyKinds() {
        return entityManager().createQuery("SELECT o FROM UncertaintyKind o", UncertaintyKind.class).getResultList();
    }

    public static List<UncertaintyKind> findAllUncertaintyKinds(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM UncertaintyKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, UncertaintyKind.class).getResultList();
    }

    public static UncertaintyKind findUncertaintyKind(Long id) {
        if (id == null)
            return null;
        return entityManager().find(UncertaintyKind.class, id);
    }

    public static List<UncertaintyKind> findUncertaintyKindEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM UncertaintyKind o", UncertaintyKind.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<UncertaintyKind> findUncertaintyKindEntries(int firstResult, int maxResults,
            String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM UncertaintyKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, UncertaintyKind.class).setFirstResult(firstResult)
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
            UncertaintyKind attached = UncertaintyKind.findUncertaintyKind(this.id);
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
    public UncertaintyKind merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        UncertaintyKind merged = this.entityManager.merge(this);
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

    public static UncertaintyKind fromJsonToUncertaintyKind(String json) {
        return new JSONDeserializer<UncertaintyKind>()
                .use(null, UncertaintyKind.class).deserialize(json);
    }

    public static String toJsonArray(Collection<UncertaintyKind> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<UncertaintyKind> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<UncertaintyKind> fromJsonArrayToUncertaintyKinds(String json) {
        return new JSONDeserializer<List<UncertaintyKind>>()
                .use("values", UncertaintyKind.class).deserialize(json);
    }
}
