

package com.labsynch.labseer.service;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
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
		lsThing = lsThingService.saveLsThing(lsThing, false, true, false, true, parent.getId());
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
		String json = "{\"codeName\": null,\"firstLsThings\": [{\"deleted\": false,\"firstLsThing\": {\"codeName\": \"PRTN-000012-1\",\"deleted\": false,\"ignored\": false,\"lsKind\": \"protein\",\"lsType\": \"batch\",\"lsTypeAndKind\": \"batch_protein\",\"recordedBy\": \"bob\",\"recordedDate\": 1424730027913,\"version\": null},\"id\": null,\"ignored\": false,\"lsKind\": \"batch_parent\",\"lsType\": \"instantiates\",\"lsTypeAndKind\": \"instantiates_batch_parent\",\"recordedBy\": \"bob\",\"recordedDate\": 1424730027913,\"version\": null}],\"ignored\": false,\"lsKind\": \"protein\",\"lsLabels\": [{ \"ignored\": false,\"imageFile\": null,\"labelText\": \"Fielderase\",\"lsKind\": \"protein\",\"lsTransaction\": 1,\"lsType\": \"name\",\"lsTypeAndKind\": \"name_protein\",\"modifiedDate\": null,\"physicallyLabled\": false,\"preferred\": true,\"recordedBy\": \"jane\",\"recordedDate\": 1375141504000}],\"lsStates\": [{ \"comments\": null,\"ignored\": false,\"lsKind\": \"protein parent\",\"lsTransaction\": 1,\"lsType\": \"metadata\",\"lsTypeAndKind\": \"metadata_protein parent\",\"lsValues\": [{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": 1342080000000, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"completion date\", \"lsTransaction\": 128, \"lsType\": \"dateValue\", \"lsTypeAndKind\": \"dateValue_completion date\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"notebook\", \"lsTransaction\": 127, \"lsType\": \"stringValue\", \"lsTypeAndKind\": \"stringValue_notebook\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": \"Notebook 1\", \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"molecular weight\", \"lsTransaction\": 128, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_molecular weight\", \"modifiedDate\": null, \"numericValue\": 231, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": \"g/mol\", \"unitType\": \"molecular weight\", \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"batch number\", \"lsTransaction\": 128, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_batch number\", \"modifiedDate\": null, \"numericValue\": 0, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": null, \"unitType\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null} ],\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141460000}],\"lsTransaction\": 1,\"lsType\": \"parent\",\"lsTypeAndKind\": \"parent_protein\",\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141508000,\"shortDescription\": \" \"}";
    	LsThing lsThing = LsThing.fromJsonToLsThing(json);
    	try{
    		lsThingService.saveLsThing(lsThing);
    	} catch (UniqueNameException e){
    		logger.error("unique name exception");
    	}
	}

}
