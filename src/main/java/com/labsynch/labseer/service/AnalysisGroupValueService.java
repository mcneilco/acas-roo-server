package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.AnalysisGroupValue;

@Service
public interface AnalysisGroupValueService {

	public List<AnalysisGroupValue> getAnalysisGroupValuesByExperimentIdAndStateTypeKind(Long experimentId, String stateType, 
			String stateKind);
	
	public List<AnalysisGroupValue> getAnalysisGroupValuesByExperimentIdAndStateTypeKindAndValueTypeKind(Long experimentId, String stateType,
			String stateKind, String valueType, String valueKind);

	public String getCsvList(List<AnalysisGroupValue> analysisGroupValues);
	
	public List<AnalysisGroupValue> getAnalysisGroupValuesByAnalysisGroupIdAndStateTypeKind(Long analysisGroupId, String stateType, String stateKind);
	
	public List<AnalysisGroupValue> getAnalysisGroupValuesByAnalysisGroupIdAndStateTypeKindAndValueTypeKind(Long analysisGroupId, String stateType, 
			String stateKind, String valueType, String valueKind);

	public AnalysisGroupValue updateAnalysisGroupValue(String idOrCodeName,
			String stateType, String stateKind, String valueType,
			String valueKind, String value);

	public AnalysisGroupValue updateAnalysisGroupValue(
			AnalysisGroupValue analysisGroupValue);

	public AnalysisGroupValue saveAnalysisGroupValue(
			AnalysisGroupValue analysisGroupValue);

	public Collection<AnalysisGroupValue> saveAnalysisGroupValues(
			Collection<AnalysisGroupValue> analysisGroupValues);


	public Collection<AnalysisGroupValue> updateAnalysisGroupValues(
			Collection<AnalysisGroupValue> analysisGroupValues);

	public AnalysisGroupValue createAnalysisGroupValueFromLsStateAndTypeAndKindAndValue(
			AnalysisGroupState analysisGroupState, String lsType, String lsKind,
			String value, String recordedBy);

	
}
