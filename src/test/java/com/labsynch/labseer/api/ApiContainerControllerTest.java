package com.labsynch.labseer.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.LabelSequence;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.dto.CmpdRegBatchCodeDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.CodeLabelDTO;
import com.labsynch.labseer.dto.PlateWellDTO;
import com.labsynch.labseer.dto.WellContentDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config-test.xml"})
public class ApiContainerControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiContainerControllerTest.class);
	
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    
    @Test
    @Transactional
    public void getWellCodesByPlateBarcodes() throws Exception{
    	List<String> plateBarcodes = new ArrayList<String>();
		plateBarcodes.add("\""+Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate").getResultList().get(0).getLsLabels().iterator().next().getLabelText()+"\"");	
		String json = plateBarcodes.toString();
		logger.info(json);
		Assert.assertFalse(json.equals("{}"));
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/getWellCodesByPlateBarcodes")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<PlateWellDTO> results = PlateWellDTO.fromJsonArrayToPlateWellDTO(responseJson);
    	for (PlateWellDTO result : results){
    		Assert.assertNotNull(result.getPlateBarcode());
    		if (result.getPlateCodeName() != null){
    			Assert.assertNotNull(result.getWellCodeName());
    			Assert.assertNotNull(result.getWellLabel());
    		}
    	}
    }
    
    @Test
    @Transactional
    public void getContainerCodesByLabels() throws Exception{
    	List<String> plateBarcodes = new ArrayList<String>();
		plateBarcodes.add("\""+Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate").getResultList().get(0).getLsLabels().iterator().next().getLabelText()+"\"");	
		String json = plateBarcodes.toString();
		logger.info(json);
		Assert.assertFalse(json.equals("{}"));
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/getContainerCodesByLabels")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<CodeLabelDTO> results = CodeLabelDTO.fromJsonArrayToCoes(responseJson);
    	for (CodeLabelDTO result : results){
    		Assert.assertNotNull(result.getCodeName());
    		Assert.assertNotNull(result.getLabel());
    	}
    }
    
    @Test
    @Transactional
    public void getContainerCodesByLabelsWithTypeKinds() throws Exception{
    	List<String> plateBarcodes = new ArrayList<String>();
		plateBarcodes.add("\""+Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate").getResultList().get(0).getLsLabels().iterator().next().getLabelText()+"\"");	
		String json = plateBarcodes.toString();
		logger.info(json);
		Assert.assertFalse(json.equals("{}"));
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/getContainerCodesByLabels"
    			+ "?containerType=container&containerKind=plate&labelType=barcode&labelKind=barcode")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<CodeLabelDTO> results = CodeLabelDTO.fromJsonArrayToCoes(responseJson);
    	for (CodeLabelDTO result : results){
    		Assert.assertNotNull(result.getCodeName());
    		Assert.assertNotNull(result.getLabel());
    	}
    }
    
    @Test
    @Transactional
    public void getWellContent() throws Exception{
    	List<String> wellCodes = new ArrayList<String>();
		wellCodes.add("\""+Container.findContainersByLsTypeEqualsAndLsKindEquals("physical","well").getResultList().get(0).getCodeName()+"\"");
		String json = wellCodes.toString();
		logger.info(json);
		Assert.assertFalse(json.equals("{}"));
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/getWellContent")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<WellContentDTO> results = WellContentDTO.fromJsonArrayToWellCoes(responseJson);
    	for (WellContentDTO result : results){
    		Assert.assertNotNull(result.getWellCodeName());
    	}
    }
    

}