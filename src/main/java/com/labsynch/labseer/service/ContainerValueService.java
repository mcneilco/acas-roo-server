package com.labsynch.labseer.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.ContainerValue;

@Service
public interface ContainerValueService {

	ContainerValue updateContainerValue(ContainerValue containerValue);

	Collection<ContainerValue> updateContainerValues(
			Collection<ContainerValue> containerValues);

	ContainerValue saveContainerValue(ContainerValue containerValue);

	Collection<ContainerValue> saveContainerValues(
			Collection<ContainerValue> containerValues);

}
