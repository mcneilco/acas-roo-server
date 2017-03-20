// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.ExperimentState;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

privileged aspect ExperimentState_Roo_Jpa_ActiveRecord {
    
    public static long ExperimentState.countExperimentStates() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ExperimentState o", Long.class).getSingleResult();
    }
    
    public static List<ExperimentState> ExperimentState.findAllExperimentStates() {
        return entityManager().createQuery("SELECT o FROM ExperimentState o", ExperimentState.class).getResultList();
    }
    
    public static ExperimentState ExperimentState.findExperimentState(Long id) {
        if (id == null) return null;
        return entityManager().find(ExperimentState.class, id);
    }
    
    public static List<ExperimentState> ExperimentState.findExperimentStateEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ExperimentState o", ExperimentState.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public ExperimentState ExperimentState.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ExperimentState merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}