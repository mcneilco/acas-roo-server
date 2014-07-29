package com.labsynch.labseer.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.domain.AbstractValue;
import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.DDictValue;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.KeyValueDTO;


@Service
@Transactional
public class ExperimentValueServiceImpl implements ExperimentValueService {

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
	public KeyValueDTO getKeyValueList(List<ExperimentValue> experimentValues) {
		List<String> values = new ArrayList<String>();
		for (ExperimentValue experimentValue : experimentValues){
			if (experimentValue.getLsType().equalsIgnoreCase("numericValue")){
				values.add(experimentValue.getNumericValue().toString());
			} else if (experimentValue.getLsType().equalsIgnoreCase("stringValue")){
				values.add(experimentValue.getStringValue());
			} else if (experimentValue.getLsType().equalsIgnoreCase("codeValue")){
				values.add(experimentValue.getCodeValue());
			}
		}
		
		KeyValueDTO transferDTO = new KeyValueDTO();
		transferDTO.setKey("lsValue");
		transferDTO.setValue(values.toString());
		return transferDTO;
		
	}
	
	@Override
	public List<CodeTableDTO> convertToCodeTables(List<ExperimentValue> experimentValues) {
		List<CodeTableDTO> codeTableList = new ArrayList<CodeTableDTO>();
		for (ExperimentValue val : experimentValues) {
			if (val.getLsType().equalsIgnoreCase("stringValue") || (val.getLsType().equalsIgnoreCase("numericValue"))){
				CodeTableDTO codeTable = new CodeTableDTO();
				if ((val.getLsType().equalsIgnoreCase("numericValue"))){
					codeTable.setName(val.getNumericValue().toString());
				} else {
					codeTable.setName(val.getStringValue());
				}
				codeTable.setCode(val.getId().toString());
				codeTable.setCodeName(val.getId().toString());
				codeTable.setIgnored(val.isIgnored());
				codeTableList.add(codeTable);			
			}
		}
		return codeTableList;	
	}
}
