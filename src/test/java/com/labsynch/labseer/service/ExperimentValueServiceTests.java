

package com.labsynch.labseer.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.api.ApiExperimentController;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.ExperimentValue;


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
	
	@Test
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
	
}
