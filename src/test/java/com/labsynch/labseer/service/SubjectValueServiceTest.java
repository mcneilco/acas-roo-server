

package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.utils.PropertiesUtilService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class SubjectValueServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(SubjectValueServiceTest.class);
	
	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
	@Autowired
	private SubjectValueService subjectValueService;
		
	//@Test
	@Transactional
	public void SimpleTest_1(){
		SubjectState subjectState = SubjectState.findSubjectState(1072L);
		logger.info(subjectState.toJson());
		logger.info(SubjectValue.toJsonArray(SubjectValue.findSubjectValuesByLsState(subjectState).getResultList()));
	}

	//@Test
	public void SimpleTest_2(){
		SubjectState subjectState = SubjectState.findSubjectState(17580L);
		SubjectValue subjectValue = new SubjectValue();
		subjectValue.setLsState(subjectState);
		subjectValue.setLsType("stringValue");
		subjectValue.setLsKind("notebook");
		subjectValue.setStringValue("blah blah testing");
		subjectValue.setRecordedBy("goshiro");
		subjectValue.setRecordedDate(new Date());
		subjectValue.persist();
		logger.info(subjectValue.toJson());
	}
	
//	@Test
	public void SimpleTest_3(){
		SubjectState subjectState = SubjectState.findSubjectState(17580L);
		List<SubjectValue> subjectValues = SubjectValue.findSubjectValuesByLsState(subjectState).getResultList();
		logger.info("number of subject values found: " + subjectValues.size());
		for (SubjectValue subjectValue : subjectValues){
			subjectValue.remove();
//			SubjectValue queryVal = SubjectValue.findSubjectValue(subjectValue.getId());
//			queryVal.getLsState().getLsValues().remove(queryVal);
//			queryVal.remove();
			//			logger.info("attempt to remove subjectValue: " + queryVal.getId());
//	        Assert.assertNotNull("'SubjectValue' failed to initialize correctly", queryVal);
//	        SubjectValue.delete(subjectValue);
		}
		logger.debug("removed all subject values");
	}
	
