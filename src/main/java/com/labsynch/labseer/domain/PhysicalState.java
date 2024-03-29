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

public class PhysicalState {

    @Size(max = 255)
    private String name;

    @Size(max = 100)
    private String code;

    @PersistenceContext
    transient EntityManager entityManager;

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("name", "code");

    public static final EntityManager entityManager() {
        EntityManager em = new PhysicalState().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countPhysicalStates() {
        return entityManager().createQuery("SELECT COUNT(o) FROM PhysicalState o", Long.class).getSingleResult();
    }

    public static List<PhysicalState> findAllPhysicalStates() {
        return entityManager().createQuery("SELECT o FROM PhysicalState o", PhysicalState.class).getResultList();
    }

    public static List<PhysicalState> findAllPhysicalStates(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM PhysicalState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, PhysicalState.class).getResultList();
    }

    public static PhysicalState findPhysicalState(Long id) {
        if (id == null)
            return null;
        return entityManager().find(PhysicalState.class, id);
    }

    public static List<PhysicalState> findPhysicalStateEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM PhysicalState o", PhysicalState.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<PhysicalState> findPhysicalStateEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM PhysicalState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, PhysicalState.class).setFirstResult(firstResult)
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
            PhysicalState attached = PhysicalState.findPhysicalState(this.id);
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
    public PhysicalState merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        PhysicalState merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public static Long countFindPhysicalStatesByCodeEquals(String code) {
        if (code == null || code.length() == 0)
            throw new IllegalArgumentException("The code argument is required");
        EntityManager em = PhysicalState.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM PhysicalState AS o WHERE o.code = :code", Long.class);
        q.setParameter("code", code);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindPhysicalStatesByNameEquals(String name) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = PhysicalState.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM PhysicalState AS o WHERE o.name = :name", Long.class);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindPhysicalStatesByNameLike(String name) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        name = name.replace('*', '%');
        if (name.charAt(0) != '%') {
            name = "%" + name;
        }
        if (name.charAt(name.length() - 1) != '%') {
            name = name + "%";
        }
        EntityManager em = PhysicalState.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM PhysicalState AS o WHERE LOWER(o.name) LIKE LOWER(:name)",
                Long.class);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<PhysicalState> findPhysicalStatesByCodeEquals(String code) {
        if (code == null || code.length() == 0)
            throw new IllegalArgumentException("The code argument is required");
        EntityManager em = PhysicalState.entityManager();
        TypedQuery<PhysicalState> q = em.createQuery("SELECT o FROM PhysicalState AS o WHERE o.code = :code",
                PhysicalState.class);
        q.setParameter("code", code);
        return q;
    }

    public static TypedQuery<PhysicalState> findPhysicalStatesByCodeEquals(String code, String sortFieldName,
            String sortOrder) {
        if (code == null || code.length() == 0)
            throw new IllegalArgumentException("The code argument is required");
        EntityManager em = PhysicalState.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM PhysicalState AS o WHERE o.code = :code");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<PhysicalState> q = em.createQuery(queryBuilder.toString(), PhysicalState.class);
        q.setParameter("code", code);
        return q;
    }

    public static TypedQuery<PhysicalState> findPhysicalStatesByCodeEqualsIgnoreCase(String code) {
        if (code == null || code.length() == 0)
            throw new IllegalArgumentException("The code argument is required");
        EntityManager em = PhysicalState.entityManager();
        TypedQuery<PhysicalState> q = em.createQuery("SELECT o FROM PhysicalState AS o WHERE LOWER(o.code) = LOWER(:code)",
                PhysicalState.class);
        q.setParameter("code", code);
        return q;
    }

    public static TypedQuery<PhysicalState> findPhysicalStatesByNameEquals(String name) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = PhysicalState.entityManager();
        TypedQuery<PhysicalState> q = em.createQuery("SELECT o FROM PhysicalState AS o WHERE o.name = :name",
                PhysicalState.class);
        q.setParameter("name", name);
        return q;
    }

    public static TypedQuery<PhysicalState> findPhysicalStatesByNameEqualsIgnoreCase(String name) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = PhysicalState.entityManager();
        TypedQuery<PhysicalState> q = em.createQuery("SELECT o FROM PhysicalState AS o WHERE LOWER(o.name) = LOWER(:name)",
                PhysicalState.class);
        q.setParameter("name", name);
        return q;
    }

    public static TypedQuery<PhysicalState> findPhysicalStatesByNameEquals(String name, String sortFieldName,
            String sortOrder) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        EntityManager em = PhysicalState.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM PhysicalState AS o WHERE o.name = :name");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<PhysicalState> q = em.createQuery(queryBuilder.toString(), PhysicalState.class);
        q.setParameter("name", name);
        return q;
    }

    public static TypedQuery<PhysicalState> findPhysicalStatesByNameLike(String name) {
        if (name == null || name.length() == 0)
            throw new IllegalArgumentException("The name argument is required");
        name = name.replace('*', '%');
        if (name.charAt(0) != '%') {
            name = "%" + name;
        }
        if (name.charAt(name.length() - 1) != '%') {
            name = name + "%";
        }
        EntityManager em = PhysicalState.entityManager();
        TypedQuery<PhysicalState> q = em.createQuery(
                "SELECT o FROM PhysicalState AS o WHERE LOWER(o.name) LIKE LOWER(:name)", PhysicalState.class);
        q.setParameter("name", name);
        return q;
    }

    public static TypedQuery<PhysicalState> findPhysicalStatesByNameLike(String name, String sortFieldName,
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
        EntityManager em = PhysicalState.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM PhysicalState AS o WHERE LOWER(o.name) LIKE LOWER(:name)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<PhysicalState> q = em.createQuery(queryBuilder.toString(), PhysicalState.class);
        q.setParameter("name", name);
        return q;
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static PhysicalState fromJsonToPhysicalState(String json) {
        return new JSONDeserializer<PhysicalState>()
                .use(null, PhysicalState.class).deserialize(json);
    }

    public static String toJsonArray(Collection<PhysicalState> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<PhysicalState> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<PhysicalState> fromJsonArrayToPhysicalStates(String json) {
        return new JSONDeserializer<List<PhysicalState>>()
                .use("values", PhysicalState.class).deserialize(json);
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
}
