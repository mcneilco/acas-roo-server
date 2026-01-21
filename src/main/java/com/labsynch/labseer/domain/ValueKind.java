package com.labsynch.labseer.domain;

import java.util.Collection;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

public class ValueKind {

    private static final Logger logger = LoggerFactory.getLogger(ValueKind.class);

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ls_type")
    private ValueType lsType;

    @NotNull
    @Size(max = 64)
    private String kindName;

    @Column(unique = true)
    @Size(max = 255)
    private String lsTypeAndKind;

    @Id
    @SequenceGenerator(name = "valueKindGen", sequenceName = "VALUE_KIND_PKSEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "valueKindGen")
    @Column(name = "id")
    private Long id;

    @Version
    @Column(name = "version")
    private Integer version;

    @PersistenceContext
    transient EntityManager entityManager;

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

    public static final EntityManager entityManager() {
        EntityManager em = new ValueKind().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countValueKinds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ValueKind o", Long.class).getSingleResult();
    }

    public static List<com.labsynch.labseer.domain.ValueKind> findAllValueKinds() {
        return entityManager().createQuery("SELECT o FROM ValueKind o", ValueKind.class).getResultList();
    }

    public static com.labsynch.labseer.domain.ValueKind findValueKind(Long id) {
        if (id == null)
            return null;
        return entityManager().find(ValueKind.class, id);
    }

    public static List<com.labsynch.labseer.domain.ValueKind> findValueKindEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ValueKind o", ValueKind.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.lsType = ValueType.findValueType(this.getLsType().getId());
        this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_')
                .append(this.getKindName()).toString();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            ValueKind attached = ValueKind.findValueKind(this.id);
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
    public com.labsynch.labseer.domain.ValueKind merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_')
                .append(this.getKindName()).toString();
        ValueKind merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public static ValueKind getOrCreate(ValueType lsType, String kindName) {
        ValueKind lsKind = null;
        List<ValueKind> lsKinds = ValueKind.findValueKindsByKindNameEqualsAndLsType(kindName, lsType).getResultList();

        if (lsKinds.size() == 0) {
            lsKind = new ValueKind();
            lsKind.setKindName(kindName);
            lsKind.setLsType(lsType);
            lsKind.persist();
        } else if (lsKinds.size() == 1) {
            lsKind = lsKinds.get(0);
        } else if (lsKinds.size() > 1) {
            logger.error("ERROR: multiple value kinds with the same name and type");
        }

        return lsKind;
    }

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "lsType",
            "kindName", "lsTypeAndKind", "id", "version", "entityManager");

    public static List<ValueKind> findAllValueKinds(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ValueKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ValueKind.class).getResultList();
    }

    public static List<ValueKind> findValueKindEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM ValueKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ValueKind.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

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

    public static ValueKind fromJsonToValueKind(String json) {
        return new JSONDeserializer<ValueKind>()
                .use(null, ValueKind.class).deserialize(json);
    }

    public static String toJsonArray(Collection<ValueKind> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<ValueKind> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<ValueKind> fromJsonArrayToValueKinds(String json) {
        return new JSONDeserializer<List<ValueKind>>()
                .use("values", ValueKind.class).deserialize(json);
    }

    public static Long countFindValueKindsByKindNameEqualsAndLsType(String kindName, ValueType lsType) {
        if (kindName == null || kindName.length() == 0)
            throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = ValueKind.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM ValueKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType", Long.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindValueKindsByLsType(ValueType lsType) {
        if (lsType == null)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = ValueKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ValueKind AS o WHERE o.lsType = :lsType", Long.class);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<ValueKind> findValueKindsByKindNameEqualsAndLsType(String kindName, ValueType lsType) {
        if (kindName == null || kindName.length() == 0)
            throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = ValueKind.entityManager();
        TypedQuery<ValueKind> q = em.createQuery(
                "SELECT o FROM ValueKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType", ValueKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }

    public static TypedQuery<ValueKind> findValueKindsByKindNameEqualsAndLsType(String kindName, ValueType lsType,
            String sortFieldName, String sortOrder) {
        if (kindName == null || kindName.length() == 0)
            throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = ValueKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM ValueKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ValueKind> q = em.createQuery(queryBuilder.toString(), ValueKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }

    public static TypedQuery<ValueKind> findValueKindsByLsType(ValueType lsType) {
        if (lsType == null)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = ValueKind.entityManager();
        TypedQuery<ValueKind> q = em.createQuery("SELECT o FROM ValueKind AS o WHERE o.lsType = :lsType",
                ValueKind.class);
        q.setParameter("lsType", lsType);
        return q;
    }

    public static TypedQuery<ValueKind> findValueKindsByLsType(ValueType lsType, String sortFieldName,
            String sortOrder) {
        if (lsType == null)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = ValueKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ValueKind AS o WHERE o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ValueKind> q = em.createQuery(queryBuilder.toString(), ValueKind.class);
        q.setParameter("lsType", lsType);
        return q;
    }

    public ValueType getLsType() {
        return this.lsType;
    }

    public void setLsType(ValueType lsType) {
        this.lsType = lsType;
    }

    public String getKindName() {
        return this.kindName;
    }

    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    public String getLsTypeAndKind() {
        return this.lsTypeAndKind;
    }

    public void setLsTypeAndKind(String lsTypeAndKind) {
        this.lsTypeAndKind = lsTypeAndKind;
    }
}
