package com.labsynch.labseer.service;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.ItxExperimentExperiment;
import com.labsynch.labseer.domain.ItxExperimentExperimentState;
import com.labsynch.labseer.domain.ItxExperimentExperimentValue;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Service
@Transactional
public class ItxExperimentExperimentStateServiceImpl implements ItxExperimentExperimentStateService {

	private static final Logger logger = LoggerFactory.getLogger(ItxExperimentExperimentStateServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	public ItxExperimentExperimentState updateItxExperimentExperimentState(ItxExperimentExperimentState itxExperimentExperimentState) {
		itxExperimentExperimentState.setVersion(ItxExperimentExperimentState.findItxExperimentExperimentState(itxExperimentExperimentState.getId()).getVersion());
		itxExperimentExperimentState.merge();
		return itxExperimentExperimentState;
	}

	@Override
	public Collection<ItxExperimentExperimentState> updateItxExperimentExperimentStates(
			Collection<ItxExperimentExperimentState> itxExperimentExperimentStates) {
		for (ItxExperimentExperimentState itxExperimentExperimentState : itxExperimentExperimentStates){
			itxExperimentExperimentState = updateItxExperimentExperimentState(itxExperimentExperimentState);
		}
		return null;
	}

	@Override
	public ItxExperimentExperimentState saveItxExperimentExperimentState(ItxExperimentExperimentState itxExperimentExperimentState) {
		itxExperimentExperimentState.setItxExperimentExperiment(ItxExperimentExperiment.findItxExperimentExperiment(itxExperimentExperimentState.getItxExperimentExperiment().getId()));		
		itxExperimentExperimentState.persist();
		return itxExperimentExperimentState;
	}

	@Override
	public Collection<ItxExperimentExperimentState> saveItxExperimentExperimentStates(
			Collection<ItxExperimentExperimentState> itxExperimentExperimentStates) {
		for (ItxExperimentExperimentState itxExperimentExperimentState: itxExperimentExperimentStates) {
			itxExperimentExperimentState = saveItxExperimentExperimentState(itxExperimentExperimentState);
		}
		return itxExperimentExperimentStates;
	}



}
