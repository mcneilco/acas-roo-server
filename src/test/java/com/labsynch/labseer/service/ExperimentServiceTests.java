package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
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
import com.labsynch.labseer.domain.AnalysisGroupLabel;
import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentLabel;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.domain.ItxSubjectContainer;
import com.labsynch.labseer.domain.ItxSubjectContainerState;
import com.labsynch.labseer.domain.ItxSubjectContainerValue;
import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectLabel;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.domain.TreatmentGroupLabel;
import com.labsynch.labseer.domain.TreatmentGroupState;
import com.labsynch.labseer.domain.TreatmentGroupValue;
import com.labsynch.labseer.exceptions.UniqueExperimentNameException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml" })
@Configurable
public class ExperimentServiceTests {

	private static final Logger logger = LoggerFactory
			.getLogger(ExperimentServiceTests.class);

	@Autowired
	private ExperimentService experimentService;

	// @Test
	@Transactional
	public void CreateProtocolTest_1() {
		Protocol protocol = new Protocol();
		protocol.setLsType("test protocol type");
		protocol.setLsKind("test protocol kind");
		protocol.setCodeName("test experiment_" + Protocol.countProtocols() + 1);
		protocol.setShortDescription("just a test");
		protocol.setRecordedBy("testUser");
		protocol.setRecordedDate(new Date());
		LsTransaction tx = new LsTransaction();
		tx.setRecordedDate(new Date());
		tx.persist();
		protocol.setLsTransaction(tx.getId());
		protocol.persist();
		logger.info(protocol.toJson());
		long id = protocol.getId();
		Assert.assertNotNull(
				"Find method for 'Protocol' illegally returned null for id '"
						+ id + "'", id);
	}

	// @Test
	// @Transactional
	public void CreateExperimentTest_2() {
		String userName = "testUser";
		Date recordedDate = new Date();

		LsTransaction lsTransaction = new LsTransaction();
		lsTransaction.setRecordedDate(recordedDate);
		lsTransaction.persist();

		Protocol protocol = new Protocol();
		protocol.setLsTransaction(lsTransaction.getId());
		protocol.setRecordedBy(userName);
		protocol.setRecordedDate(recordedDate);
		protocol.setCodeName("test protocol code name 105");
		protocol.setShortDescription("just a test");
		protocol.setLsType("default");
		protocol.setLsKind("default");
		protocol.persist();

		Experiment experiment = new Experiment();
		experiment.setProtocol(protocol);
		experiment.setLsTransaction(lsTransaction.getId());
		experiment.setRecordedBy(userName);
		experiment.setRecordedDate(recordedDate);
		experiment.setCodeName("test experiment code name 105");
		experiment.setShortDescription("some short description");
		experiment.setLsKind("default");
		experiment.setLsType("default");
		experiment.persist();

		logger.info("experiment saved: " + experiment.toPrettyJson());

		ExperimentLabel experimentLabel = new ExperimentLabel();
		experimentLabel.setExperiment(experiment);
		experimentLabel.setLabelText("Expt Test 105");
		experimentLabel.setRecordedBy(userName);
		experimentLabel.setRecordedDate(recordedDate);
		experimentLabel.setLsTransaction(lsTransaction.getId());
		experimentLabel.setLsType("name");
		experimentLabel.setLsKind("experiment name");
		experimentLabel.persist();

		AnalysisGroup ag = new AnalysisGroup();
		Set<Experiment> experiments = new HashSet<Experiment>();
		experiments.add(experiment);
		ag.setExperiments(experiments);
		ag.setRecordedBy(userName);
		ag.setRecordedDate(recordedDate);
		ag.setLsTransaction(lsTransaction.getId());
		ag.setLsType("default");
		ag.setLsKind("default");
		ag.persist();

		AnalysisGroupState ags = new AnalysisGroupState();
		ags.setAnalysisGroup(ag);
		ags.setRecordedBy(userName);
		ags.setRecordedDate(recordedDate);
		ags.setLsTransaction(lsTransaction.getId());
		ags.setLsType("data");
		ags.setLsKind("results");
		ags.persist();

		AnalysisGroupValue agv = new AnalysisGroupValue();
		agv.setLsState(ags);
		agv.setRecordedBy(userName);
		agv.setRecordedDate(recordedDate);
		agv.setLsTransaction(lsTransaction.getId());
		agv.setLsType("stringValue");
		agv.setLsKind("status");
		agv.persist();

		TreatmentGroup tg = new TreatmentGroup();
		Set<AnalysisGroup> analysisGroups = new HashSet<AnalysisGroup>();
		analysisGroups.add(ag);
		tg.setAnalysisGroups(analysisGroups);
		tg.setRecordedBy(userName);
		tg.setRecordedDate(recordedDate);
		tg.setLsTransaction(lsTransaction.getId());
		tg.setLsType("default");
		tg.setLsKind("default");
		tg.persist();

		TreatmentGroupState tgs = new TreatmentGroupState();
		tgs.setTreatmentGroup(tg);
		tgs.setRecordedBy(userName);
		tgs.setRecordedDate(recordedDate);
		tgs.setLsTransaction(lsTransaction.getId());
		tgs.setLsType("data");
		tgs.setLsKind("results");
		tgs.persist();

		TreatmentGroupValue tgv = new TreatmentGroupValue();
		tgv.setLsState(tgs);
		tgv.setRecordedBy("daffy duck");
		tgv.setRecordedDate(recordedDate);
		tgv.setLsTransaction(lsTransaction.getId());
		tgv.setLsType("stringValue");
		tgv.setLsKind("status");
		tgv.persist();

		// TreatmentGroupValue findTgv =
		// TreatmentGroupValue.findTreatmentGroupValue(tgv.getId());
		// logger.info("found the tgv: " + findTgv.getRecordedBy());

		// logger.info(experiment.toJson());
		// logger.info(Experiment.findExperiment(experiment.getId()).toJson());

	}

