package com.labsynch.labseer.manytomany;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
import com.labsynch.labseer.domain.Subject;
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
		protocol.setRecordedDate(now);
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
	
	public HashMap<String, Long> makeTestStack() {
		//each of these methods creates the full stack from protocol to subject.
		Date now = new Date();
		//create 1 Protocol
		Protocol protocol = new Protocol();
		protocol.setRecordedBy("test user");
		protocol.setRecordedDate(now);
		protocol.setLsKind("default");
		protocol.setLsType("default");
		protocol.setLsTransaction(98765L);
		protocol.persist();
		protocol.flush();
		
		//create 3 Experiments
		Experiment e1 = new Experiment();
        e1.setCodeName("T-EXPT-1111111");
        Experiment e2 = new Experiment();
        e2.setCodeName("T-EXPT-22222222");
        Experiment e3 = new Experiment();
        e3.setCodeName("T-EXPT-33333333");
        Set<Experiment> allExperiments = new HashSet<Experiment>();
        allExperiments.add(e1);
        allExperiments.add(e2);
        allExperiments.add(e3);
        for (Experiment experiment: allExperiments) {
        	experiment.setIgnored(false);
        	experiment.setLsKind(protocol.getLsKind());
        	experiment.setLsType(protocol.getLsType());
        	experiment.setRecordedBy(protocol.getRecordedBy());
        	experiment.setRecordedDate(protocol.getRecordedDate());
            experiment.setLsTransaction(protocol.getLsTransaction());
        	experiment.setProtocol(protocol);
        }
        
		//create 3 AnalysisGroups
        AnalysisGroup a1= new AnalysisGroup();
        a1.setCodeName("T-AG-11111111");
        AnalysisGroup a2= new AnalysisGroup();
        a2.setCodeName("T-AG-22222222");
        AnalysisGroup a3= new AnalysisGroup();
        a3.setCodeName("T-AG-33333333");
        Set<AnalysisGroup> allAnalysisGroups = new HashSet<AnalysisGroup>();
        allAnalysisGroups.add(a1);
        allAnalysisGroups.add(a2);
        allAnalysisGroups.add(a3);
        for (AnalysisGroup analysisGroup: allAnalysisGroups) {
        	analysisGroup.setIgnored(e1.isIgnored());
            analysisGroup.setLsKind(e1.getLsKind());
            analysisGroup.setLsType(e1.getLsType());
            analysisGroup.setRecordedBy(e1.getRecordedBy());
            analysisGroup.setRecordedDate(e1.getRecordedDate());
            analysisGroup.setLsTransaction(e1.getLsTransaction());
        }
        
        //create 3 TreatmentGroups
        TreatmentGroup t1 = new TreatmentGroup();
		t1.setCodeName("T-TG-11111111");
		TreatmentGroup t2 = new TreatmentGroup();
		t2.setCodeName("T-TG-22222222");
		TreatmentGroup t3 = new TreatmentGroup();
		t3.setCodeName("T-TG-33333333");
		Set<TreatmentGroup> allTreatmentGroups = new HashSet<TreatmentGroup>();
		allTreatmentGroups.add(t1);
		allTreatmentGroups.add(t2);
		allTreatmentGroups.add(t3);
		for (TreatmentGroup treatmentGroup: allTreatmentGroups) {
			treatmentGroup.setIgnored(a1.isIgnored());
			treatmentGroup.setLsKind(a1.getLsKind());
			treatmentGroup.setLsType(a1.getLsType());
			treatmentGroup.setRecordedBy(a1.getRecordedBy());
            treatmentGroup.setRecordedDate(a1.getRecordedDate());
			treatmentGroup.setLsTransaction(a1.getLsTransaction());
		}
		
		//create 3 Subjects
		Subject s1 = new Subject();
		s1.setCodeName("T-SUBJ-11111111");
		Subject s2 = new Subject();
		s2.setCodeName("T-SUBJ-22222222");
		Subject s3 = new Subject();
		s3.setCodeName("T-SUBJ-33333333");
		Set<Subject> allSubjects = new HashSet<Subject>();
		allSubjects.add(s1);
		allSubjects.add(s2);
		allSubjects.add(s3);
		for (Subject subject: allSubjects) {
			subject.setIgnored(t1.isIgnored());
			subject.setLsKind(t1.getLsKind());
			subject.setLsType(t1.getLsType());
			subject.setRecordedBy(t1.getRecordedBy());
            subject.setRecordedDate(t1.getRecordedDate());
			subject.setLsTransaction(t1.getLsTransaction());
		}
		
		//Create the various Sets for mapping
		Set<AnalysisGroup> e1AnalysisGroups = new HashSet<AnalysisGroup>();
		e1AnalysisGroups.add(a1);
		e1AnalysisGroups.add(a3);
		Set<AnalysisGroup> e2AnalysisGroups = new HashSet<AnalysisGroup>();
		e2AnalysisGroups.add(a1);
		e2AnalysisGroups.add(a2);
		Set<AnalysisGroup> e3AnalysisGroups = new HashSet<AnalysisGroup>();
		e3AnalysisGroups.add(a2);
		e3AnalysisGroups.add(a3);
		Set<Experiment> a1Experiments = new HashSet<Experiment>();
		a1Experiments.add(e1);
		a1Experiments.add(e2);
		Set<Experiment> a2Experiments = new HashSet<Experiment>();
		a2Experiments.add(e2);
		a2Experiments.add(e3);
		Set<Experiment> a3Experiments = new HashSet<Experiment>();
		a3Experiments.add(e1);
		a3Experiments.add(e3);
		Set<TreatmentGroup> a1TreatmentGroups = new HashSet<TreatmentGroup>();
		a1TreatmentGroups.add(t1);
		a1TreatmentGroups.add(t3);
		Set<TreatmentGroup> a2TreatmentGroups = new HashSet<TreatmentGroup>();
		a2TreatmentGroups.add(t1);
		a2TreatmentGroups.add(t2);
		Set<TreatmentGroup> a3TreatmentGroups = new HashSet<TreatmentGroup>();
		a3TreatmentGroups.add(t2);
		a3TreatmentGroups.add(t3);
		Set<AnalysisGroup> t1AnalysisGroups = new HashSet<AnalysisGroup>();
		t1AnalysisGroups.add(a1);
		t1AnalysisGroups.add(a2);
		Set<AnalysisGroup> t2AnalysisGroups = new HashSet<AnalysisGroup>();
		t2AnalysisGroups.add(a2);
		t2AnalysisGroups.add(a3);
		Set<AnalysisGroup> t3AnalysisGroups = new HashSet<AnalysisGroup>();
		t3AnalysisGroups.add(a1);
		t3AnalysisGroups.add(a3);
		Set<Subject> t1Subjects = new HashSet<Subject>();
		t1Subjects.add(s1);
		t1Subjects.add(s3);
		Set<Subject> t2Subjects = new HashSet<Subject>();
		t2Subjects.add(s1);
		t2Subjects.add(s2);
		Set<Subject> t3Subjects = new HashSet<Subject>();
		t3Subjects.add(s2);
		t3Subjects.add(s3);
		Set<TreatmentGroup> s1TreatmentGroups = new HashSet<TreatmentGroup>();
		s1TreatmentGroups.add(t1);
		s1TreatmentGroups.add(t2);
		Set<TreatmentGroup> s2TreatmentGroups = new HashSet<TreatmentGroup>();
		s2TreatmentGroups.add(t2);
		s2TreatmentGroups.add(t3);
		Set<TreatmentGroup> s3TreatmentGroups = new HashSet<TreatmentGroup>();
		s3TreatmentGroups.add(t1);
		s3TreatmentGroups.add(t3);
		
		//Then assign all the groups to their owners to create the mappings
		e1.setAnalysisGroups(e1AnalysisGroups);
		e2.setAnalysisGroups(e2AnalysisGroups);
		e3.setAnalysisGroups(e3AnalysisGroups);
		a1.setExperiments(a1Experiments);
		a2.setExperiments(a2Experiments);
		a3.setExperiments(a3Experiments);
		a1.setTreatmentGroups(a1TreatmentGroups);
		a2.setTreatmentGroups(a2TreatmentGroups);
		a3.setTreatmentGroups(a3TreatmentGroups);
		t1.setAnalysisGroups(t1AnalysisGroups);
		t2.setAnalysisGroups(t2AnalysisGroups);
		t3.setAnalysisGroups(t3AnalysisGroups);
		t1.setSubjects(t1Subjects);
		t2.setSubjects(t2Subjects);
		t3.setSubjects(t3Subjects);
		s1.setTreatmentGroups(s1TreatmentGroups);
		s2.setTreatmentGroups(s2TreatmentGroups);
		s3.setTreatmentGroups(s3TreatmentGroups);
		
		//Then persist and flush everything to the database
		e1.persist();
		e1.flush();
		e2.persist();
		e2.flush();
		e3.persist();
		e3.flush();
		a1.persist();
		a1.flush();
		a2.persist();
		a2.flush();
		a3.persist();
		a3.flush();
		t1.persist();
		t1.flush();
		t2.persist();
		t2.flush();
		t3.persist();
		t3.flush();
		s1.persist();
		s1.flush();
		s2.persist();
		s2.flush();
		s3.persist();
		s3.flush();
		
		HashMap<String, Long> idMap = new HashMap<String, Long>();
		idMap.put("e1", e1.getId());
		idMap.put("e2", e2.getId());
		idMap.put("e3", e3.getId());
		idMap.put("a1", a1.getId());
		idMap.put("a2", a2.getId());
		idMap.put("a3", a3.getId());
		idMap.put("t1", t1.getId());
		idMap.put("t2", t2.getId());
		idMap.put("t3", t3.getId());
		idMap.put("s1", s1.getId());
		idMap.put("s2", s2.getId());
		idMap.put("s3", s3.getId());
		
		return idMap;
		
	}

	
	@Transactional
	@Test
	public void findTreatmentGroupsByAnalysisGroupsTest() {
		HashMap<String, Long> idMap = makeTestStack();
		Set<AnalysisGroup> analysisGroups = new HashSet<AnalysisGroup>();
		analysisGroups.add(AnalysisGroup.findAnalysisGroup(idMap.get("a2")));
		analysisGroups.add(AnalysisGroup.findAnalysisGroup(idMap.get("a1")));
		List<TreatmentGroup> checkList = TreatmentGroup.findTreatmentGroupsByAnalysisGroups(analysisGroups).getResultList();
		Assert.assertEquals(checkList.size(), 1);
		Assert.assertEquals(checkList.get(0).toJson(), TreatmentGroup.findTreatmentGroup(idMap.get("t1")).toJson());
		
//		TreatmentGroup treatmentgroup = makeTestingTreatmentGroup();
//		Set<AnalysisGroup> analysisgroups = new HashSet<AnalysisGroup>();
//		analysisgroups = treatmentgroup.getAnalysisGroups();
//		TreatmentGroup check = TreatmentGroup.findTreatmentGroupsByAnalysisGroups(analysisgroups).getSingleResult();
//		assert(treatmentgroup.toJson() == check.toJson());
	}
	
	@Transactional
	@Test
	public void findTreatmentGroupsByLsTransactionEqualsTest() {
		TreatmentGroup treatmentgroup = makeTestingTreatmentGroup();
		Long lstransaction = treatmentgroup.getLsTransaction();
		TreatmentGroup check = TreatmentGroup.findTreatmentGroupsByLsTransactionEquals(lstransaction).getSingleResult();
		Assert.assertEquals(treatmentgroup.toJson(), check.toJson());
	}
	
	@Transactional
	@Test
	public void deleteByExperimentIdTest() {
		TreatmentGroup treatmentgroup = makeTestingTreatmentGroup();
		Long id = treatmentgroup.getId();
		Long experimentid = treatmentgroup.getAnalysisGroups().iterator().next().getExperiments().iterator().next().getId();
		TreatmentGroup checkbefore = TreatmentGroup.findTreatmentGroup(id);
		Assert.assertEquals(checkbefore.toJson(), treatmentgroup.toJson());
		TreatmentGroup.deleteByExperimentID(experimentid);
		TreatmentGroup checkafter = TreatmentGroup.findTreatmentGroup(id);
		Assert.assertEquals(true, checkafter == null);
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
