package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.ExperimentState;

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
	
}