	// @Test
	public void UpdateExeprimentName_2() {
		String json = "{    \"analysisGroups\": null,    \"codeName\": \"test experiment code name 105\",    \"id\": 3101,    \"ignored\": false,    \"lsKind\": \"default\",    \"lsLabels\": null,    \"lsStates\": null,    \"lsTransaction\": 311,    \"lsType\": \"default\",    \"lsTypeAndKind\": \"default_default\",    \"modifiedBy\": null,    \"modifiedDate\": null,    \"protocol\": {        \"codeName\": \"test protocol code name 105\",        \"id\": 3100,        \"ignored\": false,        \"lsKind\": \"default\",        \"lsTransaction\": 311,        \"lsType\": \"default\",        \"lsTypeAndKind\": \"default_default\",        \"modifiedBy\": null,        \"modifiedDate\": null,        \"recordedBy\": \"testUser\",        \"recordedDate\": 1379479721768,        \"shortDescription\": \"just a test\",        \"version\": 0    },    \"recordedBy\": \"testUser\",    \"recordedDate\": 1379479721768,    \"shortDescription\": \"some short description\",    \"version\": 0}";
		experimentService.updateExperiment(Experiment
				.fromJsonToExperiment(json));
	}

	// @Test
	// @Transactional
	public void CreateProtocolFromSimpleJson_3()
			throws UniqueExperimentNameException {
		String json = "{\"protocol\":{\"codeName\":\"PROT-00000007\",\"id\":14,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[{\"id\":9,\"ignored\":false,\"imageFile\":null,\"labelText\":\"test\",\"lsKind\":\"protocol name\",\"lsTransaction\":10,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_protocol name\",\"modifiedDate\":null,\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"smeyer\",\"recordedDate\":1371828671000,\"version\":0}],\"lsStates\":[],\"lsTransaction\":10,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1371828794000,\"shortDescription\":\"protocol created by generic data parser\",\"version\":1},\"codeName\":\"EXPT-00000004\",\"lsType\":\"default\",\"lsKind\":\"default\",\"shortDescription\":\"experiment created by generic data parser\",\"recordedBy\":\"smeyer\",\"lsTransaction\":10,\"lsLabels\":[],\"lsStates\":[]}";
		Experiment experiment = experimentService.saveLsExperiment(Experiment
				.fromJsonToExperiment(json));
		logger.info(experiment.toJson());

	}

