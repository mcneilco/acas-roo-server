
package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.NoResultException;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.ExperimentValue;

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
public class ExperimentStateServiceTests {

	private static final Logger logger = LoggerFactory.getLogger(ExperimentStateServiceTests.class);

	@Autowired
	private ExperimentStateService experimentStateService;

	// @Test
	@Transactional
	public void QueryExperimentStateByExpIdAndStateTypeKind() {

		Long experimentId = 9L;
		String stateType = "metadata";
		String stateKind = "experiment metadata";
		List<ExperimentState> results = experimentStateService
				.getExperimentStatesByExperimentIdAndStateTypeKind(experimentId, stateType, stateKind);
		logger.info(ExperimentState.toJsonArray(results));
		assert (results.size() == 5);
	}

	// @Test
	@Transactional
	public void QueryExperimentStateByExpIdAndStateTypeKindWithBadData() {
		Long experimentId = 9L;
		String stateType = "";
		String stateKind = "experiment metadata";
		List<ExperimentState> results = new ArrayList<ExperimentState>();
		try {
			results = experimentStateService.getExperimentStatesByExperimentIdAndStateTypeKind(experimentId, stateType,
					stateKind);
		} catch (IllegalArgumentException ex) {
			logger.info(ex.getMessage());
		}
		assert (results.size() == 0);
	}

	// @Test
	@Transactional
	public void QueryExperimentStateByExpIdAndStateTypeKindWithCodeName() {
		String experimentCodeName = "EXPT-00000003";
		String stateType = "metadata";
		String stateKind = "experiment metadata";
		Experiment experiment = null;
		boolean didCatch = false;
		try {
			experiment = Experiment.findExperimentsByCodeNameEquals(experimentCodeName).getSingleResult();
		} catch (NoResultException nre) {
			logger.info(nre.getMessage());
			didCatch = true;
		}
		List<ExperimentState> results = new ArrayList<ExperimentState>();
		try {
			results = experimentStateService.getExperimentStatesByExperimentIdAndStateTypeKind(experiment.getId(),
					stateType, stateKind);
		} catch (IllegalArgumentException ex) {
			logger.info(ex.getMessage());
			assert (results.size() == 0);
			didCatch = true;
		}
		if (!didCatch)
			assert (results.size() == 5);
	}

	// @Test
	@Transactional
	public void createExperimentStateByExperimentIdAndStateTypeKindTest() {
		Long experimentId = 2L;
		String lsType = "metadata";
		String lsKind = "experiment metadata";
		ExperimentState exptState = experimentStateService
				.createExperimentStateByExperimentIdAndStateTypeKind(experimentId, lsType, lsKind);
		Assert.assertNotNull(exptState);
		logger.info(exptState.toJson());
	}

	@Test
	@Transactional
	public void createExperimentStatesFromJson() {
		String json = "[{\"experiment\":{\"codeName\":\"EXPT-00004\",\"deleted\":false,\"id\":97418,\"version\":1},\"lsValues\":[{\"lsState\":null,\"lsType\":\"numericValue\",\"lsKind\":\"column order\",\"stringValue\":null,\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":1,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"goshiro\",\"recordedDate\":1423885275000,\"lsTransaction\":7},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"column name\",\"stringValue\":\"McNeil Hit\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"goshiro\",\"recordedDate\":1423885275000,\"lsTransaction\":7},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"publicData\",\"stringValue\":\"false\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"goshiro\",\"recordedDate\":1423885275000,\"lsTransaction\":7}],\"recordedBy\":\"goshiro\",\"lsType\":\"metadata\",\"lsKind\":\"data column order\",\"comments\":\"\",\"lsTransaction\":7,\"ignored\":false,\"recordedDate\":1423885275000},{\"experiment\":{\"codeName\":\"EXPT-00004\",\"deleted\":false,\"id\":97418,\"version\":1},\"lsValues\":[{\"lsState\":null,\"lsType\":\"numericValue\",\"lsKind\":\"column order\",\"stringValue\":null,\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":2,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"goshiro\",\"recordedDate\":1423885275000,\"lsTransaction\":7},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"column name\",\"stringValue\":\"Avogadro Hit\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"goshiro\",\"recordedDate\":1423885275000,\"lsTransaction\":7},{\"lsState\":null,\"lsType\":\"stringValue\",\"lsKind\":\"publicData\",\"stringValue\":\"true\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"goshiro\",\"recordedDate\":1423885275000,\"lsTransaction\":7}],\"recordedBy\":\"goshiro\",\"lsType\":\"metadata\",\"lsKind\":\"data column order\",\"comments\":\"\",\"lsTransaction\":7,\"ignored\":false,\"recordedDate\":1423885275000}]";
		Collection<ExperimentState> experimentStates = ExperimentState.fromJsonArrayToExperimentStates(json);
		Collection<ExperimentState> exptStates = experimentStateService.saveExperimentStates(experimentStates);
		Assert.assertNotNull(exptStates);
		logger.info(ExperimentState.toJsonArray(exptStates));
	}

	@Test
	public void deleteExperimentStates() {
		String codeName = "EXPT-";
		List<Experiment> experiments = Experiment.findExperimentsByCodeNameLike(codeName).getResultList();
		for (Experiment experiment : experiments) {
			for (ExperimentState es : ExperimentState.findExperimentStatesByExperiment(experiment).getResultList()) {
				if (es.getLsType().equalsIgnoreCase("metadata")
						&& es.getLsKind().equalsIgnoreCase("data column order")) {
					for (ExperimentValue ev : ExperimentValue.findExperimentValuesByLsState(es).getResultList()) {
						ev.remove();
					}
					es.remove();
				}
			}
		}

	}

}
