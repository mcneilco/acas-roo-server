// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.AuthorState;
import com.labsynch.labseer.domain.AuthorValue;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect AuthorValue_Roo_Finder {
    
    public static Long AuthorValue.countFindAuthorValuesByCodeValueEquals(String codeValue) {
        if (codeValue == null || codeValue.length() == 0) throw new IllegalArgumentException("The codeValue argument is required");
        EntityManager em = AuthorValue.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM AuthorValue AS o WHERE o.codeValue = :codeValue", Long.class);
        q.setParameter("codeValue", codeValue);
        return ((Long) q.getSingleResult());
    }
    
    public static Long AuthorValue.countFindAuthorValuesByIgnoredNotAndCodeValueEquals(boolean ignored, String codeValue) {
        if (codeValue == null || codeValue.length() == 0) throw new IllegalArgumentException("The codeValue argument is required");
        EntityManager em = AuthorValue.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM AuthorValue AS o WHERE o.ignored IS NOT :ignored  AND o.codeValue = :codeValue", Long.class);
        q.setParameter("ignored", ignored);
        q.setParameter("codeValue", codeValue);
        return ((Long) q.getSingleResult());
    }
    
    public static Long AuthorValue.countFindAuthorValuesByLsState(AuthorState lsState) {
        if (lsState == null) throw new IllegalArgumentException("The lsState argument is required");
        EntityManager em = AuthorValue.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM AuthorValue AS o WHERE o.lsState = :lsState", Long.class);
        q.setParameter("lsState", lsState);
        return ((Long) q.getSingleResult());
    }
    
    public static Long AuthorValue.countFindAuthorValuesByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = AuthorValue.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM AuthorValue AS o WHERE o.lsTransaction = :lsTransaction", Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }
    
    public static Long AuthorValue.countFindAuthorValuesByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = AuthorValue.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM AuthorValue AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind", Long.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return ((Long) q.getSingleResult());
    }
    
    public static Long AuthorValue.countFindAuthorValuesByLsTypeEqualsAndLsKindEqualsAndStringValueLikeAndIgnoredNot(String lsType, String lsKind, String stringValue, boolean ignored) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (stringValue == null || stringValue.length() == 0) throw new IllegalArgumentException("The stringValue argument is required");
        stringValue = stringValue.replace('*', '%');
        if (stringValue.charAt(0) != '%') {
            stringValue = "%" + stringValue;
        }
        if (stringValue.charAt(stringValue.length() - 1) != '%') {
            stringValue = stringValue + "%";
        }
        EntityManager em = AuthorValue.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM AuthorValue AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind  AND LOWER(o.stringValue) LIKE LOWER(:stringValue)  AND o.ignored IS NOT :ignored", Long.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("stringValue", stringValue);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<AuthorValue> AuthorValue.findAuthorValuesByCodeValueEquals(String codeValue) {
        if (codeValue == null || codeValue.length() == 0) throw new IllegalArgumentException("The codeValue argument is required");
        EntityManager em = AuthorValue.entityManager();
        TypedQuery<AuthorValue> q = em.createQuery("SELECT o FROM AuthorValue AS o WHERE o.codeValue = :codeValue", AuthorValue.class);
        q.setParameter("codeValue", codeValue);
        return q;
    }
    
    public static TypedQuery<AuthorValue> AuthorValue.findAuthorValuesByCodeValueEquals(String codeValue, String sortFieldName, String sortOrder) {
        if (codeValue == null || codeValue.length() == 0) throw new IllegalArgumentException("The codeValue argument is required");
        EntityManager em = AuthorValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AuthorValue AS o WHERE o.codeValue = :codeValue");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AuthorValue> q = em.createQuery(queryBuilder.toString(), AuthorValue.class);
        q.setParameter("codeValue", codeValue);
        return q;
    }
    
    public static TypedQuery<AuthorValue> AuthorValue.findAuthorValuesByIgnoredNotAndCodeValueEquals(boolean ignored, String codeValue) {
        if (codeValue == null || codeValue.length() == 0) throw new IllegalArgumentException("The codeValue argument is required");
        EntityManager em = AuthorValue.entityManager();
        TypedQuery<AuthorValue> q = em.createQuery("SELECT o FROM AuthorValue AS o WHERE o.ignored IS NOT :ignored  AND o.codeValue = :codeValue", AuthorValue.class);
        q.setParameter("ignored", ignored);
        q.setParameter("codeValue", codeValue);
        return q;
    }
    
    public static TypedQuery<AuthorValue> AuthorValue.findAuthorValuesByIgnoredNotAndCodeValueEquals(boolean ignored, String codeValue, String sortFieldName, String sortOrder) {
        if (codeValue == null || codeValue.length() == 0) throw new IllegalArgumentException("The codeValue argument is required");
        EntityManager em = AuthorValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AuthorValue AS o WHERE o.ignored IS NOT :ignored  AND o.codeValue = :codeValue");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AuthorValue> q = em.createQuery(queryBuilder.toString(), AuthorValue.class);
        q.setParameter("ignored", ignored);
        q.setParameter("codeValue", codeValue);
        return q;
    }
    
    public static TypedQuery<AuthorValue> AuthorValue.findAuthorValuesByLsState(AuthorState lsState) {
        if (lsState == null) throw new IllegalArgumentException("The lsState argument is required");
        EntityManager em = AuthorValue.entityManager();
        TypedQuery<AuthorValue> q = em.createQuery("SELECT o FROM AuthorValue AS o WHERE o.lsState = :lsState", AuthorValue.class);
        q.setParameter("lsState", lsState);
        return q;
    }
    
    public static TypedQuery<AuthorValue> AuthorValue.findAuthorValuesByLsState(AuthorState lsState, String sortFieldName, String sortOrder) {
        if (lsState == null) throw new IllegalArgumentException("The lsState argument is required");
        EntityManager em = AuthorValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AuthorValue AS o WHERE o.lsState = :lsState");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AuthorValue> q = em.createQuery(queryBuilder.toString(), AuthorValue.class);
        q.setParameter("lsState", lsState);
        return q;
    }
    
    public static TypedQuery<AuthorValue> AuthorValue.findAuthorValuesByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = AuthorValue.entityManager();
        TypedQuery<AuthorValue> q = em.createQuery("SELECT o FROM AuthorValue AS o WHERE o.lsTransaction = :lsTransaction", AuthorValue.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }
    
    public static TypedQuery<AuthorValue> AuthorValue.findAuthorValuesByLsTransactionEquals(Long lsTransaction, String sortFieldName, String sortOrder) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = AuthorValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AuthorValue AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AuthorValue> q = em.createQuery(queryBuilder.toString(), AuthorValue.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }
    
    public static TypedQuery<AuthorValue> AuthorValue.findAuthorValuesByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = AuthorValue.entityManager();
        TypedQuery<AuthorValue> q = em.createQuery("SELECT o FROM AuthorValue AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind", AuthorValue.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }
    
    public static TypedQuery<AuthorValue> AuthorValue.findAuthorValuesByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind, String sortFieldName, String sortOrder) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = AuthorValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AuthorValue AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AuthorValue> q = em.createQuery(queryBuilder.toString(), AuthorValue.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }
    
    public static TypedQuery<AuthorValue> AuthorValue.findAuthorValuesByLsTypeEqualsAndLsKindEqualsAndStringValueLikeAndIgnoredNot(String lsType, String lsKind, String stringValue, boolean ignored) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (stringValue == null || stringValue.length() == 0) throw new IllegalArgumentException("The stringValue argument is required");
        stringValue = stringValue.replace('*', '%');
        if (stringValue.charAt(0) != '%') {
            stringValue = "%" + stringValue;
        }
        if (stringValue.charAt(stringValue.length() - 1) != '%') {
            stringValue = stringValue + "%";
        }
        EntityManager em = AuthorValue.entityManager();
        TypedQuery<AuthorValue> q = em.createQuery("SELECT o FROM AuthorValue AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind  AND LOWER(o.stringValue) LIKE LOWER(:stringValue)  AND o.ignored IS NOT :ignored", AuthorValue.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("stringValue", stringValue);
        q.setParameter("ignored", ignored);
        return q;
    }
    
    public static TypedQuery<AuthorValue> AuthorValue.findAuthorValuesByLsTypeEqualsAndLsKindEqualsAndStringValueLikeAndIgnoredNot(String lsType, String lsKind, String stringValue, boolean ignored, String sortFieldName, String sortOrder) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (stringValue == null || stringValue.length() == 0) throw new IllegalArgumentException("The stringValue argument is required");
        stringValue = stringValue.replace('*', '%');
        if (stringValue.charAt(0) != '%') {
            stringValue = "%" + stringValue;
        }
        if (stringValue.charAt(stringValue.length() - 1) != '%') {
            stringValue = stringValue + "%";
        }
        EntityManager em = AuthorValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AuthorValue AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind  AND LOWER(o.stringValue) LIKE LOWER(:stringValue)  AND o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AuthorValue> q = em.createQuery(queryBuilder.toString(), AuthorValue.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("stringValue", stringValue);
        q.setParameter("ignored", ignored);
        return q;
    }
    
}