	// @Test
	// @Transactional
	public void CreateProtocolFromNestedJson()
			throws UniqueExperimentNameException {
		// String json =
		// "{ \"name\": \"\",\"shortDescription\": \"\",\"lsTransaction\": null,\"protocolStates\": [ { \"protocolValues\": [ { \"valueType\": \"stringValue\",\"valueKind\": \"reader instrument\",\"stringValue\": \"Molecular Dynamics FLIPR\",\"fileValue\": null,\"urlValue\": null,\"dateValue\": null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\": null,\"sigFigs\": null,\"uncertainty\": null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"lsTransaction\": null,\"thingIdValue\": null,\"sampleId\": null,\"sampleName\": \"\",\"publicData\": false,\"recordedDate\":    1353216427000 },{ \"valueType\": \"numericValue\",\"valueKind\": \"curve min\",\"stringValue\": null,\"fileValue\": null,\"urlValue\": null,\"dateValue\": null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\":                0,\"sigFigs\":                2,\"uncertainty\": null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"lsTransaction\": null,\"thingIdValue\": null,\"sampleId\": null,\"sampleName\": \"\",\"publicData\": false,\"recordedDate\":    1353216427000 },{ \"valueType\": \"numericValue\",\"valueKind\": \"curve max\",\"stringValue\": null,\"fileValue\": null,\"urlValue\": null,\"dateValue\": null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\":              100,\"sigFigs\":                2,\"uncertainty\": null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"lsTransaction\": null,\"thingIdValue\": null,\"sampleId\": null,\"sampleName\": \"\",\"publicData\": false,\"recordedDate\":    1353216427000 } ],\"recordedBy\": \"userName\",\"stateType\": \"metadata\",\"stateKind\": \"protocol parameters\",\"comments\": \"\",\"lsTransaction\": null,\"ignored\": false,\"recordedDate\":    1353216427000 } ] }";
		String json = "{\"kind\":\"primary analysis\",\"recordedBy\":\"jmcneil\",\"recordedDate\":1363503600000,\"shortDescription\":\"primary 7:34\",\"description\":\"\", \"experimentLabels\":[{\"labelType\":\"name\",\"labelKind\":\"experiment name\",\"labelText\":\"john 7:34\",\"ignored\":false,\"preferred\":true,\"recordedDate\":1363503600000,\"recordedBy\":\"jmcneil\",\"physicallyLabled\":false,\"imageFile\":null}],\"experimentStates\":[], \"protocol\":{\"kind\":\"primary analysis\",\"recordedBy\":\"username\",\"shortDescription\":\"primary analysis\",\"description\":\"\", \"codeName\":\"PROT-00000003\",\"id\":96,\"ignored\":false,\"lsTransaction\":{\"comments\":\"primary analysis protocol transactions\",\"id\":87,\"recordedDate\":1363388477000,\"version\":0},\"modifiedBy\":null,\"modifiedDate\":null,\"recordedDate\":1363388477000,\"version\":1}}";
		// TODO: fix json
		Experiment experiment = experimentService.saveLsExperiment(Experiment
				.fromJsonToExperiment(json));
		logger.info("-------DONE SAVING THE EXPERIMENT----------");
		logger.info(Experiment.findExperiment(experiment.getId()).toJson());

		// Protocol protocol = Protocol.fromJsonToProtocol(json);
		// logger.info("initial json values: " + protocol.toJson());
		//
		// protocol.persist();
		// logger.info(protocol.toJson());

	}

	//@Test
	@Transactional
	//TODO: fix this test to either test SEL delete route (real delete) or Browser delete route (soft delete via status)
	public void RemoveExperimentTest() {
		Long id = 136951L;
		Experiment experiment = Experiment.findExperiment(id);
		Experiment.deleteExperiment(experiment);
		Assert.assertNull(Experiment.findExperiment(id));
	}

	// @Test
	@Transactional
	public void DeleteExperimentTest1() {
		Experiment experiment = Experiment.findExperiment(1684L);
		// logger.info(experiment.toJson());
		// experiment.remove();
		// logger.info(experiment.getCodeName());
		if (experiment != null) {
			// Experiment.deleteExperiment(experiment);
			// List<ItxSubjectContainer> results =
			// ItxSubjectContainer.findItxSubjectContainerByExperimentID(experiment.getId());
			// logger.debug("number of results: " + results.size());
			int dels = ItxSubjectContainer.deleteByExperimentID(experiment
					.getId());
			logger.debug("deleted number " + dels);
		} else {
			logger.error("Experiment is null");
		}
		// logger.info(experiment.toJson());
	}

	// @Test
	@Transactional
	public void createExperiment_test2() throws UniqueExperimentNameException {
		String json = "";
		Experiment experiment = experimentService.saveLsExperiment(Experiment
				.fromJsonToExperiment(json));
		logger.info(experiment.toJson());

	}

