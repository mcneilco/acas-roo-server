package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Service
@Transactional
public class SubjectStateServiceImpl implements SubjectStateService {

	private static final Logger logger = LoggerFactory.getLogger(SubjectStateServiceImpl.class);
	
	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	public Collection<SubjectState> ignoreAllSubjectStates(Collection<SubjectState> subjectStates) {
		//mark subjectStates and values as ignore 
		Collection<SubjectState> subjectStateSet = new HashSet<SubjectState>();
		for (SubjectState querySubjectState : subjectStates){
			SubjectState subjectState = SubjectState.findSubjectState(querySubjectState.getId());			
				for(SubjectValue subjectValue : SubjectValue.findSubjectValuesByLsState(subjectState).getResultList()){
					subjectValue.setIgnored(true);
					subjectValue.merge();
				}
				subjectState.setIgnored(true);
				subjectState.merge();
				subjectStateSet.add(SubjectState.findSubjectState(subjectState.getId()));
		}

		return(subjectStateSet);

	}


}
