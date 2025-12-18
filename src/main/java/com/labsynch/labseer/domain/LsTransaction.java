package com.labsynch.labseer.domain;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity

public class LsTransaction {

    @Size(max = 255)
    private String comments;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Date recordedDate;

    @Size(max = 255)
    private String recordedBy;

    @Enumerated(EnumType.STRING)
    private LsTransactionStatus status;

    @Enumerated(EnumType.STRING)
    private LsTransactionType type;

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

    public static LsTransaction fromJsonToLsTransaction(String json) {
        return new JSONDeserializer<LsTransaction>()
                .use(null, LsTransaction.class).deserialize(json);
    }

    public static String toJsonArray(Collection<LsTransaction> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<LsTransaction> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<LsTransaction> fromJsonArrayToLsTransactions(String json) {
        return new JSONDeserializer<List<LsTransaction>>()
                .use("values", LsTransaction.class).deserialize(json);
    }

    @Id
    @SequenceGenerator(name = "lsTransactionGen", sequenceName = "LS_TRANSACTION_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "lsTransactionGen")
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

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getRecordedDate() {
        return this.recordedDate;
    }

    public void setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }

    public String getRecordedBy() {
        return this.recordedBy;
    }

    public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

    public LsTransactionStatus getStatus() {
        return this.status;
    }

    public void setStatus(LsTransactionStatus status) {
        this.status = status;
    }

    public LsTransactionType getType() {
        return this.type;
    }

    public void setType(LsTransactionType type) {
        this.type = type;
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("comments", "recordedDate",
            "recordedBy", "status", "type");

    public static final EntityManager entityManager() {
        EntityManager em = new LsTransaction().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countLsTransactions() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LsTransaction o", Long.class).getSingleResult();
    }

    public static List<LsTransaction> findAllLsTransactions() {
        return entityManager().createQuery("SELECT o FROM LsTransaction o", LsTransaction.class).getResultList();
    }

    public static List<LsTransaction> findAllLsTransactions(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LsTransaction o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsTransaction.class).getResultList();
    }

    public static LsTransaction findLsTransaction(Long id) {
        if (id == null)
            return null;
        return entityManager().find(LsTransaction.class, id);
    }

    public static List<LsTransaction> findLsTransactionEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LsTransaction o", LsTransaction.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<LsTransaction> findLsTransactionEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM LsTransaction o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsTransaction.class).setFirstResult(firstResult)
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
            LsTransaction attached = LsTransaction.findLsTransaction(this.id);
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
    public LsTransaction merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        LsTransaction merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}