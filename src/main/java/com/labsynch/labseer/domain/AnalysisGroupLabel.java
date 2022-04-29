package com.labsynch.labseer.domain;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity

@Transactional
public class AnalysisGroupLabel extends AbstractLabel {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "analysis_group_id")
    private AnalysisGroup analysisGroup;

    public AnalysisGroupLabel(AnalysisGroupLabel analysisGroupLabel) {
        super.setLsType(analysisGroupLabel.getLsType());
        super.setLsKind(analysisGroupLabel.getLsKind());
        super.setLsTypeAndKind(analysisGroupLabel.getLsType() + "_" + analysisGroupLabel.getLsKind());
        super.setLabelText(analysisGroupLabel.getLabelText());
        super.setPreferred(analysisGroupLabel.isPreferred());
        super.setLsTransaction(analysisGroupLabel.getLsTransaction());
        super.setRecordedBy(analysisGroupLabel.getRecordedBy());
        super.setRecordedDate(analysisGroupLabel.getRecordedDate());
        super.setPhysicallyLabled(analysisGroupLabel.isPhysicallyLabled());
    }

    public static AnalysisGroupLabel update(AnalysisGroupLabel analysisGroupLabel) {
        AnalysisGroupLabel updatedAnalysisGroupLabel = AnalysisGroupLabel
                .findAnalysisGroupLabel(analysisGroupLabel.getId());
        updatedAnalysisGroupLabel.setLsType(analysisGroupLabel.getLsType());
        updatedAnalysisGroupLabel.setLsKind(analysisGroupLabel.getLsKind());
        updatedAnalysisGroupLabel
                .setLsTypeAndKind(analysisGroupLabel.getLsType() + "_" + analysisGroupLabel.getLsKind());
        updatedAnalysisGroupLabel.setLabelText(analysisGroupLabel.getLabelText());
        updatedAnalysisGroupLabel.setPreferred(analysisGroupLabel.isPreferred());
        updatedAnalysisGroupLabel.setLsTransaction(analysisGroupLabel.getLsTransaction());
        updatedAnalysisGroupLabel.setRecordedBy(analysisGroupLabel.getRecordedBy());
        updatedAnalysisGroupLabel.setRecordedDate(analysisGroupLabel.getRecordedDate());
        updatedAnalysisGroupLabel.setModifiedDate(new Date());
        updatedAnalysisGroupLabel.setPhysicallyLabled(analysisGroupLabel.isPhysicallyLabled());
        updatedAnalysisGroupLabel.merge();
        return updatedAnalysisGroupLabel;
    }

    public static Collection<AnalysisGroupLabel> fromJsonArrayToAnalysisGroupLabels(String json) {
        return new JSONDeserializer<List<AnalysisGroupLabel>>().use(null, ArrayList.class)
                .use("values", AnalysisGroupLabel.class).deserialize(json);
    }

    public static Collection<AnalysisGroupLabel> fromJsonArrayToAnalysisGroupLabels(Reader json) {
        return new JSONDeserializer<List<AnalysisGroupLabel>>().use(null, ArrayList.class)
                .use("values", AnalysisGroupLabel.class).deserialize(json);
    }

    public static int deleteByExperimentID(Long experimentId) {
        if (experimentId == null)
            return 0;
        EntityManager em = SubjectValue.entityManager();
        String deleteSQL = "DELETE FROM AnalysisGroupLabel oo WHERE id in (select o.id from AnalysisGroupLabel o where o.analysisGroup.experiment.id = :experimentId)";

        Query q = em.createQuery(deleteSQL);
        q.setParameter("experimentId", experimentId);
        int numberOfDeletedEntities = q.executeUpdate();
        return numberOfDeletedEntities;
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static AnalysisGroupLabel fromJsonToAnalysisGroupLabel(String json) {
        return new JSONDeserializer<AnalysisGroupLabel>()
                .use(null, AnalysisGroupLabel.class).deserialize(json);
    }

    public static String toJsonArray(Collection<AnalysisGroupLabel> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<AnalysisGroupLabel> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public AnalysisGroupLabel() {
        super();
    }

    public AnalysisGroup getAnalysisGroup() {
        return this.analysisGroup;
    }

    public void setAnalysisGroup(AnalysisGroup analysisGroup) {
        this.analysisGroup = analysisGroup;
    }

    public static Long countFindAnalysisGroupLabelsByAnalysisGroup(AnalysisGroup analysisGroup) {
        if (analysisGroup == null)
            throw new IllegalArgumentException("The analysisGroup argument is required");
        EntityManager em = AnalysisGroupLabel.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM AnalysisGroupLabel AS o WHERE o.analysisGroup = :analysisGroup", Long.class);
        q.setParameter("analysisGroup", analysisGroup);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindAnalysisGroupLabelsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = AnalysisGroupLabel.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM AnalysisGroupLabel AS o WHERE o.lsTransaction = :lsTransaction", Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<AnalysisGroupLabel> findAnalysisGroupLabelsByAnalysisGroup(AnalysisGroup analysisGroup) {
        if (analysisGroup == null)
            throw new IllegalArgumentException("The analysisGroup argument is required");
        EntityManager em = AnalysisGroupLabel.entityManager();
        TypedQuery<AnalysisGroupLabel> q = em.createQuery(
                "SELECT o FROM AnalysisGroupLabel AS o WHERE o.analysisGroup = :analysisGroup",
                AnalysisGroupLabel.class);
        q.setParameter("analysisGroup", analysisGroup);
        return q;
    }

    public static TypedQuery<AnalysisGroupLabel> findAnalysisGroupLabelsByAnalysisGroup(AnalysisGroup analysisGroup,
            String sortFieldName, String sortOrder) {
        if (analysisGroup == null)
            throw new IllegalArgumentException("The analysisGroup argument is required");
        EntityManager em = AnalysisGroupLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM AnalysisGroupLabel AS o WHERE o.analysisGroup = :analysisGroup");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AnalysisGroupLabel> q = em.createQuery(queryBuilder.toString(), AnalysisGroupLabel.class);
        q.setParameter("analysisGroup", analysisGroup);
        return q;
    }

    public static TypedQuery<AnalysisGroupLabel> findAnalysisGroupLabelsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = AnalysisGroupLabel.entityManager();
        TypedQuery<AnalysisGroupLabel> q = em.createQuery(
                "SELECT o FROM AnalysisGroupLabel AS o WHERE o.lsTransaction = :lsTransaction",
                AnalysisGroupLabel.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

    public static TypedQuery<AnalysisGroupLabel> findAnalysisGroupLabelsByLsTransactionEquals(Long lsTransaction,
            String sortFieldName, String sortOrder) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = AnalysisGroupLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM AnalysisGroupLabel AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AnalysisGroupLabel> q = em.createQuery(queryBuilder.toString(), AnalysisGroupLabel.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("analysisGroup");

    public static long countAnalysisGroupLabels() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AnalysisGroupLabel o", Long.class).getSingleResult();
    }

    public static List<AnalysisGroupLabel> findAllAnalysisGroupLabels() {
        return entityManager().createQuery("SELECT o FROM AnalysisGroupLabel o", AnalysisGroupLabel.class)
                .getResultList();
    }

    public static List<AnalysisGroupLabel> findAllAnalysisGroupLabels(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM AnalysisGroupLabel o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, AnalysisGroupLabel.class).getResultList();
    }

    public static AnalysisGroupLabel findAnalysisGroupLabel(Long id) {
        if (id == null)
            return null;
        return entityManager().find(AnalysisGroupLabel.class, id);
    }

    public static List<AnalysisGroupLabel> findAnalysisGroupLabelEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AnalysisGroupLabel o", AnalysisGroupLabel.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<AnalysisGroupLabel> findAnalysisGroupLabelEntries(int firstResult, int maxResults,
            String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM AnalysisGroupLabel o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, AnalysisGroupLabel.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public AnalysisGroupLabel merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        AnalysisGroupLabel merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
