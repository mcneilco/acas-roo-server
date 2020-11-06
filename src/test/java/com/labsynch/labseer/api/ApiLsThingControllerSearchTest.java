package com.labsynch.labseer.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.dto.LabelQueryDTO;
import com.labsynch.labseer.dto.LsThingQueryDTO;
import com.labsynch.labseer.dto.StructureAndThingSearchDTO;
import com.labsynch.labseer.dto.StructureSearchDTO;
import com.labsynch.labseer.dto.ValueQueryDTO;
import com.labsynch.labseer.exceptions.ErrorMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config-test.xml"})
@Transactional
public class ApiLsThingControllerSearchTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiLsThingControllerSearchTest.class);
	
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    

//	@Test
    @Transactional
    public void structureSearch() throws Exception {
    	String json = "{\"queryMol\":\"Molecule from ChemDoodle Web Components\\n\\nhttp://www.ichemlabs.com\\n  5  5  0  0  0  0            999 V2000\\n    0.0000    0.7694    0.0000 C   0  0  0  0  0  0\\n    0.8090    0.1816    0.0000 C   0  0  0  0  0  0\\n    0.5000   -0.7694    0.0000 C   0  0  0  0  0  0\\n   -0.5000   -0.7694    0.0000 C   0  0  0  0  0  0\\n   -0.8090    0.1816    0.0000 C   0  0  0  0  0  0\\n  1  2  1  0     0  0\\n  2  3  1  0     0  0\\n  3  4  1  0     0  0\\n  4  5  1  0     0  0\\n  5  1  1  0     0  0\\nM  END\"}";
		//String queryMol= "\n  Mrv1641110051619032D          \n\n  5  5  0  0  0  0            999 V2000\n   -0.0446    0.6125    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n   -0.7121    0.1274    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n   -0.4572   -0.6572    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n    0.3679   -0.6572    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n    0.6228    0.1274    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n  2  3  1  0  0  0  0\n  3  4  1  0  0  0  0\n  4  5  1  0  0  0  0\n  1  2  1  0  0  0  0\n  1  5  1  0  0  0  0\nM  END\n";
		//StructureSearchDTO query = new StructureSearchDTO(queryMol, "", "", "SUBSTRUCTURE", 10, null);
		StructureSearchDTO query = StructureSearchDTO.fromJsonToStructureSearchDTO(json);
		query.setSearchType("SUBSTRUCTURE");
		query.setMaxResults(10);
		
		String inputJson = query.toJson();
		logger.info(query.toJson());
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/lsthings/structureSearch")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(inputJson)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
//    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.debug(responseJson);
    	Collection<LsThing> searchResults = LsThing.fromJsonArrayToLsThings(responseJson);
//    	Assert.assertTrue(searchResults.isEmpty());
		logger.debug(LsThing.toJsonArray(searchResults));
    }
	
