package com.labsynch.labseer.domain;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
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

@Entity
@Configurable

public class ExperimentType {

    @NotNull
    @Column(unique = true)
    @Size(max = 128)
    private String typeName;

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static ExperimentType fromJsonToExperimentType(String json) {
        return new JSONDeserializer<ExperimentType>()
                .use(null, ExperimentType.class).deserialize(json);
    }

    public static String toJsonArray(Collection<ExperimentType> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<ExperimentType> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<ExperimentType> fromJsonArrayToExperimentTypes(String json) {
        return new JSONDeserializer<List<ExperimentType>>()
                .use("values", ExperimentType.class).deserialize(json);
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("typeName");

    public static final EntityManager entityManager() {
        EntityManager em = new ExperimentType().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countExperimentTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ExperimentType o", Long.class).getSingleResult();
    }

    public static List<ExperimentType> findAllExperimentTypes() {
        return entityManager().createQuery("SELECT o FROM ExperimentType o", ExperimentType.class).getResultList();
    }

    public static List<ExperimentType> findAllExperimentTypes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ExperimentType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ExperimentType.class).getResultList();
    }

    public static ExperimentType findExperimentType(Long id) {
        if (id == null)
            return null;
        return entityManager().find(ExperimentType.class, id);
    }

    public static List<ExperimentType> findExperimentTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ExperimentType o", ExperimentType.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<ExperimentType> findExperimentTypeEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM ExperimentType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ExperimentType.class).setFirstResult(firstResult)
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
            ExperimentType attached = ExperimentType.findExperimentType(this.id);
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
    public ExperimentType merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        ExperimentType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public static Long countFindExperimentTypesByTypeNameEquals(String typeName) {
        if (typeName == null || typeName.length() == 0)
            throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = ExperimentType.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ExperimentType AS o WHERE o.typeName = :typeName",
                Long.class);
        q.setParameter("typeName", typeName);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<ExperimentType> findExperimentTypesByTypeNameEquals(String typeName) {
        if (typeName == null || typeName.length() == 0)
            throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = ExperimentType.entityManager();
        TypedQuery<ExperimentType> q = em.createQuery("SELECT o FROM ExperimentType AS o WHERE o.typeName = :typeName",
                ExperimentType.class);
        q.setParameter("typeName", typeName);
        return q;
    }

    public static TypedQuery<ExperimentType> findExperimentTypesByTypeNameEquals(String typeName, String sortFieldName,
            String sortOrder) {
        if (typeName == null || typeName.length() == 0)
            throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = ExperimentType.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM ExperimentType AS o WHERE o.typeName = :typeName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ExperimentType> q = em.createQuery(queryBuilder.toString(), ExperimentType.class);
        q.setParameter("typeName", typeName);
        return q;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Id
    @SequenceGenerator(name = "experimentTypeGen", sequenceName = "EXPERIMENT_TYPE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "experimentTypeGen")
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

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
