package com.labsynch.labseer.service;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.domain.TreatmentGroupLabel;
import com.labsynch.labseer.domain.TreatmentGroupState;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.dto.FlatThingCsvDTO;
import com.labsynch.labseer.dto.TempThingDTO;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Service
@Transactional
public class TreatmentGroupServiceImpl implements TreatmentGroupService {

	private static final Logger logger = LoggerFactory.getLogger(TreatmentGroupServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Autowired
	private AutoLabelService autoLabelService;

	@Autowired
	private SubjectService subjectService;

	@Override
	@Transactional
	public TreatmentGroup saveLsTreatmentGroup(TreatmentGroup treatmentGroup){

		logger.debug("incoming meta treatmentGroup: " + treatmentGroup.toJson());
		int batchSize = propertiesUtilService.getBatchSize();
		Date recordedDate = new Date();
		TreatmentGroup newTreatmentGroup = new TreatmentGroup(treatmentGroup);
		newTreatmentGroup.setAnalysisGroup(AnalysisGroup.findAnalysisGroup(treatmentGroup.getAnalysisGroup().getId()));
		newTreatmentGroup.persist();
		logger.debug("persisted the newTreatmentGroup: " + newTreatmentGroup.toJson());

		if (treatmentGroup.getLsLabels() != null) {
			for(TreatmentGroupLabel treatmentGroupLabel : treatmentGroup.getLsLabels()){
				TreatmentGroupLabel newTreatmentGroupLabel = new TreatmentGroupLabel(treatmentGroupLabel);
				newTreatmentGroupLabel.setTreatmentGroup(newTreatmentGroup);
				if (newTreatmentGroupLabel.getRecordedDate() == null) {newTreatmentGroupLabel.setRecordedDate(recordedDate);}
				newTreatmentGroupLabel.persist();	
			}		
		}
		if (treatmentGroup.getLsStates() != null){
			for(TreatmentGroupState treatmentGroupState : treatmentGroup.getLsStates()){
				TreatmentGroupState newTreatmentGroupState = new TreatmentGroupState(treatmentGroupState);
				newTreatmentGroupState.setTreatmentGroup(newTreatmentGroup);
				newTreatmentGroupState.persist();
				if (treatmentGroupState.getLsValues() != null){
					for(TreatmentGroupValue treatmentGroupValue : treatmentGroupState.getLsValues()){
						treatmentGroupValue.setLsState(newTreatmentGroupState);
						if (treatmentGroupValue.getRecordedDate() == null) {treatmentGroupValue.setRecordedDate(recordedDate);}
						treatmentGroupValue.persist();
					}										
				}
			}					
		}

		if (treatmentGroup.getSubjects() != null){
			int j = 0;
			for(Subject subject : treatmentGroup.getSubjects()){
				Subject newSubject = subjectService.saveSubject(subject);
				if ( j % batchSize == 0 ) { 
					newSubject.flush();
					newSubject.clear();
				}
				j++;
			}
		}


		return TreatmentGroup.findTreatmentGroup(newTreatmentGroup.getId());
	}

	@Override
	@Transactional
	public TreatmentGroup updateTreatmentGroup(TreatmentGroup treatmentGroup){

		logger.info("incoming meta treatmentGroup to update: " + treatmentGroup.toJson());

		TreatmentGroup updatedTreatmentGroup = TreatmentGroup.update(treatmentGroup);

		if (treatmentGroup.getLsLabels() != null) {
			for(TreatmentGroupLabel treatmentGroupLabel : treatmentGroup.getLsLabels()){
				if (treatmentGroupLabel.getId() == null){
					TreatmentGroupLabel newTreatmentGroupLabel = new TreatmentGroupLabel(treatmentGroupLabel);
					newTreatmentGroupLabel.setTreatmentGroup(updatedTreatmentGroup);
					newTreatmentGroupLabel.persist();
					treatmentGroupLabel.setId(newTreatmentGroupLabel.getId());
				} else {
					treatmentGroupLabel = TreatmentGroupLabel.update(treatmentGroupLabel);
				}
			}			
		} 

		if (treatmentGroup.getLsStates() != null){
			for(TreatmentGroupState treatmentGroupState : treatmentGroup.getLsStates()){
				if (treatmentGroupState.getId() == null){
					TreatmentGroupState newTreatmentGroupState = new TreatmentGroupState(treatmentGroupState);
					newTreatmentGroupState.setTreatmentGroup(updatedTreatmentGroup);
					if (newTreatmentGroupState.getRecordedDate() == null) {newTreatmentGroupState.setRecordedDate(new Date());}		
					if (newTreatmentGroupState.getRecordedBy() == null) { newTreatmentGroupState.setRecordedBy(updatedTreatmentGroup.getRecordedBy()); }
					newTreatmentGroupState.persist();
					treatmentGroupState.setId(newTreatmentGroupState.getId());
				} else {
					TreatmentGroupState updatedTreatmentGroupState = TreatmentGroupState.update(treatmentGroupState);
				}
				if (treatmentGroupState.getLsValues() != null){
					for(TreatmentGroupValue treatmentGroupValue : treatmentGroupState.getLsValues()){
						if (treatmentGroupValue.getId() == null){
							if (treatmentGroupValue.getRecordedDate() == null) {treatmentGroupValue.setRecordedDate(new Date());}
							treatmentGroupValue.setLsState(TreatmentGroupState.findTreatmentGroupState(treatmentGroupState.getId()));
							treatmentGroupValue.persist();
						} else {
							TreatmentGroupValue updatedTreatmentGroupValue = TreatmentGroupValue.update(treatmentGroupValue);
						}

					}				
				}
			}
		}

		if (treatmentGroup.getSubjects() != null){
			for(Subject subject : treatmentGroup.getSubjects()){
				if (subject.getId() == null){
					Subject newSubject = new Subject(subject);
					newSubject.setTreatmentGroup(treatmentGroup);
					newSubject.persist();							
				} else {
					subject = Subject.update(subject);
				}
			}
		}

		return TreatmentGroup.findTreatmentGroup(treatmentGroup.getId());

	}

	@Override
	public HashMap<String, TempThingDTO> createLsTreatmentGroupsFromCSV(
			HashMap<String, TempThingDTO> analysisGroupMap,
			String treatmentGroupCSV, String subjectCSV) {

		HashMap<String, TempThingDTO> treatmentGroupMap = createTreatmentGroupsFromCSV(analysisGroupMap, treatmentGroupCSV);

		if (subjectCSV != null){

			//HashMap<String, TempThingDTO> subjectGroupMap = treatmentGroupService.createLsTreatmentGroupsFromCSV(analysisGroupMap, treatmentGroupCSV, subjectCSV);

		}

		return analysisGroupMap;

	}

	private HashMap<String, TempThingDTO> createTreatmentGroupsFromCSV(
			HashMap<String, TempThingDTO> analysisGroupMap,
			String treatmentGroupCSV) {


		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional

	public HashMap<String, TempThingDTO> createTreatmentGroupsFromCSV(
			String treatmentGroupFilePath, HashMap<String, TempThingDTO> analysisGroupMap) throws IOException {

		int batchSize = propertiesUtilService.getBatchSize();
		ICsvBeanReader beanReader = null;
		HashMap<String, TempThingDTO> treatmentGroupMap = new HashMap<String, TempThingDTO>();
		HashMap<String, TempThingDTO> treatmentStateMap = new HashMap<String, TempThingDTO>();
		HashMap<String, TempThingDTO> treatmentValueMap = new HashMap<String, TempThingDTO>();

		try {
			logger.info("read csv delimited file");
			InputStream is = new FileInputStream(treatmentGroupFilePath);  
			InputStreamReader isr = new InputStreamReader(is);  
			BufferedReader br = new BufferedReader(isr);

			beanReader = new CsvBeanReader(br, CsvPreference.TAB_PREFERENCE);
			String[] headerText = beanReader.getHeader(true);

			List<String> headerList = new ArrayList<String>();
			int position = 0;
			for (String head : headerText){
				logger.info("header column: " + position + "  " + head);
				headerList.add(head);
				position++;
			}

			logger.info("size of header list  " + headerList.size());
			String[] header = new String[headerList.size()];
			headerList.toArray(header);

			for (String head : header){
				logger.debug("header column array : " + position + "  " + head);
				position++;
			}

			final CellProcessor[] processors = FlatThingCsvDTO.getProcessors();

			FlatThingCsvDTO treatmentGroupDTO;
			TreatmentGroup treatmentGroup;
			TreatmentGroupState treatmentGroupState;
			TreatmentGroupValue treatmentGroupValue;

			long rowIndex = 1;
			while( (treatmentGroupDTO = beanReader.read(FlatThingCsvDTO.class, header, processors)) != null ) {
				logger.debug(String.format("lineNo=%s, rowNo=%s, bulkData=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), treatmentGroupDTO));

				if (treatmentGroupDTO.getLsType() == null) treatmentGroupDTO.setLsType("default");
				if (treatmentGroupDTO.getLsKind() == null) treatmentGroupDTO.setLsKind("default");
				if (treatmentGroupDTO.getTempId() == null) treatmentGroupDTO.setTempId(treatmentGroupDTO.getId().toString());

				treatmentGroup = getOrCreateTreatmentGroup(treatmentGroupDTO, treatmentGroupMap, analysisGroupMap);
				if (treatmentGroup != null){
					treatmentGroup.persist();
					logger.debug("saved the new treatment Group: ID: " + treatmentGroup.getId() + " codeName" + treatmentGroup.getCodeName());
					logger.debug("saved the new treatment group: " + treatmentGroup.toJson());
					treatmentGroupMap = saveTempTreatmentGroup(treatmentGroup, treatmentGroupDTO, treatmentGroupMap);
				}

				if (treatmentGroupDTO.getStateType() != null && treatmentGroupDTO.getStateKind() != null){
					if (treatmentGroupDTO.getTempStateId() == null) treatmentGroupDTO.setTempStateId(treatmentGroupDTO.getStateId().toString());
					logger.debug("treatmentGroupDTO.getTempStateId() is " + treatmentGroupDTO.getTempStateId());
					treatmentGroupState = getOrCreateTreatmentState(treatmentGroupDTO, treatmentStateMap, treatmentGroupMap);
					if (treatmentGroupState != null){
						treatmentGroupState.persist();
						logger.debug("saved the new treatment group state: " + treatmentGroupState.getId());
						logger.debug("saved the new treatment group state: " + treatmentGroupState.toJson());
						treatmentStateMap = saveTempTreatmentState(treatmentGroupState, treatmentGroupDTO, treatmentStateMap);
					}

					if (treatmentGroupDTO.getValueType() != null && treatmentGroupDTO.getValueKind() != null){
						if (treatmentGroupDTO.getTempValueId() == null) treatmentGroupDTO.setTempValueId(Long.toString(rowIndex));
						treatmentGroupValue = getOrCreateTreatmentValue(treatmentGroupDTO, treatmentValueMap, treatmentStateMap);
						if (treatmentGroupValue != null){
							treatmentGroupValue.persist();
							logger.debug("saved the treatment Group Value: " + treatmentGroupValue.toJson());
							if ( rowIndex % batchSize == 0 ) {
								treatmentGroupValue.flush();
								treatmentGroupValue.clear();
							}
							treatmentValueMap = saveTempTreatmentValue(treatmentGroupValue, treatmentGroupDTO, treatmentValueMap);
						}
					}
				} else {
					logger.debug("---------- not saving a new treatment group state: " + treatmentGroupDTO.getStateType());
				}

				rowIndex++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			beanReader.close();
		}

		return treatmentGroupMap;
	}

	private HashMap<String, TempThingDTO> saveTempTreatmentValue(
			TreatmentGroupValue treatmentGroupValue,
			FlatThingCsvDTO treatmentGroupDTO,
			HashMap<String, TempThingDTO> treatmentValueMap) {

		TempThingDTO tempThingDTO = new TempThingDTO();
		tempThingDTO.setId(treatmentGroupValue.getId());
		tempThingDTO.setTempId(treatmentGroupDTO.getTempValueId());
		treatmentValueMap.put(treatmentGroupDTO.getTempValueId(), tempThingDTO);

		return treatmentValueMap;	
	}

	private TreatmentGroupValue getOrCreateTreatmentValue(
			FlatThingCsvDTO treatmentGroupDTO,
			HashMap<String, TempThingDTO> treatmentValueMap,
			HashMap<String, TempThingDTO> treatmentStateMap) {

		TreatmentGroupValue treatmentGroupValue = null;
		if (!treatmentValueMap.containsKey(treatmentGroupDTO.getTempValueId())){
			if (treatmentGroupDTO.getValueId() == null){
				treatmentGroupValue = new TreatmentGroupValue(treatmentGroupDTO);
				logger.debug("query state: " + treatmentGroupDTO.getTempStateId());
				Set<String> treatmentStateKeys = treatmentStateMap.keySet();
				for (String key : treatmentStateKeys){
					logger.debug("treatmentStateKey: " + key);
				}
				treatmentGroupValue.setLsState(TreatmentGroupState.findTreatmentGroupState(treatmentStateMap.get(treatmentGroupDTO.getTempStateId()).getId()));				
			} else {
				treatmentGroupValue = TreatmentGroupValue.findTreatmentGroupValue(treatmentGroupDTO.getValueId());
			}
		} else {
			logger.debug("skipping the saved treatmentGroupValue --------- " + treatmentGroupDTO.getValueId());
		}

		return treatmentGroupValue;
	}

	private HashMap<String, TempThingDTO> saveTempTreatmentState(
			TreatmentGroupState treatmentGroupState,
			FlatThingCsvDTO treatmentGroupDTO,
			HashMap<String, TempThingDTO> treatmentStateMap) {

		TempThingDTO tempThingDTO = new TempThingDTO();
		tempThingDTO.setId(treatmentGroupState.getId());
		tempThingDTO.setTempId(treatmentGroupDTO.getTempStateId());
		logger.debug("saving the temp state: " + tempThingDTO.getTempId());
		treatmentStateMap.put(treatmentGroupDTO.getTempStateId(), tempThingDTO);

		return treatmentStateMap;		
	}

	private TreatmentGroupState getOrCreateTreatmentState(
			FlatThingCsvDTO treatmentGroupDTO,
			HashMap<String, TempThingDTO> treatmentStateMap,
			HashMap<String, TempThingDTO> treatmentGroupMap) {

		TreatmentGroupState treatmentGroupState = null;
		if (!treatmentStateMap.containsKey(treatmentGroupDTO.getTempStateId())){
			if (treatmentGroupDTO.getStateId() == null){
				treatmentGroupState = new TreatmentGroupState(treatmentGroupDTO);
				treatmentGroupState.setTreatmentGroup(TreatmentGroup.findTreatmentGroup(treatmentGroupMap.get(treatmentGroupDTO.getTempId()).getId()));				
			} else {
				treatmentGroupState = TreatmentGroupState.findTreatmentGroupState(treatmentGroupDTO.getStateId());
			}
		} else {
			logger.debug("skipping the saved treatmentGroupState --------- " + treatmentGroupDTO.getStateId());
		}

		return treatmentGroupState;

	}

	private HashMap<String, TempThingDTO> saveTempTreatmentGroup(
			TreatmentGroup treatmentGroup, FlatThingCsvDTO treatmentGroupDTO,
			HashMap<String, TempThingDTO> treatmentGroupMap) {

		TempThingDTO tempThingDTO = new TempThingDTO();
		tempThingDTO.setId(treatmentGroup.getId());
		tempThingDTO.setCodeName(treatmentGroup.getCodeName());
		tempThingDTO.setTempId(treatmentGroupDTO.getTempId().toString());
		treatmentGroupMap.put(treatmentGroupDTO.getTempId(), tempThingDTO);
		return treatmentGroupMap;

	}

	private TreatmentGroup getOrCreateTreatmentGroup(
			FlatThingCsvDTO treatmentGroupDTO,
			HashMap<String, TempThingDTO> treatmentGroupMap,
			HashMap<String, TempThingDTO> analysisGroupMap) {

		TreatmentGroup treatmentGroup = null;
		if (!treatmentGroupMap.containsKey(treatmentGroupDTO.getTempId())){
			if (treatmentGroupDTO.getId() == null){
				if (treatmentGroupDTO.getTempParentId() != null && !treatmentGroupDTO.getTempParentId().equalsIgnoreCase("null")){
					treatmentGroup = new TreatmentGroup(treatmentGroupDTO);
					treatmentGroup.setAnalysisGroup(AnalysisGroup.findAnalysisGroup(analysisGroupMap.get(treatmentGroupDTO.getTempParentId()).getId()));
					if (treatmentGroup.getCodeName() == null){
						String newCodeName = autoLabelService.getTreatmentGroupCodeName();
						logger.debug("------------------ new codeName: " + newCodeName);
						treatmentGroup.setCodeName(newCodeName);
					}
				} else {
					logger.debug("the temp parent ID is null" + treatmentGroupDTO.getTempParentId());
				}
			} else {
				treatmentGroup = TreatmentGroup.findTreatmentGroup(treatmentGroupDTO.getId());
			}
		} else {
			logger.debug("skipping the previously saved treatmentGroup --------- " + treatmentGroupDTO.getCodeName());
		}
		return treatmentGroup;
	}

}
