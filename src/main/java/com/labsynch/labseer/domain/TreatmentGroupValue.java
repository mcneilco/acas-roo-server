package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
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

import com.labsynch.labseer.dto.FlatThingCsvDTO;
import com.labsynch.labseer.dto.TreatmentGroupValueDTO;
import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findTreatmentGroupValuesByLsState", "findTreatmentGroupValuesByLsTransactionEquals",
		"findTreatmentGroupValuesByCodeValueEquals", "findTreatmentGroupValuesByIgnoredNotAndCodeValueEquals"})
public class TreatmentGroupValue extends AbstractValue {

	private static final Logger logger = LoggerFactory.getLogger(TreatmentGroupValue.class);

    @NotNull
    @ManyToOne
    @JoinColumn(name = "treatment_state_id")
    private TreatmentGroupState lsState;
    
    public TreatmentGroupValue(FlatThingCsvDTO inputDTO) {
    	this.setCodeValue(inputDTO.getCodeValue());
    	this.setCodeOrigin(inputDTO.getCodeOrigin());
    	this.setCodeType(inputDTO.getCodeType());
    	this.setCodeKind(inputDTO.getCodeKind());
    	this.setLsType(inputDTO.getValueType());
    	this.setLsKind(inputDTO.getValueKind());
    	this.setStringValue(inputDTO.getStringValue());
    	this.setClobValue(inputDTO.getClobValue());
    	this.setFileValue(inputDTO.getFileValue());
    	this.setUrlValue(inputDTO.getUrlValue());
    	this.setDateValue(inputDTO.getDateValue());
    	this.setOperatorKind(inputDTO.getOperatorKind());
    	this.setNumericValue(inputDTO.getNumericValue());
    	this.setFileValue(inputDTO.getFileValue());
    	this.setUncertainty(inputDTO.getUncertainty());
    	this.setUncertaintyType(inputDTO.getUncertaintyType());
    	this.setUnitKind(inputDTO.getUnitKind());
    	this.setConcentration(inputDTO.getConcentration());
    	this.setConcUnit(inputDTO.getConcUnit());
    	this.setNumberOfReplicates(inputDTO.getNumberOfReplicates());
        this.setRecordedBy(inputDTO.getRecordedBy());
        this.setRecordedDate(inputDTO.getRecordedDate());
        this.setLsTransaction(inputDTO.getLsTransaction());
        this.setModifiedBy(inputDTO.getModifiedBy());
        this.setModifiedDate(inputDTO.getModifiedDate());
        this.setComments(inputDTO.getComments());
        this.setIgnored(inputDTO.isIgnored());
        this.setPublicData(inputDTO.isPublicData());
        this.setSigFigs(inputDTO.getSigFigs());	
    }