//	@Test
    @Transactional
    public void thingMetadataSearch() throws Exception {
		LsThingQueryDTO query = new LsThingQueryDTO();
		query.setLsType("parent");
		query.setLsKind("monomer");
		ValueQueryDTO valueQuery = new ValueQueryDTO();
		valueQuery.setStateType("properties");
		valueQuery.setStateKind("parent properties");
		valueQuery.setValueType("codeValue");
		valueQuery.setValueKind("structure");
		//valueQuery.setValue("");
		Collection<ValueQueryDTO> values = new HashSet<ValueQueryDTO>();
		values.add(valueQuery);
		query.setValues(values);		
		String json = query.toJson();
		logger.info(json);
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/lsthings/genericInteractionSearch")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
//    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info("########################");
    	logger.info(responseJson);
//    	Collection<LsThing> searchResults = LsThing.fromJsonArrayToLsThings(responseJson);
//    	Assert.assertFalse(searchResults.isEmpty());
//		logger.debug(LsThing.toJsonArray(searchResults));
    }
	
	//@Test
    @Transactional
    public void thingMetadataStructureSearch() throws Exception {
		StructureAndThingSearchDTO metaStructQuery = new StructureAndThingSearchDTO();		
		LsThingQueryDTO thingQuery = new LsThingQueryDTO();
		thingQuery.setLsType("parent");
		thingQuery.setLsKind("monomer");
		ValueQueryDTO valueQuery = new ValueQueryDTO();
		valueQuery.setStateType("properties");
		valueQuery.setStateKind("parent properties");
		valueQuery.setValueType("codeValue");
		valueQuery.setValueKind("structure");
		//valueQuery.setValue("");
		Collection<ValueQueryDTO> values = new HashSet<ValueQueryDTO>();
		values.add(valueQuery);
		thingQuery.setValues(values);
		Collection<LabelQueryDTO> labels = new HashSet<LabelQueryDTO>();
		LabelQueryDTO label = new LabelQueryDTO();
		label.setLabelType("name");
		//label.setLabelKind("alias");
		label.setLabelText("201");
		label.setOperator("like");
		labels.add(label);
//		LabelQueryDTO label2 = new LabelQueryDTO();
//		label2.setLabelType("name");
//		//label2.setLabelKind("alias");
//		label2.setLabelText("201");
//		labels.add(label2);		
		
		thingQuery.setLabels(labels);
		metaStructQuery.setLsThingQueryDTO(thingQuery);
//		LsThingQueryDTO emptyThingQuery = new LsThingQueryDTO();
//		metaStructQuery.setLsThingQueryDTO(emptyThingQuery);
    	String queryMolJson = "{\"queryMol\":\"Molecule from ChemDoodle Web Components\\n\\nhttp://www.ichemlabs.com\\n  5  5  0  0  0  0            999 V2000\\n    0.0000    0.7694    0.0000 C   0  0  0  0  0  0\\n    0.8090    0.1816    0.0000 C   0  0  0  0  0  0\\n    0.5000   -0.7694    0.0000 C   0  0  0  0  0  0\\n   -0.5000   -0.7694    0.0000 C   0  0  0  0  0  0\\n   -0.8090    0.1816    0.0000 C   0  0  0  0  0  0\\n  1  2  1  0     0  0\\n  2  3  1  0     0  0\\n  3  4  1  0     0  0\\n  4  5  1  0     0  0\\n  5  1  1  0     0  0\\nM  END\"}";
		StructureSearchDTO query = StructureSearchDTO.fromJsonToStructureSearchDTO(queryMolJson);

//		String queryMol= "\n  Mrv1641110051619032D          \n\n  5  5  0  0  0  0            999 V2000\n   -0.0446    0.6125    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n   -0.7121    0.1274    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n   -0.4572   -0.6572    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n    0.3679   -0.6572    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n    0.6228    0.1274    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n  2  3  1  0  0  0  0\n  3  4  1  0  0  0  0\n  4  5  1  0  0  0  0\n  1  2  1  0  0  0  0\n  1  5  1  0  0  0  0\nM  END\n";
		metaStructQuery.setQueryMol(query.getQueryMol());
		metaStructQuery.setSearchType("SUBSTRUCTURE");
		metaStructQuery.setMaxResults(10);
		
		String json = metaStructQuery.toJson();
		logger.info("############## -- metaStructQuery #################" );
		logger.info(json);
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/lsthings/structureAndMetaSearch")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
	}
  
	@Test
    @Transactional
    public void thingMetadataStructureSearch2() throws Exception {
		StructureAndThingSearchDTO metaStructQuery = new StructureAndThingSearchDTO();		
		LsThingQueryDTO thingQuery = new LsThingQueryDTO();
		thingQuery.setLsType("parent");
		thingQuery.setLsKind("small molecule");
//		ValueQueryDTO valueQuery = new ValueQueryDTO();
//		valueQuery.setStateType("properties");
//		valueQuery.setStateKind("parent properties");
//		valueQuery.setValueType("codeValue");
//		valueQuery.setValueKind("structure");
//		//valueQuery.setValue("");
//		Collection<ValueQueryDTO> values = new HashSet<ValueQueryDTO>();
//		values.add(valueQuery);
//		thingQuery.setValues(values);
//		Collection<LabelQueryDTO> labels = new HashSet<LabelQueryDTO>();
//		LabelQueryDTO label = new LabelQueryDTO();
//		label.setLabelType("name");
//		//label.setLabelKind("alias");
//		label.setLabelText("201");
//		label.setOperator("like");
//		labels.add(label);
//		LabelQueryDTO label2 = new LabelQueryDTO();
//		label2.setLabelType("name");
//		//label2.setLabelKind("alias");
//		label2.setLabelText("201");
//		labels.add(label2);		
		
//		thingQuery.setLabels(labels);
		metaStructQuery.setLsThingQueryDTO(thingQuery);
//		LsThingQueryDTO emptyThingQuery = new LsThingQueryDTO();
//		metaStructQuery.setLsThingQueryDTO(emptyThingQuery);
//    	String queryMolJson = "{\"queryMol\":\"Molecule from ChemDoodle Web Components\n\nhttp://www.ichemlabs.com\n  6  6  0  0  0  0            999 V2000\n    0.0000    1.0000    0.0000 C   0  0  0  0  0  0\n    0.8660    0.5000    0.0000 C   0  0  0  0  0  0\n    0.8660   -0.5000    0.0000 C   0  0  0  0  0  0\n    0.0000   -1.0000    0.0000 C   0  0  0  0  0  0\n   -0.8660   -0.5000    0.0000 C   0  0  0  0  0  0\n   -0.8660    0.5000    0.0000 C   0  0  0  0  0  0\n  1  2  1  0     0  0\n  2  3  2  0     0  0\n  3  4  1  0     0  0\n  4  5  2  0     0  0\n  5  6  1  0     0  0\n  6  1  2  0     0  0\nM  END\",\"searchType\":\"SUBSTRUCTURE\",\"lsType\":\"chemistry\",\"lsKind\":\"reagent\",\"maxResults\":100,\"similarity\":null}";
    	String queryMolJson = "{\"queryMol\":\"Molecule from ChemDoodle Web Components\\n\\nhttp://www.ichemlabs.com\\n  5  5  0  0  0  0            999 V2000\\n    0.0000    0.7694    0.0000 C   0  0  0  0  0  0\\n    0.8090    0.1816    0.0000 C   0  0  0  0  0  0\\n    0.5000   -0.7694    0.0000 C   0  0  0  0  0  0\\n   -0.5000   -0.7694    0.0000 C   0  0  0  0  0  0\\n   -0.8090    0.1816    0.0000 C   0  0  0  0  0  0\\n  1  2  1  0     0  0\\n  2  3  1  0     0  0\\n  3  4  1  0     0  0\\n  4  5  1  0     0  0\\n  5  1  1  0     0  0\\nM  END\"}";
  	
    	StructureSearchDTO query = StructureSearchDTO.fromJsonToStructureSearchDTO(queryMolJson);
		metaStructQuery.setQueryMol(query.getQueryMol());
		metaStructQuery.setSearchType("SUBSTRUCTURE");
		metaStructQuery.setMaxResults(10);
		
		String json = metaStructQuery.toJson();
		logger.info("############## -- metaStructQuery #################" );
		logger.info(json);
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/lsthings/structureAndMetaSearch")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(json)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
	}
    
    
}
