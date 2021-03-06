// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.ItxExperimentExperiment;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ItxExperimentExperiment_Roo_Jpa_ActiveRecord {
    
    public static final List<String> ItxExperimentExperiment.fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "firstExperiment", "secondExperiment", "lsStates");
    
    public static long ItxExperimentExperiment.countItxExperimentExperiments() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ItxExperimentExperiment o", Long.class).getSingleResult();
    }
    
    public static List<ItxExperimentExperiment> ItxExperimentExperiment.findAllItxExperimentExperiments() {
        return entityManager().createQuery("SELECT o FROM ItxExperimentExperiment o", ItxExperimentExperiment.class).getResultList();
    }
    
    public static List<ItxExperimentExperiment> ItxExperimentExperiment.findAllItxExperimentExperiments(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxExperimentExperiment o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxExperimentExperiment.class).getResultList();
    }
    
    public static ItxExperimentExperiment ItxExperimentExperiment.findItxExperimentExperiment(Long id) {
        if (id == null) return null;
        return entityManager().find(ItxExperimentExperiment.class, id);
    }
    
    public static List<ItxExperimentExperiment> ItxExperimentExperiment.findItxExperimentExperimentEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ItxExperimentExperiment o", ItxExperimentExperiment.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<ItxExperimentExperiment> ItxExperimentExperiment.findItxExperimentExperimentEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxExperimentExperiment o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxExperimentExperiment.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public ItxExperimentExperiment ItxExperimentExperiment.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ItxExperimentExperiment merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
