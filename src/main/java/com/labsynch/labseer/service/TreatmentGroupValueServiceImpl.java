package com.labsynch.labseer.service;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.domain.TreatmentGroupState;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.dto.GenericValuePathRequest;
import com.labsynch.labseer.dto.TreatmentGroupValueDTO;
import com.labsynch.labseer.dto.TreatmentGroupValuePathDTO;
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
public class TreatmentGroupValueServiceImpl implements TreatmentGroupValueService {

	private static final Logger logger = LoggerFactory.getLogger(TreatmentGroupStateServiceImpl.class);
	
	@Autowired
	private TreatmentGroupStateService treatmentGroupStateService;
	
	@Override
	public List<TreatmentGroupValue> getTreatmentGroupValuesByExperimentIdAndStateTypeKind(
			Long experimentId, String stateType, String stateKind) {
		
		List<TreatmentGroupValue> treatmentGroupValues = TreatmentGroupValue.findTreatmentGroupValuesByExptIDAndStateTypeKind(experimentId, stateType, stateKind).getResultList();

		return treatmentGroupValues;
	}
	
	public List<TreatmentGroupValue> getTreatmentGroupValuesByExperimentIdAndStateTypeKindAndValueTypeKind(Long experimentId, String stateType,
			String stateKind, String valueType, String valueKind) {
		
		List<TreatmentGroupValue> treatmentGroupValues = TreatmentGroupValue.findTreatmentGroupValuesByExptIDAndStateTypeKindAndValueTypeKind(experimentId, stateType,
				stateKind, valueType, valueKind).getResultList();
		
		return treatmentGroupValues;
	}

