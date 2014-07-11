package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.exceptions.UniqueExperimentNameException;
import com.labsynch.labseer.utils.PropertiesUtilService;


@Service
@Transactional
public class ExperimentValueServiceImpl implements ExperimentValueService {

	private static final Logger logger = LoggerFactory.getLogger(ExperimentValueServiceImpl.class);

	@Override
	public void deleteExperimentValue(ExperimentValue experimentValue){
		logger.debug("incoming meta experiment: " + experimentValue.toJson());

	}

	//Query hibernate object and grab existing table references - add them to json hydrated object
	@Override
	@Transactional
	public ExperimentValue updateExperimentValue(ExperimentValue experimentValue){
		if (experimentValue.getLsState().getId() == null) {
			ExperimentState experimentState = new ExperimentState(experimentValue.getLsState());
			experimentState.setExperiment(Experiment.findExperiment(experimentValue.getLsState().getExperiment().getId()));
			experimentState.persist();
			experimentValue.setLsState(experimentState); 
		} else {
			experimentValue.setLsState(ExperimentState.findExperimentState(experimentValue.getLsState().getId()));
		}		
		experimentValue.merge();
		return experimentValue;
	}

	//get experiment state from db and add as parent to object before save
	@Override
	@Transactional
	public ExperimentValue saveExperimentValue(ExperimentValue experimentValue) {
		if (experimentValue.getLsState().getId() == null) {
			ExperimentState experimentState = new ExperimentState(experimentValue.getLsState());
			experimentState.setExperiment(Experiment.findExperiment(experimentValue.getLsState().getExperiment().getId()));
			experimentState.persist();
			experimentValue.setLsState(experimentState); 
		} else {
			experimentValue.setLsState(ExperimentState.findExperimentState(experimentValue.getLsState().getId()));
		}		
		experimentValue.persist();
		return experimentValue;
	}

	@Override
	public List<ExperimentValue> getExperimentValuesByExperimentId(Long id){	
		List<ExperimentValue> experimentValues = new ArrayList<ExperimentValue>();
		Experiment experiment = Experiment.findExperiment(id);
		if(experiment.getLsStates() != null) {
			for (ExperimentState experimentState : experiment.getLsStates()) {
				if(experimentState.getLsValues() != null) {
					for(ExperimentValue experimentValue : experimentState.getLsValues()) {
						experimentValues.add(experimentValue);
					}
				}
			}
		}
		return experimentValues;
	}
}
