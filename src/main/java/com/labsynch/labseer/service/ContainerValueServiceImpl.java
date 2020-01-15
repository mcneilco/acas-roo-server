package com.labsynch.labseer.service;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ContainerValue;
import com.labsynch.labseer.dto.ContainerValuePathDTO;
import com.labsynch.labseer.dto.GenericValuePathRequest;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

@Service
@Transactional
public class ContainerValueServiceImpl implements ContainerValueService {

	private static final Logger logger = LoggerFactory.getLogger(ContainerValueServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
	@Autowired
	private ContainerStateService containerStateService;

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
	
	@Override
	public ContainerValue getContainerValue(String idOrCodeName,
			String stateType, String stateKind, String valueType, String valueKind) {
		ContainerValue value = null;
		try{
			Collection<ContainerValue> values = getContainerValues(idOrCodeName, stateType, stateKind, valueType, valueKind);
			value = values.iterator().next();
		}catch (Exception e){
			logger.error("Caught error "+e.toString()+" trying to find a state.",e);
			value = null;
		}
		return value;
	}

	@Override
	public Collection<ContainerValuePathDTO> getContainerValues(
			Collection<GenericValuePathRequest> genericRequests) {
		Collection<ContainerValuePathDTO> results = new ArrayList<ContainerValuePathDTO>();
		for (GenericValuePathRequest request : genericRequests){
			ContainerValuePathDTO result = new ContainerValuePathDTO();
			result.setIdOrCodeName(request.getIdOrCodeName());
			result.setValueType(request.getValueType());
			result.setValueKind(request.getValueKind());
			result.setValues(getContainerValues(request.getIdOrCodeName(), request.getStateType(), request.getStateKind(), request.getValueType(), request.getValueKind()));
			results.add(result);
		}
		return results;
	}
	
	private Collection<ContainerValue> getContainerValues(String idOrCodeName, String stateType, String stateKind, String valueType, String valueKind){
		if (SimpleUtil.isNumeric(idOrCodeName)){
			Long id = Long.valueOf(idOrCodeName);
			return ContainerValue.findContainerValuesByContainerIDAndStateTypeKindAndValueTypeKind(id, stateType, stateKind, valueType, valueKind).getResultList();
		}else{
			return ContainerValue.findContainerValuesByContainerCodeNameAndStateTypeKindAndValueTypeKind(idOrCodeName, stateType, stateKind, valueType, valueKind).getResultList();
		}
	}
	
	@Override
	public ContainerValue updateContainerValue(String idOrCodeName, String stateType, String stateKind, String valueType, String valueKind, String value) {
		//fetch the entity
		Container container;
		if(SimpleUtil.isNumeric(idOrCodeName)) {
			container = Container.findContainer(Long.valueOf(idOrCodeName));
		} else {		
			try {
				container = Container.findContainerByCodeNameEquals(idOrCodeName);
			} catch(Exception ex) {
				container = null;
			}
		}
		//fetch the state, and if it doesn't exist, create it
		List<ContainerState> containerStates;
		if(container != null) {
			Long containerId = container.getId();
			containerStates = containerStateService.getContainerStatesByContainerIdAndStateTypeKind(containerId, stateType, stateKind);
			if (containerStates.isEmpty()) {
				//create the state
				containerStates.add(containerStateService.createContainerStateByContainerIdAndStateTypeKind(containerId, stateType, stateKind));
				logger.debug("Created the container state: " + containerStates.get(0).toJson());
			}
		}
		//fetch the value, update it if it exists, and if it doesn't exist, create it
		List<ContainerValue> containerValues;
		ContainerValue containerValue = null;
		if(container != null) {
			Long containerId = container.getId();
			containerValues = getContainerValuesByContainerIdAndStateTypeKindAndValueTypeKind(containerId, stateType, stateKind, valueType, valueKind);
			if (containerValues.size() > 1){
				logger.error("Error: multiple container statuses found");
			}
			else if (containerValues.size() == 1){
				containerValue = containerValues.get(0);
				if (valueType.equals("stringValue")) containerValue.setStringValue(value);
				if (valueType.equals("fileValue")) containerValue.setFileValue(value);
				if (valueType.equals("clobValue")) containerValue.setClobValue(value);
				if (valueType.equals("blobValue")) containerValue.setBlobValue(value.getBytes(Charset.forName("UTF-8")));
				if (valueType.equals("numericValue")) containerValue.setNumericValue(new BigDecimal(value));
				if (valueType.equals("dateValue")) containerValue.setDateValue(new Date(Long.parseLong(value)));
				if (valueType.equals("codeValue")) containerValue.setCodeValue(value);
				if (valueType.equals("urlValue")) containerValue.setUrlValue(value);
				containerValue.merge();
				logger.debug("Updated the container value: " + containerValue.toJson());
			}
			else if (containerValues.isEmpty()) {
				containerValue = createContainerValueByContainerIdAndStateTypeKindAndValueTypeKind(containerId, stateType, stateKind, valueType, valueKind, value);
				logger.debug("Created the container value: " + containerValue.toJson());
			}
		}
		return containerValue;

	}
	
	@Override
	public ContainerValue createContainerValueFromLsStateAndTypeAndKindAndValue(
			ContainerState containerState, String lsType, String lsKind, String value, String recordedBy) {
		ContainerValue containerValue = new ContainerValue();
		containerValue.setLsState(containerState);
		containerValue.setLsType(lsType);
		containerValue.setLsKind(lsKind);
		if (lsType.equals("stringValue")) containerValue.setStringValue(value);
		if (lsType.equals("fileValue")) containerValue.setFileValue(value);
		if (lsType.equals("clobValue")) containerValue.setClobValue(value);
		if (lsType.equals("blobValue")) containerValue.setBlobValue(value.getBytes(Charset.forName("UTF-8")));
		if (lsType.equals("numericValue")) containerValue.setNumericValue(new BigDecimal(value));
		if (lsType.equals("dateValue")) containerValue.setDateValue(new Date(Long.parseLong(value)));
		if (lsType.equals("codeValue")) containerValue.setCodeValue(value);
		if (lsType.equals("urlValue")) containerValue.setUrlValue(value);
		containerValue.setRecordedBy(recordedBy);
		containerValue.persist();
		return containerValue;
	}
	
	@Override
	public List<ContainerValue> getContainerValuesByContainerIdAndStateTypeKindAndValueTypeKind(
			Long containerId, String stateType, String stateKind,
			String valueType, String valueKind) {
		
		List<ContainerValue> containerValues = ContainerValue.findContainerValuesByContainerIDAndStateTypeKindAndValueTypeKind(containerId, stateType,
				stateKind, valueType, valueKind).getResultList();
		
		return containerValues;
	}
	
	private ContainerValue createContainerValueByContainerIdAndStateTypeKindAndValueTypeKind(
			Long containerId, String stateType, String stateKind,
			String valueType, String valueKind, String value) {
		ContainerValue containerValue = new ContainerValue();
		ContainerState containerState = ContainerState.findContainerStatesByContainerIDAndStateTypeKind(containerId, stateType, stateKind).getSingleResult();
		containerValue.setLsState(containerState);
		containerValue.setLsType(valueType);
		containerValue.setLsKind(valueKind);
		if (valueType.equals("stringValue")) containerValue.setStringValue(value);
		if (valueType.equals("fileValue")) containerValue.setFileValue(value);
		if (valueType.equals("clobValue")) containerValue.setClobValue(value);
		if (valueType.equals("blobValue")) containerValue.setBlobValue(value.getBytes(Charset.forName("UTF-8")));
		if (valueType.equals("numericValue")) containerValue.setNumericValue(new BigDecimal(value));
		if (valueType.equals("dateValue")) containerValue.setDateValue(new Date(Long.parseLong(value)));
		if (valueType.equals("codeValue")) containerValue.setCodeValue(value);
		if (valueType.equals("urlValue")) containerValue.setUrlValue(value);
		containerValue.setRecordedBy("default");
		containerValue.persist();
		return containerValue;
	}


}
