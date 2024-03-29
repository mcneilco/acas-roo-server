package com.labsynch.labseer.domain;

import java.io.Reader;
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

import com.labsynch.labseer.dto.FlatThingCsvDTO;
import com.labsynch.labseer.dto.SubjectValueDTO;
import com.labsynch.labseer.utils.ExcludeNulls;

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
public class SubjectValue extends AbstractValue {

	private static final Logger logger = LoggerFactory.getLogger(SubjectValue.class);

	@NotNull
	@ManyToOne
	@JoinColumn(name = "subject_state_id")
	private SubjectState lsState;

	public SubjectValue() {
	}

	public SubjectValue(FlatThingCsvDTO subjectDTO) {
		this.setCodeValue(subjectDTO.getCodeValue());
		this.setCodeOrigin(subjectDTO.getCodeOrigin());
		this.setCodeType(subjectDTO.getCodeType());
		this.setCodeKind(subjectDTO.getCodeKind());
		this.setLsType(subjectDTO.getValueType());
		this.setLsKind(subjectDTO.getValueKind());
		this.setStringValue(subjectDTO.getStringValue());
		this.setClobValue(subjectDTO.getClobValue());
		this.setFileValue(subjectDTO.getFileValue());
		this.setUrlValue(subjectDTO.getUrlValue());
		this.setDateValue(subjectDTO.getDateValue());
		this.setOperatorKind(subjectDTO.getOperatorKind());
		this.setNumericValue(subjectDTO.getNumericValue());
		this.setFileValue(subjectDTO.getFileValue());
		this.setUncertainty(subjectDTO.getUncertainty());
		this.setUncertaintyType(subjectDTO.getUncertaintyType());
		this.setUnitKind(subjectDTO.getUnitKind());
		this.setConcentration(subjectDTO.getConcentration());
		this.setConcUnit(subjectDTO.getConcUnit());
		this.setNumberOfReplicates(subjectDTO.getNumberOfReplicates());
		this.setRecordedBy(subjectDTO.getRecordedBy());
		this.setRecordedDate(subjectDTO.getRecordedDate());
		this.setLsTransaction(subjectDTO.getLsTransaction());
		this.setModifiedBy(subjectDTO.getModifiedBy());
		this.setModifiedDate(subjectDTO.getModifiedDate());
		this.setComments(subjectDTO.getComments());
		this.setIgnored(subjectDTO.isIgnored());
		this.setPublicData(subjectDTO.isPublicData());
		this.setSigFigs(subjectDTO.getSigFigs());
	}

	public SubjectValue(SubjectValue subjectValue) {
		super.setBlobValue(subjectValue.getBlobValue());
		super.setClobValue(subjectValue.getClobValue());
		super.setCodeKind(subjectValue.getCodeKind());
		super.setCodeOrigin(subjectValue.getCodeOrigin());
		super.setCodeType(subjectValue.getCodeType());
		super.setCodeTypeAndKind(subjectValue.getCodeTypeAndKind());
		super.setCodeValue(subjectValue.getCodeValue());
		super.setComments(subjectValue.getComments());
		super.setConcentration(subjectValue.getConcentration());
		super.setConcUnit(subjectValue.getConcUnit());
		super.setDateValue(subjectValue.getDateValue());
		super.setDeleted(subjectValue.isDeleted());
		super.setFileValue(subjectValue.getFileValue());
		super.setIgnored(subjectValue.isIgnored());
		super.setLsKind(subjectValue.getLsKind());
		super.setLsTransaction(subjectValue.getLsTransaction());
		super.setLsType(subjectValue.getLsType());
		super.setLsTypeAndKind(subjectValue.getLsTypeAndKind());
		super.setModifiedBy(subjectValue.getModifiedBy());
		super.setModifiedDate(subjectValue.getModifiedDate());
		super.setNumberOfReplicates(subjectValue.getNumberOfReplicates());
		super.setNumericValue(subjectValue.getNumericValue());
		super.setOperatorKind(subjectValue.getOperatorKind());
		super.setOperatorType(subjectValue.getOperatorType());
		super.setOperatorTypeAndKind(subjectValue.getOperatorTypeAndKind());
		super.setPublicData(subjectValue.isPublicData());
		super.setRecordedBy(subjectValue.getRecordedBy());
		super.setRecordedDate(subjectValue.getRecordedDate());
		super.setSigFigs(subjectValue.getSigFigs());
		super.setStringValue(subjectValue.getStringValue());
		super.setUncertainty(subjectValue.getUncertainty());
		super.setUncertaintyType(subjectValue.getUncertaintyType());
		super.setUnitKind(subjectValue.getUnitKind());
		super.setUnitType(subjectValue.getUnitType());
		super.setUnitTypeAndKind(subjectValue.getUnitTypeAndKind());
		super.setUrlValue(subjectValue.getUrlValue());
	}