	// @Test
	// @Transactional
	public void createExperiment_test3() {
		Collection<ExperimentValue> savedExperimentValues = new ArrayList<ExperimentValue>();
		String json = "[{\"lsState\":{\"comments\":\"\",\"experiment\":{\"codeName\":\"EXPT-00012336\",\"id\":233771,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":13734,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"protocol\":{\"codeName\":\"PROT-00000004\",\"id\":4,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":4,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"smeyer\",\"recordedDate\":1374024874000,\"shortDescription\":\"dmpk protocol\",\"version\":1},\"recordedBy\":\"smeyer\",\"recordedDate\":1384536207000,\"shortDescription\":\"experiment created by generic data parser\",\"version\":1},\"id\":256582,\"ignored\":false,\"lsKind\":\"raw results locations\",\"lsTransaction\":13734,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_raw results locations\",\"recordedBy\":\"smeyer\",\"recordedDate\":1384536208000,\"version\":0},\"lsType\":\"fileValue\",\"lsKind\":\"source file\",\"stringValue\":null,\"fileValue\":\"FILE209321\",\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1384536208000,\"lsTransaction\":13734}]";
		int batchSize = 50;
		int i = 0;
		StringReader sr = new StringReader(json);
		BufferedReader br = new BufferedReader(sr);
		for (ExperimentValue experimentValue : ExperimentValue
				.fromJsonArrayToExperimentValues(br)) {
			experimentValue.persist();
			savedExperimentValues.add(experimentValue);

			if (i % batchSize == 0) {
				experimentValue.flush();
				experimentValue.clear();
			}
			i++;
		}
		IOUtils.closeQuietly(sr);
		IOUtils.closeQuietly(br);

		logger.debug(ExperimentValue.toJsonArray(savedExperimentValues));

	}

	// @Test
	@Transactional
	public void updateExperiment_test3() {

		List<Experiment> experiments = Experiment.findExperimentEntries(0, 2);
		Experiment testExperiment = experiments.get(0);

		Set<ExperimentState> lsStates = testExperiment.getLsStates();
		for (ExperimentState lsState : lsStates) {
			for (ExperimentValue ev : lsState.getLsValues()) {
				ev.setIgnored(false);
				ev.setComments("testing updates");
			}
		}

		logger.debug("test experiment: " + testExperiment.toPrettyJson());

	}

