package com.labsynch.labseer.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.LsTransaction;

@Service
public interface ContainerStateService {

	Collection<ContainerState> ignoreAllContainerStates(Collection<ContainerState> containerStates);

	LsTransaction ignoreByContainer(String json, String lsKind) throws Exception;

	ContainerState updateContainerState(ContainerState containerState);

	Collection<ContainerState> updateContainerStates(
			Collection<ContainerState> containerStates);

	ContainerState saveContainerState(ContainerState containerState);

	Collection<ContainerState> saveContainerStates(
			Collection<ContainerState> containerStates);

	
	
}
