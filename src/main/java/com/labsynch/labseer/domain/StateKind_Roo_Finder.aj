// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.StateKind;
import com.labsynch.labseer.domain.StateType;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect StateKind_Roo_Finder {
    
    public static Long StateKind.countFindStateKindsByKindNameEquals(String kindName) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        EntityManager em = StateKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM StateKind AS o WHERE o.kindName = :kindName", Long.class);
        q.setParameter("kindName", kindName);
        return ((Long) q.getSingleResult());
    }
    
    public static Long StateKind.countFindStateKindsByKindNameEqualsAndLsType(String kindName, StateType lsType) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = StateKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM StateKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType", Long.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }
    
    public static Long StateKind.countFindStateKindsByLsType(StateType lsType) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = StateKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM StateKind AS o WHERE o.lsType = :lsType", Long.class);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<StateKind> StateKind.findStateKindsByKindNameEquals(String kindName) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        EntityManager em = StateKind.entityManager();
        TypedQuery<StateKind> q = em.createQuery("SELECT o FROM StateKind AS o WHERE o.kindName = :kindName", StateKind.class);
        q.setParameter("kindName", kindName);
        return q;
    }
    
    public static TypedQuery<StateKind> StateKind.findStateKindsByKindNameEquals(String kindName, String sortFieldName, String sortOrder) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        EntityManager em = StateKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM StateKind AS o WHERE o.kindName = :kindName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<StateKind> q = em.createQuery(queryBuilder.toString(), StateKind.class);
        q.setParameter("kindName", kindName);
        return q;
    }
    
    public static TypedQuery<StateKind> StateKind.findStateKindsByKindNameEqualsAndLsType(String kindName, StateType lsType) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = StateKind.entityManager();
        TypedQuery<StateKind> q = em.createQuery("SELECT o FROM StateKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType", StateKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }
    
    public static TypedQuery<StateKind> StateKind.findStateKindsByKindNameEqualsAndLsType(String kindName, StateType lsType, String sortFieldName, String sortOrder) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = StateKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM StateKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<StateKind> q = em.createQuery(queryBuilder.toString(), StateKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }
    
    public static TypedQuery<StateKind> StateKind.findStateKindsByLsType(StateType lsType) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = StateKind.entityManager();
        TypedQuery<StateKind> q = em.createQuery("SELECT o FROM StateKind AS o WHERE o.lsType = :lsType", StateKind.class);
        q.setParameter("lsType", lsType);
        return q;
    }
    
    public static TypedQuery<StateKind> StateKind.findStateKindsByLsType(StateType lsType, String sortFieldName, String sortOrder) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = StateKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM StateKind AS o WHERE o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<StateKind> q = em.createQuery(queryBuilder.toString(), StateKind.class);
        q.setParameter("lsType", lsType);
        return q;
    }
    
}
