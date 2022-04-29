package com.labsynch.labseer.service;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ContainerValue;
import com.labsynch.labseer.domain.ItxProtocolProtocolState;
import com.labsynch.labseer.domain.ItxProtocolProtocolValue;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Service
@Transactional
public class ItxProtocolProtocolValueServiceImpl implements ItxProtocolProtocolValueService {

	private static final Logger logger = LoggerFactory.getLogger(ItxProtocolProtocolValueServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	public ItxProtocolProtocolValue updateItxProtocolProtocolValue(ItxProtocolProtocolValue itxProtocolProtocolValue) {
		itxProtocolProtocolValue.setVersion(
				ItxProtocolProtocolValue.findItxProtocolProtocolValue(itxProtocolProtocolValue.getId()).getVersion());
		itxProtocolProtocolValue.merge();
		return itxProtocolProtocolValue;
	}

	@Override
	public Collection<ItxProtocolProtocolValue> updateItxProtocolProtocolValues(
			Collection<ItxProtocolProtocolValue> itxProtocolProtocolValues) {
		for (ItxProtocolProtocolValue itxProtocolProtocolValue : itxProtocolProtocolValues) {
			itxProtocolProtocolValue = updateItxProtocolProtocolValue(itxProtocolProtocolValue);
		}
		return null;
	}

	@Override
	public ItxProtocolProtocolValue saveItxProtocolProtocolValue(ItxProtocolProtocolValue itxProtocolProtocolValue) {
		itxProtocolProtocolValue.setLsState(
				ItxProtocolProtocolState.findItxProtocolProtocolState(itxProtocolProtocolValue.getLsState().getId()));
		itxProtocolProtocolValue.persist();
		return itxProtocolProtocolValue;
	}

	@Override
	public Collection<ItxProtocolProtocolValue> saveItxProtocolProtocolValues(
			Collection<ItxProtocolProtocolValue> itxProtocolProtocolValues) {
		for (ItxProtocolProtocolValue itxProtocolProtocolValue : itxProtocolProtocolValues) {
			itxProtocolProtocolValue = saveItxProtocolProtocolValue(itxProtocolProtocolValue);
		}
		return itxProtocolProtocolValues;
	}

}
