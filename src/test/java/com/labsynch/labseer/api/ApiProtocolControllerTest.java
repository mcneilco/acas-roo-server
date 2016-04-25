package com.labsynch.labseer.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;

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

import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.dto.ProtocolErrorMessageDTO;

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
    

}