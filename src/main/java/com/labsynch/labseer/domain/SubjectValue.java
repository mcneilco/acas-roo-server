package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@RooJavaBean
@RooToString(excludeFields = { "lsState" })
@RooJson
@RooJpaActiveRecord(finders = { "findSubjectValuesByLsState", "findSubjectValuesByCodeValueEquals", "findSubjectValuesByIgnoredNotAndCodeValueEquals", "findSubjectValuesByLsTypeEqualsAndLsKindEquals", "findSubjectValuesByLsStateAndLsTypeEqualsAndLsKindEquals" })
public class SubjectValue extends AbstractValue {

	private static final Logger logger = LoggerFactory.getLogger(SubjectValue.class);

	@NotNull
	@ManyToOne
	@JoinColumn(name = "subject_state_id")
	private SubjectState lsState;

	public SubjectValue() {
	}

	public static com.labsynch.labseer.domain.SubjectValue create(com.labsynch.labseer.domain.SubjectValue subjectValue) {
		SubjectValue newsubjectValue = new JSONDeserializer<SubjectValue>().use(null, SubjectValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(subjectValue.toJson(), new SubjectValue());
		return newsubjectValue;
	}

	@Transactional
	public String toJson() {
		return new JSONSerializer().include().exclude("*.class", "lsState.subject").transform(new ExcludeNulls(), void.class).serialize(this);
	}

	@Transactional
	public String toJsonStub() {
		return new JSONSerializer().include().exclude("*.class", "lsState").transform(new ExcludeNulls(), void.class).serialize(this);
	}

	public static com.labsynch.labseer.domain.SubjectValue fromJsonToSubjectValue(String json) {
		return new JSONDeserializer<SubjectValue>().use(null, SubjectValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
	}

	@Transactional
	public static String toJsonArray(Collection<com.labsynch.labseer.domain.SubjectValue> collection) {
		return new JSONSerializer().exclude("*.class", "lsState.subject").transform(new ExcludeNulls(), void.class).serialize(collection);
	}

	@Transactional
	public static String toJsonArrayStub(Collection<com.labsynch.labseer.domain.SubjectValue> collection) {
		return new JSONSerializer().exclude("*.class", "lsState").transform(new ExcludeNulls(), void.class).serialize(collection);
	}

	public static Collection<com.labsynch.labseer.domain.SubjectValue> fromJsonArrayToSubjectValues(Reader json) {
		return new JSONDeserializer<List<SubjectValue>>().use(null, ArrayList.class).use("lsState", SubjectState.class).use("values", SubjectValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
	}

	public static Collection<com.labsynch.labseer.domain.SubjectValue> fromJsonArrayToSubjectValues(String json) {
		return new JSONDeserializer<List<SubjectValue>>().use(null, ArrayList.class).use("values", SubjectValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
	}

	public static long countSubjectValues() {
		return entityManager().createQuery("SELECT COUNT(o) FROM SubjectValue o", Long.class).getSingleResult();
	}

	@Transactional
	public static List<com.labsynch.labseer.domain.SubjectValue> findAllSubjectValues() {
		return entityManager().createQuery("SELECT o FROM SubjectValue o", SubjectValue.class).getResultList();
	}

	@Transactional
	public static com.labsynch.labseer.domain.SubjectValue findSubjectValue(Long id) {
		if (id == null) return null;
		return entityManager().find(SubjectValue.class, id);
	}

	public static List<com.labsynch.labseer.domain.SubjectValue> findSubjectValueEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM SubjectValue o", SubjectValue.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

	@Transactional
	public com.labsynch.labseer.domain.SubjectValue merge() {
		if (this.entityManager == null) this.entityManager = entityManager();
		SubjectValue merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	@Transactional
	public static void delete(com.labsynch.labseer.domain.SubjectValue subjectValue) {
		EntityManager em = SubjectValue.entityManager();
		if (em.contains(subjectValue)) {
			em.remove(subjectValue);
		} else {
			SubjectValue attached = SubjectValue.findSubjectValue(subjectValue.getId());
			attached.remove();
		}
	}

	@Transactional
	public static List<java.lang.Long> saveList(List<com.labsynch.labseer.domain.SubjectValue> entities) {
		logger.info("saving the list of subject values: " + entities.size());
		long startTime = new Date().getTime();
		int batchSize = 50;
		List<Long> idList = new ArrayList<Long>();
		int imported = 0;
		for (SubjectValue e : entities) {
			e.persist();
			idList.add(e.getId());
			if (++imported % batchSize == 0) {
				e.flush();
				e.clear();
			}
		}
		long endTime = new Date().getTime();
		long elapsedTime = endTime - startTime;
		logger.debug("saving the list of subject values - elapsed time: " + elapsedTime);
		return idList;
	}

	@Transactional
	public static String getSubjectValueCollectionJson(List<java.lang.Long> idList) {
		Collection<SubjectValue> subjectValues = new HashSet<SubjectValue>();
		for (Long id : idList) {
			SubjectValue query = SubjectValue.findSubjectValue(id);
			if (query != null) subjectValues.add(query);
		}
		return SubjectValue.toJsonArray(subjectValues);
	}

	@Transactional
	public static int deleteByExperimentID(Long experimentId) {
		if (experimentId == null) return 0;
		EntityManager em = SubjectValue.entityManager();
		String deleteSQL = "DELETE FROM SubjectValue oo WHERE id in (select o.id from SubjectValue o where o.lsState.subject.treatmentGroup.analysisGroup.experiment.id = :experimentId)";
		Query q = em.createQuery(deleteSQL);
		q.setParameter("experimentId", experimentId);
		int numberOfDeletedEntities = q.executeUpdate();
		return numberOfDeletedEntities;
	}

	public static com.labsynch.labseer.domain.SubjectValue update(com.labsynch.labseer.domain.SubjectValue subjectValue) {
		SubjectValue updatedSubjectValue = new JSONDeserializer<SubjectValue>().use(null, SubjectValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(subjectValue.toJson(), SubjectValue.findSubjectValue(subjectValue.getId()));
		updatedSubjectValue.setModifiedDate(new Date());
		updatedSubjectValue.merge();
		return updatedSubjectValue;
	}

	public static TypedQuery<SubjectValue> findSubjectValuesByExptIDAndStateTypeKind(Long experimentId, String stateType, String stateKind) {
		if (stateType == null || stateType.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");

		EntityManager em = entityManager();
		String hsqlQuery = "SELECT sv FROM SubjectValue AS sv " +
				"JOIN sv.lsState svs " +
				"JOIN svs.subject s " + 
				"JOIN s.treatmentGroup tg " +
				"JOIN tg.analysisGroup ag " +
				"JOIN ag.experiment exp " +
				"WHERE svs.lsType = :stateType AND svs.lsKind = :stateKind AND svs.ignored IS NOT :ignored " +
				"AND sv.ignored IS NOT :ignored " +
				"AND s.ignored IS NOT :ignored " +
				"AND tg.ignored IS NOT :ignored " +
				"AND ag.ignored IS NOT :ignored " +
				"AND exp.id = :experimentId ";
		TypedQuery<SubjectValue> q = em.createQuery(hsqlQuery, SubjectValue.class);
		q.setParameter("experimentId", experimentId);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("ignored", true);
		return q;
	}

	public static TypedQuery<SubjectValue> findSubjectValuesByExptIDAndStateTypeKindAndValueTypeKind(Long experimentId, String stateType,
			String stateKind, String valueType, String valueKind) {
		if (stateType == null || stateType.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
		if (valueType == null || valueType.length() == 0) throw new IllegalArgumentException("The valueType argument is required");
		if (valueKind == null || valueKind.length() == 0) throw new IllegalArgumentException("The valueKind argument is required");

		EntityManager em = entityManager();
		String hsqlQuery = "SELECT sv FROM SubjectValue AS sv " +
				"JOIN sv.lsState svs " +
				"JOIN svs.subject s " + 
				"JOIN s.treatmentGroup tg " +
				"JOIN tg.analysisGroup ag " +
				"JOIN ag.experiment exp " +
				"WHERE svs.lsType = :stateType AND svs.lsKind = :stateKind AND svs.ignored IS NOT :ignored " +
				"AND sv.lsType = :valueType AND sv.lsKind = :valueKind AND sv.ignored IS NOT :ignored " +
				"AND s.ignored IS NOT :ignored " +
				"AND tg.ignored IS NOT :ignored " +
				"AND ag.ignored IS NOT :ignored " +
				"AND exp.id = :experimentId ";
		TypedQuery<SubjectValue> q = em.createQuery(hsqlQuery, SubjectValue.class);
		q.setParameter("experimentId", experimentId);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("valueType", valueType);
		q.setParameter("valueKind", valueKind);
		q.setParameter("ignored", true);
		return q;
	}
}
