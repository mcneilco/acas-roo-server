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

import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.ProtocolState;
import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.dto.GenericValuePathRequest;
import com.labsynch.labseer.dto.ProtocolValuePathDTO;
import com.labsynch.labseer.utils.SimpleUtil;


@Service
@Transactional
public class ProtocolValueServiceImpl implements ProtocolValueService {

	@Autowired
	private ProtocolStateService protocolStateService;
	
	private static final Logger logger = LoggerFactory.getLogger(ProtocolValueServiceImpl.class);


	//Query hibernate object and grab existing table references - add them to json hydrated object
	@Override
	@Transactional
	public ProtocolValue updateProtocolValue(ProtocolValue protocolValue){
		if (protocolValue.getLsState().getId() == null) {
			ProtocolState protocolState = new ProtocolState(protocolValue.getLsState());
			protocolState.setProtocol(Protocol.findProtocol(protocolValue.getLsState().getProtocol().getId()));
			protocolState.persist();
			protocolValue.setLsState(protocolState); 
		} else {
			protocolValue.setLsState(ProtocolState.findProtocolState(protocolValue.getLsState().getId()));
		}
		protocolValue.setVersion(ProtocolValue.findProtocolValue(protocolValue.getId()).getVersion());
		protocolValue.merge();
		return protocolValue;
	}

	@Override
	@Transactional
	public ProtocolValue saveProtocolValue(ProtocolValue protocolValue) {
		if (protocolValue.getLsState().getId() == null) {
			ProtocolState protocolState = new ProtocolState(protocolValue.getLsState());
			protocolState.setProtocol(Protocol.findProtocol(protocolValue.getLsState().getProtocol().getId()));
			protocolState.persist();
			protocolValue.setLsState(protocolState); 
		} else {
			protocolValue.setLsState(ProtocolState.findProtocolState(protocolValue.getLsState().getId()));
		}		
		protocolValue.persist();
		return protocolValue;
	}
	
	@Override
	@Transactional
	public Collection<ProtocolValue> saveProtocolValues(Collection<ProtocolValue> protocolValues) {
		for (ProtocolValue protocolValue: protocolValues) {
			protocolValue = saveProtocolValue(protocolValue);
		}
		return protocolValues;
	}

	@Override
	public List<ProtocolValue> getProtocolValuesByProtocolId(Long id){	
		List<ProtocolValue> protocolValues = new ArrayList<ProtocolValue>();
		Protocol protocol = Protocol.findProtocol(id);
		if(protocol.getLsStates() != null) {
			for (ProtocolState protocolState : protocol.getLsStates()) {
				if(protocolState.getLsValues() != null) {
					for(ProtocolValue protocolValue : protocolState.getLsValues()) {
						protocolValues.add(protocolValue);
					}
				}
			}
		}
		return protocolValues;
	}

	@Override
	public List<ProtocolValue> getProtocolValuesByProtocolIdAndStateTypeKindAndValueTypeKind(
			Long protocolId, String stateType, String stateKind,
			String valueType, String valueKind) {
		
		List<ProtocolValue> protocolValues = ProtocolValue.findProtocolValuesByProtocolIDAndStateTypeKindAndValueTypeKind(protocolId, stateType,
				stateKind, valueType, valueKind).getResultList();
		
		return protocolValues;
	}
	
	@Override
	public ProtocolValue updateProtocolValue(String idOrCodeName, String stateType, String stateKind, String valueType, String valueKind, String value) {
		//fetch the entity
		Protocol protocol;
		if(SimpleUtil.isNumeric(idOrCodeName)) {
			protocol = Protocol.findProtocol(Long.valueOf(idOrCodeName));
		} else {		
			try {
				protocol = Protocol.findProtocolsByCodeNameEquals(idOrCodeName).getSingleResult();
			} catch(Exception ex) {
				protocol = null;
			}
		}
		//fetch the state, and if it doesn't exist, create it
		List<ProtocolState> protocolStates;
		if(protocol != null) {
			Long protocolId = protocol.getId();
			protocolStates = protocolStateService.getProtocolStatesByProtocolIdAndStateTypeKind(protocolId, stateType, stateKind);
			if (protocolStates.isEmpty()) {
				//create the state
				protocolStates.add(protocolStateService.createProtocolStateByProtocolIdAndStateTypeKind(protocolId, stateType, stateKind));
				logger.debug("Created the protocol state: " + protocolStates.get(0).toJson());
			}
		}
		//fetch the value, update it if it exists, and if it doesn't exist, create it
		List<ProtocolValue> protocolValues;
		ProtocolValue protocolValue = null;
		if(protocol != null) {
			Long protocolId = protocol.getId();
			protocolValues = getProtocolValuesByProtocolIdAndStateTypeKindAndValueTypeKind(protocolId, stateType, stateKind, valueType, valueKind);
			if (protocolValues.size() > 1){
				logger.error("Error: multiple protocol statuses found");
			}
			else if (protocolValues.size() == 1){
				protocolValue = protocolValues.get(0);
				if (valueType.equals("stringValue")) protocolValue.setStringValue(value);
				if (valueType.equals("fileValue")) protocolValue.setFileValue(value);
				if (valueType.equals("clobValue")) protocolValue.setClobValue(value);
				if (valueType.equals("blobValue")) protocolValue.setBlobValue(value.getBytes(Charset.forName("UTF-8")));
				if (valueType.equals("numericValue")) protocolValue.setNumericValue(new BigDecimal(value));
				if (valueType.equals("dateValue")) protocolValue.setDateValue(new Date(Long.parseLong(value)));
				if (valueType.equals("codeValue")) protocolValue.setCodeValue(value);
				protocolValue.merge();
				logger.debug("Updated the protocol value: " + protocolValue.toJson());
			}
			else if (protocolValues.isEmpty()){
				protocolValue = createProtocolValueByProtocolIdAndStateTypeKindAndValueTypeKind(protocolId, stateType, stateKind, valueType, valueKind, value);
				logger.debug("Created the protocol value: " + protocolValue.toJson());
			}
		}
		return protocolValue;

	}

