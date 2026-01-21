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

@Entity
@Configurable

public class Operator {

    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String code;

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

    public static Operator fromJsonToOperator(String json) {
        return new JSONDeserializer<Operator>()
                .use(null, Operator.class).deserialize(json);
    }

    public static String toJsonArray(Collection<Operator> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<Operator> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<Operator> fromJsonArrayToOperators(String json) {
        return new JSONDeserializer<List<Operator>>()
                .use("values", Operator.class).deserialize(json);
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

    public static Long countFindOperatorsByCodeEquals(String code) {
        if (code == null || code.length() == 0)
            throw new IllegalArgumentException("The code argument is required");
        EntityManager em = Operator.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Operator AS o WHERE o.code = :code", Long.class);
        q.setParameter("code", code);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindOperatorsByNameEquals(String name) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Operator.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Operator AS o WHERE o.name = :name", Long.class);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<Operator> findOperatorsByCodeEquals(String code) {
        if (code == null || code.length() == 0)
            throw new IllegalArgumentException("The code argument is required");
        EntityManager em = Operator.entityManager();
        TypedQuery<Operator> q = em.createQuery("SELECT o FROM Operator AS o WHERE o.code = :code", Operator.class);
        q.setParameter("code", code);
        return q;
    }

    public static TypedQuery<Operator> findOperatorsByCodeEquals(String code, String sortFieldName, String sortOrder) {
        if (code == null || code.length() == 0)
            throw new IllegalArgumentException("The code argument is required");
        EntityManager em = Operator.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Operator AS o WHERE o.code = :code");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Operator> q = em.createQuery(queryBuilder.toString(), Operator.class);
        q.setParameter("code", code);
        return q;
    }

    public static TypedQuery<Operator> findOperatorsByNameEquals(String name) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Operator.entityManager();
        TypedQuery<Operator> q = em.createQuery("SELECT o FROM Operator AS o WHERE o.name = :name", Operator.class);
        q.setParameter("name", name);
        return q;
    }

    public static TypedQuery<Operator> findOperatorsByNameEquals(String name, String sortFieldName, String sortOrder) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Operator.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Operator AS o WHERE o.name = :name");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Operator> q = em.createQuery(queryBuilder.toString(), Operator.class);
        q.setParameter("name", name);
        return q;
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("name", "code");

    public static final EntityManager entityManager() {
        EntityManager em = new Operator().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countOperators() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Operator o", Long.class).getSingleResult();
    }

    public static List<Operator> findAllOperators() {
        return entityManager().createQuery("SELECT o FROM Operator o", Operator.class).getResultList();
    }

    public static List<Operator> findAllOperators(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Operator o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Operator.class).getResultList();
    }

    public static Operator findOperator(Long id) {
        if (id == null)
            return null;
        return entityManager().find(Operator.class, id);
    }

    public static List<Operator> findOperatorEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Operator o", Operator.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public static List<Operator> findOperatorEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM Operator o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Operator.class).setFirstResult(firstResult)
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
            Operator attached = Operator.findOperator(this.id);
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
    public Operator merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        Operator merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
