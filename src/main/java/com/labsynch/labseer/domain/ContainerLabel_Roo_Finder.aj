// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerLabel;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect ContainerLabel_Roo_Finder {
    
    public static Long ContainerLabel.countFindContainerLabelsByContainerAndIgnoredNot(Container container, boolean ignored) {
        if (container == null) throw new IllegalArgumentException("The container argument is required");
        EntityManager em = ContainerLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ContainerLabel AS o WHERE o.container = :container AND o.ignored IS NOT :ignored", Long.class);
        q.setParameter("container", container);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }
    
    public static Long ContainerLabel.countFindContainerLabelsByLabelTextEqualsAndIgnoredNot(String labelText, boolean ignored) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        EntityManager em = ContainerLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ContainerLabel AS o WHERE o.labelText = :labelText  AND o.ignored IS NOT :ignored", Long.class);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }
    
    public static Long ContainerLabel.countFindContainerLabelsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ContainerLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ContainerLabel AS o WHERE o.lsTransaction = :lsTransaction", Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }
    
    public static Long ContainerLabel.countFindContainerLabelsByLsTypeEqualsAndLabelTextEqualsAndIgnoredNot(String lsType, String labelText, boolean ignored) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        EntityManager em = ContainerLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ContainerLabel AS o WHERE o.lsType = :lsType  AND o.labelText = :labelText  AND o.ignored IS NOT :ignored", Long.class);
        q.setParameter("lsType", lsType);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<ContainerLabel> ContainerLabel.findContainerLabelsByContainerAndIgnoredNot(Container container, boolean ignored, String sortFieldName, String sortOrder) {
        if (container == null) throw new IllegalArgumentException("The container argument is required");
        EntityManager em = ContainerLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ContainerLabel AS o WHERE o.container = :container AND o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ContainerLabel> q = em.createQuery(queryBuilder.toString(), ContainerLabel.class);
        q.setParameter("container", container);
        q.setParameter("ignored", ignored);
        return q;
    }
    
    public static TypedQuery<ContainerLabel> ContainerLabel.findContainerLabelsByLabelTextEqualsAndIgnoredNot(String labelText, boolean ignored, String sortFieldName, String sortOrder) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        EntityManager em = ContainerLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ContainerLabel AS o WHERE o.labelText = :labelText  AND o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ContainerLabel> q = em.createQuery(queryBuilder.toString(), ContainerLabel.class);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        return q;
    }
    
    public static TypedQuery<ContainerLabel> ContainerLabel.findContainerLabelsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ContainerLabel.entityManager();
        TypedQuery<ContainerLabel> q = em.createQuery("SELECT o FROM ContainerLabel AS o WHERE o.lsTransaction = :lsTransaction", ContainerLabel.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }
    
    public static TypedQuery<ContainerLabel> ContainerLabel.findContainerLabelsByLsTransactionEquals(Long lsTransaction, String sortFieldName, String sortOrder) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ContainerLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ContainerLabel AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ContainerLabel> q = em.createQuery(queryBuilder.toString(), ContainerLabel.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }
    
    public static TypedQuery<ContainerLabel> ContainerLabel.findContainerLabelsByLsTypeEqualsAndLabelTextEqualsAndIgnoredNot(String lsType, String labelText, boolean ignored) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        EntityManager em = ContainerLabel.entityManager();
        TypedQuery<ContainerLabel> q = em.createQuery("SELECT o FROM ContainerLabel AS o WHERE o.lsType = :lsType  AND o.labelText = :labelText  AND o.ignored IS NOT :ignored", ContainerLabel.class);
        q.setParameter("lsType", lsType);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        return q;
    }
    
    public static TypedQuery<ContainerLabel> ContainerLabel.findContainerLabelsByLsTypeEqualsAndLabelTextEqualsAndIgnoredNot(String lsType, String labelText, boolean ignored, String sortFieldName, String sortOrder) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        EntityManager em = ContainerLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ContainerLabel AS o WHERE o.lsType = :lsType  AND o.labelText = :labelText  AND o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ContainerLabel> q = em.createQuery(queryBuilder.toString(), ContainerLabel.class);
        q.setParameter("lsType", lsType);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        return q;
    }
    
}
