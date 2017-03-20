// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.ThingPageArchive;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ThingPageArchive_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager ThingPageArchive.entityManager;
    
    public static final EntityManager ThingPageArchive.entityManager() {
        EntityManager em = new ThingPageArchive().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long ThingPageArchive.countThingPageArchives() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ThingPageArchive o", Long.class).getSingleResult();
    }
    
    public static List<ThingPageArchive> ThingPageArchive.findAllThingPageArchives() {
        return entityManager().createQuery("SELECT o FROM ThingPageArchive o", ThingPageArchive.class).getResultList();
    }
    
    public static ThingPageArchive ThingPageArchive.findThingPageArchive(Long id) {
        if (id == null) return null;
        return entityManager().find(ThingPageArchive.class, id);
    }
    
    public static List<ThingPageArchive> ThingPageArchive.findThingPageArchiveEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ThingPageArchive o", ThingPageArchive.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void ThingPageArchive.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void ThingPageArchive.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            ThingPageArchive attached = ThingPageArchive.findThingPageArchive(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void ThingPageArchive.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void ThingPageArchive.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public ThingPageArchive ThingPageArchive.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ThingPageArchive merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}