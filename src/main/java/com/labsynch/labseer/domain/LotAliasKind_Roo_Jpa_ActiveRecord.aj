// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.LotAliasKind;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect LotAliasKind_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager LotAliasKind.entityManager;
    
    public static final List<String> LotAliasKind.fieldNames4OrderClauseFilter = java.util.Arrays.asList("lsType", "kindName");
    
    public static final EntityManager LotAliasKind.entityManager() {
        EntityManager em = new LotAliasKind().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long LotAliasKind.countLotAliasKinds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LotAliasKind o", Long.class).getSingleResult();
    }
    
    public static List<LotAliasKind> LotAliasKind.findAllLotAliasKinds() {
        return entityManager().createQuery("SELECT o FROM LotAliasKind o", LotAliasKind.class).getResultList();
    }
    
    public static List<LotAliasKind> LotAliasKind.findAllLotAliasKinds(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LotAliasKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LotAliasKind.class).getResultList();
    }
    
    public static LotAliasKind LotAliasKind.findLotAliasKind(Long id) {
        if (id == null) return null;
        return entityManager().find(LotAliasKind.class, id);
    }
    
    public static List<LotAliasKind> LotAliasKind.findLotAliasKindEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LotAliasKind o", LotAliasKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<LotAliasKind> LotAliasKind.findLotAliasKindEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LotAliasKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LotAliasKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void LotAliasKind.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void LotAliasKind.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            LotAliasKind attached = LotAliasKind.findLotAliasKind(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void LotAliasKind.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void LotAliasKind.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public LotAliasKind LotAliasKind.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        LotAliasKind merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
