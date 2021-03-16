// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.ExperimentKind;
import java.util.List;

privileged aspect ExperimentKind_Roo_Jpa_ActiveRecord {
    
    public static final List<String> ExperimentKind.fieldNames4OrderClauseFilter = java.util.Arrays.asList("lsType", "kindName", "lsTypeAndKind", "id", "version", "entityManager");
    
    public static List<ExperimentKind> ExperimentKind.findAllExperimentKinds(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ExperimentKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ExperimentKind.class).getResultList();
    }
    
    public static List<ExperimentKind> ExperimentKind.findExperimentKindEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ExperimentKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ExperimentKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}