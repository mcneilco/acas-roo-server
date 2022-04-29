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

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable

public class DryRunCompound {

    private String corpName;

    private String stereoCategory;

    private String stereoComment;

    private int CdId;

    private int RecordNumber;

    @Column(columnDefinition = "text")
    private String molStructure;

    public DryRunCompound() {
    }

    @Transactional
    public void truncateTable() {
        int output = DryRunCompound.entityManager().createNativeQuery("TRUNCATE dry_run_compound").executeUpdate();
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

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("corpName",
            "stereoCategory", "stereoComment", "CdId", "RecordNumber", "molStructure");

    public static final EntityManager entityManager() {
        EntityManager em = new DryRunCompound().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countDryRunCompounds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM DryRunCompound o", Long.class).getSingleResult();
    }

    public static List<DryRunCompound> findAllDryRunCompounds() {
        return entityManager().createQuery("SELECT o FROM DryRunCompound o", DryRunCompound.class).getResultList();
    }

    public static List<DryRunCompound> findAllDryRunCompounds(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM DryRunCompound o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, DryRunCompound.class).getResultList();
    }

    public static DryRunCompound findDryRunCompound(Long id) {
        if (id == null)
            return null;
        return entityManager().find(DryRunCompound.class, id);
    }

    public static List<DryRunCompound> findDryRunCompoundEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM DryRunCompound o", DryRunCompound.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<DryRunCompound> findDryRunCompoundEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM DryRunCompound o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, DryRunCompound.class).setFirstResult(firstResult)
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
            DryRunCompound attached = DryRunCompound.findDryRunCompound(this.id);
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
    public DryRunCompound merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        DryRunCompound merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public String getCorpName() {
        return this.corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getStereoCategory() {
        return this.stereoCategory;
    }

    public void setStereoCategory(String stereoCategory) {
        this.stereoCategory = stereoCategory;
    }

    public String getStereoComment() {
        return this.stereoComment;
    }

    public void setStereoComment(String stereoComment) {
        this.stereoComment = stereoComment;
    }

    public int getCdId() {
        return this.CdId;
    }

    public void setCdId(int CdId) {
        this.CdId = CdId;
    }

    public int getRecordNumber() {
        return this.RecordNumber;
    }

    public void setRecordNumber(int RecordNumber) {
        this.RecordNumber = RecordNumber;
    }

    public String getMolStructure() {
        return this.molStructure;
    }

    public void setMolStructure(String molStructure) {
        this.molStructure = molStructure;
    }

    public static Long countFindDryRunCompoundsByCdId(int CdId) {
        EntityManager em = DryRunCompound.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM DryRunCompound AS o WHERE o.CdId = :CdId", Long.class);
        q.setParameter("CdId", CdId);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindDryRunCompoundsByCorpNameEquals(String corpName) {
        if (corpName == null || corpName.length() == 0)
            throw new IllegalArgumentException("The corpName argument is required");
        EntityManager em = DryRunCompound.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM DryRunCompound AS o WHERE o.corpName = :corpName",
                Long.class);
        q.setParameter("corpName", corpName);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<DryRunCompound> findDryRunCompoundsByCdId(int CdId) {
        EntityManager em = DryRunCompound.entityManager();
        TypedQuery<DryRunCompound> q = em.createQuery("SELECT o FROM DryRunCompound AS o WHERE o.CdId = :CdId",
                DryRunCompound.class);
        q.setParameter("CdId", CdId);
        return q;
    }

    public static TypedQuery<DryRunCompound> findDryRunCompoundsByCdId(int CdId, String sortFieldName,
            String sortOrder) {
        EntityManager em = DryRunCompound.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM DryRunCompound AS o WHERE o.CdId = :CdId");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<DryRunCompound> q = em.createQuery(queryBuilder.toString(), DryRunCompound.class);
        q.setParameter("CdId", CdId);
        return q;
    }

    public static TypedQuery<DryRunCompound> findDryRunCompoundsByCorpNameEquals(String corpName) {
        if (corpName == null || corpName.length() == 0)
            throw new IllegalArgumentException("The corpName argument is required");
        EntityManager em = DryRunCompound.entityManager();
        TypedQuery<DryRunCompound> q = em.createQuery("SELECT o FROM DryRunCompound AS o WHERE o.corpName = :corpName",
                DryRunCompound.class);
        q.setParameter("corpName", corpName);
        return q;
    }

    public static TypedQuery<DryRunCompound> findDryRunCompoundsByCorpNameEquals(String corpName, String sortFieldName,
            String sortOrder) {
        if (corpName == null || corpName.length() == 0)
            throw new IllegalArgumentException("The corpName argument is required");
        EntityManager em = DryRunCompound.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM DryRunCompound AS o WHERE o.corpName = :corpName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<DryRunCompound> q = em.createQuery(queryBuilder.toString(), DryRunCompound.class);
        q.setParameter("corpName", corpName);
        return q;
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

    public static DryRunCompound fromJsonToDryRunCompound(String json) {
        return new JSONDeserializer<DryRunCompound>()
                .use(null, DryRunCompound.class).deserialize(json);
    }

    public static String toJsonArray(Collection<DryRunCompound> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<DryRunCompound> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<DryRunCompound> fromJsonArrayToDryRunCompounds(String json) {
        return new JSONDeserializer<List<DryRunCompound>>()
                .use("values", DryRunCompound.class).deserialize(json);
    }
}
