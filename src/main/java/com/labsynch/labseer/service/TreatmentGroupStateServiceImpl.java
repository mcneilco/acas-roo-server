package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.domain.TreatmentGroupState;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.dto.GenericStatePathRequest;
import com.labsynch.labseer.dto.TreatmentGroupStatePathDTO;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

@Service
@Transactional
public class TreatmentGroupStateServiceImpl implements TreatmentGroupStateService {

	private static final Logger logger = LoggerFactory.getLogger(TreatmentGroupStateServiceImpl.class);
	
	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	public List<TreatmentGroupState> getTreatmentGroupStatesByTreatmentGroupIdAndStateTypeKind(
			Long treatmentGroupId, String stateType, String stateKind) {
		
			List<TreatmentGroupState> treatmentGroupStates = TreatmentGroupState.findTreatmentGroupStatesByTreatmentGroupIDAndStateTypeKind(treatmentGroupId, stateType, stateKind).getResultList();

			return treatmentGroupStates;
		
	}
	
	@Override
	public Collection<TreatmentGroupState> ignoreAllTreatmentGroupStates(Collection<TreatmentGroupState> treatmentGroupStates) {
		//mark treatmentGroupStates and values as ignore 
		Collection<TreatmentGroupState> treatmentGroupStateSet = new HashSet<TreatmentGroupState>();
		for (TreatmentGroupState queryTreatmentGroupState : treatmentGroupStates){
			TreatmentGroupState treatmentGroupState = TreatmentGroupState.findTreatmentGroupState(queryTreatmentGroupState.getId());			
				for(TreatmentGroupValue treatmentGroupValue : TreatmentGroupValue.findTreatmentGroupValuesByLsState(treatmentGroupState).getResultList()){
					treatmentGroupValue.setIgnored(true);
					treatmentGroupValue.merge();
				}
				treatmentGroupState.setIgnored(true);
				treatmentGroupState.merge();
				treatmentGroupStateSet.add(TreatmentGroupState.findTreatmentGroupState(treatmentGroupState.getId()));
		}

		return(treatmentGroupStateSet);

	}

	@Override
	public TreatmentGroupState createTreatmentGroupStateByTreatmentGroupIdAndStateTypeKind(Long treatmentGroupId, String stateType, String stateKind) {
		TreatmentGroupState treatmentGroupState = new TreatmentGroupState();
		TreatmentGroup treatmentGroup = TreatmentGroup.findTreatmentGroup(treatmentGroupId);
		treatmentGroupState.setTreatmentGroup(treatmentGroup);
		treatmentGroupState.setLsType(stateType);
		treatmentGroupState.setLsKind(stateKind);
		treatmentGroupState.setRecordedBy("default");
		treatmentGroupState.persist();
		return treatmentGroupState;
	}

	@Override
	public TreatmentGroupState saveTreatmentGroupState(
			TreatmentGroupState treatmentGroupState) {
		treatmentGroupState.setTreatmentGroup(TreatmentGroup.findTreatmentGroup(treatmentGroupState.getTreatmentGroup().getId()));		
		treatmentGroupState.persist();
		Set<TreatmentGroupValue> savedValues = new HashSet<TreatmentGroupValue>();
		for (TreatmentGroupValue treatmentGroupValue : treatmentGroupState.getLsValues()){
			treatmentGroupValue.setLsState(treatmentGroupState);
			treatmentGroupValue.persist();
			savedValues.add(treatmentGroupValue);
		}
		treatmentGroupState.setLsValues(savedValues);
		treatmentGroupState.merge();
		return treatmentGroupState;
	}

	@Override
	public Collection<TreatmentGroupState> saveTreatmentGroupStates(
			Collection<TreatmentGroupState> treatmentGroupStates) {
		for (TreatmentGroupState treatmentGroupState: treatmentGroupStates) {
			treatmentGroupState = saveTreatmentGroupState(treatmentGroupState);
		}
		return treatmentGroupStates;
	}
	
	@Override
	public TreatmentGroupState updateTreatmentGroupState(
			TreatmentGroupState treatmentGroupState) {
		treatmentGroupState.setVersion(TreatmentGroupState.findTreatmentGroupState(treatmentGroupState.getId()).getVersion());
		treatmentGroupState.merge();
		return treatmentGroupState;
	}

	@Override
	public Collection<TreatmentGroupState> updateTreatmentGroupStates(
			Collection<TreatmentGroupState> treatmentGroupStates) {
		for (TreatmentGroupState treatmentGroupState : treatmentGroupStates){
			treatmentGroupState = updateTreatmentGroupState(treatmentGroupState);
		}
		return null;
	}
	
	@Override
	public TreatmentGroupState getTreatmentGroupState(String idOrCodeName,
			String stateType, String stateKind) {
		TreatmentGroupState state = null;
		try{
			Collection<TreatmentGroupState> states = getTreatmentGroupStates(idOrCodeName, stateType, stateKind);
			state = states.iterator().next();
		}catch (Exception e){
			logger.error("Caught error "+e.toString()+" trying to find a state.",e);
			state = null;
		}
		return state;
	}

	@Override
	public Collection<TreatmentGroupStatePathDTO> getTreatmentGroupStates(
			Collection<GenericStatePathRequest> genericRequests) {
		Collection<TreatmentGroupStatePathDTO> results = new ArrayList<TreatmentGroupStatePathDTO>();
		for (GenericStatePathRequest request : genericRequests){
			TreatmentGroupStatePathDTO result = new TreatmentGroupStatePathDTO();
			result.setIdOrCodeName(request.getIdOrCodeName());
			result.setStateType(request.getStateType());
			result.setStateKind(request.getStateKind());
			result.setStates(getTreatmentGroupStates(request.getIdOrCodeName(), request.getStateType(), request.getStateKind()));
			results.add(result);
		}
		return results;
	}
	
	private Collection<TreatmentGroupState> getTreatmentGroupStates(String idOrCodeName, String stateType, String stateKind){
		if (SimpleUtil.isNumeric(idOrCodeName)){
			Long id = Long.valueOf(idOrCodeName);
			return TreatmentGroupState.findTreatmentGroupStatesByTreatmentGroupIDAndStateTypeKind(id, stateType, stateKind).getResultList();
		}else{
			return TreatmentGroupState.findTreatmentGroupStatesByTreatmentGroupCodeNameAndStateTypeKind(idOrCodeName, stateType, stateKind).getResultList();
		}
	}

}
