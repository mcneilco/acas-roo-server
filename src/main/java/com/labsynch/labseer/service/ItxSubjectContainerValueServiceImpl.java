package com.labsynch.labseer.service;

import java.util.Collection;

import com.labsynch.labseer.domain.ItxSubjectContainerState;
import com.labsynch.labseer.domain.ItxSubjectContainerValue;
import com.labsynch.labseer.utils.PropertiesUtilService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItxSubjectContainerValueServiceImpl implements ItxSubjectContainerValueService {

	private static final Logger logger = LoggerFactory.getLogger(ItxSubjectContainerValueServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	public ItxSubjectContainerValue updateItxSubjectContainerValue(ItxSubjectContainerValue itxSubjectContainerValue) {
		itxSubjectContainerValue.setVersion(
				ItxSubjectContainerValue.findItxSubjectContainerValue(itxSubjectContainerValue.getId()).getVersion());
		itxSubjectContainerValue.merge();
		return itxSubjectContainerValue;
	}

	@Override
	public Collection<ItxSubjectContainerValue> updateItxSubjectContainerValues(
			Collection<ItxSubjectContainerValue> itxSubjectContainerValues) {
		for (ItxSubjectContainerValue itxSubjectContainerValue : itxSubjectContainerValues) {
			itxSubjectContainerValue = updateItxSubjectContainerValue(itxSubjectContainerValue);
		}
		return null;
	}

	@Override
	public ItxSubjectContainerValue saveItxSubjectContainerValue(ItxSubjectContainerValue itxSubjectContainerValue) {
		itxSubjectContainerValue.setLsState(
				ItxSubjectContainerState.findItxSubjectContainerState(itxSubjectContainerValue.getLsState().getId()));
		itxSubjectContainerValue.persist();
		return itxSubjectContainerValue;
	}

	@Override
	public Collection<ItxSubjectContainerValue> saveItxSubjectContainerValues(
			Collection<ItxSubjectContainerValue> itxSubjectContainerValues) {
		for (ItxSubjectContainerValue itxSubjectContainerValue : itxSubjectContainerValues) {
			itxSubjectContainerValue = saveItxSubjectContainerValue(itxSubjectContainerValue);
		}
		return itxSubjectContainerValues;
	}

}