//	@Test
	@Transactional
	public void SimpleTest_4(){
		long startTime = new Date().getTime();
		SubjectState subjectState = SubjectState.findSubjectState(17580L);
		List<SubjectValue> subjectValues = new ArrayList<SubjectValue>();
		// create 100 subject values
		for ( int i=0; i < 102; i++ ) {
			SubjectValue subjectValue = new SubjectValue();
			subjectValue.setLsState(subjectState);
			subjectValue.setLsType("stringValue");
			subjectValue.setLsKind("notebook");
			subjectValue.setStringValue("blah blah testing");
			subjectValue.setRecordedBy("goshiro");
			subjectValue.setRecordedDate(new Date());
			subjectValues.add(subjectValue);
			subjectValue.persist();
		    if ( i % 50 == 0 ) { //50, same as the JDBC batch size
		    	subjectValue.flush();
		    	subjectValue.clear();
		    }
		}
//		SubjectValue.saveList(subjectValues);
		logger.debug("saved all subject values " + subjectValues.size());
		long endTime = new Date().getTime();
		logger.info("elapsed time = " + (endTime-startTime));
	}
	
	//@Test
	@Transactional
	public void SimpleTest_5() throws IOException{
		
		String fileName = "/tmp/valuesJSON.json";

		long startTime = System.currentTimeMillis();
		int batchSize = propertiesUtilService.getBatchSize();
		int i = 0;		
		
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		
		for (SubjectValue subjectValue : SubjectValue.fromJsonArrayToSubjectValues(br)){
			logger.warn(subjectValue.toJson());
//			subjectValue.persist();
//		    if ( i % batchSize == 0 ) { //50, same as the JDBC batch size
//		    	subjectValue.flush();
//		    	subjectValue.clear();
//		    }
		    i++;
		}

		logger.warn("total number of values in the array: " + i);
//		logger.debug(json);
		long endTime = new Date().getTime();
		logger.info("elapsed time = " + (endTime-startTime));
	}
	
	@Test
	@Transactional
	public void testSubjectServiceSave() {
		String json = "{\"codeTypeAndKind\":\"null_null\",\"id\":null,\"ignored\":false,\"lsKind\":\"Response\",\"lsState\":{\"id\":20,\"ignored\":false,\"lsKind\":\"results\",\"lsTransaction\":3,\"lsType\":\"data\",\"lsTypeAndKind\":\"data_results\",\"recordedBy\":\"jmcneil\",\"recordedDate\":1401368054000,\"version\":0},\"lsTransaction\":3,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_Response\",\"numericValue\":25.30,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"POSTTest\",\"recordedDate\":1401368054000,\"unitKind\":\"efficacy\",\"unitTypeAndKind\":\"null_efficacy\",\"version\":0}";
		SubjectValue subjectValue = SubjectValue.fromJsonToSubjectValue(json);
		subjectValueService.saveSubjectValue(subjectValue);
	}
	
	@Test
	@Transactional
	public void QuerySubjectValueByExpIdAndStateTypeKind(){
			
		Long experimentId = 14L;
		String stateType = "data";
		String stateKind = "test compound treatment";
		List<SubjectValue> results = subjectValueService.getSubjectValuesByExperimentIdAndStateTypeKind(experimentId, stateType, stateKind);
		logger.info(SubjectValue.toJsonArray(results));
		assert(results.size() == 800);
	}
	
	@Test
	@Transactional
	public void QuerySubjectValueByExpIdAndStateTypeKindWithBadData() {
		Long experimentId = 14L;
		String stateType = "";
		String stateKind = "experiment metadata";
		List<SubjectValue> results = new ArrayList<SubjectValue>();
		try {
			results = subjectValueService.getSubjectValuesByExperimentIdAndStateTypeKind(experimentId, stateType, stateKind);
		} catch(IllegalArgumentException ex ) {
			logger.info(ex.getMessage());
		}
		assert(results.size() == 0);
	}
	
	@Test
	@Transactional
	public void QuerySubjectValueByExpIdAndStateTypeKindWithCodeName() {
		String experimentCodeName = "EXPT-00000152";
		String stateType = "data";
		String stateKind = "test compound treatment";
		Experiment experiment = null;
		boolean didCatch = false;
		try {
			experiment = Experiment.findExperimentsByCodeNameEquals(experimentCodeName).getSingleResult();
		} catch(NoResultException nre) {
			logger.info(nre.getMessage());
			didCatch = true;
		}
		List<SubjectValue> results = new ArrayList<SubjectValue>();
		try {
			results = subjectValueService.getSubjectValuesByExperimentIdAndStateTypeKind(experiment.getId(), stateType, stateKind);
		} catch(IllegalArgumentException ex ) {
			logger.info(ex.getMessage());
			assert(results.size() == 0);
			didCatch = true;
		}
		if(!didCatch) assert(results.size() == 800);
	}
	
	@Test
	@Transactional
	public void QuerySubjectValueByExpIdAndStateTypeKindAndValueTypeKind(){
			
		Long experimentId = 14L;
		String stateType = "data";
		String stateKind = "test compound treatment";
		String valueType = "codeValue";
		String valueKind = "batch code";
		List<SubjectValue> results = subjectValueService.getSubjectValuesByExperimentIdAndStateTypeKindAndValueTypeKind(experimentId, stateType, stateKind, valueType, valueKind);
		logger.info(SubjectValue.toJsonArray(results));
		assert(results.size() == 400);
	}
	
	@Test
	@Transactional
	public void SubjectValuesToCsv() {
		List<SubjectValue> subjectValues = subjectValueService.getSubjectValuesBySubjectId(685408L);
		String csvString = subjectValueService.getCsvList(subjectValues);
		assert(csvString != null && csvString.compareTo("") != 0);
		logger.info(csvString);
	}
	
	@Test
	@Transactional
	public void SubjectValuesToCsvForCurveFit() {
		List<SubjectValue> subjectValues = subjectValueService.getSubjectValuesByExperimentIdAndStateTypeKindAndValueTypeKind(54375L, "data", "results", "numericValue", "Response");
		String csvString = subjectValueService.getCsvList(subjectValues);
		Assert.assertNotNull(csvString);
		logger.info(csvString);
	}
}
