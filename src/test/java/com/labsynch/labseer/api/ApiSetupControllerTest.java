package com.labsynch.labseer.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collection;

import junit.framework.Assert;

import org.hibernate.SessionFactory;
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

import com.labsynch.labseer.domain.ContainerKind;
import com.labsynch.labseer.domain.ContainerType;
import com.labsynch.labseer.domain.DDictKind;
import com.labsynch.labseer.domain.DDictType;
import com.labsynch.labseer.domain.DDictValue;
import com.labsynch.labseer.domain.ExperimentKind;
import com.labsynch.labseer.domain.ExperimentType;
import com.labsynch.labseer.domain.InteractionKind;
import com.labsynch.labseer.domain.InteractionType;
import com.labsynch.labseer.domain.LabelKind;
import com.labsynch.labseer.domain.LabelSequence;
import com.labsynch.labseer.domain.LabelType;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.OperatorKind;
import com.labsynch.labseer.domain.OperatorType;
import com.labsynch.labseer.domain.ProtocolKind;
import com.labsynch.labseer.domain.ProtocolType;
import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.domain.StateKind;
import com.labsynch.labseer.domain.StateType;
import com.labsynch.labseer.domain.ThingKind;
import com.labsynch.labseer.domain.ThingType;
import com.labsynch.labseer.domain.UnitKind;
import com.labsynch.labseer.domain.UnitType;
import com.labsynch.labseer.domain.ValueKind;
import com.labsynch.labseer.domain.ValueType;
import com.labsynch.labseer.dto.CodeTableDTO;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config-test.xml"})
public class ApiSetupControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiSetupControllerTest.class);
	
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }
    
    @Test
    @Transactional
    public void saveProtocolTypeAndKind() throws Exception{
    	String typeJson = "[{\"typeName\":\"test\"},{\"typeName\":\"default\"}]";
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/setup/protocoltypes")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(typeJson))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<ProtocolType> resultTypes = ProtocolType.fromJsonArrayToProtocolTypes(responseJson);
    	Assert.assertEquals(2, resultTypes.size());
    	
    	String kindJson = "[{\"typeName\":\"test\",\"kindName\":\"test\"},{\"typeName\":\"default\",\"kindName\":\"default\"}]";
		response = this.mockMvc.perform(post("/api/v1/setup/protocolkinds")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(kindJson))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<ProtocolKind> resultKinds = ProtocolKind.fromJsonArrayToProtocolKinds(responseJson);
    	Assert.assertEquals(2, resultKinds.size());
    }
    
    @Test
    @Transactional
    public void saveExperimentTypeAndKind() throws Exception{
    	String typeJson = "[{\"typeName\":\"test\"},{\"typeName\":\"default\"}]";
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/setup/experimenttypes")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(typeJson))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<ExperimentType> resultTypes = ExperimentType.fromJsonArrayToExperimentTypes(responseJson);
    	Assert.assertEquals(2, resultTypes.size());
    	
    	String kindJson = "[{\"typeName\":\"test\",\"kindName\":\"test\"},{\"typeName\":\"default\",\"kindName\":\"default\"}]";
		response = this.mockMvc.perform(post("/api/v1/setup/experimentkinds")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(kindJson))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<ExperimentKind> resultKinds = ExperimentKind.fromJsonArrayToExperimentKinds(responseJson);
    	Assert.assertEquals(2, resultKinds.size());
    }
    
    @Test
    @Transactional
    public void saveInteractionTypeAndKind() throws Exception{
    	String typeJson = "[{\"typeName\":\"test\",\"typeVerb\":\"tests\"},{\"typeName\":\"default\",\"typeVerb\":\"refers to\"}]";
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/setup/interactiontypes")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(typeJson))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<InteractionType> resultTypes = InteractionType.fromJsonArrayToInteractionTypes(responseJson);
    	Assert.assertEquals(2, resultTypes.size());
    	
    	String kindJson = "[{\"typeName\":\"test\",\"kindName\":\"test\"},{\"typeName\":\"default\",\"kindName\":\"default\"}]";
		response = this.mockMvc.perform(post("/api/v1/setup/interactionkinds")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(kindJson))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<InteractionKind> resultKinds = InteractionKind.fromJsonArrayToInteractionKinds(responseJson);
    	Assert.assertEquals(2, resultKinds.size());
    }
    
    @Test
    @Transactional
    public void saveContainerTypeAndKind() throws Exception{
    	String typeJson = "[{\"typeName\":\"test\"},{\"typeName\":\"default\"}]";
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/setup/containertypes")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(typeJson))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<ContainerType> resultTypes = ContainerType.fromJsonArrayToContainerTypes(responseJson);
    	Assert.assertEquals(2, resultTypes.size());
    	
    	String kindJson = "[{\"typeName\":\"test\",\"kindName\":\"test\"},{\"typeName\":\"default\",\"kindName\":\"default\"}]";
		response = this.mockMvc.perform(post("/api/v1/setup/containerkinds")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(kindJson))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<ContainerKind> resultKinds = ContainerKind.fromJsonArrayToContainerKinds(responseJson);
    	Assert.assertEquals(2, resultKinds.size());
    }
    
    @Test
    @Transactional
    public void saveStateTypeAndKind() throws Exception{
    	String typeJson = "[{\"typeName\":\"test\"},{\"typeName\":\"default\"}]";
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/setup/statetypes")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(typeJson))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<StateType> resultTypes = StateType.fromJsonArrayToStateTypes(responseJson);
    	Assert.assertEquals(2, resultTypes.size());
    	
    	String kindJson = "[{\"typeName\":\"test\",\"kindName\":\"test\"},{\"typeName\":\"default\",\"kindName\":\"default\"}]";
		response = this.mockMvc.perform(post("/api/v1/setup/statekinds")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(kindJson))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<StateKind> resultKinds = StateKind.fromJsonArrayToStateKinds(responseJson);
    	Assert.assertEquals(2, resultKinds.size());
    }
    
    @Test
    @Transactional
    public void saveValueTypeAndKind() throws Exception{
    	String typeJson = "[{\"typeName\":\"test\"},{\"typeName\":\"default\"}]";
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/setup/valuetypes")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(typeJson))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<ValueType> resultTypes = ValueType.fromJsonArrayToValueTypes(responseJson);
    	Assert.assertEquals(2, resultTypes.size());
    	
    	String kindJson = "[{\"typeName\":\"test\",\"kindName\":\"test\"},{\"typeName\":\"default\",\"kindName\":\"default\"}]";
		response = this.mockMvc.perform(post("/api/v1/setup/valuekinds")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(kindJson))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<ValueKind> resultKinds = ValueKind.fromJsonArrayToValueKinds(responseJson);
    	Assert.assertEquals(2, resultKinds.size());
    }
    
    @Test
    @Transactional
    public void saveLabelTypeAndKind() throws Exception{
    	String typeJson = "[{\"typeName\":\"test\"},{\"typeName\":\"default\"}]";
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/setup/labeltypes")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(typeJson))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<LabelType> resultTypes = LabelType.fromJsonArrayToLabelTypes(responseJson);
    	Assert.assertEquals(2, resultTypes.size());
    	
    	String kindJson = "[{\"typeName\":\"test\",\"kindName\":\"test\"},{\"typeName\":\"default\",\"kindName\":\"default\"}]";
		response = this.mockMvc.perform(post("/api/v1/setup/labelkinds")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(kindJson))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<LabelKind> resultKinds = LabelKind.fromJsonArrayToLabelKinds(responseJson);
    	Assert.assertEquals(2, resultKinds.size());
    }
    
    @Test
    @Transactional
    public void saveThingTypeAndKind() throws Exception{
    	String typeJson = "[{\"typeName\":\"test\"},{\"typeName\":\"default\"}]";
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/setup/thingtypes")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(typeJson))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<ThingType> resultTypes = ThingType.fromJsonArrayToThingTypes(responseJson);
    	Assert.assertEquals(2, resultTypes.size());
    	
    	String kindJson = "[{\"typeName\":\"test\",\"kindName\":\"test\"},{\"typeName\":\"default\",\"kindName\":\"default\"}]";
		response = this.mockMvc.perform(post("/api/v1/setup/thingkinds")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(kindJson))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<ThingKind> resultKinds = ThingKind.fromJsonArrayToThingKinds(responseJson);
    	Assert.assertEquals(2, resultKinds.size());
    }
    
    @Test
    @Transactional
    public void saveOperatorTypeAndKind() throws Exception{
    	String typeJson = "[{\"typeName\":\"test\"},{\"typeName\":\"default\"}]";
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/setup/operatortypes")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(typeJson))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<OperatorType> resultTypes = OperatorType.fromJsonArrayToOperatorTypes(responseJson);
    	Assert.assertEquals(2, resultTypes.size());
    	
    	String kindJson = "[{\"typeName\":\"test\",\"kindName\":\"test\"},{\"typeName\":\"default\",\"kindName\":\"default\"}]";
		response = this.mockMvc.perform(post("/api/v1/setup/operatorkinds")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(kindJson))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<OperatorKind> resultKinds = OperatorKind.fromJsonArrayToOperatorKinds(responseJson);
    	Assert.assertEquals(2, resultKinds.size());
    }
    
    @Test
    @Transactional
    public void saveUnitTypeAndKind() throws Exception{
    	String typeJson = "[{\"typeName\":\"test\"},{\"typeName\":\"default\"}]";
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/setup/unittypes")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(typeJson))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<UnitType> resultTypes = UnitType.fromJsonArrayToUnitTypes(responseJson);
    	Assert.assertEquals(2, resultTypes.size());
    	
    	String kindJson = "[{\"typeName\":\"test\",\"kindName\":\"test\"},{\"typeName\":\"default\",\"kindName\":\"default\"}]";
		response = this.mockMvc.perform(post("/api/v1/setup/unitkinds")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(kindJson))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<UnitKind> resultKinds = UnitKind.fromJsonArrayToUnitKinds(responseJson);
    	Assert.assertEquals(2, resultKinds.size());
    }
    
    @Test
    @Transactional
    public void saveDDictTypeAndKindAndValues() throws Exception{
    	String typeJson = "[{\"typeName\":\"test\"},{\"typeName\":\"default\"}]";
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/setup/ddicttypes")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(typeJson))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<DDictType> resultTypes = DDictType.fromJsonArrayToDDictTypes(responseJson);
    	Assert.assertEquals(2, resultTypes.size());
    	
    	String kindJson = "[{\"typeName\":\"test\",\"kindName\":\"test\"},{\"typeName\":\"default\",\"kindName\":\"default\"}]";
		response = this.mockMvc.perform(post("/api/v1/setup/ddictkinds")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(kindJson))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<DDictKind> resultKinds = DDictKind.fromJsonArrayToDDictKinds(responseJson);
    	Assert.assertEquals(2, resultKinds.size());
    	
    	String valueJson="[{\"name\":\"Test\",\"codeType\":\"test\",\"codeKind\":\"test\",\"code\":\"test\",\"codeOrigin\":\"Testing\"}]";
    	response = this.mockMvc.perform(post("/api/v1/setup/codetables")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(valueJson))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<DDictValue> resultCodeTables = DDictValue.fromJsonArrayToDDictValues(responseJson);
    	Assert.assertEquals(1, resultCodeTables.size());
    }
    
    @Test
    @Transactional
    public void saveLabelSequences() throws Exception{
    	String json = "[{\"digits\":8,\"groupDigits\":false,\"labelPrefix\":\"PRCL\",\"labelSeparator\":\"-\",\"labelTypeAndKind\":\"id_codeName\",\"latestNumber\":1,\"thingTypeAndKind\":\"document_protocol\"},{\"digits\":8,\"groupDigits\":false, \"labelPrefix\":\"TEST\",\"labelSeparator\":\"-\",\"labelTypeAndKind\":\"id_codeName\",\"latestNumber\":1,\"thingTypeAndKind\":\"document_test\"}]";
    	MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/setup/labelsequences")
    			.contentType(MediaType.APPLICATION_JSON)
    			.accept(MediaType.APPLICATION_JSON)
    			.content(json))
    			.andExpect(status().isCreated())
    			.andExpect(content().contentType("application/json"))
    			.andReturn().getResponse();
    	String responseJson = response.getContentAsString();
    	logger.info(responseJson);
    	Collection<LabelSequence> resultTypes = LabelSequence.fromJsonArrayToLabelSequences(responseJson);
    	Assert.assertEquals(2, resultTypes.size());
    }

}
