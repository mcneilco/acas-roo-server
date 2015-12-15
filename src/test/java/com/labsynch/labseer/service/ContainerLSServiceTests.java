

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
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ContainerValue;
import com.labsynch.labseer.domain.ItxContainerContainer;
import com.labsynch.labseer.domain.ItxContainerContainerState;
import com.labsynch.labseer.domain.ItxContainerContainerValue;
import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.ItxLsThingLsThingState;
import com.labsynch.labseer.domain.ItxLsThingLsThingValue;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.dto.ContainerMiniDTO;
import com.labsynch.labseer.dto.ContainerStateMiniDTO;
import com.labsynch.labseer.utils.PropertiesUtilService;

import flexjson.JSONTokener;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class ContainerLSServiceTests {

	private static final Logger logger = LoggerFactory.getLogger(ContainerLSServiceTests.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;
	
	@Autowired
	private ContainerService containerService;
	
	@Autowired
	private ContainerStateService csService;

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
	
	

}
