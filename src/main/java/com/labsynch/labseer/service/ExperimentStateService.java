package com.labsynch.labseer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.ExperimentState;

@Service
public interface ExperimentStateService {
	
	public List<ExperimentState> getExperimentStatesByExperimentIdAndStateTypeKind(Long experimentId, String stateType, 
			String stateKind);

	public String getCsvList(List<ExperimentState> experimentStates);
	
}
