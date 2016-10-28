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

import com.labsynch.labseer.domain.LabelSequence;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.dto.CodeTableDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config-test.xml"})
public class ApiCurveFitControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiCurveFitControllerTest.class);
	
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    //TODO: write the following tests. Need example data loaded into host5 to be able to set up a test suite.
    //get fit data by curve id: dose response & Ki
    //get raw data by curve id
    //get tg data by curve id
    //same as 4 above, but by experiment
    //update fit data
    //update ki fit data
    //flag wells
    
    
    @Test
    @Transactional
    public void getDisplayMinMaxByCurveId() throws Exception{
    	String json = "AG-00441632_7080";
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/curvefit/displayminmax")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<ProtocolValue> results = ProtocolValue.fromJsonArrayToProtocolValues(responseJson);
    	Assert.assertEquals(2, results.size());
    }
    
    
    //"AG-00442336_7271"
    //brian [10:29 AM] this should "AG-00442336_7270"
    
    @Test
    @Transactional
    public void getFitDataByCurveIdWithMixedRenderingHintExistence() throws Exception{
    	String json = "[\"AG-00442336_7271\",\"AG-00441632_7080\"]";

    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/curvefit/fitdata")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    }
    
    @Test
    @Transactional
    public void getFitDataByCurveIdWithoutRenderingHint() throws Exception{
    	String json = "[\"AG-00442336_7270\"]";

    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/curvefit/fitdata")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    }
    
    @Test
    @Transactional
    public void getFitDataByCurveIdWithRenderingHint() throws Exception{
    	String json = "[\"AG-00441632_7080\"]";

    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/curvefit/fitdata")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    }
    
    

}