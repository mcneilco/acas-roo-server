// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.Salt;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect Salt_Roo_Finder {
    
    public static Long Salt.countFindSaltsByAbbrevEquals(String abbrev) {
        if (abbrev == null || abbrev.length() == 0) throw new IllegalArgumentException("The abbrev argument is required");
        EntityManager em = Salt.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Salt AS o WHERE o.abbrev = :abbrev", Long.class);
        q.setParameter("abbrev", abbrev);
        return ((Long) q.getSingleResult());
    }
    
    public static Long Salt.countFindSaltsByAbbrevEqualsAndNameEquals(String abbrev, String name) {
        if (abbrev == null || abbrev.length() == 0) throw new IllegalArgumentException("The abbrev argument is required");
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Salt.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Salt AS o WHERE o.abbrev = :abbrev  AND o.name = :name", Long.class);
        q.setParameter("abbrev", abbrev);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }
    
    public static Long Salt.countFindSaltsByAbbrevLike(String abbrev) {
        if (abbrev == null || abbrev.length() == 0) throw new IllegalArgumentException("The abbrev argument is required");
        abbrev = abbrev.replace('*', '%');
        if (abbrev.charAt(0) != '%') {
            abbrev = "%" + abbrev;
        }
        if (abbrev.charAt(abbrev.length() - 1) != '%') {
            abbrev = abbrev + "%";
        }
        EntityManager em = Salt.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Salt AS o WHERE LOWER(o.abbrev) LIKE LOWER(:abbrev)", Long.class);
        q.setParameter("abbrev", abbrev);
        return ((Long) q.getSingleResult());
    }
    
    public static Long Salt.countFindSaltsByCdId(int cdId) {
        EntityManager em = Salt.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Salt AS o WHERE o.cdId = :cdId", Long.class);
        q.setParameter("cdId", cdId);
        return ((Long) q.getSingleResult());
    }
    
    public static Long Salt.countFindSaltsByNameEquals(String name) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Salt.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Salt AS o WHERE o.name = :name", Long.class);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<Salt> Salt.findSaltsByAbbrevEquals(String abbrev, String sortFieldName, String sortOrder) {
        if (abbrev == null || abbrev.length() == 0) throw new IllegalArgumentException("The abbrev argument is required");
        EntityManager em = Salt.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Salt AS o WHERE o.abbrev = :abbrev");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Salt> q = em.createQuery(queryBuilder.toString(), Salt.class);
        q.setParameter("abbrev", abbrev);
        return q;
    }
    
    public static TypedQuery<Salt> Salt.findSaltsByAbbrevEqualsAndNameEquals(String abbrev, String name, String sortFieldName, String sortOrder) {
        if (abbrev == null || abbrev.length() == 0) throw new IllegalArgumentException("The abbrev argument is required");
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Salt.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Salt AS o WHERE o.abbrev = :abbrev  AND o.name = :name");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Salt> q = em.createQuery(queryBuilder.toString(), Salt.class);
        q.setParameter("abbrev", abbrev);
        q.setParameter("name", name);
        return q;
    }
    
    public static TypedQuery<Salt> Salt.findSaltsByAbbrevLike(String abbrev, String sortFieldName, String sortOrder) {
        if (abbrev == null || abbrev.length() == 0) throw new IllegalArgumentException("The abbrev argument is required");
        abbrev = abbrev.replace('*', '%');
        if (abbrev.charAt(0) != '%') {
            abbrev = "%" + abbrev;
        }
        if (abbrev.charAt(abbrev.length() - 1) != '%') {
            abbrev = abbrev + "%";
        }
        EntityManager em = Salt.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Salt AS o WHERE LOWER(o.abbrev) LIKE LOWER(:abbrev)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Salt> q = em.createQuery(queryBuilder.toString(), Salt.class);
        q.setParameter("abbrev", abbrev);
        return q;
    }
    
    public static TypedQuery<Salt> Salt.findSaltsByCdId(int cdId, String sortFieldName, String sortOrder) {
        EntityManager em = Salt.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Salt AS o WHERE o.cdId = :cdId");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Salt> q = em.createQuery(queryBuilder.toString(), Salt.class);
        q.setParameter("cdId", cdId);
        return q;
    }
    
    public static TypedQuery<Salt> Salt.findSaltsByNameEquals(String name, String sortFieldName, String sortOrder) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = Salt.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Salt AS o WHERE o.name = :name");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Salt> q = em.createQuery(queryBuilder.toString(), Salt.class);
        q.setParameter("name", name);
        return q;
    }
    
}
