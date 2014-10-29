

package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

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

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentState;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class ExperimentStateServiceTests {
	
	private static final Logger logger = LoggerFactory.getLogger(ExperimentStateServiceTests.class);
	
	@Autowired
	private ExperimentStateService experimentStateService;
	
	@Test
	@Transactional
	public void QueryExperimentStateByExpIdAndStateTypeKind(){
			
		Long experimentId = 9L;
		String stateType = "metadata";
		String stateKind = "experiment metadata";
		List<ExperimentState> results = experimentStateService.getExperimentStatesByExperimentIdAndStateTypeKind(experimentId, stateType, stateKind);
		logger.info(ExperimentState.toJsonArray(results));
		assert(results.size() == 5);
	}
	
	@Test
	@Transactional
	public void QueryExperimentStateByExpIdAndStateTypeKindWithBadData() {
		Long experimentId = 9L;
		String stateType = "";
		String stateKind = "experiment metadata";
		List<ExperimentState> results = new ArrayList<ExperimentState>();
		try {
			results = experimentStateService.getExperimentStatesByExperimentIdAndStateTypeKind(experimentId, stateType, stateKind);
		} catch(IllegalArgumentException ex ) {
			logger.info(ex.getMessage());
		}
		assert(results.size() == 0);
	}
	
	@Test
	@Transactional
	public void QueryExperimentStateByExpIdAndStateTypeKindWithCodeName() {
		String experimentCodeName = "EXPT-00000003";
		String stateType = "metadata";
		String stateKind = "experiment metadata";
		Experiment experiment = null;
		boolean didCatch = false;
		try {
			experiment = Experiment.findExperimentsByCodeNameEquals(experimentCodeName).getSingleResult();
		} catch(NoResultException nre) {
			logger.info(nre.getMessage());
			didCatch = true;
		}
		List<ExperimentState> results = new ArrayList<ExperimentState>();
		try {
			results = experimentStateService.getExperimentStatesByExperimentIdAndStateTypeKind(experiment.getId(), stateType, stateKind);
		} catch(IllegalArgumentException ex ) {
			logger.info(ex.getMessage());
			assert(results.size() == 0);
			didCatch = true;
		}
		if(!didCatch) assert(results.size() == 5);
	}
	
	@Test
	@Transactional
	public void createExperimentStateByExperimentIdAndStateTypeKindTest() {
		Long experimentId = 2L;
		String lsType = "metadata";
		String lsKind = "experiment metadata";
		ExperimentState exptState = experimentStateService.createExperimentStateByExperimentIdAndStateTypeKind(experimentId, lsType, lsKind);
		Assert.assertNotNull(exptState);
		logger.info(exptState.toJson());
	}
	
}
