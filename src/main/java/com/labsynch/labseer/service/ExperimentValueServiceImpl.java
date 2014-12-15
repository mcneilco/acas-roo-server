package com.labsynch.labseer.service;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.domain.AbstractValue;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.StateValueDTO;


@Service
@Transactional
public class ExperimentValueServiceImpl implements ExperimentValueService {
	
	@Autowired
	private ExperimentStateService experimentStateService;

	private static final Logger logger = LoggerFactory.getLogger(ExperimentValueServiceImpl.class);

	//Query hibernate object and grab existing table references - add them to json hydrated object
	@Override
	@Transactional
	public ExperimentValue updateExperimentValue(ExperimentValue experimentValue){
		if (experimentValue.getLsState().getId() == null) {
			ExperimentState experimentState = new ExperimentState(experimentValue.getLsState());
			experimentState.setExperiment(Experiment.findExperiment(experimentValue.getLsState().getExperiment().getId()));
			experimentState.persist();
			experimentValue.setLsState(experimentState); 
		} else {
			experimentValue.setLsState(ExperimentState.findExperimentState(experimentValue.getLsState().getId()));
		}		
		experimentValue.merge();
		return experimentValue;
	}

	//get experiment state from db and add as parent to object before save
	@Override
	@Transactional
	public ExperimentValue saveExperimentValue(ExperimentValue experimentValue) {
		if (experimentValue.getLsState().getId() == null) {
			ExperimentState experimentState = new ExperimentState(experimentValue.getLsState());
			experimentState.setExperiment(Experiment.findExperiment(experimentValue.getLsState().getExperiment().getId()));
			experimentState.persist();
			experimentValue.setLsState(experimentState); 
		} else {
			experimentValue.setLsState(ExperimentState.findExperimentState(experimentValue.getLsState().getId()));
		}		
		experimentValue.persist();
		return experimentValue;
	}

	@Override
	public List<ExperimentValue> getExperimentValuesByExperimentId(Long id){	
		List<ExperimentValue> experimentValues = new ArrayList<ExperimentValue>();
		Experiment experiment = Experiment.findExperiment(id);
		if(experiment.getLsStates() != null) {
			for (ExperimentState experimentState : experiment.getLsStates()) {
				if(experimentState.getLsValues() != null) {
					for(ExperimentValue experimentValue : experimentState.getLsValues()) {
						experimentValues.add(experimentValue);
					}
				}
			}
		}
		return experimentValues;
	}

	@Override
	public List<ExperimentValue> getExperimentValuesByExperimentIdAndStateTypeKindAndValueTypeKind(Long experimentId, String stateType, 
			String stateKind, String valueType, String valueKind){	
		
		List<ExperimentValue> experimentValues = ExperimentValue.findExperimentValuesByExptIDAndStateTypeKindAndValueTypeKind(experimentId, stateType, stateKind, valueType, valueKind).getResultList();

		return experimentValues;
	}
	
	@Override
	public List<ExperimentValue> getExperimentValuesByExperimentIdAndStateTypeKind(Long experimentId, String stateType, 
			String stateKind) {	
		
		List<ExperimentValue> experimentValues = ExperimentValue.findExperimentValuesByExptIDAndStateTypeKind(experimentId, stateType, stateKind).getResultList();

		return experimentValues;
	}

