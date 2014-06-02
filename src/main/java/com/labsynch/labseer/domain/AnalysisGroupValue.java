package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.dto.AnalysisGroupValueBaseDTO;
import com.labsynch.labseer.dto.AnalysisGroupValueDTO;
import com.labsynch.labseer.dto.BatchCodeDTO;
import com.labsynch.labseer.dto.ExperimentFilterSearchDTO;
import com.labsynch.labseer.dto.ValueTypeKindDTO;
import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findAnalysisGroupValuesByLsState", "findAnalysisGroupValuesByLsTransactionEquals", "findAnalysisGroupValuesByCodeValueEquals", "findAnalysisGroupValuesByIgnoredNotAndCodeValueEquals" })
public class AnalysisGroupValue extends AbstractValue {

	private static final Logger logger = LoggerFactory.getLogger(AnalysisGroupValue.class);

	@NotNull
	@ManyToOne
	@JoinColumn(name = "analysis_state_id")
	private AnalysisGroupState lsState;

	public static com.labsynch.labseer.domain.AnalysisGroupValue create(com.labsynch.labseer.domain.AnalysisGroupValue analysisGroupValue) {
		AnalysisGroupValue newAnalysisGroupValue = new JSONDeserializer<AnalysisGroupValue>().use(null, AnalysisGroupValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(analysisGroupValue.toJson(), new AnalysisGroupValue());
		return newAnalysisGroupValue;
	}

	public static long countAnalysisGroupValues() {
		return entityManager().createQuery("SELECT COUNT(o) FROM AnalysisGroupValue o", Long.class).getSingleResult();
		
	}
	
	public static Query findBatchCodeBySearchFilter(String advancedFilterSQL) {
		//NOTE: this requires the creation of api_experiment_results view in the database
		
		String sqlQuery = "select distinct tested_lot from (" + advancedFilterSQL + ") temp";
		logger.info(sqlQuery);
		EntityManager em = entityManager();
		Query q = em.createNativeQuery(sqlQuery);
//		q.setParameter("advancedFilterSQL", advancedFilterSQL);
		return q;
	}
	
	
	public static TypedQuery<AnalysisGroupValueBaseDTO> findAnalysisGroupValueExptCodeAndStateTypeKindAndValueTypeKind( String experimentCode, String stateType, String stateKind,  String valueType, String valueKind) {

		String sqlQuery = "select new com.labsynch.labseer.dto.AnalysisGroupValueBaseDTO( " +
				"agv.id, ags.id as stateId, ag.codeName as agCodeName, agv.lsType, agv.lsKind, agv.stringValue, " +
				"agv.codeValue, agv.fileValue, agv.urlValue, agv.dateValue, " +
				"agv.clobValue, agv.operatorType, agv.operatorKind, agv.numericValue, " +
				"agv.sigFigs, agv.uncertainty, agv.numberOfReplicates, agv.uncertaintyType, " +
				"agv.unitType, agv.unitKind, agv.comments, agv.ignored, " +
				"agv.lsTransaction, agv.publicData) " +
				"FROM AnalysisGroupValue agv " +
				"join agv.lsState ags with ags.ignored = false " +
				"join ags.analysisGroup ag with ag.ignored = false " +
				"join ag.experiment exp with exp.ignored = false " +
				"where ags.lsType = :stateType AND ags.lsKind = :stateKind " +
				"and agv.lsType = :valueType AND agv.lsKind = :valueKind and agv.ignored = false " +
				"and exp.codeName = :experimentCode";

		EntityManager em = entityManager();
		TypedQuery<AnalysisGroupValueBaseDTO> q = em.createQuery(sqlQuery, AnalysisGroupValueBaseDTO.class);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("valueType", valueType);
		q.setParameter("valueKind", valueKind);
		q.setParameter("experimentCode", experimentCode);
		return q;

	}
	
	public static TypedQuery<AnalysisGroupValueBaseDTO> findAnalysisGroupValueByLsTypeKindAndExptCode(String lsType, String lsKind, String experimentCode) {

		String sqlQuery = "select new com.labsynch.labseer.dto.AnalysisGroupValueBaseDTO( " +
				"agv.id, ags.id as stateId, ag.codeName as agCodeName, agv.lsType, agv.lsKind, agv.stringValue, " +
				"agv.codeValue, agv.fileValue, agv.urlValue, agv.dateValue, " +
				"agv.clobValue, agv.operatorType, agv.operatorKind, agv.numericValue, " +
				"agv.sigFigs, agv.uncertainty, agv.numberOfReplicates, agv.uncertaintyType, " +
				"agv.unitType, agv.unitKind, agv.comments, agv.ignored, " +
				"agv.lsTransaction, agv.publicData) " +
				"FROM AnalysisGroupValue agv " +
				"join agv.lsState ags with ags.ignored = false " +
				"join ags.analysisGroup ag with ag.ignored = false " +
				"join ag.experiment exp with exp.ignored = false " +
				"where ags.lsType = :lsType AND ags.lsKind = :lsKind and agv.ignored = false " +
				"and exp.codeName = :experimentCode";

		EntityManager em = entityManager();
		TypedQuery<AnalysisGroupValueBaseDTO> q = em.createQuery(sqlQuery, AnalysisGroupValueBaseDTO.class);
		q.setParameter("lsType", lsType);
		q.setParameter("lsKind", lsKind);
		q.setParameter("experimentCode", experimentCode);
		return q;

	}
	
	public static TypedQuery<AnalysisGroupValueBaseDTO> findAnalysisGroupValueByLsTypeKindAndExptCodeAll(String lsType, String lsKind, String experimentCode) {

		String sqlQuery = "select new com.labsynch.labseer.dto.AnalysisGroupValueBaseDTO( " +
				"agv.id, ags.id as stateId, ag.codeName as agCodeName, agv.lsType, agv.lsKind, agv.stringValue, " +
				"agv.codeValue, agv.fileValue, agv.urlValue, agv.dateValue, " +
				"agv.clobValue, agv.operatorType, agv.operatorKind, agv.numericValue, " +
				"agv.sigFigs, agv.uncertainty, agv.numberOfReplicates, agv.uncertaintyType, " +
				"agv.unitType, agv.unitKind, agv.comments, agv.ignored, " +
				"agv.lsTransaction, agv.publicData) " +
				"FROM AnalysisGroupValue agv " +
				"join agv.lsState ags " +
				"join ags.analysisGroup ag " +
				"join ag.experiment exp " +
				"where ags.lsType = :lsType AND ags.lsKind = :lsKind " +
				"and exp.codeName = :experimentCode";

		EntityManager em = entityManager();
		TypedQuery<AnalysisGroupValueBaseDTO> q = em.createQuery(sqlQuery, AnalysisGroupValueBaseDTO.class);
		q.setParameter("lsType", lsType);
		q.setParameter("lsKind", lsKind);
		q.setParameter("experimentCode", experimentCode);
		return q;

	}
	
	public static List<AnalysisGroupValueDTO> findAnalysisGroupValueDTO(String batchCode) {

		String sqlQueryOld = "select new com.labsynch.labseer.dto.AnalysisGroupValueDTO(agv.id, expt.codeName) FROM AnalysisGroup ag " +
				"JOIN ag.lsStates ags with ags.lsType = 'data' " +
				"JOIN ags.lsValues agv with agv.lsKind != 'tested concentration' AND agv.lsKind != 'batch code' AND agv.lsKind != 'time' " +
				"JOIN ags.lsValues agv2 with agv2.lsKind = 'batch code' " +
				"LEFT OUTER JOIN ags.lsValues agv3 with agv3.lsKind = 'tested concentration' " +
				"LEFT OUTER JOIN ags.lsValues agv4 with agv4.lsKind = 'time' " +
				"JOIN ag.experiment expt " +
				"WHERE agv2.codeValue = :batchCode " +
				"GROUP BY agv.id, expt.codeName";	

		String sqlQuery = "select new com.labsynch.labseer.dto.AnalysisGroupValueDTO(agv.id, expt.id as experimentId, expt.codeName, " +
				"agv2.codeValue AS testedLot, " +
				"agv3.numericValue AS testedConc, " +  
				"CASE " +
				"  WHEN agv4.numericValue IS NOT NULL AND agv3.numericValue IS NOT NULL " +
				"    THEN concat(agv3.unitKind, ' and ', agv4.numericValue, ' ', agv4.unitKind) " +
				"  WHEN agv4.numericValue IS NOT NULL " +
				"     THEN concat(agv4.numericValue, ' ' , agv4.unitKind) " +
				"  ELSE agv3.unitKind " +
				"END AS testedConcUnit " +
				" ) FROM AnalysisGroup ag " +
				"JOIN ag.lsStates ags with ags.lsType = 'data' " +
				"JOIN ags.lsValues agv with agv.lsKind != 'tested concentration' AND agv.lsKind != 'batch code' AND agv.lsKind != 'time' " +
				"JOIN ags.lsValues agv2 with agv2.lsKind = 'batch code' " +
				"LEFT OUTER JOIN ags.lsValues agv3 with agv3.lsKind = 'tested concentration' " +
				"LEFT OUTER JOIN ags.lsValues agv4 with agv4.lsKind = 'time' " +
				"JOIN ag.experiment expt " +
				"WHERE agv2.codeValue = :batchCode ";


		EntityManager em = entityManager();
		TypedQuery<AnalysisGroupValueDTO> q = em.createQuery(sqlQuery, AnalysisGroupValueDTO.class);
		q.setParameter("batchCode", batchCode);
		return q.getResultList();

	}

	
	public static TypedQuery<AnalysisGroupValueDTO> findAnalysisGroupValueDTOCmpds(Set<String> batchCodeList) {
			
		String sqlQuery = "select new com.labsynch.labseer.dto.AnalysisGroupValueDTO(agv.id, expt.id as experimentId, expt.codeName, el.labelText as prefName, " +
				"agv.lsType as lsType, agv.lsKind as lsKind, " +
				"agv.stringValue as stringValue, agv.numericValue as numericValue, " +
				"agv2.codeValue AS testedLot, agv2.codeValue AS geneId " +
				" ) FROM AnalysisGroup ag " +
				"JOIN ag.lsStates ags with ags.lsType = 'data' " +
				"JOIN ags.lsValues agv with agv.lsKind != 'tested concentration' AND agv.lsKind != 'batch code' AND agv.lsKind != 'time' " +
				"JOIN ags.lsValues agv2 with agv2.lsKind = 'batch code' " +
				"JOIN ag.experiment expt with expt.ignored = false " +
				"JOIN expt.lsLabels el with el.lsType = 'name' and el.lsKind = 'experiment name' and el.preferred = true and el.ignored = false " +
				"WHERE ag.ignored = false and agv2.codeValue IN (:batchCodeList) ";


		EntityManager em = entityManager();
		TypedQuery<AnalysisGroupValueDTO> q = em.createQuery(sqlQuery, AnalysisGroupValueDTO.class);
		q.setParameter("batchCodeList", batchCodeList);
		
		logger.info("here is the query: " + sqlQuery);
		for (String batch : batchCodeList){
			logger.info("here is the batch: " + batch);
		}
		
		return q;

	}
	
	
	public static TypedQuery<AnalysisGroupValueDTO> findAnalysisGroupValueDTO(Set<String> batchCodeList) {

		String sqlQuery = "select new com.labsynch.labseer.dto.AnalysisGroupValueDTO(agv.id, expt.id as experimentId, expt.codeName, el.labelText as prefName, " +
				"agv.lsType as lsType, agv.lsKind as lsKind, " +
				"agv.stringValue as stringValue, agv.numericValue as numericValue, " +
				"agv2.codeValue AS testedLot, tl.labelText as geneId  " +
				" ) FROM AnalysisGroup ag, LsThing thing " +
				"JOIN ag.lsStates ags with ags.lsType = 'data' and ags.ignored = false " +
				"JOIN ags.lsValues agv with agv.lsKind != 'tested concentration' AND agv.lsKind != 'batch code' AND agv.lsKind != 'time' and agv.ignored = false " +
				"JOIN ags.lsValues agv2 with agv2.lsKind = 'batch code' and agv2.ignored = false " +
				"JOIN thing.lsLabels tl with tl.ignored = false and tl.lsType = 'name' and tl.lsKind = 'Entrez Gene ID' and tl.ignored = false " +
				"JOIN ag.experiment expt with expt.ignored = false " +
				"JOIN expt.lsLabels el with el.lsType = 'name' and el.lsKind = 'experiment name' and el.preferred = true and el.ignored = false " +
				"WHERE ag.ignored = false AND thing.codeName = agv2.codeValue AND agv2.codeValue IN (:batchCodeList) ";
				
		String sqlQueryOld = "select new com.labsynch.labseer.dto.AnalysisGroupValueDTO(agv.id, expt.id as experimentId, expt.codeName, " +
				"agv.lsType as lsType, agv.lsKind as lsKind, " +
				"agv.stringValue as stringValue, agv.numericValue as numericValue, " +
				"agv2.codeValue AS testedLot " +
				" ) FROM AnalysisGroup ag " +
				"JOIN ag.lsStates ags with ags.lsType = 'data' " +
				"JOIN ags.lsValues agv with agv.lsKind != 'tested concentration' AND agv.lsKind != 'batch code' AND agv.lsKind != 'time' " +
				"JOIN ags.lsValues agv2 with agv2.lsKind = 'batch code' " +
				"JOIN ag.experiment expt " +
				"WHERE agv2.codeValue IN (:batchCodeList) ";


		EntityManager em = entityManager();
		TypedQuery<AnalysisGroupValueDTO> q = em.createQuery(sqlQuery, AnalysisGroupValueDTO.class);
		q.setParameter("batchCodeList", batchCodeList);
		
		logger.info("here is the query: " + sqlQuery);
		for (String batch : batchCodeList){
			logger.info("here is the batch: " + batch);
		}
		
		return q;

	}
	

	
	public static TypedQuery<AnalysisGroupValueDTO> findAnalysisGroupValueDTO(Set<String> batchCodeList, Set<String> experimentCodeList) {

		logger.debug("size for batchCodeList: " + batchCodeList.size());
		logger.debug("size for experimentCodeList: " + experimentCodeList.size());
		
		String oldsqlQuery = "select new com.labsynch.labseer.dto.AnalysisGroupValueDTO(agv.id, expt.id as experimentId, expt.codeName, el.labelText as prefName, " +
				"agv.lsType as lsType, agv.lsKind as lsKind, " +
				"agv.stringValue as stringValue, agv.numericValue as numericValue, " +
				"agv2.codeValue AS testedLot, tl.labelText as geneId  " +
				" ) FROM AnalysisGroup ag, LsThing thing " +
				"JOIN ag.lsStates ags with ags.lsType = 'data' and ags.ignored = false " +
				"JOIN ags.lsValues agv with agv.lsKind != 'tested concentration' AND agv.lsKind != 'batch code' AND agv.lsKind != 'time' and agv.ignored = false " +
				"JOIN ags.lsValues agv2 with agv2.lsKind = 'batch code' and agv2.ignored = false " +
				"JOIN thing.lsLabels tl with tl.ignored = false and tl.lsType = 'name' and tl.lsKind = 'Entrez Gene ID' and tl.ignored = false " +
				"JOIN ag.experiment expt with expt.ignored = false " +
				"JOIN expt.lsLabels el with el.lsType = 'name' and el.lsKind = 'experiment name' and el.preferred = true and el.ignored = false " +
				"WHERE ag.ignored = false AND thing.codeName = agv2.codeValue AND agv2.codeValue IN (:batchCodeList) " +
				"AND expt.codeName IN  (:experimentCodeList) and expt.ignored = false";
		
		String sqlQuery = "select new com.labsynch.labseer.dto.AnalysisGroupValueDTO(agv.id, expt.id as experimentId, expt.codeName, el.labelText as prefName, " +
				"agv.lsType as lsType, agv.lsKind as lsKind, " +
				"agv.stringValue as stringValue, agv.numericValue as numericValue, " +
				"agv2.codeValue AS testedLot, tl.labelText as geneId  " +
				" ) FROM AnalysisGroup ag,  LsThing thing " +
				"JOIN ag.lsStates ags with ags.lsType = 'data' and ags.ignored = false " +
				"JOIN ags.lsValues agv with agv.lsKind != 'tested concentration' AND agv.lsKind != 'batch code' AND agv.lsKind != 'time' and agv.ignored = false " +
				"JOIN ags.lsValues agv2 with agv2.lsKind = 'batch code' and agv2.ignored = false " +
				"JOIN thing.lsLabels tl with tl.ignored = false and tl.lsType = 'name' and tl.lsKind = 'Entrez Gene ID' and tl.ignored = false " +
				"JOIN ag.experiment expt with expt.ignored = false " +
				"JOIN expt.lsLabels el with el.lsType = 'name' and el.lsKind = 'experiment name' and el.preferred = true and el.ignored = false " +
				"WHERE ag.ignored = false AND thing.codeName = agv2.codeValue AND thing.lsType = 'gene' and thing.lsKind = 'entrez gene' AND agv2.codeValue IN (:batchCodeList) " +
				"AND expt.codeName IN  (:experimentCodeList) and expt.ignored = false";

		EntityManager em = entityManager();
		TypedQuery<AnalysisGroupValueDTO> q = em.createQuery(sqlQuery, AnalysisGroupValueDTO.class);
		q.setParameter("batchCodeList", batchCodeList);
		q.setParameter("experimentCodeList", experimentCodeList);

		return q;
	}
	
	public static TypedQuery<AnalysisGroupValueDTO> findAnalysisGroupValueDTOByExperiments(Set<String> experimentCodeList) {

		String sqlQuery = "select new com.labsynch.labseer.dto.AnalysisGroupValueDTO(agv.id, expt.id as experimentId, expt.codeName, el.labelText as prefName, " +
				"agv.lsType as lsType, agv.lsKind as lsKind, " +
				"agv.stringValue as stringValue, agv.numericValue as numericValue, " +
				"agv2.codeValue AS testedLot, tl.labelText as geneId " +
				" ) FROM AnalysisGroup ag, LsThing thing " +
				"JOIN ag.lsStates ags with ags.lsType = 'data' and ags.ignored = false " +
				"JOIN ags.lsValues agv with agv.lsKind != 'tested concentration' AND agv.lsKind != 'batch code' AND agv.lsKind != 'time' and agv.ignored = false " +
				"JOIN ags.lsValues agv2 with agv2.lsKind = 'batch code' and agv2.ignored = false " +
				"JOIN thing.lsLabels tl with tl.ignored = false and tl.lsType = 'name' and tl.lsKind = 'Entrez Gene ID' and tl.ignored = false " +
				"JOIN ag.experiment expt with expt.ignored = false " +
				"JOIN expt.lsLabels el with el.lsType = 'name' and el.lsKind = 'experiment name' and el.preferred = true and el.ignored = false " +
				"WHERE ag.ignored = false " +
				"AND thing.codeName = agv2.codeValue " +
				"AND expt.codeName IN  (:experimentCodeList) ";

		logger.debug("query sql: " + sqlQuery);
		EntityManager em = entityManager();
		TypedQuery<AnalysisGroupValueDTO> q = em.createQuery(sqlQuery, AnalysisGroupValueDTO.class);
		q.setParameter("experimentCodeList", experimentCodeList);

		return q;
	}
	
	
	public static TypedQuery<ValueTypeKindDTO> findAnalysisGroupValueTypeKindDTO(Collection<String> exptCodeList) {

		String sqlQuery = "select new com.labsynch.labseer.dto.ValueTypeKindDTO(agv.lsType as lsType, agv.lsKind as lsKind) " +
				"FROM AnalysisGroupValue agv " +
				"join agv.lsState ags " +
				"join ags.analysisGroup ag " +
				"join ag.experiment exp " +
				"where exp.codeName in (:exptCodeList) " + 
				"AND agv.lsType IN ('stringValue', 'numericValue') " +
				"and ags.lsType = 'data' and agv.ignored = false and ags.ignored = false and ag.ignored = false " +
				"group by agv.lsType, agv.lsKind";

		EntityManager em = entityManager();
		TypedQuery<ValueTypeKindDTO> q = em.createQuery(sqlQuery, ValueTypeKindDTO.class);
		q.setParameter("exptCodeList", exptCodeList);
		return q;

	}

	public static TypedQuery<ValueTypeKindDTO> findAnalysisGroupValueTypeKindDTO(String exptCode) {

		String sqlQuery = "select new com.labsynch.labseer.dto.ValueTypeKindDTO(agv.lsType as lsType, agv.lsKind as lsKind) " +
				"FROM AnalysisGroupValue agv " +
				"join agv.lsState ags " +
				"join ags.analysisGroup ag " +
				"join ag.experiment exp " +
				"where exp.codeName = :exptCode " + 
				"AND agv.lsType IN ('stringValue', 'numericValue') " +
				"and ags.lsType = 'data' and agv.ignored = false and ags.ignored = false and ag.ignored = false " +
				"group by agv.lsType, agv.lsKind";

		EntityManager em = entityManager();
		TypedQuery<ValueTypeKindDTO> q = em.createQuery(sqlQuery, ValueTypeKindDTO.class);
		q.setParameter("exptCode", exptCode);
		return q;

	}
	
	@Transactional
	public static List<com.labsynch.labseer.domain.AnalysisGroupValue> findAllAnalysisGroupValues() {
		return entityManager().createQuery("SELECT o FROM AnalysisGroupValue o", AnalysisGroupValue.class).getResultList();
	}

	@Transactional
	public static com.labsynch.labseer.domain.AnalysisGroupValue findAnalysisGroupValue(Long id) {
		if (id == null) return null;
		return entityManager().find(AnalysisGroupValue.class, id);
	}

	@Transactional
	public static List<com.labsynch.labseer.domain.AnalysisGroupValue> findAnalysisGroupValueEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM AnalysisGroupValue o", AnalysisGroupValue.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

	@Transactional
	public static com.labsynch.labseer.domain.AnalysisGroupValue fromJsonToAnalysisGroupValue(String json) {
		AnalysisGroupValue analysisGroupValue = new JSONDeserializer<AnalysisGroupValue>().use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json, AnalysisGroupValue.class);
		return analysisGroupValue;
	}

