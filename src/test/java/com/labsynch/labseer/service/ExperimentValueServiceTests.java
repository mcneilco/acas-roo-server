

package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.List;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.ExperimentValue;

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


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class ExperimentValueServiceTests {
	
	private static final Logger logger = LoggerFactory.getLogger(ExperimentValueServiceTests.class);
	
	@Autowired
	private ExperimentService experimentService;
	
	@Autowired
	private ExperimentValueService experimentValueService;
	
//	@Test
//	//@Transactional
//	public void ExperimentValueTest_1(){
//		
//		List<ExperimentValue> values = ExperimentValue.findExperimentValueEntries(0, 3);
//		for (ExperimentValue ev : values){
//			logger.info(ev.toJson());
//		}
//
//		ExperimentValue value = ExperimentValue.findExperimentValue(5010L);
//		value.setIgnored(false);
//		value.setComments("testing update of value 103");
//		ExperimentValue updateValue = ExperimentValue.update(value);
//		logger.info("#############################################");
//
//		logger.info(updateValue.toJson());
//		
//	}
	
//	@Test
	public void getExperimentValueByIdTest() {
		List<ExperimentValue> experimentValues = experimentValueService.getExperimentValuesByExperimentId(9l);
		assert(experimentValues != null);
		assert(!experimentValues.isEmpty());
	}
	
//	@Test
//	public void getExperimentValueByCodeTest() {
//		Long id = ApiExperimentController.retrieveExperimentIdFromCodeName("EXPT-00000003");
//		List<ExperimentValue> experimentValues = experimentValueService.getExperimentValuesById(id);
//		assert(experimentValues != null);
//		assert(!experimentValues.isEmpty());
//	}
	
//	@Test
	public void updateExperimentValueByIdTest() {
		List<ExperimentValue> experimentValues = experimentValueService.getExperimentValuesByExperimentId(9l);
		
		assert(experimentValues != null);
		assert(!experimentValues.isEmpty());
		ExperimentValue experimentValue = experimentValues.get(0);
		experimentValue.setRecordedBy("Test2");
		experimentValue.toJson();
		//ExperimentValue experimentValue2 = experimentValueService.updateExperimentValue(ExperimentValue.from)
	}

	//@Test
	public void updateExperimentValueExistingTest(){
		
		ExperimentValue testOutput = ExperimentValue.findExperimentValue(30l);
		//logger.info(testOutput.toJson());
		String json = "{\"clobValue\":null,\"codeKind\":null,\"codeType\":null,\"codeTypeAndKind\":\"null_null\",\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"id\":30,\"ignored\":false,\"lsKind\":\"status\",\"lsState\":{\"comments\":null,\"experiment\":{\"codeName\":\"EXPT-00000003\",\"id\":9,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":2,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"protocol\":{\"codeName\":\"PROT-00000002\",\"id\":1,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jmcneil\",\"recordedDate\":1401363350000,\"shortDescription\":\"protocol created by generic data parser\",\"version\":1},\"recordedBy\":\"jmcneil\",\"recordedDate\":1401365250000,\"shortDescription\":\"experiment created by generic data parser\",\"version\":1},\"id\":10,\"ignored\":false,\"lsKind\":\"experiment metadata\",\"lsTransaction\":2,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_experiment metadata\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jmcneil\",\"recordedDate\":1401365250000,\"version\":1},\"lsTransaction\":2,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_status\",\"modifiedBy\":null,\"modifiedDate\":null,\"numberOfReplicates\":null,\"numericValue\":null,\"operatorKind\":null,\"operatorType\":null,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jmcneil\",\"recordedDate\":1401365250000,\"sigFigs\":null,\"stringValue\":\"Approved\",\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null,\"unitType\":null,\"unitTypeAndKind\":\"null_null\",\"urlValue\":null,\"version\":0}";
		ExperimentValue experimentValue = ExperimentValue.create(json); 
		experimentValue.setRecordedBy("just testing");
		experimentValue.merge();
		logger.info("-----------" + experimentValue.toJson());

		
	}
	
	//@Test
	public void updateExperimentValueNullStateTest(){
		
		ExperimentValue testOutput = ExperimentValue.findExperimentValue(30l);
		String json = "{\"clobValue\":null,\"codeKind\":null,\"codeType\":null,\"codeTypeAndKind\":\"null_null\",\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"id\":30,\"ignored\":false,\"lsKind\":\"status\",\"lsState\":{\"comments\":null,\"experiment\":{\"codeName\":\"EXPT-00000003\",\"id\":9,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":2,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"protocol\":{\"codeName\":\"PROT-00000002\",\"id\":1,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jmcneil\",\"recordedDate\":1401363350000,\"shortDescription\":\"protocol created by generic data parser\",\"version\":1},\"recordedBy\":\"jmcneil\",\"recordedDate\":1401365250000,\"shortDescription\":\"experiment created by generic data parser\",\"version\":1},\"id\":null,\"ignored\":false,\"lsKind\":\"experiment metadata\",\"lsTransaction\":2,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_experiment metadata\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jmcneil\",\"recordedDate\":1401365250000,\"version\":1},\"lsTransaction\":2,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_status\",\"modifiedBy\":null,\"modifiedDate\":null,\"numberOfReplicates\":null,\"numericValue\":null,\"operatorKind\":null,\"operatorType\":null,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jmcneil\",\"recordedDate\":1401365250000,\"sigFigs\":null,\"stringValue\":\"Approved\",\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null,\"unitType\":null,\"unitTypeAndKind\":\"null_null\",\"urlValue\":null,\"version\":1}";
		ExperimentValue experimentValue = ExperimentValue.create(json); 
		experimentValue.setRecordedBy("just testing");
		logger.info("-----------" + experimentValue.toJson());
		
		if (experimentValue.getLsState().getId() == null){
			//create the state and persist
			ExperimentState experimentState = new ExperimentState(experimentValue.getLsState());
			logger.info("here is the experiment id: " + experimentValue.getLsState().getExperiment().getId());
			experimentState.setExperiment(Experiment.findExperiment(experimentValue.getLsState().getExperiment().getId()));
			experimentState.persist();
			experimentValue.setLsState(experimentState); 
		}
		
		experimentValue.merge();		
	}
	
	//@Test
	public void saveEvperimentValueTest() {
		String json = "{\"clobValue\":null,\"codeKind\":null,\"codeType\":null,\"codeTypeAndKind\":\"null_null\",\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":\"experiments/EXPT-00000003/1A_Generic.xlsx\",\"id\":null,\"ignored\":false,\"lsKind\":\"source file\",\"lsState\":{\"comments\":\"\",\"experiment\":{\"codeName\":\"EXPT-00000003\",\"id\":9,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":2,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"protocol\":{\"codeName\":\"PROT-00000002\",\"id\":1,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jmcneil\",\"recordedDate\":1401363350000,\"shortDescription\":\"protocol created by generic data parser\",\"version\":1},\"recordedBy\":\"jmcneil\",\"recordedDate\":1401365250000,\"shortDescription\":\"experiment created by generic data parser\",\"version\":1},\"id\":11,\"ignored\":false,\"lsKind\":\"raw results locations\",\"lsTransaction\":2,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_raw results locations\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jmcneil\",\"recordedDate\":1401365250000,\"version\":0},\"lsTransaction\":2,\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_source file\",\"modifiedBy\":null,\"modifiedDate\":null,\"numberOfReplicates\":null,\"numericValue\":null,\"operatorKind\":null,\"operatorType\":null,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"API_POST\",\"recordedDate\":1401365251000,\"sigFigs\":null,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null,\"unitType\":null,\"unitTypeAndKind\":\"null_null\",\"urlValue\":null,\"version\":0}";
		ExperimentValue experimentValue = ExperimentValue.create(json);
		experimentValue = experimentValueService.saveExperimentValue(experimentValue);
	}
	
	//@Test
	public void updateEvperimentValueTest() {
		String json = "{\"clobValue\":null,\"codeKind\":null,\"codeType\":null,\"codeTypeAndKind\":\"null_null\",\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":\"experiments/EXPT-00000003/1A_Generic.xlsx\",\"id\":37,\"ignored\":false,\"lsKind\":\"source file\",\"lsState\":{\"comments\":\"\",\"experiment\":{\"codeName\":\"EXPT-00000003\",\"id\":9,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":2,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"protocol\":{\"codeName\":\"PROT-00000002\",\"id\":1,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jmcneil\",\"recordedDate\":1401363350000,\"shortDescription\":\"protocol created by generic data parser\",\"version\":1},\"recordedBy\":\"jmcneil\",\"recordedDate\":1401365250000,\"shortDescription\":\"experiment created by generic data parser\",\"version\":1},\"id\":11,\"ignored\":false,\"lsKind\":\"raw results locations\",\"lsTransaction\":2,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_raw results locations\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jmcneil\",\"recordedDate\":1401365250000,\"version\":0},\"lsTransaction\":2,\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_source file\",\"modifiedBy\":null,\"modifiedDate\":null,\"numberOfReplicates\":null,\"numericValue\":null,\"operatorKind\":null,\"operatorType\":null,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"TEST_PUT\",\"recordedDate\":1401365251000,\"sigFigs\":null,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null,\"unitType\":null,\"unitTypeAndKind\":\"null_null\",\"urlValue\":null,\"version\":1}";
	    ExperimentValue experimentValue = ExperimentValue.create(json);
		experimentValue = experimentValueService.updateExperimentValue(experimentValue);
	}
	
	//@Test
	@Transactional
	public void QueryExperimentValueByKinds(){
		
		Long experimentId = 9L;
		String stateType = "metadata";
		String stateKind = "experiment metadata";
		String valueType = "stringValue";
		String valueKind = "analysis status";
		List<ExperimentValue> results = experimentValueService.getExperimentValuesByExperimentIdAndStateTypeKindAndValueTypeKind(experimentId, stateType, stateKind, valueType, valueKind);
		logger.info(ExperimentValue.toJsonArray(results));
		
	}
	
	//@Test
	@Transactional
	public void QueryExperimentValueByExpIdAndStateTypeKind(){
		
		Long experimentId = 9L;
		String stateType = "metadata";
		String stateKind = "experiment metadata";
		List<ExperimentValue> results = experimentValueService.getExperimentValuesByExperimentIdAndStateTypeKind(experimentId, stateType, stateKind);
		logger.info(ExperimentValue.toJsonArray(results));
		assert(results.size() == 8);
	}
	
	//@Test
	@Transactional
	public void QueryExperimentValueByExpIdAndStateTypeKindWithBadData() {
		Long experimentId = 9L;
		String stateType = "";
		String stateKind = "experiment metadata";
		List<ExperimentValue> results = new ArrayList<ExperimentValue>();
		try {
			results = experimentValueService.getExperimentValuesByExperimentIdAndStateTypeKind(experimentId, stateType, stateKind);
		} catch(IllegalArgumentException ex ) {
			logger.info(ex.getMessage());
		}
		assert(results.size() == 0);
	}
	
	//@Test
	@Transactional
	public void QueryExperimentValueByExpIdAndStateTypeKindWithCodeName() {
		String experimentCodeName = "EXPT-00000003";
		String stateType = "metadata";
		String stateKind = "experiment metadata";
		Experiment experiment = null;
		boolean didCatch = false;
		try {
			experiment = Experiment.findExperimentsByCodeNameEquals(experimentCodeName).getSingleResult();
		} catch(Exception nre) {
			logger.info(nre.getMessage());
			didCatch = true;
		}
		List<ExperimentValue> results = new ArrayList<ExperimentValue>();
		try {
			results = experimentValueService.getExperimentValuesByExperimentIdAndStateTypeKind(experiment.getId(), stateType, stateKind);
		} catch(IllegalArgumentException ex ) {
			logger.info(ex.getMessage());
			assert(results.size() == 0);
			didCatch = true;
		}
		if(!didCatch) assert(results.size() == 8);
	}
	
	@Test
	@Transactional
	public void ExperimentValuesToCodeName() {
		List<ExperimentValue> experimentValues = experimentValueService.getExperimentValuesByExperimentId(97418l);
		String csvString = experimentValueService.getCsvList(experimentValues);
		assert(csvString != null && csvString.compareTo("") != 0);
		logger.info(csvString);
	}
	
	@Test
	@Transactional
	public void updateExperimentValueTest() {
		String idOrCodeName = "EXPT-00000001";
		String stateType = "metadata";
		String stateKind = "experiment metadata";
		String valueType = "stringValue";
		String valueKind = "status";
		String value = "Created";
		ExperimentValue experimentValue = experimentValueService.updateExperimentValue(idOrCodeName, stateType, stateKind, valueType, valueKind, value);
		Assert.assertNotNull(experimentValue);
		logger.info(experimentValue.toJson());
	}
	
	@Test
	@Transactional
	public void updateExperimentValueTest2() {
		String idOrCodeName = "EXPT-00000001";
		String stateType = "metadata";
		String stateKind = "experiment metadata";
		String valueType = "clobValue";
		String valueKind = "analysis result html";
		String value = "<p>Upload completed.</p> <h4>Summary</h4><p>Information:</p> <ul> <li>Transaction Id: 1618</li><li>Format: Dose Response</li><li>Protocol: Dose Response Protocol</li><li>Experiment: John test 8-29-14a</li><li>Scientist: jam</li><li>Notebook: 911</li><li>Page: 12</li><li>Assay Date: 2012-11-07</li><li>Rows of Data: 17</li><li>Columns of Data: 7</li><li>Unique Corporate Batch ID's: 11</li><li>Raw Results Data Points: 1324</li><li>Flagged Data Points: 2</li><li>Experiment Code Name: EXPT-00000532</li> </ul><a href=/\"http://host4.labsynch.com:9080/seurat/runseurat?cmd=newjob&AssayName=Dose%20Response%20Protocol&AssayProtocol=EXPT-00000532%3a%3aJohn%20test%208-29-14a/\" target=/\"_blank/\" class=/\"btn/\">Open Seurat Report*</a> <a href=/\"mailto:?subject=Seurat Live Report for Dose Response Protocol: John test 8-29-14a&body=Click the following link to run Live Report: http%3a%2f%2fhost4.labsynch.com%3a9080%2fseurat%2frunseurat%3fcmd%3dnewjob%26AssayName%3dDose%2520Response%2520Protocol%26AssayProtocol%3dEXPT-00000532%253a%253aJohn%2520test%25208-29-14a/\" class=/\"btn/\">Email Link to Seurat Report</a> <p>*Note: there may be a delay before data is visible in Seurat</p>";
		ExperimentValue experimentValue = experimentValueService.updateExperimentValue(idOrCodeName, stateType, stateKind, valueType, valueKind, value);
		Assert.assertNotNull(experimentValue);
		logger.info(experimentValue.toJson());
	}
	
	@Test
	@Transactional
	public void updateExperimentValueTest3() {
		String idOrCodeName = "EXPT-00000002";
		String stateType = "metadata";
		String stateKind = "experiment metadata";
		String valueType = "codeValue";
		String valueKind = "previous experiment code";
		String value = "EXPT-00000009";
		ExperimentValue experimentValue = experimentValueService.updateExperimentValue(idOrCodeName, stateType, stateKind, valueType, valueKind, value);
		Assert.assertNotNull(experimentValue);
		logger.info(experimentValue.toJson());
	}
	
	@Test
//	@Transactional
	public void updateExperimentValueTest4() {
		String idOrCodeName = "EXPT-00000002";
		String stateType = "metadata";
		String stateKind = "experiment metadata";
		String valueType = "dateValue";
		String valueKind = "completion date";
		String value = "1398344800000";
		ExperimentValue experimentValue = experimentValueService.updateExperimentValue(idOrCodeName, stateType, stateKind, valueType, valueKind, value);
		Assert.assertNotNull(experimentValue);
		logger.info(experimentValue.toJson());
	}
	
	@Test
	@Transactional
	public void updateExperimentValueTest_NewKinds() {
		String idOrCodeName = "EXPT-00000002";
		String stateType = "metadata";
		String stateKind = "brian test metadata";
		String valueType = "stringValue";
		String valueKind = "brian comment";
		String value = "Hooray!";
		ExperimentValue experimentValue = experimentValueService.updateExperimentValue(idOrCodeName, stateType, stateKind, valueType, valueKind, value);
		Assert.assertNotNull(experimentValue);
		logger.info(experimentValue.toJson());
	}
	
	
	
	
}
