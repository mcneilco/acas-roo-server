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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentLabel;
import com.labsynch.labseer.dto.ExperimentErrorMessageDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config-test.xml"})
@Transactional
public class ApiExperimentControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiExperimentControllerTest.class);
	
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
  //  @Test
    public void genericSearchByScientist() throws Exception {
    	String searchString = "bob";
    	String responseJson =  this.mockMvc.perform(get("/api/v1/experiments/search?q="+searchString)
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson.toString());
    	Collection<Experiment> results = Experiment.fromJsonArrayToExperiments(responseJson);
    	Assert.assertFalse(results.isEmpty());
    }
    
  //  @Test
    public void advancedGeneFilter() throws Exception {
    	String json = "{\"experimentCodeList\":[\"EXPT-00000001\", \"PROT-00000001\"]}";
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/experiments/agdata/batchcodelist/experimentcodelist?format=csv&onlyPublicData=false")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
//    			.andExpect(status().isCreated())
//    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    }
    
    @Test
    public void getGeneCodeName() throws Exception {
    	String json = "{\"requests\":[{\"requestName\":\"HYST2477\"},{\"requestName\":\"2\"}]}";
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/lsthings/getGeneCodeNameFromNameRequest")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
//    			.andExpect(status().isCreated())
//    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    }
    
    @Test
    public void genericSearchByProtocolCodeName() throws Exception {
    	String searchString = "PROT-00000131";
    	String responseJson =  this.mockMvc.perform(get("/api/v1/experiments/search?q="+searchString)
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson.toString());
    	Collection<Experiment> results = Experiment.fromJsonArrayToExperiments(responseJson);
    	Assert.assertFalse(results.isEmpty());
    }
    
//    @Test
    public void genericSearchByDate() throws Exception {
    	String searchString = "2015-03-05";
    	String responseJson =  this.mockMvc.perform(get("/api/v1/experiments/search?q="+searchString)
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson.toString());
    	Collection<Experiment> results = Experiment.fromJsonArrayToExperiments(responseJson);
    	Assert.assertFalse(results.isEmpty());
    }
    
//    @Test
    public void genericSearchByAnalysisStatus() throws Exception {
    	String searchString = "Fiona approved";
    	String responseJson =  this.mockMvc.perform(get("/api/v1/experiments/search?q="+searchString)
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson.toString());
    	Collection<Experiment> results = Experiment.fromJsonArrayToExperiments(responseJson);
    	Assert.assertFalse(results.isEmpty());
    }
    
//    @Test
    public void genericSearchByExperimentStatus() throws Exception {
    	String searchString = "bob";
    	String responseJson =  this.mockMvc.perform(get("/api/v1/experiments/search?q="+searchString)
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson.toString());
    	Collection<Experiment> results = Experiment.fromJsonArrayToExperiments(responseJson);
    	Assert.assertFalse(results.isEmpty());
    }
    
    @Test
    public void genericSearchForAllExperiments() throws Exception {
    	String searchString = "fly";
    	String responseJson =  this.mockMvc.perform(get("/api/v1/experiments/search?q="+searchString)
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isInternalServerError())
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson.toString());
    	Assert.assertTrue(responseJson.contains("Too many"));
    }
    
    @Test
    public void getExperimentsByCodeName_StubWithProt() throws Exception {
    	Collection<Experiment> experiments = Experiment.findAllExperiments();
    	List<String> codeNames = new ArrayList<String>();
    	for (Experiment experiment : experiments){
    		codeNames.add("\""+experiment.getCodeName()+"\"");
    	}
    	String format = "stubWithProt";
    	logger.info(codeNames.toString());
    	String responseJson =  this.mockMvc.perform(post("/api/v1/experiments/codename/jsonArray?with="+format)
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(codeNames.toString())
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andReturn().getResponse().getContentAsString();
    	logger.info(responseJson.toString());
    	Collection<ExperimentErrorMessageDTO> responses = ExperimentErrorMessageDTO.fromJsonArrayToExperimentErroes(responseJson);
    	for (ExperimentErrorMessageDTO response : responses){
    		Assert.assertNotNull(response.getExperimentCodeName());
        	Assert.assertNotNull(response.getExperiment());
        	Assert.assertNotNull(response.getExperiment().getLsStates());
        	Assert.assertTrue(!response.getExperiment().getLsStates().isEmpty());
        	Assert.assertNotNull(response.getExperiment().getLsLabels());
        	Assert.assertTrue(!response.getExperiment().getLsLabels().isEmpty());
        	Assert.assertNotNull(response.getExperiment().getProtocol());
        	Assert.assertNotNull(response.getExperiment().getProtocol().getLsLabels());
        	Assert.assertTrue(!response.getExperiment().getProtocol().getLsLabels().isEmpty());
    	}
    }

}