package com.labsynch.labseer.domain;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable

public class SubjectLabel extends AbstractLabel {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    public SubjectLabel() {
    }

    public SubjectLabel(com.labsynch.labseer.domain.SubjectLabel subjectLabel) {
        super.setLsType(subjectLabel.getLsType());
        super.setLsKind(subjectLabel.getLsKind());
        super.setLsTypeAndKind(subjectLabel.getLsType() + "_" + subjectLabel.getLsKind());
        super.setLabelText(subjectLabel.getLabelText());
        super.setPreferred(subjectLabel.isPreferred());
        super.setLsTransaction(subjectLabel.getLsTransaction());
        super.setRecordedBy(subjectLabel.getRecordedBy());
        super.setRecordedDate(subjectLabel.getRecordedDate());
        super.setPhysicallyLabled(subjectLabel.isPhysicallyLabled());
    }

    public static SubjectLabel update(SubjectLabel subjectLabel) {
        SubjectLabel updatedSubjectLabel = SubjectLabel.findSubjectLabel(subjectLabel.getId());
        updatedSubjectLabel.setLsType(subjectLabel.getLsType());
        updatedSubjectLabel.setLsKind(subjectLabel.getLsKind());
        updatedSubjectLabel.setLsTypeAndKind(subjectLabel.getLsType() + "_" + subjectLabel.getLsKind());
        updatedSubjectLabel.setLabelText(subjectLabel.getLabelText());
        updatedSubjectLabel.setPreferred(subjectLabel.isPreferred());
        updatedSubjectLabel.setLsTransaction(subjectLabel.getLsTransaction());
        updatedSubjectLabel.setRecordedBy(subjectLabel.getRecordedBy());
        updatedSubjectLabel.setRecordedDate(subjectLabel.getRecordedDate());
        updatedSubjectLabel.setModifiedDate(new Date());
        updatedSubjectLabel.setPhysicallyLabled(subjectLabel.isPhysicallyLabled());
        updatedSubjectLabel.setIgnored(subjectLabel.isIgnored());
        updatedSubjectLabel.merge();
        return updatedSubjectLabel;
    }

    @Transactional
    public static int deleteByExperimentID(Long experimentId) {
        if (experimentId == null)
            return 0;
        EntityManager em = ItxSubjectContainer.entityManager();
        String deleteSQL = "DELETE FROM SubjectLabel oo WHERE id in (select o.id from SubjectLabel o where o.subject.treatmentGroup.analysisGroup.experiment.id = :experimentId)";
        Query q = em.createQuery(deleteSQL);
        q.setParameter("experimentId", experimentId);
        int numberOfDeletedEntities = q.executeUpdate();
        return numberOfDeletedEntities;
    }

    public static Collection<SubjectLabel> fromJsonArrayToSubjectLabels(String json) {
        return new JSONDeserializer<List<SubjectLabel>>().use(null, ArrayList.class).use("values", SubjectLabel.class)
                .deserialize(json);
    }

    public static Collection<SubjectLabel> fromJsonArrayToSubjectLabels(Reader json) {
        return new JSONDeserializer<List<SubjectLabel>>().use(null, ArrayList.class).use("values", SubjectLabel.class)
                .deserialize(json);
    }

    public static Long countFindSubjectLabelsBySubject(Subject subject) {
        if (subject == null)
            throw new IllegalArgumentException("The subject argument is required");
        EntityManager em = SubjectLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM SubjectLabel AS o WHERE o.subject = :subject", Long.class);
        q.setParameter("subject", subject);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<SubjectLabel> findSubjectLabelsBySubject(Subject subject) {
        if (subject == null)
            throw new IllegalArgumentException("The subject argument is required");
        EntityManager em = SubjectLabel.entityManager();
        TypedQuery<SubjectLabel> q = em.createQuery("SELECT o FROM SubjectLabel AS o WHERE o.subject = :subject",
                SubjectLabel.class);
        q.setParameter("subject", subject);
        return q;
    }

    public static TypedQuery<SubjectLabel> findSubjectLabelsBySubject(Subject subject, String sortFieldName,
            String sortOrder) {
        if (subject == null)
            throw new IllegalArgumentException("The subject argument is required");
        EntityManager em = SubjectLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM SubjectLabel AS o WHERE o.subject = :subject");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<SubjectLabel> q = em.createQuery(queryBuilder.toString(), SubjectLabel.class);
        q.setParameter("subject", subject);
        return q;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("subject");

    public static long countSubjectLabels() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SubjectLabel o", Long.class).getSingleResult();
    }

    public static List<SubjectLabel> findAllSubjectLabels() {
        return entityManager().createQuery("SELECT o FROM SubjectLabel o", SubjectLabel.class).getResultList();
    }

    public static List<SubjectLabel> findAllSubjectLabels(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM SubjectLabel o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, SubjectLabel.class).getResultList();
    }

    public static SubjectLabel findSubjectLabel(Long id) {
        if (id == null)
            return null;
        return entityManager().find(SubjectLabel.class, id);
    }

    public static List<SubjectLabel> findSubjectLabelEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SubjectLabel o", SubjectLabel.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<SubjectLabel> findSubjectLabelEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM SubjectLabel o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, SubjectLabel.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public SubjectLabel merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        SubjectLabel merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static SubjectLabel fromJsonToSubjectLabel(String json) {
        return new JSONDeserializer<SubjectLabel>()
                .use(null, SubjectLabel.class).deserialize(json);
    }

    public static String toJsonArray(Collection<SubjectLabel> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<SubjectLabel> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
