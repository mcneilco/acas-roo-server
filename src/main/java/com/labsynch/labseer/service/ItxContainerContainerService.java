package com.labsynch.labseer.service;

import java.util.Collection;

import com.labsynch.labseer.domain.ItxContainerContainer;

import org.springframework.stereotype.Service;

@Service
public interface ItxContainerContainerService {

	ItxContainerContainer saveLsItxContainer(ItxContainerContainer itxContainer);

	Collection<ItxContainerContainer> saveLsItxContainers(String json);

	Collection<ItxContainerContainer> saveLsItxContainers(Collection<ItxContainerContainer> itxContainerContainers);

	ItxContainerContainer updateItxContainerContainer(
			ItxContainerContainer jsonItxContainerContainer);

}