	public Long getStateId() {
		return this.lsState.getId();
	}

	public String getStateType() {
		return this.lsState.getLsType();
	}

	public String getStateKind() {
		return this.lsState.getLsKind();
	}

	// TODO: work out a different strategy with the many to many
	// public Long getTreatmentGroupId() {
	// return 0L;
	// }
	//
	public Long getSubjectId() {
		return this.lsState.getSubject().getId();
	}

	public static com.labsynch.labseer.domain.SubjectValue create(
			com.labsynch.labseer.domain.SubjectValue subjectValue) {
		SubjectValue newsubjectValue = new JSONDeserializer<SubjectValue>().use(null, SubjectValue.class)
				.deserializeInto(subjectValue.toJson(), new SubjectValue());
		return newsubjectValue;
	}

	@Transactional
	public String toJson() {
		return new JSONSerializer()
				.include("lsState.subject.treatmentGroups")
				.exclude("*.class", "lsState.subject.treatmentGroups.analysisGroups")
				.transform(new ExcludeNulls(), void.class).serialize(this);
	}

	@Transactional
	public String toJsonStub() {
		return new JSONSerializer().include().exclude("*.class", "lsState").transform(new ExcludeNulls(), void.class)
				.serialize(this);
	}

	public static com.labsynch.labseer.domain.SubjectValue fromJsonToSubjectValue(String json) {
		return new JSONDeserializer<SubjectValue>().use(null, SubjectValue.class).deserialize(json);
	}

	@Transactional
	public static String toJsonArray(Collection<com.labsynch.labseer.domain.SubjectValue> collection) {
		return new JSONSerializer()
				.include("lsState.subject.treatmentGroups")
				.exclude("*.class", "lsState.subject.treatmentGroups.analysisGroups")
				.transform(new ExcludeNulls(), void.class).serialize(collection);
	}

	@Transactional
	public static String toJsonArrayStub(Collection<com.labsynch.labseer.domain.SubjectValue> collection) {
		return new JSONSerializer().exclude("*.class", "lsState").transform(new ExcludeNulls(), void.class)
				.serialize(collection);
	}

	public static Collection<com.labsynch.labseer.domain.SubjectValue> fromJsonArrayToSubjectValues(Reader json) {
		return new JSONDeserializer<List<SubjectValue>>().use(null, ArrayList.class).use("lsState", SubjectState.class)
				.use("values", SubjectValue.class).deserialize(json);
	}

	public static Collection<com.labsynch.labseer.domain.SubjectValue> fromJsonArrayToSubjectValues(String json) {
		return new JSONDeserializer<List<SubjectValue>>().use(null, ArrayList.class).use("values", SubjectValue.class)
				.deserialize(json);
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
		if (id == null)
			return null;
		return entityManager().find(SubjectValue.class, id);
	}

