// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.RDKitSaltStructure;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect RDKitSaltStructure_Roo_Jpa_ActiveRecord {
    
    public static final List<String> RDKitSaltStructure.fieldNames4OrderClauseFilter = java.util.Arrays.asList("");
    
    public static long RDKitSaltStructure.countRDKitSaltStructures() {
        return entityManager().createQuery("SELECT COUNT(o) FROM RDKitSaltStructure o", Long.class).getSingleResult();
    }
    
    public static List<RDKitSaltStructure> RDKitSaltStructure.findAllRDKitSaltStructures() {
        return entityManager().createQuery("SELECT o FROM RDKitSaltStructure o", RDKitSaltStructure.class).getResultList();
    }
    
    public static List<RDKitSaltStructure> RDKitSaltStructure.findAllRDKitSaltStructures(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM RDKitSaltStructure o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, RDKitSaltStructure.class).getResultList();
    }
    
    public static RDKitSaltStructure RDKitSaltStructure.findRDKitSaltStructure(Long id) {
        if (id == null) return null;
        return entityManager().find(RDKitSaltStructure.class, id);
    }
    
    public static List<RDKitSaltStructure> RDKitSaltStructure.findRDKitSaltStructureEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM RDKitSaltStructure o", RDKitSaltStructure.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<RDKitSaltStructure> RDKitSaltStructure.findRDKitSaltStructureEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM RDKitSaltStructure o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, RDKitSaltStructure.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public RDKitSaltStructure RDKitSaltStructure.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        RDKitSaltStructure merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}