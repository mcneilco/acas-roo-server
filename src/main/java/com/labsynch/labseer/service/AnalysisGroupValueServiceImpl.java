package com.labsynch.labseer.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.AnalysisGroupValue;


@Service
@Transactional
public class AnalysisGroupValueServiceImpl implements AnalysisGroupValueService {

	@Override
	public List<AnalysisGroupValue> getAnalysisGroupValuesByExperimentIdAndStateTypeKind(
			Long experimentId, String stateType, String stateKind) {
		
		List<AnalysisGroupValue> analysisGroupValues = AnalysisGroupValue.findAnalysisGroupValuesByExptIDAndStateTypeKind(experimentId, stateType, stateKind).getResultList();

		return analysisGroupValues;
	}
	
	public List<AnalysisGroupValue> getAnalysisGroupValuesByExperimentIdAndStateTypeKindAndValueTypeKind(Long experimentId, String stateType,
			String stateKind, String valueType, String valueKind) {
		
		List<AnalysisGroupValue> analysisGroupValues = AnalysisGroupValue.findAnalysisGroupValuesByExptIDAndStateTypeKindAndValueTypeKind(experimentId, stateType,
				stateKind, valueType, valueKind).getResultList();
		
		return analysisGroupValues;
	}

	@Override
	public String getCsvList(List<AnalysisGroupValue> AnalysisGroupValues) {
		// TODO Auto-generated method stub
		return "NEED TO IMPLEMENT";
	}


}
