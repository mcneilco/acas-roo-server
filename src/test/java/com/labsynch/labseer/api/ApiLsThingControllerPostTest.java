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
import com.labsynch.labseer.dto.StructureSearchDTO;
import com.labsynch.labseer.exceptions.ErrorMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config-test.xml"})
@Transactional
public class ApiLsThingControllerPostTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiLsThingControllerPostTest.class);
	
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
	@Test
    @Transactional
    public void structureSearch() throws Exception {
		String queryMol= "\n  Mrv1641110051619032D          \n\n  5  5  0  0  0  0            999 V2000\n   -0.0446    0.6125    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n   -0.7121    0.1274    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n   -0.4572   -0.6572    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n    0.3679   -0.6572    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n    0.6228    0.1274    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n  2  3  1  0  0  0  0\n  3  4  1  0  0  0  0\n  4  5  1  0  0  0  0\n  1  2  1  0  0  0  0\n  1  5  1  0  0  0  0\nM  END\n";
		StructureSearchDTO query = new StructureSearchDTO(queryMol, "small molecule", "reagent", "SUBSTRUCTURE", 10, null);
		String json = query.toJson();
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/lsthings/structureSearch")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
//    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.debug(responseJson);
    	Collection<LsThing> searchResults = LsThing.fromJsonArrayToLsThings(responseJson);
    	Assert.assertFalse(searchResults.isEmpty());
		logger.debug(LsThing.toJsonArray(searchResults));
    }
	
    @Test
    public void registerReferenceThingTest() throws Exception {
    	String json = "{\"lsType\":\"reference\",\"lsKind\":\"webpage\",\"corpName\":\"\",\"recordedBy\":\"egao\",\"recordedDate\":1436823212184,\"shortDescription\":\" \",\"lsLabels\":[],\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"reference metadata\",\"lsValues\":[{\"lsType\":\"urlValue\",\"lsKind\":\"external url\",\"ignored\":false,\"recordedDate\":1436823212188,\"recordedBy\":\"egao\",\"value\":\"http://www.google.com\",\"urlValue\":\"http://www.google.com\"}],\"ignored\":false,\"recordedDate\":1436823212185,\"recordedBy\":\"egao\"}],\"firstLsThings\":[],\"secondLsThings\":[],\"cid\":\"c38\",\"_changing\":false,\"_previousAttributes\":{\"lsType\":\"thing\",\"lsKind\":\"thing\",\"corpName\":\"\",\"recordedBy\":\"egao\",\"recordedDate\":1436823200318,\"shortDescription\":\" \",\"lsLabels\":[],\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"reference metadata\",\"lsValues\":[{\"lsType\":\"urlValue\",\"lsKind\":\"external url\",\"ignored\":false,\"recordedDate\":1436823212188,\"recordedBy\":\"egao\",\"value\":\"http://www.google.com\",\"urlValue\":\"http://www.google.com\"}],\"ignored\":false,\"recordedDate\":1436823212185,\"recordedBy\":\"egao\"}],\"firstLsThings\":[]},\"changed\":{\"secondLsThings\":[]},\"_pending\":false,\"urlRoot\":\"/api/things/reference/webpage\",\"className\":\"WebpageReference\",\"lsProperties\":{\"defaultLabels\":[],\"defaultValues\":[{\"key\":\"external url\",\"stateType\":\"metadata\",\"stateKind\":\"reference metadata\",\"type\":\"urlValue\",\"kind\":\"external url\"}],\"defaultFirstLsThingItx\":[],\"defaultSecondLsThingItx\":[]},\"validationError\":null,\"idAttribute\":\"id\"}";
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/lsthings/reference/webpage")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	LsThing postedLsThing = LsThing.fromJsonToLsThing(responseJson);
    	logger.info(postedLsThing.toJson());
    }

}