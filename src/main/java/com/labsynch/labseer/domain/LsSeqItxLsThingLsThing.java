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
public class LsSeqItxLsThingLsThing {

    @Id
    @SequenceGenerator(name = "lsSeqItxLsThingLsThingGen", sequenceName = "LSSEQ_ITXTHGTHG_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "lsSeqItxLsThingLsThingGen")
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
        EntityManager em = new LsSeqItxLsThingLsThing().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countLsSeqItxLsThingLsThings() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LsSeqItxLsThingLsThing o", Long.class)
                .getSingleResult();
    }

    public static List<LsSeqItxLsThingLsThing> findAllLsSeqItxLsThingLsThings() {
        return entityManager().createQuery("SELECT o FROM LsSeqItxLsThingLsThing o", LsSeqItxLsThingLsThing.class)
                .getResultList();
    }

    public static List<LsSeqItxLsThingLsThing> findAllLsSeqItxLsThingLsThings(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LsSeqItxLsThingLsThing o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsSeqItxLsThingLsThing.class).getResultList();
    }

    public static LsSeqItxLsThingLsThing findLsSeqItxLsThingLsThing(Long id) {
        if (id == null)
            return null;
        return entityManager().find(LsSeqItxLsThingLsThing.class, id);
    }

    public static List<LsSeqItxLsThingLsThing> findLsSeqItxLsThingLsThingEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LsSeqItxLsThingLsThing o", LsSeqItxLsThingLsThing.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<LsSeqItxLsThingLsThing> findLsSeqItxLsThingLsThingEntries(int firstResult, int maxResults,
            String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LsSeqItxLsThingLsThing o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsSeqItxLsThingLsThing.class).setFirstResult(firstResult)
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
            LsSeqItxLsThingLsThing attached = LsSeqItxLsThingLsThing.findLsSeqItxLsThingLsThing(this.id);
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
    public LsSeqItxLsThingLsThing merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        LsSeqItxLsThingLsThing merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
