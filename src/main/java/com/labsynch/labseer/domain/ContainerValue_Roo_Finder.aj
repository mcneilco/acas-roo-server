// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ContainerValue;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect ContainerValue_Roo_Finder {
    
    public static TypedQuery<ContainerValue> ContainerValue.findContainerValuesByIgnoredNot(boolean ignored) {
        EntityManager em = ContainerValue.entityManager();
        TypedQuery<ContainerValue> q = em.createQuery("SELECT o FROM ContainerValue AS o WHERE o.ignored IS NOT :ignored", ContainerValue.class);
        q.setParameter("ignored", ignored);
        return q;
    }
    
    public static TypedQuery<ContainerValue> ContainerValue.findContainerValuesByLsState(ContainerState lsState) {
        if (lsState == null) throw new IllegalArgumentException("The lsState argument is required");
        EntityManager em = ContainerValue.entityManager();
        TypedQuery<ContainerValue> q = em.createQuery("SELECT o FROM ContainerValue AS o WHERE o.lsState = :lsState", ContainerValue.class);
        q.setParameter("lsState", lsState);
        return q;
    }
    
}