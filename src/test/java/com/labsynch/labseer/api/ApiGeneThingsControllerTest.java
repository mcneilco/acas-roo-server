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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.exceptions.ErrorMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config-test.xml"})
@Transactional
public class ApiGeneThingsControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiGeneThingsControllerTest.class);
	
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    @Transactional
    @Test
    public void getGeneExperimentDataTest() throws Exception {
    	String json = "{\"experimentCodeList\":[\"EXPT-00000116\"],\"batchCodeList\":[],\"booleanFilter\":\"and\",\"advancedFilter\":\"\"}";
    	logger.info("@@@@@@@@@@@@@@@@@     " + json + "        @@@@@@@@@@@@@@@@@");
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/experiments/agdata/batchcodelist/experimentcodelist?format=csv&onlyPublicData=true")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);

    }

//	  paste0(serverURL, "experiments/agdata/batchcodelist/experimentcodelist?format=csv&onlyPublicData=", onlyPublicData),
//	  customrequest='POST',
//	  httpheader=c('Content-Type'='application/json'),
//	  postfields=toJSON(searchParams))    
    
    
}