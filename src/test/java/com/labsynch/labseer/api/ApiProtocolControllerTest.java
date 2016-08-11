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

import com.labsynch.labseer.domain.AuthorRole;
import com.labsynch.labseer.domain.LabelSequence;
import com.labsynch.labseer.domain.LsRole;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.dto.CodeTableDTO;

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
    

}