	public static List<com.labsynch.labseer.domain.SubjectValue> findSubjectValueEntries(int firstResult,
			int maxResults) {
		return entityManager().createQuery("SELECT o FROM SubjectValue o", SubjectValue.class)
				.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

	@Transactional
	public com.labsynch.labseer.domain.SubjectValue merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
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
			if (query != null)
				subjectValues.add(query);
		}
		return SubjectValue.toJsonArray(subjectValues);
	}

	@Transactional
	public static int deleteByExperimentID(Long experimentId) {
		if (experimentId == null)
			return 0;
		EntityManager em = SubjectValue.entityManager();
		String deleteSQL = "DELETE FROM SubjectValue oo WHERE id in (select o.id from SubjectValue o where o.lsState.subject.treatmentGroups.analysisGroups.experiments.id = :experimentId)";
		Query q = em.createQuery(deleteSQL);
		q.setParameter("experimentId", experimentId);
		int numberOfDeletedEntities = q.executeUpdate();
		return numberOfDeletedEntities;
	}

	public static com.labsynch.labseer.domain.SubjectValue update(
			com.labsynch.labseer.domain.SubjectValue subjectValue) {
		SubjectValue updatedSubjectValue = new JSONDeserializer<SubjectValue>().use(null, SubjectValue.class)
				.deserializeInto(subjectValue.toJson(), SubjectValue.findSubjectValue(subjectValue.getId()));
		updatedSubjectValue.setModifiedDate(new Date());
		updatedSubjectValue.merge();
		return updatedSubjectValue;
	}

	public static TypedQuery<SubjectValue> findSubjectValuesByExptIDAndStateTypeKind(Long experimentId,
			String stateType, String stateKind) {
		if (stateType == null || stateType.length() == 0)
			throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0)
			throw new IllegalArgumentException("The stateKind argument is required");

		EntityManager em = entityManager();
		String hsqlQuery = "SELECT sv FROM SubjectValue AS sv " +
				"JOIN sv.lsState svs " +
				"JOIN svs.subject s " +
				"JOIN s.treatmentGroups tg " +
				"JOIN tg.analysisGroups ag " +
				"JOIN ag.experiments exp " +
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

	public static TypedQuery<SubjectValue> findSubjectValuesByExptIDAndStateTypeKindAndValueTypeKind(Long experimentId,
			String stateType,
			String stateKind, String valueType, String valueKind) {
		if (stateType == null || stateType.length() == 0)
			throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0)
			throw new IllegalArgumentException("The stateKind argument is required");
		if (valueType == null || valueType.length() == 0)
			throw new IllegalArgumentException("The valueType argument is required");
		if (valueKind == null || valueKind.length() == 0)
			throw new IllegalArgumentException("The valueKind argument is required");

		EntityManager em = entityManager();
		String hsqlQuery = "SELECT sv FROM SubjectValue AS sv " +
				"JOIN sv.lsState svs " +
				"JOIN svs.subject s " +
				"JOIN s.treatmentGroups tg " +
				"JOIN tg.analysisGroups ag " +
				"JOIN ag.experiments exp " +
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

	public static TypedQuery<SubjectValue> findSubjectValuesByAnalysisGroupIDAndStateTypeKind(Long analysisGroupId,
			String stateType, String stateKind) {
		if (stateType == null || stateType.length() == 0)
			throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0)
			throw new IllegalArgumentException("The stateKind argument is required");

		EntityManager em = entityManager();
		String hsqlQuery = "SELECT sv FROM SubjectValue AS sv " +
				"JOIN sv.lsState svs " +
				"JOIN svs.subject s " +
				"JOIN s.treatmentGroups tg " +
				"JOIN tg.analysisGroups ag " +
				"WHERE svs.lsType = :stateType AND svs.lsKind = :stateKind AND svs.ignored IS NOT :ignored " +
				"AND sv.ignored IS NOT :ignored " +
				"AND s.ignored IS NOT :ignored " +
				"AND tg.ignored IS NOT :ignored " +
				"AND ag.ignored IS NOT :ignored " +
				"AND ag.id = :analysisGroupId ";
		TypedQuery<SubjectValue> q = em.createQuery(hsqlQuery, SubjectValue.class);
		q.setParameter("analysisGroupId", analysisGroupId);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("ignored", true);
		return q;
	}

	public static String[] getColumns() {
		String[] headerColumns = new String[] {
				"treatmentGroupId",
				"subjectId",
				"stateId",
				"stateType",
				"stateKind",
				"id",
				"lsType",
				"lsKind",
				"codeOrigin",
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
				"concentration",
				"concUnit",
				"comments",
				"ignored",
				"lsTransaction",

				"recordedDate",
				"recordedBy",
				"modifiedDate",
				"modifiedBy",
				"publicData"
		};
		// 35 columns
		return headerColumns;

	}

	public static CellProcessor[] getProcessors() {
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

	public Collection<SubjectValueDTO> makeDTOsByTreatmentGroupIds() {
		Collection<SubjectValueDTO> subjectValueDTOs = new HashSet<SubjectValueDTO>();
		Collection<TreatmentGroup> treatmentGroups = Subject.findSubject(this.getSubjectId()).getTreatmentGroups();
		for (TreatmentGroup treatmentGroup : treatmentGroups) {
			SubjectValueDTO subjectValueDTO = new SubjectValueDTO(this);
			subjectValueDTO.setTreatmentGroupId(treatmentGroup.getId());
			subjectValueDTOs.add(subjectValueDTO);
		}

		return subjectValueDTOs;
	}

	public static TypedQuery<SubjectValue> findSubjectValuesBySubjectIDAndStateTypeKindAndValueTypeKind(
			Long subjectId, String stateType, String stateKind,
			String valueType, String valueKind) {

		if (stateType == null || stateType.length() == 0)
			throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0)
			throw new IllegalArgumentException("The stateKind argument is required");
		if (valueType == null || valueType.length() == 0)
			throw new IllegalArgumentException("The valueType argument is required");
		if (valueKind == null || valueKind.length() == 0)
			throw new IllegalArgumentException("The valueKind argument is required");

		EntityManager em = entityManager();
		String hsqlQuery = "SELECT sv FROM SubjectValue AS sv " +
				"JOIN sv.lsState ss " +
				"JOIN ss.subject s " +
				"WHERE ss.lsType = :stateType AND ss.lsKind = :stateKind AND ss.ignored IS NOT :ignored " +
				"AND sv.lsType = :valueType AND sv.lsKind = :valueKind AND sv.ignored IS NOT :ignored " +
				"AND s.ignored IS NOT :ignored " +
				"AND s.id = :subjectId ";
		TypedQuery<SubjectValue> q = em.createQuery(hsqlQuery, SubjectValue.class);
		q.setParameter("subjectId", subjectId);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("valueType", valueType);
		q.setParameter("valueKind", valueKind);
		q.setParameter("ignored", true);
		return q;
	}

	public static TypedQuery<SubjectValue> findSubjectValuesBySubjectCodeNameAndStateTypeKindAndValueTypeKind(
			String subjectCodeName, String stateType, String stateKind,
			String valueType, String valueKind) {

		if (stateType == null || stateType.length() == 0)
			throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0)
			throw new IllegalArgumentException("The stateKind argument is required");
		if (valueType == null || valueType.length() == 0)
			throw new IllegalArgumentException("The valueType argument is required");
		if (valueKind == null || valueKind.length() == 0)
			throw new IllegalArgumentException("The valueKind argument is required");

		EntityManager em = entityManager();
		String hsqlQuery = "SELECT sv FROM SubjectValue AS sv " +
				"JOIN sv.lsState ss " +
				"JOIN ss.subject s " +
				"WHERE ss.lsType = :stateType AND ss.lsKind = :stateKind AND ss.ignored IS NOT :ignored " +
				"AND sv.lsType = :valueType AND sv.lsKind = :valueKind AND sv.ignored IS NOT :ignored " +
				"AND s.ignored IS NOT :ignored " +
				"AND s.codeName = :subjectCodeName ";
		TypedQuery<SubjectValue> q = em.createQuery(hsqlQuery, SubjectValue.class);
		q.setParameter("subjectCodeName", subjectCodeName);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("valueType", valueType);
		q.setParameter("valueKind", valueKind);
		q.setParameter("ignored", true);
		return q;
	}

	public SubjectState getLsState() {
		return this.lsState;
	}

	public void setLsState(SubjectState lsState) {
		this.lsState = lsState;
	}

	public static Long countFindSubjectValuesByCodeValueEquals(String codeValue) {
		if (codeValue == null || codeValue.length() == 0)
			throw new IllegalArgumentException("The codeValue argument is required");
		EntityManager em = SubjectValue.entityManager();
		TypedQuery q = em.createQuery("SELECT COUNT(o) FROM SubjectValue AS o WHERE o.codeValue = :codeValue",
				Long.class);
		q.setParameter("codeValue", codeValue);
		return ((Long) q.getSingleResult());
	}

	public static Long countFindSubjectValuesByIgnoredNotAndCodeValueEquals(boolean ignored, String codeValue) {
		if (codeValue == null || codeValue.length() == 0)
			throw new IllegalArgumentException("The codeValue argument is required");
		EntityManager em = SubjectValue.entityManager();
		TypedQuery q = em.createQuery(
				"SELECT COUNT(o) FROM SubjectValue AS o WHERE o.ignored IS NOT :ignored  AND o.codeValue = :codeValue",
				Long.class);
		q.setParameter("ignored", ignored);
		q.setParameter("codeValue", codeValue);
		return ((Long) q.getSingleResult());
	}

	public static Long countFindSubjectValuesByLsState(SubjectState lsState) {
		if (lsState == null)
			throw new IllegalArgumentException("The lsState argument is required");
		EntityManager em = SubjectValue.entityManager();
		TypedQuery q = em.createQuery("SELECT COUNT(o) FROM SubjectValue AS o WHERE o.lsState = :lsState", Long.class);
		q.setParameter("lsState", lsState);
		return ((Long) q.getSingleResult());
	}

	public static Long countFindSubjectValuesByLsStateAndLsTypeEqualsAndLsKindEquals(SubjectState lsState,
			String lsType, String lsKind) {
		if (lsState == null)
			throw new IllegalArgumentException("The lsState argument is required");
		if (lsType == null || lsType.length() == 0)
			throw new IllegalArgumentException("The lsType argument is required");
		if (lsKind == null || lsKind.length() == 0)
			throw new IllegalArgumentException("The lsKind argument is required");
		EntityManager em = SubjectValue.entityManager();
		TypedQuery q = em.createQuery(
				"SELECT COUNT(o) FROM SubjectValue AS o WHERE o.lsState = :lsState AND o.lsType = :lsType  AND o.lsKind = :lsKind",
				Long.class);
		q.setParameter("lsState", lsState);
		q.setParameter("lsType", lsType);
		q.setParameter("lsKind", lsKind);
		return ((Long) q.getSingleResult());
	}

	public static Long countFindSubjectValuesByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind) {
		if (lsType == null || lsType.length() == 0)
			throw new IllegalArgumentException("The lsType argument is required");
		if (lsKind == null || lsKind.length() == 0)
			throw new IllegalArgumentException("The lsKind argument is required");
		EntityManager em = SubjectValue.entityManager();
		TypedQuery q = em.createQuery(
				"SELECT COUNT(o) FROM SubjectValue AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind", Long.class);
		q.setParameter("lsType", lsType);
		q.setParameter("lsKind", lsKind);
		return ((Long) q.getSingleResult());
	}

	public static TypedQuery<SubjectValue> findSubjectValuesByCodeValueEquals(String codeValue) {
		if (codeValue == null || codeValue.length() == 0)
			throw new IllegalArgumentException("The codeValue argument is required");
		EntityManager em = SubjectValue.entityManager();
		TypedQuery<SubjectValue> q = em.createQuery("SELECT o FROM SubjectValue AS o WHERE o.codeValue = :codeValue",
				SubjectValue.class);
		q.setParameter("codeValue", codeValue);
		return q;
	}

	public static TypedQuery<SubjectValue> findSubjectValuesByCodeValueEquals(String codeValue, String sortFieldName,
			String sortOrder) {
		if (codeValue == null || codeValue.length() == 0)
			throw new IllegalArgumentException("The codeValue argument is required");
		EntityManager em = SubjectValue.entityManager();
		StringBuilder queryBuilder = new StringBuilder(
				"SELECT o FROM SubjectValue AS o WHERE o.codeValue = :codeValue");
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			queryBuilder.append(" ORDER BY ").append(sortFieldName);
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				queryBuilder.append(" ").append(sortOrder);
			}
		}
		TypedQuery<SubjectValue> q = em.createQuery(queryBuilder.toString(), SubjectValue.class);
		q.setParameter("codeValue", codeValue);
		return q;
	}

	public static TypedQuery<SubjectValue> findSubjectValuesByIgnoredNotAndCodeValueEquals(boolean ignored,
			String codeValue) {
		if (codeValue == null || codeValue.length() == 0)
			throw new IllegalArgumentException("The codeValue argument is required");
		EntityManager em = SubjectValue.entityManager();
		TypedQuery<SubjectValue> q = em.createQuery(
				"SELECT o FROM SubjectValue AS o WHERE o.ignored IS NOT :ignored  AND o.codeValue = :codeValue",
				SubjectValue.class);
		q.setParameter("ignored", ignored);
		q.setParameter("codeValue", codeValue);
		return q;
	}

	public static TypedQuery<SubjectValue> findSubjectValuesByIgnoredNotAndCodeValueEquals(boolean ignored,
			String codeValue, String sortFieldName, String sortOrder) {
		if (codeValue == null || codeValue.length() == 0)
			throw new IllegalArgumentException("The codeValue argument is required");
		EntityManager em = SubjectValue.entityManager();
		StringBuilder queryBuilder = new StringBuilder(
				"SELECT o FROM SubjectValue AS o WHERE o.ignored IS NOT :ignored  AND o.codeValue = :codeValue");
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			queryBuilder.append(" ORDER BY ").append(sortFieldName);
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				queryBuilder.append(" ").append(sortOrder);
			}
		}
		TypedQuery<SubjectValue> q = em.createQuery(queryBuilder.toString(), SubjectValue.class);
		q.setParameter("ignored", ignored);
		q.setParameter("codeValue", codeValue);
		return q;
	}

	public static TypedQuery<SubjectValue> findSubjectValuesByLsState(SubjectState lsState) {
		if (lsState == null)
			throw new IllegalArgumentException("The lsState argument is required");
		EntityManager em = SubjectValue.entityManager();
		TypedQuery<SubjectValue> q = em.createQuery("SELECT o FROM SubjectValue AS o WHERE o.lsState = :lsState",
				SubjectValue.class);
		q.setParameter("lsState", lsState);
		return q;
	}

	public static TypedQuery<SubjectValue> findSubjectValuesByLsState(SubjectState lsState, String sortFieldName,
			String sortOrder) {
		if (lsState == null)
			throw new IllegalArgumentException("The lsState argument is required");
		EntityManager em = SubjectValue.entityManager();
		StringBuilder queryBuilder = new StringBuilder("SELECT o FROM SubjectValue AS o WHERE o.lsState = :lsState");
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			queryBuilder.append(" ORDER BY ").append(sortFieldName);
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				queryBuilder.append(" ").append(sortOrder);
			}
		}
		TypedQuery<SubjectValue> q = em.createQuery(queryBuilder.toString(), SubjectValue.class);
		q.setParameter("lsState", lsState);
		return q;
	}

	public static TypedQuery<SubjectValue> findSubjectValuesByLsStateAndLsTypeEqualsAndLsKindEquals(
			SubjectState lsState, String lsType, String lsKind) {
		if (lsState == null)
			throw new IllegalArgumentException("The lsState argument is required");
		if (lsType == null || lsType.length() == 0)
			throw new IllegalArgumentException("The lsType argument is required");
		if (lsKind == null || lsKind.length() == 0)
			throw new IllegalArgumentException("The lsKind argument is required");
		EntityManager em = SubjectValue.entityManager();
		TypedQuery<SubjectValue> q = em.createQuery(
				"SELECT o FROM SubjectValue AS o WHERE o.lsState = :lsState AND o.lsType = :lsType  AND o.lsKind = :lsKind",
				SubjectValue.class);
		q.setParameter("lsState", lsState);
		q.setParameter("lsType", lsType);
		q.setParameter("lsKind", lsKind);
		return q;
	}

	public static TypedQuery<SubjectValue> findSubjectValuesByLsStateAndLsTypeEqualsAndLsKindEquals(
			SubjectState lsState, String lsType, String lsKind, String sortFieldName, String sortOrder) {
		if (lsState == null)
			throw new IllegalArgumentException("The lsState argument is required");
		if (lsType == null || lsType.length() == 0)
			throw new IllegalArgumentException("The lsType argument is required");
		if (lsKind == null || lsKind.length() == 0)
			throw new IllegalArgumentException("The lsKind argument is required");
		EntityManager em = SubjectValue.entityManager();
		StringBuilder queryBuilder = new StringBuilder(
				"SELECT o FROM SubjectValue AS o WHERE o.lsState = :lsState AND o.lsType = :lsType  AND o.lsKind = :lsKind");
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			queryBuilder.append(" ORDER BY ").append(sortFieldName);
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				queryBuilder.append(" ").append(sortOrder);
			}
		}
		TypedQuery<SubjectValue> q = em.createQuery(queryBuilder.toString(), SubjectValue.class);
		q.setParameter("lsState", lsState);
		q.setParameter("lsType", lsType);
		q.setParameter("lsKind", lsKind);
		return q;
	}

	public static TypedQuery<SubjectValue> findSubjectValuesByLsTypeEqualsAndLsKindEquals(String lsType,
			String lsKind) {
		if (lsType == null || lsType.length() == 0)
			throw new IllegalArgumentException("The lsType argument is required");
		if (lsKind == null || lsKind.length() == 0)
			throw new IllegalArgumentException("The lsKind argument is required");
		EntityManager em = SubjectValue.entityManager();
		TypedQuery<SubjectValue> q = em.createQuery(
				"SELECT o FROM SubjectValue AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind", SubjectValue.class);
		q.setParameter("lsType", lsType);
		q.setParameter("lsKind", lsKind);
		return q;
	}

	public static TypedQuery<SubjectValue> findSubjectValuesByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind,
			String sortFieldName, String sortOrder) {
		if (lsType == null || lsType.length() == 0)
			throw new IllegalArgumentException("The lsType argument is required");
		if (lsKind == null || lsKind.length() == 0)
			throw new IllegalArgumentException("The lsKind argument is required");
		EntityManager em = SubjectValue.entityManager();
		StringBuilder queryBuilder = new StringBuilder(
				"SELECT o FROM SubjectValue AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind");
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			queryBuilder.append(" ORDER BY ").append(sortFieldName);
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				queryBuilder.append(" ").append(sortOrder);
			}
		}
		TypedQuery<SubjectValue> q = em.createQuery(queryBuilder.toString(), SubjectValue.class);
		q.setParameter("lsType", lsType);
		q.setParameter("lsKind", lsKind);
		return q;
	}

	public String toString() {
		return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("lsState")
				.toString();
	}

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "lsState");

	public static List<SubjectValue> findAllSubjectValues(String sortFieldName, String sortOrder) {
		String jpaQuery = "SELECT o FROM SubjectValue o";
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				jpaQuery = jpaQuery + " " + sortOrder;
			}
		}
		return entityManager().createQuery(jpaQuery, SubjectValue.class).getResultList();
	}

	public static List<SubjectValue> findSubjectValueEntries(int firstResult, int maxResults, String sortFieldName,
			String sortOrder) {
		String jpaQuery = "SELECT o FROM SubjectValue o";
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				jpaQuery = jpaQuery + " " + sortOrder;
			}
		}
		return entityManager().createQuery(jpaQuery, SubjectValue.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}
	public static TypedQuery<SubjectValue> findSubjectValuesByLsKindEqualsAndCodeValueEquals(
		String valueKind, String codeValue) {

	if (valueKind == null || valueKind.length() == 0)
		throw new IllegalArgumentException("The valueKind argument is required");
	if (codeValue == null || codeValue.length() == 0)
		throw new IllegalArgumentException("The valueKind argument is required");

	// Join through protocol to only fetch where top level protocol is not ignored
	EntityManager em = entityManager();
	String hsqlQuery = "SELECT sv FROM SubjectValue AS sv " +
			"JOIN sv.lsState ss " +
			"JOIN ss.subject s " +
			"JOIN s.treatmentGroups tg " +
			"JOIN tg.analysisGroups ag " +
			"JOIN ag.experiments e " +
			"JOIN e.protocol p " +
			"WHERE sv.codeValue = :codeValue AND sv.lsType = :valueType AND sv.lsKind = :valueKind AND sv.ignored IS NOT :ignored " +
			"AND ss.ignored IS NOT :ignored " +
			"AND s.ignored IS NOT :ignored " +
			"AND tg.ignored IS NOT :ignored " +
			"AND ag.ignored IS NOT :ignored " +
			"AND e.ignored IS NOT :ignored " +
			"AND p.ignored IS NOT :ignored ";

	TypedQuery<SubjectValue> q = em.createQuery(hsqlQuery, SubjectValue.class);
	q.setParameter("valueType", "codeValue");
	q.setParameter("valueKind", valueKind);
	q.setParameter("codeValue", codeValue);
	q.setParameter("ignored", true);
	return q;
}
}
