package com.labsynch.labseer.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.Container;

@Service
public interface ContainerService {

	Container saveLsContainer(Container container);
	
	Collection<Container> saveLsContainers(Collection<Container> containers);

	Container updateContainer(Container fromJsonToContainer);

	Collection<Container> saveLsContainers(String json);

	Collection<Container> saveLsContainersParse(String json);

	Collection<Container> saveLsContainersFile(String jsonFile);

	Container saveLsContainer(String json);
	
	
	
}
