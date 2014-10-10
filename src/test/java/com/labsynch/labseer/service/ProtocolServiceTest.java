

package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

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
import com.labsynch.labseer.dto.CodeTableDTO;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
public class ProtocolServiceTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ProtocolServiceTest.class);
	
	@Autowired
	private ProtocolService protocolService;
	
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
		
		Protocol output = protocolService.saveLsProtocol(protocol);
		logger.info(output.toJson());

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
		Protocol output = protocolService.saveLsProtocol(protocol);
		logger.info(output.toJson());

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

}
