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
import jakarta.persistence.TypedQuery;

import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable

public class ItxLsThingLsThingValue extends AbstractValue {

    @ManyToOne
    @JoinColumn(name = "ls_state")
    private ItxLsThingLsThingState lsState;

    public ItxLsThingLsThingValue(com.labsynch.labseer.domain.ItxLsThingLsThingValue itxLsThingLsThingValue) {
        super.setBlobValue(itxLsThingLsThingValue.getBlobValue());
        super.setClobValue(itxLsThingLsThingValue.getClobValue());
        super.setCodeKind(itxLsThingLsThingValue.getCodeKind());
        super.setCodeOrigin(itxLsThingLsThingValue.getCodeOrigin());
        super.setCodeType(itxLsThingLsThingValue.getCodeType());
        super.setCodeTypeAndKind(itxLsThingLsThingValue.getCodeTypeAndKind());
        super.setCodeValue(itxLsThingLsThingValue.getCodeValue());
        super.setComments(itxLsThingLsThingValue.getComments());
        super.setConcentration(itxLsThingLsThingValue.getConcentration());
        super.setConcUnit(itxLsThingLsThingValue.getConcUnit());
        super.setDateValue(itxLsThingLsThingValue.getDateValue());
        super.setDeleted(itxLsThingLsThingValue.isDeleted());
        super.setFileValue(itxLsThingLsThingValue.getFileValue());
        super.setIgnored(itxLsThingLsThingValue.isIgnored());
        super.setLsKind(itxLsThingLsThingValue.getLsKind());
        super.setLsTransaction(itxLsThingLsThingValue.getLsTransaction());
        super.setLsType(itxLsThingLsThingValue.getLsType());
        super.setLsTypeAndKind(itxLsThingLsThingValue.getLsTypeAndKind());
        super.setModifiedBy(itxLsThingLsThingValue.getModifiedBy());
        super.setModifiedDate(itxLsThingLsThingValue.getModifiedDate());
        super.setNumberOfReplicates(itxLsThingLsThingValue.getNumberOfReplicates());
        super.setNumericValue(itxLsThingLsThingValue.getNumericValue());
        super.setOperatorKind(itxLsThingLsThingValue.getOperatorKind());
        super.setOperatorType(itxLsThingLsThingValue.getOperatorType());
        super.setOperatorTypeAndKind(itxLsThingLsThingValue.getOperatorTypeAndKind());
        super.setPublicData(itxLsThingLsThingValue.isPublicData());
        super.setRecordedBy(itxLsThingLsThingValue.getRecordedBy());
        super.setRecordedDate(itxLsThingLsThingValue.getRecordedDate());
        super.setSigFigs(itxLsThingLsThingValue.getSigFigs());
        super.setStringValue(itxLsThingLsThingValue.getStringValue());
        super.setUncertainty(itxLsThingLsThingValue.getUncertainty());
        super.setUncertaintyType(itxLsThingLsThingValue.getUncertaintyType());
        super.setUnitKind(itxLsThingLsThingValue.getUnitKind());
        super.setUnitType(itxLsThingLsThingValue.getUnitType());
        super.setUnitTypeAndKind(itxLsThingLsThingValue.getUnitTypeAndKind());
        super.setUrlValue(itxLsThingLsThingValue.getUrlValue());
        super.setVersion(itxLsThingLsThingValue.getVersion());
    }

    public ItxLsThingLsThingValue() {
    }

    public static com.labsynch.labseer.domain.ItxLsThingLsThingValue update(
            com.labsynch.labseer.domain.ItxLsThingLsThingValue object) {
        ItxLsThingLsThingValue updatedObject = new JSONDeserializer<ItxLsThingLsThingValue>()
                .use(null, ItxLsThingLsThingValue.class)
                .deserializeInto(object.toJson(), ItxLsThingLsThingValue.findItxLsThingLsThingValue(object.getId()));
        updatedObject.setModifiedDate(new Date());
        updatedObject.merge();
        return updatedObject;
    }

    public static com.labsynch.labseer.domain.ItxLsThingLsThingValue updateNoMerge(
            com.labsynch.labseer.domain.ItxLsThingLsThingValue object) {
        ItxLsThingLsThingValue updatedObject = new JSONDeserializer<ItxLsThingLsThingValue>()
                .use(null, ItxLsThingLsThingValue.class)
                .deserializeInto(object.toJson(), ItxLsThingLsThingValue.findItxLsThingLsThingValue(object.getId()));
        updatedObject.setModifiedDate(new Date());
        return updatedObject;
    }

