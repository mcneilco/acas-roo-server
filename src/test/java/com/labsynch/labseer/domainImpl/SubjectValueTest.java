package com.labsynch.labseer.domainImpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.SubjectValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml" })
@Configurable
public class SubjectValueTest {

	private static final Logger logger = LoggerFactory.getLogger(SubjectValueTest.class);

	// @Test
	@Transactional
	public void QuerySubjectValueByExpIdAndStateTypeKind() {

		Long experimentId = 14L;
		String stateType = "data";
		String stateKind = "test compound treatment";
		List<SubjectValue> results = SubjectValue
				.findSubjectValuesByExptIDAndStateTypeKind(experimentId, stateType, stateKind).getResultList();
		logger.info(SubjectValue.toJsonArray(results));
		assert (results.size() == 800);
	}

	// @Test
	@Transactional
	public void QuerySubjectValueByExpIdAndStateTypeKindWithBadData() {
		Long experimentId = 14L;
		String stateType = "";
		String stateKind = "experiment metadata";
		List<SubjectValue> results = new ArrayList<SubjectValue>();
		try {
			results = SubjectValue.findSubjectValuesByExptIDAndStateTypeKind(experimentId, stateType, stateKind)
					.getResultList();
		} catch (IllegalArgumentException ex) {
			logger.info(ex.getMessage());
		}
		assert (results.size() == 0);
	}

	// @Test
	@Transactional
	public void QuerySubjectValueByExpIdAndStateTypeKindWithCodeName() {
		String experimentCodeName = "EXPT-00000004";
		String stateType = "data";
		String stateKind = "test compound treatment";
		Experiment experiment = null;
		boolean didCatch = false;
		try {
			experiment = Experiment.findExperimentsByCodeNameEquals(experimentCodeName).getSingleResult();
		} catch (NoResultException nre) {
			logger.info(nre.getMessage());
			didCatch = true;
		}
		List<SubjectValue> results = new ArrayList<SubjectValue>();
		try {
			results = SubjectValue.findSubjectValuesByExptIDAndStateTypeKind(experiment.getId(), stateType, stateKind)
					.getResultList();
		} catch (IllegalArgumentException ex) {
			logger.info(ex.getMessage());
			assert (results.size() == 0);
			didCatch = true;
		}
		if (!didCatch)
			assert (results.size() == 800);
	}

	// @Test
	@Transactional
	public void QuerySubjectValueByExpIdAndStateTypeKindAndValueTypeKind() {

		Long experimentId = 14L;
		String stateType = "data";
		String stateKind = "test compound treatment";
		String valueType = "numericValue";
		String valueKind = "Dose";
		List<SubjectValue> results = SubjectValue.findSubjectValuesByExptIDAndStateTypeKindAndValueTypeKind(
				experimentId, stateType, stateKind, valueType, valueKind).getResultList();
		logger.info(SubjectValue.toJsonArray(results));
		assert (results.size() == 400);
	}

	@Test
	@Transactional
	public void QuerySubjectValueByAnalysisGroupIdStateTypeAndKind() {
		Long analysisGroupId = 15L;
		String stateType = "data";
		String stateKind = "results";
		List<SubjectValue> subjectValues = SubjectValue
				.findSubjectValuesByAnalysisGroupIDAndStateTypeKind(analysisGroupId, stateType, stateKind)
				.getResultList();
		logger.info(String.valueOf(subjectValues.size()));
	}
}
