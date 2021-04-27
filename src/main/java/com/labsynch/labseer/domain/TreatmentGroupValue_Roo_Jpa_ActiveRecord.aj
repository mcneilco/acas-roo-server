// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.TreatmentGroupValue;
import java.util.List;

privileged aspect TreatmentGroupValue_Roo_Jpa_ActiveRecord {
    
    public static final List<String> TreatmentGroupValue.fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "lsState");
    
    public static List<TreatmentGroupValue> TreatmentGroupValue.findAllTreatmentGroupValues(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM TreatmentGroupValue o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, TreatmentGroupValue.class).getResultList();
    }
    
    public static List<TreatmentGroupValue> TreatmentGroupValue.findTreatmentGroupValueEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM TreatmentGroupValue o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, TreatmentGroupValue.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}