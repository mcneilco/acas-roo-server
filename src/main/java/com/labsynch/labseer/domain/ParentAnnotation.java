package com.labsynch.labseer.domain;

import java.util.Collection;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable

public class ParentAnnotation {

    @NotNull
    private String code;

    @NotNull
    private String name;

    private Integer displayOrder;

    private String comment;

    public static Long countFindParentAnnotationsByCodeEquals(String code) {
        if (code == null || code.length() == 0)
            throw new IllegalArgumentException("The code argument is required");
        EntityManager em = ParentAnnotation.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ParentAnnotation AS o WHERE o.code = :code", Long.class);
        q.setParameter("code", code);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<ParentAnnotation> findParentAnnotationsByCodeEquals(String code) {
        if (code == null || code.length() == 0)
            throw new IllegalArgumentException("The code argument is required");
        EntityManager em = ParentAnnotation.entityManager();
        TypedQuery<ParentAnnotation> q = em.createQuery("SELECT o FROM ParentAnnotation AS o WHERE o.code = :code",
                ParentAnnotation.class);
        q.setParameter("code", code);
        return q;
    }

    public static TypedQuery<ParentAnnotation> findParentAnnotationsByCodeEquals(String code, String sortFieldName,
            String sortOrder) {
        if (code == null || code.length() == 0)
            throw new IllegalArgumentException("The code argument is required");
        EntityManager em = ParentAnnotation.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ParentAnnotation AS o WHERE o.code = :code");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ParentAnnotation> q = em.createQuery(queryBuilder.toString(), ParentAnnotation.class);
        q.setParameter("code", code);
        return q;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDisplayOrder() {
        return this.displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static ParentAnnotation fromJsonToParentAnnotation(String json) {
        return new JSONDeserializer<ParentAnnotation>()
                .use(null, ParentAnnotation.class).deserialize(json);
    }

    public static String toJsonArray(Collection<ParentAnnotation> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<ParentAnnotation> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<ParentAnnotation> fromJsonArrayToParentAnnotations(String json) {
        return new JSONDeserializer<List<ParentAnnotation>>()
                .use("values", ParentAnnotation.class).deserialize(json);
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

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("code", "name",
            "displayOrder", "comment");

    public static final EntityManager entityManager() {
        EntityManager em = new ParentAnnotation().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countParentAnnotations() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ParentAnnotation o", Long.class).getSingleResult();
    }

    public static List<ParentAnnotation> findAllParentAnnotations() {
        return entityManager().createQuery("SELECT o FROM ParentAnnotation o", ParentAnnotation.class).getResultList();
    }

    public static List<ParentAnnotation> findAllParentAnnotations(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ParentAnnotation o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ParentAnnotation.class).getResultList();
    }

    public static ParentAnnotation findParentAnnotation(Long id) {
        if (id == null)
            return null;
        return entityManager().find(ParentAnnotation.class, id);
    }

    public static List<ParentAnnotation> findParentAnnotationEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ParentAnnotation o", ParentAnnotation.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<ParentAnnotation> findParentAnnotationEntries(int firstResult, int maxResults,
            String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ParentAnnotation o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ParentAnnotation.class).setFirstResult(firstResult)
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
            ParentAnnotation attached = ParentAnnotation.findParentAnnotation(this.id);
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
    public ParentAnnotation merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        ParentAnnotation merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
