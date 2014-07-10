

package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.domain.AbstractThing;
import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.LabelKind;
import com.labsynch.labseer.domain.LabelSequence;
import com.labsynch.labseer.domain.LabelType;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.domain.LsThingState;
import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.domain.StateKind;
import com.labsynch.labseer.domain.StateType;
import com.labsynch.labseer.domain.ThingKind;
import com.labsynch.labseer.domain.ThingType;
import com.labsynch.labseer.domain.ValueKind;
import com.labsynch.labseer.domain.ValueType;
import com.labsynch.labseer.dto.AnalysisGroupCsvDTO;
import com.labsynch.labseer.dto.AutoLabelDTO;
import com.labsynch.labseer.dto.EntrezDbGeneDTO;
import com.labsynch.labseer.dto.TempThingDTO;
import com.labsynch.labseer.utils.PropertiesUtilService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Configurable
public class CreateAnalysisGroupsFromCSVFileTests {

	private static final Logger logger = LoggerFactory.getLogger(CreateAnalysisGroupsFromCSVFileTests.class);

	@Autowired
	private AutoLabelService autoLabelService;

	@Autowired
	private AnalysisGroupService analysisGroupService;

	@Autowired
	private PropertiesUtilService propertiesUtilService;


	private String fieldDelimiter = ",";

	
	@Test
	@Transactional
	public void ReadCSVFile_Test11() throws IOException{
		
		String absoluteFilePath = "/Users/goshiro2014/Documents/McNeilco_2012/clients/ACAS_ROO_GIT/git/acas-roo-server/src/test/resources/analysisGroupData_v2.csv";
		HashMap<String, TempThingDTO> output = analysisGroupService.createAnalysisGroupsFromCSV(absoluteFilePath );
		
	}
	
