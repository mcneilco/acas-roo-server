// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.TreatmentGroup;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect TreatmentGroup_Roo_Finder {
    
    public static TypedQuery<TreatmentGroup> TreatmentGroup.findTreatmentGroupsByAnalysisGroups(Set<AnalysisGroup> analysisGroups) {
        if (analysisGroups == null) throw new IllegalArgumentException("The analysisGroups argument is required");
        EntityManager em = TreatmentGroup.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM TreatmentGroup AS o WHERE");
        for (int i = 0; i < analysisGroups.size(); i++) {
            if (i > 0) queryBuilder.append(" AND");
            queryBuilder.append(" :analysisGroups_item").append(i).append(" MEMBER OF o.analysisGroups");
        }
        TypedQuery<TreatmentGroup> q = em.createQuery(queryBuilder.toString(), TreatmentGroup.class);
        int analysisGroupsIndex = 0;
        for (AnalysisGroup _analysisgroup: analysisGroups) {
            q.setParameter("analysisGroups_item" + analysisGroupsIndex++, _analysisgroup);
        }
        return q;
    }
    
    public static TypedQuery<TreatmentGroup> TreatmentGroup.findTreatmentGroupsByCodeNameEquals(String codeName) {
        if (codeName == null || codeName.length() == 0) throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = TreatmentGroup.entityManager();
        TypedQuery<TreatmentGroup> q = em.createQuery("SELECT o FROM TreatmentGroup AS o WHERE o.codeName = :codeName", TreatmentGroup.class);
        q.setParameter("codeName", codeName);
        return q;
    }
    
    public static TypedQuery<TreatmentGroup> TreatmentGroup.findTreatmentGroupsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = TreatmentGroup.entityManager();
        TypedQuery<TreatmentGroup> q = em.createQuery("SELECT o FROM TreatmentGroup AS o WHERE o.lsTransaction = :lsTransaction", TreatmentGroup.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }
    
}