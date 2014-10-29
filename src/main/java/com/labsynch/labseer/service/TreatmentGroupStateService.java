package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.TreatmentGroupState;

@Service
public interface TreatmentGroupStateService {

	Collection<TreatmentGroupState> ignoreAllTreatmentGroupStates(Collection<TreatmentGroupState> subjectStates);

	List<TreatmentGroupState> getTreatmentGroupStatesByTreatmentGroupIdAndStateTypeKind(
			Long treatmentGroupId, String stateType, String stateKind);

	TreatmentGroupState createTreatmentGroupStateByTreatmentGroupIdAndStateTypeKind(
			Long treatmentGroupId, String stateType, String stateKind);

	
	
}
