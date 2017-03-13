package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ContainerValue;
import com.labsynch.labseer.dto.ContainerValuePathDTO;
import com.labsynch.labseer.dto.GenericValuePathRequest;

@Service
public interface ContainerValueService {

	ContainerValue updateContainerValue(ContainerValue containerValue);

	Collection<ContainerValue> updateContainerValues(
			Collection<ContainerValue> containerValues);

	ContainerValue saveContainerValue(ContainerValue containerValue);

	Collection<ContainerValue> saveContainerValues(
			Collection<ContainerValue> containerValues);

	ContainerValue getContainerValue(String idOrCodeName, String stateType,
			String stateKind, String valueType, String valueKind);

	ContainerValue updateContainerValue(String idOrCodeName, String stateType,
			String stateKind, String valueType, String valueKind, String value);

	ContainerValue createContainerValueFromLsStateAndTypeAndKindAndValue(
			ContainerState containerState, String lsType, String lsKind,
			String value, String recordedBy);

	List<ContainerValue> getContainerValuesByContainerIdAndStateTypeKindAndValueTypeKind(
			Long containerId, String stateType, String stateKind,
			String valueType, String valueKind);

	Collection<ContainerValuePathDTO> getContainerValues(
			Collection<GenericValuePathRequest> genericRequests);

}
