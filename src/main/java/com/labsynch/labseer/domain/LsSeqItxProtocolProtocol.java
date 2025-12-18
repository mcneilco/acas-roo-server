package com.labsynch.labseer.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Version;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable

public class LsSeqItxProtocolProtocol {

    @Id
    @SequenceGenerator(name = "lsSeqItxProtocolProtocolGen", sequenceName = "LSSEQ_ITXPROTPROT_PKSEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "lsSeqItxProtocolProtocolGen")
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

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

    public static final EntityManager entityManager() {
        EntityManager em = new LsSeqItxProtocolProtocol().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countLsSeqItxProtocolProtocols() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LsSeqItxProtocolProtocol o", Long.class)
                .getSingleResult();
    }

    public static List<LsSeqItxProtocolProtocol> findAllLsSeqItxProtocolProtocols() {
        return entityManager().createQuery("SELECT o FROM LsSeqItxProtocolProtocol o", LsSeqItxProtocolProtocol.class)
                .getResultList();
    }

    public static List<LsSeqItxProtocolProtocol> findAllLsSeqItxProtocolProtocols(String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM LsSeqItxProtocolProtocol o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsSeqItxProtocolProtocol.class).getResultList();
    }

    public static LsSeqItxProtocolProtocol findLsSeqItxProtocolProtocol(Long id) {
        if (id == null)
            return null;
        return entityManager().find(LsSeqItxProtocolProtocol.class, id);
    }

    public static List<LsSeqItxProtocolProtocol> findLsSeqItxProtocolProtocolEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LsSeqItxProtocolProtocol o", LsSeqItxProtocolProtocol.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<LsSeqItxProtocolProtocol> findLsSeqItxProtocolProtocolEntries(int firstResult, int maxResults,
            String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LsSeqItxProtocolProtocol o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsSeqItxProtocolProtocol.class).setFirstResult(firstResult)
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
            LsSeqItxProtocolProtocol attached = LsSeqItxProtocolProtocol.findLsSeqItxProtocolProtocol(this.id);
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
    public LsSeqItxProtocolProtocol merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        LsSeqItxProtocolProtocol merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
