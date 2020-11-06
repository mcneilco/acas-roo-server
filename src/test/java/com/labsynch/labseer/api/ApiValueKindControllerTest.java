package com.labsynch.labseer.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.labsynch.labseer.domain.LabelSequence;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.ValueKind;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ValueTypeKindDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config-test.xml"})
public class ApiValueKindControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiValueKindControllerTest.class);
	
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    @Test
    public void getOrCreateValueKinds() throws Exception {
    	String json = "[{\"lsType\":\"test\",\"lsKind\":\"test\"}]";
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/valuekinds/getOrCreate/jsonArray")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	List<ValueKind> dtos = (List<ValueKind>) ValueKind.fromJsonArrayToValueKinds(responseJson);
    	Assert.assertNotNull(dtos.get(0).getId());
    }
    
    @Test
    public void getValueKindsByDTO() throws Exception {
    	String json = "[{\"lsType\":\"codeValue\",\"lsKind\":\"scientist\"}]";
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/valuekinds/get/jsonArray")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	List<ValueTypeKindDTO> dtos = (List<ValueTypeKindDTO>) ValueTypeKindDTO.fromJsonArrayToValueTypeKindDTO(responseJson);
    	Assert.assertNotNull(dtos.get(0).getValueKind());
    }

}
