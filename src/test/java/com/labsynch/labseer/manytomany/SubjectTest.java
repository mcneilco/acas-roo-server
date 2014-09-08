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
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.service.SubjectService;
import com.labsynch.labseer.service.TreatmentGroupService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable

public class SubjectTest {
	
	private static final Logger logger = LoggerFactory.getLogger(SubjectTest.class);

	@Autowired
	private SubjectService subjectService;
	
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
	
	private Subject makeTestingSubject() {
		TreatmentGroup treatmentgroup = makeTestingTreatmentGroup();
		Subject subject = new Subject();
		subject.setCodeName("SUBJ-12345678");
		subject.setIgnored(treatmentgroup.isIgnored());
		subject.setLsKind(treatmentgroup.getLsKind());
		subject.setLsType(treatmentgroup.getLsType());
		subject.setRecordedBy(treatmentgroup.getRecordedBy());
		subject.setLsTransaction(treatmentgroup.getLsTransaction());
		Set<TreatmentGroup> treatmentGroups = new HashSet<TreatmentGroup>();
		treatmentGroups.add(treatmentgroup);
		subject.setTreatmentGroups(treatmentGroups);
		subject.persist();
		subject.flush();
		return subject;
	}
	
	@Transactional
	//@Test
	public void updateTest() {
	}

	@Transactional
	//@Test
	public void fromJsonToSampleTest() {
		
	}
	
	@Transactional
	//@Test
	public void toJsonArrayTest() {
		
	}
	
	@Transactional
	//@Test
	public void fromJsonToSubjectTest() {
		
	}

	@Transactional
	//@Test
	public void fromJsonArrayToSubjectsStringTest() {
		
	}

	@Transactional
	//@Test
	public void fromJsonArrayToSubjectsReaderTest() {
		
	}
	
	@Transactional
	@Test
	public void findSubjectsByTreatmentGroupsTest() {
		Subject subject = makeTestingSubject();
		Set<TreatmentGroup> treatmentgroups = new HashSet<TreatmentGroup>();
		treatmentgroups = subject.getTreatmentGroups();
		Subject check = Subject.findSubjectsByTreatmentGroups(treatmentgroups).getSingleResult();
		assert(subject.toJson() == check.toJson());
	}
	
	@Transactional
	@Test
	public void findSubjectsByLsTransactionEqualsTest() {
		Subject subject = makeTestingSubject();
		Long lstransaction = subject.getLsTransaction();
		Subject check = Subject.findSubjectsByLsTransactionEquals(lstransaction).getSingleResult();
		assert(subject.toJson() == check.toJson());
	}
	
	@Transactional
	@Test
	public void findSubjectsByCodeNameEqualsTest() {
		Subject subject = makeTestingSubject();
		String codename = subject.getCodeName();
		Subject check = Subject.findSubjectsByCodeNameEquals(codename).getSingleResult();
		assert(subject.toJson() == check.toJson());
	}

	@Transactional
	@Test
	public void deleteByExperimentIDTest() {
		Subject subject = makeTestingSubject();
		Long id = subject.getId();
		Long experimentid = subject.getTreatmentGroups().iterator().next().getAnalysisGroups().iterator().next().getExperiments().iterator().next().getId();
		Subject checkbefore = Subject.findSubject(id);
		assert(checkbefore.toJson() == subject.toJson());
		Subject.deleteByExperimentID(experimentid);
		Subject checkafter = Subject.findSubject(id);
		assert(checkafter.toJson() != subject.toJson());
	}

}
