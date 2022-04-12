

package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.dto.AnalysisGroupValueDTO;
import com.labsynch.labseer.dto.PreferredNameDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;

import flexjson.JSONTokener;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class AnalysisGroupValueServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(AnalysisGroupValueServiceTest.class);


	@Autowired
	private LsThingService lsThingService;
	
	@Autowired
	private AnalysisGroupValueService analysisGroupValueService;

	//@Test
	public void SimpleTest_1(){
		AnalysisGroupState analysisGroupState = AnalysisGroupState.findAnalysisGroupState(5527L);
		//		logger.info(analysisGroupState.toJson());
		String json = "{            \"lsState\": {\"id\":1324,\"version\":0},            \"lsType\": \"numericValue\",            \"lsKind\": \"B/P Ratio\",            \"stringValue\": null,            \"fileValue\": null,            \"urlValue\": null,            \"dateValue\": null,            \"clobValue\": null,            \"blobValue\": null,            \"operatorKind\": null,            \"operatorType\": \"comparison\",            \"numericValue\": 0.00398036849215413,            \"sigFigs\": null,            \"uncertainty\": null,            \"uncertaintyType\": null,            \"numberOfReplicates\": null,            \"unitKind\": null,            \"comments\": null,            \"ignored\": false,            \"publicData\": true,            \"codeValue\": null,            \"recordedBy\": \"smeyer\",            \"recordedDate\": 1379364052000,            \"lsTransaction\": 11564          } ";
		logger.info("incoming json:" + json);
		AnalysisGroupValue analysisGroupValue = AnalysisGroupValue.fromJsonToAnalysisGroupValue(json);
		analysisGroupValue.persist();
		logger.info("numericValue: " + analysisGroupValue.getNumericValue());
		logger.info(analysisGroupValue.toPrettyJson());
		analysisGroupValue.remove();

	}

	//	@Test
	public void SimpleTest_2(){
		AnalysisGroupState analysisGroupState = AnalysisGroupState.findAnalysisGroupState(5527L);
		//		logger.info(analysisGroupState.toJson());
		String json = "[{            \"lsState\": {\"id\":1324,\"version\":0},            \"lsType\": \"numericValue\",            \"lsKind\": \"B/P Ratio\",            \"stringValue\": null,            \"fileValue\": null,            \"urlValue\": null,            \"dateValue\": null,            \"clobValue\": null,            \"blobValue\": null,            \"operatorKind\": null,            \"operatorType\": \"comparison\",            \"numericValue\": 0.00398036849215413,            \"sigFigs\": null,            \"uncertainty\": null,            \"uncertaintyType\": null,            \"numberOfReplicates\": null,            \"unitKind\": null,            \"comments\": null,            \"ignored\": false,            \"publicData\": true,            \"codeValue\": null,            \"recordedBy\": \"smeyer\",            \"recordedDate\": 1379364052000,            \"lsTransaction\": 11564          } ]";
		logger.info("incoming json:" + json);
		Collection<AnalysisGroupValue> analysisGroupValues = AnalysisGroupValue.fromJsonArrayToAnalysisGroupValues(json);
		for (AnalysisGroupValue analysisGroupValue : analysisGroupValues){
			logger.info("numericValue: " + analysisGroupValue.getNumericValue());
			logger.info(analysisGroupValue.toPrettyJson());        	
		}

	}

	//@Test
	public void SimpleTest_3(){
		String json = "[   {     \"codeName\": \"AG-00219240\",     \"lsType\": \"default\",     \"lsKind\": \"Generic\",     \"experiment\": {       \"id\": 206273,       \"version\": 1     },     \"recordedBy\": \"smeyer\",     \"lsTransaction\": 12426,     \"treatmentGroups\": null,     \"lsStates\": [       {         \"analysisGroup\": null,         \"lsValues\": [           {             \"lsState\": null,             \"lsType\": \"numericValue\",             \"lsKind\": \"%Control\",             \"stringValue\": null,             \"fileValue\": null,             \"urlValue\": null,             \"dateValue\": null,             \"clobValue\": null,             \"blobValue\": null,             \"operatorKind\": null,             \"operatorType\": \"comparison\",             \"numericValue\": 68.87,             \"sigFigs\": null,             \"uncertainty\": null,             \"uncertaintyType\": null,             \"numberOfReplicates\": null,             \"unitKind\": null,             \"comments\": null,             \"ignored\": false,             \"publicData\": true,             \"codeValue\": null,             \"recordedBy\": \"smeyer\",             \"recordedDate\": 1380132462000,             \"lsTransaction\": 12426           }         ],         \"recordedBy\": \"smeyer\",         \"lsType\": \"data\",         \"lsKind\": \"Generic\",         \"comments\": \"\",         \"lsTransaction\": 12426,         \"ignored\": false,         \"recordedDate\": 1380132462000       }     ],     \"recordedDate\": 1380132462000   } ]";
		logger.info("incoming json:" + json);
		Collection<AnalysisGroup> analysisGroups = AnalysisGroup.fromJsonArrayToAnalysisGroups(json);
		//		for (AnalysisGroup analysisGroup : analysisGroups){
		//			logger.info("numericValue: " + analysisGroupValue.getNumericValue());
		//			logger.info(analysisGroupValue.toPrettyJson());        	
		//		}

	}

	//@Test
	@Transactional
	public void SaveAnalysisGroupValuesJackson() throws JsonParseException, IOException{

		String fileName = "/Users/goshiro2014/Documents/McNeilco_2012/clients/Development/acas/agdata.json";

		long startTime = System.currentTimeMillis();

		int batchSize = 1000;
		int i = 0;		
		BufferedReader br = new BufferedReader(new FileReader(fileName));


		// Initialize 'stream', e.g.:
		//		 InputStream stream = new FileInputStream(fileName);

		ObjectMapper mapper = new ObjectMapper();
		JsonFactory jsonFactory = mapper.getFactory();
		JsonParser jp = jsonFactory.createParser(br);
		JsonToken token;
		long currentTime = System.currentTimeMillis();
		while ((token = jp.nextToken()) != null) {
			switch (token) {
			case START_OBJECT:
				JsonNode node = jp.readValueAsTree();
				// ... do something with the object
				AnalysisGroupValue analysisGroupValue = AnalysisGroupValue.fromJsonToAnalysisGroupValue(node.toString());
				analysisGroupValue.setId(null);
				analysisGroupValue.persist();
				if ( i % batchSize == 0 ) { 
					long loopTime = System.currentTimeMillis() - currentTime ;
					logger.debug(i + "  " + loopTime + "  Read object: " + node.toString());
					analysisGroupValue.flush();
					analysisGroupValue.clear();
					currentTime = System.currentTimeMillis();
				}
				i++;
				break;
			case END_ARRAY:
				break;
			case END_OBJECT:
				break;
			case FIELD_NAME:
				break;
			case NOT_AVAILABLE:
				break;
			case START_ARRAY:
				break;
			case VALUE_EMBEDDED_OBJECT:
				break;
			case VALUE_FALSE:
				break;
			case VALUE_NULL:
				break;
			case VALUE_NUMBER_FLOAT:
				break;
			case VALUE_NUMBER_INT:
				break;
			case VALUE_STRING:
				break;
			case VALUE_TRUE:
				break;
			default:
				break;
			}
		}



		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		logger.info("total elapsed time = " + totalTime + " miliseconds.");

	}

	//@Test
	//@Transactional
	public void SaveAnalysisGroupValues_3() throws FileNotFoundException{

		String fileName = "/tmp/agdata.json";

		long startTime = System.currentTimeMillis();

		int batchSize = 50;
		int i = 0;		
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		JSONTokener jsonTokens = new JSONTokener(br);		
		Object token;
		char delimiter;
		char END_OF_ARRAY = ']';
		while (jsonTokens.more()){
			delimiter = jsonTokens.nextClean();
			if (delimiter != END_OF_ARRAY){
				token = jsonTokens.nextValue();
				logger.debug(token.toString());
				AnalysisGroupValue analysisGroupValue = AnalysisGroupValue.fromJsonToAnalysisGroupValue(token.toString());
				analysisGroupValue.setId(null);
				analysisGroupValue.persist();
				if ( i % batchSize == 0 ) { 
					analysisGroupValue.flush();
					analysisGroupValue.clear();
				}
				i++;
			}
		}

		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		logger.info("total elapsed time = " + totalTime + " miliseconds.");

	}

	//@Test
	@Transactional
	public void GetAnalysisGroupByCodeValue_4(){

		String codeValue = "FL0029938-1-1";
		//		List<AnalysisGroupValue> agValues = AnalysisGroupValue.findAnalysisGroupValuesByCodeValueEquals(codeValue).getResultList();
		List<AnalysisGroupValueDTO> agValues = AnalysisGroupValue.findAnalysisGroupValueDTO(codeValue, true).getResultList();

		logger.info("number of agValues: " + agValues.size());

		for (Object agValue : agValues){
			logger.info(agValue.toString());

		}

		//		logger.info("AnalysisGroupValues: " + AnalysisGroupValue.toJsonArray(agValues));
		//		
		//		for (AnalysisGroupValue agValue : agValues){
		//			Experiment experiment = Experiment.findExperiment(agValue.getLsState().getAnalysisGroup().getExperiment().getId());
		//			Protocol protocol = Protocol.findProtocol(experiment.getProtocol().getId());
		//			
		//		}


	}

	//@Test
	@Transactional
	public void GetPrefferedName_5(){

		String thingName = "FL0029938-1-1";

		List<LsThingLabel> names = LsThingLabel.findLsThingLabelsByLabelTextEqualsAndIgnoredNot(thingName, true).getResultList();

		boolean foundPreferred = false;
		String preferredName = null;
		for (LsThingLabel name : names){
			if (name.isPreferred()){
				foundPreferred = true;
				preferredName = name.getLabelText();
			}
		}
		if (!foundPreferred){
			logger.error("did not find the preferred name");
		}

		logger.info("the preferred name is " + preferredName);


	}

	//@Test
	@Transactional
	public void GetCodeName_6(){

		String thingName = "Project 1";

		List<LsThing> lsThings = LsThing.findLsThingByLabelText(thingName).getResultList();

		logger.info("number of lsThings found " + lsThings.size());
		for (LsThing lsThing : lsThings){

			logger.info("lsThing codeName is : " + lsThing.getId() + "   " + lsThing.getCodeName());

		}

		List<String> thingNameList = new ArrayList<String>();
		thingNameList.add("Project 1");
		thingNameList.add("Apple");

		List<LsThing> lsThings2 = LsThing.findLsThingByLabelTextList(thingNameList).getResultList();

		logger.info("number of lsThings2 found " + lsThings2.size());
		for (LsThing lsThing : lsThings2){

			logger.info("lsThing codeName is : " + lsThing.getId() + "   " + lsThing.getCodeName());

		}

		PreferredNameDTO prefName1 = new PreferredNameDTO();
		prefName1.setRequestName("Project 1");
		PreferredNameDTO prefName2 = new PreferredNameDTO();
		prefName2.setRequestName("12");

		Collection<PreferredNameDTO> prefNamesCollection = new HashSet<PreferredNameDTO>();
		prefNamesCollection.add(prefName1);
		prefNamesCollection.add(prefName2);



		PreferredNameResultsDTO results = lsThingService.getGeneCodeNameFromName(PreferredNameDTO.toJsonArray(prefNamesCollection));
		String thingType = "gene";
		String thingKind = "entrez gene";
		String labelType = "name";
		String labelKind = "Entrez Gene ID";
		PreferredNameResultsDTO results2 = lsThingService.getPreferredNameFromName(thingType, thingKind, labelType, labelKind, PreferredNameDTO.toJsonArray(prefNamesCollection));

		logger.info(results.toJson());
		logger.info("HERE IS RESULTS 2 ------------" + results2.toJson());

		String labelText = "A2M";
		List<LsThingLabel> ack = LsThingLabel.findLsThingPreferredName("gene", "entrez gene", labelText).getResultList();
		logger.info(LsThingLabel.toJsonArray(ack));


	}

	//@Test
	@Transactional
	public void GetGenedata_7(){

		String thingType = "gene";
		String thingKind = "entrez gene";
		String labelType = "name";
		String labelKind = "Entrez Gene ID";
		String labelText = "5";

		List<LsThing> lsThings = LsThing.findLsThingByLabelText(thingType, thingKind, labelType, labelKind, labelText).getResultList();
		LinkedHashSet<String> geneCodeHash = new LinkedHashSet<String>();
		for (LsThing lsThing : lsThings){
			geneCodeHash.add(lsThing.getCodeName());
			logger.info("gene code name: " + lsThing.getCodeName());
		}

		Set<String> geneCodeList = new HashSet<String>();
//		geneCodeList.addAll(geneCodeHash);
		geneCodeList.add("CMPD-0000123-01");
		
		List<AnalysisGroupValueDTO> agValues = AnalysisGroupValue.findAnalysisGroupValueDTO(geneCodeList).getResultList();
		logger.info(AnalysisGroupValueDTO.toJsonArray(agValues));
		
		for(AnalysisGroupValueDTO agvDTO : agValues){
			logger.info(agvDTO.toJson());
		}


	}
	
	@Test
	@Transactional
	public void QueryAnalysisGroupValueByExpIdAndStateTypeKind(){
			
		Long experimentId = 9L;
		String stateType = "data";
		String stateKind = "Generic";
		List<AnalysisGroupValue> results = analysisGroupValueService.getAnalysisGroupValuesByExperimentIdAndStateTypeKind(experimentId, stateType, stateKind);
		logger.info(AnalysisGroupValue.toJsonArray(results));
		assert(results.size() == 11);
	}
	
	@Test
	@Transactional
	public void QueryAnalysisGroupValueByExpIdAndStateTypeKindWithBadData() {
		Long experimentId = 9L;
		String stateType = "";
		String stateKind = "experiment metadata";
		List<AnalysisGroupValue> results = new ArrayList<AnalysisGroupValue>();
		try {
			results = analysisGroupValueService.getAnalysisGroupValuesByExperimentIdAndStateTypeKind(experimentId, stateType, stateKind);
		} catch(IllegalArgumentException ex ) {
			logger.info(ex.getMessage());
		}
		assert(results.size() == 0);
	}
	
	@Test
	@Transactional
	public void QueryAnalysisGroupValueByExpIdAndStateTypeKindWithCodeName() {
		String experimentCodeName = "EXPT-00000184";
		String stateType = "data";
		String stateKind = "Generic";
		Experiment experiment = null;
		boolean didCatch = false;
		try {
			experiment = Experiment.findExperimentsByCodeNameEquals(experimentCodeName).getSingleResult();
		} catch(NoResultException nre) {
			logger.info(nre.getMessage());
			didCatch = true;
		}
		List<AnalysisGroupValue> results = new ArrayList<AnalysisGroupValue>();
		try {
			results = analysisGroupValueService.getAnalysisGroupValuesByExperimentIdAndStateTypeKind(experiment.getId(), stateType, stateKind);
		} catch(IllegalArgumentException ex ) {
			logger.info(ex.getMessage());
			assert(results.size() == 0);
			didCatch = true;
		}
		if(!didCatch) assert(results.size() == 11);
	}
	
	@Test
	@Transactional
	public void QueryAnalysisGroupValueByExpIdAndStateTypeKindAndValueTypeKind(){
			
		Long experimentId = 9L;
		String stateType = "data";
		String stateKind = "Generic";
		String valueType = "numericValue";
		String valueKind = "solubility";
		List<AnalysisGroupValue> results = analysisGroupValueService.getAnalysisGroupValuesByExperimentIdAndStateTypeKindAndValueTypeKind(experimentId, stateType, stateKind, valueType, valueKind);
		logger.info(AnalysisGroupValue.toJsonArray(results));
		assert(results.size() == 2);
	}
	
	//@Test
	@Transactional
	public void AnalysisGroupValuesToCsv() {
		List<AnalysisGroupValue> analysisGroupValues = analysisGroupValueService.getAnalysisGroupValuesByExperimentIdAndStateTypeKind(9l, "metadata", "experiment metadata");
		String csvString = analysisGroupValueService.getCsvList(analysisGroupValues);
		assert(csvString != null && csvString.compareTo("") != 0);
		logger.info(csvString);
	}
	
	@Test
	@Transactional
	public void QueryAnalysisGroupValueByAnalysisGroupIdAndStateTypeKind(){
			
		Long analysisGroupId = 10L;
		String stateType = "data";
		String stateKind = "Generic";
		List<AnalysisGroupValue> results = analysisGroupValueService.getAnalysisGroupValuesByAnalysisGroupIdAndStateTypeKind(analysisGroupId, stateType, stateKind);
		logger.info(AnalysisGroupValue.toJsonArray(results));
		assert(results.size() == 11);
	}
	
	@Test
	@Transactional
	public void QueryAnalysisGroupValueByAnalysisGroupIdAndStateTypeKindWithBadData() {
		Long analysisGroupId = 9L;
		String stateType = "";
		String stateKind = "experiment metadata";
		List<AnalysisGroupValue> results = new ArrayList<AnalysisGroupValue>();
		try {
			results = analysisGroupValueService.getAnalysisGroupValuesByAnalysisGroupIdAndStateTypeKind(analysisGroupId, stateType, stateKind);
		} catch(IllegalArgumentException ex ) {
			logger.info(ex.getMessage());
		}
		assert(results.size() == 0);
	}
	
	@Test
	@Transactional
	public void QueryAnalysisGroupValueByAnalysisGroupIdAndStateTypeKindWithCodeName() {
		String analysisGroupCodeName = "AG-00190427";
		String stateType = "data";
		String stateKind = "Generic";
		AnalysisGroup analysisGroup = null;
		boolean didCatch = false;
		try {
			analysisGroup = AnalysisGroup.findAnalysisGroupsByCodeNameEquals(analysisGroupCodeName).getSingleResult();
		} catch(NoResultException nre) {
			logger.info(nre.getMessage());
			didCatch = true;
		}
		List<AnalysisGroupValue> results = new ArrayList<AnalysisGroupValue>();
		try {
			results = analysisGroupValueService.getAnalysisGroupValuesByAnalysisGroupIdAndStateTypeKind(analysisGroup.getId(), stateType, stateKind);
		} catch(IllegalArgumentException ex ) {
			logger.info(ex.getMessage());
			assert(results.size() == 0);
			didCatch = true;
		}
		if(!didCatch) assert(results.size() == 11);
	}
	
	@Test
	@Transactional
	public void updateAnalysisGroupValueTest() {
		String idOrCodeName = "3";
		String stateType = "data";
		String stateKind = "results";
		String valueType = "stringValue";
		String valueKind = "status";
		String value = "Deleted";
		AnalysisGroupValue analysisGroupValue = analysisGroupValueService.updateAnalysisGroupValue(idOrCodeName, stateType, stateKind, valueType, valueKind, value);
		Assert.assertNotNull(analysisGroupValue);
		logger.info(analysisGroupValue.toJson());
	}

}
