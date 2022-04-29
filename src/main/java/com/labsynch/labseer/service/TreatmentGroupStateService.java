package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.TreatmentGroupState;
import com.labsynch.labseer.dto.GenericStatePathRequest;
import com.labsynch.labseer.dto.TreatmentGroupStatePathDTO;

import org.springframework.stereotype.Service;

@Service
public interface TreatmentGroupStateService {

	Collection<TreatmentGroupState> ignoreAllTreatmentGroupStates(Collection<TreatmentGroupState> subjectStates);

	List<TreatmentGroupState> getTreatmentGroupStatesByTreatmentGroupIdAndStateTypeKind(
			Long treatmentGroupId, String stateType, String stateKind);

	TreatmentGroupState createTreatmentGroupStateByTreatmentGroupIdAndStateTypeKind(
			Long treatmentGroupId, String stateType, String stateKind);

	TreatmentGroupState saveTreatmentGroupState(
			TreatmentGroupState treatmentGroupState);

	Collection<TreatmentGroupState> saveTreatmentGroupStates(
			Collection<TreatmentGroupState> treatmentGroupStates);

	TreatmentGroupState updateTreatmentGroupState(
			TreatmentGroupState treatmentGroupState);

	Collection<TreatmentGroupState> updateTreatmentGroupStates(
			Collection<TreatmentGroupState> treatmentGroupStates);

	TreatmentGroupState getTreatmentGroupState(String idOrCodeName,
			String stateType, String stateKind);

	Collection<TreatmentGroupStatePathDTO> getTreatmentGroupStates(
			Collection<GenericStatePathRequest> genericRequests);

	
	
}
