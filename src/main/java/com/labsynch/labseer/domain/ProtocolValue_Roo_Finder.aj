// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.ProtocolState;
import com.labsynch.labseer.domain.ProtocolValue;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect ProtocolValue_Roo_Finder {
    
    public static TypedQuery<ProtocolValue> ProtocolValue.findProtocolValuesByLsKindEqualsAndCodeValueLike(String lsKind, String codeValue) {
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (codeValue == null || codeValue.length() == 0) throw new IllegalArgumentException("The codeValue argument is required");
        codeValue = codeValue.replace('*', '%');
        if (codeValue.charAt(0) != '%') {
            codeValue = "%" + codeValue;
        }
        if (codeValue.charAt(codeValue.length() - 1) != '%') {
            codeValue = codeValue + "%";
        }
        EntityManager em = ProtocolValue.entityManager();
        TypedQuery<ProtocolValue> q = em.createQuery("SELECT o FROM ProtocolValue AS o WHERE o.lsKind = :lsKind  AND LOWER(o.codeValue) LIKE LOWER(:codeValue)", ProtocolValue.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("codeValue", codeValue);
        return q;
    }
    
    public static TypedQuery<ProtocolValue> ProtocolValue.findProtocolValuesByLsKindEqualsAndStringValueLike(String lsKind, String stringValue) {
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (stringValue == null || stringValue.length() == 0) throw new IllegalArgumentException("The stringValue argument is required");
        stringValue = stringValue.replace('*', '%');
        if (stringValue.charAt(0) != '%') {
            stringValue = "%" + stringValue;
        }
        if (stringValue.charAt(stringValue.length() - 1) != '%') {
            stringValue = stringValue + "%";
        }
        EntityManager em = ProtocolValue.entityManager();
        TypedQuery<ProtocolValue> q = em.createQuery("SELECT o FROM ProtocolValue AS o WHERE o.lsKind = :lsKind  AND LOWER(o.stringValue) LIKE LOWER(:stringValue)", ProtocolValue.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("stringValue", stringValue);
        return q;
    }
    
    public static TypedQuery<ProtocolValue> ProtocolValue.findProtocolValuesByLsState(ProtocolState lsState) {
        if (lsState == null) throw new IllegalArgumentException("The lsState argument is required");
        EntityManager em = ProtocolValue.entityManager();
        TypedQuery<ProtocolValue> q = em.createQuery("SELECT o FROM ProtocolValue AS o WHERE o.lsState = :lsState", ProtocolValue.class);
        q.setParameter("lsState", lsState);
        return q;
    }
    
    public static TypedQuery<ProtocolValue> ProtocolValue.findProtocolValuesByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ProtocolValue.entityManager();
        TypedQuery<ProtocolValue> q = em.createQuery("SELECT o FROM ProtocolValue AS o WHERE o.lsTransaction = :lsTransaction", ProtocolValue.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }
    
}