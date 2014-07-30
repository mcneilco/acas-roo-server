package com.labsynch.labseer.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.ExperimentState;


@Service
@Transactional
public class ExperimentStateServiceImpl implements ExperimentStateService {

	@Override
	public List<ExperimentState> getExperimentStatesByExperimentIdAndStateTypeKind(Long experimentId, String stateType, 
			String stateKind) {	
		
		List<ExperimentState> experimentStates = ExperimentState.findExperimentStatesByExptIDAndStateTypeKind(experimentId, stateType, stateKind).getResultList();

		return experimentStates;
	}

	@Override
	public String getCsvList(List<ExperimentState> experimentStates) {
		// TODO Auto-generated method stub
		return "NEED TO IMPLEMENT";
	}
}
