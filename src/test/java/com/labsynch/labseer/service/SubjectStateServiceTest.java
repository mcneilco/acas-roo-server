

package com.labsynch.labseer.service;

import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import junit.framework.Assert;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
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
	
	@Test
	@Transactional
	public void createSubjectStateBySubjectIdAndStateTypeKindTest() {
		Long subjectId = 155L;
		String lsType = "metadata";
		String lsKind = "subject metadata";
		SubjectState subjectState = subjectStateService.createSubjectStateBySubjectIdAndStateTypeKind(subjectId, lsType, lsKind);
		Assert.assertNotNull(subjectState);
		logger.info(subjectState.toJson());
	}
}
