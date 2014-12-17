package com.labsynch.labseer.service;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ContainerValue;
import com.labsynch.labseer.domain.ItxExperimentExperimentState;
import com.labsynch.labseer.domain.ItxExperimentExperimentValue;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Service
@Transactional
public class ItxExperimentExperimentValueServiceImpl implements ItxExperimentExperimentValueService {

	private static final Logger logger = LoggerFactory.getLogger(ItxExperimentExperimentValueServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	public ItxExperimentExperimentValue updateItxExperimentExperimentValue(ItxExperimentExperimentValue itxExperimentExperimentValue) {
		itxExperimentExperimentValue.setVersion(ItxExperimentExperimentValue.findItxExperimentExperimentValue(itxExperimentExperimentValue.getId()).getVersion());
		itxExperimentExperimentValue.merge();
		return itxExperimentExperimentValue;
	}

	@Override
	public Collection<ItxExperimentExperimentValue> updateItxExperimentExperimentValues(
			Collection<ItxExperimentExperimentValue> itxExperimentExperimentValues) {
		for (ItxExperimentExperimentValue itxExperimentExperimentValue : itxExperimentExperimentValues){
			itxExperimentExperimentValue = updateItxExperimentExperimentValue(itxExperimentExperimentValue);
		}
		return null;
	}

	@Override
	public ItxExperimentExperimentValue saveItxExperimentExperimentValue(ItxExperimentExperimentValue itxExperimentExperimentValue) {
		itxExperimentExperimentValue.setLsState(ItxExperimentExperimentState.findItxExperimentExperimentState(itxExperimentExperimentValue.getLsState().getId()));		
		itxExperimentExperimentValue.persist();
		return itxExperimentExperimentValue;
	}

	@Override
	public Collection<ItxExperimentExperimentValue> saveItxExperimentExperimentValues(
			Collection<ItxExperimentExperimentValue> itxExperimentExperimentValues) {
		for (ItxExperimentExperimentValue itxExperimentExperimentValue: itxExperimentExperimentValues) {
			itxExperimentExperimentValue = saveItxExperimentExperimentValue(itxExperimentExperimentValue);
		}
		return itxExperimentExperimentValues;
	}



}
