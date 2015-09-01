

package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.LsThingLabel;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Configurable
public class DiscontinuedGenesFromCSVFileTests {

	private static final Logger logger = LoggerFactory.getLogger(DiscontinuedGenesFromCSVFileTests.class);

	@Autowired
	public AutoLabelService autoLabelService;

	@Autowired
	public GeneThingService geneThingService;

	@Test
	@Transactional
	public void ReadCSVFile_Test1() throws IOException{

		String entrezGenesFile = "/Users/goshiro2014/temp/Mus_musculus-test.gene_info";
		String geneHistoryFile = "/Users/goshiro2014/temp/mouseGeneHistory-missed.txt";
		String taxonomyId = "10090";
		
//		String entrezGenesFile = "/Users/goshiro2014/Documents/McNeilco_2012/clients/Dart/acasDataExplorer/genesToLoad/sampleHumanGenes.txt";
//		String geneHistoryFile = "/Users/goshiro2014/Documents/McNeilco_2012/clients/Dart/acasDataExplorer/genesToLoad/sample_human_history.txt";
//		String taxonomyId = "9606";
		
//		String entrezGenesFile = "/Users/goshiro2014/Documents/McNeilco_2012/clients/Dart/acasDataExplorer/genesToLoad/fly_sample_gene.txt";
//		String geneHistoryFile = "/Users/goshiro2014/Documents/McNeilco_2012/clients/Dart/acasDataExplorer/genesToLoad/fly_sample_history.txt";
//		String taxonomyId = "7227";
		

		geneThingService.updateEntrezGenes(entrezGenesFile, geneHistoryFile, taxonomyId);
		
		List<LsThingLabel> results = LsThingLabel.findLsThingLabelsByLabelTextEquals("633417").getResultList();
		logger.info("number of genes found: " + results.size());
		for (LsThingLabel result : results){
			logger.debug(result.toJson());
		}
		
		List<LsThingLabel> results2 = LsThingLabel.findLsThingLabelsByLabelTextEqualsAndIgnoredNot("633417", true).getResultList();
		logger.info("--------- number of not ignored genes found: " + results2.size());
		for (LsThingLabel result : results2){
			logger.debug(result.toJson());
		}


	}

}
