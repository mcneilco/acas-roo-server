// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.ThingPage;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ThingPage_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager ThingPage.entityManager;
    
    public static final List<String> ThingPage.fieldNames4OrderClauseFilter = java.util.Arrays.asList("pageName", "recordedBy", "recordedDate", "pageContent", "modifiedBy", "modifiedDate", "currentEditor", "ignored", "archived", "lsTransaction", "thing");
    
    public static final EntityManager ThingPage.entityManager() {
        EntityManager em = new ThingPage().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long ThingPage.countThingPages() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ThingPage o", Long.class).getSingleResult();
    }
    
    public static List<ThingPage> ThingPage.findAllThingPages() {
        return entityManager().createQuery("SELECT o FROM ThingPage o", ThingPage.class).getResultList();
    }
    
    public static List<ThingPage> ThingPage.findAllThingPages(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ThingPage o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ThingPage.class).getResultList();
    }
    
    public static ThingPage ThingPage.findThingPage(Long id) {
        if (id == null) return null;
        return entityManager().find(ThingPage.class, id);
    }
    
    public static List<ThingPage> ThingPage.findThingPageEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ThingPage o", ThingPage.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<ThingPage> ThingPage.findThingPageEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ThingPage o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ThingPage.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void ThingPage.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void ThingPage.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            ThingPage attached = ThingPage.findThingPage(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void ThingPage.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void ThingPage.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public ThingPage ThingPage.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ThingPage merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
