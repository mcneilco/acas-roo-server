package com.labsynch.labseer.service;

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

import com.labsynch.labseer.domain.TreatmentGroupState;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class TreatmentGroupStateServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(TreatmentGroupStateServiceTest.class);

	@Autowired
	private TreatmentGroupStateService treatmentGroupStateService;
	
	@Test
	@Transactional
	public void createTreatmentGroupStateByTreatmentGroupIdAndStateTypeKindTest() {
		Long treatmentGroupId = 35L;
		String lsType = "metadata";
		String lsKind = "treatmentGroup metadata";
		TreatmentGroupState treatmentGroupState = treatmentGroupStateService.createTreatmentGroupStateByTreatmentGroupIdAndStateTypeKind(treatmentGroupId, lsType, lsKind);
		Assert.assertNotNull(treatmentGroupState);
		logger.info(treatmentGroupState.toJson());
	}
}
