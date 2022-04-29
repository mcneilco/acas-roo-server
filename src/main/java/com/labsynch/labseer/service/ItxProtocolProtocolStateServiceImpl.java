package com.labsynch.labseer.service;

import java.util.Collection;

import com.labsynch.labseer.domain.ItxProtocolProtocol;
import com.labsynch.labseer.domain.ItxProtocolProtocolState;
import com.labsynch.labseer.utils.PropertiesUtilService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItxProtocolProtocolStateServiceImpl implements ItxProtocolProtocolStateService {

	private static final Logger logger = LoggerFactory.getLogger(ItxProtocolProtocolStateServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	public ItxProtocolProtocolState updateItxProtocolProtocolState(ItxProtocolProtocolState itxProtocolProtocolState) {
		itxProtocolProtocolState.setVersion(
				ItxProtocolProtocolState.findItxProtocolProtocolState(itxProtocolProtocolState.getId()).getVersion());
		itxProtocolProtocolState.merge();
		return itxProtocolProtocolState;
	}

	@Override
	public Collection<ItxProtocolProtocolState> updateItxProtocolProtocolStates(
			Collection<ItxProtocolProtocolState> itxProtocolProtocolStates) {
		for (ItxProtocolProtocolState itxProtocolProtocolState : itxProtocolProtocolStates) {
			itxProtocolProtocolState = updateItxProtocolProtocolState(itxProtocolProtocolState);
		}
		return null;
	}

	@Override
	public ItxProtocolProtocolState saveItxProtocolProtocolState(ItxProtocolProtocolState itxProtocolProtocolState) {
		itxProtocolProtocolState.setItxProtocolProtocol(
				ItxProtocolProtocol.findItxProtocolProtocol(itxProtocolProtocolState.getItxProtocolProtocol().getId()));
		itxProtocolProtocolState.persist();
		return itxProtocolProtocolState;
	}

	@Override
	public Collection<ItxProtocolProtocolState> saveItxProtocolProtocolStates(
			Collection<ItxProtocolProtocolState> itxProtocolProtocolStates) {
		for (ItxProtocolProtocolState itxProtocolProtocolState : itxProtocolProtocolStates) {
			itxProtocolProtocolState = saveItxProtocolProtocolState(itxProtocolProtocolState);
		}
		return itxProtocolProtocolStates;
	}

}
