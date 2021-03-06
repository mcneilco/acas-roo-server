// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentLabel;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect ExperimentLabel_Roo_Finder {
    
    public static Long ExperimentLabel.countFindExperimentLabelsByExperiment(Experiment experiment) {
        if (experiment == null) throw new IllegalArgumentException("The experiment argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ExperimentLabel AS o WHERE o.experiment = :experiment", Long.class);
        q.setParameter("experiment", experiment);
        return ((Long) q.getSingleResult());
    }
    
    public static Long ExperimentLabel.countFindExperimentLabelsByExperimentAndIgnoredNot(Experiment experiment, boolean ignored) {
        if (experiment == null) throw new IllegalArgumentException("The experiment argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ExperimentLabel AS o WHERE o.experiment = :experiment AND o.ignored IS NOT :ignored", Long.class);
        q.setParameter("experiment", experiment);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }
    
    public static Long ExperimentLabel.countFindExperimentLabelsByLabelTextLike(String labelText) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        labelText = labelText.replace('*', '%');
        if (labelText.charAt(0) != '%') {
            labelText = "%" + labelText;
        }
        if (labelText.charAt(labelText.length() - 1) != '%') {
            labelText = labelText + "%";
        }
        EntityManager em = ExperimentLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ExperimentLabel AS o WHERE LOWER(o.labelText) LIKE LOWER(:labelText)", Long.class);
        q.setParameter("labelText", labelText);
        return ((Long) q.getSingleResult());
    }
    
    public static Long ExperimentLabel.countFindExperimentLabelsByLabelTextLikeAndLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(String labelText, String lsTypeAndKind, boolean preferred, boolean ignored) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        labelText = labelText.replace('*', '%');
        if (labelText.charAt(0) != '%') {
            labelText = "%" + labelText;
        }
        if (labelText.charAt(labelText.length() - 1) != '%') {
            labelText = labelText + "%";
        }
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ExperimentLabel AS o WHERE LOWER(o.labelText) LIKE LOWER(:labelText)  AND o.lsTypeAndKind = :lsTypeAndKind  AND o.preferred IS NOT :preferred  AND o.ignored IS NOT :ignored", Long.class);
        q.setParameter("labelText", labelText);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        q.setParameter("preferred", preferred);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }
    
    public static Long ExperimentLabel.countFindExperimentLabelsByLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(String lsTypeAndKind, boolean preferred, boolean ignored) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ExperimentLabel AS o WHERE o.lsTypeAndKind = :lsTypeAndKind  AND o.preferred IS NOT :preferred  AND o.ignored IS NOT :ignored", Long.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        q.setParameter("preferred", preferred);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<ExperimentLabel> ExperimentLabel.findExperimentLabelsByExperiment(Experiment experiment) {
        if (experiment == null) throw new IllegalArgumentException("The experiment argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        TypedQuery<ExperimentLabel> q = em.createQuery("SELECT o FROM ExperimentLabel AS o WHERE o.experiment = :experiment", ExperimentLabel.class);
        q.setParameter("experiment", experiment);
        return q;
    }
    
    public static TypedQuery<ExperimentLabel> ExperimentLabel.findExperimentLabelsByExperiment(Experiment experiment, String sortFieldName, String sortOrder) {
        if (experiment == null) throw new IllegalArgumentException("The experiment argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ExperimentLabel AS o WHERE o.experiment = :experiment");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ExperimentLabel> q = em.createQuery(queryBuilder.toString(), ExperimentLabel.class);
        q.setParameter("experiment", experiment);
        return q;
    }
    
    public static TypedQuery<ExperimentLabel> ExperimentLabel.findExperimentLabelsByExperimentAndIgnoredNot(Experiment experiment, boolean ignored) {
        if (experiment == null) throw new IllegalArgumentException("The experiment argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        TypedQuery<ExperimentLabel> q = em.createQuery("SELECT o FROM ExperimentLabel AS o WHERE o.experiment = :experiment AND o.ignored IS NOT :ignored", ExperimentLabel.class);
        q.setParameter("experiment", experiment);
        q.setParameter("ignored", ignored);
        return q;
    }
    
    public static TypedQuery<ExperimentLabel> ExperimentLabel.findExperimentLabelsByExperimentAndIgnoredNot(Experiment experiment, boolean ignored, String sortFieldName, String sortOrder) {
        if (experiment == null) throw new IllegalArgumentException("The experiment argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ExperimentLabel AS o WHERE o.experiment = :experiment AND o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ExperimentLabel> q = em.createQuery(queryBuilder.toString(), ExperimentLabel.class);
        q.setParameter("experiment", experiment);
        q.setParameter("ignored", ignored);
        return q;
    }
    
    public static TypedQuery<ExperimentLabel> ExperimentLabel.findExperimentLabelsByLabelTextLike(String labelText) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        labelText = labelText.replace('*', '%');
        if (labelText.charAt(0) != '%') {
            labelText = "%" + labelText;
        }
        if (labelText.charAt(labelText.length() - 1) != '%') {
            labelText = labelText + "%";
        }
        EntityManager em = ExperimentLabel.entityManager();
        TypedQuery<ExperimentLabel> q = em.createQuery("SELECT o FROM ExperimentLabel AS o WHERE LOWER(o.labelText) LIKE LOWER(:labelText)", ExperimentLabel.class);
        q.setParameter("labelText", labelText);
        return q;
    }
    
    public static TypedQuery<ExperimentLabel> ExperimentLabel.findExperimentLabelsByLabelTextLike(String labelText, String sortFieldName, String sortOrder) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        labelText = labelText.replace('*', '%');
        if (labelText.charAt(0) != '%') {
            labelText = "%" + labelText;
        }
        if (labelText.charAt(labelText.length() - 1) != '%') {
            labelText = labelText + "%";
        }
        EntityManager em = ExperimentLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ExperimentLabel AS o WHERE LOWER(o.labelText) LIKE LOWER(:labelText)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ExperimentLabel> q = em.createQuery(queryBuilder.toString(), ExperimentLabel.class);
        q.setParameter("labelText", labelText);
        return q;
    }
    
    public static TypedQuery<ExperimentLabel> ExperimentLabel.findExperimentLabelsByLabelTextLikeAndLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(String labelText, String lsTypeAndKind, boolean preferred, boolean ignored) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        labelText = labelText.replace('*', '%');
        if (labelText.charAt(0) != '%') {
            labelText = "%" + labelText;
        }
        if (labelText.charAt(labelText.length() - 1) != '%') {
            labelText = labelText + "%";
        }
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        TypedQuery<ExperimentLabel> q = em.createQuery("SELECT o FROM ExperimentLabel AS o WHERE LOWER(o.labelText) LIKE LOWER(:labelText)  AND o.lsTypeAndKind = :lsTypeAndKind  AND o.preferred IS NOT :preferred  AND o.ignored IS NOT :ignored", ExperimentLabel.class);
        q.setParameter("labelText", labelText);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        q.setParameter("preferred", preferred);
        q.setParameter("ignored", ignored);
        return q;
    }
    
    public static TypedQuery<ExperimentLabel> ExperimentLabel.findExperimentLabelsByLabelTextLikeAndLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(String labelText, String lsTypeAndKind, boolean preferred, boolean ignored, String sortFieldName, String sortOrder) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        labelText = labelText.replace('*', '%');
        if (labelText.charAt(0) != '%') {
            labelText = "%" + labelText;
        }
        if (labelText.charAt(labelText.length() - 1) != '%') {
            labelText = labelText + "%";
        }
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ExperimentLabel AS o WHERE LOWER(o.labelText) LIKE LOWER(:labelText)  AND o.lsTypeAndKind = :lsTypeAndKind  AND o.preferred IS NOT :preferred  AND o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ExperimentLabel> q = em.createQuery(queryBuilder.toString(), ExperimentLabel.class);
        q.setParameter("labelText", labelText);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        q.setParameter("preferred", preferred);
        q.setParameter("ignored", ignored);
        return q;
    }
    
    public static TypedQuery<ExperimentLabel> ExperimentLabel.findExperimentLabelsByLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(String lsTypeAndKind, boolean preferred, boolean ignored) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        TypedQuery<ExperimentLabel> q = em.createQuery("SELECT o FROM ExperimentLabel AS o WHERE o.lsTypeAndKind = :lsTypeAndKind  AND o.preferred IS NOT :preferred  AND o.ignored IS NOT :ignored", ExperimentLabel.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        q.setParameter("preferred", preferred);
        q.setParameter("ignored", ignored);
        return q;
    }
    
    public static TypedQuery<ExperimentLabel> ExperimentLabel.findExperimentLabelsByLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(String lsTypeAndKind, boolean preferred, boolean ignored, String sortFieldName, String sortOrder) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ExperimentLabel AS o WHERE o.lsTypeAndKind = :lsTypeAndKind  AND o.preferred IS NOT :preferred  AND o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ExperimentLabel> q = em.createQuery(queryBuilder.toString(), ExperimentLabel.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        q.setParameter("preferred", preferred);
        q.setParameter("ignored", ignored);
        return q;
    }
    
}
