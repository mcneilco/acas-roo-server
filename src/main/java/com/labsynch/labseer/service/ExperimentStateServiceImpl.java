package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.dto.ExperimentStatePathDTO;
import com.labsynch.labseer.dto.GenericStatePathRequest;
import com.labsynch.labseer.utils.SimpleUtil;


@Service
@Transactional
public class ExperimentStateServiceImpl implements ExperimentStateService {
	
	private static final Logger logger = LoggerFactory.getLogger(ExperimentStateServiceImpl.class);

	@Override
	public List<ExperimentState> getExperimentStatesByExperimentIdAndStateTypeKind(Long experimentId, String stateType, 
			String stateKind) {	
		
		List<ExperimentState> experimentStates = ExperimentState.findExperimentStatesByExptIDAndStateTypeKind(experimentId, stateType, stateKind).getResultList();

		return experimentStates;
	}

	@Override
	public String getCsvList(List<ExperimentState> experimentStates) {
		// TODO Auto-generated method stub
		return "NEED TO IMPLEMENT";
	}
	
	@Override
	public ExperimentState createExperimentStateByExperimentIdAndStateTypeKind(Long experimentId, String stateType, String stateKind) {
		ExperimentState experimentState = new ExperimentState();
		Experiment experiment = Experiment.findExperiment(experimentId);
		experimentState.setExperiment(experiment);
		experimentState.setLsType(stateType);
		experimentState.setLsKind(stateKind);
		experimentState.setRecordedBy("default");
		experimentState.persist();
		return experimentState;
	}
	
	@Override
	public ExperimentState saveExperimentState(ExperimentState experimentState) {
		ExperimentState newExperimentState = new ExperimentState(experimentState);
		newExperimentState.setExperiment(Experiment.findExperiment(experimentState.getExperiment().getId()));		
		newExperimentState.persist();
		Set<ExperimentValue> savedValues = new HashSet<ExperimentValue>();
		for (ExperimentValue experimentValue : experimentState.getLsValues()){
			experimentValue.setLsState(newExperimentState);
			experimentValue.persist();
			savedValues.add(experimentValue);
		}
		newExperimentState.setLsValues(savedValues);
		newExperimentState.merge();
		return newExperimentState;
	}

	@Override
	public Collection<ExperimentState> saveExperimentStates(
			Collection<ExperimentState> experimentStates) {
		for (ExperimentState experimentState: experimentStates) {
			experimentState = saveExperimentState(experimentState);
		}
		return experimentStates;
	}
	
	@Override
	public ExperimentState updateExperimentState(
			ExperimentState experimentState) {
		experimentState.setVersion(ExperimentState.findExperimentState(experimentState.getId()).getVersion());
		experimentState.merge();
		return experimentState;
	}

	@Override
	public Collection<ExperimentState> updateExperimentStates(
			Collection<ExperimentState> experimentStates) {
		for (ExperimentState experimentState : experimentStates){
			experimentState = updateExperimentState(experimentState);
		}
		return null;
	}
	
	@Override
	public ExperimentState getExperimentState(String idOrCodeName,
			String stateType, String stateKind) {
		ExperimentState state = null;
		try{
			Collection<ExperimentState> states = getExperimentStates(idOrCodeName, stateType, stateKind);
			state = states.iterator().next();
		}catch (Exception e){
			logger.error("Caught error "+e.toString()+" trying to find a state.",e);
			state = null;
		}
		return state;
	}

	@Override
	public Collection<ExperimentStatePathDTO> getExperimentStates(
			Collection<GenericStatePathRequest> genericRequests) {
		Collection<ExperimentStatePathDTO> results = new ArrayList<ExperimentStatePathDTO>();
		for (GenericStatePathRequest request : genericRequests){
			ExperimentStatePathDTO result = new ExperimentStatePathDTO();
			result.setIdOrCodeName(request.getIdOrCodeName());
			result.setStateType(request.getStateType());
			result.setStateKind(request.getStateKind());
			result.setStates(getExperimentStates(request.getIdOrCodeName(), request.getStateType(), request.getStateKind()));
			results.add(result);
		}
		return results;
	}
	
	private Collection<ExperimentState> getExperimentStates(String idOrCodeName, String stateType, String stateKind){
		if (SimpleUtil.isNumeric(idOrCodeName)){
			Long id = Long.valueOf(idOrCodeName);
			return ExperimentState.findExperimentStatesByExptIDAndStateTypeKind(id, stateType, stateKind).getResultList();
		}else{
			return ExperimentState.findExperimentStatesByExperimentCodeNameAndStateTypeKind(idOrCodeName, stateType, stateKind).getResultList();
		}
	}
}
