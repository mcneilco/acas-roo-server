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
import jakarta.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity

public class PurityMeasuredBy {

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String code;

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static PurityMeasuredBy fromJsonToPurityMeasuredBy(String json) {
        return new JSONDeserializer<PurityMeasuredBy>()
                .use(null, PurityMeasuredBy.class).deserialize(json);
    }

    public static String toJsonArray(Collection<PurityMeasuredBy> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<PurityMeasuredBy> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<PurityMeasuredBy> fromJsonArrayToPurityMeasuredBys(String json) {
        return new JSONDeserializer<List<PurityMeasuredBy>>()
                .use("values", PurityMeasuredBy.class).deserialize(json);
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("name", "code");

    public static final EntityManager entityManager() {
        EntityManager em = new PurityMeasuredBy().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countPurityMeasuredBys() {
        return entityManager().createQuery("SELECT COUNT(o) FROM PurityMeasuredBy o", Long.class).getSingleResult();
    }

    public static List<PurityMeasuredBy> findAllPurityMeasuredBys() {
        return entityManager().createQuery("SELECT o FROM PurityMeasuredBy o", PurityMeasuredBy.class).getResultList();
    }

    public static List<PurityMeasuredBy> findAllPurityMeasuredBys(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM PurityMeasuredBy o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, PurityMeasuredBy.class).getResultList();
    }

    public static PurityMeasuredBy findPurityMeasuredBy(Long id) {
        if (id == null)
            return null;
        return entityManager().find(PurityMeasuredBy.class, id);
    }

    public static List<PurityMeasuredBy> findPurityMeasuredByEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM PurityMeasuredBy o", PurityMeasuredBy.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<PurityMeasuredBy> findPurityMeasuredByEntries(int firstResult, int maxResults,
            String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM PurityMeasuredBy o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, PurityMeasuredBy.class).setFirstResult(firstResult)
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
            PurityMeasuredBy attached = PurityMeasuredBy.findPurityMeasuredBy(this.id);
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
    public PurityMeasuredBy merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        PurityMeasuredBy merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public static Long countFindPurityMeasuredBysByCodeEquals(String code) {
        if (code == null || code.length() == 0)
            throw new IllegalArgumentException("The code argument is required");
        EntityManager em = PurityMeasuredBy.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM PurityMeasuredBy AS o WHERE o.code = :code", Long.class);
        q.setParameter("code", code);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindPurityMeasuredBysByNameEquals(String name) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = PurityMeasuredBy.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM PurityMeasuredBy AS o WHERE o.name = :name", Long.class);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindPurityMeasuredBysByNameLike(String name) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        name = name.replace('*', '%');
        if (name.charAt(0) != '%') {
            name = "%" + name;
        }
        if (name.charAt(name.length() - 1) != '%') {
            name = name + "%";
        }
        EntityManager em = PurityMeasuredBy.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM PurityMeasuredBy AS o WHERE LOWER(o.name) LIKE LOWER(:name)", Long.class);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<PurityMeasuredBy> findPurityMeasuredBysByCodeEquals(String code) {
        if (code == null || code.length() == 0)
            throw new IllegalArgumentException("The code argument is required");
        EntityManager em = PurityMeasuredBy.entityManager();
        TypedQuery<PurityMeasuredBy> q = em.createQuery("SELECT o FROM PurityMeasuredBy AS o WHERE o.code = :code",
                PurityMeasuredBy.class);
        q.setParameter("code", code);
        return q;
    }

    public static TypedQuery<PurityMeasuredBy> findPurityMeasuredBysByCodeEquals(String code, String sortFieldName,
            String sortOrder) {
        if (code == null || code.length() == 0)
            throw new IllegalArgumentException("The code argument is required");
        EntityManager em = PurityMeasuredBy.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM PurityMeasuredBy AS o WHERE o.code = :code");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<PurityMeasuredBy> q = em.createQuery(queryBuilder.toString(), PurityMeasuredBy.class);
        q.setParameter("code", code);
        return q;
    }

    public static TypedQuery<PurityMeasuredBy> findPurityMeasuredBysByNameEquals(String name) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = PurityMeasuredBy.entityManager();
        TypedQuery<PurityMeasuredBy> q = em.createQuery("SELECT o FROM PurityMeasuredBy AS o WHERE o.name = :name",
                PurityMeasuredBy.class);
        q.setParameter("name", name);
        return q;
    }

    public static TypedQuery<PurityMeasuredBy> findPurityMeasuredBysByNameEquals(String name, String sortFieldName,
            String sortOrder) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = PurityMeasuredBy.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM PurityMeasuredBy AS o WHERE o.name = :name");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<PurityMeasuredBy> q = em.createQuery(queryBuilder.toString(), PurityMeasuredBy.class);
        q.setParameter("name", name);
        return q;
    }

    public static TypedQuery<PurityMeasuredBy> findPurityMeasuredBysByNameLike(String name) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        name = name.replace('*', '%');
        if (name.charAt(0) != '%') {
            name = "%" + name;
        }
        if (name.charAt(name.length() - 1) != '%') {
            name = name + "%";
        }
        EntityManager em = PurityMeasuredBy.entityManager();
        TypedQuery<PurityMeasuredBy> q = em.createQuery(
                "SELECT o FROM PurityMeasuredBy AS o WHERE LOWER(o.name) LIKE LOWER(:name)", PurityMeasuredBy.class);
        q.setParameter("name", name);
        return q;
    }

    public static TypedQuery<PurityMeasuredBy> findPurityMeasuredBysByNameLike(String name, String sortFieldName,
            String sortOrder) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        name = name.replace('*', '%');
        if (name.charAt(0) != '%') {
            name = "%" + name;
        }
        if (name.charAt(name.length() - 1) != '%') {
            name = name + "%";
        }
        EntityManager em = PurityMeasuredBy.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM PurityMeasuredBy AS o WHERE LOWER(o.name) LIKE LOWER(:name)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<PurityMeasuredBy> q = em.createQuery(queryBuilder.toString(), PurityMeasuredBy.class);
        q.setParameter("name", name);
        return q;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
