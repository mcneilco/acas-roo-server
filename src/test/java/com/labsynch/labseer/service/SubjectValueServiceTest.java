

package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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

import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.utils.PropertiesUtilService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class SubjectValueServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(SubjectValueServiceTest.class);
	
	@Autowired
	private PropertiesUtilService propertiesUtilService;
		
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
	
	@Test
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
}
