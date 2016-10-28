package com.labsynch.labseer.api;


import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.ModelAndViewAssert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.junit.runners.JUnit4;
import org.springframework.mock.staticmock.MockStaticEntityMethods;

//import com.google.common.collect.Lists;
//import org.apache.http.HttpHeaders;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import java.util.List;

import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;







import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.service.AnalysisGroupService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config-test.xml"})
@Transactional
public class ApiAnalysisGroupControllerTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiAnalysisGroupControllerTest.class);

	
	@Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    
    @Autowired
    private AnalysisGroupService analysisGroupService;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    @Test
    public void parseAnalysisGroupsFromJSONTest() throws Exception {
    	String json = "[{\"id\":16752,\"lsKind\":\"default\",\"lsStates\":[{\"lsKind\":\"dose response\",\"lsTransaction\":65,\"lsType\":\"data\",\"lsValues\":[{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"Min\",\"lsTransaction\":65,\"lsType\":\"numericValue\",\"numericValue\":17.1522,\"publicData\":true,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":\"efficacy\"},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"SSE\",\"lsTransaction\":65,\"lsType\":\"numericValue\",\"numericValue\":1640.4174,\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"SST\",\"lsTransaction\":65,\"lsType\":\"numericValue\",\"numericValue\":30844.8291,\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"rSquared\",\"lsTransaction\":65,\"lsType\":\"numericValue\",\"numericValue\":0.9468,\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"Rendering Hint\",\"lsTransaction\":65,\"lsType\":\"stringValue\",\"numericValue\":null,\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":\"TRUE\",\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"category\",\"lsTransaction\":65,\"lsType\":\"stringValue\",\"numericValue\":null,\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":\"sigmoid\",\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null},{\"clobValue\":null,\"codeValue\":\"\",\"lsKind\":\"algorithm flag status\",\"lsTransaction\":65,\"lsType\":\"codeValue\",\"numericValue\":null,\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"Max\",\"lsTransaction\":65,\"lsType\":\"numericValue\",\"numericValue\":91.5111,\"publicData\":true,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":\"efficacy\"},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"Slope\",\"lsTransaction\":65,\"lsType\":\"numericValue\",\"numericValue\":1.2444,\"publicData\":true,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"EC50\",\"lsTransaction\":65,\"lsType\":\"numericValue\",\"numericValue\":0.5135,\"publicData\":true,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":\"uM\"},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"curve id\",\"lsTransaction\":65,\"lsType\":\"stringValue\",\"numericValue\":null,\"publicData\":true,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":\"AG-00000205_65\",\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"Fitted Slope\",\"lsTransaction\":65,\"lsType\":\"numericValue\",\"numericValue\":-1.2444,\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"Fitted Min\",\"lsTransaction\":65,\"lsType\":\"numericValue\",\"numericValue\":17.1522,\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":\"efficacy\"},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"Fitted Max\",\"lsTransaction\":65,\"lsType\":\"numericValue\",\"numericValue\":91.5111,\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":\"efficacy\"},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"Fitted EC50\",\"lsTransaction\":65,\"lsType\":\"numericValue\",\"numericValue\":0.5135,\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":\"uM\"}],\"recordedBy\":\"bob\",\"recordedDate\":1433969151000},{\"lsKind\":\"results\",\"lsTransaction\":65,\"lsType\":\"data\",\"lsValues\":[{\"clobValue\":null,\"codeValue\":\"CMPD-0000011-01A\",\"lsKind\":\"batch code\",\"lsTransaction\":65,\"lsType\":\"codeValue\",\"numericValue\":null,\"publicData\":true,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null}],\"recordedBy\":\"bob\",\"recordedDate\":1433969151000}],\"lsType\":\"default\",\"recordedBy\":\"bob\",\"recordedDate\":1433969152000}]";
    	try{
    		Collection<AnalysisGroup> analysisGroups = AnalysisGroup.fromJsonArrayToAnalysisGroups(json);
    	}catch (Exception e){
    		logger.error(e.toString());
    	}
    }
    
    @Test
    @Transactional
    public void updateAnalysisGroupsFromJSONServiceTest() {
    	String json = "[{\"id\":546,\"lsKind\":\"default\",\"lsStates\":[{\"lsKind\":\"dose response\",\"lsTransaction\":65,\"lsType\":\"data\",\"lsValues\":[{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"Min\",\"lsTransaction\":65,\"lsType\":\"numericValue\",\"numericValue\":17.1522,\"publicData\":true,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":\"efficacy\"},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"SSE\",\"lsTransaction\":65,\"lsType\":\"numericValue\",\"numericValue\":1640.4174,\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"SST\",\"lsTransaction\":65,\"lsType\":\"numericValue\",\"numericValue\":30844.8291,\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"rSquared\",\"lsTransaction\":65,\"lsType\":\"numericValue\",\"numericValue\":0.9468,\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"Rendering Hint\",\"lsTransaction\":65,\"lsType\":\"stringValue\",\"numericValue\":null,\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":\"TRUE\",\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"category\",\"lsTransaction\":65,\"lsType\":\"stringValue\",\"numericValue\":null,\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":\"sigmoid\",\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null},{\"clobValue\":null,\"codeValue\":\"\",\"lsKind\":\"algorithm flag status\",\"lsTransaction\":65,\"lsType\":\"codeValue\",\"numericValue\":null,\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"Max\",\"lsTransaction\":65,\"lsType\":\"numericValue\",\"numericValue\":91.5111,\"publicData\":true,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":\"efficacy\"},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"Slope\",\"lsTransaction\":65,\"lsType\":\"numericValue\",\"numericValue\":1.2444,\"publicData\":true,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"EC50\",\"lsTransaction\":65,\"lsType\":\"numericValue\",\"numericValue\":0.5135,\"publicData\":true,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":\"uM\"},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"curve id\",\"lsTransaction\":65,\"lsType\":\"stringValue\",\"numericValue\":null,\"publicData\":true,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":\"AG-00000205_65\",\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"Fitted Slope\",\"lsTransaction\":65,\"lsType\":\"numericValue\",\"numericValue\":-1.2444,\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"Fitted Min\",\"lsTransaction\":65,\"lsType\":\"numericValue\",\"numericValue\":17.1522,\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":\"efficacy\"},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"Fitted Max\",\"lsTransaction\":65,\"lsType\":\"numericValue\",\"numericValue\":91.5111,\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":\"efficacy\"},{\"clobValue\":null,\"codeValue\":null,\"lsKind\":\"Fitted EC50\",\"lsTransaction\":65,\"lsType\":\"numericValue\",\"numericValue\":0.5135,\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":\"uM\"}],\"recordedBy\":\"bob\",\"recordedDate\":1433969151000},{\"lsKind\":\"results\",\"lsTransaction\":65,\"lsType\":\"data\",\"lsValues\":[{\"clobValue\":null,\"codeValue\":\"CMPD-0000011-01A\",\"lsKind\":\"batch code\",\"lsTransaction\":65,\"lsType\":\"codeValue\",\"numericValue\":null,\"publicData\":true,\"recordedBy\":\"bob\",\"recordedDate\":1433969151000,\"stringValue\":null,\"uncertainty\":null,\"uncertaintyType\":null,\"unitKind\":null}],\"recordedBy\":\"bob\",\"recordedDate\":1433969151000}],\"lsType\":\"default\",\"recordedBy\":\"bob\",\"recordedDate\":1433969152000}]";
		Collection<AnalysisGroup> analysisGroups = AnalysisGroup.fromJsonArrayToAnalysisGroups(json);
		for (AnalysisGroup analysisGroup : analysisGroups){
			analysisGroupService.updateLsAnalysisGroup(analysisGroup);
		}
    }
    
    @Test
    @Transactional
    @Rollback(value=false)
    public void updateAnalysisGroupsFromJSONTest() throws Exception {
    	String json = "[{\"id\":3,\"lsStates\":[{\"lsKind\":\"dose response\",\"lsTransaction\":3,\"lsType\":\"data\",\"lsValues\":[{\"lsKind\":\"Min\",\"lsTransaction\":3,\"lsType\":\"numericValue\",\"numericValue\":17.1522,\"publicData\":true,\"recordedBy\":\"bob\",\"recordedDate\":1433979304000,\"unitKind\":\"efficacy\"}],\"recordedBy\":\"bob\",\"recordedDate\":1433979304000}]}]";
    	MockHttpServletResponse response = this.mockMvc.perform(put("/api/v1/analysisgroups/jsonArray")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    }
}
