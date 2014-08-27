// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.Experiment;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect AnalysisGroup_Roo_Finder {
    
    public static TypedQuery<AnalysisGroup> AnalysisGroup.findAnalysisGroupsByExperiments(Set<Experiment> experiments) {
        if (experiments == null) throw new IllegalArgumentException("The experiments argument is required");
        EntityManager em = AnalysisGroup.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AnalysisGroup AS o WHERE");
        for (int i = 0; i < experiments.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :experiments_item").append(i).append(" MEMBER OF o.experiments");
        }
        TypedQuery<AnalysisGroup> q = em.createQuery(queryBuilder.toString(), AnalysisGroup.class);
        int experimentsIndex = 0;
        for (Experiment _experiment: experiments) {
            q.setParameter("experiments_item" + experimentsIndex++, _experiment);
        }
        return q;
    }
    
    public static TypedQuery<AnalysisGroup> AnalysisGroup.findAnalysisGroupsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = AnalysisGroup.entityManager();
        TypedQuery<AnalysisGroup> q = em.createQuery("SELECT o FROM AnalysisGroup AS o WHERE o.lsTransaction = :lsTransaction", AnalysisGroup.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }
    
}
