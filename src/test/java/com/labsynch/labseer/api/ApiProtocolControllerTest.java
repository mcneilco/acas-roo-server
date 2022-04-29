package com.labsynch.labseer.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.AuthorRole;
import com.labsynch.labseer.domain.LsRole;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.ProtocolLabel;
import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.DateValueComparisonRequest;
import com.labsynch.labseer.dto.ProtocolErrorMessageDTO;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config-test.xml"})
public class ApiProtocolControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiProtocolControllerTest.class);
	
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    @Test
    public void genericSearchByScientist() throws Exception {
    	String searchString = "bfielder";
    	String responseJson =  this.mockMvc.perform(get("/api/v1/protocols/search?q="+searchString)
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson.toString());
    	Collection<Protocol> results = Protocol.fromJsonArrayToProtocols(responseJson);
    	Assert.assertFalse(results.isEmpty());
    }
    
    @Test
    public void genericSearchByDate() throws Exception {
    	String searchString = "2015-03-06";
    	String responseJson =  this.mockMvc.perform(get("/api/v1/protocols/search?q="+searchString)
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson.toString());
    	Collection<Protocol> results = Protocol.fromJsonArrayToProtocols(responseJson);
    	Assert.assertFalse(results.isEmpty());
    }
    
    @Test
    public void genericSearchWithProjectRoles() throws Exception {
    	//see instructions on ProtocolServiceTest for setup to search by project roles
    	//this test verifies the ApiProtocolController search route is using project roles
    	String projectCode = ProtocolValue.findProtocolValuesByLsKindEqualsAndCodeValueLike("project", "*").getResultList().get(0).getCodeValue();
		logger.info(projectCode);
		String userName = AuthorRole.findAuthorRolesByRoleEntry(LsRole.findLsRolesByLsTypeEqualsAndLsKindEquals("Project", projectCode).getResultList().get(0)).getResultList().get(0).getUserEntry().getUserName();
		String code = "PROT-00000006E";
		String query = code;
		logger.info("Searching with the query: "+ query + " with userName: "+userName);
		
		String responseJson =  this.mockMvc.perform(get("/api/v1/protocols/search?userName="+userName+"&q="+query)
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson.toString());
    	Collection<Protocol> results = Protocol.fromJsonArrayToProtocols(responseJson);
    	Assert.assertFalse(results.isEmpty());
		
		userName = "bsplit";
		logger.info("Searching with the query: "+ query + " with userName: "+userName);
		
		responseJson =  this.mockMvc.perform(get("/api/v1/protocols/search?userName="+userName+"&q="+query)
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson.toString());
    	results = Protocol.fromJsonArrayToProtocols(responseJson);
    	Assert.assertTrue(results.isEmpty());
    }
    
    @Test
    public void getProtocolsByCodeName_Stub() throws Exception {
    	Collection<Protocol> experiments = Protocol.findAllProtocols();
    	List<String> codeNames = new ArrayList<String>();
    	for (Protocol experiment : experiments){
    		codeNames.add("\""+experiment.getCodeName()+"\"");
    	}
    	String format = "stub";
    	logger.info(codeNames.toString());
    	String responseJson =  this.mockMvc.perform(post("/api/v1/protocols/codename/jsonArray?with="+format)
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(codeNames.toString())
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson.toString());
    	Collection<ProtocolErrorMessageDTO> responses = ProtocolErrorMessageDTO.fromJsonArrayToProtocolErroes(responseJson);
    	for (ProtocolErrorMessageDTO response : responses){
    		Assert.assertNotNull(response.getProtocolCodeName());
        	Assert.assertNotNull(response.getProtocol());
        	Assert.assertTrue(response.getProtocol().getLsStates().isEmpty());
        	Assert.assertNotNull(response.getProtocol().getLsLabels());
        	Assert.assertTrue(!response.getProtocol().getLsLabels().isEmpty());
    	}
    }
    
    @Test
    public void getProtocolsByCodeName_fullObject() throws Exception {
    	Collection<Protocol> experiments = Protocol.findAllProtocols();
    	List<String> codeNames = new ArrayList<String>();
    	for (Protocol experiment : experiments){
    		codeNames.add("\""+experiment.getCodeName()+"\"");
    	}
    	String format = "fullObject";
    	logger.info(codeNames.toString());
    	String responseJson =  this.mockMvc.perform(post("/api/v1/protocols/codename/jsonArray?with="+format)
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(codeNames.toString())
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson.toString());
    	Collection<ProtocolErrorMessageDTO> responses = ProtocolErrorMessageDTO.fromJsonArrayToProtocolErroes(responseJson);
    	for (ProtocolErrorMessageDTO response : responses){
    		Assert.assertNotNull(response.getProtocolCodeName());
        	Assert.assertNotNull(response.getProtocol());
        	Assert.assertNotNull(response.getProtocol().getLsStates());
        	Assert.assertTrue(!response.getProtocol().getLsStates().isEmpty());
        	Assert.assertNotNull(response.getProtocol().getLsLabels());
        	Assert.assertTrue(!response.getProtocol().getLsLabels().isEmpty());
    	}
    }
    
    @Test
    public void getProtocolCodeTables() throws Exception {
    	String responseJson =  this.mockMvc.perform(get("/api/v1/protocols/codetable")
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson.toString());
    	Collection<CodeTableDTO> responses = CodeTableDTO.fromJsonArrayToCoes(responseJson);
    	for (CodeTableDTO response : responses){
    		Assert.assertNotNull(response.getCode());
        	Assert.assertNotNull(response.getName());
        	Assert.assertNotNull(response.getId());
    	}
    }
    
    @Test
    public void getProtocolCodeTablesByLsKind() throws Exception {
    	Protocol protocol = Protocol.findProtocolEntries(0, 1).get(0);
    	String responseJson =  this.mockMvc.perform(get("/api/v1/protocols/codetable?lsKind="+protocol.getLsKind())
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson.toString());
    	Collection<CodeTableDTO> responses = CodeTableDTO.fromJsonArrayToCoes(responseJson);
    	for (CodeTableDTO response : responses){
    		Assert.assertNotNull(response.getCode());
        	Assert.assertNotNull(response.getName());
        	Assert.assertNotNull(response.getId());
    	}
    }
    
    @Test
    public void getProtocolCodeTablesByName() throws Exception {
    	ProtocolLabel label = ProtocolLabel.findProtocolLabelEntries(0, 1).get(0);
    	String responseJson =  this.mockMvc.perform(get("/api/v1/protocols/codetable?protocolName="+label.getLabelText())
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson.toString());
    	Collection<CodeTableDTO> responses = CodeTableDTO.fromJsonArrayToCoes(responseJson);
    	for (CodeTableDTO response : responses){
    		Assert.assertNotNull(response.getCode());
        	Assert.assertNotNull(response.getName());
        	Assert.assertNotNull(response.getId());
    	}
    }
    
    @Test
    public void getProtocolCodesByDateValueComparison() throws Exception {
    	DateValueComparisonRequest request = new DateValueComparisonRequest();
		request.setStateType("metadata");
		request.setStateKind("protocol metadata");
		request.setValueKind("completion date");
		request.setSecondsDelta(60);
		
		String requestJson = request.toJson();
		
		logger.info(requestJson);
    	String responseJson =  this.mockMvc.perform(post("/api/v1/protocols/getProtocolCodesByDateValueComparison")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(requestJson)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson.toString());
		
    }
    

}