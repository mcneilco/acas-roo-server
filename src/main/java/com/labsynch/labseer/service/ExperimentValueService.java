package com.labsynch.labseer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.exceptions.UniqueExperimentNameException;

@Service
public interface ExperimentValueService {

	public ExperimentValue saveExperimentValue(ExperimentValue experimentValue);

	public void deleteExperimentValue(ExperimentValue experimentValue);

	public ExperimentValue updateExperimentValue(ExperimentValue experimentValue);
	
	public List<ExperimentValue> getExperimentValuesByExperimentId(Long id);
	
}