	public TreatmentGroupValue() {
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

	public Long getTreatmentGroupId() {
		return this.lsState.getTreatmentGroup().getId();
	}
	
	//TODO: work out a different strategy with the many to many
	public Long getAnalysisGroupId() {
		return 0L;
	}
	

	public static TreatmentGroupValue create(TreatmentGroupValue treatmentGroupValue) {
    	TreatmentGroupValue newTreatmentGroupValue = new JSONDeserializer<TreatmentGroupValue>().use(null, TreatmentGroupValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(treatmentGroupValue.toJson(), 
        				new TreatmentGroupValue());	
    
        return newTreatmentGroupValue;
    }

	public static long countTreatmentGroupValues() {
        return entityManager().createQuery("SELECT COUNT(o) FROM TreatmentGroupValue o", Long.class).getSingleResult();
    }

	public static List<TreatmentGroupValue> findAllTreatmentGroupValues() {
        return entityManager().createQuery("SELECT o FROM TreatmentGroupValue o", TreatmentGroupValue.class).getResultList();
    }

	public static TreatmentGroupValue findTreatmentGroupValue(Long id) {
        if (id == null) return null;
        return entityManager().find(TreatmentGroupValue.class, id);
    }

	public static List<TreatmentGroupValue> findTreatmentGroupValueEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM TreatmentGroupValue o", TreatmentGroupValue.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

   
	public static TreatmentGroupValue update(TreatmentGroupValue treatmentGroupValue) {
		TreatmentGroupValue updatedTreatmentGroupValue = new JSONDeserializer<TreatmentGroupValue>().
		use(null, TreatmentGroupValue.class).
		use(BigDecimal.class, new CustomBigDecimalFactory()).
		deserializeInto(treatmentGroupValue.toJson(), TreatmentGroupValue.findTreatmentGroupValue(treatmentGroupValue.getId()));
		updatedTreatmentGroupValue.setModifiedDate(new Date());
		updatedTreatmentGroupValue.merge();
		return updatedTreatmentGroupValue;
	}
	
	@Transactional
    public TreatmentGroupValue merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        TreatmentGroupValue merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
	
	@Transactional
	public static List<Long> saveList(List<TreatmentGroupValue> entities) {
		logger.debug("saving the list of TreatmentGroupValues: " + entities.size());
		//return a list of ids
		List<Long> idList = new ArrayList<Long>();
		int batchSize = 50;

	    int imported = 0;
	    for (TreatmentGroupValue e : entities) {
	        e.persist();  // 1. Roo introduces this method via an ITD
	        idList.add(e.getId());
	         if (++imported % batchSize == 0) {
	        	 e.flush();  // 2. ... and this one
	        	 e.clear();  // 3. ... and this one
	        }
	    }
	    return idList;
	 }
		
	@Transactional
	public static String getTreatmentGroupValueCollectionJson(List<Long> idList) {
		Collection<TreatmentGroupValue> treatmentGroupValues = new HashSet<TreatmentGroupValue>();
		for (Long id : idList){
			TreatmentGroupValue query = TreatmentGroupValue.findTreatmentGroupValue(id);
			if (query != null) treatmentGroupValues.add(query);
		}
        return TreatmentGroupValue.toJsonArray(treatmentGroupValues);
    }
	
	@Transactional
	public static int deleteByExperimentID(Long experimentId) {
		if (experimentId == null) return 0;
		EntityManager em = TreatmentGroupValue.entityManager();
		String deleteSQL = "DELETE FROM TreatmentGroupValue oo WHERE id in (select o.id from TreatmentGroupValue o where o.lsState.treatmentGroup.analysisGroups.experiments.id = :experimentId)";
		Query q = em.createQuery(deleteSQL);
		q.setParameter("experimentId", experimentId);
		int numberOfDeletedEntities = q.executeUpdate();
		return numberOfDeletedEntities;
	}

    public static TreatmentGroupValue fromJsonToTreatmentGroupValue(String json) {
        return new JSONDeserializer<TreatmentGroupValue>().
        		use(null, TreatmentGroupValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
        
    public static Collection<TreatmentGroupValue> fromJsonArrayToTreatmentGroupValues(String json) {
        return new JSONDeserializer<List<TreatmentGroupValue>>().
        		use(null, ArrayList.class).
        		use("values", TreatmentGroupValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
    
    public static Collection<TreatmentGroupValue> fromJsonArrayToTreatmentGroupValues(Reader json) {
        return new JSONDeserializer<List<TreatmentGroupValue>>().
        		use(null, ArrayList.class).
        		use("values", TreatmentGroupValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
 
	@Transactional
    public String toJson() {
        return new JSONSerializer()
				.exclude("*.class", "lsState.treatmentGroup.analysisGroups.experiments")
				.include("lsState.treatmentGroup.analysisGroups")
				.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }
    
	@Transactional
    public static String toJsonArray(Collection<TreatmentGroupValue> collection) {
        return new JSONSerializer()
        		.exclude("*.class", "lsState.treatmentGroup.analysisGroups.experiments")
        		.include("lsState.treatmentGroup.analysisGroups")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }

	@Transactional
    public static String toJsonArrayStub(Collection<TreatmentGroupValue> collection) {
        return new JSONSerializer().exclude("*.class", "lsState")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }
	
	public static TypedQuery<TreatmentGroupValue> findTreatmentGroupValuesByExptIDAndStateTypeKind(Long experimentId, String stateType, String stateKind) {
		if (stateType == null || stateType.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
		
		EntityManager em = entityManager();
		String hsqlQuery = "SELECT tgv FROM TreatmentGroupValue AS tgv " +
		"JOIN tgv.lsState tvs " +
		"JOIN tvs.treatmentGroup tg " +
		"JOIN tg.analysisGroups ag " +
		"JOIN ag.experiments exp " +
		"WHERE tvs.lsType = :stateType AND tvs.lsKind = :stateKind AND tvs.ignored IS NOT :ignored " +
		"AND tgv.ignored IS NOT :ignored " +
		"AND tg.ignored IS NOT :ignored " +
		"AND ag.ignored IS NOT :ignored " +
		"AND exp.id = :experimentId ";
		TypedQuery<TreatmentGroupValue> q = em.createQuery(hsqlQuery, TreatmentGroupValue.class);
		q.setParameter("experimentId", experimentId);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("ignored", true);
		return q;
	}
	
	public static TypedQuery<TreatmentGroupValue> findTreatmentGroupValuesByAnalysisGroupIDAndStateTypeKind(Long analysisGroupId, String stateType, String stateKind) {
		if (stateType == null || stateType.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
		
		EntityManager em = entityManager();
		String hsqlQuery = "SELECT tgv FROM TreatmentGroupValue AS tgv " +
		"JOIN tgv.lsState tvs " +
		"JOIN tvs.treatmentGroup tg " +
		"JOIN tg.analysisGroups ag " +
		"WHERE tvs.lsType = :stateType AND tvs.lsKind = :stateKind AND tvs.ignored IS NOT :ignored " +
		"AND tgv.ignored IS NOT :ignored " +
		"AND tg.ignored IS NOT :ignored " +
		"AND ag.ignored IS NOT :ignored " +
		"AND ag.id = :analysisGroupId ";
		TypedQuery<TreatmentGroupValue> q = em.createQuery(hsqlQuery, TreatmentGroupValue.class);
		q.setParameter("analysisGroupId", analysisGroupId);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("ignored", true);
		return q;
	}
	
	public static TypedQuery<TreatmentGroupValue> findTreatmentGroupValuesByExptIDAndStateTypeKindAndValueTypeKind(Long experimentId, String stateType, String stateKind,
			String valueType, String valueKind) {
		if (stateType == null || stateType.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
		if (valueType == null || valueType.length() == 0) throw new IllegalArgumentException("The valueType argument is required");
		if (valueKind == null || valueKind.length() == 0) throw new IllegalArgumentException("The valueKind argument is required");
		
		EntityManager em = entityManager();
		String hsqlQuery = "SELECT tgv FROM TreatmentGroupValue AS tgv " +
				"JOIN tgv.lsState tvs " +
				"JOIN tvs.treatmentGroup tg " +
				"JOIN tg.analysisGroups ag " +
				"JOIN ag.experiments exp " +
				"WHERE tvs.lsType = :stateType AND tvs.lsKind = :stateKind AND tvs.ignored IS NOT :ignored " +
				"AND tgv.lsType = :valueType AND tgv.lsKind = :valueKind AND tgv.ignored IS NOT :ignored " +
				"AND tg.ignored IS NOT :ignored " +
				"AND ag.ignored IS NOT :ignored " +
				"AND exp.id = :experimentId ";
		TypedQuery<TreatmentGroupValue> q = em.createQuery(hsqlQuery, TreatmentGroupValue.class);
		q.setParameter("experimentId", experimentId);
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
				"treatmentGroupId",
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

	public Collection<TreatmentGroupValueDTO> makeDTOsByAnalysisGroupIds() {
		Collection<TreatmentGroupValueDTO> treatmentGroupValueDTOs = new HashSet<TreatmentGroupValueDTO>();
		Collection<AnalysisGroup> analysisGroups = TreatmentGroup.findTreatmentGroup(this.getTreatmentGroupId()).getAnalysisGroups();
		for (AnalysisGroup analysisGroup: analysisGroups) {
			TreatmentGroupValueDTO treatmentGroupValueDTO = new TreatmentGroupValueDTO(this);
			treatmentGroupValueDTO.setAnalysisGroupId(analysisGroup.getId());
			treatmentGroupValueDTOs.add(treatmentGroupValueDTO);
		}
		
		return treatmentGroupValueDTOs;
	}

	public static TypedQuery<TreatmentGroupValue> findTreatmentGroupValuesByTreatmentGroupIDAndStateTypeKindAndValueTypeKind(
			Long treatmentGroupId, String stateType, String stateKind,
			String valueType, String valueKind) {
		if (stateType == null || stateType.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
		if (valueType == null || valueType.length() == 0) throw new IllegalArgumentException("The valueType argument is required");
		if (valueKind == null || valueKind.length() == 0) throw new IllegalArgumentException("The valueKind argument is required");
		
		EntityManager em = entityManager();
		String hsqlQuery = "SELECT tgv FROM TreatmentGroupValue AS tgv " +
				"JOIN tgv.lsState tgs " +
				"JOIN tgs.treatmentGroup tg " +
				"WHERE tgs.lsType = :stateType AND tgs.lsKind = :stateKind AND tgs.ignored IS NOT :ignored " +
				"AND tgv.lsType = :valueType AND tgv.lsKind = :valueKind AND tgv.ignored IS NOT :ignored " +
				"AND tg.ignored IS NOT :ignored " +
				"AND tg.id = :treatmentGroupId ";
		TypedQuery<TreatmentGroupValue> q = em.createQuery(hsqlQuery, TreatmentGroupValue.class);
		q.setParameter("treatmentGroupId", treatmentGroupId);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("valueType", valueType);
		q.setParameter("valueKind", valueKind);
		q.setParameter("ignored", true);
		return q;
	}


}
