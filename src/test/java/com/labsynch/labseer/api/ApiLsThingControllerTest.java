package com.labsynch.labseer.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import com.labsynch.labseer.dto.CodeTableDTO;

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
    	String componentName = "[\"Ad\"]";
    	String response = this.mockMvc.perform(post("/api/v1/lsthings/validatename?lsKind=protein")
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
    

}