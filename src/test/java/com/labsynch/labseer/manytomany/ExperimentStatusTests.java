package com.labsynch.labseer.manytomany;

import java.util.ArrayList;
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
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.SubjectValue;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.dto.KeyValueDTO;
import com.labsynch.labseer.service.AutoLabelService;
import com.labsynch.labseer.service.ExperimentService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class ExperimentStatusTests {

	private static final Logger logger = LoggerFactory.getLogger(ExperimentStatusTests.class);

	@Autowired
	private AutoLabelService autoLabelService;

	@Autowired
	private ExperimentService experimentService;

	@Transactional
	//@Test
	public void CountExperimentsTest() {
		List<Experiment> experiments = Experiment.findAllExperiments();
		logger.info("number of experiments found -- " + experiments.size());
		for (Experiment experiment : experiments){
			logger.info("found experiment: " + experiment.getId());
			logger.info("found experiment: " + experiment.toPrettyJson());
		}

		Assert.assertTrue(experiments.size() > 0);
	}

	@Transactional
	@Test
	public void FindExperimentStatusTest() {
		Long id = 2222L;
		String stateType = "metadata";
		String stateKind = "experiment metadata";
		String stateValueType = "stringValue";
		String stateValueKind = "analysis status";

		List<String> values = new ArrayList<String>();
		Experiment experiment = Experiment.findExperiment(id);
		Set<Experiment> experiments = new HashSet<Experiment>();
		experiments.add(experiment);
		Set<AnalysisGroup> analysisGroups = experiment.getAnalysisGroups();
		for (AnalysisGroup analysisGroup: analysisGroups) {
			Set<TreatmentGroup> treatmentGroups = analysisGroup.getTreatmentGroups();
			for (TreatmentGroup treatmentGroup : treatmentGroups) {
				Set<Subject> subjects = treatmentGroup.getSubjects();
				for (Subject subject : subjects) {
					List<SubjectState> subjectStates = SubjectState
							.findSubjectStatesByLsTypeEqualsAndLsKindEqualsAndSubject(
									stateType, stateKind, subject)
									.getResultList();
					for (SubjectState subjectState : subjectStates) {
						List<SubjectValue> subjectValues = SubjectValue
								.findSubjectValuesByLsStateAndLsTypeEqualsAndLsKindEquals(
										subjectState, stateValueType, stateValueKind)
										.getResultList();
						for (SubjectValue subjectValue : subjectValues) {
							if (stateValueType.equalsIgnoreCase("stringValue")) {
								values.add(subjectValue.getStringValue());
							} else if (stateValueType.equalsIgnoreCase("numericValue")) {
								values.add(subjectValue.getNumericValue().toString());
							}
						}
					}
				}
			}
		}
		
		KeyValueDTO transferDTO = new KeyValueDTO();
		transferDTO.setKey("lsValue");
		transferDTO.setValue(values.toString());
		logger.info("----------transferDTO----------");

		logger.info(transferDTO.toJson());

	}


}
