package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.dto.AnalysisGroupStatePathDTO;
import com.labsynch.labseer.dto.GenericStatePathRequest;

@Service
public interface AnalysisGroupStateService {

	Collection<AnalysisGroupState> ignoreAllAnalysisGroupStates(Collection<AnalysisGroupState> analysisGroupStates);

	AnalysisGroupState createAnalysisGroupStateByAnalysisGroupIdAndStateTypeKind(
			Long analysisGroupId, String stateType, String stateKind);
	
	AnalysisGroupState createAnalysisGroupStateByAnalysisGroupIdAndStateTypeKindAndRecordedBy(
			Long analysisGroupId, String stateType, String stateKind, String recordedBy);

	List<AnalysisGroupState> getAnalysisGroupStatesByAnalysisGroupIdAndStateTypeKind(
			Long analysisGroupId, String stateType, String stateKind);

	AnalysisGroupState saveAnalysisGroupState(
			AnalysisGroupState analysisGroupState);

	Collection<AnalysisGroupState> saveAnalysisGroupStates(
			Collection<AnalysisGroupState> analysisGroupStates);

	AnalysisGroupState updateAnalysisGroupState(
			AnalysisGroupState analysisGroupState);

	Collection<AnalysisGroupState> updateAnalysisGroupStates(
			Collection<AnalysisGroupState> analysisGroupStates);

	AnalysisGroupState getAnalysisGroupState(String idOrCodeName,
			String stateType, String stateKind);

	Collection<AnalysisGroupStatePathDTO> getAnalysisGroupStates(
			Collection<GenericStatePathRequest> genericRequests);

	
	
}