	// @Test
	@Transactional
	public void deleteExperimentAnalysisGroups() {
		long id = 140116L;

		// Experiment experiment = Experiment.findExperiment(id);
		//
		// // logger.debug("deleting analysis groups within an experiment: " +
		// id);
		// //
		// // logger.debug("first get a fresh count of all objects");
		// //
		// List<AnalysisGroup> analysisGroups =
		// AnalysisGroup.findAnalysisGroupsByExperiment(Experiment.findExperiment(id)).getResultList();
		// int numberOfAnalysisGroups = analysisGroups.size();
		// logger.debug("total number of numberOfAnalysisGroups: " +
		// numberOfAnalysisGroups);
		//
		// HashSet<Long> lsTranasactionIds = new HashSet<Long>();
		// for (AnalysisGroup analysisGroup : analysisGroups){
		// lsTranasactionIds.add(analysisGroup.getLsTransaction());
		// }
		// logger.debug("total number of lsTranasactionIds: " +
		// lsTranasactionIds.size());

		//
		// Collection<TreatmentGroup> treamentGroupSet = new
		// HashSet<TreatmentGroup>();
		// for (AnalysisGroup analysisGroup : analysisGroups){
		// List<TreatmentGroup> treatmentGroups =
		// TreatmentGroup.findTreatmentGroupsByAnalysisGroup(analysisGroup).getResultList();
		// logger.debug("found number of treatment group per analyisGroup: " +
		// analysisGroup.getId() + "  " + treatmentGroups.size());
		// treamentGroupSet.addAll(treatmentGroups);
		// }
		// logger.debug("total number of treamentGroups: " +
		// treamentGroupSet.size());
		//
		// Collection<Subject> subjectSet = new HashSet<Subject>();
		// for (TreatmentGroup treamentGroup : treamentGroupSet){
		// List<Subject> subjectGroups =
		// Subject.findSubjectsByTreatmentGroup(treamentGroup).getResultList();
		// logger.debug("found number of subject group per treamentGroup: " +
		// treamentGroup.getId() + "  " + subjectGroups.size());
		// subjectSet.addAll(subjectGroups);
		// }
		// logger.debug("total number of subjectSet: " + subjectSet.size());
		//
		// // now delete everything
		//
		// for (Subject subject : subjectSet){
		// subject.remove();
		// }
		//
		// for (TreatmentGroup subject : treamentGroupSet){
		// subject.remove();
		// }
		//
		// for (AnalysisGroup subject : analysisGroups){
		// subject.remove();
		// }
		//
		// experiment.remove();

		ItxSubjectContainerValue.deleteByExperimentID(id);
		ItxSubjectContainerState.deleteByExperimentID(id);
		ItxSubjectContainer.deleteByExperimentID(id);

		int deletedValues = SubjectValue.deleteByExperimentID(id);
		logger.debug("deleted number of subject values: " + deletedValues);

		int deletedLabels = SubjectLabel.deleteByExperimentID(id);
		logger.debug("deleted number of labels: " + deletedLabels);

		int numberOfStates = SubjectState.deleteByExperimentID(id);
		logger.debug("deleted number of numberOfStates: " + numberOfStates);

		// for (Long transactionId : lsTranasactionIds) {
		// logger.debug("current transaction to delete: " + transactionId);
		// //int deletedStates =
		// SubjectState.deleteByTransactionID(transactionId);
		// //logger.debug("deleted number of states: " + deletedStates);
		// }

		int deletedSubjects = Subject.deleteByExperimentID(id);
		logger.debug("deleted number of subjects: " + deletedSubjects);

		int tt2 = TreatmentGroupValue.deleteByExperimentID(id);
		logger.debug("deleted number of TreatmentGroupValue: " + tt2);

		int tt1 = TreatmentGroupState.deleteByExperimentID(id);
		logger.debug("deleted number of TreatmentGroupState: " + tt1);

		int tt3 = TreatmentGroupLabel.deleteByExperimentID(id);
		logger.debug("deleted number of TreatmentGroupLabel: " + tt3);

		int tt = TreatmentGroup.deleteByExperimentID(id);
		logger.debug("deleted number of TreatmentGroups: " + tt);

		int ag1 = AnalysisGroupValue.deleteByExperimentID(id);
		int ag2 = AnalysisGroupState.deleteByExperimentID(id);
		int ag3 = AnalysisGroupLabel.deleteByExperimentID(id);
		int ag4 = AnalysisGroup.deleteByExperimentID(id);
		logger.debug("deleted number of AnalysisGroupValue: " + ag1);
		logger.debug("deleted number of AnalysisGroupState: " + ag2);
		logger.debug("deleted number of AnalysisGroupLabel: " + ag3);
		logger.debug("deleted number of AnalysisGroup: " + ag4);

		Experiment.findExperiment(id).remove();
		logger.debug("deleted the experiment by cascade!"); // takes about a
															// minute

		//
		// int deletedAnalysisGroups = AnalysisGroup.deleteByExperimentID(id);
		// logger.debug("number of deletedAnalysisGroups: " +
		// deletedAnalysisGroups);
	}

	@Test
	@Transactional
	public void findExperiementByNameAndProtocol() {

		String labelText = "john test 2 9-2";
		String labelType = "name";
		String labelKind = "experiment name";
		boolean preferred = true;
		boolean ignored = true;
		long protocolId = 658L;
		List<ExperimentLabel> labels = ExperimentLabel
				.findExperimentLabelsByNameAndProtocol(labelText, labelType,
						labelKind, preferred, ignored, protocolId)
				.getResultList();

		logger.debug(ExperimentLabel.toJsonArray(labels));

	}

