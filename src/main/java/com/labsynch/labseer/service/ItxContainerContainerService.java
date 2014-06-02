package com.labsynch.labseer.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.ItxContainerContainer;

@Service
public interface ItxContainerContainerService {

	ItxContainerContainer saveLsItxContainer(ItxContainerContainer itxContainer);

	Collection<ItxContainerContainer> saveLsItxContainers(String json);
	
	
	
}
