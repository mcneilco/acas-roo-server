package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ContainerValue;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Service
@Transactional
public class ContainerValueServiceImpl implements ContainerValueService {

	private static final Logger logger = LoggerFactory.getLogger(ContainerValueServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	public ContainerValue updateContainerValue(ContainerValue containerValue) {
		containerValue.setVersion(ContainerValue.findContainerValue(containerValue.getId()).getVersion());
		containerValue.merge();
		return containerValue;
	}

	@Override
	public Collection<ContainerValue> updateContainerValues(
			Collection<ContainerValue> containerValues) {
		for (ContainerValue containerValue : containerValues){
			containerValue = updateContainerValue(containerValue);
		}
		return null;
	}

	@Override
	public ContainerValue saveContainerValue(ContainerValue containerValue) {
		containerValue.setLsState(ContainerState.findContainerState(containerValue.getLsState().getId()));		
		containerValue.persist();
		return containerValue;
	}

	@Override
	public Collection<ContainerValue> saveContainerValues(
			Collection<ContainerValue> containerValues) {
		Collection<ContainerValue> savedContainerValues = new ArrayList<ContainerValue>();
		for (ContainerValue containerValue: containerValues) {
			ContainerValue savedContainerValue = saveContainerValue(containerValue);
			savedContainerValues.add(savedContainerValue);
		}
		return savedContainerValues;
	}



}
