// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.ValueKind;
import com.labsynch.labseer.domain.ValueType;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect ValueKind_Roo_Finder {
    
    public static TypedQuery<ValueKind> ValueKind.findValueKindsByKindNameEqualsAndLsType(String kindName, ValueType lsType) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = ValueKind.entityManager();
        TypedQuery<ValueKind> q = em.createQuery("SELECT o FROM ValueKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType", ValueKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }
    
    public static TypedQuery<ValueKind> ValueKind.findValueKindsByLsType(ValueType lsType) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = ValueKind.entityManager();
        TypedQuery<ValueKind> q = em.createQuery("SELECT o FROM ValueKind AS o WHERE o.lsType = :lsType", ValueKind.class);
        q.setParameter("lsType", lsType);
        return q;
    }
    
}