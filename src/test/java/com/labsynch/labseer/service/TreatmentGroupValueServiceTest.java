

package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.NoResultException;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.TreatmentGroupValue;

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
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class TreatmentGroupValueServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(TreatmentGroupValueServiceTest.class);

	@Autowired
	private TreatmentGroupValueService treatmentGroupValueService;

	@Test
	@Transactional
	public void QueryTreatmentGroupValueByExpIdAndStateTypeKind(){
			
		Long experimentId = 14L;
		String stateType = "data";
		String stateKind = "test compound treatment";
		List<TreatmentGroupValue> results = treatmentGroupValueService.getTreatmentGroupValuesByExperimentIdAndStateTypeKind(experimentId, stateType, stateKind);
		logger.info(TreatmentGroupValue.toJsonArray(results));
		assert(results.size() == 120);
	}
	
	@Test
	@Transactional
	public void QueryTreatmentGroupValueByExpIdAndStateTypeKindWithBadData() {
		Long experimentId = 14L;
		String stateType = "";
		String stateKind = "experiment metadata";
		List<TreatmentGroupValue> results = new ArrayList<TreatmentGroupValue>();
		try {
			results = treatmentGroupValueService.getTreatmentGroupValuesByExperimentIdAndStateTypeKind(experimentId, stateType, stateKind);
		} catch(IllegalArgumentException ex ) {
			logger.info(ex.getMessage());
		}
		assert(results.size() == 0);
	}
	
	@Test
	@Transactional
	public void QueryTreatmentGroupValueByExpIdAndStateTypeKindWithCodeName() {
		String experimentCodeName = "EXPT-00000152";
		String stateType = "data";
		String stateKind = "test compound treatment";
		Experiment experiment = null;
		boolean didCatch = false;
		try {
			experiment = Experiment.findExperimentsByCodeNameEquals(experimentCodeName).getSingleResult();
		} catch(NoResultException nre) {
			logger.info(nre.getMessage());
			didCatch = true;
		}
		List<TreatmentGroupValue> results = new ArrayList<TreatmentGroupValue>();
		try {
			results = treatmentGroupValueService.getTreatmentGroupValuesByExperimentIdAndStateTypeKind(experiment.getId(), stateType, stateKind);
		} catch(IllegalArgumentException ex ) {
			logger.info(ex.getMessage());
			assert(results.size() == 0);
			didCatch = true;
		}
		if(!didCatch) assert(results.size() == 120);
	}
	
	@Test
	@Transactional
	public void QueryTreatmentGroupValueByExpIdAndStateTypeKindAndValueTypeKind(){
			
		Long experimentId = 14L;
		String stateType = "data";
		String stateKind = "test compound treatment";
		String valueType = "numericValue";
		String valueKind = "Dose";
		List<TreatmentGroupValue> results = treatmentGroupValueService.getTreatmentGroupValuesByExperimentIdAndStateTypeKindAndValueTypeKind(experimentId, stateType, stateKind, valueType, valueKind);
		logger.info(TreatmentGroupValue.toJsonArray(results));
		assert(results.size() == 120);
	}
	
	@Test
	@Transactional
	public void TreatmentGroupValuesToCsv() {
		List<TreatmentGroupValue> treatmentGroupValues = treatmentGroupValueService.getTreatmentGroupValuesByExperimentIdAndStateTypeKind(9l, "metadata", "experiment metadata");
		String csvString = treatmentGroupValueService.getCsvList(treatmentGroupValues);
		assert(csvString != null && csvString.compareTo("") != 0);
		logger.info(csvString);
	}
	
	@Test
	@Transactional
	public void TreatmentGroupValuesToCsvForCurveFit() {
		List<TreatmentGroupValue> treatmentGroupValues = treatmentGroupValueService.getTreatmentGroupValuesByExperimentIdAndStateTypeKindAndValueTypeKind(54375L, "data", "results", "numericValue", "Response");
		String csvString = treatmentGroupValueService.getCsvList(treatmentGroupValues);
		Assert.assertNotNull(csvString);
		logger.info(csvString);
	}
	
	@Test
	@Transactional
	public void updateTreatmentGroupValueTest() {
		String idOrCodeName = "35";
		String stateType = "data";
		String stateKind = "results";
		String valueType = "stringValue";
		String valueKind = "status";
		String value = "Deleted";
		TreatmentGroupValue treatmentGroupValue = treatmentGroupValueService.updateTreatmentGroupValue(idOrCodeName, stateType, stateKind, valueType, valueKind, value);
		Assert.assertNotNull(treatmentGroupValue);
		logger.info(treatmentGroupValue.toJson());
	}
	
	@Test
	@Transactional
	public void saveTreatmentGroupValueFromJson() {
		String json = "{\"lsState\":{\"deleted\":false,\"id\":1074932,\"ignored\":false,\"lsKind\":\"results\",\"lsTransaction\":1928,\"lsType\":\"data\",\"lsTypeAndKind\":\"data_results\",\"recordedBy\":\"bob\",\"recordedDate\":1412716394689,\"treatmentGroup\":{\"codeName\":\"TG-00108073\",\"deleted\":false,\"id\":703067,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1928,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"bob\",\"recordedDate\":1412716394686,\"version\":0},\"version\":0},\"lsType\":\"stringValue\",\"lsKind\":\"analysis status\",\"stringValue\":\"now\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"username\",\"recordedDate\":1415752566000,\"lsTransaction\":3068}";
		TreatmentGroupValue treatmentGroupValue = TreatmentGroupValue.fromJsonToTreatmentGroupValue(json);
		treatmentGroupValue = treatmentGroupValueService.saveTreatmentGroupValue(treatmentGroupValue);
		Assert.assertNotNull(treatmentGroupValue.getId());
	}
	
	@Test
	@Transactional
	public void saveTreatmentGroupValuesFromJson() {
		String json = "[{\"lsState\":{\"deleted\":false,\"id\":1074932,\"ignored\":false,\"lsKind\":\"results\",\"lsTransaction\":1928,\"lsType\":\"data\",\"lsTypeAndKind\":\"data_results\",\"recordedBy\":\"bob\",\"recordedDate\":1412716394689,\"treatmentGroup\":{\"codeName\":\"TG-00108073\",\"deleted\":false,\"id\":703067,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1928,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"bob\",\"recordedDate\":1412716394686,\"version\":0},\"version\":0},\"lsType\":\"stringValue\",\"lsKind\":\"analysis status\",\"stringValue\":\"now\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"username\",\"recordedDate\":1415752566000,\"lsTransaction\":3068},{\"lsState\":{\"deleted\":false,\"id\":1074932,\"ignored\":false,\"lsKind\":\"results\",\"lsTransaction\":1928,\"lsType\":\"data\",\"lsTypeAndKind\":\"data_results\",\"recordedBy\":\"bob\",\"recordedDate\":1412716394689,\"treatmentGroup\":{\"codeName\":\"TG-00108073\",\"deleted\":false,\"id\":703067,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1928,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"bob\",\"recordedDate\":1412716394686,\"version\":0},\"version\":0},\"lsType\":\"stringValue\",\"lsKind\":\"analysis status\",\"stringValue\":\"now\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"username\",\"recordedDate\":1415752566000,\"lsTransaction\":3068},{\"lsState\":{\"deleted\":false,\"id\":1074932,\"ignored\":false,\"lsKind\":\"results\",\"lsTransaction\":1928,\"lsType\":\"data\",\"lsTypeAndKind\":\"data_results\",\"recordedBy\":\"bob\",\"recordedDate\":1412716394689,\"treatmentGroup\":{\"codeName\":\"TG-00108073\",\"deleted\":false,\"id\":703067,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1928,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"bob\",\"recordedDate\":1412716394686,\"version\":0},\"version\":0},\"lsType\":\"stringValue\",\"lsKind\":\"analysis status\",\"stringValue\":\"now\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"username\",\"recordedDate\":1415752566000,\"lsTransaction\":3068},{\"lsState\":{\"deleted\":false,\"id\":1074932,\"ignored\":false,\"lsKind\":\"results\",\"lsTransaction\":1928,\"lsType\":\"data\",\"lsTypeAndKind\":\"data_results\",\"recordedBy\":\"bob\",\"recordedDate\":1412716394689,\"treatmentGroup\":{\"codeName\":\"TG-00108073\",\"deleted\":false,\"id\":703067,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1928,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"bob\",\"recordedDate\":1412716394686,\"version\":0},\"version\":0},\"lsType\":\"stringValue\",\"lsKind\":\"analysis status\",\"stringValue\":\"now\",\"fileValue\":null,\"urlValue\":null,\"dateValue\":null,\"clobValue\":null,\"blobValue\":null,\"operatorKind\":null,\"operatorType\":null,\"numericValue\":null,\"sigFigs\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"numberOfReplicates\":null,\"unitKind\":null,\"comments\":null,\"ignored\":false,\"publicData\":true,\"codeValue\":null,\"codeOrigin\":null,\"codeType\":null,\"codeKind\":null,\"recordedBy\":\"username\",\"recordedDate\":1415752566000,\"lsTransaction\":3068}]";
		Collection<TreatmentGroupValue> treatmentGroupValues = TreatmentGroupValue.fromJsonArrayToTreatmentGroupValues(json);
		treatmentGroupValues = treatmentGroupValueService.saveTreatmentGroupValues(treatmentGroupValues);
		for (TreatmentGroupValue treatmentGroupValue: treatmentGroupValues) {
			Assert.assertNotNull(treatmentGroupValue.getId());
		}
	}

}
