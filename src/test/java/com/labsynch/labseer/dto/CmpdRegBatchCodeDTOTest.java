package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import junit.framework.Assert;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/META-INF/spring/applicationContext.xml", "classpath:/META-INF/spring/applicationContext-security.xml"})
@Configurable
@Transactional
public class CmpdRegBatchCodeDTOTest {
	
	private static final Logger logger = LoggerFactory.getLogger(CmpdRegBatchCodeDTOTest.class);

	@Test
	public void checkForDependentDataTest() {
		Collection<String> batchCodes = new HashSet<String>();
		batchCodes.add("CMPD-0000001-01A");
		batchCodes.add("CMPD-0000002-01A");
		batchCodes.add("CMPD-0000003-01A");
		batchCodes.add("NOT-A-VALID-BATCH-CODE");
		
		CmpdRegBatchCodeDTO cmpdRegBatchCodeDTO = new CmpdRegBatchCodeDTO(batchCodes);
		cmpdRegBatchCodeDTO.checkForDependentData();
		logger.info(cmpdRegBatchCodeDTO.toJson());
		
		//check for duplicate codes
		ArrayList<String> codeList = new ArrayList<String>();
		HashSet<String> codeSet = new HashSet<String>();
		for (CodeTableDTO codeTable : cmpdRegBatchCodeDTO.getLinkedExperiments()){
			codeList.add(codeTable.getCode());
			codeSet.add(codeTable.getCode());
		}
		Assert.assertEquals(codeSet.size(), codeList.size());
		
		Assert.assertTrue(cmpdRegBatchCodeDTO.getLinkedDataExists());
		Assert.assertFalse(cmpdRegBatchCodeDTO.getLinkedExperiments().isEmpty());
	}
	
	@Test
	public void checkForDependentDataTestNoData() {
		Collection<String> batchCodes = new HashSet<String>();
		batchCodes.add("NOT-A-VALID-BATCH-CODE");
		
		CmpdRegBatchCodeDTO cmpdRegBatchCodeDTO = new CmpdRegBatchCodeDTO(batchCodes);
		cmpdRegBatchCodeDTO.checkForDependentData();
		logger.info(cmpdRegBatchCodeDTO.toJson());
		Assert.assertFalse(cmpdRegBatchCodeDTO.getLinkedDataExists());
		Assert.assertTrue(cmpdRegBatchCodeDTO.getLinkedExperiments().isEmpty());
	}
	
	@Test
	public void toFromJsonTest(){
		Collection<String> batchCodes = new HashSet<String>();
		batchCodes.add("CMPD-0000001-01A");
		batchCodes.add("CMPD-0000002-01A");
		batchCodes.add("CMPD-0000003-01A");
		batchCodes.add("NOT-A-VALID-BATCH-CODE");
		
		CmpdRegBatchCodeDTO cmpdRegBatchCodeDTO = new CmpdRegBatchCodeDTO(batchCodes);
		logger.info("starting json: "+cmpdRegBatchCodeDTO.toJson());
		Assert.assertTrue(cmpdRegBatchCodeDTO.toJson().indexOf("linkedExperiments") == -1);
		cmpdRegBatchCodeDTO.checkForDependentData();
		logger.info("after check json: "+cmpdRegBatchCodeDTO.toJson());
		Assert.assertFalse(cmpdRegBatchCodeDTO.toJson().indexOf("linkedExperiments") == -1);
		CmpdRegBatchCodeDTO checkDTO = CmpdRegBatchCodeDTO.fromJsonToCmpdRegBatchCodeDTO(cmpdRegBatchCodeDTO.toJson());
		logger.info("after to/from json: "+checkDTO.toJson());
		Assert.assertFalse(cmpdRegBatchCodeDTO.toJson().indexOf("linkedExperiments") == -1);

	}
	
}
