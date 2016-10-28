

package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.dto.GeneIdDTO;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Configurable
public class GeneThingServiceTests {

	private static final Logger logger = LoggerFactory.getLogger(GeneThingServiceTests.class);

	@Autowired
	private GeneThingService geneThingService;

	//@Test
	public void Test_1() throws IOException{
		String testFileName = "/tmp/sample_genes.txt";
		logger.info("testing loading genes: " + testFileName);
		geneThingService.RegisterGeneThingsFromCSV(testFileName);

		
	}
	
	@Test
	public void Test_2() throws IOException{
		String geneTypeString = "gene";
		String geneKindString = "entrez gene";
		List<LsThingLabel> geneIdList = LsThingLabel.findLsThingName(geneTypeString, geneKindString, "name", "Entrez Gene ID", "1").getResultList();
		logger.info("gene count is " + geneIdList.size());
		
	}

	@Test
	public void Test_3() throws IOException{
		
//		BatchCodeDTO ack = new BatchCodeDTO();
//		ack.setBatchCode("apple");
//		logger.info(ack.toJson());

		
		
		
//		String json = "{\"batchCode\":\"apple\"}";
//		BatchCodeDTO ack = BatchCodeDTO.fromJsonToBatchCodeDTO(json);
//		logger.info(ack.toJson());
		String json = "[{\"gid\":\"1\"}]";
		Collection<GeneIdDTO> batchCodes = GeneIdDTO.fromJsonArrayToGeneIdDTO(json);
		for (GeneIdDTO bc : batchCodes){
			logger.info("bc: " + bc.getGid());
		}

	}
	

}
