package com.labsynch.labseer.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.ContainerValue;
import com.labsynch.labseer.domain.ItxProtocolProtocolState;

@Service
public interface ItxProtocolProtocolStateService {

	ItxProtocolProtocolState updateItxProtocolProtocolState(ItxProtocolProtocolState itxProtocolProtocolState);

	Collection<ItxProtocolProtocolState> updateItxProtocolProtocolStates(
			Collection<ItxProtocolProtocolState> itxProtocolProtocolStates);

	ItxProtocolProtocolState saveItxProtocolProtocolState(ItxProtocolProtocolState itxProtocolProtocolState);

	Collection<ItxProtocolProtocolState> saveItxProtocolProtocolStates(
			Collection<ItxProtocolProtocolState> itxProtocolProtocolStates);

}
