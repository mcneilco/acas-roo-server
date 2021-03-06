// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.LsThing;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect LsThing_Roo_Jpa_ActiveRecord {
    
    public static final List<String> LsThing.fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "lsStates", "lsLabels", "lsTags", "firstLsThings", "secondLsThings");
    
    public static long LsThing.countLsThings() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LsThing o", Long.class).getSingleResult();
    }
    
    public static List<LsThing> LsThing.findAllLsThings() {
        return entityManager().createQuery("SELECT o FROM LsThing o", LsThing.class).getResultList();
    }
    
    public static List<LsThing> LsThing.findAllLsThings(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LsThing o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsThing.class).getResultList();
    }
    
    public static LsThing LsThing.findLsThing(Long id) {
        if (id == null) return null;
        return entityManager().find(LsThing.class, id);
    }
    
    public static List<LsThing> LsThing.findLsThingEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LsThing o", LsThing.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<LsThing> LsThing.findLsThingEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LsThing o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsThing.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public LsThing LsThing.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        LsThing merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
