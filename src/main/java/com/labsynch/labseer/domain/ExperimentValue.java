package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;

import flexjson.JSONDeserializer;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findExperimentValuesByLsState", "findExperimentValuesByLsStateAndIgnoredNotAndLsKindEqualsAndLsTypeEqualsAndStringValueEquals", "findExperimentValuesByLsKindEqualsAndCodeValueLike", "findExperimentValuesByLsKindEqualsAndStringValueLike" })
public class ExperimentValue extends AbstractValue {

    private static final Logger logger = LoggerFactory.getLogger(ExperimentValue.class);

    @NotNull
    @ManyToOne
    @JoinColumn(name = "experiment_state_id")
    private ExperimentState lsState;

    public ExperimentValue() {
    }

    public static com.labsynch.labseer.domain.ExperimentValue create(com.labsynch.labseer.domain.ExperimentValue experimentValue) {
        ExperimentValue newExperimentValue = new JSONDeserializer<ExperimentValue>().use(null, ExperimentValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(experimentValue.toJson(), new ExperimentValue());
        return newExperimentValue;
    }

    public static com.labsynch.labseer.domain.ExperimentValue create(String experimentValueJson) {
        ExperimentValue newExperimentValue = new JSONDeserializer<ExperimentValue>().use(null, ExperimentValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(experimentValueJson, new ExperimentValue());
        return newExperimentValue;
    }

    public static long countExperimentValues() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ExperimentValue o", Long.class).getSingleResult();
    }

    public static List<com.labsynch.labseer.domain.ExperimentValue> findAllExperimentValues() {
        return entityManager().createQuery("SELECT o FROM ExperimentValue o", ExperimentValue.class).getResultList();
    }

    public static com.labsynch.labseer.domain.ExperimentValue findExperimentValue(Long id) {
        if (id == null) return null;
        return entityManager().find(ExperimentValue.class, id);
    }

    public static List<com.labsynch.labseer.domain.ExperimentValue> findExperimentValueEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ExperimentValue o", ExperimentValue.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static com.labsynch.labseer.domain.ExperimentValue fromJsonToExperimentValue(String json) {
        return new JSONDeserializer<ExperimentValue>().use(null, ExperimentValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.ExperimentValue> fromJsonArrayToExperimentValues(String json) {
        return new JSONDeserializer<List<ExperimentValue>>().use(null, ArrayList.class).use("values", ExperimentValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.ExperimentValue> fromJsonArrayToExperimentValues(Reader json) {
        return new JSONDeserializer<List<ExperimentValue>>().use(null, ArrayList.class).use("values", ExperimentValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }

    @Transactional
    public com.labsynch.labseer.domain.ExperimentValue merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
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
            if (query != null) experimentValues.add(query);
        }
        return ExperimentValue.toJsonArray(experimentValues);
    }

    @Transactional
    public static int deleteByExperimentID(Long experimentId) {
        if (experimentId == null) return 0;
        EntityManager em = TreatmentGroupValue.entityManager();
        String deleteSQL = "DELETE FROM ExperimentValue oo WHERE id in (select o.id from ExperimentValue o where o.lsState.subject.treatmentGroup.analysisGroup.experiment.id = :experimentId)";
        Query q = em.createQuery(deleteSQL);
        q.setParameter("experimentId", experimentId);
        int numberOfDeletedEntities = q.executeUpdate();
        return numberOfDeletedEntities;
    }

    public static com.labsynch.labseer.domain.ExperimentValue update(com.labsynch.labseer.domain.ExperimentValue experimentValue) {
        ExperimentValue updatedExperimentValue = new JSONDeserializer<ExperimentValue>().use(null, ExperimentValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(experimentValue.toJson(), ExperimentValue.findExperimentValue(experimentValue.getId()));
        updatedExperimentValue.setModifiedDate(new Date());
        updatedExperimentValue.merge();
        return updatedExperimentValue;
    }

    public static TypedQuery<com.labsynch.labseer.domain.ExperimentValue> findExperimentValuesByExptIDAndStateTypeKindAndValueTypeKind(Long experimentId, String stateType, String stateKind, String valueType, String valueKind) {
        if (stateType == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
        if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
        if (valueType == null || valueType.length() == 0) throw new IllegalArgumentException("The valueType argument is required");
        if (valueKind == null || valueKind.length() == 0) throw new IllegalArgumentException("The valueKind argument is required");
        EntityManager em = entityManager();
        String hsqlQuery = "SELECT ev FROM ExperimentValue AS ev " + "JOIN ev.lsState evs " + "JOIN evs.experiment exp " + "WHERE evs.lsType = :stateType AND evs.lsKind = :stateKind AND evs.ignored IS NOT :ignored " + "AND ev.lsType = :valueType AND ev.lsKind = :valueKind AND ev.ignored IS NOT :ignored " + "AND exp.id = :experimentId ";
        TypedQuery<ExperimentValue> q = em.createQuery(hsqlQuery, ExperimentValue.class);
        q.setParameter("experimentId", experimentId);
        q.setParameter("stateType", stateType);
        q.setParameter("stateKind", stateKind);
        q.setParameter("valueType", valueType);
        q.setParameter("valueKind", valueKind);
        q.setParameter("ignored", true);
        return q;
    }
    
    public static TypedQuery<com.labsynch.labseer.domain.ExperimentValue> findExperimentValuesByExperimentCodeNameAndStateTypeKindAndValueTypeKind(String experimentCodeName, String stateType, String stateKind, String valueType, String valueKind) {
        if (stateType == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
        if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
        if (valueType == null || valueType.length() == 0) throw new IllegalArgumentException("The valueType argument is required");
        if (valueKind == null || valueKind.length() == 0) throw new IllegalArgumentException("The valueKind argument is required");
        EntityManager em = entityManager();
        String hsqlQuery = "SELECT ev FROM ExperimentValue AS ev " + "JOIN ev.lsState evs " + "JOIN evs.experiment exp " + "WHERE evs.lsType = :stateType AND evs.lsKind = :stateKind AND evs.ignored IS NOT :ignored " + "AND ev.lsType = :valueType AND ev.lsKind = :valueKind AND ev.ignored IS NOT :ignored " + "AND exp.codeName = :experimentCodeName ";
        TypedQuery<ExperimentValue> q = em.createQuery(hsqlQuery, ExperimentValue.class);
        q.setParameter("experimentCodeName", experimentCodeName);
        q.setParameter("stateType", stateType);
        q.setParameter("stateKind", stateKind);
        q.setParameter("valueType", valueType);
        q.setParameter("valueKind", valueKind);
        q.setParameter("ignored", true);
        return q;
    }

    public static TypedQuery<com.labsynch.labseer.domain.ExperimentValue> findExperimentValuesByExptIDAndStateTypeKind(Long experimentId, String stateType, String stateKind) {
        if (stateType == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
        if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
        EntityManager em = entityManager();
        String hsqlQuery = "SELECT ev FROM ExperimentValue AS ev " + "JOIN ev.lsState evs " + "JOIN evs.experiment exp " + "WHERE evs.lsType = :stateType AND evs.lsKind = :stateKind AND evs.ignored IS NOT :ignored " + "AND ev.ignored IS NOT :ignored " + "AND exp.id = :experimentId ";
        TypedQuery<ExperimentValue> q = em.createQuery(hsqlQuery, ExperimentValue.class);
        q.setParameter("experimentId", experimentId);
        q.setParameter("stateType", stateType);
        q.setParameter("stateKind", stateKind);
        q.setParameter("ignored", true);
        return q;
    }
    
    public static TypedQuery<ExperimentValue> findExperimentValuesByLsKindEqualsAndDateValueLike(String lsKind, Date dateValue) {
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (dateValue == null) throw new IllegalArgumentException("The dateValue argument is required");
        EntityManager em = ExperimentValue.entityManager();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateValue);
        cal.add(Calendar.DATE, -1);
        Date beforeDate = cal.getTime();
        cal.setTime(dateValue);
        cal.add(Calendar.DATE, 1);
        Date afterDate = cal.getTime();
        TypedQuery<ExperimentValue> q = em.createQuery("SELECT o FROM ExperimentValue AS o WHERE o.lsKind = :lsKind AND o.ignored =false AND o.dateValue > :beforeDate AND o.dateValue < :afterDate", ExperimentValue.class);
        q.setParameter("lsKind", lsKind);
        q.setParameter("beforeDate", beforeDate);
        q.setParameter("afterDate", afterDate);
        return q;
    }

    public static String[] getColumns() {
        String[] headerColumns = new String[] { "id", "codeName", "lsType", "lsKind", "labelText", "description", "comments", "ignored", "displayOrder" };
        return headerColumns;
    }

    public static CellProcessor[] getProcessors() {
        final CellProcessor[] processors = new CellProcessor[] { new Optional(), new Optional(), new Optional(), new Optional(), new Optional(), new Optional(), new Optional(), new Optional(), new Optional() };
        return processors;
    }
    
	public static String[] getAllColumns(){
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
//31 columns
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
	

}
