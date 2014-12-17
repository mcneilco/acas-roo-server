package com.labsynch.labseer.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.ItxExperimentExperimentValue;

@Service
public interface ItxExperimentExperimentValueService {

	ItxExperimentExperimentValue updateItxExperimentExperimentValue(ItxExperimentExperimentValue itxExperimentExperimentValue);

	Collection<ItxExperimentExperimentValue> updateItxExperimentExperimentValues(
			Collection<ItxExperimentExperimentValue> itxExperimentExperimentValues);

	ItxExperimentExperimentValue saveItxExperimentExperimentValue(ItxExperimentExperimentValue itxExperimentExperimentValue);

	Collection<ItxExperimentExperimentValue> saveItxExperimentExperimentValues(
			Collection<ItxExperimentExperimentValue> itxExperimentExperimentValues);

}
