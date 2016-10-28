package com.labsynch.labseer.domainImpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentValue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class ExperimentValueTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ExperimentValueTest.class);

	@Test
	@Transactional
	public void QueryExperimentValueByKinds(){
		
		Long experimentId = 9L;
		String stateType = "metadata";
		String stateKind = "experiment metadata";
		String valueType = "stringValue";
		String valueKind = "analysis status";
		List<ExperimentValue> results = ExperimentValue.findExperimentValuesByExptIDAndStateTypeKindAndValueTypeKind(experimentId, stateType, stateKind, valueType, valueKind).getResultList();
		logger.info(ExperimentValue.toJsonArray(results));
		//do some asserts to validate the tests are correct
	}
	
	@Test
	@Transactional
	public void QueryExperimentValueByExpIdAndStateTypeKind(){
			
		Long experimentId = 9L;
		String stateType = "metadata";
		String stateKind = "experiment metadata";
		List<ExperimentValue> results = ExperimentValue.findExperimentValuesByExptIDAndStateTypeKind(experimentId, stateType, stateKind).getResultList();
		logger.info(ExperimentValue.toJsonArray(results));
		assert(results.size() == 8);
	}
	
	@Test
	@Transactional
	public void QueryExperimentValueByExpIdAndStateTypeKindWithBadData() {
		Long experimentId = 9L;
		String stateType = "";
		String stateKind = "experiment metadata";
		List<ExperimentValue> results = new ArrayList<ExperimentValue>();
		try {
			results = ExperimentValue.findExperimentValuesByExptIDAndStateTypeKind(experimentId, stateType, stateKind).getResultList();
		} catch(IllegalArgumentException ex ) {
			logger.info(ex.getMessage());
		}
		assert(results.size() == 0);
	}
	
	@Test
	@Transactional
	public void QueryExperimentValueByExpIdAndStateTypeKindWithCodeName() {
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
		List<ExperimentValue> results = new ArrayList<ExperimentValue>();
		try {
			results = ExperimentValue.findExperimentValuesByExptIDAndStateTypeKind(experiment.getId(), stateType, stateKind).getResultList();
		} catch(IllegalArgumentException ex ) {
			logger.info(ex.getMessage());
			assert(results.size() == 0);
			didCatch = true;
		}
		if(!didCatch) assert(results.size() == 8);
	}
	
	@Test
	@Transactional
	public void updateExperimentValueTest() {
		String json = "{\"clobValue\":\"hello world 3\",\"codeKind\":null,\"codeOrigin\":null,\"codeType\":null,\"codeTypeAndKind\":\"null_null\",\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"deleted\":false,\"fileValue\":null,\"id\":1,\"ignored\":false,\"lsKind\":\"status\",\"lsState\":{\"comments\":null,\"deleted\":false,\"experiment\":{\"codeName\":\"EXPT-00000001\",\"deleted\":false,\"id\":2,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"protocol\":{\"codeName\":\"PROT-00000001\",\"deleted\":false,\"id\":1,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"bob\",\"recordedDate\":1414185360000,\"shortDescription\":\"protocol created by generic data parser\",\"version\":1},\"recordedBy\":\"bob\",\"recordedDate\":1414185360000,\"shortDescription\":\"experiment created by generic data parser\",\"version\":1},\"id\":1,\"ignored\":false,\"lsKind\":\"experiment metadata\",\"lsTransaction\":1,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_experiment metadata\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"bob\",\"recordedDate\":1414185360000,\"version\":1},\"lsTransaction\":1,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_status\",\"modifiedBy\":null,\"modifiedDate\":null,\"numberOfReplicates\":null,\"numericValue\":null,\"operatorKind\":null,\"operatorType\":null,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"bob\",\"recordedDate\":1414185360000,\"sigFigs\":null,\"stringValue\":\"Approved\",\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null,\"unitType\":null,\"unitTypeAndKind\":\"null_null\",\"urlValue\":null,\"version\":null}";
		ExperimentValue experimentValue = ExperimentValue.fromJsonToExperimentValue(json);
		ExperimentValue ev2 = ExperimentValue.findExperimentValue(experimentValue.getId());
		experimentValue.setVersion(ev2.getVersion());
		experimentValue.merge();
		experimentValue.flush();
		ExperimentValue ev3 = ExperimentValue.findExperimentValue(experimentValue.getId());
		logger.debug(ev3.toJson());
	}
	
}
