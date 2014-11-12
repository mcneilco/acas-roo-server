package com.labsynch.labseer.service;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.Charset;
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
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.domain.TreatmentGroupState;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.dto.TreatmentGroupValueDTO;


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
		if(ApiValueController.isNumeric(idOrCodeName)) {
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
				if (valueType.equals("stringValue")) treatmentGroupValue.setStringValue(value);
				if (valueType.equals("fileValue")) treatmentGroupValue.setFileValue(value);
				if (valueType.equals("clobValue")) treatmentGroupValue.setClobValue(value);
				if (valueType.equals("blobValue")) treatmentGroupValue.setBlobValue(value.getBytes(Charset.forName("UTF-8")));
				if (valueType.equals("numericValue")) treatmentGroupValue.setNumericValue(new BigDecimal(value));
				if (valueType.equals("dateValue")) treatmentGroupValue.setDateValue(new Date(Long.parseLong(value)));
				if (valueType.equals("codeValue")) treatmentGroupValue.setCodeValue(value);
				treatmentGroupValue.merge();
				logger.debug("Updated the treatmentGroup value: " + treatmentGroupValue.toJson());
			}
			else if (treatmentGroupValues.isEmpty()) {
				treatmentGroupValue = createTreatmentGroupValueByTreatmentGroupIdAndStateTypeKindAndValueTypeKind(treatmentGroupId, stateType, stateKind, valueType, valueKind, value);
				logger.debug("Created the treatmentGroup value: " + treatmentGroupValue.toJson());
			}
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


}
