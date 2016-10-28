package com.labsynch.labseer.service;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.ItxContainerContainer;
import com.labsynch.labseer.domain.ItxContainerContainerState;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Service
@Transactional
public class ItxContainerContainerStateServiceImpl implements ItxContainerContainerStateService {

	private static final Logger logger = LoggerFactory.getLogger(ItxContainerContainerStateServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	public ItxContainerContainerState updateItxContainerContainerState(ItxContainerContainerState itxContainerContainerState) {
		itxContainerContainerState.setVersion(ItxContainerContainerState.findItxContainerContainerState(itxContainerContainerState.getId()).getVersion());
		itxContainerContainerState.merge();
		return itxContainerContainerState;
	}

	@Override
	public Collection<ItxContainerContainerState> updateItxContainerContainerStates(
			Collection<ItxContainerContainerState> itxContainerContainerStates) {
		for (ItxContainerContainerState itxContainerContainerState : itxContainerContainerStates){
			itxContainerContainerState = updateItxContainerContainerState(itxContainerContainerState);
		}
		return null;
	}

	@Override
	public ItxContainerContainerState saveItxContainerContainerState(ItxContainerContainerState itxContainerContainerState) {
		itxContainerContainerState.setItxContainerContainer(ItxContainerContainer.findItxContainerContainer(itxContainerContainerState.getItxContainerContainer().getId()));		
		itxContainerContainerState.persist();
		return itxContainerContainerState;
	}

	@Override
	public Collection<ItxContainerContainerState> saveItxContainerContainerStates(
			Collection<ItxContainerContainerState> itxContainerContainerStates) {
		for (ItxContainerContainerState itxContainerContainerState: itxContainerContainerStates) {
			itxContainerContainerState = saveItxContainerContainerState(itxContainerContainerState);
		}
		return itxContainerContainerStates;
	}



}
