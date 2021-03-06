// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.SaltFormAliasKind;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect SaltFormAliasKind_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager SaltFormAliasKind.entityManager;
    
    public static final List<String> SaltFormAliasKind.fieldNames4OrderClauseFilter = java.util.Arrays.asList("lsType", "kindName");
    
    public static final EntityManager SaltFormAliasKind.entityManager() {
        EntityManager em = new SaltFormAliasKind().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long SaltFormAliasKind.countSaltFormAliasKinds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SaltFormAliasKind o", Long.class).getSingleResult();
    }
    
    public static List<SaltFormAliasKind> SaltFormAliasKind.findAllSaltFormAliasKinds() {
        return entityManager().createQuery("SELECT o FROM SaltFormAliasKind o", SaltFormAliasKind.class).getResultList();
    }
    
    public static List<SaltFormAliasKind> SaltFormAliasKind.findAllSaltFormAliasKinds(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM SaltFormAliasKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, SaltFormAliasKind.class).getResultList();
    }
    
    public static SaltFormAliasKind SaltFormAliasKind.findSaltFormAliasKind(Long id) {
        if (id == null) return null;
        return entityManager().find(SaltFormAliasKind.class, id);
    }
    
    public static List<SaltFormAliasKind> SaltFormAliasKind.findSaltFormAliasKindEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SaltFormAliasKind o", SaltFormAliasKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<SaltFormAliasKind> SaltFormAliasKind.findSaltFormAliasKindEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM SaltFormAliasKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, SaltFormAliasKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void SaltFormAliasKind.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void SaltFormAliasKind.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            SaltFormAliasKind attached = SaltFormAliasKind.findSaltFormAliasKind(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void SaltFormAliasKind.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void SaltFormAliasKind.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public SaltFormAliasKind SaltFormAliasKind.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SaltFormAliasKind merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
