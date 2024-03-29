package com.labsynch.labseer.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;

import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.dto.DependencyCheckDTO;
import com.labsynch.labseer.dto.StructureSearchDTO;
import com.labsynch.labseer.exceptions.ErrorMessage;

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

import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/applicationContext.xml",
		"classpath:/META-INF/spring/applicationContext-security.xml",
		"file:src/main/webapp/WEB-INF/spring/webmvc-config-test.xml" })
@Transactional
public class ApiLsThingControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(ApiLsThingControllerTest.class);

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void registerComponentParentTest() throws Exception {
		String json = "{\"codeName\":null,\"ignored\":false,\"lsKind\":\"linker small molecule\",\"lsLabels\":[{\"ignored\":false,\"imageFile\":null,\"labelText\":\"LSM 2\",\"lsKind\":\"linker small molecule\",\"lsTransaction\":1,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_linker small molecule\",\"modifiedDate\":null,\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"jane\",\"recordedDate\":1375141504000}],\"lsStates\":[{\"comments\":null,\"ignored\":false,\"lsKind\":\"linker small molecule parent\",\"lsTransaction\":1,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_linker small molecule parent\",\"lsValues\":[{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":1342080000000,\"fileValue\":null,\"ignored\":false,\"lsTransaction\":128,\"modifiedDate\":null,\"numericValue\":null,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":null,\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"completion date\",\"valueOperator\":null,\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"valueUnit\":null},{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"ignored\":false,\"lsTransaction\":127,\"modifiedDate\":null,\"numericValue\":null,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":\"Notebook 1\",\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"notebook\",\"valueOperator\":null,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"valueUnit\":null},{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"ignored\":false,\"lsTransaction\":128,\"modifiedDate\":null,\"numericValue\":231,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":null,\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"molecular weight\",\"valueOperator\":null,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_molecular weight\",\"unitKind\":\"g/mol\",\"unitType\":\"molecular weight\",\"valueUnit\":null},{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"ignored\":false,\"lsTransaction\":128,\"modifiedDate\":null,\"numericValue\":0,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":null,\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"batch number\",\"valueOperator\":null,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_batch number\",\"unitKind\":null,\"unitType\":null,\"valueUnit\":null}],\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jane\",\"recordedDate\":1375141460000}],\"lsTransaction\":1,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_linker small molecule\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jane\",\"recordedDate\":1375141508000,\"shortDescription\":\" \"}";
		MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/lsthings/parent/linker small molecule")
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

	@Test
	public void searchLabelText() throws Exception {
		logger.info("starting the test");
		String json = "{\"requests\":[{\"requestName\":\"GENE-000002\"}]}";
		MockHttpServletResponse response = this.mockMvc
				.perform(post("/api/v1/lsthings/getCodeNameFromNameRequest?thingType=gene&thingKind=entrez gene")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse();
		String responseJson = response.getContentAsString();
		logger.info("test finished: responseJSON is:");
		logger.info(responseJson);
	}

	@Test
	public void registerComponentBatchTest() throws Exception {
		String json = "{\"codeName\":null,\"ignored\":false,\"lsKind\":\"linker small molecule\",\"lsStates\":[{\"comments\":null,\"id\":11,\"ignored\":false,\"lsKind\":\"linker small molecule batch\",\"lsTransaction\":1,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_linker small molecule batch\",\"lsValues\":[{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":1342080000000,\"fileValue\":null,\"id\":12,\"ignored\":false,\"lsTransaction\":128,\"modifiedDate\":null,\"numericValue\":null,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":null,\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"completion date\",\"valueOperator\":null,\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"valueUnit\":null,\"version\":0},{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"id\":13,\"ignored\":false,\"lsTransaction\":127,\"modifiedDate\":null,\"numericValue\":null,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":\"Notebook 1\",\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"notebook\",\"valueOperator\":null,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"valueUnit\":null,\"version\":0}],\"recordedDate\":1363388477000,\"recordedBy\":\"jane\"},{\"comments\":null,\"id\":111,\"ignored\":false,\"lsKind\":\"inventory\",\"lsTransaction\":1,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"id\":14,\"ignored\":false,\"lsTransaction\":128,\"modifiedDate\":null,\"numericValue\":2.3,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":null,\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"amount\",\"valueOperator\":null,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount\",\"unitKind\":\"g\",\"unitType\":\"mass\",\"valueUnit\":null,\"version\":0},{\"clobValue\":null,\"codeValue\":null,\"comments\":null,\"dateValue\":null,\"fileValue\":null,\"id\":15,\"ignored\":false,\"lsTransaction\":127,\"modifiedDate\":null,\"numericValue\":null,\"publicData\":true,\"recordedDate\":1363388477000,\"recordedBy\":\"jane\",\"sigFigs\":null,\"stringValue\":\"Cabinet 1\",\"uncertainty\":null,\"urlValue\":null,\"lsKind\":\"location\",\"valueOperator\":null,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"valueUnit\":null,\"version\":0}],\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jane\",\"recordedDate\":1375141460000,\"version\":0}],\"lsTransaction\":1,\"lsType\":\"batch\",\"lsTypeAndKind\":\"batch_linker small molecule\",\"modifiedBy\":null,\"modifiedDate\":null,\"recordedBy\":\"jane\",\"recordedDate\":1375141508000,\"shortDescription\":\" \",\"version\":0}";
		String responseJson = this.mockMvc
				.perform(post("/api/v1/lsthings/batch/linker small molecule?parentIdOrCodeName=LSM000002")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse().getContentAsString();
		logger.info(responseJson);
		LsThing postedLsThing = LsThing.fromJsonToLsThing(responseJson);
		logger.info(postedLsThing.toJson());
	}

	@Test
	public void updateComponentParentTest() throws Exception {
		String json = "{\"codeName\":\"LSM000001\",\"deleted\":false,\"id\":1191916,\"ignored\":false,\"lsKind\":\"linker small molecule\",\"lsLabels\":[{\"deleted\":false,\"id\":297063,\"ignored\":false,\"labelText\":\"LSM\",\"lsKind\":\"linker small molecule\",\"lsTransaction\":1,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_linker small molecule\",\"physicallyLabeled\":false,\"preferred\":true,\"recordedBy\":\"jane\",\"recordedDate\":1375141504000,\"version\":0}],\"lsStates\":[{\"comments\":\"test state comment\",\"deleted\":false,\"id\":2170023,\"ignored\":false,\"lsKind\":\"linker small molecule parent\",\"lsTransaction\":1,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_linker small molecule parent\",\"lsValues\":[{\"comments\":\"test value comment\",\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4584944,\"ignored\":false,\"lsKind\":\"batch number\",\"lsTransaction\":128,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_batch number\",\"numericValue\":1,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"unitTypeAndKind\":\"null_null\",\"version\":1},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4584942,\"ignored\":false,\"lsKind\":\"molecular weight\",\"lsTransaction\":128,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_molecular weight\",\"numericValue\":231,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"unitKind\":\"g/mol\",\"unitType\":\"molecular weight\",\"unitTypeAndKind\":\"molecular weight_g/mol\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4584945,\"ignored\":false,\"lsKind\":\"notebook\",\"lsTransaction\":127,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"stringValue\":\"Notebook 1\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1342080000000,\"deleted\":false,\"id\":4584943,\"ignored\":false,\"lsKind\":\"completion date\",\"lsTransaction\":128,\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"unitTypeAndKind\":\"null_null\",\"version\":0}],\"recordedBy\":\"jane\",\"recordedDate\":1375141460000,\"version\":1}],\"lsTags\":[],\"lsTransaction\":1,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_linker small molecule\",\"recordedBy\":\"jane\",\"recordedDate\":1375141508000,\"version\":1}";
		MockHttpServletResponse response = this.mockMvc
				.perform(put("/api/v1/lsthings/parent/linker small molecule/LSM000001")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse();
		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		LsThing postedLsThing = LsThing.fromJsonToLsThing(responseJson);
		logger.info(postedLsThing.toJson());
	}

	@Test
	public void updateComponentBatchTest() throws Exception {
		String json = "{\"codeName\":\"LSM000001-1\",\"deleted\":false,\"id\":1191917,\"ignored\":false,\"lsKind\":\"linker small molecule\",\"lsLabels\":[],\"lsStates\":[{\"deleted\":false,\"id\":2170024,\"ignored\":false,\"lsKind\":\"linker small molecule batch\",\"lsTransaction\":1,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_linker small molecule batch\",\"lsValues\":[{\"comments\":\"test notebook update comment\",\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4584947,\"ignored\":false,\"lsKind\":\"notebook\",\"lsTransaction\":127,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"stringValue\":\"Notebook 1\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1342080000000,\"deleted\":false,\"id\":4584946,\"ignored\":false,\"lsKind\":\"completion date\",\"lsTransaction\":128,\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"unitTypeAndKind\":\"null_null\",\"version\":0}],\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"version\":1},{\"deleted\":false,\"id\":2170025,\"ignored\":false,\"lsKind\":\"inventory\",\"lsTransaction\":1,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4584949,\"ignored\":false,\"lsKind\":\"amount\",\"lsTransaction\":128,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount\",\"numericValue\":2.29999999999999982236431605997495353221893310546875,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"unitKind\":\"g\",\"unitType\":\"mass\",\"unitTypeAndKind\":\"mass_g\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":4584948,\"ignored\":false,\"lsKind\":\"location\",\"lsTransaction\":127,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"stringValue\":\"Cabinet 1\",\"unitTypeAndKind\":\"null_null\",\"version\":0}],\"recordedBy\":\"jane\",\"recordedDate\":1375141460000,\"version\":1}],\"lsTags\":[],\"lsTransaction\":1,\"lsType\":\"batch\",\"lsTypeAndKind\":\"batch_linker small molecule\",\"recordedBy\":\"jane\",\"recordedDate\":1375141508000,\"version\":1}";
		String responseJson = this.mockMvc.perform(put("/api/v1/lsthings/batch/linker small molecule/LSM000001-1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse().getContentAsString();
		logger.info(responseJson);
		LsThing postedLsThing = LsThing.fromJsonToLsThing(responseJson);
		logger.info(postedLsThing.toJson());
	}

	@Test
	@Rollback(value = false)
	public void registerAssemblyParentTest() throws Exception {
		String json = "{\"lsType\":\"parent\",\"lsKind\":\"internalization agent\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1428352474150,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"internalization agent\",\"labelText\":\"LSM10-peg5\",\"ignored\":false,\"preferred\":true,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null}],\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"internalization agent parent\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"scientist\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"codeOrigin\":\"ACAS authors\",\"codeValue\":\"egao\",\"value\":\"egao\"},{\"lsType\":\"dateValue\",\"lsKind\":\"completion date\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"value\":1428303600000,\"dateValue\":1428303600000},{\"lsType\":\"stringValue\",\"lsKind\":\"notebook\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"value\":\"test\",\"stringValue\":\"test\"},{\"lsType\":\"codeValue\",\"lsKind\":\"conjugation type\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"codeKind\":\"conjugation type\",\"codeType\":\"internalization agent\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"cys\",\"value\":\"cys\"},{\"lsType\":\"stringValue\",\"lsKind\":\"conjugation site\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"numericValue\",\"lsKind\":\"batch number\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"numericValue\":0,\"value\":0}],\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\"}],\"firstLsThings\":[],\"secondLsThings\":[{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":1}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1428352474145,\"secondLsThing\":{\"codeName\":\"LSM000010\",\"deleted\":false,\"id\":1442338,\"ignored\":false,\"lsKind\":\"linker small molecule\",\"lsLabels\":[{\"deleted\":false,\"id\":297426,\"ignored\":false,\"labelText\":\"LSM10\",\"lsKind\":\"linker small molecule\",\"lsType\":\"name\",\"lsTypeAndKind\":\"name_linker small molecule\",\"modifiedDate\":1426028499740,\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1426024482348,\"version\":3}],\"lsTags\":[],\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_linker small molecule\",\"modifiedDate\":1426028499738,\"recordedBy\":\"bob\",\"recordedDate\":1426028499321,\"version\":4},\"className\":\"ThingItx\",\"validationError\":null,\"idAttribute\":\"id\"},{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":2}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1428352474148,\"secondLsThing\":{\"codeName\":\"PEG000010\",\"deleted\":false,\"id\":1442920,\"ignored\":false,\"lsKind\":\"peg\",\"lsLabels\":[{\"deleted\":false,\"id\":297434,\"ignored\":false,\"labelText\":\"peg5\",\"lsKind\":\"peg\",\"lsType\":\"name\",\"lsTypeAndKind\":\"name_peg\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1426100246459,\"version\":0}],\"lsTags\":[],\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_peg\",\"recordedBy\":\"bob\",\"recordedDate\":1426100246459,\"version\":1},\"className\":\"ThingItx\",\"validationError\":null,\"idAttribute\":\"id\"}],\"cid\":\"c104\",\"_changing\":false,\"_previousAttributes\":{\"lsType\":\"thing\",\"lsKind\":\"thing\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1428352432123,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"internalization agent\",\"labelText\":\"LSM10-peg5\",\"ignored\":false,\"preferred\":true,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null}],\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"internalization agent parent\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"scientist\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"codeOrigin\":\"ACAS authors\",\"codeValue\":\"egao\",\"value\":\"egao\"},{\"lsType\":\"dateValue\",\"lsKind\":\"completion date\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"value\":1428303600000,\"dateValue\":1428303600000},{\"lsType\":\"stringValue\",\"lsKind\":\"notebook\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"value\":\"test\",\"stringValue\":\"test\"},{\"lsType\":\"codeValue\",\"lsKind\":\"conjugation type\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"codeKind\":\"conjugation type\",\"codeType\":\"internalization agent\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"cys\",\"value\":\"cys\"},{\"lsType\":\"stringValue\",\"lsKind\":\"conjugation site\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"numericValue\",\"lsKind\":\"batch number\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"numericValue\":0,\"value\":0}],\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\"}],\"firstLsThings\":[]},\"changed\":{\"secondLsThings\":[]},\"_pending\":false,\"urlRoot\":\"/api/things/parent/internalization agent\",\"className\":\"InternalizationAgentParent\",\"lsProperties\":{\"defaultLabels\":[{\"key\":\"internalization agent name\",\"type\":\"name\",\"kind\":\"internalization agent\",\"preferred\":true}],\"defaultValues\":[{\"key\":\"scientist\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"codeValue\",\"kind\":\"scientist\",\"codeOrigin\":\"ACAS authors\",\"value\":\"unassigned\"},{\"key\":\"completion date\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"dateValue\",\"kind\":\"completion date\"},{\"key\":\"notebook\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"stringValue\",\"kind\":\"notebook\"},{\"key\":\"conjugation type\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"codeValue\",\"kind\":\"conjugation type\",\"codeType\":\"internalization agent\",\"codeKind\":\"conjugation type\",\"codeOrigin\":\"ACAS DDICT\",\"value\":\"unassigned\"},{\"key\":\"conjugation site\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"stringValue\",\"kind\":\"conjugation site\"},{\"key\":\"batch number\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"numericValue\",\"kind\":\"batch number\",\"value\":0}],\"defaultFirstLsThingItx\":[],\"defaultSecondLsThingItx\":[]},\"validationError\":null,\"idAttribute\":\"id\"}";
		String responseJson = this.mockMvc.perform(post("/api/v1/lsthings/parent/internalization agent")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse().getContentAsString();
		logger.info(responseJson);
		LsThing postedLsThing = LsThing.fromJsonToLsThing(responseJson);
		logger.info(postedLsThing.toJson());
	}

	// @Test
	public void registerAssemblyBatchTest() throws Exception {
		String json = "";
		String responseJson = this.mockMvc
				.perform(post("/api/v1/lsthings/batch/internalization agent?parentIdOrCodeName=")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse().getContentAsString();
		logger.info(responseJson);
		LsThing postedLsThing = LsThing.fromJsonToLsThing(responseJson);
		logger.info(postedLsThing.toJson());
	}

	@Test
	public void validateInvalidComponentNameTest() throws Exception {
		String json = "{\"codeName\": null,\"firstLsThings\": [{\"deleted\": false,\"firstLsThing\": {\"codeName\": \"PRTN-000013-1\",\"deleted\": false,\"ignored\": false,\"lsKind\": \"protein\",\"lsStates\":[{\"deleted\":false,\"ignored\":false,\"lsKind\":\"inventory\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"location\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729705505,\"stringValue\":\"123\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"amount made\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount made\",\"numericValue\":1123,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729706303,\"unitKind\":\"g\",\"unitType\":\"mass\",\"unitTypeAndKind\":\"mass_g\",\"version\":0}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"version\":1},{\"deleted\":false,\"ignored\":false,\"lsKind\":\"protein batch\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_protein batch\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"hplc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_hplc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeOrigin\":\"ACAS authors\",\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"egao\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"scientist\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702585,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"DeleteExperimentInstructions (6).pdf\",\"ignored\":false,\"lsKind\":\"gpc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_gpc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1424678400000,\"deleted\":false,\"ignored\":false,\"lsKind\":\"completion date\",\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703242,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"ms\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_ms\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeKind\":\"source\",\"codeOrigin\":\"ACAS DDICT\",\"codeType\":\"component\",\"codeTypeAndKind\":\"component_source\",\"codeValue\":\"ACAS\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"source\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_source\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729672870,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"purity\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_purity\",\"numericValue\":129,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729708062,\"unitKind\":\"% purity\",\"unitType\":\"percentage\",\"unitTypeAndKind\":\"percentage_% purity\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"nmr\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_nmr\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"notebook\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703953,\"stringValue\":\"test\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"source id\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_source id\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702610,\"stringValue\":\"\",\"unitTypeAndKind\":\"null_null\",\"version\":0}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"version\":1}],\"lsType\": \"batch\",\"lsTypeAndKind\": \"batch_protein\",\"recordedBy\": \"jane\",\"recordedDate\": 1424730027913,\"version\": null},\"id\": null,\"ignored\": false,\"lsKind\": \"batch_parent\",\"lsType\": \"instantiates\",\"lsTypeAndKind\": \"instantiates_batch_parent\",\"recordedBy\": \"jane\",\"recordedDate\": 1424730027913,\"version\": null}],\"ignored\": false,\"lsKind\": \"protein\",\"lsLabels\": [{ \"ignored\": false,\"imageFile\": null,\"labelText\": \"testing2\",\"lsKind\": \"protein\",\"lsTransaction\": 9999,\"lsType\": \"name\",\"lsTypeAndKind\": \"name_protein\",\"modifiedDate\": null,\"physicallyLabled\": false,\"preferred\": true,\"recordedBy\": \"jane\",\"recordedDate\": 1375141504000}],\"lsStates\": [{ \"comments\": null,\"ignored\": false,\"lsKind\": \"protein parent\",\"lsTransaction\": 9999,\"lsType\": \"metadata\",\"lsTypeAndKind\": \"metadata_protein parent\",\"lsValues\": [{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": 1342080000000, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"completion date\", \"lsTransaction\": 9999, \"lsType\": \"dateValue\", \"lsTypeAndKind\": \"dateValue_completion date\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"notebook\", \"lsTransaction\": 9999, \"lsType\": \"stringValue\", \"lsTypeAndKind\": \"stringValue_notebook\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": \"Notebook 1\", \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"molecular weight\", \"lsTransaction\": 9999, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_molecular weight\", \"modifiedDate\": null, \"numericValue\": 231, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": \"g/mol\", \"unitType\": \"molecular weight\", \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"batch number\", \"lsTransaction\": 9999, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_batch number\", \"modifiedDate\": null, \"numericValue\": 0, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": null, \"unitType\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null} ],\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141460000}],\"lsTransaction\": 9999,\"lsType\": \"parent\",\"lsTypeAndKind\": \"parent_protein\",\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141508000,\"shortDescription\": \" \"}";
		String response = this.mockMvc.perform(post("/api/v1/lsthings/validate?uniqueName=true")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict())
				.andExpect(content().contentType("application/json;charset=utf-8"))
				.andReturn().getResponse().getContentAsString();
		logger.info(response);
	}

	@Test
	public void validateValidComponentNameTest() throws Exception {
		String json = "{\"codeName\": null,\"firstLsThings\": [{\"deleted\": false,\"firstLsThing\": {\"codeName\": \"PRTN-000013-1\",\"deleted\": false,\"ignored\": false,\"lsKind\": \"protein\",\"lsStates\":[{\"deleted\":false,\"ignored\":false,\"lsKind\":\"inventory\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"location\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729705505,\"stringValue\":\"123\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"amount made\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount made\",\"numericValue\":1123,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729706303,\"unitKind\":\"g\",\"unitType\":\"mass\",\"unitTypeAndKind\":\"mass_g\",\"version\":0}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"version\":1},{\"deleted\":false,\"ignored\":false,\"lsKind\":\"protein batch\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_protein batch\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"hplc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_hplc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeOrigin\":\"ACAS authors\",\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"egao\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"scientist\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702585,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"DeleteExperimentInstructions (6).pdf\",\"ignored\":false,\"lsKind\":\"gpc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_gpc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1424678400000,\"deleted\":false,\"ignored\":false,\"lsKind\":\"completion date\",\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703242,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"ms\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_ms\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeKind\":\"source\",\"codeOrigin\":\"ACAS DDICT\",\"codeType\":\"component\",\"codeTypeAndKind\":\"component_source\",\"codeValue\":\"ACAS\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"source\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_source\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729672870,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"purity\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_purity\",\"numericValue\":129,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729708062,\"unitKind\":\"% purity\",\"unitType\":\"percentage\",\"unitTypeAndKind\":\"percentage_% purity\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"nmr\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_nmr\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"notebook\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703953,\"stringValue\":\"test\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"source id\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_source id\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702610,\"stringValue\":\"\",\"unitTypeAndKind\":\"null_null\",\"version\":0}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"version\":1}],\"lsType\": \"batch\",\"lsTypeAndKind\": \"batch_protein\",\"recordedBy\": \"jane\",\"recordedDate\": 1424730027913,\"version\": null},\"id\": null,\"ignored\": false,\"lsKind\": \"batch_parent\",\"lsType\": \"instantiates\",\"lsTypeAndKind\": \"instantiates_batch_parent\",\"recordedBy\": \"jane\",\"recordedDate\": 1424730027913,\"version\": null}],\"ignored\": false,\"lsKind\": \"protein\",\"lsLabels\": [{ \"ignored\": false,\"imageFile\": null,\"labelText\": \"Fielderase\",\"lsKind\": \"protein\",\"lsTransaction\": 9999,\"lsType\": \"name\",\"lsTypeAndKind\": \"name_protein\",\"modifiedDate\": null,\"physicallyLabled\": false,\"preferred\": true,\"recordedBy\": \"jane\",\"recordedDate\": 1375141504000}],\"lsStates\": [{ \"comments\": null,\"ignored\": false,\"lsKind\": \"protein parent\",\"lsTransaction\": 9999,\"lsType\": \"metadata\",\"lsTypeAndKind\": \"metadata_protein parent\",\"lsValues\": [{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": 1342080000000, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"completion date\", \"lsTransaction\": 9999, \"lsType\": \"dateValue\", \"lsTypeAndKind\": \"dateValue_completion date\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"notebook\", \"lsTransaction\": 9999, \"lsType\": \"stringValue\", \"lsTypeAndKind\": \"stringValue_notebook\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": \"Notebook 1\", \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"molecular weight\", \"lsTransaction\": 9999, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_molecular weight\", \"modifiedDate\": null, \"numericValue\": 231, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": \"g/mol\", \"unitType\": \"molecular weight\", \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"batch number\", \"lsTransaction\": 9999, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_batch number\", \"modifiedDate\": null, \"numericValue\": 0, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": null, \"unitType\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null} ],\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141460000}],\"lsTransaction\": 9999,\"lsType\": \"parent\",\"lsTypeAndKind\": \"parent_protein\",\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141508000,\"shortDescription\": \" \"}";
		String response = this.mockMvc.perform(post("/api/v1/lsthings/validate?uniqueName=true")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andExpect(content().contentType("application/json;charset=utf-8"))
				.andReturn().getResponse().getContentAsString();
	}

	// @Test
	public void validateInvalidAssemblyTest() throws Exception {
		String componentNameList = "[\"CMPT-00001\", \"CMPT-00002\", \"CMPT-00003\"]";
		String response = this.mockMvc.perform(post("/api/v1/lsthings/validatename?lsKind=internalization agent")
				.contentType(MediaType.APPLICATION_JSON)
				.content(componentNameList)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=utf-8"))
				.andReturn().getResponse().getContentAsString();
		logger.info(response);
		Assert.assertEquals(false, Boolean.parseBoolean(response));
	}

	@Test
	public void validateValidAssemblyTest() throws Exception {
		String json = "{\"lsType\":\"parent\",\"lsKind\":\"internalization agent\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1428352474150,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"internalization agent\",\"labelText\":\"LSM10-peg5\",\"ignored\":false,\"preferred\":true,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null}],\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"internalization agent parent\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"scientist\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"codeOrigin\":\"ACAS authors\",\"codeValue\":\"egao\",\"value\":\"egao\"},{\"lsType\":\"dateValue\",\"lsKind\":\"completion date\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"value\":1428303600000,\"dateValue\":1428303600000},{\"lsType\":\"stringValue\",\"lsKind\":\"notebook\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"value\":\"test\",\"stringValue\":\"test\"},{\"lsType\":\"codeValue\",\"lsKind\":\"conjugation type\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"codeKind\":\"conjugation type\",\"codeType\":\"internalization agent\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"cys\",\"value\":\"cys\"},{\"lsType\":\"stringValue\",\"lsKind\":\"conjugation site\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"numericValue\",\"lsKind\":\"batch number\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"numericValue\":0,\"value\":0}],\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\"}],\"firstLsThings\":[],\"secondLsThings\":[{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":1}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1428352474145,\"secondLsThing\":{\"codeName\":\"LSM000010\",\"deleted\":false,\"id\":1442338,\"ignored\":false,\"lsKind\":\"linker small molecule\",\"lsLabels\":[{\"deleted\":false,\"id\":297426,\"ignored\":false,\"labelText\":\"LSM10\",\"lsKind\":\"linker small molecule\",\"lsType\":\"name\",\"lsTypeAndKind\":\"name_linker small molecule\",\"modifiedDate\":1426028499740,\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1426024482348,\"version\":3}],\"lsTags\":[],\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_linker small molecule\",\"modifiedDate\":1426028499738,\"recordedBy\":\"bob\",\"recordedDate\":1426028499321,\"version\":4},\"className\":\"ThingItx\",\"validationError\":null,\"idAttribute\":\"id\"},{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":2}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1428352474148,\"secondLsThing\":{\"codeName\":\"PEG000010\",\"deleted\":false,\"id\":1442920,\"ignored\":false,\"lsKind\":\"peg\",\"lsLabels\":[{\"deleted\":false,\"id\":297434,\"ignored\":false,\"labelText\":\"peg5\",\"lsKind\":\"peg\",\"lsType\":\"name\",\"lsTypeAndKind\":\"name_peg\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1426100246459,\"version\":0}],\"lsTags\":[],\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_peg\",\"recordedBy\":\"bob\",\"recordedDate\":1426100246459,\"version\":1},\"className\":\"ThingItx\",\"validationError\":null,\"idAttribute\":\"id\"}],\"cid\":\"c104\",\"_changing\":false,\"_previousAttributes\":{\"lsType\":\"thing\",\"lsKind\":\"thing\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1428352432123,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"internalization agent\",\"labelText\":\"LSM10-peg5\",\"ignored\":false,\"preferred\":true,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null}],\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"internalization agent parent\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"scientist\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"codeOrigin\":\"ACAS authors\",\"codeValue\":\"egao\",\"value\":\"egao\"},{\"lsType\":\"dateValue\",\"lsKind\":\"completion date\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"value\":1428303600000,\"dateValue\":1428303600000},{\"lsType\":\"stringValue\",\"lsKind\":\"notebook\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"value\":\"test\",\"stringValue\":\"test\"},{\"lsType\":\"codeValue\",\"lsKind\":\"conjugation type\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"codeKind\":\"conjugation type\",\"codeType\":\"internalization agent\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"cys\",\"value\":\"cys\"},{\"lsType\":\"stringValue\",\"lsKind\":\"conjugation site\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"numericValue\",\"lsKind\":\"batch number\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"numericValue\":0,\"value\":0}],\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\"}],\"firstLsThings\":[]},\"changed\":{\"secondLsThings\":[]},\"_pending\":false,\"urlRoot\":\"/api/things/parent/internalization agent\",\"className\":\"InternalizationAgentParent\",\"lsProperties\":{\"defaultLabels\":[{\"key\":\"internalization agent name\",\"type\":\"name\",\"kind\":\"internalization agent\",\"preferred\":true}],\"defaultValues\":[{\"key\":\"scientist\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"codeValue\",\"kind\":\"scientist\",\"codeOrigin\":\"ACAS authors\",\"value\":\"unassigned\"},{\"key\":\"completion date\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"dateValue\",\"kind\":\"completion date\"},{\"key\":\"notebook\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"stringValue\",\"kind\":\"notebook\"},{\"key\":\"conjugation type\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"codeValue\",\"kind\":\"conjugation type\",\"codeType\":\"internalization agent\",\"codeKind\":\"conjugation type\",\"codeOrigin\":\"ACAS DDICT\",\"value\":\"unassigned\"},{\"key\":\"conjugation site\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"stringValue\",\"kind\":\"conjugation site\"},{\"key\":\"batch number\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"numericValue\",\"kind\":\"batch number\",\"value\":0}],\"defaultFirstLsThingItx\":[],\"defaultSecondLsThingItx\":[]},\"validationError\":null,\"idAttribute\":\"id\"}";
		String response = this.mockMvc.perform(post("/api/v1/lsthings/validate?uniqueName=true&orderMatters=true")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andExpect(content().contentType("application/json;charset=utf-8"))
				.andReturn().getResponse().getContentAsString();
		logger.info(response);
	}

	@Test
	public void validateValidAssemblyTest2() throws Exception {
		String json = "{\"lsType\":\"parent\",\"lsKind\":\"internalization agent\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1428352474150,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"internalization agent\",\"labelText\":\"LSM10-peg5\",\"ignored\":false,\"preferred\":true,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null}],\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"internalization agent parent\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"scientist\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"codeOrigin\":\"ACAS authors\",\"codeValue\":\"egao\",\"value\":\"egao\"},{\"lsType\":\"dateValue\",\"lsKind\":\"completion date\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"value\":1428303600000,\"dateValue\":1428303600000},{\"lsType\":\"stringValue\",\"lsKind\":\"notebook\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"value\":\"test\",\"stringValue\":\"test\"},{\"lsType\":\"codeValue\",\"lsKind\":\"conjugation type\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"codeKind\":\"conjugation type\",\"codeType\":\"internalization agent\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"cys\",\"value\":\"cys\"},{\"lsType\":\"stringValue\",\"lsKind\":\"conjugation site\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"numericValue\",\"lsKind\":\"batch number\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"numericValue\":0,\"value\":0}],\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\"}],\"firstLsThings\":[],\"secondLsThings\":[{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":1}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1428352474145,\"secondLsThing\":{\"codeName\":\"LSM000010\",\"deleted\":false,\"id\":1442338,\"ignored\":false,\"lsKind\":\"linker small molecule\",\"lsLabels\":[{\"deleted\":false,\"id\":297426,\"ignored\":false,\"labelText\":\"LSM10\",\"lsKind\":\"linker small molecule\",\"lsType\":\"name\",\"lsTypeAndKind\":\"name_linker small molecule\",\"modifiedDate\":1426028499740,\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1426024482348,\"version\":3}],\"lsTags\":[],\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_linker small molecule\",\"modifiedDate\":1426028499738,\"recordedBy\":\"bob\",\"recordedDate\":1426028499321,\"version\":4},\"className\":\"ThingItx\",\"validationError\":null,\"idAttribute\":\"id\"},{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":2}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1428352474148,\"secondLsThing\":{\"codeName\":\"PEG000010\",\"deleted\":false,\"id\":1442920,\"ignored\":false,\"lsKind\":\"peg\",\"lsLabels\":[{\"deleted\":false,\"id\":297434,\"ignored\":false,\"labelText\":\"peg5\",\"lsKind\":\"peg\",\"lsType\":\"name\",\"lsTypeAndKind\":\"name_peg\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1426100246459,\"version\":0}],\"lsTags\":[],\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_peg\",\"recordedBy\":\"bob\",\"recordedDate\":1426100246459,\"version\":1},\"className\":\"ThingItx\",\"validationError\":null,\"idAttribute\":\"id\"}],\"cid\":\"c104\",\"_changing\":false,\"_previousAttributes\":{\"lsType\":\"thing\",\"lsKind\":\"thing\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1428352432123,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"internalization agent\",\"labelText\":\"LSM10-peg5\",\"ignored\":false,\"preferred\":true,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null}],\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"internalization agent parent\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"scientist\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"codeOrigin\":\"ACAS authors\",\"codeValue\":\"egao\",\"value\":\"egao\"},{\"lsType\":\"dateValue\",\"lsKind\":\"completion date\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"value\":1428303600000,\"dateValue\":1428303600000},{\"lsType\":\"stringValue\",\"lsKind\":\"notebook\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"value\":\"test\",\"stringValue\":\"test\"},{\"lsType\":\"codeValue\",\"lsKind\":\"conjugation type\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"codeKind\":\"conjugation type\",\"codeType\":\"internalization agent\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"cys\",\"value\":\"cys\"},{\"lsType\":\"stringValue\",\"lsKind\":\"conjugation site\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"value\":\"\",\"stringValue\":\"\"},{\"lsType\":\"numericValue\",\"lsKind\":\"batch number\",\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\",\"numericValue\":0,\"value\":0}],\"ignored\":false,\"recordedDate\":1428352474150,\"recordedBy\":\"bob\"}],\"firstLsThings\":[]},\"changed\":{\"secondLsThings\":[]},\"_pending\":false,\"urlRoot\":\"/api/things/parent/internalization agent\",\"className\":\"InternalizationAgentParent\",\"lsProperties\":{\"defaultLabels\":[{\"key\":\"internalization agent name\",\"type\":\"name\",\"kind\":\"internalization agent\",\"preferred\":true}],\"defaultValues\":[{\"key\":\"scientist\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"codeValue\",\"kind\":\"scientist\",\"codeOrigin\":\"ACAS authors\",\"value\":\"unassigned\"},{\"key\":\"completion date\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"dateValue\",\"kind\":\"completion date\"},{\"key\":\"notebook\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"stringValue\",\"kind\":\"notebook\"},{\"key\":\"conjugation type\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"codeValue\",\"kind\":\"conjugation type\",\"codeType\":\"internalization agent\",\"codeKind\":\"conjugation type\",\"codeOrigin\":\"ACAS DDICT\",\"value\":\"unassigned\"},{\"key\":\"conjugation site\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"stringValue\",\"kind\":\"conjugation site\"},{\"key\":\"batch number\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent parent\",\"type\":\"numericValue\",\"kind\":\"batch number\",\"value\":0}],\"defaultFirstLsThingItx\":[],\"defaultSecondLsThingItx\":[]},\"validationError\":null,\"idAttribute\":\"id\"}";
		String response = this.mockMvc.perform(
				post("/api/v1/lsthings/validate?uniqueName=true&orderMatters=true&forwardAndReverseAreSame=true")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted())
				.andExpect(content().contentType("application/json;charset=utf-8"))
				.andReturn().getResponse().getContentAsString();
		logger.info(response);
	}

	@Test
	public void getLsThing() throws Exception {
		String json = this.mockMvc.perform(get("/api/v1/lsthings/parent/linker small molecule/LSM000001-1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse().getContentAsString();

		logger.info(json);
		LsThing lsThing = LsThing.fromJsonToLsThing(json);
		logger.info(lsThing.toJson());
	}

	@Test
	public void getComponentBatches() throws Exception {
		String json = this.mockMvc.perform(get("/api/v1/lsthings/parent/linker small molecule/getbatches/LSM000001")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse().getContentAsString();

		logger.info(json);
		Collection<LsThing> lsThings = LsThing.fromJsonArrayToLsThings(json);
		Assert.assertEquals(1, lsThings.size());
		logger.info(LsThing.toJsonArray(lsThings));
	}

	@Test
	public void searchForComponentBatch() throws Exception {
		String json = this.mockMvc.perform(get("/api/v1/lsthings/search?lsType=batch&q=LSM000001-1 2012-07-12")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse().getContentAsString();

		logger.info(json);
		Collection<LsThing> lsThings = LsThing.fromJsonArrayToLsThings(json);
		Assert.assertEquals(1, lsThings.size());
		logger.info(LsThing.toJsonArray(lsThings));
	}

	@Test
	public void getCodeTableLsThings() throws Exception {
		String json = this.mockMvc.perform(get("/api/v1/lsthings/codetable?lsType=parent&lsKind=linker small molecule")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=utf-8"))
				.andReturn().getResponse().getContentAsString();
		logger.info(json);
	}

	@Test
	public void getCodeTableLsThings2() throws Exception {
		String json = this.mockMvc.perform(get("/api/v1/lsthings/codetable?lsType=term&lsKind=documentTerm")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=utf-8"))
				.andReturn().getResponse().getContentAsString();
		logger.info(json);
	}

	@Test
	public void searchByScientist() throws Exception {
		String searchString = "jane";
		String json = this.mockMvc.perform(get("/api/v1/lsthings/search?q=" + searchString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse().getContentAsString();

		logger.info(json);
		Collection<LsThing> lsThings = LsThing.fromJsonArrayToLsThings(json);
		Assert.assertFalse(lsThings.isEmpty());
		logger.info(LsThing.toJsonArray(lsThings));
	}

	@Test
	@Transactional
	public void registerParentAndBatchTest() throws Exception {
		// String json = "{\"codeName\": null,\"firstLsThings\": [{\"deleted\":
		// false,\"firstLsThing\": {\"codeName\": \"PRTN-000013-1\",\"deleted\":
		// false,\"ignored\": false,\"lsKind\": \"protein\",\"lsType\":
		// \"batch\",\"lsTypeAndKind\": \"batch_protein\",\"recordedBy\":
		// \"jane\",\"recordedDate\": 1424730027913,\"version\": null},\"id\":
		// null,\"ignored\": false,\"lsKind\": \"batch_parent\",\"lsType\":
		// \"instantiates\",\"lsTypeAndKind\":
		// \"instantiates_batch_parent\",\"recordedBy\": \"jane\",\"recordedDate\":
		// 1424730027913,\"version\": null}],\"ignored\": false,\"lsKind\":
		// \"protein\",\"lsLabels\": [{ \"ignored\": false,\"imageFile\":
		// null,\"labelText\": \"Fielderase\",\"lsKind\": \"protein\",\"lsTransaction\":
		// 9999,\"lsType\": \"name\",\"lsTypeAndKind\":
		// \"name_protein\",\"modifiedDate\": null,\"physicallyLabled\":
		// false,\"preferred\": true,\"recordedBy\": \"jane\",\"recordedDate\":
		// 1375141504000}],\"lsStates\": [{ \"comments\": null,\"ignored\":
		// false,\"lsKind\": \"protein parent\",\"lsTransaction\": 9999,\"lsType\":
		// \"metadata\",\"lsTypeAndKind\": \"metadata_protein parent\",\"lsValues\":
		// [{\"clobValue\": null, \"codeValue\": null, \"comments\": null,
		// \"dateValue\": 1342080000000, \"fileValue\": null, \"ignored\": false,
		// \"lsKind\": \"completion date\", \"lsTransaction\": 9999, \"lsType\":
		// \"dateValue\", \"lsTypeAndKind\": \"dateValue_completion date\",
		// \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true,
		// \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null,
		// \"stringValue\": null, \"uncertainty\": null, \"urlValue\": null,
		// \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null,
		// \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\":
		// null, \"ignored\": false, \"lsKind\": \"notebook\", \"lsTransaction\": 9999,
		// \"lsType\": \"stringValue\", \"lsTypeAndKind\": \"stringValue_notebook\",
		// \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true,
		// \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null,
		// \"stringValue\": \"Notebook 1\", \"uncertainty\": null, \"urlValue\": null,
		// \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null,
		// \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\":
		// null, \"ignored\": false, \"lsKind\": \"molecular weight\",
		// \"lsTransaction\": 9999, \"lsType\": \"numericValue\", \"lsTypeAndKind\":
		// \"numericValue_molecular weight\", \"modifiedDate\": null, \"numericValue\":
		// 231, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\":
		// 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\":
		// null, \"unitKind\": \"g/mol\", \"unitType\": \"molecular weight\",
		// \"urlValue\": null, \"valueOperator\": null, \"valueUnit\":
		// null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null,
		// \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\":
		// \"batch number\", \"lsTransaction\": 9999, \"lsType\": \"numericValue\",
		// \"lsTypeAndKind\": \"numericValue_batch number\", \"modifiedDate\": null,
		// \"numericValue\": 0, \"publicData\": true, \"recordedBy\": \"jane\",
		// \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null,
		// \"uncertainty\": null, \"unitKind\": null, \"unitType\": null, \"urlValue\":
		// null, \"valueOperator\": null, \"valueUnit\": null} ],\"modifiedBy\":
		// null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\":
		// 1375141460000}],\"lsTransaction\": 9999,\"lsType\":
		// \"parent\",\"lsTypeAndKind\": \"parent_protein\",\"modifiedBy\":
		// null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\":
		// 1375141508000,\"shortDescription\": \" \"}";
		String json = "{\"codeName\": null,\"firstLsThings\": [{\"deleted\": false,\"firstLsThing\": {\"codeName\": \"PRTN-000013-1\",\"deleted\": false,\"ignored\": false,\"lsKind\": \"protein\",\"lsStates\":[{\"deleted\":false,\"ignored\":false,\"lsKind\":\"inventory\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"location\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729705505,\"stringValue\":\"123\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"amount made\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount made\",\"numericValue\":1123,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729706303,\"unitKind\":\"g\",\"unitType\":\"mass\",\"unitTypeAndKind\":\"mass_g\",\"version\":0}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"version\":1},{\"deleted\":false,\"ignored\":false,\"lsKind\":\"protein batch\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_protein batch\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"hplc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_hplc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeOrigin\":\"ACAS authors\",\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"egao\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"scientist\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702585,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"DeleteExperimentInstructions (6).pdf\",\"ignored\":false,\"lsKind\":\"gpc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_gpc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1424678400000,\"deleted\":false,\"ignored\":false,\"lsKind\":\"completion date\",\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703242,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"ms\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_ms\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeKind\":\"source\",\"codeOrigin\":\"ACAS DDICT\",\"codeType\":\"component\",\"codeTypeAndKind\":\"component_source\",\"codeValue\":\"ACAS\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"source\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_source\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729672870,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"purity\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_purity\",\"numericValue\":129,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729708062,\"unitKind\":\"% purity\",\"unitType\":\"percentage\",\"unitTypeAndKind\":\"percentage_% purity\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"ignored\":false,\"lsKind\":\"nmr\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_nmr\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"notebook\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703953,\"stringValue\":\"test\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"source id\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_source id\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702610,\"stringValue\":\"\",\"unitTypeAndKind\":\"null_null\",\"version\":0}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"version\":1}],\"lsType\": \"batch\",\"lsTypeAndKind\": \"batch_protein\",\"recordedBy\": \"jane\",\"recordedDate\": 1424730027913,\"version\": null},\"id\": null,\"ignored\": false,\"lsKind\": \"batch_parent\",\"lsType\": \"instantiates\",\"lsTypeAndKind\": \"instantiates_batch_parent\",\"recordedBy\": \"jane\",\"recordedDate\": 1424730027913,\"version\": null}],\"ignored\": false,\"lsKind\": \"protein\",\"lsLabels\": [{ \"ignored\": false,\"imageFile\": null,\"labelText\": \"Fielderase\",\"lsKind\": \"protein\",\"lsTransaction\": 9999,\"lsType\": \"name\",\"lsTypeAndKind\": \"name_protein\",\"modifiedDate\": null,\"physicallyLabled\": false,\"preferred\": true,\"recordedBy\": \"jane\",\"recordedDate\": 1375141504000}],\"lsStates\": [{ \"comments\": null,\"ignored\": false,\"lsKind\": \"protein parent\",\"lsTransaction\": 9999,\"lsType\": \"metadata\",\"lsTypeAndKind\": \"metadata_protein parent\",\"lsValues\": [{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": 1342080000000, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"completion date\", \"lsTransaction\": 9999, \"lsType\": \"dateValue\", \"lsTypeAndKind\": \"dateValue_completion date\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"notebook\", \"lsTransaction\": 9999, \"lsType\": \"stringValue\", \"lsTypeAndKind\": \"stringValue_notebook\", \"modifiedDate\": null, \"numericValue\": null, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": \"Notebook 1\", \"uncertainty\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"molecular weight\", \"lsTransaction\": 9999, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_molecular weight\", \"modifiedDate\": null, \"numericValue\": 231, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": \"g/mol\", \"unitType\": \"molecular weight\", \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null},{\"clobValue\": null, \"codeValue\": null, \"comments\": null, \"dateValue\": null, \"fileValue\": null, \"ignored\": false, \"lsKind\": \"batch number\", \"lsTransaction\": 9999, \"lsType\": \"numericValue\", \"lsTypeAndKind\": \"numericValue_batch number\", \"modifiedDate\": null, \"numericValue\": 0, \"publicData\": true, \"recordedBy\": \"jane\", \"recordedDate\": 1363388477000, \"sigFigs\": null, \"stringValue\": null, \"uncertainty\": null, \"unitKind\": null, \"unitType\": null, \"urlValue\": null, \"valueOperator\": null, \"valueUnit\": null} ],\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141460000}],\"lsTransaction\": 9999,\"lsType\": \"parent\",\"lsTypeAndKind\": \"parent_protein\",\"modifiedBy\": null,\"modifiedDate\": null,\"recordedBy\": \"jane\",\"recordedDate\": 1375141508000,\"shortDescription\": \" \"}";
		MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/lsthings/parent/protein?with=nested")
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

	@Test
	@Transactional
	public void updateParentAndBatchTest() throws Exception {
		String json = "{\"codeName\":\"PRTN000015\",\"deleted\":false,\"firstLsThings\":[{\"deleted\":false,\"firstLsThing\":{\"codeName\":\"PRTN000015-1\",\"deleted\":false,\"id\":1493852,\"ignored\":false,\"lsKind\":\"protein\",\"lsStates\":[{\"comments\":\"test comment 2\",\"deleted\":false,\"id\":2634316,\"ignored\":false,\"lsKind\":\"protein batch\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_protein batch\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"edited ms file value\",\"id\":5926370,\"ignored\":false,\"lsKind\":\"ms\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_ms\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"id\":5926365,\"ignored\":false,\"lsKind\":\"nmr\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_nmr\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"DeleteExperimentInstructions (6).pdf\",\"id\":5926369,\"ignored\":false,\"lsKind\":\"gpc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_gpc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1424678400000,\"deleted\":false,\"id\":5926368,\"ignored\":false,\"lsKind\":\"completion date\",\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703242,\"unitTypeAndKind\":\"null_null\"},{\"codeKind\":\"source\",\"codeOrigin\":\"ACAS DDICT\",\"codeType\":\"component\",\"codeTypeAndKind\":\"component_source\",\"codeValue\":\"ACAS Edited\",\"deleted\":false,\"id\":5926373,\"ignored\":false,\"lsKind\":\"source\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_source\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729672870,\"unitTypeAndKind\":\"null_null\"},{\"codeOrigin\":\"ACAS authors\",\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"egao\",\"deleted\":false,\"id\":5926367,\"ignored\":false,\"lsKind\":\"scientist\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702585,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926372,\"ignored\":false,\"lsKind\":\"purity\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_purity\",\"numericValue\":129,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729708062,\"unitKind\":\"% purity\",\"unitType\":\"percentage\",\"unitTypeAndKind\":\"percentage_% purity\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"fileValue\":\"\",\"id\":5926371,\"ignored\":false,\"lsKind\":\"hplc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_hplc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729720940,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926364,\"ignored\":false,\"lsKind\":\"notebook\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729703953,\"stringValue\":\"test\",\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926366,\"ignored\":false,\"lsKind\":\"source id\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_source id\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729702610,\"stringValue\":\"test source id\",\"unitTypeAndKind\":\"null_null\"}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940},{\"deleted\":false,\"id\":2634315,\"ignored\":false,\"lsKind\":\"inventory\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926362,\"ignored\":false,\"lsKind\":\"location\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729705505,\"stringValue\":\"123\",\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926363,\"ignored\":false,\"lsKind\":\"amount made\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount made\",\"numericValue\":1123,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"jane\",\"recordedDate\":1424729706303,\"unitKind\":\"g\",\"unitType\":\"mass\",\"unitTypeAndKind\":\"mass_g\"}],\"recordedBy\":\"jane\",\"recordedDate\":1424729720940}],\"lsType\":\"batch\",\"lsTypeAndKind\":\"batch_protein\",\"recordedBy\":\"jane\",\"recordedDate\":1424730027913},\"id\":1493853,\"ignored\":false,\"lsKind\":\"batch_parent\",\"lsType\":\"instantiates\",\"lsTypeAndKind\":\"instantiates_batch_parent\",\"recordedBy\":\"jane\",\"recordedDate\":1424730027913}],\"id\":1493851,\"ignored\":false,\"lsKind\":\"protein\",\"lsLabels\":[{\"deleted\":false,\"id\":297595,\"ignored\":false,\"labelText\":\"Fielderase Z\",\"lsKind\":\"protein\",\"lsTransaction\":9999,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_protein\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"jane\",\"recordedDate\":1375141504000}],\"lsStates\":[{\"deleted\":false,\"id\":2634314,\"ignored\":false,\"lsKind\":\"protein parent\",\"lsTransaction\":9999,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_protein parent\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926359,\"ignored\":false,\"lsKind\":\"notebook\",\"lsTransaction\":9999,\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"stringValue\":\"Notebook 1\",\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926361,\"ignored\":false,\"lsKind\":\"molecular weight\",\"lsTransaction\":9999,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_molecular weight\",\"numericValue\":890,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"unitKind\":\"g/mol\",\"unitType\":\"molecular weight\",\"unitTypeAndKind\":\"molecular weight_g/mol\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5926360,\"ignored\":false,\"lsKind\":\"batch number\",\"lsTransaction\":9999,\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_batch number\",\"numericValue\":0,\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1342080000000,\"deleted\":false,\"id\":5926358,\"ignored\":false,\"lsKind\":\"completion date\",\"lsTransaction\":9999,\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":true,\"recordedBy\":\"jane\",\"recordedDate\":1363388477000,\"unitTypeAndKind\":\"null_null\"}],\"recordedBy\":\"jane\",\"recordedDate\":1375141460000}],\"lsTags\":[],\"lsTransaction\":9999,\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_protein\",\"recordedBy\":\"jane\",\"recordedDate\":1375141508000,\"secondLsThings\":[]}";
		MockHttpServletResponse response = this.mockMvc
				.perform(put("/api/v1/lsthings/parent/protein/PRTN000015?with=nested")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse();
		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		LsThing postedLsThing = LsThing.fromJsonToLsThing(responseJson);
		logger.info(postedLsThing.toJson());
	}

	@Test
	@Rollback(value = true)
	public void updateBatchInteractions() throws Exception {
		// String json = "{\"lsType\":\"batch\",\"lsKind\":\"internalization
		// agent\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1428517111212,\"shortDescription\":\"
		// \",\"lsLabels\":[],\"lsStates\":[{\"deleted\":false,\"id\":2649027,\"ignored\":false,\"lsKind\":\"internalization
		// agent
		// batch\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_internalization
		// agent
		// batch\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960432,\"ignored\":false,\"lsKind\":\"notebook\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"stringValue\":\"test\",\"unitTypeAndKind\":\"null_null\",\"version\":0,\"value\":\"test\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960435,\"ignored\":false,\"lsKind\":\"molecular
		// weight\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_molecular
		// weight\",\"numericValue\":123,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"unitKind\":\"kDa\",\"unitType\":\"molecular
		// weight\",\"unitTypeAndKind\":\"molecular
		// weight_kDa\",\"version\":0,\"value\":123},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960433,\"ignored\":true,\"lsKind\":\"purity\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_purity\",\"numericValue\":12,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"unitKind\":\"%
		// purity\",\"unitType\":\"percentage\",\"unitTypeAndKind\":\"percentage_%
		// purity\",\"version\":0,\"value\":121},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1428476400000,\"deleted\":false,\"id\":5960434,\"ignored\":false,\"lsKind\":\"completion
		// date\",\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion
		// date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"unitTypeAndKind\":\"null_null\",\"version\":0,\"value\":1428476400000},{\"codeOrigin\":\"ACAS
		// authors\",\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"egao\",\"deleted\":false,\"id\":5960438,\"ignored\":false,\"lsKind\":\"scientist\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"unitTypeAndKind\":\"null_null\",\"version\":0,\"value\":\"egao\"},{\"codeTypeAndKind\":\"null_null\",\"comments\":\"Testing
		// (46).pdf\",\"deleted\":false,\"fileValue\":\"entities/internalizationAgents/I000012-6/Testing
		// (46).pdf\",\"id\":5960439,\"ignored\":false,\"lsKind\":\"gpc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_gpc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"unitTypeAndKind\":\"null_null\",\"version\":1},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960436,\"ignored\":false,\"lsKind\":\"source
		// id\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_source
		// id\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"stringValue\":\"\",\"unitTypeAndKind\":\"null_null\",\"version\":0,\"value\":\"\"},{\"codeKind\":\"source\",\"codeOrigin\":\"ACAS
		// DDICT\",\"codeType\":\"component\",\"codeTypeAndKind\":\"component_source\",\"codeValue\":\"ACAS\",\"deleted\":false,\"id\":5960437,\"ignored\":false,\"lsKind\":\"source\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_source\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"unitTypeAndKind\":\"null_null\",\"version\":0,\"value\":\"ACAS\"},{\"lsType\":\"numericValue\",\"lsKind\":\"purity\",\"ignored\":false,\"recordedDate\":1428517111212,\"recordedBy\":\"bob\",\"numericValue\":121,\"value\":121}],\"modifiedDate\":1428517031912,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"version\":2},{\"deleted\":false,\"id\":2649026,\"ignored\":false,\"lsKind\":\"inventory\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960430,\"ignored\":false,\"lsKind\":\"location\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"stringValue\":\"13\",\"unitTypeAndKind\":\"null_null\",\"version\":0,\"value\":\"13\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960431,\"ignored\":false,\"lsKind\":\"amount
		// made\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount
		// made\",\"numericValue\":23,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"unitKind\":\"g\",\"unitType\":\"mass\",\"unitTypeAndKind\":\"mass_g\",\"version\":0,\"value\":23}],\"modifiedDate\":1428517031934,\"recordedBy\":\"bob\",\"recordedDate\":1428517031603,\"version\":2}],\"codeName\":\"I000012-6\",\"deleted\":false,\"id\":1508706,\"ignored\":false,\"lsTags\":[],\"lsTypeAndKind\":\"batch_internalization
		// agent\",\"modifiedDate\":1428517031909,\"version\":3,\"cid\":\"c346\",\"_changing\":false,\"_previousAttributes\":{\"lsType\":\"thing\",\"lsKind\":\"thing\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1428517037610,\"shortDescription\":\"
		// \",\"lsLabels\":[],\"lsStates\":[],\"firstLsThings\":[]},\"changed\":{\"secondLsThings\":[]},\"_pending\":false,\"urlRoot\":\"/api/things/batch/internalization
		// agent\",\"lsProperties\":{\"defaultLabels\":[],\"defaultValues\":[{\"key\":\"scientist\",\"stateType\":\"metadata\",\"stateKind\":\"internalization
		// agent
		// batch\",\"type\":\"codeValue\",\"kind\":\"scientist\",\"codeOrigin\":\"ACAS
		// authors\",\"value\":\"unassigned\"},{\"key\":\"completion
		// date\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent
		// batch\",\"type\":\"dateValue\",\"kind\":\"completion
		// date\"},{\"key\":\"notebook\",\"stateType\":\"metadata\",\"stateKind\":\"internalization
		// agent
		// batch\",\"type\":\"stringValue\",\"kind\":\"notebook\"},{\"key\":\"source\",\"stateType\":\"metadata\",\"stateKind\":\"internalization
		// agent
		// batch\",\"type\":\"codeValue\",\"kind\":\"source\",\"value\":\"ACAS\",\"codeType\":\"component\",\"codeKind\":\"source\",\"codeOrigin\":\"ACAS
		// DDICT\"},{\"key\":\"source
		// id\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent
		// batch\",\"type\":\"stringValue\",\"kind\":\"source id\"},{\"key\":\"molecular
		// weight\",\"stateType\":\"metadata\",\"stateKind\":\"internalization agent
		// batch\",\"type\":\"numericValue\",\"kind\":\"molecular
		// weight\",\"unitType\":\"molecular
		// weight\",\"unitKind\":\"kDa\"},{\"key\":\"purity\",\"stateType\":\"metadata\",\"stateKind\":\"internalization
		// agent
		// batch\",\"type\":\"numericValue\",\"kind\":\"purity\",\"unitType\":\"percentage\",\"unitKind\":\"%
		// purity\"},{\"key\":\"amount
		// made\",\"stateType\":\"metadata\",\"stateKind\":\"inventory\",\"type\":\"numericValue\",\"kind\":\"amount
		// made\",\"unitType\":\"mass\",\"unitKind\":\"g\"},{\"key\":\"location\",\"stateType\":\"metadata\",\"stateKind\":\"inventory\",\"type\":\"stringValue\",\"kind\":\"location\"}],\"defaultFirstLsThingItx\":[],\"defaultSecondLsThingItx\":[]},\"className\":\"Thing\",\"validationError\":null,\"idAttribute\":\"id\",\"firstLsThings\":[],\"secondLsThings\":[{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"composition\",\"lsValues\":[{\"lsType\":\"numericValue\",\"lsKind\":\"order\",\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\",\"numericValue\":1}],\"ignored\":false,\"recordedDate\":null,\"recordedBy\":\"\"}],\"recordedBy\":\"bob\",\"recordedDate\":1428517111206,\"secondLsThing\":{\"codeName\":\"PEG000010-2\",\"deleted\":false,\"id\":1508673,\"ignored\":false,\"lsKind\":\"peg\",\"lsLabels\":[],\"lsStates\":[{\"deleted\":false,\"id\":2648998,\"ignored\":false,\"lsKind\":\"inventory\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960363,\"ignored\":false,\"lsKind\":\"location\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"stringValue\":\"lab23\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960364,\"ignored\":false,\"lsKind\":\"amount
		// made\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount
		// made\",\"numericValue\":123,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"unitKind\":\"g\",\"unitType\":\"mass\",\"unitTypeAndKind\":\"mass_g\",\"version\":0}],\"modifiedDate\":1428516368847,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"version\":7},{\"deleted\":false,\"id\":2648997,\"ignored\":false,\"lsKind\":\"peg
		// batch\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_peg
		// batch\",\"lsValues\":[{\"codeOrigin\":\"ACAS
		// authors\",\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"egao\",\"deleted\":false,\"id\":5960362,\"ignored\":false,\"lsKind\":\"scientist\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960361,\"ignored\":false,\"lsKind\":\"purity\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_purity\",\"numericValue\":123,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"unitKind\":\"%
		// purity\",\"unitType\":\"percentage\",\"unitTypeAndKind\":\"percentage_%
		// purity\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1428390000000,\"deleted\":false,\"id\":5960360,\"ignored\":false,\"lsKind\":\"completion
		// date\",\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion
		// date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960358,\"ignored\":false,\"lsKind\":\"notebook\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"stringValue\":\"test\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960357,\"ignored\":false,\"lsKind\":\"source
		// id\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_source
		// id\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"stringValue\":\"\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeKind\":\"source\",\"codeOrigin\":\"ACAS
		// DDICT\",\"codeType\":\"component\",\"codeTypeAndKind\":\"component_source\",\"codeValue\":\"ACAS\",\"deleted\":false,\"id\":5960359,\"ignored\":false,\"lsKind\":\"source\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_source\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeTypeAndKind\":\"null_null\",\"comments\":\"Testing
		// (45).pdf\",\"deleted\":false,\"fileValue\":\"entities/pegs/PEG000010-2/Testing
		// (45).pdf\",\"id\":5960356,\"ignored\":false,\"lsKind\":\"gpc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_gpc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"unitTypeAndKind\":\"null_null\",\"version\":1}],\"modifiedDate\":1428516368853,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"version\":7}],\"lsTags\":[],\"lsType\":\"batch\",\"lsTypeAndKind\":\"batch_peg\",\"modifiedDate\":1428516368844,\"recordedBy\":\"bob\",\"recordedDate\":1428447081473,\"version\":8},\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"},{\"lsType\":\"incorporates\",\"lsKind\":\"assembly_component\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"lsStates\":[{\"deleted\":false,\"id\":2649029,\"ignored\":false,\"lsKind\":\"composition\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_composition\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960441,\"ignored\":false,\"lsKind\":\"order\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_order\",\"numericValue\":1,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"\",\"recordedDate\":1428517031733,\"unitTypeAndKind\":\"null_null\",\"version\":0}],\"recordedBy\":\"\",\"recordedDate\":1428517031732,\"version\":1}],\"recordedBy\":\"bob\",\"recordedDate\":1428517031598,\"secondLsThing\":{\"codeName\":\"PEG000010-1\",\"deleted\":false,\"id\":1442921,\"ignored\":false,\"lsKind\":\"peg\",\"lsType\":\"batch\",\"lsTypeAndKind\":\"batch_peg\",\"modifiedDate\":1428516368872,\"recordedBy\":\"bob\",\"recordedDate\":1426100366796,\"version\":14},\"deleted\":false,\"id\":1508708,\"ignored\":true,\"version\":1,\"className\":\"SecondThingItx\",\"validationError\":null,\"idAttribute\":\"id\"}]}";
		String json = "{\"codeName\":\"I000015-4\",\"deleted\":false,\"firstLsThings\":[],\"id\":1508776,\"ignored\":false,\"lsKind\":\"internalization agent\",\"lsLabels\":[],\"lsStates\":[{\"deleted\":false,\"id\":2649094,\"ignored\":false,\"lsKind\":\"internalization agent batch\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_internalization agent batch\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1428476400000,\"deleted\":false,\"id\":5960622,\"ignored\":false,\"lsKind\":\"completion date\",\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428526043830,\"unitTypeAndKind\":\"null_null\"},{\"codeOrigin\":\"ACAS authors\",\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"egao\",\"deleted\":false,\"id\":5960620,\"ignored\":false,\"lsKind\":\"scientist\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_scientist\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428526043830,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960625,\"ignored\":false,\"lsKind\":\"purity\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_purity\",\"numericValue\":132,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428526043830,\"unitKind\":\"% purity\",\"unitType\":\"percentage\",\"unitTypeAndKind\":\"percentage_% purity\"},{\"codeKind\":\"source\",\"codeOrigin\":\"ACAS DDICT\",\"codeType\":\"component\",\"codeTypeAndKind\":\"component_source\",\"codeValue\":\"ACAS\",\"deleted\":false,\"id\":5960623,\"ignored\":false,\"lsKind\":\"source\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_source\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428526043830,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960618,\"ignored\":false,\"lsKind\":\"molecular weight\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_molecular weight\",\"numericValue\":123,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428526043830,\"unitKind\":\"kDa\",\"unitType\":\"molecular weight\",\"unitTypeAndKind\":\"molecular weight_kDa\"},{\"codeTypeAndKind\":\"null_null\",\"comments\":\"Testing (46).pdf\",\"deleted\":false,\"fileValue\":\"entities/internalizationAgents/I000015-4/Testing (46).pdf\",\"id\":5960624,\"ignored\":false,\"lsKind\":\"gpc\",\"lsType\":\"fileValue\",\"lsTypeAndKind\":\"fileValue_gpc\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428526043830,\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960619,\"ignored\":false,\"lsKind\":\"notebook\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428526043830,\"stringValue\":\"test\",\"unitTypeAndKind\":\"null_null\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960621,\"ignored\":false,\"lsKind\":\"source id\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_source id\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428526043830,\"stringValue\":\"\",\"unitTypeAndKind\":\"null_null\"}],\"modifiedDate\":1428526315002,\"recordedBy\":\"bob\",\"recordedDate\":1428526043830},{\"deleted\":false,\"id\":2649093,\"ignored\":false,\"lsKind\":\"inventory\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_inventory\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960617,\"ignored\":false,\"lsKind\":\"amount made\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_amount made\",\"numericValue\":132,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428526043830,\"unitKind\":\"g\",\"unitType\":\"mass\",\"unitTypeAndKind\":\"mass_g\"},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960616,\"ignored\":false,\"lsKind\":\"location\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_location\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1428526043830,\"stringValue\":\"lab\",\"unitTypeAndKind\":\"null_null\"}],\"modifiedDate\":1428526314953,\"recordedBy\":\"bob\",\"recordedDate\":1428526043830}],\"lsTags\":[],\"lsType\":\"batch\",\"lsTypeAndKind\":\"batch_internalization agent\",\"modifiedDate\":1428526314898,\"recordedBy\":\"bob\",\"recordedDate\":1428526315031,\"secondLsThings\":[{\"deleted\":false,\"id\":1508779,\"ignored\":false,\"lsKind\":\"batch_parent\",\"lsStates\":[],\"lsType\":\"instantiates\",\"lsTypeAndKind\":\"instantiates_batch_parent\",\"recordedBy\":\"bob\",\"recordedDate\":1428526043830,\"secondLsThing\":{\"codeName\":\"I000015\",\"deleted\":false,\"id\":1508754,\"ignored\":false,\"lsKind\":\"internalization agent\",\"lsType\":\"parent\",\"lsTypeAndKind\":\"parent_internalization agent\",\"modifiedDate\":1428524476945,\"recordedBy\":\"bob\",\"recordedDate\":1428524477527}},{\"deleted\":false,\"id\":1508780,\"ignored\":false,\"lsKind\":\"assembly_component\",\"lsStates\":[{\"deleted\":false,\"id\":2649097,\"ignored\":false,\"lsKind\":\"composition\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_composition\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":5960628,\"ignored\":false,\"lsKind\":\"order\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_order\",\"numericValue\":2.000000000000000000,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"\",\"recordedDate\":1428526315511,\"unitTypeAndKind\":\"null_null\"}],\"recordedBy\":\"\",\"recordedDate\":1428526315509}],\"lsType\":\"incorporates\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"recordedBy\":\"bob\",\"recordedDate\":1428526315025,\"secondLsThing\":{\"codeName\":\"PEG000010-2\",\"deleted\":false,\"id\":1508673,\"ignored\":true,\"lsKind\":\"peg\",\"lsType\":\"batch\",\"lsTypeAndKind\":\"batch_peg\",\"modifiedDate\":1428502760820,\"recordedBy\":\"bob\",\"recordedDate\":1428421881473}},{\"deleted\":false,\"id\":1508778,\"ignored\":true,\"lsKind\":\"assembly_component\",\"lsStates\":[{\"deleted\":false,\"ignored\":false,\"lsKind\":\"composition\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_composition\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"order\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_order\",\"numericValue\":2.000000000000000000,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"\",\"recordedDate\":1428526315511,\"unitTypeAndKind\":\"null_null\"}],\"recordedBy\":\"\",\"recordedDate\":1428526315509}],\"lsType\":\"incorporates\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"modifiedDate\":1428526315175,\"recordedBy\":\"bob\",\"recordedDate\":1428526043829,\"secondLsThing\":{\"codeName\":\"PEG000010-1\",\"deleted\":false,\"id\":1442921,\"ignored\":false,\"lsKind\":\"peg\",\"lsType\":\"batch\",\"lsTypeAndKind\":\"batch_peg\",\"modifiedDate\":1428502763554,\"recordedBy\":\"bob\",\"recordedDate\":1426075166796}},{\"deleted\":false,\"id\":1508777,\"ignored\":false,\"lsKind\":\"assembly_component\",\"lsStates\":[{\"deleted\":false,\"ignored\":false,\"lsKind\":\"composition\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_composition\",\"lsValues\":[{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"ignored\":false,\"lsKind\":\"order\",\"lsType\":\"numericValue\",\"lsTypeAndKind\":\"numericValue_order\",\"numericValue\":1.000000000000000000,\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"\",\"recordedDate\":1428526315511,\"unitTypeAndKind\":\"null_null\"}],\"recordedBy\":\"\",\"recordedDate\":1428526315509}],\"lsType\":\"incorporates\",\"lsTypeAndKind\":\"incorporates_assembly_component\",\"modifiedDate\":1428526315362,\"recordedBy\":\"bob\",\"recordedDate\":1428526043827,\"secondLsThing\":{\"codeName\":\"CB000078-1\",\"deleted\":false,\"id\":1386395,\"ignored\":false,\"lsKind\":\"cationic block\",\"lsType\":\"batch\",\"lsTypeAndKind\":\"batch_cationic block\",\"modifiedDate\":1428526397033,\"recordedBy\":\"bob\",\"recordedDate\":1424799163633}}]}";
		MockHttpServletResponse response = this.mockMvc
				.perform(put("/api/v1/lsthings/batch/internalization agent/I000015-4?with=nestedfull")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				// .andExpect(content().contentType("application/json"))
				.andReturn().getResponse();
		String responseJson = response.getContentAsString();
		logger.info(responseJson);
		LsThing updatedLsThing = LsThing.fromJsonToLsThing(responseJson);
		logger.info(updatedLsThing.toJsonWithNestedFull());
		Assert.assertEquals(2, updatedLsThing.getSecondLsThings().size());
		Assert.assertEquals(1, updatedLsThing.getSecondLsThings().iterator().next().getLsStates().size());
	}

	@Test
	@Transactional
	public void documentManagerSearchTest1() throws Exception {
		String searchTerms = ""
				+ "documentType=MTA"
				+ "&documentCode=MTA"
				+ "&titleContains=test"
				+ "&amountFrom=0"
				+ "&amountTo=190"
				+ "&createdDateFrom=1439704800000"
				+ "&createdDateTo=1440223200000"
				+ "&active=true"
				+ "&termType=TTYP-00000002"
				+ "&daysBeforeTerm=1"
				+ "&termDateFrom=1430309600000"
				+ "&termDateTo=1440828000000"
				+ "&nonSolicit=no"
				+ "&nonTransfer=yes"
				// + "&restrictedMaterialsContains=restricted"
				+ "&project=PROJ-00000002"
				+ "&company=COMP-00000002"
				+ "&owner=OWNR-00000002"
				+ "&termType=TTYP-00000002"
				+ "&with=nestedFull"
				+ "";
		logger.info("GET on url: /api/v1/lsthings/documentmanagersearch?" + searchTerms);
		String json = this.mockMvc.perform(get("/api/v1/lsthings/documentmanagersearch?" + searchTerms)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse().getContentAsString();

		logger.info(json);
		Collection<LsThing> lsThings = LsThing.fromJsonArrayToLsThings(json);
		Assert.assertEquals(1, lsThings.size());
		logger.info(LsThing.toJsonArray(lsThings));
	}

	@Test
	public void getComponentBatchesAndCheckOrder() throws Exception {
		String parentCodeName = "CB000020";
		String json = this.mockMvc
				.perform(get("/api/v1/lsthings/parent/linker small molecule/getbatches/" + parentCodeName)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse().getContentAsString();

		logger.info(json);
		Collection<LsThing> lsThings = LsThing.fromJsonArrayToLsThings(json);
		int batchNo = 1;
		for (LsThing lsThing : lsThings) {
			logger.info(lsThing.getCodeName());
			Assert.assertEquals(parentCodeName + "-" + batchNo, lsThing.getCodeName());
			batchNo++;
		}
	}

	@Test
	public void deleteLsThing() throws Exception {
		String parentCodeName = "PROT000004";
		String json = this.mockMvc
				.perform(MockMvcRequestBuilders.delete("/api/v1/lsthings/default/default/" + parentCodeName)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		Assert.assertEquals("", json);
	}

	@Test
	public void deleteLsThingFail() throws Exception {
		String parentCodeName = "alkdsfngakjdsgn23894273429";
		String json = this.mockMvc
				.perform(MockMvcRequestBuilders.delete("/api/v1/lsthings/default/default/" + parentCodeName)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn().getResponse().getContentAsString();
		Collection<ErrorMessage> errors = ErrorMessage.fromJsonArrayToErrorMessages(json);
		Assert.assertEquals(1, errors.size());
	}

	@Transactional
	@Test
	public void checkBatchDependencies() throws Exception {
		String codeName = "PEG000003-1";
		String json = this.mockMvc.perform(get("/api/v1/lsthings/batch/peg/checkDependencies/" + codeName)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse().getContentAsString();

		DependencyCheckDTO result = DependencyCheckDTO.fromJsonToDependencyCheckDTO(json);
		logger.info(result.toJson());
		Assert.assertTrue(result.getLinkedDataExists());
	}

	@Transactional
	@Test
	public void checkParentDependencies() throws Exception {
		String codeName = "PEG000003";
		String json = this.mockMvc.perform(get("/api/v1/lsthings/parent/peg/checkDependencies/" + codeName)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse().getContentAsString();

		DependencyCheckDTO result = DependencyCheckDTO.fromJsonToDependencyCheckDTO(json);
		logger.info(result.toJson());
		Assert.assertTrue(result.getLinkedDataExists());
	}

	@Transactional
	@Test
	public void deleteBatch() throws Exception {
		String codeName = "CB000004-6";
		String json = this.mockMvc
				.perform(MockMvcRequestBuilders.delete("/api/v1/lsthings/batch/cationic block/deleteBatch/" + codeName)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse().getContentAsString();
	}

	@Transactional
	@Test
	public void deleteBatchError() throws Exception {
		String codeName = "CB000004-6";
		String json = this.mockMvc
				.perform(MockMvcRequestBuilders.delete("/api/v1/lsthings/parent/cationic block/deleteBatch/" + codeName)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse().getContentAsString();
		logger.info(json);
	}

	@Transactional
	@Test
	public void deleteParent() throws Exception {
		String codeName = "CB000004";
		String json = this.mockMvc
				.perform(
						MockMvcRequestBuilders.delete("/api/v1/lsthings/parent/cationic block/deleteParent/" + codeName)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse().getContentAsString();
	}

	@Transactional
	@Test
	public void deleteParentError() throws Exception {
		String codeName = "CB000004-6";
		String json = this.mockMvc
				.perform(MockMvcRequestBuilders.delete("/api/v1/lsthings/batch/cationic block/deleteParent/" + codeName)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse().getContentAsString();
		logger.info(json);
	}

	@Test
	@Transactional
	public void structureSearch() throws Exception {
		String queryMol = "\n  Mrv1641110051619032D          \n\n  5  5  0  0  0  0            999 V2000\n   -0.0446    0.6125    0.0000 O   0  0  0  0  0  0  0  0  0  0  0  0\n   -0.7121    0.1274    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n   -0.4572   -0.6572    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n    0.3679   -0.6572    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n    0.6228    0.1274    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n  2  3  1  0  0  0  0\n  3  4  1  0  0  0  0\n  4  5  1  0  0  0  0\n  1  2  1  0  0  0  0\n  1  5  1  0  0  0  0\nM  END\n";
		StructureSearchDTO query = new StructureSearchDTO(queryMol, "", "", "SUBSTRUCTURE", 10, null);
		String json = query.toJson();
		MockHttpServletResponse response = this.mockMvc.perform(post("/api/v1/lsthings/structureSearch")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				// .andExpect(content().contentType("application/json"))
				.andReturn().getResponse();
		String responseJson = response.getContentAsString();
		logger.debug(responseJson);
		Collection<LsThing> searchResults = LsThing.fromJsonArrayToLsThings(responseJson);
		Assert.assertFalse(searchResults.isEmpty());
		logger.debug(LsThing.toJsonArray(searchResults));
	}

	@Test
	public void searchReturnWithFirstLsThings() throws Exception {
		String json = this.mockMvc.perform(get("/api/v1/lsthings/search?lsType=batch&with=firstLsThings&q=LSMB000005")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse().getContentAsString();

		logger.info(json);
		Collection<LsThing> lsThings = LsThing.fromJsonArrayToLsThings(json);
		for (LsThing lsThing : lsThings) {
			Assert.assertNotNull(lsThing.getFirstLsThings());
			Assert.assertTrue(!lsThing.getFirstLsThings().isEmpty());
			for (ItxLsThingLsThing firstItx : lsThing.getFirstLsThings()) {
				Assert.assertNotNull(firstItx.getFirstLsThing());
			}
		}
	}

	@Test
	public void searchReturnWithSecondLsThings() throws Exception {
		String json = this.mockMvc.perform(get("/api/v1/lsthings/search?lsType=batch&with=secondLsThings&q=LSMB000005")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andReturn().getResponse().getContentAsString();

		logger.info(json);
		Collection<LsThing> lsThings = LsThing.fromJsonArrayToLsThings(json);
		for (LsThing lsThing : lsThings) {
			Assert.assertNotNull(lsThing.getSecondLsThings());
			Assert.assertTrue(!lsThing.getSecondLsThings().isEmpty());
			for (ItxLsThingLsThing secondItx : lsThing.getSecondLsThings()) {
				Assert.assertNotNull(secondItx.getSecondLsThing());
			}
		}
	}

}