	@Transactional
	public static Collection<com.labsynch.labseer.domain.AnalysisGroupValue> fromJsonArrayToAnalysisGroupValues(String json) {
		return new JSONDeserializer<List<AnalysisGroupValue>>().use(null, ArrayList.class).use("values", AnalysisGroupValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
	}

	@Transactional
	public static Collection<com.labsynch.labseer.domain.AnalysisGroupValue> fromJsonArrayToAnalysisGroupValues(Reader json) {
		return new JSONDeserializer<List<AnalysisGroupValue>>().use(null, ArrayList.class).use("values", AnalysisGroupValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
	}

	@Transactional
	public String toPrettyJson() {
		return new JSONSerializer().exclude("*.class").prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(this);
	}

	@Transactional
	public String toJson() {
		return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
	}

	@Transactional
	public static String toJsonArray(Collection<com.labsynch.labseer.domain.AnalysisGroupValue> collection) {
		return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
	}

	@Transactional
	public static String toPrettyJsonArray(Collection<com.labsynch.labseer.domain.AnalysisGroupValue> collection) {
		return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).prettyPrint(true).serialize(collection);
	}
	
	@Transactional
	public static String toJsonArrayStub(Collection<com.labsynch.labseer.domain.AnalysisGroupValue> collection) {
		return new JSONSerializer().exclude("*.class", "lsState").transform(new ExcludeNulls(), void.class).serialize(collection);
	}

	@Transactional
	public com.labsynch.labseer.domain.AnalysisGroupValue merge() {
		if (this.entityManager == null) this.entityManager = entityManager();
		AnalysisGroupValue merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	@Transactional
	public static List<java.lang.Long> saveList(List<com.labsynch.labseer.domain.AnalysisGroupValue> entities) {
		logger.debug("saving the list of AnalysisGroupValues: " + entities.size());
		List<Long> idList = new ArrayList<Long>();
		int imported = 0;
		int batchSize = 50;
		for (AnalysisGroupValue e : entities) {
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
	public static String getAnalysisGroupValueCollectionJson(List<java.lang.Long> idList) {
		Collection<AnalysisGroupValue> analysisGroupValues = new HashSet<AnalysisGroupValue>();
		for (Long id : idList) {
			AnalysisGroupValue query = AnalysisGroupValue.findAnalysisGroupValue(id);
			if (query != null) analysisGroupValues.add(query);
		}
		return AnalysisGroupValue.toJsonArray(analysisGroupValues);
	}

	@Transactional
	public static int deleteByExperimentID(Long experimentId) {
		if (experimentId == null) return 0;
		EntityManager em = TreatmentGroupValue.entityManager();
		String deleteSQL = "DELETE FROM AnalysisGroupValue oo WHERE id in (select o.id from AnalysisGroupValue o where o.lsState.analysisGroup.experiment.id = :experimentId)";
		Query q = em.createQuery(deleteSQL);
		q.setParameter("experimentId", experimentId);
		int numberOfDeletedEntities = q.executeUpdate();
		return numberOfDeletedEntities;
	}

	public static com.labsynch.labseer.domain.AnalysisGroupValue update(com.labsynch.labseer.domain.AnalysisGroupValue analysisGroupValue) {
		AnalysisGroupValue updatedAnalysisGroupValue = new JSONDeserializer<AnalysisGroupValue>().use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(analysisGroupValue.toJson(), analysisGroupValue);
		updatedAnalysisGroupValue.setModifiedDate(new Date());
		updatedAnalysisGroupValue.merge();
		return updatedAnalysisGroupValue;
	}


	public static TypedQuery<String> findBatchCodedBySearchFilter(Collection<ExperimentFilterSearchDTO> searchFilters) {
		
        EntityManager em = AnalysisGroupValue.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//        CriteriaQuery<Tuple> criteria = criteriaBuilder.createTupleQuery();
        CriteriaQuery<String> criteria = criteriaBuilder.createQuery(String.class);

        Root<AnalysisGroupValue> agvRoot = criteria.from(AnalysisGroupValue.class);
        Root<AnalysisGroupValue> agvRoot2 = criteria.from(AnalysisGroupValue.class);
//        Join<AnalysisGroupValue, AnalysisGroupState> ags = agvRoot.join("lsState");
//        Join<AnalysisGroupValue, AnalysisGroupState> ags2 = agvRoot2.join("lsState");
//
//        Join<AnalysisGroupState, AnalysisGroup> ag = ags.join("analysisGroup");
//        Join<AnalysisGroup, Experiment> exp = ag.join("experiment");

        criteria.distinct(true);
		criteria.select(agvRoot2.<String>get("codeValue"));
        
        Predicate[] predicates = new Predicate[0];
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate predicate00 = criteriaBuilder.equal(agvRoot.<Long>get("lsState").get("id"), agvRoot2.<Long>get("lsState").get("id"));
        predicateList.add(predicate00);
        Predicate predicate01 = criteriaBuilder.equal(agvRoot2.<String>get("lsType"), "codeValue");
        predicateList.add(predicate01);
        Predicate predicate02 = criteriaBuilder.equal(agvRoot2.<String>get("lsKind"), "batch code");
        predicateList.add(predicate02);

        
        for (ExperimentFilterSearchDTO searchFilter : searchFilters){
        	
            Predicate predicate1 = criteriaBuilder.equal(agvRoot.<String>get("lsType"), searchFilter.getLsType());
            predicateList.add(predicate1);
            Predicate predicate2 = criteriaBuilder.equal(agvRoot.<String>get("lsKind"), searchFilter.getLsKind());
            predicateList.add(predicate2);
            Predicate predicate3 = criteriaBuilder.equal(agvRoot.<String>get("lsState").get("analysisGroup").get("experiment").get("codeName"), searchFilter.getCodeName());
            predicateList.add(predicate3);

            if (searchFilter.getLsType().equalsIgnoreCase("numericValue")){
            	Predicate predicate4 = null;
            	if(searchFilter.getOperator().equalsIgnoreCase(">")){
            		predicate4 = criteriaBuilder.gt(agvRoot.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
            	} else if (searchFilter.getOperator().equalsIgnoreCase("<")){
            		predicate4 = criteriaBuilder.lt(agvRoot.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
             	} else if (searchFilter.getOperator().equalsIgnoreCase("=")){
            		predicate4 = criteriaBuilder.equal(agvRoot.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
             	}
            	Predicate predicate5 = criteriaBuilder.isNull(agvRoot.<BigDecimal>get("numericValue"));
            	if (predicate4 != null){
                	Predicate predicate6 = criteriaBuilder.or(predicate5, predicate4);
                    predicateList.add(predicate6);
            	} else {
                	Predicate predicate6 = criteriaBuilder.or(predicate5);
                    predicateList.add(predicate6);           		
            	}
            } else if (searchFilter.getLsType().equalsIgnoreCase("stringValue")) {
            	Predicate predicate4 = null;
            	if(searchFilter.getOperator().equalsIgnoreCase("equals")){
            		predicate4 = criteriaBuilder.equal(agvRoot.<String>get("stringValue"), searchFilter.getFilterValue());
            	} else if (searchFilter.getOperator().equalsIgnoreCase("contains")){
            		predicate4 = criteriaBuilder.like(agvRoot.<String>get("stringValue"), "%" + searchFilter.getFilterValue() + "%");
             	} 
            	Predicate predicate5 = criteriaBuilder.isNull(agvRoot.<String>get("stringValue"));
            	if (predicate4 != null){
                	Predicate predicate6 = criteriaBuilder.or(predicate5, predicate4);
                    predicateList.add(predicate6);
            	} else {
                	Predicate predicate6 = criteriaBuilder.or(predicate5);
                    predicateList.add(predicate6);
            	}
            }
            
        }
        criteria.where(criteriaBuilder.and(predicateList.toArray(predicates)));
        TypedQuery<String> q = em.createQuery(criteria);

        return q;
	}

	//something is wrong with the following method. DO NOT USE!!!
	public static TypedQuery<String> findBatchCodeBySearchFilters(
			Set<String> batchCodeList, Set<String> experimentCodeList,
			Set<ExperimentFilterSearchDTO> searchFilters) {
		
        EntityManager em = AnalysisGroupValue.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<String> criteria = criteriaBuilder.createQuery(String.class);

        Root<AnalysisGroupValue> agvRoot = criteria.from(AnalysisGroupValue.class);
        Root<AnalysisGroupValue> agvRoot2 = criteria.from(AnalysisGroupValue.class);

        criteria.distinct(true);
		criteria.select(agvRoot2.<String>get("codeValue"));
        
        Predicate[] predicates = new Predicate[0];
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate predicate00 = criteriaBuilder.equal(agvRoot.<Long>get("lsState").get("id"), agvRoot2.<Long>get("lsState").get("id"));
        predicateList.add(predicate00);

        if (batchCodeList != null && batchCodeList.size() > 0){
          Predicate predicate01 = criteriaBuilder.equal(agvRoot2.<String>get("lsType"), "codeValue");
          predicateList.add(predicate01);
          Predicate predicate02 = criteriaBuilder.equal(agvRoot2.<String>get("lsKind"), "batch code");
          predicateList.add(predicate02);
            Expression<String> exp03 = agvRoot2.get("codeValue");
            Predicate predicate03 = exp03.in(batchCodeList);
            predicateList.add(predicate03);
            
        } else {
        	logger.debug("no batchCodeList present");
        }

        if (experimentCodeList != null && experimentCodeList.size() > 0){
            Expression<String> exp04 = agvRoot.<String>get("lsState").get("analysisGroup").get("experiment").get("codeName");
            Predicate predicate04 = exp04.in(experimentCodeList);
            predicateList.add(predicate04);

        } else {
            Predicate predicate04b = criteriaBuilder.equal(agvRoot.<Boolean>get("lsState").get("analysisGroup").get("experiment").get("ignored"), false);
            predicateList.add(predicate04b);

        }
        logger.debug("number of search filters: " + searchFilters.size());
        if (searchFilters != null && searchFilters.size() > 0){
            for (ExperimentFilterSearchDTO searchFilter : searchFilters){
            	logger.debug(searchFilter.toJson());
            	
                Root<AnalysisGroupValue> agvRootNew = criteria.from(AnalysisGroupValue.class);
                Predicate predicateNew = criteriaBuilder.equal(agvRoot2.<Long>get("lsState").get("id"), agvRootNew.<Long>get("lsState").get("id"));
                predicateList.add(predicateNew);
                Predicate predicate11 = criteriaBuilder.equal(agvRoot2.<String>get("lsType"), "codeValue");
                predicateList.add(predicate11);
                Predicate predicate12 = criteriaBuilder.equal(agvRoot2.<String>get("lsKind"), "batch code");
                predicateList.add(predicate12);

                Predicate predicate1 = criteriaBuilder.equal(agvRootNew.<String>get("lsType"), searchFilter.getLsType());
                predicateList.add(predicate1);
                Predicate predicate2 = criteriaBuilder.equal(agvRootNew.<String>get("lsKind"), searchFilter.getLsKind());
                predicateList.add(predicate2);
                Predicate predicate3 = criteriaBuilder.equal(agvRootNew.<String>get("lsState").get("analysisGroup").get("experiment").get("codeName"), searchFilter.getExperimentCode());
                predicateList.add(predicate3);

                if (searchFilter.getLsType().equalsIgnoreCase("numericValue")){
                	Predicate predicate4 = null;
                	if(searchFilter.getOperator().equalsIgnoreCase(">")){
                		predicate4 = criteriaBuilder.gt(agvRootNew.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
                	} else if (searchFilter.getOperator().equalsIgnoreCase("<")){
                		predicate4 = criteriaBuilder.lt(agvRootNew.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
                 	} else if (searchFilter.getOperator().equalsIgnoreCase("=")){
                		predicate4 = criteriaBuilder.equal(agvRootNew.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
                 	}
                	Predicate predicate5 = criteriaBuilder.isNull(agvRootNew.<BigDecimal>get("numericValue"));
                	if (predicate4 != null){
                    	Predicate predicate6 = criteriaBuilder.or(predicate5, predicate4);
                        predicateList.add(predicate6);
                	} else {
                    	Predicate predicate6 = criteriaBuilder.or(predicate5);
                        predicateList.add(predicate6);           		
                	}
                } else if (searchFilter.getLsType().equalsIgnoreCase("stringValue")) {
                	logger.debug("stringValue search filter " + searchFilter.getFilterValue());
                	Predicate predicate7 = null;
                	if(searchFilter.getOperator().equalsIgnoreCase("equals")){
                		predicate7 = criteriaBuilder.like(agvRootNew.<String>get("stringValue"), searchFilter.getFilterValue());
                	} else if (searchFilter.getOperator().equalsIgnoreCase("contains")){
                		predicate7 = criteriaBuilder.like(agvRootNew.<String>get("stringValue"), "%" + searchFilter.getFilterValue() + "%");
                 	} 
                	Predicate predicate8 = criteriaBuilder.isNull(agvRootNew.<String>get("stringValue"));
                	if (predicate7 != null){
                    	Predicate predicate9 = criteriaBuilder.or(predicate8, predicate7);
                        predicateList.add(predicate9);
                	} else {
                    	Predicate predicate9 = criteriaBuilder.or(predicate8);
                        predicateList.add(predicate9);
                	}
                } 
                
            }
        }
        
        for (Predicate p : predicateList){
        	logger.debug("predicate: " + p.toString());
        }
        
        criteria.where(criteriaBuilder.and(predicateList.toArray(predicates)));
        TypedQuery<String> q = em.createQuery(criteria);
        return q;
	}

	public static TypedQuery<String> findBatchCodeBySearchFilter(
			Set<String> batchCodeList, Set<String> experimentCodeList,
			ExperimentFilterSearchDTO searchFilter) {
		
        EntityManager em = AnalysisGroupValue.entityManager();
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<String> criteria = criteriaBuilder.createQuery(String.class);

        Root<AnalysisGroupValue> agvRoot = criteria.from(AnalysisGroupValue.class);
        Root<AnalysisGroupValue> agvRoot2 = criteria.from(AnalysisGroupValue.class);

        criteria.distinct(true);
		criteria.select(agvRoot2.<String>get("codeValue"));
        
        Predicate[] predicates = new Predicate[0];
        List<Predicate> predicateList = new ArrayList<Predicate>();
        Predicate predicate00 = criteriaBuilder.equal(agvRoot.<Long>get("lsState").get("id"), agvRoot2.<Long>get("lsState").get("id"));
        predicateList.add(predicate00);

        Predicate predicate01 = criteriaBuilder.equal(agvRoot2.<String>get("lsType"), "codeValue");
        predicateList.add(predicate01);
        Predicate predicate02 = criteriaBuilder.equal(agvRoot2.<String>get("lsKind"), "batch code");
        predicateList.add(predicate02);
        
        if (batchCodeList != null && batchCodeList.size() > 0){
          Expression<String> exp03 = agvRoot2.get("codeValue");
          Predicate predicate03 = exp03.in(batchCodeList);
          predicateList.add(predicate03);
        }

        if (experimentCodeList != null && experimentCodeList.size() > 0){
            Expression<String> exp04 = agvRoot.<String>get("lsState").get("analysisGroup").get("experiment").get("codeName");
            Predicate predicate04 = exp04.in(experimentCodeList);
            predicateList.add(predicate04);
        }
        
        if (searchFilter != null){
            	
                Predicate predicate1 = criteriaBuilder.equal(agvRoot.<String>get("lsType"), searchFilter.getLsType());
                predicateList.add(predicate1);
                Predicate predicate2 = criteriaBuilder.equal(agvRoot.<String>get("lsKind"), searchFilter.getLsKind());
                predicateList.add(predicate2);
                Predicate predicate3 = criteriaBuilder.equal(agvRoot.<String>get("lsState").get("analysisGroup").get("experiment").get("codeName"), searchFilter.getExperimentCode());
                predicateList.add(predicate3);

                if (searchFilter.getLsType().equalsIgnoreCase("numericValue")){
                	Predicate predicate4 = null;
                	if(searchFilter.getOperator().equalsIgnoreCase(">")){
                		predicate4 = criteriaBuilder.gt(agvRoot.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
                	} else if (searchFilter.getOperator().equalsIgnoreCase("<")){
                		predicate4 = criteriaBuilder.lt(agvRoot.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
                 	} else if (searchFilter.getOperator().equalsIgnoreCase("=")){
                		predicate4 = criteriaBuilder.equal(agvRoot.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
                 	}
                	Predicate predicate5 = criteriaBuilder.isNull(agvRoot.<BigDecimal>get("numericValue"));
                	if (predicate4 != null){
                    	Predicate predicate6 = criteriaBuilder.or(predicate5, predicate4);
                        predicateList.add(predicate6);
                	} else {
                    	Predicate predicate6 = criteriaBuilder.or(predicate5);
                        predicateList.add(predicate6);           		
                	}
                } else if (searchFilter.getLsType().equalsIgnoreCase("stringValue")) {
                	Predicate predicate4 = null;
                	if(searchFilter.getOperator().equalsIgnoreCase("equals")){
                		predicate4 = criteriaBuilder.like(agvRoot.<String>get("stringValue"), searchFilter.getFilterValue());
                	} else if (searchFilter.getOperator().equalsIgnoreCase("contains")){
                		predicate4 = criteriaBuilder.like(agvRoot.<String>get("stringValue"), "%" + searchFilter.getFilterValue() + "%");
                 	} 
                	Predicate predicate5 = criteriaBuilder.isNull(agvRoot.<String>get("stringValue"));
                	if (predicate4 != null){
                    	Predicate predicate6 = criteriaBuilder.or(predicate5, predicate4);
                        predicateList.add(predicate6);
                	} else {
                    	Predicate predicate6 = criteriaBuilder.or(predicate5);
                        predicateList.add(predicate6);
                	}
                }
        }
        
        criteria.where(criteriaBuilder.and(predicateList.toArray(predicates)));
        TypedQuery<String> q = em.createQuery(criteria);
        return q;
        
	}




}
