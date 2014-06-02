

package com.labsynch.labseer.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class SubjectStateServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(SubjectStateServiceTest.class);
	
	@Autowired
	private SubjectStateService subjectStateService;
	
	
	
	@Test
	public void SimpleTest_1(){
		SubjectState subjectState = SubjectState.findSubjectState(1203L);
		logger.info(subjectState.toJson());
		logger.info(SubjectValue.toJsonArray(SubjectValue.findSubjectValuesByLsState(subjectState).getResultList()));
		
		logger.info(subjectState.toString());
	}
}
