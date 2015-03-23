package com.labsynch.labseer.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.labsynch.labseer.domain.LsThing;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config-test.xml"})
public class ApiLsThingControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiLsThingControllerTest.class);
	
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    @Test
    public void registerComponentParentTest() throws Exception {
    	String json = "{\"codeName\":null,\"ignored\":false,\"lsKind\":\"linker small molecule\",\"lsLabels\":[{\"ignored\":false,\"imageFile\":null,\"labelText\":\"Ad 2\",\"lsKind\":\"linker small molecule\",\"lsTransaction\":1,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_linker small molecule\",\"modifiedDate\":null,\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"jane\",\"recordedDate\":1375141504000}],\"lsStates\":[{\"comments\":null,\"ignored\":false,\"lsKind\":\"linker small molecule parent\",\"lsTransaction\":1,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_linker small molecule parent\",\"lsValues\":[{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":1342080000000,\"fileValue\":null,\"ignored\":false,\"lsTransaction\":128,\"modifiedDate\":null,\"numericValue\":null,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":null,\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"completion date\",\"valueOperator\":null,\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"valueUnit\":null},{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"ignored\":false,\"lsTransaction\":127,\"modifiedDate\":null,\"numericValue\":null,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":\"Notebook 1\",\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"notebook\",\"valueOperator\":null,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"valueUnit\":null},{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"ignored\":false,\"lsTransaction\":128,\"modifiedDate\":null,\"numericValue\":231,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":null,\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"molecular weight\",\"valueOperator\":null,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_molecular weight\",\"unitKind\":\"g/mol\",\"unitType\":\"molecular weight\",\"valueUnit\":null},{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"ignored\":false,\"lsTransaction\":128,\"modifiedDate\":null,\"numericValue\":0,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":null,\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"batch number\",\"valueOperator\":null,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_batch number\",\"unitKind\":null,\"unitType\":null,\"valueUnit\":null}],\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jane\",\"recordedDate\":1375141460000}],\"lsTransaction\":1,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_linker small molecule\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jane\",\"recordedDate\":1375141508000,\"shortDescription\":\" \"}";
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/lsthings/parent/linker small molecule")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	LsThing postedLsThing = LsThing.fromJsonToLsThing(responseJson);
    	logger.info(postedLsThing.toJson());
    }
    
    @Test
    public void registerComponentBatchTest() throws Exception {
    	String json = "{\"codeName\":null,\"ignored\":false,\"lsKind\":\"linker small molecule\",\"lsStates\":[{\"comments\":null,\"id\":11,\"ignored\":false,\"lsKind\":\"linker small molecule batch\",\"lsTransaction\":1,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_linker small molecule batch\",\"lsValues\":[{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":1342080000000,\"fileValue\":null,\"id\":12,\"ignored\":false,\"lsTransaction\":128,\"modifiedDate\":null,\"numericValue\":null,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":null,\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"completion date\",\"valueOperator\":null,\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"valueUnit\":null,\"version\":0},{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"id\":13,\"ignored\":false,\"lsTransaction\":127,\"modifiedDate\":null,\"numericValue\":null,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":\"Notebook 1\",\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"notebook\",\"valueOperator\":null,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"valueUnit\":null,\"version\":0}],\"recordedDate\":1363388477000,\"recordedBy\":\"jane\"},{\"comments\":null,\"id\":111,\"ignored\":false,\"lsKind\":\"inventory\",\"lsTransaction\":1,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"id\":14,\"ignored\":false,\"lsTransaction\":128,\"modifiedDate\":null,\"numericValue\":2.3,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":null,\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"amount\",\"valueOperator\":null,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount\",\"unitKind\":\"g\",\"unitType\":\"mass\",\"valueUnit\":null,\"version\":0},{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"id\":15,\"ignored\":false,\"lsTransaction\":127,\"modifiedDate\":null,\"numericValue\":null,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":\"Cabinet 1\",\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"location\",\"valueOperator\":null,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"valueUnit\":null,\"version\":0}],\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jane\",\"recordedDate\":1375141460000,\"version\":0}],\"lsTransaction\":1,\"lsType\":\"batch\",\"lsTypeAndKind\":\"batch_linker small molecule\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jane\",\"recordedDate\":1375141508000,\"shortDescription\":\" \",\"version\":0}";
    	String responseJson = this.mockMvc.perform(post("/api/v1/lsthings/batch/linker small molecule?parentIdOrCodeName=LSM000002")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson);
    	LsThing postedLsThing = LsThing.fromJsonToLsThing(responseJson);
    	logger.info(postedLsThing.toJson());
    }
    
    @Test
    public void updateComponentParentTest() throws Exception {
    	String json = "{\"codeName\":\"LSM000001\",\"deleted\":false,\"id\":1191916,\"ignored\":false,\"lsKind\":\"linker small molecule\",\"lsLabels\":[{\"deleted\":false,\"id\":297063,\"ignored\":false,\"labelText\":\"Ad\",\"lsKind\":\"linker small molecule\",\"lsTransaction\":1,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_linker small molecule\",\"physicallyLabeled\":false,\"preferred\":true,\"recordedBy\":\"jane\",\"recordedDate\":1375141504000,\"version\":0}],\"lsStates\":[{\"comments\":\"test state comment\",\"deleted\":false,\"id\":2170023,\"ignored\":false,\"lsKind\":\"linker small molecule parent\",\"lsTransaction\":1,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_linker small molecule parent\",\"lsValues\":[{\"comments\":\"test value comment\",\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4584944,\"ignored\":false,\"lsKind\":\"batch number\",\"lsTransaction\":128,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_batch number\",\"numericValue\":1,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"unitTypeAndKind\":\"null_null\",\"version\":1},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4584942,\"ignored\":false,\"lsKind\":\"molecular weight\",\"lsTransaction\":128,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_molecular weight\",\"numericValue\":231,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"unitKind\":\"g/mol\",\"unitType\":\"molecular weight\",\"unitTypeAndKind\":\"molecular weight_g/mol\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4584945,\"ignored\":false,\"lsKind\":\"notebook\",\"lsTransaction\":127,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"stringValue\":\"Notebook 1\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1342080000000,\"deleted\":false,\"id\":4584943,\"ignored\":false,\"lsKind\":\"completion date\",\"lsTransaction\":128,\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"unitTypeAndKind\":\"null_null\",\"version\":0}],\"recordedBy\":\"jane\",\"recordedDate\":1375141460000,\"version\":1}],\"lsTags\":[],\"lsTransaction\":1,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_linker small molecule\",\"recordedBy\":\"jane\",\"recordedDate\":1375141508000,\"version\":1}";
    	MockHttpServletResponse response = this.mockMvc.perform(put("/api/v1/lsthings/parent/linker small molecule/LSM000001")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	LsThing postedLsThing = LsThing.fromJsonToLsThing(responseJson);
    	logger.info(postedLsThing.toJson());
    }
    
    @Test
    public void updateComponentBatchTest() throws Exception {
    	String json = "{\"codeName\":\"LSM000001-1\",\"deleted\":false,\"id\":1191917,\"ignored\":false,\"lsKind\":\"linker small molecule\",\"lsLabels\":[],\"lsStates\":[{\"deleted\":false,\"id\":2170024,\"ignored\":false,\"lsKind\":\"linker small molecule batch\",\"lsTransaction\":1,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_linker small molecule batch\",\"lsValues\":[{\"comments\":\"test notebook update comment\",\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4584947,\"ignored\":false,\"lsKind\":\"notebook\",\"lsTransaction\":127,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"stringValue\":\"Notebook 1\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1342080000000,\"deleted\":false,\"id\":4584946,\"ignored\":false,\"lsKind\":\"completion date\",\"lsTransaction\":128,\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"unitTypeAndKind\":\"null_null\",\"version\":0}],\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"version\":1},{\"deleted\":false,\"id\":2170025,\"ignored\":false,\"lsKind\":\"inventory\",\"lsTransaction\":1,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4584949,\"ignored\":false,\"lsKind\":\"amount\",\"lsTransaction\":128,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount\",\"numericValue\":2.29999999999999982236431605997495353221893310546875,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"unitKind\":\"g\",\"unitType\":\"mass\",\"unitTypeAndKind\":\"mass_g\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4584948,\"ignored\":false,\"lsKind\":\"location\",\"lsTransaction\":127,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"stringValue\":\"Cabinet 1\",\"unitTypeAndKind\":\"null_null\",\"version\":0}],\"recordedBy\":\"jane\",\"recordedDate\":1375141460000,\"version\":1}],\"lsTags\":[],\"lsTransaction\":1,\"lsType\":\"batch\",\"lsTypeAndKind\":\"batch_linker small molecule\",\"recordedBy\":\"jane\",\"recordedDate\":1375141508000,\"version\":1}";
    	String responseJson = this.mockMvc.perform(put("/api/v1/lsthings/batch/linker small molecule/LSM000001-1")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson);
    	LsThing postedLsThing = LsThing.fromJsonToLsThing(responseJson);
    	logger.info(postedLsThing.toJson());
    }
    
    //@Test
    public void registerAssemblyParentTest() throws Exception {
    	String json = "";
    	String responseJson = this.mockMvc.perform(post("/api/v1/lsthings/parent/internalization agent")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson);
    	LsThing postedLsThing = LsThing.fromJsonToLsThing(responseJson);
    	logger.info(postedLsThing.toJson());
    }
    
    //@Test
    public void registerAssemblyBatchTest() throws Exception {
    	String json = "";
    	String responseJson = this.mockMvc.perform(post("/api/v1/lsthings/batch/internalization agent?parentIdOrCodeName=")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson);
    	LsThing postedLsThing = LsThing.fromJsonToLsThing(responseJson);
    	logger.info(postedLsThing.toJson());
    }
    
    @Test
    public void validateInvalidComponentNameTest() throws Exception {
    	String componentName = "[\"Ad 2\"]";
    	String response = this.mockMvc.perform(post("/api/v1/lsthings/validatename?lsKind=linker small molecule")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(componentName)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse().getContentAsString();
    	logger.info(response);
    	Assert.assertEquals(false, Boolean.parseBoolean(response)); 
    }
    
    @Test
    public void validateValidComponentNameTest() throws Exception {
    	String componentName = "[\"valid name\"]";
    	String response = this.mockMvc.perform(post("/api/v1/lsthings/validatename?lsKind=protein")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(componentName)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse().getContentAsString();
    	logger.info(response);
    	Assert.assertEquals(true, Boolean.parseBoolean(response)); 
    }
    
    //@Test
    public void validateInvalidAssemblyTest() throws Exception {
    	String componentNameList = "[\"CMPT-00001\", \"CMPT-00002\", \"CMPT-00003\"]";
    	String response = this.mockMvc.perform(post("/api/v1/lsthings/validatename?lsKind=internalization agent")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(componentNameList)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse().getContentAsString();
    	logger.info(response);
    	Assert.assertEquals(false, Boolean.parseBoolean(response)); 
    }
    
    //@Test
    public void validateValidAssemblyTest() throws Exception {
    	String componentNameList = "[\"CMPT-00001\", \"CMPT-00002\", \"CMPT-00003\"]";
    	String response = this.mockMvc.perform(post("/api/v1/lsthings/validatename?lsKind=internalization agent")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(componentNameList)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse().getContentAsString();
    	logger.info(response);
    	Assert.assertEquals(true, Boolean.parseBoolean(response)); 
    }

    @Test
    public void getLsThing() throws Exception {
        String json = this.mockMvc.perform(get("/api/v1/lsthings/parent/linker small molecule/LSM000001-1")
        		.contentType(MediaType.APPLICATION_JSON)
        		.accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isOk())
        		.andExpect(content().contentType("application/json"))
        		.andReturn().getResponse().getContentAsString();
        
        logger.info(json);
        LsThing lsThing = LsThing.fromJsonToLsThing(json);
        logger.info(lsThing.toJson());
    }
    
    @Test
    public void getComponentBatches() throws Exception {
        String json = this.mockMvc.perform(get("/api/v1/lsthings/parent/linker small molecule/getbatches/LSM000001")
        		.contentType(MediaType.APPLICATION_JSON)
        		.accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isOk())
        		.andExpect(content().contentType("application/json"))
        		.andReturn().getResponse().getContentAsString();
        
        logger.info(json);
        Collection<LsThing> lsThings = LsThing.fromJsonArrayToLsThings(json);
        Assert.assertEquals(1, lsThings.size());
        logger.info(LsThing.toJsonArray(lsThings));
    }
    
    @Test
    public void searchForComponentBatch() throws Exception {
        String json = this.mockMvc.perform(get("/api/v1/lsthings/search?lsType=batch&q=LSM000001-1 2012-07-12")
        		.contentType(MediaType.APPLICATION_JSON)
        		.accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isOk())
        		.andExpect(content().contentType("application/json"))
        		.andReturn().getResponse().getContentAsString();
        
        logger.info(json);
        Collection<LsThing> lsThings = LsThing.fromJsonArrayToLsThings(json);
        Assert.assertEquals(1, lsThings.size());
        logger.info(LsThing.toJsonArray(lsThings));
    }
    
    @Test
    public void getCodeTableLsThings() throws Exception {
    	String json = this.mockMvc.perform(get("/api/v1/lsthings/codetable?lsType=parent&lsKind=linker small molecule")
        		.contentType(MediaType.APPLICATION_JSON)
        		.accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isOk())
        		.andExpect(content().contentType("application/json;charset=utf-8"))
        		.andReturn().getResponse().getContentAsString();
    	logger.info(json);
    }
    
    @Test
    public void getCodeTableLsThings2() throws Exception {
    	String json = this.mockMvc.perform(get("/api/v1/lsthings/codetable?lsType=term&lsKind=documentTerm")
        		.contentType(MediaType.APPLICATION_JSON)
        		.accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isOk())
        		.andExpect(content().contentType("application/json;charset=utf-8"))
        		.andReturn().getResponse().getContentAsString();
    	logger.info(json);
    }
    
    @Test
    public void searchByScientist() throws Exception {
    	String searchString = "jane";
        String json = this.mockMvc.perform(get("/api/v1/lsthings/search?q="+searchString)
        		.contentType(MediaType.APPLICATION_JSON)
        		.accept(MediaType.APPLICATION_JSON))
        		.andExpect(status().isOk())
        		.andExpect(content().contentType("application/json"))
        		.andReturn().getResponse().getContentAsString();
        
        logger.info(json);
        Collection<LsThing> lsThings = LsThing.fromJsonArrayToLsThings(json);
        Assert.assertFalse(lsThings.isEmpty());
        logger.info(LsThing.toJsonArray(lsThings));
    }
    
    @Test
    @Transactional
    public void registerParentAndBatchTest() throws Exception {
//    	String json = "{\"codeName\": null,\"firstLsThings\": [{\"deleted\": false,\"firstLsThing\": {\"codeName\": \"PRTN-000013-1\",\"deleted\": false,\"ignored\": false,\"lsKind\": \"protein\",\"lsType\": \"batch\",\"lsTypeAndKind\": \"batch_protein\",\"recordedBy\": \"jane\",\"recordedDate\": 1424730027913,\"version\": null},\"id\": null,\"ignored\": false,\"lsKind\": \"batch_parent\",\"lsType\": \"instantiates\",\"lsTypeAndKind\": \"instantiates_batch_parent\",\"recordedBy\": \"jane\",\"recordedDate\": 1424730027913,\"version\": null}],\"ignored\": false,\"lsKind\": \"protein\",\"lsLabels\": [{ \"ignored\": false,\"imageFile\": null,\"labelText\": \"Fielderase\",\"lsKind\": \"protein\",\"lsTransaction\": 9999,\"lsType\": \"name\",\"lsTypeAndKind\": \"name_protein\",\"modifiedDate\": null,\"physicallyLabled\": false,\"preferred\": true,\"recordedBy\": \"jane\",\"recordedDate\": 1375141504000}],\"lsStates\": [{ \"comments\": null,\"ignored\": false,\"lsKind\": \"protein parent\",\"lsTransaction\": 9999,\"lsType\": \"metadata\",\"lsTypeAndKind\": \"metadata_protein parent\",\"lsValues\": [{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": 1342080000000, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"completion date\", \"lsTransaction\": 9999, \"lsType\": \"dateValue\", \"lsTypeAndKind\": \"dateValue_completion date\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"notebook\", \"lsTransaction\": 9999, \"lsType\": \"stringValue\", \"lsTypeAndKind\": \"stringValue_notebook\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": \"Notebook 1\", \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"molecular weight\", \"lsTransaction\": 9999, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_molecular weight\", \"modifiedDate\": null, \"numericValue\": 231, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": \"g/mol\", \"unitType\": \"molecular weight\", \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"batch number\", \"lsTransaction\": 9999, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_batch number\", \"modifiedDate\": null, \"numericValue\": 0, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": null, \"unitType\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null} ],\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141460000}],\"lsTransaction\": 9999,\"lsType\": \"parent\",\"lsTypeAndKind\": \"parent_protein\",\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141508000,\"shortDescription\": \" \"}";
    	String json = "{\"codeName\": null,\"firstLsThings\": [{\"deleted\": false,\"firstLsThing\": {\"codeName\": \"PRTN-000013-1\",\"deleted\": false,\"ignored\": false,\"lsKind\": \"protein\",\"lsStates\":[{\"deleted\":false,\"ignored\":false,\"lsKind\":\"inventory\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"location\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729705505,\"stringValue\":\"123\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"amount made\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount made\",\"numericValue\":1123,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729706303,\"unitKind\":\"g\",\"unitType\":\"mass\",\"unitTypeAndKind\":\"mass_g\",\"version\":0}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"version\":1},{\"deleted\":false,\"ignored\":false,\"lsKind\":\"protein batch\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_protein batch\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"hplc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_hplc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeOrigin\":\"ACAS authors\",\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"egao\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"scientist\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702585,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"DeleteExperimentInstructions (6).pdf\",\"ignored\":false,\"lsKind\":\"gpc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_gpc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1424678400000,\"deleted\":false,\"ignored\":false,\"lsKind\":\"completion date\",\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703242,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"ms\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_ms\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeKind\":\"source\",\"codeOrigin\":\"ACAS DDICT\",\"codeType\":\"component\",\"codeTypeAndKind\":\"component_source\",\"codeValue\":\"Avidity\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"source\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_source\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729672870,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"purity\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_purity\",\"numericValue\":129,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729708062,\"unitKind\":\"% purity\",\"unitType\":\"percentage\",\"unitTypeAndKind\":\"percentage_% purity\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"nmr\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_nmr\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"notebook\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703953,\"stringValue\":\"test\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"source id\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_source id\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702610,\"stringValue\":\"\",\"unitTypeAndKind\":\"null_null\",\"version\":0}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"version\":1}],\"lsType\": \"batch\",\"lsTypeAndKind\": \"batch_protein\",\"recordedBy\": \"jane\",\"recordedDate\": 1424730027913,\"version\": null},\"id\": null,\"ignored\": false,\"lsKind\": \"batch_parent\",\"lsType\": \"instantiates\",\"lsTypeAndKind\": \"instantiates_batch_parent\",\"recordedBy\": \"jane\",\"recordedDate\": 1424730027913,\"version\": null}],\"ignored\": false,\"lsKind\": \"protein\",\"lsLabels\": [{ \"ignored\": false,\"imageFile\": null,\"labelText\": \"Fielderase\",\"lsKind\": \"protein\",\"lsTransaction\": 9999,\"lsType\": \"name\",\"lsTypeAndKind\": \"name_protein\",\"modifiedDate\": null,\"physicallyLabled\": false,\"preferred\": true,\"recordedBy\": \"jane\",\"recordedDate\": 1375141504000}],\"lsStates\": [{ \"comments\": null,\"ignored\": false,\"lsKind\": \"protein parent\",\"lsTransaction\": 9999,\"lsType\": \"metadata\",\"lsTypeAndKind\": \"metadata_protein parent\",\"lsValues\": [{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": 1342080000000, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"completion date\", \"lsTransaction\": 9999, \"lsType\": \"dateValue\", \"lsTypeAndKind\": \"dateValue_completion date\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"notebook\", \"lsTransaction\": 9999, \"lsType\": \"stringValue\", \"lsTypeAndKind\": \"stringValue_notebook\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": \"Notebook 1\", \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"molecular weight\", \"lsTransaction\": 9999, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_molecular weight\", \"modifiedDate\": null, \"numericValue\": 231, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": \"g/mol\", \"unitType\": \"molecular weight\", \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"batch number\", \"lsTransaction\": 9999, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_batch number\", \"modifiedDate\": null, \"numericValue\": 0, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": null, \"unitType\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null} ],\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141460000}],\"lsTransaction\": 9999,\"lsType\": \"parent\",\"lsTypeAndKind\": \"parent_protein\",\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141508000,\"shortDescription\": \" \"}";
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/lsthings/parent/protein?with=nested")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	LsThing postedLsThing = LsThing.fromJsonToLsThing(responseJson);
    	logger.info(postedLsThing.toJson());
    }
    
    @Test
    @Transactional
    public void updateParentAndBatchTest() throws Exception {
    	String json = "{\"codeName\":\"PRTN000015\",\"deleted\":false,\"firstLsThings\":[{\"deleted\":false,\"firstLsThing\":{\"codeName\":\"PRTN000015-1\",\"deleted\":false,\"id\":1493852,\"ignored\":false,\"lsKind\":\"protein\",\"lsStates\":[{\"comments\":\"test comment 2\",\"deleted\":false,\"id\":2634316,\"ignored\":false,\"lsKind\":\"protein batch\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_protein batch\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"edited ms file value\",\"id\":5926370,\"ignored\":false,\"lsKind\":\"ms\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_ms\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"id\":5926365,\"ignored\":false,\"lsKind\":\"nmr\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_nmr\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"DeleteExperimentInstructions (6).pdf\",\"id\":5926369,\"ignored\":false,\"lsKind\":\"gpc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_gpc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1424678400000,\"deleted\":false,\"id\":5926368,\"ignored\":false,\"lsKind\":\"completion date\",\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703242,\"unitTypeAndKind\":\"null_null\"},{\"codeKind\":\"source\",\"codeOrigin\":\"ACAS DDICT\",\"codeType\":\"component\",\"codeTypeAndKind\":\"component_source\",\"codeValue\":\"Avidity Edited\",\"deleted\":false,\"id\":5926373,\"ignored\":false,\"lsKind\":\"source\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_source\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729672870,\"unitTypeAndKind\":\"null_null\"},{\"codeOrigin\":\"ACAS authors\",\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"egao\",\"deleted\":false,\"id\":5926367,\"ignored\":false,\"lsKind\":\"scientist\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702585,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926372,\"ignored\":false,\"lsKind\":\"purity\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_purity\",\"numericValue\":129,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729708062,\"unitKind\":\"% purity\",\"unitType\":\"percentage\",\"unitTypeAndKind\":\"percentage_% purity\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"id\":5926371,\"ignored\":false,\"lsKind\":\"hplc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_hplc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926364,\"ignored\":false,\"lsKind\":\"notebook\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703953,\"stringValue\":\"test\",\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926366,\"ignored\":false,\"lsKind\":\"source id\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_source id\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702610,\"stringValue\":\"test source id\",\"unitTypeAndKind\":\"null_null\"}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940},{\"deleted\":false,\"id\":2634315,\"ignored\":false,\"lsKind\":\"inventory\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926362,\"ignored\":false,\"lsKind\":\"location\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729705505,\"stringValue\":\"123\",\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926363,\"ignored\":false,\"lsKind\":\"amount made\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount made\",\"numericValue\":1123,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729706303,\"unitKind\":\"g\",\"unitType\":\"mass\",\"unitTypeAndKind\":\"mass_g\"}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940}],\"lsType\":\"batch\",\"lsTypeAndKind\":\"batch_protein\",\"recordedBy\":\"jane\",\"recordedDate\":1424730027913},\"id\":1493853,\"ignored\":false,\"lsKind\":\"batch_parent\",\"lsType\":\"instantiates\",\"lsTypeAndKind\":\"instantiates_batch_parent\",\"recordedBy\":\"jane\",\"recordedDate\":1424730027913}],\"id\":1493851,\"ignored\":false,\"lsKind\":\"protein\",\"lsLabels\":[{\"deleted\":false,\"id\":297595,\"ignored\":false,\"labelText\":\"Fielderase Z\",\"lsKind\":\"protein\",\"lsTransaction\":9999,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_protein\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"jane\",\"recordedDate\":1375141504000}],\"lsStates\":[{\"deleted\":false,\"id\":2634314,\"ignored\":false,\"lsKind\":\"protein parent\",\"lsTransaction\":9999,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_protein parent\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926359,\"ignored\":false,\"lsKind\":\"notebook\",\"lsTransaction\":9999,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"stringValue\":\"Notebook 1\",\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926361,\"ignored\":false,\"lsKind\":\"molecular weight\",\"lsTransaction\":9999,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_molecular weight\",\"numericValue\":890,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"unitKind\":\"g/mol\",\"unitType\":\"molecular weight\",\"unitTypeAndKind\":\"molecular weight_g/mol\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926360,\"ignored\":false,\"lsKind\":\"batch number\",\"lsTransaction\":9999,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_batch number\",\"numericValue\":0,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1342080000000,\"deleted\":false,\"id\":5926358,\"ignored\":false,\"lsKind\":\"completion date\",\"lsTransaction\":9999,\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"unitTypeAndKind\":\"null_null\"}],\"recordedBy\":\"jane\",\"recordedDate\":1375141460000}],\"lsTags\":[],\"lsTransaction\":9999,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_protein\",\"recordedBy\":\"jane\",\"recordedDate\":1375141508000,\"secondLsThings\":[]}";
    	MockHttpServletResponse response = this.mockMvc.perform(put("/api/v1/lsthings/parent/protein/PRTN000015?with=nested")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	LsThing postedLsThing = LsThing.fromJsonToLsThing(responseJson);
    	logger.info(postedLsThing.toJson());
    }
    
    @Test
    @Transactional
    public void documentManagerSearchTest1() throws Exception {
    	String searchTerms="";
    	 String json = this.mockMvc.perform(get("/api/v1/lsthings/documentmanagersearch?"+searchTerms)
         		.contentType(MediaType.APPLICATION_JSON)
         		.accept(MediaType.APPLICATION_JSON))
         		.andExpect(status().isOk())
         		.andExpect(content().contentType("application/json"))
         		.andReturn().getResponse().getContentAsString();
         
         logger.info(json);
         LsThing lsThing = LsThing.fromJsonToLsThing(json);
         logger.info(lsThing.toJson());
    }
}