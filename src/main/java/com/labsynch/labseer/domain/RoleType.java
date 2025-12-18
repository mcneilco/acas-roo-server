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
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity

public class RoleType {

    @NotNull
    @Column(unique = true)
    @Size(max = 128)
    private String typeName;

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

    public static RoleType fromJsonToRoleType(String json) {
        return new JSONDeserializer<RoleType>()
                .use(null, RoleType.class).deserialize(json);
    }

    public static String toJsonArray(Collection<RoleType> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<RoleType> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<RoleType> fromJsonArrayToRoleTypes(String json) {
        return new JSONDeserializer<List<RoleType>>()
                .use("values", RoleType.class).deserialize(json);
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("typeName");

    public static final EntityManager entityManager() {
        EntityManager em = new RoleType().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countRoleTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM RoleType o", Long.class).getSingleResult();
    }

    public static List<RoleType> findAllRoleTypes() {
        return entityManager().createQuery("SELECT o FROM RoleType o", RoleType.class).getResultList();
    }

    public static List<RoleType> findAllRoleTypes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM RoleType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, RoleType.class).getResultList();
    }

    public static RoleType findRoleType(Long id) {
        if (id == null)
            return null;
        return entityManager().find(RoleType.class, id);
    }

    public static List<RoleType> findRoleTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM RoleType o", RoleType.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public static List<RoleType> findRoleTypeEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM RoleType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, RoleType.class).setFirstResult(firstResult)
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
            RoleType attached = RoleType.findRoleType(this.id);
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
    public RoleType merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        RoleType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Id
    @SequenceGenerator(name = "roleTypeGen", sequenceName = "ROLE_TYPE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "roleTypeGen")
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

    public static Long countFindRoleTypesByTypeNameEquals(String typeName) {
        if (typeName == null || typeName.length() == 0)
            throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = RoleType.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM RoleType AS o WHERE o.typeName = :typeName", Long.class);
        q.setParameter("typeName", typeName);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<RoleType> findRoleTypesByTypeNameEquals(String typeName) {
        if (typeName == null || typeName.length() == 0)
            throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = RoleType.entityManager();
        TypedQuery<RoleType> q = em.createQuery("SELECT o FROM RoleType AS o WHERE o.typeName = :typeName",
                RoleType.class);
        q.setParameter("typeName", typeName);
        return q;
    }

    public static TypedQuery<RoleType> findRoleTypesByTypeNameEquals(String typeName, String sortFieldName,
            String sortOrder) {
        if (typeName == null || typeName.length() == 0)
            throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = RoleType.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM RoleType AS o WHERE o.typeName = :typeName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<RoleType> q = em.createQuery(queryBuilder.toString(), RoleType.class);
        q.setParameter("typeName", typeName);
        return q;
    }
}
