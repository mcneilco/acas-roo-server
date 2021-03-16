// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.CodeKind;
import java.util.List;

privileged aspect CodeKind_Roo_Jpa_ActiveRecord {
    
    public static final List<String> CodeKind.fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "lsType", "kindName", "lsTypeAndKind", "id", "version", "entityManager");
    
    public static List<CodeKind> CodeKind.findAllCodeKinds(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM CodeKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, CodeKind.class).getResultList();
    }
    
    public static List<CodeKind> CodeKind.findCodeKindEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM CodeKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, CodeKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}