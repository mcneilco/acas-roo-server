package com.labsynch.labseer.manytomany;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.exceptions.NotFoundException;
import com.labsynch.labseer.exceptions.UniqueNameException;
import com.labsynch.labseer.service.ExperimentService;

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
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml" })
@Configurable
public class ExperimentTest {

	private static final Logger logger = LoggerFactory.getLogger(ExperimentTest.class);

	@Autowired
	private ExperimentService experimentService;

	@Transactional
	// @Test
	public void Experiment_Many_To_Many_Test() {
		Experiment theExperiment = Experiment.findExperiment(2165L);

		// logger.debug(theExperiment.toJson());

		int theSize = theExperiment.getAnalysisGroups().size();
		logger.debug(String.valueOf(theSize));
		System.out.println("" + theSize);
		Assert.assertEquals(7, theSize);
	}

	public HashMap<String, Long> makeTestStack() {
		// each of these methods creates the full stack from protocol to subject.
		Date now = new Date();
		// create 1 Protocol
		Protocol protocol = new Protocol();
		protocol.setRecordedBy("test user");
		protocol.setRecordedDate(now);
		protocol.setLsKind("default");
		protocol.setLsType("default");
		protocol.setLsTransaction(98765L);
		protocol.persist();

		// create 3 Experiments
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
		for (Experiment experiment : allExperiments) {
			experiment.setIgnored(false);
			experiment.setLsKind(protocol.getLsKind());
			experiment.setLsType(protocol.getLsType());
			experiment.setRecordedBy(protocol.getRecordedBy());
			experiment.setRecordedDate(protocol.getRecordedDate());
			experiment.setLsTransaction(protocol.getLsTransaction());
			experiment.setProtocol(protocol);
		}

		// create 3 AnalysisGroups
		AnalysisGroup a1 = new AnalysisGroup();
		a1.setCodeName("T-AG-11111111");
		AnalysisGroup a2 = new AnalysisGroup();
		a2.setCodeName("T-AG-22222222");
		AnalysisGroup a3 = new AnalysisGroup();
		a3.setCodeName("T-AG-33333333");
		Set<AnalysisGroup> allAnalysisGroups = new HashSet<AnalysisGroup>();
		allAnalysisGroups.add(a1);
		allAnalysisGroups.add(a2);
		allAnalysisGroups.add(a3);
		for (AnalysisGroup analysisGroup : allAnalysisGroups) {
			analysisGroup.setIgnored(e1.isIgnored());
			analysisGroup.setLsKind(e1.getLsKind());
			analysisGroup.setLsType(e1.getLsType());
			analysisGroup.setRecordedBy(e1.getRecordedBy());
			analysisGroup.setRecordedDate(e1.getRecordedDate());
			analysisGroup.setLsTransaction(e1.getLsTransaction());
		}

		// create 3 TreatmentGroups
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
		for (TreatmentGroup treatmentGroup : allTreatmentGroups) {
			treatmentGroup.setIgnored(a1.isIgnored());
			treatmentGroup.setLsKind(a1.getLsKind());
			treatmentGroup.setLsType(a1.getLsType());
			treatmentGroup.setRecordedBy(a1.getRecordedBy());
			treatmentGroup.setRecordedDate(a1.getRecordedDate());
			treatmentGroup.setLsTransaction(a1.getLsTransaction());
		}

		// create 3 Subjects
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
		for (Subject subject : allSubjects) {
			subject.setIgnored(t1.isIgnored());
			subject.setLsKind(t1.getLsKind());
			subject.setLsType(t1.getLsType());
			subject.setRecordedBy(t1.getRecordedBy());
			subject.setRecordedDate(t1.getRecordedDate());
			subject.setLsTransaction(t1.getLsTransaction());
		}

		// Create the various Sets for mapping
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

		// Then assign all the groups to their owners to create the mappings
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

		// Then persist and flush everything to the database
		e1.persist();
		// e2.persist();
		// e3.persist();
		// a1.persist();
		// a2.persist();
		// a3.persist();
		// t1.persist();
		// t2.persist();
		// t3.persist();
		// s1.persist();
		// s2.persist();
		// s3.persist();

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

	// @Transactional
	// //@Test
	// public void CreateFullExperiment_Test() throws UniqueExperimentNameException
	// {
	//
	// Protocol protocol = Protocol.findProtocolEntries(0, 1).get(0);
	// Experiment experiment = new Experiment();
	// experiment.setProtocol(protocol);
	// String experimentCodeName = autoLabelService.getExperimentCodeName();
	// logger.debug("experiment CodeName is: " + experimentCodeName);
	// experiment.setCodeName(experimentCodeName);
	// experiment.setRecordedBy("tester");
	// experiment.setRecordedDate(new Date());
	// experiment.setLsType("default");
	// experiment.setLsKind("default");
	// experiment.persist();
	//
	// AnalysisGroup ag = new AnalysisGroup();
	// String analysisGroupCodeName = autoLabelService.getAnalysisGroupCodeName();
	// logger.debug("analysis Group CodeName is: " + analysisGroupCodeName);
	// ag.setCodeName(analysisGroupCodeName);
	// Set<Experiment> experiments = new HashSet<Experiment>();
	// experiments.add(Experiment.findExperiment(experiment.getId()));
	// ag.setExperiments(experiments);
	// ag.setLsType("data");
	// ag.setLsKind("results");
	// ag.setRecordedBy("tester");
	// ag.setRecordedDate(new Date());
	// ag.persist();
	//
	// TreatmentGroup tg = new TreatmentGroup();
	// String treatmentGroupCodeName = autoLabelService.getTreatmentGroupCodeName();
	// logger.debug("TreatmentGroup CodeName is: " + treatmentGroupCodeName);
	// tg.setCodeName(treatmentGroupCodeName);
	// Set<AnalysisGroup> analysisGroups = new HashSet<AnalysisGroup>();
	// analysisGroups.add(AnalysisGroup.findAnalysisGroup(ag.getId()));
	// tg.setAnalysisGroups(analysisGroups);
	// tg.getAnalysisGroups().add(ag);
	// tg.setLsType("default");
	// tg.setLsKind("default");
	// tg.setRecordedBy("tester");
	// tg.setRecordedDate(new Date());
	// tg.persist();
	//
	// Subject subj = new Subject();
	// String subjectCodeName = autoLabelService.getSubjectCodeName();
	// logger.debug("Subject CodeName is: " + subjectCodeName);
	// subj.setCodeName(subjectCodeName);
	// Set<TreatmentGroup> treatmentGroups = new HashSet<TreatmentGroup>();
	// treatmentGroups.add(TreatmentGroup.findTreatmentGroup(tg.getId()));
	// subj.setTreatmentGroups(treatmentGroups);
	// subj.getTreatmentGroups().add(tg);
	// subj.setLsType("default");
	// subj.setLsKind("default");
	// subj.setRecordedBy("tester");
	// subj.setRecordedDate(new Date());
	// subj.persist();
	//
	// logger.debug("experiment json: " +
	// Experiment.findExperiment(experiment.getId()).toPrettyJson());
	// logger.debug("AnalysisGroup json: " +
	// AnalysisGroup.findAnalysisGroup(ag.getId()).toPrettyFullJson());
	// logger.debug("TreatmentGroup json: " +
	// TreatmentGroup.findTreatmentGroup(tg.getId()).toJson());
	// logger.debug("Subject json: " +
	// Subject.findSubject(subj.getId()).toPrettyJson());
	//
	// Subject retSubj = Subject.findSubject(subj.getId());
	// TreatmentGroup retTg = TreatmentGroup.findTreatmentGroup(tg.getId());
	// retTg.getSubjects().add(retSubj);
	// AnalysisGroup retAg = AnalysisGroup.findAnalysisGroup(ag.getId());
	// retAg.getTreatmentGroups().add(retTg);
	// Experiment retExp = Experiment.findExperiment(experiment.getId());
	// retExp.getAnalysisGroups().add(retAg);
	// logger.debug("full ret experiment: " + retExp.toJson());
	//
	//// String json =
	// "{\"analysisGroups\":[{\"id\":null,\"ignored\":false,\"lsKind\":\"results\",\"lsLabels\":[],\"lsStates\":[],\"lsType\":\"data\",\"lsTypeAndKind\":\"data_results\",\"recordedBy\":\"tester\",\"recordedDate\":1403419760648,\"treatmentGroups\":[{\"id\":null,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsStates\":[],\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"tester\",\"recordedDate\":1403419760694,\"subjects\":[{\"id\":null,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsStates\":[],\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"tester\",\"recordedDate\":1403419760738,\"version\":0}],\"version\":0}],\"version\":0}],\"codeName\":null,\"id\":null,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsStates\":[],\"lsTags\":[],\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"protocol\":{\"codeName\":\"PROT-00000002\",\"id\":1,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"smeyer\",\"recordedDate\":1402943976000,\"shortDescription\":\"confirmation
	// screen\",\"version\":1},\"recordedBy\":\"tester\",\"recordedDate\":1403419760597,\"version\":0}";
	//// Experiment output =
	// experimentService.saveLsExperiment(Experiment.fromJsonToExperiment(json));
	//// logger.debug("created experiment from json: " + output.toJson());
	//
	// }

	@Transactional
	// @Test
	public void CreateFullExperimentFromJson1_Test() throws UniqueNameException, NotFoundException {

		String json = "{\"analysisGroups\":[{\"id\":null,\"ignored\":false,\"lsKind\":\"results\",\"lsLabels\":[],\"lsStates\":[],\"lsType\":\"data\",\"lsTypeAndKind\":\"data_results\",\"recordedBy\":\"tester\",\"recordedDate\":1403419760648,\"treatmentGroups\":[{\"id\":null,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsStates\":[],\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"tester\",\"recordedDate\":1403419760694,\"subjects\":[{\"id\":null,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsStates\":[],\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"tester\",\"recordedDate\":1403419760738,\"version\":0}],\"version\":0}],\"version\":0}],\"codeName\":null,\"id\":null,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsStates\":[],\"lsTags\":[],\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"protocol\":{\"codeName\":\"PROT-00000002\",\"id\":1,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"smeyer\",\"recordedDate\":1402943976000,\"shortDescription\":\"confirmation screen\",\"version\":1},\"recordedBy\":\"tester\",\"recordedDate\":1403419760597,\"version\":0}";
		Experiment output = experimentService.saveLsExperiment(Experiment.fromJsonToExperiment(json));
		logger.debug("created experiment from just json: " + output.toJson());

	}

	@Transactional
	// @Test
	public void GetFullExperimentJson1_Test() {

		Experiment experiment = Experiment.findExperiment(242L);
		logger.debug("Get full experiment json: " + experiment.toJson());
		logger.debug("Get full experiment pretty json: " + experiment.toPrettyJson());
		String testJson = "{\"analysisGroups\":[{\"codeName\":\"AG-00000030\",\"id\":243,\"ignored\":false,\"lsKind\":\"results\",\"lsLabels\":[],\"lsStates\":[],\"lsType\":\"data\",\"lsTypeAndKind\":\"data_results\",\"recordedBy\":\"tester\",\"recordedDate\":1403419760648,\"treatmentGroups\":[{\"id\":244,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsStates\":[],\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"tester\",\"recordedDate\":1403419760694,\"subjects\":[{\"id\":245,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsStates\":[],\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"tester\",\"recordedDate\":1403419760738,\"version\":0}],\"version\":0}],\"version\":0}],\"codeName\":\"EXPT-00000017\",\"id\":242,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsStates\":[],\"lsTags\":[],\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"protocol\":{\"codeName\":\"PROT-00000002\",\"id\":1,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"smeyer\",\"recordedDate\":1402943976000,\"shortDescription\":\"confirmation screen\",\"version\":1},\"recordedBy\":\"tester\",\"recordedDate\":1403419760597,\"version\":1}";
		Experiment testExperiment = Experiment.fromJsonToExperiment(testJson);
		// Assert.assertEquals(testExperiment.toJson(), experiment.toJson());
		Assert.assertEquals(Experiment.findExperiment(testExperiment.getId()), experiment);

	}

	// This method searches by Experiment Code Name
	@Transactional
	@Test
	public void findExperimentByNameTest() {
		String experimentCodeName = "EXPT-00000002";
		Collection<Experiment> experiments = Experiment.findExperimentByName(experimentCodeName);
		assert (!experiments.isEmpty());
	}

	// This method searches by Experiment Label Text
	@Transactional
	@Test
	public void findExperimentByExperimentNameTest() {
		String experimentLabelText = "Test Load 102";
		List<Experiment> experiments = Experiment.findExperimentByExperimentName(experimentLabelText);
		assert (experiments.get(0).getId() == 1007);
	}

	@Transactional
	@Test
	public void findExperimentByExperimentNameAndProtocolIdTest() {
		String experimentName = "Test Load 102";
		Long protocolId = 1006L;
		List<Experiment> experiments = Experiment.findExperimentByExperimentNameAndProtocolId(experimentName,
				protocolId);
		assert (experiments.get(0).getId() == 1007);
	}

	@Transactional
	@Test
	public void findExperimentByExperimentNameAndProtocolIdAndIgnoredNotTest() {
		String experimentName = "Test Load 102";
		Long protocolId = 1006L;
		List<Experiment> experiments = Experiment
				.findExperimentListByExperimentNameAndProtocolIdAndIgnoredNot(experimentName, protocolId);
		assert (experiments.get(0).getId() == 1007);
	}

	@Transactional
	@Test
	public void findExperimentByNameAndProtocolIdTest() {
		String experimentLabelText = "Test Load 102";
		Long protocolId = 1006L;
		Collection<Experiment> experiments = Experiment.findExperimentByNameAndProtocolId(experimentLabelText,
				protocolId);
		assert (!experiments.isEmpty());
	}

	// Need to locate appropriate JSON string for new model
	@Transactional
	// @Test
	public void fromJsonToExperimentTest(String json) {

	}

	@Transactional
	@Test
	public void removeExperimentTest() {
		HashMap<String, Long> idMap = makeTestStack();
		Long e1Id = idMap.get("e1");
		// Long e2Id = idMap.get("e2");
		Experiment e1 = Experiment.findExperiment(e1Id);
		// Experiment e2 = Experiment.findExperiment(e2Id);
		Experiment.removeExperimentCascadeAware(e1Id);
		// e2.remove();
		Assert.assertNull(Experiment.findExperiment(e1Id));
		// Assert.assertNull(Experiment.findExperiment(e2Id));
	}

	@Transactional
	// @Test
	public void removeExperimentPostTest() {
		logger.debug("AG 1 experiments are: " + AnalysisGroup.findAnalysisGroup(626299L).getExperiments());
	}

	// Delete SQL query is now invalid
	@Transactional
	// @Test
	public void deleteExperimentTest() {
		List<Experiment> experiment = Experiment.findExperimentByExperimentName("Test Load 102");
		assert (experiment.size() == 1);
		Experiment.deleteExperiment(experiment.get(0));
		experiment = Experiment.findExperimentByExperimentName("Test Load 102");
		assert (experiment.size() == 0);
	}

	@Transactional
	@Test
	public void findExperimentsByCodeNameEqualsTest() {
		List<Experiment> experiment = Experiment.findExperimentsByCodeNameEquals("EXPT-00000002").getResultList();
		assert (experiment.size() == 1);
	}

	@Transactional
	@Test
	public void findExperimentsByLsTransaction() {
		Long lsTransaction = 5L;
		List<Experiment> experiment = Experiment.findExperimentsByLsTransaction(lsTransaction).getResultList();
		assert (experiment.size() == 1);
	}

	@Transactional
	@Test
	public void findExperimentsByProtocol() {
		Long protocolId = 1006L;
		Protocol protocol = Protocol.findProtocol(protocolId);
		List<Experiment> experiments = Experiment.findExperimentsByProtocol(protocol).getResultList();
		assert (experiments.size() == 23);
	}

	@Transactional
	@Test
	public void findExperimentsByProtocolTypeAndKindAndExperimentTypeAndKindTest() {
		String protocolType = "default";
		String protocolKind = "default";
		String experimentType = "default";
		String experimentKind = "default";
		List<Experiment> experiments = Experiment.findExperimentsByProtocolTypeAndKindAndExperimentTypeAndKind(
				protocolType, protocolKind, experimentType, experimentKind).getResultList();
		assert (experiments.size() == 70);
	}
}