	//@Test
	@Transactional
	public void ReadCSVFile_Test1() throws IOException{
		
		int batchSize = propertiesUtilService.getBatchSize();


		ICsvBeanReader beanReader = null;

		HashMap<String, TempThingDTO> analysisGroupMap = new HashMap<String, TempThingDTO>();
		HashMap<String, TempThingDTO> analysisStateMap = new HashMap<String, TempThingDTO>();
		HashMap<String, TempThingDTO> analysisValueMap = new HashMap<String, TempThingDTO>();

		try {




			HashMap<String, String> headerMap = getHeaderMap();

			logger.info("read csv delimited file");
			String testFileName = "analysisGroupData_v2.csv";
			InputStream is = CreateAnalysisGroupsFromCSVFileTests.class.getClassLoader().getResourceAsStream(testFileName);

			InputStreamReader isr = new InputStreamReader(is);  
			BufferedReader br = new BufferedReader(isr);



			String nameMapping;


			beanReader = new CsvBeanReader(br, CsvPreference.EXCEL_PREFERENCE);
			String[] headerText = beanReader.getHeader(true);



			List<String> headerList = new ArrayList<String>();
			int position = 0;
			for (String head : headerText){
				logger.info("header column: " + position + "  " + head);
				headerList.add(head);
				//				if (geneBeanMap.get(head) != null){
				//					headerList.add(geneBeanMap.get(head));   				
				//				}
				position++;
			}


			logger.info("size of header list  " + headerList.size());
			String[] header = new String[headerList.size()];
			headerList.toArray(header);

			for (String head : header){
				logger.debug("header column array : " + position + "  " + head);
				position++;
			}

			final CellProcessor[] processors = AnalysisGroupCsvDTO.getProcessors();

			AnalysisGroupCsvDTO analysisGroupDTO;
			AnalysisGroup analysisGroup;
			AnalysisGroupState analysisGroupState;
			AnalysisGroupValue analysisGroupValue;
			
			long rowIndex = 1;
			while( (analysisGroupDTO = beanReader.read(AnalysisGroupCsvDTO.class, header, processors)) != null ) {
				System.out.println(String.format("lineNo=%s, rowNo=%s, bulkData=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), analysisGroupDTO));

				Experiment testExperiment = Experiment.findExperimentEntries(0, 1).get(0);
				analysisGroupDTO.setExperimentID(testExperiment.getId().toString());
				analysisGroupDTO.setId(rowIndex);
//				analysisGroupDTO.setLsType("default");
//				analysisGroupDTO.setLsKind("default");

				analysisGroup = getOrCreateAnalysisGroup(analysisGroupDTO, analysisGroupMap);
				if (analysisGroup != null){
					analysisGroup.persist();
//				    if ( rowIndex % batchSize == 0 ) {
//				    	analysisGroup.flush();
//				    	analysisGroup.clear();
//				    }
					analysisGroupMap = saveTempAnalysisGroup(analysisGroup, analysisGroupDTO, analysisGroupMap);
				}
			

				analysisGroupState = getOrCreateAnalysisState(analysisGroupDTO, analysisStateMap, analysisGroupMap);
				if (analysisGroupState != null){
					analysisGroupState.persist();
//				    if ( rowIndex % batchSize == 0 ) {
//				    	analysisGroupState.flush();
//				    	analysisGroupState.clear();
//				    }
				    analysisStateMap = saveTempAnalysisState(analysisGroupState, analysisGroupDTO, analysisStateMap);
				}
				
				analysisGroupValue = getOrCreateAnalysisValue(analysisGroupDTO, analysisValueMap, analysisStateMap);
				if (analysisGroupValue != null){
					analysisGroupValue.persist();
				    if ( rowIndex % batchSize == 0 ) {
				    	analysisGroupValue.flush();
				    	analysisGroupValue.clear();
				    }
				    analysisValueMap = saveTempAnalysisValue(analysisGroupValue, analysisGroupDTO, analysisValueMap);
				}
				
				rowIndex++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			beanReader.close();
		}
	}

	private HashMap<String, TempThingDTO> saveTempAnalysisValue(
			AnalysisGroupValue analysisGroupValue,
			AnalysisGroupCsvDTO analysisGroupDTO,
			HashMap<String, TempThingDTO> analysisValueMap) {

		TempThingDTO tempThingDTO = new TempThingDTO();
		tempThingDTO.setId(analysisGroupValue.getId());
		tempThingDTO.setTempId(analysisGroupDTO.getId().toString());
		analysisValueMap.put(analysisGroupDTO.getId().toString(), tempThingDTO);
		return analysisValueMap;
	}

	private HashMap<String, TempThingDTO> saveTempAnalysisState(
			AnalysisGroupState analysisGroupState,
			AnalysisGroupCsvDTO analysisGroupDTO,
			HashMap<String, TempThingDTO> analysisStateMap) {

		TempThingDTO tempThingDTO = new TempThingDTO();
		tempThingDTO.setId(analysisGroupState.getId());
		tempThingDTO.setTempId(analysisGroupDTO.getAnalysisGroupID());
		analysisStateMap.put(analysisGroupDTO.getStateID().toString(), tempThingDTO);

		return analysisStateMap;
	}

	private HashMap<String, TempThingDTO> saveTempAnalysisGroup(
				AnalysisGroup analysisGroup,
				AnalysisGroupCsvDTO analysisGroupDTO, 
				HashMap<String, TempThingDTO> analysisGroupMap) {
		
		TempThingDTO tempThingDTO = new TempThingDTO();
		tempThingDTO.setId(analysisGroup.getId());
		tempThingDTO.setCodeName(analysisGroup.getCodeName());
		tempThingDTO.setTempId(analysisGroupDTO.getId().toString());
		analysisGroupMap.put(analysisGroupDTO.getId().toString(), tempThingDTO);
		return analysisGroupMap;
	}

	private AnalysisGroupValue getOrCreateAnalysisValue(
			AnalysisGroupCsvDTO analysisGroupDTO,
			HashMap<String, TempThingDTO> analysisValueMap, HashMap<String, TempThingDTO> analysisStateMap) {

		logger.debug("searching for analysisGroupDTO.id ---- " + analysisGroupDTO.getId());
		AnalysisGroupValue analysisGroupValue = null;

		if (!analysisValueMap.containsKey(analysisGroupDTO.getId().toString())){
			analysisGroupValue = new AnalysisGroupValue(analysisGroupDTO);
			AnalysisGroupState ags = AnalysisGroupState.findAnalysisGroupState(analysisStateMap.get(analysisGroupDTO.getStateID().toString()).getId());
			analysisGroupValue.setLsState(ags);


		} else {
			logger.debug("skipping the saved analysisGroupValue --------- " + analysisGroupDTO.getStateID());
		}

		return analysisGroupValue;
	}

	private AnalysisGroupState getOrCreateAnalysisState(
			AnalysisGroupCsvDTO analysisGroupDTO,
			HashMap<String, TempThingDTO> analysisStateMap, HashMap<String, TempThingDTO> analysisGroupMap) {

		AnalysisGroupState analysisGroupState = null;
		if (!analysisStateMap.containsKey(analysisGroupDTO.getStateID().toString())){
			analysisGroupState = new AnalysisGroupState(analysisGroupDTO);
			analysisGroupState.setAnalysisGroup(AnalysisGroup.findAnalysisGroup(analysisGroupMap.get(analysisGroupDTO.getAnalysisGroupID()).getId()));


		} else {
			logger.debug("skipping the saved analysisGroupState --------- " + analysisGroupDTO.getStateID());
		}

		return analysisGroupState;
	}


	private AnalysisGroup getOrCreateAnalysisGroup(AnalysisGroupCsvDTO analysisGroupDTO, HashMap<String, TempThingDTO> analysisGroupMap) {
		AnalysisGroup analysisGroup = null;
		if (!analysisGroupMap.containsKey(analysisGroupDTO.getAnalysisGroupID().toString())){
			analysisGroup = new AnalysisGroup(analysisGroupDTO);
			analysisGroup.setExperiment(Experiment.findExperiment(analysisGroupDTO.getExperimentID()));
			analysisGroup = analysisGroupService.saveLsAnalysisGroup(analysisGroup);
		} else {
			logger.debug("skipping the saved analysisGroup --------- " + analysisGroupDTO.getCodeName());
		}

		return analysisGroup;
	}



	private HashMap<String, String> getHeaderMap() {
		// map header text to standard bean names
		HashMap<String, String> beanMap = new HashMap<String, String>();
		beanMap.put("tax_id", "taxId");
		beanMap.put("GeneID", "geneId");
		beanMap.put("Symbol", "symbol");
		beanMap.put("LocusTag", "locusTag");
		beanMap.put("Synonyms", "synonyms");
		beanMap.put("dbXrefs", "dbXrefs");
		beanMap.put("chromosome", "chromosome");
		beanMap.put("map_location", "mapLocation");
		beanMap.put("description", "description");
		beanMap.put("type_of_gene", "typeOfGene");
		beanMap.put("Symbol_from_nomenclature_authority", "symbolFromAuthority");
		beanMap.put("Full_name_from_nomenclature_authority", "fullNameFromAuthority");
		beanMap.put("Nomenclature_status", "nomenclatureStatus");
		beanMap.put("Other_designations", "otherDesignations");
		beanMap.put("Modification_date", "modificationDate");		

		return beanMap;
	}

}