	@Test
	public void hydrateJson_test() {
		String josn = "{\"protocol\":{\"codeName\":\"PROT-00000026\",\"id\":484909,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[{\"id\":11714,\"ignored\":false,\"labelText\":\"APMS\",\"lsKind\":\"protocol name\",\"lsTransaction\":302,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_protocol name\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"nouser\",\"recordedDate\":1393216025000,\"version\":0}],\"lsStates\":[],\"lsTags\":[],\"lsTransaction\":302,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"nouser\",\"recordedDate\":1393216025000,\"shortDescription\":\"protocol created by generic data parser\",\"version\":1},\"codeName\":\"EXPT-00000389\",\"lsType\":\"default\",\"lsKind\":\"default\",\"shortDescription\":\"NA\",\"recordedBy\":\"nouser\",\"lsTransaction\":306,\"lsLabels\":[{\"experiment\":null,\"labelText\":\"Sample AMPS Expt 1001-V2\",\"recordedBy\":\"nouser\",\"lsType\":\"name\",\"lsKind\":\"experiment name\",\"preferred\":true,\"ignored\":false,\"lsTransaction\":306,\"recordedDate\":1393218563000}],\"lsStates\":[{\"experiment\":null,\"lsValues\":[{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"notebook\",\"stringValue\":\"JAM-000033\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"nouser\",\"recordedDate\":1393218563000,\"lsTransaction\":306},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"notebook page\",\"stringValue\":\"6\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"nouser\",\"recordedDate\":1393218563000,\"lsTransaction\":306},{\"lsState\":null,\"lsType\":\"dateValue\",\"lsKind\":\"completion date\",\"stringValue\":null,\"fileValue\":null,\"urlValue\":null,\"dateValue\":1373270400000,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"nouser\",\"recordedDate\":1393218563000,\"lsTransaction\":306},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"scientist\",\"stringValue\":\"jmcneil\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"nouser\",\"recordedDate\":1393218563000,\"lsTransaction\":306},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"status\",\"stringValue\":\"Approved\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"nouser\",\"recordedDate\":1393218563000,\"lsTransaction\":306},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"analysis status\",\"stringValue\":\"running\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"nouser\",\"recordedDate\":1393218563000,\"lsTransaction\":306},{\"lsState\":null,\"lsType\":\"clobValue\",\"lsKind\":\"analysis result html\",\"stringValue\":null,\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":\"<p>Analysis not yet completed</p>\",\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"recordedBy\":\"nouser\",\"recordedDate\":1393218563000,\"lsTransaction\":306},{\"lsState\":null,\"lsType\":\"codeValue\",\"lsKind\":\"project\",\"stringValue\":null,\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":\"Fluomics Project 3\",\"recordedBy\":\"nouser\",\"recordedDate\":1393218563000,\"lsTransaction\":306}],\"recordedBy\":\"nouser\",\"lsType\":\"metadata\",\"lsKind\":\"experiment metadata\",\"comments\":\"\",\"lsTransaction\":306,\"ignored\":false,\"recordedDate\":1393218563000}],\"lsTags\":[{\"tagText\":[\"apple\",\"banana\"],\"id\":null,\"version\":null,\"recordedDate\":1393218563000}],\"recordedDate\":1393218563000}";
	}

