package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectLabel;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.dto.SubjectDTO;
import com.labsynch.labseer.dto.SubjectLabelDTO;
import com.labsynch.labseer.dto.SubjectStateDTO;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Service
@Transactional
public class SubjectServiceImpl implements SubjectService {

	private static final Logger logger = LoggerFactory.getLogger(SubjectServiceImpl.class);

	@Autowired
	private AutoLabelService autoLabelService;
	
	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
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
	public Subject saveSubject(Subject subject){
		logger.debug("incoming meta subject: " + subject.toJson());

		Subject newSubject = new Subject(subject);
		if (newSubject.getCodeName() == null){
			newSubject.setCodeName(autoLabelService.getSubjectCodeName());
		}
		if (newSubject.getRecordedDate() == null){
			newSubject.setRecordedDate(new Date());
		}
		
		Set<TreatmentGroup> treatmentGroups = new HashSet<TreatmentGroup>();
		for (TreatmentGroup treatmentGroup : subject.getTreatmentGroups()){
			treatmentGroups.add(TreatmentGroup.findTreatmentGroup(treatmentGroup.getId()));
		}
		newSubject.setTreatmentGroups(treatmentGroups);
		newSubject.persist();
		if (subject.getLsLabels() != null) {
			for(SubjectLabel subjectLabel : subject.getLsLabels()){
				SubjectLabel newSubjectLabel = new SubjectLabel(subjectLabel);
				newSubjectLabel.setSubject(newSubject);
				newSubjectLabel.persist();	
			}		
		}
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
		
		return Subject.findSubject(newSubject.getId());
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



}
