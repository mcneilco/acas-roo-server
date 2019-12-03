// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.StructureKind;
import com.labsynch.labseer.domain.StructureType;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect StructureKind_Roo_Finder {
    
    public static Long StructureKind.countFindStructureKindsByKindNameEquals(String kindName) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        EntityManager em = StructureKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM StructureKind AS o WHERE o.kindName = :kindName", Long.class);
        q.setParameter("kindName", kindName);
        return ((Long) q.getSingleResult());
    }
    
    public static Long StructureKind.countFindStructureKindsByKindNameEqualsAndLsType(String kindName, StructureType lsType) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = StructureKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM StructureKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType", Long.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }
    
    public static Long StructureKind.countFindStructureKindsByLsType(StructureType lsType) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = StructureKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM StructureKind AS o WHERE o.lsType = :lsType", Long.class);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<StructureKind> StructureKind.findStructureKindsByKindNameEquals(String kindName) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        EntityManager em = StructureKind.entityManager();
        TypedQuery<StructureKind> q = em.createQuery("SELECT o FROM StructureKind AS o WHERE o.kindName = :kindName", StructureKind.class);
        q.setParameter("kindName", kindName);
        return q;
    }
    
    public static TypedQuery<StructureKind> StructureKind.findStructureKindsByKindNameEquals(String kindName, String sortFieldName, String sortOrder) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        EntityManager em = StructureKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM StructureKind AS o WHERE o.kindName = :kindName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<StructureKind> q = em.createQuery(queryBuilder.toString(), StructureKind.class);
        q.setParameter("kindName", kindName);
        return q;
    }
    
    public static TypedQuery<StructureKind> StructureKind.findStructureKindsByKindNameEqualsAndLsType(String kindName, StructureType lsType) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = StructureKind.entityManager();
        TypedQuery<StructureKind> q = em.createQuery("SELECT o FROM StructureKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType", StructureKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }
    
    public static TypedQuery<StructureKind> StructureKind.findStructureKindsByKindNameEqualsAndLsType(String kindName, StructureType lsType, String sortFieldName, String sortOrder) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = StructureKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM StructureKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<StructureKind> q = em.createQuery(queryBuilder.toString(), StructureKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }
    
    public static TypedQuery<StructureKind> StructureKind.findStructureKindsByLsType(StructureType lsType) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = StructureKind.entityManager();
        TypedQuery<StructureKind> q = em.createQuery("SELECT o FROM StructureKind AS o WHERE o.lsType = :lsType", StructureKind.class);
        q.setParameter("lsType", lsType);
        return q;
    }
    
    public static TypedQuery<StructureKind> StructureKind.findStructureKindsByLsType(StructureType lsType, String sortFieldName, String sortOrder) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = StructureKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM StructureKind AS o WHERE o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<StructureKind> q = em.createQuery(queryBuilder.toString(), StructureKind.class);
        q.setParameter("lsType", lsType);
        return q;
    }
    
}