	@Test
	public void findAndSaveExperimentFromJson1() {
		String experimentName = "Test Experiment Brian";
		String json = "{\"codeName\":null,\"id\":null,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[{\"id\":null,\"ignored\":false,\"labelText\":\"Test Experiment Brian\",\"lsKind\":\"experiment name\",\"lsTransaction\":22,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_experiment name\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"nouser\",\"recordedDate\":1396912531000,\"version\":null}],\"lsStates\":[{\"comments\":\"\",\"id\":null,\"ignored\":false,\"lsKind\":\"raw results locations\",\"lsTransaction\":22,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_raw results locations\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"fileValue\":\"experiments/EXPT-00000017/2_Concentration2.xls\",\"id\":null,\"ignored\":false,\"lsKind\":\"source file\",\"lsTransaction\":22,\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_source file\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"nouser\",\"recordedDate\":1396912531000,\"unitTypeAndKind\":\"null_null\",\"version\":null}],\"recordedBy\":\"nouser\",\"recordedDate\":1396912531000,\"version\":null},{\"id\":null,\"ignored\":false,\"lsKind\":\"experiment metadata\",\"lsTransaction\":22,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_experiment metadata\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"id\":null,\"ignored\":false,\"lsKind\":\"scientist\",\"lsTransaction\":22,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"nouser\",\"recordedDate\":1396912531000,\"stringValue\":\"jmcneil\",\"unitTypeAndKind\":\"null_null\",\"version\":null},{\"codeTypeAndKind\":\"null_null\",\"id\":null,\"ignored\":false,\"lsKind\":\"notebook\",\"lsTransaction\":22,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"nouser\",\"recordedDate\":1396912531000,\"stringValue\":\"JM-576\",\"unitTypeAndKind\":\"null_null\",\"version\":null},{\"codeTypeAndKind\":\"null_null\",\"id\":null,\"ignored\":false,\"lsKind\":\"analysis status\",\"lsTransaction\":22,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_analysis status\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"username\",\"recordedDate\":1396912532000,\"stringValue\":\"complete\",\"unitTypeAndKind\":\"null_null\",\"version\":null},{\"codeTypeAndKind\":\"null_null\",\"id\":null,\"ignored\":false,\"lsKind\":\"status\",\"lsTransaction\":22,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_status\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"nouser\",\"recordedDate\":1396912531000,\"stringValue\":\"Approved\",\"unitTypeAndKind\":\"null_null\",\"version\":null},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1342076400000,\"id\":null,\"ignored\":false,\"lsKind\":\"completion date\",\"lsTransaction\":22,\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"nouser\",\"recordedDate\":1396912531000,\"unitTypeAndKind\":\"null_null\",\"version\":null},{\"codeTypeAndKind\":\"null_null\",\"id\":null,\"ignored\":false,\"lsKind\":\"notebook page\",\"lsTransaction\":22,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook page\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"nouser\",\"recordedDate\":1396912531000,\"stringValue\":\"12\",\"unitTypeAndKind\":\"null_null\",\"version\":null}],\"recordedBy\":\"nouser\",\"recordedDate\":1396912531000,\"version\": null}],\"lsTags\":[],\"lsTransaction\":22,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"protocol\":{\"codeName\":\"PROT-00000009\",\"id\":1584,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":12,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"nouser\",\"recordedDate\":1396403869000,\"shortDescription\":\"protocol created by generic data parser\",\"version\": null},\"recordedBy\":\"nouser\",\"recordedDate\":1396912531000,\"shortDescription\":\"experiment created by generic data parser\",\"version\": null}";
		List<Experiment> check1 = Experiment.findExperimentListByExperimentNameAndIgnoredNot(experimentName);
		Assert.assertTrue(check1.isEmpty());
		Experiment experiment = null;
		try {
			experiment = experimentService.saveLsExperiment(Experiment.fromJsonToExperiment(json));
		} catch (UniqueExperimentNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("Saved experiment with id:" + experiment.getId());
		List<Experiment> check2 = Experiment.findExperimentListByExperimentNameAndIgnoredNot(experimentName);
		Assert.assertFalse(check2.isEmpty());
	}
	
	@Test
	public void findAndSaveExperimentFromJson2() {
		String experimentName = "Test Experiment Brian";
		String json = "{\"codeName\":null,\"id\":null,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[{\"id\":null,\"ignored\":false,\"labelText\":\"Test Experiment Brian\",\"lsKind\":\"experiment name\",\"lsTransaction\":22,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_experiment name\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"nouser\",\"recordedDate\":null,\"version\":null}],\"lsStates\":[{\"comments\":\"\",\"id\":null,\"ignored\":false,\"lsKind\":\"raw results locations\",\"lsTransaction\":22,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_raw results locations\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"fileValue\":\"experiments/EXPT-00000017/2_Concentration2.xls\",\"id\":null,\"ignored\":false,\"lsKind\":\"source file\",\"lsTransaction\":22,\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_source file\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"nouser\",\"recordedDate\":null,\"unitTypeAndKind\":\"null_null\",\"version\":null}],\"recordedBy\":\"nouser\",\"recordedDate\":null,\"version\":null},{\"id\":null,\"ignored\":false,\"lsKind\":\"experiment metadata\",\"lsTransaction\":22,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_experiment metadata\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"id\":null,\"ignored\":false,\"lsKind\":\"scientist\",\"lsTransaction\":22,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"nouser\",\"recordedDate\":null,\"stringValue\":\"jmcneil\",\"unitTypeAndKind\":\"null_null\",\"version\":null},{\"codeTypeAndKind\":\"null_null\",\"id\":null,\"ignored\":false,\"lsKind\":\"notebook\",\"lsTransaction\":22,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"nouser\",\"recordedDate\":null,\"stringValue\":\"JM-576\",\"unitTypeAndKind\":\"null_null\",\"version\":null},{\"codeTypeAndKind\":\"null_null\",\"id\":null,\"ignored\":false,\"lsKind\":\"analysis status\",\"lsTransaction\":22,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_analysis status\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"username\",\"recordedDate\":null,\"stringValue\":\"complete\",\"unitTypeAndKind\":\"null_null\",\"version\":null},{\"codeTypeAndKind\":\"null_null\",\"id\":null,\"ignored\":false,\"lsKind\":\"status\",\"lsTransaction\":22,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_status\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"nouser\",\"recordedDate\":null,\"stringValue\":\"Approved\",\"unitTypeAndKind\":\"null_null\",\"version\":null},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1342076400000,\"id\":null,\"ignored\":false,\"lsKind\":\"completion date\",\"lsTransaction\":22,\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"nouser\",\"recordedDate\":null,\"unitTypeAndKind\":\"null_null\",\"version\":null},{\"codeTypeAndKind\":\"null_null\",\"id\":null,\"ignored\":false,\"lsKind\":\"notebook page\",\"lsTransaction\":22,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook page\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"nouser\",\"recordedDate\":null,\"stringValue\":\"12\",\"unitTypeAndKind\":\"null_null\",\"version\":null}],\"recordedBy\":\"nouser\",\"recordedDate\":null,\"version\": null}],\"lsTags\":[],\"lsTransaction\":22,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"protocol\":{\"codeName\":\"PROT-00000009\",\"id\":1584,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":12,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"nouser\",\"recordedDate\":null,\"shortDescription\":\"protocol created by generic data parser\",\"version\": null},\"recordedBy\":\"nouser\",\"recordedDate\":null,\"shortDescription\":\"experiment created by generic data parser\",\"version\": null}";
		List<Experiment> check1 = Experiment.findExperimentListByExperimentNameAndIgnoredNot(experimentName);
		Assert.assertEquals(1, check1.size());
		Experiment experiment = new Experiment();
		Boolean caughtException = false;
		try {
			experiment = experimentService.saveLsExperiment(Experiment.fromJsonToExperiment(json));
			experiment.persist();
		} catch (UniqueExperimentNameException e) {
			caughtException = true;
		}
		Assert.assertTrue(caughtException);
		logger.info("Saved experiment with id:" + experiment.getId());
		List<Experiment> check2 = Experiment.findExperimentListByExperimentNameAndIgnoredNot(experimentName);
		Assert.assertEquals(1, check2.size());
	}
	
	@Test
//	@Transactional
	public void deleteExperimentByName() {
		String experimentName = "Test Experiment Brian";
		List<Experiment> check1 = Experiment.findExperimentListByExperimentNameAndIgnoredNot(experimentName);
		Assert.assertEquals(1, check1.size());
		for (Experiment experiment: check1) {
			experiment.logicalDelete();
		}
		List<Experiment> check2 = Experiment.findExperimentListByExperimentNameAndIgnoredNot(experimentName);
		Assert.assertTrue(check2.isEmpty());
	}
	
	@Test
	public void verifyDeleted() {
		String experimentName = "Test Experiment Brian";
		List<Experiment> check1 = Experiment.findExperimentListByExperimentNameAndIgnoredNot(experimentName);
		Assert.assertEquals(0, check1.size());
	}
	
	@Transactional
	@Test
	public void experimentBrowserFilterTest() {
		String protocolName = "PAMPA Buffer A";
		String protocolCodeName = "PROT-00000001";
		String protocolType = "default";
		String protocolKind = "default";
		String name = "Buffer A Test01";
		String codeName = "EXPT-00000001";
		String type = "default";
		String kind = "default";
		
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("protocolName", protocolName);
		
		Set<Experiment> experiments = experimentService.findExperimentsByRequestMetadata(requestParams);
		Assert.assertEquals(1, experiments.size());
		experiments.clear();
		
		requestParams.put("protocolCodeName", protocolCodeName);
		experiments = experimentService.findExperimentsByRequestMetadata(requestParams);
		Assert.assertEquals(1, experiments.size());
		experiments.clear();
		
		requestParams.put("protocolType", protocolType);
		requestParams.put("protocolKind", protocolKind);
		
		experiments = experimentService.findExperimentsByRequestMetadata(requestParams);
		Assert.assertEquals(1, experiments.size());
		experiments.clear();
		
		requestParams.remove("protocolName");
		experiments = experimentService.findExperimentsByRequestMetadata(requestParams);
		Assert.assertEquals(1, experiments.size());
		
		requestParams.clear();
		
		requestParams.put("name", name);
		
		experiments = experimentService.findExperimentsByRequestMetadata(requestParams);
		Assert.assertEquals(1, experiments.size());
		experiments.clear();
		
		requestParams.put("codeName", codeName);
		experiments = experimentService.findExperimentsByRequestMetadata(requestParams);
		Assert.assertEquals(1, experiments.size());
		experiments.clear();
		
		requestParams.put("type", type);
		requestParams.put("kind", kind);
		
		experiments = experimentService.findExperimentsByRequestMetadata(requestParams);
		Assert.assertEquals(1, experiments.size());
		experiments.clear();
		
		requestParams.remove("name");
		experiments = experimentService.findExperimentsByRequestMetadata(requestParams);
		Assert.assertEquals(1, experiments.size());
	}
}
