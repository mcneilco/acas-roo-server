package com.labsynch.labseer.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.ItxExperimentExperiment;

@Service
public interface ItxExperimentExperimentService {

	ItxExperimentExperiment saveLsItxExperiment(ItxExperimentExperiment itxExperiment) throws Exception;

	Collection<ItxExperimentExperiment> saveLsItxExperiments(String json);
	
	Collection<ItxExperimentExperiment> saveLsItxExperiments(Collection<ItxExperimentExperiment> itxExperimentExperiments) throws Exception;

	ItxExperimentExperiment updateItxExperimentExperiment(
			ItxExperimentExperiment jsonItxExperimentExperiment);
	
	
	
}
