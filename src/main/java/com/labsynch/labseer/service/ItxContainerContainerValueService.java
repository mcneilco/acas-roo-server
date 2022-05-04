package com.labsynch.labseer.service;

import java.util.Collection;

import com.labsynch.labseer.domain.ItxContainerContainerValue;

import org.springframework.stereotype.Service;

@Service
public interface ItxContainerContainerValueService {

	ItxContainerContainerValue updateItxContainerContainerValue(ItxContainerContainerValue itxContainerContainerValue);

	Collection<ItxContainerContainerValue> updateItxContainerContainerValues(
			Collection<ItxContainerContainerValue> itxContainerContainerValues);

	ItxContainerContainerValue saveItxContainerContainerValue(ItxContainerContainerValue itxContainerContainerValue);

	Collection<ItxContainerContainerValue> saveItxContainerContainerValues(
			Collection<ItxContainerContainerValue> itxContainerContainerValues);

}
