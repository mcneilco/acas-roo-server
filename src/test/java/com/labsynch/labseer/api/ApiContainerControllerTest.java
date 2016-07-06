package com.labsynch.labseer.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.TypedQuery;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerLabel;
import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ContainerValue;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentLabel;
import com.labsynch.labseer.domain.ItxSubjectContainer;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.TreatmentGroup;
import com.labsynch.labseer.dto.CodeLabelDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ContainerDependencyCheckDTO;
import com.labsynch.labseer.dto.ContainerErrorMessageDTO;
import com.labsynch.labseer.dto.ContainerLocationDTO;
import com.labsynch.labseer.dto.ContainerRequestDTO;
import com.labsynch.labseer.dto.ContainerSearchRequestDTO;
import com.labsynch.labseer.dto.ContainerWellCodeDTO;
import com.labsynch.labseer.dto.CreatePlateRequestDTO;
import com.labsynch.labseer.dto.DependencyCheckDTO;
import com.labsynch.labseer.dto.PlateStubDTO;
import com.labsynch.labseer.dto.PlateWellDTO;
import com.labsynch.labseer.dto.PreferredNameDTO;
import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.dto.WellContentDTO;
import com.labsynch.labseer.dto.WellStubDTO;
import com.labsynch.labseer.exceptions.ErrorMessage;
import com.labsynch.labseer.service.ContainerService;

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
    
    @Autowired
	private ContainerService containerService;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
