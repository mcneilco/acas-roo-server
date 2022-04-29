package com.labsynch.labseer.service;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.dto.AnalysisGroupValuePathDTO;
import com.labsynch.labseer.dto.GenericValuePathRequest;
import com.labsynch.labseer.utils.SimpleUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;


@Service
@Transactional
public class AnalysisGroupValueServiceImpl implements AnalysisGroupValueService {
	
	@Autowired
	private AnalysisGroupStateService analysisGroupStateService;
	
	private static final Logger logger = LoggerFactory.getLogger(AnalysisGroupValueServiceImpl.class);

	@Override
	public List<AnalysisGroupValue> getAnalysisGroupValuesByExperimentIdAndStateTypeKind(
			Long experimentId, String stateType, String stateKind) {
		
		List<AnalysisGroupValue> analysisGroupValues = AnalysisGroupValue.findAnalysisGroupValuesByExptIDAndStateTypeKind(experimentId, stateType, stateKind).getResultList();

		return analysisGroupValues;
	}
	
	public List<AnalysisGroupValue> getAnalysisGroupValuesByExperimentIdAndStateTypeKindAndValueTypeKind(Long experimentId, String stateType,
			String stateKind, String valueType, String valueKind) {
		
		List<AnalysisGroupValue> analysisGroupValues = AnalysisGroupValue.findAnalysisGroupValuesByExptIDAndStateTypeKindAndValueTypeKind(experimentId, stateType,
				stateKind, valueType, valueKind).getResultList();
		
		return analysisGroupValues;
	}
	
	public List<AnalysisGroupValue> getAnalysisGroupValuesByAnalysisGroupIdAndStateTypeKind(Long analysisGroupId, String stateType, String stateKind) {
		List<AnalysisGroupValue> analysisGroupValues = AnalysisGroupValue.findAnalysisGroupValuesByAnalysisGroupIDAndStateTypeKind(analysisGroupId, stateType, stateKind).getResultList();

		return analysisGroupValues;
	}

