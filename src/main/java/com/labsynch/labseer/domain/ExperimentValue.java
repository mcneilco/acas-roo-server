package com.labsynch.labseer.domain;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity

public class ExperimentValue extends AbstractValue {

    private static final Logger logger = LoggerFactory.getLogger(ExperimentValue.class);

    @NotNull
    @ManyToOne
    @JoinColumn(name = "experiment_state_id")
    private ExperimentState lsState;

    public ExperimentValue() {
    }

    public static com.labsynch.labseer.domain.ExperimentValue create(
            com.labsynch.labseer.domain.ExperimentValue experimentValue) {
        ExperimentValue newExperimentValue = new JSONDeserializer<ExperimentValue>().use(null, ExperimentValue.class)
                .deserializeInto(experimentValue.toJson(), new ExperimentValue());
        return newExperimentValue;
    }

    public static com.labsynch.labseer.domain.ExperimentValue create(String experimentValueJson) {
        ExperimentValue newExperimentValue = new JSONDeserializer<ExperimentValue>().use(null, ExperimentValue.class)
                .deserializeInto(experimentValueJson, new ExperimentValue());
        return newExperimentValue;
    }

    public static long countExperimentValues() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ExperimentValue o", Long.class).getSingleResult();
    }

    public static List<com.labsynch.labseer.domain.ExperimentValue> findAllExperimentValues() {
        return entityManager().createQuery("SELECT o FROM ExperimentValue o", ExperimentValue.class).getResultList();
    }

    public static com.labsynch.labseer.domain.ExperimentValue findExperimentValue(Long id) {
        if (id == null)
            return null;
        return entityManager().find(ExperimentValue.class, id);
    }

    public static List<com.labsynch.labseer.domain.ExperimentValue> findExperimentValueEntries(int firstResult,
            int maxResults) {
        return entityManager().createQuery("SELECT o FROM ExperimentValue o", ExperimentValue.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static com.labsynch.labseer.domain.ExperimentValue fromJsonToExperimentValue(String json) {
        return new JSONDeserializer<ExperimentValue>().use(null, ExperimentValue.class).deserialize(json);

    }

    public static Collection<com.labsynch.labseer.domain.ExperimentValue> fromJsonArrayToExperimentValues(String json) {
        return new JSONDeserializer<List<ExperimentValue>>().use(null, ArrayList.class)
                .use("values", ExperimentValue.class).deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.ExperimentValue> fromJsonArrayToExperimentValues(Reader json) {
        return new JSONDeserializer<List<ExperimentValue>>().use(null, ArrayList.class)
                .use("values", ExperimentValue.class).deserialize(json);
    }

    @Transactional
    public com.labsynch.labseer.domain.ExperimentValue merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        ExperimentValue merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    @Transactional
    public static List<java.lang.Long> saveList(List<com.labsynch.labseer.domain.ExperimentValue> entities) {
        logger.debug("saving the list of ExperimentValues: " + entities.size());
        List<Long> idList = new ArrayList<Long>();
        int imported = 0;
        for (ExperimentValue e : entities) {
            e.persist();
            idList.add(e.getId());
            if (++imported % 50 == 0) {
                e.flush();
                e.clear();
            }
        }
        return idList;
    }

    @Transactional
    public static String getExperimentGroupValueCollectionJson(List<java.lang.Long> idList) {
        Collection<ExperimentValue> experimentValues = new HashSet<ExperimentValue>();
        for (Long id : idList) {
            ExperimentValue query = ExperimentValue.findExperimentValue(id);
            if (query != null)
                experimentValues.add(query);
        }
        return ExperimentValue.toJsonArray(experimentValues);
    }

    @Transactional
    public static int deleteByExperimentID(Long experimentId) {
        if (experimentId == null)
            return 0;
        EntityManager em = TreatmentGroupValue.entityManager();
        String deleteSQL = "DELETE FROM ExperimentValue oo WHERE id in (select o.id from ExperimentValue o where o.lsState.subject.treatmentGroup.analysisGroup.experiment.id = :experimentId)";
        Query q = em.createQuery(deleteSQL);
        q.setParameter("experimentId", experimentId);
        int numberOfDeletedEntities = q.executeUpdate();
        return numberOfDeletedEntities;
    }

    public static com.labsynch.labseer.domain.ExperimentValue update(
            com.labsynch.labseer.domain.ExperimentValue experimentValue) {
        ExperimentValue updatedExperimentValue = new JSONDeserializer<ExperimentValue>()
                .use(null, ExperimentValue.class).deserializeInto(experimentValue.toJson(),
                        ExperimentValue.findExperimentValue(experimentValue.getId()));
        updatedExperimentValue.setModifiedDate(new Date());
        updatedExperimentValue.merge();
        return updatedExperimentValue;
    }

    public static TypedQuery<com.labsynch.labseer.domain.ExperimentValue> findExperimentValuesByExptIDAndStateTypeKindAndValueTypeKind(
            Long experimentId, String stateType, String stateKind, String valueType, String valueKind) {
        if (stateType == null || stateKind.length() == 0)
            throw new IllegalArgumentException("The stateType argument is required");
        if (stateKind == null || stateKind.length() == 0)
            throw new IllegalArgumentException("The stateKind argument is required");
        if (valueType == null || valueType.length() == 0)
            throw new IllegalArgumentException("The valueType argument is required");
        if (valueKind == null || valueKind.length() == 0)
            throw new IllegalArgumentException("The valueKind argument is required");
        EntityManager em = entityManager();
        String hsqlQuery = "SELECT ev FROM ExperimentValue AS ev " + "JOIN ev.lsState evs " + "JOIN evs.experiment exp "
                + "WHERE evs.lsType = :stateType AND evs.lsKind = :stateKind AND evs.ignored IS NOT :ignored "
                + "AND ev.lsType = :valueType AND ev.lsKind = :valueKind AND ev.ignored IS NOT :ignored "
                + "AND exp.id = :experimentId ";
        TypedQuery<ExperimentValue> q = em.createQuery(hsqlQuery, ExperimentValue.class);
        q.setParameter("experimentId", experimentId);
        q.setParameter("stateType", stateType);
        q.setParameter("stateKind", stateKind);
        q.setParameter("valueType", valueType);
        q.setParameter("valueKind", valueKind);
        q.setParameter("ignored", true);
        return q;
    }

    public static TypedQuery<com.labsynch.labseer.domain.ExperimentValue> findExperimentValuesByStateTypeKindAndValueTypeKind(
        String stateType, String stateKind, String valueType, String valueKind) {
        if (stateType == null || stateKind.length() == 0)
            throw new IllegalArgumentException("The stateType argument is required");
        if (stateKind == null || stateKind.length() == 0)
            throw new IllegalArgumentException("The stateKind argument is required");
        if (valueType == null || valueType.length() == 0)
            throw new IllegalArgumentException("The valueType argument is required");
        if (valueKind == null || valueKind.length() == 0)
            throw new IllegalArgumentException("The valueKind argument is required");
        EntityManager em = entityManager();
        String hsqlQuery = "SELECT ev FROM ExperimentValue AS ev " + "JOIN ev.lsState evs " + "JOIN evs.experiment exp "
                + "WHERE evs.lsType = :stateType AND evs.lsKind = :stateKind AND evs.ignored IS NOT :ignored "
                + "AND ev.lsType = :valueType AND ev.lsKind = :valueKind AND ev.ignored IS NOT :ignored ";
        TypedQuery<ExperimentValue> q = em.createQuery(hsqlQuery, ExperimentValue.class);
        q.setParameter("stateType", stateType);
        q.setParameter("stateKind", stateKind);
        q.setParameter("valueType", valueType);
        q.setParameter("valueKind", valueKind);
        q.setParameter("ignored", true);
        return q;
    }

    public static TypedQuery<com.labsynch.labseer.domain.ExperimentValue> findExperimentValuesByExperimentCodeNameAndStateTypeKindAndValueTypeKind(
            String experimentCodeName, String stateType, String stateKind, String valueType, String valueKind) {
        if (stateType == null || stateKind.length() == 0)
            throw new IllegalArgumentException("The stateType argument is required");
        if (stateKind == null || stateKind.length() == 0)
            throw new IllegalArgumentException("The stateKind argument is required");
        if (valueType == null || valueType.length() == 0)
            throw new IllegalArgumentException("The valueType argument is required");
        if (valueKind == null || valueKind.length() == 0)
            throw new IllegalArgumentException("The valueKind argument is required");
        EntityManager em = entityManager();
        String hsqlQuery = "SELECT ev FROM ExperimentValue AS ev " + "JOIN ev.lsState evs " + "JOIN evs.experiment exp "
                + "WHERE evs.lsType = :stateType AND evs.lsKind = :stateKind AND evs.ignored IS NOT :ignored "
                + "AND ev.lsType = :valueType AND ev.lsKind = :valueKind AND ev.ignored IS NOT :ignored "
                + "AND exp.codeName = :experimentCodeName ";
        TypedQuery<ExperimentValue> q = em.createQuery(hsqlQuery, ExperimentValue.class);
        q.setParameter("experimentCodeName", experimentCodeName);
        q.setParameter("stateType", stateType);
        q.setParameter("stateKind", stateKind);
        q.setParameter("valueType", valueType);
        q.setParameter("valueKind", valueKind);
        q.setParameter("ignored", true);
        return q;
    }

    public static TypedQuery<com.labsynch.labseer.domain.ExperimentValue> findExperimentValuesByExptIDAndStateTypeKind(
            Long experimentId, String stateType, String stateKind) {
        if (stateType == null || stateKind.length() == 0)
            throw new IllegalArgumentException("The stateType argument is required");
        if (stateKind == null || stateKind.length() == 0)
            throw new IllegalArgumentException("The stateKind argument is required");
        EntityManager em = entityManager();
        String hsqlQuery = "SELECT ev FROM ExperimentValue AS ev " + "JOIN ev.lsState evs " + "JOIN evs.experiment exp "
                + "WHERE evs.lsType = :stateType AND evs.lsKind = :stateKind AND evs.ignored IS NOT :ignored "
                + "AND ev.ignored IS NOT :ignored " + "AND exp.id = :experimentId ";
        TypedQuery<ExperimentValue> q = em.createQuery(hsqlQuery, ExperimentValue.class);
        q.setParameter("experimentId", experimentId);
        q.setParameter("stateType", stateType);
        q.setParameter("stateKind", stateKind);
        q.setParameter("ignored", true);
        return q;
    }

    public static TypedQuery<ExperimentValue> findExperimentValuesByLsKindEqualsAndDateValueLike(String lsKind,
            Date dateValue) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        if (dateValue == null)
            throw new IllegalArgumentException("The dateValue argument is required");
        EntityManager em = ExperimentValue.entityManager();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateValue);
        cal.add(Calendar.DATE, -1);
        Date beforeDate = cal.getTime();
        cal.setTime(dateValue);
        cal.add(Calendar.DATE, 1);
        Date afterDate = cal.getTime();
        TypedQuery<ExperimentValue> q = em.createQuery(
                "SELECT o FROM ExperimentValue AS o WHERE o.lsKind = :lsKind AND o.ignored =false AND o.dateValue > :beforeDate AND o.dateValue < :afterDate",
                ExperimentValue.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("beforeDate", beforeDate);
        q.setParameter("afterDate", afterDate);
        return q;
    }

    public static String[] getColumns() {
        String[] headerColumns = new String[] { "id", "codeName", "lsType", "lsKind", "labelText", "description",
                "comments", "ignored", "displayOrder" };
        return headerColumns;
    }

    public static CellProcessor[] getProcessors() {
        final CellProcessor[] processors = new CellProcessor[] { new Optional(), new Optional(), new Optional(),
                new Optional(), new Optional(), new Optional(), new Optional(), new Optional(), new Optional() };
        return processors;
    }

    public static String[] getAllColumns() {
        String[] headerColumns = new String[] {
                "lsState",
                "id",
                "lsType",
                "lsKind",
                "codeType",
                "codeKind",
                "codeValue",
                "stringValue",

                "fileValue",
                "urlValue",
                "dateValue",
                "clobValue",
                "operatorType",
                "operatorKind",
                "numericValue",
                "sigFigs",
                "uncertainty",
                "numberOfReplicates",

                "uncertaintyType",
                "unitType",
                "unitKind",
                "comments",
                "ignored",
                "lsTransaction",
                "recordedDate",
                "recordedBy",
                "modifiedDate",
                "modifiedBy",

                "publicData"
        };
        // 31 columns
        return headerColumns;

    }

    public static CellProcessor[] getAllProcessors() {
        final CellProcessor[] processors = new CellProcessor[] {

                new Optional(),

                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),

                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),

                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),
                new Optional(),

                new Optional()

        };

        return processors;
    }

    public ExperimentState getLsState() {
        return this.lsState;
    }

    public void setLsState(ExperimentState lsState) {
        this.lsState = lsState;
    }

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "lsState");

    public static List<ExperimentValue> findAllExperimentValues(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ExperimentValue o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ExperimentValue.class).getResultList();
    }

    public static List<ExperimentValue> findExperimentValueEntries(int firstResult, int maxResults,
            String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ExperimentValue o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ExperimentValue.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public static Long countFindExperimentValuesByLsKindEqualsAndCodeValueLike(String lsKind, String codeValue) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        if (codeValue == null || codeValue.length() == 0)
            throw new IllegalArgumentException("The codeValue argument is required");
        codeValue = codeValue.replace('*', '%');
        if (codeValue.charAt(0) != '%') {
            codeValue = "%" + codeValue;
        }
        if (codeValue.charAt(codeValue.length() - 1) != '%') {
            codeValue = codeValue + "%";
        }
        EntityManager em = ExperimentValue.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM ExperimentValue AS o WHERE o.lsKind = :lsKind  AND LOWER(o.codeValue) LIKE LOWER(:codeValue)",
                Long.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("codeValue", codeValue);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindExperimentValuesByLsKindEqualsAndStringValueLike(String lsKind, String stringValue) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        if (stringValue == null || stringValue.length() == 0)
            throw new IllegalArgumentException("The stringValue argument is required");
        stringValue = stringValue.replace('*', '%');
        if (stringValue.charAt(0) != '%') {
            stringValue = "%" + stringValue;
        }
        if (stringValue.charAt(stringValue.length() - 1) != '%') {
            stringValue = stringValue + "%";
        }
        EntityManager em = ExperimentValue.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM ExperimentValue AS o WHERE o.lsKind = :lsKind  AND LOWER(o.stringValue) LIKE LOWER(:stringValue)",
                Long.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("stringValue", stringValue);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindExperimentValuesByLsState(ExperimentState lsState) {
        if (lsState == null)
            throw new IllegalArgumentException("The lsState argument is required");
        EntityManager em = ExperimentValue.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ExperimentValue AS o WHERE o.lsState = :lsState",
                Long.class);
        q.setParameter("lsState", lsState);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindExperimentValuesByLsStateAndIgnoredNotAndLsKindEqualsAndLsTypeEqualsAndStringValueEquals(
            ExperimentState lsState, boolean ignored, String lsKind, String lsType, String stringValue) {
        if (lsState == null)
            throw new IllegalArgumentException("The lsState argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (stringValue == null || stringValue.length() == 0)
            throw new IllegalArgumentException("The stringValue argument is required");
        EntityManager em = ExperimentValue.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM ExperimentValue AS o WHERE o.lsState = :lsState AND o.ignored IS NOT :ignored  AND o.lsKind = :lsKind  AND o.lsType = :lsType  AND o.stringValue = :stringValue",
                Long.class);
        q.setParameter("lsState", lsState);
        q.setParameter("ignored", ignored);
        q.setParameter("lsKind", lsKind);
        q.setParameter("lsType", lsType);
        q.setParameter("stringValue", stringValue);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<ExperimentValue> findExperimentValuesByLsKindEqualsAndCodeValueLike(String lsKind,
            String codeValue) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        if (codeValue == null || codeValue.length() == 0)
            throw new IllegalArgumentException("The codeValue argument is required");
        codeValue = codeValue.replace('*', '%');
        if (codeValue.charAt(0) != '%') {
            codeValue = "%" + codeValue;
        }
        if (codeValue.charAt(codeValue.length() - 1) != '%') {
            codeValue = codeValue + "%";
        }
        EntityManager em = ExperimentValue.entityManager();
        TypedQuery<ExperimentValue> q = em.createQuery(
                "SELECT o FROM ExperimentValue AS o WHERE o.lsKind = :lsKind  AND LOWER(o.codeValue) LIKE LOWER(:codeValue)",
                ExperimentValue.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("codeValue", codeValue);
        return q;
    }

    public static TypedQuery<ExperimentValue> findExperimentValuesByLsKindEqualsAndCodeValueLike(String lsKind,
            String codeValue, String sortFieldName, String sortOrder) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        if (codeValue == null || codeValue.length() == 0)
            throw new IllegalArgumentException("The codeValue argument is required");
        codeValue = codeValue.replace('*', '%');
        if (codeValue.charAt(0) != '%') {
            codeValue = "%" + codeValue;
        }
        if (codeValue.charAt(codeValue.length() - 1) != '%') {
            codeValue = codeValue + "%";
        }
        EntityManager em = ExperimentValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM ExperimentValue AS o WHERE o.lsKind = :lsKind  AND LOWER(o.codeValue) LIKE LOWER(:codeValue)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ExperimentValue> q = em.createQuery(queryBuilder.toString(), ExperimentValue.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("codeValue", codeValue);
        return q;
    }

    public static TypedQuery<ExperimentValue> findExperimentValuesByLsKindEqualsAndStringValueLike(String lsKind,
            String stringValue) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        if (stringValue == null || stringValue.length() == 0)
            throw new IllegalArgumentException("The stringValue argument is required");
        stringValue = stringValue.replace('*', '%');
        if (stringValue.charAt(0) != '%') {
            stringValue = "%" + stringValue;
        }
        if (stringValue.charAt(stringValue.length() - 1) != '%') {
            stringValue = stringValue + "%";
        }
        EntityManager em = ExperimentValue.entityManager();
        TypedQuery<ExperimentValue> q = em.createQuery(
                "SELECT o FROM ExperimentValue AS o WHERE o.lsKind = :lsKind  AND LOWER(o.stringValue) LIKE LOWER(:stringValue)",
                ExperimentValue.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("stringValue", stringValue);
        return q;
    }

    public static TypedQuery<ExperimentValue> findExperimentValuesByLsKindEqualsAndStringValueLike(String lsKind,
            String stringValue, String sortFieldName, String sortOrder) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        if (stringValue == null || stringValue.length() == 0)
            throw new IllegalArgumentException("The stringValue argument is required");
        stringValue = stringValue.replace('*', '%');
        if (stringValue.charAt(0) != '%') {
            stringValue = "%" + stringValue;
        }
        if (stringValue.charAt(stringValue.length() - 1) != '%') {
            stringValue = stringValue + "%";
        }
        EntityManager em = ExperimentValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM ExperimentValue AS o WHERE o.lsKind = :lsKind  AND LOWER(o.stringValue) LIKE LOWER(:stringValue)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ExperimentValue> q = em.createQuery(queryBuilder.toString(), ExperimentValue.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("stringValue", stringValue);
        return q;
    }

    public static TypedQuery<ExperimentValue> findExperimentValuesByLsState(ExperimentState lsState) {
        if (lsState == null)
            throw new IllegalArgumentException("The lsState argument is required");
        EntityManager em = ExperimentValue.entityManager();
        TypedQuery<ExperimentValue> q = em.createQuery("SELECT o FROM ExperimentValue AS o WHERE o.lsState = :lsState",
                ExperimentValue.class);
        q.setParameter("lsState", lsState);
        return q;
    }

    public static TypedQuery<ExperimentValue> findExperimentValuesByLsState(ExperimentState lsState,
            String sortFieldName, String sortOrder) {
        if (lsState == null)
            throw new IllegalArgumentException("The lsState argument is required");
        EntityManager em = ExperimentValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ExperimentValue AS o WHERE o.lsState = :lsState");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ExperimentValue> q = em.createQuery(queryBuilder.toString(), ExperimentValue.class);
        q.setParameter("lsState", lsState);
        return q;
    }

    public static TypedQuery<ExperimentValue> findExperimentValuesByLsStateAndIgnoredNotAndLsKindEqualsAndLsTypeEqualsAndStringValueEquals(
            ExperimentState lsState, boolean ignored, String lsKind, String lsType, String stringValue) {
        if (lsState == null)
            throw new IllegalArgumentException("The lsState argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (stringValue == null || stringValue.length() == 0)
            throw new IllegalArgumentException("The stringValue argument is required");
        EntityManager em = ExperimentValue.entityManager();
        TypedQuery<ExperimentValue> q = em.createQuery(
                "SELECT o FROM ExperimentValue AS o WHERE o.lsState = :lsState AND o.ignored IS NOT :ignored  AND o.lsKind = :lsKind  AND o.lsType = :lsType  AND o.stringValue = :stringValue",
                ExperimentValue.class);
        q.setParameter("lsState", lsState);
        q.setParameter("ignored", ignored);
        q.setParameter("lsKind", lsKind);
        q.setParameter("lsType", lsType);
        q.setParameter("stringValue", stringValue);
        return q;
    }

    public static TypedQuery<ExperimentValue> findExperimentValuesByLsStateAndIgnoredNotAndLsKindEqualsAndLsTypeEqualsAndStringValueEquals(
            ExperimentState lsState, boolean ignored, String lsKind, String lsType, String stringValue,
            String sortFieldName, String sortOrder) {
        if (lsState == null)
            throw new IllegalArgumentException("The lsState argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (stringValue == null || stringValue.length() == 0)
            throw new IllegalArgumentException("The stringValue argument is required");
        EntityManager em = ExperimentValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM ExperimentValue AS o WHERE o.lsState = :lsState AND o.ignored IS NOT :ignored  AND o.lsKind = :lsKind  AND o.lsType = :lsType  AND o.stringValue = :stringValue");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ExperimentValue> q = em.createQuery(queryBuilder.toString(), ExperimentValue.class);
        q.setParameter("lsState", lsState);
        q.setParameter("ignored", ignored);
        q.setParameter("lsKind", lsKind);
        q.setParameter("lsType", lsType);
        q.setParameter("stringValue", stringValue);
        return q;
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static String toJsonArray(Collection<ExperimentValue> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<ExperimentValue> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static TypedQuery<ExperimentValue> findExperimentValuesByLsKindEqualsAndCodeValueEquals(
            String valueKind, String codeValue) {

        if (valueKind == null || valueKind.length() == 0)
            throw new IllegalArgumentException("The valueKind argument is required");
        if (codeValue == null || codeValue.length() == 0)
            throw new IllegalArgumentException("The valueKind argument is required");

        EntityManager em = entityManager();
        String hsqlQuery = "SELECT ev FROM ExperimentValue AS ev " +
                "JOIN ev.lsState es " +
                "JOIN es.experiment e " +
                "JOIN e.protocol p " +
                "WHERE ev.codeValue = :codeValue AND ev.lsType = :valueType AND ev.lsKind = :valueKind AND ev.ignored IS NOT :ignored " +
                "AND es.ignored IS NOT :ignored " +
                "AND p.ignored IS NOT :ignored " +
                "AND e.ignored IS NOT :ignored ";
        TypedQuery<ExperimentValue> q = em.createQuery(hsqlQuery, ExperimentValue.class);
        q.setParameter("valueType", "codeValue");
        q.setParameter("valueKind", valueKind);
        q.setParameter("codeValue", codeValue);
        q.setParameter("ignored", true);
        return q;
    }
}
