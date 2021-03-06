// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.UnitType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect UnitType_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager UnitType.entityManager;
    
    public static final List<String> UnitType.fieldNames4OrderClauseFilter = java.util.Arrays.asList("typeName", "id", "version");
    
    public static final EntityManager UnitType.entityManager() {
        EntityManager em = new UnitType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long UnitType.countUnitTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM UnitType o", Long.class).getSingleResult();
    }
    
    public static List<UnitType> UnitType.findAllUnitTypes() {
        return entityManager().createQuery("SELECT o FROM UnitType o", UnitType.class).getResultList();
    }
    
    public static List<UnitType> UnitType.findAllUnitTypes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM UnitType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, UnitType.class).getResultList();
    }
    
    public static UnitType UnitType.findUnitType(Long id) {
        if (id == null) return null;
        return entityManager().find(UnitType.class, id);
    }
    
    public static List<UnitType> UnitType.findUnitTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM UnitType o", UnitType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<UnitType> UnitType.findUnitTypeEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM UnitType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, UnitType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void UnitType.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void UnitType.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            UnitType attached = UnitType.findUnitType(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void UnitType.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void UnitType.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public UnitType UnitType.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        UnitType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
