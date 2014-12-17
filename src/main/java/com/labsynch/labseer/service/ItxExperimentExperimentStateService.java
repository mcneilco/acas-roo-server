package com.labsynch.labseer.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.ItxExperimentExperimentState;

@Service
public interface ItxExperimentExperimentStateService {

	ItxExperimentExperimentState updateItxExperimentExperimentState(ItxExperimentExperimentState itxExperimentExperimentState);

	Collection<ItxExperimentExperimentState> updateItxExperimentExperimentStates(
			Collection<ItxExperimentExperimentState> itxExperimentExperimentStates);

	ItxExperimentExperimentState saveItxExperimentExperimentState(ItxExperimentExperimentState itxExperimentExperimentState);

	Collection<ItxExperimentExperimentState> saveItxExperimentExperimentStates(
			Collection<ItxExperimentExperimentState> itxExperimentExperimentStates);

}
