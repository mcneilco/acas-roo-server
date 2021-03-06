// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.ApplicationSetting;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect ApplicationSetting_Roo_Finder {
    
    public static Long ApplicationSetting.countFindApplicationSettingsByPropNameEquals(String propName) {
        if (propName == null || propName.length() == 0) throw new IllegalArgumentException("The propName argument is required");
        EntityManager em = ApplicationSetting.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ApplicationSetting AS o WHERE o.propName = :propName", Long.class);
        q.setParameter("propName", propName);
        return ((Long) q.getSingleResult());
    }
    
    public static Long ApplicationSetting.countFindApplicationSettingsByPropNameEqualsAndIgnoredNot(String propName, boolean ignored) {
        if (propName == null || propName.length() == 0) throw new IllegalArgumentException("The propName argument is required");
        EntityManager em = ApplicationSetting.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ApplicationSetting AS o WHERE o.propName = :propName  AND o.ignored IS NOT :ignored", Long.class);
        q.setParameter("propName", propName);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<ApplicationSetting> ApplicationSetting.findApplicationSettingsByPropNameEquals(String propName) {
        if (propName == null || propName.length() == 0) throw new IllegalArgumentException("The propName argument is required");
        EntityManager em = ApplicationSetting.entityManager();
        TypedQuery<ApplicationSetting> q = em.createQuery("SELECT o FROM ApplicationSetting AS o WHERE o.propName = :propName", ApplicationSetting.class);
        q.setParameter("propName", propName);
        return q;
    }
    
    public static TypedQuery<ApplicationSetting> ApplicationSetting.findApplicationSettingsByPropNameEquals(String propName, String sortFieldName, String sortOrder) {
        if (propName == null || propName.length() == 0) throw new IllegalArgumentException("The propName argument is required");
        EntityManager em = ApplicationSetting.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ApplicationSetting AS o WHERE o.propName = :propName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ApplicationSetting> q = em.createQuery(queryBuilder.toString(), ApplicationSetting.class);
        q.setParameter("propName", propName);
        return q;
    }
    
    public static TypedQuery<ApplicationSetting> ApplicationSetting.findApplicationSettingsByPropNameEqualsAndIgnoredNot(String propName, boolean ignored) {
        if (propName == null || propName.length() == 0) throw new IllegalArgumentException("The propName argument is required");
        EntityManager em = ApplicationSetting.entityManager();
        TypedQuery<ApplicationSetting> q = em.createQuery("SELECT o FROM ApplicationSetting AS o WHERE o.propName = :propName  AND o.ignored IS NOT :ignored", ApplicationSetting.class);
        q.setParameter("propName", propName);
        q.setParameter("ignored", ignored);
        return q;
    }
    
    public static TypedQuery<ApplicationSetting> ApplicationSetting.findApplicationSettingsByPropNameEqualsAndIgnoredNot(String propName, boolean ignored, String sortFieldName, String sortOrder) {
        if (propName == null || propName.length() == 0) throw new IllegalArgumentException("The propName argument is required");
        EntityManager em = ApplicationSetting.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ApplicationSetting AS o WHERE o.propName = :propName  AND o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ApplicationSetting> q = em.createQuery(queryBuilder.toString(), ApplicationSetting.class);
        q.setParameter("propName", propName);
        q.setParameter("ignored", ignored);
        return q;
    }
    
}