//    @Test
    @Transactional
    public void getContainerByLsTypeAndLsKind() throws Exception{
    	MockHttpServletResponse response;
    	String responseJson;
    	Collection<Container> foundContainers;
    	//no type and kind
    	response = this.mockMvc.perform(get("/api/v1/containers")
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	foundContainers = Container.fromJsonArrayToContainers(responseJson);
    	Assert.assertFalse(foundContainers.isEmpty());
    	//only type
    	response = this.mockMvc.perform(get("/api/v1/containers?lsType=subject")
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	foundContainers = Container.fromJsonArrayToContainers(responseJson);
    	Assert.assertFalse(foundContainers.isEmpty());
    	//only kind
    	response = this.mockMvc.perform(get("/api/v1/containers?lsKind=nhp test subject")
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	foundContainers = Container.fromJsonArrayToContainers(responseJson);
    	Assert.assertFalse(foundContainers.isEmpty());
    	//type and kind
    	response = this.mockMvc.perform(get("/api/v1/containers?lsType=subject&lsKind=nhp test subject")
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	foundContainers = Container.fromJsonArrayToContainers(responseJson);
    	Assert.assertFalse(foundContainers.isEmpty());
    	//find no results
    	response = this.mockMvc.perform(get("/api/v1/containers?lsType=nope&lsKind=")
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	foundContainers = Container.fromJsonArrayToContainers(responseJson);
    	Assert.assertTrue(foundContainers.isEmpty());
    }
    
    
    @Test
    @Transactional
    public void getWellCodesByPlateBarcodes() throws Exception{
    	List<String> plateBarcodes = new ArrayList<String>();
    	TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate");
    	query.setMaxResults(3);
    	for (Container container : query.getResultList()){
    		plateBarcodes.add("\""+container.getLsLabels().iterator().next().getLabelText()+"\"");	
    	}
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
    	TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate");
    	query.setMaxResults(3);
    	for (Container container : query.getResultList()){
    		plateBarcodes.add("\""+container.getLsLabels().iterator().next().getLabelText()+"\"");	
    	}
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
    	int i=0;
    	for (CodeLabelDTO result : results){
    		Assert.assertNotNull(result.getRequestLabel());
    		Assert.assertFalse(result.getFoundCodeNames().isEmpty());
    		Assert.assertEquals(plateBarcodes.get(i).replaceAll("\"", ""), result.getRequestLabel());
    		i++;
    	}
    }
    
    @Test
    @Transactional
    public void getContainerCodesByLabels_with_noresult_entries() throws Exception{
    	List<String> plateBarcodes = new ArrayList<String>();
    	TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate");
    	query.setMaxResults(3);
    	for (Container container : query.getResultList()){
    		plateBarcodes.add("\""+container.getLsLabels().iterator().next().getLabelText()+"\"");	
    	}
    	plateBarcodes.add(1, "\"BADBARCODE\"");
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
    	int i=0;
    	for (CodeLabelDTO result : results){
    		Assert.assertNotNull(result.getRequestLabel());
//    		Assert.assertFalse(result.getFoundCodeNames().isEmpty());
    		Assert.assertEquals(plateBarcodes.get(i).replaceAll("\"", ""), result.getRequestLabel());
    		i++;
    	}
    }
    
    @Test
    @Transactional
    public void getContainerCodesByLabelsWithTypeKinds() throws Exception{
    	List<String> plateBarcodes = new ArrayList<String>();
    	TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate");
    	query.setMaxResults(3);
    	for (Container container : query.getResultList()){
    		plateBarcodes.add("\""+container.getLsLabels().iterator().next().getLabelText()+"\"");	
    	}	
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
    	int i=0;
    	for (CodeLabelDTO result : results){
    		Assert.assertNotNull(result.getRequestLabel());
    		Assert.assertFalse(result.getFoundCodeNames().isEmpty());
    		Assert.assertEquals(plateBarcodes.get(i).replaceAll("\"", ""), result.getRequestLabel());
    		i++;
    	}
    }
    
    @Test
    @Transactional
    public void getWellContent() throws Exception{
    	List<String> wellCodes = new ArrayList<String>();
    	TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("well","default");
    	query.setMaxResults(25);
    	for (Container container : query.getResultList()){
    		wellCodes.add("\""+container.getCodeName()+"\"");	
    	}
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
    	int i = 0;
    	for (WellContentDTO result : results){
    		Assert.assertNotNull(result.getContainerCodeName());
    		Assert.assertEquals(wellCodes.get(i).replaceAll("\"",""), result.getContainerCodeName());
    		if (result.getLevel() != null) Assert.assertNotNull(result.getWellName());
    		i++;
    	}
    }
    
    @Test
    @Transactional
    public void containerDependencyCheck_Well() throws Exception{
    	String codeName = Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate").getResultList().get(0).getCodeName();
    	MockHttpServletResponse response = this.mockMvc.perform(get("/api/v1/containers/checkDependencies/"+codeName)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
//    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	DependencyCheckDTO results = DependencyCheckDTO.fromJsonToDependencyCheckDTO(responseJson);
    	
    }
    
    @Test
    @Transactional
    public void validateContainerUniqueName() throws Exception{
		String json = "{\"lsType\":\"subject\",\"lsKind\":\"nhp test subject\",\"recordedBy\":\"bob\",\"recordedDate\":1452795015412,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"subject name\",\"labelText\":\"Test Cont 8\",\"ignored\":false,\"preferred\":true,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null},{\"lsType\":\"corpName\",\"lsKind\":\"ACAS LsContainer\",\"labelText\":\"NHP-00014\",\"ignored\":false,\"preferred\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null},{\"lsType\":\"barcode\",\"lsKind\":\"subject barcode\",\"labelText\":\"\",\"ignored\":false,\"preferred\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null}],\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"subject attributes\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"sex\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"codeKind\":\"sex\",\"codeType\":\"subject attributes\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"male\",\"value\":\"male\"},{\"lsType\":\"codeValue\",\"lsKind\":\"species\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"codeKind\":\"species\",\"codeType\":\"subject attributes\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"homo sapiens\",\"value\":\"homo sapiens\"}],\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\"},{\"lsType\":\"status\",\"lsKind\":\"current subject status\",\"lsValues\":[{\"lsType\":\"dateValue\",\"lsKind\":\"weight measured date\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"value\":1452643200000,\"dateValue\":1452643200000},{\"lsType\":\"codeValue\",\"lsKind\":\"weight measured by\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"value\":\"bob\",\"codeValue\":\"bob\"},{\"lsType\":\"numericValue\",\"lsKind\":\"current weight\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"value\":123,\"numericValue\":123}],\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\"}],\"cid\":\"c162\",\"_changing\":false,\"_previousAttributes\":{\"lsType\":\"container\",\"lsKind\":\"container\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1452795007545,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"subject name\",\"labelText\":\"Test Cont 8\",\"ignored\":false,\"preferred\":true,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null},{\"lsType\":\"corpName\",\"lsKind\":\"ACAS LsContainer\",\"labelText\":\"NHP-00014\",\"ignored\":false,\"preferred\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null},{\"lsType\":\"barcode\",\"lsKind\":\"subject barcode\",\"labelText\":\"\",\"ignored\":false,\"preferred\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null}]},\"changed\":{\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"subject attributes\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"sex\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"codeKind\":\"sex\",\"codeType\":\"subject attributes\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"male\",\"value\":\"male\"},{\"lsType\":\"codeValue\",\"lsKind\":\"species\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"codeKind\":\"species\",\"codeType\":\"subject attributes\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"homo sapiens\",\"value\":\"homo sapiens\"}],\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\"},{\"lsType\":\"status\",\"lsKind\":\"current subject status\",\"lsValues\":[{\"lsType\":\"dateValue\",\"lsKind\":\"weight measured date\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"value\":1452643200000,\"dateValue\":1452643200000},{\"lsType\":\"codeValue\",\"lsKind\":\"weight measured by\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"value\":\"bob\",\"codeValue\":\"bob\"},{\"lsType\":\"numericValue\",\"lsKind\":\"current weight\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"value\":123,\"numericValue\":123}],\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\"}]},\"_pending\":false,\"urlRoot\":\"/api/containers/subject/nhp test subject\",\"lsProperties\":{\"defaultLabels\":[{\"key\":\"subject name\",\"type\":\"name\",\"kind\":\"subject name\",\"preferred\":true},{\"key\":\"corpName\",\"type\":\"corpName\",\"kind\":\"ACAS LsContainer\",\"preferred\":false},{\"key\":\"subject barcode\",\"type\":\"barcode\",\"kind\":\"subject barcode\",\"preferred\":false}],\"defaultValues\":[{\"key\":\"sex\",\"stateType\":\"metadata\",\"stateKind\":\"subject attributes\",\"type\":\"codeValue\",\"kind\":\"sex\",\"codeType\":\"subject attributes\",\"codeKind\":\"sex\",\"codeOrigin\":\"ACAS DDICT\",\"value\":\"\"},{\"key\":\"species\",\"stateType\":\"metadata\",\"stateKind\":\"subject attributes\",\"type\":\"codeValue\",\"kind\":\"species\",\"codeType\":\"subject attributes\",\"codeKind\":\"species\",\"codeOrigin\":\"ACAS DDICT\",\"value\":\"\"},{\"key\":\"weight measured date\",\"stateType\":\"status\",\"stateKind\":\"current subject status\",\"type\":\"dateValue\",\"kind\":\"weight measured date\"},{\"key\":\"weight measured by\",\"stateType\":\"status\",\"stateKind\":\"current subject status\",\"type\":\"codeValue\",\"kind\":\"weight measured by\"},{\"key\":\"current weight\",\"stateType\":\"status\",\"stateKind\":\"current subject status\",\"type\":\"numericValue\",\"kind\":\"current weight\"}]},\"className\":\"Container\",\"validationError\":null,\"idAttribute\":\"id\"}";
		Container jsonContainer = Container.fromJsonToContainer(json);
		MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/validate")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonContainer.toJson()))
				.andExpect(status().isConflict())
				.andExpect(content().contentType("application/json;charset=utf-8"))
				.andReturn().getResponse();
		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		Collection<ErrorMessage> errors = ErrorMessage.fromJsonArrayToErrorMessages(responseJson);
		Assert.assertFalse(errors.isEmpty());
    }
    
    @Test
    @Transactional
    public void getContainerById() throws Exception{
    	Container container = Container.findContainersByLsTypeEqualsAndLsKindEquals("container", "plate").getResultList().get(0);
    	String id = container.getId().toString();
    	MockHttpServletResponse response = this.mockMvc.perform(get("/api/v1/containers/"+id)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Container result = Container.fromJsonToContainer(responseJson);
    	Assert.assertNotNull(result.getCodeName());
    }
    
    @Test
    @Transactional
    public void getContainerByCodeName() throws Exception{
    	Container container = Container.findContainersByLsTypeEqualsAndLsKindEquals("container", "plate").getResultList().get(0);
    	String codeName = container.getCodeName();
    	MockHttpServletResponse response = this.mockMvc.perform(get("/api/v1/containers/"+codeName)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Container result = Container.fromJsonToContainer(responseJson);
    	Assert.assertNotNull(result.getCodeName());
    }
    
    @Test
    @Transactional
    public void getCodeNameFromNameRequest() throws Exception{
    	Container container = Container.findContainersByLsTypeEqualsAndLsKindEquals("container", "plate").getResultList().get(0);
    	String containerType = container.getLsType();
    	String containerKind = container.getLsKind();
		String label = container.getLsLabels().iterator().next().getLabelText();
		PreferredNameDTO request = new PreferredNameDTO(label, null, null);
		Collection<PreferredNameDTO> requests = new HashSet<PreferredNameDTO>();
		requests.add(request);
		PreferredNameRequestDTO requestDTO = new PreferredNameRequestDTO();
		requestDTO.setRequests(requests);
		String json = requestDTO.toJson();
		Assert.assertFalse(json.equals("{}"));
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/getCodeNameFromNameRequest"
    			+ "?containerType="+containerType+"&containerKind="+containerKind)
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	PreferredNameResultsDTO results = PreferredNameResultsDTO.fromJsonToPreferredNameResultsDTO(responseJson);
    	for (PreferredNameDTO result : results.getResults()){
    		Assert.assertNotNull(result.getRequestName());
    		Assert.assertNotNull(result.getReferenceName());
    		Assert.assertNotNull(result.getPreferredName());
    	}
    }
    @Test
    @Transactional
    public void getWellContentPartialSuccess() throws Exception{
    	List<String> wellCodes = new ArrayList<String>();
    	TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("well","default");
    	query.setMaxResults(25);
    	for (Container container : query.getResultList()){
    		wellCodes.add("\""+container.getCodeName()+"\"");	
    	}
    	wellCodes.add("\"NOT-A-VALID-CODE\"");
		String json = wellCodes.toString();
		logger.info(json);
		Assert.assertFalse(json.equals("{}"));
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/getWellContent")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isBadRequest())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<WellContentDTO> results = WellContentDTO.fromJsonArrayToWellCoes(responseJson);
    	for (WellContentDTO result : results){
    		Assert.assertNotNull(result.getContainerCodeName());
    	}
    }
    
    @Test
    @Transactional
    public void throwInTrash() throws Exception{
    	//TODO: add query with containerCodeName modifiedBy and modifiedDate
    	Collection<ContainerRequestDTO> containerCodeDTOs = new HashSet<ContainerRequestDTO>();
		ContainerRequestDTO containerCodeDTO = new ContainerRequestDTO();
		containerCodeDTO.setContainerCodeName(Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate").getResultList().get(0).getCodeName());
		containerCodeDTO.setModifiedBy("bfielder");
		containerCodeDTO.setModifiedDate(new Date());
		containerCodeDTOs.add(containerCodeDTO);
		String json = ContainerRequestDTO.toJsonArray(containerCodeDTOs);
		logger.info(json);
		Assert.assertFalse(json.equals("[{}]"));
    	ResultActions response = this.mockMvc.perform(post("/api/v1/containers/throwInTrash")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isNoContent());
    }
    
    @Test
    @Transactional
    public void throwInTrash_InternalError() throws Exception{
		String json = "[{\"containerCodeName\":\"total-garbage\",\"modifiedDate\":\"not-a-date\"}]";
		logger.info(json);
		Assert.assertFalse(json.equals("[{}]"));
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/throwInTrash")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isInternalServerError())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();;
		logger.info(response.getContentAsString());
		Assert.assertTrue(response.getContentAsString().length() > 0);
    }
    
    @Test
    @Transactional
    public void throwInTrash_PartialError() throws Exception{
		String json = "[{\"containerCodeName\":\"total-garbage\",\"modifiedDate\":1455057684000}]";
		logger.info(json);
		Assert.assertFalse(json.equals("[{}]"));
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/throwInTrash")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isBadRequest())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();;
		logger.info(response.getContentAsString());
		Assert.assertTrue(response.getContentAsString().length() > 0);
		Collection<ContainerErrorMessageDTO> errors = ContainerErrorMessageDTO.fromJsonArrayToContainerErroes(json);
		Assert.assertFalse(errors.isEmpty());
    }
    
    @Test
    @Transactional
    public void updateAmountInWell_success() throws Exception{
    	Collection<ContainerRequestDTO> containerCodeDTOs = new HashSet<ContainerRequestDTO>();
		ContainerRequestDTO containerCodeDTO = new ContainerRequestDTO();
		TypedQuery<Container> wellQuery = Container.findContainersByLsTypeEqualsAndLsKindEquals("well","default");
		wellQuery.setMaxResults(1);
		containerCodeDTO.setContainerCodeName(wellQuery.getSingleResult().getCodeName());
		containerCodeDTO.setAmount(new BigDecimal(10));
		containerCodeDTO.setAmountUnits("mg");
		containerCodeDTO.setModifiedBy("acas");
		containerCodeDTO.setModifiedDate(new Date());
		containerCodeDTOs.add(containerCodeDTO);
		String json = ContainerRequestDTO.toJsonArray(containerCodeDTOs);
		logger.info(json);
		Assert.assertFalse(json.equals("[{}]"));
    	ResultActions response = this.mockMvc.perform(post("/api/v1/containers/updateAmountInWell")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isNoContent());
    }
    
    @Test
    @Transactional
    public void getWellContentByPlateBarcode() throws Exception{
    	TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate");
    	query.setMaxResults(1);
    	Container container = query.getSingleResult();
    	String plateBarcode = container.getLsLabels().iterator().next().getLabelText();
		logger.info(plateBarcode);
    	MockHttpServletResponse response = this.mockMvc.perform(get("/api/v1/containers/getWellContentByPlateBarcode/"+plateBarcode)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<WellContentDTO> results = WellContentDTO.fromJsonArrayToWellCoes(responseJson);
    	for (WellContentDTO result : results){
    		Assert.assertNotNull(result.getContainerCodeName());
    		Assert.assertNotNull(result.getWellName());
    		Assert.assertNotNull(result.getRowIndex());
    		Assert.assertNotNull(result.getColumnIndex());
    		Assert.assertNotNull(result.getRecordedBy());
    		Assert.assertNotNull(result.getRecordedDate());
    	}
    }
    
    @Test
	@Transactional
	@Rollback(value=false)
	public void createEmptyPlate() throws Exception{
		TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("definition container", "plate");
		query.setMaxResults(1);
		try{
			Container definition = query.getSingleResult();
		}catch (EmptyResultDataAccessException e){
			Container definition = Container.fromJsonToContainer("{\"codeName\":\"special-definition-code\",\"deleted\":false,\"id\":null,\"ignored\":false,\"lsKind\":\"plate\",\"lsLabels\":[{\"deleted\":false,\"id\":null,\"ignored\":false,\"labelText\":\"1536\",\"lsKind\":\"common\",\"lsType\":\"name\",\"lsTypeAndKind\":\"name_common\",\"physicallyLabled\":true,\"preferred\":true,\"recordedBy\":\"acas\",\"recordedDate\":1456208484968,\"version\":null}],\"lsStates\":[{\"deleted\":false,\"id\":null,\"ignored\":false,\"lsKind\":\"format\",\"lsType\":\"constants\",\"lsTypeAndKind\":\"constants_format\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":null,\"ignored\":false,\"lsKind\":\"columns\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_columns\",\"numericValue\":48,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"acas\",\"recordedDate\":1456208484968,\"unitTypeAndKind\":\"null_null\",\"version\":null},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":null,\"ignored\":false,\"lsKind\":\"rows\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_rows\",\"numericValue\":32,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"acas\",\"recordedDate\":1456208484968,\"unitTypeAndKind\":\"null_null\",\"version\":null},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":null,\"ignored\":false,\"lsKind\":\"wells\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_wells\",\"numericValue\":1536,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"acas\",\"recordedDate\":1456208484968,\"unitTypeAndKind\":\"null_null\",\"version\":null},{\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"A001\",\"deleted\":false,\"id\":null,\"ignored\":false,\"lsKind\":\"subcontainer naming convention\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_subcontainer naming convention\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"acas\",\"recordedDate\":1456208484968,\"unitTypeAndKind\":\"null_null\",\"version\":null}],\"recordedBy\":\"acas\",\"recordedDate\":1456208484968,\"version\":null}],\"lsType\":\"definition container\",\"lsTypeAndKind\":\"definition container_plate\",\"recordedBy\":\"acas\",\"recordedDate\":1456208484968,\"version\":null}");
			containerService.saveLsContainer(definition);
		}
		Container definition = query.getSingleResult();
		CreatePlateRequestDTO plateRequest = new CreatePlateRequestDTO();
		plateRequest.setDefinition(definition.getCodeName());
		plateRequest.setBarcode("TESTBARCODE-123");
		plateRequest.setRecordedBy("acas");
		plateRequest.setDescription("test description");
		String json = plateRequest.toJson();
		logger.info(json);
		Assert.assertFalse(json.equals("{}"));
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/createPlate")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();;
		logger.info(response.getContentAsString());
		PlateStubDTO result = PlateStubDTO.fromJsonToPlateStubDTO(response.getContentAsString());
    	logger.info(result.toJson());
    	Assert.assertEquals(1536, result.getWells().size());
    	String[][] plateLayout = new String[32][48];
    	for (WellStubDTO well : result.getWells()){
    		logger.debug(well.getRowIndex().toString() + ", "+well.getColumnIndex().toString());
    		plateLayout[well.getRowIndex()-1][well.getColumnIndex()-1] = well.getWellName();
    	}
    	logger.info(Arrays.deepToString(plateLayout));
    	Container newPlate = Container.findContainerByCodeNameEquals(result.getCodeName());
    	Assert.assertFalse(newPlate.getLsStates().isEmpty());
    	Assert.assertEquals(1, newPlate.getLsStates().size());
    	for (ContainerState state : newPlate.getLsStates()){
    		Assert.assertFalse(state.getLsValues().isEmpty());
    		Assert.assertEquals("metadata", state.getLsType());
    		Assert.assertEquals("information", state.getLsKind());
    		Assert.assertEquals(3, state.getLsValues().size());
    	}
	}
	
	@Test
	@Transactional
	@Rollback(value=true)
	public void createPartiallyFilledPlate() throws Exception{
		TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("definition container", "plate");
		query.setMaxResults(1);
		Container definition = query.getSingleResult();
		CreatePlateRequestDTO plateRequest = new CreatePlateRequestDTO();
		plateRequest.setDefinition(definition.getCodeName());
		plateRequest.setBarcode("TESTBARCODE-123");
		plateRequest.setRecordedBy("acas");
		Collection<WellContentDTO> wellsToPopulate = new ArrayList<WellContentDTO>();
		WellContentDTO wellA1 = new WellContentDTO();
		wellA1.setWellName("A1");
		wellA1.setAmount(new BigDecimal(1));
		wellA1.setAmountUnits("µL");
		wellsToPopulate.add(wellA1);
		WellContentDTO wellB3 = new WellContentDTO();
		wellB3.setWellName("B03");
		wellB3.setAmount(new BigDecimal(2));
		wellB3.setAmountUnits("µL");
		wellsToPopulate.add(wellB3);
		WellContentDTO wellC7 = new WellContentDTO();
		wellC7.setWellName("AA007");
		wellC7.setAmount(new BigDecimal(3));
		wellC7.setAmountUnits("µL");
		wellsToPopulate.add(wellC7);
		plateRequest.setWells(wellsToPopulate);
		String json = plateRequest.toJson();
		logger.info(json);
		Assert.assertFalse(json.equals("[{}]"));
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/createPlate")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();;
		logger.info(response.getContentAsString());
		PlateStubDTO result = PlateStubDTO.fromJsonToPlateStubDTO(response.getContentAsString());
    	logger.info(result.toJson());
    	Assert.assertEquals(1536, result.getWells().size());
    	String[][] plateLayout = new String[32][48];
//    	for (WellStubDTO well : result.getWells()){
//    		plateLayout[well.getRowIndex()][well.getColumnIndex()] = well.getWellName();
//    		if (well.getWellName().equals("A001")){
//    			Collection<ContainerRequestDTO> checkWells = new ArrayList<ContainerRequestDTO>();
//    			ContainerRequestDTO checkWell = new ContainerRequestDTO(well.getCodeName(), null, null);
//    			checkWells.add(checkWell);
//    			WellContentDTO checkResult = containerService.getWellContent(checkWells).iterator().next();
//    			Assert.assertEquals(new BigDecimal(1), checkResult.getAmount());
//    			logger.info("checked well A001");
//    		}
//    		if (well.getWellName().equals("B003")){
//    			Collection<ContainerRequestDTO> checkWells = new ArrayList<ContainerRequestDTO>();
//    			ContainerRequestDTO checkWell = new ContainerRequestDTO(well.getCodeName(), null, null);
//    			checkWells.add(checkWell);
//    			WellContentDTO checkResult = containerService.getWellContent(checkWells).iterator().next();
//    			Assert.assertEquals(new BigDecimal(2), checkResult.getAmount());
//    			logger.info("checked well B003");
//
//    		}
//    		if (well.getWellName().equals("AA007")){
//    			Collection<ContainerRequestDTO> checkWells = new ArrayList<ContainerRequestDTO>();
//    			ContainerRequestDTO checkWell = new ContainerRequestDTO(well.getCodeName(), null, null);
//    			checkWells.add(checkWell);
//    			WellContentDTO checkResult = containerService.getWellContent(checkWells).iterator().next();
//    			Assert.assertEquals(new BigDecimal(3), checkResult.getAmount());
//    			logger.info("checked well AA007");
//
//    		}
//    	}
    	logger.info(Arrays.deepToString(plateLayout));
    	
    	
	}
	
	@Test
    @Transactional
    public void createPlateAndGetContent() throws Exception{
		TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("definition container", "plate");
		query.setMaxResults(1);
		Container definition = query.getSingleResult();
		CreatePlateRequestDTO plateRequest = new CreatePlateRequestDTO();
		plateRequest.setDefinition(definition.getCodeName());
		plateRequest.setBarcode("TESTBARCODE-123");
		plateRequest.setRecordedBy("acas");
		String json = plateRequest.toJson();
		logger.info(json);
		Assert.assertFalse(json.equals("{}"));
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/createPlate")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();;
		logger.info(response.getContentAsString());
		PlateStubDTO result = PlateStubDTO.fromJsonToPlateStubDTO(response.getContentAsString());
    	logger.info(result.toJson());
    	Assert.assertEquals(1536, result.getWells().size());
    	String[][] plateLayout = new String[32][48];
    	for (WellStubDTO well : result.getWells()){
    		logger.debug(well.getRowIndex().toString() + ", "+well.getColumnIndex().toString());
    		plateLayout[well.getRowIndex()][well.getColumnIndex()] = well.getWellName();
    	}
    	
    	String plateBarcode = result.getBarcode();
		logger.info(plateBarcode);
    	MockHttpServletResponse response2 = this.mockMvc.perform(get("/api/v1/containers/getWellContentByPlateBarcode/"+plateBarcode)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response2.getContentAsString();
    	logger.info(responseJson);
    	Collection<WellContentDTO> results = WellContentDTO.fromJsonArrayToWellCoes(responseJson);
    	for (WellContentDTO resultWell : results){
    		Assert.assertNotNull(resultWell.getContainerCodeName());
    		Assert.assertNotNull(resultWell.getWellName());
    		Assert.assertNotNull(resultWell.getRowIndex());
    		Assert.assertNotNull(resultWell.getColumnIndex());
    		Assert.assertNotNull(resultWell.getRecordedBy());
    		Assert.assertNotNull(resultWell.getRecordedDate());
    	}
    }
	
	@Test
    @Transactional
    public void getPlateTypeByPlateBarcode() throws Exception{
//    	TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate");
//    	query.setMaxResults(1);
    	Container container = Container.findContainerByCodeNameEquals("CONT-5");
    	String plateBarcode = container.getLsLabels().iterator().next().getLabelText();
		logger.info(plateBarcode);
    	MockHttpServletResponse response = this.mockMvc.perform(get("/api/v1/containers/getPlateTypeByPlateBarcode/"+plateBarcode)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	PlateStubDTO result = PlateStubDTO.fromJsonToPlateStubDTO(responseJson);
    	Assert.assertNotNull(result.getCodeName());
    	Assert.assertNotNull(result.getBarcode());
    	Assert.assertNotNull(result.getPlateType());
    }
	
	@Test
    @Transactional
    public void getPlateTypeByPlateBarcode_notFound() throws Exception{
    	String plateBarcode = "Not-A-Valid-Barcode123";
		logger.info(plateBarcode);
    	MockHttpServletResponse response = this.mockMvc.perform(get("/api/v1/containers/getPlateTypeByPlateBarcode/"+plateBarcode)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isNotFound())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    }
	
	@Test
    @Transactional
    public void createPlateAndRejectDupe() throws Exception{
		TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("definition container", "plate");
		query.setMaxResults(1);
		Container definition = query.getSingleResult();
		CreatePlateRequestDTO plateRequest = new CreatePlateRequestDTO();
		plateRequest.setDefinition(definition.getCodeName());
		plateRequest.setBarcode("TESTBARCODE-123");
		plateRequest.setRecordedBy("acas");
		String json = plateRequest.toJson();
		logger.info(json);
		Assert.assertFalse(json.equals("{}"));
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/createPlate")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
		logger.info(response.getContentAsString());
		PlateStubDTO result = PlateStubDTO.fromJsonToPlateStubDTO(response.getContentAsString());
    	logger.info(result.toJson());
    	Assert.assertEquals(1536, result.getWells().size());
    	
		String dupeJson = plateRequest.toJson();
		logger.info(dupeJson);
		MockHttpServletResponse dupeResponse = this.mockMvc.perform(post("/api/v1/containers/createPlate")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isBadRequest())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
		logger.info(dupeResponse.getContentAsString());
    }
	
	@Test
    @Transactional
    public void getContainersByCodeNames() throws Exception{
    	List<String> codeNames = new ArrayList<String>();
    	TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("well","default");
    	query.setMaxResults(25);
    	for (Container container : query.getResultList()){
    		codeNames.add("\""+container.getCodeName()+"\"");	
    	}
		String json = codeNames.toString();
		logger.info(json);
		Assert.assertFalse(json.equals("{}"));
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/getContainersByCodeNames")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<ContainerErrorMessageDTO> results = ContainerErrorMessageDTO.fromJsonArrayToContainerErroes(responseJson);
    	for (ContainerErrorMessageDTO result : results){
    		Assert.assertNotNull(result);
    		if (result.getLevel() == null){
    			Assert.assertNotNull(result.getContainer());
    			Assert.assertFalse(result.getContainer().getLsStates().isEmpty());
    		}
    		
    	}
    }
	
	@Test
    @Transactional
    public void getContainersByCodeNames_errors() throws Exception{
    	List<String> codeNames = new ArrayList<String>();
    	TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("well","default");
    	query.setMaxResults(25);
    	for (Container container : query.getResultList()){
    		codeNames.add("\""+container.getCodeName()+"\"");	
    	}
    	codeNames.add("\"INVALID-CODENAME\"");
		String json = codeNames.toString();
		logger.info(json);
		Assert.assertFalse(json.equals("{}"));
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/getContainersByCodeNames")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isBadRequest())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<ContainerErrorMessageDTO> results = ContainerErrorMessageDTO.fromJsonArrayToContainerErroes(responseJson);
    	for (ContainerErrorMessageDTO result : results){
    		Assert.assertNotNull(result);
    		if (result.getLevel() == null){
    			Assert.assertNotNull(result.getContainer());
    			Assert.assertFalse(result.getContainer().getLsStates().isEmpty());
    		}
    		else{
    			Assert.assertNull(result.getContainer());
    		}
    		
    	}
    }
	
	@Test
    @Transactional
    public void getContainersByCodeNames_thousands() throws Exception{
    	List<String> codeNames = new ArrayList<String>();
    	TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("well","default");
    	query.setMaxResults(1020);
    	for (Container container : query.getResultList()){
    		codeNames.add("\""+container.getCodeName()+"\"");	
    	}
		String json = codeNames.toString();
		logger.info(json);
		Assert.assertFalse(json.equals("{}"));
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/getContainersByCodeNames")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<ContainerErrorMessageDTO> results = ContainerErrorMessageDTO.fromJsonArrayToContainerErroes(responseJson);
    	for (ContainerErrorMessageDTO result : results){
    		Assert.assertNotNull(result);
    		if (result.getLevel() == null){
    			Assert.assertNotNull(result.getContainer());
    			Assert.assertFalse(result.getContainer().getLsStates().isEmpty());
    		}
    		
    	}
    }
	
	@Test
    @Transactional
    public void getDefinitionContainersByContainerCodeNames() throws Exception{
    	List<String> codeNames = new ArrayList<String>();
    	TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate");
    	query.setMaxResults(1);
    	for (Container container : query.getResultList()){
    		codeNames.add("\""+container.getCodeName()+"\"");	
    	}
		String json = codeNames.toString();
		logger.info(json);
		Assert.assertFalse(json.equals("{}"));
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/getDefinitionContainersByContainerCodeNames")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<ContainerErrorMessageDTO> results = ContainerErrorMessageDTO.fromJsonArrayToContainerErroes(responseJson);
    	for (ContainerErrorMessageDTO result : results){
    		Assert.assertNotNull(result);
    		if (result.getLevel() == null){
    			Assert.assertNotNull(result.getDefinition());
    			Assert.assertFalse(result.getDefinition().getLsStates().isEmpty());
    		}
    		Assert.assertNull(result.getContainer());
    		
    	}
    }
	
	@Test
    @Transactional
    public void containerDependencyCheck_Fail() throws Exception{
		Container plate = Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate").getResultList().get(0);
		Container well = Container.findContainersByLsTypeEqualsAndLsKindEquals("well","default").getResultList().get(0);
		Protocol testProtocol = new Protocol();
		testProtocol.setCodeName("TEST-PRCL");
		testProtocol.setRecordedBy("acas");
		testProtocol.setRecordedDate(new Date());
		testProtocol.setLsType("default");
		testProtocol.setLsKind("default");
		testProtocol.persist();
		Experiment testExpt = new Experiment();
		testExpt.setCodeName("TEST-EXPT");
		testExpt.setRecordedBy("acas");
		testExpt.setRecordedDate(new Date());
		testExpt.setLsType("default");
		testExpt.setLsKind("default");
		ExperimentLabel testExptName = new ExperimentLabel();
		testExptName.setLabelText("Test Experiment Name");
		testExptName.setRecordedBy("acas");
		testExptName.setRecordedDate(new Date());
		testExptName.setLsType("name");
		testExptName.setLsKind("experiment name");
		testExptName.setPreferred(true);
		testExptName.setExperiment(testExpt);
		testExpt.getLsLabels().add(testExptName);
		testExpt.setProtocol(testProtocol);
		testExpt.persist();
		AnalysisGroup testAG = new AnalysisGroup();
		testAG.setCodeName("TEST-AG");
		testAG.setRecordedBy("acas");
		testAG.setRecordedDate(new Date());
		testAG.setLsType("default");
		testAG.setLsKind("default");
		testAG.getExperiments().add(testExpt);
		testAG.persist();
		TreatmentGroup testTG = new TreatmentGroup();
		testTG.setCodeName("TEST-TG");
		testTG.setRecordedBy("acas");
		testTG.setRecordedDate(new Date());
		testTG.setLsType("default");
		testTG.setLsKind("default");
		testTG.getAnalysisGroups().add(testAG);
		testTG.persist();
		Subject testSubject = new Subject();
		testSubject.setCodeName("TEST-SUBJ");
		testSubject.setRecordedBy("acas");
		testSubject.setRecordedDate(new Date());
		testSubject.setLsType("default");
		testSubject.setLsKind("default");
		testSubject.getTreatmentGroups().add(testTG);
		testSubject.persist();
		testExpt.getAnalysisGroups().add(testAG);
		testAG.getTreatmentGroups().add(testTG);
		testTG.getSubjects().add(testSubject);
		testExpt.merge();
		testAG.merge();
		testTG.merge();
		
		ItxSubjectContainer itx = new ItxSubjectContainer();
		itx.setCodeName("TEST-ITX");
		itx.setRecordedBy("acas");
		itx.setRecordedDate(new Date());
		itx.setLsType("default");
		itx.setLsKind("default");
		itx.setSubject(testSubject);
		itx.setContainer(well);
		itx.persist();
		well.getSubjects().add(itx);
		well.merge();
		Long before = (new Date()).getTime();
    	ContainerDependencyCheckDTO result = containerService.checkDependencies(plate);
		Long after = (new Date()).getTime();
		logger.info("ms elapsed: "+ String.valueOf(after-before));
		logger.info(result.toJson());
		Assert.assertTrue(result.isLinkedDataExists());
		Assert.assertFalse(result.getLinkedExperiments().isEmpty());
    	String codeName = plate.getCodeName();
    	MockHttpServletResponse response = this.mockMvc.perform(get("/api/v1/containers/checkDependencies/"+codeName)
    			.accept(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk())
//    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	DependencyCheckDTO results = DependencyCheckDTO.fromJsonToDependencyCheckDTO(responseJson);
    	Assert.assertTrue(results.getLinkedDataExists());
    	
    }

	@Test
    @Transactional
    public void logicalDeleteContainer() throws Exception{
    	TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate");
    	query.setMaxResults(1);
    	Container container = query.getSingleResult();
    	MockHttpServletResponse response = this.mockMvc.perform(delete("/api/v1/containers/"+container.getId()))
    			.andExpect(status().isOk())
    			.andReturn().getResponse();
    	
    	MockHttpServletResponse response2 = this.mockMvc.perform(delete("/api/v1/containers/"+container.getId()))
    			.andExpect(status().isNotFound())
    			.andReturn().getResponse();
    }
	
	@Test
    @Transactional
    public void getWellCodesByContainerCodes() throws Exception{
    	List<String> codeNames = new ArrayList<String>();
    	TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate");
    	query.setMaxResults(25);
    	for (Container container : query.getResultList()){
    		codeNames.add("\""+container.getCodeName()+"\"");	
    	}
    	codeNames.add("\"INVALID-CODENAME\"");
		String json = codeNames.toString();
		logger.info(json);
		Assert.assertFalse(json.equals("{}"));
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/getWellCodesByContainerCodes")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<ContainerWellCodeDTO> results = ContainerWellCodeDTO.fromJsonArrayToContainerWellCoes(responseJson);
    	for (ContainerWellCodeDTO result : results){
    		Assert.assertNotNull(result);
    		Assert.assertNotNull(result.getRequestCodeName());
    		if (result.getRequestCodeName().equals("INVALID-CODENAME")) Assert.assertTrue(result.getWellCodeNames().isEmpty());
    		else Assert.assertFalse(result.getWellCodeNames().isEmpty());
    	}
    }
	
	@Test
    @Transactional
    public void getContainerCodesByLabels_thousands() throws Exception{
    	List<String> plateBarcodes = new ArrayList<String>();
//    	TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate");
    	for (ContainerLabel containerLabel : ContainerLabel.findContainerLabelEntries(0, 4000)){
    		plateBarcodes.add("\""+containerLabel.getLabelText()+"\"");	
    	}
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
    	int i=0;
    	for (CodeLabelDTO result : results){
    		Assert.assertNotNull(result.getRequestLabel());
    		Assert.assertFalse(result.getFoundCodeNames().isEmpty());
    		Assert.assertEquals(plateBarcodes.get(i).replaceAll("\"", ""), result.getRequestLabel());
    		i++;
    	}
    }
	
	@Test
    @Transactional
    public void getWellContent_thousands() throws Exception{
    	List<String> wellCodes = new ArrayList<String>();
    	TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("well","default");
    	query.setMaxResults(10000);
    	for (Container container : query.getResultList()){
    		wellCodes.add("\""+container.getCodeName()+"\"");	
    	}
		String json = wellCodes.toString();
		logger.info(json);
		Long before = (new Date()).getTime();
		Assert.assertFalse(json.equals("{}"));
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/getWellContent")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
		Long after = (new Date()).getTime();
		logger.info("ms elapsed: "+ String.valueOf(after-before));
    	logger.info(responseJson);
    	Collection<WellContentDTO> results = WellContentDTO.fromJsonArrayToWellCoes(responseJson);
    	int i = 0;
    	for (WellContentDTO result : results){
    		Assert.assertNotNull(result.getContainerCodeName());
    		Assert.assertEquals(wellCodes.get(i).replaceAll("\"",""), result.getContainerCodeName());
    		if (result.getLevel() != null) Assert.assertNotNull(result.getWellName());
    		i++;
    	}
    }
	
	@Test
    @Transactional
    public void getDefinitionContainersByContainerCodeNames_thousands() throws Exception{
    	List<String> codeNames = new ArrayList<String>();
    	TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate");
    	query.setMaxResults(1100);
    	for (Container container : query.getResultList()){
    		codeNames.add("\""+container.getCodeName()+"\"");	
    	}
		String json = codeNames.toString();
		logger.info(json);
		Assert.assertFalse(json.equals("{}"));
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/getDefinitionContainersByContainerCodeNames")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<ContainerErrorMessageDTO> results = ContainerErrorMessageDTO.fromJsonArrayToContainerErroes(responseJson);
    	for (ContainerErrorMessageDTO result : results){
    		Assert.assertNotNull(result);
    		if (result.getLevel() == null){
    			Assert.assertNotNull(result.getDefinition());
    			Assert.assertFalse(result.getDefinition().getLsStates().isEmpty());
    		}
    		Assert.assertNull(result.getContainer());
    		
    	}
    }
	
	@Test
    @Transactional
    public void getWellCodesByContainerCodes_thousands() throws Exception{
    	List<String> codeNames = new ArrayList<String>();
    	TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate");
    	query.setMaxResults(1100);
    	for (Container container : query.getResultList()){
    		codeNames.add("\""+container.getCodeName()+"\"");	
    	}
    	codeNames.add("\"INVALID-CODENAME\"");
		String json = codeNames.toString();
		logger.info(json);
		Assert.assertFalse(json.equals("{}"));
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/getWellCodesByContainerCodes")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<ContainerWellCodeDTO> results = ContainerWellCodeDTO.fromJsonArrayToContainerWellCoes(responseJson);
    	for (ContainerWellCodeDTO result : results){
    		Assert.assertNotNull(result);
    		Assert.assertNotNull(result.getRequestCodeName());
    		if (result.getRequestCodeName().equals("INVALID-CODENAME")) Assert.assertTrue(result.getWellCodeNames().isEmpty());
    		else Assert.assertFalse(result.getWellCodeNames().isEmpty());
    	}
    }
	
	@Test
    @Transactional
    @Rollback(value=true)
    public void changeLocation_success() throws Exception{
    	Collection<ContainerLocationDTO> requests = new HashSet<ContainerLocationDTO>();
		TypedQuery<Container> containerQuery = Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate");
		containerQuery.setMaxResults(5);
		List<Container> containers = containerQuery.getResultList();
		Assert.assertEquals(5, containers.size());
		TypedQuery<Container> locationQuery = Container.findContainersByLsTypeEqualsAndLsKindEquals("location","default");
		locationQuery.setMaxResults(5);
		List<Container> locations = locationQuery.getResultList();
		Assert.assertEquals(5, locations.size());
		int i = 0;
		while (i<5){
			ContainerLocationDTO request = new ContainerLocationDTO();
			request.setContainerCodeName(containers.get(i).getCodeName());
			request.setLocationCodeName(locations.get(i).getCodeName());
			request.setModifiedBy("bob");
			request.setModifiedDate(new Date());
			requests.add(request);
			i++;
		}
		String json = ContainerLocationDTO.toJsonArray(requests);
		logger.info(json);
		Assert.assertFalse(json.equals("[{}]"));
    	ResultActions response = this.mockMvc.perform(post("/api/v1/containers/moveToLocation")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isNoContent());
    	List<String> containerCodeNames = new ArrayList<String>();
    	List<String> locationCodeNames = new ArrayList<String>();
    	for (ContainerLocationDTO request : requests){
    		locationCodeNames.add("\""+request.getLocationCodeName()+"\"");
    		containerCodeNames.add("\""+request.getContainerCodeName()+"\"");
    	}
    	MockHttpServletResponse checkResponse = this.mockMvc.perform(post("/api/v1/containers/getContainersInLocation")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(locationCodeNames.toString()))
    			.andExpect(status().isOk())
    			.andReturn().getResponse();
		String responseJson = checkResponse.getContentAsString();
		logger.info(responseJson);
    	Collection<ContainerLocationDTO> checkResults = ContainerLocationDTO.fromJsonArrayToContainerLocatioes(responseJson);
    	for (ContainerLocationDTO checkResult : checkResults){
    		if (containerCodeNames.contains("\""+checkResult.getContainerCodeName()+"\"")){
    			Assert.assertEquals(locationCodeNames.get(containerCodeNames.indexOf("\""+checkResult.getContainerCodeName()+"\"")).replaceAll("\"", ""), checkResult.getLocationCodeName());
    			logger.info("container: "+checkResult.getContainerCodeName()+" location: "+checkResult.getLocationCodeName());
    		}
    	}
    }
	
	@Test
    @Transactional
    public void changeLocation_errors() throws Exception{
    	Collection<ContainerLocationDTO> requests = new ArrayList<ContainerLocationDTO>();
		TypedQuery<Container> containerQuery = Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate");
		containerQuery.setMaxResults(6);
		List<Container> containers = containerQuery.getResultList();
		Assert.assertEquals(6, containers.size());
		TypedQuery<Container> locationQuery = Container.findContainersByLsTypeEqualsAndLsKindEquals("location","default");
		locationQuery.setMaxResults(6);
		List<Container> locations = locationQuery.getResultList();
		Assert.assertEquals(6, locations.size());
		int i = 0;
		while (i<6){
			ContainerLocationDTO request = new ContainerLocationDTO();
			request.setContainerCodeName(containers.get(i).getCodeName());
			request.setLocationCodeName(locations.get(i).getCodeName());
			request.setModifiedBy("bob");
			request.setModifiedDate(new Date());
			//now we add in errors
			if (i==0) request.setContainerCodeName("BAD-CODENAME");
			if (i==1) request.setLocationCodeName("BAD-CODENAME");
			if (i==2) request.setLocationCodeName(containers.get(i).getCodeName());
			if (i==3) request.setModifiedBy(null);
			if (i==4) request.setModifiedDate(null);
			requests.add(request);
			i++;
		}
		String json = ContainerLocationDTO.toJsonArray(requests);
		logger.info(json);
		Assert.assertFalse(json.equals("[{}]"));
		MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/moveToLocation")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isBadRequest())
    			.andReturn().getResponse();
		String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<ContainerLocationDTO> results = ContainerLocationDTO.fromJsonArrayToContainerLocatioes(responseJson);
    	Assert.assertEquals(6, results.size());
    	i=0;
    	for (ContainerLocationDTO result : results){
    		if (i!=5) Assert.assertEquals("error", result.getLevel());
    		logger.error(result.getMessage());
    		if (i==5) Assert.assertNull(result.getLevel());
    		i++;
    	}
    }
	
	@Test
    @Transactional
    public void updateWellContent_speedTest() throws Exception{
    	List<String> wellCodes = new ArrayList<String>();
    	TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("well","default");
    	query.setMaxResults(1200);
    	for (Container container : query.getResultList()){
    		wellCodes.add("\""+container.getCodeName()+"\"");	
    	}
		String json = wellCodes.toString();
		logger.info(json);
		Long before = (new Date()).getTime();
		Assert.assertFalse(json.equals("{}"));
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/getWellContent")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
		Long after = (new Date()).getTime();
		logger.info("ms elapsed: "+ String.valueOf(after-before));
    	logger.info(responseJson);
    	Collection<WellContentDTO> results = WellContentDTO.fromJsonArrayToWellCoes(responseJson);
    	for (WellContentDTO result : results){
    		result.setRecordedBy("bob");
    		result.setRecordedDate(new Date());
    		result.setAmount(new BigDecimal(10));
    	}
    	json = WellContentDTO.toJsonArray(results);
    	MockHttpServletResponse response2 = this.mockMvc.perform(post("/api/v1/containers/updateWellContent")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    }
	
	@Test
    @Transactional
    public void searchContainers() throws Exception{
    	MockHttpServletResponse response;
    	String responseJson;
    	Collection<Container> foundContainers;
    	TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("definition container", "plate");
		query.setMaxResults(1);
		Container definition = query.getSingleResult();
    	ContainerSearchRequestDTO searchRequest = new ContainerSearchRequestDTO();
    	searchRequest.setBarcode("TESTBARCODE");
    	searchRequest.setDescription("test");
    	searchRequest.setDefinition(definition.getCodeName());
    	String json = searchRequest.toJson();
    	logger.info(json);
    	response = this.mockMvc.perform(post("/api/v1/containers/searchContainers")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isOk())
    			.andExpect(content().contentType("application/json;charset=utf-8"))
    			.andReturn().getResponse();
    	responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	foundContainers = Container.fromJsonArrayToContainers(responseJson);
    	Assert.assertFalse(foundContainers.isEmpty());
    	for (Container result : foundContainers){
    		Assert.assertNotNull(result);
    	}
    }
	
	    @Test
	    @Transactional
	    public void getContainerCodeTablesByTypeAndKind() throws Exception{
	    	MockHttpServletResponse response;
	    	String responseJson;
	    	Collection<CodeTableDTO> foundContainers;
//	    	//no type and kind
//	    	response = this.mockMvc.perform(get("/api/v1/containers/codeTable")
//	    			.accept(MediaType.APPLICATION_JSON))
//	    			.andExpect(status().isOk())
//	    			.andExpect(content().contentType("application/json;charset=utf-8"))
//	    			.andReturn().getResponse();
//	    	responseJson = response.getContentAsString();
//	    	logger.info(responseJson);
//	    	foundContainers = CodeTableDTO.fromJsonArrayToCoes(responseJson);
//	    	Assert.assertFalse(foundContainers.isEmpty());
	    	//only type
	    	response = this.mockMvc.perform(get("/api/v1/containers/?format=codeTable&lsType=definition container")
	    			.accept(MediaType.APPLICATION_JSON))
	    			.andExpect(status().isOk())
	    			.andExpect(content().contentType("application/json;charset=utf-8"))
	    			.andReturn().getResponse();
	    	responseJson = response.getContentAsString();
	    	logger.info(responseJson);
	    	foundContainers = CodeTableDTO.fromJsonArrayToCoes(responseJson);
	    	Assert.assertFalse(foundContainers.isEmpty());
	    	//only kind
	    	response = this.mockMvc.perform(get("/api/v1/containers/?format=codeTable&lsKind=plate")
	    			.accept(MediaType.APPLICATION_JSON))
	    			.andExpect(status().isOk())
	    			.andExpect(content().contentType("application/json;charset=utf-8"))
	    			.andReturn().getResponse();
	    	responseJson = response.getContentAsString();
	    	logger.info(responseJson);
	    	foundContainers = CodeTableDTO.fromJsonArrayToCoes(responseJson);
	    	Assert.assertFalse(foundContainers.isEmpty());
	    	//type and kind
	    	response = this.mockMvc.perform(get("/api/v1/containers/?format=codeTable&lsType=definition container&lsKind=plate")
	    			.accept(MediaType.APPLICATION_JSON))
	    			.andExpect(status().isOk())
	    			.andExpect(content().contentType("application/json;charset=utf-8"))
	    			.andReturn().getResponse();
	    	responseJson = response.getContentAsString();
	    	logger.info(responseJson);
	    	foundContainers = CodeTableDTO.fromJsonArrayToCoes(responseJson);
	    	Assert.assertFalse(foundContainers.isEmpty());
	    	//find no results
	    	response = this.mockMvc.perform(get("/api/v1/containers/?format=codeTable&lsType=nope&lsKind=")
	    			.accept(MediaType.APPLICATION_JSON))
	    			.andExpect(status().isOk())
	    			.andExpect(content().contentType("application/json;charset=utf-8"))
	    			.andReturn().getResponse();
	    	responseJson = response.getContentAsString();
	    	logger.info(responseJson);
	    	foundContainers = CodeTableDTO.fromJsonArrayToCoes(responseJson);
	    	Assert.assertTrue(foundContainers.isEmpty());
	    }
	    
	    @Test
	    @Transactional
	    @Rollback(value=false)
	    public void saveAndSearchContainers() throws Exception{
	    	TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("definition container", "plate");
			query.setMaxResults(1);
			try{
				Container definition = query.getSingleResult();
			}catch (EmptyResultDataAccessException e){
				Container definition = Container.fromJsonToContainer("{\"codeName\":\"special-definition-code\",\"deleted\":false,\"id\":null,\"ignored\":false,\"lsKind\":\"plate\",\"lsLabels\":[{\"deleted\":false,\"id\":null,\"ignored\":false,\"labelText\":\"1536\",\"lsKind\":\"common\",\"lsType\":\"name\",\"lsTypeAndKind\":\"name_common\",\"physicallyLabled\":true,\"preferred\":true,\"recordedBy\":\"acas\",\"recordedDate\":1456208484968,\"version\":null}],\"lsStates\":[{\"deleted\":false,\"id\":null,\"ignored\":false,\"lsKind\":\"format\",\"lsType\":\"constants\",\"lsTypeAndKind\":\"constants_format\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":null,\"ignored\":false,\"lsKind\":\"columns\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_columns\",\"numericValue\":48,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"acas\",\"recordedDate\":1456208484968,\"unitTypeAndKind\":\"null_null\",\"version\":null},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":null,\"ignored\":false,\"lsKind\":\"rows\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_rows\",\"numericValue\":32,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"acas\",\"recordedDate\":1456208484968,\"unitTypeAndKind\":\"null_null\",\"version\":null},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":null,\"ignored\":false,\"lsKind\":\"wells\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_wells\",\"numericValue\":1536,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"acas\",\"recordedDate\":1456208484968,\"unitTypeAndKind\":\"null_null\",\"version\":null},{\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"A001\",\"deleted\":false,\"id\":null,\"ignored\":false,\"lsKind\":\"subcontainer naming convention\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_subcontainer naming convention\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"acas\",\"recordedDate\":1456208484968,\"unitTypeAndKind\":\"null_null\",\"version\":null}],\"recordedBy\":\"acas\",\"recordedDate\":1456208484968,\"version\":null}],\"lsType\":\"definition container\",\"lsTypeAndKind\":\"definition container_plate\",\"recordedBy\":\"acas\",\"recordedDate\":1456208484968,\"version\":null}");
				containerService.saveLsContainer(definition);
			}
			Container definition = query.getSingleResult();
			String barcode = "TESTBARCODE-130";
			String createdUser = "bob";
			String recordedBy = "acas";
			CreatePlateRequestDTO plateRequest = new CreatePlateRequestDTO();
			plateRequest.setDefinition(definition.getCodeName());
			plateRequest.setBarcode(barcode);
			plateRequest.setRecordedBy(recordedBy);
			plateRequest.setDescription("test description");
//			plateRequest.setCreatedDate(new Date(713232000000L));
//			plateRequest.setCreatedUser(createdUser);
			String json = plateRequest.toJson();
			logger.info(json);
			Assert.assertFalse(json.equals("{}"));
	    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/createPlate")
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.accept(MediaType.APPLICATION_JSON)
	    			.content(json))
	    			.andExpect(status().isOk())
	    			.andExpect(content().contentType("application/json;charset=utf-8"))
	    			.andReturn().getResponse();;
			logger.info(response.getContentAsString());
			PlateStubDTO result = PlateStubDTO.fromJsonToPlateStubDTO(response.getContentAsString());
	    	logger.info(result.toJson());
	    	Assert.assertEquals(1536, result.getWells().size());
	    	String[][] plateLayout = new String[32][48];
	    	for (WellStubDTO well : result.getWells()){
	    		logger.debug(well.getRowIndex().toString() + ", "+well.getColumnIndex().toString());
	    		plateLayout[well.getRowIndex()-1][well.getColumnIndex()-1] = well.getWellName();
	    	}
	    	logger.info(Arrays.deepToString(plateLayout));
	    	Container newPlate = Container.findContainerByCodeNameEquals(result.getCodeName());
	    	Assert.assertFalse(newPlate.getLsStates().isEmpty());
	    	Assert.assertEquals(1, newPlate.getLsStates().size());
	    	for (ContainerState state : newPlate.getLsStates()){
	    		Assert.assertFalse(state.getLsValues().isEmpty());
	    		Assert.assertEquals("metadata", state.getLsType());
	    		Assert.assertEquals("information", state.getLsKind());
	    		Assert.assertEquals(3, state.getLsValues().size());
	    	}
	    	
	    	ContainerSearchRequestDTO searchRequest = new ContainerSearchRequestDTO();
	    	searchRequest.setBarcode(barcode);
//	    	searchRequest.setDescription("test");
//	    	searchRequest.setDefinition(definition.getCodeName());
//	    	searchRequest.setCreatedUser(createdUser);
	    	json = searchRequest.toJson();
	    	logger.info(json);
	    	response = this.mockMvc.perform(post("/api/v1/containers/searchContainers")
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.accept(MediaType.APPLICATION_JSON)
	    			.content(json))
	    			.andExpect(status().isOk())
	    			.andExpect(content().contentType("application/json;charset=utf-8"))
	    			.andReturn().getResponse();
	    	String responseJson = response.getContentAsString();
	    	logger.info(responseJson);
	    	Collection<Container> foundContainers = Container.fromJsonArrayToContainers(responseJson);
	    	Assert.assertFalse(foundContainers.isEmpty());
	    	for (Container foundContainer : foundContainers){
	    		Assert.assertNotNull(foundContainer);
	    		for (ContainerState containerState : foundContainer.getLsStates()){
	    			for (ContainerValue containerValue : containerState.getLsValues()){
	    				if (containerValue.getLsKind().equals("created date")) logger.info(containerValue.getDateValue().toGMTString());
	    			}
	    		}
	    	}
	    }
	    
	    @Test
	    @Transactional
	    public void updateWellContent_doNotCopyPreviousValuesTest() throws Exception{
	    	List<String> wellCodes = new ArrayList<String>();
	    	TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("well","default");
	    	query.setMaxResults(1200);
	    	for (Container container : query.getResultList()){
	    		wellCodes.add("\""+container.getCodeName()+"\"");	
	    	}
			String json = wellCodes.toString();
			logger.info(json);
			Long before = (new Date()).getTime();
			Assert.assertFalse(json.equals("{}"));
	    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/getWellContent")
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.accept(MediaType.APPLICATION_JSON)
	    			.content(json))
	    			.andExpect(status().isOk())
	    			.andExpect(content().contentType("application/json;charset=utf-8"))
	    			.andReturn().getResponse();
	    	String responseJson = response.getContentAsString();
			Long after = (new Date()).getTime();
			logger.info("ms elapsed: "+ String.valueOf(after-before));
	    	logger.info(responseJson);
	    	Collection<WellContentDTO> results = WellContentDTO.fromJsonArrayToWellCoes(responseJson);
	    	for (WellContentDTO result : results){
	    		result.setRecordedBy("bob");
	    		result.setRecordedDate(new Date());
	    		result.setAmount(new BigDecimal(10));
	    		result.setAmountUnits("mg");
	    	}
	    	json = WellContentDTO.toJsonArray(results);
	    	MockHttpServletResponse response1 = this.mockMvc.perform(post("/api/v1/containers/updateWellContent")
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.accept(MediaType.APPLICATION_JSON)
	    			.content(json))
	    			.andExpect(status().isNoContent())
	    			.andReturn().getResponse();
	    	response = this.mockMvc.perform(post("/api/v1/containers/getWellContent")
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.accept(MediaType.APPLICATION_JSON)
	    			.content(wellCodes.toString()))
	    			.andExpect(status().isOk())
	    			.andExpect(content().contentType("application/json;charset=utf-8"))
	    			.andReturn().getResponse();
	    	String checkJson1 = response.getContentAsString();
	    	logger.info(checkJson1);
	    	Collection<WellContentDTO> checkResults1 = WellContentDTO.fromJsonArrayToWellCoes(checkJson1);
	    	for (WellContentDTO checkResult : checkResults1){
	    		Assert.assertNotNull(checkResult.getAmount());
	    		Assert.assertNotNull(checkResult.getAmountUnits());
	    	}
	    	Collection<WellContentDTO> emptyWells = new ArrayList<WellContentDTO>();
	    	emptyWells.addAll(results);
	    	for (WellContentDTO emptyWell : emptyWells){
	    		emptyWell.setRecordedBy("bob");
	    		emptyWell.setRecordedDate(new Date());
	    		emptyWell.setAmount(null);
	    		emptyWell.setAmountUnits(null);
	    	}
	    	String emptyWellsJson = WellContentDTO.toJsonArray(emptyWells);
	    	MockHttpServletResponse response2 = this.mockMvc.perform(post("/api/v1/containers/updateWellContent?copyPreviousValues=false")
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.accept(MediaType.APPLICATION_JSON)
	    			.content(emptyWellsJson))
	    			.andExpect(status().isNoContent())
	    			.andReturn().getResponse();
	    	response = this.mockMvc.perform(post("/api/v1/containers/getWellContent")
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.accept(MediaType.APPLICATION_JSON)
	    			.content(wellCodes.toString()))
	    			.andExpect(status().isOk())
	    			.andExpect(content().contentType("application/json;charset=utf-8"))
	    			.andReturn().getResponse();
	    	String checkJson = response.getContentAsString();
	    	logger.info(checkJson);
	    	Collection<WellContentDTO> checkResults = WellContentDTO.fromJsonArrayToWellCoes(checkJson);
	    	for (WellContentDTO checkResult : checkResults){
	    		Assert.assertNull(checkResult.getAmount());
	    		Assert.assertNull(checkResult.getAmountUnits());
	    	}
	    }
	    
	    @Test
	    @Transactional
	    public void createPlateWithDefaultsAndGetContent() throws Exception{
			TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("definition container", "plate");
			query.setMaxResults(1);
			Container definition = query.getSingleResult();
			CreatePlateRequestDTO plateRequest = new CreatePlateRequestDTO();
			plateRequest.setDefinition(definition.getCodeName());
			plateRequest.setBarcode("TESTBARCODE-135");
			plateRequest.setRecordedBy("acas");
			plateRequest.setBatchConcentrationUnits("mM");
			plateRequest.setPhysicalState("liquid");
			String json = plateRequest.toJson();
			logger.info(json);
			Assert.assertFalse(json.equals("{}"));
	    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/containers/createPlate")
	    			.contentType(MediaType.APPLICATION_JSON)
	    			.accept(MediaType.APPLICATION_JSON)
	    			.content(json))
	    			.andExpect(status().isOk())
	    			.andExpect(content().contentType("application/json;charset=utf-8"))
	    			.andReturn().getResponse();;
			logger.info(response.getContentAsString());
			PlateStubDTO result = PlateStubDTO.fromJsonToPlateStubDTO(response.getContentAsString());
	    	logger.info(result.toJson());
	    	Assert.assertEquals(1536, result.getWells().size());
	    	String plateBarcode = result.getBarcode();
			logger.info(plateBarcode);
	    	MockHttpServletResponse response2 = this.mockMvc.perform(get("/api/v1/containers/getWellContentByPlateBarcode/"+plateBarcode)
	    			.accept(MediaType.APPLICATION_JSON))
	    			.andExpect(status().isOk())
	    			.andExpect(content().contentType("application/json;charset=utf-8"))
	    			.andReturn().getResponse();
	    	String responseJson = response2.getContentAsString();
	    	logger.info(responseJson);
	    	Collection<WellContentDTO> results = WellContentDTO.fromJsonArrayToWellCoes(responseJson);
	    	for (WellContentDTO resultWell : results){
	    		Assert.assertNotNull(resultWell.getContainerCodeName());
	    		Assert.assertNotNull(resultWell.getWellName());
	    		Assert.assertNotNull(resultWell.getRowIndex());
	    		Assert.assertNotNull(resultWell.getColumnIndex());
	    		Assert.assertNotNull(resultWell.getRecordedBy());
	    		Assert.assertNotNull(resultWell.getRecordedDate());
	    		Assert.assertEquals("liquid", resultWell.getPhysicalState());
	    		Assert.assertEquals("mM", resultWell.getBatchConcUnits());
	    	}
	    }
    

}