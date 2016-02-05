

package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerLabel;
import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ContainerValue;
import com.labsynch.labseer.domain.InteractionKind;
import com.labsynch.labseer.domain.InteractionType;
import com.labsynch.labseer.domain.LabelKind;
import com.labsynch.labseer.domain.LabelType;
import com.labsynch.labseer.domain.ItxContainerContainer;
import com.labsynch.labseer.domain.ItxContainerContainerState;
import com.labsynch.labseer.domain.ItxContainerContainerValue;
import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.ItxLsThingLsThingState;
import com.labsynch.labseer.domain.ItxLsThingLsThingValue;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.dto.CodeLabelDTO;
import com.labsynch.labseer.dto.ContainerRequestDTO;
import com.labsynch.labseer.dto.ContainerErrorMessageDTO;
import com.labsynch.labseer.dto.ContainerLocationDTO;
import com.labsynch.labseer.dto.ContainerMiniDTO;
import com.labsynch.labseer.dto.ContainerStateMiniDTO;
import com.labsynch.labseer.dto.CreatePlateRequestDTO;
import com.labsynch.labseer.dto.PlateStubDTO;
import com.labsynch.labseer.dto.PlateWellDTO;
import com.labsynch.labseer.dto.PreferredNameDTO;
import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.dto.WellContentDTO;
import com.labsynch.labseer.dto.WellStubDTO;
import com.labsynch.labseer.exceptions.ErrorMessage;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