    public static com.labsynch.labseer.domain.ItxLsThingLsThingValue fromJsonToItxLsThingLsThingValue(String json) {
        return new JSONDeserializer<ItxLsThingLsThingValue>().use(null, ItxLsThingLsThingValue.class).deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.ItxLsThingLsThingValue> fromJsonArrayToItxLsThingLsThingValues(
            String json) {
        return new JSONDeserializer<List<ItxLsThingLsThingValue>>().use(null, ArrayList.class)
                .use("values", ItxLsThingLsThingValue.class).deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.ItxLsThingLsThingValue> fromJsonArrayToItxLsThingLsThingValues(
            Reader json) {
        return new JSONDeserializer<List<ItxLsThingLsThingValue>>().use(null, ArrayList.class)
                .use("values", ItxLsThingLsThingValue.class).deserialize(json);
    }

    @Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public static String toJsonArray(Collection<com.labsynch.labseer.domain.ItxLsThingLsThingValue> collection) {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStub(Collection<com.labsynch.labseer.domain.ItxLsThingLsThingValue> collection) {
        return new JSONSerializer().exclude("*.class", "lsState").transform(new ExcludeNulls(), void.class)
                .serialize(collection);
    }

    public static com.labsynch.labseer.domain.ItxLsThingLsThingValue create(
            com.labsynch.labseer.domain.ItxLsThingLsThingValue lsThingValue) {
        ItxLsThingLsThingValue newItxLsThingLsThingValue = new JSONDeserializer<ItxLsThingLsThingValue>()
                .use(null, ItxLsThingLsThingValue.class)
                .deserializeInto(lsThingValue.toJson(), new ItxLsThingLsThingValue());
        return newItxLsThingLsThingValue;
    }

    public static TypedQuery<ItxLsThingLsThingValue> findItxLsValueByItxThingAndStateTypeStateKindAndValueTypeKind(
            Long itxOrthologId, String stateType,
            String stateKind, String valueType, String valueKind) {
        if (valueType == null || valueType.length() == 0)
            throw new IllegalArgumentException("The valueType argument is required");
        if (valueKind == null || valueKind.length() == 0)
            throw new IllegalArgumentException("The valueKind argument is required");

        EntityManager em = ItxLsThingLsThingValue.entityManager();
        String hqlQuery = "SELECT o FROM ItxLsThingLsThingValue AS o "
                + "JOIN o.lsState its with its.ignored = false AND its.lsType = :stateType and its.lsKind = :stateKind "
                + "JOIN its.itxLsThingLsThing itxThing with itxThing.ignored = false "
                + "WHERE o.lsType = :valueType AND o.lsKind = :valueKind "
                + "AND itxThing.id = :itxOrthologId";

        TypedQuery<ItxLsThingLsThingValue> q = em.createQuery(hqlQuery, ItxLsThingLsThingValue.class);
        q.setParameter("stateType", stateType);
        q.setParameter("stateKind", stateKind);
        q.setParameter("valueType", valueType);
        q.setParameter("valueKind", valueKind);
        q.setParameter("itxOrthologId", itxOrthologId);
        return q;
    }

    public static TypedQuery<ItxLsThingLsThingValue> findItxLsThingLsThingValuesByLsKindEquals(String lsKind) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = ItxLsThingLsThingValue.entityManager();
        TypedQuery<ItxLsThingLsThingValue> q = em.createQuery(
                "SELECT o FROM ItxLsThingLsThingValue AS o WHERE o.lsKind = :lsKind", ItxLsThingLsThingValue.class);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    public static TypedQuery<ItxLsThingLsThingValue> findItxLsThingLsThingValuesByLsTransactionEquals(
            Long lsTransaction) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ItxLsThingLsThingValue.entityManager();
        TypedQuery<ItxLsThingLsThingValue> q = em.createQuery(
                "SELECT o FROM ItxLsThingLsThingValue AS o WHERE o.lsTransaction = :lsTransaction",
                ItxLsThingLsThingValue.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("lsState");

    public static long countItxLsThingLsThingValues() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ItxLsThingLsThingValue o", Long.class)
                .getSingleResult();
    }

    public static List<ItxLsThingLsThingValue> findAllItxLsThingLsThingValues() {
        return entityManager().createQuery("SELECT o FROM ItxLsThingLsThingValue o", ItxLsThingLsThingValue.class)
                .getResultList();
    }

    public static List<ItxLsThingLsThingValue> findAllItxLsThingLsThingValues(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxLsThingLsThingValue o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxLsThingLsThingValue.class).getResultList();
    }

    public static ItxLsThingLsThingValue findItxLsThingLsThingValue(Long id) {
        if (id == null)
            return null;
        return entityManager().find(ItxLsThingLsThingValue.class, id);
    }

    public static List<ItxLsThingLsThingValue> findItxLsThingLsThingValueEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ItxLsThingLsThingValue o", ItxLsThingLsThingValue.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<ItxLsThingLsThingValue> findItxLsThingLsThingValueEntries(int firstResult, int maxResults,
            String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxLsThingLsThingValue o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxLsThingLsThingValue.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public ItxLsThingLsThingValue merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        ItxLsThingLsThingValue merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public static Long countFindItxLsThingLsThingValuesByLsKindEquals(String lsKind) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = ItxLsThingLsThingValue.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ItxLsThingLsThingValue AS o WHERE o.lsKind = :lsKind",
                Long.class);
        q.setParameter("lsKind", lsKind);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindItxLsThingLsThingValuesByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ItxLsThingLsThingValue.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM ItxLsThingLsThingValue AS o WHERE o.lsTransaction = :lsTransaction", Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<ItxLsThingLsThingValue> findItxLsThingLsThingValuesByLsKindEquals(String lsKind,
            String sortFieldName, String sortOrder) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = ItxLsThingLsThingValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM ItxLsThingLsThingValue AS o WHERE o.lsKind = :lsKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ItxLsThingLsThingValue> q = em.createQuery(queryBuilder.toString(), ItxLsThingLsThingValue.class);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    public static TypedQuery<ItxLsThingLsThingValue> findItxLsThingLsThingValuesByLsTransactionEquals(
            Long lsTransaction, String sortFieldName, String sortOrder) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ItxLsThingLsThingValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM ItxLsThingLsThingValue AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ItxLsThingLsThingValue> q = em.createQuery(queryBuilder.toString(), ItxLsThingLsThingValue.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

    public ItxLsThingLsThingState getLsState() {
        return this.lsState;
    }

    public void setLsState(ItxLsThingLsThingState lsState) {
        this.lsState = lsState;
    }
}
