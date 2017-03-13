package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.dto.GenericValuePathRequest;
import com.labsynch.labseer.dto.TreatmentGroupValuePathDTO;

@Service
public interface TreatmentGroupValueService {

	public List<TreatmentGroupValue> getTreatmentGroupValuesByExperimentIdAndStateTypeKind(Long experimentId, String stateType, 
			String stateKind);
	
	public List<TreatmentGroupValue> getTreatmentGroupValuesByExperimentIdAndStateTypeKindAndValueTypeKind(Long experimentId, String stateType,
			String stateKind, String valueType, String valueKind);

	public String getCsvList(List<TreatmentGroupValue> treatmentGroupValues);
	
	public List<TreatmentGroupValue> getTreatmentGroupValuesByAnalysisGroupIdAndStateTypeKind(Long analysisGroupId, String stateType, String stateKind);

	public TreatmentGroupValue updateTreatmentGroupValue(String idOrCodeName,
			String stateType, String stateKind, String valueType,
			String valueKind, String value);

	TreatmentGroupValue updateTreatmentGroupValue(
			TreatmentGroupValue treatmentGroupValue);

	TreatmentGroupValue saveTreatmentGroupValue(
			TreatmentGroupValue treatmentGroupValue);

	Collection<TreatmentGroupValue> saveTreatmentGroupValues(
			Collection<TreatmentGroupValue> treatmentGroupValues);

	public Collection<TreatmentGroupValue> updateTreatmentGroupValues(
			Collection<TreatmentGroupValue> treatmentGroupValues);

	public TreatmentGroupValue getTreatmentGroupValue(String idOrCodeName,
			String stateType, String stateKind, String valueType,
			String valueKind);

	public Collection<TreatmentGroupValuePathDTO> getTreatmentGroupValues(
			Collection<GenericValuePathRequest> genericRequests);
	
}
