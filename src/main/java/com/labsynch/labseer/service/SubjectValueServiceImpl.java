package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;


@Service
@Transactional
public class SubjectValueServiceImpl implements SubjectValueService {

	private static final Logger logger = LoggerFactory.getLogger(SubjectValueServiceImpl.class);

	@Override
	public void deleteSubjectValue(SubjectValue subjectValue){
		logger.debug("incoming meta subject: " + subjectValue.toJson());

	}

	//Query hibernate object and grab existing table references - add them to json hydrated object
	@Override
	@Transactional
	public SubjectValue updateSubjectValue(SubjectValue subjectValue){
		if (subjectValue.getLsState().getId() == null) {
			SubjectState subjectState = new SubjectState(subjectValue.getLsState());
			subjectState.setSubject(Subject.findSubject(subjectValue.getLsState().getSubject().getId()));
			subjectState.persist();
			subjectValue.setLsState(subjectState); 
		} else {
			subjectValue.setLsState(SubjectState.findSubjectState(subjectValue.getLsState().getId()));
		}		
		subjectValue.merge();
		return subjectValue;
	}

	@Override
	@Transactional
	public SubjectValue saveSubjectValue(SubjectValue subjectValue) {
		if (subjectValue.getLsState().getId() == null) {
			SubjectState subjectState = new SubjectState(subjectValue.getLsState());
			subjectState.setSubject(Subject.findSubject(subjectValue.getLsState().getSubject().getId()));
			subjectState.persist();
			subjectValue.setLsState(subjectState); 
		} else {
			subjectValue.setLsState(SubjectState.findSubjectState(subjectValue.getLsState().getId()));
		}		
		subjectValue.persist();
		return subjectValue;
	}

	@Override
	public List<SubjectValue> getSubjectValuesBySubjectId(Long id){	
		List<SubjectValue> subjectValues = new ArrayList<SubjectValue>();
		Subject subject = Subject.findSubject(id);
		if(subject.getLsStates() != null) {
			for (SubjectState subjectState : subject.getLsStates()) {
				if(subjectState.getLsValues() != null) {
					for(SubjectValue subjectValue : subjectState.getLsValues()) {
						subjectValues.add(subjectValue);
					}
				}
			}
		}
		return subjectValues;
	}
}
