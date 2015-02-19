package com.labsynch.labseer.dto;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.domain.TreatmentGroupState;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.utils.StatCalc;

@RooJavaBean
@RooToString
@RooJson
public class FlagWellDTO {
	
	private static final Logger logger = LoggerFactory.getLogger(FlagWellDTO.class);
	
	private Long responseSubjectValueId;
	private String recordedBy;
	private Long lsTransaction;
	private String algorithmFlagStatus;
	private String algorithmFlagObservation;
	private String algorithmFlagReason;
	private String algorithmFlagComment;
	private String preprocessFlagStatus;
	private String preprocessFlagObservation;
	private String preprocessFlagReason;
	private String preprocessFlagComment;
	private String userFlagStatus;
	private String userFlagObservation;
	private String userFlagReason;
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
		this.algorithmFlagReason = flagMap.get("algorithmFlagReason");
		this.algorithmFlagComment = flagMap.get("userFlagComment");
		this.preprocessFlagStatus = flagMap.get("preprocessFlagStatus");
		this.preprocessFlagObservation = flagMap.get("preprocessFlagObservation");
		this.preprocessFlagReason = flagMap.get("preprocessFlagReason");
		this.preprocessFlagComment = flagMap.get("userFlagComment");
		this.userFlagStatus = flagMap.get("userFlagStatus");
		this.userFlagObservation = flagMap.get("userFlagObservation");
		this.userFlagReason = flagMap.get("userFlagReason");
		this.userFlagComment = flagMap.get("userFlagComment");
	}

	public static String[] getColumns(){
		String[] headerColumns = new String[] {
				"responseSubjectValueId",
				"recordedBy",
				"lsTransaction",
				"algorithmFlagStatus",
				"algorithmFlagObservation",
				"algorithmFlagReason",
				"algorithmFlagComment",
				"preprocessFlagStatus",
				"preprocessFlagObservation",
				"preprocessFlagReason",
				"preprocessFlagComment",
				"userFlagStatus",
				"userFlagObservation",
				"userFlagReason",
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
			if ( !(flagWellDTO.getAlgorithmFlagStatus()==null) || !(flagWellDTO.getAlgorithmFlagObservation()==null) || !(flagWellDTO.getAlgorithmFlagReason()==null) || !(flagWellDTO.getAlgorithmFlagComment()==null)) {
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
			if ( !(flagWellDTO.getPreprocessFlagStatus()==null) || !(flagWellDTO.getPreprocessFlagObservation()==null) || !(flagWellDTO.getPreprocessFlagReason()==null) || !(flagWellDTO.getPreprocessFlagComment()==null)) {
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
			if ( !(flagWellDTO.getUserFlagStatus()==null) || !(flagWellDTO.getUserFlagObservation()==null) || !(flagWellDTO.getUserFlagReason()==null) || !(flagWellDTO.getUserFlagComment()==null)) {
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
			String renderingHint = CurveFitDTO.findRenderingHint(treatmentGroup);
			if (renderingHint.equalsIgnoreCase("4 parameter D-R")) {
				Collection<SubjectValue> notKOTransformedEfficacySubjectValues = new HashSet<SubjectValue>();
				Collection<SubjectValue> transformedEfficacySubjectValues = findTransformedEfficacySubjectValuesByTreatmentGroup(treatmentGroup);
				Collection<SubjectValue> flagStatusKOSubjectValues = findFlagStatusKOSubjectValuesByTreatmentGroup(treatmentGroup);
				List<Long> koSubjectIdList = makeSubjectIdList(flagStatusKOSubjectValues);
				for (SubjectValue subjectValue : transformedEfficacySubjectValues){
					Long subjectId = subjectValue.getLsState().getSubject().getId();
					if (!koSubjectIdList.contains(subjectId)){
						notKOTransformedEfficacySubjectValues.add(subjectValue);
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
				TreatmentGroupValue newTransformedEfficacyTreatmentGroupValue = createTreatmentGroupValue(newState, "numericValue", "transformed efficacy", recordedBy, lsTransaction);
				StatCalc statCalc = new StatCalc();
				for (SubjectValue value : notKOTransformedEfficacySubjectValues){
					statCalc.add(value.getNumericValue().doubleValue()); 
				}
				newTransformedEfficacyTreatmentGroupValue.setNumericValue(BigDecimal.valueOf(statCalc.getArithmeticMean()));
				newTransformedEfficacyTreatmentGroupValue.setUnitKind("efficacy");
				newTransformedEfficacyTreatmentGroupValue.setNumberOfReplicates(statCalc.getCount());
				newTransformedEfficacyTreatmentGroupValue.setUncertainty(BigDecimal.valueOf(statCalc.getStandardDeviation()));
				newTransformedEfficacyTreatmentGroupValue.setUncertaintyType("standard deviation");
				logger.debug("Calculated new mean and standard deviation: " + newTransformedEfficacyTreatmentGroupValue.toJson());
				newTransformedEfficacyTreatmentGroupValue.merge();
//				newTransformedEfficacyTreatmentGroupValue.flush();
			}
			
		}
	}


	
	private static List<Long> makeSubjectIdList(
			Collection<SubjectValue> subjectValues) {
		List<Long> subjectIdList = new ArrayList<Long>();
		for (SubjectValue subjectValue : subjectValues) {
			Long subjectId = subjectValue.getLsState().getSubject().getId();
			subjectIdList.add(subjectId);
		}
		return subjectIdList;
	}


	@Transactional
	public static Collection<SubjectValue> findFlagStatusKOSubjectValuesByTreatmentGroup(
			TreatmentGroup treatmentGroup) {
		EntityManager em = SubjectValue.entityManager();
		TypedQuery<SubjectValue> q = em.createQuery("SELECT flagStatusValue "
				+ "FROM TreatmentGroup AS treatmentGroup "
				+ "JOIN treatmentGroup.subjects AS subject "
				+ "JOIN subject.lsStates AS flagStatusState "
				+ "JOIN flagStatusState.lsValues AS flagStatusValue "
				+ "WHERE treatmentGroup = :treatmentGroup "
				+ "AND subject.ignored IS NOT :ignored "
				+ "AND flagStatusState.ignored IS NOT :ignored "
				+ "AND flagStatusValue.lsType = :flagStatusValueType "
				+ "AND flagStatusValue.lsKind = :flagStatusValueKind "
				+ "AND flagStatusValue.codeValue = :ko ", SubjectValue.class);
		
		q.setParameter("flagStatusValueType", "codeValue");
		q.setParameter("flagStatusValueKind", "flag status");
		q.setParameter("ko", "knocked out");
		q.setParameter("treatmentGroup", treatmentGroup);
		q.setParameter("ignored", true);
		return q.getResultList();
	}
	
	@Transactional
	public static Collection<SubjectValue> findResponseSubjectValuesByTreatmentGroup(
			TreatmentGroup treatmentGroup) {
		EntityManager em = SubjectValue.entityManager();
		TypedQuery<SubjectValue> q = em.createQuery("SELECT resultsValue "
				+ "FROM TreatmentGroup AS treatmentGroup "
				+ "JOIN treatmentGroup.subjects AS subject "
				+ "JOIN subject.lsStates AS resultsState "
				+ "JOIN resultsState.lsValues AS resultsValue "
				+ "WHERE treatmentGroup = :treatmentGroup "
				+ "AND subject.ignored IS NOT :ignored "
				+ "AND resultsState.ignored IS NOT :ignored "
				+ "AND resultsState.lsType = :resultsStateType "
				+ "AND resultsState.lsKind = :resultsStateKind "
				+ "AND resultsValue.lsType = :resultsValueType "
				+ "AND resultsValue.lsKind = :resultsValueKind ", SubjectValue.class);
		
		q.setParameter("resultsStateType", "data");
		q.setParameter("resultsStateKind", "results");
		q.setParameter("resultsValueType", "numericValue");
		q.setParameter("resultsValueKind", "Response");
		q.setParameter("treatmentGroup", treatmentGroup);
		q.setParameter("ignored", true);
		return q.getResultList();
	}
	
	@Transactional
	public static Collection<SubjectValue> findTransformedEfficacySubjectValuesByTreatmentGroup(
			TreatmentGroup treatmentGroup) {
		EntityManager em = SubjectValue.entityManager();
		TypedQuery<SubjectValue> q = em.createQuery("SELECT resultsValue "
				+ "FROM TreatmentGroup AS treatmentGroup "
				+ "JOIN treatmentGroup.subjects AS subject "
				+ "JOIN subject.lsStates AS resultsState "
				+ "JOIN resultsState.lsValues AS resultsValue "
				+ "WHERE treatmentGroup = :treatmentGroup "
				+ "AND subject.ignored IS NOT :ignored "
				+ "AND resultsState.ignored IS NOT :ignored "
				+ "AND resultsState.lsType = :resultsStateType "
				+ "AND resultsState.lsKind = :resultsStateKind "
				+ "AND resultsValue.lsType = :resultsValueType "
				+ "AND resultsValue.lsKind = :resultsValueKind ", SubjectValue.class);
		
		q.setParameter("resultsStateType", "data");
		q.setParameter("resultsStateKind", "results");
		q.setParameter("resultsValueType", "numericValue");
		q.setParameter("resultsValueKind", "transformed efficacy");
		q.setParameter("treatmentGroup", treatmentGroup);
		q.setParameter("ignored", true);
		return q.getResultList();
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
		String flagReason = null;
		String flagComment = null;
		if (stateLsKind.equals("auto flag")) {
			flagType = "algorithm";
			flagStatus = flagWellDTO.getAlgorithmFlagStatus();
			flagObservation = flagWellDTO.getAlgorithmFlagObservation();
			flagReason = flagWellDTO.getAlgorithmFlagReason();
			flagComment = flagWellDTO.getAlgorithmFlagComment();
		} else if (stateLsKind.equals("preprocess flag")) {
			flagType = "preprocess";
			flagStatus = flagWellDTO.getPreprocessFlagStatus();
			flagObservation = flagWellDTO.getPreprocessFlagObservation();
			flagReason = flagWellDTO.getPreprocessFlagReason();
			flagComment = flagWellDTO.getPreprocessFlagComment();
		} else {
			flagType = "user";
			flagStatus = flagWellDTO.getUserFlagStatus();
			flagObservation = flagWellDTO.getUserFlagObservation();
			flagReason = flagWellDTO.getUserFlagReason();
			flagComment = flagWellDTO.getUserFlagComment();
		}
		String recordedBy = flagWellDTO.getRecordedBy();
		Long lsTransaction = flagWellDTO.getLsTransaction();
		//only create SubjectValues if they would not be empty/null
		if (!(flagStatus==null)){
			SubjectValue flagStatusValue = createWellFlagValue(state, "codeValue", flagType+" flag status", flagStatus, recordedBy, lsTransaction);
			flagStatusValue.setCodeType(flagType+" well flags");
			flagStatusValue.setCodeKind("flag status");
			newValues.add(flagStatusValue);
		}
		if (!(flagObservation==null)){
			SubjectValue flagObservationValue = createWellFlagValue(state, "codeValue", flagType+" flag observation", flagObservation, recordedBy, lsTransaction);
			flagObservationValue.setCodeType(flagType+" well flags");
			flagObservationValue.setCodeKind("flag observation");
			newValues.add(flagObservationValue);
		}
		if (!(flagReason==null)){
			SubjectValue flagReasonValue = createWellFlagValue(state, "codeValue", flagType+" flag reason", flagReason, recordedBy, lsTransaction);
			flagReasonValue.setCodeType(flagType+" well flags");
			flagReasonValue.setCodeKind("flag reason");
			newValues.add(flagReasonValue);
		}
		if (!(flagComment==null)){
			SubjectValue flagCommentValue = createWellFlagValue(state, "stringValue", "comment", flagComment, recordedBy, lsTransaction);
			flagCommentValue.setCodeType(flagType+" well flags");
			flagCommentValue.setCodeKind("flag reason");
			newValues.add(flagCommentValue);
		}
		//persist and flush all the new values
		for (SubjectValue value: newValues){
			value.setCodeOrigin("ACAS Curve Curator");
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


}