	private ProtocolValue createProtocolValueByProtocolIdAndStateTypeKindAndValueTypeKind(
			Long protocolId, String stateType, String stateKind,
			String valueType, String valueKind, String value) {
		ProtocolValue protocolValue = new ProtocolValue();
		ProtocolState protocolState = ProtocolState.findProtocolStatesByProtocolIDAndStateTypeKind(protocolId, stateType, stateKind).getSingleResult();
		protocolValue.setLsState(protocolState);
		protocolValue.setLsType(valueType);
		protocolValue.setLsKind(valueKind);
		if (valueType.equals("stringValue")) protocolValue.setStringValue(value);
		if (valueType.equals("fileValue")) protocolValue.setFileValue(value);
		if (valueType.equals("clobValue")) protocolValue.setClobValue(value);
		if (valueType.equals("blobValue")) protocolValue.setBlobValue(value.getBytes(Charset.forName("UTF-8")));
		if (valueType.equals("numericValue")) protocolValue.setNumericValue(new BigDecimal(value));
		if (valueType.equals("dateValue")) protocolValue.setDateValue(new Date(Long.parseLong(value)));
		if (valueType.equals("codeValue")) protocolValue.setCodeValue(value);
		protocolValue.setRecordedBy("default");
		protocolValue.persist();
		return protocolValue;
	}
	
	@Override
	public Collection<ProtocolValue> updateProtocolValues(
			Collection<ProtocolValue> protocolValues) {
		for (ProtocolValue protocolValue: protocolValues) {
			protocolValue = updateProtocolValue(protocolValue);
		}
		return protocolValues;
	}
	
	@Override
	public ProtocolValue getProtocolValue(String idOrCodeName,
			String stateType, String stateKind, String valueType, String valueKind) {
		ProtocolValue value = null;
		try{
			Collection<ProtocolValue> values = getProtocolValues(idOrCodeName, stateType, stateKind, valueType, valueKind);
			value = values.iterator().next();
		}catch (Exception e){
			logger.error("Caught error "+e.toString()+" trying to find a state.",e);
			value = null;
		}
		return value;
	}

	@Override
	public Collection<ProtocolValuePathDTO> getProtocolValues(
			Collection<GenericValuePathRequest> genericRequests) {
		Collection<ProtocolValuePathDTO> results = new ArrayList<ProtocolValuePathDTO>();
		for (GenericValuePathRequest request : genericRequests){
			ProtocolValuePathDTO result = new ProtocolValuePathDTO();
			result.setIdOrCodeName(request.getIdOrCodeName());
			result.setValueType(request.getValueType());
			result.setValueKind(request.getValueKind());
			result.setValues(getProtocolValues(request.getIdOrCodeName(), request.getStateType(), request.getStateKind(), request.getValueType(), request.getValueKind()));
			results.add(result);
		}
		return results;
	}
	
	private Collection<ProtocolValue> getProtocolValues(String idOrCodeName, String stateType, String stateKind, String valueType, String valueKind){
		if (SimpleUtil.isNumeric(idOrCodeName)){
			Long id = Long.valueOf(idOrCodeName);
			return ProtocolValue.findProtocolValuesByProtocolIDAndStateTypeKindAndValueTypeKind(id, stateType, stateKind, valueType, valueKind).getResultList();
		}else{
			return ProtocolValue.findProtocolValuesByProtocolCodeNameAndStateTypeKindAndValueTypeKind(idOrCodeName, stateType, stateKind, valueType, valueKind).getResultList();
		}
	}
}
