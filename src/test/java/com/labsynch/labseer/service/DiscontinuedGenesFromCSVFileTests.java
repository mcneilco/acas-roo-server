

package com.labsynch.labseer.service;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Configurable
public class DiscontinuedGenesFromCSVFileTests {

	private static final Logger logger = LoggerFactory.getLogger(DiscontinuedGenesFromCSVFileTests.class);

	@Autowired
	private AutoLabelService autoLabelService;

	@Autowired
	private GeneThingService geneThingService;

	@Test
	@Transactional
	public void ReadCSVFile_Test1() throws IOException{

//		String entrezGenesFile = "/Users/goshiro2014/Documents/McNeilco_2012/clients/Dart/acasDataExplorer/genesToLoad/sampleNewGenes.txt";
//		String geneHistoryFile = "/Users/goshiro2014/Documents/McNeilco_2012/clients/Dart/acasDataExplorer/genesToLoad/sample_human_history.txt";
//		String taxonomyId = "9606";
		
		String entrezGenesFile = "/Users/goshiro2014/Documents/McNeilco_2012/clients/Dart/acasDataExplorer/genesToLoad/fly_sample_gene.txt";
		String geneHistoryFile = "/Users/goshiro2014/Documents/McNeilco_2012/clients/Dart/acasDataExplorer/genesToLoad/fly_sample_history.txt";
		String taxonomyId = "7227";
		

		geneThingService.updateEntrezGenes(entrezGenesFile, geneHistoryFile, taxonomyId);


	}

}
