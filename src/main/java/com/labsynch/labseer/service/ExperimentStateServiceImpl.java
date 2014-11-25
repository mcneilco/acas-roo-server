package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentState;


@Service
@Transactional
public class ExperimentStateServiceImpl implements ExperimentStateService {

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
	public ExperimentState saveExperimentState(
			ExperimentState experimentState) {
		experimentState.setExperiment(Experiment.findExperiment(experimentState.getExperiment().getId()));		
		experimentState.persist();
		return experimentState;
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
}
