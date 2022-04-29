package com.labsynch.labseer.service;

import java.util.Collection;

import com.labsynch.labseer.domain.ItxExperimentExperimentState;

import org.springframework.stereotype.Service;

@Service
public interface ItxExperimentExperimentStateService {

	ItxExperimentExperimentState updateItxExperimentExperimentState(
			ItxExperimentExperimentState itxExperimentExperimentState);

	Collection<ItxExperimentExperimentState> updateItxExperimentExperimentStates(
			Collection<ItxExperimentExperimentState> itxExperimentExperimentStates);

	ItxExperimentExperimentState saveItxExperimentExperimentState(
			ItxExperimentExperimentState itxExperimentExperimentState);

	Collection<ItxExperimentExperimentState> saveItxExperimentExperimentStates(
			Collection<ItxExperimentExperimentState> itxExperimentExperimentStates);

}
