package com.labsynch.labseer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.ExperimentValue;

@Service
public interface ExperimentValueService {

	public ExperimentValue saveExperimentValue(ExperimentValue experimentValue);

	public ExperimentValue updateExperimentValue(ExperimentValue experimentValue);
	
	public List<ExperimentValue> getExperimentValuesByExperimentId(Long id);

	public List<ExperimentValue> getExperimentValuesByExperimentIdAndStateTypeKindAndValueTypeKind(
			Long experimentId, String stateType, String stateKind,
			String valueType, String valueKind);

	public String getCsvList(List<ExperimentValue> experimentValues);
	
}
