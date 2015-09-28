package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

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

@Service
public class GeneThingServiceImpl implements GeneThingService {

	public static final Logger logger = LoggerFactory.getLogger(GeneThingServiceImpl.class);

	@Autowired
	public AutoLabelService autoLabelService;

	
	@Override
	public void RegisterDiscontinuedGenesFromCSV(String inputFile) throws IOException{
		
	}

	@Override
	public void RegisterGeneThingsFromCSV(String inputFile) throws IOException{

		logger.debug("read tab delimited file");
		BufferedReader br = new BufferedReader(new FileReader(inputFile));

		ICsvBeanReader beanReader = null;
		try {
			LsTransaction lsTransaction = new LsTransaction();
			lsTransaction.setComments("saving entrez gene id info");
			lsTransaction.setRecordedDate(new Date());
			lsTransaction.persist();
			beanReader = new CsvBeanReader(br, CsvPreference.TAB_PREFERENCE);
			String[] headerText = beanReader.getHeader(true);

			//set up gene type and kinds (check if already exists)
			setGeneDefaults();


			String geneTypeString = "gene";
			String geneKindString = "entrez gene";
			String thingTypeAndKind = "gene_entrez gene";
			String labelTypeAndKind = "name_gene code name";


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
				logger.debug("header column: " + position + "  " + head);
				if (geneBeanMap.get(head) != null){
					headerList.add(geneBeanMap.get(head));   				
				}
				position++;
			}


			logger.debug("size of header list  " + headerList.size());
			String[] header = new String[headerList.size()];
			headerList.toArray(header);

			for (String head : header){
				logger.warn("header column array : " + position + "  " + head);
				position++;
			}


			final CellProcessor[] processors = getProcessors();

			EntrezDbGeneDTO geneDTO;

			int numberOfLines = SimpleUtil.countLines(inputFile);
			Long numberOfLabels = new Long(numberOfLines-1);
			
			List<AutoLabelDTO> thingCodes = autoLabelService.getAutoLabels(thingTypeAndKind, labelTypeAndKind, numberOfLabels);
			
			int i = 0;
			int batchSize = 25;
			LsThing geneThing = null;
			while( (geneDTO = beanReader.read(EntrezDbGeneDTO.class, header, processors)) != null ) {
				System.out.println(String.format("lineNo=%s, rowNo=%s, bulkData=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), geneDTO));
				logger.debug("current gene: " + geneDTO.getGeneId());

				// check if gene currently exists
				Long geneIdCount = LsThingLabel.countOfLsThingByName(geneTypeString, geneKindString, "name", "Entrez Gene ID", geneDTO.getGeneId());

				if (geneIdCount == 0L){

					geneThing = new LsThing();
					geneThing.setCodeName(thingCodes.get(i).getAutoLabel());
					geneThing.setLsType(geneTypeString);
					geneThing.setLsKind(geneKindString);
					geneThing.setLsTransaction(lsTransaction.getId());
					geneThing.setRecordedBy("acas admin");
					geneThing.persist();

					String entrezSeparator = "\\|";
					saveGeneLabel(lsTransaction, geneThing, "name", "Entrez Gene ID", true, geneDTO.getGeneId(), entrezSeparator);
					saveGeneLabel(lsTransaction, geneThing, "name", "gene symbol", false, geneDTO.getSymbol(), entrezSeparator);
					saveGeneLabel(lsTransaction, geneThing, "name", "gene synonym", false, geneDTO.getSynonyms(), entrezSeparator);
					saveGeneLabel(lsTransaction, geneThing, "name", "authoritative gene name", false, geneDTO.getFullNameFromAuthority(), entrezSeparator);
					saveGeneLabel(lsTransaction, geneThing, "name", "authoritative gene symbol", false, geneDTO.getSymbolFromAuthority(), entrezSeparator);
					saveGeneLabel(lsTransaction, geneThing, "name", "other designations", false, geneDTO.getOtherDesignations(), entrezSeparator);

					LsThingState geneState = new LsThingState();
					geneState.setLsThing(geneThing);
					geneState.setLsType("metadata");
					geneState.setLsKind("gene metadata");
					geneState.setLsTransaction(lsTransaction.getId());
					geneState.setRecordedBy("acas admin");
					geneState.persist();

					saveGeneDescriptor(lsTransaction, geneState, "stringValue", "tax id", geneDTO.getTaxId(), entrezSeparator);
					saveGeneDescriptor(lsTransaction, geneState, "stringValue", "locus tag", geneDTO.getLocusTag(), entrezSeparator);
					saveGeneDescriptor(lsTransaction, geneState, "stringValue", "dbXrefs", geneDTO.getDbXrefs(), entrezSeparator);
					saveGeneDescriptor(lsTransaction, geneState, "stringValue", "chromosome", geneDTO.getChromosome(), entrezSeparator);
					saveGeneDescriptor(lsTransaction, geneState, "stringValue", "map location", geneDTO.getMapLocation(), entrezSeparator);
					saveGeneDescriptor(lsTransaction, geneState, "stringValue", "description", geneDTO.getDescription(), entrezSeparator);
					saveGeneDescriptor(lsTransaction, geneState, "stringValue", "type of gene", geneDTO.getTypeOfGene(), entrezSeparator);
					saveGeneDescriptor(lsTransaction, geneState, "stringValue", "nomenclature status", geneDTO.getNomenclatureStatus(), entrezSeparator);
					saveGeneDescriptor(lsTransaction, geneState, "dateValue", "modification date", geneDTO.getModificationDate());

					if ( i % batchSize == 0 ) { // same as the JDBC batch size
						geneThing.flush();
						geneThing.clear();
					}
					i++;

				} else {
					logger.warn("current gene id exists in the database: " + geneDTO.getGeneId());
				}



			}
		}
		finally {
			if( beanReader != null ) {
				beanReader.close();
			}
			if (br != null)br.close();
		}

	}

	public LsThingValue saveGeneDescriptor(LsTransaction lsTransaction, LsThingState geneState, String lsType, String lsKind, Date dateValue) {
		LsThingValue lsValue = new LsThingValue();
		lsValue.setLsState(geneState);
		lsValue.setLsType(lsType);
		lsValue.setLsKind(lsKind);
		lsValue.setDateValue(dateValue);
		lsValue.setLsTransaction(lsTransaction.getId());
		lsValue.setRecordedBy("acas admin");
		lsValue.persist();
		logger.debug("saving gene descriptor date: " + dateValue);
		return lsValue;

	}
	
	public List<LsThingValue> saveGeneDescriptor(LsTransaction lsTransaction, LsThingState geneState, String lsType, String lsKind, String dataValues, String entrezSeparator) {
		List<LsThingValue> lsValues = new ArrayList<LsThingValue>();
		String[] valueList = dataValues.split(entrezSeparator);
		for (String value : valueList){
			if (value.equalsIgnoreCase("-")){
				logger.debug("skipping gene descriptor: " + value);
			} else {
			LsThingValue lsValue = new LsThingValue();
			lsValue.setLsState(geneState);
			lsValue.setLsType(lsType);
			lsValue.setLsKind(lsKind);
			lsValue.setStringValue(value);
			lsValue.setLsTransaction(lsTransaction.getId());
			lsValue.setRecordedBy("acas admin");
			lsValue.persist();
			lsValues.add(lsValue);
			logger.debug("saving gene descriptor: " + value);
			}
		}		
		return lsValues;
	}

	public List<LsThingLabel> saveGeneLabel(LsTransaction lsTransaction, LsThing geneThing, String labelType, String labelKind, boolean preferred, String labels, String entrezSeparator) {
		List<LsThingLabel> newLabels = new ArrayList<LsThingLabel>();
		String[] labelList = labels.split(entrezSeparator);
		for (String label : labelList){
			if (label.equalsIgnoreCase("-")){
				logger.debug("skipping gene label: " + label);
			} else {
				
//				existingLabels = LsThingLabel.findLsThingName("gene", "entrez gene", labelType, labelKind, label).getResultList();
				if (LsThingLabel.countOfLsThingLabelsByLabel(geneThing, labelType, labelKind, label) == 0) {
					LsThingLabel lsLabel = new LsThingLabel();
					lsLabel.setLsThing(geneThing);
					lsLabel.setLsType(labelType);
					lsLabel.setLsKind(labelKind);
					lsLabel.setPreferred(preferred);
					lsLabel.setLabelText(label);
					lsLabel.setLsTransaction(lsTransaction.getId());
					lsLabel.setRecordedBy("acas admin");
					lsLabel.persist();
					newLabels.add(lsLabel);
					logger.debug("saving gene label: " + label);
				} else {
					logger.debug("-------------- the label already exists -- not creating new entry" + label);
				}
				
			}
		}		
		return newLabels;
	}

	public CellProcessor[] getProcessors() {

		//    	"dd/MM/yyyy" (parses a date formatted as "25/12/2011")
		//    	"dd-MMM-yy" (parses a date formatted as "25-Dec-11")
		//    	"yyyy.MM.dd.HH.mm.ss" (parses a date formatted as "2011.12.25.08.36.33"
		//    	"E, dd MMM yyyy HH:mm:ss Z" (parses a date formatted as "Tue, 25 Dec 2011 08:36:33 -0500")

		final CellProcessor[] processors = new CellProcessor[] { 
				new Optional(),  // 
				new Optional(),  // 
				new Optional(),  // 
				new Optional(),  // 
				new Optional(),  // 
				new Optional(),  // 
				new Optional(),  // 
				new Optional(),  // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), //
				new Optional(), //
				new Optional(), //
				new Optional(new ParseDate("yyyyMMdd")), // dateTime
		};

		return processors;
	}

	@Override
	public void setGeneDefaults() {
		
		//set up gene type and kinds (check if already exists)
		String geneTypeString = "gene";
		String geneKindString = "entrez gene";
		ThingType geneType = ThingType.getOrCreate(geneTypeString);
		ThingKind.getOrCreate(geneType, geneKindString);

		LabelType labelType = LabelType.getOrCreate("name");
		LabelKind.getOrCreate(labelType, "gene code name");
		LabelKind.getOrCreate(labelType, "Entrez Gene ID");
		LabelKind.getOrCreate(labelType, "gene symbol");
		LabelKind.getOrCreate(labelType, "authoritative gene symbol");
		LabelKind.getOrCreate(labelType, "gene synonym");
		LabelKind.getOrCreate(labelType, "authoritative gene name");
		LabelKind.getOrCreate(labelType, "other designations");

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

		StateType stateType = StateType.getOrCreate("metadata");
		StateKind.getOrCreate(stateType, "gene metadata");
		StateKind.getOrCreate(stateType, "gene discontinued metadata");

		ValueType valueType = ValueType.getOrCreate("stringValue");
		ValueKind.getOrCreate(valueType, "tax id");
		ValueKind.getOrCreate(valueType, "locus tag");
		ValueKind.getOrCreate(valueType, "dbXrefs");
		ValueKind.getOrCreate(valueType, "chromosome");
		ValueKind.getOrCreate(valueType, "map location");
		ValueKind.getOrCreate(valueType, "description");
		ValueKind.getOrCreate(valueType, "type of gene");
		ValueKind.getOrCreate(valueType, "nomenclature status");
		ValueKind.getOrCreate(valueType, "discontinued gene id");
		ValueKind.getOrCreate(valueType, "discontinued gene symbol");

		ValueType valueTypeDate = ValueType.getOrCreate("dateValue");
		ValueKind.getOrCreate(valueTypeDate, "modification date");
		ValueKind.getOrCreate(valueTypeDate, "discontinued date");
		
	}

	@Override
	public LsThing registerNewGene(EntrezDbGeneDTO geneDTO, EntrezDiscontinuedGeneDTO entrezDiscontinuedGeneDTO, LsTransaction lsTransaction) {
		
		// check if gene currently exists
		String geneTypeString = "gene";
		String geneKindString = "entrez gene";
		String thingTypeAndKind = "gene_entrez gene";
		String labelTypeAndKind = "name_gene code name";
		
		Long geneIdCount = LsThingLabel.countOfLsThingByName(geneTypeString, geneKindString, "name", "Entrez Gene ID", geneDTO.getGeneId());
		LsThing geneThing = null;

		if (geneIdCount == 0L){
			geneThing = new LsThing();
			geneThing.setCodeName(autoLabelService.getAutoLabels(thingTypeAndKind, labelTypeAndKind, 1L).get(0).getAutoLabel());
			geneThing.setLsType(geneTypeString);
			geneThing.setLsKind(geneKindString);
			geneThing.setLsTransaction(lsTransaction.getId());
			geneThing.setRecordedBy("acas admin");
			geneThing.persist();

			String entrezSeparator = "\\|";
			saveGeneLabel(lsTransaction, geneThing, "name", "Entrez Gene ID", true, geneDTO.getGeneId(), entrezSeparator);
			saveGeneLabel(lsTransaction, geneThing, "name", "gene symbol", false, geneDTO.getSymbol(), entrezSeparator);
			saveGeneLabel(lsTransaction, geneThing, "name", "gene synonym", false, geneDTO.getSynonyms(), entrezSeparator);
			saveGeneLabel(lsTransaction, geneThing, "name", "authoritative gene name", false, geneDTO.getFullNameFromAuthority(), entrezSeparator);
			saveGeneLabel(lsTransaction, geneThing, "name", "authoritative gene symbol", false, geneDTO.getSymbolFromAuthority(), entrezSeparator);
			saveGeneLabel(lsTransaction, geneThing, "name", "other designations", false, geneDTO.getOtherDesignations(), entrezSeparator);

			LsThingState geneState = new LsThingState();
			geneState.setLsThing(geneThing);
			geneState.setLsType("metadata");
			geneState.setLsKind("gene metadata");
			geneState.setLsTransaction(lsTransaction.getId());
			geneState.setRecordedBy("acas admin");
			geneState.persist();

			saveGeneDescriptor(lsTransaction, geneState, "stringValue", "tax id", geneDTO.getTaxId(), entrezSeparator);
			saveGeneDescriptor(lsTransaction, geneState, "stringValue", "locus tag", geneDTO.getLocusTag(), entrezSeparator);
			saveGeneDescriptor(lsTransaction, geneState, "stringValue", "dbXrefs", geneDTO.getDbXrefs(), entrezSeparator);
			saveGeneDescriptor(lsTransaction, geneState, "stringValue", "chromosome", geneDTO.getChromosome(), entrezSeparator);
			saveGeneDescriptor(lsTransaction, geneState, "stringValue", "map location", geneDTO.getMapLocation(), entrezSeparator);
			saveGeneDescriptor(lsTransaction, geneState, "stringValue", "description", geneDTO.getDescription(), entrezSeparator);
			saveGeneDescriptor(lsTransaction, geneState, "stringValue", "type of gene", geneDTO.getTypeOfGene(), entrezSeparator);
			saveGeneDescriptor(lsTransaction, geneState, "stringValue", "nomenclature status", geneDTO.getNomenclatureStatus(), entrezSeparator);
			saveGeneDescriptor(lsTransaction, geneState, "dateValue", "modification date", geneDTO.getModificationDate());
				
		} else {
			logger.debug("gene already exists: " + geneDTO.toJson());
			//add old gene ID as another label
			//saveGeneLabel(lsTransaction, geneThing, "name", "Entrez Gene ID", true, geneDTO.getGeneId());

			
		}
		
		
		return geneThing;
		
	}
	
	public LsThingLabel saveGeneLabel(LsTransaction lsTransaction, LsThing geneThing,
			String labelType, String labelKind, boolean preferred, String label) {
		LsThingLabel lsLabel = new LsThingLabel();
		lsLabel.setLsThing(geneThing);
		lsLabel.setLsType(labelType);
		lsLabel.setLsKind(labelKind);
		lsLabel.setPreferred(preferred);
		lsLabel.setLabelText(label);
		lsLabel.setLsTransaction(lsTransaction.getId());
		lsLabel.setRecordedBy("acas admin");
		lsLabel.setRecordedDate(new Date());
		lsLabel.persist();
		logger.debug("saving gene label: " + label);
		return lsLabel;
		
	}

	@Override
	public String getGeneCodeName() {

		String thingTypeAndKind = "gene_entrez gene";
		String labelTypeAndKind = "name_gene code name";
		Long numberOfLabels = 1L;
		List<AutoLabelDTO> thingCodes = autoLabelService.getAutoLabels(thingTypeAndKind, labelTypeAndKind, numberOfLabels);		

		return thingCodes.get(0).getAutoLabel();

	}
	
	public LsTransaction createLsTransaction(String txComment) {
		LsTransaction lsTransaction = new LsTransaction();
		lsTransaction.setComments(txComment);
		lsTransaction.setRecordedDate(new Date());
		lsTransaction.persist();
		return lsTransaction;
	}
	
	public void updateEntrezGenes(String entrezGenesFile, String geneHistoryFile, String taxonomyId) throws IOException{

		String txComment = "update entrez genes";
		LsTransaction lsTransaction = createLsTransaction(txComment);
		updateEntrezGenes(entrezGenesFile, geneHistoryFile, taxonomyId, lsTransaction);

	}
	
	@Override
	@Transactional
	public void updateEntrezGenes(String entrezGenesFile,
			String geneHistoryFile, String taxonomyId,
			LsTransaction lsTransaction) throws IOException {
		
		GeneHistoryDTO historyResults = processGeneHistory(geneHistoryFile, taxonomyId);
		processEntrezGenes(entrezGenesFile, historyResults, taxonomyId, lsTransaction );
		processDiscontinuedGenes(historyResults.getDiscontinuedGenes(), taxonomyId, lsTransaction );		
	}


	public void processEntrezGenes(String entrezGenes, GeneHistoryDTO historyResults, 
			String taxonomyId, LsTransaction lsTransaction) throws IOException {

		setGeneDefaults();

		logger.debug("read tab delimited file");
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
				logger.debug("header column: " + position + "  " + head);
				if (geneBeanMap.get(head) != null){
					headerList.add(geneBeanMap.get(head));   				
				}
				position++;
			}


			logger.debug("size of header list  " + headerList.size());
			String[] header = new String[headerList.size()];
			headerList.toArray(header);

			for (String head : header){
				logger.warn("header column array : " + position + "  " + head);
				position++;
			}


			final CellProcessor[] processors = getProcessors();

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
				logger.debug("current gene: " + geneDTO.getGeneId());

				// check if gene currently exists
				Long geneIdCount = LsThingLabel.countOfLsThingByName(geneTypeString, geneKindString, "name", "Entrez Gene ID", geneDTO.getGeneId());
//				Long oldGeneIdCount = LsThingLabel.countOfLsThingByName(geneTypeString, geneKindString, "name", "Entrez Gene ID", historyResults.getUpdatedGenes().get(geneDTO.getGeneId()).getDiscontinuedGeneId());

				logger.debug("Process Entrez Genes ------------------- geneId count"  + geneIdCount);

				
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
					geneThing = registerDiscontinuedGene(historyResults.getDiscontinuedGenes().get(geneDTO.getGeneId()), lsTransaction);

				} else if (updatedGene ){
					// updating existing gene info with new gene label and meta (merging genes if new gene does not exist)
					// else keep 2 gene entities
					geneThing = registerUpdatedGene(geneDTO, historyResults.getUpdatedGenes().get(geneDTO.getGeneId()), lsTransaction);					
					//if (logger.isDebugEnabled()) logger.debug("updated the gene entry: " + LsThing.findLsThing(geneThing.getId()).toPrettyJson());

				} else if (geneIdCount == 0L ) {
					// brand new gene to register
					if (logger.isDebugEnabled()) logger.debug("registering new gene: " + geneDTO.getGeneId());
					geneThing = registerNewGene(geneDTO, null, lsTransaction);

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
				logger.debug("-----------------------------------------------current entrez gene update count: " + i);

			}
		}
		finally {
			if( beanReader != null ) {
				beanReader.close();
			}
			if (br != null)br.close();
		}

	}

	
	@Override
	public LsThing registerUpdatedGene(EntrezDbGeneDTO geneDTO, EntrezDiscontinuedGeneDTO entrezDiscontinuedGeneDTO,  LsTransaction lsTransaction) {
		// geneID not registered but the gene is on the update list
		// check if registered under different geneID
		// if so -- then update
		// else  -- register new
		
		// two copies of the gene exist
		// one copy is discontinued
		// the other copy is now the preferred gene
		// mark the old one as discontinued (it will keep the old gene id)
		// the other gene will keep the new gene id and just update the gene metadata
		
		String geneTypeString = "gene";
		String geneKindString = "entrez gene";
//		Long geneIdCount = LsThingLabel.countOfLsThingByName(geneTypeString, geneKindString, "name", "Entrez Gene ID", entrezDiscontinuedGeneDTO.getDiscontinuedGeneId());
//		Long newGeneIdCount = LsThingLabel.countOfLsThingByName(geneTypeString, geneKindString, "name", "Entrez Gene ID", geneDTO.getGeneId());
		
//		things = LsThing.findLsThingByLabelKindAndLabelText(geneTypeString, geneKindString, labelKind, labelText);
		List<LsThing> discontinuedGenes = LsThing.findLsThingByLabelText(geneTypeString, geneKindString, "name", "Entrez Gene ID", entrezDiscontinuedGeneDTO.getDiscontinuedGeneId()).getResultList();
		List<LsThing> newGenes = LsThing.findLsThingByLabelText(geneTypeString, geneKindString, "name", "Entrez Gene ID",  geneDTO.getGeneId()).getResultList();

		int geneIdCount = discontinuedGenes.size();
		int newGeneIdCount = newGenes.size();
		
		logger.debug("------------ the gene ID count is: " + discontinuedGenes.size() + "  for discontinued gene " + entrezDiscontinuedGeneDTO.getDiscontinuedGeneId());
		logger.debug("------------ the gene ID count is: " + newGenes.size() + "  for new gene " + geneDTO.getGeneId());
		
		//are the genes the same?
		boolean sameGene = false;
		if (discontinuedGenes.size() == 1 && newGenes.size() == 1){
			if (discontinuedGenes.get(0).getId() == newGenes.get(0).getId()){
				logger.debug("@@@@@@  Mapping back to the same gene   @@@@@@");
				sameGene = true;
			}
		}
		
		LsThing geneThing = null ;
		if (geneIdCount == 0 && newGeneIdCount == 0){
			logger.debug("gene is not registered yet");
			geneThing = registerNewGene(geneDTO, entrezDiscontinuedGeneDTO, lsTransaction);
		} else if (geneIdCount == 1 && newGeneIdCount == 1 && sameGene){
			logger.debug("------ both gene IDs are currently registered with the same geneThing");
			// add discontinued meta to the old gene
			
			LsThing oldGene = discontinueGene(entrezDiscontinuedGeneDTO, lsTransaction);
			//if (logger.isDebugEnabled()) logger.debug("added discontinued gene meta to the old gene: " + oldGene.toPrettyJson());
						
			// update the gene with the new info
			List<LsThing> genes = LsThing.findLsThingByLabelText(geneTypeString, geneKindString, "name", "Entrez Gene ID", geneDTO.getGeneId()).getResultList();
			if (genes.size() == 0){
				logger.error("Gene not found");
			} else if (genes.size() > 1){
				logger.error("multiple genes found with the same gene ID");
				
			} else {
				geneThing = genes.get(0);
//				geneThing = updateGeneID(geneThing, geneDTO, entrezDiscontinuedGeneDTO, lsTransaction);
				
				//check the modification date
				boolean isNewDate = false;
				List<LsThingValue> dateValues = LsThingValue.findLsThingValuesByLsThingIDAndStateTypeKindAndValueTypeKind(geneThing.getId(), "metadata", "gene metadata", "dateValue", "modification date").getResultList();
				logger.debug("number of mod dates found: " + dateValues.size());
				if (dateValues.size() > 1) logger.error("Found more than 1 mod date: " + dateValues.size());
				for (LsThingValue dateValue : dateValues){
					if (geneDTO.getModificationDate().after(dateValue.getDateValue())){
						isNewDate = true;
						logger.debug("It is a recent update");
					} else {
						logger.debug("It is a not a recent update");
					}
				}

				
				if (isNewDate){
					logger.debug("Updating the gene metadata");
					geneThing = updateGeneMetaData(geneThing, geneDTO, lsTransaction);
				}
				
				
				if (LsThingLabel.countOfLsThingByName(geneTypeString, geneKindString, "name", "Entrez Gene ID", entrezDiscontinuedGeneDTO.getDiscontinuedGeneId()) == 0){
					logger.debug("adding old gene ID");
					geneThing = addOldGeneID(geneThing, geneDTO, entrezDiscontinuedGeneDTO, lsTransaction);
				}
				
			}
			
		} else if (geneIdCount == 0 && newGeneIdCount == 1){
			logger.debug("------ just the new gene is currently registered");
			// add the old entrez gene id to the registered gene
			// add discontinued meta to the old gene
									
			// update the new gene with the new info
			List<LsThing> genes = LsThing.findLsThingByLabelText(geneTypeString, geneKindString, "name", "Entrez Gene ID", geneDTO.getGeneId()).getResultList();
			if (genes.size() == 0){
				logger.error("Gene not found");
			} else if (genes.size() > 1){
				logger.error("multiple genes found with the same gene ID");
				
			} else {
				geneThing = genes.get(0);
				geneThing = addOldGeneID(geneThing, geneDTO, entrezDiscontinuedGeneDTO, lsTransaction);
			}			
		} else {
			logger.debug("-------- gene is registered via old id   geneIdCount: " + geneIdCount + "  newGeneIdCount: " + newGeneIdCount);
			
//			if (logger.isDebugEnabled()) logger.debug("old gene: " + LsThing.findLsThingByLabelText(geneTypeString, geneKindString, "name", "Entrez Gene ID", entrezDiscontinuedGeneDTO.getDiscontinuedGeneId()).getResultList().get(0).toPrettyJson());
			
			
			List<LsThing> genes = LsThing.findLsThingByLabelText(geneTypeString, geneKindString, "name", "Entrez Gene ID", geneDTO.getGeneId()).getResultList();
			if (genes.size() == 0){
				logger.error("Gene not found");
			} else if (genes.size() > 1){
				logger.error("multiple genes found with the same gene ID");
				List<Long> thingIds = new ArrayList();
				for (LsThing lsThing : genes){
					thingIds.add(lsThing.getId());
					logger.error("Found this gene ID: " + lsThing.toJson());
					
				}
				Collections.sort(thingIds);
				boolean first = true;
				for (Long geneId : thingIds){
					if (first){
						first = false;
						geneThing = LsThing.findLsThing(geneId);
					} else {
						logger.debug("ignored this gene thing ID " + geneId);
						LsThing queryGene = LsThing.findLsThing(geneId);
						queryGene.setIgnored(true);
						queryGene.merge();
					}
				}
				
			} else {
				geneThing = genes.get(0);
			}	
			
			
			//check the modification date
			boolean isNewDate = false;
			List<LsThingValue> dateValues = LsThingValue.findLsThingValuesByLsThingIDAndStateTypeKindAndValueTypeKind(geneThing.getId(), "metadata", "gene metadata", "dateValue", "modification date").getResultList();
			logger.debug("number of mod dates found: " + dateValues.size());
			if (dateValues.size() > 1) logger.error("Found more than 1 mod date: " + dateValues.size());
			for (LsThingValue dateValue : dateValues){
				if (geneDTO.getModificationDate().after(dateValue.getDateValue())){
					isNewDate = true;
					logger.debug("It is a recent update");
				} else {
					logger.debug("It is a not a recent update");
				}
			}

			
			if (isNewDate){
				logger.debug("Updating the gene metadata");
				geneThing = updateGeneMetaData(geneThing, geneDTO, lsTransaction);
				geneThing = updateGene(geneDTO, entrezDiscontinuedGeneDTO, lsTransaction);
			}

		}

		
		
		return geneThing;
	}
	
	
	public LsThing addOldGeneID(LsThing geneThing, EntrezDbGeneDTO geneDTO, EntrezDiscontinuedGeneDTO entrezDiscontinuedGeneDTO, LsTransaction lsTransaction) {
		if (entrezDiscontinuedGeneDTO.getDiscontinuedGeneId() != null)  geneThing.getLsLabels().add(saveGeneLabel(lsTransaction, geneThing, "name", "Entrez Gene ID", false, entrezDiscontinuedGeneDTO.getDiscontinuedGeneId()));
		if (entrezDiscontinuedGeneDTO.getDiscontinuedSymbol() != null)  geneThing.getLsLabels().add(saveGeneLabel(lsTransaction, geneThing, "name", "gene symbol", false, entrezDiscontinuedGeneDTO.getDiscontinuedSymbol()));
		return geneThing;
	}

	public LsThing discontinueGene(EntrezDiscontinuedGeneDTO entrezDiscontinuedGeneDTO, LsTransaction lsTransaction) {
		String geneTypeString = "gene";
		String geneKindString = "entrez gene";
		List<LsThing> genes = LsThing.findLsThingByLabelText(geneTypeString, geneKindString, "name", "Entrez Gene ID", entrezDiscontinuedGeneDTO.getDiscontinuedGeneId()).getResultList();

		LsThing geneThing = null ;
		
		logger.debug("---------- add discontinued Gene meta data:  number of genes found: " + genes.size());
		
		if (genes.size() == 0){
			logger.error("Gene not found");
		} else if (genes.size() > 1){
			logger.error("multiple genes found with the same gene ID");
			
		} else {
			geneThing = genes.get(0);
			geneThing = addDiscontinueMetadata(geneThing, entrezDiscontinuedGeneDTO, lsTransaction);

		}

		return geneThing;
		
	}

	public LsThing addDiscontinueMetadata(LsThing geneThing, EntrezDiscontinuedGeneDTO entrezDiscontinuedGeneDTO, LsTransaction lsTransaction) {
		
		List<LsThingValue> thingValues = LsThingValue.findLsThingValuesByLsThingIDAndStateTypeKindAndValueTypeKind(geneThing.getId(), "metadata", "gene discontinued metadata", "stringValue", "discontinued gene").getResultList();
		
		boolean discontinued = false;
		for (LsThingValue thingValue : thingValues){
			if (!thingValue.isIgnored() && thingValue.getStringValue().equalsIgnoreCase("true")){
				discontinued = true;
			}
		}
		
		if (!discontinued){
			// save discontinued metadata
			LsThingState geneState = new LsThingState();
			geneState.setLsThing(geneThing);
			geneState.setLsType("metadata");
			geneState.setLsKind("gene discontinued metadata");
			geneState.setLsTransaction(lsTransaction.getId());
			geneState.setRecordedBy("acas admin");
			geneState.setRecordedDate(new Date());
			geneState.persist();

			String entrezSeparator = "\\|";
			geneState.getLsValues().addAll(saveGeneDescriptor(lsTransaction, geneState, "stringValue", "discontinued gene", "true", entrezSeparator));
			geneState.getLsValues().addAll(saveGeneDescriptor(lsTransaction, geneState, "stringValue", "discontinued gene id", entrezDiscontinuedGeneDTO.getDiscontinuedGeneId(), entrezSeparator));
			geneState.getLsValues().addAll(saveGeneDescriptor(lsTransaction, geneState, "stringValue", "discontinued gene symbol", entrezDiscontinuedGeneDTO.getDiscontinuedSymbol(), entrezSeparator));
			geneState.getLsValues().add(saveGeneDescriptor(lsTransaction, geneState, "dateValue", "discontinued date", entrezDiscontinuedGeneDTO.getDiscontinuedDate()));
			geneThing.getLsStates().add(geneState);
		}
		
		
		return geneThing;
	}

	public LsThing updateGene(EntrezDbGeneDTO geneDTO, EntrezDiscontinuedGeneDTO entrezDiscontinuedGeneDTO, LsTransaction lsTransaction) {
		String geneTypeString = "gene";
		String geneKindString = "entrez gene";
		List<LsThing> genes = LsThing.findLsThingByLabelText(geneTypeString, geneKindString, "name", "Entrez Gene ID", entrezDiscontinuedGeneDTO.getDiscontinuedGeneId()).getResultList();
		LsThing geneThing = null ;
		
		logger.debug("updateGene:  number of genes found: " + genes.size());
		
		if (genes.size() == 0){
			logger.error("Gene not found");
		} else if (genes.size() > 1){
			logger.error("multiple genes found with the same gene ID");
			
		} else {
			geneThing = genes.get(0);
			geneThing = updateGeneID(geneThing, geneDTO, entrezDiscontinuedGeneDTO, lsTransaction);
			geneThing = updateGeneMetaData(geneThing, geneDTO, lsTransaction);
		}
				
		return geneThing;
	}

	public LsThing updateGeneMetaData(LsThing geneThing, EntrezDbGeneDTO geneDTO, LsTransaction lsTransaction) {
		
// ignore old states
		//		List<LsThingState> oldStates = LsThingState.findLsThingStatesByLsThingIDAndStateTypeKind(geneThing.getId(), "metadata", "gene metadata").getResultList();
		for (LsThingState state : geneThing.getLsStates()){
			if (state.getLsType().equalsIgnoreCase("metadata") && state.getLsKind().equalsIgnoreCase("gene metadata")){
				state.setIgnored(true);
				state.merge();				
			}
		}

		// ignore all old labels
		for (LsThingLabel geneLabel : geneThing.getLsLabels()){
			    logger.debug("@@@@@@@@@@@@@@  IGNORING GENE LABELS @@@@@@@@@@@@@    GeneThing: " + geneThing.getCodeName() + "  " + geneLabel.getLabelText() );
				// ignore all of the existing labels
				geneLabel.setIgnored(true);
		}

		
		//geneThing = ignoreNonGeneIDLabels(lsTransaction, geneThing);
		
		String entrezSeparator = "\\|";
		geneThing.getLsLabels().addAll(saveGeneLabel(lsTransaction, geneThing, "name", "Entrez Gene ID", true, geneDTO.getGeneId(), entrezSeparator));
		geneThing.getLsLabels().addAll(saveGeneLabel(lsTransaction, geneThing, "name", "gene symbol", false, geneDTO.getSymbol(), entrezSeparator));
		geneThing.getLsLabels().addAll(saveGeneLabel(lsTransaction, geneThing, "name", "gene synonym", false, geneDTO.getSynonyms(), entrezSeparator));
		geneThing.getLsLabels().addAll(saveGeneLabel(lsTransaction, geneThing, "name", "authoritative gene name", false, geneDTO.getFullNameFromAuthority(), entrezSeparator));
		geneThing.getLsLabels().addAll(saveGeneLabel(lsTransaction, geneThing, "name", "authoritative gene symbol", false, geneDTO.getSymbolFromAuthority(), entrezSeparator));
		geneThing.getLsLabels().addAll(saveGeneLabel(lsTransaction, geneThing, "name", "other designations", false, geneDTO.getOtherDesignations(), entrezSeparator));

		LsThingState geneState = new LsThingState();
		geneState.setLsThing(geneThing);
		geneState.setLsType("metadata");
		geneState.setLsKind("gene metadata");
		geneState.setLsTransaction(lsTransaction.getId());
		geneState.setRecordedBy("acas admin");
		geneState.persist();

		geneState.getLsValues().addAll(saveGeneDescriptor(lsTransaction, geneState, "stringValue", "tax id", geneDTO.getTaxId(), entrezSeparator));
		geneState.getLsValues().addAll(saveGeneDescriptor(lsTransaction, geneState, "stringValue", "locus tag", geneDTO.getLocusTag(), entrezSeparator));
		geneState.getLsValues().addAll(saveGeneDescriptor(lsTransaction, geneState, "stringValue", "dbXrefs", geneDTO.getDbXrefs(), entrezSeparator));
		geneState.getLsValues().addAll(saveGeneDescriptor(lsTransaction, geneState, "stringValue", "chromosome", geneDTO.getChromosome(), entrezSeparator));
		geneState.getLsValues().addAll(saveGeneDescriptor(lsTransaction, geneState, "stringValue", "map location", geneDTO.getMapLocation(), entrezSeparator));
		geneState.getLsValues().addAll(saveGeneDescriptor(lsTransaction, geneState, "stringValue", "description", geneDTO.getDescription(), entrezSeparator));
		geneState.getLsValues().addAll(saveGeneDescriptor(lsTransaction, geneState, "stringValue", "type of gene", geneDTO.getTypeOfGene(), entrezSeparator));
		geneState.getLsValues().addAll(saveGeneDescriptor(lsTransaction, geneState, "stringValue", "nomenclature status", geneDTO.getNomenclatureStatus(), entrezSeparator));
		geneState.getLsValues().add(saveGeneDescriptor(lsTransaction, geneState, "dateValue", "modification date", geneDTO.getModificationDate()));
		geneThing.getLsStates().add(geneState);
		
		return geneThing;

	}

	


	public LsThing ignoreNonGeneIDLabels(LsTransaction lsTransaction, LsThing geneThing) {
		Set<LsThingLabel> lsLabels = new HashSet<LsThingLabel>();
		for (LsThingLabel geneLabel : geneThing.getLsLabels()){
			if (geneLabel.getLsType().equalsIgnoreCase("name") && geneLabel.getLsKind().equalsIgnoreCase("Entrez Gene ID")){
				// do nothing to the Entrez Gene IDs
			} else {
				// ignore all of the existing labels
				geneLabel.setIgnored(true);
				//lsLabels.add(geneLabel);
			}
		}
		
		//geneThing.setLsLabels(lsLabels);
		return geneThing;
	}

	public LsThing updateGeneID(LsThing geneThing, EntrezDbGeneDTO geneDTO, EntrezDiscontinuedGeneDTO entrezDiscontinuedGeneDTO, LsTransaction lsTransaction) {
		
		logger.debug("attempting to update the gene");
		logger.debug(geneThing.toJson());
		
		// mark old geneId as not preferred
		// create new label
		for (LsThingLabel geneLabel : geneThing.getLsLabels()){
			if (geneLabel.getLsType().equalsIgnoreCase("name") && geneLabel.getLsKind().equalsIgnoreCase("Entrez Gene ID") 
					&& geneLabel.getLabelText().equalsIgnoreCase(entrezDiscontinuedGeneDTO.getDiscontinuedGeneId()) && geneLabel.isPreferred()){
				geneLabel.setPreferred(false);
			}
		}
		
		LsThingLabel newGeneLabel = saveGeneLabel(lsTransaction, geneThing, "name", "Entrez Gene ID", true, entrezDiscontinuedGeneDTO.getGeneId());
		geneThing.getLsLabels().add(newGeneLabel);
		
		// save discontinued metadata
		LsThingState geneState = new LsThingState();
		geneState.setLsThing(geneThing);
		geneState.setLsType("metadata");
		geneState.setLsKind("gene discontinued metadata");
		geneState.setLsTransaction(lsTransaction.getId());
		geneState.setRecordedBy("acas admin");
		geneState.persist();

		String entrezSeparator = "\\|";
		geneState.getLsValues().addAll(saveGeneDescriptor(lsTransaction, geneState, "stringValue", "discontinued gene id", entrezDiscontinuedGeneDTO.getDiscontinuedGeneId(), entrezSeparator));
		geneState.getLsValues().addAll(saveGeneDescriptor(lsTransaction, geneState, "stringValue", "discontinued gene symbol", entrezDiscontinuedGeneDTO.getDiscontinuedSymbol(), entrezSeparator));
		geneState.getLsValues().add(saveGeneDescriptor(lsTransaction, geneState, "dateValue", "discontinued date", entrezDiscontinuedGeneDTO.getDiscontinuedDate()));
		geneThing.getLsStates().add(geneState);
		
		return geneThing;

		
	}

	@Override
	public LsThing registerDiscontinuedGene(EntrezDiscontinuedGeneDTO geneDTO, LsTransaction lsTransaction) {
		logger.debug("create discontinued gene: " + geneDTO.getDiscontinuedGeneId());
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
		geneThing.setCodeName(getGeneCodeName());
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


	public void processDiscontinuedGenes(HashMap<String, EntrezDiscontinuedGeneDTO> discontinuedGenes,
			String taxonomyId, LsTransaction lsTransaction) {

		logger.info("########### PROCESSING DISCONTINUED GENES ##############");
		logger.info("number of discontinued genes:  " + discontinuedGenes.size());
		
		EntrezDiscontinuedGeneDTO discontinuedGene;
		String geneTypeString = "gene";
		String geneKindString = "entrez gene";
		LsThing geneThing = null;
		int geneCount = 0;
		int batchSize = 25;
		for (String key : discontinuedGenes.keySet()){
			
			logger.debug("the key is: " + key);
			discontinuedGene = discontinuedGenes.get(key);
			// check if gene currently exists
			Long geneIdCount = LsThingLabel.countOfLsThingByName(geneTypeString, geneKindString, "name", "Entrez Gene ID", discontinuedGene.getDiscontinuedGeneId());
//			Long geneIdCount = LsThingLabel.countOfLsLabelsByName("name", "Entrez Gene ID", discontinuedGene.getDiscontinuedGeneId());

			logger.debug("------------- number of genes found: " + geneIdCount + "  geneId: " + discontinuedGene.getDiscontinuedGeneId());
			
			if (geneIdCount == 0){
				geneThing = createDiscontinuedGene(discontinuedGene, lsTransaction);
			}
			
			
			
			geneCount++;

		

			if ( geneCount % batchSize == 0 ) { // same as the JDBC batch size
				logger.info("-----------------------------------------------current discontinued gene update count: " + geneCount);
				if (geneThing != null){
					geneThing.flush();
					geneThing.clear();						
				}
			}
		
		}

	}

	public LsThing createDiscontinuedGene(EntrezDiscontinuedGeneDTO geneDTO, LsTransaction lsTransaction) {
		logger.debug("create discontinued gene: " + geneDTO.getDiscontinuedGeneId());
		String geneTypeString = "gene";
		String geneKindString = "entrez gene";
//		ThingType geneType = ThingType.getOrCreate(geneTypeString);
//		ThingKind.getOrCreate(geneType, geneKindString);
//
//		LabelType labelType = LabelType.getOrCreate("name");
//		LabelKind.getOrCreate(labelType, "gene code name");
//		LabelKind.getOrCreate(labelType, "Entrez Gene ID");
//		LabelKind.getOrCreate(labelType, "gene symbol");
//
//		StateType stateType = StateType.getOrCreate("metadata");
//		StateKind.getOrCreate(stateType, "gene metadata");
//
//		ValueType valueType = ValueType.getOrCreate("stringValue");
//		ValueKind.getOrCreate(valueType, "discontinued gene id");
//		ValueKind.getOrCreate(valueType, "discontinued gene symbol");
//		ValueKind.getOrCreate(valueType, "discontinued date");
//		ValueKind.getOrCreate(valueType, "discontinued gene");
//		ValueType valueTypeDate = ValueType.getOrCreate("dateValue");
//		ValueKind.getOrCreate(valueTypeDate, "modification date");


		LsThing geneThing = new LsThing();
		geneThing.setCodeName(getGeneCodeName());
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

	
	public GeneHistoryDTO processGeneHistory(String geneHistoryFileName, String taxonomyId) throws IOException {

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
				logger.debug("header column: " + position + "  " + head);
				if (geneBeanMap.get(head) != null){
					headerList.add(geneBeanMap.get(head));   				
				}
				position++;
			}


			logger.debug("size of header list  " + headerList.size());
			String[] header = new String[headerList.size()];
			headerList.toArray(header);

			for (String head : header){
				logger.warn("header column array : " + position + "  " + head);
				position++;
			}

			while( (geneDTO = beanReader.read(EntrezDiscontinuedGeneDTO.class, header, processors)) != null ) {
				System.out.println(String.format("lineNo=%s, rowNo=%s, bulkData=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), geneDTO));
				logger.debug("current gene: " + geneDTO.getGeneId());
				if (geneDTO.getTaxId().equalsIgnoreCase(taxonomyId)){
					if (geneDTO.getGeneId().equalsIgnoreCase("-")){
						discontinuedGenes.put(geneDTO.getDiscontinuedGeneId(), geneDTO);					
					} else {
						updatedGenes.put(geneDTO.getGeneId(), geneDTO);
					}
				}

			}
			logger.debug("size of the discontinuedGenes hash is : " + discontinuedGenes.size());
			logger.debug("size of the updatedGenes hash is : " + updatedGenes.size());

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


	public class GeneHistoryDTO {
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

	public static CellProcessor[] getDiscontinuedGenesProcessors() {

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
