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

@Configurable
@Entity
public class LsSeqTrtGrp {

    @PersistenceContext
    transient EntityManager entityManager;

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

    public static final EntityManager entityManager() {
        EntityManager em = new LsSeqTrtGrp().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countLsSeqTrtGrps() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LsSeqTrtGrp o", Long.class).getSingleResult();
    }

    public static List<LsSeqTrtGrp> findAllLsSeqTrtGrps() {
        return entityManager().createQuery("SELECT o FROM LsSeqTrtGrp o", LsSeqTrtGrp.class).getResultList();
    }

    public static List<LsSeqTrtGrp> findAllLsSeqTrtGrps(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LsSeqTrtGrp o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsSeqTrtGrp.class).getResultList();
    }

    public static LsSeqTrtGrp findLsSeqTrtGrp(Long id) {
        if (id == null)
            return null;
        return entityManager().find(LsSeqTrtGrp.class, id);
    }

    public static List<LsSeqTrtGrp> findLsSeqTrtGrpEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LsSeqTrtGrp o", LsSeqTrtGrp.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public static List<LsSeqTrtGrp> findLsSeqTrtGrpEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM LsSeqTrtGrp o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsSeqTrtGrp.class).setFirstResult(firstResult)
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
            LsSeqTrtGrp attached = LsSeqTrtGrp.findLsSeqTrtGrp(this.id);
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
    public LsSeqTrtGrp merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        LsSeqTrtGrp merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    @Id
    @SequenceGenerator(name = "lsSeqTrtGrpGen", sequenceName = "LSSEQ_TRTGRP_PKSEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "lsSeqTrtGrpGen")
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
}
