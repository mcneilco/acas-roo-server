package com.labsynch.labseer.domain;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@RooJavaBean

@RooJpaActiveRecord(sequenceName = "LSSEQ_EXPT_PKSEQ" )
public class LsSeqExpt {
	
	

	@Id
    @SequenceGenerator(name = "lsSeqExptGen", sequenceName = "LSSEQ_EXPT_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "lsSeqExptGen")
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
        EntityManager em = new LsSeqExpt().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countLsSeqExpts() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LsSeqExpt o", Long.class).getSingleResult();
    }

	public static List<LsSeqExpt> findAllLsSeqExpts() {
        return entityManager().createQuery("SELECT o FROM LsSeqExpt o", LsSeqExpt.class).getResultList();
    }

	public static List<LsSeqExpt> findAllLsSeqExpts(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LsSeqExpt o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsSeqExpt.class).getResultList();
    }

	public static LsSeqExpt findLsSeqExpt(Long id) {
        if (id == null) return null;
        return entityManager().find(LsSeqExpt.class, id);
    }

	public static List<LsSeqExpt> findLsSeqExptEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LsSeqExpt o", LsSeqExpt.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<LsSeqExpt> findLsSeqExptEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LsSeqExpt o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsSeqExpt.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            LsSeqExpt attached = LsSeqExpt.findLsSeqExpt(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public LsSeqExpt merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        LsSeqExpt merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
