package com.labsynch.labseer.service;

import java.util.Collection;

import com.labsynch.labseer.domain.ItxContainerContainerState;
import com.labsynch.labseer.domain.ItxContainerContainerValue;
import com.labsynch.labseer.utils.PropertiesUtilService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItxContainerContainerValueServiceImpl implements ItxContainerContainerValueService {

	private static final Logger logger = LoggerFactory.getLogger(ItxContainerContainerValueServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	public ItxContainerContainerValue updateItxContainerContainerValue(
			ItxContainerContainerValue itxContainerContainerValue) {
		itxContainerContainerValue.setVersion(ItxContainerContainerValue
				.findItxContainerContainerValue(itxContainerContainerValue.getId()).getVersion());
		itxContainerContainerValue.merge();
		return itxContainerContainerValue;
	}

	@Override
	public Collection<ItxContainerContainerValue> updateItxContainerContainerValues(
			Collection<ItxContainerContainerValue> itxContainerContainerValues) {
		for (ItxContainerContainerValue itxContainerContainerValue : itxContainerContainerValues) {
			itxContainerContainerValue = updateItxContainerContainerValue(itxContainerContainerValue);
		}
		return null;
	}

	@Override
	public ItxContainerContainerValue saveItxContainerContainerValue(
			ItxContainerContainerValue itxContainerContainerValue) {
		itxContainerContainerValue.setLsState(ItxContainerContainerState
				.findItxContainerContainerState(itxContainerContainerValue.getLsState().getId()));
		itxContainerContainerValue.persist();
		return itxContainerContainerValue;
	}

	@Override
	public Collection<ItxContainerContainerValue> saveItxContainerContainerValues(
			Collection<ItxContainerContainerValue> itxContainerContainerValues) {
		for (ItxContainerContainerValue itxContainerContainerValue : itxContainerContainerValues) {
			itxContainerContainerValue = saveItxContainerContainerValue(itxContainerContainerValue);
		}
		return itxContainerContainerValues;
	}

}
