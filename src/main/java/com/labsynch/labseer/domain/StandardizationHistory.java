package com.labsynch.labseer.domain;

import java.util.Collection;
import java.util.Date;
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

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable

public class StandardizationHistory {

    @Id
    @SequenceGenerator(name = "stndznHistGen", sequenceName = "STNDZN_HIST_PKSEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "stndznHistGen")
    @Column(name = "id")
    private Long id;

    private Date recordedDate;

    private String settings;

    private int settingsHash;

    private String dryRunStatus;

    private Date dryRunStart;

    private Date dryRunComplete;

    private String standardizationStatus;

    private String standardizationUser;

    private String standardizationReason;

    private Date standardizationStart;

    private Date standardizationComplete;

    private Integer structuresStandardizedCount;

    private Integer structuresUpdatedCount;

    private Integer newDuplicateCount;

    private Integer existingDuplicateCount;

    private Integer displayChangeCount;

    private Integer asDrawnDisplayChangeCount;

    private Integer changedStructureCount;

    private Integer dryRunStandardizationChangesCount;

    private Integer standardizationErrorCount;

    private Integer registrationErrorCount;

    public StandardizationHistory() {
    }

    @Version
    @Column(name = "version")
    private Integer version;

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("id", "recordedDate",
            "settings", "settingsHash", "dryRunStatus", "dryRunStart", "dryRunComplete", "standardizationStatus",
            "standardizationUser", "standardizationReason", "standardizationStart", "standardizationComplete",
            "structuresStandardizedCount", "structuresUpdatedCount", "newDuplicateCount", "existingDuplicateCount",
            "displayChangeCount", "asDrawnDisplayChangeCount", "changedStructureCount",
            "dryRunStandardizationChangesCount", "standardizationErrorCount", "registrationErrorCount", "needsStandardiationReasons");

    public static final EntityManager entityManager() {
        EntityManager em = new StandardizationHistory().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countStandardizationHistorys() {
        return entityManager().createQuery("SELECT COUNT(o) FROM StandardizationHistory o", Long.class)
                .getSingleResult();
    }

    public static List<StandardizationHistory> findAllStandardizationHistorys() {
        return entityManager().createQuery("SELECT o FROM StandardizationHistory o", StandardizationHistory.class)
                .getResultList();
    }

    public static List<StandardizationHistory> findAllStandardizationHistorys(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM StandardizationHistory o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, StandardizationHistory.class).getResultList();
    }

    public static StandardizationHistory findStandardizationHistory(Long id) {
        if (id == null)
            return null;
        return entityManager().find(StandardizationHistory.class, id);
    }

    public static List<StandardizationHistory> findStandardizationHistoryEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM StandardizationHistory o", StandardizationHistory.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<StandardizationHistory> findStandardizationHistoryEntries(int firstResult, int maxResults,
            String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM StandardizationHistory o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, StandardizationHistory.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public static TypedQuery<StandardizationHistory> findStandardizationHistoriesByStatus(String status, int firstResult, int maxResults,
            String sortFieldName, String sortOrder) {
            if (status == null)
            throw new IllegalArgumentException("The status argument is required");
        EntityManager em = StandardizationHistory.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM StandardizationHistory AS o WHERE");
        queryBuilder.append(" o.standardizationStatus = :status");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY " + sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" " + sortOrder);
            }
        }
        TypedQuery<StandardizationHistory> q = em.createQuery(queryBuilder.toString(), StandardizationHistory.class);
        q.setParameter("status", status);
        q.setFirstResult(firstResult);
        q.setMaxResults(maxResults);
        return q;
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
            StandardizationHistory attached = StandardizationHistory.findStandardizationHistory(this.id);
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
    public StandardizationHistory merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        StandardizationHistory merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
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

    public static StandardizationHistory fromJsonToStandardizationHistory(String json) {
        return new JSONDeserializer<StandardizationHistory>()
                .use(null, StandardizationHistory.class).deserialize(json);
    }

