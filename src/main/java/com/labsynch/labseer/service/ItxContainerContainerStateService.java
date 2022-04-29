package com.labsynch.labseer.service;

import java.util.Collection;

import com.labsynch.labseer.domain.ItxContainerContainerState;

import org.springframework.stereotype.Service;

@Service
public interface ItxContainerContainerStateService {

	ItxContainerContainerState updateItxContainerContainerState(ItxContainerContainerState itxContainerContainerState);

	Collection<ItxContainerContainerState> updateItxContainerContainerStates(
			Collection<ItxContainerContainerState> itxContainerContainerStates);

	ItxContainerContainerState saveItxContainerContainerState(ItxContainerContainerState itxContainerContainerState);

	Collection<ItxContainerContainerState> saveItxContainerContainerStates(
			Collection<ItxContainerContainerState> itxContainerContainerStates);

}
