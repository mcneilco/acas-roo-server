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
		
		List<AnalysisGroupValue> AnalysisGroupValues = AnalysisGroupValue.findAnalysisGroupValuesByExptIDAndStateTypeKind(experimentId, stateType, stateKind).getResultList();

		return AnalysisGroupValues;
	}

	@Override
	public String getCsvList(List<AnalysisGroupValue> AnalysisGroupValues) {
		// TODO Auto-generated method stub
		return "NEED TO IMPLEMENT";
	}


}
