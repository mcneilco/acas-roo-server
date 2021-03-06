// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.ContainerKind;
import com.labsynch.labseer.domain.ContainerType;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect ContainerKind_Roo_Finder {
    
    public static Long ContainerKind.countFindContainerKindsByKindNameEqualsAndLsType(String kindName, ContainerType lsType) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = ContainerKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ContainerKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType", Long.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<ContainerKind> ContainerKind.findContainerKindsByKindNameEqualsAndLsType(String kindName, ContainerType lsType) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = ContainerKind.entityManager();
        TypedQuery<ContainerKind> q = em.createQuery("SELECT o FROM ContainerKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType", ContainerKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }
    
    public static TypedQuery<ContainerKind> ContainerKind.findContainerKindsByKindNameEqualsAndLsType(String kindName, ContainerType lsType, String sortFieldName, String sortOrder) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = ContainerKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ContainerKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ContainerKind> q = em.createQuery(queryBuilder.toString(), ContainerKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }
    
}
