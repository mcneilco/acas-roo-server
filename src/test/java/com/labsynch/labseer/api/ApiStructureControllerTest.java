package com.labsynch.labseer.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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

import com.labsynch.labseer.dto.MolPropertiesDTO;
import com.labsynch.labseer.dto.StructureSearchDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config-test.xml"})
public class ApiStructureControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiStructureControllerTest.class);
	
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
	    
    //@Test
    @Transactional
    public void calculateMoleculeProperties() throws Exception{
    	String json = "{\"molStructure\":\"\\n  Mrv1641110051619032D          \\n\\n  5  5  0  0  0  0            999 V2000\\n   -0.0446    0.6125    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\\n   -0.7121    0.1274    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\\n   -0.4572   -0.6572    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\\n    0.3679   -0.6572    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\\n    0.6228    0.1274    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\\n  2  3  1  0  0  0  0\\n  3  4  1  0  0  0  0\\n  4  5  1  0  0  0  0\\n  1  2  1  0  0  0  0\\n  1  5  1  0  0  0  0\\nM  END\\n\"}";
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/structure/calculateMoleculeProperties")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
		logger.info(response.getContentAsString());
		
		MolPropertiesDTO result = MolPropertiesDTO.fromJsonToMolPropertiesDTO(response.getContentAsString());
		Assert.assertEquals("C4H8O",result.getMolFormula());
		Assert.assertTrue(result.getMolStructure().length()>0);
		Assert.assertTrue(result.getMolWeight() - new Double(72.1059) < 0.001);
    }

    @Test
    @Transactional
    public void searchStructures() throws Exception{
    	String json = "{\"queryMol\":\"Molecule from ChemDoodle Web Components\\n\\nhttp://www.ichemlabs.com\\n  5  5  0  0  0  0            999 V2000\\n    0.0000    0.7694    0.0000 C   0  0  0  0  0  0\\n    0.8090    0.1816    0.0000 C   0  0  0  0  0  0\\n    0.5000   -0.7694    0.0000 C   0  0  0  0  0  0\\n   -0.5000   -0.7694    0.0000 C   0  0  0  0  0  0\\n   -0.8090    0.1816    0.0000 C   0  0  0  0  0  0\\n  1  2  1  0     0  0\\n  2  3  1  0     0  0\\n  3  4  1  0     0  0\\n  4  5  1  0     0  0\\n  5  1  1  0     0  0\\nM  END\"}";
    	StructureSearchDTO queryDTO = StructureSearchDTO.fromJsonToStructureSearchDTO(json);
//    	queryDTO.setLsType("chemistry");
//    	queryDTO.setLsKind("monomer");
    	queryDTO.setMaxResults(100);
    	queryDTO.setSearchType("exact");
    	logger.info(queryDTO.toJson());
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/structure/searchStructures")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(queryDTO.toJson()))
    			//.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
		logger.info(response.getContentAsString());
		
    }
    
   // @Test
    @Transactional
    public void searchStructuresAndMeta() throws Exception{
    	String json = "{\"molStructure\":\"\\n  Mrv1641110051619032D          \\n\\n  5  5  0  0  0  0            999 V2000\\n   -0.0446    0.6125    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\\n   -0.7121    0.1274    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\\n   -0.4572   -0.6572    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\\n    0.3679   -0.6572    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\\n    0.6228    0.1274    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\\n  2  3  1  0  0  0  0\\n  3  4  1  0  0  0  0\\n  4  5  1  0  0  0  0\\n  1  2  1  0  0  0  0\\n  1  5  1  0  0  0  0\\nM  END\\n\"}";
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/structure/structureAndMetaSearch")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
		logger.info(response.getContentAsString());
		
    }
    
    @Test
	public void convertSmilesToMol() throws Exception{
		String smiles = "O=C1NC%91=NC2NC=NC=21.[*:1]%91 |$;;;;;;;;;;_R1$|";
		//String smiles = "CCC>>CCN";
		MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/structure/convertSmilesToMol")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(smiles))
    			.andExpect(status().isOk())
    			.andReturn().getResponse();
		String molStructure = response.getContentAsString();
		logger.info(molStructure);
	}
    
    @Test
	public void cleanMolStructure() throws Exception{
    	String molStructure = "\n  Mrv1641110051619032D          \n\n  5  5  0  0  0  0            999 V2000\n   -0.0446    0.6125    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n   -0.7121    0.1274    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n   -0.4572   -0.6572    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n    0.3679   -0.6572    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n    0.6228    0.1274    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n  2  3  1  0  0  0  0\n  3  4  1  0  0  0  0\n  4  5  1  0  0  0  0\n  1  2  1  0  0  0  0\n  1  5  1  0  0  0  0\nM  END\n";
		//String smiles = "CCC>>CCN";
		MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/structure/cleanMolStructure")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(molStructure))
    			.andExpect(status().isOk())
    			.andReturn().getResponse();
		String cleanedMolStructure = response.getContentAsString();
		logger.info(cleanedMolStructure);
	}
    

}