	@Override
	public String getCsvList(List<ExperimentValue> experimentValues) {
		StringWriter outFile = new StringWriter();
		ICsvBeanWriter beanWriter = null;
		try {
			beanWriter = new CsvBeanWriter(outFile, CsvPreference.STANDARD_PREFERENCE);
			final String[] header = AbstractValue.getColumns();
			final CellProcessor[] processors = AbstractValue.getProcessors();
			beanWriter.writeHeader(header);
			for (final ExperimentValue experimentValue : experimentValues) {
				beanWriter.write(experimentValue, header, processors);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (beanWriter != null) {
				try {
					beanWriter.close();
					outFile.flush();
					outFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return outFile.toString();		
		
	}

	@Override
	public List<StateValueDTO> getKeyValueList(List<ExperimentValue> experimentValues) {
		List<StateValueDTO> stateValues = new ArrayList<StateValueDTO>();
		for (ExperimentValue experimentValue : experimentValues){
			StateValueDTO stateValue = new StateValueDTO();
			stateValue.setId(experimentValue.getId());
			if (experimentValue.getLsType().equalsIgnoreCase("numericValue")){
				stateValue.setLsValue(experimentValue.getNumericValue().toString());
			} else if (experimentValue.getLsType().equalsIgnoreCase("stringValue")){
				stateValue.setLsValue(experimentValue.getStringValue());
			} else if (experimentValue.getLsType().equalsIgnoreCase("codeValue")){
				stateValue.setLsValue(experimentValue.getCodeValue());
			}
			stateValues.add(stateValue);
		}
		
		return stateValues;
		
	}
	
	@Override
	public List<CodeTableDTO> convertToCodeTables(List<ExperimentValue> experimentValues) {
		List<CodeTableDTO> codeTableList = new ArrayList<CodeTableDTO>();
		for (ExperimentValue val : experimentValues) {
			//TODO: add in codeValue as well
			if (val.getLsType().equalsIgnoreCase("stringValue") || (val.getLsType().equalsIgnoreCase("numericValue"))){
				CodeTableDTO codeTable = new CodeTableDTO();
				if ((val.getLsType().equalsIgnoreCase("numericValue"))){
					codeTable.setName(val.getNumericValue().toString());
				} else {
					codeTable.setName(val.getStringValue());
				}
				codeTable.setCode(val.getId().toString());
				//codeTable.setCodeName(val.getId().toString());
				codeTable.setIgnored(val.isIgnored());
				codeTableList.add(codeTable);			
			}
		}
		return codeTableList;	
	}
	
	@Override
	public ExperimentValue updateExperimentValue(String idOrCodeName, String stateType, String stateKind, String valueType, String valueKind, String value) {
		//fetch the entity
		Experiment experiment;
		if(isNumeric(idOrCodeName)) {
			experiment = Experiment.findExperiment(Long.valueOf(idOrCodeName));
		} else {		
			try {
				experiment = Experiment.findExperimentsByCodeNameEquals(idOrCodeName).getSingleResult();
			} catch(Exception ex) {
				experiment = null;
			}
		}
		//fetch the state, and if it doesn't exist, create it
		List<ExperimentState> experimentStates;
		if(experiment != null) {
			Long experimentId = experiment.getId();
			experimentStates = experimentStateService.getExperimentStatesByExperimentIdAndStateTypeKind(experimentId, stateType, stateKind);
			if (experimentStates.isEmpty()) {
				//create the state
				experimentStates.add(experimentStateService.createExperimentStateByExperimentIdAndStateTypeKind(experimentId, stateType, stateKind));
				logger.debug("Created the experiment state: " + experimentStates.get(0).toJson());
			}
		}
		//fetch the value, update it if it exists, and if it doesn't exist, create it
		List<ExperimentValue> experimentValues;
		ExperimentValue experimentValue = null;
		if(experiment != null) {
			Long experimentId = experiment.getId();
			experimentValues = getExperimentValuesByExperimentIdAndStateTypeKindAndValueTypeKind(experimentId, stateType, stateKind, valueType, valueKind);
			if (experimentValues.size() > 1){
				logger.error("Error: multiple experiment values of same type and kind found for the same experiment");
			}
			else if (experimentValues.size() == 1){
				experimentValue = experimentValues.get(0);
				if (valueType.equals("stringValue")) experimentValue.setStringValue(value);
				if (valueType.equals("fileValue")) experimentValue.setFileValue(value);
				if (valueType.equals("clobValue")) experimentValue.setClobValue(value);
				if (valueType.equals("blobValue")) experimentValue.setBlobValue(value.getBytes(Charset.forName("UTF-8")));
				if (valueType.equals("numericValue")) experimentValue.setNumericValue(new BigDecimal(value));
				if (valueType.equals("dateValue")) experimentValue.setDateValue(new Date(Long.parseLong(value)));
				if (valueType.equals("codeValue")) experimentValue.setCodeValue(value);
				experimentValue.merge();
				logger.debug("Updated the experiment value: " + experimentValue.toJson());
			}
			else if (experimentValues.isEmpty()){
				experimentValue = createExperimentValueByExperimentIdAndStateTypeKindAndValueTypeKind(experimentId, stateType, stateKind, valueType, valueKind, value);
				logger.debug("Created the experiment value: " + experimentValue.toJson());
			}
		}
		return experimentValue;
	}
	
	private ExperimentValue createExperimentValueByExperimentIdAndStateTypeKindAndValueTypeKind(
			Long experimentId, String stateType, String stateKind,
			String valueType, String valueKind, String value) {
		ExperimentValue experimentValue = new ExperimentValue();
		ExperimentState experimentState = ExperimentState.findExperimentStatesByExptIDAndStateTypeKind(experimentId, stateType, stateKind).getSingleResult();
		experimentValue.setLsState(experimentState);
		experimentValue.setLsType(valueType);
		experimentValue.setLsKind(valueKind);
		if (valueType.equals("stringValue")) experimentValue.setStringValue(value);
		if (valueType.equals("fileValue")) experimentValue.setFileValue(value);
		if (valueType.equals("clobValue")) experimentValue.setClobValue(value);
		if (valueType.equals("blobValue")) experimentValue.setBlobValue(value.getBytes(Charset.forName("UTF-8")));
		if (valueType.equals("numericValue")) experimentValue.setNumericValue(new BigDecimal(value));
		if (valueType.equals("dateValue")) experimentValue.setDateValue(new Date(Long.parseLong(value)));
		if (valueType.equals("codeValue")) experimentValue.setCodeValue(value);
		experimentValue.setRecordedBy("default");
		//TODO: figure out who to record as RecordedBy
		experimentValue.persist();
		return experimentValue;
	}

	private static boolean isNumeric(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c)) return false;
		}
		return true;
	}
}
