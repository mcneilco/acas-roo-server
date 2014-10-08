package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.domain.AbstractThing;
import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectLabel;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.dto.FlatThingCsvDTO;
import com.labsynch.labseer.dto.SubjectDTO;
import com.labsynch.labseer.dto.SubjectLabelDTO;
import com.labsynch.labseer.dto.SubjectStateDTO;
import com.labsynch.labseer.dto.TempThingDTO;
import com.labsynch.labseer.utils.PropertiesUtilService;

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
			logger.info("query subject id is: " + subject.getId());
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
				logger.error("subject is null!!");				
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
		logger.debug("number of incoming subjects: " + subjects.size());
		for (Subject subject : subjects){
			Subject newSubject = saveSubject(treatmentGroups, subject, recordedDate);
			if ( j % propertiesUtilService.getBatchSize() == 0 ) { 
				newSubject.flush();
				newSubject.clear();
			}
			j++;
		}
	}

	@Override
	@Transactional
	public Subject saveSubject(Subject subject){
		logger.debug("incoming meta subject: " + subject.toJson());
		Date recordedDate = new Date();

		return saveSubject(subject.getTreatmentGroups(), subject, recordedDate);
	}


	@Override
	public Subject saveSubject(Set<TreatmentGroup> treatmentGroups, Subject subject, Date recordedDate){
		logger.debug("incoming meta subject: " + subject.toJson());
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

			newSubject.persist();
			saveLabels(subject, newSubject, recordedDate );
			saveStates(subject, newSubject, recordedDate );

		} else {
			logger.debug("this is an existing subject -----------");
			newSubject = Subject.findSubject(subject.getId());
			for (TreatmentGroup treatmentGroup : treatmentGroups){
				logger.debug("incoming treatment group: ------------ " + treatmentGroup.toJson());
				newSubject.getTreatmentGroups().add(TreatmentGroup.findTreatmentGroup(treatmentGroup.getId()));
			}

			newSubject.merge();

		}
		return Subject.findSubject(newSubject.getId());
	}


	private void saveStates(Subject subject, Subject newSubject, Date recordedDate) {
		if (subject.getLsStates() != null){
			for(SubjectState subjectState : subject.getLsStates()){
				SubjectState newSubjectState = new SubjectState(subjectState);
				newSubjectState.setSubject(newSubject);
				newSubjectState.persist();
				if (subjectState.getLsValues() != null){
					for (SubjectValue subjectValue : subjectState.getLsValues()){
						subjectValue.setLsState(newSubjectState);
						subjectValue.persist();
					}								
				}
			}
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

		logger.debug("incoming meta subject to update: " + subject.toJson());
		subject = Subject.update(subject);

		if (subject.getLsLabels() != null) {
			for(SubjectLabel subjectLabel : subject.getLsLabels()){
				if (subjectLabel.getId() == null){
					SubjectLabel newSubjectLabel = new SubjectLabel(subjectLabel);
					newSubjectLabel.setSubject(Subject.findSubject(subject.getId()));
					newSubjectLabel.persist();						
				} else {
					subjectLabel = SubjectLabel.update(subjectLabel);
				}
			}		
		}
		if (subject.getLsStates() != null){
			for(SubjectState subjectState : subject.getLsStates()){
				if (subjectState.getId() == null){
					SubjectState newSubjectState = new SubjectState(subjectState);
					newSubjectState.setSubject(Subject.findSubject(subject.getId()));
					newSubjectState.persist();	
					subjectState.setId(newSubjectState.getId());
				} else {
					subjectState = SubjectState.update(subjectState);

				}
				if (subjectState.getLsValues() != null){
					for (SubjectValue subjectValue : subjectState.getLsValues()){
						if (subjectValue.getId() == null){
							subjectValue.setLsState(SubjectState.findSubjectState(subjectState.getId()));
							subjectValue.persist();
						} else {
							subjectValue = SubjectValue.update(subjectValue);
						}

					}								
				}
			}
		}

		return Subject.findSubject(subject.getId());
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
			logger.info("read csv delimited file");
			InputStream is = new FileInputStream(subjectFilePath);  
			InputStreamReader isr = new InputStreamReader(is);  
			BufferedReader br = new BufferedReader(isr);

			beanReader = new CsvBeanReader(br, CsvPreference.TAB_PREFERENCE);
			String[] headerText = beanReader.getHeader(true);

			List<String> headerList = new ArrayList<String>();
			int position = 0;
			for (String head : headerText){
				logger.info("header column: " + position + "  " + head);
				headerList.add(head);
				position++;
			}

			logger.info("size of header list  " + headerList.size());
			String[] header = new String[headerList.size()];
			headerList.toArray(header);

			for (String head : header){
				logger.debug("header column array : " + position + "  " + head);
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
				logger.debug("-------------working on rowIndex: " + rowIndex + "--------------------");
				logger.debug(String.format("lineNo=%s, rowNo=%s, bulkData=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), subjectDTO));

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
					TreatmentGroup treatmentGroup = TreatmentGroup.findTreatmentGroup(treatmentGroupMap.get(subjectDTO.getTempParentId()).getId());
					treatmentGroup.getSubjects().add(subject);
					treatmentGroups.add(treatmentGroup);
					logger.debug("saved the new subject: ID: " + subject.getId() + " codeName" + subject.getCodeName());
					logger.debug("saved the new subject: " + subject.toJson());
					subjectMap = saveTempSubject(subject, subjectDTO, subjectMap);
				}

				if (subjectDTO.getStateType() != null && subjectDTO.getStateKind() != null){
					if (subjectDTO.getTempStateId() == null) subjectDTO.setTempStateId(subjectDTO.getStateId().toString());
					logger.debug("subjectDTO.getTempStateId() is " + subjectDTO.getTempStateId());
					subjectState = getOrCreateSubjectState(subjectDTO, subjectStateMap, subjectMap);
					if (subjectState != null){
						subjectState.persist();
						logger.debug("saved the new subject state: " + subjectState.getId());
						logger.debug("saved the new subject state: " + subjectState.toJson());
						subjectStateMap = saveTempSubjectState(subjectState, subjectDTO, subjectStateMap);
					}

					if (subjectDTO.getValueType() != null && subjectDTO.getValueKind() != null){
						if (subjectDTO.getTempValueId() == null) subjectDTO.setTempValueId(Long.toString(rowIndex));
						subjectValue = getOrCreateSubjectValue(subjectDTO, subjectValueMap, subjectStateMap);
						if (subjectValue != null){
							subjectValue.persist();
							logger.debug("saved the subject Value: " + subjectValue.toJson());
							if ( rowIndex % batchSize == 0 ) {
								subjectValue.flush();
								subjectValue.clear();
							}
							subjectValueMap = saveTempSubjectValue(subjectValue, subjectDTO, subjectValueMap);
						}
					}
				} else {
					logger.debug("---------- not saving a new subject state: " + subjectDTO.getStateType());
				}

				rowIndex++;
			}
			Long beforeMerge = new Date().getTime();
			logger.info("Number of TreatmentGroups to merge: "+ treatmentGroups.size());
			for (TreatmentGroup treatmentGroup: treatmentGroups) {
				treatmentGroup.merge();	
			}
			Long afterMerge = new Date().getTime();
			Long mergeDuration = afterMerge - beforeMerge;
			logger.info("Merging TreatmentGroups took: "+ mergeDuration + " ms");

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
				logger.debug("query state: " + subjectDTO.getTempStateId());
				subjectValue.setLsState(SubjectState.findSubjectState(subjectStateMap.get(subjectDTO.getTempStateId()).getId()));				
			} else {
				subjectValue = SubjectValue.findSubjectValue(subjectDTO.getValueId());
			}
		} else {
			logger.debug("skipping the saved subjectValue --------- " + subjectDTO.getValueId());
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
		logger.debug("saving the temp state: " + tempThingDTO.getTempId());
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
				logger.debug("subjectDTO TempId: " + subjectDTO.getTempId());
				subjectState.setSubject(Subject.findSubject(subjectMap.get(subjectDTO.getTempId()).getId()));	
			} else {
				subjectState = SubjectState.findSubjectState(subjectDTO.getStateId());
			}
		} else {
			logger.debug("skipping the saved subjectState --------- " + subjectDTO.getStateId());
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
						logger.debug("incoming subject codeName: " + subjectDTO.getCodeName());
						String newCodeName = autoLabelService.getSubjectCodeName();
						logger.debug("------------------ new codeName: " + newCodeName);
						subject.setCodeName(newCodeName);
					}
				} else {
					logger.debug("the temp parent ID is null " + subjectDTO.getTempParentId());
				}
			} else {
				subject = Subject.findSubject(subjectDTO.getId());
			}
//			Set<TreatmentGroup> treatmentGroups = new HashSet<TreatmentGroup>();
//			treatmentGroups.add(TreatmentGroup.findTreatmentGroup(treatmentGroupMap.get(subjectDTO.getTempParentId()).getId()));
//			if (subject.getTreatmentGroups() == null){
//				subject.setTreatmentGroups(treatmentGroups);
//			} else {
//				subject.getTreatmentGroups().addAll(treatmentGroups);
//			}			
		} else {
			logger.debug("skipping the previously saved subject --------- " + subjectDTO.getCodeName());
		}
		return subject;
	}

}
