package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

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

	@Override
	public List<SubjectState> getSubjectStatesBySubjectIdAndStateTypeKind(
			Long subjectId, String stateType, String stateKind) {
		
			List<SubjectState> subjectStates = SubjectState.findSubjectStatesBySubjectIDAndStateTypeKind(subjectId, stateType, stateKind).getResultList();

			return subjectStates;
	}

	@Override
	public SubjectState createSubjectStateBySubjectIdAndStateTypeKind(
			Long subjectId, String stateType, String stateKind) {
		SubjectState subjectState = new SubjectState();
		Subject subject = Subject.findSubject(subjectId);
		subjectState.setSubject(subject);
		subjectState.setLsType(stateType);
		subjectState.setLsKind(stateKind);
		subjectState.setRecordedBy("default");
		subjectState.persist();
		return subjectState;
	}

	@Override
	public SubjectState saveSubjectState(
			SubjectState subjectState) {
		subjectState.setSubject(Subject.findSubject(subjectState.getSubject().getId()));		
		subjectState.persist();
		Set<SubjectValue> savedValues = new HashSet<SubjectValue>();
		for (SubjectValue subjectValue : subjectState.getLsValues()){
			subjectValue.setLsState(subjectState);
			subjectValue.persist();
			savedValues.add(subjectValue);
		}
		subjectState.setLsValues(savedValues);
		subjectState.merge();
		return subjectState;
	}

	@Override
	public Collection<SubjectState> saveSubjectStates(
			Collection<SubjectState> subjectStates) {
		for (SubjectState subjectState: subjectStates) {
			subjectState = saveSubjectState(subjectState);
		}
		return subjectStates;
	}
	
	@Override
	public SubjectState updateSubjectState(
			SubjectState subjectState) {
		subjectState.setVersion(SubjectState.findSubjectState(subjectState.getId()).getVersion());
		subjectState.merge();
		return subjectState;
	}

	@Override
	public Collection<SubjectState> updateSubjectStates(
			Collection<SubjectState> subjectStates) {
		for (SubjectState subjectState : subjectStates){
			subjectState = updateSubjectState(subjectState);
		}
		return null;
	}
	
	@Override
	public SubjectState getSubjectState(String idOrCodeName,
			String stateType, String stateKind) {
		SubjectState state = null;
		try{
			Long id;
			if (SimpleUtil.isNumeric(idOrCodeName)) id = Long.valueOf(idOrCodeName);
			else id = Subject.findSubjectsByCodeNameEquals(idOrCodeName).getSingleResult().getId();
			state = SubjectState.findSubjectStatesBySubjectIDAndStateTypeKind(id, stateType, stateKind).getSingleResult();
		}catch (Exception e){
			logger.error("Caught error "+e.toString()+" trying to find a state.",e);
			state = null;
		}
		return state;
	}

}
