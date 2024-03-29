package com.labsynch.labseer.domainImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;

import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.LsTag;
import com.labsynch.labseer.domain.LsThing;

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
public class LsThingTest {

	private static final Logger logger = LoggerFactory.getLogger(LsThingTest.class);

	@Test
	@Transactional
	public void getLsthingsByProjectName() {
		String lsType = "project";
		String lsKind = "project";
		String labelType = "name";
		String labelKind = "project name";
		String labelText = "Project 1";
		List<LsThing> results = LsThing.findLsThingByLabelText(lsType, lsKind, labelType, labelKind, labelText)
				.getResultList();
		logger.info("query labelText: " + labelText + " number of results: " + results.size());
		if (results.size() > 0) {
			for (LsThing result : results) {
				// 12597
				logger.info("attempting to output JSON: -----------------  " + result.getCodeName() + "  "
						+ result.getId());
				LsThing newThing = LsThing.findLsThing(result.getId());
				Set<LsTag> tags = newThing.getLsTags();
				for (LsTag tag : tags) {
					logger.info("tag here: " + tag.toJson());
				}
				logger.info(newThing.toJson());
			}
		}
	}

	// @Test
	@Transactional
	public void getLsthingsByName() {
		String lsType = "gene";
		String lsKind = "entrez gene";
		String labelType = "name";
		String labelKind = "Entrez Gene ID";
		String labelText = "47767";
		List<LsThing> results = LsThing.findLsThingByLabelText(lsType, lsKind, labelType, labelKind, labelText)
				.getResultList();
		logger.info("query labelText: " + labelText + " number of results: " + results.size());
		if (results.size() > 0) {
			for (LsThing result : results) {
				// 12597
				logger.info("attempting to output JSON: -----------------  " + result.getCodeName() + "  "
						+ result.getId());
				LsThing newThing = LsThing.findLsThing(result.getId());
				Set<LsTag> tags = newThing.getLsTags();
				for (LsTag tag : tags) {
					logger.info("tag here: " + tag.toJson());
				}
				logger.info(newThing.toJson());
			}
		}
	}

	// @Test
	@Transactional
	public void QueryAnalysisGroupValueByExpIdAndStateTypeKind() {

		Long experimentId = 9L;
		String stateType = "data";
		String stateKind = "Generic";
		List<AnalysisGroupValue> results = AnalysisGroupValue
				.findAnalysisGroupValuesByExptIDAndStateTypeKind(experimentId, stateType, stateKind).getResultList();
		logger.info(AnalysisGroupValue.toJsonArray(results));
		assert (results.size() == 11);
	}

	// @Test
	@Transactional
	public void QueryAnalysisGroupValueByExpIdAndStateTypeKindWithBadData() {
		Long experimentId = 9L;
		String stateType = "";
		String stateKind = "experiment metadata";
		List<AnalysisGroupValue> results = new ArrayList<AnalysisGroupValue>();
		try {
			results = AnalysisGroupValue
					.findAnalysisGroupValuesByExptIDAndStateTypeKind(experimentId, stateType, stateKind)
					.getResultList();
		} catch (IllegalArgumentException ex) {
			logger.info(ex.getMessage());
		}
		assert (results.size() == 0);
	}

	// @Test
	@Transactional
	public void QueryAnalysisGroupValueByExpIdAndStateTypeKindWithCodeName() {
		String experimentCodeName = "EXPT-00000003";
		String stateType = "data";
		String stateKind = "Generic";
		Experiment experiment = null;
		boolean didCatch = false;
		try {
			experiment = Experiment.findExperimentsByCodeNameEquals(experimentCodeName).getSingleResult();
		} catch (NoResultException nre) {
			logger.info(nre.getMessage());
			didCatch = true;
		}
		List<AnalysisGroupValue> results = new ArrayList<AnalysisGroupValue>();
		try {
			results = AnalysisGroupValue
					.findAnalysisGroupValuesByExptIDAndStateTypeKind(experiment.getId(), stateType, stateKind)
					.getResultList();
		} catch (IllegalArgumentException ex) {
			logger.info(ex.getMessage());
			assert (results.size() == 0);
			didCatch = true;
		}
		if (!didCatch)
			assert (results.size() == 11);
	}

	// @Test
	@Transactional
	public void QueryAnalysisGroupValueByExpIdAndStateTypeKindAndValueTypeKind() {

		Long experimentId = 9L;
		String stateType = "data";
		String stateKind = "Generic";
		String valueType = "numericValue";
		String valueKind = "solubility";
		List<AnalysisGroupValue> results = AnalysisGroupValue
				.findAnalysisGroupValuesByExptIDAndStateTypeKindAndValueTypeKind(experimentId, stateType, stateKind,
						valueType, valueKind)
				.getResultList();
		logger.info(AnalysisGroupValue.toJsonArray(results));
		assert (results.size() == 2);
	}

	@Test
	@Transactional
	public void QueryAnalysisGroupValueByAnalysisGroupIdStateTypeAndKind() {
		Long analysisGroupId = 10L;
		String stateType = "data";
		String stateKind = "Generic";
		List<AnalysisGroupValue> analysisGroupValues = AnalysisGroupValue
				.findAnalysisGroupValuesByAnalysisGroupIDAndStateTypeKind(analysisGroupId, stateType, stateKind)
				.getResultList();
		logger.info(String.valueOf(analysisGroupValues.size()));
	}

	@Test
	@Transactional
	public void QueryAnalysisGroupValueByAnalysisGroupIdStateTypeAndKindAndValueTypeKind() {
		Long analysisGroupId = 10L;
		String stateType = "data";
		String stateKind = "Generic";
		String valueType = "stringValue";
		String valueKind = "Assay Comment";
		List<AnalysisGroupValue> analysisGroupValues = AnalysisGroupValue
				.findAnalysisGroupValuesByAnalysisGroupIDAndStateTypeKindAndValueTypeKind(analysisGroupId, stateType,
						stateKind, valueType, valueKind)
				.getResultList();
		logger.info(String.valueOf(analysisGroupValues.size()));
	}

}
