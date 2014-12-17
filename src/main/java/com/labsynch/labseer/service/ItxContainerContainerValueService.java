package com.labsynch.labseer.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.ItxContainerContainerValue;

@Service
public interface ItxContainerContainerValueService {

	ItxContainerContainerValue updateItxContainerContainerValue(ItxContainerContainerValue itxContainerContainerValue);

	Collection<ItxContainerContainerValue> updateItxContainerContainerValues(
			Collection<ItxContainerContainerValue> itxContainerContainerValues);

	ItxContainerContainerValue saveItxContainerContainerValue(ItxContainerContainerValue itxContainerContainerValue);

	Collection<ItxContainerContainerValue> saveItxContainerContainerValues(
			Collection<ItxContainerContainerValue> itxContainerContainerValues);

}