    public static String toJsonArray(Collection<StandardizationHistory> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<StandardizationHistory> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<StandardizationHistory> fromJsonArrayToStandardizationHistorys(String json) {
        return new JSONDeserializer<List<StandardizationHistory>>()
                .use("values", StandardizationHistory.class).deserialize(json);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getRecordedDate() {
        return this.recordedDate;
    }

    public void setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }

    public String getSettings() {
        return this.settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public int getSettingsHash() {
        return this.settingsHash;
    }

    public void setSettingsHash(int settingsHash) {
        this.settingsHash = settingsHash;
    }

    public String getDryRunStatus() {
        return this.dryRunStatus;
    }

    public void setDryRunStatus(String dryRunStatus) {
        this.dryRunStatus = dryRunStatus;
    }

    public Date getDryRunStart() {
        return this.dryRunStart;
    }

    public void setDryRunStart(Date dryRunStart) {
        this.dryRunStart = dryRunStart;
    }

    public Date getDryRunComplete() {
        return this.dryRunComplete;
    }

    public void setDryRunComplete(Date dryRunComplete) {
        this.dryRunComplete = dryRunComplete;
    }

    public String getStandardizationStatus() {
        return this.standardizationStatus;
    }

    public void setStandardizationStatus(String standardizationStatus) {
        this.standardizationStatus = standardizationStatus;
    }

    public String getStandardizationUser() {
        return this.standardizationUser;
    }

    public void setStandardizationUser(String standardizationUser) {
        this.standardizationUser = standardizationUser;
    }

    public String getStandardizationReason() {
        return this.standardizationReason;
    }

    public void setStandardizationReason(String standardizationReason) {
        this.standardizationReason = standardizationReason;
    }

    public Date getStandardizationStart() {
        return this.standardizationStart;
    }

    public void setStandardizationStart(Date standardizationStart) {
        this.standardizationStart = standardizationStart;
    }

    public Date getStandardizationComplete() {
        return this.standardizationComplete;
    }

    public void setStandardizationComplete(Date standardizationComplete) {
        this.standardizationComplete = standardizationComplete;
    }

    public Integer getStructuresStandardizedCount() {
        return this.structuresStandardizedCount;
    }

    public void setStructuresStandardizedCount(Integer structuresStandardizedCount) {
        this.structuresStandardizedCount = structuresStandardizedCount;
    }

    public Integer getStructuresUpdatedCount() {
        return this.structuresUpdatedCount;
    }

    public void setStructuresUpdatedCount(Integer structuresUpdatedCount) {
        this.structuresUpdatedCount = structuresUpdatedCount;
    }

    public Integer getNewDuplicateCount() {
        return this.newDuplicateCount;
    }

    public void setNewDuplicateCount(Integer newDuplicateCount) {
        this.newDuplicateCount = newDuplicateCount;
    }

    public Integer getExistingDuplicateCount() {
        return this.existingDuplicateCount;
    }

    public void setExistingDuplicateCount(Integer existingDuplicateCount) {
        this.existingDuplicateCount = existingDuplicateCount;
    }

    public Integer getDisplayChangeCount() {
        return this.displayChangeCount;
    }

    public void setDisplayChangeCount(Integer displayChangeCount) {
        this.displayChangeCount = displayChangeCount;
    }

    public Integer getAsDrawnDisplayChangeCount() {
        return this.asDrawnDisplayChangeCount;
    }

    public void setAsDrawnDisplayChangeCount(Integer asDrawnDisplayChangeCount) {
        this.asDrawnDisplayChangeCount = asDrawnDisplayChangeCount;
    }

    public Integer getChangedStructureCount() {
        return this.changedStructureCount;
    }

    public void setChangedStructureCount(Integer changedStructureCount) {
        this.changedStructureCount = changedStructureCount;
    }

    public Integer getDryRunStandardizationChangesCount() {
        return this.dryRunStandardizationChangesCount;
    }

    public void setDryRunStandardizationChangesCount(Integer dryRunStandardizationChangesCount) {
        this.dryRunStandardizationChangesCount = dryRunStandardizationChangesCount;
    }

    public Integer getStandardizationErrorCount() {
        return this.standardizationErrorCount;
    }

    public void setStandardizationErrorCount(Integer standardizationErrorCount) {
        this.standardizationErrorCount = standardizationErrorCount;
    }

    public Integer getRegistrationErrorCount() {
        return this.registrationErrorCount;
    }

    public void setRegistrationErrorCount(Integer registrationErrorCount) {
        this.registrationErrorCount = registrationErrorCount;
    }
}