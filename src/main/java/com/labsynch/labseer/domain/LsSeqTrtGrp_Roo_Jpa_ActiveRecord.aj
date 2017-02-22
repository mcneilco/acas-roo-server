// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.LsSeqTrtGrp;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect LsSeqTrtGrp_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager LsSeqTrtGrp.entityManager;
    
    public static final EntityManager LsSeqTrtGrp.entityManager() {
        EntityManager em = new LsSeqTrtGrp().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long LsSeqTrtGrp.countLsSeqTrtGrps() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LsSeqTrtGrp o", Long.class).getSingleResult();
    }
    
    public static List<LsSeqTrtGrp> LsSeqTrtGrp.findAllLsSeqTrtGrps() {
        return entityManager().createQuery("SELECT o FROM LsSeqTrtGrp o", LsSeqTrtGrp.class).getResultList();
    }
    
    public static LsSeqTrtGrp LsSeqTrtGrp.findLsSeqTrtGrp(Long id) {
        if (id == null) return null;
        return entityManager().find(LsSeqTrtGrp.class, id);
    }
    
    public static List<LsSeqTrtGrp> LsSeqTrtGrp.findLsSeqTrtGrpEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LsSeqTrtGrp o", LsSeqTrtGrp.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void LsSeqTrtGrp.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void LsSeqTrtGrp.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            LsSeqTrtGrp attached = LsSeqTrtGrp.findLsSeqTrtGrp(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void LsSeqTrtGrp.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void LsSeqTrtGrp.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public LsSeqTrtGrp LsSeqTrtGrp.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        LsSeqTrtGrp merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}