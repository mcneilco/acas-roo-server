package com.labsynch.labseer.domainImpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.TreatmentGroupValue;

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
public class TreatmentGroupValueTest {

	private static final Logger logger = LoggerFactory.getLogger(TreatmentGroupValueTest.class);

	// @Test
	@Transactional
	public void QueryTreatmentGroupValueByExpIdAndStateTypeKind() {

		Long experimentId = 14L;
		String stateType = "data";
		String stateKind = "test compound treatment";
		List<TreatmentGroupValue> results = TreatmentGroupValue
				.findTreatmentGroupValuesByExptIDAndStateTypeKind(experimentId, stateType, stateKind).getResultList();
		logger.info(TreatmentGroupValue.toJsonArray(results));
		assert (results.size() == 120);
	}

	// @Test
	@Transactional
	public void QueryTreatmentGroupValueByExpIdAndStateTypeKindWithBadData() {
		Long experimentId = 14L;
		String stateType = "";
		String stateKind = "experiment metadata";
		List<TreatmentGroupValue> results = new ArrayList<TreatmentGroupValue>();
		try {
			results = TreatmentGroupValue
					.findTreatmentGroupValuesByExptIDAndStateTypeKind(experimentId, stateType, stateKind)
					.getResultList();
		} catch (IllegalArgumentException ex) {
			logger.info(ex.getMessage());
		}
		assert (results.size() == 0);
	}

	// @Test
	@Transactional
	public void QueryTreatmentGroupValueByExpIdAndStateTypeKindWithCodeName() {
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
		List<TreatmentGroupValue> results = new ArrayList<TreatmentGroupValue>();
		try {
			results = TreatmentGroupValue
					.findTreatmentGroupValuesByExptIDAndStateTypeKind(experiment.getId(), stateType, stateKind)
					.getResultList();
		} catch (IllegalArgumentException ex) {
			logger.info(ex.getMessage());
			assert (results.size() == 0);
			didCatch = true;
		}
		if (!didCatch)
			assert (results.size() == 120);
	}

	// @Test
	@Transactional
	public void QueryTreatmentGroupValueByExpIdAndStateTypeKindAndValueTypeKind() {

		Long experimentId = 14L;
		String stateType = "data";
		String stateKind = "test compound treatment";
		String valueType = "numericValue";
		String valueKind = "Dose";
		List<TreatmentGroupValue> results = TreatmentGroupValue
				.findTreatmentGroupValuesByExptIDAndStateTypeKindAndValueTypeKind(experimentId, stateType, stateKind,
						valueType, valueKind)
				.getResultList();
		logger.info(TreatmentGroupValue.toJsonArray(results));
		assert (results.size() == 120);
	}

	@Test
	@Transactional
	public void QueryTreatmentGroupValueByAnalysisGroupIdStateTypeAndKind() {
		Long analysisGroupId = 15L;
		String stateType = "data";
		String stateKind = "test compound treatment";
		List<TreatmentGroupValue> treatmentGroupValues = TreatmentGroupValue
				.findTreatmentGroupValuesByAnalysisGroupIDAndStateTypeKind(analysisGroupId, stateType, stateKind)
				.getResultList();
		logger.info(String.valueOf(treatmentGroupValues.size()));
	}
}
