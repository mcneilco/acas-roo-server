package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ExperimentValuePathDTO;
import com.labsynch.labseer.dto.GenericValuePathRequest;
import com.labsynch.labseer.dto.StateValueDTO;

import org.springframework.stereotype.Service;

@Service
public interface ExperimentValueService {

	public ExperimentValue saveExperimentValue(ExperimentValue experimentValue);

	public ExperimentValue updateExperimentValue(ExperimentValue experimentValue);

	public List<ExperimentValue> getExperimentValuesByExperimentId(Long id);

	public List<ExperimentValue> getExperimentValuesByExperimentIdAndStateTypeKindAndValueTypeKind(
			Long experimentId, String stateType, String stateKind,
			String valueType, String valueKind);

	public List<ExperimentValue> getExperimentValuesByExperimentIdAndStateTypeKind(Long experimentId, String stateType,
			String stateKind);

	public String getCsvList(List<ExperimentValue> experimentValues);

	public List<StateValueDTO> getKeyValueList(List<ExperimentValue> experimentValues);

	List<CodeTableDTO> convertToCodeTables(
			List<ExperimentValue> experimentValues);

	ExperimentValue updateExperimentValue(String idOrCodeName,
			String stateType, String stateKind, String valueType,
			String valueKind, String value);

	Collection<ExperimentValue> saveExperimentValues(
			Collection<ExperimentValue> experimentValues);

	public Collection<ExperimentValue> updateExperimentValues(
			Collection<ExperimentValue> experimentValues);

	public ExperimentValue getExperimentValue(String idOrCodeName,
			String stateType, String stateKind, String valueType,
			String valueKind);

	public Collection<ExperimentValuePathDTO> getExperimentValues(
			Collection<GenericValuePathRequest> genericRequests);

}
