

package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.LabelKind;
import com.labsynch.labseer.domain.LabelSequence;
import com.labsynch.labseer.domain.LabelType;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.LsThingLabel;
import com.labsynch.labseer.domain.LsThingState;
import com.labsynch.labseer.domain.LsThingValue;
import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.domain.StateKind;
import com.labsynch.labseer.domain.StateType;
import com.labsynch.labseer.domain.ThingKind;
import com.labsynch.labseer.domain.ThingType;
import com.labsynch.labseer.domain.ValueKind;
import com.labsynch.labseer.domain.ValueType;
import com.labsynch.labseer.dto.AutoLabelDTO;
import com.labsynch.labseer.dto.EntrezDbGeneDTO;
import com.labsynch.labseer.dto.EntrezDiscontinuedGeneDTO;
import com.labsynch.labseer.utils.SimpleUtil;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Configurable
public class DiscontinuedGenesFromCSVFileTests {

	private static final Logger logger = LoggerFactory.getLogger(DiscontinuedGenesFromCSVFileTests.class);

	@Autowired
	private AutoLabelService autoLabelService;

	@Autowired
	private GeneThingService geneThingService;

	private String fieldDelimiter = "\t";

	@Test
	@Transactional
	public void ReadCSVFile_Test1() throws IOException{

		String testFileName = "discontinuedGenes.txt";

		String entrezGenesFile = "/Users/goshiro2014/Documents/McNeilco_2012/clients/Dart/acasDataExplorer/genesToLoad/sampleNewGenes.txt";

//		String entrezGenesFile = "/Users/goshiro2014/Documents/McNeilco_2012/clients/Dart/acasDataExplorer/genesToLoad/sampleOldGenes.txt";
//		String entrezGenesFile = "/Users/goshiro2014/Documents/McNeilco_2012/clients/NewAcasRoo/code/src/test/resources/sample_genes.txt";

		String geneHistoryFile = "/Users/goshiro2014/Documents/McNeilco_2012/clients/Dart/acasDataExplorer/genesToLoad/sample_human_history.txt";
				//"/Users/goshiro2014/Documents/McNeilco_2012/clients/NewAcasRoo/code/src/test/resources/discontinuedGenes.txt";
		String taxonomyId = "9606";

		geneThingService.updateEntrezGenes(entrezGenesFile, geneHistoryFile, taxonomyId);


	}


	private void updateEntrezGenes(String entrezGenes, String geneHistory,
			String taxonomyId, LsTransaction lsTransaction) throws IOException {

		GeneHistoryDTO historyResults = processGeneHistory(geneHistory, taxonomyId);

		processEntrezGenes(entrezGenes, historyResults, taxonomyId, lsTransaction );

		//processDiscontinuedGenes(historyResults.getDiscontinuedGenes(), taxonomyId, lsTransaction );

	}


	private void processDiscontinuedGenes(HashMap<String, EntrezDiscontinuedGeneDTO> discontinuedGenes,
			String taxonomyId, LsTransaction lsTransaction) {

		EntrezDiscontinuedGeneDTO discontinuedGene;
		String geneTypeString = "gene";
		String geneKindString = "entrez gene";
		LsThing geneThing = null;
		for (String key : discontinuedGenes.keySet()){
			discontinuedGene = discontinuedGenes.get(key);
			// check if gene currently exists
			Long geneIdCount = LsThingLabel.countOfLsThingByName(geneTypeString, geneKindString, "name", "Entrez Gene ID", discontinuedGene.getDiscontinuedGeneId());
			if (geneIdCount == 0){
				geneThing = createDiscontinuedGene(discontinuedGene, lsTransaction);
			}
		}

	}


