package com.labsynch.labseer.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.TreatmentGroupValue;


@Service
@Transactional
public class TreatmentGroupValueServiceImpl implements TreatmentGroupValueService {

	@Override
	public List<TreatmentGroupValue> getTreatmentGroupValuesByExperimentIdAndStateTypeKind(
			Long experimentId, String stateType, String stateKind) {
		
		List<TreatmentGroupValue> treatmentGroupValues = TreatmentGroupValue.findTreatmentGroupValuesByExptIDAndStateTypeKind(experimentId, stateType, stateKind).getResultList();

		return treatmentGroupValues;
	}
	
	public List<TreatmentGroupValue> getTreatmentGroupValuesByExperimentIdAndStateTypeKindAndValueTypeKind(Long experimentId, String stateType,
			String stateKind, String valueType, String valueKind) {
		
		List<TreatmentGroupValue> treatmentGroupValues = TreatmentGroupValue.findTreatmentGroupValuesByExptIDAndStateTypeKindAndValueTypeKind(experimentId, stateType,
				stateKind, valueType, valueKind).getResultList();
		
		return treatmentGroupValues;
	}

	@Override
	public String getCsvList(List<TreatmentGroupValue> treatmentGroupValues) {
		// TODO Auto-generated method stub
		return "NEED TO IMPLEMENT";
	}


}
