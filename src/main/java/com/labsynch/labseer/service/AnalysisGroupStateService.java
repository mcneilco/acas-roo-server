package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.AnalysisGroupState;

@Service
public interface AnalysisGroupStateService {

	Collection<AnalysisGroupState> ignoreAllAnalysisGroupStates(Collection<AnalysisGroupState> analysisGroupStates);

	AnalysisGroupState createAnalysisGroupStateByAnalysisGroupIdAndStateTypeKind(
			Long analysisGroupId, String stateType, String stateKind);

	List<AnalysisGroupState> getAnalysisGroupStatesByAnalysisGroupIdAndStateTypeKind(
			Long analysisGroupId, String stateType, String stateKind);

	AnalysisGroupState saveAnalysisGroupState(
			AnalysisGroupState analysisGroupState);

	Collection<AnalysisGroupState> saveAnalysisGroupStates(
			Collection<AnalysisGroupState> analysisGroupStates);

	
	
}
