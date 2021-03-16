// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.PurityMeasuredBy;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect PurityMeasuredBy_Roo_Finder {
    
    public static Long PurityMeasuredBy.countFindPurityMeasuredBysByCodeEquals(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = PurityMeasuredBy.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM PurityMeasuredBy AS o WHERE o.code = :code", Long.class);
        q.setParameter("code", code);
        return ((Long) q.getSingleResult());
    }
    
    public static Long PurityMeasuredBy.countFindPurityMeasuredBysByNameEquals(String name) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = PurityMeasuredBy.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM PurityMeasuredBy AS o WHERE o.name = :name", Long.class);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }
    
    public static Long PurityMeasuredBy.countFindPurityMeasuredBysByNameLike(String name) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        name = name.replace('*', '%');
        if (name.charAt(0) != '%') {
            name = "%" + name;
        }
        if (name.charAt(name.length() - 1) != '%') {
            name = name + "%";
        }
        EntityManager em = PurityMeasuredBy.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM PurityMeasuredBy AS o WHERE LOWER(o.name) LIKE LOWER(:name)", Long.class);
        q.setParameter("name", name);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<PurityMeasuredBy> PurityMeasuredBy.findPurityMeasuredBysByCodeEquals(String code) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = PurityMeasuredBy.entityManager();
        TypedQuery<PurityMeasuredBy> q = em.createQuery("SELECT o FROM PurityMeasuredBy AS o WHERE o.code = :code", PurityMeasuredBy.class);
        q.setParameter("code", code);
        return q;
    }
    
    public static TypedQuery<PurityMeasuredBy> PurityMeasuredBy.findPurityMeasuredBysByCodeEquals(String code, String sortFieldName, String sortOrder) {
        if (code == null || code.length() == 0) throw new IllegalArgumentException("The code argument is required");
        EntityManager em = PurityMeasuredBy.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM PurityMeasuredBy AS o WHERE o.code = :code");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<PurityMeasuredBy> q = em.createQuery(queryBuilder.toString(), PurityMeasuredBy.class);
        q.setParameter("code", code);
        return q;
    }
    
    public static TypedQuery<PurityMeasuredBy> PurityMeasuredBy.findPurityMeasuredBysByNameEquals(String name) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = PurityMeasuredBy.entityManager();
        TypedQuery<PurityMeasuredBy> q = em.createQuery("SELECT o FROM PurityMeasuredBy AS o WHERE o.name = :name", PurityMeasuredBy.class);
        q.setParameter("name", name);
        return q;
    }
    
    public static TypedQuery<PurityMeasuredBy> PurityMeasuredBy.findPurityMeasuredBysByNameEquals(String name, String sortFieldName, String sortOrder) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        EntityManager em = PurityMeasuredBy.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM PurityMeasuredBy AS o WHERE o.name = :name");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<PurityMeasuredBy> q = em.createQuery(queryBuilder.toString(), PurityMeasuredBy.class);
        q.setParameter("name", name);
        return q;
    }
    
    public static TypedQuery<PurityMeasuredBy> PurityMeasuredBy.findPurityMeasuredBysByNameLike(String name) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        name = name.replace('*', '%');
        if (name.charAt(0) != '%') {
            name = "%" + name;
        }
        if (name.charAt(name.length() - 1) != '%') {
            name = name + "%";
        }
        EntityManager em = PurityMeasuredBy.entityManager();
        TypedQuery<PurityMeasuredBy> q = em.createQuery("SELECT o FROM PurityMeasuredBy AS o WHERE LOWER(o.name) LIKE LOWER(:name)", PurityMeasuredBy.class);
        q.setParameter("name", name);
        return q;
    }
    
    public static TypedQuery<PurityMeasuredBy> PurityMeasuredBy.findPurityMeasuredBysByNameLike(String name, String sortFieldName, String sortOrder) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        name = name.replace('*', '%');
        if (name.charAt(0) != '%') {
            name = "%" + name;
        }
        if (name.charAt(name.length() - 1) != '%') {
            name = name + "%";
        }
        EntityManager em = PurityMeasuredBy.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM PurityMeasuredBy AS o WHERE LOWER(o.name) LIKE LOWER(:name)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<PurityMeasuredBy> q = em.createQuery(queryBuilder.toString(), PurityMeasuredBy.class);
        q.setParameter("name", name);
        return q;
    }
    
}