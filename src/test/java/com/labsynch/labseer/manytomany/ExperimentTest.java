package com.labsynch.labseer.manytomany;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import com.labsynch.labseer.exceptions.UniqueExperimentNameException;
import com.labsynch.labseer.service.AutoLabelService;
import com.labsynch.labseer.service.ExperimentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class ExperimentTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ExperimentTest.class);
	
	@Autowired
	private AutoLabelService autoLabelService;
	
	@Autowired
	private ExperimentService experimentService;

	@Transactional
	//@Test
	public void Experiment_Many_To_Many_Test() {
		Experiment theExperiment = Experiment.findExperiment(2165L);

		//logger.debug(theExperiment.toJson());
		
		int theSize = theExperiment.getAnalysisGroups().size();
		logger.debug(String.valueOf(theSize));
		System.out.println("" + theSize);
		Assert.assertEquals(7, theSize);
	}
	
	@Transactional
	//@Test
	public void CreateFullExperiment_Test() throws UniqueExperimentNameException {
		
		Protocol protocol = Protocol.findProtocolEntries(0, 1).get(0);
		Experiment experiment = new Experiment();
		experiment.setProtocol(protocol);
		String experimentCodeName = autoLabelService.getExperimentCodeName();
		logger.debug("experiment CodeName is: " + experimentCodeName);
		experiment.setCodeName(experimentCodeName);
		experiment.setRecordedBy("tester");
		experiment.setRecordedDate(new Date());
		experiment.setLsType("default");
		experiment.setLsKind("default");
		experiment.persist();

		AnalysisGroup ag = new AnalysisGroup();
		String analysisGroupCodeName = autoLabelService.getAnalysisGroupCodeName();
		logger.debug("analysis Group CodeName is: " + analysisGroupCodeName);
		ag.setCodeName(analysisGroupCodeName);
		Set<Experiment> experiments = new HashSet<Experiment>();
		experiments.add(Experiment.findExperiment(experiment.getId()));
		ag.setExperiments(experiments);
		ag.setLsType("data");
		ag.setLsKind("results");
		ag.setRecordedBy("tester");
		ag.setRecordedDate(new Date());
		ag.persist();
		
		TreatmentGroup tg = new TreatmentGroup();
		String treatmentGroupCodeName = autoLabelService.getTreatmentGroupCodeName();
		logger.debug("TreatmentGroup CodeName is: " + treatmentGroupCodeName);
		tg.setCodeName(treatmentGroupCodeName);
		Set<AnalysisGroup> analysisGroups = new HashSet<AnalysisGroup>();
		analysisGroups.add(AnalysisGroup.findAnalysisGroup(ag.getId()));
		tg.setAnalysisGroups(analysisGroups);
		tg.getAnalysisGroups().add(ag);
		tg.setLsType("default");
		tg.setLsKind("default");
		tg.setRecordedBy("tester");
		tg.setRecordedDate(new Date());
		tg.persist();
		
		Subject subj = new Subject();
		String subjectCodeName = autoLabelService.getSubjectCodeName();
		logger.debug("Subject CodeName is: " + subjectCodeName);
		subj.setCodeName(subjectCodeName);
		Set<TreatmentGroup> treatmentGroups = new HashSet<TreatmentGroup>();
		treatmentGroups.add(TreatmentGroup.findTreatmentGroup(tg.getId()));
		subj.setTreatmentGroups(treatmentGroups);
		subj.getTreatmentGroups().add(tg);
		subj.setLsType("default");
		subj.setLsKind("default");
		subj.setRecordedBy("tester");
		subj.setRecordedDate(new Date());
		subj.persist();
		
		logger.debug("experiment json: " + Experiment.findExperiment(experiment.getId()).toPrettyJson());
		logger.debug("AnalysisGroup json: " + AnalysisGroup.findAnalysisGroup(ag.getId()).toPrettyFullJson());
		logger.debug("TreatmentGroup json: " + TreatmentGroup.findTreatmentGroup(tg.getId()).toJson());
		logger.debug("Subject json: " + Subject.findSubject(subj.getId()).toPrettyJson());

		Subject retSubj = Subject.findSubject(subj.getId());
		TreatmentGroup retTg = TreatmentGroup.findTreatmentGroup(tg.getId());
		retTg.getSubjects().add(retSubj);
		AnalysisGroup retAg = AnalysisGroup.findAnalysisGroup(ag.getId());
		retAg.getTreatmentGroups().add(retTg);
		Experiment retExp = Experiment.findExperiment(experiment.getId());
		retExp.getAnalysisGroups().add(retAg);
		logger.debug("full ret experiment: " + retExp.toJson());
		
//		String json = "{\"analysisGroups\":[{\"id\":null,\"ignored\":false,\"lsKind\":\"results\",\"lsLabels\":[],\"lsStates\":[],\"lsType\":\"data\",\"lsTypeAndKind\":\"data_results\",\"recordedBy\":\"tester\",\"recordedDate\":1403419760648,\"treatmentGroups\":[{\"id\":null,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsStates\":[],\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"tester\",\"recordedDate\":1403419760694,\"subjects\":[{\"id\":null,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsStates\":[],\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"tester\",\"recordedDate\":1403419760738,\"version\":0}],\"version\":0}],\"version\":0}],\"codeName\":null,\"id\":null,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsStates\":[],\"lsTags\":[],\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"protocol\":{\"codeName\":\"PROT-00000002\",\"id\":1,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"smeyer\",\"recordedDate\":1402943976000,\"shortDescription\":\"confirmation screen\",\"version\":1},\"recordedBy\":\"tester\",\"recordedDate\":1403419760597,\"version\":0}";
//		Experiment output = experimentService.saveLsExperiment(Experiment.fromJsonToExperiment(json));
//		logger.debug("created experiment from json: " + output.toJson());
		
	}

	@Transactional
	//@Test
	public void CreateFullExperimentFromJson1_Test() throws UniqueExperimentNameException {
		
		String json = "{\"analysisGroups\":[{\"id\":null,\"ignored\":false,\"lsKind\":\"results\",\"lsLabels\":[],\"lsStates\":[],\"lsType\":\"data\",\"lsTypeAndKind\":\"data_results\",\"recordedBy\":\"tester\",\"recordedDate\":1403419760648,\"treatmentGroups\":[{\"id\":null,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsStates\":[],\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"tester\",\"recordedDate\":1403419760694,\"subjects\":[{\"id\":null,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsStates\":[],\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"tester\",\"recordedDate\":1403419760738,\"version\":0}],\"version\":0}],\"version\":0}],\"codeName\":null,\"id\":null,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsStates\":[],\"lsTags\":[],\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"protocol\":{\"codeName\":\"PROT-00000002\",\"id\":1,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"smeyer\",\"recordedDate\":1402943976000,\"shortDescription\":\"confirmation screen\",\"version\":1},\"recordedBy\":\"tester\",\"recordedDate\":1403419760597,\"version\":0}";
		Experiment output = experimentService.saveLsExperiment(Experiment.fromJsonToExperiment(json));
		logger.debug("created experiment from just json: " + output.toJson());

	}
	
	@Transactional
	//@Test
	public void GetFullExperimentJson1_Test() {
		
		Experiment experiment = Experiment.findExperiment(242L);
		logger.debug("Get full experiment json: " + experiment.toJson());
		logger.debug("Get full experiment pretty json: " + experiment.toPrettyJson());
		String testJson = "{\"analysisGroups\":[{\"codeName\":\"AG-00000030\",\"id\":243,\"ignored\":false,\"lsKind\":\"results\",\"lsLabels\":[],\"lsStates\":[],\"lsType\":\"data\",\"lsTypeAndKind\":\"data_results\",\"recordedBy\":\"tester\",\"recordedDate\":1403419760648,\"treatmentGroups\":[{\"id\":244,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsStates\":[],\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"tester\",\"recordedDate\":1403419760694,\"subjects\":[{\"id\":245,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsStates\":[],\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"tester\",\"recordedDate\":1403419760738,\"version\":0}],\"version\":0}],\"version\":0}],\"codeName\":\"EXPT-00000017\",\"id\":242,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsStates\":[],\"lsTags\":[],\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"protocol\":{\"codeName\":\"PROT-00000002\",\"id\":1,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"smeyer\",\"recordedDate\":1402943976000,\"shortDescription\":\"confirmation screen\",\"version\":1},\"recordedBy\":\"tester\",\"recordedDate\":1403419760597,\"version\":1}";
		Experiment testExperiment = Experiment.fromJsonToExperiment(testJson);
		//Assert.assertEquals(testExperiment.toJson(), experiment.toJson());
		Assert.assertEquals(Experiment.findExperiment(testExperiment.getId()), experiment);

	}
	
	//This method searches by Experiment Code Name
	@Transactional
	@Test
	public void findExperimentByNameTest () {
		String experimentCodeName = "EXPT-00000002";
		Collection<Experiment> experiments = Experiment.findExperimentByName(experimentCodeName);
		assert(!experiments.isEmpty());
	}
	
	//This method searches by Experiment Label Text
	@Transactional
	@Test
	public void findExperimentByExperimentNameTest () {
		String experimentLabelText = "Test Load 102";
		List<Experiment> experiments = Experiment.findExperimentByExperimentName(experimentLabelText);
		assert(experiments.get(0).getId() == 1007);
	}
	
	@Transactional
	@Test
	public void findExperimentByExperimentNameAndProtocolIdTest () {
		String experimentName = "Test Load 102";
		Long protocolId = 1006L;
		List<Experiment> experiments = Experiment.findExperimentByExperimentNameAndProtocolId(experimentName, protocolId);
		assert(experiments.get(0).getId() == 1007);
	}
	
	//Experiment.getProtocol is broken
	@Transactional
	@Test
	public void findExperimentByNameAndProtocolIdTest () {
		String experimentCodeName = "EXPT-00000002";
		Long protocolId = 1006L;
		Collection<Experiment> experiments = Experiment.findExperimentByNameAndProtocolId(experimentCodeName, protocolId);
		Experiment[] arrayOfExperiments = (Experiment[]) experiments.toArray();
		Experiment experiment = arrayOfExperiments[0];
		assert(!experiments.isEmpty());
	}
	
	//Need to locate appropriate JSON string for new model
	@Transactional
	//@Test
	public void fromJsonToExperimentTest (String json) {
		
	}
	
	//Delete SQL query is now invalid
	@Transactional
	@Test
	public void deleteExperimentTest () {
		List<Experiment> experiment = Experiment.findExperimentByExperimentName("Test Load 102");
		assert(experiment.size() == 1);
		Experiment.deleteExperiment(experiment.get(0));
		experiment = Experiment.findExperimentByExperimentName("Test Load 102");
		assert(experiment.size() == 0);
	}
	
	@Transactional
	@Test
	public void findExperimentsByCodeNameEqualsTest() {
		List<Experiment> experiment = Experiment.findExperimentsByCodeNameEquals("EXPT-00000002").getResultList();
		assert(experiment.size() == 1);
	}
	
	@Transactional
	@Test
	public void findExperimentsByLsTransaction() {
		Long lsTransaction = 5L;
		List<Experiment> experiment = Experiment.findExperimentsByLsTransaction(lsTransaction).getResultList();
		assert(experiment.size() == 1);
	}
	
	@Transactional
	@Test
	public void findExperimentsByProtocol()  {
		Long protocolId = 1006L;
		Protocol protocol = Protocol.findProtocol(protocolId);
		List<Experiment> experiments = Experiment.findExperimentsByProtocol(protocol).getResultList();
		assert(experiments.size() == 23);
	}
	
	@Transactional
	@Test
	public void findExperimentsByProtocolTypeAndKindAndExperimentTypeAndKindTest() {
		String protocolType = "default";
		String protocolKind = "default";
		String experimentType = "default";
		String experimentKind = "default";
		List<Experiment> experiments = Experiment.findExperimentsByProtocolTypeAndKindAndExperimentTypeAndKind(protocolType, protocolKind, experimentType, experimentKind).getResultList();
		assert(experiments.size() == 70);
	}
}
