package com.labsynch.labseer.service;

import java.util.Collection;

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

import com.labsynch.labseer.domain.TreatmentGroupState;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class TreatmentGroupStateServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(TreatmentGroupStateServiceTest.class);

	@Autowired
	private TreatmentGroupStateService treatmentGroupStateService;
	
	@Test
	@Transactional
	public void createTreatmentGroupStateByTreatmentGroupIdAndStateTypeKindTest() {
		Long treatmentGroupId = 35L;
		String lsType = "metadata";
		String lsKind = "treatmentGroup metadata";
		TreatmentGroupState treatmentGroupState = treatmentGroupStateService.createTreatmentGroupStateByTreatmentGroupIdAndStateTypeKind(treatmentGroupId, lsType, lsKind);
		Assert.assertNotNull(treatmentGroupState);
		logger.info(treatmentGroupState.toJson());
	}
	
	@Test
	@Transactional
	public void saveTreatmentGroupStateFromJson() {
		String json = "{\"treatmentGroup\":{\"analysisGroups\":[{\"codeName\":\"AG-00342416\",\"deleted\":false,\"id\":499698,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142896894,\"version\":0}],\"codeName\":\"TG-00076401\",\"deleted\":false,\"id\":499710,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsStates\":[{\"comments\":null,\"deleted\":false,\"id\":685925,\"ignored\":false,\"lsKind\":\"test compound treatment\",\"lsTransaction\":1368,\"lsType\":\"data\",\"lsTypeAndKind\":\"data_test compound treatment\",\"lsValues\":[{\"analysisGroupId\":0,\"clobValue\":null,\"codeKind\":null,\"codeOrigin\":null,\"codeType\":null,\"codeTypeAndKind\":\"null_null\",\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"deleted\":false,\"fileValue\":null,\"id\":2284749,\"ignored\":false,\"lsKind\":\"Dose\",\"lsTransaction\":1368,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_Dose\",\"modifiedBy\":null,\"modifiedDate\":null,\"numberOfReplicates\":null,\"numericValue\":20,\"operatorKind\":null,\"operatorType\":null,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142898184,\"sigFigs\":null,\"stateId\":685925,\"stateKind\":\"test compound treatment\",\"stateType\":\"data\",\"stringValue\":null,\"treatmentGroupId\":499710,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":\"uM\",\"unitType\":null,\"unitTypeAndKind\":\"null_uM\",\"urlValue\":null,\"version\":0}],\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142898182,\"version\":0},{\"comments\":null,\"deleted\":false,\"id\":685926,\"ignored\":false,\"lsKind\":\"results\",\"lsTransaction\":1368,\"lsType\":\"data\",\"lsTypeAndKind\":\"data_results\",\"lsValues\":[{\"analysisGroupId\":0,\"clobValue\":null,\"codeKind\":null,\"codeOrigin\":null,\"codeType\":null,\"codeTypeAndKind\":\"null_null\",\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"deleted\":false,\"fileValue\":null,\"id\":2284750,\"ignored\":false,\"lsKind\":\"Response\",\"lsTransaction\":1368,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_Response\",\"modifiedBy\":null,\"modifiedDate\":null,\"numberOfReplicates\":null,\"numericValue\":93.85875,\"operatorKind\":null,\"operatorType\":null,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142898187,\"sigFigs\":null,\"stateId\":685926,\"stateKind\":\"results\",\"stateType\":\"data\",\"stringValue\":null,\"treatmentGroupId\":499710,\"uncertainty\":3.59397712615611,\"uncertaintyType\":\"standard deviation\",\"unitKind\":\"efficacy\",\"unitType\":null,\"unitTypeAndKind\":\"null_efficacy\",\"urlValue\":null,\"version\":0}],\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142898186,\"version\":0}],\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142898181,\"subjects\":[{\"codeName\":\"SUBJ-00236004\",\"deleted\":false,\"id\":499830,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142900029,\"version\":0},{\"codeName\":\"SUBJ-00236024\",\"deleted\":false,\"id\":499850,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142900482,\"version\":0},{\"codeName\":\"SUBJ-00236014\",\"deleted\":false,\"id\":499840,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142900255,\"version\":0},{\"codeName\":\"SUBJ-00236034\",\"deleted\":false,\"id\":499860,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142900674,\"version\":0}],\"version\":0},\"lsValues\":null,\"recordedBy\":\"userName\",\"lsType\":\"data\",\"lsKind\":\"results\",\"comments\":\"\",\"lsTransaction\":3067,\"ignored\":false,\"recordedDate\":1415752381000}";
		TreatmentGroupState treatmentGroupState = TreatmentGroupState.fromJsonToTreatmentGroupState(json);
		treatmentGroupState = treatmentGroupStateService.saveTreatmentGroupState(treatmentGroupState);
		Assert.assertNotNull(treatmentGroupState.getId());
	}
	
	@Test
	@Transactional
	public void saveTreatmentGroupStatesFromJson() {
		String json = "[{\"treatmentGroup\":{\"analysisGroups\":[{\"codeName\":\"AG-00342416\",\"deleted\":false,\"id\":499698,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142896894,\"version\":0}],\"codeName\":\"TG-00076401\",\"deleted\":false,\"id\":499710,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsStates\":[{\"comments\":null,\"deleted\":false,\"id\":685925,\"ignored\":false,\"lsKind\":\"test compound treatment\",\"lsTransaction\":1368,\"lsType\":\"data\",\"lsTypeAndKind\":\"data_test compound treatment\",\"lsValues\":[{\"analysisGroupId\":0,\"clobValue\":null,\"codeKind\":null,\"codeOrigin\":null,\"codeType\":null,\"codeTypeAndKind\":\"null_null\",\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"deleted\":false,\"fileValue\":null,\"id\":2284749,\"ignored\":false,\"lsKind\":\"Dose\",\"lsTransaction\":1368,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_Dose\",\"modifiedBy\":null,\"modifiedDate\":null,\"numberOfReplicates\":null,\"numericValue\":20,\"operatorKind\":null,\"operatorType\":null,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142898184,\"sigFigs\":null,\"stateId\":685925,\"stateKind\":\"test compound treatment\",\"stateType\":\"data\",\"stringValue\":null,\"treatmentGroupId\":499710,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":\"uM\",\"unitType\":null,\"unitTypeAndKind\":\"null_uM\",\"urlValue\":null,\"version\":0}],\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142898182,\"version\":0},{\"comments\":null,\"deleted\":false,\"id\":685926,\"ignored\":false,\"lsKind\":\"results\",\"lsTransaction\":1368,\"lsType\":\"data\",\"lsTypeAndKind\":\"data_results\",\"lsValues\":[{\"analysisGroupId\":0,\"clobValue\":null,\"codeKind\":null,\"codeOrigin\":null,\"codeType\":null,\"codeTypeAndKind\":\"null_null\",\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"deleted\":false,\"fileValue\":null,\"id\":2284750,\"ignored\":false,\"lsKind\":\"Response\",\"lsTransaction\":1368,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_Response\",\"modifiedBy\":null,\"modifiedDate\":null,\"numberOfReplicates\":null,\"numericValue\":93.85875,\"operatorKind\":null,\"operatorType\":null,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142898187,\"sigFigs\":null,\"stateId\":685926,\"stateKind\":\"results\",\"stateType\":\"data\",\"stringValue\":null,\"treatmentGroupId\":499710,\"uncertainty\":3.59397712615611,\"uncertaintyType\":\"standard deviation\",\"unitKind\":\"efficacy\",\"unitType\":null,\"unitTypeAndKind\":\"null_efficacy\",\"urlValue\":null,\"version\":0}],\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142898186,\"version\":0}],\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142898181,\"subjects\":[{\"codeName\":\"SUBJ-00236004\",\"deleted\":false,\"id\":499830,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142900029,\"version\":0},{\"codeName\":\"SUBJ-00236024\",\"deleted\":false,\"id\":499850,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142900482,\"version\":0},{\"codeName\":\"SUBJ-00236014\",\"deleted\":false,\"id\":499840,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142900255,\"version\":0},{\"codeName\":\"SUBJ-00236034\",\"deleted\":false,\"id\":499860,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142900674,\"version\":0}],\"version\":0},\"lsValues\":null,\"recordedBy\":\"userName\",\"lsType\":\"data\",\"lsKind\":\"results\",\"comments\":\"\",\"lsTransaction\":3067,\"ignored\":false,\"recordedDate\":1415752381000},{\"treatmentGroup\":{\"analysisGroups\":[{\"codeName\":\"AG-00342416\",\"deleted\":false,\"id\":499698,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142896894,\"version\":0}],\"codeName\":\"TG-00076401\",\"deleted\":false,\"id\":499710,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsStates\":[{\"comments\":null,\"deleted\":false,\"id\":685925,\"ignored\":false,\"lsKind\":\"test compound treatment\",\"lsTransaction\":1368,\"lsType\":\"data\",\"lsTypeAndKind\":\"data_test compound treatment\",\"lsValues\":[{\"analysisGroupId\":0,\"clobValue\":null,\"codeKind\":null,\"codeOrigin\":null,\"codeType\":null,\"codeTypeAndKind\":\"null_null\",\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"deleted\":false,\"fileValue\":null,\"id\":2284749,\"ignored\":false,\"lsKind\":\"Dose\",\"lsTransaction\":1368,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_Dose\",\"modifiedBy\":null,\"modifiedDate\":null,\"numberOfReplicates\":null,\"numericValue\":20,\"operatorKind\":null,\"operatorType\":null,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142898184,\"sigFigs\":null,\"stateId\":685925,\"stateKind\":\"test compound treatment\",\"stateType\":\"data\",\"stringValue\":null,\"treatmentGroupId\":499710,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":\"uM\",\"unitType\":null,\"unitTypeAndKind\":\"null_uM\",\"urlValue\":null,\"version\":0}],\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142898182,\"version\":0},{\"comments\":null,\"deleted\":false,\"id\":685926,\"ignored\":false,\"lsKind\":\"results\",\"lsTransaction\":1368,\"lsType\":\"data\",\"lsTypeAndKind\":\"data_results\",\"lsValues\":[{\"analysisGroupId\":0,\"clobValue\":null,\"codeKind\":null,\"codeOrigin\":null,\"codeType\":null,\"codeTypeAndKind\":\"null_null\",\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"deleted\":false,\"fileValue\":null,\"id\":2284750,\"ignored\":false,\"lsKind\":\"Response\",\"lsTransaction\":1368,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_Response\",\"modifiedBy\":null,\"modifiedDate\":null,\"numberOfReplicates\":null,\"numericValue\":93.85875,\"operatorKind\":null,\"operatorType\":null,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142898187,\"sigFigs\":null,\"stateId\":685926,\"stateKind\":\"results\",\"stateType\":\"data\",\"stringValue\":null,\"treatmentGroupId\":499710,\"uncertainty\":3.59397712615611,\"uncertaintyType\":\"standard deviation\",\"unitKind\":\"efficacy\",\"unitType\":null,\"unitTypeAndKind\":\"null_efficacy\",\"urlValue\":null,\"version\":0}],\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142898186,\"version\":0}],\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142898181,\"subjects\":[{\"codeName\":\"SUBJ-00236004\",\"deleted\":false,\"id\":499830,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142900029,\"version\":0},{\"codeName\":\"SUBJ-00236024\",\"deleted\":false,\"id\":499850,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142900482,\"version\":0},{\"codeName\":\"SUBJ-00236014\",\"deleted\":false,\"id\":499840,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142900255,\"version\":0},{\"codeName\":\"SUBJ-00236034\",\"deleted\":false,\"id\":499860,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142900674,\"version\":0}],\"version\":0},\"lsValues\":null,\"recordedBy\":\"userName\",\"lsType\":\"data\",\"lsKind\":\"results\",\"comments\":\"\",\"lsTransaction\":3067,\"ignored\":false,\"recordedDate\":1415752381000},{\"treatmentGroup\":{\"analysisGroups\":[{\"codeName\":\"AG-00342416\",\"deleted\":false,\"id\":499698,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142896894,\"version\":0}],\"codeName\":\"TG-00076401\",\"deleted\":false,\"id\":499710,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[],\"lsStates\":[{\"comments\":null,\"deleted\":false,\"id\":685925,\"ignored\":false,\"lsKind\":\"test compound treatment\",\"lsTransaction\":1368,\"lsType\":\"data\",\"lsTypeAndKind\":\"data_test compound treatment\",\"lsValues\":[{\"analysisGroupId\":0,\"clobValue\":null,\"codeKind\":null,\"codeOrigin\":null,\"codeType\":null,\"codeTypeAndKind\":\"null_null\",\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"deleted\":false,\"fileValue\":null,\"id\":2284749,\"ignored\":false,\"lsKind\":\"Dose\",\"lsTransaction\":1368,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_Dose\",\"modifiedBy\":null,\"modifiedDate\":null,\"numberOfReplicates\":null,\"numericValue\":20,\"operatorKind\":null,\"operatorType\":null,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142898184,\"sigFigs\":null,\"stateId\":685925,\"stateKind\":\"test compound treatment\",\"stateType\":\"data\",\"stringValue\":null,\"treatmentGroupId\":499710,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":\"uM\",\"unitType\":null,\"unitTypeAndKind\":\"null_uM\",\"urlValue\":null,\"version\":0}],\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142898182,\"version\":0},{\"comments\":null,\"deleted\":false,\"id\":685926,\"ignored\":false,\"lsKind\":\"results\",\"lsTransaction\":1368,\"lsType\":\"data\",\"lsTypeAndKind\":\"data_results\",\"lsValues\":[{\"analysisGroupId\":0,\"clobValue\":null,\"codeKind\":null,\"codeOrigin\":null,\"codeType\":null,\"codeTypeAndKind\":\"null_null\",\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"deleted\":false,\"fileValue\":null,\"id\":2284750,\"ignored\":false,\"lsKind\":\"Response\",\"lsTransaction\":1368,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_Response\",\"modifiedBy\":null,\"modifiedDate\":null,\"numberOfReplicates\":null,\"numericValue\":93.85875,\"operatorKind\":null,\"operatorType\":null,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142898187,\"sigFigs\":null,\"stateId\":685926,\"stateKind\":\"results\",\"stateType\":\"data\",\"stringValue\":null,\"treatmentGroupId\":499710,\"uncertainty\":3.59397712615611,\"uncertaintyType\":\"standard deviation\",\"unitKind\":\"efficacy\",\"unitType\":null,\"unitTypeAndKind\":\"null_efficacy\",\"urlValue\":null,\"version\":0}],\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142898186,\"version\":0}],\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142898181,\"subjects\":[{\"codeName\":\"SUBJ-00236004\",\"deleted\":false,\"id\":499830,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142900029,\"version\":0},{\"codeName\":\"SUBJ-00236024\",\"deleted\":false,\"id\":499850,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142900482,\"version\":0},{\"codeName\":\"SUBJ-00236014\",\"deleted\":false,\"id\":499840,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142900255,\"version\":0},{\"codeName\":\"SUBJ-00236034\",\"deleted\":false,\"id\":499860,\"ignored\":false,\"lsKind\":\"default\",\"lsTransaction\":1368,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"smeyer\",\"recordedDate\":1408142900674,\"version\":0}],\"version\":0},\"lsValues\":null,\"recordedBy\":\"userName\",\"lsType\":\"data\",\"lsKind\":\"results\",\"comments\":\"\",\"lsTransaction\":3067,\"ignored\":false,\"recordedDate\":1415752381000}]";
		Collection<TreatmentGroupState> treatmentGroupStates = TreatmentGroupState.fromJsonArrayToTreatmentGroupStates(json);
		treatmentGroupStates = treatmentGroupStateService.saveTreatmentGroupStates(treatmentGroupStates);
		for (TreatmentGroupState treatmentGroupState: treatmentGroupStates) {
			Assert.assertNotNull(treatmentGroupState.getId());
		}
	}
}
