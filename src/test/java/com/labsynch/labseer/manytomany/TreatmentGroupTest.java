package com.labsynch.labseer.manytomany;

import java.util.Date;
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
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.service.TreatmentGroupService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class TreatmentGroupTest {
	
	private static final Logger logger = LoggerFactory.getLogger(TreatmentGroupTest.class);

	@Autowired
	private TreatmentGroupService treatmentGroupService;
	
	private Protocol makeTestingProtocol() {
		//initialize some entries to fill in the fields
		Date now = new Date();
		//create protocol
		Protocol protocol = new Protocol();
		protocol.setRecordedBy("test user");
		protocol.setLsKind("default");
		protocol.setLsType("default");
		protocol.setLsTransaction(98765L);
		protocol.persist();
		protocol.flush();
		return protocol;
	}
	
	private Experiment makeTestingExperiment() {
		Protocol protocol = makeTestingProtocol();
		Experiment experiment = new Experiment();
        experiment.setCodeName("EXPT-12345678");
        experiment.setIgnored(false);
        experiment.setLsKind(protocol.getLsKind());
        experiment.setLsType(protocol.getLsType());
        experiment.setRecordedBy(protocol.getRecordedBy());
        experiment.setRecordedDate(protocol.getRecordedDate());
        experiment.setLsTransaction(protocol.getLsTransaction());
        experiment.setProtocol(protocol);
        experiment.persist();
        experiment.flush();
        return experiment;
	}
	
	private AnalysisGroup makeTestingAnalysisGroup() {
		Experiment experiment = makeTestingExperiment();
		AnalysisGroup analysisgroup= new AnalysisGroup();
        analysisgroup.setCodeName("AG-12345678");
        analysisgroup.setIgnored(experiment.isIgnored());
        analysisgroup.setLsKind(experiment.getLsKind());
        analysisgroup.setLsType(experiment.getLsType());
        analysisgroup.setRecordedBy(experiment.getRecordedBy());
        analysisgroup.setLsTransaction(experiment.getLsTransaction());
        Set<Experiment> experimentSet = new HashSet<Experiment>();
        experimentSet.add(experiment);
        analysisgroup.setExperiments(experimentSet);
        analysisgroup.persist();
        analysisgroup.flush();
        return analysisgroup;
	}
	
	private TreatmentGroup makeTestingTreatmentGroup() {
		AnalysisGroup analysisgroup = makeTestingAnalysisGroup();
		TreatmentGroup treatmentgroup = new TreatmentGroup();
		treatmentgroup.setCodeName("TG-12345678");
		treatmentgroup.setIgnored(analysisgroup.isIgnored());
		treatmentgroup.setLsKind(analysisgroup.getLsKind());
		treatmentgroup.setLsType(analysisgroup.getLsType());
		treatmentgroup.setRecordedBy(analysisgroup.getRecordedBy());
		treatmentgroup.setLsTransaction(analysisgroup.getLsTransaction());
		Set<AnalysisGroup> analysisGroups = new HashSet<AnalysisGroup>();
		analysisGroups.add(analysisgroup);
		treatmentgroup.setAnalysisGroups(analysisGroups);
		treatmentgroup.persist();
		treatmentgroup.flush();
		return treatmentgroup;
	}
	
	@Transactional
	@Test
	public void findTreatmentGroupsByAnalysisGroupsTest() {
		TreatmentGroup treatmentgroup = makeTestingTreatmentGroup();
		Set<AnalysisGroup> analysisgroups = new HashSet<AnalysisGroup>();
		analysisgroups = treatmentgroup.getAnalysisGroups();
		TreatmentGroup check = TreatmentGroup.findTreatmentGroupsByAnalysisGroups(analysisgroups).getSingleResult();
		assert(treatmentgroup.toJson() == check.toJson());
	}
	
	@Transactional
	@Test
	public void findTreatmentGroupsByLsTransactionEqualsTest() {
		TreatmentGroup treatmentgroup = makeTestingTreatmentGroup();
		Long lstransaction = treatmentgroup.getLsTransaction();
		TreatmentGroup check = TreatmentGroup.findTreatmentGroupsByLsTransactionEquals(lstransaction).getSingleResult();
		assert(treatmentgroup.toJson() == check.toJson());
	}
	
	@Transactional
	@Test
	public void deleteByExperimentIdTest() {
		TreatmentGroup treatmentgroup = makeTestingTreatmentGroup();
		Long id = treatmentgroup.getId();
		Long experimentid = treatmentgroup.getAnalysisGroups().iterator().next().getExperiments().iterator().next().getId();
		TreatmentGroup checkbefore = TreatmentGroup.findTreatmentGroup(id);
		assert(checkbefore.toJson() == treatmentgroup.toJson());
		TreatmentGroup.deleteByExperimentID(experimentid);
		TreatmentGroup checkafter = TreatmentGroup.findTreatmentGroup(id);
		assert(checkafter.toJson() != treatmentgroup.toJson());
	}
	
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
	
	@Transactional
	//@Test
	public void SimpleTest4() {

		String json = "{\"analysisGroups\":[{\"id\":290,\"version\":0}],\"lsType\":\"default\",\"lsKind\":\"default\",\"codeName\":\"TG-00000035\",\"subjects\":null,\"lsStates\":null,\"recordedBy\":\"dfenger\",\"comments\":\"\",\"lsTransaction\":20,\"ignored\":false,\"recordedDate\":1403543647000}";
		TreatmentGroup inputTreatmentGroup = TreatmentGroup.fromJsonToTreatmentGroup(json);
		Date recordedDate = null;
		if (inputTreatmentGroup.getRecordedDate() != null){
			recordedDate = inputTreatmentGroup.getRecordedDate();
		} else {
			recordedDate = new Date();
		}
		TreatmentGroup treatmentGroup = treatmentGroupService.saveLsTreatmentGroup(inputTreatmentGroup.getAnalysisGroups(), inputTreatmentGroup, recordedDate);
		logger.debug(treatmentGroup.toJson());
	}
	
	@Transactional
	@Test
	public void updateTest() {
		TreatmentGroup treatmentgroup = new TreatmentGroup();
		
	}
	

}