	@Override
	public String getCsvList(List<TreatmentGroupValue> treatmentGroupValues) {
		StringWriter outFile = new StringWriter();
		ICsvBeanWriter beanWriter = null;
		try {
			beanWriter = new CsvBeanWriter(outFile, CsvPreference.TAB_PREFERENCE);
			final String[] header = TreatmentGroupValue.getColumns();
			final CellProcessor[] processors = TreatmentGroupValue.getProcessors();
			beanWriter.writeHeader(header);
			for (final TreatmentGroupValue treatmentGroupValue : treatmentGroupValues) {
				Collection<TreatmentGroupValueDTO> treatmentGroupValueDTOs = treatmentGroupValue.makeDTOsByAnalysisGroupIds();
				for (TreatmentGroupValueDTO treatmentGroupValueDTO : treatmentGroupValueDTOs) {
					beanWriter.write(treatmentGroupValueDTO, header, processors);
				}
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
	@Transactional
	public TreatmentGroupValue saveTreatmentGroupValue(TreatmentGroupValue treatmentGroupValue) {
		if (treatmentGroupValue.getLsState().getId() == null) {
			TreatmentGroupState treatmentGroupState = new TreatmentGroupState(treatmentGroupValue.getLsState());
			treatmentGroupState.setTreatmentGroup(TreatmentGroup.findTreatmentGroup(treatmentGroupValue.getLsState().getTreatmentGroup().getId()));
			treatmentGroupState.persist();
			treatmentGroupValue.setLsState(treatmentGroupState); 
		} else {
			treatmentGroupValue.setLsState(TreatmentGroupState.findTreatmentGroupState(treatmentGroupValue.getLsState().getId()));
		}		
		treatmentGroupValue.persist();
		return treatmentGroupValue;
	}
	
	@Override
	@Transactional
	public Collection<TreatmentGroupValue> saveTreatmentGroupValues(Collection<TreatmentGroupValue> treatmentGroupValues) {
		for (TreatmentGroupValue treatmentGroupValue: treatmentGroupValues) {
			treatmentGroupValue = saveTreatmentGroupValue(treatmentGroupValue);
		}
		return treatmentGroupValues;
	}
	
	@Override
	public TreatmentGroupValue updateTreatmentGroupValue(TreatmentGroupValue treatmentGroupValue) {
		if (treatmentGroupValue.getLsState().getId() == null) {
			TreatmentGroupState treatmentGroupState = new TreatmentGroupState(treatmentGroupValue.getLsState());
			treatmentGroupState.setTreatmentGroup(TreatmentGroup.findTreatmentGroup(treatmentGroupValue.getLsState().getTreatmentGroup().getId()));
			treatmentGroupState.persist();
			treatmentGroupValue.setLsState(treatmentGroupState); 
		} else {
			treatmentGroupValue.setLsState(TreatmentGroupState.findTreatmentGroupState(treatmentGroupValue.getLsState().getId()));
		}
		treatmentGroupValue.setVersion(TreatmentGroupValue.findTreatmentGroupValue(treatmentGroupValue.getId()).getVersion());
		treatmentGroupValue.merge();
		return treatmentGroupValue;
	}

	@Override
	public List<TreatmentGroupValue> getTreatmentGroupValuesByAnalysisGroupIdAndStateTypeKind(
			Long analysisGroupId, String stateType, String stateKind) {
		
		List<TreatmentGroupValue> treatmentGroupValues = TreatmentGroupValue.findTreatmentGroupValuesByAnalysisGroupIDAndStateTypeKind(analysisGroupId, stateType, stateKind).getResultList();

		return treatmentGroupValues;
	}

	@Override
	public TreatmentGroupValue updateTreatmentGroupValue(String idOrCodeName,
			String stateType, String stateKind, String valueType,
			String valueKind, String value) {
		//fetch the entity
		TreatmentGroup treatmentGroup;
		if(SimpleUtil.isNumeric(idOrCodeName)) {
			treatmentGroup = TreatmentGroup.findTreatmentGroup(Long.valueOf(idOrCodeName));
		} else {		
			try {
				treatmentGroup = TreatmentGroup.findTreatmentGroupsByCodeNameEquals(idOrCodeName).getSingleResult();
			} catch(Exception ex) {
				treatmentGroup = null;
			}
		}
		//fetch the state, and if it doesn't exist, create it
		List<TreatmentGroupState> treatmentGroupStates;
		if(treatmentGroup != null) {
			Long treatmentGroupId = treatmentGroup.getId();
			treatmentGroupStates = treatmentGroupStateService.getTreatmentGroupStatesByTreatmentGroupIdAndStateTypeKind(treatmentGroupId, stateType, stateKind);
			if (treatmentGroupStates.isEmpty()) {
				//create the state
				treatmentGroupStates.add(treatmentGroupStateService.createTreatmentGroupStateByTreatmentGroupIdAndStateTypeKind(treatmentGroupId, stateType, stateKind));
				logger.debug("Created the treatmentGroup state: " + treatmentGroupStates.get(0).toJson());
			}
		}
		//fetch the value, update it if it exists, and if it doesn't exist, create it
		List<TreatmentGroupValue> treatmentGroupValues;
		TreatmentGroupValue treatmentGroupValue = null;
		if(treatmentGroup != null) {
			Long treatmentGroupId = treatmentGroup.getId();
			treatmentGroupValues = getTreatmentGroupValuesByTreatmentGroupIdAndStateTypeKindAndValueTypeKind(treatmentGroupId, stateType, stateKind, valueType, valueKind);
			if (treatmentGroupValues.size() > 1){
				logger.error("Error: multiple treatmentGroup statuses found");
			}
			else if (treatmentGroupValues.size() == 1){
				treatmentGroupValue = treatmentGroupValues.get(0);
				treatmentGroupValue.setIgnored(true);
				treatmentGroupValue.merge();
				logger.debug("Ignored the treatmentGroup value: " + treatmentGroupValue.toJson());
			}
			treatmentGroupValue = createTreatmentGroupValueByTreatmentGroupIdAndStateTypeKindAndValueTypeKind(treatmentGroupId, stateType, stateKind, valueType, valueKind, value);
			logger.debug("Created the treatmentGroup value: " + treatmentGroupValue.toJson());
		}
		return treatmentGroupValue;
	}

	private TreatmentGroupValue createTreatmentGroupValueByTreatmentGroupIdAndStateTypeKindAndValueTypeKind(
			Long treatmentGroupId, String stateType, String stateKind,
			String valueType, String valueKind, String value) {
		TreatmentGroupValue treatmentGroupValue = new TreatmentGroupValue();
		TreatmentGroupState treatmentGroupState = TreatmentGroupState.findTreatmentGroupStatesByTreatmentGroupIDAndStateTypeKind(treatmentGroupId, stateType, stateKind).getSingleResult();
		treatmentGroupValue.setLsState(treatmentGroupState);
		treatmentGroupValue.setLsType(valueType);
		treatmentGroupValue.setLsKind(valueKind);
		if (valueType.equals("stringValue")) treatmentGroupValue.setStringValue(value);
		if (valueType.equals("fileValue")) treatmentGroupValue.setFileValue(value);
		if (valueType.equals("clobValue")) treatmentGroupValue.setClobValue(value);
		if (valueType.equals("blobValue")) treatmentGroupValue.setBlobValue(value.getBytes(Charset.forName("UTF-8")));
		if (valueType.equals("numericValue")) treatmentGroupValue.setNumericValue(new BigDecimal(value));
		if (valueType.equals("dateValue")) treatmentGroupValue.setDateValue(new Date(Long.parseLong(value)));
		if (valueType.equals("codeValue")) treatmentGroupValue.setCodeValue(value);
		if (valueType.equals("urlValue")) treatmentGroupValue.setUrlValue(value);
		treatmentGroupValue.setRecordedBy("default");
		treatmentGroupValue.persist();
		return treatmentGroupValue;
	}

	private List<TreatmentGroupValue> getTreatmentGroupValuesByTreatmentGroupIdAndStateTypeKindAndValueTypeKind(
			Long treatmentGroupId, String stateType, String stateKind,
			String valueType, String valueKind) {
		List<TreatmentGroupValue> treatmentGroupValues = TreatmentGroupValue.findTreatmentGroupValuesByTreatmentGroupIDAndStateTypeKindAndValueTypeKind(treatmentGroupId, stateType,
				stateKind, valueType, valueKind).getResultList();
		
		return treatmentGroupValues;
	}
	
	@Override
	public Collection<TreatmentGroupValue> updateTreatmentGroupValues(
			Collection<TreatmentGroupValue> treatmentGroupValues) {
		for (TreatmentGroupValue treatmentGroupValue: treatmentGroupValues) {
			treatmentGroupValue = updateTreatmentGroupValue(treatmentGroupValue);
		}
		return treatmentGroupValues;
	}

	@Override
	public TreatmentGroupValue getTreatmentGroupValue(String idOrCodeName,
			String stateType, String stateKind, String valueType, String valueKind) {
		TreatmentGroupValue value = null;
		try{
			Collection<TreatmentGroupValue> values = getTreatmentGroupValues(idOrCodeName, stateType, stateKind, valueType, valueKind);
			value = values.iterator().next();
		}catch (Exception e){
			logger.error("Caught error "+e.toString()+" trying to find a state.",e);
			value = null;
		}
		return value;
	}

	@Override
	public Collection<TreatmentGroupValuePathDTO> getTreatmentGroupValues(
			Collection<GenericValuePathRequest> genericRequests) {
		Collection<TreatmentGroupValuePathDTO> results = new ArrayList<TreatmentGroupValuePathDTO>();
		for (GenericValuePathRequest request : genericRequests){
			TreatmentGroupValuePathDTO result = new TreatmentGroupValuePathDTO();
			result.setIdOrCodeName(request.getIdOrCodeName());
			result.setValueType(request.getValueType());
			result.setValueKind(request.getValueKind());
			result.setValues(getTreatmentGroupValues(request.getIdOrCodeName(), request.getStateType(), request.getStateKind(), request.getValueType(), request.getValueKind()));
			results.add(result);
		}
		return results;
	}
	
	private Collection<TreatmentGroupValue> getTreatmentGroupValues(String idOrCodeName, String stateType, String stateKind, String valueType, String valueKind){
		if (SimpleUtil.isNumeric(idOrCodeName)){
			Long id = Long.valueOf(idOrCodeName);
			return TreatmentGroupValue.findTreatmentGroupValuesByTreatmentGroupIDAndStateTypeKindAndValueTypeKind(id, stateType, stateKind, valueType, valueKind).getResultList();
		}else{
			return TreatmentGroupValue.findTreatmentGroupValuesByTreatmentGroupCodeNameAndStateTypeKindAndValueTypeKind(idOrCodeName, stateType, stateKind, valueType, valueKind).getResultList();
		}
	}

}
