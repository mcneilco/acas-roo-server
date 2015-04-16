

package com.labsynch.labseer.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.InteractionType;
import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.ItxLsThingLsThingState;
import com.labsynch.labseer.domain.ItxLsThingLsThingValue;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.domain.ThingKind;
import com.labsynch.labseer.domain.ThingType;
import com.labsynch.labseer.dto.PreferredNameDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.exceptions.UniqueNameException;
import com.labsynch.labseer.utils.ExcludeNulls;
import com.labsynch.labseer.utils.PropertiesUtilService;

import flexjson.JSONSerializer;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class LsThingServiceTests {

	private static final Logger logger = LoggerFactory.getLogger(LsThingServiceTests.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
	@Autowired
	private LsThingService lsThingService;
	
	@Transactional
	@Rollback(value=false)
	public LsThing createLsThingStack(){
		//register types and kinds
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
		thing1.persist();
		
		LsThing thing2 = new LsThing();
		thing2.setCodeName("THING-02");
		thing2.setLsType("thing");
		thing2.setLsKind("test thing");
		thing2.setRecordedBy("bfielder");
		thing2.setRecordedDate(new Date());
		thing2.persist();
		
		LsThing thing3 = new LsThing();
		thing3.setCodeName("THING-03");
		thing3.setLsType("thing");
		thing3.setLsKind("test thing");
		thing3.setRecordedBy("bfielder");
		thing3.setRecordedDate(new Date());
		thing3.persist();
		
		ItxLsThingLsThing inc12 = makeItxLsThingLsThing("ITXTHING-01", "incorporates", "assembly_component", thing1, thing2);
		thing1.getSecondLsThings().add(inc12);
		inc12.persist();
		thing1.merge();
		
		return thing1;
		
	}
	
	public ItxLsThingLsThing makeItxLsThingLsThing(String codeName, String lsType, String lsKind, LsThing firstLsThing, LsThing secondLsThing){
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
		Set<ItxLsThingLsThingState> itxStates = new HashSet<ItxLsThingLsThingState>();
		Set<ItxLsThingLsThingValue> itxValues = new HashSet<ItxLsThingLsThingValue>();
		itxValues.add(itxValue);
		itxState.setLsValues(itxValues);
		itxStates.add(itxState);
		itx.setLsStates(itxStates);
		return itx;
	}
	
	@Transactional
	@Rollback(value=false)
	public void deleteLsThingStack(){
		try{
			ItxLsThingLsThing inc12 = ItxLsThingLsThing.findItxLsThingLsThingsByCodeNameEquals("ITXTHING-01").getSingleResult();
			for (ItxLsThingLsThingState state : inc12.getLsStates()){
				for (ItxLsThingLsThingValue value: state.getLsValues()){
					value.remove();
				}
				state.remove();
			}
			inc12.remove();
		}catch (Exception e){}
		
		try{
			ItxLsThingLsThing inc13 = ItxLsThingLsThing.findItxLsThingLsThingsByCodeNameEquals("ITXTHING-02").getSingleResult();
			for (ItxLsThingLsThingState state : inc13.getLsStates()){
				for (ItxLsThingLsThingValue value: state.getLsValues()){
					value.remove();
				}
				state.remove();
			}
			inc13.remove();
		} catch (Exception e){}
		try{
			LsThing thing1 = LsThing.findLsThingsByCodeNameEquals("THING-01").getSingleResult();
			thing1.remove();
		}catch (Exception e){}
		try{
			LsThing thing2 = LsThing.findLsThingsByCodeNameEquals("THING-02").getSingleResult();
			thing2.remove();
		}catch (Exception e){}
		try{
			LsThing thing3 = LsThing.findLsThingsByCodeNameEquals("THING-03").getSingleResult();
			thing3.remove();
		}catch (Exception e){}
		try{
			ThingKind thingKind1 = ThingKind.findThingKindsByLsTypeAndKindEquals("thing_test thing").getSingleResult();
			thingKind1.remove();
		}catch (Exception e){}
		try{
			ThingType thingType1 = ThingType.findThingTypesByTypeNameEquals("thing").getSingleResult();
			thingType1.remove();
		}catch (Exception e){}
//		try{
//		}catch (Exception e){}


	}
	
	@Test
	public void buildAndRemoveLsThingStack(){
		try{
			LsThing thing1 = createLsThingStack();
		}catch (Exception e){
			deleteLsThingStack();
		}
		deleteLsThingStack();
	}
	
	@Test
	@Transactional
	@Rollback(value=false)
	public void updateInteraction(){
		LsThing thing1 = createLsThingStack();
		LsThing thing2 = LsThing.findLsThingsByCodeNameEquals("THING-02").getSingleResult();
		LsThing thing3 = LsThing.findLsThingsByCodeNameEquals("THING-03").getSingleResult();
		ItxLsThingLsThing inc12 = ItxLsThingLsThing.findItxLsThingLsThingsByCodeNameEquals("ITXTHING-01").getSingleResult();
		ItxLsThingLsThing inc13 = makeItxLsThingLsThing("ITXTHING-02", "incorporates", "assembly_component", thing1, thing3);
		inc12.setIgnored(true);
		thing1.getSecondLsThings().add(inc13);
		Assert.assertEquals(2, thing1.getSecondLsThings().size());
		for (ItxLsThingLsThing itx : thing1.getSecondLsThings()){
			logger.info(itx.getCodeName());
			if (itx.getId() != null) logger.info("id: "+itx.getId().toString());
			else logger.info("id is null");
		}
		LsThing updatedThing1 = lsThingService.updateLsThing(thing1);
		logger.info(updatedThing1.toJsonWithNestedFull());
		Assert.assertEquals(2, updatedThing1.getSecondLsThings().size());
		for (ItxLsThingLsThing itx : updatedThing1.getSecondLsThings()){
			Assert.assertEquals(1, itx.getLsStates().size());
		}
		
//		deleteLsThingStack();
	}

	//@Test
	public void Test_1(){
		
		String json = "{\"requests\":[{\"requestName\":\"CRA-025995-1\"},{\"requestName\":\"CMPD-0000052-01\"}]}";
		PreferredNameDTO request = PreferredNameDTO.fromJsonToPreferredNameDTO(json);	

		logger.info("#############################################");

		
	}
	
	//@Test
	public void Test_2(){
		
		String json = "{\"requests\":[{\"requestName\":\"CRA-025995-1\"},{\"requestName\":\"9\"}]}";

		PreferredNameResultsDTO results = lsThingService.getGeneCodeNameFromName(json);
		logger.info("#############################################");
		logger.info("results: " + results.toJson());

		
	}
	
	@Test
	public void Test_3(){
		
		String json = "{\"requests\":[{\"requestName\":\"1\"}, {\"requestName\":\"2\"}, {\"requestName\":\"15\"}, {\"requestName\":\"blah\"}]}";

        String thingType = "gene";
        String thingKind = "entrez gene";
        String labelType = "name";
        String labelKind = "Entrez Gene ID";
        logger.info("getGeneCodeNameFromNameRequest incoming json: " + json);
        PreferredNameResultsDTO results = lsThingService.getPreferredNameFromName(thingType, thingKind, labelType, labelKind, json);
		logger.info("#############################################");
		logger.info("results: " + results.toJson());

		
	}
	
	@Test
	@Transactional
	public void findItxLsThingLsThingsByLsTypeEqualsAndLsKindEqualsAndFirstLsThingEquals(){
		//TODO
	}
	
	@Test
	@Transactional
	public void findItxLsThingLsThingsByLsTypeEqualsAndLsKindEqualsAndSecondLsThingEquals(){
		//TODO
	}
	
	@Test
	@Transactional
	public void retrieveOrder(){
		//TODO
	}
	
	@Test
	@Transactional
	public void validateComponent(){
		//TODO
	}
	
	@Test
	@Transactional
	public void validateAssembly(){
		//TODO
	}
	
	@Test
	public void saveLSMParent() throws UniqueNameException{
    	String json = "{\"codeName\":\"LSM000001\",\"ignored\":false,\"lsKind\":\"linker small molecule\",\"lsLabels\":[{\"ignored\":false,\"imageFile\":null,\"labelText\":\"Ad\",\"lsKind\":\"linker small molecule\",\"lsTransaction\":1,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_linker small molecule\",\"modifiedDate\":null,\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"jane\",\"recordedDate\":1375141504000}],\"lsStates\":[{\"comments\":null,\"ignored\":false,\"lsKind\":\"linker small molecule parent\",\"lsTransaction\":1,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_linker small molecule parent\",\"lsValues\":[{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":1342080000000,\"fileValue\":null,\"ignored\":false,\"lsTransaction\":128,\"modifiedDate\":null,\"numericValue\":null,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":null,\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"completion date\",\"valueOperator\":null,\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"valueUnit\":null},{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"ignored\":false,\"lsTransaction\":127,\"modifiedDate\":null,\"numericValue\":null,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":\"Notebook 1\",\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"notebook\",\"valueOperator\":null,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"valueUnit\":null},{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"ignored\":false,\"lsTransaction\":128,\"modifiedDate\":null,\"numericValue\":231,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":null,\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"molecular weight\",\"valueOperator\":null,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_molecular weight\",\"unitKind\":\"g/mol\",\"unitType\":\"molecular weight\",\"valueUnit\":null},{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"ignored\":false,\"lsTransaction\":128,\"modifiedDate\":null,\"numericValue\":0,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":null,\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"batch number\",\"valueOperator\":null,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_batch number\",\"unitKind\":null,\"unitType\":null,\"valueUnit\":null}],\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jane\",\"recordedDate\":1375141460000}],\"lsTransaction\":1,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_linker small molecule\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jane\",\"recordedDate\":1375141508000,\"shortDescription\":\" \"}";
		LsThing lsThing = LsThing.fromJsonToLsThing(json);
		lsThing = lsThingService.saveLsThing(lsThing, false);
	}
	
	@Test
	@Transactional
	public void generateBatchCodeName(){
		LsThing parent = LsThing.findLsThingsByCodeNameEquals("LSM000001").getSingleResult();
		String batchCodeName = lsThingService.generateBatchCodeName(parent);
		Assert.assertEquals("LSM000001-1", batchCodeName);
	}
	
	@Test
	public void saveLSMBatch() throws UniqueNameException{
    	String json = "{\"codeName\":null,\"id\":11,\"ignored\":false,\"lsKind\":\"linker small molecule\",\"lsStates\":[{\"comments\":null,\"id\":11,\"ignored\":false,\"lsKind\":\"linker small molecule batch\",\"lsTransaction\":1,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_linker small molecule batch\",\"lsValues\":[{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":1342080000000,\"fileValue\":null,\"id\":12,\"ignored\":false,\"lsTransaction\":128,\"modifiedDate\":null,\"numericValue\":null,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":null,\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"completion date\",\"valueOperator\":null,\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"valueUnit\":null,\"version\":0},{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"id\":13,\"ignored\":false,\"lsTransaction\":127,\"modifiedDate\":null,\"numericValue\":null,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":\"Notebook 1\",\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"notebook\",\"valueOperator\":null,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"valueUnit\":null,\"version\":0}],\"recordedDate\":1363388477000,\"recordedBy\":\"jane\"},{\"comments\":null,\"id\":111,\"ignored\":false,\"lsKind\":\"inventory\",\"lsTransaction\":1,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"id\":14,\"ignored\":false,\"lsTransaction\":128,\"modifiedDate\":null,\"numericValue\":2.3,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":null,\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"amount\",\"valueOperator\":null,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount\",\"unitKind\":\"g\",\"unitType\":\"mass\",\"valueUnit\":null,\"version\":0},{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"id\":15,\"ignored\":false,\"lsTransaction\":127,\"modifiedDate\":null,\"numericValue\":null,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":\"Cabinet 1\",\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"location\",\"valueOperator\":null,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"valueUnit\":null,\"version\":0}],\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jane\",\"recordedDate\":1375141460000,\"version\":0}],\"lsTransaction\":1,\"lsType\":\"batch\",\"lsTypeAndKind\":\"batch_linker small molecule\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jane\",\"recordedDate\":1375141508000,\"shortDescription\":\" \",\"version\":0}";
		LsThing lsThing = LsThing.fromJsonToLsThing(json);
    	LsThing parent = LsThing.findLsThingsByCodeNameEquals("LSM000001").getSingleResult();
    	lsThing.setCodeName(lsThingService.generateBatchCodeName(parent));
		lsThing = lsThingService.saveLsThing(lsThing, false, true, parent.getId());
	}
	
	@Test
	public void getComponentBatches() {
    	LsThing parent = LsThing.findLsThingsByCodeNameEquals("LSM000001").getSingleResult();
		Collection<LsThing> batches = lsThingService.findBatchesByParentEquals(parent);
		Assert.assertEquals(1, batches.size());
	}
	
	@Test
	public void findLsThingLabels() {
		String queryString ="linker";
		Collection<LsThingLabel> lsThingLabels = LsThingLabel.findLsThingLabelsByLabelTextLike(queryString).getResultList();
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
	public void toFullJson(){
		LsThing lsThing = LsThing.findLsThingsByCodeNameEquals("PROT000006").getSingleResult();
		
		
		String json1 = new JSONSerializer().
					exclude("*.class","lsStates.lsThing","lsLabels.lsThing"
							,"firstLsThings.secondLsThing","secondLsThings.firstLsThing"
//							,"firstLsThings.firstLsThing.firstLsThings", "firstLsThings.firstLsThing.secondLsThings"
							)
					.include("lsLabels", "lsStates.lsValues"
							,"secondLsThings","firstLsThings"
//							,"firstLsThings.firstLsThing"
							)
					.transform(new ExcludeNulls(), void.class)
					.serialize(lsThing);
		logger.info(json1);
		
//		logger.info(lsThing.toFullJson());
	}
	
	@Test
	@Transactional
    public void toJsonTest() {
		LsThing lsThing = LsThing.findLsThingsByCodeNameEquals("PROT000006-1").getSingleResult();
		
        String json = new JSONSerializer().
        		exclude("*.class").
        		include("lsTags", "lsLabels", "lsStates.lsValues", "firstLsThings","secondLsThings").
        		transform(new ExcludeNulls(), void.class).
        		serialize(lsThing);
        
        logger.info(json);
    }
	
//	public String toFullJson() {
//        return new JSONSerializer().
//        		exclude("*.class", "lsStates.analysisGroup", "lsLabels.analysisGroup", "treatmentGroups.analysisGroup", "experiment.protocol")
//        		.include("lsLabels", "lsStates.lsValues", "treatmentGroups.lsStates.lsValues", "treatmentGroups.lsLabels", "treatmentGroups.subjects.lsStates.lsValues", "treatmentGroups.subjects.lsLabels")
//        		.transform(new ExcludeNulls(), void.class).serialize(this);
//    }
	
	@Test
	@Transactional
	public void saveParentAndBatch() {
//		String json = "{\"codeName\": null,\"firstLsThings\": [{\"deleted\": false,\"firstLsThing\": {\"codeName\": \"PRTN-000012-1\",\"deleted\": false,\"ignored\": false,\"lsKind\": \"protein\",\"lsType\": \"batch\",\"lsTypeAndKind\": \"batch_protein\",\"recordedBy\": \"bob\",\"recordedDate\": 1424730027913,\"version\": null},\"id\": null,\"ignored\": false,\"lsKind\": \"batch_parent\",\"lsType\": \"instantiates\",\"lsTypeAndKind\": \"instantiates_batch_parent\",\"recordedBy\": \"bob\",\"recordedDate\": 1424730027913,\"version\": null}],\"ignored\": false,\"lsKind\": \"protein\",\"lsLabels\": [{ \"ignored\": false,\"imageFile\": null,\"labelText\": \"Fielderase\",\"lsKind\": \"protein\",\"lsTransaction\": 1,\"lsType\": \"name\",\"lsTypeAndKind\": \"name_protein\",\"modifiedDate\": null,\"physicallyLabled\": false,\"preferred\": true,\"recordedBy\": \"jane\",\"recordedDate\": 1375141504000}],\"lsStates\": [{ \"comments\": null,\"ignored\": false,\"lsKind\": \"protein parent\",\"lsTransaction\": 1,\"lsType\": \"metadata\",\"lsTypeAndKind\": \"metadata_protein parent\",\"lsValues\": [{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": 1342080000000, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"completion date\", \"lsTransaction\": 128, \"lsType\": \"dateValue\", \"lsTypeAndKind\": \"dateValue_completion date\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"notebook\", \"lsTransaction\": 127, \"lsType\": \"stringValue\", \"lsTypeAndKind\": \"stringValue_notebook\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": \"Notebook 1\", \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"molecular weight\", \"lsTransaction\": 128, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_molecular weight\", \"modifiedDate\": null, \"numericValue\": 231, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": \"g/mol\", \"unitType\": \"molecular weight\", \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"batch number\", \"lsTransaction\": 128, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_batch number\", \"modifiedDate\": null, \"numericValue\": 0, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": null, \"unitType\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null} ],\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141460000}],\"lsTransaction\": 1,\"lsType\": \"parent\",\"lsTypeAndKind\": \"parent_protein\",\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141508000,\"shortDescription\": \" \"}";
    	String json = "{\"codeName\": null,\"firstLsThings\": [{\"deleted\": false,\"firstLsThing\": {\"codeName\": \"PRTN-000013-1\",\"deleted\": false,\"ignored\": false,\"lsKind\": \"protein\",\"lsStates\":[{\"deleted\":false,\"ignored\":false,\"lsKind\":\"inventory\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"location\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729705505,\"stringValue\":\"123\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"amount made\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount made\",\"numericValue\":1123,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729706303,\"unitKind\":\"g\",\"unitType\":\"mass\",\"unitTypeAndKind\":\"mass_g\",\"version\":0}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"version\":1},{\"deleted\":false,\"ignored\":false,\"lsKind\":\"protein batch\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_protein batch\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"hplc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_hplc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeOrigin\":\"ACAS authors\",\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"egao\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"scientist\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702585,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"DeleteExperimentInstructions (6).pdf\",\"ignored\":false,\"lsKind\":\"gpc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_gpc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1424678400000,\"deleted\":false,\"ignored\":false,\"lsKind\":\"completion date\",\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703242,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"ms\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_ms\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeKind\":\"source\",\"codeOrigin\":\"ACAS DDICT\",\"codeType\":\"component\",\"codeTypeAndKind\":\"component_source\",\"codeValue\":\"Avidity\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"source\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_source\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729672870,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"purity\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_purity\",\"numericValue\":129,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729708062,\"unitKind\":\"% purity\",\"unitType\":\"percentage\",\"unitTypeAndKind\":\"percentage_% purity\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"nmr\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_nmr\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"notebook\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703953,\"stringValue\":\"test\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"source id\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_source id\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702610,\"stringValue\":\"\",\"unitTypeAndKind\":\"null_null\",\"version\":0}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"version\":1}],\"lsType\": \"batch\",\"lsTypeAndKind\": \"batch_protein\",\"recordedBy\": \"jane\",\"recordedDate\": 1424730027913,\"version\": null},\"id\": null,\"ignored\": false,\"lsKind\": \"batch_parent\",\"lsType\": \"instantiates\",\"lsTypeAndKind\": \"instantiates_batch_parent\",\"recordedBy\": \"jane\",\"recordedDate\": 1424730027913,\"version\": null}],\"ignored\": false,\"lsKind\": \"protein\",\"lsLabels\": [{ \"ignored\": false,\"imageFile\": null,\"labelText\": \"Fielderase\",\"lsKind\": \"protein\",\"lsTransaction\": 9999,\"lsType\": \"name\",\"lsTypeAndKind\": \"name_protein\",\"modifiedDate\": null,\"physicallyLabled\": false,\"preferred\": true,\"recordedBy\": \"jane\",\"recordedDate\": 1375141504000}],\"lsStates\": [{ \"comments\": null,\"ignored\": false,\"lsKind\": \"protein parent\",\"lsTransaction\": 9999,\"lsType\": \"metadata\",\"lsTypeAndKind\": \"metadata_protein parent\",\"lsValues\": [{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": 1342080000000, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"completion date\", \"lsTransaction\": 9999, \"lsType\": \"dateValue\", \"lsTypeAndKind\": \"dateValue_completion date\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"notebook\", \"lsTransaction\": 9999, \"lsType\": \"stringValue\", \"lsTypeAndKind\": \"stringValue_notebook\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": \"Notebook 1\", \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"molecular weight\", \"lsTransaction\": 9999, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_molecular weight\", \"modifiedDate\": null, \"numericValue\": 231, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": \"g/mol\", \"unitType\": \"molecular weight\", \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"batch number\", \"lsTransaction\": 9999, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_batch number\", \"modifiedDate\": null, \"numericValue\": 0, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": null, \"unitType\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null} ],\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141460000}],\"lsTransaction\": 9999,\"lsType\": \"parent\",\"lsTypeAndKind\": \"parent_protein\",\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141508000,\"shortDescription\": \" \"}";
		LsThing lsThing = LsThing.fromJsonToLsThing(json);
    	try{
    		LsThing savedLsThing = lsThingService.saveLsThing(lsThing);
    		logger.info(savedLsThing.toJsonWithNestedFull());
    	} catch (UniqueNameException e){
    		logger.error("unique name exception");
    	}
	}
	
	@Test
	@Transactional
	public void updateParentAndBatch() {
    	String json = "{\"codeName\":\"PRTN000015\",\"deleted\":false,\"firstLsThings\":[{\"deleted\":false,\"firstLsThing\":{\"codeName\":\"PRTN000015-1\",\"deleted\":false,\"id\":1493852,\"ignored\":false,\"lsKind\":\"protein\",\"lsStates\":[{\"comments\":\"test comment 2\",\"deleted\":false,\"id\":2634316,\"ignored\":false,\"lsKind\":\"protein batch\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_protein batch\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"edited ms file value\",\"id\":5926370,\"ignored\":false,\"lsKind\":\"ms\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_ms\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"id\":5926365,\"ignored\":false,\"lsKind\":\"nmr\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_nmr\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"DeleteExperimentInstructions (6).pdf\",\"id\":5926369,\"ignored\":false,\"lsKind\":\"gpc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_gpc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1424678400000,\"deleted\":false,\"id\":5926368,\"ignored\":false,\"lsKind\":\"completion date\",\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703242,\"unitTypeAndKind\":\"null_null\"},{\"codeKind\":\"source\",\"codeOrigin\":\"ACAS DDICT\",\"codeType\":\"component\",\"codeTypeAndKind\":\"component_source\",\"codeValue\":\"Avidity Edited\",\"deleted\":false,\"id\":5926373,\"ignored\":false,\"lsKind\":\"source\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_source\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729672870,\"unitTypeAndKind\":\"null_null\"},{\"codeOrigin\":\"ACAS authors\",\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"egao\",\"deleted\":false,\"id\":5926367,\"ignored\":false,\"lsKind\":\"scientist\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702585,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926372,\"ignored\":false,\"lsKind\":\"purity\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_purity\",\"numericValue\":129,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729708062,\"unitKind\":\"% purity\",\"unitType\":\"percentage\",\"unitTypeAndKind\":\"percentage_% purity\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"id\":5926371,\"ignored\":false,\"lsKind\":\"hplc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_hplc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926364,\"ignored\":false,\"lsKind\":\"notebook\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703953,\"stringValue\":\"test\",\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926366,\"ignored\":false,\"lsKind\":\"source id\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_source id\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702610,\"stringValue\":\"test source id\",\"unitTypeAndKind\":\"null_null\"}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940},{\"deleted\":false,\"id\":2634315,\"ignored\":false,\"lsKind\":\"inventory\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926362,\"ignored\":false,\"lsKind\":\"location\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729705505,\"stringValue\":\"123\",\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926363,\"ignored\":false,\"lsKind\":\"amount made\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount made\",\"numericValue\":1123,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729706303,\"unitKind\":\"g\",\"unitType\":\"mass\",\"unitTypeAndKind\":\"mass_g\"}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940}],\"lsType\":\"batch\",\"lsTypeAndKind\":\"batch_protein\",\"recordedBy\":\"jane\",\"recordedDate\":1424730027913},\"id\":1493853,\"ignored\":false,\"lsKind\":\"batch_parent\",\"lsType\":\"instantiates\",\"lsTypeAndKind\":\"instantiates_batch_parent\",\"recordedBy\":\"jane\",\"recordedDate\":1424730027913}],\"id\":1493851,\"ignored\":false,\"lsKind\":\"protein\",\"lsLabels\":[{\"deleted\":false,\"id\":297595,\"ignored\":false,\"labelText\":\"Fielderase Z\",\"lsKind\":\"protein\",\"lsTransaction\":9999,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_protein\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"jane\",\"recordedDate\":1375141504000}],\"lsStates\":[{\"deleted\":false,\"id\":2634314,\"ignored\":false,\"lsKind\":\"protein parent\",\"lsTransaction\":9999,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_protein parent\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926359,\"ignored\":false,\"lsKind\":\"notebook\",\"lsTransaction\":9999,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"stringValue\":\"Notebook 1\",\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926361,\"ignored\":false,\"lsKind\":\"molecular weight\",\"lsTransaction\":9999,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_molecular weight\",\"numericValue\":890,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"unitKind\":\"g/mol\",\"unitType\":\"molecular weight\",\"unitTypeAndKind\":\"molecular weight_g/mol\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926360,\"ignored\":false,\"lsKind\":\"batch number\",\"lsTransaction\":9999,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_batch number\",\"numericValue\":0,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1342080000000,\"deleted\":false,\"id\":5926358,\"ignored\":false,\"lsKind\":\"completion date\",\"lsTransaction\":9999,\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"unitTypeAndKind\":\"null_null\"}],\"recordedBy\":\"jane\",\"recordedDate\":1375141460000}],\"lsTags\":[],\"lsTransaction\":9999,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_protein\",\"recordedBy\":\"jane\",\"recordedDate\":1375141508000,\"secondLsThings\":[]}";
		LsThing lsThing = LsThing.fromJsonToLsThing(json);
		LsThing savedLsThing = lsThingService.updateLsThing(lsThing);
		logger.info(savedLsThing.toJsonWithNestedFull());
	}

	@Test
	@Transactional
	public void toJsonWithNested() {
//		String json = "{\"codeName\": null,\"firstLsThings\": [{\"deleted\": false,\"firstLsThing\": {\"codeName\": \"PRTN-000012-1\",\"deleted\": false,\"ignored\": false,\"lsKind\": \"protein\",\"lsType\": \"batch\",\"lsTypeAndKind\": \"batch_protein\",\"recordedBy\": \"bob\",\"recordedDate\": 1424730027913,\"version\": null},\"id\": null,\"ignored\": false,\"lsKind\": \"batch_parent\",\"lsType\": \"instantiates\",\"lsTypeAndKind\": \"instantiates_batch_parent\",\"recordedBy\": \"bob\",\"recordedDate\": 1424730027913,\"version\": null}],\"ignored\": false,\"lsKind\": \"protein\",\"lsLabels\": [{ \"ignored\": false,\"imageFile\": null,\"labelText\": \"Fielderase\",\"lsKind\": \"protein\",\"lsTransaction\": 1,\"lsType\": \"name\",\"lsTypeAndKind\": \"name_protein\",\"modifiedDate\": null,\"physicallyLabled\": false,\"preferred\": true,\"recordedBy\": \"jane\",\"recordedDate\": 1375141504000}],\"lsStates\": [{ \"comments\": null,\"ignored\": false,\"lsKind\": \"protein parent\",\"lsTransaction\": 1,\"lsType\": \"metadata\",\"lsTypeAndKind\": \"metadata_protein parent\",\"lsValues\": [{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": 1342080000000, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"completion date\", \"lsTransaction\": 128, \"lsType\": \"dateValue\", \"lsTypeAndKind\": \"dateValue_completion date\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"notebook\", \"lsTransaction\": 127, \"lsType\": \"stringValue\", \"lsTypeAndKind\": \"stringValue_notebook\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": \"Notebook 1\", \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"molecular weight\", \"lsTransaction\": 128, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_molecular weight\", \"modifiedDate\": null, \"numericValue\": 231, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": \"g/mol\", \"unitType\": \"molecular weight\", \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"batch number\", \"lsTransaction\": 128, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_batch number\", \"modifiedDate\": null, \"numericValue\": 0, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": null, \"unitType\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null} ],\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141460000}],\"lsTransaction\": 1,\"lsType\": \"parent\",\"lsTypeAndKind\": \"parent_protein\",\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141508000,\"shortDescription\": \" \"}";
    	String json = "{\"codeName\": null,\"firstLsThings\": [{\"deleted\": false,\"firstLsThing\": {\"codeName\": \"PRTN-000013-1\",\"deleted\": false,\"ignored\": false,\"lsKind\": \"protein\",\"lsStates\":[{\"deleted\":false,\"ignored\":false,\"lsKind\":\"inventory\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"location\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729705505,\"stringValue\":\"123\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"amount made\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount made\",\"numericValue\":1123,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729706303,\"unitKind\":\"g\",\"unitType\":\"mass\",\"unitTypeAndKind\":\"mass_g\",\"version\":0}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"version\":1},{\"deleted\":false,\"ignored\":false,\"lsKind\":\"protein batch\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_protein batch\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"hplc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_hplc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeOrigin\":\"ACAS authors\",\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"egao\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"scientist\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702585,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"DeleteExperimentInstructions (6).pdf\",\"ignored\":false,\"lsKind\":\"gpc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_gpc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1424678400000,\"deleted\":false,\"ignored\":false,\"lsKind\":\"completion date\",\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703242,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"ms\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_ms\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeKind\":\"source\",\"codeOrigin\":\"ACAS DDICT\",\"codeType\":\"component\",\"codeTypeAndKind\":\"component_source\",\"codeValue\":\"Avidity\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"source\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_source\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729672870,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"purity\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_purity\",\"numericValue\":129,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729708062,\"unitKind\":\"% purity\",\"unitType\":\"percentage\",\"unitTypeAndKind\":\"percentage_% purity\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"nmr\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_nmr\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"notebook\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703953,\"stringValue\":\"test\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"source id\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_source id\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702610,\"stringValue\":\"\",\"unitTypeAndKind\":\"null_null\",\"version\":0}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"version\":1}],\"lsType\": \"batch\",\"lsTypeAndKind\": \"batch_protein\",\"recordedBy\": \"jane\",\"recordedDate\": 1424730027913,\"version\": null},\"id\": null,\"ignored\": false,\"lsKind\": \"batch_parent\",\"lsType\": \"instantiates\",\"lsTypeAndKind\": \"instantiates_batch_parent\",\"recordedBy\": \"jane\",\"recordedDate\": 1424730027913,\"version\": null}],\"ignored\": false,\"lsKind\": \"protein\",\"lsLabels\": [{ \"ignored\": false,\"imageFile\": null,\"labelText\": \"Fielderase\",\"lsKind\": \"protein\",\"lsTransaction\": 9999,\"lsType\": \"name\",\"lsTypeAndKind\": \"name_protein\",\"modifiedDate\": null,\"physicallyLabled\": false,\"preferred\": true,\"recordedBy\": \"jane\",\"recordedDate\": 1375141504000}],\"lsStates\": [{ \"comments\": null,\"ignored\": false,\"lsKind\": \"protein parent\",\"lsTransaction\": 9999,\"lsType\": \"metadata\",\"lsTypeAndKind\": \"metadata_protein parent\",\"lsValues\": [{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": 1342080000000, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"completion date\", \"lsTransaction\": 9999, \"lsType\": \"dateValue\", \"lsTypeAndKind\": \"dateValue_completion date\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"notebook\", \"lsTransaction\": 9999, \"lsType\": \"stringValue\", \"lsTypeAndKind\": \"stringValue_notebook\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": \"Notebook 1\", \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"molecular weight\", \"lsTransaction\": 9999, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_molecular weight\", \"modifiedDate\": null, \"numericValue\": 231, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": \"g/mol\", \"unitType\": \"molecular weight\", \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"batch number\", \"lsTransaction\": 9999, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_batch number\", \"modifiedDate\": null, \"numericValue\": 0, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": null, \"unitType\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null} ],\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141460000}],\"lsTransaction\": 9999,\"lsType\": \"parent\",\"lsTypeAndKind\": \"parent_protein\",\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141508000,\"shortDescription\": \" \"}";
		LsThing lsThing = LsThing.fromJsonToLsThing(json);
		logger.info("JSON: "+lsThing.toJson());
		logger.info("JSON Stub: "+lsThing.toJsonStub());
		logger.info("JSON Nested with Stubs: "+lsThing.toJsonWithNestedStubs());
		logger.info("JSON Nested with Full: "+lsThing.toJsonWithNestedFull());
	}
	
	@Test
	@Transactional
	public void updateBatchInteractions(){
		String json = "{\"lsType\":\"batch\",\"lsKind\":\"internalization agent\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1428517111212,\"shortDescription\":\" \",\"lsLabels\":[],\"lsStates\":[{\"deleted\":false,\"id\":2649027,\"ignored\":false,\"lsKind\":\"internalization agent batch\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_internalization agent batch\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960432,\"ignored\":false,\"lsKind\":\"notebook\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"stringValue\":\"test\",\"unitTypeAndKind\":\"null_null\",\"version\":0,\"value\":\"test\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960435,\"ignored\":false,\"lsKind\":\"molecular weight\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_molecular weight\",\"numericValue\":123,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"unitKind\":\"kDa\",\"unitType\":\"molecular weight\",\"unitTypeAndKind\":\"molecular weight_kDa\",\"version\":0,\"value\":123},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960433,\"ignored\":true,\"lsKind\":\"purity\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_purity\",\"numericValue\":12,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"unitKind\":\"% purity\",\"unitType\":\"percentage\",\"unitTypeAndKind\":\"percentage_% purity\",\"version\":0,\"value\":121},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1428476400000,\"deleted\":false,\"id\":5960434,\"ignored\":false,\"lsKind\":\"completion date\",\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"unitTypeAndKind\":\"null_null\",\"version\":0,\"value\":1428476400000},{\"codeOrigin\":\"ACAS authors\",\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"egao\",\"deleted\":false,\"id\":5960438,\"ignored\":false,\"lsKind\":\"scientist\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"unitTypeAndKind\":\"null_null\",\"version\":0,\"value\":\"egao\"},{\"codeTypeAndKind\":\"null_null\",\"comments\":\"Testing (46).pdf\",\"deleted\":false,\"fileValue\":\"entities/internalizationAgents/I000012-6/Testing (46).pdf\",\"id\":5960439,\"ignored\":false,\"lsKind\":\"gpc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_gpc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"unitTypeAndKind\":\"null_null\",\"version\":1},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960436,\"ignored\":false,\"lsKind\":\"source id\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_source id\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"stringValue\":\"\",\"unitTypeAndKind\":\"null_null\",\"version\":0,\"value\":\"\"},{\"codeKind\":\"source\",\"codeOrigin\":\"ACAS DDICT\",\"codeType\":\"component\",\"codeTypeAndKind\":\"component_source\",\"codeValue\":\"Avidity\",\"deleted\":false,\"id\":5960437,\"ignored\":false,\"lsKind\":\"source\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_source\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"unitTypeAndKind\":\"null_null\",\"version\":0,\"value\":\"Avidity\"},{\"lsType\":\"numericValue\",\"lsKind\":\"purity\",\"ignored\":false,\"recordedDate\":1428517111212,\"recordedBy\":\"bob\",\"numericValue\":121,\"value\":121}],\"modifiedDate\":1428517031912,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"version\":2},{\"deleted\":false,\"id\":2649026,\"ignored\":false,\"lsKind\":\"inventory\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960430,\"ignored\":false,\"lsKind\":\"location\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"stringValue\":\"13\",\"unitTypeAndKind\":\"null_null\",\"version\":0,\"value\":\"13\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960431,\"ignored\":false,\"lsKind\":\"amount made\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount made\",\"numericValue\":23,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"unitKind\":\"g\",\"unitType\":\"mass\",\"unitTypeAndKind\":\"mass_g\",\"version\":0,\"value\":23}],\"modifiedDate\":1428517031934,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"version\":2}],\"codeName\":\"I000012-6\",\"deleted\":false,\"id\":1508706,\"ignored\":false,\"lsTags\":[],\"lsTypeAndKind\":\"batch_internalization agent\",\"modifiedDate\":1428517031909,\"version\":3,\"cid\":\"c346\",\"_changing\":false,\"_previousAttributes\":{\"lsType\":\"thing\",\"lsKind\":\"thing\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1428517037610,\"shortDescription\":\" \",\"lsLabels\":[],\"lsStates\":[],\"firstLsThings\":[]},\"changed\":{\"secondLsThings\":[]},\"_pending\":false,\"urlRoot\":\"/api/things/batch/internalization agent\",\"lsProperties\":{\"defaultLabels\":[],\"defaultValues\":[{\"key\":\"scientist\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent batch\",\"type\":\"codeValue\",\"kind\":\"scientist\",\"codeOrigin\":\"ACAS authors\",\"value\":\"unassigned\"},{\"key\":\"completion date\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent batch\",\"type\":\"dateValue\",\"kind\":\"completion date\"},{\"key\":\"notebook\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent batch\",\"type\":\"stringValue\",\"kind\":\"notebook\"},{\"key\":\"source\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent batch\",\"type\":\"codeValue\",\"kind\":\"source\",\"value\":\"Avidity\",\"codeType\":\"component\",\"codeKind\":\"source\",\"codeOrigin\":\"ACAS DDICT\"},{\"key\":\"source id\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent batch\",\"type\":\"stringValue\",\"kind\":\"source id\"},{\"key\":\"molecular weight\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent batch\",\"type\":\"numericValue\",\"kind\":\"molecular weight\",\"unitType\":\"molecular weight\",\"unitKind\":\"kDa\"},{\"key\":\"purity\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent batch\",\"type\":\"numericValue\",\"kind\":\"purity\",\"unitType\":\"percentage\",\"unitKind\":\"% purity\"},{\"key\":\"amount made\",\"stateType\":\"metadata\",\"stateKind\":\"inventory\",\"type\":\"numericValue\",\"kind\":\"amount made\",\"unitType\":\"mass\",\"unitKind\":\"g\"},{\"key\":\"location\",\"stateType\":\"metadata\",\"stateKind\":\"inventory\",\"type\":\"stringValue\",\"kind\":\"location\"}],\"defaultFirstLsThingItx\":[],\"defaultSecondLsThingItx\":[]},\"className\":\"Thing\",\"validationError\":null,\"idAttribute\":\"id\",\"firstLsThings\":[],\"secondLsThings\":[{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":1}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1428517111206,\"secondLsThing\":{\"codeName\":\"PEG000010-2\",\"deleted\":false,\"id\":1508673,\"ignored\":false,\"lsKind\":\"peg\",\"lsLabels\":[],\"lsStates\":[{\"deleted\":false,\"id\":2648998,\"ignored\":false,\"lsKind\":\"inventory\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960363,\"ignored\":false,\"lsKind\":\"location\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"stringValue\":\"lab23\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960364,\"ignored\":false,\"lsKind\":\"amount made\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount made\",\"numericValue\":123,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"unitKind\":\"g\",\"unitType\":\"mass\",\"unitTypeAndKind\":\"mass_g\",\"version\":0}],\"modifiedDate\":1428516368847,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"version\":7},{\"deleted\":false,\"id\":2648997,\"ignored\":false,\"lsKind\":\"peg batch\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_peg batch\",\"lsValues\":[{\"codeOrigin\":\"ACAS authors\",\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"egao\",\"deleted\":false,\"id\":5960362,\"ignored\":false,\"lsKind\":\"scientist\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960361,\"ignored\":false,\"lsKind\":\"purity\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_purity\",\"numericValue\":123,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"unitKind\":\"% purity\",\"unitType\":\"percentage\",\"unitTypeAndKind\":\"percentage_% purity\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1428390000000,\"deleted\":false,\"id\":5960360,\"ignored\":false,\"lsKind\":\"completion date\",\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960358,\"ignored\":false,\"lsKind\":\"notebook\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"stringValue\":\"test\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960357,\"ignored\":false,\"lsKind\":\"source id\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_source id\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"stringValue\":\"\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeKind\":\"source\",\"codeOrigin\":\"ACAS DDICT\",\"codeType\":\"component\",\"codeTypeAndKind\":\"component_source\",\"codeValue\":\"Avidity\",\"deleted\":false,\"id\":5960359,\"ignored\":false,\"lsKind\":\"source\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_source\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"comments\":\"Testing (45).pdf\",\"deleted\":false,\"fileValue\":\"entities/pegs/PEG000010-2/Testing (45).pdf\",\"id\":5960356,\"ignored\":false,\"lsKind\":\"gpc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_gpc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"unitTypeAndKind\":\"null_null\",\"version\":1}],\"modifiedDate\":1428516368853,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"version\":7}],\"lsTags\":[],\"lsType\":\"batch\",\"lsTypeAndKind\":\"batch_peg\",\"modifiedDate\":1428516368844,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"version\":8},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"},{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"deleted\":false,\"id\":2649029,\"ignored\":false,\"lsKind\":\"composition\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_composition\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960441,\"ignored\":false,\"lsKind\":\"order\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_order\",\"numericValue\":1,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"\",\"recordedDate\":1428517031733,\"unitTypeAndKind\":\"null_null\",\"version\":0}],\"recordedBy\":\"\",\"recordedDate\":1428517031732,\"version\":1}],\"recordedBy\":\"bob\",\"recordedDate\":1428517031598,\"secondLsThing\":{\"codeName\":\"PEG000010-1\",\"deleted\":false,\"id\":1442921,\"ignored\":false,\"lsKind\":\"peg\",\"lsType\":\"batch\",\"lsTypeAndKind\":\"batch_peg\",\"modifiedDate\":1428516368872,\"recordedBy\":\"bob\",\"recordedDate\":1426100366796,\"version\":14},\"deleted\":false,\"id\":1508708,\"ignored\":true,\"version\":1,\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"}]}";
		LsThing lsThing = LsThing.fromJsonToLsThing(json);
		LsThing updatedLsThing = lsThingService.updateLsThing(lsThing);
		logger.info(updatedLsThing.toJsonWithNestedFull());
		Assert.assertEquals(2, updatedLsThing.getSecondLsThings().size());
		Assert.assertEquals(1, updatedLsThing.getSecondLsThings().iterator().next().getLsStates().size());
	}
	
	@Test
	@Transactional
	public void sortLsThingsByCodeName(){
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
		for (LsThing lsThing : lsThings){
			Assert.assertEquals("THING-0"+num, lsThing.getCodeName());
			num++;
		}
	}
	
	@Test
	@Transactional
	public void sortLsThingsByBatchNumber(){
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
		for (LsThing lsThing : lsThings){
			Assert.assertEquals("THING-0"+num, lsThing.getCodeName());
			num++;
		}
	}

}
