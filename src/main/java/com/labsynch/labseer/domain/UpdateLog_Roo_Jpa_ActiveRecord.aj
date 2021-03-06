// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.UpdateLog;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect UpdateLog_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager UpdateLog.entityManager;
    
    public static final List<String> UpdateLog.fieldNames4OrderClauseFilter = java.util.Arrays.asList("thing", "updateAction", "comments", "lsTransaction", "recordedDate", "recordedBy");
    
    public static final EntityManager UpdateLog.entityManager() {
        EntityManager em = new UpdateLog().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long UpdateLog.countUpdateLogs() {
        return entityManager().createQuery("SELECT COUNT(o) FROM UpdateLog o", Long.class).getSingleResult();
    }
    
    public static List<UpdateLog> UpdateLog.findAllUpdateLogs() {
        return entityManager().createQuery("SELECT o FROM UpdateLog o", UpdateLog.class).getResultList();
    }
    
    public static List<UpdateLog> UpdateLog.findAllUpdateLogs(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM UpdateLog o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, UpdateLog.class).getResultList();
    }
    
    public static UpdateLog UpdateLog.findUpdateLog(Long id) {
        if (id == null) return null;
        return entityManager().find(UpdateLog.class, id);
    }
    
    public static List<UpdateLog> UpdateLog.findUpdateLogEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM UpdateLog o", UpdateLog.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<UpdateLog> UpdateLog.findUpdateLogEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM UpdateLog o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, UpdateLog.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void UpdateLog.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void UpdateLog.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            UpdateLog attached = UpdateLog.findUpdateLog(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void UpdateLog.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void UpdateLog.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public UpdateLog UpdateLog.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        UpdateLog merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
