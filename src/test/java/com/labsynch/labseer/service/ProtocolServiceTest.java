

package com.labsynch.labseer.service;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.exceptions.UniqueNameException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class ProtocolServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ProtocolServiceTest.class);
	
	@Autowired
	private ProtocolService protocolService;
	
	@Autowired
    private ProtocolValueService protocolValueService;
	
	//@Test
	public void FindProtocolByName_Test10(){
		List<Protocol> protocols = Protocol.findProtocolByProtocolName("test");
		logger.info("Found number of protocols: " + protocols.size());
		for (Protocol protocol : protocols){
			logger.info(protocol.toJson());
		}
		
	}
	
	//@Test
	public void CreateProtocolTest_1(){
		Protocol protocol = new Protocol();
		protocol.setLsType("test protocol type");
		protocol.setLsKind("test protocol kind");
		protocol.setShortDescription("just a test");
		protocol.setRecordedBy("joeblow");
		protocol.setRecordedDate(new Date());
		LsTransaction trx = new LsTransaction();
		trx.setRecordedDate(new Date());
		trx.persist();
		protocol.setLsTransaction(trx.getId());
		protocol.persist();
		logger.info("just persisted a protocol");
		logger.info(protocol.toJson());
		long id = protocol.getId();
//		long expectedId = 6l;
        Assert.assertNotNull("Find method for 'Protocol' illegally returned null for id '" + id + "'", id);
        //Assert.assertEquals("Find method for 'Protocol' returned the incorrect identifier", expectedId, id);		
	}
	
	//@Test
	@Transactional
	public void CreateProtocolsFromJson(){
		//String json = "[ { \"name\": \"PROT-101\",\"shortDescription\": \"Just a simple test\",\"lsTransaction\": null,\"protocolStates\": [ { \"protocolValues\": [ { \"valueType\": \"stringValue\",\"valueKind\": \"reader instrument\",\"stringValue\": \"Molecular Dynamics FLIPR\",\"fileValue\": null,\"urlValue\": null,\"dateValue\": null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\": null,\"sigFigs\": null,\"uncertainty\": null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"lsTransaction\": null,\"thingIdValue\": null,\"sampleId\": null,\"sampleName\": \"\",\"publicData\": false,\"recordedDate\":    1353267278000 },{ \"valueType\": \"numericValue\",\"valueKind\": \"curve min\",\"stringValue\": null,\"fileValue\": null,\"urlValue\": null,\"dateValue\": null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\":                0,\"sigFigs\":                2,\"uncertainty\": null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"lsTransaction\": null,\"thingIdValue\": null,\"sampleId\": null,\"sampleName\": \"\",\"publicData\": false,\"recordedDate\":    1353267278000 },{ \"valueType\": \"numericValue\",\"valueKind\": \"curve max\",\"stringValue\": null,\"fileValue\": null,\"urlValue\": null,\"dateValue\": null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\":              100,\"sigFigs\":                2,\"uncertainty\": null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"lsTransaction\": null,\"thingIdValue\": null,\"sampleId\": null,\"sampleName\": \"\",\"publicData\": false,\"recordedDate\":    1353267278000 } ],\"recordedBy\": \"userName\",\"stateType\": \"metadata\",\"stateKind\": \"protocol parameters\",\"comments\": \"\",\"lsTransaction\": null,\"ignored\": false,\"recordedDate\":    1353267278000 } ] } ]";
		String json = "{ \"codeName\": \"protocolName\",\"shortDescription\": \"protocol short description\",\"lsTransaction\": { \"comments\": \"protocol 102 transactions\",\"id\":      1,\"recordedDate\": 1.354e+12,\"version\":      0 },\"recordedBy\": \"userName\",\"recordedDate\": 1.354e+12,\"protocolStates\": [ { \"protocolValues\": [ { \"valueType\": \"stringValue\",\"valueKind\": \"reader instrument\",\"stringValue\": \"Molecular Dynamics FLIPR\",\"fileValue\": null,\"urlValue\": null,\"dateValue\": null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\": null,\"sigFigs\": null,\"uncertainty\": null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"publicData\": true,\"lsTransaction\": { \"comments\": \"protocol 102 transactions\",\"id\":      1,\"recordedDate\": 1.354e+12,\"version\":      0 },\"batchCode\": null,\"recordedDate\": 1.354e+12 },{ \"valueType\": \"numericValue\",\"valueKind\": \"curve min\",\"stringValue\": null,\"fileValue\": null,\"urlValue\": null,\"dateValue\": null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\":      0,\"sigFigs\":      2,\"uncertainty\": null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"publicData\": true,\"lsTransaction\": { \"comments\": \"protocol 102 transactions\",\"id\":      1,\"recordedDate\": 1.354e+12,\"version\":      0 },\"batchCode\": null,\"recordedDate\": 1.354e+12 },{ \"valueType\": \"numericValue\",\"valueKind\": \"curve max\",\"stringValue\": null,\"fileValue\": null,\"urlValue\": null,\"dateValue\": null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\":    100,\"sigFigs\":      2,\"uncertainty\": null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"publicData\": true,\"lsTransaction\": { \"comments\": \"protocol 102 transactions\",\"id\":      1,\"recordedDate\": 1.354e+12,\"version\":      0 },\"batchCode\": null,\"recordedDate\": 1.354e+12 } ],\"recordedBy\": \"userName\",\"stateType\": \"metadata\",\"stateKind\": \"protocol analysis parameters\",\"comments\": \"\",\"lsTransaction\": { \"comments\": \"protocol 102 transactions\",\"id\":      1,\"recordedDate\": 1.354e+12,\"version\":      0 },\"ignored\": false,\"recordedDate\": 1.354e+12 } ] }";

		Collection<Protocol> protocols = Protocol.fromJsonArrayToProtocols(json)	;
		for (Protocol protocol:protocols){
			logger.info("initial json protocol " + protocol.toJson());
			protocol.persist();
			logger.info(protocol.toJson());
		}
	}
	
	@Test
	//@Transactional
	public void CreateProtocolFromNestedJson(){
		LsTransaction lsTransaction = new LsTransaction();
		lsTransaction.setComments("protocol 102 transactions");
		lsTransaction.setRecordedDate(new Date());
		lsTransaction.persist();
		
		//String json = "{ \"name\": \"\",\"shortDescription\": \"\",\"lsTransaction\": null,\"protocolStates\": [ { \"protocolValues\": [ { \"valueType\": \"stringValue\",\"valueKind\": \"reader instrument\",\"stringValue\": \"Molecular Dynamics FLIPR\",\"fileValue\": null,\"urlValue\": null,\"dateValue\": null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\": null,\"sigFigs\": null,\"uncertainty\": null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"lsTransaction\": null,\"thingIdValue\": null,\"sampleId\": null,\"sampleName\": \"\",\"publicData\": false,\"recordedDate\":    1353216427000 },{ \"valueType\": \"numericValue\",\"valueKind\": \"curve min\",\"stringValue\": null,\"fileValue\": null,\"urlValue\": null,\"dateValue\": null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\":                0,\"sigFigs\":                2,\"uncertainty\": null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"lsTransaction\": null,\"thingIdValue\": null,\"sampleId\": null,\"sampleName\": \"\",\"publicData\": false,\"recordedDate\":    1353216427000 },{ \"valueType\": \"numericValue\",\"valueKind\": \"curve max\",\"stringValue\": null,\"fileValue\": null,\"urlValue\": null,\"dateValue\": null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\":              100,\"sigFigs\":                2,\"uncertainty\": null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"lsTransaction\": null,\"thingIdValue\": null,\"sampleId\": null,\"sampleName\": \"\",\"publicData\": false,\"recordedDate\":    1353216427000 } ],\"recordedBy\": \"userName\",\"stateType\": \"metadata\",\"stateKind\": \"protocol parameters\",\"comments\": \"\",\"lsTransaction\": null,\"ignored\": false,\"recordedDate\":    1353216427000 } ] }";
		//String json = "{ \"codeName\": \"protocolName101\",\"shortDescription\": \"protocol short description\",\"lsTransaction\": { \"comments\": \"protocol 102 transactions\",\"id\":      1,\"recordedDate\": 1.354e+12,\"version\":      0 },\"recordedBy\": \"userName\",\"recordedDate\": 1.354e+12,\"protocolStates\": [ { \"protocolValues\": [ { \"valueType\": \"stringValue\",\"valueKind\": \"reader instrument\",\"stringValue\": \"Molecular Dynamics FLIPR\",\"fileValue\": null,\"urlValue\": null,\"dateValue\": null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\": null,\"sigFigs\": null,\"uncertainty\": null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"publicData\": true,\"lsTransaction\": { \"comments\": \"protocol 102 transactions\",\"id\":      1,\"recordedDate\": 1.354e+12,\"version\":      0 },\"batchCode\": null,\"recordedDate\": 1.354e+12 },{ \"valueType\": \"numericValue\",\"valueKind\": \"curve min\",\"stringValue\": null,\"fileValue\": null,\"urlValue\": null,\"dateValue\": null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\":      0,\"sigFigs\":      2,\"uncertainty\": null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"publicData\": true,\"lsTransaction\": { \"comments\": \"protocol 102 transactions\",\"id\":      1,\"recordedDate\": 1.354e+12,\"version\":      0 },\"batchCode\": null,\"recordedDate\": 1.354e+12 },{ \"valueType\": \"numericValue\",\"valueKind\": \"curve max\",\"stringValue\": null,\"fileValue\": null,\"urlValue\": null,\"dateValue\": null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\":    100,\"sigFigs\":      2,\"uncertainty\": null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"publicData\": true,\"lsTransaction\": { \"comments\": \"protocol 102 transactions\",\"id\":      1,\"recordedDate\": 1.354e+12,\"version\":      0 },\"batchCode\": null,\"recordedDate\": 1.354e+12 } ],\"recordedBy\": \"userName\",\"stateType\": \"metadata\",\"stateKind\": \"protocol analysis parameters\",\"comments\": \"\",\"lsTransaction\": { \"comments\": \"protocol 102 transactions\",\"id\":      1,\"recordedDate\": 1.354e+12,\"version\":      0 },\"ignored\": false,\"recordedDate\": 1.354e+12 } ] }";
		String json = "{\"codeName\":\"PROT-00000006E\",\"lsType\":\"default\",\"lsKind\":\"default\",\"shortDescription\":\"protocol created by generic data parser\",\"lsTransaction\":7,\"recordedBy\":\"mcneilco\",\"recordedDate\":1398908476000,\"lsLabels\":[{\"protocol\":null,\"labelText\":\"Target Y binding\",\"recordedBy\":\"mcneilco\",\"lsType\":\"name\",\"lsKind\":\"protocol name\",\"preferred\":true,\"ignored\":false,\"lsTransaction\":7,\"recordedDate\":1398908476000}],\"lsStates\":[]}";
		Protocol protocol = Protocol.fromJsonToProtocol(json);
		logger.info("initial json values: " + protocol.toJson());
		
		Protocol output;
		try {
			output = protocolService.saveLsProtocol(protocol);
			logger.info(output.toJson());
		} catch (UniqueNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

//		output = protocolService.saveLsProtocol(Protocol.fromJsonToProtocol(json));
//		logger.info(output.toJson());

//		protocol.persist();
//		logger.info(protocol.toJson());

	}
	
	//@Test
	@Transactional
	public void CreateProtocolFromNestedJson_5(){
		//String json = "{\"experiments\":[],\"fullDescription\":\"\",\"ignored\":false,\"lsTransaction\":null,\"modifiedBy\":\"\",\"modifiedDate\":null,\"name\":\"asdf\",\"protocolStates\":[{\"recordedBy\":\"guy\",\"recordedDate\":1353225600000, \"lsTransaction\":null}],\"recordedBy\":\"guy\",\"recordedDate\":1353225600000,\"shortDescription\":\"asd\"}";
		
		//String json = "{ \"name\": \"PROT-101\",\"shortDescription\": \"Just a simple test\",\"lsTransaction\": null,\"protocolStates\": [ { \"protocolValues\": [ { \"valueType\": \"stringValue\",\"valueKind\": \"reader instrument\",\"stringValue\": \"Molecular Dynamics FLIPR\",\"fileValue\": null,\"urlValue\": null,\"dateValue\": null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\": null,\"sigFigs\": null,\"uncertainty\": null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"lsTransaction\": null,\"thingIdValue\": null,\"sampleId\": null,\"sampleName\": \"\",\"publicData\": false,\"recordedDate\":    1353267278000 },{ \"valueType\": \"numericValue\",\"valueKind\": \"curve min\",\"stringValue\": null,\"fileValue\": null,\"urlValue\": null,\"dateValue\": null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\":                0,\"sigFigs\":                2,\"uncertainty\": null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"lsTransaction\": null,\"thingIdValue\": null,\"sampleId\": null,\"sampleName\": \"\",\"publicData\": false,\"recordedDate\":    1353267278000 },{ \"valueType\": \"numericValue\",\"valueKind\": \"curve max\",\"stringValue\": null,\"fileValue\": null,\"urlValue\": null,\"dateValue\": null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\":              100,\"sigFigs\":                2,\"uncertainty\": null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"lsTransaction\": null,\"thingIdValue\": null,\"sampleId\": null,\"sampleName\": \"\",\"publicData\": false,\"recordedDate\":    1353267278000 } ],\"recordedBy\": \"userName\",\"stateType\": \"metadata\",\"stateKind\": \"protocol parameters\",\"comments\": \"\",\"lsTransaction\": null,\"ignored\": false,\"recordedDate\":    1353267278000 } ] }";
		
		String json = "{ \"name\": \"protocolName\",\"shortDescription\": \"protocol short description\",\"lsTransaction\": null,\"recordedBy\": \"userName\",\"recordedDate\":1353292248000,\"protocolStates\": [ { \"protocolValues\": [ { \"valueType\": \"stringValue\",\"valueKind\": \"reader instrument\",\"stringValue\": \"Molecular Dynamics FLIPR\",\"fileValue\": null,\"urlValue\": null,\"dateValue\": null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\": null,\"sigFigs\": null,\"uncertainty\": null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"lsTransaction\": null,\"thingIdValue\": null,\"sampleId\": null,\"sampleName\": \"\",\"publicData\": false,\"recordedDate\":1353292248000 },{ \"valueType\": \"numericValue\",\"valueKind\": \"curve min\",\"stringValue\": null,\"fileValue\": null,\"urlValue\": null,\"dateValue\": null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\":0,\"sigFigs\":2,\"uncertainty\": null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"lsTransaction\": null,\"thingIdValue\": null,\"sampleId\": null,\"sampleName\": \"\",\"publicData\": false,\"recordedDate\":1353292248000 },{ \"valueType\": \"numericValue\",\"valueKind\": \"curve max\",\"stringValue\": null,\"fileValue\": null,\"urlValue\": null,\"dateValue\": null,\"clobValue\": null,\"blobValue\": null,\"valueOperator\": null,\"numericValue\":100,\"sigFigs\":2,\"uncertainty\": null,\"valueUnit\": null,\"concValue\": null,\"concUnit\": \"\",\"comments\": null,\"ignored\": false,\"lsTransaction\": null,\"thingIdValue\": null,\"sampleId\": null,\"sampleName\": \"\",\"publicData\": false,\"recordedDate\":1353292248000 } ],\"recordedBy\": \"userName\",\"stateType\": \"metadata\",\"stateKind\": \"protocol parameters\",\"comments\": \"\",\"lsTransaction\": null,\"ignored\": false,\"recordedDate\":1353292248000 } ] }";
		
		Protocol protocol = Protocol.fromJsonToProtocol(json);
		logger.info("initial json values: " + protocol.toJson());
		Protocol output;
		try {
			output = protocolService.saveLsProtocol(protocol);
			logger.info(output.toJson());
		} catch (UniqueNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	//@Test
	@Transactional
	public void UpdateProtocol_6(){
		
		String protocolCodeName = "test protocol code name 105";
		Protocol fetchProtocol = Protocol.findProtocolsByCodeNameEquals(protocolCodeName).getSingleResult();
		fetchProtocol.setShortDescription("some new short description");
		Protocol output = protocolService.updateProtocol(fetchProtocol);
		logger.info(output.toJson());

	}

	//@Test
	@Transactional
	public void ProtocolCodetable_7(){
		
		List<CodeTableDTO> output = Protocol.getProtocolCodeTable();
		logger.info(CodeTableDTO.toJsonArray(output));

	}
	
	//@Test
	@Transactional
	public void ProtocolCodetable_8(){
		String protocolNameLike = "MetStab";
		List<CodeTableDTO> output = Protocol.getProtocolCodeTableByNameLike(protocolNameLike);
		logger.info(CodeTableDTO.toJsonArray(output));

	}
	
	@Transactional
	@Test
	public void ProtocolToJsonTest(){
		Protocol protocol = Protocol.findProtocol(1575L);
		protocol.toJson();
	}

	//Testing protocol browser search
	//TODO: make this test pass!
	//Fields that can be searched for:
	//Protocol Name
	//Code
	//Kind
	//Assay Tree Rule
	//Scientist
	//Date
	//Notebook
	//Key Words (Tags)
	//Assay Activity
	//Molecular Target
	//Target Origin
	//Assay Type
	//Assay Technology/Kit Name
	//Cell Line
	//Assay Stage
	@Transactional
	@Test
	public void protocolBrowserSearchTest() {
		String name = "FLIPR target A biochemical";
		String code = "PROT-00000005";
		String scientist = "smeyer";
		String kind = "flipr screening assay";
		String date;
		String notebook = "NB 1234-123";
		String keyWords;
		String assayActivity = "Fluorescence";
		String molecularTarget;
		String targetOrigin;
		String assayType;
		String assayTech;
		String cellLine;
		String assayStage;
		String query = name + " " + code + " " + kind  + " " + notebook;
		logger.info("Searching with the query: "+ query);
		Collection<Protocol> resultProtocols = protocolService.findProtocolsByGenericMetaDataSearch(query);
		logger.info("Found: "+ resultProtocols.toString());
		Assert.assertNotNull(resultProtocols);
		code = "PROT-";
		query = code;
		logger.info("Searching with the query: "+ query);
		resultProtocols = protocolService.findProtocolsByGenericMetaDataSearch(query);
		logger.info("Found: "+ resultProtocols.toString());
		Assert.assertNotNull(resultProtocols);
	}
	
	
	@Transactional
	@Test
	public void protocolUpdate2() {
		String json = "{\"codeName\":\"PROT-00000002\",\"deleted\":false,\"experiments\":[],\"id\":46686,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[{\"deleted\":false,\"id\":291587,\"ignored\":true,\"labelText\":\"published data\",\"lsKind\":\"protocol name\",\"lsTransaction\":4,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_protocol name\",\"modifiedDate\":1416777131198,\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"goshiro\",\"recordedDate\":1398461251000,\"version\":6},{\"deleted\":false,\"id\":291596,\"ignored\":false,\"labelText\":\"published data\",\"lsKind\":\"protocol name\",\"lsType\":\"name\",\"lsTypeAndKind\":\"name_protocol name\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"xxl7932\",\"recordedDate\":1416777131158,\"version\":0}],\"lsStates\":[{\"deleted\":false,\"id\":50733,\"ignored\":false,\"lsKind\":\"protocol metadata\",\"lsType\":\"metadata\",\"lsTypeAndKind\":\"metadata_protocol metadata\",\"lsValues\":[{\"clobValue\":\"\",\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":430507,\"ignored\":false,\"lsKind\":\"assay principle\",\"lsType\":\"clobValue\",\"lsTypeAndKind\":\"clobValue_assay principle\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"goshiro\",\"recordedDate\":1416600516194,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"clobValue\":\"\",\"codeKind\":\"\",\"codeOrigin\":\"\",\"codeType\":\"\",\"codeTypeAndKind\":\"null_null\",\"codeValue\":\"\",\"comments\":\"\",\"deleted\":false,\"fileValue\":\"\",\"id\":430508,\"ignored\":false,\"lsKind\":\"assay tree rule\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_assay tree rule\",\"modifiedBy\":\"\",\"operatorKind\":\"\",\"operatorType\":\"\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"goshiro\",\"recordedDate\":1416600516000,\"stringValue\":\"/ebola/cDNA/special assay/ex vivo/next\",\"uncertaintyType\":\"\",\"unitKind\":\"\",\"unitType\":\"\",\"unitTypeAndKind\":\"null_null\",\"urlValue\":\"\",\"version\":5},{\"codeTypeAndKind\":\"null_null\",\"dateValue\":1416643200000,\"deleted\":false,\"id\":430509,\"ignored\":false,\"lsKind\":\"completion date\",\"lsType\":\"dateValue\",\"lsTypeAndKind\":\"dateValue_completion date\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"goshiro\",\"recordedDate\":1416600516194,\"unitTypeAndKind\":\"null_null\",\"version\":1},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":430505,\"ignored\":false,\"lsKind\":\"notebook\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_notebook\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"goshiro\",\"recordedDate\":1416600516194,\"stringValue\":\"JMCO-0001-002\",\"unitTypeAndKind\":\"null_null\",\"version\":1},{\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":430506,\"ignored\":false,\"lsKind\":\"status\",\"lsType\":\"stringValue\",\"lsTypeAndKind\":\"stringValue_status\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"goshiro\",\"recordedDate\":1416600516194,\"stringValue\":\"created\",\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"clobValue\":\"\",\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":430512,\"ignored\":false,\"lsKind\":\"description\",\"lsType\":\"clobValue\",\"lsTypeAndKind\":\"clobValue_description\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"goshiro\",\"recordedDate\":1416600516194,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"clobValue\":\"\",\"codeTypeAndKind\":\"null_null\",\"deleted\":false,\"id\":430511,\"ignored\":false,\"lsKind\":\"comments\",\"lsType\":\"clobValue\",\"lsTypeAndKind\":\"clobValue_comments\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"goshiro\",\"recordedDate\":1416600516194,\"unitTypeAndKind\":\"null_null\",\"version\":0},{\"codeKind\":\"assay stage\",\"codeOrigin\":\"acas ddict\",\"codeType\":\"protocolMetadata\",\"codeTypeAndKind\":\"protocolMetadata_assay stage\",\"codeValue\":\"unassigned\",\"deleted\":false,\"id\":430510,\"ignored\":false,\"lsKind\":\"assay stage\",\"lsType\":\"codeValue\",\"lsTypeAndKind\":\"codeValue_assay stage\",\"operatorTypeAndKind\":\"null_null\",\"publicData\":false,\"recordedBy\":\"goshiro\",\"recordedDate\":1416600516194,\"unitTypeAndKind\":\"null_null\",\"version\":0}],\"modifiedDate\":1416777131203,\"recordedBy\":\"goshiro\",\"recordedDate\":1416600516194,\"version\":4}],\"lsTags\":[{\"tagText\":\"pineapple\"},{\"tagText\":\"gene\"},{\"tagText\":\"tag\"},{\"tagText\":\"guava\"},{\"tagText\":\"key\"},{\"tagText\":\"lobster\"}],\"lsTransaction\":4,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"modifiedDate\":1416777131188,\"recordedBy\":\"xxl7932\",\"recordedDate\":1416960127895,\"shortDescription\":\"protocol created by generic data parser\",\"version\":11}";
		Protocol protocol = Protocol.fromJsonToProtocol(json);
		protocolService.updateProtocol(protocol);
				
	}
	
	
	//@Transactional
	@Test
	public void protocolBrowserFilterTest() {
		String name = "PAMPA Buffer A";
		String codeName = "PROT-00000001";
		String type = "default";
		String kind = "default";
		
		
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("name", name);
		
		Set<Protocol> protocols = protocolService.findProtocolsByRequestMetadata(requestParams);
		Assert.assertEquals(1, protocols.size());
		protocols.clear();
		
		requestParams.put("codeName", codeName);
		protocols = protocolService.findProtocolsByRequestMetadata(requestParams);
		Assert.assertEquals(1, protocols.size());
		protocols.clear();
		
		requestParams.put("type", type);
		requestParams.put("kind", kind);
		
		protocols = protocolService.findProtocolsByRequestMetadata(requestParams);
		Assert.assertEquals(1, protocols.size());
		protocols.clear();
		
		requestParams.remove("name");
		protocols = protocolService.findProtocolsByRequestMetadata(requestParams);
		Assert.assertEquals(1, protocols.size());
		
	}
	
	//@Test
	@Transactional
	public void uniqueProtocolNameExceptionTest() throws UniqueNameException {
		String name = "APMS";
		String json = "{\"codeName\":null,\"deleted\":false,\"experiments\":[],\"id\":null,\"ignored\":false,\"lsKind\":\"default\",\"lsLabels\":[{\"deleted\":false,\"id\":null,\"ignored\":false,\"labelText\":\"APMS\",\"lsKind\":\"protocol name\",\"lsTransaction\":5,\"lsType\":\"name\",\"lsTypeAndKind\":\"name_protocol name\",\"physicallyLabled\":false,\"preferred\":true,\"recordedBy\":\"nouser\",\"recordedDate\":1395708972000,\"version\":null}],\"lsStates\":[],\"lsTags\":[],\"lsTransaction\":5,\"lsType\":\"default\",\"lsTypeAndKind\":\"default_default\",\"recordedBy\":\"nouser\",\"recordedDate\":1395708972000,\"shortDescription\":\"protocol created by generic data parser\",\"version\":null}";
		Protocol protocol = Protocol.fromJsonToProtocol(json);
		try {
			protocolService.saveLsProtocol(protocol);
		} catch (UniqueNameException e) {
			Assert.assertNotNull(e);
		}
	}
	
	@Test
//	@Transactional
	public void createProtocolStatusTest() {
		Protocol protocol = Protocol.findProtocol(702987L);
		ProtocolValue protocolValue = protocolValueService.updateProtocolValue(protocol.getCodeName(), "metadata", "protocol metadata", "codeValue", "protocol status", "created");
		String createdProtocolStatus = protocolValue.getCodeValue();
		Assert.assertEquals("created", createdProtocolStatus);		
	}
	
	@Test
	@Transactional
	public void updateProtocolStatusTest() {
		Protocol protocol = Protocol.findProtocol(702987L);
		ProtocolValue protocolValue = ProtocolValue.findProtocolValuesByProtocolIDAndStateTypeKindAndValueTypeKind(702987L, "metadata", "protocol metadata", "codeValue", "protocol status").getSingleResult();
		String originalProtocolStatus = protocolValue.getCodeValue();
		Assert.assertTrue(!originalProtocolStatus.equals("deleted"));
		ProtocolValue protocolValue2 = protocolValueService.updateProtocolValue(protocol.getCodeName(), "metadata", "protocol metadata", "codeValue", "protocol status", "deleted");
		protocol.setIgnored(true);
		String deletedProtocolStatus = protocolValue2.getCodeValue();
		ProtocolValue protocolValue3 = ProtocolValue.findProtocolValuesByProtocolIDAndStateTypeKindAndValueTypeKind(702987L, "metadata", "protocol metadata", "codeValue", "protocol status").getSingleResult();
		String checkDeletedProtocolStatus = protocolValue3.getCodeValue();
		Assert.assertEquals("deleted", deletedProtocolStatus);
		Assert.assertEquals("deleted", checkDeletedProtocolStatus);
		
	}
	
	@Test
	@Transactional
	public void findScientistProtocolValue() {
		Collection<ProtocolValue> protocolValues  = ProtocolValue.findProtocolValuesByLsKindEqualsAndStringValueLike("scientist", "*").getResultList();
		logger.info(ProtocolValue.toJsonArray(protocolValues));
	}
}
