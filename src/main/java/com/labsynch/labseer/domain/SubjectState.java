package com.labsynch.labseer.domain;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.validation.constraints.NotNull;

import com.labsynch.labseer.dto.FlatThingCsvDTO;
import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity
public class SubjectState extends AbstractState {

    private static final Logger logger = LoggerFactory.getLogger(SubjectState.class);

    @NotNull
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lsState", fetch = FetchType.LAZY)
    private Set<SubjectValue> lsValues = new HashSet<SubjectValue>();

    public SubjectState() {
    }

    public SubjectState(com.labsynch.labseer.domain.SubjectState subjectState) {
        this.setRecordedBy(subjectState.getRecordedBy());
        this.setRecordedDate(subjectState.getRecordedDate());
        this.setLsTransaction(subjectState.getLsTransaction());
        this.setModifiedBy(subjectState.getModifiedBy());
        this.setModifiedDate(subjectState.getModifiedDate());
        this.setLsType(subjectState.getLsType());
        this.setLsKind(subjectState.getLsKind());
        this.setIgnored(subjectState.isIgnored());
    }

    public SubjectState(FlatThingCsvDTO subjectDTO) {
        this.setRecordedBy(subjectDTO.getRecordedBy());
        this.setRecordedDate(subjectDTO.getRecordedDate());
        this.setLsTransaction(subjectDTO.getLsTransaction());
        this.setModifiedBy(subjectDTO.getModifiedBy());
        this.setModifiedDate(subjectDTO.getModifiedDate());
        this.setLsType(subjectDTO.getStateType());
        this.setLsKind(subjectDTO.getStateKind());
    }

    public static com.labsynch.labseer.domain.SubjectState update(
            com.labsynch.labseer.domain.SubjectState subjectState) {
        SubjectState updatedSubjectState = SubjectState.findSubjectState(subjectState.getId());
        updatedSubjectState.setRecordedBy(subjectState.getRecordedBy());
        updatedSubjectState.setRecordedDate(subjectState.getRecordedDate());
        updatedSubjectState.setLsTransaction(subjectState.getLsTransaction());
        updatedSubjectState.setModifiedBy(subjectState.getModifiedBy());
        updatedSubjectState.setModifiedDate(new Date());
        updatedSubjectState.setLsType(subjectState.getLsType());
        updatedSubjectState.setLsKind(subjectState.getLsKind());
        updatedSubjectState.setIgnored(subjectState.isIgnored());
        return updatedSubjectState;
    }

