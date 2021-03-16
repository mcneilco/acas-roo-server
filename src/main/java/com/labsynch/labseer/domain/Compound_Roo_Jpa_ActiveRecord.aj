// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.Compound;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Compound_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager Compound.entityManager;
    
    public static final List<String> Compound.fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "corpName", "external_id", "CdId", "createdDate", "modifiedDate", "ignored", "deleted");
    
    public static final EntityManager Compound.entityManager() {
        EntityManager em = new Compound().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Compound.countCompounds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Compound o", Long.class).getSingleResult();
    }
    
    public static List<Compound> Compound.findAllCompounds() {
        return entityManager().createQuery("SELECT o FROM Compound o", Compound.class).getResultList();
    }
    
    public static List<Compound> Compound.findAllCompounds(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Compound o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Compound.class).getResultList();
    }
    
    public static Compound Compound.findCompound(Long id) {
        if (id == null) return null;
        return entityManager().find(Compound.class, id);
    }
    
    public static List<Compound> Compound.findCompoundEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Compound o", Compound.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<Compound> Compound.findCompoundEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Compound o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Compound.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void Compound.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Compound.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Compound attached = Compound.findCompound(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Compound.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Compound.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Compound Compound.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Compound merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}