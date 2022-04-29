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
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable

public class Unit {

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String code;

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

    public static Long countFindUnitsByCodeEquals(String code) {
        if (code == null || code.length() == 0)
            throw new IllegalArgumentException("The code argument is required");
        EntityManager em = Unit.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Unit AS o WHERE o.code = :code", Long.class);
        q.setParameter("code", code);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindUnitsByCodeLike(String code) {
        if (code == null || code.length() == 0)
            throw new IllegalArgumentException("The code argument is required");
        code = code.replace('*', '%');
        if (code.charAt(0) != '%') {
            code = "%" + code;
        }
        if (code.charAt(code.length() - 1) != '%') {
            code = code + "%";
        }
        EntityManager em = Unit.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Unit AS o WHERE LOWER(o.code) LIKE LOWER(:code)",
                Long.class);
        q.setParameter("code", code);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindUnitsByNameEquals(String name) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Unit.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Unit AS o WHERE o.name = :name", Long.class);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<Unit> findUnitsByCodeEquals(String code) {
        if (code == null || code.length() == 0)
            throw new IllegalArgumentException("The code argument is required");
        EntityManager em = Unit.entityManager();
        TypedQuery<Unit> q = em.createQuery("SELECT o FROM Unit AS o WHERE o.code = :code", Unit.class);
        q.setParameter("code", code);
        return q;
    }

    public static TypedQuery<Unit> findUnitsByCodeEquals(String code, String sortFieldName, String sortOrder) {
        if (code == null || code.length() == 0)
            throw new IllegalArgumentException("The code argument is required");
        EntityManager em = Unit.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Unit AS o WHERE o.code = :code");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Unit> q = em.createQuery(queryBuilder.toString(), Unit.class);
        q.setParameter("code", code);
        return q;
    }

    public static TypedQuery<Unit> findUnitsByCodeLike(String code) {
        if (code == null || code.length() == 0)
            throw new IllegalArgumentException("The code argument is required");
        code = code.replace('*', '%');
        if (code.charAt(0) != '%') {
            code = "%" + code;
        }
        if (code.charAt(code.length() - 1) != '%') {
            code = code + "%";
        }
        EntityManager em = Unit.entityManager();
        TypedQuery<Unit> q = em.createQuery("SELECT o FROM Unit AS o WHERE LOWER(o.code) LIKE LOWER(:code)",
                Unit.class);
        q.setParameter("code", code);
        return q;
    }

    public static TypedQuery<Unit> findUnitsByCodeLike(String code, String sortFieldName, String sortOrder) {
        if (code == null || code.length() == 0)
            throw new IllegalArgumentException("The code argument is required");
        code = code.replace('*', '%');
        if (code.charAt(0) != '%') {
            code = "%" + code;
        }
        if (code.charAt(code.length() - 1) != '%') {
            code = code + "%";
        }
        EntityManager em = Unit.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Unit AS o WHERE LOWER(o.code) LIKE LOWER(:code)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Unit> q = em.createQuery(queryBuilder.toString(), Unit.class);
        q.setParameter("code", code);
        return q;
    }

    public static TypedQuery<Unit> findUnitsByNameEquals(String name) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Unit.entityManager();
        TypedQuery<Unit> q = em.createQuery("SELECT o FROM Unit AS o WHERE o.name = :name", Unit.class);
        q.setParameter("name", name);
        return q;
    }

    public static TypedQuery<Unit> findUnitsByNameEquals(String name, String sortFieldName, String sortOrder) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Unit.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Unit AS o WHERE o.name = :name");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Unit> q = em.createQuery(queryBuilder.toString(), Unit.class);
        q.setParameter("name", name);
        return q;
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("name", "code");

    public static final EntityManager entityManager() {
        EntityManager em = new Unit().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countUnits() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Unit o", Long.class).getSingleResult();
    }

    public static List<Unit> findAllUnits() {
        return entityManager().createQuery("SELECT o FROM Unit o", Unit.class).getResultList();
    }

    public static List<Unit> findAllUnits(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Unit o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Unit.class).getResultList();
    }

    public static Unit findUnit(Long id) {
        if (id == null)
            return null;
        return entityManager().find(Unit.class, id);
    }

    public static List<Unit> findUnitEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Unit o", Unit.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public static List<Unit> findUnitEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Unit o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Unit.class).setFirstResult(firstResult).setMaxResults(maxResults)
                .getResultList();
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
            Unit attached = Unit.findUnit(this.id);
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
    public Unit merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        Unit merged = this.entityManager.merge(this);
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

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static Unit fromJsonToUnit(String json) {
        return new JSONDeserializer<Unit>()
                .use(null, Unit.class).deserialize(json);
    }

    public static String toJsonArray(Collection<Unit> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<Unit> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<Unit> fromJsonArrayToUnits(String json) {
        return new JSONDeserializer<List<Unit>>()
                .use("values", Unit.class).deserialize(json);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