    public static TypedQuery<com.labsynch.labseer.domain.SubjectState> findSubjectStatesBylsTypeAndKindEquals(
            String lsTypeAndKind) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0)
            throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = SubjectState.entityManager();
        TypedQuery<SubjectState> q = em.createQuery(
                "SELECT o FROM SubjectState AS o WHERE o.lsTypeAndKind = :lsTypeAndKind  AND o.ignored IS NOT :ignored",
                SubjectState.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        boolean ignored = true;
        q.setParameter("ignored", ignored);
        return q;
    }

    public static TypedQuery<com.labsynch.labseer.domain.SubjectState> findSubjectStatesBySubject(Subject subject) {
        if (subject == null)
            throw new IllegalArgumentException("The subject argument is required");
        EntityManager em = SubjectState.entityManager();
        TypedQuery<SubjectState> q = em.createQuery("SELECT o FROM SubjectState AS o WHERE o.subject = :subject",
                SubjectState.class);
        q.setParameter("subject", subject);
        return q;
    }

    public static TypedQuery<com.labsynch.labseer.domain.SubjectState> findSubjectStatesBySubjectAndLsTypeAndKindEqualsAndIgnoredNot(
            Subject subject, String lsTypeAndKind) {
        if (subject == null)
            throw new IllegalArgumentException("The subject argument is required");
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0)
            throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = SubjectState.entityManager();
        TypedQuery<SubjectState> q = em.createQuery(
                "SELECT o FROM SubjectState AS o WHERE o.subject = :subject AND o.lsTypeAndKind = :lsTypeAndKind  AND o.ignored IS NOT :ignored",
                SubjectState.class);
        q.setParameter("subject", subject);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        boolean ignored = true;
        q.setParameter("ignored", ignored);
        return q;
    }

    public static long countSubjectStates() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SubjectState o", Long.class).getSingleResult();
    }

    public static List<com.labsynch.labseer.domain.SubjectState> findAllSubjectStates() {
        return entityManager().createQuery("SELECT o FROM SubjectState o", SubjectState.class).getResultList();
    }

    public static com.labsynch.labseer.domain.SubjectState findSubjectState(Long id) {
        if (id == null)
            return null;
        return entityManager().find(SubjectState.class, id);
    }

    public static List<com.labsynch.labseer.domain.SubjectState> findSubjectStateEntries(int firstResult,
            int maxResults) {
        return entityManager().createQuery("SELECT o FROM SubjectState o", SubjectState.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public com.labsynch.labseer.domain.SubjectState merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        SubjectState merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    @Transactional
    public static List<java.lang.Long> saveList(List<com.labsynch.labseer.domain.SubjectState> entities) {
        List<Long> idList = new ArrayList<Long>();
        int batchSize = 50;
        int imported = 0;
        for (SubjectState e : entities) {
            e.persist();
            idList.add(e.getId());
            if (++imported % batchSize == 0) {
                e.flush();
                e.clear();
            }
        }
        return idList;
    }

    @Transactional
    public static String getSubjectStateCollectionJson(List<java.lang.Long> idList) {
        Collection<SubjectState> subjectStates = new ArrayList<SubjectState>();
        for (Long id : idList) {
            SubjectState query = SubjectState.findSubjectState(id);
            if (query != null)
                subjectStates.add(query);
        }
        return SubjectState.toJsonArray(subjectStates);
    }

    public static int deleteByExperimentID(Long experimentId) {
        if (experimentId == null)
            return 0;
        EntityManager em = SubjectState.entityManager();
        String deleteSQL = "DELETE FROM SubjectState oo WHERE id in (select o.id from SubjectState o where o.subject.treatmentGroup.analysisGroup.experiment.id = :experimentId)";
        logger.debug("attempting to delete subject states: " + deleteSQL);
        Query q = em.createQuery(deleteSQL);
        q.setParameter("experimentId", experimentId);
        int numberOfDeletedEntities = q.executeUpdate();
        return numberOfDeletedEntities;
    }

    public static int deleteByTransactionID(Long transactionId) {
        if (transactionId == null)
            return 0;
        EntityManager em = SubjectState.entityManager();
        String deleteSQL = "DELETE FROM SubjectState oo WHERE lsTransaction = :transactionId";
        logger.debug("attempting to delete subject states: " + deleteSQL);
        Query q = em.createQuery(deleteSQL);
        q.setParameter("transactionId", transactionId);
        int numberOfDeletedEntities = q.executeUpdate();
        return numberOfDeletedEntities;
    }

    public static com.labsynch.labseer.domain.SubjectState fromJsonToSubjectState(String json) {
        return new JSONDeserializer<SubjectState>().use(null, SubjectState.class).deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.SubjectState> fromJsonArrayToSubjectStates(Reader json) {
        return new JSONDeserializer<List<SubjectState>>().use(null, ArrayList.class).use("values", SubjectState.class)
                .deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.SubjectState> fromJsonArrayToSubjectStates(String json) {
        return new JSONDeserializer<List<SubjectState>>().use(null, ArrayList.class).use("values", SubjectState.class)
                .deserialize(json);
    }

    @Transactional
    public static String toJsonArray(Collection<com.labsynch.labseer.domain.SubjectState> collection) {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStub(Collection<com.labsynch.labseer.domain.SubjectState> collection) {
        return new JSONSerializer().exclude("*.class", "subject").include("subject.id")
                .transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public String toJson() {
        return new JSONSerializer().include("lsValues").exclude("*.class", "subject")
                .transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toJsonStub() {
        return new JSONSerializer().exclude("*.class", "subject").transform(new ExcludeNulls(), void.class)
                .serialize(this);
    }

    public static TypedQuery<SubjectState> findSubjectStatesBySubjectIDAndStateTypeKind(
            Long subjectId, String stateType, String stateKind) {

        if (stateType == null || stateKind.length() == 0)
            throw new IllegalArgumentException("The stateType argument is required");
        if (stateKind == null || stateKind.length() == 0)
            throw new IllegalArgumentException("The stateKind argument is required");

        EntityManager em = entityManager();
        String hsqlQuery = "SELECT ss FROM SubjectState AS ss " +
                "JOIN ss.subject s " +
                "WHERE ss.lsType = :stateType AND ss.lsKind = :stateKind AND ss.ignored IS NOT :ignored " +
                "AND s.id = :subjectId ";
        TypedQuery<SubjectState> q = em.createQuery(hsqlQuery, SubjectState.class);
        q.setParameter("subjectId", subjectId);
        q.setParameter("stateType", stateType);
        q.setParameter("stateKind", stateKind);
        q.setParameter("ignored", true);
        return q;
    }

    public static TypedQuery<SubjectState> findSubjectStatesBySubjectCodeNameAndStateTypeKind(
            String subjectCodeName, String stateType, String stateKind) {
        if (stateType == null || stateKind.length() == 0)
            throw new IllegalArgumentException("The stateType argument is required");
        if (stateKind == null || stateKind.length() == 0)
            throw new IllegalArgumentException("The stateKind argument is required");

        EntityManager em = entityManager();
        String hsqlQuery = "SELECT ss FROM SubjectState AS ss " +
                "JOIN ss.subject s " +
                "WHERE ss.lsType = :stateType AND ss.lsKind = :stateKind AND ss.ignored IS NOT :ignored " +
                "AND s.codeName = :subjectCodeName ";
        TypedQuery<SubjectState> q = em.createQuery(hsqlQuery, SubjectState.class);
        q.setParameter("subjectCodeName", subjectCodeName);
        q.setParameter("stateType", stateType);
        q.setParameter("stateKind", stateKind);
        q.setParameter("ignored", true);
        return q;
    }

    public static Long countFindSubjectStatesByLsTypeEqualsAndLsKindEqualsAndSubject(String lsType, String lsKind,
            Subject subject) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        if (subject == null)
            throw new IllegalArgumentException("The subject argument is required");
        EntityManager em = SubjectState.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM SubjectState AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind  AND o.subject = :subject",
                Long.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("subject", subject);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindSubjectStatesBySubject(Subject subject) {
        if (subject == null)
            throw new IllegalArgumentException("The subject argument is required");
        EntityManager em = SubjectState.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM SubjectState AS o WHERE o.subject = :subject", Long.class);
        q.setParameter("subject", subject);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<SubjectState> findSubjectStatesByLsTypeEqualsAndLsKindEqualsAndSubject(String lsType,
            String lsKind, Subject subject) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        if (subject == null)
            throw new IllegalArgumentException("The subject argument is required");
        EntityManager em = SubjectState.entityManager();
        TypedQuery<SubjectState> q = em.createQuery(
                "SELECT o FROM SubjectState AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind  AND o.subject = :subject",
                SubjectState.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("subject", subject);
        return q;
    }

    public static TypedQuery<SubjectState> findSubjectStatesByLsTypeEqualsAndLsKindEqualsAndSubject(String lsType,
            String lsKind, Subject subject, String sortFieldName, String sortOrder) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        if (subject == null)
            throw new IllegalArgumentException("The subject argument is required");
        EntityManager em = SubjectState.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM SubjectState AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind  AND o.subject = :subject");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<SubjectState> q = em.createQuery(queryBuilder.toString(), SubjectState.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("subject", subject);
        return q;
    }

    public static TypedQuery<SubjectState> findSubjectStatesBySubject(Subject subject, String sortFieldName,
            String sortOrder) {
        if (subject == null)
            throw new IllegalArgumentException("The subject argument is required");
        EntityManager em = SubjectState.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM SubjectState AS o WHERE o.subject = :subject");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<SubjectState> q = em.createQuery(queryBuilder.toString(), SubjectState.class);
        q.setParameter("subject", subject);
        return q;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Set<SubjectValue> getLsValues() {
        return this.lsValues;
    }

    public void setLsValues(Set<SubjectValue> lsValues) {
        this.lsValues = lsValues;
    }

    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .setExcludeFieldNames("subject", "lsValues").toString();
    }

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "subject",
            "lsValues");

    public static List<SubjectState> findAllSubjectStates(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM SubjectState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, SubjectState.class).getResultList();
    }

    public static List<SubjectState> findSubjectStateEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM SubjectState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, SubjectState.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }
}
