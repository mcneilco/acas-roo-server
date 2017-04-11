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
import javax.persistence.criteria.Join;
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
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;

import com.labsynch.labseer.dto.AnalysisGroupCsvDTO;
import com.labsynch.labseer.dto.AnalysisGroupValueBaseDTO;
import com.labsynch.labseer.dto.AnalysisGroupValueDTO;
import com.labsynch.labseer.dto.ExperimentFilterSearchDTO;
import com.labsynch.labseer.dto.FlatThingCsvDTO;
import com.labsynch.labseer.dto.ValueTypeKindDTO;
import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findAnalysisGroupValuesByLsState", "findAnalysisGroupValuesByLsTransactionEquals", "findAnalysisGroupValuesByCodeValueEquals", "findAnalysisGroupValuesByIgnoredNotAndCodeValueEquals", "findAnalysisGroupValuesByLsTypeEqualsAndLsKindEquals", "findAnalysisGroupValuesByLsTypeEqualsAndLsKindEqualsAndStringValueLikeAndIgnoredNot" })
public class AnalysisGroupValue extends AbstractValue {

	private static final Logger logger = LoggerFactory.getLogger(AnalysisGroupValue.class);

	private static final int PARAMETER_LIMIT = 999;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "analysis_state_id")
    private AnalysisGroupState lsState;
    
    public AnalysisGroupValue(AnalysisGroupCsvDTO analysisGroupDTO) {
    	this.setCodeValue(analysisGroupDTO.getCodeValue());
    	this.setCodeOrigin(analysisGroupDTO.getCodeOrigin());
    	this.setCodeType(analysisGroupDTO.getCodeType());
    	this.setCodeKind(analysisGroupDTO.getCodeKind());
    	this.setLsType(analysisGroupDTO.getValueType());
    	this.setLsKind(analysisGroupDTO.getValueKind());
    	this.setClobValue(analysisGroupDTO.getClobValue());
    	this.setStringValue(analysisGroupDTO.getStringValue());
    	this.setFileValue(analysisGroupDTO.getFileValue());
    	this.setUrlValue(analysisGroupDTO.getUrlValue());
    	this.setDateValue(analysisGroupDTO.getDateValue());
    	this.setOperatorKind(analysisGroupDTO.getValueOperator());
    	this.setNumericValue(analysisGroupDTO.getNumericValue());
    	this.setFileValue(analysisGroupDTO.getFileValue());
    	this.setUncertainty(analysisGroupDTO.getUncertainty());
    	this.setUncertaintyType(analysisGroupDTO.getUncertaintyType());
    	this.setUnitKind(analysisGroupDTO.getValueUnit());
    	this.setConcentration(analysisGroupDTO.getConcentration());
    	this.setConcUnit(analysisGroupDTO.getConcUnit());
    	this.setNumberOfReplicates(analysisGroupDTO.getNumberOfReplicates());
        this.setRecordedBy(analysisGroupDTO.getRecordedBy());
        this.setRecordedDate(analysisGroupDTO.getRecordedDate());
        this.setLsTransaction(analysisGroupDTO.getLsTransaction());
        this.setModifiedBy(analysisGroupDTO.getModifiedBy());
        this.setModifiedDate(analysisGroupDTO.getModifiedDate());
        this.setComments(analysisGroupDTO.getComments());
        this.setIgnored(analysisGroupDTO.isIgnored());
        this.setPublicData(analysisGroupDTO.isPublicData());
        this.setSigFigs(analysisGroupDTO.getSigFigs());
	}

	public AnalysisGroupValue(FlatThingCsvDTO analysisGroupDTO) {
    	this.setCodeValue(analysisGroupDTO.getCodeValue());
    	this.setCodeOrigin(analysisGroupDTO.getCodeOrigin());
    	this.setCodeType(analysisGroupDTO.getCodeType());
    	this.setCodeKind(analysisGroupDTO.getCodeKind());
    	this.setLsType(analysisGroupDTO.getValueType());
    	this.setLsKind(analysisGroupDTO.getValueKind());
    	this.setStringValue(analysisGroupDTO.getStringValue());
    	this.setClobValue(analysisGroupDTO.getClobValue());
    	this.setFileValue(analysisGroupDTO.getFileValue());
    	this.setUrlValue(analysisGroupDTO.getUrlValue());
    	this.setDateValue(analysisGroupDTO.getDateValue());
    	this.setOperatorKind(analysisGroupDTO.getOperatorKind());
    	this.setNumericValue(analysisGroupDTO.getNumericValue());
    	this.setFileValue(analysisGroupDTO.getFileValue());
    	this.setUncertainty(analysisGroupDTO.getUncertainty());
    	this.setUncertaintyType(analysisGroupDTO.getUncertaintyType());
    	this.setUnitKind(analysisGroupDTO.getUnitKind());
    	this.setConcentration(analysisGroupDTO.getConcentration());
    	this.setConcUnit(analysisGroupDTO.getConcUnit());
    	this.setNumberOfReplicates(analysisGroupDTO.getNumberOfReplicates());
        this.setRecordedBy(analysisGroupDTO.getRecordedBy());
        this.setRecordedDate(analysisGroupDTO.getRecordedDate());
        this.setLsTransaction(analysisGroupDTO.getLsTransaction());
        this.setModifiedBy(analysisGroupDTO.getModifiedBy());
        this.setModifiedDate(analysisGroupDTO.getModifiedDate());
        this.setComments(analysisGroupDTO.getComments());
        this.setIgnored(analysisGroupDTO.isIgnored());
        this.setPublicData(analysisGroupDTO.isPublicData());
        this.setSigFigs(analysisGroupDTO.getSigFigs());
   }


	public Long getStateId() {
		return this.lsState.getId();
	}

	public boolean getIgnored() {
		return this.isIgnored();
	}

	public boolean getDeleted() {
		return this.isDeleted();
	}

	public boolean getPublicData() {
		return this.isPublicData();
	}

	public String getStateType() {
		return this.lsState.getLsType();
	}

	public String getStateKind() {
		return this.lsState.getLsKind();
	}

	public Long getAnalysisGroupId() {
		return this.lsState.getAnalysisGroup().getId();
	}

	public String getAnalysisGroupCode() {
		return this.lsState.getAnalysisGroup().getCodeName();
	}

	public static com.labsynch.labseer.domain.AnalysisGroupValue create(com.labsynch.labseer.domain.AnalysisGroupValue analysisGroupValue) {
		AnalysisGroupValue newAnalysisGroupValue = new JSONDeserializer<AnalysisGroupValue>().use(null, AnalysisGroupValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(analysisGroupValue.toJson(), new AnalysisGroupValue());
		return newAnalysisGroupValue;
	}

	public static long countAnalysisGroupValues() {
		return entityManager().createQuery("SELECT COUNT(o) FROM AnalysisGroupValue o", Long.class).getSingleResult();
	}

	public static Query findBatchCodeBySearchFilter(String advancedFilterSQL) {
		String sqlQuery = "select distinct tested_lot from (" + advancedFilterSQL + ") temp";
		logger.info(sqlQuery);
		EntityManager em = entityManager();
		Query q = em.createNativeQuery(sqlQuery);
		return q;
	}

	public static TypedQuery<com.labsynch.labseer.dto.AnalysisGroupValueBaseDTO> findAnalysisGroupValueExptCodeAndStateTypeKindAndValueTypeKind(String experimentCode, String stateType, String stateKind, String valueType, String valueKind) {
		String sqlQuery = "select new com.labsynch.labseer.dto.AnalysisGroupValueBaseDTO( " 
				+ "agv.id, ags.id as stateId, ag.codeName as agCodeName, agv.lsType, agv.lsKind, agv.stringValue, " 
				+ "agv.codeValue, agv.fileValue, agv.urlValue, agv.dateValue, " 
				+ "agv.clobValue, agv.operatorType, agv.operatorKind, agv.numericValue, " 
				+ "agv.sigFigs, agv.uncertainty, agv.numberOfReplicates, agv.uncertaintyType, " 
				+ "agv.unitType, agv.unitKind, agv.comments, agv.ignored, " + "agv.lsTransaction, agv.publicData) " 
				+ "FROM AnalysisGroupValue agv " + "join agv.lsState ags with ags.ignored = false " 
				+ "join ags.analysisGroup ag with ag.ignored = false " + "join ag.experiments exp with exp.ignored = false " 
				+ "where ags.lsType = :stateType AND ags.lsKind = :stateKind " + "and agv.lsType = :valueType AND agv.lsKind = :valueKind and agv.ignored = false " 
				+ "and exp.codeName = :experimentCode";
		EntityManager em = entityManager();
		TypedQuery<AnalysisGroupValueBaseDTO> q = em.createQuery(sqlQuery, AnalysisGroupValueBaseDTO.class);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("valueType", valueType);
		q.setParameter("valueKind", valueKind);
		q.setParameter("experimentCode", experimentCode);
		return q;
	}

	public static TypedQuery<com.labsynch.labseer.dto.AnalysisGroupValueBaseDTO> findAnalysisGroupValueByLsTypeKindAndExptCode(String lsType, String lsKind, String experimentCode) {
		String sqlQuery = "select new com.labsynch.labseer.dto.AnalysisGroupValueBaseDTO( " 
				+ "agv.id, ags.id as stateId, ag.codeName as agCodeName, agv.lsType, agv.lsKind, agv.stringValue, " 
				+ "agv.codeValue, agv.fileValue, agv.urlValue, agv.dateValue, " 
				+ "agv.clobValue, agv.operatorType, agv.operatorKind, agv.numericValue, " 
				+ "agv.sigFigs, agv.uncertainty, agv.numberOfReplicates, agv.uncertaintyType, " 
				+ "agv.unitType, agv.unitKind, agv.comments, agv.ignored, " + "agv.lsTransaction, agv.publicData) " 
				+ "FROM AnalysisGroupValue agv " + "join agv.lsState ags with ags.ignored = false " 
				+ "join ags.analysisGroup ag with ag.ignored = false " + "join ag.experiments exp with exp.ignored = false " 
				+ "where ags.lsType = :lsType AND ags.lsKind = :lsKind and agv.ignored = false " 
				+ "and exp.codeName = :experimentCode";
		EntityManager em = entityManager();
		TypedQuery<AnalysisGroupValueBaseDTO> q = em.createQuery(sqlQuery, AnalysisGroupValueBaseDTO.class);
		q.setParameter("lsType", lsType);
		q.setParameter("lsKind", lsKind);
		q.setParameter("experimentCode", experimentCode);
		return q;
	}

	public static TypedQuery<com.labsynch.labseer.dto.AnalysisGroupValueBaseDTO> findAnalysisGroupValueByLsTypeKindAndExptCodeAll(String lsType, String lsKind, String experimentCode) {
		String sqlQuery = "select new com.labsynch.labseer.dto.AnalysisGroupValueBaseDTO( " + "agv.id, ags.id as stateId, ag.codeName as agCodeName, agv.lsType, agv.lsKind, agv.stringValue, " +
				"agv.codeValue, agv.fileValue, agv.urlValue, agv.dateValue, " + "agv.clobValue, agv.operatorType, agv.operatorKind, agv.numericValue, " +
				"agv.sigFigs, agv.uncertainty, agv.numberOfReplicates, agv.uncertaintyType, " + "agv.unitType, agv.unitKind, agv.comments, agv.ignored, " + 
				"agv.lsTransaction, agv.publicData) " + 
				"FROM AnalysisGroupValue agv " + "join agv.lsState ags " + "join ags.analysisGroup ag " + 
				"join ag.experiments exp " + "where ags.lsType = :lsType AND ags.lsKind = :lsKind " + 
				"and exp.codeName = :experimentCode";
		EntityManager em = entityManager();
		TypedQuery<AnalysisGroupValueBaseDTO> q = em.createQuery(sqlQuery, AnalysisGroupValueBaseDTO.class);
		q.setParameter("lsType", lsType);
		q.setParameter("lsKind", lsKind);
		q.setParameter("experimentCode", experimentCode);
		return q;
	}

	public static TypedQuery<com.labsynch.labseer.dto.AnalysisGroupValueDTO> findAnalysisGroupValueDTO(String batchCode, boolean showOnlyPublicData) {
		String sqlQuery;
		if (showOnlyPublicData){
			sqlQuery = "select new com.labsynch.labseer.dto.AnalysisGroupValueDTO(agv.id, ags.id as stateId, expt.id as experimentId, prot.id as protocolId, expt.codeName, el.labelText as prefName, " 
					+ "agv.lsType as lsType, agv.lsKind as lsKind, " + "agv.stringValue as stringValue, agv.numericValue as numericValue, " 
					+ "agv2.codeValue AS testedLot, tl.labelText as geneId  " 
					+ " ) FROM AnalysisGroup ag, LsThing thing " 
					+ "JOIN ag.lsStates ags with ags.lsType = 'data' and ags.ignored = false " 
					+ "JOIN ags.lsValues agv with agv.lsKind != 'tested concentration' AND agv.lsKind != 'batch code' AND agv.lsKind != 'time' and agv.ignored = false " 
					+ "JOIN ags.lsValues agv2 with agv2.lsKind = 'batch code' and agv2.ignored = false " 
					+ "JOIN thing.lsLabels tl with tl.ignored = false and tl.lsType = 'name' and tl.ignored = false and tl.preferred = true " 
					+ "JOIN ag.experiments expt with expt.ignored = false "
					+ "JOIN expt.protocol prot "
					+ "JOIN expt.lsLabels el with el.lsType = 'name' and el.lsKind = 'experiment name' and el.preferred = true and el.ignored = false " 
					+ "WHERE ag.ignored = false AND thing.codeName = agv2.codeValue AND agv2.codeValue = :batchCode "
					+ "AND agv.publicData = :showOnlyPublicData " 
					;
		} else {
			sqlQuery = "select new com.labsynch.labseer.dto.AnalysisGroupValueDTO(agv.id, ags.id as stateId, expt.id as experimentId, prot.id as protocolId, expt.codeName, el.labelText as prefName, " 
					+ "agv.lsType as lsType, agv.lsKind as lsKind, " + "agv.stringValue as stringValue, agv.numericValue as numericValue, " 
					+ "agv2.codeValue AS testedLot, tl.labelText as geneId  " 
					+ " ) FROM AnalysisGroup ag, LsThing thing " 
					+ "JOIN ag.lsStates ags with ags.lsType = 'data' and ags.ignored = false " 
					+ "JOIN ags.lsValues agv with agv.lsKind != 'tested concentration' AND agv.lsKind != 'batch code' AND agv.lsKind != 'time' and agv.ignored = false " 
					+ "JOIN ags.lsValues agv2 with agv2.lsKind = 'batch code' and agv2.ignored = false " 
					+ "JOIN thing.lsLabels tl with tl.ignored = false and tl.lsType = 'name' and tl.ignored = false and tl.preferred = true " 
					+ "JOIN ag.experiments expt with expt.ignored = false "
					+ "JOIN expt.protocol prot "
					+ "JOIN expt.lsLabels el with el.lsType = 'name' and el.lsKind = 'experiment name' and el.preferred = true and el.ignored = false " 
					+ "WHERE ag.ignored = false AND thing.codeName = agv2.codeValue AND agv2.codeValue = :batchCode ";
		}
		EntityManager em = entityManager();
		TypedQuery<AnalysisGroupValueDTO> q = em.createQuery(sqlQuery, AnalysisGroupValueDTO.class);
		q.setParameter("batchCode", batchCode);
		if (showOnlyPublicData) q.setParameter("showOnlyPublicData", showOnlyPublicData);
		return q;
	}



	public static TypedQuery<com.labsynch.labseer.dto.AnalysisGroupValueDTO> findAnalysisGroupValueDTOCmpds(Set<java.lang.String> batchCodeList) {
		String sqlQuery = "select new com.labsynch.labseer.dto.AnalysisGroupValueDTO(agv.id, ags.id as stateId, expt.id as experimentId, prot.id as protocolId, expt.codeName, el.labelText as prefName, " 
				+ "agv.lsType as lsType, agv.lsKind as lsKind, " 
				+ "agv.stringValue as stringValue, agv.numericValue as numericValue, " 
				+ "agv2.codeValue AS testedLot, agv2.codeValue AS geneId " + " ) FROM AnalysisGroup ag " 
				+ "JOIN ag.lsStates ags with ags.lsType = 'data' " 
				+ "JOIN ags.lsValues agv with agv.lsKind != 'tested concentration' AND agv.lsKind != 'batch code' AND agv.lsKind != 'time' " 
				+ "JOIN ags.lsValues agv2 with agv2.lsKind = 'batch code' " 
				+ "JOIN ag.experiments expt with expt.ignored = false " 
				+ "JOIN expt.lsLabels el with el.lsType = 'name' and el.lsKind = 'experiment name' and el.preferred = true and el.ignored = false " 
				+ "JOIN expt.protocol prot "
				+ "WHERE ag.ignored = false and agv2.codeValue IN (:batchCodeList) ";
		EntityManager em = entityManager();
		TypedQuery<AnalysisGroupValueDTO> q = em.createQuery(sqlQuery, AnalysisGroupValueDTO.class);
		q.setParameter("batchCodeList", batchCodeList);
		logger.info("here is the query: " + sqlQuery);
		return q;
	}

	public static TypedQuery<com.labsynch.labseer.dto.AnalysisGroupValueDTO> findAnalysisGroupValueDTO(Set<java.lang.String> batchCodeList) {
		String sqlQuery = "select new com.labsynch.labseer.dto.AnalysisGroupValueDTO(agv.id, ags.id as stateId, expt.id as experimentId, prot.id as protocolId, expt.codeName, el.labelText as prefName, " 
				+ "agv.lsType as lsType, agv.lsKind as lsKind, " + "agv.stringValue as stringValue, agv.numericValue as numericValue, " 
				+ "agv2.codeValue AS testedLot, agv2.codeValue as geneId  " 
				+ " ) FROM AnalysisGroup ag"
//				+ ", LsThing thing " 
				+ "JOIN ag.lsStates ags with ags.lsType = 'data' and ags.ignored = false " 
				+ "JOIN ags.lsValues agv with agv.lsKind != 'tested concentration' AND agv.lsKind != 'batch code' AND agv.lsKind != 'time' and agv.ignored = false " 
				+ "JOIN ags.lsValues agv2 with agv2.lsKind = 'batch code' and agv2.ignored = false " 
//				+ "JOIN thing.lsLabels tl with tl.ignored = false and tl.lsType = 'name' and tl.ignored = false and tl.preferred = true " 
				+ "JOIN ag.experiments expt with expt.ignored = false " 
				+ "JOIN expt.lsLabels el with el.lsType = 'name' and el.lsKind = 'experiment name' and el.preferred = true and el.ignored = false " 
				+ "WHERE ag.ignored = false AND thing.codeName = agv2.codeValue AND agv2.codeValue IN (:batchCodeList) ";

		EntityManager em = entityManager();
		TypedQuery<AnalysisGroupValueDTO> q = em.createQuery(sqlQuery, AnalysisGroupValueDTO.class);
		q.setParameter("batchCodeList", batchCodeList);
		logger.info("here is the query: " + sqlQuery);
		return q;
	}

	public static TypedQuery<AnalysisGroupValueDTO> findAnalysisGroupValueDTO(Set<String> batchCodeList, boolean publicData) {
		String sqlQuery = "select new com.labsynch.labseer.dto.AnalysisGroupValueDTO(agv.id, ags.id as stateId, expt.id as experimentId, prot.id as protocolId, expt.codeName, el.labelText as prefName, " 
				+ "agv.lsType as lsType, agv.lsKind as lsKind, " + "agv.stringValue as stringValue, agv.numericValue as numericValue, " 
				+ "agv2.codeValue AS testedLot, agv2.codeValue as geneId  " 
				+ " ) FROM AnalysisGroup ag " 
				+ "JOIN ag.lsStates ags with ags.lsType = 'data' and ags.ignored = false " 
				+ "JOIN ags.lsValues agv with agv.lsKind != 'tested concentration' AND agv.lsKind != 'batch code' AND agv.lsKind != 'time' and agv.ignored = false " 
				+ "JOIN ags.lsValues agv2 with agv2.lsKind = 'batch code' and agv2.ignored = false " 
//				+ "LEFT OUTER JOIN LsThing thing with thing.codeName = agv2.codeValue "
//				+ "LEFT OUTER JOIN thing.lsLabels tl with tl.ignored = false and tl.lsType = 'name' and tl.ignored = false and tl.preferred = true  " 
				+ "JOIN ag.experiments expt with expt.ignored = false " 
				+ "JOIN expt.lsLabels el with el.lsType = 'name' and el.lsKind = 'experiment name' and el.preferred = true and el.ignored = false " 
				+ "WHERE ag.ignored = false AND agv2.codeValue IN (:batchCodeList) AND agv.publicData = :publicData  ";
		EntityManager em = entityManager();
		TypedQuery<AnalysisGroupValueDTO> q = em.createQuery(sqlQuery, AnalysisGroupValueDTO.class);
		q.setParameter("batchCodeList", batchCodeList);
		q.setParameter("publicData", publicData);
		logger.info("here is the query: " + sqlQuery);
		return q;
	}


	public static TypedQuery<com.labsynch.labseer.dto.AnalysisGroupValueDTO> findAnalysisGroupValueDTO(Set<java.lang.String> batchCodeList, Set<java.lang.String> experimentCodeList) {
		logger.debug("size for batchCodeList: " + batchCodeList.size());
		logger.debug("size for experimentCodeList: " + experimentCodeList.size());
		String sqlQuery = "select new com.labsynch.labseer.dto.AnalysisGroupValueDTO(agv.id, prot.id as protocolId, protLabel.labelText as protocolName, "
		+ "expt.id as experimentId, expt.codeName, el.labelText as prefName, agv.lsType as lsType, agv.lsKind as lsKind, "
				+ "agv.stringValue as stringValue, agv.numericValue as numericValue, agv.codeValue as codeValue, agv.dateValue as dateValue, agv.fileValue as fileValue, " 
				+ "agv2.codeValue AS testedLot "
				+ ", agv2.codeValue as geneId  " 
//				+ ", tl.labelText as geneId  " 
				+ ", agv.unitKind as resultUnit "
				+ ", agv.operatorKind as operator "
				+ ", agv.uncertainty as uncertainty "
				+ ", agv.uncertaintyType as uncertaintyUnit "
				+ ", agv2.concentration as testedConcentration "
				+ ", agv2.concUnit as testedConcentrationUnit "
				+ ", agv3.numericValue as testedTime "
				+ ", agv3.unitKind as testedTimeUnit "
				+ " ) FROM AnalysisGroup ag "
		+ "JOIN ag.lsStates ags with ags.ignored = false AND ags.lsType in ('data', 'metadata') " 
		+ "JOIN ags.lsValues agv with agv.lsKind NOT IN ('tested concentration', 'batch code', 'time') and agv.ignored = false "
		+ "JOIN ags.lsValues agv2 with agv2.lsKind = 'batch code' and agv2.ignored = false " 
		+ "LEFT OUTER JOIN ags.lsValues agv3 with agv3.lsKind = 'time' and agv3.ignored = false "
//		+ "LEFT OUTER JOIN LsThingLabel tl with agv2.codeValue = tl.labelText and tl.ignored = false and tl.preferred = true "
//		+ "LEFT OUTER JOIN tl.lsThing thing " 
		+ "JOIN ag.experiments expt with expt.ignored = false " 
		+ "JOIN expt.protocol prot with prot.ignored = false "
        + "JOIN prot.lsLabels protLabel with protLabel.ignored = false "
		+ "JOIN expt.lsLabels el with el.lsType = 'name' and el.lsKind = 'experiment name' and el.preferred = true and el.ignored = false " 
				+ "WHERE ag.ignored = false  "
//				+ "AND thing.codeName = agv2.codeValue "
				+ "AND agv2.codeValue IN (:batchCodeList) " + "AND expt.codeName IN  (:experimentCodeList) and expt.ignored = false";
		
		logger.debug("Query: "  + sqlQuery);
		EntityManager em = entityManager();
		TypedQuery<AnalysisGroupValueDTO> q = em.createQuery(sqlQuery, AnalysisGroupValueDTO.class);
		q.setParameter("batchCodeList", batchCodeList);
		q.setParameter("experimentCodeList", experimentCodeList);
		return q;
	}
	
	public static List<AnalysisGroupValueDTO> findAnalysisGroupValueDTO(Set<java.lang.String> batchCodeList, Set<java.lang.String> experimentCodeList, boolean publicData) {
		logger.debug("size of batchCodeList: " + batchCodeList.size());
		logger.debug("size of experimentCodeList: " + experimentCodeList.size());
		
		List<AnalysisGroupValueDTO> results;
		
		if (batchCodeList.size() > 500 || experimentCodeList.size() > 500){
			results = findAnalysisGroupValueDTOByTempTables(batchCodeList, experimentCodeList, publicData);
		} else {
			results = findAnalysisGroupValueDTOByInClause(batchCodeList, experimentCodeList, publicData);
		}
		
		return results;
	}

	public static List<AnalysisGroupValueDTO> findAnalysisGroupValueDTOByInClause(Set<java.lang.String> batchCodeList, Set<java.lang.String> experimentCodeList, boolean publicData) {
			
		String sqlQuery = "select new com.labsynch.labseer.dto.AnalysisGroupValueDTO( agv.id, prot.id as protocolId, protLabel.labelText as protocolName, " 
		+ "expt.id as experimentId, expt.codeName, el.labelText as prefName, agv.lsType as lsType, agv.lsKind as lsKind, " 
				+ "agv.stringValue as stringValue, agv.numericValue as numericValue, agv.codeValue as codeValue, agv.dateValue as dateValue, agv.fileValue as fileValue, " 
				+ "agv2.codeValue AS testedLot, agv2.codeValue as geneId  " 
				+ ", agv.unitKind as resultUnit "
				+ ", agv.operatorKind as operator "
				+ ", agv.uncertainty as uncertainty "
				+ ", agv.uncertaintyType as uncertaintyUnit "
				+ ", agv2.concentration as testedConcentration "
				+ ", agv2.concUnit as testedConcentrationUnit "
				+ ", agv3.numericValue as testedTime "
				+ ", agv3.unitKind as testedTimeUnit "
		+ " ) FROM AnalysisGroup ag  "
//						+ "JOIN  LsThing thing " 
		+ "JOIN ag.lsStates ags with ags.lsType IN ('data', 'metadata') and ags.ignored = false " 
		+ "JOIN ags.lsValues agv with agv.lsKind NOT IN ('tested concentration', 'batch code', 'time') and agv.ignored = false " 
		+ "JOIN ags.lsValues agv2 with agv2.lsKind = 'batch code' and agv2.ignored = false "
		+ "LEFT OUTER JOIN ags.lsValues agv3 with agv3.lsKind = 'time' and agv3.ignored = false "
//		+ "JOIN thing.lsLabels tl with tl.ignored = false and tl.lsType = 'name' and tl.lsKind = 'Entrez Gene ID' and tl.ignored = false and tl.preferred = true " 
		+ "JOIN ag.experiments expt with expt.ignored = false " 
		+ "LEFT OUTER JOIN expt.protocol prot with prot.ignored = false "
		+ "JOIN expt.protocol prot with prot.ignored = false "
        + "JOIN prot.lsLabels protLabel with protLabel.ignored = false "
		+ "LEFT OUTER JOIN expt.lsLabels el with el.lsType = 'name' and el.lsKind = 'experiment name' and el.preferred = true and el.ignored = false " 
//        + "JOIN TempSelectTable tst with tst.lsTransaction = :transactionId "
		+ "WHERE ag.ignored = false "
//				+ "AND thing.codeName = agv2.codeValue AND thing.lsType = 'gene' "
//				+ "and thing.lsKind = 'entrez gene' "
		+ "AND agv2.codeValue IN (:batchCodeList) AND agv.publicData = :publicData " 
		+ "AND expt.codeName IN (:experimentCodeList) and expt.ignored = false";
		
		
		logger.debug("query sql: " + sqlQuery);

		EntityManager em = entityManager();
		TypedQuery<AnalysisGroupValueDTO> q = em.createQuery(sqlQuery, AnalysisGroupValueDTO.class);
		q.setParameter("batchCodeList", batchCodeList);
		q.setParameter("experimentCodeList", experimentCodeList);
		q.setParameter("publicData", publicData);
		
		List<AnalysisGroupValueDTO> results = q.getResultList();
		
		return results;
	}
	
	public static List<AnalysisGroupValueDTO> findAnalysisGroupValueDTOByTempTables(Set<java.lang.String> batchCodeList, Set<java.lang.String> experimentCodeList, boolean publicData) {
		
		Date recordedDate = new Date();
		String recordedBy = "system";
		long batchTransactionId = TempSelectTable.saveStrings(batchCodeList,  recordedBy, recordedDate);
		long expTransactionId = TempSelectTable.saveStrings(experimentCodeList,  recordedBy, recordedDate);

		String sqlQuery = "select new com.labsynch.labseer.dto.AnalysisGroupValueDTO( agv.id, prot.id as protocolId, protLabel.labelText as protocolName, " 
		+ "expt.id as experimentId, expt.codeName, el.labelText as prefName, agv.lsType as lsType, agv.lsKind as lsKind, " 
				+ "agv.stringValue as stringValue, agv.numericValue as numericValue, agv.codeValue as codeValue, agv.dateValue as dateValue, agv.fileValue as fileValue, " 
				+ "agv2.codeValue AS testedLot, agv2.codeValue as geneId  " 
				+ ", agv.unitKind as resultUnit "
				+ ", agv.operatorKind as operator "
				+ ", agv.uncertainty as uncertainty "
				+ ", agv.uncertaintyType as uncertaintyUnit "
				+ ", agv2.concentration as testedConcentration "
				+ ", agv2.concUnit as testedConcentrationUnit "
				+ ", agv3.numericValue as testedTime "
				+ ", agv3.unitKind as testedTimeUnit "
		+ " ) FROM AnalysisGroup ag, TempSelectTable tst, TempSelectTable tst2 "
//						+ "JOIN  LsThing thing " 
		+ "JOIN ag.lsStates ags with ags.lsType IN ('data', 'metadata') and ags.ignored = false " 
		+ "JOIN ags.lsValues agv with agv.lsKind NOT IN ('tested concentration', 'batch code', 'time') and agv.ignored = false " 
		+ "JOIN ags.lsValues agv2 with agv2.lsKind = 'batch code' and agv2.ignored = false "
		+ "LEFT OUTER JOIN ags.lsValues agv3 with agv3.lsKind = 'time' and agv3.ignored = false "
//		+ "JOIN thing.lsLabels tl with tl.ignored = false and tl.lsType = 'name' and tl.lsKind = 'Entrez Gene ID' and tl.ignored = false and tl.preferred = true " 
		+ "JOIN ag.experiments expt with expt.ignored = false " 
		+ "LEFT OUTER JOIN expt.protocol prot with prot.ignored = false "
		+ "JOIN expt.protocol prot with prot.ignored = false "
        + "JOIN prot.lsLabels protLabel with protLabel.ignored = false "
		+ "LEFT OUTER JOIN expt.lsLabels el with el.lsType = 'name' and el.lsKind = 'experiment name' and el.preferred = true and el.ignored = false " 
//        + "JOIN TempSelectTable tst with tst.lsTransaction = :transactionId "
		+ "WHERE ag.ignored = false "
//				+ "AND thing.codeName = agv2.codeValue AND thing.lsType = 'gene' "
//				+ "and thing.lsKind = 'entrez gene' "
		+ "AND tst.lsTransaction = :batchTransactionId "
		+ "AND tst2.lsTransaction = :expTransactionId "
		+ "AND agv2.codeValue = tst.stringVar AND agv.publicData = :publicData " 
		+ "AND expt.codeName = tst2.stringVar and expt.ignored = false";
		
		
		logger.debug("query sql: " + sqlQuery);

		EntityManager em = entityManager();
		TypedQuery<AnalysisGroupValueDTO> q = em.createQuery(sqlQuery, AnalysisGroupValueDTO.class);
		q.setParameter("batchTransactionId", batchTransactionId);
		q.setParameter("expTransactionId", expTransactionId);
		q.setParameter("publicData", publicData);
		
		List<AnalysisGroupValueDTO> results = q.getResultList();
		
		int numberOfEntries = TempSelectTable.deleteTempSelectTableEntries(expTransactionId);
		int numberOfBatchEntries = TempSelectTable.deleteTempSelectTableEntries(batchTransactionId);
		logger.info("Deleted number of batch entries: " + numberOfBatchEntries);
		logger.info("Deleted number of experiemnt entries: " + numberOfEntries);
		
		return results;
	}
	
	public static TypedQuery<com.labsynch.labseer.dto.AnalysisGroupValueDTO> findAnalysisGroupValueDTOByExperiments(Set<java.lang.String> experimentCodeList) {
		String sqlQuery = "select new com.labsynch.labseer.dto.AnalysisGroupValueDTO(agv.id, ags.id as stateId, expt.id as experimentId, prot.id as protocolId, expt.codeName, el.labelText as prefName, " 
				+ "agv.lsType as lsType, agv.lsKind as lsKind, " + "agv.stringValue as stringValue, agv.numericValue as numericValue, " 
				+ "agv2.codeValue AS testedLot, tl.labelText as geneId " + " ) FROM AnalysisGroup ag, LsThing thing " 
				+ "JOIN ag.lsStates ags with ags.lsType = 'data' and ags.ignored = false " 
				+ "JOIN ags.lsValues agv with agv.lsKind != 'tested concentration' AND agv.lsKind != 'batch code' AND agv.lsKind != 'time' and agv.ignored = false " 
				+ "JOIN ags.lsValues agv2 with agv2.lsKind = 'batch code' and agv2.ignored = false " 
				+ "JOIN thing.lsLabels tl with tl.ignored = false and tl.ignored = false and tl.preferred = true " 
				+ "JOIN ag.experiments expt with expt.ignored = false " 
				+ "JOIN expt.lsLabels el with el.lsType = 'name' and el.lsKind = 'experiment name' and el.preferred = true and el.ignored = false " 
				+ "JOIN expt.protocol prot "
				+ "WHERE ag.ignored = false " 
				+ "AND thing.codeName = agv2.codeValue " 
				+ "AND expt.codeName IN  (:experimentCodeList) ";
		logger.debug("query sql: " + sqlQuery);
		EntityManager em = entityManager();
		TypedQuery<AnalysisGroupValueDTO> q = em.createQuery(sqlQuery, AnalysisGroupValueDTO.class);
		q.setParameter("experimentCodeList", experimentCodeList);
		return q;
	}

	public static TypedQuery<com.labsynch.labseer.dto.AnalysisGroupValueDTO> findAnalysisGroupValueDTOByExperiments(Set<java.lang.String> experimentCodeList, boolean publicData) {
		String sqlQuery = "select new com.labsynch.labseer.dto.AnalysisGroupValueDTO(agv.id, ags.id as stateId, expt.id as experimentId, "
				+ "prot.id as protocolId, expt.codeName, el.labelText as prefName, " 
				+ "agv.lsType as lsType, agv.lsKind as lsKind, " + "agv.stringValue as stringValue, agv.numericValue as numericValue, " 
				+ "agv2.codeValue AS testedLot, tl.labelText as geneId " + " ) "
				+ "FROM AnalysisGroup ag, LsThing thing " 
				+ "JOIN ag.lsStates ags with ags.lsType = 'data' and ags.ignored = false " 
				+ "JOIN ags.lsValues agv with agv.lsKind != 'tested concentration' AND agv.lsKind != 'batch code' AND agv.lsKind != 'time' and agv.ignored = false " 
				+ "JOIN ags.lsValues agv2 with agv2.lsKind = 'batch code' and agv2.ignored = false " 
				+ "JOIN thing.lsLabels tl with tl.ignored = false and tl.lsType = 'name' and tl.lsKind = 'Entrez Gene ID' and tl.ignored = false and tl.preferred = true " 
				+ "JOIN ag.experiments expt with expt.ignored = false " + "JOIN expt.lsLabels el with el.lsType = 'name' "
				+ "and el.lsKind = 'experiment name' and el.preferred = true and el.ignored = false " 
				+ "JOIN expt.protocol prot "
				+ "WHERE ag.ignored = false " + "AND thing.codeName = agv2.codeValue AND agv.publicData = :publicData " 
				+ "AND expt.codeName IN  (:experimentCodeList) ";
		logger.debug("query sql: " + sqlQuery);
		EntityManager em = entityManager();
		TypedQuery<AnalysisGroupValueDTO> q = em.createQuery(sqlQuery, AnalysisGroupValueDTO.class);
		q.setParameter("experimentCodeList", experimentCodeList);
		q.setParameter("publicData", publicData);
		return q;
	}

	public static TypedQuery<com.labsynch.labseer.dto.ValueTypeKindDTO> findAnalysisGroupValueTypeKindDTO(Collection<java.lang.String> exptCodeList) {
		String sqlQuery = "select new com.labsynch.labseer.dto.ValueTypeKindDTO(agv.lsType as lsType, agv.lsKind as lsKind) " 
				+ "FROM AnalysisGroupValue agv " + "join agv.lsState ags " 
				+ "join ags.analysisGroup ag " 
				+ "join ag.experiments exp " 
				+ "where exp.codeName in (:exptCodeList) " 
				+ "AND agv.lsType IN ('stringValue', 'numericValue') " 
				+ "and ags.lsType = 'data' and agv.ignored = false and ags.ignored = false and ag.ignored = false " 
				+ "group by agv.lsType, agv.lsKind";
		EntityManager em = entityManager();
		TypedQuery<ValueTypeKindDTO> q = em.createQuery(sqlQuery, ValueTypeKindDTO.class);
		q.setParameter("exptCodeList", exptCodeList);
		return q;
	}

	@Transactional
	public static TypedQuery<com.labsynch.labseer.dto.ValueTypeKindDTO> findAnalysisGroupValueTypeKindDTO(String exptCode) {
		String sqlQuery = "select new com.labsynch.labseer.dto.ValueTypeKindDTO(agv.lsType as lsType, agv.lsKind as lsKind) " 
				+ "FROM AnalysisGroupValue agv " 
				+ "JOIN agv.lsState ags " 
				+ "JOIN ags.analysisGroup ag " 
				+ "JOIN ag.experiments exp " 
				+ "where exp.codeName = :exptCode " 
				+ "AND agv.lsType IN ('stringValue', 'numericValue', 'dateValue', 'fileValue', 'urlValue') " 
				+ "AND ags.lsType = 'data' and agv.ignored = false and ags.ignored = false and ag.ignored = false " 
				+ "group by agv.lsType, agv.lsKind";
		EntityManager em = entityManager();
		TypedQuery<ValueTypeKindDTO> q = em.createQuery(sqlQuery, ValueTypeKindDTO.class);
		q.setParameter("exptCode", exptCode);
		return q;
	}

	@Transactional
	public static TypedQuery<com.labsynch.labseer.dto.ValueTypeKindDTO> findAnalysisGroupValueTypeKindDTO(String exptCode, boolean showOnlyPublicData) {
		String sqlQuery;
		if (showOnlyPublicData){
			sqlQuery = "select new com.labsynch.labseer.dto.ValueTypeKindDTO(agv.lsType as lsType, agv.lsKind as lsKind) " 
					+ "FROM AnalysisGroupValue agv " 
					+ "join agv.lsState ags " 
					+ "join ags.analysisGroup ag " 
					+ "join ag.experiments exp " 
					+ "where exp.codeName = :exptCode " 
					+ "AND agv.publicData = :showOnlyPublicData " 
					+ "and ags.lsType = 'data' and agv.ignored = false and ags.ignored = false and ag.ignored = false " 
					+ "group by agv.lsType, agv.lsKind ORDER by agv.lsKind";

		} else {
			sqlQuery = "select new com.labsynch.labseer.dto.ValueTypeKindDTO(agv.lsType as lsType, agv.lsKind as lsKind) " 
					+ "FROM AnalysisGroupValue agv " 
					+ "join agv.lsState ags " 
					+ "join ags.analysisGroup ag " 
					+ "join ag.experiments exp " 
					+ "where exp.codeName = :exptCode " 
					+ "and ags.lsType = 'data' and agv.ignored = false and ags.ignored = false and ag.ignored = false " 
					+ "group by agv.lsType, agv.lsKind ORDER by agv.lsKind";			
		}

		EntityManager em = entityManager();
		TypedQuery<ValueTypeKindDTO> q = em.createQuery(sqlQuery, ValueTypeKindDTO.class);
		q.setParameter("exptCode", exptCode);
		if (showOnlyPublicData) q.setParameter("showOnlyPublicData", showOnlyPublicData);
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
		return new JSONSerializer()
				.include("lsState.analysisGroup")
				.exclude("*.class", "lsState.analysisGroup.experiment").transform(new ExcludeNulls(), void.class).serialize(this);
	}

	@Transactional
	public static String toJsonArray(Collection<com.labsynch.labseer.domain.AnalysisGroupValue> collection) {
		return new JSONSerializer()
				.exclude("*.class", "lsState.analysisGroup.experiment")
				.include("lsState.analysisGroup")
				.transform(new ExcludeNulls(), void.class)
				.serialize(collection);
	}

	@Transactional
	public static String toPrettyJsonArray(Collection<com.labsynch.labseer.domain.AnalysisGroupValue> collection) {
		return new JSONSerializer().exclude("*.class", "lsState.analysisGroup.experiment").transform(new ExcludeNulls(), void.class).prettyPrint(true).serialize(collection);
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

	public static TypedQuery<java.lang.String> findBatchCodedBySearchFilter(Collection<com.labsynch.labseer.dto.ExperimentFilterSearchDTO> searchFilters) {
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
		for (ExperimentFilterSearchDTO searchFilter : searchFilters) {
			Predicate predicate1 = criteriaBuilder.equal(agvRoot.<String>get("lsType"), searchFilter.getLsType());
			predicateList.add(predicate1);
			Predicate predicate2 = criteriaBuilder.equal(agvRoot.<String>get("lsKind"), searchFilter.getLsKind());
			predicateList.add(predicate2);
			Predicate predicate3 = criteriaBuilder.equal(agvRoot.<String>get("lsState").get("analysisGroup").get("experiment").get("codeName"), searchFilter.getCodeName());
			predicateList.add(predicate3);
			if (searchFilter.getLsType().equalsIgnoreCase("numericValue")) {
				Predicate predicate4 = null;
				if (searchFilter.getOperator().equalsIgnoreCase(">")) {
					predicate4 = criteriaBuilder.gt(agvRoot.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
				} else if (searchFilter.getOperator().equalsIgnoreCase("<")) {
					predicate4 = criteriaBuilder.lt(agvRoot.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
				} else if (searchFilter.getOperator().equalsIgnoreCase("=")) {
					predicate4 = criteriaBuilder.equal(agvRoot.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
				} else if (searchFilter.getOperator().equalsIgnoreCase("!=")) {
					predicate4 = criteriaBuilder.notEqual(agvRoot.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
				} else {
					predicate4 = criteriaBuilder.notEqual(agvRoot.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
				}
				Predicate predicate5 = criteriaBuilder.isNull(agvRoot.<BigDecimal>get("numericValue"));
				if (predicate4 != null) {
					Predicate predicate6 = criteriaBuilder.or(predicate5, predicate4);
					predicateList.add(predicate6);
				} else {
					Predicate predicate6 = criteriaBuilder.or(predicate5);
					predicateList.add(predicate6);
				}
			} else if (searchFilter.getLsType().equalsIgnoreCase("stringValue")) {
				Predicate predicate4 = null;
				if (searchFilter.getOperator().equalsIgnoreCase("equals")) {
					predicate4 = criteriaBuilder.equal(agvRoot.<String>get("stringValue"), searchFilter.getFilterValue());
				} else if (searchFilter.getOperator().equalsIgnoreCase("contains")) {
					predicate4 = criteriaBuilder.like(agvRoot.<String>get("stringValue"), "%" + searchFilter.getFilterValue() + "%");
				}
				Predicate predicate5 = criteriaBuilder.isNull(agvRoot.<String>get("stringValue"));
				if (predicate4 != null) {
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


	public static TypedQuery<java.lang.String> findBatchCodeBySearchFilters(Set<java.lang.String> batchCodeList, Set<java.lang.String> experimentCodeList, 
			Set<com.labsynch.labseer.dto.ExperimentFilterSearchDTO> searchFilters,
			boolean includeHiddenData) {

		if (includeHiddenData){
			return findBatchCodeBySearchFilters(batchCodeList, experimentCodeList, searchFilters, false);
		} else {
			return findBatchCodeBySearchFilters(batchCodeList, experimentCodeList, searchFilters, true);
		}
	}

	public static TypedQuery<java.lang.String> findBatchCodeBySearchFilters(Set<java.lang.String> batchCodeList, Set<java.lang.String> experimentCodeList, 
			Set<com.labsynch.labseer.dto.ExperimentFilterSearchDTO> searchFilters, Boolean excludeHiddenData) {
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

		if (excludeHiddenData != null && excludeHiddenData == true) {
			Predicate predicateHidden = criteriaBuilder.equal(agvRoot.<Boolean>get("publicData"), true);
			predicateList.add(predicateHidden);
		}

		if (batchCodeList != null && batchCodeList.size() > 0) {
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
		if (experimentCodeList != null && experimentCodeList.size() > 0) {
			Expression<String> exp04 = agvRoot.<String>get("lsState").get("analysisGroup").get("experiment").get("codeName");
			Predicate predicate04 = exp04.in(experimentCodeList);
			predicateList.add(predicate04);
		} else {
			Predicate predicate04b = criteriaBuilder.equal(agvRoot.<Boolean>get("lsState").get("analysisGroup").get("experiment").get("ignored"), false);
			predicateList.add(predicate04b);
		}
		logger.debug("number of search filters: " + searchFilters.size());
		if (searchFilters != null && searchFilters.size() > 0) {
			for (ExperimentFilterSearchDTO searchFilter : searchFilters) {
				logger.debug(searchFilter.toJson());
				Root<AnalysisGroupValue> agvRootNew = criteria.from(AnalysisGroupValue.class);
				Predicate predicateNew = criteriaBuilder.equal(agvRoot2.<Long>get("lsState").get("id"), agvRootNew.<Long>get("lsState").get("id"));
				predicateList.add(predicateNew);

				if (excludeHiddenData != null && excludeHiddenData == true) {
					Predicate predicateHidden = criteriaBuilder.equal(agvRootNew.<Boolean>get("publicData"), true);
					predicateList.add(predicateHidden);
				}

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
				if (searchFilter.getLsType().equalsIgnoreCase("numericValue")) {
					Predicate predicate4 = null;
					if (searchFilter.getOperator().equalsIgnoreCase(">")) {
						predicate4 = criteriaBuilder.gt(agvRootNew.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
					} else if (searchFilter.getOperator().equalsIgnoreCase("<")) {
						predicate4 = criteriaBuilder.lt(agvRootNew.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
					} else if (searchFilter.getOperator().equalsIgnoreCase("=")) {
						predicate4 = criteriaBuilder.equal(agvRootNew.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
					} else if (searchFilter.getOperator().equalsIgnoreCase("!=")) {
						predicate4 = criteriaBuilder.notEqual(agvRoot.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
					} else {
						predicate4 = criteriaBuilder.notEqual(agvRoot.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
					}
					Predicate predicate5 = criteriaBuilder.isNull(agvRootNew.<BigDecimal>get("numericValue"));
					if (predicate4 != null) {
						Predicate predicate6 = criteriaBuilder.or(predicate5, predicate4);
						predicateList.add(predicate6);
					} else {
						Predicate predicate6 = criteriaBuilder.or(predicate5);
						predicateList.add(predicate6);
					}
				} else if (searchFilter.getLsType().equalsIgnoreCase("stringValue")) {
					logger.debug("stringValue search filter " + searchFilter.getFilterValue());
					Predicate predicate7 = null;
					if (searchFilter.getOperator().equalsIgnoreCase("equals")) {
						predicate7 = criteriaBuilder.like(agvRootNew.<String>get("stringValue"), searchFilter.getFilterValue());
					} else if (searchFilter.getOperator().equalsIgnoreCase("contains")) {
						predicate7 = criteriaBuilder.like(agvRootNew.<String>get("stringValue"), "%" + searchFilter.getFilterValue() + "%");
					}
					Predicate predicate8 = criteriaBuilder.isNull(agvRootNew.<String>get("stringValue"));
					if (predicate7 != null) {
						Predicate predicate9 = criteriaBuilder.or(predicate8, predicate7);
						predicateList.add(predicate9);
					} else {
						Predicate predicate9 = criteriaBuilder.or(predicate8);
						predicateList.add(predicate9);
					}
				}
			}
		}
		for (Predicate p : predicateList) {
			logger.debug("predicate: " + p.toString());
		}
		criteria.where(criteriaBuilder.and(predicateList.toArray(predicates)));
		TypedQuery<String> q = em.createQuery(criteria);
		return q;
	}

	//	public static TypedQuery<java.lang.String> findBatchCodeBySearchFilters(Set<java.lang.String> batchCodeList, Set<java.lang.String> experimentCodeList, Set<com.labsynch.labseer.dto.ExperimentFilterSearchDTO> searchFilters) {
	//		EntityManager em = AnalysisGroupValue.entityManager();
	//		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
	//		CriteriaQuery<String> criteria = criteriaBuilder.createQuery(String.class);
	//		Root<AnalysisGroupValue> agvRoot = criteria.from(AnalysisGroupValue.class);
	//		Root<AnalysisGroupValue> agvRoot2 = criteria.from(AnalysisGroupValue.class);
	//		criteria.distinct(true);
	//		criteria.select(agvRoot2.<String>get("codeValue"));
	//		Predicate[] predicates = new Predicate[0];
	//		List<Predicate> predicateList = new ArrayList<Predicate>();
	//		Predicate predicate00 = criteriaBuilder.equal(agvRoot.<Long>get("lsState").get("id"), agvRoot2.<Long>get("lsState").get("id"));
	//		predicateList.add(predicate00);
	//		if (batchCodeList != null && batchCodeList.size() > 0) {
	//			Predicate predicate01 = criteriaBuilder.equal(agvRoot2.<String>get("lsType"), "codeValue");
	//			predicateList.add(predicate01);
	//			Predicate predicate02 = criteriaBuilder.equal(agvRoot2.<String>get("lsKind"), "batch code");
	//			predicateList.add(predicate02);
	//			Expression<String> exp03 = agvRoot2.get("codeValue");
	//			Predicate predicate03 = exp03.in(batchCodeList);
	//			predicateList.add(predicate03);
	//		} else {
	//			logger.debug("no batchCodeList present");
	//		}
	//		if (experimentCodeList != null && experimentCodeList.size() > 0) {
	//			Expression<String> exp04 = agvRoot.<String>get("lsState").get("analysisGroup").get("experiment").get("codeName");
	//			Predicate predicate04 = exp04.in(experimentCodeList);
	//			predicateList.add(predicate04);
	//		} else {
	//			Predicate predicate04b = criteriaBuilder.equal(agvRoot.<Boolean>get("lsState").get("analysisGroup").get("experiment").get("ignored"), false);
	//			predicateList.add(predicate04b);
	//		}
	//		logger.debug("number of search filters: " + searchFilters.size());
	//		if (searchFilters != null && searchFilters.size() > 0) {
	//			for (ExperimentFilterSearchDTO searchFilter : searchFilters) {
	//				logger.debug(searchFilter.toJson());
	//				Root<AnalysisGroupValue> agvRootNew = criteria.from(AnalysisGroupValue.class);
	//				Predicate predicateNew = criteriaBuilder.equal(agvRoot2.<Long>get("lsState").get("id"), agvRootNew.<Long>get("lsState").get("id"));
	//				predicateList.add(predicateNew);
	//				Predicate predicate11 = criteriaBuilder.equal(agvRoot2.<String>get("lsType"), "codeValue");
	//				predicateList.add(predicate11);
	//				Predicate predicate12 = criteriaBuilder.equal(agvRoot2.<String>get("lsKind"), "batch code");
	//				predicateList.add(predicate12);
	//				Predicate predicate1 = criteriaBuilder.equal(agvRootNew.<String>get("lsType"), searchFilter.getLsType());
	//				predicateList.add(predicate1);
	//				Predicate predicate2 = criteriaBuilder.equal(agvRootNew.<String>get("lsKind"), searchFilter.getLsKind());
	//				predicateList.add(predicate2);
	//				Predicate predicate3 = criteriaBuilder.equal(agvRootNew.<String>get("lsState").get("analysisGroup").get("experiment").get("codeName"), searchFilter.getExperimentCode());
	//				predicateList.add(predicate3);
	//				if (searchFilter.getLsType().equalsIgnoreCase("numericValue")) {
	//					Predicate predicate4 = null;
	//					if (searchFilter.getOperator().equalsIgnoreCase(">")) {
	//						predicate4 = criteriaBuilder.gt(agvRootNew.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
	//					} else if (searchFilter.getOperator().equalsIgnoreCase("<")) {
	//						predicate4 = criteriaBuilder.lt(agvRootNew.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
	//					} else if (searchFilter.getOperator().equalsIgnoreCase("=")) {
	//						predicate4 = criteriaBuilder.equal(agvRootNew.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
	//					}
	//					Predicate predicate5 = criteriaBuilder.isNull(agvRootNew.<BigDecimal>get("numericValue"));
	//					if (predicate4 != null) {
	//						Predicate predicate6 = criteriaBuilder.or(predicate5, predicate4);
	//						predicateList.add(predicate6);
	//					} else {
	//						Predicate predicate6 = criteriaBuilder.or(predicate5);
	//						predicateList.add(predicate6);
	//					}
	//				} else if (searchFilter.getLsType().equalsIgnoreCase("stringValue")) {
	//					logger.debug("stringValue search filter " + searchFilter.getFilterValue());
	//					Predicate predicate7 = null;
	//					if (searchFilter.getOperator().equalsIgnoreCase("equals")) {
	//						predicate7 = criteriaBuilder.like(agvRootNew.<String>get("stringValue"), searchFilter.getFilterValue());
	//					} else if (searchFilter.getOperator().equalsIgnoreCase("contains")) {
	//						predicate7 = criteriaBuilder.like(agvRootNew.<String>get("stringValue"), "%" + searchFilter.getFilterValue() + "%");
	//					}
	//					Predicate predicate8 = criteriaBuilder.isNull(agvRootNew.<String>get("stringValue"));
	//					if (predicate7 != null) {
	//						Predicate predicate9 = criteriaBuilder.or(predicate8, predicate7);
	//						predicateList.add(predicate9);
	//					} else {
	//						Predicate predicate9 = criteriaBuilder.or(predicate8);
	//						predicateList.add(predicate9);
	//					}
	//				}
	//			}
	//		}
	//		for (Predicate p : predicateList) {
	//			logger.debug("predicate: " + p.toString());
	//		}
	//		criteria.where(criteriaBuilder.and(predicateList.toArray(predicates)));
	//		TypedQuery<String> q = em.createQuery(criteria);
	//		return q;
	//	}

	public static TypedQuery<java.lang.String> findBatchCodeBySearchFilter(Set<java.lang.String> batchCodeList, Set<java.lang.String> experimentCodeList, ExperimentFilterSearchDTO searchFilter, Boolean excludeHiddenData) {
		EntityManager em = AnalysisGroupValue.entityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<String> criteria = criteriaBuilder.createQuery(String.class);
		Root<AnalysisGroupValue> agvRoot = criteria.from(AnalysisGroupValue.class);
		Root<AnalysisGroupValue> agvRoot2 = criteria.from(AnalysisGroupValue.class);

		Join<AnalysisGroupValue, AnalysisGroupState> agState = agvRoot.join("lsState");
		Join<AnalysisGroupState, AnalysisGroup> ag = agState.join("analysisGroup");
		Join<AnalysisGroup, Experiment> experiments = ag.join("experiments");
		criteria.distinct(true);
		criteria.select(agvRoot2.<String>get("codeValue"));

		Predicate[] globalPredicates = new Predicate[0];
		List<Predicate> globalPredicateList = new ArrayList<Predicate>();
		
		List<String> batchCodes = new ArrayList<String>();
		batchCodes.addAll(batchCodeList);
		int batchCodeListSize = batchCodes.size();
		logger.debug("in findBatchCodeBySearchFilter --- size of batch code list: " + batchCodeListSize);
		logger.debug("incoming search filter" + searchFilter.toJson());

		for (int i = 0; i <= batchCodeListSize; i += PARAMETER_LIMIT){
			List<String> batchCodeSubList;
			if (batchCodeListSize > i + PARAMETER_LIMIT) {
				batchCodeSubList = batchCodes.subList(i, (i + PARAMETER_LIMIT));
			} else {
				batchCodeSubList = batchCodes.subList(i, batchCodeListSize);
			}

			Predicate[] predicates = new Predicate[0];
			List<Predicate> predicateList = new ArrayList<Predicate>();

			if (excludeHiddenData != null && excludeHiddenData == true) {
				Predicate predicateHidden = criteriaBuilder.equal(agvRoot.<Boolean>get("publicData"), true);
				predicateList.add(predicateHidden);
			}

			Predicate predicate00 = criteriaBuilder.equal(agvRoot.<Long>get("lsState").get("id"), agvRoot2.<Long>get("lsState").get("id"));
			predicateList.add(predicate00);
			Predicate predicate01 = criteriaBuilder.equal(agvRoot2.<String>get("lsType"), "codeValue");
			predicateList.add(predicate01);
			Predicate predicate02 = criteriaBuilder.equal(agvRoot2.<String>get("lsKind"), "batch code");
			predicateList.add(predicate02);
			if (batchCodeSubList != null && batchCodeSubList.size() > 0) {
				Expression<String> exp03 = agvRoot2.get("codeValue");
				Predicate predicate03 = exp03.in(batchCodeSubList);
				predicateList.add(predicate03);
			}
			if (experimentCodeList != null && experimentCodeList.size() > 0) {
				Expression<String> exp04 = experiments.<String>get("codeName");
				Predicate predicate04 = exp04.in(experimentCodeList);
				predicateList.add(predicate04);
			}
			if (searchFilter != null) {
				Predicate predicate1 = criteriaBuilder.equal(agvRoot.<String>get("lsType"), searchFilter.getLsType());
				predicateList.add(predicate1);
				Predicate predicate2 = criteriaBuilder.equal(agvRoot.<String>get("lsKind"), searchFilter.getLsKind());
				predicateList.add(predicate2);
				Predicate predicate3 = criteriaBuilder.equal(experiments.<String>get("codeName"), searchFilter.getExperimentCode());
				predicateList.add(predicate3);
				if (searchFilter.getLsType().equalsIgnoreCase("numericValue")) {
					logger.debug("setting lsType to " + searchFilter.getLsType());

					Predicate predicate4 = null;
					if (searchFilter.getOperator().equalsIgnoreCase(">")) {
						predicate4 = criteriaBuilder.gt(agvRoot.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
					} else if (searchFilter.getOperator().equalsIgnoreCase("<")) {
						predicate4 = criteriaBuilder.lt(agvRoot.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
					} else if (searchFilter.getOperator().equalsIgnoreCase("=")) {
						predicate4 = criteriaBuilder.equal(agvRoot.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
					} else if (searchFilter.getOperator().equalsIgnoreCase("!=")) {
						logger.debug("setting filter != " + searchFilter.getFilterValue());
						predicate4 = criteriaBuilder.notEqual(agvRoot.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
					} else {
						predicate4 = criteriaBuilder.notEqual(agvRoot.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
					}
					Predicate predicate5 = criteriaBuilder.isNull(agvRoot.<BigDecimal>get("numericValue"));
					if (predicate4 != null) {
						Predicate predicate6 = criteriaBuilder.or(predicate5, predicate4);
						predicateList.add(predicate6);
					} else {
						Predicate predicate6 = criteriaBuilder.or(predicate5);
						predicateList.add(predicate6);
					}
				} else if (searchFilter.getLsType().equalsIgnoreCase("stringValue")) {
					Predicate predicate4 = null;
					if (searchFilter.getOperator().equalsIgnoreCase("equals")) {
						predicate4 = criteriaBuilder.like(agvRoot.<String>get("stringValue"), searchFilter.getFilterValue());
					} else if (searchFilter.getOperator().equalsIgnoreCase("contains")) {
						predicate4 = criteriaBuilder.like(agvRoot.<String>get("stringValue"), "%" + searchFilter.getFilterValue() + "%");
					}
					Predicate predicate5 = criteriaBuilder.isNull(agvRoot.<String>get("stringValue"));
					if (predicate4 != null) {
						Predicate predicate6 = criteriaBuilder.or(predicate5, predicate4);
						predicateList.add(predicate6);
					} else {
						Predicate predicate6 = criteriaBuilder.or(predicate5);
						predicateList.add(predicate6);
					}
				}
			}
			//join all the predicates with AND
			predicates = predicateList.toArray(predicates);
			globalPredicateList.add(criteriaBuilder.and(predicates));
			
		}
		
		//join all the globalPredicates with OR
		globalPredicates = globalPredicateList.toArray(globalPredicates);
		criteria.where(criteriaBuilder.or(globalPredicates));
		TypedQuery<String> q = em.createQuery(criteria);
		logger.debug("unwrapped query string: ");
		logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
		return q;
	}

	public static TypedQuery<AnalysisGroupValue> findAGValuesBySearchFilter(Set<java.lang.String> batchCodeList, Set<java.lang.String> experimentCodeList, ExperimentFilterSearchDTO searchFilter, Boolean excludeHiddenData) {
		EntityManager em = AnalysisGroupValue.entityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<AnalysisGroupValue> criteria = criteriaBuilder.createQuery(AnalysisGroupValue.class);
		Root<AnalysisGroupValue> agvRoot = criteria.from(AnalysisGroupValue.class);
		Root<AnalysisGroupValue> agvRoot2 = criteria.from(AnalysisGroupValue.class);

		Join<AnalysisGroupValue, AnalysisGroupState> agState = agvRoot.join("lsState");
		Join<AnalysisGroupState, AnalysisGroup> ag = agState.join("analysisGroup");
		Join<AnalysisGroup, Experiment> experiments = ag.join("experiments");
		criteria.distinct(true);
		criteria.multiselect(agvRoot2.<String>get("codeValue"));
		Predicate[] globalPredicates = new Predicate[0];
		List<Predicate> globalPredicateList = new ArrayList<Predicate>();

		List<String> batchCodes = new ArrayList<String>();
		batchCodes.addAll(batchCodeList);
		int batchCodeListSize = batchCodes.size();

		for (int i = 0; i <= batchCodeListSize; i += PARAMETER_LIMIT){
			List<String> batchCodeSubList;
			if (batchCodeListSize > i + PARAMETER_LIMIT) {
				batchCodeSubList = batchCodes.subList(i, (i + PARAMETER_LIMIT));
			} else {
				batchCodeSubList = batchCodes.subList(i, batchCodeListSize);
			}


			Predicate[] predicates = new Predicate[0];
			List<Predicate> predicateList = new ArrayList<Predicate>();

			if (excludeHiddenData != null && excludeHiddenData == true) {
				Predicate predicateHidden = criteriaBuilder.equal(agvRoot.<Boolean>get("publicData"), true);
				predicateList.add(predicateHidden);
			}

			Predicate predicate00 = criteriaBuilder.equal(agvRoot.<Long>get("lsState").get("id"), agvRoot2.<Long>get("lsState").get("id"));
			predicateList.add(predicate00);
			Predicate predicate01 = criteriaBuilder.equal(agvRoot2.<String>get("lsType"), "codeValue");
			predicateList.add(predicate01);
			Predicate predicate02 = criteriaBuilder.equal(agvRoot2.<String>get("lsKind"), "batch code");
			predicateList.add(predicate02);
			if (batchCodeSubList != null && batchCodeSubList.size() > 0) {
				Expression<String> exp03 = agvRoot2.get("codeValue");
				Predicate predicate03 = exp03.in(batchCodeSubList);
				predicateList.add(predicate03);
			}
			if (experimentCodeList != null && experimentCodeList.size() > 0) {
				Expression<String> exp04 = experiments.<String>get("codeName");
				Predicate predicate04 = exp04.in(experimentCodeList);
				predicateList.add(predicate04);
			}
			if (searchFilter != null) {
				Predicate predicate1 = criteriaBuilder.equal(agvRoot.<String>get("lsType"), searchFilter.getLsType());
				predicateList.add(predicate1);
				Predicate predicate2 = criteriaBuilder.equal(agvRoot.<String>get("lsKind"), searchFilter.getLsKind());
				predicateList.add(predicate2);
				Predicate predicate3 = criteriaBuilder.equal(experiments.<String>get("codeName"), searchFilter.getExperimentCode());
				predicateList.add(predicate3);
				if (searchFilter.getLsType().equalsIgnoreCase("numericValue")) {
					Predicate predicate4 = null;
					if (searchFilter.getOperator().equalsIgnoreCase(">")) {
						predicate4 = criteriaBuilder.gt(agvRoot.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
					} else if (searchFilter.getOperator().equalsIgnoreCase("<")) {
						predicate4 = criteriaBuilder.lt(agvRoot.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
					} else if (searchFilter.getOperator().equalsIgnoreCase("=")) {
						predicate4 = criteriaBuilder.equal(agvRoot.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
					} else if (searchFilter.getOperator().equalsIgnoreCase("!=")) {
						predicate4 = criteriaBuilder.notEqual(agvRoot.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
					} else {
						predicate4 = criteriaBuilder.notEqual(agvRoot.<BigDecimal>get("numericValue"), new BigDecimal(searchFilter.getFilterValue()));
					}
					Predicate predicate5 = criteriaBuilder.isNull(agvRoot.<BigDecimal>get("numericValue"));
					if (predicate4 != null) {
						Predicate predicate6 = criteriaBuilder.or(predicate5, predicate4);
						predicateList.add(predicate6);
					} else {
						Predicate predicate6 = criteriaBuilder.or(predicate5);
						predicateList.add(predicate6);
					}
				} else if (searchFilter.getLsType().equalsIgnoreCase("stringValue")) {
					Predicate predicate4 = null;
					if (searchFilter.getOperator().equalsIgnoreCase("equals")) {
						predicate4 = criteriaBuilder.like(agvRoot.<String>get("stringValue"), searchFilter.getFilterValue());
					} else if (searchFilter.getOperator().equalsIgnoreCase("contains")) {
						predicate4 = criteriaBuilder.like(agvRoot.<String>get("stringValue"), "%" + searchFilter.getFilterValue() + "%");
					}
					Predicate predicate5 = criteriaBuilder.isNull(agvRoot.<String>get("stringValue"));
					if (predicate4 != null) {
						Predicate predicate6 = criteriaBuilder.or(predicate5, predicate4);
						predicateList.add(predicate6);
					} else {
						Predicate predicate6 = criteriaBuilder.or(predicate5);
						predicateList.add(predicate6);
					}
				}
			}						
			//join all the predicates with AND
			predicates = predicateList.toArray(predicates);
			globalPredicateList.add(criteriaBuilder.and(predicates));	
		}
		
		criteria.where(criteriaBuilder.and(globalPredicateList.toArray(globalPredicates)));
		TypedQuery<AnalysisGroupValue> q = em.createQuery(criteria);
		logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());

		return q;
	}



	public static TypedQuery<AnalysisGroupValue> findAnalysisGroupValuesByExptIDAndStateTypeKind(Long experimentId, 
			String stateType, 
			String stateKind) {
		if (stateType == null || stateType.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");

		EntityManager em = entityManager();
		String hsqlQuery = "SELECT agv FROM AnalysisGroupValue AS agv " +
				"JOIN agv.lsState evs " +
				"JOIN evs.analysisGroup ag " +
				"JOIN ag.experiments exp " +
				"WHERE evs.lsType = :stateType AND evs.lsKind = :stateKind AND evs.ignored IS NOT :ignored " +
				"AND agv.ignored IS NOT :ignored " +
				"AND ag.ignored IS NOT :ignored " +
				"AND exp.id = :experimentId ";
		TypedQuery<AnalysisGroupValue> q = em.createQuery(hsqlQuery, AnalysisGroupValue.class);
		q.setParameter("experimentId", experimentId);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("ignored", true);
		return q;
	}

	public static TypedQuery<AnalysisGroupValue> findAnalysisGroupValuesByAnalysisGroupIDAndStateTypeKind(Long analysisGroupId, String stateType, String stateKind) {
		if (stateType == null || stateType.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");

		EntityManager em = entityManager();
		String hsqlQuery = "SELECT agv from AnalysisGroupValue AS agv " +
				"JOIN agv.lsState ags " +
				"JOIN ags.analysisGroup ag " +
				"WHERE ags.lsType = :stateType " +
				"AND ags.ignored IS NOT :ignored " +
				"AND agv.ignored IS NOT :ignored " +
				"AND ags.lsKind = :stateKind " +
				"AND ag.id = :analysisGroupId AND ag.ignored IS NOT :ignored";
		TypedQuery<AnalysisGroupValue> q = em.createQuery(hsqlQuery, AnalysisGroupValue.class);
		q.setParameter("analysisGroupId", analysisGroupId);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("ignored", true);
		return q;
	}

	public static TypedQuery<AnalysisGroupValue> findAnalysisGroupValuesByExptIDAndStateTypeKindAndValueTypeKind(Long experimentId, String stateType,
			String stateKind, String valueType, String valueKind) {
		if (stateType == null || stateType.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
		if (valueType == null || valueType.length() == 0) throw new IllegalArgumentException("The valueType argument is required");
		if (valueKind == null || valueKind.length() == 0) throw new IllegalArgumentException("The valueKind argument is required");

		EntityManager em = entityManager();
		String hsqlQuery = "SELECT agv FROM AnalysisGroupValue AS agv " +
				"JOIN agv.lsState evs " +
				"JOIN evs.analysisGroup ag " +
				"JOIN ag.experiments exp " +
				"WHERE evs.lsType = :stateType AND evs.lsKind = :stateKind AND evs.ignored IS NOT :ignored " +
				"AND agv.lsType = :valueType AND agv.lsKind = :valueKind AND agv.ignored IS NOT :ignored " +
				"AND ag.ignored IS NOT :ignored " +
				"AND exp.id = :experimentId ";
		TypedQuery<AnalysisGroupValue> q = em.createQuery(hsqlQuery, AnalysisGroupValue.class);
		q.setParameter("experimentId", experimentId);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("valueType", valueType);
		q.setParameter("valueKind", valueKind);
		q.setParameter("ignored", true);
		return q;
	}
	
	public static TypedQuery<AnalysisGroupValue> findAnalysisGroupValuesByIdList(Set<Long> idList, Boolean onlyPublicData) {
		if (idList == null ) throw new IllegalArgumentException("The idList argument is required");
		if (onlyPublicData == null ) throw new IllegalArgumentException("The onlyPublicData argument is required");

		EntityManager em = entityManager();
		
		
		String hsqlQuery = "SELECT agv FROM AnalysisGroupValue AS agv " +
				"WHERE agv.id in (:idList) AND agv.ignored IS NOT :ignored ";
		
		String hsqlQueryPublic = "SELECT agv FROM AnalysisGroupValue AS agv " +
				"WHERE agv.id in (:idList) AND agv.ignored IS NOT :ignored " +
				"AND agv.publicData IS :publicData ";

		if (onlyPublicData){
			TypedQuery<AnalysisGroupValue> q = em.createQuery(hsqlQueryPublic, AnalysisGroupValue.class);
			q.setParameter("idList", idList);
			q.setParameter("ignored", true);
			q.setParameter("publicData", true);
			logger.debug("query running: hsqlQueryPublic   " + hsqlQueryPublic);
			return q;
			
		} else {
			TypedQuery<AnalysisGroupValue> q = em.createQuery(hsqlQuery, AnalysisGroupValue.class);
			q.setParameter("idList", idList);
			q.setParameter("ignored", true);
			logger.debug("query running: hsqlQuery   " + hsqlQuery);
			return q;			
		}
	}

	public static TypedQuery<AnalysisGroupValue> findAnalysisGroupValuesByAnalysisGroupIDAndStateTypeKindAndValueTypeKind(Long analysisGroupId, String stateType,
			String stateKind, String valueType, String valueKind) {

		if (stateType == null || stateType.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
		if (valueType == null || valueType.length() == 0) throw new IllegalArgumentException("The valueType argument is required");
		if (valueKind == null || valueKind.length() == 0) throw new IllegalArgumentException("The valueKind argument is required");

		EntityManager em = entityManager();
		String hsqlQuery = "SELECT agv FROM AnalysisGroupValue AS agv " +
				"JOIN agv.lsState ags " +
				"JOIN ags.analysisGroup ag " +
				"WHERE ags.lsType = :stateType AND ags.lsKind = :stateKind AND ags.ignored IS NOT :ignored " +
				"AND agv.lsType = :valueType AND agv.lsKind = :valueKind AND agv.ignored IS NOT :ignored " +
				"AND ag.ignored IS NOT :ignored " +
				"AND ag.id = :analysisGroupId ";
		TypedQuery<AnalysisGroupValue> q = em.createQuery(hsqlQuery, AnalysisGroupValue.class);
		q.setParameter("analysisGroupId", analysisGroupId);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("valueType", valueType);
		q.setParameter("valueKind", valueKind);
		q.setParameter("ignored", true);
		return q;
	}

	public static String[] getColumns(){
		String[] headerColumns = new String[] {
				"analysisGroupId",
				"analysisGroupCode",				
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
		//35 columns
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

}
