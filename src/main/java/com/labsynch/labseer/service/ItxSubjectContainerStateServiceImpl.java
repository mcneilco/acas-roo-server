package com.labsynch.labseer.service;

import java.util.Collection;

import com.labsynch.labseer.domain.ItxSubjectContainer;
import com.labsynch.labseer.domain.ItxSubjectContainerState;
import com.labsynch.labseer.utils.PropertiesUtilService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItxSubjectContainerStateServiceImpl implements ItxSubjectContainerStateService {

	private static final Logger logger = LoggerFactory.getLogger(ItxSubjectContainerStateServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	public ItxSubjectContainerState updateItxSubjectContainerState(ItxSubjectContainerState itxSubjectContainerState) {
		itxSubjectContainerState.setVersion(
				ItxSubjectContainerState.findItxSubjectContainerState(itxSubjectContainerState.getId()).getVersion());
		itxSubjectContainerState.merge();
		return itxSubjectContainerState;
	}

	@Override
	public Collection<ItxSubjectContainerState> updateItxSubjectContainerStates(
			Collection<ItxSubjectContainerState> itxSubjectContainerStates) {
		for (ItxSubjectContainerState itxSubjectContainerState : itxSubjectContainerStates) {
			itxSubjectContainerState = updateItxSubjectContainerState(itxSubjectContainerState);
		}
		return null;
	}

	@Override
	public ItxSubjectContainerState saveItxSubjectContainerState(ItxSubjectContainerState itxSubjectContainerState) {
		itxSubjectContainerState.setItxSubjectContainer(
				ItxSubjectContainer.findItxSubjectContainer(itxSubjectContainerState.getItxSubjectContainer().getId()));
		itxSubjectContainerState.persist();
		return itxSubjectContainerState;
	}

	@Override
	public Collection<ItxSubjectContainerState> saveItxSubjectContainerStates(
			Collection<ItxSubjectContainerState> itxSubjectContainerStates) {
		for (ItxSubjectContainerState itxSubjectContainerState : itxSubjectContainerStates) {
			itxSubjectContainerState = saveItxSubjectContainerState(itxSubjectContainerState);
		}
		return itxSubjectContainerStates;
	}

}
