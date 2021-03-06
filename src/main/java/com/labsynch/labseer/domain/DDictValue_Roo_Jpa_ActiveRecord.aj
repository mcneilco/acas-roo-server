// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.DDictValue;
import java.util.List;

privileged aspect DDictValue_Roo_Jpa_ActiveRecord {
    
    public static final List<String> DDictValue.fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "lsType", "lsKind", "lsTypeAndKind", "shortName", "labelText", "description", "comments", "ignored", "displayOrder", "codeName", "id", "version", "entityManager");
    
    public static List<DDictValue> DDictValue.findAllDDictValues(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM DDictValue o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, DDictValue.class).getResultList();
    }
    
    public static List<DDictValue> DDictValue.findDDictValueEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM DDictValue o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, DDictValue.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}
