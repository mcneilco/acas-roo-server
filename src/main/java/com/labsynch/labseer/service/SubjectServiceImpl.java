package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentLabel;
import com.labsynch.labseer.domain.ItxSubjectContainer;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.ProtocolLabel;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectLabel;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.dto.AnalysisGroupCodeDTO;
import com.labsynch.labseer.dto.ContainerSubjectsDTO;
import com.labsynch.labseer.dto.ExperimentCodeDTO;
import com.labsynch.labseer.dto.FlatThingCsvDTO;
import com.labsynch.labseer.dto.SubjectCodeDTO;
import com.labsynch.labseer.dto.SubjectCodeNameDTO;
import com.labsynch.labseer.dto.SubjectDTO;
import com.labsynch.labseer.dto.SubjectLabelDTO;
import com.labsynch.labseer.dto.SubjectSearchRequest;
import com.labsynch.labseer.dto.SubjectStateDTO;
import com.labsynch.labseer.dto.TempThingDTO;
import com.labsynch.labseer.dto.TreatmentGroupCodeDTO;
import com.labsynch.labseer.dto.ValueQueryDTO;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

@Service
@Transactional
public class SubjectServiceImpl implements SubjectService {

	private static final Logger logger = LoggerFactory.getLogger(SubjectServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Autowired
	private AutoLabelService autoLabelService;

	@Override
	public Set<Subject> ignoreAllSubjectStates(Set<Subject> subjects) {
		//mark subject and all states and values as ignore 
		int batchSize = propertiesUtilService.getBatchSize();
		int i = 0;
		int j = 0;
		Set<Subject> subjectSet = new HashSet<Subject>();
		for (Subject subject : subjects){
			Subject subj = Subject.findSubject(subject.getId());			
			for (SubjectState subjectState : SubjectState.findSubjectStatesBySubject(subj).getResultList()){
				subjectState.setIgnored(true);
				for(SubjectValue subjectValue : SubjectValue.findSubjectValuesByLsState(subjectState).getResultList()){
					subjectValue.setIgnored(true);
					subjectValue.merge();
					if ( i % batchSize == 0 ) { 
						subjectValue.flush();
						subjectValue.clear();
					}
					i++;
				}
				subjectState.setIgnored(true);
				subjectState.merge();
				if ( j % batchSize == 0 ) { 
					subjectState.flush();
					subjectState.clear();
				}
				j++;
			}
			subjectSet.add(Subject.findSubject(subject.getId()));
		}

		return(subjectSet);

	}

	@Override
	public SubjectDTO getSubject(Subject subject) {
		Subject subj = Subject.findSubject(subject.getId());			
		SubjectDTO subjectDTO = new SubjectDTO(subj);
		List<SubjectLabel> subjectLabels = SubjectLabel.findSubjectLabelsBySubject(subj).getResultList();
		Set<SubjectLabelDTO> subjectLabelDTOs = new HashSet<SubjectLabelDTO>();
		for (SubjectLabel subjectLabel : subjectLabels){
			SubjectLabelDTO subjectLabelDTO = new SubjectLabelDTO(subjectLabel);
			subjectLabelDTOs.add(subjectLabelDTO);
		}
		subjectDTO.setLsLabels(subjectLabelDTOs);

		List<SubjectState> subjectStates = SubjectState.findSubjectStatesBySubject(subj).getResultList();
		Set<SubjectStateDTO> subjectStateDTOs = new HashSet<SubjectStateDTO>();
		for (SubjectState ss:subjectStates ){
			SubjectStateDTO ssDTO = new SubjectStateDTO(ss);
			Set<SubjectValue> subjectValues = new HashSet<SubjectValue>(SubjectValue.findSubjectValuesByLsState(ss).getResultList());
			ssDTO.setSubjectValues(subjectValues);
			subjectStateDTOs.add(ssDTO);
		}
		subjectDTO.setLsStates(subjectStateDTOs);

		return subjectDTO;
	}

	@Override
	public Set<SubjectDTO> getSubjects(Set<Subject> subjects) {
		Set<SubjectDTO> subjectListDTO = new HashSet<SubjectDTO>();

		for (Subject subject : subjects){
			Subject subj = Subject.findSubject(subject.getId());			
			SubjectDTO subjectDTO = new SubjectDTO(subj);
			List<SubjectLabel> subjectLabels = SubjectLabel.findSubjectLabelsBySubject(subj).getResultList();
			Set<SubjectLabelDTO> subjectLabelDTOs = new HashSet<SubjectLabelDTO>();
			for (SubjectLabel subjectLabel : subjectLabels){
				SubjectLabelDTO subjectLabelDTO = new SubjectLabelDTO(subjectLabel);
				subjectLabelDTOs.add(subjectLabelDTO);
			}
			subjectDTO.setLsLabels(subjectLabelDTOs);

			List<SubjectState> subjectStates = SubjectState.findSubjectStatesBySubject(subj).getResultList();
			Set<SubjectStateDTO> subjectStateDTOs = new HashSet<SubjectStateDTO>();
			for (SubjectState ss:subjectStates ){
				SubjectStateDTO ssDTO = new SubjectStateDTO(ss);
				Set<SubjectValue> subjectValues = new HashSet<SubjectValue>(SubjectValue.findSubjectValuesByLsState(ss).getResultList());				
				ssDTO.setSubjectValues(subjectValues);
				subjectStateDTOs.add(ssDTO);
			}
			subjectDTO.setLsStates(subjectStateDTOs);
			subjectListDTO.add(subjectDTO);			
		}

		return(subjectListDTO);

	}

	@Override
	public Set<SubjectDTO> getSubjectsWithStateTypeAndKind(Collection<Subject> subjects, String stateTypeAndKind) {
		Set<SubjectDTO> subjectListDTO = new HashSet<SubjectDTO>();
		for (Subject subject : subjects){
			// logger.info("query subject id is: " + subject.getId());
			Subject subj = Subject.findSubject(subject.getId());
			if (subj != null){
				SubjectDTO subjectDTO = new SubjectDTO(subj);
				List<SubjectLabel> subjectLabels = SubjectLabel.findSubjectLabelsBySubject(subj).getResultList();
				Set<SubjectLabelDTO> subjectLabelDTOs = new HashSet<SubjectLabelDTO>();
				for (SubjectLabel subjectLabel : subjectLabels){
					SubjectLabelDTO subjectLabelDTO = new SubjectLabelDTO(subjectLabel);
					subjectLabelDTOs.add(subjectLabelDTO);
				}
				subjectDTO.setLsLabels(subjectLabelDTOs);

				List<SubjectState> subjectStates = SubjectState.findSubjectStatesBySubjectAndLsTypeAndKindEqualsAndIgnoredNot(subj, stateTypeAndKind).getResultList();
				Set<SubjectStateDTO> subjectStateDTOs = new HashSet<SubjectStateDTO>();
				for (SubjectState ss:subjectStates ){
					SubjectStateDTO ssDTO = new SubjectStateDTO(ss);
					Set<SubjectValue> subjectValues = new HashSet<SubjectValue>(SubjectValue.findSubjectValuesByLsState(ss).getResultList());
					ssDTO.setSubjectValues(subjectValues);
					subjectStateDTOs.add(ssDTO);
				}
				subjectDTO.setLsStates(subjectStateDTOs);
				subjectListDTO.add(subjectDTO);			

			} else {
				// logger.error("subject is null!!");				
			}
		}

		return(subjectListDTO);

	}

	@Override
	@Transactional
	public void saveSubjects(TreatmentGroup treatmentGroup, Set<Subject> subjects, Date recordedDate){
		Set<TreatmentGroup> treatmentGroups = new HashSet<TreatmentGroup>();
		treatmentGroups.add(treatmentGroup);
		int j = 0;
		if (subjects != null && !subjects.isEmpty()) {
			logger.debug("number of incoming subjects: " + subjects.size());
			Set<Long> subjectIds = new HashSet<Long>();
			for (Subject subject : subjects){
				subjectIds.add(subject.getId());
			}
			for (Long subjectId : subjectIds) {
				Subject subject = Subject.findSubject(subjectId);
				logger.debug("attempting to save subject: " + subject.getId());
				Subject newSubject = saveSubject(treatmentGroups, subject,
						recordedDate);
				if (j % propertiesUtilService.getBatchSize() == 0) {
					newSubject.flush();
					newSubject.clear();
				}
				j++;
				logger.debug("updated subject: " + subject.getId());
			}
		}
	}

	@Override
	@Transactional
	public Subject saveSubject(Subject subject){
		// logger.debug("incoming meta subject: " + subject.toJson());
		Date recordedDate = new Date();

		return saveSubject(subject.getTreatmentGroups(), subject, recordedDate);
	}


	@Override
	public Subject saveSubject(Set<TreatmentGroup> treatmentGroups, Subject subject, Date recordedDate){
		// logger.debug("incoming meta subject: " + subject.toJson());
		Subject newSubject = null;

		if (subject.getId() == null){
			newSubject = new Subject(subject);
			if (newSubject.getCodeName() == null){
				newSubject.setCodeName(autoLabelService.getSubjectCodeName());
			}
			if (newSubject.getRecordedDate() == null){
				newSubject.setRecordedDate(recordedDate);
			}

			for (TreatmentGroup treatmentGroup : treatmentGroups){
				newSubject.getTreatmentGroups().add(TreatmentGroup.findTreatmentGroup(treatmentGroup.getId()));
			}

			saveLabels(subject, newSubject, recordedDate );
			saveStates(subject, newSubject, recordedDate );
			newSubject.persist();

		} else {
			logger.debug("this is an existing subject -----------");
			newSubject = Subject.findSubject(subject.getId());
			for (TreatmentGroup treatmentGroup : treatmentGroups){
				// logger.debug("incoming treatment group: ------------ " + treatmentGroup.toJson());
				newSubject.getTreatmentGroups().add(TreatmentGroup.findTreatmentGroup(treatmentGroup.getId()));
			}

			newSubject.merge();

		}
		return Subject.findSubject(newSubject.getId());
	}


	private void saveStates(Subject subject, Subject newSubject, Date recordedDate) {
		if (subject.getLsStates() != null){
			Set<SubjectState> newSubjectStates = new HashSet<SubjectState>();
			for(SubjectState subjectState : subject.getLsStates()){
				SubjectState newSubjectState = new SubjectState(subjectState);
				newSubjectState.setSubject(newSubject);
				newSubjectStates.add(newSubjectState);
//				newSubjectState.persist();
				if (subjectState.getLsValues() != null){
					Set<SubjectValue> newSubjectValues = new HashSet<SubjectValue>();
					for (SubjectValue subjectValue : subjectState.getLsValues()){
						subjectValue.setLsState(newSubjectState);
						newSubjectValues.add(subjectValue);
					}
					newSubjectState.setLsValues(newSubjectValues);
				}
			}
			newSubject.setLsStates(newSubjectStates);
		}		
	}

	private void saveLabels(Subject subject, Subject newSubject, Date recordedDate) {
		if (subject.getLsLabels() != null) {
			for(SubjectLabel subjectLabel : subject.getLsLabels()){
				SubjectLabel newSubjectLabel = new SubjectLabel(subjectLabel);
				newSubjectLabel.setSubject(newSubject);
				newSubjectLabel.setRecordedDate(recordedDate);
				newSubjectLabel.setRecordedBy(newSubject.getRecordedBy());
				newSubjectLabel.persist();	
			}		
		}		
	}

	@Override
	@Transactional
	public Subject updateSubject(Subject subject){

		// logger.debug("incoming meta subject to update: " + subject.toJson());
		Subject updatedSubject = Subject.update(subject);

		if (subject.getLsLabels() != null) {
			for(SubjectLabel subjectLabel : subject.getLsLabels()){
				if (subjectLabel.getId() == null){
					SubjectLabel newSubjectLabel = new SubjectLabel(subjectLabel);
					newSubjectLabel.setSubject(Subject.findSubject(subject.getId()));
					newSubjectLabel.persist();						
				} else {
					SubjectLabel updatedLabel = SubjectLabel.update(subjectLabel);
					updatedSubject.getLsLabels().add(updatedLabel);
				}
			}		
		}
		updateLsStates(subject, updatedSubject);

		return Subject.findSubject(subject.getId());
	}
	
	public void updateLsStates(Subject jsonSubject, Subject updatedSubject){
		if(jsonSubject.getLsStates() != null){
			for(SubjectState subjectState : jsonSubject.getLsStates()){
				SubjectState updatedSubjectState;
				if (subjectState.getId() == null){
					updatedSubjectState = new SubjectState(subjectState);
					updatedSubjectState.setSubject(updatedSubject);
					updatedSubjectState.persist();
					updatedSubject.getLsStates().add(updatedSubjectState);
					if (logger.isDebugEnabled()) logger.debug("persisted new subject state " + updatedSubjectState.getId());

				} else {
					updatedSubjectState = SubjectState.update(subjectState);
					updatedSubject.getLsStates().add(updatedSubjectState);

					if (logger.isDebugEnabled()) logger.debug("updated subject state " + updatedSubjectState.getId());

				}
				if (subjectState.getLsValues() != null){
					for(SubjectValue subjectValue : subjectState.getLsValues()){
						if (subjectValue.getLsState() == null) subjectValue.setLsState(updatedSubjectState);
						SubjectValue updatedSubjectValue;
						if (subjectValue.getId() == null){
							updatedSubjectValue = SubjectValue.create(subjectValue);
							updatedSubjectValue.setLsState(SubjectState.findSubjectState(updatedSubjectState.getId()));
							updatedSubjectValue.persist();
							updatedSubjectState.getLsValues().add(updatedSubjectValue);
						} else {
							updatedSubjectValue = SubjectValue.update(subjectValue);
							if (logger.isDebugEnabled()) logger.debug("updated subject value " + updatedSubjectValue.getId());
						}
						if (logger.isDebugEnabled()) logger.debug("checking subjectValue " + updatedSubjectValue.toJson());

					}	
				} else {
					if (logger.isDebugEnabled()) logger.debug("No subject values to update");
				}
			}
		}
	}
	
	@Override
	public HashMap<String, TempThingDTO> createOnlySubjectsFromCSV(String subjectFilePath, List<Long> treatmentGroupIds) throws Exception{
		HashMap<String, TempThingDTO> treatmentGroupMap = new HashMap<String, TempThingDTO>();
		for (Long treatmentGroupId : treatmentGroupIds){
			TempThingDTO treatmentGroupThing = new TempThingDTO();
			treatmentGroupThing.setId(treatmentGroupId);
			treatmentGroupMap.put(treatmentGroupId.toString(), treatmentGroupThing);
		}
		try{
			HashMap<String, TempThingDTO> resultMap = createSubjectsFromCSV(subjectFilePath, treatmentGroupMap);
			return resultMap;
		}catch (Exception e){
			logger.error("Caught exception loading subjects from tsv", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public HashMap<String, TempThingDTO> createSubjectsFromCSV(
			String subjectFilePath, HashMap<String, TempThingDTO> treatmentGroupMap) throws IOException {

		int batchSize = propertiesUtilService.getBatchSize();
		ICsvBeanReader beanReader = null;
		HashMap<String, TempThingDTO> subjectMap = new HashMap<String, TempThingDTO>();
		HashMap<String, TempThingDTO> subjectStateMap = new HashMap<String, TempThingDTO>();
		HashMap<String, TempThingDTO> subjectValueMap = new HashMap<String, TempThingDTO>();

		try {
			// logger.info("read csv delimited file");
			InputStream is = new FileInputStream(subjectFilePath);  
			InputStreamReader isr = new InputStreamReader(is);  
			BufferedReader br = new BufferedReader(isr);

			beanReader = new CsvBeanReader(br, CsvPreference.TAB_PREFERENCE);
			String[] headerText = beanReader.getHeader(true);

			List<String> headerList = new ArrayList<String>();
			int position = 0;
			for (String head : headerText){
				// logger.info("header column: " + position + "  " + head);
				headerList.add(head);
				position++;
			}

			// logger.info("size of header list  " + headerList.size());
			String[] header = new String[headerList.size()];
			headerList.toArray(header);

			for (String head : header){
				// logger.debug("header column array : " + position + "  " + head);
				position++;
			}

			final CellProcessor[] processors = FlatThingCsvDTO.getProcessors();

			FlatThingCsvDTO subjectDTO;
			Subject subject;
			SubjectState subjectState;
			SubjectValue subjectValue;

			long rowIndex = 1;
			Set<TreatmentGroup> treatmentGroups = new HashSet<TreatmentGroup>();
			while( (subjectDTO = beanReader.read(FlatThingCsvDTO.class, header, processors)) != null ) {
				// logger.debug("-------------working on rowIndex: " + rowIndex + "--------------------");
				// logger.debug(String.format("lineNo=%s, rowNo=%s, bulkData=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), subjectDTO));

				if (subjectDTO.getLsType() == null) subjectDTO.setLsType("default");
				if (subjectDTO.getLsKind() == null) subjectDTO.setLsKind("default");
				if (subjectDTO.getTempId() == null) subjectDTO.setTempId(subjectDTO.getId().toString());

				subject = getOrCreateSubject(subjectDTO, subjectMap, treatmentGroupMap);
				if (subject != null){
					if (subject.getId() == null){
						subject.persist();
					} else {
						subject.merge();
					}
//					TreatmentGroup treatmentGroup = TreatmentGroup.findTreatmentGroup(treatmentGroupMap.get(subjectDTO.getTempParentId()).getId());
//					treatmentGroup.getSubjects().add(subject);
//					treatmentGroups.add(treatmentGroup);
					// logger.debug("saved the new subject: ID: " + subject.getId() + " codeName" + subject.getCodeName());
					// logger.debug("saved the new subject: " + subject.toJson());
					subjectMap = saveTempSubject(subject, subjectDTO, subjectMap);
				}

				if (subjectDTO.getStateType() != null && subjectDTO.getStateKind() != null){
					if (subjectDTO.getTempStateId() == null) subjectDTO.setTempStateId(subjectDTO.getStateId().toString());
					// logger.debug("subjectDTO.getTempStateId() is " + subjectDTO.getTempStateId());
					subjectState = getOrCreateSubjectState(subjectDTO, subjectStateMap, subjectMap);
					if (subjectState != null){
						subjectState.persist();
						// logger.debug("saved the new subject state: " + subjectState.getId());
						// logger.debug("saved the new subject state: " + subjectState.toJson());
						subjectStateMap = saveTempSubjectState(subjectState, subjectDTO, subjectStateMap);
					}

					if (subjectDTO.getValueType() != null && subjectDTO.getValueKind() != null){
						if (subjectDTO.getTempValueId() == null) subjectDTO.setTempValueId(Long.toString(rowIndex));
						subjectValue = getOrCreateSubjectValue(subjectDTO, subjectValueMap, subjectStateMap);
						if (subjectValue != null){
							subjectValue.persist();
							// logger.debug("saved the subject Value: " + subjectValue.toJson());
							if ( rowIndex % batchSize == 0 ) {
								subjectValue.flush();
								subjectValue.clear();
							}
							subjectValueMap = saveTempSubjectValue(subjectValue, subjectDTO, subjectValueMap);
						}
					}
				} else {
					// logger.debug("---------- not saving a new subject state: " + subjectDTO.getStateType());
				}

				rowIndex++;
			}
//			Long beforeMerge = new Date().getTime();
//			// logger.info("Number of TreatmentGroups to merge: "+ treatmentGroups.size());
//			for (TreatmentGroup treatmentGroup: treatmentGroups) {
//				// logger.debug("merging treatment group:" + treatmentGroup.getCodeName());
////				try {
////					// logger.debug(treatmentGroup.toJson());
////				} catch (Exception e) {
////					// logger.debug("Found exception: " + e);
////				}
//				treatmentGroup.merge();	
//			}
//			Long afterMerge = new Date().getTime();
//			Long mergeDuration = afterMerge - beforeMerge;
//			// logger.info("Merging TreatmentGroups took: "+ mergeDuration + " ms");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			beanReader.close();
		}

		return subjectMap;
	}

	private HashMap<String, TempThingDTO> saveTempSubjectValue(
			SubjectValue subjectValue,
			FlatThingCsvDTO subjectDTO,
			HashMap<String, TempThingDTO> subjectValueMap) {

		TempThingDTO tempThingDTO = new TempThingDTO();
		tempThingDTO.setId(subjectValue.getId());
		tempThingDTO.setTempId(subjectDTO.getTempValueId());
		subjectValueMap.put(subjectDTO.getTempValueId(), tempThingDTO);

		return subjectValueMap;	
	}

	private SubjectValue getOrCreateSubjectValue(
			FlatThingCsvDTO subjectDTO,
			HashMap<String, TempThingDTO> subjectValueMap,
			HashMap<String, TempThingDTO> subjectStateMap) {

		SubjectValue subjectValue = null;
		if (!subjectValueMap.containsKey(subjectDTO.getTempValueId())){
			if (subjectDTO.getValueId() == null){
				subjectValue = new SubjectValue(subjectDTO);
				// logger.debug("query state: " + subjectDTO.getTempStateId());
				subjectValue.setLsState(SubjectState.findSubjectState(subjectStateMap.get(subjectDTO.getTempStateId()).getId()));				
			} else {
				subjectValue = SubjectValue.findSubjectValue(subjectDTO.getValueId());
			}
		} else {
			// logger.debug("skipping the saved subjectValue --------- " + subjectDTO.getValueId());
		}

		return subjectValue;
	}

	private HashMap<String, TempThingDTO> saveTempSubjectState(
			SubjectState subjectState,
			FlatThingCsvDTO subjectDTO,
			HashMap<String, TempThingDTO> subjectStateMap) {

		TempThingDTO tempThingDTO = new TempThingDTO();
		tempThingDTO.setId(subjectState.getId());
		tempThingDTO.setTempId(subjectDTO.getTempStateId());
		// logger.debug("saving the temp state: " + tempThingDTO.getTempId());
		subjectStateMap.put(subjectDTO.getTempStateId(), tempThingDTO);

		return subjectStateMap;		
	}

	private SubjectState getOrCreateSubjectState(
			FlatThingCsvDTO subjectDTO,
			HashMap<String, TempThingDTO> subjectStateMap,
			HashMap<String, TempThingDTO> subjectMap) {

		SubjectState subjectState = null;
		if (!subjectStateMap.containsKey(subjectDTO.getTempStateId())){
			if (subjectDTO.getStateId() == null){
				subjectState = new SubjectState(subjectDTO);
				// logger.debug("subjectDTO TempId: " + subjectDTO.getTempId());
				subjectState.setSubject(Subject.findSubject(subjectMap.get(subjectDTO.getTempId()).getId()));	
			} else {
				subjectState = SubjectState.findSubjectState(subjectDTO.getStateId());
			}
		} else {
			// logger.debug("skipping the saved subjectState --------- " + subjectDTO.getStateId());
		}

		return subjectState;

	}

	private HashMap<String, TempThingDTO> saveTempSubject(
			Subject subject, FlatThingCsvDTO subjectDTO,
			HashMap<String, TempThingDTO> subjectMap) {

		TempThingDTO tempThingDTO = new TempThingDTO();
		tempThingDTO.setId(subject.getId());
		tempThingDTO.setCodeName(subject.getCodeName());
		tempThingDTO.setTempId(subjectDTO.getTempId().toString());
		subjectMap.put(subjectDTO.getTempId(), tempThingDTO);
		return subjectMap;

	}

	private Subject getOrCreateSubject(
			FlatThingCsvDTO subjectDTO,
			HashMap<String, TempThingDTO> subjectMap,
			HashMap<String, TempThingDTO> treatmentGroupMap) {

		Subject subject = null;
		if (!subjectMap.containsKey(subjectDTO.getTempId())){
			if (subjectDTO.getId() == null){
				if (subjectDTO.getTempParentId() != null && !subjectDTO.getTempParentId().equalsIgnoreCase("null")){
					subject = new Subject(subjectDTO);
					if (subject.getCodeName() == null){
						// logger.debug("incoming subject codeName: " + subjectDTO.getCodeName());
						String newCodeName = autoLabelService.getSubjectCodeName();
						// logger.debug("------------------ new codeName: " + newCodeName);
						subject.setCodeName(newCodeName);
					}
				} else {
					// logger.debug("the temp parent ID is null " + subjectDTO.getTempParentId());
				}
			} else {
				subject = Subject.findSubject(subjectDTO.getId());
			}
			Set<TreatmentGroup> treatmentGroups = new HashSet<TreatmentGroup>();
			treatmentGroups.add(TreatmentGroup.findTreatmentGroup(treatmentGroupMap.get(subjectDTO.getTempParentId()).getId()));
			if (subject.getTreatmentGroups() == null){
				subject.setTreatmentGroups(treatmentGroups);
			} else {
				subject.getTreatmentGroups().addAll(treatmentGroups);
			}			
		} else {
			// logger.debug("skipping the previously saved subject --------- " + subjectDTO.getCodeName());
		}
		return subject;
	}

	@Override
	public Collection<SubjectCodeNameDTO> getSubjectsByCodeNames(
			List<String> codeNames) {
		if (codeNames.isEmpty()) return new ArrayList<SubjectCodeNameDTO>();
		EntityManager em = Subject.entityManager();
		String queryString = "SELECT new com.labsynch.labseer.dto.SubjectCodeNameDTO( "
				+ "subject.codeName, "
				+ "subject )"
				+ " FROM Subject subject ";
		queryString += "where ( subject.ignored <> true ) and ( ";
		Query q = SimpleUtil.addHqlInClause(em, queryString, "subject.codeName", codeNames);
		
//			if (logger.isDebugEnabled()) logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
		@SuppressWarnings("unchecked")
		Collection<SubjectCodeNameDTO> results = q.getResultList();
		//diff request with results to find codeNames that could not be found
		HashSet<String> requestCodeNames = new HashSet<String>();
		requestCodeNames.addAll(codeNames);
		HashSet<String> foundCodeNames = new HashSet<String>();
		for (SubjectCodeNameDTO result : results){
			foundCodeNames.add(result.getRequestCodeName());
		}
		requestCodeNames.removeAll(foundCodeNames);
		if (!requestCodeNames.isEmpty()){
			for (String requestCodeName : requestCodeNames){
				SubjectCodeNameDTO emptyResult = new SubjectCodeNameDTO();
				emptyResult.setRequestCodeName(requestCodeName);
				results.add(emptyResult);
			}		
		}
		return results;
	}
	
	@Override
	public Collection<ContainerSubjectsDTO> getSubjectsByContainerAndInteraction(Collection<ContainerSubjectsDTO> requests){
		for (ContainerSubjectsDTO request : requests){
			EntityManager em = Subject.entityManager();
			if (SimpleUtil.isNumeric(request.getContainerIdOrCodeName())){
				Long id = Long.valueOf(request.getContainerIdOrCodeName());
				String queryString = "SELECT subject FROM Subject subject "
						+ "JOIN subject.containers itx "
						+ "JOIN itx.container container "
						+ "WHERE subject.ignored <> true AND itx.ignored <> true AND container.ignored <> true ";
				if (request.getInteractionType() != null) queryString+= "AND itx.lsType = :itxType ";
				if (request.getInteractionKind() != null) queryString+= "AND itx.lsKind = :itxKind ";
				queryString += "AND container.id = :id ";
				TypedQuery<Subject> q = em.createQuery(queryString, Subject.class);
				q.setParameter("id", id);
				if (request.getInteractionType() != null) q.setParameter("itxType", request.getInteractionType());
				if (request.getInteractionKind() != null) q.setParameter("itxKind", request.getInteractionKind());
				Collection<Subject> subjects = q.getResultList();
				request.setSubjects(subjects);
			}else{
				String codeName = request.getContainerIdOrCodeName();
				String queryString = "SELECT subject FROM Subject subject "
						+ "JOIN subject.containers itx "
						+ "JOIN itx.container container "
						+ "WHERE subject.ignored <> true AND itx.ignored <> true AND container.ignored <> true ";
				if (request.getInteractionType() != null) queryString+= "AND itx.lsType = :itxType ";
				if (request.getInteractionKind() != null) queryString+= "AND itx.lsKind = :itxKind ";
				queryString += "AND container.codeName = :codeName ";
				TypedQuery<Subject> q = em.createQuery(queryString, Subject.class);
				q.setParameter("codeName", codeName);
				if (request.getInteractionType() != null) q.setParameter("itxType", request.getInteractionType());
				if (request.getInteractionKind() != null) q.setParameter("itxKind", request.getInteractionKind());
				Collection<Subject> subjects = q.getResultList();
				request.setSubjects(subjects);
			}
		}
		return requests;
	}

	@Override
	public Collection<Long> searchSubjectIdsByQueryDTO(
			SubjectSearchRequest query) throws Exception {
		List<Long> idList = new ArrayList<Long>();
		EntityManager em = Subject.entityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
		Root<Subject> subject = criteria.from(Subject.class);
		
		criteria.select(subject.<Long>get("id"));
		criteria.distinct(true);
		Predicate[] predicates = new Predicate[0];
		List<Predicate> predicateList = new ArrayList<Predicate>();
		
		//subjectType
		if (query.getSubjectType() != null){
			Predicate subjectType = cb.equal(subject.<String>get("lsType"), query.getSubjectType());
			predicateList.add(subjectType);
		}
		//subjectKind
		if (query.getSubjectKind() != null){
			Predicate subjectKind = cb.equal(subject.<String>get("lsKind"), query.getSubjectKind());
			predicateList.add(subjectKind);
		}
		//protocol label and experiment label
		if (query.getProtocolLabelLike() != null && query.getExperimentLabelLike() != null){
			Join<Subject, TreatmentGroup> tg = subject.join("treatmentGroups");
			Join<TreatmentGroup, AnalysisGroup> ag = tg.join("analysisGroups");
			Join<AnalysisGroup, Experiment> experiment = ag.join("experiments");
			Predicate tgNotIgn = cb.not(tg.<Boolean>get("ignored"));
			Predicate agNotIgn = cb.not(ag.<Boolean>get("ignored"));
			Predicate exptNotIgn = cb.not(experiment.<Boolean>get("ignored"));
			predicateList.add(tgNotIgn);
			predicateList.add(agNotIgn);
			predicateList.add(exptNotIgn);
			
			if (query.getExperimentLabelLike() != null){
				Join<Experiment, ExperimentLabel> experimentLabel = experiment.join("lsLabels");
				Predicate exptLabelNotIgn = cb.not(experimentLabel.<Boolean>get("ignored"));
				predicateList.add(exptLabelNotIgn);
				Predicate experimentLabelLike = cb.like(experimentLabel.<String>get("labelText"), '%'+query.getExperimentLabelLike()+'%');
				predicateList.add(experimentLabelLike);
			}
			if (query.getProtocolLabelLike() != null){
				Join<Experiment, Protocol> protocol = experiment.join("protocol");
				Join<Protocol, ProtocolLabel> protocolLabel = protocol.join("lsLabels");
				Predicate protNotIgn = cb.not(protocol.<Boolean>get("ignored"));
				Predicate protLabelNotIgn = cb.not(protocolLabel.<Boolean>get("ignored"));
				predicateList.add(protNotIgn);
				predicateList.add(protLabelNotIgn);
				Predicate protocolLabelLike = cb.like(protocolLabel.<String>get("labelText"), '%'+query.getProtocolLabelLike()+'%');
				predicateList.add(protocolLabelLike);
			}
		}
		//container code
		if (query.getContainerCode() != null){
			Join<Subject, ItxSubjectContainer> itx = subject.join("containers");
			Join<ItxSubjectContainer, Container> container = itx.join("container");
			Predicate itxNotIgn = cb.not(itx.<Boolean>get("ignored"));
			Predicate containerNotIgn = cb.not(container.<Boolean>get("ignored"));
			Predicate containerCode = cb.equal(container.<String>get("codeName"), query.getContainerCode());
			predicateList.add(itxNotIgn);
			predicateList.add(containerNotIgn);
			predicateList.add(containerCode);
		}
		//values
		if (query.getValues() != null){
			for (ValueQueryDTO valueQuery : query.getValues()){
				List<Predicate> valuePredicatesList = new ArrayList<Predicate>();
				Join<Subject, SubjectState> state = subject.join("lsStates");
				Join<SubjectState, SubjectValue> value = state.join("lsValues");
				
				Predicate stateNotIgn = cb.isFalse(state.<Boolean>get("ignored"));
				Predicate valueNotIgn = cb.isFalse(value.<Boolean>get("ignored"));
				valuePredicatesList.add(stateNotIgn);
				valuePredicatesList.add(valueNotIgn);
				
				if (valueQuery.getStateType() != null){
					Predicate stateType = cb.equal(state.<String>get("lsType"),valueQuery.getStateType());
					valuePredicatesList.add(stateType);
				}
				if (valueQuery.getStateKind() != null){
					Predicate stateKind = cb.equal(state.<String>get("lsKind"),valueQuery.getStateKind());
					valuePredicatesList.add(stateKind);
				}
				if (valueQuery.getValueType() != null){
					Predicate valueType = cb.equal(value.<String>get("lsType"),valueQuery.getValueType());
					valuePredicatesList.add(valueType);
				}
				if (valueQuery.getValueKind() != null){
					Predicate valueKind = cb.equal(value.<String>get("lsKind"),valueQuery.getValueKind());
					valuePredicatesList.add(valueKind);
				}
				if (valueQuery.getValue() != null){
					if (valueQuery.getValueType() == null){
						logger.error("valueType must be specified if value is specified!");
						throw new Exception("valueType must be specified if value is specified!");
					}else if(valueQuery.getValueType().equals("dateValue")){
						String postgresTimeUnit = "day";
						Expression<Date> dateTruncExpr = cb.function("date_trunc", Date.class, cb.literal(postgresTimeUnit), value.<Date>get("dateValue"));
						Calendar cal = Calendar.getInstance(); // locale-specific
						cal.setTimeInMillis(Long.valueOf(valueQuery.getValue()));
						cal.set(Calendar.HOUR_OF_DAY, 0);
						cal.set(Calendar.MINUTE, 0);
						cal.set(Calendar.SECOND, 0);
						cal.set(Calendar.MILLISECOND, 0);
						long time = cal.getTimeInMillis();
						Date queryDate = new Date(time);
						Predicate valueLike = cb.equal(dateTruncExpr, queryDate);
						valuePredicatesList.add(valueLike);
					}else{
//						only works with string value types: stringValue, codeValue, fileValue, clobValue
						Predicate valueLike = cb.like(value.<String>get(valueQuery.getValueType()), '%' + valueQuery.getValue() + '%');
						valuePredicatesList.add(valueLike);
					}
				}
//				gather predicates with AND
				Predicate[] valuePredicates = new Predicate[0];
				valuePredicates = valuePredicatesList.toArray(valuePredicates);
				predicateList.add(cb.and(valuePredicates));
			}
		}
		predicates = predicateList.toArray(predicates);
		criteria.where(cb.and(predicates));
		TypedQuery<Long> q = em.createQuery(criteria);
		logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
		idList = q.getResultList();
		logger.debug("Found "+idList.size()+" results.");
		return idList;
	}

	@Override
	public Collection<Subject> getSubjectsByIds(Collection<Long> ids) {
		Collection<Subject> results = new ArrayList<Subject>();
		for (Long id : ids){
			results.add(Subject.findSubject(id));
		}
		return results;
	}

	@Transactional
	@Override
	public boolean setSubjectValuesByPath(Subject subject, ValueQueryDTO pathDTO) {
		EntityManager em = Subject.entityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<SubjectValue> criteria = cb.createQuery(SubjectValue.class);
		Root<SubjectValue> subjectValue = criteria.from(SubjectValue.class);
		Join<SubjectValue, SubjectState> subjectState = subjectValue.join("lsState");
		
		Predicate[] predicates = new Predicate[0];
		List<Predicate> predicateList = new ArrayList<Predicate>();
		Predicate subjectEqual = cb.equal(subjectState.<Subject>get("subject"), subject);
		predicateList.add(subjectEqual);
		if (pathDTO.getStateType() != null){
			Predicate stateType = cb.equal(subjectState.<String>get("lsType"), pathDTO.getStateType());
			predicateList.add(stateType);
		}
		if (pathDTO.getStateKind() != null){
			Predicate stateKind = cb.equal(subjectState.<String>get("lsKind"), pathDTO.getStateKind());
			predicateList.add(stateKind);
		}
		if (pathDTO.getValueType() != null){
			Predicate valueType = cb.equal(subjectValue.<String>get("lsType"), pathDTO.getValueType());
			predicateList.add(valueType);
		}
		if (pathDTO.getValueKind() != null){
			Predicate valueKind = cb.equal(subjectValue.<String>get("lsKind"), pathDTO.getValueKind());
			predicateList.add(valueKind);
		}
		
		
		predicates = predicateList.toArray(predicates);
		criteria.where(cb.and(predicates));
		TypedQuery<SubjectValue> q = em.createQuery(criteria);
		logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
		Collection<SubjectValue> subjectValues = q.getResultList();
		logger.debug("Found "+subjectValues.size()+" subject values.");
		
		if (subjectValues != null){
			for (SubjectValue value : subjectValues){
				if (pathDTO.getValueType().equals("stringValue")) value.setStringValue(pathDTO.getValue());
				if (pathDTO.getValueType().equals("codeValue")) value.setCodeValue(pathDTO.getValue());
				if (pathDTO.getValueType().equals("fileValue")) value.setFileValue(pathDTO.getValue());
				if (pathDTO.getValueType().equals("urlValue")) value.setUrlValue(pathDTO.getValue());
				if (pathDTO.getValueType().equals("clobValue")) value.setClobValue(pathDTO.getValue());
				if (pathDTO.getValueType().equals("numericValue")) value.setNumericValue(new BigDecimal(pathDTO.getValue()));
				value.merge();
			}
		}
		return true;
	}

	@Override
	public Collection<SubjectCodeDTO> getExperimentCodes(
			Collection<SubjectCodeDTO> subjectCodeDTOs) {
		for (SubjectCodeDTO subjectDTO : subjectCodeDTOs){
			Subject subject = Subject.findSubjectByCodeNameEquals(subjectDTO.getSubjectCode());
			Collection<TreatmentGroupCodeDTO> tgDTOs = new ArrayList<TreatmentGroupCodeDTO>();
			for (TreatmentGroup tg : subject.getTreatmentGroups()){
				TreatmentGroupCodeDTO tgDTO = new TreatmentGroupCodeDTO();
				tgDTO.setTreatmentGroupCode(tg.getCodeName());
				Collection<AnalysisGroupCodeDTO> agDTOs = new ArrayList<AnalysisGroupCodeDTO>();
				for (AnalysisGroup ag : tg.getAnalysisGroups()){
					AnalysisGroupCodeDTO agDTO = new AnalysisGroupCodeDTO();
					agDTO.setAnalysisGroupCode(ag.getCodeName());
					Collection<ExperimentCodeDTO> exptDTOs = new ArrayList<ExperimentCodeDTO>();
					for (Experiment expt : ag.getExperiments()){
						ExperimentCodeDTO exptDTO = new ExperimentCodeDTO();
						exptDTO.setExperimentCode(expt.getCodeName());
						exptDTO.setProtocolCode(expt.getProtocol().getCodeName());
						exptDTOs.add(exptDTO);
					}
					agDTO.setExperimentCodes(exptDTOs);
					agDTOs.add(agDTO);
				}
				tgDTO.setAnalysisGroupCodes(agDTOs);
				tgDTOs.add(tgDTO);
			}
			subjectDTO.setTreatmentGroupCodes(tgDTOs);
		}
		return subjectCodeDTOs;
	}

	
	

}
