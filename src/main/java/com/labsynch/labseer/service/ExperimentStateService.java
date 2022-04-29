package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.dto.ExperimentStatePathDTO;
import com.labsynch.labseer.dto.GenericStatePathRequest;

import org.springframework.stereotype.Service;

@Service
public interface ExperimentStateService {
	
	public List<ExperimentState> getExperimentStatesByExperimentIdAndStateTypeKind(Long experimentId, String stateType, 
			String stateKind);

	public String getCsvList(List<ExperimentState> experimentStates);

	public ExperimentState createExperimentStateByExperimentIdAndStateTypeKind(
			Long experimentId, String stateType, String stateKind);

	public ExperimentState saveExperimentState(ExperimentState experimentState);

	public Collection<ExperimentState> saveExperimentStates(
			Collection<ExperimentState> experimentStates);

	public ExperimentState updateExperimentState(ExperimentState experimentState);

	public Collection<ExperimentState> updateExperimentStates(
			Collection<ExperimentState> experimentStates);

	public ExperimentState getExperimentState(String idOrCodeName,
			String stateType, String stateKind);

	public Collection<ExperimentStatePathDTO> getExperimentStates(
			Collection<GenericStatePathRequest> genericRequests);
	
}
