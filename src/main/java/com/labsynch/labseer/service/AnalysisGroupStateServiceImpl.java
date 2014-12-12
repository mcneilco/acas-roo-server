package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Service
@Transactional
public class AnalysisGroupStateServiceImpl implements AnalysisGroupStateService {

	private static final Logger logger = LoggerFactory.getLogger(AnalysisGroupStateServiceImpl.class);
	
	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	public List<AnalysisGroupState> getAnalysisGroupStatesByAnalysisGroupIdAndStateTypeKind(Long analysisGroupId, String stateType, 
			String stateKind) {	
		
		List<AnalysisGroupState> analysisGroupStates = AnalysisGroupState.findAnalysisGroupStatesByAnalysisGroupIDAndStateTypeKind(analysisGroupId, stateType, stateKind).getResultList();

		return analysisGroupStates;
	}
	
	@Override
	public Collection<AnalysisGroupState> ignoreAllAnalysisGroupStates(Collection<AnalysisGroupState> analysisGroupStates) {
		//mark AnalysisGroupStates and values as ignore 
		Collection<AnalysisGroupState> analysisGroupStateSet = new HashSet<AnalysisGroupState>();
		for (AnalysisGroupState queryAnalysisGroupState : analysisGroupStates){
			AnalysisGroupState analysisGroupState = AnalysisGroupState.findAnalysisGroupState(queryAnalysisGroupState.getId());			
				for(AnalysisGroupValue analysisGroupValue : AnalysisGroupValue.findAnalysisGroupValuesByLsState(analysisGroupState).getResultList()){
					analysisGroupValue.setIgnored(true);
					analysisGroupValue.merge();
				}
				analysisGroupState.setIgnored(true);
				analysisGroupState.merge();
				analysisGroupStateSet.add(AnalysisGroupState.findAnalysisGroupState(analysisGroupState.getId()));
		}

		return(analysisGroupStateSet);

	}
	
	@Override
	public AnalysisGroupState createAnalysisGroupStateByAnalysisGroupIdAndStateTypeKind(Long analysisGroupId, String stateType, String stateKind) {
		AnalysisGroupState analysisGroupState = new AnalysisGroupState();
		AnalysisGroup analysisGroup = AnalysisGroup.findAnalysisGroup(analysisGroupId);
		analysisGroupState.setAnalysisGroup(analysisGroup);
		analysisGroupState.setLsType(stateType);
		analysisGroupState.setLsKind(stateKind);
		analysisGroupState.setRecordedBy("default");
		analysisGroupState.persist();
		return analysisGroupState;
	}
	
	@Override
	public AnalysisGroupState createAnalysisGroupStateByAnalysisGroupIdAndStateTypeKindAndRecordedBy(Long analysisGroupId, String stateType, String stateKind, String recordedBy) {
		AnalysisGroupState analysisGroupState = new AnalysisGroupState();
		AnalysisGroup analysisGroup = AnalysisGroup.findAnalysisGroup(analysisGroupId);
		analysisGroupState.setAnalysisGroup(analysisGroup);
		analysisGroupState.setLsType(stateType);
		analysisGroupState.setLsKind(stateKind);
		analysisGroupState.setRecordedBy(recordedBy);
		analysisGroupState.persist();
		return analysisGroupState;
	}

	@Override
	public AnalysisGroupState saveAnalysisGroupState(
			AnalysisGroupState analysisGroupState) {
		analysisGroupState.setAnalysisGroup(AnalysisGroup.findAnalysisGroup(analysisGroupState.getAnalysisGroup().getId()));		
		analysisGroupState.persist();
		return analysisGroupState;
	}

	@Override
	public Collection<AnalysisGroupState> saveAnalysisGroupStates(
			Collection<AnalysisGroupState> analysisGroupStates) {
		for (AnalysisGroupState analysisGroupState: analysisGroupStates) {
			analysisGroupState = saveAnalysisGroupState(analysisGroupState);
		}
		return analysisGroupStates;
	}

	@Override
	public AnalysisGroupState updateAnalysisGroupState(
			AnalysisGroupState analysisGroupState) {
		analysisGroupState.setVersion(AnalysisGroupState.findAnalysisGroupState(analysisGroupState.getId()).getVersion());
		analysisGroupState.merge();
		return analysisGroupState;
	}

	@Override
	public Collection<AnalysisGroupState> updateAnalysisGroupStates(
			Collection<AnalysisGroupState> analysisGroupStates) {
		for (AnalysisGroupState analysisGroupState : analysisGroupStates){
			analysisGroupState = updateAnalysisGroupState(analysisGroupState);
		}
		return null;
	}


}
