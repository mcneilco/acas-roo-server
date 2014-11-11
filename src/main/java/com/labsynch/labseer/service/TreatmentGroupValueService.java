package com.labsynch.labseer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.TreatmentGroupValue;

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
	
}
