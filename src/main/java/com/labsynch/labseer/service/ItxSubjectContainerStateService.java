package com.labsynch.labseer.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.ItxSubjectContainerState;

@Service
public interface ItxSubjectContainerStateService {

	ItxSubjectContainerState updateItxSubjectContainerState(ItxSubjectContainerState itxSubjectContainerState);

	Collection<ItxSubjectContainerState> updateItxSubjectContainerStates(
			Collection<ItxSubjectContainerState> itxSubjectContainerStates);

	ItxSubjectContainerState saveItxSubjectContainerState(ItxSubjectContainerState itxSubjectContainerState);

	Collection<ItxSubjectContainerState> saveItxSubjectContainerStates(
			Collection<ItxSubjectContainerState> itxSubjectContainerStates);

}