	@Override
	public String getCsvList(List<AnalysisGroupValue> analysisGroupValues) {
		StringWriter outFile = new StringWriter();
		ICsvBeanWriter beanWriter = null;
		try {
			beanWriter = new CsvBeanWriter(outFile, CsvPreference.TAB_PREFERENCE);
			final String[] header = AnalysisGroupValue.getColumns();
			final CellProcessor[] processors = AnalysisGroupValue.getProcessors();
			beanWriter.writeHeader(header);
			for (final AnalysisGroupValue analysisGroupValue : analysisGroupValues) {
				beanWriter.write(analysisGroupValue, header, processors);
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
	public List<AnalysisGroupValue> getAnalysisGroupValuesByAnalysisGroupIdAndStateTypeKindAndValueTypeKind(
			Long analysisGroupId, String stateType, String stateKind,
			String valueType, String valueKind) {
		
		List<AnalysisGroupValue> analysisGroupValues = AnalysisGroupValue.findAnalysisGroupValuesByAnalysisGroupIDAndStateTypeKindAndValueTypeKind(analysisGroupId, stateType,
				stateKind, valueType, valueKind).getResultList();
		
		return analysisGroupValues;
	}
	
	@Override
	@Transactional
	public AnalysisGroupValue saveAnalysisGroupValue(AnalysisGroupValue analysisGroupValue) {
		if (analysisGroupValue.getLsState().getId() == null) {
			AnalysisGroupState analysisGroupState = new AnalysisGroupState(analysisGroupValue.getLsState());
			analysisGroupState.setAnalysisGroup(AnalysisGroup.findAnalysisGroup(analysisGroupValue.getLsState().getAnalysisGroup().getId()));
			analysisGroupState.persist();
			analysisGroupValue.setLsState(analysisGroupState); 
		} else {
			analysisGroupValue.setLsState(AnalysisGroupState.findAnalysisGroupState(analysisGroupValue.getLsState().getId()));
		}		
		analysisGroupValue.persist();
		return analysisGroupValue;
	}
	
	@Override
	@Transactional
	public Collection<AnalysisGroupValue> saveAnalysisGroupValues(Collection<AnalysisGroupValue> analysisGroupValues) {
		for (AnalysisGroupValue analysisGroupValue: analysisGroupValues) {
			analysisGroupValue = saveAnalysisGroupValue(analysisGroupValue);
		}
		return analysisGroupValues;
	}
	
	@Override
	public AnalysisGroupValue updateAnalysisGroupValue(AnalysisGroupValue analysisGroupValue) {
		if (analysisGroupValue.getLsState().getId() == null) {
			AnalysisGroupState analysisGroupState = new AnalysisGroupState(analysisGroupValue.getLsState());
			analysisGroupState.setAnalysisGroup(AnalysisGroup.findAnalysisGroup(analysisGroupValue.getLsState().getAnalysisGroup().getId()));
			analysisGroupState.persist();
			analysisGroupValue.setLsState(analysisGroupState); 
		} else {
			analysisGroupValue.setLsState(AnalysisGroupState.findAnalysisGroupState(analysisGroupValue.getLsState().getId()));
		}
//		analysisGroupValue.setVersion(AnalysisGroupValue.findAnalysisGroupValue(analysisGroupValue.getId()).getVersion());
		analysisGroupValue.merge();
		return analysisGroupValue;
	}
	
	@Override
	public AnalysisGroupValue updateAnalysisGroupValue(String idOrCodeName, String stateType, String stateKind, String valueType, String valueKind, String value) {
		//fetch the entity
		AnalysisGroup analysisGroup;
		if(SimpleUtil.isNumeric(idOrCodeName)) {
			analysisGroup = AnalysisGroup.findAnalysisGroup(Long.valueOf(idOrCodeName));
		} else {		
			try {
				analysisGroup = AnalysisGroup.findAnalysisGroupsByCodeNameEquals(idOrCodeName).getSingleResult();
			} catch(Exception ex) {
				analysisGroup = null;
			}
		}
		//fetch the state, and if it doesn't exist, create it
		List<AnalysisGroupState> analysisGroupStates;
		if(analysisGroup != null) {
			Long analysisGroupId = analysisGroup.getId();
			analysisGroupStates = analysisGroupStateService.getAnalysisGroupStatesByAnalysisGroupIdAndStateTypeKind(analysisGroupId, stateType, stateKind);
			if (analysisGroupStates.isEmpty()) {
				//create the state
				analysisGroupStates.add(analysisGroupStateService.createAnalysisGroupStateByAnalysisGroupIdAndStateTypeKind(analysisGroupId, stateType, stateKind));
				logger.debug("Created the analysisGroup state: " + analysisGroupStates.get(0).toJson());
			}
		}
		//fetch the value, ignore it if it exists, then create a new one
		List<AnalysisGroupValue> analysisGroupValues;
		AnalysisGroupValue analysisGroupValue = null;
		if(analysisGroup != null) {
			Long analysisGroupId = analysisGroup.getId();
			analysisGroupValues = getAnalysisGroupValuesByAnalysisGroupIdAndStateTypeKindAndValueTypeKind(analysisGroupId, stateType, stateKind, valueType, valueKind);
			if (analysisGroupValues.size() > 1){
				logger.error("Error: multiple analysisGroup statuses found");
			}
			else if (analysisGroupValues.size() == 1){
				analysisGroupValue = analysisGroupValues.get(0);
				analysisGroupValue.setIgnored(true);
				analysisGroupValue.merge();
				logger.debug("Ignored the analysisGroup value: " + analysisGroupValue.toJson());
			}
			analysisGroupValue = createAnalysisGroupValueByAnalysisGroupIdAndStateTypeKindAndValueTypeKind(analysisGroupId, stateType, stateKind, valueType, valueKind, value);
			logger.debug("Created the analysisGroup value: " + analysisGroupValue.toJson());
		}
		return analysisGroupValue;

	}

	private AnalysisGroupValue createAnalysisGroupValueByAnalysisGroupIdAndStateTypeKindAndValueTypeKind(
			Long analysisGroupId, String stateType, String stateKind,
			String valueType, String valueKind, String value) {
		AnalysisGroupValue analysisGroupValue = new AnalysisGroupValue();
		AnalysisGroupState analysisGroupState = AnalysisGroupState.findAnalysisGroupStatesByAnalysisGroupIDAndStateTypeKind(analysisGroupId, stateType, stateKind).getSingleResult();
		analysisGroupValue.setLsState(analysisGroupState);
		analysisGroupValue.setLsType(valueType);
		analysisGroupValue.setLsKind(valueKind);
		if (valueType.equals("stringValue")) analysisGroupValue.setStringValue(value);
		if (valueType.equals("fileValue")) analysisGroupValue.setFileValue(value);
		if (valueType.equals("clobValue")) analysisGroupValue.setClobValue(value);
		if (valueType.equals("blobValue")) analysisGroupValue.setBlobValue(value.getBytes(Charset.forName("UTF-8")));
		if (valueType.equals("numericValue")) analysisGroupValue.setNumericValue(new BigDecimal(value));
		if (valueType.equals("dateValue")) analysisGroupValue.setDateValue(new Date(Long.parseLong(value)));
		if (valueType.equals("codeValue")) analysisGroupValue.setCodeValue(value);
		if (valueType.equals("urlValue")) analysisGroupValue.setUrlValue(value);
		analysisGroupValue.setRecordedBy("default");
		analysisGroupValue.persist();
		return analysisGroupValue;
	}


	@Override
	public Collection<AnalysisGroupValue> updateAnalysisGroupValues(
			Collection<AnalysisGroupValue> analysisGroupValues) {
		for (AnalysisGroupValue analysisGroupValue: analysisGroupValues) {
			analysisGroupValue = updateAnalysisGroupValue(analysisGroupValue);
		}
		return analysisGroupValues;
	}
	
	@Override
	public AnalysisGroupValue createAnalysisGroupValueFromLsStateAndTypeAndKindAndValue(
			AnalysisGroupState analysisGroupState, String lsType, String lsKind, String value, String recordedBy) {
		AnalysisGroupValue analysisGroupValue = new AnalysisGroupValue();
		analysisGroupValue.setLsState(analysisGroupState);
		analysisGroupValue.setLsType(lsType);
		analysisGroupValue.setLsKind(lsKind);
		if (lsType.equals("stringValue")) analysisGroupValue.setStringValue(value);
		if (lsType.equals("fileValue")) analysisGroupValue.setFileValue(value);
		if (lsType.equals("clobValue")) analysisGroupValue.setClobValue(value);
		if (lsType.equals("blobValue")) analysisGroupValue.setBlobValue(value.getBytes(Charset.forName("UTF-8")));
		if (lsType.equals("numericValue")) analysisGroupValue.setNumericValue(new BigDecimal(value));
		if (lsType.equals("dateValue")) analysisGroupValue.setDateValue(new Date(Long.parseLong(value)));
		if (lsType.equals("codeValue")) analysisGroupValue.setCodeValue(value);
		if (lsType.equals("urlValue")) analysisGroupValue.setUrlValue(value);
		analysisGroupValue.setRecordedBy(recordedBy);
		analysisGroupValue.persist();
		return analysisGroupValue;
	}

	@Override
	public AnalysisGroupValue getAnalysisGroupValue(String idOrCodeName,
			String stateType, String stateKind, String valueType, String valueKind) {
		AnalysisGroupValue value = null;
		try{
			Collection<AnalysisGroupValue> values = getAnalysisGroupValues(idOrCodeName, stateType, stateKind, valueType, valueKind);
			value = values.iterator().next();
		}catch (Exception e){
			logger.error("Caught error "+e.toString()+" trying to find a state.",e);
			value = null;
		}
		return value;
	}

	@Override
	public Collection<AnalysisGroupValuePathDTO> getAnalysisGroupValues(
			Collection<GenericValuePathRequest> genericRequests) {
		Collection<AnalysisGroupValuePathDTO> results = new ArrayList<AnalysisGroupValuePathDTO>();
		for (GenericValuePathRequest request : genericRequests){
			AnalysisGroupValuePathDTO result = new AnalysisGroupValuePathDTO();
			result.setIdOrCodeName(request.getIdOrCodeName());
			result.setValueType(request.getValueType());
			result.setValueKind(request.getValueKind());
			result.setValues(getAnalysisGroupValues(request.getIdOrCodeName(), request.getStateType(), request.getStateKind(), request.getValueType(), request.getValueKind()));
			results.add(result);
		}
		return results;
	}
	
	private Collection<AnalysisGroupValue> getAnalysisGroupValues(String idOrCodeName, String stateType, String stateKind, String valueType, String valueKind){
		if (SimpleUtil.isNumeric(idOrCodeName)){
			Long id = Long.valueOf(idOrCodeName);
			return AnalysisGroupValue.findAnalysisGroupValuesByAnalysisGroupIDAndStateTypeKindAndValueTypeKind(id, stateType, stateKind, valueType, valueKind).getResultList();
		}else{
			return AnalysisGroupValue.findAnalysisGroupValuesByAnalysisGroupCodeNameAndStateTypeKindAndValueTypeKind(idOrCodeName, stateType, stateKind, valueType, valueKind).getResultList();
		}
	}
}
