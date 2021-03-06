// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.domain.TreatmentGroupState;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect TreatmentGroupState_Roo_Finder {
    
    public static Long TreatmentGroupState.countFindTreatmentGroupStatesByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = TreatmentGroupState.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM TreatmentGroupState AS o WHERE o.lsTransaction = :lsTransaction", Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }
    
    public static Long TreatmentGroupState.countFindTreatmentGroupStatesByLsTypeAndKindEquals(String lsTypeAndKind) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = TreatmentGroupState.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM TreatmentGroupState AS o WHERE o.lsTypeAndKind = :lsTypeAndKind", Long.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return ((Long) q.getSingleResult());
    }
    
    public static Long TreatmentGroupState.countFindTreatmentGroupStatesByTreatmentGroup(TreatmentGroup treatmentGroup) {
        if (treatmentGroup == null) throw new IllegalArgumentException("The treatmentGroup argument is required");
        EntityManager em = TreatmentGroupState.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM TreatmentGroupState AS o WHERE o.treatmentGroup = :treatmentGroup", Long.class);
        q.setParameter("treatmentGroup", treatmentGroup);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<TreatmentGroupState> TreatmentGroupState.findTreatmentGroupStatesByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = TreatmentGroupState.entityManager();
        TypedQuery<TreatmentGroupState> q = em.createQuery("SELECT o FROM TreatmentGroupState AS o WHERE o.lsTransaction = :lsTransaction", TreatmentGroupState.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }
    
    public static TypedQuery<TreatmentGroupState> TreatmentGroupState.findTreatmentGroupStatesByLsTransactionEquals(Long lsTransaction, String sortFieldName, String sortOrder) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = TreatmentGroupState.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM TreatmentGroupState AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<TreatmentGroupState> q = em.createQuery(queryBuilder.toString(), TreatmentGroupState.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }
    
    public static TypedQuery<TreatmentGroupState> TreatmentGroupState.findTreatmentGroupStatesByLsTypeAndKindEquals(String lsTypeAndKind) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = TreatmentGroupState.entityManager();
        TypedQuery<TreatmentGroupState> q = em.createQuery("SELECT o FROM TreatmentGroupState AS o WHERE o.lsTypeAndKind = :lsTypeAndKind", TreatmentGroupState.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return q;
    }
    
    public static TypedQuery<TreatmentGroupState> TreatmentGroupState.findTreatmentGroupStatesByLsTypeAndKindEquals(String lsTypeAndKind, String sortFieldName, String sortOrder) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = TreatmentGroupState.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM TreatmentGroupState AS o WHERE o.lsTypeAndKind = :lsTypeAndKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<TreatmentGroupState> q = em.createQuery(queryBuilder.toString(), TreatmentGroupState.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return q;
    }
    
    public static TypedQuery<TreatmentGroupState> TreatmentGroupState.findTreatmentGroupStatesByTreatmentGroup(TreatmentGroup treatmentGroup) {
        if (treatmentGroup == null) throw new IllegalArgumentException("The treatmentGroup argument is required");
        EntityManager em = TreatmentGroupState.entityManager();
        TypedQuery<TreatmentGroupState> q = em.createQuery("SELECT o FROM TreatmentGroupState AS o WHERE o.treatmentGroup = :treatmentGroup", TreatmentGroupState.class);
        q.setParameter("treatmentGroup", treatmentGroup);
        return q;
    }
    
    public static TypedQuery<TreatmentGroupState> TreatmentGroupState.findTreatmentGroupStatesByTreatmentGroup(TreatmentGroup treatmentGroup, String sortFieldName, String sortOrder) {
        if (treatmentGroup == null) throw new IllegalArgumentException("The treatmentGroup argument is required");
        EntityManager em = TreatmentGroupState.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM TreatmentGroupState AS o WHERE o.treatmentGroup = :treatmentGroup");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<TreatmentGroupState> q = em.createQuery(queryBuilder.toString(), TreatmentGroupState.class);
        q.setParameter("treatmentGroup", treatmentGroup);
        return q;
    }
    
}
