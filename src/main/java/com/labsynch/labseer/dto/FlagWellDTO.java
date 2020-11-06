package com.labsynch.labseer.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;

import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.domain.TreatmentGroupState;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.utils.StatCalc;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class FlagWellDTO {
	
	private static final Logger logger = LoggerFactory.getLogger(FlagWellDTO.class);
	
	private Long responseSubjectValueId;
	private String recordedBy;
	private Long lsTransaction;
	private String algorithmFlagStatus;
	private String algorithmFlagObservation;
	private String algorithmFlagCause;
	private String algorithmFlagComment;
	private String preprocessFlagStatus;
	private String preprocessFlagObservation;
	private String preprocessFlagCause;
	private String preprocessFlagComment;
	private String userFlagStatus;
	private String userFlagObservation;
	private String userFlagCause;
	private String userFlagComment;
	
	public FlagWellDTO(){
		//empty constructor
	}
	
	public FlagWellDTO(Long responseSubjectValueId, String recordedBy, Long lsTransaction, Map<String, String> flagMap) {
		this.responseSubjectValueId = responseSubjectValueId;
		this.recordedBy = recordedBy;
		this.lsTransaction = lsTransaction;
		this.algorithmFlagStatus = flagMap.get("algorithmFlagStatus");
		this.algorithmFlagObservation = flagMap.get("algorithmFlagObservation");
		this.algorithmFlagCause = flagMap.get("algorithmFlagCause");
		this.algorithmFlagComment = flagMap.get("userFlagComment");
		this.preprocessFlagStatus = flagMap.get("preprocessFlagStatus");
		this.preprocessFlagObservation = flagMap.get("preprocessFlagObservation");
		this.preprocessFlagCause = flagMap.get("preprocessFlagCause");
		this.preprocessFlagComment = flagMap.get("userFlagComment");
		this.userFlagStatus = flagMap.get("userFlagStatus");
		this.userFlagObservation = flagMap.get("userFlagObservation");
		this.userFlagCause = flagMap.get("userFlagCause");
		this.userFlagComment = flagMap.get("userFlagComment");
	}

	public static String[] getColumns(){
		String[] headerColumns = new String[] {
				"responseSubjectValueId",
				"recordedBy",
				"lsTransaction",
				"algorithmFlagStatus",
				"algorithmFlagObservation",
				"algorithmFlagCause",
				"algorithmFlagComment",
				"preprocessFlagStatus",
				"preprocessFlagObservation",
				"preprocessFlagCause",
				"preprocessFlagComment",
				"userFlagStatus",
				"userFlagObservation",
				"userFlagCause",
				"userFlagComment"
				};

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
				new Optional()
		};

		return processors;
	}

	@Transactional
	public static void updateWellFlags( Collection<FlagWellDTO> flagWellDTOs) {
		Collection<TreatmentGroup> allTreatmentGroups = new HashSet<TreatmentGroup>();
		String recordedBy = null;
		Long lsTransaction = null;
		for (FlagWellDTO flagWellDTO : flagWellDTOs) {
			if (flagWellDTO.getResponseSubjectValueId() == null) logger.error("FIELD MISSING: responseSubjectValueId");
			if (flagWellDTO.getRecordedBy() == null) logger.error("FIELD MISSING: recordedBy");
			recordedBy = flagWellDTO.getRecordedBy();
			lsTransaction = flagWellDTO.getLsTransaction();
			Subject subject = findSubject(flagWellDTO.getResponseSubjectValueId());
			Collection<TreatmentGroup> treatmentGroups = subject.getTreatmentGroups();
			allTreatmentGroups.addAll(treatmentGroups);
			if ( !(flagWellDTO.getAlgorithmFlagStatus()==null) || !(flagWellDTO.getAlgorithmFlagObservation()==null) || !(flagWellDTO.getAlgorithmFlagCause()==null) || !(flagWellDTO.getAlgorithmFlagComment()==null)) {
				logger.debug("Change in algorithm flags detected. Attempting to ignore old state.");
				try {
					SubjectState oldAlgorithmFlagState = findAlgorithmFlagState(subject).getSingleResult();
					oldAlgorithmFlagState.setIgnored(true);
					oldAlgorithmFlagState.merge();
//					oldAlgorithmFlagState.flush();
					logger.debug("Old algorithm flag state ignored.");
				}catch(EmptyResultDataAccessException e) {
					logger.debug("Old state of typekind data/auto flag not found for Subject " + subject.getCodeName() + " , creating new one");
				}
				SubjectState newAlgorithmState = createWellFlagState(subject.getId(), "data", "auto flag", recordedBy, lsTransaction);
				saveWellFlags(newAlgorithmState, flagWellDTO);
			}
			if ( !(flagWellDTO.getPreprocessFlagStatus()==null) || !(flagWellDTO.getPreprocessFlagObservation()==null) || !(flagWellDTO.getPreprocessFlagCause()==null) || !(flagWellDTO.getPreprocessFlagComment()==null)) {
				logger.debug("Change in preprocess flags detected. Attempting to ignore old state.");
				try {
					SubjectState oldPreprocessFlagState = findPreprocessFlagState(subject).getSingleResult();
					oldPreprocessFlagState.setIgnored(true);
					oldPreprocessFlagState.merge();
//					oldPreprocessFlagState.flush();
					logger.debug("Old preprocess flag state ignored.");
				}catch(EmptyResultDataAccessException e) {
					logger.debug("Old state of typekind data/preprocess flag not found for Subject " + subject.getCodeName() + " , creating new one");
				}
				SubjectState newPreprocessState = createWellFlagState(subject.getId(), "data", "preprocess flag", recordedBy, lsTransaction);
				saveWellFlags(newPreprocessState, flagWellDTO);
			}
			if ( !(flagWellDTO.getUserFlagStatus()==null) || !(flagWellDTO.getUserFlagObservation()==null) || !(flagWellDTO.getUserFlagCause()==null) || !(flagWellDTO.getUserFlagComment()==null)) {
				logger.debug("Change in user flags detected. Attempting to ignore old state.");
				try {
					SubjectState oldUserFlagState = findUserFlagState(subject).getSingleResult();
					oldUserFlagState.setIgnored(true);
					oldUserFlagState.merge();
//					oldUserFlagState.flush();
					logger.debug("Old user flag state ignored.");
				}catch(EmptyResultDataAccessException e) {
					logger.debug("Old state of typekind data/user flag not found for Subject " + subject.getCodeName() + " , creating new one");
				}
				SubjectState newUserState = createWellFlagState(subject.getId(), "data", "user flag", recordedBy, lsTransaction);
				saveWellFlags(newUserState, flagWellDTO);
			}
		}
		//Then go update all of the aggregate values in the treatment groups
		for (TreatmentGroup treatmentGroup: allTreatmentGroups){
				Collection<SubjectValue> notKONumericValueSubjectValues = findNotKONumericValueSubjectValues(treatmentGroup);
				Map<String, Collection<SubjectValue>> numericSubjectValueMapByLsKind = new HashMap<String, Collection<SubjectValue>>();
				for (SubjectValue subjectValue : notKONumericValueSubjectValues){
					String lsKind = subjectValue.getLsKind();
					if (!numericSubjectValueMapByLsKind.containsKey(lsKind)){
						Collection<SubjectValue> collection = new HashSet<SubjectValue>();
						collection.add(subjectValue);
						numericSubjectValueMapByLsKind.put(lsKind, collection);
					} else{
						Collection<SubjectValue> collection = numericSubjectValueMapByLsKind.get(lsKind);
						collection.add(subjectValue);
					}
				}
				try {
					TreatmentGroupState oldState = TreatmentGroupState.findTreatmentGroupStatesByTreatmentGroupIDAndStateTypeKind(treatmentGroup.getId(), "data", "results").getSingleResult();
					oldState.setIgnored(true);
					oldState.merge();
//					oldState.flush();
					logger.debug("Old TreatmentGroupState ignored.");
				}catch(EmptyResultDataAccessException e){
					logger.debug("No state data/results found. Creating a new state.");
				}
				TreatmentGroupState newState = createResultsTreatmentGroupState(treatmentGroup.getId(), "data", "results", recordedBy, lsTransaction);
				//fill in the batch code, using the batch code from one of the subjects (they are all the same under a treatmentGroup)
				SubjectValue batchCodeSubjectValue = findBatchCodeSubjectValue(treatmentGroup);
				if (batchCodeSubjectValue == null) logger.error("Unable to find batch code subject value for treatmentgroup: " + treatmentGroup.getCodeName());
				TreatmentGroupValue newBatchCodeTreatmentGroupValue = createTreatmentGroupValue(newState, batchCodeSubjectValue.getLsType(), batchCodeSubjectValue.getLsKind(), recordedBy, lsTransaction);
				newBatchCodeTreatmentGroupValue.setCodeValue(batchCodeSubjectValue.getCodeValue());
				newBatchCodeTreatmentGroupValue.setConcentration(batchCodeSubjectValue.getConcentration());
				newBatchCodeTreatmentGroupValue.setConcUnit(batchCodeSubjectValue.getConcUnit());
				newBatchCodeTreatmentGroupValue.setCodeType(batchCodeSubjectValue.getCodeType());
				newBatchCodeTreatmentGroupValue.setCodeKind(batchCodeSubjectValue.getCodeKind());
				newBatchCodeTreatmentGroupValue.setCodeTypeAndKind(batchCodeSubjectValue.getCodeTypeAndKind());
				newBatchCodeTreatmentGroupValue.merge();
				//calculate a new averaged treatmentGroupValue for each lsKind of numericValue found in the not knocked out subjects
				for (String key : numericSubjectValueMapByLsKind.keySet()){
					String lsType = "numericValue";
					String lsKind = key;
					Collection<SubjectValue> subjectValues = numericSubjectValueMapByLsKind.get(key);
					String unitKind = subjectValues.iterator().next().getUnitKind();
					String uncertaintyType = "standard deviation";
					StatCalc statCalc = new StatCalc();
					for (SubjectValue subjectValue : subjectValues){
						statCalc.add(subjectValue.getNumericValue().doubleValue()); 
					}
					TreatmentGroupValue newNumericTreatmentGroupValue = createTreatmentGroupValue(newState, lsType, lsKind, recordedBy, lsTransaction);
					newNumericTreatmentGroupValue.setNumericValue(BigDecimal.valueOf(statCalc.getArithmeticMean()));
					newNumericTreatmentGroupValue.setUnitKind(unitKind);
					newNumericTreatmentGroupValue.setNumberOfReplicates(statCalc.getCount());
					newNumericTreatmentGroupValue.setUncertainty(BigDecimal.valueOf(statCalc.getStandardDeviation()));
					newNumericTreatmentGroupValue.setUncertaintyType(uncertaintyType);
					newNumericTreatmentGroupValue.merge();
				}
		}
	}



	public static Collection<SubjectValue> findNotKONumericValueSubjectValues(
			TreatmentGroup treatmentGroup) {
		EntityManager em = SubjectValue.entityManager();
		Query q = em.createNativeQuery( "SELECT lsvalues4_.* "
				+ "FROM treatment_group treatmentg0_ "
				+ "INNER JOIN treatmentgroup_subject subjects1_ "
				+ "ON treatmentg0_.id=subjects1_.treatment_group_id "
				+ "INNER JOIN subject subject2_ "
				+ "ON subjects1_.subject_id=subject2_.id "
				+ "INNER JOIN subject_state lsstates3_ "
				+ "ON subject2_.id=lsstates3_.subject_id "
				+ "INNER JOIN subject_value lsvalues4_ "
				+ "ON lsstates3_.id       =lsvalues4_.subject_state_id "
				+ "WHERE treatmentg0_.id  = ?1 "
				+ "AND subject2_.ignored <> ?2 "
				+ "AND lsstates3_.ignored<> ?3 "
				+ "AND lsstates3_.ls_type = ?4 "
				+ "AND lsstates3_.ls_kind = ?5 "
				+ "AND lsvalues4_.ls_type ='numericValue' "
				+ "AND (subject2_.id NOT IN "
				+ "  (SELECT "
				+ "  /*+  "
				+ "      index(lsvalues9_ SBJVL_SBJST_FK) "
				+ "  */ "
				+ "  subject7_.id "
				+ "  FROM treatment_group treatmentg5_ "
				+ "  INNER JOIN treatmentgroup_subject subjects6_ "
				+ "  ON treatmentg5_.id=subjects6_.treatment_group_id "
				+ "  INNER JOIN subject subject7_ "
				+ "  ON subjects6_.subject_id=subject7_.id "
				+ "  INNER JOIN subject_state lsstates8_ "
				+ "  ON subject7_.id=lsstates8_.subject_id "
				+ "  INNER JOIN subject_value lsvalues9_ "
				+ "  ON lsstates8_.id         =lsvalues9_.subject_state_id "
				+ "  WHERE treatmentg5_.id    = ?6 "
				+ "  AND subject7_.ignored   <> ?7 "
				+ "  AND lsstates8_.ignored  <> ?8 "
				+ "  AND lsvalues9_.ls_type   = ?9 "
				+ "  AND lsvalues9_.ls_kind   = ?10 "
				+ "  AND lsvalues9_.code_value= ?11 "
				+ "  )) ", SubjectValue.class);
		
		q.setParameter(1, treatmentGroup.getId());
		q.setParameter(2, true);
		q.setParameter(3, true);
		q.setParameter(4, "data");
		q.setParameter(5, "results");
		q.setParameter(6, treatmentGroup.getId());
		q.setParameter(7, true);
		q.setParameter(8, true);
		q.setParameter(9, "codeValue");
		q.setParameter(10, "flag status");
		q.setParameter(11, "knocked out");
		
		return q.getResultList();
	}
	
	public static SubjectValue findBatchCodeSubjectValue(
			TreatmentGroup treatmentGroup) {
		EntityManager em = SubjectValue.entityManager();
		TypedQuery<SubjectValue> q = em.createQuery("SELECT batchCodeValue "
				+ "FROM TreatmentGroup AS treatmentGroup "
				+ "JOIN treatmentGroup.subjects AS subject "
				+ "JOIN subject.lsStates AS resultsState "
				+ "JOIN resultsState.lsValues AS batchCodeValue "
				+ "WHERE treatmentGroup = :treatmentGroup "
				+ "AND subject.ignored IS NOT :ignored "
				+ "AND resultsState.ignored IS NOT :ignored "
				+ "AND resultsState.lsType = :resultsStateType "
				+ "AND resultsState.lsKind = :resultsStateKind "
				+ "AND batchCodeValue.lsType = :batchCodeValueType "
				+ "AND batchCodeValue.lsKind = :batchCodeValueKind ", SubjectValue.class);
		
		q.setParameter("resultsStateType", "data");
		q.setParameter("resultsStateKind", "results");
		q.setParameter("batchCodeValueType", "codeValue");
		q.setParameter("batchCodeValueKind", "batch code");
		q.setParameter("treatmentGroup", treatmentGroup);
		q.setParameter("ignored", true);
		Collection<SubjectValue> results = q.getResultList();
		if (!results.isEmpty()) return results.iterator().next();
		else return null;
	}
	
	@Transactional
	private static TypedQuery<SubjectState> findAlgorithmFlagState(Subject subject) {
		EntityManager em = SubjectState.entityManager();
		TypedQuery<SubjectState> q = em.createQuery("SELECT state "
				+ "FROM SubjectState AS state "
				+ "WHERE state.lsType = :stateType "
				+ "AND state.lsKind = :stateKind "
				+ "AND state.subject = :subject "
				+ "AND state.ignored IS NOT :ignored", SubjectState.class);
		q.setParameter("subject", subject);
		q.setParameter("stateType", "data");
		q.setParameter("stateKind", "auto flag");
		q.setParameter("ignored", true);
		return q;
	}
	
	@Transactional
	private static TypedQuery<SubjectState> findPreprocessFlagState(Subject subject) {
		EntityManager em = SubjectState.entityManager();
		TypedQuery<SubjectState> q = em.createQuery("SELECT state "
				+ "FROM SubjectState AS state "
				+ "WHERE state.lsType = :stateType "
				+ "AND state.lsKind = :stateKind "
				+ "AND state.subject = :subject "
				+ "AND state.ignored IS NOT :ignored", SubjectState.class);
		q.setParameter("subject", subject);
		q.setParameter("stateType", "data");
		q.setParameter("stateKind", "preprocess flag");
		q.setParameter("ignored", true);
		return q;
	}
	
	@Transactional
	private static TypedQuery<SubjectState> findUserFlagState(Subject subject) {
		EntityManager em = SubjectState.entityManager();
		TypedQuery<SubjectState> q = em.createQuery("SELECT state "
				+ "FROM SubjectState AS state "
				+ "WHERE state.lsType = :stateType "
				+ "AND state.lsKind = :stateKind "
				+ "AND state.subject = :subject "
				+ "AND state.ignored IS NOT :ignored", SubjectState.class);
		q.setParameter("subject", subject);
		q.setParameter("stateType", "data");
		q.setParameter("stateKind", "user flag");
		q.setParameter("ignored", true);
		return q;
	}

	@Transactional
	private static Subject findSubject(Long responseSubjectValueId) {
		EntityManager em = Subject.entityManager();
		TypedQuery<Subject> q = em.createQuery("SELECT s "
				+ "FROM SubjectValue AS sv "
				+ "JOIN sv.lsState AS ss "
				+ "JOIN ss.subject AS s "
				+ "WHERE sv.id = :subjectValueId "
				+ "AND sv.ignored IS NOT :ignored "
				+ "AND ss.ignored IS NOT :ignored", Subject.class);
		q.setParameter("subjectValueId", responseSubjectValueId);
		q.setParameter("ignored", true);
		Subject result = q.getSingleResult();

		return result;
	}
	
	@Transactional
	private static void saveWellFlags(SubjectState state, FlagWellDTO flagWellDTO) {
		Collection<SubjectValue> newValues = new HashSet<SubjectValue>();
		String stateLsKind = state.getLsKind();
		String flagType = null;
		String flagStatus = null;
		String flagObservation = null;
		String flagCause = null;
		String flagComment = null;
		if (stateLsKind.equals("auto flag")) {
			flagType = "algorithm";
			flagStatus = flagWellDTO.getAlgorithmFlagStatus();
			flagObservation = flagWellDTO.getAlgorithmFlagObservation();
			flagCause = flagWellDTO.getAlgorithmFlagCause();
			flagComment = flagWellDTO.getAlgorithmFlagComment();
		} else if (stateLsKind.equals("preprocess flag")) {
			flagType = "preprocess";
			flagStatus = flagWellDTO.getPreprocessFlagStatus();
			flagObservation = flagWellDTO.getPreprocessFlagObservation();
			flagCause = flagWellDTO.getPreprocessFlagCause();
			flagComment = flagWellDTO.getPreprocessFlagComment();
		} else {
			flagType = "user";
			flagStatus = flagWellDTO.getUserFlagStatus();
			flagObservation = flagWellDTO.getUserFlagObservation();
			flagCause = flagWellDTO.getUserFlagCause();
			flagComment = flagWellDTO.getUserFlagComment();
		}
		String recordedBy = flagWellDTO.getRecordedBy();
		Long lsTransaction = flagWellDTO.getLsTransaction();
		//only create SubjectValues if they would not be empty/null
		if (!(flagStatus==null)){
			SubjectValue flagStatusValue = createWellFlagValue(state, "codeValue", "flag status", flagStatus, recordedBy, lsTransaction);
			flagStatusValue.setCodeType(flagType+" well flags");
			flagStatusValue.setCodeKind("flag status");
			newValues.add(flagStatusValue);
		}
		if (!(flagObservation==null)){
			SubjectValue flagObservationValue = createWellFlagValue(state, "codeValue", "flag observation", flagObservation, recordedBy, lsTransaction);
			flagObservationValue.setCodeType(flagType+" well flags");
			flagObservationValue.setCodeKind("flag observation");
			newValues.add(flagObservationValue);
		}
		if (!(flagCause==null)){
			SubjectValue flagCauseValue = createWellFlagValue(state, "codeValue", "flag cause", flagCause, recordedBy, lsTransaction);
			flagCauseValue.setCodeType(flagType+" well flags");
			flagCauseValue.setCodeKind("flag cause");
			newValues.add(flagCauseValue);
		}
		if (!(flagComment==null)){
			SubjectValue flagCommentValue = createWellFlagValue(state, "stringValue", "comment", flagComment, recordedBy, lsTransaction);
			newValues.add(flagCommentValue);
		}
		//persist and flush all the new values
		for (SubjectValue value: newValues){
			value.setCodeOrigin("ACAS DDICT");
			value.setRecordedBy(recordedBy);
			value.persist();
//			value.flush();
		}		
	}
	
	@Transactional
	private static TreatmentGroupValue createTreatmentGroupValue(TreatmentGroupState lsState, String lsType, String lsKind, String recordedBy, Long lsTransaction) {
		TreatmentGroupValue treatmentGroupValue = new TreatmentGroupValue();
		treatmentGroupValue.setLsState(lsState);
		treatmentGroupValue.setLsType(lsType);
		treatmentGroupValue.setLsKind(lsKind);
		treatmentGroupValue.setRecordedBy(recordedBy);
		treatmentGroupValue.setLsTransaction(lsTransaction);
		logger.debug("Creating value: " + treatmentGroupValue.toJson());
		treatmentGroupValue.persist();
		return treatmentGroupValue;
	}
	
	@Transactional
	private static TreatmentGroupState createResultsTreatmentGroupState(Long treatmentGroupId, String stateType, String stateKind, String recordedBy, Long lsTransaction) {
		TreatmentGroupState treatmentGroupState = new TreatmentGroupState();
		TreatmentGroup treatmentGroup = TreatmentGroup.findTreatmentGroup(treatmentGroupId);
		treatmentGroupState.setTreatmentGroup(treatmentGroup);
		treatmentGroupState.setLsType(stateType);
		treatmentGroupState.setLsKind(stateKind);
		treatmentGroupState.setRecordedBy(recordedBy);
		treatmentGroupState.setLsTransaction(lsTransaction);
		logger.debug("Creating state: " + treatmentGroupState.toJson());
		treatmentGroupState.persist();
		return treatmentGroupState;
	}
	
	
	@Transactional
	private static SubjectValue createWellFlagValue(SubjectState lsState, String lsType, String lsKind, String value, String recordedBy, Long lsTransaction) {
		SubjectValue subjectValue = new SubjectValue();
		subjectValue.setLsState(lsState);
		subjectValue.setLsType(lsType);
		subjectValue.setLsKind(lsKind);
		if (lsType.equals("stringValue")) subjectValue.setStringValue(value);
		if (lsType.equals("codeValue")) subjectValue.setCodeValue(value);
		subjectValue.setRecordedBy(recordedBy);
		subjectValue.setLsTransaction(lsTransaction);
		logger.debug("Creating value: " + subjectValue.toJson());
		subjectValue.persist();
		return subjectValue;
	}
	

	@Transactional
	private static SubjectState createWellFlagState(Long subjectId, String stateType, String stateKind, String recordedBy, Long lsTransaction) {
		SubjectState subjectState = new SubjectState();
		Subject subject = Subject.findSubject(subjectId);
		subjectState.setSubject(subject);
		subjectState.setLsType(stateType);
		subjectState.setLsKind(stateKind);
		subjectState.setRecordedBy(recordedBy);
		subjectState.setLsTransaction(lsTransaction);
		logger.debug("Creating state: " + subjectState.toJson());
		subjectState.persist();
		return subjectState;
	}



	public Long getResponseSubjectValueId() {
        return this.responseSubjectValueId;
    }

	public void setResponseSubjectValueId(Long responseSubjectValueId) {
        this.responseSubjectValueId = responseSubjectValueId;
    }

	public String getRecordedBy() {
        return this.recordedBy;
    }

	public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

	public Long getLsTransaction() {
        return this.lsTransaction;
    }

	public void setLsTransaction(Long lsTransaction) {
        this.lsTransaction = lsTransaction;
    }

	public String getAlgorithmFlagStatus() {
        return this.algorithmFlagStatus;
    }

	public void setAlgorithmFlagStatus(String algorithmFlagStatus) {
        this.algorithmFlagStatus = algorithmFlagStatus;
    }

	public String getAlgorithmFlagObservation() {
        return this.algorithmFlagObservation;
    }

	public void setAlgorithmFlagObservation(String algorithmFlagObservation) {
        this.algorithmFlagObservation = algorithmFlagObservation;
    }

	public String getAlgorithmFlagCause() {
        return this.algorithmFlagCause;
    }

	public void setAlgorithmFlagCause(String algorithmFlagCause) {
        this.algorithmFlagCause = algorithmFlagCause;
    }

	public String getAlgorithmFlagComment() {
        return this.algorithmFlagComment;
    }

	public void setAlgorithmFlagComment(String algorithmFlagComment) {
        this.algorithmFlagComment = algorithmFlagComment;
    }

	public String getPreprocessFlagStatus() {
        return this.preprocessFlagStatus;
    }

	public void setPreprocessFlagStatus(String preprocessFlagStatus) {
        this.preprocessFlagStatus = preprocessFlagStatus;
    }

	public String getPreprocessFlagObservation() {
        return this.preprocessFlagObservation;
    }

	public void setPreprocessFlagObservation(String preprocessFlagObservation) {
        this.preprocessFlagObservation = preprocessFlagObservation;
    }

	public String getPreprocessFlagCause() {
        return this.preprocessFlagCause;
    }

	public void setPreprocessFlagCause(String preprocessFlagCause) {
        this.preprocessFlagCause = preprocessFlagCause;
    }

	public String getPreprocessFlagComment() {
        return this.preprocessFlagComment;
    }

	public void setPreprocessFlagComment(String preprocessFlagComment) {
        this.preprocessFlagComment = preprocessFlagComment;
    }

	public String getUserFlagStatus() {
        return this.userFlagStatus;
    }

	public void setUserFlagStatus(String userFlagStatus) {
        this.userFlagStatus = userFlagStatus;
    }

	public String getUserFlagObservation() {
        return this.userFlagObservation;
    }

	public void setUserFlagObservation(String userFlagObservation) {
        this.userFlagObservation = userFlagObservation;
    }

	public String getUserFlagCause() {
        return this.userFlagCause;
    }

	public void setUserFlagCause(String userFlagCause) {
        this.userFlagCause = userFlagCause;
    }

	public String getUserFlagComment() {
        return this.userFlagComment;
    }

	public void setUserFlagComment(String userFlagComment) {
        this.userFlagComment = userFlagComment;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static FlagWellDTO fromJsonToFlagWellDTO(String json) {
        return new JSONDeserializer<FlagWellDTO>()
        .use(null, FlagWellDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<FlagWellDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<FlagWellDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<FlagWellDTO> fromJsonArrayToFlagWellDTO(String json) {
        return new JSONDeserializer<List<FlagWellDTO>>()
        .use("values", FlagWellDTO.class).deserialize(json);
    }
}
