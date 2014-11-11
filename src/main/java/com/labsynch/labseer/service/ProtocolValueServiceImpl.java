package com.labsynch.labseer.service;

import java.io.IOException;
import java.io.StringWriter;
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
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.api.ApiValueController;
import com.labsynch.labseer.domain.AbstractValue;
import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.ProtocolState;
import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.dto.TreatmentGroupValueDTO;


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
		if(ApiValueController.isNumeric(idOrCodeName)) {
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
}
