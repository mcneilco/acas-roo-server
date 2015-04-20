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
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingState;
import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.dto.TreatmentGroupValueDTO;
import com.labsynch.labseer.utils.SimpleUtil;


@Service
@Transactional
public class LsThingValueServiceImpl implements LsThingValueService {

	@Autowired
	private LsThingStateService lsThingStateService;
	
	private static final Logger logger = LoggerFactory.getLogger(LsThingValueServiceImpl.class);


	//Query hibernate object and grab existing table references - add them to json hydrated object
	@Override
	@Transactional
	public LsThingValue updateLsThingValue(LsThingValue lsThingValue){
		if (lsThingValue.getLsState().getId() == null) {
			LsThingState lsThingState = new LsThingState(lsThingValue.getLsState());
			lsThingState.setLsThing(LsThing.findLsThing(lsThingValue.getLsState().getLsThing().getId()));
			lsThingState.persist();
			lsThingValue.setLsState(lsThingState); 
		} else {
			lsThingValue.setLsState(LsThingState.findLsThingState(lsThingValue.getLsState().getId()));
		}
		lsThingValue.setVersion(LsThingValue.findLsThingValue(lsThingValue.getId()).getVersion());
		lsThingValue.merge();
		return lsThingValue;
	}

	@Override
	@Transactional
	public LsThingValue saveLsThingValue(LsThingValue lsThingValue) {
		if (lsThingValue.getLsState().getId() == null) {
			LsThingState lsThingState = new LsThingState(lsThingValue.getLsState());
			lsThingState.setLsThing(LsThing.findLsThing(lsThingValue.getLsState().getLsThing().getId()));
			lsThingState.persist();
			lsThingValue.setLsState(lsThingState); 
		} else {
			lsThingValue.setLsState(LsThingState.findLsThingState(lsThingValue.getLsState().getId()));
		}		
		lsThingValue.persist();
		return lsThingValue;
	}
	
	@Override
	@Transactional
	public Collection<LsThingValue> saveLsThingValues(Collection<LsThingValue> lsThingValues) {
		for (LsThingValue lsThingValue: lsThingValues) {
			lsThingValue = saveLsThingValue(lsThingValue);
		}
		return lsThingValues;
	}

	@Override
	public List<LsThingValue> getLsThingValuesByLsThingId(Long id){	
		List<LsThingValue> lsThingValues = new ArrayList<LsThingValue>();
		LsThing lsThing = LsThing.findLsThing(id);
		if(lsThing.getLsStates() != null) {
			for (LsThingState lsThingState : lsThing.getLsStates()) {
				if(lsThingState.getLsValues() != null) {
					for(LsThingValue lsThingValue : lsThingState.getLsValues()) {
						lsThingValues.add(lsThingValue);
					}
				}
			}
		}
		return lsThingValues;
	}

	@Override
	public List<LsThingValue> getLsThingValuesByLsThingIdAndStateTypeKindAndValueTypeKind(
			Long lsThingId, String stateType, String stateKind,
			String valueType, String valueKind) {
		
		List<LsThingValue> lsThingValues = LsThingValue.findLsThingValuesByLsThingIDAndStateTypeKindAndValueTypeKind(lsThingId, stateType,
				stateKind, valueType, valueKind).getResultList();
		
		return lsThingValues;
	}
	
