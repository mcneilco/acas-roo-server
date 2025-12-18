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

import com.labsynch.labseer.dto.AnalysisGroupCsvDTO;
import com.labsynch.labseer.dto.FlatThingCsvDTO;
import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable

@Transactional
public class AnalysisGroupState extends AbstractState {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "analysis_group_id")
    private AnalysisGroup analysisGroup;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lsState", fetch = FetchType.LAZY)
    private Set<AnalysisGroupValue> lsValues = new HashSet<AnalysisGroupValue>();

    public AnalysisGroupState(com.labsynch.labseer.domain.AnalysisGroupState analysisGroupState) {
        this.setRecordedBy(analysisGroupState.getRecordedBy());
        this.setRecordedDate(analysisGroupState.getRecordedDate());
        this.setLsTransaction(analysisGroupState.getLsTransaction());
        this.setModifiedBy(analysisGroupState.getModifiedBy());
        this.setModifiedDate(analysisGroupState.getModifiedDate());
        this.setLsType(analysisGroupState.getLsType());
        this.setLsKind(analysisGroupState.getLsKind());
    }

    public AnalysisGroupState(AnalysisGroupCsvDTO analysisGroupDTO) {
        this.setRecordedBy(analysisGroupDTO.getRecordedBy());
        this.setRecordedDate(analysisGroupDTO.getRecordedDate());
        this.setLsTransaction(analysisGroupDTO.getLsTransaction());
        this.setModifiedBy(analysisGroupDTO.getModifiedBy());
        this.setModifiedDate(analysisGroupDTO.getModifiedDate());
        this.setLsType(analysisGroupDTO.getStateType());
        this.setLsKind(analysisGroupDTO.getStateKind());
    }

    public AnalysisGroupState(FlatThingCsvDTO analysisGroupDTO) {
        this.setRecordedBy(analysisGroupDTO.getRecordedBy());
        this.setRecordedDate(analysisGroupDTO.getRecordedDate());
        this.setLsTransaction(analysisGroupDTO.getLsTransaction());
        this.setModifiedBy(analysisGroupDTO.getModifiedBy());
        this.setModifiedDate(analysisGroupDTO.getModifiedDate());
        this.setLsType(analysisGroupDTO.getStateType());
        this.setLsKind(analysisGroupDTO.getStateKind());
    }

    public static com.labsynch.labseer.domain.AnalysisGroupState update(
            com.labsynch.labseer.domain.AnalysisGroupState analysisGroupState) {
        AnalysisGroupState updatedAnalysisGroupState = AnalysisGroupState
                .findAnalysisGroupState(analysisGroupState.getId());
        updatedAnalysisGroupState.setRecordedBy(analysisGroupState.getRecordedBy());
        updatedAnalysisGroupState.setRecordedDate(analysisGroupState.getRecordedDate());
        updatedAnalysisGroupState.setLsTransaction(analysisGroupState.getLsTransaction());
        updatedAnalysisGroupState.setModifiedBy(analysisGroupState.getModifiedBy());
        updatedAnalysisGroupState.setModifiedDate(new Date());
        updatedAnalysisGroupState.setLsType(analysisGroupState.getLsType());
        updatedAnalysisGroupState.setLsKind(analysisGroupState.getLsKind());
        updatedAnalysisGroupState.merge();
        return updatedAnalysisGroupState;
    }

    public String toJson() {
        return new JSONSerializer().include("lsValues").exclude("*.class", "analysisGroup.experiment")
                .transform(new ExcludeNulls(), void.class).serialize(this);
    }

    public static com.labsynch.labseer.domain.AnalysisGroupState fromJsonToAnalysisGroupState(String json) {
        return new JSONDeserializer<AnalysisGroupState>().use(null, AnalysisGroupState.class).deserialize(json);
    }

    public static String toJsonArray(Collection<com.labsynch.labseer.domain.AnalysisGroupState> collection) {
        return new JSONSerializer().exclude("*.class", "analysisGroup.experiment")
                .transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    public static String toJsonArrayStub(Collection<com.labsynch.labseer.domain.AnalysisGroupState> collection) {
        return new JSONSerializer().exclude("*.class", "analysisGroup").transform(new ExcludeNulls(), void.class)
                .serialize(collection);
    }

    public static Collection<com.labsynch.labseer.domain.AnalysisGroupState> fromJsonArrayToAnalysisGroupStates(
            String json) {
        return new JSONDeserializer<List<AnalysisGroupState>>().use(null, ArrayList.class)
                .use("values", AnalysisGroupState.class).deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.AnalysisGroupState> fromJsonArrayToAnalysisGroupStates(
            Reader json) {
        return new JSONDeserializer<List<AnalysisGroupState>>().use(null, ArrayList.class)
                .use("values", AnalysisGroupState.class).deserialize(json);
    }

    public static int deleteByExperimentID(Long experimentId) {
        if (experimentId == null)
            return 0;
        EntityManager em = SubjectValue.entityManager();
        String deleteSQL = "DELETE FROM AnalysisGroupState oo WHERE id in (select o.id from AnalysisGroupState o where o.analysisGroup.experiment.id = :experimentId)";
        Query q = em.createQuery(deleteSQL);
        q.setParameter("experimentId", experimentId);
        int numberOfDeletedEntities = q.executeUpdate();
        return numberOfDeletedEntities;
    }

    public static TypedQuery<AnalysisGroupState> findAnalysisGroupStatesByAnalysisGroupIDAndStateTypeKind(
            Long analysisGroupId,
            String stateType,
            String stateKind) {
        if (stateType == null || stateKind.length() == 0)
            throw new IllegalArgumentException("The stateType argument is required");
        if (stateKind == null || stateKind.length() == 0)
            throw new IllegalArgumentException("The stateKind argument is required");

        EntityManager em = entityManager();
        String hsqlQuery = "SELECT ags FROM AnalysisGroupState AS ags " +
                "JOIN ags.analysisGroup ag " +
                "WHERE ags.lsType = :stateType AND ags.lsKind = :stateKind AND ags.ignored IS NOT :ignored " +
                "AND ag.id = :analysisGroupId ";
        TypedQuery<AnalysisGroupState> q = em.createQuery(hsqlQuery, AnalysisGroupState.class);
        q.setParameter("analysisGroupId", analysisGroupId);
        q.setParameter("stateType", stateType);
        q.setParameter("stateKind", stateKind);
        q.setParameter("ignored", true);
        return q;
    }

    public static TypedQuery<AnalysisGroupState> findAnalysisGroupStatesByAnalysisGroupCodeNameAndStateTypeKind(
            String analysisGroupCodeName,
            String stateType,
            String stateKind) {
        if (stateType == null || stateKind.length() == 0)
            throw new IllegalArgumentException("The stateType argument is required");
        if (stateKind == null || stateKind.length() == 0)
            throw new IllegalArgumentException("The stateKind argument is required");

        EntityManager em = entityManager();
        String hsqlQuery = "SELECT ags FROM AnalysisGroupState AS ags " +
                "JOIN ags.analysisGroup ag " +
                "WHERE ags.lsType = :stateType AND ags.lsKind = :stateKind AND ags.ignored IS NOT :ignored " +
                "AND ag.codeName = :analysisGroupCodeName ";
        TypedQuery<AnalysisGroupState> q = em.createQuery(hsqlQuery, AnalysisGroupState.class);
        q.setParameter("analysisGroupCodeName", analysisGroupCodeName);
        q.setParameter("stateType", stateType);
        q.setParameter("stateKind", stateKind);
        q.setParameter("ignored", true);
        return q;
    }

    public static TypedQuery<AnalysisGroupState> findAnalysisGroupStatesByAnalysisGroupAndLsTypeEqualsAndLsKindEqualsAndIgnoredNot(
            AnalysisGroup analysisGroup, String lsType, String lsKind, boolean ignored) {
        if (analysisGroup == null)
            throw new IllegalArgumentException("The analysisGroup argument is required");
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = AnalysisGroupState.entityManager();
        TypedQuery<AnalysisGroupState> q = em.createQuery(
                "SELECT o FROM AnalysisGroupState AS o WHERE o.analysisGroup = :analysisGroup AND o.lsType = :lsType  AND o.lsKind = :lsKind  AND o.ignored IS NOT :ignored",
                AnalysisGroupState.class);
        q.setParameter("analysisGroup", analysisGroup);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("ignored", ignored);
        return q;
    }

    public static TypedQuery<AnalysisGroupState> findAnalysisGroupStatesByLsTypeAndKindEquals(String lsTypeAndKind) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0)
            throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = AnalysisGroupState.entityManager();
        TypedQuery<AnalysisGroupState> q = em.createQuery(
                "SELECT o FROM AnalysisGroupState AS o WHERE o.lsTypeAndKind = :lsTypeAndKind",
                AnalysisGroupState.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return q;
    }

    public static TypedQuery<AnalysisGroupState> findAnalysisGroupStatesByLsTypeEqualsAndLsKindEquals(String lsType,
            String lsKind) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = AnalysisGroupState.entityManager();
        TypedQuery<AnalysisGroupState> q = em.createQuery(
                "SELECT o FROM AnalysisGroupState AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind",
                AnalysisGroupState.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("analysisGroup",
            "lsValues");

    public static long countAnalysisGroupStates() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AnalysisGroupState o", Long.class).getSingleResult();
    }

    public static List<AnalysisGroupState> findAllAnalysisGroupStates() {
        return entityManager().createQuery("SELECT o FROM AnalysisGroupState o", AnalysisGroupState.class)
                .getResultList();
    }

    public static List<AnalysisGroupState> findAllAnalysisGroupStates(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM AnalysisGroupState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, AnalysisGroupState.class).getResultList();
    }

    public static AnalysisGroupState findAnalysisGroupState(Long id) {
        if (id == null)
            return null;
        return entityManager().find(AnalysisGroupState.class, id);
    }

    public static List<AnalysisGroupState> findAnalysisGroupStateEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AnalysisGroupState o", AnalysisGroupState.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<AnalysisGroupState> findAnalysisGroupStateEntries(int firstResult, int maxResults,
            String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM AnalysisGroupState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, AnalysisGroupState.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public AnalysisGroupState merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        AnalysisGroupState merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public AnalysisGroupState() {
        super();
    }

    public static Long countFindAnalysisGroupStatesByAnalysisGroup(AnalysisGroup analysisGroup) {
        if (analysisGroup == null)
            throw new IllegalArgumentException("The analysisGroup argument is required");
        EntityManager em = AnalysisGroupState.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM AnalysisGroupState AS o WHERE o.analysisGroup = :analysisGroup", Long.class);
        q.setParameter("analysisGroup", analysisGroup);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindAnalysisGroupStatesByAnalysisGroupAndLsTypeEqualsAndLsKindEqualsAndIgnoredNot(
            AnalysisGroup analysisGroup, String lsType, String lsKind, boolean ignored) {
        if (analysisGroup == null)
            throw new IllegalArgumentException("The analysisGroup argument is required");
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = AnalysisGroupState.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM AnalysisGroupState AS o WHERE o.analysisGroup = :analysisGroup AND o.lsType = :lsType  AND o.lsKind = :lsKind  AND o.ignored IS NOT :ignored",
                Long.class);
        q.setParameter("analysisGroup", analysisGroup);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindAnalysisGroupStatesByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = AnalysisGroupState.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM AnalysisGroupState AS o WHERE o.lsTransaction = :lsTransaction", Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindAnalysisGroupStatesByLsTypeAndKindEquals(String lsTypeAndKind) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0)
            throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = AnalysisGroupState.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM AnalysisGroupState AS o WHERE o.lsTypeAndKind = :lsTypeAndKind", Long.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindAnalysisGroupStatesByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = AnalysisGroupState.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM AnalysisGroupState AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind",
                Long.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<AnalysisGroupState> findAnalysisGroupStatesByAnalysisGroup(AnalysisGroup analysisGroup) {
        if (analysisGroup == null)
            throw new IllegalArgumentException("The analysisGroup argument is required");
        EntityManager em = AnalysisGroupState.entityManager();
        TypedQuery<AnalysisGroupState> q = em.createQuery(
                "SELECT o FROM AnalysisGroupState AS o WHERE o.analysisGroup = :analysisGroup",
                AnalysisGroupState.class);
        q.setParameter("analysisGroup", analysisGroup);
        return q;
    }

    public static TypedQuery<AnalysisGroupState> findAnalysisGroupStatesByAnalysisGroup(AnalysisGroup analysisGroup,
            String sortFieldName, String sortOrder) {
        if (analysisGroup == null)
            throw new IllegalArgumentException("The analysisGroup argument is required");
        EntityManager em = AnalysisGroupState.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM AnalysisGroupState AS o WHERE o.analysisGroup = :analysisGroup");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AnalysisGroupState> q = em.createQuery(queryBuilder.toString(), AnalysisGroupState.class);
        q.setParameter("analysisGroup", analysisGroup);
        return q;
    }

    public static TypedQuery<AnalysisGroupState> findAnalysisGroupStatesByAnalysisGroupAndLsTypeEqualsAndLsKindEqualsAndIgnoredNot(
            AnalysisGroup analysisGroup, String lsType, String lsKind, boolean ignored, String sortFieldName,
            String sortOrder) {
        if (analysisGroup == null)
            throw new IllegalArgumentException("The analysisGroup argument is required");
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = AnalysisGroupState.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM AnalysisGroupState AS o WHERE o.analysisGroup = :analysisGroup AND o.lsType = :lsType  AND o.lsKind = :lsKind  AND o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AnalysisGroupState> q = em.createQuery(queryBuilder.toString(), AnalysisGroupState.class);
        q.setParameter("analysisGroup", analysisGroup);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("ignored", ignored);
        return q;
    }

    public static TypedQuery<AnalysisGroupState> findAnalysisGroupStatesByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = AnalysisGroupState.entityManager();
        TypedQuery<AnalysisGroupState> q = em.createQuery(
                "SELECT o FROM AnalysisGroupState AS o WHERE o.lsTransaction = :lsTransaction",
                AnalysisGroupState.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

    public static TypedQuery<AnalysisGroupState> findAnalysisGroupStatesByLsTransactionEquals(Long lsTransaction,
            String sortFieldName, String sortOrder) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = AnalysisGroupState.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM AnalysisGroupState AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AnalysisGroupState> q = em.createQuery(queryBuilder.toString(), AnalysisGroupState.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

    public static TypedQuery<AnalysisGroupState> findAnalysisGroupStatesByLsTypeAndKindEquals(String lsTypeAndKind,
            String sortFieldName, String sortOrder) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0)
            throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = AnalysisGroupState.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM AnalysisGroupState AS o WHERE o.lsTypeAndKind = :lsTypeAndKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AnalysisGroupState> q = em.createQuery(queryBuilder.toString(), AnalysisGroupState.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return q;
    }

    public static TypedQuery<AnalysisGroupState> findAnalysisGroupStatesByLsTypeEqualsAndLsKindEquals(String lsType,
            String lsKind, String sortFieldName, String sortOrder) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = AnalysisGroupState.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM AnalysisGroupState AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AnalysisGroupState> q = em.createQuery(queryBuilder.toString(), AnalysisGroupState.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    public AnalysisGroup getAnalysisGroup() {
        return this.analysisGroup;
    }

    public void setAnalysisGroup(AnalysisGroup analysisGroup) {
        this.analysisGroup = analysisGroup;
    }

    public Set<AnalysisGroupValue> getLsValues() {
        return this.lsValues;
    }

    public void setLsValues(Set<AnalysisGroupValue> lsValues) {
        this.lsValues = lsValues;
    }
}
