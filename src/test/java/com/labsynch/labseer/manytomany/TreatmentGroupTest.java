package com.labsynch.labseer.manytomany;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.TreatmentGroup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class TreatmentGroupTest {
	
	private static final Logger logger = LoggerFactory.getLogger(TreatmentGroupTest.class);

	@Transactional
	@Test
	public void Treatment_Group_Many_To_Many_Test() {
		TreatmentGroup treatmentGroup = TreatmentGroup.findTreatmentGroup(16L);
		logger.debug(treatmentGroup.toJson());
		
		int theSize = treatmentGroup.getSubjects().size();
		logger.debug(String.valueOf(theSize));
		
		Assert.assertEquals(4, theSize);
	}

}