	@Override
	public LsThingValue updateLsThingValue(String idOrCodeName, String stateType, String stateKind, String valueType, String valueKind, String value) {
		//fetch the entity
		LsThing lsThing;
		if(SimpleUtil.isNumeric(idOrCodeName)) {
			lsThing = LsThing.findLsThing(Long.valueOf(idOrCodeName));
		} else {		
			try {
				lsThing = LsThing.findLsThingsByCodeNameEquals(idOrCodeName).getSingleResult();
			} catch(Exception ex) {
				lsThing = null;
			}
		}
		//fetch the state, and if it doesn't exist, create it
		List<LsThingState> lsThingStates;
		if(lsThing != null) {
			Long lsThingId = lsThing.getId();
			lsThingStates = lsThingStateService.getLsThingStatesByLsThingIdAndStateTypeKind(lsThingId, stateType, stateKind);
			if (lsThingStates.isEmpty()) {
				//create the state
				lsThingStates.add(lsThingStateService.createLsThingStateByLsThingIdAndStateTypeKind(lsThingId, stateType, stateKind));
				logger.debug("Created the lsThing state: " + lsThingStates.get(0).toJson());
			}
		}
		//fetch the value, update it if it exists, and if it doesn't exist, create it
		List<LsThingValue> lsThingValues;
		LsThingValue lsThingValue = null;
		if(lsThing != null) {
			Long lsThingId = lsThing.getId();
			lsThingValues = getLsThingValuesByLsThingIdAndStateTypeKindAndValueTypeKind(lsThingId, stateType, stateKind, valueType, valueKind);
			if (lsThingValues.size() > 1){
				logger.error("Error: multiple lsThing statuses found");
			}
			else if (lsThingValues.size() == 1){
				lsThingValue = lsThingValues.get(0);
				if (valueType.equals("stringValue")) lsThingValue.setStringValue(value);
				if (valueType.equals("fileValue")) lsThingValue.setFileValue(value);
				if (valueType.equals("clobValue")) lsThingValue.setClobValue(value);
				if (valueType.equals("blobValue")) lsThingValue.setBlobValue(value.getBytes(Charset.forName("UTF-8")));
				if (valueType.equals("numericValue")) lsThingValue.setNumericValue(new BigDecimal(value));
				if (valueType.equals("dateValue")) lsThingValue.setDateValue(new Date(Long.parseLong(value)));
				if (valueType.equals("codeValue")) lsThingValue.setCodeValue(value);
				lsThingValue.merge();
				logger.debug("Updated the lsThing value: " + lsThingValue.toJson());
			}
			else if (lsThingValues.isEmpty()){
				lsThingValue = createLsThingValueByLsThingIdAndStateTypeKindAndValueTypeKind(lsThingId, stateType, stateKind, valueType, valueKind, value);
				logger.debug("Created the lsThing value: " + lsThingValue.toJson());
			}
		}
		return lsThingValue;

	}

	private LsThingValue createLsThingValueByLsThingIdAndStateTypeKindAndValueTypeKind(
			Long lsThingId, String stateType, String stateKind,
			String valueType, String valueKind, String value) {
		LsThingValue lsThingValue = new LsThingValue();
		LsThingState lsThingState = LsThingState.findLsThingStatesByLsThingIDAndStateTypeKind(lsThingId, stateType, stateKind).getSingleResult();
		lsThingValue.setLsState(lsThingState);
		lsThingValue.setLsType(valueType);
		lsThingValue.setLsKind(valueKind);
		if (valueType.equals("stringValue")) lsThingValue.setStringValue(value);
		if (valueType.equals("fileValue")) lsThingValue.setFileValue(value);
		if (valueType.equals("clobValue")) lsThingValue.setClobValue(value);
		if (valueType.equals("blobValue")) lsThingValue.setBlobValue(value.getBytes(Charset.forName("UTF-8")));
		if (valueType.equals("numericValue")) lsThingValue.setNumericValue(new BigDecimal(value));
		if (valueType.equals("dateValue")) lsThingValue.setDateValue(new Date(Long.parseLong(value)));
		if (valueType.equals("codeValue")) lsThingValue.setCodeValue(value);
		lsThingValue.setRecordedBy("default");
		lsThingValue.persist();
		return lsThingValue;
	}
	
	@Override
	public Collection<LsThingValue> updateLsThingValues(
			Collection<LsThingValue> lsThingValues) {
		for (LsThingValue lsThingValue: lsThingValues) {
			lsThingValue = updateLsThingValue(lsThingValue);
		}
		return lsThingValues;
	}
}
