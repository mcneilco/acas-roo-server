package com.labsynch.labseer.service;

import java.util.Collection;

import com.labsynch.labseer.domain.ItxExperimentExperiment;

import org.springframework.stereotype.Service;

@Service
public interface ItxExperimentExperimentService {

	ItxExperimentExperiment saveLsItxExperiment(ItxExperimentExperiment itxExperiment) throws Exception;

	Collection<ItxExperimentExperiment> saveLsItxExperiments(String json);

	Collection<ItxExperimentExperiment> saveLsItxExperiments(
			Collection<ItxExperimentExperiment> itxExperimentExperiments) throws Exception;

	ItxExperimentExperiment updateItxExperimentExperiment(
			ItxExperimentExperiment jsonItxExperimentExperiment);

}
