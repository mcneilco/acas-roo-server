package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.dto.ContainerStatePathDTO;
import com.labsynch.labseer.dto.ContainerValueRequestDTO;
import com.labsynch.labseer.dto.GenericStatePathRequest;

import org.springframework.stereotype.Service;

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

	ContainerState getContainerState(String idOrCodeName, String stateType,
			String stateKind);

	List<ContainerState> getContainerStatesByContainerIdAndStateTypeKind(
			Long containerId, String stateType, String stateKind);

	ContainerState createContainerStateByContainerIdAndStateTypeKind(
			Long containerId, String stateType, String stateKind);

	ContainerState createContainerStateByContainerIdAndStateTypeKindAndRecordedBy(
			Long containerId, String stateType, String stateKind,
			String recordedBy);

	Collection<ContainerStatePathDTO> getContainerStates(
			Collection<GenericStatePathRequest> genericRequests);

	Collection<ContainerState> getContainerStatesByContainerValue(
			ContainerValueRequestDTO requestDTO, Boolean like, Boolean rightLike)
			throws Exception;

	
	
}
