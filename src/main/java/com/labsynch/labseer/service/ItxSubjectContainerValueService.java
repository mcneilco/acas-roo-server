package com.labsynch.labseer.service;

import java.util.Collection;

import com.labsynch.labseer.domain.ItxSubjectContainerValue;

import org.springframework.stereotype.Service;

@Service
public interface ItxSubjectContainerValueService {

	ItxSubjectContainerValue updateItxSubjectContainerValue(ItxSubjectContainerValue itxSubjectContainerValue);

	Collection<ItxSubjectContainerValue> updateItxSubjectContainerValues(
			Collection<ItxSubjectContainerValue> itxSubjectContainerValues);

	ItxSubjectContainerValue saveItxSubjectContainerValue(ItxSubjectContainerValue itxSubjectContainerValue);

	Collection<ItxSubjectContainerValue> saveItxSubjectContainerValues(
			Collection<ItxSubjectContainerValue> itxSubjectContainerValues);

}
