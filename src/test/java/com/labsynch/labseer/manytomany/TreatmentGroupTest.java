package com.labsynch.labseer.manytomany;

import java.util.HashSet;
import java.util.Set;

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

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.service.TreatmentGroupService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class TreatmentGroupTest {
	
	private static final Logger logger = LoggerFactory.getLogger(TreatmentGroupTest.class);

	@Autowired
	private TreatmentGroupService treatmentGroupService;
	
	@Transactional
	//@Test
	public void Treatment_Group_Many_To_Many_Test() {
		TreatmentGroup treatmentGroup = TreatmentGroup.findTreatmentGroup(16L);
		logger.debug(treatmentGroup.toJson());
		
		int theSize = treatmentGroup.getSubjects().size();
		logger.debug(String.valueOf(theSize));
		
		Assert.assertEquals(4, theSize);
	}
	
	@Transactional
	//@Test
	public void SimpleTest2() {

		TreatmentGroup treatmentGroup = new TreatmentGroup();
		
		treatmentGroup.getAnalysisGroups().add(new AnalysisGroup());
		
		logger.debug(treatmentGroup.toJson());
		
		
	}
	
	@Transactional
	//@Test
	public void SimpleTest3() {

		TreatmentGroup treatmentGroup = new TreatmentGroup();
		Set<AnalysisGroup> analysisGroups = new HashSet<AnalysisGroup>();
		analysisGroups.add(new AnalysisGroup());
		treatmentGroup.setAnalysisGroups(analysisGroups);
		
		logger.debug(treatmentGroup.toJson());
		
		
	}
	
	//@Transactional
	@Test
	public void SimpleTest4() {

		String json = "{\"analysisGroups\":[{\"id\":290,\"version\":0}],\"lsType\":\"default\",\"lsKind\":\"default\",\"codeName\":\"TG-00000035\",\"subjects\":null,\"lsStates\":null,\"recordedBy\":\"dfenger\",\"comments\":\"\",\"lsTransaction\":20,\"ignored\":false,\"recordedDate\":1403543647000}";
		TreatmentGroup treatmentGroup = treatmentGroupService.saveLsTreatmentGroup(TreatmentGroup.fromJsonToTreatmentGroup(json));
		logger.debug(treatmentGroup.toJson());
		
		
	}

}
