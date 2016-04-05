package com.labsynch.labseer.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.labsynch.labseer.domain.ItxProtocolProtocol;
import com.labsynch.labseer.domain.Protocol;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config-test.xml"})
public class ApiItxProtocolProtocolControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiItxProtocolProtocolControllerTest.class);
	
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    @Test
    @Transactional
    @Rollback(value=true)
    public void createFromJson() throws Exception {
    	List<Protocol> protocols = Protocol.findProtocolEntries(0, 2);
    	Protocol firstProtocol = protocols.get(0);
    	Protocol secondProtocol = protocols.get(1);
    	ItxProtocolProtocol newItx = new ItxProtocolProtocol();
    	newItx.setFirstProtocol(firstProtocol);
    	newItx.setSecondProtocol(secondProtocol);
    	newItx.setLsType("has member");
    	newItx.setLsKind("parent_child");
    	newItx.setRecordedBy("test");
    	newItx.setRecordedDate(new Date());
    	String json = newItx.toJson();
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/itxprotocolprotocols/")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	ItxProtocolProtocol postedItx = ItxProtocolProtocol.fromJsonToItxProtocolProtocol(responseJson);
    	logger.info(postedItx.toJson());
    }
    
    @Test
    @Transactional
    @Rollback(value=true)
    public void createFromJsonArray() throws Exception {
    	List<Protocol> protocols = Protocol.findProtocolEntries(0, 3);
    	Protocol firstProtocol = protocols.get(0);
    	Protocol secondProtocol = protocols.get(1);
    	Protocol thirdProtocol = protocols.get(2);
    	ItxProtocolProtocol newItx = new ItxProtocolProtocol();
    	newItx.setFirstProtocol(firstProtocol);
    	newItx.setSecondProtocol(secondProtocol);
    	newItx.setLsType("has member");
    	newItx.setLsKind("parent_child");
    	newItx.setRecordedBy("test");
    	newItx.setRecordedDate(new Date());
    	ItxProtocolProtocol newItx2 = new ItxProtocolProtocol();
    	newItx2.setFirstProtocol(firstProtocol);
    	newItx2.setSecondProtocol(thirdProtocol);
    	newItx2.setLsType("has member");
    	newItx2.setLsKind("parent_child");
    	newItx2.setRecordedBy("test");
    	newItx2.setRecordedDate(new Date());
    	Collection<ItxProtocolProtocol> itxProtocolProtocols = new ArrayList<ItxProtocolProtocol>();
    	itxProtocolProtocols.add(newItx);
    	itxProtocolProtocols.add(newItx2);
    	String json = ItxProtocolProtocol.toJsonArray(itxProtocolProtocols);
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/itxprotocolprotocols/jsonArray")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<ItxProtocolProtocol> results = ItxProtocolProtocol.fromJsonArrayToItxProtocolProtocols(responseJson);
    	logger.info(ItxProtocolProtocol.toJsonArray(results));
    }
    
}