	private void processEntrezGenes(String entrezGenes, GeneHistoryDTO historyResults, 
			String taxonomyId, LsTransaction lsTransaction) throws IOException {
		// TODO Auto-generated method stub

		geneThingService.setGeneDefaults();

		logger.info("read tab delimited file");
		BufferedReader br = new BufferedReader(new FileReader(entrezGenes));

		ICsvBeanReader beanReader = null;
		try {

			beanReader = new CsvBeanReader(br, CsvPreference.TAB_PREFERENCE);
			String[] headerText = beanReader.getHeader(true);

			String geneTypeString = "gene";
			String geneKindString = "entrez gene";

			// map header text to standard bean names

			HashMap<String, String> geneBeanMap = new HashMap<String, String>();
			geneBeanMap.put("tax_id", "taxId");
			geneBeanMap.put("GeneID", "geneId");
			geneBeanMap.put("Symbol", "symbol");
			geneBeanMap.put("LocusTag", "locusTag");
			geneBeanMap.put("Synonyms", "synonyms");
			geneBeanMap.put("dbXrefs", "dbXrefs");
			geneBeanMap.put("chromosome", "chromosome");
			geneBeanMap.put("map_location", "mapLocation");
			geneBeanMap.put("description", "description");
			geneBeanMap.put("type_of_gene", "typeOfGene");
			geneBeanMap.put("Symbol_from_nomenclature_authority", "symbolFromAuthority");
			geneBeanMap.put("Full_name_from_nomenclature_authority", "fullNameFromAuthority");
			geneBeanMap.put("Nomenclature_status", "nomenclatureStatus");
			geneBeanMap.put("Other_designations", "otherDesignations");
			geneBeanMap.put("Modification_date", "modificationDate");

			List<String> headerList = new ArrayList<String>();
			int position = 0;
			for (String head : headerText){
				logger.info("header column: " + position + "  " + head);
				if (geneBeanMap.get(head) != null){
					headerList.add(geneBeanMap.get(head));   				
				}
				position++;
			}


			logger.info("size of header list  " + headerList.size());
			String[] header = new String[headerList.size()];
			headerList.toArray(header);

			for (String head : header){
				logger.warn("header column array : " + position + "  " + head);
				position++;
			}


			final CellProcessor[] processors = geneThingService.getProcessors();

			EntrezDbGeneDTO geneDTO;

			//			int numberOfLines = SimpleUtil.countLines(entrezGenes);
			//			Long numberOfLabels = new Long(numberOfLines-1);
			//			
			//			List<AutoLabelDTO> thingCodes = autoLabelService.getAutoLabels(thingTypeAndKind, labelTypeAndKind, numberOfLabels);

			int i = 0;
			int batchSize = 25;
			LsThing geneThing = null;
			while( (geneDTO = beanReader.read(EntrezDbGeneDTO.class, header, processors)) != null ) {
				System.out.println(String.format("lineNo=%s, rowNo=%s, bulkData=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), geneDTO));
				logger.info("current gene: " + geneDTO.getGeneId());

				// check if gene currently exists
				Long geneIdCount = LsThingLabel.countOfLsThingByName(geneTypeString, geneKindString, "name", "Entrez Gene ID", geneDTO.getGeneId());
//				Long oldGeneIdCount = LsThingLabel.countOfLsThingByName(geneTypeString, geneKindString, "name", "Entrez Gene ID", historyResults.getUpdatedGenes().get(geneDTO.getGeneId()).getDiscontinuedGeneId());

				// check if discontinued gene
				boolean discontinuedGene = false;
				if (historyResults.getDiscontinuedGenes().containsKey(geneDTO.getGeneId())) {
					logger.debug("discontinued gene");
					discontinuedGene = true;
				}

				// check if updated gene
				boolean updatedGene = false;
				if (historyResults.getUpdatedGenes().containsKey(geneDTO.getGeneId())) {
					logger.debug("updated gene");
					updatedGene = true;
				}


				// use cases -- flipping through entrez gene list
				// 1. gene ID not registered and in list of discontinued genes --> register discontinued gene
				// 2. gene ID not registered and in list of updated genes --> update gene entry; 
				//       set old gene ID to not preferred, 
				//       add new gene ID as the preferred; 
				//       ignore old gene metadata, 
				//       add new gene metadata
				// 3. index through discontinued genes -- register all genes not already registered 


				if (discontinuedGene && geneIdCount == 0L){
					//create new entry for discontinued gene
					geneThing = geneThingService.registerDiscontinuedGene(historyResults.getDiscontinuedGenes().get(geneDTO.getGeneId()), lsTransaction);

				} else if (updatedGene ){
					// updating existing gene info with new gene label and meta (merging genes if new gene does not exist)
					// else keep 2 gene entities
					geneThing = geneThingService.registerUpdatedGene(geneDTO, historyResults.getUpdatedGenes().get(geneDTO.getGeneId()), lsTransaction);					
					if (logger.isDebugEnabled()) logger.debug("updated the gene entry: " + LsThing.findLsThing(geneThing.getId()).toPrettyJson());

				} else if (geneIdCount == 0L ) {
					// brand new gene to register
					if (logger.isDebugEnabled()) logger.debug("registering new gene: " + geneDTO.getGeneId());
					geneThing = geneThingService.registerNewGene(geneDTO, null, lsTransaction);

				} else {
					if (logger.isDebugEnabled()) logger.debug("gene already registered: " + geneDTO.getGeneId());
				}

				if ( i % batchSize == 0 ) { // same as the JDBC batch size
					if (geneThing != null){
						geneThing.flush();
						geneThing.clear();						
					}
				}
				i++;

			}
		}
		finally {
			if( beanReader != null ) {
				beanReader.close();
			}
			if (br != null)br.close();
		}

	}






	private LsTransaction createLsTransaction(String txComment) {
		LsTransaction lsTransaction = new LsTransaction();
		lsTransaction.setComments(txComment);
		lsTransaction.setRecordedDate(new Date());
		lsTransaction.persist();
		return lsTransaction;
	}


	private class GeneHistoryDTO {
		private HashMap<String, EntrezDiscontinuedGeneDTO> discontinuedGenes;
		private HashMap<String, EntrezDiscontinuedGeneDTO> updatedGenes;
		public HashMap<String, EntrezDiscontinuedGeneDTO> getDiscontinuedGenes() {
			return discontinuedGenes;
		}
		public void setDiscontinuedGenes(HashMap<String, EntrezDiscontinuedGeneDTO> discontinuedGenes) {
			this.discontinuedGenes = discontinuedGenes;
		}
		public HashMap<String, EntrezDiscontinuedGeneDTO> getUpdatedGenes() {
			return updatedGenes;
		}
		public void setUpdatedGenes(HashMap<String, EntrezDiscontinuedGeneDTO> updatedGenes) {
			this.updatedGenes = updatedGenes;
		}
	}

	private void addDiscontinuedGenes(String testFileName, String taxonomyId, LsTransaction lsTransaction) throws IOException {

		logger.info("read tab delimited file");
		InputStream is = DiscontinuedGenesFromCSVFileTests.class.getClassLoader().getResourceAsStream(testFileName);
		InputStreamReader isr = new InputStreamReader(is);  
		BufferedReader br = new BufferedReader(isr);

		GeneHistoryDTO historyResults = processGeneHistory(testFileName, taxonomyId);

		HashMap<String, EntrezDiscontinuedGeneDTO> discontinuedGenes = historyResults.getDiscontinuedGenes();
		HashMap<String, EntrezDiscontinuedGeneDTO> updatedGenes = historyResults.getUpdatedGenes();

		logger.info("Number of discontinued genes is: " + discontinuedGenes.size());
		logger.info("Number of updated genes is: " + updatedGenes.size());

		String geneTypeString = "gene";
		String geneKindString = "entrez gene";

		EntrezDiscontinuedGeneDTO geneDTO;
		int counter = 0;
		for (String geneId : discontinuedGenes.keySet()){
			geneDTO = discontinuedGenes.get(geneId);
			List<LsThing> genes = LsThing.findLsThingByLabelText(geneTypeString, geneKindString, "name", "Entrez Gene ID", geneDTO.getDiscontinuedGeneId()).getResultList();
			if (genes.size() == 0){
				logger.info("Need to create the discontinued gene entry");
				counter++;
				//createDiscontinuedGene(geneDTO, lsTransaction);

			} else {
				logger.info("update the gene entry" + geneDTO.getDiscontinuedGeneId());
			}

		}
		logger.info("number of genes to create: " + counter);


	}


	private HashMap<String, EntrezDiscontinuedGeneDTO> getUpdatedGeneList(String testFileName) throws IOException {

		InputStream is = DiscontinuedGenesFromCSVFileTests.class.getClassLoader().getResourceAsStream(testFileName);
		InputStreamReader isr = new InputStreamReader(is);  
		BufferedReader br = new BufferedReader(isr);

		ICsvBeanReader beanReader = null;
		final CellProcessor[] processors = getDiscontinuedGenesProcessors();
		EntrezDiscontinuedGeneDTO geneDTO;
		HashMap<String, String> geneBeanMap = new HashMap<String, String>();
		geneBeanMap.put("tax_id", "taxId");
		geneBeanMap.put("GeneID", "geneId");
		geneBeanMap.put("Discontinued_GeneID", "discontinuedGeneId");
		geneBeanMap.put("Discontinued_Symbol", "discontinuedSymbol");
		geneBeanMap.put("Discontinue_Date", "discontinuedDate");

		HashMap<String, EntrezDiscontinuedGeneDTO> geneList = new HashMap<String, EntrezDiscontinuedGeneDTO>();

		try {
			beanReader = new CsvBeanReader(br, CsvPreference.TAB_PREFERENCE);
			String[] headerText = beanReader.getHeader(true);
			List<String> headerList = new ArrayList<String>();
			int position = 0;
			for (String head : headerText){
				logger.info("header column: " + position + "  " + head);
				if (geneBeanMap.get(head) != null){
					headerList.add(geneBeanMap.get(head));   				
				}
				position++;
			}


			logger.info("size of header list  " + headerList.size());
			String[] header = new String[headerList.size()];
			headerList.toArray(header);

			for (String head : header){
				logger.warn("header column array : " + position + "  " + head);
				position++;
			}

			while( (geneDTO = beanReader.read(EntrezDiscontinuedGeneDTO.class, header, processors)) != null ) {
				System.out.println(String.format("lineNo=%s, rowNo=%s, bulkData=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), geneDTO));
				logger.info("current gene: " + geneDTO.getGeneId());
				if (!geneDTO.getGeneId().equalsIgnoreCase("-")){
					geneList.put(geneDTO.getGeneId(), geneDTO);					
				} 
			}
			logger.info("size of the updated geneList hash is : " + geneList.size());

		}		
		finally {
			if( beanReader != null ) {
				beanReader.close();
			}
		}	

		return geneList;
	}


	private LsThing createDiscontinuedGene(EntrezDiscontinuedGeneDTO geneDTO, LsTransaction lsTransaction) {
		logger.info("create discontinued gene: " + geneDTO.getDiscontinuedGeneId());
		String geneTypeString = "gene";
		String geneKindString = "entrez gene";
		ThingType geneType = ThingType.getOrCreate(geneTypeString);
		ThingKind.getOrCreate(geneType, geneKindString);

		LabelType labelType = LabelType.getOrCreate("name");
		LabelKind.getOrCreate(labelType, "gene code name");
		LabelKind.getOrCreate(labelType, "Entrez Gene ID");
		LabelKind.getOrCreate(labelType, "gene symbol");

		StateType stateType = StateType.getOrCreate("metadata");
		StateKind.getOrCreate(stateType, "gene metadata");

		ValueType valueType = ValueType.getOrCreate("stringValue");
		ValueKind.getOrCreate(valueType, "discontinued gene id");
		ValueKind.getOrCreate(valueType, "discontinued gene symbol");
		ValueKind.getOrCreate(valueType, "discontinued date");
		ValueKind.getOrCreate(valueType, "discontinued gene");
		ValueType valueTypeDate = ValueType.getOrCreate("dateValue");
		ValueKind.getOrCreate(valueTypeDate, "modification date");


		LsThing geneThing = new LsThing();
		geneThing.setCodeName(geneThingService.getGeneCodeName());
		geneThing.setLsType(geneTypeString);
		geneThing.setLsKind(geneKindString);
		geneThing.setLsTransaction(lsTransaction.getId());
		geneThing.setRecordedBy("acas admin");
		geneThing.persist();

		String entrezSeparator = "\\|";
		saveGeneLabel(lsTransaction, geneThing, "name", "Entrez Gene ID", true, geneDTO.getDiscontinuedGeneId(), entrezSeparator);
		saveGeneLabel(lsTransaction, geneThing, "name", "gene symbol", false, geneDTO.getDiscontinuedSymbol(), entrezSeparator);

		LsThingState geneState = new LsThingState();
		geneState.setLsThing(geneThing);
		geneState.setLsType("metadata");
		geneState.setLsKind("gene metadata");
		geneState.setLsTransaction(lsTransaction.getId());
		geneState.setRecordedBy("acas admin");
		geneState.persist();

		saveGeneDescriptor(lsTransaction, geneState, "stringValue", "tax id", geneDTO.getTaxId(), entrezSeparator);
		saveGeneDescriptor(lsTransaction, geneState, "stringValue", "discontinued gene", "true", entrezSeparator);
		saveGeneDescriptor(lsTransaction, geneState, "stringValue", "discontinued gene id", geneDTO.getDiscontinuedGeneId(), entrezSeparator);
		saveGeneDescriptor(lsTransaction, geneState, "stringValue", "discontinued gene symbol", geneDTO.getDiscontinuedSymbol(), entrezSeparator);
		saveGeneDescriptor(lsTransaction, geneState, "dateValue", "discontinued date", geneDTO.getDiscontinuedDate());

		return geneThing;

	}


	private HashMap<String, EntrezDiscontinuedGeneDTO> getDiscontinuedGeneList(String testFileName) throws IOException {
		InputStream is = DiscontinuedGenesFromCSVFileTests.class.getClassLoader().getResourceAsStream(testFileName);
		InputStreamReader isr = new InputStreamReader(is);  
		BufferedReader br = new BufferedReader(isr);

		ICsvBeanReader beanReader = null;
		final CellProcessor[] processors = getDiscontinuedGenesProcessors();
		EntrezDiscontinuedGeneDTO geneDTO;
		HashMap<String, String> geneBeanMap = new HashMap<String, String>();
		geneBeanMap.put("tax_id", "taxId");
		geneBeanMap.put("GeneID", "geneId");
		geneBeanMap.put("Discontinued_GeneID", "discontinuedGeneId");
		geneBeanMap.put("Discontinued_Symbol", "discontinuedSymbol");
		geneBeanMap.put("Discontinue_Date", "discontinuedDate");

		HashMap<String, EntrezDiscontinuedGeneDTO> geneList = new HashMap<String, EntrezDiscontinuedGeneDTO>();

		try {
			beanReader = new CsvBeanReader(br, CsvPreference.TAB_PREFERENCE);
			String[] headerText = beanReader.getHeader(true);
			List<String> headerList = new ArrayList<String>();
			int position = 0;
			for (String head : headerText){
				logger.info("header column: " + position + "  " + head);
				if (geneBeanMap.get(head) != null){
					headerList.add(geneBeanMap.get(head));   				
				}
				position++;
			}


			logger.info("size of header list  " + headerList.size());
			String[] header = new String[headerList.size()];
			headerList.toArray(header);

			for (String head : header){
				logger.warn("header column array : " + position + "  " + head);
				position++;
			}

			while( (geneDTO = beanReader.read(EntrezDiscontinuedGeneDTO.class, header, processors)) != null ) {
				System.out.println(String.format("lineNo=%s, rowNo=%s, bulkData=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), geneDTO));
				logger.info("current gene: " + geneDTO.getGeneId());
				if (geneDTO.getGeneId().equalsIgnoreCase("-")){
					geneList.put(geneDTO.getDiscontinuedGeneId(), geneDTO);					
				} 
			}
			logger.info("size of the geneList hash is : " + geneList.size());

		}		
		finally {
			if( beanReader != null ) {
				beanReader.close();
			}
		}	

		return geneList;
	}


	private GeneHistoryDTO processGeneHistory(String geneHistoryFileName, String taxonomyId) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(geneHistoryFileName));
		ICsvBeanReader beanReader = null;
		final CellProcessor[] processors = getDiscontinuedGenesProcessors();
		EntrezDiscontinuedGeneDTO geneDTO;
		HashMap<String, String> geneBeanMap = new HashMap<String, String>();
		geneBeanMap.put("tax_id", "taxId");
		geneBeanMap.put("GeneID", "geneId");
		geneBeanMap.put("Discontinued_GeneID", "discontinuedGeneId");
		geneBeanMap.put("Discontinued_Symbol", "discontinuedSymbol");
		geneBeanMap.put("Discontinue_Date", "discontinuedDate");

		HashMap<String, EntrezDiscontinuedGeneDTO> updatedGenes = new HashMap<String, EntrezDiscontinuedGeneDTO>();
		HashMap<String, EntrezDiscontinuedGeneDTO> discontinuedGenes = new HashMap<String, EntrezDiscontinuedGeneDTO>();

		try {
			beanReader = new CsvBeanReader(br, CsvPreference.TAB_PREFERENCE);
			String[] headerText = beanReader.getHeader(true);
			List<String> headerList = new ArrayList<String>();
			int position = 0;
			for (String head : headerText){
				logger.info("header column: " + position + "  " + head);
				if (geneBeanMap.get(head) != null){
					headerList.add(geneBeanMap.get(head));   				
				}
				position++;
			}


			logger.info("size of header list  " + headerList.size());
			String[] header = new String[headerList.size()];
			headerList.toArray(header);

			for (String head : header){
				logger.warn("header column array : " + position + "  " + head);
				position++;
			}

			while( (geneDTO = beanReader.read(EntrezDiscontinuedGeneDTO.class, header, processors)) != null ) {
				System.out.println(String.format("lineNo=%s, rowNo=%s, bulkData=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), geneDTO));
				logger.info("current gene: " + geneDTO.getGeneId());
				if (geneDTO.getTaxId().equalsIgnoreCase(taxonomyId)){
					if (geneDTO.getGeneId().equalsIgnoreCase("-")){
						discontinuedGenes.put(geneDTO.getDiscontinuedGeneId(), geneDTO);					
					} else {
						updatedGenes.put(geneDTO.getGeneId(), geneDTO);
					}
				}

			}
			logger.info("size of the discontinuedGenes hash is : " + discontinuedGenes.size());
			logger.info("size of the updatedGenes hash is : " + updatedGenes.size());

		}		
		finally {
			if( beanReader != null ) {
				beanReader.close();
			}
		}		

		GeneHistoryDTO ghDTO = new GeneHistoryDTO();
		ghDTO.setDiscontinuedGenes(discontinuedGenes);
		ghDTO.setUpdatedGenes(updatedGenes);

		return ghDTO;

	}




	private void doStuf(BufferedReader br) throws IOException{
		String nameMapping;

		ICsvBeanReader beanReader = null;
		try {
			LsTransaction lsTransaction = new LsTransaction();
			lsTransaction.setComments("saving updated entrez gene id info");
			lsTransaction.setRecordedDate(new Date());
			lsTransaction.persist();
			beanReader = new CsvBeanReader(br, CsvPreference.TAB_PREFERENCE);
			String[] headerText = beanReader.getHeader(true);


			//set up gene type and kinds (check if already exists)

			String thingTypeAndKind = "gene_entrez gene";
			String labelTypeAndKind = "name_gene code name";
			List<LabelSequence> labelSeqs = LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(thingTypeAndKind, labelTypeAndKind).getResultList();
			if (labelSeqs.size() == 0){
				LabelSequence newLabelSeq = new LabelSequence();
				newLabelSeq.setDigits(6);
				newLabelSeq.setLabelPrefix("GENE");
				newLabelSeq.setLabelSeparator("-");
				newLabelSeq.setLatestNumber(1L);
				newLabelSeq.setLabelTypeAndKind(labelTypeAndKind);
				newLabelSeq.setThingTypeAndKind(thingTypeAndKind);
				newLabelSeq.persist();
			} 

			//			tax_id	GeneID	Discontinued_GeneID	Discontinued_Symbol	Discontinue_Date
			//			9606	-	4	A12M1	20050508
			//			9606	-	5	A12M2	20050510
			//			
			//		    private String discontinuedGeneId;
			//		    private String discontinuedSymbol;    
			//		    private String discontinuedDate;

			// map header text to standard bean names

			HashMap<String, String> geneBeanMap = new HashMap<String, String>();
			geneBeanMap.put("tax_id", "taxId");
			geneBeanMap.put("GeneID", "geneId");
			geneBeanMap.put("Discontinued_GeneID", "discontinuedGeneId");
			geneBeanMap.put("Discontinued_Symbol", "discontinuedSymbol");
			geneBeanMap.put("Discontinue_Date", "discontinuedDate");


			List<String> headerList = new ArrayList<String>();
			int position = 0;
			for (String head : headerText){
				logger.info("header column: " + position + "  " + head);
				if (geneBeanMap.get(head) != null){
					headerList.add(geneBeanMap.get(head));   				
				}
				position++;
			}


			logger.info("size of header list  " + headerList.size());
			String[] header = new String[headerList.size()];
			headerList.toArray(header);

			for (String head : header){
				logger.warn("header column array : " + position + "  " + head);
				position++;
			}

			final CellProcessor[] processors = getDiscontinuedGenesProcessors();

			EntrezDiscontinuedGeneDTO geneDTO;

			String geneTypeString = "gene";
			String geneKindString = "entrez gene";
			ThingType geneType = ThingType.getOrCreate(geneTypeString);
			ThingKind.getOrCreate(geneType, geneKindString);

			//			LabelType labelType = LabelType.findLabelTypesByTypeNameEquals("name").getSingleResult();
			//			LabelKind.getOrCreate(labelType, "gene code name");
			//			LabelKind.getOrCreate(labelType, "Entrez Gene ID");
			//			LabelKind.getOrCreate(labelType, "gene symbol");
			//			
			//			StateType stateType = StateType.getOrCreate("metadata");
			//			StateKind.getOrCreate(stateType, "gene metadata");
			//
			//			ValueType valueType = ValueType.getOrCreate("stringValue");
			//			ValueKind.getOrCreate(valueType, "discontinued gene id");
			//			ValueKind.getOrCreate(valueType, "discontinued gene symbol");
			//			ValueKind.getOrCreate(valueType, "discontinued date");

			//		    private String discontinuedGeneId;
			//		    private String discontinuedSymbol;    
			//		    private String discontinuedDate;

			//			ValueType valueTypeDate = ValueType.getOrCreate("dateValue");
			//			ValueKind.getOrCreate(valueTypeDate, "modification date");

			// List of tax ids that we will update info
			Set<String> taxIds = new HashSet<String>();
			taxIds.add("63221");
			taxIds.add("7227");
			taxIds.add("39442");
			taxIds.add("10090");
			taxIds.add("10091");
			taxIds.add("10092");
			taxIds.add("9606");
			taxIds.add("10116");
			taxIds.add("741158");

			int i = 0;
			int batchSize = 25;
			while( (geneDTO = beanReader.read(EntrezDiscontinuedGeneDTO.class, header, processors)) != null ) {
				System.out.println(String.format("lineNo=%s, rowNo=%s, bulkData=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), geneDTO));
				logger.info("current gene: " + geneDTO.getGeneId());
				if (taxIds.contains(geneDTO.getTaxId())){
					logger.info("gene taxId to process: " + geneDTO.getTaxId());

					// does this gene exist?
					if (geneDTO.getGeneId().equalsIgnoreCase("-")){
						logger.info("this gene does not exit");
					}

					List<LsThing> genes = LsThing.findLsThingByLabelText(geneTypeString, geneKindString, "name", "Entrez Gene ID", geneDTO.getDiscontinuedGeneId()).getResultList();

					List<LsThing> otherGenes = LsThing.findLsThingByLabelText(geneTypeString, geneKindString, "name", "Entrez Gene ID", geneDTO.getGeneId()).getResultList();

					logger.info("number of genes: " + genes.size());
					logger.info("number of otherGenes: " + otherGenes.size());

					for (LsThing foundGene : otherGenes){
						logger.info(foundGene.getCodeName());
						logger.info(foundGene.toPrettyJson());

					}




				} else {
					logger.debug("Skipping this taxId: " + geneDTO.getTaxId());
				}


				Long numberOfLabels = 1L;
				List<AutoLabelDTO> thingCodes = autoLabelService.getAutoLabels(thingTypeAndKind, labelTypeAndKind, numberOfLabels);			


				LsThing geneThing = new LsThing();
				geneThing.setCodeName(thingCodes.get(0).getAutoLabel());
				geneThing.setLsType(geneTypeString);
				geneThing.setLsKind(geneKindString);
				geneThing.setLsTransaction(lsTransaction.getId());
				geneThing.setRecordedBy("acas admin");
				//				geneThing.persist();

				String entrezSeparator = "\\|";
				saveGeneLabel(lsTransaction, geneThing, "name", "Entrez Gene ID", true, geneDTO.getGeneId(), entrezSeparator);
				//				saveGeneLabel(lsTransaction, geneThing, "name", "gene symbol", false, geneDTO.getSymbol(), entrezSeparator);
				//				saveGeneLabel(lsTransaction, geneThing, "name", "gene synonym", false, geneDTO.getSynonyms(), entrezSeparator);
				//				saveGeneLabel(lsTransaction, geneThing, "name", "authoritative gene name", false, geneDTO.getFullNameFromAuthority(), entrezSeparator);
				//				saveGeneLabel(lsTransaction, geneThing, "name", "authoritative gene symbol", false, geneDTO.getSymbolFromAuthority(), entrezSeparator);
				//				saveGeneLabel(lsTransaction, geneThing, "name", "other designations", false, geneDTO.getOtherDesignations(), entrezSeparator);

				LsThingState geneState = new LsThingState();
				geneState.setLsThing(geneThing);
				geneState.setLsType("metadata");
				geneState.setLsKind("gene metadata");
				geneState.setLsTransaction(lsTransaction.getId());
				geneState.setRecordedBy("acas admin");
				//				geneState.persist();

				saveGeneDescriptor(lsTransaction, geneState, "stringValue", "tax id", geneDTO.getTaxId(), entrezSeparator);
				//				saveGeneDescriptor(lsTransaction, geneState, "stringValue", "locus tag", geneDTO.getLocusTag(), entrezSeparator);
				//				saveGeneDescriptor(lsTransaction, geneState, "stringValue", "dbXrefs", geneDTO.getDbXrefs(), entrezSeparator);
				//				saveGeneDescriptor(lsTransaction, geneState, "stringValue", "chromosome", geneDTO.getChromosome(), entrezSeparator);
				//				saveGeneDescriptor(lsTransaction, geneState, "stringValue", "map location", geneDTO.getMapLocation(), entrezSeparator);
				//				saveGeneDescriptor(lsTransaction, geneState, "stringValue", "description", geneDTO.getDescription(), entrezSeparator);
				//				saveGeneDescriptor(lsTransaction, geneState, "stringValue", "type of gene", geneDTO.getTypeOfGene(), entrezSeparator);
				//				saveGeneDescriptor(lsTransaction, geneState, "stringValue", "nomenclature status", geneDTO.getNomenclatureStatus(), entrezSeparator);
				//				saveGeneDescriptor(lsTransaction, geneState, "dateValue", "modification date", geneDTO.getModificationDate());

				if ( i % batchSize == 0 ) { // same as the JDBC batch size
					//					geneThing.flush();
					//					geneThing.clear();
				}
				i++;
			}
		}
		finally {
			if( beanReader != null ) {
				beanReader.close();
			}
		}



		//beanReader.read(asd, nameMapping);

		//		String inputLine = null;
		//		while( (inputLine = br.readLine()) != null) {
		//			//break delimiter separated line using fieldDelimiter
		//			String[] tempString = inputLine.split(fieldDelimiter);
		//		}




		//	    Source Barcode,Source Well,Destination Barcode,Destination Plate Size,
		//	    Destination Well,Amount Transferred,Amount Units,Final Concentration,
		//	    Concentration Units,Final Volume,Final Volume Units,Liquid Type,Date Time,
		//	    Protocol,Is New Plate,Possible Transfer Error

		//		logger.info("Registering samples");
		//	ICsvMapReader inputMapFile = new CsvMapReader(new StringReader(csvFile), CsvPreference.EXCEL_PREFERENCE);
		//		Map<String, String> dataMapIn = new HashMap<String, String>();
		//		StringWriter outFile = new StringWriter();
		//ICsvMapWriter writer = new CsvMapWriter(outFile, CsvPreference.EXCEL_PREFERENCE);
		//		try {
		//			//			String[] inputCSVheader = inputMapFile.getHeader(true);
		//			//			writer.writeHeader(inputCSVheader);
		//		} catch (Exception e){
		//			e.printStackTrace();
		//		} 
		//
		//		logger.info(outFile.toString());
	}

	private void saveGeneDescriptor(LsTransaction lsTransaction, LsThingState geneState, String lsType, String lsKind, Date dateValue) {
		LsThingValue lsValue = new LsThingValue();
		lsValue.setLsState(geneState);
		lsValue.setLsType(lsType);
		lsValue.setLsKind(lsKind);
		lsValue.setDateValue(dateValue);
		lsValue.setLsTransaction(lsTransaction.getId());
		lsValue.setRecordedBy("acas admin");
		lsValue.persist();
		logger.info("saving gene descriptor date: " + dateValue);

	}

	private void saveGeneDescriptor(LsTransaction lsTransaction, LsThingState geneState, String lsType, String lsKind, String dataValues, String entrezSeparator) {
		String[] valueList = dataValues.split(entrezSeparator);
		for (String value : valueList){
			LsThingValue lsValue = new LsThingValue();
			lsValue.setLsState(geneState);
			lsValue.setLsType(lsType);
			lsValue.setLsKind(lsKind);
			lsValue.setStringValue(value);
			lsValue.setLsTransaction(lsTransaction.getId());
			lsValue.setRecordedBy("acas admin");
			lsValue.persist();
			logger.info("saving gene descriptor: " + value);
		}			
	}

	private void saveGeneLabel(LsTransaction lsTransaction, LsThing geneThing, String labelType, String labelKind, boolean preferred, String labels, String entrezSeparator) {
		String[] labelList = labels.split(entrezSeparator);
		for (String label : labelList){
			LsThingLabel lsLabel = new LsThingLabel();
			lsLabel.setLsThing(geneThing);
			lsLabel.setLsType(labelType);
			lsLabel.setLsKind(labelKind);
			lsLabel.setPreferred(preferred);
			lsLabel.setLabelText(label);
			lsLabel.setLsTransaction(lsTransaction.getId());
			lsLabel.setRecordedBy("acas admin");
			lsLabel.persist();
			logger.info("saving gene label: " + label);
		}		
	}


	private static CellProcessor[] getDiscontinuedGenesProcessors() {

		//    	"dd/MM/yyyy" (parses a date formatted as "25/12/2011")
		//    	"dd-MMM-yy" (parses a date formatted as "25-Dec-11")
		//    	"yyyy.MM.dd.HH.mm.ss" (parses a date formatted as "2011.12.25.08.36.33"
		//    	"E, dd MMM yyyy HH:mm:ss Z" (parses a date formatted as "Tue, 25 Dec 2011 08:36:33 -0500")

		//		tax_id	GeneID	Discontinued_GeneID	Discontinued_Symbol	Discontinue_Date
		//		9606	-	4	A12M1	20050508
		//		9606	-	5	A12M2	20050510
		//		9606	-	6	A12M3	20050510

		final CellProcessor[] processors = new CellProcessor[] { 
				new Optional(),  // 
				new Optional(),  // 
				new Optional(),  // 
				new Optional(),  // 
				new Optional(new ParseDate("yyyyMMdd")), // dateTime
		};

		return processors;
	}


}
