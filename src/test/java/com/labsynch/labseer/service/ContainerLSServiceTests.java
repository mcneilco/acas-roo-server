

package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerLabel;
import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ContainerValue;
import com.labsynch.labseer.domain.InteractionKind;
import com.labsynch.labseer.domain.InteractionType;
import com.labsynch.labseer.domain.ItxContainerContainer;
import com.labsynch.labseer.domain.LabelKind;
import com.labsynch.labseer.domain.LabelType;
import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.dto.ContainerCodeDTO;
import com.labsynch.labseer.dto.ContainerLocationDTO;
import com.labsynch.labseer.dto.ContainerMiniDTO;
import com.labsynch.labseer.dto.ContainerStateMiniDTO;
import com.labsynch.labseer.dto.PlateWellDTO;
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
		locationCodeNameList.add(Container.findContainersByLsTypeEqualsAndLsKindEquals("physical","racks").getResultList().get(0).getCodeName());
		Collection<ContainerLocationDTO> result = containerService.getContainersInLocation(locationCodeNameList);
		logger.info(ContainerLocationDTO.toJsonArray(result));
		Assert.assertTrue(result.size() > 0);
	}
	
	@Test
	@Transactional
	public void getContainersByLocationAndTypeKind(){
		List<String> locationCodeNameList = new ArrayList<String>();
		locationCodeNameList.add(Container.findContainersByLsTypeEqualsAndLsKindEquals("physical","racks").getResultList().get(0).getCodeName());
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
		Collection<ContainerCodeDTO> result = containerService.getContainerCodesByLabels(plateBarcodes, null, null, null, null);
		logger.info(ContainerCodeDTO.toJsonArray(result));
		Assert.assertTrue(result.size() > 0);
	}
	
	@Test
	@Transactional
	public void getContainerCodesByLabelsWithTypeKinds(){
		List<String> plateBarcodes = new ArrayList<String>();
		plateBarcodes.add(Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate").getResultList().get(0).getLsLabels().iterator().next().getLabelText());
		logger.info("querying with: "+plateBarcodes.toString());
		Collection<ContainerCodeDTO> result = containerService.getContainerCodesByLabels(plateBarcodes, "container", "plate", "barcode", "barcode");
		logger.info(ContainerCodeDTO.toJsonArray(result));
		Assert.assertTrue(result.size() > 0);
	}
	
	@Test
	@Transactional
	public void getContainerCodesByLabelsWithConflictingTypeKinds(){
		List<String> plateBarcodes = new ArrayList<String>();
		plateBarcodes.add(Container.findContainersByLsTypeEqualsAndLsKindEquals("container","plate").getResultList().get(0).getLsLabels().iterator().next().getLabelText());
		logger.info("querying with: "+plateBarcodes.toString());
		Collection<ContainerCodeDTO> result = containerService.getContainerCodesByLabels(plateBarcodes, "plate", "plate", "name", "barcode");
		logger.info(ContainerCodeDTO.toJsonArray(result));
		Assert.assertTrue(result.size() == 0);
	}
	
}
