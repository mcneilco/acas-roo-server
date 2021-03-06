// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.ContainerLabel;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ContainerLabel_Roo_Jpa_ActiveRecord {
    
    public static final List<String> ContainerLabel.fieldNames4OrderClauseFilter = java.util.Arrays.asList("container");
    
    public static long ContainerLabel.countContainerLabels() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ContainerLabel o", Long.class).getSingleResult();
    }
    
    public static List<ContainerLabel> ContainerLabel.findAllContainerLabels() {
        return entityManager().createQuery("SELECT o FROM ContainerLabel o", ContainerLabel.class).getResultList();
    }
    
    public static List<ContainerLabel> ContainerLabel.findAllContainerLabels(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ContainerLabel o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ContainerLabel.class).getResultList();
    }
    
    public static ContainerLabel ContainerLabel.findContainerLabel(Long id) {
        if (id == null) return null;
        return entityManager().find(ContainerLabel.class, id);
    }
    
    public static List<ContainerLabel> ContainerLabel.findContainerLabelEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ContainerLabel o", ContainerLabel.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<ContainerLabel> ContainerLabel.findContainerLabelEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ContainerLabel o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ContainerLabel.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public ContainerLabel ContainerLabel.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ContainerLabel merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
