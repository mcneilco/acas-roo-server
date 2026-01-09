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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity

public class ValueType {

    private static final Logger logger = LoggerFactory.getLogger(ValueType.class);

    @NotNull
    @Column(unique = true)
    @Size(max = 64)
    private String typeName;

    public static ValueType getOrCreate(String name) {
        ValueType lsType = null;
        List<ValueType> lsTypes = ValueType.findValueTypesByTypeNameEquals(name).getResultList();
        if (lsTypes.size() == 0) {
            lsType = new ValueType();
            lsType.setTypeName(name);
            lsType.persist();
        } else if (lsTypes.size() == 1) {
            lsType = lsTypes.get(0);
        } else if (lsTypes.size() > 1) {
            logger.error("ERROR: multiple Label types with the same name");
        }
        return lsType;
    }

    @Id
    @SequenceGenerator(name = "valueTypeGen", sequenceName = "VALUE_TYPE_PKSEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "valueTypeGen")
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

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "typeName", "id",
            "version");

    public static final EntityManager entityManager() {
        EntityManager em = new ValueType().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countValueTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ValueType o", Long.class).getSingleResult();
    }

    public static List<ValueType> findAllValueTypes() {
        return entityManager().createQuery("SELECT o FROM ValueType o", ValueType.class).getResultList();
    }

    public static List<ValueType> findAllValueTypes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ValueType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ValueType.class).getResultList();
    }

    public static ValueType findValueType(Long id) {
        if (id == null)
            return null;
        return entityManager().find(ValueType.class, id);
    }

    public static List<ValueType> findValueTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ValueType o", ValueType.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public static List<ValueType> findValueTypeEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM ValueType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ValueType.class).setFirstResult(firstResult)
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
            ValueType attached = ValueType.findValueType(this.id);
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
    public ValueType merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        ValueType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static Long countFindValueTypesByTypeNameEquals(String typeName) {
        if (typeName == null || typeName.length() == 0)
            throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = ValueType.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ValueType AS o WHERE o.typeName = :typeName", Long.class);
        q.setParameter("typeName", typeName);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<ValueType> findValueTypesByTypeNameEquals(String typeName) {
        if (typeName == null || typeName.length() == 0)
            throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = ValueType.entityManager();
        TypedQuery<ValueType> q = em.createQuery("SELECT o FROM ValueType AS o WHERE o.typeName = :typeName",
                ValueType.class);
        q.setParameter("typeName", typeName);
        return q;
    }

    public static TypedQuery<ValueType> findValueTypesByTypeNameEquals(String typeName, String sortFieldName,
            String sortOrder) {
        if (typeName == null || typeName.length() == 0)
            throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = ValueType.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ValueType AS o WHERE o.typeName = :typeName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ValueType> q = em.createQuery(queryBuilder.toString(), ValueType.class);
        q.setParameter("typeName", typeName);
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

    public static ValueType fromJsonToValueType(String json) {
        return new JSONDeserializer<ValueType>()
                .use(null, ValueType.class).deserialize(json);
    }

    public static String toJsonArray(Collection<ValueType> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<ValueType> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<ValueType> fromJsonArrayToValueTypes(String json) {
        return new JSONDeserializer<List<ValueType>>()
                .use("values", ValueType.class).deserialize(json);
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
