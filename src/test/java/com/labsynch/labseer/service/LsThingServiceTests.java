
package com.labsynch.labseer.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.ItxLsThingLsThingState;
import com.labsynch.labseer.domain.ItxLsThingLsThingValue;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.domain.ThingKind;
import com.labsynch.labseer.domain.ThingType;
import com.labsynch.labseer.dto.CodeTypeKindDTO;
import com.labsynch.labseer.dto.DependencyCheckDTO;
import com.labsynch.labseer.dto.LsThingValidationDTO;
import com.labsynch.labseer.dto.PreferredNameDTO;
import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.dto.StoichiometryPropertiesResultsDTO;
import com.labsynch.labseer.dto.ValuePathDTO;
import com.labsynch.labseer.dto.ValueRuleDTO;
import com.labsynch.labseer.exceptions.LsThingValidationErrorMessage;
import com.labsynch.labseer.exceptions.UniqueNameException;
import com.labsynch.labseer.service.ChemStructureService.SearchType;
import com.labsynch.labseer.utils.ExcludeNulls;
import com.labsynch.labseer.utils.PropertiesUtilService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONSerializer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml" })
@Configurable
public class LsThingServiceTests {

	private static final Logger logger = LoggerFactory.getLogger(LsThingServiceTests.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Autowired
	private LsThingService lsThingService;

	@Autowired
	private AutoLabelService autoLabelService;

	@Transactional
	@Rollback(value = false)
	public LsThing createLsThingStack() {
		// register types and kinds
		ThingType thingType1 = new ThingType();
		thingType1.setTypeName("thing");
		thingType1.persist();
		ThingKind thingKind1 = new ThingKind();
		thingKind1.setLsType(thingType1);
		thingKind1.setKindName("test thing");
		thingKind1.persist();

		LsThing thing1 = new LsThing();
		thing1.setCodeName("THING-01");
		thing1.setLsType("thing");
		thing1.setLsKind("test thing");
		thing1.setRecordedBy("bfielder");
		thing1.setRecordedDate(new Date());
		thing1.setDeleted(false);
		thing1.setIgnored(false);
		thing1.persist();

		LsThing thing2 = new LsThing();
		thing2.setCodeName("THING-02");
		thing2.setLsType("thing");
		thing2.setLsKind("test thing");
		thing2.setRecordedBy("bfielder");
		thing2.setRecordedDate(new Date());
		thing2.setDeleted(false);
		thing2.setIgnored(false);
		thing2.persist();

		LsThing thing3 = new LsThing();
		thing3.setCodeName("THING-03");
		thing3.setLsType("thing");
		thing3.setLsKind("test thing");
		thing3.setRecordedBy("bfielder");
		thing3.setRecordedDate(new Date());
		thing3.setDeleted(false);
		thing3.setIgnored(false);
		thing3.persist();

		ItxLsThingLsThing inc12 = makeItxLsThingLsThing("ITXTHING-01", "incorporates", "assembly_component", thing1,
				thing2);
		thing1.getSecondLsThings().add(inc12);
		inc12.persist();
		thing1.merge();

		return thing1;

	}

	public LsThing createTransientLsThingStack() {
		LsThing thing1 = new LsThing();
		thing1.setCodeName("THING-01");
		thing1.setLsType("thing");
		thing1.setLsKind("test thing");
		thing1.setRecordedBy("bfielder");
		thing1.setRecordedDate(new Date());
		thing1.setDeleted(false);
		thing1.setIgnored(false);

		LsThing thing2 = new LsThing();
		thing2.setCodeName("THING-02");
		thing2.setLsType("thing");
		thing2.setLsKind("test thing");
		thing2.setRecordedBy("bfielder");
		thing2.setRecordedDate(new Date());
		thing2.setDeleted(false);
		thing2.setIgnored(false);

		LsThing thing3 = new LsThing();
		thing3.setCodeName("THING-03");
		thing3.setLsType("thing");
		thing3.setLsKind("test thing");
		thing3.setRecordedBy("bfielder");
		thing3.setRecordedDate(new Date());
		thing3.setDeleted(false);
		thing3.setIgnored(false);

		ItxLsThingLsThing inc12 = makeItxLsThingLsThing("ITXTHING-01", "incorporates", "assembly_component", thing1,
				thing2);
		thing1.getSecondLsThings().add(inc12);

		return thing1;

	}

	public ItxLsThingLsThing makeItxLsThingLsThing(String codeName, String lsType, String lsKind, LsThing firstLsThing,
			LsThing secondLsThing) {
		ItxLsThingLsThing itx = new ItxLsThingLsThing();
		itx.setCodeName(codeName);
		itx.setLsType(lsType);
		itx.setLsKind(lsKind);
		itx.setRecordedBy("bfielder");
		itx.setRecordedDate(new Date());
		itx.setFirstLsThing(firstLsThing);
		itx.setSecondLsThing(secondLsThing);
		ItxLsThingLsThingState itxState = new ItxLsThingLsThingState();
		itxState.setLsType("metadata");
		itxState.setLsKind("composition");
		itxState.setItxLsThingLsThing(itx);
		itxState.setRecordedBy("bfielder");
		itxState.setRecordedDate(new Date());
		ItxLsThingLsThingValue itxValue = new ItxLsThingLsThingValue();
		itxValue.setLsType("numericValue");
		itxValue.setLsKind("order");
		itxValue.setRecordedBy("bfielder");
		itxValue.setRecordedDate(new Date());
		itxValue.setNumericValue(new BigDecimal(1));
		itxValue.setLsState(itxState);
		ItxLsThingLsThingValue itxValue2 = new ItxLsThingLsThingValue();
		itxValue2.setLsType("numericValue");
		itxValue2.setLsKind("pegylated");
		itxValue2.setRecordedBy("bfielder");
		itxValue2.setRecordedDate(new Date());
		itxValue2.setNumericValue(new BigDecimal(50));
		itxValue2.setLsState(itxState);
		Set<ItxLsThingLsThingState> itxStates = new HashSet<ItxLsThingLsThingState>();
		Set<ItxLsThingLsThingValue> itxValues = new HashSet<ItxLsThingLsThingValue>();
		itxValues.add(itxValue);
		itxValues.add(itxValue2);
		itxState.setLsValues(itxValues);
		itxStates.add(itxState);
		itx.setLsStates(itxStates);
		return itx;
	}

	@Transactional
	@Rollback(value = false)
	public void deleteLsThingStack() {
		try {
			ItxLsThingLsThing inc12 = ItxLsThingLsThing.findItxLsThingLsThingsByCodeNameEquals("ITXTHING-01")
					.getSingleResult();
			for (ItxLsThingLsThingState state : inc12.getLsStates()) {
				for (ItxLsThingLsThingValue value : state.getLsValues()) {
					value.remove();
				}
				state.remove();
			}
			inc12.remove();
		} catch (Exception e) {
		}

		try {
			ItxLsThingLsThing inc13 = ItxLsThingLsThing.findItxLsThingLsThingsByCodeNameEquals("ITXTHING-02")
					.getSingleResult();
			for (ItxLsThingLsThingState state : inc13.getLsStates()) {
				for (ItxLsThingLsThingValue value : state.getLsValues()) {
					value.remove();
				}
				state.remove();
			}
			inc13.remove();
		} catch (Exception e) {
		}
		try {
			LsThing thing1 = LsThing.findLsThingsByCodeNameEquals("THING-01").getSingleResult();
			thing1.remove();
		} catch (Exception e) {
		}
		try {
			LsThing thing2 = LsThing.findLsThingsByCodeNameEquals("THING-02").getSingleResult();
			thing2.remove();
		} catch (Exception e) {
		}
		try {
			LsThing thing3 = LsThing.findLsThingsByCodeNameEquals("THING-03").getSingleResult();
			thing3.remove();
		} catch (Exception e) {
		}
		try {
			ThingKind thingKind1 = ThingKind.findThingKindsByLsTypeAndKindEquals("thing_test thing").getSingleResult();
			thingKind1.remove();
		} catch (Exception e) {
		}
		try {
			ThingType thingType1 = ThingType.findThingTypesByTypeNameEquals("thing").getSingleResult();
			thingType1.remove();
		} catch (Exception e) {
		}
		// try{
		// }catch (Exception e){}

	}

	@Test
	public void buildAndRemoveLsThingStack() {
		try {
			LsThing thing1 = createLsThingStack();
		} catch (Exception e) {
			deleteLsThingStack();
		}
		deleteLsThingStack();
	}

	@Test
	@Transactional
	@Rollback(value = false)
	public void updateInteraction() {
		LsThing thing1 = createLsThingStack();
		LsThing thing2 = LsThing.findLsThingsByCodeNameEquals("THING-02").getSingleResult();
		LsThing thing3 = LsThing.findLsThingsByCodeNameEquals("THING-03").getSingleResult();
		ItxLsThingLsThing inc12 = ItxLsThingLsThing.findItxLsThingLsThingsByCodeNameEquals("ITXTHING-01")
				.getSingleResult();
		ItxLsThingLsThing inc13 = makeItxLsThingLsThing("ITXTHING-02", "incorporates", "assembly_component", thing1,
				thing3);
		inc12.setIgnored(true);
		thing1.getSecondLsThings().add(inc13);
		Assert.assertEquals(2, thing1.getSecondLsThings().size());
		for (ItxLsThingLsThing itx : thing1.getSecondLsThings()) {
			logger.info(itx.getCodeName());
			if (itx.getId() != null)
				logger.info("id: " + itx.getId().toString());
			else
				logger.info("id is null");
		}
		LsThing updatedThing1 = lsThingService.updateLsThing(thing1);
		logger.info(updatedThing1.toJsonWithNestedFull());
		Assert.assertEquals(2, updatedThing1.getSecondLsThings().size());
		for (ItxLsThingLsThing itx : updatedThing1.getSecondLsThings()) {
			Assert.assertEquals(1, itx.getLsStates().size());
		}

		// deleteLsThingStack();
	}

	// @Test
	public void Test_1() {

		String json = "{\"requests\":[{\"requestName\":\"CRA-025995-1\"},{\"requestName\":\"CMPD-0000052-01\"}]}";
		PreferredNameDTO request = PreferredNameDTO.fromJsonToPreferredNameDTO(json);

		logger.info("#############################################");

	}

	// @Test
	public void Test_2() {

		String json = "{\"requests\":[{\"requestName\":\"CRA-025995-1\"},{\"requestName\":\"9\"}]}";

		PreferredNameResultsDTO results = lsThingService.getGeneCodeNameFromName(json);
		logger.info("#############################################");
		logger.info("results: " + results.toJson());

	}

	@Test
	public void Test_3() {

		String json = "{\"requests\":[{\"requestName\":\"1\"}, {\"requestName\":\"2\"}, {\"requestName\":\"15\"}, {\"requestName\":\"blah\"}]}";

		String thingType = "gene";
		String thingKind = "entrez gene";
		String labelType = "name";
		String labelKind = "Entrez Gene ID";
		logger.info("getGeneCodeNameFromNameRequest incoming json: " + json);
		PreferredNameResultsDTO results = lsThingService.getPreferredNameFromName(thingType, thingKind, labelType,
				labelKind, json);
		logger.info("#############################################");
		logger.info("results: " + results.toJson());

	}

	@Test
	@Transactional
	public void findItxLsThingLsThingsByLsTypeEqualsAndLsKindEqualsAndFirstLsThingEquals() {
		// TODO
	}

	@Test
	@Transactional
	public void findItxLsThingLsThingsByLsTypeEqualsAndLsKindEqualsAndSecondLsThingEquals() {
		// TODO
	}

	@Test
	@Transactional
	public void retrieveOrder() {
		// TODO
	}

	@Test
	@Transactional
	public void validateComponent() {
		// TODO
	}

	@Test
	@Transactional
	@Rollback(value = false)
	public void buildLsThingStack() {
		createLsThingStack();
	}

	@Test
	@Transactional
	@Rollback(value = false)
	public void validateAssembly() {
		LsThing thing2 = createTransientLsThingStack();
		ValuePathDTO pathDTO = new ValuePathDTO();
		pathDTO.setEntity("ItxLsThingLsThing");
		pathDTO.setEntityType("incorporates");
		pathDTO.setEntityKind("assembly_component");
		pathDTO.setStateType("metadata");
		pathDTO.setStateKind("composition");
		pathDTO.setValueType("numericValue");
		pathDTO.setValueKind("pegylated");
		ValueRuleDTO valueRule = new ValueRuleDTO();
		valueRule.setComparisonMethod("numeric exact match");
		valueRule.setValue(pathDTO);
		Set<ValueRuleDTO> valueRules = new HashSet<ValueRuleDTO>();
		valueRules.add(valueRule);
		LsThingValidationDTO validationDTO = new LsThingValidationDTO();
		validationDTO.setLsThing(thing2);
		validationDTO.setUniqueName(true);
		validationDTO.setUniqueInteractions(true);
		validationDTO.setOrderMatters(true);
		validationDTO.setForwardAndReverseAreSame(true);
		validationDTO.setValueRules(valueRules);
		ArrayList<LsThingValidationErrorMessage> errorMessages = lsThingService.validateLsThing(validationDTO);
		Assert.assertTrue(errorMessages.isEmpty());
	}

	@Test
	public void saveLSMParent() throws UniqueNameException {
		String json = "{\"codeName\":\"LSM000001\",\"ignored\":false,\"lsKind\":\"linker small molecule\",\"lsLabels\":[{\"ignored\":false,\"imageFile\":null,\"labelText\":\"LSM\",\"lsKind\":\"linker small molecule\",\"lsTransaction\":1,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_linker small molecule\",\"modifiedDate\":null,\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"jane\",\"recordedDate\":1375141504000}],\"lsStates\":[{\"comments\":null,\"ignored\":false,\"lsKind\":\"linker small molecule parent\",\"lsTransaction\":1,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_linker small molecule parent\",\"lsValues\":[{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":1342080000000,\"fileValue\":null,\"ignored\":false,\"lsTransaction\":128,\"modifiedDate\":null,\"numericValue\":null,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":null,\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"completion date\",\"valueOperator\":null,\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"valueUnit\":null},{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"ignored\":false,\"lsTransaction\":127,\"modifiedDate\":null,\"numericValue\":null,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":\"Notebook 1\",\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"notebook\",\"valueOperator\":null,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"valueUnit\":null},{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"ignored\":false,\"lsTransaction\":128,\"modifiedDate\":null,\"numericValue\":231,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":null,\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"molecular weight\",\"valueOperator\":null,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_molecular weight\",\"unitKind\":\"g/mol\",\"unitType\":\"molecular weight\",\"valueUnit\":null},{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"ignored\":false,\"lsTransaction\":128,\"modifiedDate\":null,\"numericValue\":0,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":null,\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"batch number\",\"valueOperator\":null,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_batch number\",\"unitKind\":null,\"unitType\":null,\"valueUnit\":null}],\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jane\",\"recordedDate\":1375141460000}],\"lsTransaction\":1,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_linker small molecule\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jane\",\"recordedDate\":1375141508000,\"shortDescription\":\" \"}";
		LsThing lsThing = LsThing.fromJsonToLsThing(json);
		lsThing = lsThingService.saveLsThing(lsThing, false);
	}

	// @Test
	// @Transactional
	// public void generateBatchCodeName(){
	// LsThing parent =
	// LsThing.findLsThingsByCodeNameEquals("LSM000001").getSingleResult();
	// String batchCodeName = lsThingService.generateBatchCodeName(parent);
	// Assert.assertEquals("LSM000001-1", batchCodeName);
	// }
	//
	// @Test
	// public void saveLSMBatch() throws UniqueNameException{
	// String json =
	// "{\"codeName\":null,\"id\":11,\"ignored\":false,\"lsKind\":\"linker small
	// molecule\",\"lsStates\":[{\"comments\":null,\"id\":11,\"ignored\":false,\"lsKind\":\"linker
	// small molecule
	// batch\",\"lsTransaction\":1,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_linker
	// small molecule
	// batch\",\"lsValues\":[{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":1342080000000,\"fileValue\":null,\"id\":12,\"ignored\":false,\"lsTransaction\":128,\"modifiedDate\":null,\"numericValue\":null,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":null,\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"completion
	// date\",\"valueOperator\":null,\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion
	// date\",\"valueUnit\":null,\"version\":0},{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"id\":13,\"ignored\":false,\"lsTransaction\":127,\"modifiedDate\":null,\"numericValue\":null,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":\"Notebook
	// 1\",\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"notebook\",\"valueOperator\":null,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"valueUnit\":null,\"version\":0}],\"recordedDate\":1363388477000,\"recordedBy\":\"jane\"},{\"comments\":null,\"id\":111,\"ignored\":false,\"lsKind\":\"inventory\",\"lsTransaction\":1,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"id\":14,\"ignored\":false,\"lsTransaction\":128,\"modifiedDate\":null,\"numericValue\":2.3,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":null,\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"amount\",\"valueOperator\":null,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount\",\"unitKind\":\"g\",\"unitType\":\"mass\",\"valueUnit\":null,\"version\":0},{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"id\":15,\"ignored\":false,\"lsTransaction\":127,\"modifiedDate\":null,\"numericValue\":null,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":\"Cabinet
	// 1\",\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"location\",\"valueOperator\":null,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"valueUnit\":null,\"version\":0}],\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jane\",\"recordedDate\":1375141460000,\"version\":0}],\"lsTransaction\":1,\"lsType\":\"batch\",\"lsTypeAndKind\":\"batch_linker
	// small
	// molecule\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jane\",\"recordedDate\":1375141508000,\"shortDescription\":\"
	// \",\"version\":0}";
	// LsThing lsThing = LsThing.fromJsonToLsThing(json);
	// LsThing parent =
	// LsThing.findLsThingsByCodeNameEquals("LSM000001").getSingleResult();
	// lsThing.setCodeName(lsThingService.generateBatchCodeName(parent));
	// lsThing = lsThingService.saveLsThing(lsThing, false, true, parent.getId());
	// }

	@Test
	public void getComponentBatches() {
		LsThing parent = LsThing.findLsThingsByCodeNameEquals("LSM000001").getSingleResult();
		Collection<LsThing> batches = lsThingService.findBatchesByParentEquals(parent);
		Assert.assertEquals(1, batches.size());
	}

	@Test
	public void findLsThingLabels() {
		String queryString = "linker";
		Collection<LsThingLabel> lsThingLabels = LsThingLabel.findLsThingLabelsByLabelTextLike(queryString)
				.getResultList();
		logger.info(LsThingLabel.toJsonArray(lsThingLabels));
	}

	@Test
	public void findLsThingsByLsTypeAndKindEquals() {
		String lsTypeAndKind = "term_documentTerm";
		Collection<LsThing> results = LsThing.findLsThingsByLsTypeAndKindEquals(lsTypeAndKind).getResultList();
		Assert.assertEquals(1, results.size());
		LsThing lsThing = results.iterator().next();
		logger.info(lsThing.getId().toString());
		logger.info(lsThing.getCodeName());
		logger.info(LsThing.toJsonArray(results));
	}

	@Transactional
	@Test
	public void toFullJson() {
		LsThing lsThing = LsThing.findLsThingsByCodeNameEquals("PROT000006").getSingleResult();

		String json1 = new JSONSerializer().exclude("*.class", "lsStates.lsThing", "lsLabels.lsThing",
				"firstLsThings.secondLsThing", "secondLsThings.firstLsThing"
		// ,"firstLsThings.firstLsThing.firstLsThings",
		// "firstLsThings.firstLsThing.secondLsThings"
		)
				.include("lsLabels", "lsStates.lsValues", "secondLsThings", "firstLsThings"
				// ,"firstLsThings.firstLsThing"
				)
				.transform(new ExcludeNulls(), void.class)
				.serialize(lsThing);
		logger.info(json1);

		// logger.info(lsThing.toFullJson());
	}

	@Test
	@Transactional
	public void toJsonTest() {
		LsThing lsThing = LsThing.findLsThingsByCodeNameEquals("PROT000006-1").getSingleResult();

		String json = new JSONSerializer().exclude("*.class")
				.include("lsTags", "lsLabels", "lsStates.lsValues", "firstLsThings", "secondLsThings")
				.transform(new ExcludeNulls(), void.class).serialize(lsThing);

		logger.info(json);
	}

	// public String toFullJson() {
	// return new JSONSerializer().
	// exclude("*.class", "lsStates.analysisGroup", "lsLabels.analysisGroup",
	// "treatmentGroups.analysisGroup", "experiment.protocol")
	// .include("lsLabels", "lsStates.lsValues",
	// "treatmentGroups.lsStates.lsValues", "treatmentGroups.lsLabels",
	// "treatmentGroups.subjects.lsStates.lsValues",
	// "treatmentGroups.subjects.lsLabels")
	// .transform(new ExcludeNulls(), void.class).serialize(this);
	// }

	@Test
	@Transactional
	public void saveParentAndBatch() {
		// String json = "{\"codeName\": null,\"firstLsThings\": [{\"deleted\":
		// false,\"firstLsThing\": {\"codeName\": \"PRTN-000012-1\",\"deleted\":
		// false,\"ignored\": false,\"lsKind\": \"protein\",\"lsType\":
		// \"batch\",\"lsTypeAndKind\": \"batch_protein\",\"recordedBy\":
		// \"bob\",\"recordedDate\": 1424730027913,\"version\": null},\"id\":
		// null,\"ignored\": false,\"lsKind\": \"batch_parent\",\"lsType\":
		// \"instantiates\",\"lsTypeAndKind\":
		// \"instantiates_batch_parent\",\"recordedBy\": \"bob\",\"recordedDate\":
		// 1424730027913,\"version\": null}],\"ignored\": false,\"lsKind\":
		// \"protein\",\"lsLabels\": [{ \"ignored\": false,\"imageFile\":
		// null,\"labelText\": \"Fielderase\",\"lsKind\": \"protein\",\"lsTransaction\":
		// 1,\"lsType\": \"name\",\"lsTypeAndKind\": \"name_protein\",\"modifiedDate\":
		// null,\"physicallyLabled\": false,\"preferred\": true,\"recordedBy\":
		// \"jane\",\"recordedDate\": 1375141504000}],\"lsStates\": [{ \"comments\":
		// null,\"ignored\": false,\"lsKind\": \"protein parent\",\"lsTransaction\":
		// 1,\"lsType\": \"metadata\",\"lsTypeAndKind\": \"metadata_protein
		// parent\",\"lsValues\": [{\"clobValue\": null, \"codeValue\": null,
		// \"comments\": null, \"dateValue\": 1342080000000, \"fileValue\": null,
		// \"ignored\": false, \"lsKind\": \"completion date\", \"lsTransaction\": 128,
		// \"lsType\": \"dateValue\", \"lsTypeAndKind\": \"dateValue_completion date\",
		// \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true,
		// \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null,
		// \"stringValue\": null, \"uncertainty\": null, \"urlValue\": null,
		// \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null,
		// \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\":
		// null, \"ignored\": false, \"lsKind\": \"notebook\", \"lsTransaction\": 127,
		// \"lsType\": \"stringValue\", \"lsTypeAndKind\": \"stringValue_notebook\",
		// \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true,
		// \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null,
		// \"stringValue\": \"Notebook 1\", \"uncertainty\": null, \"urlValue\": null,
		// \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null,
		// \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\":
		// null, \"ignored\": false, \"lsKind\": \"molecular weight\",
		// \"lsTransaction\": 128, \"lsType\": \"numericValue\", \"lsTypeAndKind\":
		// \"numericValue_molecular weight\", \"modifiedDate\": null, \"numericValue\":
		// 231, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\":
		// 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\":
		// null, \"unitKind\": \"g/mol\", \"unitType\": \"molecular weight\",
		// \"urlValue\": null, \"valueOperator\": null, \"valueUnit\":
		// null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null,
		// \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\":
		// \"batch number\", \"lsTransaction\": 128, \"lsType\": \"numericValue\",
		// \"lsTypeAndKind\": \"numericValue_batch number\", \"modifiedDate\": null,
		// \"numericValue\": 0, \"publicData\": true, \"recordedBy\": \"jane\",
		// \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null,
		// \"uncertainty\": null, \"unitKind\": null, \"unitType\": null, \"urlValue\":
		// null, \"valueOperator\": null, \"valueUnit\": null} ],\"modifiedBy\":
		// null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\":
		// 1375141460000}],\"lsTransaction\": 1,\"lsType\":
		// \"parent\",\"lsTypeAndKind\": \"parent_protein\",\"modifiedBy\":
		// null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\":
		// 1375141508000,\"shortDescription\": \" \"}";
		String json = "{\"codeName\": null,\"firstLsThings\": [{\"deleted\": false,\"firstLsThing\": {\"codeName\": \"PRTN-000013-1\",\"deleted\": false,\"ignored\": false,\"lsKind\": \"protein\",\"lsStates\":[{\"deleted\":false,\"ignored\":false,\"lsKind\":\"inventory\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"location\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729705505,\"stringValue\":\"123\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"amount made\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount made\",\"numericValue\":1123,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729706303,\"unitKind\":\"g\",\"unitType\":\"mass\",\"unitTypeAndKind\":\"mass_g\",\"version\":0}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"version\":1},{\"deleted\":false,\"ignored\":false,\"lsKind\":\"protein batch\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_protein batch\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"hplc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_hplc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeOrigin\":\"ACAS authors\",\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"egao\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"scientist\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702585,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"DeleteExperimentInstructions (6).pdf\",\"ignored\":false,\"lsKind\":\"gpc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_gpc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1424678400000,\"deleted\":false,\"ignored\":false,\"lsKind\":\"completion date\",\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703242,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"ms\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_ms\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeKind\":\"source\",\"codeOrigin\":\"ACAS DDICT\",\"codeType\":\"component\",\"codeTypeAndKind\":\"component_source\",\"codeValue\":\"ACAS\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"source\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_source\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729672870,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"purity\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_purity\",\"numericValue\":129,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729708062,\"unitKind\":\"% purity\",\"unitType\":\"percentage\",\"unitTypeAndKind\":\"percentage_% purity\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"nmr\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_nmr\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"notebook\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703953,\"stringValue\":\"test\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"source id\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_source id\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702610,\"stringValue\":\"\",\"unitTypeAndKind\":\"null_null\",\"version\":0}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"version\":1}],\"lsType\": \"batch\",\"lsTypeAndKind\": \"batch_protein\",\"recordedBy\": \"jane\",\"recordedDate\": 1424730027913,\"version\": null},\"id\": null,\"ignored\": false,\"lsKind\": \"batch_parent\",\"lsType\": \"instantiates\",\"lsTypeAndKind\": \"instantiates_batch_parent\",\"recordedBy\": \"jane\",\"recordedDate\": 1424730027913,\"version\": null}],\"ignored\": false,\"lsKind\": \"protein\",\"lsLabels\": [{ \"ignored\": false,\"imageFile\": null,\"labelText\": \"Fielderase\",\"lsKind\": \"protein\",\"lsTransaction\": 9999,\"lsType\": \"name\",\"lsTypeAndKind\": \"name_protein\",\"modifiedDate\": null,\"physicallyLabled\": false,\"preferred\": true,\"recordedBy\": \"jane\",\"recordedDate\": 1375141504000}],\"lsStates\": [{ \"comments\": null,\"ignored\": false,\"lsKind\": \"protein parent\",\"lsTransaction\": 9999,\"lsType\": \"metadata\",\"lsTypeAndKind\": \"metadata_protein parent\",\"lsValues\": [{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": 1342080000000, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"completion date\", \"lsTransaction\": 9999, \"lsType\": \"dateValue\", \"lsTypeAndKind\": \"dateValue_completion date\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"notebook\", \"lsTransaction\": 9999, \"lsType\": \"stringValue\", \"lsTypeAndKind\": \"stringValue_notebook\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": \"Notebook 1\", \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"molecular weight\", \"lsTransaction\": 9999, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_molecular weight\", \"modifiedDate\": null, \"numericValue\": 231, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": \"g/mol\", \"unitType\": \"molecular weight\", \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"batch number\", \"lsTransaction\": 9999, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_batch number\", \"modifiedDate\": null, \"numericValue\": 0, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": null, \"unitType\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null} ],\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141460000}],\"lsTransaction\": 9999,\"lsType\": \"parent\",\"lsTypeAndKind\": \"parent_protein\",\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141508000,\"shortDescription\": \" \"}";
		LsThing lsThing = LsThing.fromJsonToLsThing(json);
		try {
			LsThing savedLsThing = lsThingService.saveLsThing(lsThing);
			logger.info(savedLsThing.toJsonWithNestedFull());
		} catch (UniqueNameException e) {
			logger.error("unique name exception");
		}
	}

	@Test
	@Transactional
	public void updateParentAndBatch() {
		String json = "{\"codeName\":\"PRTN000015\",\"deleted\":false,\"firstLsThings\":[{\"deleted\":false,\"firstLsThing\":{\"codeName\":\"PRTN000015-1\",\"deleted\":false,\"id\":1493852,\"ignored\":false,\"lsKind\":\"protein\",\"lsStates\":[{\"comments\":\"test comment 2\",\"deleted\":false,\"id\":2634316,\"ignored\":false,\"lsKind\":\"protein batch\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_protein batch\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"edited ms file value\",\"id\":5926370,\"ignored\":false,\"lsKind\":\"ms\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_ms\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"id\":5926365,\"ignored\":false,\"lsKind\":\"nmr\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_nmr\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"DeleteExperimentInstructions (6).pdf\",\"id\":5926369,\"ignored\":false,\"lsKind\":\"gpc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_gpc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1424678400000,\"deleted\":false,\"id\":5926368,\"ignored\":false,\"lsKind\":\"completion date\",\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703242,\"unitTypeAndKind\":\"null_null\"},{\"codeKind\":\"source\",\"codeOrigin\":\"ACAS DDICT\",\"codeType\":\"component\",\"codeTypeAndKind\":\"component_source\",\"codeValue\":\"ACAS Edited\",\"deleted\":false,\"id\":5926373,\"ignored\":false,\"lsKind\":\"source\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_source\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729672870,\"unitTypeAndKind\":\"null_null\"},{\"codeOrigin\":\"ACAS authors\",\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"egao\",\"deleted\":false,\"id\":5926367,\"ignored\":false,\"lsKind\":\"scientist\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702585,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926372,\"ignored\":false,\"lsKind\":\"purity\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_purity\",\"numericValue\":129,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729708062,\"unitKind\":\"% purity\",\"unitType\":\"percentage\",\"unitTypeAndKind\":\"percentage_% purity\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"id\":5926371,\"ignored\":false,\"lsKind\":\"hplc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_hplc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926364,\"ignored\":false,\"lsKind\":\"notebook\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703953,\"stringValue\":\"test\",\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926366,\"ignored\":false,\"lsKind\":\"source id\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_source id\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702610,\"stringValue\":\"test source id\",\"unitTypeAndKind\":\"null_null\"}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940},{\"deleted\":false,\"id\":2634315,\"ignored\":false,\"lsKind\":\"inventory\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926362,\"ignored\":false,\"lsKind\":\"location\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729705505,\"stringValue\":\"123\",\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926363,\"ignored\":false,\"lsKind\":\"amount made\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount made\",\"numericValue\":1123,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729706303,\"unitKind\":\"g\",\"unitType\":\"mass\",\"unitTypeAndKind\":\"mass_g\"}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940}],\"lsType\":\"batch\",\"lsTypeAndKind\":\"batch_protein\",\"recordedBy\":\"jane\",\"recordedDate\":1424730027913},\"id\":1493853,\"ignored\":false,\"lsKind\":\"batch_parent\",\"lsType\":\"instantiates\",\"lsTypeAndKind\":\"instantiates_batch_parent\",\"recordedBy\":\"jane\",\"recordedDate\":1424730027913}],\"id\":1493851,\"ignored\":false,\"lsKind\":\"protein\",\"lsLabels\":[{\"deleted\":false,\"id\":297595,\"ignored\":false,\"labelText\":\"Fielderase Z\",\"lsKind\":\"protein\",\"lsTransaction\":9999,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_protein\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"jane\",\"recordedDate\":1375141504000}],\"lsStates\":[{\"deleted\":false,\"id\":2634314,\"ignored\":false,\"lsKind\":\"protein parent\",\"lsTransaction\":9999,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_protein parent\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926359,\"ignored\":false,\"lsKind\":\"notebook\",\"lsTransaction\":9999,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"stringValue\":\"Notebook 1\",\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926361,\"ignored\":false,\"lsKind\":\"molecular weight\",\"lsTransaction\":9999,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_molecular weight\",\"numericValue\":890,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"unitKind\":\"g/mol\",\"unitType\":\"molecular weight\",\"unitTypeAndKind\":\"molecular weight_g/mol\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926360,\"ignored\":false,\"lsKind\":\"batch number\",\"lsTransaction\":9999,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_batch number\",\"numericValue\":0,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1342080000000,\"deleted\":false,\"id\":5926358,\"ignored\":false,\"lsKind\":\"completion date\",\"lsTransaction\":9999,\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"unitTypeAndKind\":\"null_null\"}],\"recordedBy\":\"jane\",\"recordedDate\":1375141460000}],\"lsTags\":[],\"lsTransaction\":9999,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_protein\",\"recordedBy\":\"jane\",\"recordedDate\":1375141508000,\"secondLsThings\":[]}";
		LsThing lsThing = LsThing.fromJsonToLsThing(json);
		LsThing savedLsThing = lsThingService.updateLsThing(lsThing);
		logger.info(savedLsThing.toJsonWithNestedFull());
	}

	@Test
	@Transactional
	public void toJsonWithNested() {
		// String json = "{\"codeName\": null,\"firstLsThings\": [{\"deleted\":
		// false,\"firstLsThing\": {\"codeName\": \"PRTN-000012-1\",\"deleted\":
		// false,\"ignored\": false,\"lsKind\": \"protein\",\"lsType\":
		// \"batch\",\"lsTypeAndKind\": \"batch_protein\",\"recordedBy\":
		// \"bob\",\"recordedDate\": 1424730027913,\"version\": null},\"id\":
		// null,\"ignored\": false,\"lsKind\": \"batch_parent\",\"lsType\":
		// \"instantiates\",\"lsTypeAndKind\":
		// \"instantiates_batch_parent\",\"recordedBy\": \"bob\",\"recordedDate\":
		// 1424730027913,\"version\": null}],\"ignored\": false,\"lsKind\":
		// \"protein\",\"lsLabels\": [{ \"ignored\": false,\"imageFile\":
		// null,\"labelText\": \"Fielderase\",\"lsKind\": \"protein\",\"lsTransaction\":
		// 1,\"lsType\": \"name\",\"lsTypeAndKind\": \"name_protein\",\"modifiedDate\":
		// null,\"physicallyLabled\": false,\"preferred\": true,\"recordedBy\":
		// \"jane\",\"recordedDate\": 1375141504000}],\"lsStates\": [{ \"comments\":
		// null,\"ignored\": false,\"lsKind\": \"protein parent\",\"lsTransaction\":
		// 1,\"lsType\": \"metadata\",\"lsTypeAndKind\": \"metadata_protein
		// parent\",\"lsValues\": [{\"clobValue\": null, \"codeValue\": null,
		// \"comments\": null, \"dateValue\": 1342080000000, \"fileValue\": null,
		// \"ignored\": false, \"lsKind\": \"completion date\", \"lsTransaction\": 128,
		// \"lsType\": \"dateValue\", \"lsTypeAndKind\": \"dateValue_completion date\",
		// \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true,
		// \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null,
		// \"stringValue\": null, \"uncertainty\": null, \"urlValue\": null,
		// \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null,
		// \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\":
		// null, \"ignored\": false, \"lsKind\": \"notebook\", \"lsTransaction\": 127,
		// \"lsType\": \"stringValue\", \"lsTypeAndKind\": \"stringValue_notebook\",
		// \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true,
		// \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null,
		// \"stringValue\": \"Notebook 1\", \"uncertainty\": null, \"urlValue\": null,
		// \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null,
		// \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\":
		// null, \"ignored\": false, \"lsKind\": \"molecular weight\",
		// \"lsTransaction\": 128, \"lsType\": \"numericValue\", \"lsTypeAndKind\":
		// \"numericValue_molecular weight\", \"modifiedDate\": null, \"numericValue\":
		// 231, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\":
		// 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\":
		// null, \"unitKind\": \"g/mol\", \"unitType\": \"molecular weight\",
		// \"urlValue\": null, \"valueOperator\": null, \"valueUnit\":
		// null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null,
		// \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\":
		// \"batch number\", \"lsTransaction\": 128, \"lsType\": \"numericValue\",
		// \"lsTypeAndKind\": \"numericValue_batch number\", \"modifiedDate\": null,
		// \"numericValue\": 0, \"publicData\": true, \"recordedBy\": \"jane\",
		// \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null,
		// \"uncertainty\": null, \"unitKind\": null, \"unitType\": null, \"urlValue\":
		// null, \"valueOperator\": null, \"valueUnit\": null} ],\"modifiedBy\":
		// null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\":
		// 1375141460000}],\"lsTransaction\": 1,\"lsType\":
		// \"parent\",\"lsTypeAndKind\": \"parent_protein\",\"modifiedBy\":
		// null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\":
		// 1375141508000,\"shortDescription\": \" \"}";
		String json = "{\"codeName\": null,\"firstLsThings\": [{\"deleted\": false,\"firstLsThing\": {\"codeName\": \"PRTN-000013-1\",\"deleted\": false,\"ignored\": false,\"lsKind\": \"protein\",\"lsStates\":[{\"deleted\":false,\"ignored\":false,\"lsKind\":\"inventory\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"location\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729705505,\"stringValue\":\"123\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"amount made\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount made\",\"numericValue\":1123,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729706303,\"unitKind\":\"g\",\"unitType\":\"mass\",\"unitTypeAndKind\":\"mass_g\",\"version\":0}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"version\":1},{\"deleted\":false,\"ignored\":false,\"lsKind\":\"protein batch\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_protein batch\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"hplc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_hplc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeOrigin\":\"ACAS authors\",\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"egao\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"scientist\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702585,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"DeleteExperimentInstructions (6).pdf\",\"ignored\":false,\"lsKind\":\"gpc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_gpc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1424678400000,\"deleted\":false,\"ignored\":false,\"lsKind\":\"completion date\",\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703242,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"ms\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_ms\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeKind\":\"source\",\"codeOrigin\":\"ACAS DDICT\",\"codeType\":\"component\",\"codeTypeAndKind\":\"component_source\",\"codeValue\":\"ACAS\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"source\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_source\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729672870,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"purity\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_purity\",\"numericValue\":129,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729708062,\"unitKind\":\"% purity\",\"unitType\":\"percentage\",\"unitTypeAndKind\":\"percentage_% purity\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"nmr\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_nmr\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"notebook\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703953,\"stringValue\":\"test\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"source id\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_source id\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702610,\"stringValue\":\"\",\"unitTypeAndKind\":\"null_null\",\"version\":0}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"version\":1}],\"lsType\": \"batch\",\"lsTypeAndKind\": \"batch_protein\",\"recordedBy\": \"jane\",\"recordedDate\": 1424730027913,\"version\": null},\"id\": null,\"ignored\": false,\"lsKind\": \"batch_parent\",\"lsType\": \"instantiates\",\"lsTypeAndKind\": \"instantiates_batch_parent\",\"recordedBy\": \"jane\",\"recordedDate\": 1424730027913,\"version\": null}],\"ignored\": false,\"lsKind\": \"protein\",\"lsLabels\": [{ \"ignored\": false,\"imageFile\": null,\"labelText\": \"Fielderase\",\"lsKind\": \"protein\",\"lsTransaction\": 9999,\"lsType\": \"name\",\"lsTypeAndKind\": \"name_protein\",\"modifiedDate\": null,\"physicallyLabled\": false,\"preferred\": true,\"recordedBy\": \"jane\",\"recordedDate\": 1375141504000}],\"lsStates\": [{ \"comments\": null,\"ignored\": false,\"lsKind\": \"protein parent\",\"lsTransaction\": 9999,\"lsType\": \"metadata\",\"lsTypeAndKind\": \"metadata_protein parent\",\"lsValues\": [{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": 1342080000000, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"completion date\", \"lsTransaction\": 9999, \"lsType\": \"dateValue\", \"lsTypeAndKind\": \"dateValue_completion date\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"notebook\", \"lsTransaction\": 9999, \"lsType\": \"stringValue\", \"lsTypeAndKind\": \"stringValue_notebook\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": \"Notebook 1\", \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"molecular weight\", \"lsTransaction\": 9999, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_molecular weight\", \"modifiedDate\": null, \"numericValue\": 231, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": \"g/mol\", \"unitType\": \"molecular weight\", \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"batch number\", \"lsTransaction\": 9999, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_batch number\", \"modifiedDate\": null, \"numericValue\": 0, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": null, \"unitType\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null} ],\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141460000}],\"lsTransaction\": 9999,\"lsType\": \"parent\",\"lsTypeAndKind\": \"parent_protein\",\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141508000,\"shortDescription\": \" \"}";
		LsThing lsThing = LsThing.fromJsonToLsThing(json);
		logger.info("JSON: " + lsThing.toJson());
		logger.info("JSON Stub: " + lsThing.toJsonStub());
		logger.info("JSON Nested with Stubs: " + lsThing.toJsonWithNestedStubs());
		logger.info("JSON Nested with Full: " + lsThing.toJsonWithNestedFull());
	}

	@Test
	@Transactional
	public void updateBatchInteractions() {
		String json = "{\"lsType\":\"batch\",\"lsKind\":\"internalization agent\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1428517111212,\"shortDescription\":\" \",\"lsLabels\":[],\"lsStates\":[{\"deleted\":false,\"id\":2649027,\"ignored\":false,\"lsKind\":\"internalization agent batch\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_internalization agent batch\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960432,\"ignored\":false,\"lsKind\":\"notebook\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"stringValue\":\"test\",\"unitTypeAndKind\":\"null_null\",\"version\":0,\"value\":\"test\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960435,\"ignored\":false,\"lsKind\":\"molecular weight\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_molecular weight\",\"numericValue\":123,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"unitKind\":\"kDa\",\"unitType\":\"molecular weight\",\"unitTypeAndKind\":\"molecular weight_kDa\",\"version\":0,\"value\":123},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960433,\"ignored\":true,\"lsKind\":\"purity\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_purity\",\"numericValue\":12,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"unitKind\":\"% purity\",\"unitType\":\"percentage\",\"unitTypeAndKind\":\"percentage_% purity\",\"version\":0,\"value\":121},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1428476400000,\"deleted\":false,\"id\":5960434,\"ignored\":false,\"lsKind\":\"completion date\",\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"unitTypeAndKind\":\"null_null\",\"version\":0,\"value\":1428476400000},{\"codeOrigin\":\"ACAS authors\",\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"egao\",\"deleted\":false,\"id\":5960438,\"ignored\":false,\"lsKind\":\"scientist\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"unitTypeAndKind\":\"null_null\",\"version\":0,\"value\":\"egao\"},{\"codeTypeAndKind\":\"null_null\",\"comments\":\"Testing (46).pdf\",\"deleted\":false,\"fileValue\":\"entities/internalizationAgents/I000012-6/Testing (46).pdf\",\"id\":5960439,\"ignored\":false,\"lsKind\":\"gpc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_gpc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"unitTypeAndKind\":\"null_null\",\"version\":1},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960436,\"ignored\":false,\"lsKind\":\"source id\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_source id\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"stringValue\":\"\",\"unitTypeAndKind\":\"null_null\",\"version\":0,\"value\":\"\"},{\"codeKind\":\"source\",\"codeOrigin\":\"ACAS DDICT\",\"codeType\":\"component\",\"codeTypeAndKind\":\"component_source\",\"codeValue\":\"ACAS\",\"deleted\":false,\"id\":5960437,\"ignored\":false,\"lsKind\":\"source\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_source\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"unitTypeAndKind\":\"null_null\",\"version\":0,\"value\":\"ACAS\"},{\"lsType\":\"numericValue\",\"lsKind\":\"purity\",\"ignored\":false,\"recordedDate\":1428517111212,\"recordedBy\":\"bob\",\"numericValue\":121,\"value\":121}],\"modifiedDate\":1428517031912,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"version\":2},{\"deleted\":false,\"id\":2649026,\"ignored\":false,\"lsKind\":\"inventory\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960430,\"ignored\":false,\"lsKind\":\"location\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"stringValue\":\"13\",\"unitTypeAndKind\":\"null_null\",\"version\":0,\"value\":\"13\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960431,\"ignored\":false,\"lsKind\":\"amount made\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount made\",\"numericValue\":23,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"unitKind\":\"g\",\"unitType\":\"mass\",\"unitTypeAndKind\":\"mass_g\",\"version\":0,\"value\":23}],\"modifiedDate\":1428517031934,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"version\":2}],\"codeName\":\"I000012-6\",\"deleted\":false,\"id\":1508706,\"ignored\":false,\"lsTags\":[],\"lsTypeAndKind\":\"batch_internalization agent\",\"modifiedDate\":1428517031909,\"version\":3,\"cid\":\"c346\",\"_changing\":false,\"_previousAttributes\":{\"lsType\":\"thing\",\"lsKind\":\"thing\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1428517037610,\"shortDescription\":\" \",\"lsLabels\":[],\"lsStates\":[],\"firstLsThings\":[]},\"changed\":{\"secondLsThings\":[]},\"_pending\":false,\"urlRoot\":\"/api/things/batch/internalization agent\",\"lsProperties\":{\"defaultLabels\":[],\"defaultValues\":[{\"key\":\"scientist\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent batch\",\"type\":\"codeValue\",\"kind\":\"scientist\",\"codeOrigin\":\"ACAS authors\",\"value\":\"unassigned\"},{\"key\":\"completion date\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent batch\",\"type\":\"dateValue\",\"kind\":\"completion date\"},{\"key\":\"notebook\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent batch\",\"type\":\"stringValue\",\"kind\":\"notebook\"},{\"key\":\"source\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent batch\",\"type\":\"codeValue\",\"kind\":\"source\",\"value\":\"ACAS\",\"codeType\":\"component\",\"codeKind\":\"source\",\"codeOrigin\":\"ACAS DDICT\"},{\"key\":\"source id\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent batch\",\"type\":\"stringValue\",\"kind\":\"source id\"},{\"key\":\"molecular weight\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent batch\",\"type\":\"numericValue\",\"kind\":\"molecular weight\",\"unitType\":\"molecular weight\",\"unitKind\":\"kDa\"},{\"key\":\"purity\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent batch\",\"type\":\"numericValue\",\"kind\":\"purity\",\"unitType\":\"percentage\",\"unitKind\":\"% purity\"},{\"key\":\"amount made\",\"stateType\":\"metadata\",\"stateKind\":\"inventory\",\"type\":\"numericValue\",\"kind\":\"amount made\",\"unitType\":\"mass\",\"unitKind\":\"g\"},{\"key\":\"location\",\"stateType\":\"metadata\",\"stateKind\":\"inventory\",\"type\":\"stringValue\",\"kind\":\"location\"}],\"defaultFirstLsThingItx\":[],\"defaultSecondLsThingItx\":[]},\"className\":\"Thing\",\"validationError\":null,\"idAttribute\":\"id\",\"firstLsThings\":[],\"secondLsThings\":[{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":1}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1428517111206,\"secondLsThing\":{\"codeName\":\"PEG000010-2\",\"deleted\":false,\"id\":1508673,\"ignored\":false,\"lsKind\":\"peg\",\"lsLabels\":[],\"lsStates\":[{\"deleted\":false,\"id\":2648998,\"ignored\":false,\"lsKind\":\"inventory\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960363,\"ignored\":false,\"lsKind\":\"location\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"stringValue\":\"lab23\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960364,\"ignored\":false,\"lsKind\":\"amount made\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount made\",\"numericValue\":123,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"unitKind\":\"g\",\"unitType\":\"mass\",\"unitTypeAndKind\":\"mass_g\",\"version\":0}],\"modifiedDate\":1428516368847,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"version\":7},{\"deleted\":false,\"id\":2648997,\"ignored\":false,\"lsKind\":\"peg batch\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_peg batch\",\"lsValues\":[{\"codeOrigin\":\"ACAS authors\",\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"egao\",\"deleted\":false,\"id\":5960362,\"ignored\":false,\"lsKind\":\"scientist\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960361,\"ignored\":false,\"lsKind\":\"purity\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_purity\",\"numericValue\":123,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"unitKind\":\"% purity\",\"unitType\":\"percentage\",\"unitTypeAndKind\":\"percentage_% purity\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1428390000000,\"deleted\":false,\"id\":5960360,\"ignored\":false,\"lsKind\":\"completion date\",\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960358,\"ignored\":false,\"lsKind\":\"notebook\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"stringValue\":\"test\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960357,\"ignored\":false,\"lsKind\":\"source id\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_source id\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"stringValue\":\"\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeKind\":\"source\",\"codeOrigin\":\"ACAS DDICT\",\"codeType\":\"component\",\"codeTypeAndKind\":\"component_source\",\"codeValue\":\"ACAS\",\"deleted\":false,\"id\":5960359,\"ignored\":false,\"lsKind\":\"source\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_source\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"comments\":\"Testing (45).pdf\",\"deleted\":false,\"fileValue\":\"entities/pegs/PEG000010-2/Testing (45).pdf\",\"id\":5960356,\"ignored\":false,\"lsKind\":\"gpc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_gpc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"unitTypeAndKind\":\"null_null\",\"version\":1}],\"modifiedDate\":1428516368853,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"version\":7}],\"lsTags\":[],\"lsType\":\"batch\",\"lsTypeAndKind\":\"batch_peg\",\"modifiedDate\":1428516368844,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"version\":8},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"},{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"deleted\":false,\"id\":2649029,\"ignored\":false,\"lsKind\":\"composition\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_composition\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960441,\"ignored\":false,\"lsKind\":\"order\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_order\",\"numericValue\":1,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"\",\"recordedDate\":1428517031733,\"unitTypeAndKind\":\"null_null\",\"version\":0}],\"recordedBy\":\"\",\"recordedDate\":1428517031732,\"version\":1}],\"recordedBy\":\"bob\",\"recordedDate\":1428517031598,\"secondLsThing\":{\"codeName\":\"PEG000010-1\",\"deleted\":false,\"id\":1442921,\"ignored\":false,\"lsKind\":\"peg\",\"lsType\":\"batch\",\"lsTypeAndKind\":\"batch_peg\",\"modifiedDate\":1428516368872,\"recordedBy\":\"bob\",\"recordedDate\":1426100366796,\"version\":14},\"deleted\":false,\"id\":1508708,\"ignored\":true,\"version\":1,\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"}]}";
		LsThing lsThing = LsThing.fromJsonToLsThing(json);
		LsThing updatedLsThing = lsThingService.updateLsThing(lsThing);
		logger.info(updatedLsThing.toJsonWithNestedFull());
		Assert.assertEquals(2, updatedLsThing.getSecondLsThings().size());
		Assert.assertEquals(1, updatedLsThing.getSecondLsThings().iterator().next().getLsStates().size());
	}

	@Test
	@Transactional
	public void sortLsThingsByCodeName() {
		createLsThingStack();
		LsThing thing1 = LsThing.findLsThingsByCodeNameEquals("THING-01").getSingleResult();
		LsThing thing2 = LsThing.findLsThingsByCodeNameEquals("THING-02").getSingleResult();
		LsThing thing3 = LsThing.findLsThingsByCodeNameEquals("THING-03").getSingleResult();

		List<LsThing> lsThings = new ArrayList<LsThing>();
		lsThings.add(thing2);
		lsThings.add(thing3);
		lsThings.add(thing1);
		logger.info(LsThing.toJsonArrayStub(lsThings));
		lsThingService.sortLsThingsByCodeName(lsThings);
		logger.info(LsThing.toJsonArrayStub(lsThings));
		int num = 1;
		for (LsThing lsThing : lsThings) {
			Assert.assertEquals("THING-0" + num, lsThing.getCodeName());
			num++;
		}
	}

	@Test
	@Transactional
	public void sortLsThingsByBatchNumber() {
		createLsThingStack();
		LsThing thing1 = LsThing.findLsThingsByCodeNameEquals("THING-01").getSingleResult();
		LsThing thing2 = LsThing.findLsThingsByCodeNameEquals("THING-02").getSingleResult();
		LsThing thing3 = LsThing.findLsThingsByCodeNameEquals("THING-03").getSingleResult();

		Collection<LsThing> lsThings = new ArrayList<LsThing>();
		lsThings.add(thing2);
		lsThings.add(thing3);
		lsThings.add(thing1);
		logger.info(LsThing.toJsonArrayStub(lsThings));
		lsThings = lsThingService.sortBatches(lsThings);
		logger.info(LsThing.toJsonArrayStub(lsThings));
		int num = 1;
		for (LsThing lsThing : lsThings) {
			Assert.assertEquals("THING-0" + num, lsThing.getCodeName());
			num++;
		}
	}

	@Transactional
	@Test
	public void lsThingBrowserSearchTestWithQuotes() {
		String query = "Ad 2";
		logger.info("Searching with the query: " + query);
		Collection<LsThing> resultLsThings = lsThingService.findLsThingsByGenericMetaDataSearch(query);
		// logger.info("Found: "+ resultLsThings.toString());
		logger.info("Number of results: " + resultLsThings.size());
		Assert.assertTrue(resultLsThings.size() > 1);
		query = "\"LSM 2\"";
		logger.info("Searching with the query: " + query);
		Collection<LsThing> resultLsThings2 = lsThingService.findLsThingsByGenericMetaDataSearch(query);
		// logger.info("Found: "+ resultLsThings.toString());
		logger.info("Number of results: " + resultLsThings2.size());
		Assert.assertTrue(resultLsThings2.size() < resultLsThings.size());
	}

	@Transactional
	@Test
	public void lsThingBrowserSearchByName() {
		// testing partial match and case sensitivity
		String query = "CDAP(PEG2k)20";
		logger.info("Searching with the query: " + query);
		Collection<LsThing> resultLsThings = lsThingService.findLsThingsByGenericMetaDataSearch(query);
		// logger.info("Found: "+ resultLsThings.toString());
		logger.info("Number of results: " + resultLsThings.size());
		Assert.assertTrue(resultLsThings.size() > 1);
		query = "CDAP";
		logger.info("Searching with the query: " + query);
		resultLsThings = lsThingService.findLsThingsByGenericMetaDataSearch(query);
		// logger.info("Found: "+ resultLsThings.toString());
		logger.info("Number of results: " + resultLsThings.size());
		Assert.assertTrue(resultLsThings.size() > 1);
		query = "cdap(peg2k)20";
		logger.info("Searching with the query: " + query);
		resultLsThings = lsThingService.findLsThingsByGenericMetaDataSearch(query);
		// logger.info("Found: "+ resultLsThings.toString());
		logger.info("Number of results: " + resultLsThings.size());
		Assert.assertTrue(resultLsThings.size() > 1);
		query = "cdap";
		logger.info("Searching with the query: " + query);
		resultLsThings = lsThingService.findLsThingsByGenericMetaDataSearch(query);
		// logger.info("Found: "+ resultLsThings.toString());
		logger.info("Number of results: " + resultLsThings.size());
		Assert.assertTrue(resultLsThings.size() > 1);
	}

	@Test
	public void getCodeNameFromCodeNameRequest() {
		PreferredNameRequestDTO requestDTO = new PreferredNameRequestDTO();
		Collection<PreferredNameDTO> requests = new HashSet<PreferredNameDTO>();
		String codeName = "LSM000001";
		requests.add(new PreferredNameDTO(codeName, null, null));
		requests.add(new PreferredNameDTO("NOT-A-CODE-NAME", null, null));
		requestDTO.setRequests(requests);
		PreferredNameResultsDTO results = lsThingService.getCodeNameFromName("parent", "linker small molecule",
				"codeName", "codeName", requestDTO);
		Assert.assertEquals(2, results.getResults().size());
		for (PreferredNameDTO result : results.getResults()) {
			if (result.getRequestName().equals(codeName)) {
				Assert.assertEquals(codeName, result.getPreferredName());
				Assert.assertEquals(codeName, result.getReferenceName());
			} else {
				Assert.assertEquals("", result.getPreferredName());
				Assert.assertEquals("", result.getReferenceName());
			}
		}
	}

	@Test
	public void getCodeNameFromCodeNameRequestNoLabelTypeKind() {
		PreferredNameRequestDTO requestDTO = new PreferredNameRequestDTO();
		Collection<PreferredNameDTO> requests = new HashSet<PreferredNameDTO>();
		String codeName = "LSM000001";
		requests.add(new PreferredNameDTO(codeName, null, null));
		requests.add(new PreferredNameDTO("NOT-A-CODE-NAME", null, null));
		requestDTO.setRequests(requests);
		PreferredNameResultsDTO results = lsThingService.getCodeNameFromName("parent", "linker small molecule", null,
				null, requestDTO);
		Assert.assertEquals(2, results.getResults().size());
		for (PreferredNameDTO result : results.getResults()) {
			if (result.getRequestName().equals(codeName)) {
				Assert.assertEquals(codeName, result.getPreferredName());
				Assert.assertEquals(codeName, result.getReferenceName());
			} else {
				Assert.assertEquals("", result.getPreferredName());
				Assert.assertEquals("", result.getReferenceName());
			}
		}
	}

	@Test
	@Transactional
	public void lsThingValueValidationTest_rna() {
		String json = "{\"lsThing\":{\"lsType\":\"parent\",\"lsKind\":\"rna\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1438041404017,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"rna\",\"labelText\":\"AHA1-12-none-testing eg may13 pp4(2)-ad19(1) test\",\"ignored\":false,\"preferred\":true,\"recordedDate\":null,\"recordedBy\":\"\",\"physicallyLabled\":false,\"imageFile\":null}],\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"rna parent\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"scientist\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeOrigin\":\"ACAS authors\",\"value\":\"bob\",\"codeValue\":\"bob\"},{\"lsType\":\"dateValue\",\"lsKind\":\"completion date\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":null,\"dateValue\":null},{\"lsType\":\"stringValue\",\"lsKind\":\"notebook\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"codeValue\",\"lsKind\":\"target transcript\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeKind\":\"target transcript\",\"codeType\":\"rna\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"AHA1\",\"value\":\"AHA1\"},{\"lsType\":\"numericValue\",\"lsKind\":\"gene position\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":12,\"numericValue\":12},{\"lsType\":\"codeValue\",\"lsKind\":\"backbone modification\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeKind\":\"backbone modification\",\"codeType\":\"rna\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"none\",\"value\":\"none\"},{\"lsType\":\"stringValue\",\"lsKind\":\"unmodified sequence passenger strand\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"aaa\",\"stringValue\":\"aaa\"},{\"lsType\":\"stringValue\",\"lsKind\":\"unmodified sequence guide strand\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"cc\",\"stringValue\":\"cc\"},{\"lsType\":\"stringValue\",\"lsKind\":\"modified sequence passenger strand\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"stringValue\",\"lsKind\":\"modified sequence guide strand\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"numericValue\",\"lsKind\":\"charge density\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"unitKind\":\"g/charge\",\"unitType\":\"charge density\",\"value\":514.1039999999999,\"numericValue\":514.1039999999999},{\"lsType\":\"numericValue\",\"lsKind\":\"batch number\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":0,\"value\":0}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"firstLsThings\":[],\"secondLsThings\":[{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":1},{\"lsType\":\"numericValue\",\"lsKind\":\"conjugation site\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":2}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1438041514575,\"secondLsThing\":{\"codeName\":\"PRTN000021\",\"deleted\":false,\"id\":1513844,\"ignored\":false,\"lsKind\":\"protein\",\"lsLabels\":[{\"deleted\":false,\"id\":298153,\"ignored\":false,\"labelText\":\"testing eg may13 pp4\",\"lsKind\":\"protein\",\"lsTransaction\":9128,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_protein\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1431546990102,\"version\":0}],\"lsTags\":[],\"lsTransaction\":9128,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_protein\",\"modifiedDate\":1437691401160,\"recordedBy\":\"bob\",\"recordedDate\":1431546990100,\"version\":22},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"},{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":2},{\"lsType\":\"numericValue\",\"lsKind\":\"conjugation site\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":1}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1438041514577,\"secondLsThing\":{\"codeName\":\"LSM000027\",\"deleted\":false,\"id\":1513681,\"ignored\":false,\"lsKind\":\"linker small molecule\",\"lsLabels\":[{\"deleted\":false,\"id\":298114,\"ignored\":false,\"labelText\":\"LSM19\",\"lsKind\":\"linker small molecule\",\"lsTransaction\":9110,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_linker small molecule\",\"modifiedDate\":1431112215563,\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1426019762681,\"version\":2}],\"lsTags\":[],\"lsTransaction\":9110,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_linker small molecule\",\"modifiedDate\":1431112215561,\"recordedBy\":\"bob\",\"recordedDate\":1431112215790,\"version\":5},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"}],\"cid\":\"c50\",\"_changing\":false,\"_previousAttributes\":{\"lsType\":\"thing\",\"lsKind\":\"thing\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1438041404017,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"rna\",\"labelText\":\"AHA1-12-none-testing eg may13 pp4(2)-ad19(1)\",\"ignored\":false,\"preferred\":true,\"recordedDate\":null,\"recordedBy\":\"\",\"physicallyLabled\":false,\"imageFile\":null}],\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"rna parent\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"scientist\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeOrigin\":\"ACAS authors\",\"value\":\"bob\",\"codeValue\":\"bob\"},{\"lsType\":\"dateValue\",\"lsKind\":\"completion date\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":null,\"dateValue\":null},{\"lsType\":\"stringValue\",\"lsKind\":\"notebook\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"codeValue\",\"lsKind\":\"target transcript\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeKind\":\"target transcript\",\"codeType\":\"rna\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"AHA1\",\"value\":\"AHA1\"},{\"lsType\":\"numericValue\",\"lsKind\":\"gene position\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":12,\"numericValue\":12},{\"lsType\":\"codeValue\",\"lsKind\":\"backbone modification\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeKind\":\"backbone modification\",\"codeType\":\"rna\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"none\",\"value\":\"none\"},{\"lsType\":\"stringValue\",\"lsKind\":\"unmodified sequence passenger strand\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"aaa\",\"stringValue\":\"aaa\"},{\"lsType\":\"stringValue\",\"lsKind\":\"unmodified sequence guide strand\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"cc\",\"stringValue\":\"cc\"},{\"lsType\":\"stringValue\",\"lsKind\":\"modified sequence passenger strand\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"stringValue\",\"lsKind\":\"modified sequence guide strand\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"numericValue\",\"lsKind\":\"charge density\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"unitKind\":\"g/charge\",\"unitType\":\"charge density\",\"value\":514.1039999999999,\"numericValue\":514.1039999999999},{\"lsType\":\"numericValue\",\"lsKind\":\"batch number\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":0,\"value\":0}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"firstLsThings\":[]},\"changed\":{\"secondLsThings\":[]},\"_pending\":false,\"urlRoot\":\"/api/things/parent/rna\",\"className\":\"RnaParent\",\"lsProperties\":{\"defaultLabels\":[{\"key\":\"rna name\",\"type\":\"name\",\"kind\":\"rna\",\"preferred\":true}],\"defaultValues\":[{\"key\":\"scientist\",\"stateType\":\"metadata\",\"stateKind\":\"rna parent\",\"type\":\"codeValue\",\"kind\":\"scientist\",\"codeOrigin\":\"ACAS authors\"},{\"key\":\"completion date\",\"stateType\":\"metadata\",\"stateKind\":\"rna parent\",\"type\":\"dateValue\",\"kind\":\"completion date\"},{\"key\":\"notebook\",\"stateType\":\"metadata\",\"stateKind\":\"rna parent\",\"type\":\"stringValue\",\"kind\":\"notebook\"},{\"key\":\"target transcript\",\"stateType\":\"metadata\",\"stateKind\":\"rna parent\",\"type\":\"codeValue\",\"kind\":\"target transcript\",\"codeType\":\"rna\",\"codeKind\":\"target transcript\",\"codeOrigin\":\"ACAS DDICT\",\"value\":\"unassigned\"},{\"key\":\"gene position\",\"stateType\":\"metadata\",\"stateKind\":\"rna parent\",\"type\":\"numericValue\",\"kind\":\"gene position\"},{\"key\":\"backbone modification\",\"stateType\":\"metadata\",\"stateKind\":\"rna parent\",\"type\":\"codeValue\",\"kind\":\"backbone modification\",\"codeType\":\"rna\",\"codeKind\":\"backbone modification\",\"codeOrigin\":\"ACAS DDICT\",\"value\":\"unassigned\"},{\"key\":\"unmodified sequence passenger strand\",\"stateType\":\"metadata\",\"stateKind\":\"rna parent\",\"type\":\"stringValue\",\"kind\":\"unmodified sequence passenger strand\"},{\"key\":\"unmodified sequence guide strand\",\"stateType\":\"metadata\",\"stateKind\":\"rna parent\",\"type\":\"stringValue\",\"kind\":\"unmodified sequence guide strand\"},{\"key\":\"modified sequence passenger strand\",\"stateType\":\"metadata\",\"stateKind\":\"rna parent\",\"type\":\"stringValue\",\"kind\":\"modified sequence passenger strand\"},{\"key\":\"modified sequence guide strand\",\"stateType\":\"metadata\",\"stateKind\":\"rna parent\",\"type\":\"stringValue\",\"kind\":\"modified sequence guide strand\"},{\"key\":\"charge density\",\"stateType\":\"metadata\",\"stateKind\":\"rna parent\",\"type\":\"numericValue\",\"kind\":\"charge density\",\"unitType\":\"charge density\",\"unitKind\":\"g/charge\"},{\"key\":\"batch number\",\"stateType\":\"metadata\",\"stateKind\":\"rna parent\",\"type\":\"numericValue\",\"kind\":\"batch number\",\"value\":0}],\"defaultFirstLsThingItx\":[],\"defaultSecondLsThingItx\":[]},\"validationError\":null,\"idAttribute\":\"id\"},\"uniqueName\":true,\"uniqueInteractions\":true,\"valueRules\":[{\"comparisonMethod\":\"numeric exact match\",\"comparisonRange\":null,\"value\":{\"entity\":\"ItxLsThingLsThing\",\"entityType\":\"incorporates\",\"entityKind\":\"assembly_component\",\"stateType\":\"metadata\",\"stateKind\":\"composition\",\"valueType\":\"numericValue\",\"valueKind\":\"conjugation site\"}}]}";
		LsThingValidationDTO validationDTO = LsThingValidationDTO.fromJsonToLsThingValidationDTO(json);
		Collection<LsThingValidationErrorMessage> errorMessages = lsThingService.validateLsThing(validationDTO);
		Assert.assertFalse(errorMessages.isEmpty());
		logger.info(LsThingValidationErrorMessage.toJsonArray(errorMessages));
	}

	@Test
	@Transactional
	public void lsThingValueValidationTest_formulation() {
		String json = "{\"lsThing\":{\"lsType\":\"parent\",\"lsKind\":\"formulation\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1438122372856,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"descriptive\",\"labelText\":\"testing eg feb 3 cbp10(1)-AHA1-12-none-testing eg may13 pp4(2)-ad19(1)-ad10-testing2(3%P)-Aminoethyl-PBA-PEG40(2%P)-testing eg feb 6 cbp 9-testing eg april15 peg5(4)-NP5 test\",\"ignored\":false,\"preferred\":true,\"recordedDate\":null,\"recordedBy\":\"\",\"physicallyLabled\":false,\"imageFile\":null},{\"lsType\":\"name\",\"lsKind\":\"component id\",\"labelText\":\"I000027(1)-R000030-L000002(3%P)-L000004(2%P)-P000043(4)-NP5 test\",\"ignored\":false,\"preferred\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"physicallyLabled\":false,\"imageFile\":null}],\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"formulation parent\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"scientist\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeOrigin\":\"ACAS authors\",\"value\":\"bob\",\"codeValue\":\"bob\"},{\"lsType\":\"dateValue\",\"lsKind\":\"completion date\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":null,\"dateValue\":null},{\"lsType\":\"stringValue\",\"lsKind\":\"notebook\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"codeValue\",\"lsKind\":\"project\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeKind\":\"project\",\"codeType\":\"formulation\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"AR-SV\",\"value\":\"AR-SV\"},{\"lsType\":\"numericValue\",\"lsKind\":\"total charge ratio\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":5,\"numericValue\":5},{\"lsType\":\"clobValue\",\"lsKind\":\"comments\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"clobValue\":\"\"},{\"lsType\":\"numericValue\",\"lsKind\":\"batch number\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":0,\"value\":0}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"firstLsThings\":[],\"secondLsThings\":[{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":1},{\"lsType\":\"numericValue\",\"lsKind\":\"ia siRNA mole ratio\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":1}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1438122449639,\"secondLsThing\":{\"codeName\":\"I000027\",\"deleted\":false,\"id\":1513475,\"ignored\":false,\"lsKind\":\"internalization agent\",\"lsLabels\":[{\"deleted\":false,\"id\":298079,\"ignored\":false,\"labelText\":\"testing eg feb 3 cbp10\",\"lsKind\":\"internalization agent\",\"lsTransaction\":8873,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_internalization agent\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1430431782315,\"version\":0}],\"lsTags\":[],\"lsTransaction\":8873,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_internalization agent\",\"modifiedDate\":1430755528275,\"recordedBy\":\"bob\",\"recordedDate\":1430771015624,\"version\":1},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"},{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":2},{\"lsType\":\"numericValue\",\"lsKind\":\"charge ratio\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":4}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1438122449641,\"secondLsThing\":{\"codeName\":\"P000043\",\"deleted\":false,\"id\":1514347,\"ignored\":false,\"lsKind\":\"polymer\",\"lsLabels\":[{\"deleted\":false,\"id\":298266,\"ignored\":false,\"labelText\":\"testing eg feb 6 cbp 9-testing eg april15 peg5\",\"lsKind\":\"polymer\",\"lsTransaction\":9326,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_polymer\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"egao\",\"recordedDate\":1432072358504,\"version\":0}],\"lsTags\":[],\"lsTransaction\":9326,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_polymer\",\"recordedBy\":\"egao\",\"recordedDate\":1432072358502,\"version\":1},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"},{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":3},{\"lsType\":\"numericValue\",\"lsKind\":\"pegylated\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":3}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1438122449642,\"secondLsThing\":{\"codeName\":\"L000002\",\"deleted\":false,\"id\":1512917,\"ignored\":false,\"lsKind\":\"linker\",\"lsLabels\":[{\"deleted\":false,\"id\":297912,\"ignored\":false,\"labelText\":\"LSM10-testing2\",\"lsKind\":\"linker\",\"lsType\":\"name\",\"lsTypeAndKind\":\"name_linker\",\"modifiedDate\":1430175416507,\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1429118074579,\"version\":2}],\"lsTags\":[],\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_linker\",\"modifiedDate\":1437592234459,\"recordedBy\":\"bob\",\"recordedDate\":1430175416029,\"version\":10},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"},{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":4},{\"lsType\":\"numericValue\",\"lsKind\":\"pegylated\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":2}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1438122449643,\"secondLsThing\":{\"codeName\":\"L000004\",\"deleted\":false,\"id\":1522457,\"ignored\":false,\"lsKind\":\"linker\",\"lsLabels\":[{\"deleted\":false,\"id\":298948,\"ignored\":false,\"labelText\":\"Aminoethyl-PBA-PEG40\",\"lsKind\":\"linker\",\"lsTransaction\":9877,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_linker\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1437154303599,\"version\":0}],\"lsTags\":[],\"lsTransaction\":9877,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_linker\",\"recordedBy\":\"bob\",\"recordedDate\":1437154303598,\"version\":1},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"},{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":5}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1438122449644,\"secondLsThing\":{\"codeName\":\"R000030\",\"deleted\":false,\"id\":1523078,\"ignored\":false,\"lsKind\":\"rna\",\"lsLabels\":[{\"deleted\":false,\"id\":299351,\"ignored\":false,\"labelText\":\"AHA1-12-none-testing eg may13 pp4(2)-ad19(1)\",\"lsKind\":\"rna\",\"lsTransaction\":10069,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_rna\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1438041524718,\"version\":0}],\"lsTags\":[],\"lsTransaction\":10069,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_rna\",\"recordedBy\":\"bob\",\"recordedDate\":1438041524717,\"version\":1},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"}],\"cid\":\"c50\",\"_changing\":false,\"_previousAttributes\":{\"lsType\":\"thing\",\"lsKind\":\"thing\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1438122372856,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"descriptive\",\"labelText\":\"testing eg feb 3 cbp10(1)-AHA1-12-none-testing eg may13 pp4(2)-ad19(1)-ad10-testing2(3%P)-Aminoethyl-PBA-PEG40(2%P)-testing eg feb 6 cbp 9-testing eg april15 peg5(4)-NP5\",\"ignored\":false,\"preferred\":true,\"recordedDate\":null,\"recordedBy\":\"\",\"physicallyLabled\":false,\"imageFile\":null},{\"lsType\":\"name\",\"lsKind\":\"component id\",\"labelText\":\"I000027(1)-R000030-L000002(3%P)-L000004(2%P)-P000043(4)-NP5\",\"ignored\":false,\"preferred\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"physicallyLabled\":false,\"imageFile\":null}],\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"formulation parent\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"scientist\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeOrigin\":\"ACAS authors\",\"value\":\"bob\",\"codeValue\":\"bob\"},{\"lsType\":\"dateValue\",\"lsKind\":\"completion date\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":null,\"dateValue\":null},{\"lsType\":\"stringValue\",\"lsKind\":\"notebook\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"codeValue\",\"lsKind\":\"project\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeKind\":\"project\",\"codeType\":\"formulation\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"AR-SV\",\"value\":\"AR-SV\"},{\"lsType\":\"numericValue\",\"lsKind\":\"total charge ratio\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":5,\"numericValue\":5},{\"lsType\":\"clobValue\",\"lsKind\":\"comments\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"clobValue\":\"\"},{\"lsType\":\"numericValue\",\"lsKind\":\"batch number\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":0,\"value\":0}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"firstLsThings\":[]},\"changed\":{\"secondLsThings\":[]},\"_pending\":false,\"urlRoot\":\"/api/things/parent/formulation\",\"className\":\"FormulationParent\",\"lsProperties\":{\"defaultLabels\":[{\"key\":\"formulation name\",\"type\":\"name\",\"kind\":\"descriptive\",\"preferred\":true},{\"key\":\"formulation component id name\",\"type\":\"name\",\"kind\":\"component id\",\"preferred\":false}],\"defaultValues\":[{\"key\":\"scientist\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"type\":\"codeValue\",\"kind\":\"scientist\",\"codeOrigin\":\"ACAS authors\"},{\"key\":\"completion date\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"type\":\"dateValue\",\"kind\":\"completion date\"},{\"key\":\"notebook\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"type\":\"stringValue\",\"kind\":\"notebook\"},{\"key\":\"project\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"type\":\"codeValue\",\"kind\":\"project\",\"codeType\":\"formulation\",\"codeKind\":\"project\",\"codeOrigin\":\"ACAS DDICT\",\"value\":\"unassigned\"},{\"key\":\"total charge ratio\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"type\":\"numericValue\",\"kind\":\"total charge ratio\"},{\"key\":\"comments\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"type\":\"clobValue\",\"kind\":\"comments\"},{\"key\":\"batch number\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"type\":\"numericValue\",\"kind\":\"batch number\",\"value\":0}],\"defaultFirstLsThingItx\":[],\"defaultSecondLsThingItx\":[]},\"validationError\":null,\"idAttribute\":\"id\"},\"uniqueName\":true,\"uniqueInteractions\":true,\"valueRules\":[{\"comparisonMethod\":\"numeric exact match\",\"comparisonRange\":null,\"value\":{\"entity\":\"ItxLsThingLsThing\",\"entityType\":\"incorporates\",\"entityKind\":\"assembly_component\",\"stateType\":\"metadata\",\"stateKind\":\"composition\",\"valueType\":\"numericValue\",\"valueKind\":\"ia siRNA mole ratio\"}},{\"comparisonMethod\":\"numeric exact match\",\"comparisonRange\":null,\"value\":{\"entity\":\"ItxLsThingLsThing\",\"entityType\":\"incorporates\",\"entityKind\":\"assembly_component\",\"stateType\":\"metadata\",\"stateKind\":\"composition\",\"valueType\":\"numericValue\",\"valueKind\":\"pegylated\"}},{\"comparisonMethod\":\"numeric exact match\",\"comparisonRange\":null,\"value\":{\"entity\":\"ItxLsThingLsThing\",\"entityType\":\"incorporates\",\"entityKind\":\"assembly_component\",\"stateType\":\"metadata\",\"stateKind\":\"composition\",\"valueType\":\"numericValue\",\"valueKind\":\"charge ratio\"}},{\"comparisonMethod\":\"numeric exact match\",\"comparisonRange\":null,\"value\":{\"entity\":\"lsThing\",\"entityType\":\"parent\",\"entityKind\":\"formulation\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"valueType\":\"numericValue\",\"valueKind\":\"total charge ratio\"}}]}";
		LsThingValidationDTO validationDTO = LsThingValidationDTO.fromJsonToLsThingValidationDTO(json);
		Collection<LsThingValidationErrorMessage> errorMessages = lsThingService.validateLsThing(validationDTO);
		Assert.assertFalse(errorMessages.isEmpty());
		logger.info(LsThingValidationErrorMessage.toJsonArray(errorMessages));
	}

	@Test
	@Transactional
	public void lsThingValueValidationTest_formulation_accept() {
		String json = "{\"lsThing\":{\"lsType\":\"parent\",\"lsKind\":\"formulation\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1438122372856,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"descriptive\",\"labelText\":\"testing eg feb 3 cbp10(1)-AHA1-12-none-testing eg may13 pp4(2)-ad19(1)-ad10-testing2(3%P)-Aminoethyl-PBA-PEG40(2%P)-testing eg feb 6 cbp 9-testing eg april15 peg5(4)-NP5 test\",\"ignored\":false,\"preferred\":true,\"recordedDate\":null,\"recordedBy\":\"\",\"physicallyLabled\":false,\"imageFile\":null},{\"lsType\":\"name\",\"lsKind\":\"component id\",\"labelText\":\"I000027(1)-R000030-L000002(3%P)-L000004(2%P)-P000043(4)-NP5 test\",\"ignored\":false,\"preferred\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"physicallyLabled\":false,\"imageFile\":null}],\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"formulation parent\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"scientist\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeOrigin\":\"ACAS authors\",\"value\":\"bob\",\"codeValue\":\"bob\"},{\"lsType\":\"dateValue\",\"lsKind\":\"completion date\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":null,\"dateValue\":null},{\"lsType\":\"stringValue\",\"lsKind\":\"notebook\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"codeValue\",\"lsKind\":\"project\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeKind\":\"project\",\"codeType\":\"formulation\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"AR-SV\",\"value\":\"AR-SV\"},{\"lsType\":\"numericValue\",\"lsKind\":\"total charge ratio\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":5,\"numericValue\":5},{\"lsType\":\"clobValue\",\"lsKind\":\"comments\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"clobValue\":\"\"},{\"lsType\":\"numericValue\",\"lsKind\":\"batch number\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":0,\"value\":0}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"firstLsThings\":[],\"secondLsThings\":[{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":1},{\"lsType\":\"numericValue\",\"lsKind\":\"ia siRNA mole ratio\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":1}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1438122449639,\"secondLsThing\":{\"codeName\":\"I000027\",\"deleted\":false,\"id\":1513475,\"ignored\":false,\"lsKind\":\"internalization agent\",\"lsLabels\":[{\"deleted\":false,\"id\":298079,\"ignored\":false,\"labelText\":\"testing eg feb 3 cbp10\",\"lsKind\":\"internalization agent\",\"lsTransaction\":8873,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_internalization agent\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1430431782315,\"version\":0}],\"lsTags\":[],\"lsTransaction\":8873,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_internalization agent\",\"modifiedDate\":1430755528275,\"recordedBy\":\"bob\",\"recordedDate\":1430771015624,\"version\":1},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"},{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":2},{\"lsType\":\"numericValue\",\"lsKind\":\"charge ratio\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":5}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1438122449641,\"secondLsThing\":{\"codeName\":\"P000043\",\"deleted\":false,\"id\":1514347,\"ignored\":false,\"lsKind\":\"polymer\",\"lsLabels\":[{\"deleted\":false,\"id\":298266,\"ignored\":false,\"labelText\":\"testing eg feb 6 cbp 9-testing eg april15 peg5\",\"lsKind\":\"polymer\",\"lsTransaction\":9326,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_polymer\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"egao\",\"recordedDate\":1432072358504,\"version\":0}],\"lsTags\":[],\"lsTransaction\":9326,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_polymer\",\"recordedBy\":\"egao\",\"recordedDate\":1432072358502,\"version\":1},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"},{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":3},{\"lsType\":\"numericValue\",\"lsKind\":\"pegylated\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":3}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1438122449642,\"secondLsThing\":{\"codeName\":\"L000002\",\"deleted\":false,\"id\":1512917,\"ignored\":false,\"lsKind\":\"linker\",\"lsLabels\":[{\"deleted\":false,\"id\":297912,\"ignored\":false,\"labelText\":\"LSM10-testing2\",\"lsKind\":\"linker\",\"lsType\":\"name\",\"lsTypeAndKind\":\"name_linker\",\"modifiedDate\":1430175416507,\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1429118074579,\"version\":2}],\"lsTags\":[],\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_linker\",\"modifiedDate\":1437592234459,\"recordedBy\":\"bob\",\"recordedDate\":1430175416029,\"version\":10},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"},{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":4},{\"lsType\":\"numericValue\",\"lsKind\":\"pegylated\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":2}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1438122449643,\"secondLsThing\":{\"codeName\":\"L000004\",\"deleted\":false,\"id\":1522457,\"ignored\":false,\"lsKind\":\"linker\",\"lsLabels\":[{\"deleted\":false,\"id\":298948,\"ignored\":false,\"labelText\":\"Aminoethyl-PBA-PEG40\",\"lsKind\":\"linker\",\"lsTransaction\":9877,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_linker\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1437154303599,\"version\":0}],\"lsTags\":[],\"lsTransaction\":9877,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_linker\",\"recordedBy\":\"bob\",\"recordedDate\":1437154303598,\"version\":1},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"},{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":5}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1438122449644,\"secondLsThing\":{\"codeName\":\"R000030\",\"deleted\":false,\"id\":1523078,\"ignored\":false,\"lsKind\":\"rna\",\"lsLabels\":[{\"deleted\":false,\"id\":299351,\"ignored\":false,\"labelText\":\"AHA1-12-none-testing eg may13 pp4(2)-ad19(1)\",\"lsKind\":\"rna\",\"lsTransaction\":10069,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_rna\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1438041524718,\"version\":0}],\"lsTags\":[],\"lsTransaction\":10069,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_rna\",\"recordedBy\":\"bob\",\"recordedDate\":1438041524717,\"version\":1},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"}],\"cid\":\"c50\",\"_changing\":false,\"_previousAttributes\":{\"lsType\":\"thing\",\"lsKind\":\"thing\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1438122372856,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"descriptive\",\"labelText\":\"testing eg feb 3 cbp10(1)-AHA1-12-none-testing eg may13 pp4(2)-ad19(1)-ad10-testing2(3%P)-Aminoethyl-PBA-PEG40(2%P)-testing eg feb 6 cbp 9-testing eg april15 peg5(4)-NP5\",\"ignored\":false,\"preferred\":true,\"recordedDate\":null,\"recordedBy\":\"\",\"physicallyLabled\":false,\"imageFile\":null},{\"lsType\":\"name\",\"lsKind\":\"component id\",\"labelText\":\"I000027(1)-R000030-L000002(3%P)-L000004(2%P)-P000043(4)-NP5\",\"ignored\":false,\"preferred\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"physicallyLabled\":false,\"imageFile\":null}],\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"formulation parent\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"scientist\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeOrigin\":\"ACAS authors\",\"value\":\"bob\",\"codeValue\":\"bob\"},{\"lsType\":\"dateValue\",\"lsKind\":\"completion date\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":null,\"dateValue\":null},{\"lsType\":\"stringValue\",\"lsKind\":\"notebook\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"codeValue\",\"lsKind\":\"project\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeKind\":\"project\",\"codeType\":\"formulation\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"AR-SV\",\"value\":\"AR-SV\"},{\"lsType\":\"numericValue\",\"lsKind\":\"total charge ratio\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":5,\"numericValue\":5},{\"lsType\":\"clobValue\",\"lsKind\":\"comments\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"clobValue\":\"\"},{\"lsType\":\"numericValue\",\"lsKind\":\"batch number\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":0,\"value\":0}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"firstLsThings\":[]},\"changed\":{\"secondLsThings\":[]},\"_pending\":false,\"urlRoot\":\"/api/things/parent/formulation\",\"className\":\"FormulationParent\",\"lsProperties\":{\"defaultLabels\":[{\"key\":\"formulation name\",\"type\":\"name\",\"kind\":\"descriptive\",\"preferred\":true},{\"key\":\"formulation component id name\",\"type\":\"name\",\"kind\":\"component id\",\"preferred\":false}],\"defaultValues\":[{\"key\":\"scientist\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"type\":\"codeValue\",\"kind\":\"scientist\",\"codeOrigin\":\"ACAS authors\"},{\"key\":\"completion date\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"type\":\"dateValue\",\"kind\":\"completion date\"},{\"key\":\"notebook\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"type\":\"stringValue\",\"kind\":\"notebook\"},{\"key\":\"project\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"type\":\"codeValue\",\"kind\":\"project\",\"codeType\":\"formulation\",\"codeKind\":\"project\",\"codeOrigin\":\"ACAS DDICT\",\"value\":\"unassigned\"},{\"key\":\"total charge ratio\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"type\":\"numericValue\",\"kind\":\"total charge ratio\"},{\"key\":\"comments\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"type\":\"clobValue\",\"kind\":\"comments\"},{\"key\":\"batch number\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"type\":\"numericValue\",\"kind\":\"batch number\",\"value\":0}],\"defaultFirstLsThingItx\":[],\"defaultSecondLsThingItx\":[]},\"validationError\":null,\"idAttribute\":\"id\"},\"uniqueName\":true,\"uniqueInteractions\":true,\"valueRules\":[{\"comparisonMethod\":\"numeric exact match\",\"comparisonRange\":null,\"value\":{\"entity\":\"ItxLsThingLsThing\",\"entityType\":\"incorporates\",\"entityKind\":\"assembly_component\",\"stateType\":\"metadata\",\"stateKind\":\"composition\",\"valueType\":\"numericValue\",\"valueKind\":\"ia siRNA mole ratio\"}},{\"comparisonMethod\":\"numeric exact match\",\"comparisonRange\":null,\"value\":{\"entity\":\"ItxLsThingLsThing\",\"entityType\":\"incorporates\",\"entityKind\":\"assembly_component\",\"stateType\":\"metadata\",\"stateKind\":\"composition\",\"valueType\":\"numericValue\",\"valueKind\":\"pegylated\"}},{\"comparisonMethod\":\"numeric exact match\",\"comparisonRange\":null,\"value\":{\"entity\":\"ItxLsThingLsThing\",\"entityType\":\"incorporates\",\"entityKind\":\"assembly_component\",\"stateType\":\"metadata\",\"stateKind\":\"composition\",\"valueType\":\"numericValue\",\"valueKind\":\"charge ratio\"}},{\"comparisonMethod\":\"numeric exact match\",\"comparisonRange\":null,\"value\":{\"entity\":\"lsThing\",\"entityType\":\"parent\",\"entityKind\":\"formulation\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"valueType\":\"numericValue\",\"valueKind\":\"total charge ratio\"}}]}";
		LsThingValidationDTO validationDTO = LsThingValidationDTO.fromJsonToLsThingValidationDTO(json);
		Collection<LsThingValidationErrorMessage> errorMessages = lsThingService.validateLsThing(validationDTO);
		Assert.assertTrue(errorMessages.isEmpty());
	}

	@Test
	@Transactional
	public void lsThingValueValidationTest_formulation_unique() {
		String json = "{\"lsThing\":{\"lsType\":\"parent\",\"lsKind\":\"formulation\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1438195786235,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"descriptive\",\"labelText\":\"testing eg feb 9 pp1-testing eg april3 peg1(22)-AHA1-12-none-testing eg may13 pp4(2)-ad19(1)-Aminoethyl-PBA-PEG40(3%P)-testing eg feb 6 cbp 9-testing eg april15 peg5(4)-NP5\",\"ignored\":false,\"preferred\":true,\"recordedDate\":null,\"recordedBy\":\"\",\"physicallyLabled\":false,\"imageFile\":null},{\"lsType\":\"name\",\"lsKind\":\"component id\",\"labelText\":\"I000022(22)-R000030-L000004(3%P)-P000043(4)-NP5 test\",\"ignored\":false,\"preferred\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"physicallyLabled\":false,\"imageFile\":null}],\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"formulation parent\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"scientist\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeOrigin\":\"ACAS authors\",\"value\":\"bob\",\"codeValue\":\"bob\"},{\"lsType\":\"dateValue\",\"lsKind\":\"completion date\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":null,\"dateValue\":null},{\"lsType\":\"stringValue\",\"lsKind\":\"notebook\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"codeValue\",\"lsKind\":\"project\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeKind\":\"project\",\"codeType\":\"formulation\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"AR-SV\",\"value\":\"AR-SV\"},{\"lsType\":\"numericValue\",\"lsKind\":\"total charge ratio\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":5,\"numericValue\":5},{\"lsType\":\"clobValue\",\"lsKind\":\"comments\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"clobValue\":\"\"},{\"lsType\":\"numericValue\",\"lsKind\":\"batch number\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":0,\"value\":0}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"firstLsThings\":[],\"secondLsThings\":[{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":1},{\"lsType\":\"numericValue\",\"lsKind\":\"ia siRNA mole ratio\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":22}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1438195893619,\"secondLsThing\":{\"codeName\":\"I000022\",\"deleted\":false,\"id\":1513237,\"ignored\":false,\"lsKind\":\"internalization agent\",\"lsLabels\":[{\"deleted\":false,\"id\":297990,\"ignored\":false,\"labelText\":\"testing eg feb 9 pp1-testing eg april3 peg1\",\"lsKind\":\"internalization agent\",\"lsType\":\"name\",\"lsTypeAndKind\":\"name_internalization agent\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"egao\",\"recordedDate\":1430178241818,\"version\":0}],\"lsTags\":[],\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_internalization agent\",\"recordedBy\":\"egao\",\"recordedDate\":1430178241818,\"version\":1},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"},{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":2},{\"lsType\":\"numericValue\",\"lsKind\":\"pegylated\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":3}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1438195893621,\"secondLsThing\":{\"codeName\":\"L000004\",\"deleted\":false,\"id\":1522457,\"ignored\":false,\"lsKind\":\"linker\",\"lsLabels\":[{\"deleted\":false,\"id\":298948,\"ignored\":false,\"labelText\":\"Aminoethyl-PBA-PEG40\",\"lsKind\":\"linker\",\"lsTransaction\":9877,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_linker\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1437154303599,\"version\":0}],\"lsTags\":[],\"lsTransaction\":9877,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_linker\",\"recordedBy\":\"bob\",\"recordedDate\":1437154303598,\"version\":1},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"},{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":3},{\"lsType\":\"numericValue\",\"lsKind\":\"charge ratio\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":4}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1438195893622,\"secondLsThing\":{\"codeName\":\"P000043\",\"deleted\":false,\"id\":1514347,\"ignored\":false,\"lsKind\":\"polymer\",\"lsLabels\":[{\"deleted\":false,\"id\":298266,\"ignored\":false,\"labelText\":\"testing eg feb 6 cbp 9-testing eg april15 peg5\",\"lsKind\":\"polymer\",\"lsTransaction\":9326,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_polymer\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"egao\",\"recordedDate\":1432072358504,\"version\":0}],\"lsTags\":[],\"lsTransaction\":9326,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_polymer\",\"recordedBy\":\"egao\",\"recordedDate\":1432072358502,\"version\":1},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"},{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":4}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1438195893623,\"secondLsThing\":{\"codeName\":\"R000030\",\"deleted\":false,\"id\":1523078,\"ignored\":false,\"lsKind\":\"rna\",\"lsLabels\":[{\"deleted\":false,\"id\":299351,\"ignored\":false,\"labelText\":\"AHA1-12-none-testing eg may13 pp4(2)-ad19(1)\",\"lsKind\":\"rna\",\"lsTransaction\":10069,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_rna\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1438041524718,\"version\":0}],\"lsTags\":[],\"lsTransaction\":10069,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_rna\",\"recordedBy\":\"bob\",\"recordedDate\":1438041524717,\"version\":1},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"}],\"cid\":\"c50\",\"_changing\":false,\"_previousAttributes\":{\"lsType\":\"thing\",\"lsKind\":\"thing\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1438195786235,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"descriptive\",\"labelText\":\"testing eg feb 9 pp1-testing eg april3 peg1(22)-AHA1-12-none-testing eg may13 pp4(2)-ad19(1)-Aminoethyl-PBA-PEG40(3%P)-testing eg feb 6 cbp 9-testing eg april15 peg5(4)-NP5\",\"ignored\":false,\"preferred\":true,\"recordedDate\":null,\"recordedBy\":\"\",\"physicallyLabled\":false,\"imageFile\":null},{\"lsType\":\"name\",\"lsKind\":\"component id\",\"labelText\":\"I000022(22)-R000030-L000004(3%P)-P000043(4)-NP5\",\"ignored\":false,\"preferred\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"physicallyLabled\":false,\"imageFile\":null}],\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"formulation parent\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"scientist\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeOrigin\":\"ACAS authors\",\"value\":\"bob\",\"codeValue\":\"bob\"},{\"lsType\":\"dateValue\",\"lsKind\":\"completion date\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":null,\"dateValue\":null},{\"lsType\":\"stringValue\",\"lsKind\":\"notebook\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"codeValue\",\"lsKind\":\"project\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeKind\":\"project\",\"codeType\":\"formulation\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"AR-SV\",\"value\":\"AR-SV\"},{\"lsType\":\"numericValue\",\"lsKind\":\"total charge ratio\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":5,\"numericValue\":5},{\"lsType\":\"clobValue\",\"lsKind\":\"comments\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"clobValue\":\"\"},{\"lsType\":\"numericValue\",\"lsKind\":\"batch number\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":0,\"value\":0}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"firstLsThings\":[]},\"changed\":{\"secondLsThings\":[]},\"_pending\":false,\"urlRoot\":\"/api/things/parent/formulation\",\"className\":\"FormulationParent\",\"lsProperties\":{\"defaultLabels\":[{\"key\":\"formulation name\",\"type\":\"name\",\"kind\":\"descriptive\",\"preferred\":true},{\"key\":\"formulation component id name\",\"type\":\"name\",\"kind\":\"component id\",\"preferred\":false}],\"defaultValues\":[{\"key\":\"scientist\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"type\":\"codeValue\",\"kind\":\"scientist\",\"codeOrigin\":\"ACAS authors\"},{\"key\":\"completion date\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"type\":\"dateValue\",\"kind\":\"completion date\"},{\"key\":\"notebook\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"type\":\"stringValue\",\"kind\":\"notebook\"},{\"key\":\"project\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"type\":\"codeValue\",\"kind\":\"project\",\"codeType\":\"formulation\",\"codeKind\":\"project\",\"codeOrigin\":\"ACAS DDICT\",\"value\":\"unassigned\"},{\"key\":\"total charge ratio\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"type\":\"numericValue\",\"kind\":\"total charge ratio\"},{\"key\":\"comments\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"type\":\"clobValue\",\"kind\":\"comments\"},{\"key\":\"batch number\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"type\":\"numericValue\",\"kind\":\"batch number\",\"value\":0}],\"defaultFirstLsThingItx\":[],\"defaultSecondLsThingItx\":[]},\"validationError\":null,\"idAttribute\":\"id\"},\"uniqueName\":true,\"uniqueInteractions\":true,\"valueRules\":[{\"comparisonMethod\":\"numeric exact match\",\"comparisonRange\":null,\"value\":{\"entity\":\"ItxLsThingLsThing\",\"entityType\":\"incorporates\",\"entityKind\":\"assembly_component\",\"stateType\":\"metadata\",\"stateKind\":\"composition\",\"valueType\":\"numericValue\",\"valueKind\":\"ia siRNA mole ratio\"}},{\"comparisonMethod\":\"numeric exact match\",\"comparisonRange\":null,\"value\":{\"entity\":\"ItxLsThingLsThing\",\"entityType\":\"incorporates\",\"entityKind\":\"assembly_component\",\"stateType\":\"metadata\",\"stateKind\":\"composition\",\"valueType\":\"numericValue\",\"valueKind\":\"pegylated\"}},{\"comparisonMethod\":\"numeric exact match\",\"comparisonRange\":null,\"value\":{\"entity\":\"ItxLsThingLsThing\",\"entityType\":\"incorporates\",\"entityKind\":\"assembly_component\",\"stateType\":\"metadata\",\"stateKind\":\"composition\",\"valueType\":\"numericValue\",\"valueKind\":\"charge ratio\"}},{\"comparisonMethod\":\"numeric exact match\",\"comparisonRange\":null,\"value\":{\"entity\":\"lsThing\",\"entityType\":\"parent\",\"entityKind\":\"formulation\",\"stateType\":\"metadata\",\"stateKind\":\"formulation parent\",\"valueType\":\"numericValue\",\"valueKind\":\"total charge ratio\"}}]}";
		LsThingValidationDTO validationDTO = LsThingValidationDTO.fromJsonToLsThingValidationDTO(json);
		Collection<LsThingValidationErrorMessage> errorMessages = lsThingService.validateLsThing(validationDTO);
		Assert.assertTrue(errorMessages.isEmpty());
	}

	@Test
	@Transactional
	public void lsThingValueValidationTest_formulation_order_reverse_duplicate() {
		String json = "{\"lsThing\":{\"lsType\":\"parent\",\"lsKind\":\"polymer\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1438206548123,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"polymer\",\"labelText\":\"testing eg feb 23 pp2-testing eg may4 cbp3-testing eg feb 10 lsmp2 test\",\"ignored\":false,\"preferred\":true,\"recordedDate\":null,\"recordedBy\":\"\",\"physicallyLabled\":false,\"imageFile\":null}],\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"polymer parent\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"scientist\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeOrigin\":\"ACAS authors\",\"value\":\"bob\",\"codeValue\":\"bob\"},{\"lsType\":\"dateValue\",\"lsKind\":\"completion date\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":null,\"dateValue\":null},{\"lsType\":\"stringValue\",\"lsKind\":\"notebook\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"stringValue\",\"lsKind\":\"architecture\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"stringValue\":\"linear\",\"value\":\"linear\"},{\"lsType\":\"numericValue\",\"lsKind\":\"batch number\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":0,\"value\":0}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"firstLsThings\":[],\"secondLsThings\":[{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":1}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1438206660956,\"secondLsThing\":{\"codeName\":\"PROT000007\",\"deleted\":false,\"id\":1386285,\"ignored\":false,\"lsKind\":\"protein\",\"lsLabels\":[{\"deleted\":false,\"id\":297313,\"ignored\":false,\"labelText\":\"testing eg feb 23 pp2\",\"lsKind\":\"protein\",\"lsType\":\"name\",\"lsTypeAndKind\":\"name_protein\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1424730027663,\"version\":0}],\"lsTags\":[],\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_protein\",\"recordedBy\":\"bob\",\"recordedDate\":1424730027663,\"version\":1},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"},{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":2}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1438206660958,\"secondLsThing\":{\"codeName\":\"CB000128\",\"deleted\":false,\"id\":1513512,\"ignored\":false,\"lsKind\":\"cationic block\",\"lsLabels\":[{\"deleted\":false,\"id\":298083,\"ignored\":false,\"labelText\":\"testing eg may4 cbp3\",\"lsKind\":\"cationic block\",\"lsTransaction\":8900,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_cationic block\",\"modifiedDate\":1430773690972,\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1428955576687,\"version\":1}],\"lsTags\":[],\"lsTransaction\":8897,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_cationic block\",\"modifiedDate\":1430774577035,\"recordedBy\":\"bob\",\"recordedDate\":1430773343151,\"version\":16},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"},{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":3}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1438206660959,\"secondLsThing\":{\"codeName\":\"LSM000005\",\"deleted\":false,\"id\":1356068,\"ignored\":false,\"lsKind\":\"linker small molecule\",\"lsLabels\":[{\"deleted\":false,\"id\":297238,\"ignored\":false,\"labelText\":\"testing eg feb 10 lsmp2\",\"lsKind\":\"linker small molecule\",\"lsType\":\"name\",\"lsTypeAndKind\":\"name_linker small molecule\",\"modifiedDate\":1424216308983,\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1423600529206,\"version\":3}],\"lsTags\":[],\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_linker small molecule\",\"modifiedDate\":1424216308981,\"recordedBy\":\"bob\",\"recordedDate\":1423600529206,\"version\":4},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"}],\"cid\":\"c50\",\"_changing\":false,\"_previousAttributes\":{\"lsType\":\"thing\",\"lsKind\":\"thing\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1438206548123,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"polymer\",\"labelText\":\"testing eg feb 23 pp2-testing eg may4 cbp3-testing eg feb 10 lsmp2\",\"ignored\":false,\"preferred\":true,\"recordedDate\":null,\"recordedBy\":\"\",\"physicallyLabled\":false,\"imageFile\":null}],\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"polymer parent\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"scientist\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeOrigin\":\"ACAS authors\",\"value\":\"bob\",\"codeValue\":\"bob\"},{\"lsType\":\"dateValue\",\"lsKind\":\"completion date\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":null,\"dateValue\":null},{\"lsType\":\"stringValue\",\"lsKind\":\"notebook\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"stringValue\",\"lsKind\":\"architecture\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"stringValue\":\"linear\",\"value\":\"linear\"},{\"lsType\":\"numericValue\",\"lsKind\":\"batch number\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":0,\"value\":0}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"firstLsThings\":[]},\"changed\":{\"secondLsThings\":[]},\"_pending\":false,\"urlRoot\":\"/api/things/parent/polymer\",\"className\":\"PolymerParent\",\"lsProperties\":{\"defaultLabels\":[{\"key\":\"polymer name\",\"type\":\"name\",\"kind\":\"polymer\",\"preferred\":true}],\"defaultValues\":[{\"key\":\"scientist\",\"stateType\":\"metadata\",\"stateKind\":\"polymer parent\",\"type\":\"codeValue\",\"kind\":\"scientist\",\"codeOrigin\":\"ACAS authors\"},{\"key\":\"completion date\",\"stateType\":\"metadata\",\"stateKind\":\"polymer parent\",\"type\":\"dateValue\",\"kind\":\"completion date\"},{\"key\":\"notebook\",\"stateType\":\"metadata\",\"stateKind\":\"polymer parent\",\"type\":\"stringValue\",\"kind\":\"notebook\"},{\"key\":\"architecture\",\"stateType\":\"metadata\",\"stateKind\":\"polymer parent\",\"type\":\"stringValue\",\"kind\":\"architecture\",\"value\":\"linear\"},{\"key\":\"structural file\",\"stateType\":\"metadata\",\"stateKind\":\"polymer parent\",\"type\":\"fileValue\",\"kind\":\"structural file\"},{\"key\":\"batch number\",\"stateType\":\"metadata\",\"stateKind\":\"polymer parent\",\"type\":\"numericValue\",\"kind\":\"batch number\",\"value\":0}],\"defaultFirstLsThingItx\":[],\"defaultSecondLsThingItx\":[]},\"validationError\":null,\"idAttribute\":\"id\"},\"uniqueName\":true,\"uniqueInteractions\":true,\"orderMatters\":true,\"forwardAndReverseAreSame\":true}";
		LsThingValidationDTO validationDTO = LsThingValidationDTO.fromJsonToLsThingValidationDTO(json);
		Collection<LsThingValidationErrorMessage> errorMessages = lsThingService.validateLsThing(validationDTO);
		Assert.assertFalse(errorMessages.isEmpty());
		logger.info(LsThingValidationErrorMessage.toJsonArray(errorMessages));
	}

	@Test
	@Transactional
	public void lsThingValueValidationTest_same_order_itx_subset_accept() {
		String json = "{\"lsThing\":{\"lsType\":\"parent\",\"lsKind\":\"internalization agent\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1438272717386,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"internalization agent\",\"labelText\":\"testing eg feb 23 pp2-peg5\",\"ignored\":false,\"preferred\":true,\"recordedDate\":null,\"recordedBy\":\"\",\"physicallyLabled\":false,\"imageFile\":null}],\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"internalization agent parent\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"scientist\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeOrigin\":\"ACAS authors\",\"value\":\"bob\",\"codeValue\":\"bob\"},{\"lsType\":\"dateValue\",\"lsKind\":\"completion date\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":null,\"dateValue\":null},{\"lsType\":\"stringValue\",\"lsKind\":\"notebook\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"codeValue\",\"lsKind\":\"conjugation type\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeKind\":\"conjugation type\",\"codeType\":\"internalization agent\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"cys\",\"value\":\"cys\"},{\"lsType\":\"stringValue\",\"lsKind\":\"conjugation site\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"numericValue\",\"lsKind\":\"batch number\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":0,\"value\":0}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"firstLsThings\":[],\"secondLsThings\":[{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":1}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1438272767249,\"secondLsThing\":{\"codeName\":\"PROT000007\",\"deleted\":false,\"id\":1386285,\"ignored\":false,\"lsKind\":\"protein\",\"lsLabels\":[{\"deleted\":false,\"id\":297313,\"ignored\":false,\"labelText\":\"testing eg feb 23 pp2\",\"lsKind\":\"protein\",\"lsType\":\"name\",\"lsTypeAndKind\":\"name_protein\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1424730027663,\"version\":0}],\"lsTags\":[],\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_protein\",\"recordedBy\":\"bob\",\"recordedDate\":1424730027663,\"version\":1},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"},{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":2}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1438272767251,\"secondLsThing\":{\"codeName\":\"PEG000010\",\"deleted\":false,\"id\":1442920,\"ignored\":false,\"lsKind\":\"peg\",\"lsLabels\":[{\"deleted\":false,\"id\":297434,\"ignored\":false,\"labelText\":\"peg5\",\"lsKind\":\"peg\",\"lsTransaction\":9903,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_peg\",\"modifiedDate\":1437430613372,\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1426100246459,\"version\":2}],\"lsTags\":[],\"lsTransaction\":9903,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_peg\",\"modifiedDate\":1437430613370,\"recordedBy\":\"bob\",\"recordedDate\":1437430615262,\"version\":5},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"}],\"cid\":\"c50\",\"_changing\":false,\"_previousAttributes\":{\"lsType\":\"thing\",\"lsKind\":\"thing\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1438272717386,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"internalization agent\",\"labelText\":\"testing eg feb 23 pp2-peg5\",\"ignored\":false,\"preferred\":true,\"recordedDate\":null,\"recordedBy\":\"\",\"physicallyLabled\":false,\"imageFile\":null}],\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"internalization agent parent\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"scientist\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeOrigin\":\"ACAS authors\",\"value\":\"bob\",\"codeValue\":\"bob\"},{\"lsType\":\"dateValue\",\"lsKind\":\"completion date\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":null,\"dateValue\":null},{\"lsType\":\"stringValue\",\"lsKind\":\"notebook\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"codeValue\",\"lsKind\":\"conjugation type\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"codeKind\":\"conjugation type\",\"codeType\":\"internalization agent\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"cys\",\"value\":\"cys\"},{\"lsType\":\"stringValue\",\"lsKind\":\"conjugation site\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"numericValue\",\"lsKind\":\"batch number\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":0,\"value\":0}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"firstLsThings\":[]},\"changed\":{\"secondLsThings\":[]},\"_pending\":false,\"urlRoot\":\"/api/things/parent/internalization agent\",\"className\":\"InternalizationAgentParent\",\"lsProperties\":{\"defaultLabels\":[{\"key\":\"internalization agent name\",\"type\":\"name\",\"kind\":\"internalization agent\",\"preferred\":true}],\"defaultValues\":[{\"key\":\"scientist\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"codeValue\",\"kind\":\"scientist\",\"codeOrigin\":\"ACAS authors\"},{\"key\":\"completion date\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"dateValue\",\"kind\":\"completion date\"},{\"key\":\"notebook\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"stringValue\",\"kind\":\"notebook\"},{\"key\":\"conjugation type\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"codeValue\",\"kind\":\"conjugation type\",\"codeType\":\"internalization agent\",\"codeKind\":\"conjugation type\",\"codeOrigin\":\"ACAS DDICT\",\"value\":\"unassigned\"},{\"key\":\"conjugation site\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"stringValue\",\"kind\":\"conjugation site\"},{\"key\":\"batch number\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"numericValue\",\"kind\":\"batch number\",\"value\":0}],\"defaultFirstLsThingItx\":[],\"defaultSecondLsThingItx\":[]},\"validationError\":null,\"idAttribute\":\"id\"},\"uniqueName\":true,\"uniqueInteractions\":true,\"orderMatters\":true,\"forwardAndReverseAreSame\":true}";
		LsThingValidationDTO validationDTO = LsThingValidationDTO.fromJsonToLsThingValidationDTO(json);
		Collection<LsThingValidationErrorMessage> errorMessages = lsThingService.validateLsThing(validationDTO);
		Assert.assertTrue(errorMessages.isEmpty());
	}

	@Test
	@Transactional
	public void lsThingValidationFail() {
		String json = "{ \"lsThing\":{\"codeName\":\"R000016-1\",\"deleted\":false,\"firstLsThings\":[],\"id\":3529,\"ignored\":false,\"lsKind\":\"rna\",\"lsLabels\":[{\"deleted\":false,\"id\":210,\"ignored\":false,\"labelText\":\"R000016-1\",\"lsKind\":\"ACAS LsThing\",\"lsType\":\"corpName\",\"lsTypeAndKind\":\"corpName_ACAS LsThing\",\"physicallyLabled\":true,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1447097802490,\"version\":0}],\"lsStates\":[{\"deleted\":false,\"id\":3798,\"ignored\":false,\"lsKind\":\"inventory\",\"lsTransaction\":214,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":11456,\"ignored\":false,\"lsKind\":\"location\",\"lsTransaction\":214,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097802490,\"stringValue\":\"lab\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":11457,\"ignored\":false,\"lsKind\":\"amount made\",\"lsTransaction\":214,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount made\",\"numericValue\":23.000000000000000000,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097802490,\"unitKind\":\"mg\",\"unitType\":\"mass\",\"unitTypeAndKind\":\"mass_mg\",\"version\":0}],\"recordedBy\":\"bob\",\"recordedDate\":1447097802490,\"version\":1},{\"deleted\":false,\"id\":3797,\"ignored\":false,\"lsKind\":\"rna batch\",\"lsTransaction\":214,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_rna batch\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"id\":11447,\"ignored\":false,\"lsKind\":\"unassigned\",\"lsTransaction\":214,\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_unassigned\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097802490,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeOrigin\":\"ACAS authors\",\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"bob\",\"deleted\":false,\"id\":11451,\"ignored\":false,\"lsKind\":\"scientist\",\"lsTransaction\":214,\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097802490,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1447056000000,\"deleted\":false,\"id\":11450,\"ignored\":false,\"lsKind\":\"completion date\",\"lsTransaction\":214,\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097802490,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"clobValue\":\"\",\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":11449,\"ignored\":false,\"lsKind\":\"comments\",\"lsTransaction\":214,\"lsType\":\"clobValue\",\"lsTypeAndKind\":\"clobValue_comments\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097802490,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":11446,\"ignored\":false,\"lsKind\":\"antisense strand purity\",\"lsTransaction\":214,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_antisense strand purity\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097802490,\"unitKind\":\"% purity\",\"unitType\":\"percentage\",\"unitTypeAndKind\":\"percentage_% purity\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":11454,\"ignored\":false,\"lsKind\":\"duplex purity\",\"lsTransaction\":214,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_duplex purity\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097802490,\"unitKind\":\"% purity\",\"unitType\":\"percentage\",\"unitTypeAndKind\":\"percentage_% purity\",\"version\":0},{\"codeKind\":\"source\",\"codeOrigin\":\"ACAS DDICT\",\"codeType\":\"component\",\"codeTypeAndKind\":\"component_source\",\"codeValue\":\"ACAS\",\"deleted\":false,\"id\":11448,\"ignored\":false,\"lsKind\":\"source\",\"lsTransaction\":214,\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_source\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097802490,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":11452,\"ignored\":false,\"lsKind\":\"source id\",\"lsTransaction\":214,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_source id\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097802490,\"stringValue\":\"\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":11455,\"ignored\":false,\"lsKind\":\"notebook\",\"lsTransaction\":214,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097802490,\"stringValue\":\"\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":11453,\"ignored\":false,\"lsKind\":\"sense strand purity\",\"lsTransaction\":214,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_sense strand purity\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097802490,\"unitKind\":\"% purity\",\"unitType\":\"percentage\",\"unitTypeAndKind\":\"percentage_% purity\",\"version\":0}],\"recordedBy\":\"bob\",\"recordedDate\":1447097802490,\"version\":1}],\"lsTags\":[],\"lsTransaction\":214,\"lsType\":\"batch\",\"lsTypeAndKind\":\"batch_rna\",\"recordedBy\":\"bob\",\"recordedDate\":1447097802490,\"secondLsThings\":[{\"deleted\":false,\"id\":3530,\"ignored\":false,\"lsKind\":\"assembly_component\",\"lsStates\":[{\"deleted\":false,\"id\":3799,\"ignored\":false,\"lsKind\":\"composition\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_composition\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":11458,\"ignored\":false,\"lsKind\":\"order\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_order\",\"numericValue\":1.000000000000000000,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"\",\"recordedDate\":1447097803039,\"unitTypeAndKind\":\"null_null\",\"version\":0}],\"recordedBy\":\"\",\"recordedDate\":1447097803039,\"version\":1}],\"lsType\":\"incorporates\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"recordedBy\":\"bob\",\"recordedDate\":1447097802489,\"secondLsThing\":{\"codeName\":\"LSM000004-1\",\"deleted\":false,\"id\":1132,\"ignored\":false,\"lsKind\":\"linker small molecule\",\"lsLabels\":[{\"deleted\":false,\"id\":288,\"ignored\":false,\"labelText\":\"LSM000004-1\",\"lsKind\":\"ACAS LsThing\",\"lsType\":\"corpName\",\"lsTypeAndKind\":\"corpName_ACAS LsThing\",\"physicallyLabled\":true,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1440707916880,\"version\":0}],\"lsStates\":[{\"deleted\":false,\"id\":1318,\"ignored\":false,\"lsKind\":\"linker small molecule batch\",\"lsTransaction\":67,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_linker small molecule batch\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4937,\"ignored\":false,\"lsKind\":\"purity\",\"lsTransaction\":67,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_purity\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1440707916880,\"unitKind\":\"% purity\",\"unitType\":\"percentage\",\"unitTypeAndKind\":\"percentage_% purity\",\"version\":1},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1440658800000,\"deleted\":false,\"id\":4936,\"ignored\":false,\"lsKind\":\"completion date\",\"lsTransaction\":67,\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1440707916880,\"unitTypeAndKind\":\"null_null\",\"version\":1},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4931,\"ignored\":false,\"lsKind\":\"notebook\",\"lsTransaction\":67,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1440707916880,\"stringValue\":\"test\",\"unitTypeAndKind\":\"null_null\",\"version\":1},{\"clobValue\":\"\",\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4934,\"ignored\":false,\"lsKind\":\"comments\",\"lsTransaction\":67,\"lsType\":\"clobValue\",\"lsTypeAndKind\":\"clobValue_comments\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1440707916880,\"unitTypeAndKind\":\"null_null\",\"version\":1},{\"codeTypeAndKind\":\"null_null\",\"comments\":\"Testing (8).pdf\",\"deleted\":false,\"fileValue\":\"entities/linkerSmallMolecules/LSM000004-1/Testing (8).pdf\",\"id\":4933,\"ignored\":false,\"lsKind\":\"gpc\",\"lsTransaction\":67,\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_gpc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1440707916880,\"unitTypeAndKind\":\"null_null\",\"version\":1},{\"codeOrigin\":\"ACAS authors\",\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"bob\",\"deleted\":false,\"id\":4932,\"ignored\":false,\"lsKind\":\"scientist\",\"lsTransaction\":67,\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1440707916880,\"unitTypeAndKind\":\"null_null\",\"version\":1},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4938,\"ignored\":false,\"lsKind\":\"source id\",\"lsTransaction\":67,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_source id\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1440707916880,\"stringValue\":\"\",\"unitTypeAndKind\":\"null_null\",\"version\":1},{\"codeKind\":\"source\",\"codeOrigin\":\"ACAS DDICT\",\"codeType\":\"component\",\"codeTypeAndKind\":\"component_source\",\"codeValue\":\"ACAS\",\"deleted\":false,\"id\":4935,\"ignored\":false,\"lsKind\":\"source\",\"lsTransaction\":67,\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_source\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1440707916880,\"unitTypeAndKind\":\"null_null\",\"version\":1}],\"modifiedDate\":1440707917536,\"recordedBy\":\"bob\",\"recordedDate\":1440707916880,\"version\":2},{\"deleted\":false,\"id\":1319,\"ignored\":false,\"lsKind\":\"inventory\",\"lsTransaction\":67,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4940,\"ignored\":false,\"lsKind\":\"location\",\"lsTransaction\":67,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1440707916880,\"stringValue\":\"lab\",\"unitTypeAndKind\":\"null_null\",\"version\":1},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4939,\"ignored\":false,\"lsKind\":\"amount made\",\"lsTransaction\":67,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount made\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1440707916880,\"unitKind\":\"mg\",\"unitType\":\"mass\",\"unitTypeAndKind\":\"mass_mg\",\"version\":1}],\"modifiedDate\":1440707917576,\"recordedBy\":\"bob\",\"recordedDate\":1440707916880,\"version\":2}],\"lsTransaction\":67,\"lsType\":\"batch\",\"lsTypeAndKind\":\"batch_linker small molecule\",\"modifiedDate\":1440707917534,\"recordedBy\":\"bob\",\"recordedDate\":1440707916880,\"version\":9},\"version\":1},{\"deleted\":false,\"id\":3531,\"ignored\":false,\"lsKind\":\"batch_parent\",\"lsStates\":[],\"lsType\":\"instantiates\",\"lsTypeAndKind\":\"instantiates_batch_parent\",\"recordedBy\":\"bob\",\"recordedDate\":1447097802490,\"secondLsThing\":{\"codeName\":\"RP000016\",\"deleted\":false,\"id\":3527,\"ignored\":false,\"lsKind\":\"rna\",\"lsLabels\":[{\"deleted\":false,\"id\":83,\"ignored\":false,\"labelText\":\"Pan-AR-35-none-Tetra-Ad(3' passenger)\",\"lsKind\":\"rna\",\"lsTransaction\":213,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_rna\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1447097801443,\"version\":0},{\"deleted\":false,\"id\":215,\"ignored\":false,\"labelText\":\"R000016\",\"lsKind\":\"ACAS LsThing\",\"lsType\":\"corpName\",\"lsTypeAndKind\":\"corpName_ACAS LsThing\",\"physicallyLabled\":true,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1447097801438,\"version\":0}],\"lsStates\":[{\"deleted\":false,\"id\":3795,\"ignored\":false,\"lsKind\":\"rna parent\",\"lsTransaction\":213,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_rna parent\",\"lsValues\":[{\"codeOrigin\":\"ACAS authors\",\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"bob\",\"deleted\":false,\"id\":11437,\"ignored\":false,\"lsKind\":\"scientist\",\"lsTransaction\":213,\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097801474,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":11435,\"ignored\":false,\"lsKind\":\"notebook\",\"lsTransaction\":213,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097801532,\"stringValue\":\"\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":11439,\"ignored\":false,\"lsKind\":\"gene position\",\"lsTransaction\":213,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_gene position\",\"numericValue\":35.000000000000000000,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097801645,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeKind\":\"species\",\"codeOrigin\":\"ACAS DDICT\",\"codeType\":\"rna\",\"codeTypeAndKind\":\"rna_species\",\"codeValue\":\"unassigned\",\"deleted\":false,\"id\":11438,\"ignored\":false,\"lsKind\":\"species\",\"lsTransaction\":213,\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_species\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097801586,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":11440,\"ignored\":false,\"lsKind\":\"passenger strand molecular weight\",\"lsTransaction\":213,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_passenger strand molecular weight\",\"numericValue\":966.817999999999869942,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097801799,\"unitKind\":\"Da\",\"unitType\":\"molecular weight\",\"unitTypeAndKind\":\"molecular weight_Da\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":11430,\"ignored\":false,\"lsKind\":\"unmodified sequence guide strand\",\"lsTransaction\":213,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_unmodified sequence guide strand\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097801719,\"stringValue\":\"ccc\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeKind\":\"target transcript\",\"codeOrigin\":\"ACAS DDICT\",\"codeType\":\"rna\",\"codeTypeAndKind\":\"rna_target transcript\",\"codeValue\":\"Pan-AR\",\"deleted\":false,\"id\":11442,\"ignored\":false,\"lsKind\":\"target transcript\",\"lsTransaction\":213,\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_target transcript\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097801562,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":11431,\"ignored\":false,\"lsKind\":\"guide strand molecular weight\",\"lsTransaction\":213,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_guide strand molecular weight\",\"numericValue\":894.727999999999951797,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097801824,\"unitKind\":\"Da\",\"unitType\":\"molecular weight\",\"unitTypeAndKind\":\"molecular weight_Da\",\"version\":0},{\"codeKind\":\"backbone modification\",\"codeOrigin\":\"ACAS DDICT\",\"codeType\":\"rna\",\"codeTypeAndKind\":\"rna_backbone modification\",\"codeValue\":\"none\",\"deleted\":false,\"id\":11443,\"ignored\":false,\"lsKind\":\"backbone modification\",\"lsTransaction\":213,\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_backbone modification\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097801669,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":11433,\"ignored\":false,\"lsKind\":\"duplex molecular weight\",\"lsTransaction\":213,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_duplex molecular weight\",\"numericValue\":1861.545999999999821739,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097801855,\"unitKind\":\"Da\",\"unitType\":\"molecular weight\",\"unitTypeAndKind\":\"molecular weight_Da\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1447056000000,\"deleted\":false,\"id\":11441,\"ignored\":false,\"lsKind\":\"completion date\",\"lsTransaction\":213,\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097801503,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":11428,\"ignored\":false,\"lsKind\":\"modified sequence guide strand\",\"lsTransaction\":213,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_modified sequence guide strand\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097801774,\"stringValue\":\"\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":11436,\"ignored\":false,\"lsKind\":\"modified sequence passenger strand\",\"lsTransaction\":213,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_modified sequence passenger strand\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097801748,\"stringValue\":\"\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":11429,\"ignored\":false,\"lsKind\":\"batch number\",\"lsTransaction\":213,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_batch number\",\"numericValue\":1.000000000000000000,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097801903,\"unitTypeAndKind\":\"null_null\",\"version\":1},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":11432,\"ignored\":false,\"lsKind\":\"unmodified sequence passenger strand\",\"lsTransaction\":213,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_unmodified sequence passenger strand\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097801693,\"stringValue\":\"aaa\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":11434,\"ignored\":false,\"lsKind\":\"charge density\",\"lsTransaction\":213,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_charge density\",\"numericValue\":465.386499999999955435,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097801879,\"unitKind\":\"g/charge\",\"unitType\":\"charge density\",\"unitTypeAndKind\":\"charge density_g/charge\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":11427,\"ignored\":false,\"lsKind\":\"transcript\",\"lsTransaction\":213,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_transcript\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1447097801610,\"stringValue\":\"\",\"unitTypeAndKind\":\"null_null\",\"version\":0}],\"recordedBy\":\"bob\",\"recordedDate\":1447097801459,\"version\":1}],\"lsTransaction\":213,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_rna\",\"recordedBy\":\"bob\",\"recordedDate\":1447097801438,\"version\":1},\"version\":0}],\"version\":7},\"uniqueName\":true,\"uniqueInteractions\":true,\"orderMatters\":true,\"forwardAndReverseAreSame\":true}}";
		LsThingValidationDTO validationDTO = LsThingValidationDTO.fromJsonToLsThingValidationDTO(json);
		Collection<LsThingValidationErrorMessage> errorMessages = lsThingService.validateLsThing(validationDTO);
		Assert.assertFalse(errorMessages.isEmpty());
		logger.info(LsThingValidationErrorMessage.toJsonArray(errorMessages));
	}

	@Transactional
	@Rollback(value = false)
	public LsThing createParentBatchStack() {
		LsThing componentParent = makeLsThing("COMPONENT-01", "parent", "test component");
		componentParent = addLsThingLabel("COMPONENT-01", "corpName", "ACAS LsThing", true, componentParent);
		componentParent.persist();

		LsThing assemblyParent = makeLsThing("ASSEMBLY-01", "parent", "test assembly");
		assemblyParent = addLsThingLabel("ASSEMBLY-01", "corpName", "ACAS LsThing", true, assemblyParent);
		assemblyParent.persist();

		LsThing componentBatch = makeLsThing("COMPONENT-01-1", "batch", "test component");
		componentBatch = addLsThingLabel("COMPONENT-01-1", "corpName", "ACAS LsThing", true, componentBatch);
		componentBatch.persist();

		LsThing assemblyBatch = makeLsThing("ASSEMBLY-01-1", "batch", "test assembly");
		assemblyBatch = addLsThingLabel("ASSEMBLY-01-1", "corpName", "ACAS LsThing", true, assemblyBatch);
		assemblyBatch.persist();

		ItxLsThingLsThing parentInc = makeItxLsThingLsThing("ITXTHING-01", "incorporates", "assembly_component",
				assemblyParent, componentParent);
		assemblyParent.getSecondLsThings().add(parentInc);
		componentParent.getFirstLsThings().add(parentInc);

		ItxLsThingLsThing batchInc = makeItxLsThingLsThing("ITXTHING-02", "incorporates", "assembly_component",
				assemblyBatch, componentBatch);
		assemblyBatch.getSecondLsThings().add(batchInc);
		componentBatch.getFirstLsThings().add(batchInc);

		ItxLsThingLsThing componentParentBatch = makeItxLsThingLsThing("ITXTHING-01", "instantiates", "batch_parent",
				componentBatch, componentParent);
		componentBatch.getSecondLsThings().add(componentParentBatch);
		componentParent.getFirstLsThings().add(componentParentBatch);

		ItxLsThingLsThing assemblyParentBatch = makeItxLsThingLsThing("ITXTHING-01", "instantiates", "batch_parent",
				assemblyBatch, assemblyParent);
		assemblyBatch.getSecondLsThings().add(assemblyParentBatch);
		assemblyParent.getFirstLsThings().add(assemblyParentBatch);

		assemblyBatch.merge();
		assemblyParent.merge();
		componentBatch.merge();
		componentParent.merge();

		return componentParent;
	}

	private LsThing makeLsThing(String codeName, String lsType, String lsKind) {
		LsThing lsThing = new LsThing();
		lsThing.setCodeName(codeName);
		lsThing.setLsType(lsType);
		lsThing.setLsKind(lsKind);
		lsThing.setRecordedBy("bfielder");
		lsThing.setRecordedDate(new Date());
		lsThing.setDeleted(false);
		lsThing.setIgnored(false);
		return lsThing;
	}

	private LsThing addLsThingLabel(String labelText, String lsType, String lsKind, Boolean preferred,
			LsThing lsThing) {
		LsThingLabel label = new LsThingLabel();
		// (id, label_text, ls_kind, ls_type, ls_type_and_kind, preferred,
		// physically_labled, ignored, deleted, recorded_by, recorded_date, version,
		// lsthing_id)
		label.setLabelText(labelText);
		label.setLsType(lsType);
		label.setLsKind(lsKind);
		label.setRecordedBy("bfielder");
		label.setRecordedDate(new Date());
		label.setDeleted(false);
		label.setIgnored(false);
		label.setPreferred(preferred);
		label.setPhysicallyLabled(false);
		label.setLsThing(lsThing);
		lsThing.getLsLabels().add(label);
		return lsThing;
	}

	public LsThing createTransientParentBatchStack() {

		LsThing componentParent = makeLsThing("COMPONENT-01", "parent", "test component");
		LsThing assemblyParent = makeLsThing("ASSEMBLY-01", "parent", "test assembly");
		LsThing componentBatch = makeLsThing("COMPONENT-01-1", "batch", "test component");
		LsThing assemblyBatch = makeLsThing("ASSEMBLY-01-1", "batch", "test assembly");

		ItxLsThingLsThing parentInc = makeItxLsThingLsThing("ITXTHING-01", "incorporates", "assembly_component",
				assemblyParent, componentParent);
		assemblyParent.getSecondLsThings().add(parentInc);
		componentParent.getFirstLsThings().add(parentInc);

		ItxLsThingLsThing batchInc = makeItxLsThingLsThing("ITXTHING-02", "incorporates", "assembly_component",
				assemblyBatch, componentBatch);
		assemblyBatch.getSecondLsThings().add(batchInc);
		componentBatch.getFirstLsThings().add(batchInc);

		ItxLsThingLsThing componentParentBatch = makeItxLsThingLsThing("ITXTHING-01", "instantiates", "batch_parent",
				componentBatch, componentParent);
		componentBatch.getSecondLsThings().add(componentParentBatch);
		componentParent.getFirstLsThings().add(componentParentBatch);

		ItxLsThingLsThing assemblyParentBatch = makeItxLsThingLsThing("ITXTHING-01", "instantiates", "batch_parent",
				assemblyBatch, assemblyParent);
		assemblyBatch.getSecondLsThings().add(assemblyParentBatch);
		assemblyParent.getFirstLsThings().add(assemblyParentBatch);

		return componentParent;
	}

	@Test
	public void buildBatchParentLsThingStack() {
		LsThing thing1 = createParentBatchStack();
	}

	@Transactional
	@Test
	public void checkBatchDependencies() {
		// LsThing componentParent = createParentBatchStack();
		// logger.info(componentParent.toJsonWithNestedFull());
		LsThing batch = LsThing.findLsThingsByCodeNameEquals("PEG000003-1").getSingleResult();
		logger.info(batch.toJsonWithNestedFull());
		DependencyCheckDTO result = lsThingService.checkBatchDependencies(batch);
		logger.info(result.toJson());
		Assert.assertTrue(result.getLinkedDataExists());
	}

	@Transactional
	@Test
	public void checkParentDependencies() {
		// LsThing componentParent = createParentBatchStack();
		// logger.info(componentParent.toJsonWithNestedFull());
		LsThing parent = LsThing.findLsThingsByCodeNameEquals("PEG000003").getSingleResult();
		DependencyCheckDTO result = lsThingService.checkParentDependencies(parent);
		logger.info(result.toJson());
		Assert.assertTrue(result.getLinkedDataExists());
	}

	@Transactional
	@Test
	public void deleteBatch() {
		LsThing batch = LsThing.findLsThingsByCodeNameEquals("CB000004-6").getSingleResult();
		LsThing parent = LsThing.findLsThingsByCodeNameEquals("CB000004").getSingleResult();
		int lastBatchNumber = lsThingService.getBatchNumber(parent);
		lsThingService.deleteBatch(batch);
		LsThing checkBatch = LsThing.findLsThingsByCodeNameEquals("CB000004-6").getSingleResult();
		LsThing checkParent = LsThing.findLsThingsByCodeNameEquals("CB000004").getSingleResult();
		int newLastBatchNumber = lsThingService.getBatchNumber(checkParent);
		logger.info(checkBatch.toJsonWithNestedFull());
		Assert.assertTrue(checkBatch.isIgnored());
		Assert.assertTrue(checkBatch.isDeleted());
		for (ItxLsThingLsThing itxLsThingLsThing : checkBatch.getFirstLsThings()) {
			Assert.assertTrue(itxLsThingLsThing.isIgnored());
			Assert.assertTrue(itxLsThingLsThing.isDeleted());
		}
		for (ItxLsThingLsThing itxLsThingLsThing : checkBatch.getSecondLsThings()) {
			Assert.assertTrue(itxLsThingLsThing.isIgnored());
			Assert.assertTrue(itxLsThingLsThing.isDeleted());
		}
		logger.info("Old lastBatchNumber: " + lastBatchNumber + " newLastBatchNumber: " + newLastBatchNumber);
		Assert.assertTrue(newLastBatchNumber == lastBatchNumber - 1);
	}

	@Transactional
	@Test
	public void deleteParent() {
		LsThing parent = LsThing.findLsThingsByCodeNameEquals("PEG000003").getSingleResult();
		logger.info(parent.toJsonWithNestedFull());
		String lastCorpName = autoLabelService.getLastLabel(parent.getLsTypeAndKind(), "corpName_ACAS LsThing")
				.getAutoLabel();
		lsThingService.deleteParent(parent);
		LsThing checkParent = LsThing.findLsThingsByCodeNameEquals("PEG000003").getSingleResult();
		logger.info(checkParent.toJsonWithNestedFull());
		Assert.assertTrue(checkParent.isIgnored());
		Assert.assertTrue(checkParent.isDeleted());
		for (ItxLsThingLsThing itxLsThingLsThing : checkParent.getFirstLsThings()) {
			Assert.assertTrue(itxLsThingLsThing.isIgnored());
			Assert.assertTrue(itxLsThingLsThing.isDeleted());
			if (itxLsThingLsThing.getLsType().equals("instantiates")) {
				LsThing checkBatch = itxLsThingLsThing.getFirstLsThing();
				Assert.assertTrue(checkBatch.isIgnored());
				Assert.assertTrue(checkBatch.isDeleted());
				for (ItxLsThingLsThing batchItxLsThingLsThing : checkBatch.getFirstLsThings()) {
					Assert.assertTrue(itxLsThingLsThing.isIgnored());
					Assert.assertTrue(itxLsThingLsThing.isDeleted());
				}
				for (ItxLsThingLsThing batchItxLsThingLsThing : checkBatch.getSecondLsThings()) {
					Assert.assertTrue(itxLsThingLsThing.isIgnored());
					Assert.assertTrue(itxLsThingLsThing.isDeleted());
				}
			}
		}
		for (ItxLsThingLsThing itxLsThingLsThing : checkParent.getSecondLsThings()) {
			Assert.assertTrue(itxLsThingLsThing.isIgnored());
			Assert.assertTrue(itxLsThingLsThing.isDeleted());
		}
		String newLastCorpName = autoLabelService.getLastLabel(parent.getLsTypeAndKind(), "corpName_ACAS LsThing")
				.getAutoLabel();
		logger.info("Old lastCorpName: " + lastCorpName + " New lastCorpName: " + newLastCorpName);
		Assert.assertFalse(lastCorpName.equals(newLastCorpName));
	}

	@Transactional
	@Test
	public void getStoichiometryProperties() {
		String codeName = "CMPD-00000002";
		String lsType = "parent";
		String lsKind = "small molecule";
		CodeTypeKindDTO codeDTO = new CodeTypeKindDTO(codeName, lsType, lsKind);
		Collection<CodeTypeKindDTO> codeDTOs = new ArrayList<CodeTypeKindDTO>();
		codeDTOs.add(codeDTO);
		StoichiometryPropertiesResultsDTO result = lsThingService.getStoichiometryProperties(codeDTOs);
		logger.info(result.toJson());
		Assert.assertFalse(result.isHasError());
		Assert.assertFalse(result.isHasWarning());
		Assert.assertTrue(result.getResults().size() > 0);
	}

	@Test
	@Transactional
	public void substructureSearch() {
		String queryMol = "\n  Mrv1641110051619032D          \n\n  5  5  0  0  0  0            999 V2000\n   -0.0446    0.6125    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n   -0.7121    0.1274    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n   -0.4572   -0.6572    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n    0.3679   -0.6572    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n    0.6228    0.1274    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n  2  3  1  0  0  0  0\n  3  4  1  0  0  0  0\n  4  5  1  0  0  0  0\n  1  2  1  0  0  0  0\n  1  5  1  0  0  0  0\nM  END\n";
		Collection<LsThing> searchResults = lsThingService.structureSearch(queryMol, "", "", SearchType.SUBSTRUCTURE,
				10, null);
		Assert.assertFalse(searchResults.isEmpty());
		logger.debug(LsThing.toJsonArray(searchResults));

		queryMol = "\n  MJ160418                      \n\n 20 23  0  0  0  0  0  0  0  0999 V2000\n   -0.9620   -1.3787    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n   -1.2169   -2.1634    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n   -2.0419   -2.1634    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n   -2.2968   -1.3787    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n   -1.6294   -0.8938    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n   -1.6294   -0.0688    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n   -0.9620    0.4160    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n   -1.2169    1.2006    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n   -2.0419    1.2006    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n   -2.2968    0.4160    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n   -0.1773    0.1611    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n    0.4900    0.6460    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n    1.1574    0.1611    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n    0.9025   -0.6235    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n    0.0775   -0.6235    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n    0.4900    1.4710    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n   -0.1773    1.9559    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n    0.0775    2.7405    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n    0.9025    2.7405    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n    1.1574    1.9559    0.0000 Si  0  0  0  0  0  0  0  0  0  0  0  0\n 18 19  1  0  0  0  0\n 17 18  1  0  0  0  0\n 16 17  1  0  0  0  0\n 19 20  1  0  0  0  0\n 16 20  1  0  0  0  0\n 12 16  1  0  0  0  0\n 11 12  1  0  0  0  0\n  7 11  1  0  0  0  0\n  7  8  1  0  0  0  0\n  8  9  1  0  0  0  0\n  9 10  1  0  0  0  0\n  6  7  1  0  0  0  0\n  6 10  1  0  0  0  0\n  5  6  1  0  0  0  0\n  4  5  1  0  0  0  0\n  3  4  1  0  0  0  0\n  2  3  1  0  0  0  0\n  1  5  1  0  0  0  0\n  1  2  1  0  0  0  0\n 11 15  1  0  0  0  0\n 14 15  1  0  0  0  0\n 12 13  1  0  0  0  0\n 13 14  1  0  0  0  0\nM  END\n";
		searchResults = lsThingService.structureSearch(queryMol, "", "", SearchType.SUBSTRUCTURE, 10, null);
		Assert.assertTrue(searchResults.isEmpty());
	}

}