import flexjson.JSONTokener;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class ContainerLSServiceTests {

	private static final Logger logger = LoggerFactory.getLogger(ContainerLSServiceTests.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
	@Autowired
	private ContainerStateService csService;

	@Autowired
	private ContainerService containerService;
	
	@Autowired
	private AutoLabelService autoLabelService;
	
	//@Test
	//@Transactional
	public void SaveContainerValues_3() throws FileNotFoundException{

		String fileName = "/tmp/roo/containerValuesBigJson.json";

		long startTime = System.currentTimeMillis();

		int batchSize = propertiesUtilService.getBatchSize();
		int i = 0;		
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		JSONTokener jsonTokens = new JSONTokener(br);		
		Object token;
		char delimiter;
		char END_OF_ARRAY = ']';
		while (jsonTokens.more()){
			delimiter = jsonTokens.nextClean();
			if (delimiter != END_OF_ARRAY){
				token = jsonTokens.nextValue();
				ContainerValue containerValue = ContainerValue.fromJsonToContainerValue(token.toString());
				containerValue.setId(null);
				containerValue.persist();
				if ( i % batchSize == 0 ) { 
					containerValue.flush();
					containerValue.clear();
				}
				i++;
			}
	    }

		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		logger.info("total elapsed time = " + totalTime + " miliseconds.");

	}

	//@Test
	@Transactional
	public void SaveContainerStates_2(){
		Container container = new Container();
		container.setRecordedBy("me");
		container.setRecordedDate(new Date());
		container.setLsType("well");
		container.setLsKind("plate well");
		container.persist();
		logger.info(container.toJson());

		Collection<ContainerState> savedContainerStates = new ArrayList<ContainerState>();

		int counter = 0;
		while (counter < 30000){
			ContainerState cs = new ContainerState();
			cs.setRecordedBy("me");
			cs.setRecordedDate(new Date());
			cs.setContainer(container);
			cs.setLsType("metadata");
			cs.setLsKind("size");
			cs.setComments("---");
			cs.persist();
			savedContainerStates.add(cs);
			counter++;
		}

		long startTime = System.currentTimeMillis();
		String savedJson = ContainerState.toJsonArray(savedContainerStates);

		int batchSize = 50;
		int i = 0;
		Collection<ContainerStateMiniDTO> savedContainerStatesDTO = new ArrayList<ContainerStateMiniDTO>();
		//Collection<ContainerState> savedContainerStatesDTO = new ArrayList<ContainerState>();

		//savedJson = savedJson.replaceAll("\"\"", "null");
		//logger.info(savedJson);
		JSONTokener jsonTokens = new JSONTokener(savedJson);		
		Object token;
		char delimiter;
		char END_OF_ARRAY = ']';
		while (jsonTokens.more()){
			delimiter = jsonTokens.nextClean();
			if (delimiter != END_OF_ARRAY){
				token = jsonTokens.nextValue();
				ContainerState containerState = ContainerState.fromJsonToContainerState(token.toString());
				containerState.setId(null);
				containerState.persist();
				ContainerStateMiniDTO stateDTO = new ContainerStateMiniDTO(containerState);
				stateDTO.setId(containerState.getId());
				stateDTO.setVersion(containerState.getVersion());
				//				ContainerMiniDTO containerDTO = new ContainerMiniDTO(Container.findContainer(containerState.getContainer().getId()));
				ContainerMiniDTO containerDTO = new ContainerMiniDTO(containerState.getContainer());
				stateDTO.setContainer(containerDTO);
				savedContainerStatesDTO.add(stateDTO);
				//				savedContainerStatesDTO.add(containerState);
				if ( i % batchSize == 0 ) { 
					containerState.flush();
					containerState.clear();
				}
				i++;
			}
		}	

		String output = ContainerStateMiniDTO.toJsonArray(savedContainerStatesDTO);
		logger.info(output);

		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;

		logger.info("total elapsed time = " + totalTime + " miliseconds.");



	}

	//@Test
	//@Transactional
	public void CreateMetaContainerTest_1() throws IOException{
		Container container = new Container();
		container.setRecordedBy("me");
		container.setRecordedDate(new Date());
		container.setLsType("well");
		container.setLsKind("plate well");
		container.persist();
		logger.info(container.toJson());

		ContainerState cs = new ContainerState();
		cs.setRecordedBy("me");
		cs.setRecordedDate(new Date());
		cs.setContainer(container);
		cs.setLsType("metadata");
		cs.setLsKind("size");
		cs.persist();
		logger.info(cs.toJson());

		long startTime = System.currentTimeMillis();

		Collection<ContainerValue> savedContainerValues = new ArrayList<ContainerValue>();

		//		int batchSize = propertiesUtilService.getBatchSize();
		int batchSize = 50;
		int i = 0;
		int numberOfValues = 30000;
		while (i < numberOfValues){
			ContainerValue cv = new ContainerValue();
			cv.setRecordedBy("me");
			cv.setLsType("data");
			cv.setLsKind("stringValue");
			cv.setStringValue("blah data");
			cv.setLsState(cs);
			cv.persist();
			savedContainerValues.add(cv);
			if ( i % batchSize == 0 ) { // same as the JDBC batch size
				cv.flush();
				cv.clear();
			}
			i++;

		}

		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;

		logger.info("total elapsed time = " + totalTime + " miliseconds. Saved " + i + "  container values");

		long startTime2 = System.currentTimeMillis();

		String bigJsonString = ContainerValue.toJsonArray(savedContainerValues);
		logger.info("size of big string: " + bigJsonString.length());
//		Collection<ContainerValue> ack = ContainerValue.fromJsonArrayToContainerValues(bigJsonString);

//		logger.info("size of json array " + ack.size());
		long endTime2 = System.currentTimeMillis();
		long totalTime2 = endTime2 - startTime2;
		logger.info("total elapsed time2 = " + totalTime2 + " miliseconds. ");

		//logger.info(bigJsonString);

		bigJsonString = bigJsonString.replaceAll("\"\"", "null");

		//write file

		File file = new File("/tmp/roo/containerValuesBigJson.json");

		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(bigJsonString);
		bw.close();

		//		JSONTokener eek = new JSONTokener(bigJsonString);		
		//		Object token;
		//		char t1;
		//		char END_OF_ARRAY = ']';
		//		while (eek.more()){
		//		//		while ((token = eek.next()) != null){
		//			t1 = eek.next();
		//			if (t1 != END_OF_ARRAY){
		//				token = eek.nextValue();
		//				logger.info("token is " + token.toString());				
		//				ContainerValue ick = ContainerValue.fromJsonToContainerValue(token.toString());
		//				logger.info(ick.toJson());
		//			}
		//			
		////		    switch (token) {
		////	        case START_OBJECT:
		////	            JsonNode node = jp.readValueAsTree();
		////	            // ... do something with the object
		////	            System.out.println("Read object: " + node.toString());
		////	            break;
		//	    }




		//		long startTime2 = System.currentTimeMillis();
		//		
		////		int batchSize = propertiesUtilService.getBatchSize();
		//		batchSize = 50;
		//		i = 0;
		//		int fetchSize = 500;
		//		ContainerState updatedCs = ContainerState.findContainerState(cs.getId());
		////		ContainerState updatedCs = ContainerState.findContainerState(48L);
		////		long totalCount = ContainerValue.countContainerValuesByLsState(updatedCs);
		//		while (ContainerValue.countValidContainerValuesByLsState(updatedCs) > 0){
		////			List<ContainerValue> containerValues = ContainerValue.findValidContainerValuesByLsState(updatedCs, fetchSize);
		//			for ( ContainerValue containerValue : ContainerValue.findValidContainerValuesByLsState(updatedCs, fetchSize)){
		//				containerValue.setIgnored(true);
		//				containerValue.setModifiedDate(new Date());
		//				containerValue.merge();
		////				ContainerValue.update(containerValue);
		//				if ( i % batchSize == 0 ) { // same as the JDBC batch size
		//					containerValue.flush();
		//					containerValue.clear();
		//				}
		//				i++;
		//				
		//				logger.info("current i: " + i);
		//			}
		////			containerValues = null;
		//			
		//		}
		//
		//		long endTime2 = System.currentTimeMillis();
		//		long totalTime2 = endTime2 - startTime2;
		//
		//		logger.info("total elapsed time = " + totalTime2 + " miliseconds. Updated " + i + "  container values");


		//		logger.info(container.toJson());
		//		logger.info(cs.toJson());
		//		logger.info(cv.toJson());

		//        Assert.assertNotNull("Find method for 'Protocol' illegally returned null for id '" + id + "'", id);
		//        Assert.assertEquals("Find method for 'Author' returned the incorrect identifier", expectedId, id);		

		// 30,000 values = 10.301 seconds

	}

	@Test
	@Transactional
	public void MarkStatesToIgnore_Test1() throws Exception{

		String json = "[{\"id\":8093}]";
		String lsKind = "results";
		LsTransaction uplogTransaction= csService.ignoreByContainer(json, lsKind);
		logger.info(uplogTransaction.toJson());
		
	}
	
	public Container createTransientContainerStack(){
		Container container1 = new Container();
		container1.setCodeName("CONT-01");
		container1.setLsType("container");
		container1.setLsKind("test container");
		container1.setRecordedBy("bfielder");
		container1.setRecordedDate(new Date());
		container1.setDeleted(false);
		container1.setIgnored(false);
		
		Container container2 = new Container();
		container2.setCodeName("CONT-02");
		container2.setLsType("container");
		container2.setLsKind("test container");
		container2.setRecordedBy("bfielder");
		container2.setRecordedDate(new Date());
		container2.setDeleted(false);
		container2.setIgnored(false);
		
		ContainerState state1 = new ContainerState();
		state1.setLsType("test");
		state1.setLsKind("test");
		state1.setRecordedBy("bfielder");
		state1.setRecordedDate(new Date());
		state1.setDeleted(false);
		state1.setIgnored(false);
		state1.setContainer(container1);
		
		ContainerValue value1 = new ContainerValue();
		value1.setLsType("test");
		value1.setLsKind("test");
		value1.setRecordedBy("bfielder");
		value1.setRecordedDate(new Date());
		value1.setDeleted(false);
		value1.setIgnored(false);
		value1.setLsState(state1);
		
		Set<ContainerValue> values = new HashSet<ContainerValue>();
		values.add(value1);
		Set<ContainerState> states = new HashSet<ContainerState>();
		states.add(state1);
		state1.setLsValues(values);
		container1.setLsStates(states);
		
		ItxContainerContainer inc12 = makeItxContainerContainer("ITXCONT-01", "incorporates", "container_container", container1, container2);
		container1.getSecondContainers().add(inc12);
		
		return container1;
		
	}
	
	public ItxContainerContainer makeItxContainerContainer(String codeName, String lsType, String lsKind, Container firstContainer, Container secondContainer){
		ItxContainerContainer itx = new ItxContainerContainer();
		itx.setCodeName(codeName);
		itx.setLsType(lsType);
		itx.setLsKind(lsKind);
		itx.setRecordedBy("bfielder");
		itx.setRecordedDate(new Date());
		itx.setFirstContainer(firstContainer);
		itx.setSecondContainer(secondContainer);
		ItxContainerContainerState itxState = new ItxContainerContainerState();
		itxState.setLsType("metadata");
		itxState.setLsKind("composition");
		itxState.setItxContainerContainer(itx);
		itxState.setRecordedBy("bfielder");
		itxState.setRecordedDate(new Date());
		ItxContainerContainerValue itxValue = new ItxContainerContainerValue();
		itxValue.setLsType("numericValue");
		itxValue.setLsKind("order");
		itxValue.setRecordedBy("bfielder");
		itxValue.setRecordedDate(new Date());
		itxValue.setNumericValue(new BigDecimal(1));
		itxValue.setLsState(itxState);
		ItxContainerContainerValue itxValue2 = new ItxContainerContainerValue();
		itxValue2.setLsType("numericValue");
		itxValue2.setLsKind("pegylated");
		itxValue2.setRecordedBy("bfielder");
		itxValue2.setRecordedDate(new Date());
		itxValue2.setNumericValue(new BigDecimal(50));
		itxValue2.setLsState(itxState);
		Set<ItxContainerContainerState> itxStates = new HashSet<ItxContainerContainerState>();
		Set<ItxContainerContainerValue> itxValues = new HashSet<ItxContainerContainerValue>();
		itxValues.add(itxValue);
		itxValues.add(itxValue2);
		itxState.setLsValues(itxValues);
		itxStates.add(itxState);
		itx.setLsStates(itxStates);
		return itx;
	}
	
	@Transactional
	@Test
	public void saveLsContainerWithNestedValues(){
		String json = "{\"lsType\":\"subject\",\"lsKind\":\"nhp test subject\",\"recordedBy\":\"bob\",\"recordedDate\":1452795015412,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"subject name\",\"labelText\":\"Test Cont 8\",\"ignored\":false,\"preferred\":true,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null},{\"lsType\":\"corpName\",\"lsKind\":\"ACAS LsContainer\",\"labelText\":\"NHP-00014\",\"ignored\":false,\"preferred\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null},{\"lsType\":\"barcode\",\"lsKind\":\"subject barcode\",\"labelText\":\"\",\"ignored\":false,\"preferred\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null}],\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"subject attributes\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"sex\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"codeKind\":\"sex\",\"codeType\":\"subject attributes\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"male\",\"value\":\"male\"},{\"lsType\":\"codeValue\",\"lsKind\":\"species\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"codeKind\":\"species\",\"codeType\":\"subject attributes\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"homo sapiens\",\"value\":\"homo sapiens\"}],\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\"},{\"lsType\":\"status\",\"lsKind\":\"current subject status\",\"lsValues\":[{\"lsType\":\"dateValue\",\"lsKind\":\"weight measured date\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"value\":1452643200000,\"dateValue\":1452643200000},{\"lsType\":\"codeValue\",\"lsKind\":\"weight measured by\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"value\":\"bob\",\"codeValue\":\"bob\"},{\"lsType\":\"numericValue\",\"lsKind\":\"current weight\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"value\":123,\"numericValue\":123}],\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\"}],\"cid\":\"c162\",\"_changing\":false,\"_previousAttributes\":{\"lsType\":\"container\",\"lsKind\":\"container\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1452795007545,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"subject name\",\"labelText\":\"Test Cont 8\",\"ignored\":false,\"preferred\":true,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null},{\"lsType\":\"corpName\",\"lsKind\":\"ACAS LsContainer\",\"labelText\":\"NHP-00014\",\"ignored\":false,\"preferred\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null},{\"lsType\":\"barcode\",\"lsKind\":\"subject barcode\",\"labelText\":\"\",\"ignored\":false,\"preferred\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null}]},\"changed\":{\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"subject attributes\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"sex\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"codeKind\":\"sex\",\"codeType\":\"subject attributes\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"male\",\"value\":\"male\"},{\"lsType\":\"codeValue\",\"lsKind\":\"species\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"codeKind\":\"species\",\"codeType\":\"subject attributes\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"homo sapiens\",\"value\":\"homo sapiens\"}],\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\"},{\"lsType\":\"status\",\"lsKind\":\"current subject status\",\"lsValues\":[{\"lsType\":\"dateValue\",\"lsKind\":\"weight measured date\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"value\":1452643200000,\"dateValue\":1452643200000},{\"lsType\":\"codeValue\",\"lsKind\":\"weight measured by\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"value\":\"bob\",\"codeValue\":\"bob\"},{\"lsType\":\"numericValue\",\"lsKind\":\"current weight\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"value\":123,\"numericValue\":123}],\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\"}]},\"_pending\":false,\"urlRoot\":\"/api/containers/subject/nhp test subject\",\"lsProperties\":{\"defaultLabels\":[{\"key\":\"subject name\",\"type\":\"name\",\"kind\":\"subject name\",\"preferred\":true},{\"key\":\"corpName\",\"type\":\"corpName\",\"kind\":\"ACAS LsContainer\",\"preferred\":false},{\"key\":\"subject barcode\",\"type\":\"barcode\",\"kind\":\"subject barcode\",\"preferred\":false}],\"defaultValues\":[{\"key\":\"sex\",\"stateType\":\"metadata\",\"stateKind\":\"subject attributes\",\"type\":\"codeValue\",\"kind\":\"sex\",\"codeType\":\"subject attributes\",\"codeKind\":\"sex\",\"codeOrigin\":\"ACAS DDICT\",\"value\":\"\"},{\"key\":\"species\",\"stateType\":\"metadata\",\"stateKind\":\"subject attributes\",\"type\":\"codeValue\",\"kind\":\"species\",\"codeType\":\"subject attributes\",\"codeKind\":\"species\",\"codeOrigin\":\"ACAS DDICT\",\"value\":\"\"},{\"key\":\"weight measured date\",\"stateType\":\"status\",\"stateKind\":\"current subject status\",\"type\":\"dateValue\",\"kind\":\"weight measured date\"},{\"key\":\"weight measured by\",\"stateType\":\"status\",\"stateKind\":\"current subject status\",\"type\":\"codeValue\",\"kind\":\"weight measured by\"},{\"key\":\"current weight\",\"stateType\":\"status\",\"stateKind\":\"current subject status\",\"type\":\"numericValue\",\"kind\":\"current weight\"}]},\"className\":\"Container\",\"validationError\":null,\"idAttribute\":\"id\"}";
		Container jsonContainer = Container.fromJsonToContainer(json);
		Container newContainer = containerService.saveLsContainer(jsonContainer);
		for (ContainerState state : newContainer.getLsStates()){
			for (ContainerValue value : state.getLsValues()){
				if (value.getLsType().equals("codeValue")) Assert.assertNotNull(value.getCodeValue());
				if (value.getLsType().equals("dateValue")) Assert.assertNotNull(value.getDateValue());
				if (value.getLsType().equals("numericValue")) Assert.assertNotNull(value.getNumericValue());
				if (value.getLsType().equals("stringValue")) Assert.assertNotNull(value.getStringValue());
			}
		}
	}
	
	@Transactional
	@Test
	public void saveNestedContainer(){
		Container container = createTransientContainerStack();
		Container savedContainer = containerService.saveLsContainer(container);
		logger.info(savedContainer.toJsonWithNestedFull());
		Assert.assertNotNull(savedContainer.getLsStates());
		Assert.assertFalse(savedContainer.getLsStates().isEmpty());
		for (ContainerState state : savedContainer.getLsStates()){
			Assert.assertNotNull(state.getLsValues());
			Assert.assertFalse(state.getLsValues().isEmpty());
		}
		Assert.assertNotNull(savedContainer.getSecondContainers());
		Assert.assertFalse(savedContainer.getSecondContainers().isEmpty());
		for (ItxContainerContainer itx : savedContainer.getSecondContainers()){
			Assert.assertNotNull(itx.getLsStates());
			Assert.assertFalse(itx.getLsStates().isEmpty());
			for (ItxContainerContainerState state : itx.getLsStates()){
				Assert.assertNotNull(state.getLsValues());
				Assert.assertFalse(state.getLsValues().isEmpty());
			}
			Assert.assertNotNull(itx.getSecondContainer());
		}
		Container jsonParsedSavedContainer = Container.fromJsonToContainer(savedContainer.toJsonWithNestedFull());
		Assert.assertNotNull(jsonParsedSavedContainer.getLsStates());
		Assert.assertFalse(jsonParsedSavedContainer.getLsStates().isEmpty());
		for (ContainerState state : jsonParsedSavedContainer.getLsStates()){
			Assert.assertNotNull(state.getLsValues());
			Assert.assertFalse(state.getLsValues().isEmpty());
		}
		Assert.assertNotNull(jsonParsedSavedContainer.getSecondContainers());
		Assert.assertFalse(jsonParsedSavedContainer.getSecondContainers().isEmpty());
		for (ItxContainerContainer itx : jsonParsedSavedContainer.getSecondContainers()){
			Assert.assertNotNull(itx.getLsStates());
			Assert.assertFalse(itx.getLsStates().isEmpty());
			for (ItxContainerContainerState state : itx.getLsStates()){
				Assert.assertNotNull(state.getLsValues());
				Assert.assertFalse(state.getLsValues().isEmpty());
			}
			Assert.assertNotNull(itx.getSecondContainer());
		}
		
	}
	
	
	
	@Test
//	@Transactional
	public void createTestContainers(){
		//physical/plate
		Container plate = new Container();
		plate.setCodeName(autoLabelService.getAutoLabels("material_container", "id_codeName", 1L).get(0).getAutoLabel());
		plate.setLsType("physical");
		plate.setLsKind("plate");
		plate.setRecordedBy("acas admin");
		plate.setRecordedDate(new Date());
		//barcode/barcode
		try{
			LabelType labelType = LabelType.findLabelTypesByTypeNameEquals("barcode").getSingleResult();
		}catch (EmptyResultDataAccessException e){
			LabelType labelType = new LabelType();
			labelType.setTypeName("barcode");
			labelType.persist();
		}
		try{
			LabelKind labelKind = LabelKind.findLabelKindsByKindNameEqualsAndLsType("barcode", LabelType.findLabelTypesByTypeNameEquals("barcode").getSingleResult()).getSingleResult();
		}catch (EmptyResultDataAccessException e){
			LabelKind labelKind = new LabelKind();
			labelKind.setLsType(LabelType.findLabelTypesByTypeNameEquals("barcode").getSingleResult());
			labelKind.setKindName("barcode");
			labelKind.persist();
		}
		ContainerLabel plateBarcode = new ContainerLabel();
		plateBarcode.setLsType("barcode");
		plateBarcode.setLsKind("barcode");
		plateBarcode.setRecordedBy("acas admin");
		plateBarcode.setLabelText("TESTBARCODE-0000001");
		plateBarcode.setRecordedDate(new Date());
		Set<ContainerLabel> labels = new HashSet<ContainerLabel>();
		labels.add(plateBarcode);
		plate.setLsLabels(labels);
		//physical/racks
		Container racks = new Container();
		racks.setCodeName(autoLabelService.getAutoLabels("material_container", "id_codeName", 1L).get(0).getAutoLabel());
		racks.setLsType("physical");
		racks.setLsKind("racks");
		racks.setRecordedBy("acas admin");
		racks.setRecordedDate(new Date());
		//moved to/plate container
		try{
			InteractionKind itxKind = InteractionKind.findInteractionKindsByKindNameEqualsAndLsType("plate container", InteractionType.findInteractionTypesByTypeNameEquals("moved to").getSingleResult()).getSingleResult();
		}catch (EmptyResultDataAccessException e){
			InteractionKind itxKind = new InteractionKind();
			itxKind.setLsType(InteractionType.findInteractionTypesByTypeNameEquals("moved to").getSingleResult());
			itxKind.setKindName("plate container");
			itxKind.persist();
		}
		ItxContainerContainer movedToItx = new ItxContainerContainer();
		movedToItx.setCodeName(autoLabelService.getAutoLabels("interaction_containerContainer", "id_codeName", 1L).get(0).getAutoLabel());
		movedToItx.setLsType("moved to");
		movedToItx.setLsKind("plate container");
		movedToItx.setRecordedBy("acas admin");
		movedToItx.setRecordedDate(new Date());
		
		plate = containerService.saveLsContainer(plate);
		racks = containerService.saveLsContainer(racks);
		movedToItx.setFirstContainer(plate);
		movedToItx.setSecondContainer(racks);
		movedToItx.persist();
	}
	
	@Test
	@Transactional
	public void getContainersByLocation(){
		List<String> locationCodeNameList = new ArrayList<String>();
		TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("location","default");
		query.setMaxResults(100);
		for (Container container : query.getResultList()){
			locationCodeNameList.add(container.getCodeName());
		}
		logger.info(locationCodeNameList.toString());
		Collection<ContainerLocationDTO> result = containerService.getContainersInLocation(locationCodeNameList);
		logger.info(ContainerLocationDTO.toJsonArray(result));
		Assert.assertTrue(result.size() > 0);
	}
	
	@Test
	@Transactional
	public void getContainersByLocationAndTypeKind(){
		List<String> locationCodeNameList = new ArrayList<String>();
		TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("location","default");
		query.setMaxResults(1);
		Container container = query.getSingleResult();
		locationCodeNameList.add(container.getCodeName());
		Collection<ContainerLocationDTO> result = containerService.getContainersInLocation(locationCodeNameList, "physical", "plate");
		logger.info(ContainerLocationDTO.toJsonArray(result));
		Assert.assertTrue(result.size() > 0);

	}
	
	@Test
	@Transactional
	public void getWellCodesByPlateBarcodes(){
		List<String> plateBarcodes = new ArrayList<String>();
		plateBarcodes.add(Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate").getResultList().get(0).getLsLabels().iterator().next().getLabelText());
		logger.info("querying with: "+plateBarcodes.toString());
		Collection<PlateWellDTO> result = containerService.getWellCodesByPlateBarcodes(plateBarcodes);
		logger.info(PlateWellDTO.toJsonArray(result));
		Assert.assertTrue(result.size() > 0);
	}
	
	@Test
	@Transactional
	public void getContainerCodesByLabels(){
		List<String> plateBarcodes = new ArrayList<String>();
		plateBarcodes.add(Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate").getResultList().get(0).getLsLabels().iterator().next().getLabelText());
		logger.info("querying with: "+plateBarcodes.toString());
		Collection<CodeLabelDTO> result = containerService.getContainerCodesByLabels(plateBarcodes, null, null, null, null);
		logger.info(CodeLabelDTO.toJsonArray(result));
		Assert.assertTrue(result.size() > 0);
	}
	
	@Test
	@Transactional
	public void getContainerCodesByLabelsWithTypeKinds(){
		List<String> plateBarcodes = new ArrayList<String>();
		plateBarcodes.add(Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate").getResultList().get(0).getLsLabels().iterator().next().getLabelText());
		logger.info("querying with: "+plateBarcodes.toString());
		Collection<CodeLabelDTO> result = containerService.getContainerCodesByLabels(plateBarcodes, "container", "plate", "barcode", "barcode");
		logger.info(CodeLabelDTO.toJsonArray(result));
		Assert.assertTrue(result.size() > 0);
	}
	
	@Test
	@Transactional
	public void getContainerCodesByLabelsWithConflictingTypeKinds(){
		List<String> plateBarcodes = new ArrayList<String>();
		TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate");
		query.setMaxResults(1);
		Container container = query.getSingleResult();
		plateBarcodes.add(container.getLsLabels().iterator().next().getLabelText());
		logger.info("querying with: "+plateBarcodes.toString());
		Collection<CodeLabelDTO> result = containerService.getContainerCodesByLabels(plateBarcodes, "plate", "plate", "name", "barcode");
		logger.info(CodeLabelDTO.toJsonArray(result));
		Assert.assertTrue(result.size() == 0);
	}
	
	@Test
	@Transactional
	public void getWellContent(){
		Collection<ContainerRequestDTO> wellCodes = new HashSet<ContainerRequestDTO>();
		ContainerRequestDTO wellCode = new ContainerRequestDTO();
		TypedQuery<Container> wellQuery = Container.findContainersByLsTypeEqualsAndLsKindEquals("well","default");
		wellQuery.setMaxResults(1);
		wellCode.setContainerCodeName(wellQuery.getSingleResult().getCodeName());
		wellCodes.add(wellCode);
		logger.info("querying with: "+ContainerRequestDTO.toJsonArray(wellCodes));
		Collection<WellContentDTO> result = containerService.getWellContent(wellCodes);
		logger.info(WellContentDTO.toJsonArray(result));
		Assert.assertTrue(result.size() > 0);
	}
	
	@Test
	@Transactional
	public void getManyWellContent(){
		Collection<ContainerRequestDTO> wellCodes = new HashSet<ContainerRequestDTO>();
		TypedQuery<Container> wellQuery = Container.findContainersByLsTypeEqualsAndLsKindEquals("well","default");
		wellQuery.setMaxResults(2000);
		for (Container well : wellQuery.getResultList()){
			ContainerRequestDTO wellCode = new ContainerRequestDTO();
			wellCode.setContainerCodeName(well.getCodeName());
			wellCodes.add(wellCode);
		}
		logger.info("querying with: "+wellCodes.size() + " well codes");
		Long before = (new Date()).getTime();
		Collection<WellContentDTO> result = containerService.getWellContent(wellCodes);
		logger.info(WellContentDTO.toJsonArray(result));
		Long after = (new Date()).getTime();
		logger.info("ms elapsed: "+ String.valueOf(after-before));
		Assert.assertTrue(result.size() > 0);
	}
	
	@Test
	@Transactional
	public void getContainerCodesWithoutBarcodes(){
		List<String> plateBarcodes = new ArrayList<String>();
		plateBarcodes.add("hitpick master plate");
		logger.info("querying with: "+plateBarcodes.toString());
		Collection<CodeLabelDTO> result = containerService.getContainerCodesByLabels(plateBarcodes, null, null, null, null);
		logger.info(CodeLabelDTO.toJsonArray(result));
		Assert.assertTrue(result.size() > 0);
	}
	
	@Test
	@Transactional
	public void checkDependencies_pass(){
    	String codeName = Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate").getResultList().get(0).getCodeName();
    	Container container = Container.findContainersByCodeNameEquals(codeName).getSingleResult();
    	logger.info(container.toJson());
		Long before = (new Date()).getTime();
    	ContainerDependencyCheckDTO result = containerService.checkDependencies(container);
		Long after = (new Date()).getTime();
		logger.info("ms elapsed: "+ String.valueOf(after-before));
		logger.info(result.toJson());
		Assert.assertFalse(result.isLinkedDataExists());
	}
	
	@Test
	@Transactional
	public void checkDependencies_fail_on_added_to(){
//		String codeName = Container.findContainersByLsTypeEqualsAndLsKindEquals("location","default").getResultList().get(0).getCodeName();
		String codeName = "CONT-388802";
    	Container container = Container.findContainersByCodeNameEquals(codeName).getSingleResult();
    	logger.info(container.toJson());
		Long before = (new Date()).getTime();
    	ContainerDependencyCheckDTO result = containerService.checkDependencies(container);
		Long after = (new Date()).getTime();
		logger.info("ms elapsed: "+ String.valueOf(after-before));
		logger.info(result.toJson());
		Assert.assertTrue(result.isLinkedDataExists());
		Assert.assertFalse(result.getDependentCorpNames().isEmpty());
	}
	
	@Test
	@Transactional
	public void checkDependencies_fail_on_subject_itx(){
		
	}
	
	@Test
	@Transactional
	public void checkDependencies_fail_on_member_subject_itx(){
		
	}
	
	@Test
	@Transactional
	@Rollback(value=false)
	public void updateContainer(){
		String json = "{\"codeName\":\"CONT-3075\",\"deleted\":false,\"id\":6147,\"ignored\":false,\"lsKind\":\"CUSTOM_LOCATION\","
				+ "\"lsLabels\":[{\"deleted\":false,\"id\":3075,\"ignored\":false,\"labelText\":\"screen system plate\",\"lsKind\":\"common\",\"lsTransaction\":1,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_common\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"bob\",\"recordedDate\":1449581762000,\"version\":0}],"
				+ "\"lsStates\":[{\"deleted\":false,\"id\":6151,\"ignored\":true,\"lsKind\":\"location information\",\"lsTransaction\":1,\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_location information\","
					+ "\"lsValues\":[{\"codeKind\":\"screen system plate\",\"codeOrigin\":\"CMGLOCATION\",\"codeType\":\"screen system plate\",\"codeTypeAndKind\":\"screen system plate_screen system plate\",\"codeValue\":\"screen system plate\",\"deleted\":false,\"id\":24602,\"ignored\":false,\"lsKind\":\"CUSTOM_LOCATION\",\"lsTransaction\":1,\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_CUSTOM_LOCATION\",\"modifiedBy\":\"\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"bob\",\"recordedDate\":1449581762000,\"unitKind\":\"NA\",\"unitTypeAndKind\":\"null_NA\",\"version\":0}]"
				+ ",\"modifiedBy\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1449581762000,\"version\":0}]"
				+ ",\"lsTransaction\":1,\"lsType\":\"storage\",\"lsTypeAndKind\":\"storage_CUSTOM_LOCATION\",\"modifiedBy\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1449581762000,\"version\":0}";
		Container container = Container.fromJsonToContainer(json);
		Container updatedContainer = containerService.updateContainer(container);
		Assert.assertTrue(updatedContainer.getLsStates().iterator().next().isIgnored());
		Assert.assertTrue(updatedContainer.getVersion() == container.getVersion() + 1);
		Container fetchedContainer = Container.findContainer(container.getId());
		Assert.assertTrue(updatedContainer.getVersion() == fetchedContainer.getVersion());
	}
	
	@Test
	@Transactional
	public void validateContainerUniqueName_Fail(){
		String json = "{\"lsType\":\"subject\",\"lsKind\":\"nhp test subject\",\"recordedBy\":\"bob\",\"recordedDate\":1452795015412,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"subject name\",\"labelText\":\"Test Cont 8\",\"ignored\":false,\"preferred\":true,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null},{\"lsType\":\"corpName\",\"lsKind\":\"ACAS LsContainer\",\"labelText\":\"NHP-00014\",\"ignored\":false,\"preferred\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null},{\"lsType\":\"barcode\",\"lsKind\":\"subject barcode\",\"labelText\":\"\",\"ignored\":false,\"preferred\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null}],\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"subject attributes\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"sex\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"codeKind\":\"sex\",\"codeType\":\"subject attributes\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"male\",\"value\":\"male\"},{\"lsType\":\"codeValue\",\"lsKind\":\"species\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"codeKind\":\"species\",\"codeType\":\"subject attributes\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"homo sapiens\",\"value\":\"homo sapiens\"}],\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\"},{\"lsType\":\"status\",\"lsKind\":\"current subject status\",\"lsValues\":[{\"lsType\":\"dateValue\",\"lsKind\":\"weight measured date\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"value\":1452643200000,\"dateValue\":1452643200000},{\"lsType\":\"codeValue\",\"lsKind\":\"weight measured by\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"value\":\"bob\",\"codeValue\":\"bob\"},{\"lsType\":\"numericValue\",\"lsKind\":\"current weight\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"value\":123,\"numericValue\":123}],\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\"}],\"cid\":\"c162\",\"_changing\":false,\"_previousAttributes\":{\"lsType\":\"container\",\"lsKind\":\"container\",\"corpName\":\"\",\"recordedBy\":\"bob\",\"recordedDate\":1452795007545,\"shortDescription\":\" \",\"lsLabels\":[{\"lsType\":\"name\",\"lsKind\":\"subject name\",\"labelText\":\"Test Cont 8\",\"ignored\":false,\"preferred\":true,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null},{\"lsType\":\"corpName\",\"lsKind\":\"ACAS LsContainer\",\"labelText\":\"NHP-00014\",\"ignored\":false,\"preferred\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null},{\"lsType\":\"barcode\",\"lsKind\":\"subject barcode\",\"labelText\":\"\",\"ignored\":false,\"preferred\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"physicallyLabled\":false,\"imageFile\":null}]},\"changed\":{\"lsStates\":[{\"lsType\":\"metadata\",\"lsKind\":\"subject attributes\",\"lsValues\":[{\"lsType\":\"codeValue\",\"lsKind\":\"sex\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"codeKind\":\"sex\",\"codeType\":\"subject attributes\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"male\",\"value\":\"male\"},{\"lsType\":\"codeValue\",\"lsKind\":\"species\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"codeKind\":\"species\",\"codeType\":\"subject attributes\",\"codeOrigin\":\"ACAS DDICT\",\"codeValue\":\"homo sapiens\",\"value\":\"homo sapiens\"}],\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\"},{\"lsType\":\"status\",\"lsKind\":\"current subject status\",\"lsValues\":[{\"lsType\":\"dateValue\",\"lsKind\":\"weight measured date\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"value\":1452643200000,\"dateValue\":1452643200000},{\"lsType\":\"codeValue\",\"lsKind\":\"weight measured by\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"value\":\"bob\",\"codeValue\":\"bob\"},{\"lsType\":\"numericValue\",\"lsKind\":\"current weight\",\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\",\"value\":123,\"numericValue\":123}],\"ignored\":false,\"recordedDate\":1452795015412,\"recordedBy\":\"bob\"}]},\"_pending\":false,\"urlRoot\":\"/api/containers/subject/nhp test subject\",\"lsProperties\":{\"defaultLabels\":[{\"key\":\"subject name\",\"type\":\"name\",\"kind\":\"subject name\",\"preferred\":true},{\"key\":\"corpName\",\"type\":\"corpName\",\"kind\":\"ACAS LsContainer\",\"preferred\":false},{\"key\":\"subject barcode\",\"type\":\"barcode\",\"kind\":\"subject barcode\",\"preferred\":false}],\"defaultValues\":[{\"key\":\"sex\",\"stateType\":\"metadata\",\"stateKind\":\"subject attributes\",\"type\":\"codeValue\",\"kind\":\"sex\",\"codeType\":\"subject attributes\",\"codeKind\":\"sex\",\"codeOrigin\":\"ACAS DDICT\",\"value\":\"\"},{\"key\":\"species\",\"stateType\":\"metadata\",\"stateKind\":\"subject attributes\",\"type\":\"codeValue\",\"kind\":\"species\",\"codeType\":\"subject attributes\",\"codeKind\":\"species\",\"codeOrigin\":\"ACAS DDICT\",\"value\":\"\"},{\"key\":\"weight measured date\",\"stateType\":\"status\",\"stateKind\":\"current subject status\",\"type\":\"dateValue\",\"kind\":\"weight measured date\"},{\"key\":\"weight measured by\",\"stateType\":\"status\",\"stateKind\":\"current subject status\",\"type\":\"codeValue\",\"kind\":\"weight measured by\"},{\"key\":\"current weight\",\"stateType\":\"status\",\"stateKind\":\"current subject status\",\"type\":\"numericValue\",\"kind\":\"current weight\"}]},\"className\":\"Container\",\"validationError\":null,\"idAttribute\":\"id\"}";
		Container jsonContainer = Container.fromJsonToContainer(json);
		ArrayList<ErrorMessage> errors = containerService.validateContainer(jsonContainer);
		Assert.assertFalse(errors.isEmpty());
	}
	@Test
	@Transactional
	public void throwInTrash() throws Exception{
		Collection<ContainerRequestDTO> containerCodeDTOs = new HashSet<ContainerRequestDTO>();
		ContainerRequestDTO containerCodeDTO = new ContainerRequestDTO();
		containerCodeDTO.setContainerCodeName(Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate").getResultList().get(0).getCodeName());
		containerCodeDTO.setModifiedBy("bfielder");
		containerCodeDTO.setModifiedDate(new Date());
		containerCodeDTOs.add(containerCodeDTO);
		Collection<ContainerErrorMessageDTO> results = containerService.throwInTrash(containerCodeDTOs);
		Assert.assertFalse(results.isEmpty());
	}
	
	@Test
	@Transactional
	public void throwInTrash_tmp() throws Exception{
		String json = "[{\"containerCodeName\":\"CONT-2265608\",\"modifiedBy\":\"acas\",\"modifiedDate\":1455057684000}]";
		Collection<ContainerRequestDTO> containerCodeDTOs = ContainerRequestDTO.fromJsonArrayToCoes(json);
		Collection<ContainerErrorMessageDTO> results = containerService.throwInTrash(containerCodeDTOs);
		logger.info(ContainerErrorMessageDTO.toJsonArray(results));
		Assert.assertFalse(results.isEmpty());
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
		Collection<ContainerErrorMessageDTO> results = containerService.updateAmountInWell(containerCodeDTOs);
		logger.info(ContainerErrorMessageDTO.toJsonArray(results));
		Assert.assertFalse(results.isEmpty());
	}
	
	
	@Test
	@Transactional
	public void getCodeNameFromName(){
		Container container = Container.findContainersByLsTypeEqualsAndLsKindEquals("container", "plate").getResultList().get(0);
    	String containerType = container.getLsType();
    	String containerKind = container.getLsKind();
		String label = container.getLsLabels().iterator().next().getLabelText();
		PreferredNameDTO request = new PreferredNameDTO(label, null, null);
		Collection<PreferredNameDTO> requests = new HashSet<PreferredNameDTO>();
		requests.add(request);
		PreferredNameRequestDTO requestDTO = new PreferredNameRequestDTO();
		requestDTO.setRequests(requests);
		PreferredNameResultsDTO results = containerService.getCodeNameFromName(containerType, containerKind, null, null, requestDTO);
		logger.info(results.toJson());
		for (PreferredNameDTO result : results.getResults()){
    		Assert.assertNotNull(result.getRequestName());
    		Assert.assertNotNull(result.getReferenceName());
    		Assert.assertNotNull(result.getPreferredName());
    	}
	}
	
	@Test
	@Transactional
	public void getAllWellsForPlate(){
		Container container = Container.findContainer(289095L);
    	Collection<Container> wells = new ArrayList<Container>();
    	for (ItxContainerContainer itx : container.getSecondContainers()){
    		Container well = itx.getSecondContainer();
    		wells.add(well);
    	}
    	logger.info(Container.toJsonArray(wells));
	}
	
	@Test
	@Transactional
	public void createEmptyPlate() throws Exception{
		TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("definition container", "plate");
		query.setMaxResults(1);
		Container definition = query.getSingleResult();
		CreatePlateRequestDTO plateRequest = new CreatePlateRequestDTO();
		plateRequest.setDefinition(definition.getCodeName());
		plateRequest.setBarcode("TESTBARCODE-123");
		plateRequest.setRecordedBy("acas");
    	PlateStubDTO result = containerService.createPlate(plateRequest);
    	try{
    		Container dupeContainer = Container.findContainerByLabelText("container", "plate", "name", "barcode", plateRequest.getBarcode()).getSingleResult();
    	}catch(NoResultException e){
    		logger.error("no result",e);
    	}
    	logger.info(result.toJson());
    	Assert.assertEquals(1536, result.getWells().size());
    	String[][] plateLayout = new String[32][48];
    	for (WellStubDTO well : result.getWells()){
    		logger.debug(well.getRowIndex().toString() + ", "+well.getColumnIndex().toString());
    		plateLayout[well.getRowIndex()][well.getColumnIndex()] = well.getWellName();
    	}
    	logger.info(Arrays.deepToString(plateLayout));
	}
	
	@Test
	@Transactional
	@Rollback(value=false)
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
    	PlateStubDTO result = containerService.createPlate(plateRequest);
    	Assert.assertEquals(1536, result.getWells().size());
    	String[][] plateLayout = new String[32][48];
    	for (WellStubDTO well : result.getWells()){
    		plateLayout[well.getRowIndex()][well.getColumnIndex()] = well.getWellName();
    		if (well.getWellName().equals("A001")){
    			Collection<ContainerRequestDTO> checkWells = new ArrayList<ContainerRequestDTO>();
    			ContainerRequestDTO checkWell = new ContainerRequestDTO(well.getCodeName(), null, null);
    			checkWells.add(checkWell);
    			WellContentDTO checkResult = containerService.getWellContent(checkWells).iterator().next();
    			Assert.assertEquals(new BigDecimal(1), checkResult.getAmount());
    			Assert.assertEquals("µL", checkResult.getAmountUnits());
    			logger.info("checked well A001");
    		}
    		if (well.getWellName().equals("B003")){
    			Collection<ContainerRequestDTO> checkWells = new ArrayList<ContainerRequestDTO>();
    			ContainerRequestDTO checkWell = new ContainerRequestDTO(well.getCodeName(), null, null);
    			checkWells.add(checkWell);
    			WellContentDTO checkResult = containerService.getWellContent(checkWells).iterator().next();
    			Assert.assertEquals(new BigDecimal(2), checkResult.getAmount());
    			Assert.assertEquals("µL", checkResult.getAmountUnits());
    			logger.info("checked well B003");

    		}
    		if (well.getWellName().equals("AA007")){
    			Collection<ContainerRequestDTO> checkWells = new ArrayList<ContainerRequestDTO>();
    			ContainerRequestDTO checkWell = new ContainerRequestDTO(well.getCodeName(), null, null);
    			checkWells.add(checkWell);
    			WellContentDTO checkResult = containerService.getWellContent(checkWells).iterator().next();
    			Assert.assertEquals(new BigDecimal(3), checkResult.getAmount());
    			Assert.assertEquals("µL", checkResult.getAmountUnits());
    			logger.info("checked well AA007");

    		}
    	}
    	logger.info(Arrays.deepToString(plateLayout));
    	
    	
	}
	
	@Test
	@Transactional
	public void updateWellStatus() throws Exception{
		TypedQuery<Container> query = Container.findContainersByLsTypeEqualsAndLsKindEquals("well", "default");
		query.setMaxResults(100);
		Collection<Container> wells = query.getResultList();
		Assert.assertEquals(100, wells.size());
		Collection<WellContentDTO> wellDTOs = new ArrayList<WellContentDTO>();
		String recordedBy = "acas";
		//values to set:
		BigDecimal amount = new BigDecimal(10);
		String amountUnits = "mg";
		Double batchConcentration = new Double(1);
		String batchConcUnits = "mM";
		String physicalState = "liquid";
		for(Container well : wells){
			WellContentDTO wellDTO = new WellContentDTO(well.getCodeName(), null, null, null, recordedBy, null, amount, amountUnits, null, batchConcentration, batchConcUnits, null, physicalState);
			wellDTOs.add(wellDTO);
		}
		logger.info(WellContentDTO.toJsonArray(wellDTOs));
		Assert.assertEquals(100, wellDTOs.size());
		Collection<ContainerErrorMessageDTO> results = containerService.updateWellStatus(wellDTOs);
		Assert.assertEquals(100, results.size());
		logger.info(ContainerErrorMessageDTO.toJsonArray(results));
		Collection<ContainerRequestDTO> checkWellCodes = new ArrayList<ContainerRequestDTO>();
    	for (ContainerErrorMessageDTO result : results){
    		Assert.assertNull(result.getLevel());
    		Assert.assertNotNull(result.getContainerCodeName());
    		ContainerRequestDTO wellCode = new ContainerRequestDTO(result.getContainerCodeName(), null, null);
    		checkWellCodes.add(wellCode);
    	}
    	Collection<WellContentDTO> checkResults = containerService.getWellContent(checkWellCodes);
    	for (WellContentDTO checkResult : checkResults){
    		Assert.assertEquals(amount, checkResult.getAmount());
    		Assert.assertEquals(amountUnits, checkResult.getAmountUnits());
    		Assert.assertEquals(batchConcentration, checkResult.getBatchConcentration());
    		Assert.assertEquals(batchConcUnits, checkResult.getBatchConcUnits());
    		Assert.assertEquals(physicalState, checkResult.getPhysicalState());
    		Assert.assertNotNull(checkResult.getBatchCode());
    	}
    	
	}
	
	@Test
	@Transactional
	public void getWellContentByPlateBarcode(){
		String plateBarcode = Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate").getResultList().get(0).getLsLabels().iterator().next().getLabelText();
		logger.info("querying with: "+plateBarcode);
		Collection<WellContentDTO> results = containerService.getWellContentByPlateBarcode(plateBarcode);
		logger.info(WellContentDTO.toJsonArray(results));
		Assert.assertTrue(results.size() > 0);
		for (WellContentDTO result : results){
			Assert.assertNull(result.getLevel());
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
	public void getPlateTypeByPlateBarcode(){
		String plateBarcode = Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate").getResultList().get(0).getLsLabels().iterator().next().getLabelText();
		logger.info("querying with: "+plateBarcode);
		PlateStubDTO result = containerService.getPlateTypeByPlateBarcode(plateBarcode);
		logger.info(result.toJson());
    	Assert.assertNotNull(result.getCodeName());
    	Assert.assertNotNull(result.getBarcode());
    	Assert.assertNotNull(result.getPlateType());
	}
	
	
	
}
