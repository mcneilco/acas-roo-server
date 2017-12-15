package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.MultiValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.domain.InteractionKind;
import com.labsynch.labseer.domain.InteractionType;
import com.labsynch.labseer.domain.ItxLsThingLsThing;
import com.labsynch.labseer.domain.ItxLsThingLsThingState;
import com.labsynch.labseer.domain.ItxLsThingLsThingValue;
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
import com.labsynch.labseer.dto.GeneOrthologDTO;
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
		if (dataValues != null){
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
		}
		return lsValues;
	}

	public List<LsThingLabel> saveGeneLabel(LsTransaction lsTransaction, LsThing geneThing, String labelType, String labelKind, boolean preferred, String labels, String entrezSeparator) {

		List<LsThingLabel> geneLabels = LsThingLabel.findLsThingLabelsByLsThing(geneThing).getResultList();
		Map<String, LsThingLabel> existingLabels = new HashMap<String, LsThingLabel>();
		String geneLabelKey ;
		for (LsThingLabel geneLabel : geneLabels){
			geneLabelKey = new StringBuilder().append(geneLabel.getLsType()).append("_")
					.append(geneLabel.getLsKind()).append("_").append(geneLabel.getLabelText()).toString();
			existingLabels.put(geneLabelKey, geneLabel);			
		}
		List<LsThingLabel> newLabels = new ArrayList<LsThingLabel>();
		if (labels != null) {
			String[] labelList = labels.split(entrezSeparator);
			for (String label : labelList){
				if (label.equalsIgnoreCase("-")){
					logger.debug("skipping gene label: " + label);
				} else {

					geneLabelKey = new StringBuilder().append(labelType).append("_")
							.append(labelKind).append("_").append(label).toString();

					//				if (LsThingLabel.countOfLsThingLabelsByLabel(geneThing, labelType, labelKind, label) == 0) {
					if (!existingLabels.containsKey(geneLabelKey )) {
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
						LsThingLabel existingLabel = existingLabels.get(geneLabelKey);
						if (existingLabel.isIgnored()){
							existingLabel.setIgnored(false);
							existingLabel.setDeleted(false);
							existingLabel.setModifiedDate(new Date());
							existingLabel.merge();
						}
					}
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
			newLabelSeq.setStartingNumber(1L);
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

	public LsThing registerNewGene(EntrezDbGeneDTO geneDTO, LsTransaction lsTransaction) {

		//EntrezDiscontinuedGeneDTO entrezDiscontinuedGeneDTO, 
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

	@Override
	@Transactional
	public void updateEntrezGenes(String entrezGenesFile, String geneHistoryFile, String taxonomyId) throws IOException{

		String txComment = "update entrez genes";
		LsTransaction lsTransaction = createLsTransaction(txComment);
		updateEntrezGenes(entrezGenesFile, geneHistoryFile, taxonomyId, lsTransaction);

	}


	public void updateEntrezGenes(String entrezGenesFile,
			String geneHistoryFile, String taxonomyId,
			LsTransaction lsTransaction) throws IOException {

		updateEntrezGeneEntities( entrezGenesFile, taxonomyId, lsTransaction );
		logger.info("############# Completed updating Entrez genes ###################");

		GeneHistoryDTO processedHistory = processGeneHistory(geneHistoryFile, taxonomyId);
		logger.info("############# Completed processing gene history file ###################");


		processDiscontinuedGenes(processedHistory.getDiscontinuedGenes(), lsTransaction);
		logger.info("############# Completed adding discounted genes ###################");


		processUpdatedGenes(processedHistory.getUpdatedGenes(), lsTransaction);
		logger.info("############# Completed updating genes ###################");

		//updateGeneHistoryEntities( geneHistoryFile, taxonomyId, lsTransaction );

	}


	private void processUpdatedGenes(List<EntrezDiscontinuedGeneDTO> updatedGenes, LsTransaction lsTransaction) {
		int i = 0;
		int batchSize = 50;
		LsThing geneThing = null;
		logger.info("Processing updated genes");
		for (EntrezDiscontinuedGeneDTO geneDTO : updatedGenes){
			geneThing = updateMergedGene(geneDTO, lsTransaction);

			if ( i % batchSize == 0 ) { // same as the JDBC batch size
				if (geneThing != null){
					geneThing.flush();
					geneThing.clear();						
				}
			}
			i++;
			logger.info("-----------------------------------------------current updated gene count: " + i);
		}

	}

	private void processDiscontinuedGenes(
			List<EntrezDiscontinuedGeneDTO> discontinuedGenes, LsTransaction lsTransaction) {

		int i = 0;
		int batchSize = 50;
		LsThing geneThing = null;
		logger.info("Processing discontinued genes");
		for (EntrezDiscontinuedGeneDTO geneDTO : discontinuedGenes){
			geneThing = registerDiscontinuedGeneNew(geneDTO, lsTransaction);

			if ( i % batchSize == 0 ) { // same as the JDBC batch size
				if (geneThing != null){
					geneThing.flush();
					geneThing.clear();						
				}
			}
			i++;
			logger.info("-----------------------------------------------current discontinued gene count: " + i);
		}


	}

	private void updateGeneHistoryEntities(String geneHistoryFile, String taxonomyId, LsTransaction lsTransaction) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(geneHistoryFile));
		ICsvBeanReader beanReader = null;
		final CellProcessor[] processors = getDiscontinuedGenesProcessors();
		EntrezDiscontinuedGeneDTO geneDTO;
		HashMap<String, String> geneBeanMap = new HashMap<String, String>();
		geneBeanMap.put("tax_id", "taxId");
		geneBeanMap.put("GeneID", "geneId");
		geneBeanMap.put("Discontinued_GeneID", "discontinuedGeneId");
		geneBeanMap.put("Discontinued_Symbol", "discontinuedSymbol");
		geneBeanMap.put("Discontinue_Date", "discontinuedDate");

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
				logger.debug(String.format("lineNo=%s, rowNo=%s, bulkData=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), geneDTO));
				logger.info("current gene: " + geneDTO.getGeneId());
				if (geneDTO.getTaxId().equalsIgnoreCase(taxonomyId)){
					if (geneDTO.getGeneId().equalsIgnoreCase("-")){

						registerDiscontinuedGeneNew(geneDTO, lsTransaction);

					} else {
						updateMergedGene(geneDTO, lsTransaction);
					}
				}

			}
		}		
		finally {
			if( beanReader != null ) {
				beanReader.close();
			}
		}		

	}


	private LsThing updateMergedGene(EntrezDiscontinuedGeneDTO geneDTO, LsTransaction lsTransaction) {

		List<LsThing> oldGenes = LsThing.findAllLsThingByLabelTypeAndKindAndLabelText("gene", "entrez gene", "name", "Entrez Gene ID", geneDTO.getDiscontinuedGeneId()).getResultList();
		List<LsThing> activeGenes = LsThing.findAllLsThingByPreferredLabelText("gene", "entrez gene", "name", "Entrez Gene ID", geneDTO.getGeneId()).getResultList();
		//		List<LsThing> activeGenes = LsThing.findAllLsThingByPreferredLabelText("gene", "entrez gene", "name", "Entrez Gene ID", geneDTO.getGeneId()).getResultList();

		logger.debug("Number of oldGenes: " + oldGenes.size() + "  number of activeGenes: " + activeGenes.size());


		LsThing geneThing;
		LsThing oldGene;

		if (oldGenes.size() == 0 && activeGenes.size() == 0){
			//check by any label
			List<LsThing> allGenes = LsThing.findAllLsThingByLabelTypeAndKindAndLabelText("gene", "entrez gene", "name", "Entrez Gene ID", geneDTO.getGeneId()).getResultList();
			if (allGenes.size() > 0){
				logger.info("Found a gene entity by non-preferred id  -- could be a previously merged gene.  Number of genes found: " + allGenes.size());
				for (LsThing foundGene : allGenes){
					logger.info(foundGene.toPrettyJsonStub());
				}
				if (allGenes.size() == 1){
					//if just a single gene -- set it as the active gene?
					activeGenes.add(allGenes.get(0));
					logger.info("active gene found" + allGenes.get(0).toPrettyJsonStub());
				}
			} else {
				if (logger.isDebugEnabled()) logger.debug("registering new gene: " + geneDTO.getGeneId());
				EntrezDbGeneDTO newGeneDTO = new EntrezDbGeneDTO();
				newGeneDTO.setGeneId(geneDTO.getGeneId());
				geneThing = registerNewGene(newGeneDTO, lsTransaction);
				activeGenes = LsThing.findAllLsThingByPreferredLabelText("gene", "entrez gene", "name", "Entrez Gene ID", geneDTO.getGeneId()).getResultList();
				oldGenes = LsThing.findAllLsThingByLabelTypeAndKindAndLabelText("gene", "entrez gene", "name", "Entrez Gene ID", geneDTO.getDiscontinuedGeneId()).getResultList();
			}

		} 

		if (activeGenes.size() == 1 && oldGenes.size() > 1) {
			List<LsThing> removeGeneList = new ArrayList<LsThing>();
			for (LsThing query : oldGenes){
				if (query.getId().longValue() == activeGenes.get(0).getId().longValue()){
					removeGeneList.add(query);
				}
			}
			for (LsThing query : removeGeneList){
				oldGenes.remove(query);
			}			
			logger.debug("old genes size is now: " + oldGenes.size()); 
		}

		if (oldGenes.size() == 0 && activeGenes.size() == 1){
			// add discontinued Gene ID to the active Gene -- unless it already has the gene ID
			geneThing = activeGenes.get(0);

			//check if old gene ID exists, if not create it
			Long countOfGeneID = LsThingLabel.countOfLsThingLabelsByLabel(geneThing, "name", "Entrez Gene ID", geneDTO.getDiscontinuedGeneId());
			if (countOfGeneID == 0){
				geneThing.getLsLabels().add(saveGeneLabel(lsTransaction, geneThing, "name", "Entrez Gene ID", false, geneDTO.getDiscontinuedGeneId()));
			}

			Long countOfGeneSymbol = LsThingLabel.countOfLsThingLabelsByLabel(geneThing, "name", "gene symbol", geneDTO.getDiscontinuedSymbol());
			if (countOfGeneSymbol == 0){
				geneThing.getLsLabels().add(saveGeneLabel(lsTransaction, geneThing, "name", "gene symbol", false, geneDTO.getDiscontinuedSymbol()));
			}



		} else if (oldGenes.size() == 1 && activeGenes.size() == 1) {
			oldGene = oldGenes.get(0);
			geneThing = activeGenes.get(0);
			//are the genes the same?
			boolean sameGene = false;
			if (oldGene.getId().longValue() == geneThing.getId().longValue()){
				logger.debug("@@@@@@  Mapping back to the same gene   @@@@@@     " + geneThing.getCodeName());
				sameGene = true;
			}
			if (sameGene){
				//Do nothing -- fine
			} else {
				// ignore the old gene and add labels  --- 				
				oldGene = discontinueGene(oldGene, geneDTO, lsTransaction);

				// ignore non Gene ID lables
				Collection<LsThingLabel> geneLabels = oldGene.getLsLabels();
				for (LsThingLabel geneLabel : geneLabels){
					if (!geneLabel.getLsType().equalsIgnoreCase("name") && !geneLabel.getLsKind().equalsIgnoreCase("Entrez Gene ID")){
						if (!geneLabel.isIgnored()){
							geneLabel.setIgnored(true);
							geneLabel.setModifiedDate(new Date());
							geneLabel.merge();
						}
					}
				}

				//add reference labels
				List<LsThingLabel> oldGeneIdLabels = LsThingLabel.findLsThingLabelsByLsThingAndLsTypeAndLsKindAndLabelText(oldGene, "name", "Entrez Gene ID", geneDTO.getGeneId()).getResultList();
				if (oldGeneIdLabels.size() == 0){
					oldGene.getLsLabels().add(saveGeneLabel(lsTransaction, oldGene, "name", "Entrez Gene ID", false, geneDTO.getGeneId()));
				}

				List<LsThingLabel> newGeneIdLabels = LsThingLabel.findLsThingLabelsByLsThingAndLsTypeAndLsKindAndLabelText(geneThing, "name", "Entrez Gene ID", geneDTO.getDiscontinuedGeneId()).getResultList();
				if (newGeneIdLabels.size() == 0){
					geneThing.getLsLabels().add(saveGeneLabel(lsTransaction, geneThing, "name", "Entrez Gene ID", false, geneDTO.getDiscontinuedGeneId()));
				}

			}


		} else if (activeGenes.size() == 0 && oldGenes.size() == 1) {
			// activeGene should = to 1 old and the other is the original
			List<LsThing> geneThingsAll = LsThing.findAllLsThingByLabelTypeAndKindAndLabelText("gene", "entrez gene", "name", "Entrez Gene ID", geneDTO.getGeneId()).getResultList();
			geneThing = geneThingsAll.get(0);
			boolean matchActive = false;
			boolean otherIgnored = false;
			for (LsThing oldGeneQuery : oldGenes){
				if (oldGeneQuery.getId().longValue() == geneThing.getId().longValue()){
					matchActive = true;
				}  else {
					if (oldGeneQuery.isIgnored()){
						logger.debug("the gene is ignored: " + oldGeneQuery.getCodeName());
						otherIgnored = true;
					}
				}
			}

			if (!matchActive && !otherIgnored){
				logger.error(geneDTO.toJson());
				logger.error("Hit an error case!!!  Error case with old and new genes");

				throw new RuntimeException("Error case with old and new genes");
			}

		} else {
			// potential error case
			logger.error("Number of oldGenes: " + oldGenes.size() + "  number of activeGenes: " + activeGenes.size());
			logger.error(geneDTO.toJson());
			logger.error("Hit an error case!!!");
			for (LsThing query : activeGenes){
				logger.error("Active Gene");
				logger.error(query.toPrettyJsonStub());
			}
			for (LsThing query : oldGenes){
				logger.error("Old Gene");
				logger.error(query.toPrettyJsonStub());
			}

			throw new RuntimeException("Error case");

		}

		return geneThing;

	}


	private LsThing registerDiscontinuedGeneNew(EntrezDiscontinuedGeneDTO geneDTO, LsTransaction lsTransaction) {

		LsThing geneThing = null;

		List<LsThing> genes = LsThing.findAllLsThingByLabelTypeAndKindAndLabelText("gene", "entrez gene", "name", "Entrez Gene ID", geneDTO.getDiscontinuedGeneId()).getResultList();

		if (genes.size() == 0){
			//create new discontinued gene
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

			geneThing = new LsThing();
			geneThing.setCodeName(getGeneCodeName());
			geneThing.setLsType(geneTypeString);
			geneThing.setLsKind(geneKindString);
			geneThing.setLsTransaction(lsTransaction.getId());
			geneThing.setRecordedBy("acas admin");
			geneThing.persist();

			String entrezSeparator = "\\|";
			geneThing.getLsLabels().addAll(saveGeneLabel(lsTransaction, geneThing, "name", "Entrez Gene ID", true, geneDTO.getDiscontinuedGeneId(), entrezSeparator));
			geneThing.getLsLabels().addAll(saveGeneLabel(lsTransaction, geneThing, "name", "gene symbol", false, geneDTO.getDiscontinuedSymbol(), entrezSeparator));

			LsThingState geneState = new LsThingState();
			geneState.setLsThing(geneThing);
			geneState.setLsType("metadata");
			geneState.setLsKind("gene metadata");
			geneState.setLsTransaction(lsTransaction.getId());
			geneState.setRecordedBy("acas admin");
			geneState.persist();

			geneState.getLsValues().addAll(saveGeneDescriptor(lsTransaction, geneState, "stringValue", "tax id", geneDTO.getTaxId(), entrezSeparator));
			geneState.getLsValues().addAll(saveGeneDescriptor(lsTransaction, geneState, "stringValue", "discontinued gene", "true", entrezSeparator));
			geneState.getLsValues().addAll(saveGeneDescriptor(lsTransaction, geneState, "stringValue", "discontinued gene id", geneDTO.getDiscontinuedGeneId(), entrezSeparator));
			geneState.getLsValues().addAll(saveGeneDescriptor(lsTransaction, geneState, "stringValue", "discontinued gene symbol", geneDTO.getDiscontinuedSymbol(), entrezSeparator));
			geneState.getLsValues().add(saveGeneDescriptor(lsTransaction, geneState, "dateValue", "discontinued date", geneDTO.getDiscontinuedDate()));

		} else if (genes.size() == 1){
			//update existing gene

			geneThing = genes.get(0);

			// is this an active gene or a merged gene?
			// don't want to add discontinued meta data to an active gene?
			List<LsThingLabel> geneLabels = LsThingLabel.findLsThingPreferredName(geneThing.getId(), "name", "Entrez Gene ID").getResultList();
			boolean geneMatch = false;
			for (LsThingLabel geneLabel : geneLabels){
				if (geneLabel.getLabelText().equalsIgnoreCase(geneDTO.getDiscontinuedGeneId())){
					geneMatch = true;
				}
			}
			if (geneMatch) {
				geneThing = addDiscontinueMetadata(geneThing, geneDTO, lsTransaction);
			} else {
				logger.error(geneThing.getCodeName());
				logger.error(geneDTO.toJson());
				throw new RuntimeException("Match to reference gene ");
			}

		} else if (genes.size() > 1){
			logger.error("ERROR: Found multiple genes");
			for (LsThing gene : genes){
				logger.error(gene.toJsonStub());
			}

			throw new RuntimeException("Multiple genes found");

			// possible due to merged genes?

		}

		return geneThing;

	}


	public void updateEntrezGeneEntities(String entrezGenesFile, String taxonomyId, LsTransaction lsTransaction) throws IOException {

		setGeneDefaults();
		logger.debug("read tab delimited file");
		BufferedReader br = new BufferedReader(new FileReader(entrezGenesFile));

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
				logger.debug("header column array : " + position + "  " + head);
				position++;
			}

			final CellProcessor[] processors = getProcessors();
			EntrezDbGeneDTO geneDTO;

			int i = 0;
			int batchSize = 25;
			LsThing geneThing = null;
			while( (geneDTO = beanReader.read(EntrezDbGeneDTO.class, header, processors)) != null ) {
				logger.debug(String.format("lineNo=%s, rowNo=%s, bulkData=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), geneDTO));
				logger.debug("------------------------------- Here is the geneDTO ---------------------");
				logger.debug(geneDTO.toJson());
				logger.info("current gene: " + geneDTO.getGeneId());

				// check if gene currently exists
				Long geneIdCount = LsThingLabel.countOfAllLsThingByPreferredName(geneTypeString, geneKindString, "name", "Entrez Gene ID", geneDTO.getGeneId());
				logger.debug("Process Entrez Genes ------------------- geneId count "  + geneIdCount);


				// may need to see if the gene ID exists on another gene -- not as a preferred name  Is that possible?
				if (geneIdCount == 0L ) {
					// brand new gene to register
					if (logger.isDebugEnabled()) logger.debug("registering new gene: " + geneDTO.getGeneId());
					geneThing = registerNewGene(geneDTO, lsTransaction);

				} else if (geneIdCount == 1L) {
					if (logger.isDebugEnabled()) logger.debug("gene already registered: " + geneDTO.getGeneId());
					geneThing = updateExistingGene(geneDTO, lsTransaction);
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

	private LsThing updateExistingGene(EntrezDbGeneDTO geneDTO, LsTransaction lsTransaction) {

		String geneTypeString = "gene";
		String geneKindString = "entrez gene";

		logger.debug("searching for new genes by: " + geneDTO.getGeneId());
		List<LsThing> genes = LsThing.findAllLsThingByPreferredLabelText(geneTypeString, geneKindString, "name", "Entrez Gene ID",  geneDTO.getGeneId()).getResultList();
		logger.debug("------------ the gene ID count is: " + genes.size() + "  for new gene " + geneDTO.getGeneId());

		LsThing geneThing = null ;
		if (genes.size() == 0){
			logger.error("Gene not found");
		} else if (genes.size() > 1){
			logger.error("multiple genes found with the same gene ID");
		} else {
			geneThing = genes.get(0);
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

			// only update the gene if it is a new update (in Entrez Gene file = geneDTO)
			if (isNewDate){
				logger.debug("Updating the gene metadata");
				geneThing = updateGeneMetaData(geneThing, geneDTO, lsTransaction);

			} else {
				logger.debug("@@@@@@ NOT A NEW DATE @@@@@@@@");
			}
		}

		return geneThing;
	}




	//	public LsThing updateGene(EntrezDbGeneDTO geneDTO, EntrezDiscontinuedGeneDTO entrezDiscontinuedGeneDTO, LsTransaction lsTransaction) {
	//		String geneTypeString = "gene";
	//		String geneKindString = "entrez gene";
	//		List<LsThing> genes = LsThing.findLsThingByAllLabelText(geneTypeString, geneKindString, "name", "Entrez Gene ID", entrezDiscontinuedGeneDTO.getDiscontinuedGeneId()).getResultList();
	//		List<LsThing> activeGenes = LsThing.findLsThingByPreferredLabelText(geneTypeString, geneKindString, "name", "Entrez Gene ID", geneDTO.getGeneId()).getResultList();
	//
	//		LsThing geneThing = null ;
	//
	//		logger.debug("Searching for discontinued gene: " + entrezDiscontinuedGeneDTO.getDiscontinuedGeneId());
	//		logger.debug("updateGene:  number of discontinued genes found: " + genes.size());
	//		logger.debug("Searching for active gene: " + geneDTO.getGeneId());
	//		logger.debug("updateGene:  number of active genes found: " + activeGenes.size());
	//
	//
	//		if (genes.size() == 0){
	//			logger.error("Gene not found");
	//			throw new RuntimeException("Gene not found");
	//		} else if (genes.size() > 1){
	//			logger.error("multiple genes found with the same gene ID");
	//			throw new RuntimeException("multiple genes found with the same gene ID");			
	//		} else {
	//			geneThing = genes.get(0);
	//			geneThing = updateGeneID(geneThing, geneDTO, entrezDiscontinuedGeneDTO, lsTransaction);
	//			geneThing = updateGeneMetaData(geneThing, geneDTO, lsTransaction);
	//		}
	//
	//		return geneThing;
	//	}

	public LsThing updateGeneMetaData(LsThing geneThing, EntrezDbGeneDTO geneDTO, LsTransaction lsTransaction) {

		// ignore old states
		for (LsThingState state : geneThing.getLsStates()){
			if (state.getLsType().equalsIgnoreCase("metadata") && state.getLsKind().equalsIgnoreCase("gene metadata")){
				state.setIgnored(true);
				state.setModifiedBy("acas admin");
				state.setModifiedDate(new Date());
				state.merge();				
			}
		}


		Set<LsThingLabel> allGeneLabels = geneThing.getLsLabels();
		String geneLabelKey ;
		Map<String, LsThingLabel> existingLabels = new HashMap<String, LsThingLabel>();
		for (LsThingLabel geneLabel : allGeneLabels){
			geneLabelKey = new StringBuilder().append(geneLabel.getLsType()).append("_")
					.append(geneLabel.getLsKind()).append("_").append(geneLabel.getLabelText()).toString();
			existingLabels.put(geneLabelKey, geneLabel);			
		}		

		logger.debug("number of labels: " + existingLabels.size());

		String entrezSeparator = "\\|";
		existingLabels = saveGeneLabel(existingLabels, lsTransaction, geneThing, "name", "gene symbol", false, geneDTO.getSymbol(), entrezSeparator);
		existingLabels = saveGeneLabel(existingLabels, lsTransaction, geneThing, "name", "gene synonym", false, geneDTO.getSynonyms(), entrezSeparator);
		existingLabels = saveGeneLabel(existingLabels, lsTransaction, geneThing, "name", "authoritative gene name", false, geneDTO.getFullNameFromAuthority(), entrezSeparator);
		existingLabels = saveGeneLabel(existingLabels, lsTransaction, geneThing, "name", "authoritative gene symbol", false, geneDTO.getSymbolFromAuthority(), entrezSeparator);
		existingLabels = saveGeneLabel(existingLabels, lsTransaction, geneThing, "name", "other designations", false, geneDTO.getOtherDesignations(), entrezSeparator);

		// ignore all other existing genes
		LsThingLabel geneLabel;
		Set<String> labelKeys = existingLabels.keySet();
		for (String labelKey : labelKeys){
			geneLabel = existingLabels.get(labelKey);
			if (geneLabel.getLsType().equalsIgnoreCase("name") && geneLabel.getLsKind().equalsIgnoreCase("Entrez Gene ID")){
				// keep the Entrez Gene ID labels
			} else {
				// ignore the rest
				logger.debug("@@@@@@@@@@ Ignoring the label:   " + geneLabel.getLabelText());
				geneLabel.setIgnored(true);
				geneLabel.setDeleted(true);
				geneLabel.setModifiedDate(new Date());
				geneLabel.merge();
			}
		}

		geneThing.setLsLabels(geneThing.getLsLabels());

		// mark all old labels as ignored and deleted
		// harder to parse changing labels between updates, i.e. authorized symbols and etc
		//		for (LsThingLabel geneLabel : geneThing.getLsLabels()){
		//			if (geneLabel.getLsType().equalsIgnoreCase("name") 
		//					&& geneLabel.getLsKind().equalsIgnoreCase("Entrez Gene ID") 
		//					&& geneLabel.getLabelText().equalsIgnoreCase(geneDTO.getGeneId())){
		//
		//				logger.debug("Leaving Entrez Gene ID label: " + geneLabel.toJson());
		//			} else {
		//
		//				logger.debug("@@@@@@@@@@@@@@  IGNORING GENE LABELS @@@@@@@@@@@@@    GeneThing: " + geneThing.getCodeName() + "  " + geneLabel.getLabelText() );
		//				// ignore all of the existing labels except Entrez Gene ID
		//				geneLabel.setIgnored(true);
		//				geneLabel.setDeleted(true);
		//				geneLabel.setModifiedDate(new Date());
		//				geneLabel.merge();
		//
		//			}
		//		}
		//
		//		//		geneThing.getLsLabels().addAll(saveGeneLabel(lsTransaction, geneThing, "name", "Entrez Gene ID", true, geneDTO.getGeneId(), entrezSeparator));
		//		geneThing.getLsLabels().addAll(saveGeneLabel(lsTransaction, geneThing, "name", "gene symbol", false, geneDTO.getSymbol(), entrezSeparator));
		//		geneThing.getLsLabels().addAll(saveGeneLabel(lsTransaction, geneThing, "name", "gene synonym", false, geneDTO.getSynonyms(), entrezSeparator));
		//		geneThing.getLsLabels().addAll(saveGeneLabel(lsTransaction, geneThing, "name", "authoritative gene name", false, geneDTO.getFullNameFromAuthority(), entrezSeparator));
		//		geneThing.getLsLabels().addAll(saveGeneLabel(lsTransaction, geneThing, "name", "authoritative gene symbol", false, geneDTO.getSymbolFromAuthority(), entrezSeparator));
		//		geneThing.getLsLabels().addAll(saveGeneLabel(lsTransaction, geneThing, "name", "other designations", false, geneDTO.getOtherDesignations(), entrezSeparator));

		LsThingState geneState = new LsThingState();
		geneState.setLsThing(geneThing);
		geneState.setLsType("metadata");
		geneState.setLsKind("gene metadata");
		geneState.setLsTransaction(lsTransaction.getId());
		geneState.setRecordedBy("acas admin");
		geneState.setRecordedDate(new Date());
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


	private Map<String, LsThingLabel> saveGeneLabel(Map<String, LsThingLabel> existingLabels,
			LsTransaction lsTransaction, LsThing geneThing, String labelType,
			String labelKind, boolean preferred, String labels, String entrezSeparator) {

		String geneLabelKey ;
		String[] labelList = labels.split(entrezSeparator);
		for (String label : labelList){
			if (label.equalsIgnoreCase("-")){
				logger.debug("skipping gene label: " + label);
			} else {

				geneLabelKey = new StringBuilder().append(labelType).append("_")
						.append(labelKind).append("_").append(label).toString();

				if (!existingLabels.containsKey(geneLabelKey )) {
					LsThingLabel lsLabel = new LsThingLabel();
					lsLabel.setLsThing(geneThing);
					lsLabel.setLsType(labelType);
					lsLabel.setLsKind(labelKind);
					lsLabel.setPreferred(preferred);
					lsLabel.setLabelText(label);
					lsLabel.setLsTransaction(lsTransaction.getId());
					lsLabel.setRecordedBy("acas admin");
					lsLabel.persist();
					logger.debug("saving new gene label: " + label);
				} else {
					logger.debug("-------------- the label already exists -- not creating new entry" + label);
					existingLabels.remove(geneLabelKey);
				}
			}
		}		
		return existingLabels;

	}

	public LsThing discontinueGene(LsThing inactiveGene, EntrezDiscontinuedGeneDTO discontinuedGeneDTO, LsTransaction lsTransaction) {
		inactiveGene = addDiscontinueMetadata(inactiveGene, discontinuedGeneDTO, lsTransaction);

		//ignore all non Gene ID labels
		Set<LsThingLabel> geneLabels = inactiveGene.getLsLabels();
		for (LsThingLabel geneLabel : geneLabels){
			if (geneLabel.getLsType().equalsIgnoreCase("name") && geneLabel.getLsKind().equalsIgnoreCase("Entrez Gene ID")){
				// do nothing -- keep the Entrez Gene ID labels
			} else {
				geneLabel.setIgnored(true);
				geneLabel.setModifiedDate(new Date());
				geneLabel.merge();
			}			
		}


		logger.debug("@@@@@@@@@@@@ Gene to be ingored @@@@@@@@@@@@@@@@@@     " + inactiveGene.getCodeName());
		if (!inactiveGene.isIgnored()){
			inactiveGene.setIgnored(true);
			inactiveGene.setModifiedBy("acas admin");
			inactiveGene.setModifiedDate(new Date());
			inactiveGene.merge();
		}

		return inactiveGene;

	}

	public LsThing addDiscontinueMetadata(LsThing geneThing, EntrezDiscontinuedGeneDTO entrezDiscontinuedGeneDTO, LsTransaction lsTransaction) {

		List<LsThingValue> thingValues = LsThingValue.findLsThingValuesByLsThingIDAndStateTypeKindAndValueTypeKind(geneThing.getId(), "metadata", "gene discontinued metadata", "stringValue", "discontinued gene").getResultList();

		boolean discontinued = false;
		for (LsThingValue thingValue : thingValues){
			if (!thingValue.isIgnored() && thingValue.getStringValue().equalsIgnoreCase("true")){
				discontinued = true;
			}
		}

		logger.debug("Query discontinue Gene ID: " + entrezDiscontinuedGeneDTO.getDiscontinuedGeneId());

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

			logger.debug("discontinue Gene ID: " + entrezDiscontinuedGeneDTO.getDiscontinuedGeneId());

			String entrezSeparator = "\\|";
			geneState.getLsValues().addAll(saveGeneDescriptor(lsTransaction, geneState, "stringValue", "discontinued gene", "true", entrezSeparator));
			geneState.getLsValues().addAll(saveGeneDescriptor(lsTransaction, geneState, "stringValue", "discontinued gene id", entrezDiscontinuedGeneDTO.getDiscontinuedGeneId(), entrezSeparator));
			geneState.getLsValues().addAll(saveGeneDescriptor(lsTransaction, geneState, "stringValue", "discontinued gene symbol", entrezDiscontinuedGeneDTO.getDiscontinuedSymbol(), entrezSeparator));
			geneState.getLsValues().add(saveGeneDescriptor(lsTransaction, geneState, "dateValue", "discontinued date", entrezDiscontinuedGeneDTO.getDiscontinuedDate()));
			geneThing.getLsStates().add(geneState);
		}

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


		//		MultiValueMap updatedGenes = new MultiValueMap();
		//		MultiValueMap discontinuedGenes = new MultiValueMap();

		List<EntrezDiscontinuedGeneDTO> discontinuedGenes = new ArrayList<EntrezDiscontinuedGeneDTO>();
		List<EntrezDiscontinuedGeneDTO> updatedGenes = new ArrayList<EntrezDiscontinuedGeneDTO>();

		//HashMap<String, EntrezDiscontinuedGeneDTO> updatedGenes = new HashMap<String, EntrezDiscontinuedGeneDTO>();
		//HashMap<String, EntrezDiscontinuedGeneDTO> discontinuedGenes = new HashMap<String, EntrezDiscontinuedGeneDTO>();

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
						//						discontinuedGenes.put(geneDTO.getDiscontinuedGeneId(), geneDTO);	
						discontinuedGenes.add(geneDTO);
					} else {
						//updatedGenes.put(geneDTO.getGeneId(), geneDTO);
						updatedGenes.add(geneDTO);
						// note: using a multi value map to allow multiple instances

					}
				}

			}

			//			@SuppressWarnings("unchecked")
			//			Set<String> updatedGeneKeys = updatedGenes.keySet();
			//			Collection<String> uniqueGenes = new HashSet<String>();
			//			for (String queryGeneId : updatedGeneKeys){
			//				List<EntrezDiscontinuedGeneDTO> updatedGeneList = (List<EntrezDiscontinuedGeneDTO>) updatedGenes.get(queryGeneId);
			//				for (EntrezDiscontinuedGeneDTO updateGene : updatedGeneList){
			//					uniqueGenes.add(updateGene.getDiscontinuedGeneId());
			//				}
			//			}

			logger.debug("size of the discontinuedGenes hash is : " + discontinuedGenes.size());
			logger.debug("size of the updatedGenes hash is : " + updatedGenes.size());

		}		
		finally {
			if( beanReader != null ) {
				beanReader.close();
			}
		}		

		Collections.sort(discontinuedGenes, new Comparator<EntrezDiscontinuedGeneDTO>() {
			public int compare(EntrezDiscontinuedGeneDTO m1, EntrezDiscontinuedGeneDTO m2) {
				return m2.getDiscontinuedDate().compareTo(m1.getDiscontinuedDate());
			}
		});

		Collections.sort(updatedGenes, new Comparator<EntrezDiscontinuedGeneDTO>() {
			public int compare(EntrezDiscontinuedGeneDTO m1, EntrezDiscontinuedGeneDTO m2) {
				return m2.getDiscontinuedDate().compareTo(m1.getDiscontinuedDate());
			}
		});

		GeneHistoryDTO ghDTO = new GeneHistoryDTO();
		ghDTO.setDiscontinuedGenes(discontinuedGenes);
		ghDTO.setUpdatedGenes(updatedGenes);

		return ghDTO;

	}

	public class GeneHistoryDTO {
		private List<EntrezDiscontinuedGeneDTO> discontinuedGenes;
		private List<EntrezDiscontinuedGeneDTO> updatedGenes;
		public List<EntrezDiscontinuedGeneDTO> getDiscontinuedGenes() {
			return discontinuedGenes;
		}
		public void setDiscontinuedGenes(List<EntrezDiscontinuedGeneDTO> discontinuedGenes) {
			this.discontinuedGenes = discontinuedGenes;
		}
		public List<EntrezDiscontinuedGeneDTO> getUpdatedGenes() {
			return updatedGenes;
		}
		public void setUpdatedGenes(List<EntrezDiscontinuedGeneDTO> updatedGenes) {
			this.updatedGenes = updatedGenes;
		}
	}



	//	public void addDiscontinuedGeneIDs(GeneHistoryDTO historyResults,
	//			String taxonomyId, LsTransaction lsTransaction) {
	//		//check if gene exists -- 
	//		// check if discontinued gene ID label exists -- if not -- create it
	//		LsThing geneThing = null;
	//
	//		MultiValueMap updatedGenes = historyResults.getUpdatedGenes();
	//		Set<String> geneIdKeys = updatedGenes.keySet();
	//		int batchSize = 50;
	//		int i = 0;
	//		for (String geneIdKey : geneIdKeys){
	//			for (EntrezDiscontinuedGeneDTO singleGene : (List<EntrezDiscontinuedGeneDTO>) updatedGenes.get(geneIdKey)){
	//				geneThing = updateGeneLabels(singleGene, lsTransaction);
	//
	//				if ( i % batchSize == 0 ) { // same as the JDBC batch size
	//					if (geneThing != null){
	//						geneThing.flush();
	//						geneThing.clear();						
	//					}
	//				}
	//				i++;
	//			}
	//		}
	//
	//	}
	//
	//	private LsThing updateGeneLabels(EntrezDiscontinuedGeneDTO singleGene, LsTransaction lsTransaction) {
	//
	//		List<LsThing> discontinuedGenes = LsThing.findLsThingByLabelText("gene", "entrez gene", "name", "Entrez Gene ID", singleGene.getDiscontinuedGeneId()).getResultList();
	//		List<LsThing> genes = LsThing.findLsThingByLabelText("gene", "entrez gene", "name", "Entrez Gene ID", singleGene.getGeneId()).getResultList();
	//
	//		int discontinuedGenesCount = discontinuedGenes.size();
	//		int geneCount = genes.size();
	//
	//		logger.debug("------------ the gene ID count is: " + discontinuedGenes.size() + "  for discontinued gene " + singleGene.getDiscontinuedGeneId());
	//		logger.debug("------------ the gene ID count is: " + genes.size() + "  for new gene " + singleGene.getGeneId());
	//
	//		LsThing geneThing = null ;
	//		if (discontinuedGenesCount == 0 && geneCount == 0){
	//			logger.debug("gene is not registered yet");
	//		} else if (discontinuedGenesCount == 0 && geneCount == 1 ){
	//			logger.debug("@@@@@@ ------ add discontinued geneID to the geneThing");		
	//			geneThing = genes.get(0);
	//
	//			//check if old gene ID exists, if not create it
	//			Long countOfGeneID = LsThingLabel.countOfLsThingLabelsByLabel(geneThing, "name", "Entrez Gene ID", singleGene.getDiscontinuedGeneId());
	//			Long countOfGeneSymbol = LsThingLabel.countOfLsThingLabelsByLabel(geneThing, "name", "gene symbol", singleGene.getDiscontinuedSymbol());
	//			if (countOfGeneID == 0){
	//				geneThing.getLsLabels().add(saveGeneLabel(lsTransaction, geneThing, "name", "Entrez Gene ID", false, singleGene.getDiscontinuedGeneId()));
	//			}
	//			if (countOfGeneSymbol == 0){
	//				geneThing.getLsLabels().add(saveGeneLabel(lsTransaction, geneThing, "name", "gene symbol", false, singleGene.getDiscontinuedSymbol()));
	//			}
	//
	//			// add discontinued meta to the old gene
	//
	//		}
	//
	//		return geneThing;
	//	}

	//	@SuppressWarnings("unchecked")
	//	public void processEntrezGenes(String entrezGenes, GeneHistoryDTO historyResults, 
	//			String taxonomyId, LsTransaction lsTransaction) throws IOException {
	//
	//		setGeneDefaults();
	//
	//		logger.debug("read tab delimited file");
	//		BufferedReader br = new BufferedReader(new FileReader(entrezGenes));
	//
	//		ICsvBeanReader beanReader = null;
	//		try {
	//
	//			beanReader = new CsvBeanReader(br, CsvPreference.TAB_PREFERENCE);
	//			String[] headerText = beanReader.getHeader(true);
	//
	//			String geneTypeString = "gene";
	//			String geneKindString = "entrez gene";
	//
	//			// map header text to standard bean names
	//
	//			HashMap<String, String> geneBeanMap = new HashMap<String, String>();
	//			geneBeanMap.put("tax_id", "taxId");
	//			geneBeanMap.put("GeneID", "geneId");
	//			geneBeanMap.put("Symbol", "symbol");
	//			geneBeanMap.put("LocusTag", "locusTag");
	//			geneBeanMap.put("Synonyms", "synonyms");
	//			geneBeanMap.put("dbXrefs", "dbXrefs");
	//			geneBeanMap.put("chromosome", "chromosome");
	//			geneBeanMap.put("map_location", "mapLocation");
	//			geneBeanMap.put("description", "description");
	//			geneBeanMap.put("type_of_gene", "typeOfGene");
	//			geneBeanMap.put("Symbol_from_nomenclature_authority", "symbolFromAuthority");
	//			geneBeanMap.put("Full_name_from_nomenclature_authority", "fullNameFromAuthority");
	//			geneBeanMap.put("Nomenclature_status", "nomenclatureStatus");
	//			geneBeanMap.put("Other_designations", "otherDesignations");
	//			geneBeanMap.put("Modification_date", "modificationDate");
	//
	//			List<String> headerList = new ArrayList<String>();
	//			int position = 0;
	//			for (String head : headerText){
	//				logger.debug("header column: " + position + "  " + head);
	//				if (geneBeanMap.get(head) != null){
	//					headerList.add(geneBeanMap.get(head));   				
	//				}
	//				position++;
	//			}
	//
	//
	//			logger.debug("size of header list  " + headerList.size());
	//			String[] header = new String[headerList.size()];
	//			headerList.toArray(header);
	//
	//			for (String head : header){
	//				logger.warn("header column array : " + position + "  " + head);
	//				position++;
	//			}
	//
	//
	//			final CellProcessor[] processors = getProcessors();
	//
	//			EntrezDbGeneDTO geneDTO;
	//
	//			//			int numberOfLines = SimpleUtil.countLines(entrezGenes);
	//			//			Long numberOfLabels = new Long(numberOfLines-1);
	//			//			
	//			//			List<AutoLabelDTO> thingCodes = autoLabelService.getAutoLabels(thingTypeAndKind, labelTypeAndKind, numberOfLabels);
	//
	//			int i = 0;
	//			int batchSize = 25;
	//			LsThing geneThing = null;
	//			while( (geneDTO = beanReader.read(EntrezDbGeneDTO.class, header, processors)) != null ) {
	//				System.out.println(String.format("lineNo=%s, rowNo=%s, bulkData=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), geneDTO));
	//				logger.info("------------------------------- Here is the geneDTO ---------------------");
	//				logger.info(geneDTO.toJson());
	//				logger.debug("current gene: " + geneDTO.getGeneId());
	//
	//				// check if gene currently exists
	//				Long geneIdCount = LsThingLabel.countOfAllLsThingByPreferredName(geneTypeString, geneKindString, "name", "Entrez Gene ID", geneDTO.getGeneId());
	//				//				Long oldGeneIdCount = LsThingLabel.countOfLsThingByName(geneTypeString, geneKindString, "name", "Entrez Gene ID", historyResults.getUpdatedGenes().get(geneDTO.getGeneId()).getDiscontinuedGeneId());
	//
	//				logger.debug("Process Entrez Genes ------------------- geneId count "  + geneIdCount);
	//
	//
	//				// check if discontinued gene
	//				boolean discontinuedGene = false;
	//				if (historyResults.getDiscontinuedGenes().containsKey(geneDTO.getGeneId())) {
	//					logger.debug("discontinued gene");
	//					discontinuedGene = true;
	//				}
	//
	//				// check if updated gene
	//				boolean updatedGene = false;
	//				if (historyResults.getUpdatedGenes().containsKey(geneDTO.getGeneId())) {
	//					logger.debug("updated gene");
	//					updatedGene = true;
	//				}
	//
	//				// temp for debugging only
	//				//updatedGene = true;
	//
	//
	//				// use cases -- flipping through entrez gene list
	//				// 1. gene ID not registered and in list of discontinued genes --> register discontinued gene
	//				// 2. gene ID not registered and in list of updated genes --> update gene entry; 
	//				//       set old gene ID to not preferred, 
	//				//       add new gene ID as the preferred; 
	//				//       ignore old gene metadata, 
	//				//       add new gene metadata
	//				// 3. index through discontinued genes -- register all genes not already registered 
	//
	//				//				for (){
	//				//					
	//				//				}
	//
	//				if (discontinuedGene && geneIdCount == 0L){
	//					//create new entry for discontinued gene
	//					for (EntrezDiscontinuedGeneDTO singleGene : (List<EntrezDiscontinuedGeneDTO>) historyResults.getDiscontinuedGenes().get(geneDTO.getGeneId())){
	//						geneThing = registerDiscontinuedGene(singleGene, lsTransaction);						
	//					}
	//
	//				} else if (updatedGene ){
	//					// updating existing gene info with new gene label and meta (merging genes if new gene does not exist)
	//					// else keep 2 gene entities
	//
	//					for (EntrezDiscontinuedGeneDTO singleGene : (List<EntrezDiscontinuedGeneDTO>) historyResults.getUpdatedGenes().get(geneDTO.getGeneId())){
	//						geneThing = registerUpdatedGene(geneDTO, singleGene, (List<EntrezDiscontinuedGeneDTO>) historyResults.getUpdatedGenes().get(geneDTO.getGeneId()), lsTransaction);					
	//						if (logger.isDebugEnabled()) logger.debug("updated the gene entry: " + LsThing.findLsThing(geneThing.getId()).toPrettyJson());
	//					}
	//
	//				} else if (geneIdCount == 0L ) {
	//					// brand new gene to register
	//					if (logger.isDebugEnabled()) logger.debug("registering new gene: " + geneDTO.getGeneId());
	//					geneThing = registerNewGene(geneDTO, lsTransaction);
	//
	//				} else {
	//					if (logger.isDebugEnabled()) logger.debug("gene already registered: " + geneDTO.getGeneId());
	//				}
	//
	//				if ( i % batchSize == 0 ) { // same as the JDBC batch size
	//					if (geneThing != null){
	//						geneThing.flush();
	//						geneThing.clear();						
	//					}
	//				}
	//				i++;
	//				logger.debug("-----------------------------------------------current entrez gene update count: " + i);
	//
	//			}
	//		}
	//		finally {
	//			if( beanReader != null ) {
	//				beanReader.close();
	//			}
	//			if (br != null)br.close();
	//		}
	//
	//	}


	//	public LsThing registerUpdatedGene(EntrezDbGeneDTO geneDTO, EntrezDiscontinuedGeneDTO singleDiscontinuedGene,  List<EntrezDiscontinuedGeneDTO> discontinuedGeneList, LsTransaction lsTransaction) {
	//		// geneID not registered but the gene is on the update list
	//		// check if registered under different geneID
	//		// if so -- then update
	//		// else  -- register new
	//
	//		// two copies of the gene exist
	//		// one copy is discontinued
	//		// the other copy is now the preferred gene
	//		// mark the old one as discontinued (it will keep the old gene id)
	//		// the other gene will keep the new gene id and just update the gene metadata
	//
	//		// error above
	//		// keep both gene entities as active entities
	//		// old gene thing will keep the old gene id
	//		// new gene thing will not reference the old gene id
	//		// hmmm... what if a user searches for a gene by symbol?
	//		// the user will find the new gene but not find the old gene
	//		// how do we know to link the two genes? 
	//
	//		// use case -- user wants to see all the data for gene id 11797; 11797 maps back to discontinued gene 77616 and 102757
	//		// gene A has a gene ID label for 11797 and 10257
	//		// gene B has a gene ID label for 77616
	//		// AG data for both geneA and geneB.
	//		// option 1: rename the batch code for geneB to geneA. (actual change of data)
	//		// option 2: leave the AG data alone. If user queries for data for 11797 -- we must also return data for 77616
	//		// so gene A should have a label for the discontinued gene id: name_discontinued Entrez Gene ID? or just another Entrez Gene ID not preferred
	//		// in some cases -- there may or may not be another gene thing with a matching preferred Entrez Gene ID 
	//		// let's work with option 2 for now. So genes will also have a label of discontinued gene id -- mark more info in comments?
	//
	//		String geneTypeString = "gene";
	//		String geneKindString = "entrez gene";
	//		//		Long geneIdCount = LsThingLabel.countOfLsThingByName(geneTypeString, geneKindString, "name", "Entrez Gene ID", entrezDiscontinuedGeneDTO.getDiscontinuedGeneId());
	//		//		Long newGeneIdCount = LsThingLabel.countOfLsThingByName(geneTypeString, geneKindString, "name", "Entrez Gene ID", geneDTO.getGeneId());
	//
	//		//		things = LsThing.findLsThingByLabelKindAndLabelText(geneTypeString, geneKindString, labelKind, labelText);
	//
	//		logger.debug("searching for new genes by: " + geneDTO.getGeneId());
	//		List<LsThing> newGenes = LsThing.findAllLsThingByPreferredLabelText(geneTypeString, geneKindString, "name", "Entrez Gene ID",  geneDTO.getGeneId()).getResultList();
	//
	//		logger.debug("searching for discontinued genes by: " + singleDiscontinuedGene.getDiscontinuedGeneId());
	//		List<LsThing> discontinuedGenes = LsThing.findLsThingByLabelText("gene", "entrez gene", "name", "Entrez Gene ID", singleDiscontinuedGene.getDiscontinuedGeneId()).getResultList();
	//
	//
	//		int geneIdCount = discontinuedGenes.size();
	//		int newGeneIdCount = newGenes.size();
	//
	//		logger.debug("------------ the gene ID count is: " + discontinuedGenes.size() + "  for discontinued gene " + singleDiscontinuedGene.getDiscontinuedGeneId());
	//		logger.debug("------------ the gene ID count is: " + newGenes.size() + "  for new gene " + geneDTO.getGeneId());
	//
	//		//are the genes the same?
	//		boolean sameGene = false;
	//		if (discontinuedGenes.size() == 1 && newGenes.size() == 1){
	//			if (discontinuedGenes.get(0).getId().longValue() == newGenes.get(0).getId().longValue()){
	//				logger.debug("@@@@@@  Mapping back to the same gene   @@@@@@     " + discontinuedGenes.get(0).getCodeName());
	//				sameGene = true;
	//			}
	//		}
	//
	//		LsThing geneThing = null ;
	//		if (geneIdCount == 0 && newGeneIdCount == 0){
	//			logger.debug("gene is not registered yet");
	//			//			geneThing = registerNewGene(geneDTO, singleDiscontinuedGene, lsTransaction);
	//			geneThing = registerNewGene(geneDTO, lsTransaction);
	//
	//		} else if (geneIdCount == 1 && newGeneIdCount == 1 && sameGene){
	//			logger.debug("------ both gene IDs are currently registered with the same geneThing");
	//			// add discontinued meta to the old gene
	//
	//
	//			//TODO add the discontinued meta data to teh old gene (not the active gene)
	//			//LsThing oldGene = discontinueGene(singleDiscontinuedGene, lsTransaction);
	//			//if (logger.isDebugEnabled()) logger.debug("added discontinued gene meta to the old gene: " + oldGene.toPrettyJson());
	//
	//			// update the gene with the new info
	//			List<LsThing> genes = LsThing.findLsThingByPreferredLabelText(geneTypeString, geneKindString, "name", "Entrez Gene ID", geneDTO.getGeneId()).getResultList();
	//			if (genes.size() == 0){
	//				logger.error("Gene not found");
	//			} else if (genes.size() > 1){
	//				logger.error("multiple genes found with the same gene ID");
	//
	//			} else {
	//				geneThing = genes.get(0);
	//				//				geneThing = updateGeneID(geneThing, geneDTO, entrezDiscontinuedGeneDTO, lsTransaction);
	//
	//				//check the modification date
	//				boolean isNewDate = false;
	//				List<LsThingValue> dateValues = LsThingValue.findLsThingValuesByLsThingIDAndStateTypeKindAndValueTypeKind(geneThing.getId(), "metadata", "gene metadata", "dateValue", "modification date").getResultList();
	//				logger.debug("number of mod dates found: " + dateValues.size());
	//				if (dateValues.size() > 1) logger.error("Found more than 1 mod date: " + dateValues.size());
	//				for (LsThingValue dateValue : dateValues){
	//					if (geneDTO.getModificationDate().after(dateValue.getDateValue())){
	//						isNewDate = true;
	//						logger.debug("It is a recent update");
	//					} else {
	//						logger.debug("It is a not a recent update");
	//					}
	//				}
	//
	//				// only update the gene if it is a new update (in Entrez Gene file = geneDTO)
	//				if (isNewDate){
	//					logger.debug("Updating the gene metadata");
	//					geneThing = updateGeneMetaData(geneThing, geneDTO, lsTransaction);
	//
	//				} else {
	//					logger.debug("@@@@@@ NOT A NEW DATE @@@@@@@@");
	//				}
	//
	//				//always check entries in the history file
	//				for (EntrezDiscontinuedGeneDTO discontinuedGene : discontinuedGeneList){
	//					logger.debug("adding old gene ID " + discontinuedGene.getDiscontinuedGeneId() + " to the gene: " + geneThing.getCodeName());
	//					geneThing = addOldGeneID(geneThing, discontinuedGene, lsTransaction);
	//
	//					//add reference gene to the old Gene as well
	//					logger.debug("adding reference gene ID " + geneDTO.getGeneId() + "   to the following old gene: " + discontinuedGene.getDiscontinuedGeneId());
	//					addReferenceGeneIdToDiscontinuedGene(geneDTO, discontinuedGene, lsTransaction);
	//				}
	//
	//
	//			}
	//
	//		} else if (geneIdCount == 0 && newGeneIdCount == 1){
	//			logger.debug("------ just the new gene is currently registered");
	//			// add the old entrez gene id to the registered gene
	//			// add discontinued meta to the old gene
	//
	//			// update the new gene with the new info
	//			List<LsThing> genes = LsThing.findLsThingByLabelText(geneTypeString, geneKindString, "name", "Entrez Gene ID", geneDTO.getGeneId()).getResultList();
	//			if (genes.size() == 0){
	//				logger.error("Gene not found");
	//			} else if (genes.size() > 1){
	//				logger.error("multiple genes found with the same gene ID");
	//
	//			} else {
	//				geneThing = genes.get(0);
	//				geneThing = addOldGeneID(geneThing, geneDTO, singleDiscontinuedGene, lsTransaction);
	//			}			
	//		} else {
	//			logger.debug("-------- gene is registered via old id   geneIdCount: " + geneIdCount + "  newGeneIdCount: " + newGeneIdCount);
	//
	//			//			if (logger.isDebugEnabled()) logger.debug("old gene: " + LsThing.findLsThingByLabelText(geneTypeString, geneKindString, "name", "Entrez Gene ID", entrezDiscontinuedGeneDTO.getDiscontinuedGeneId()).getResultList().get(0).toPrettyJson());
	//
	//
	//			List<LsThing> genes = LsThing.findLsThingByPreferredLabelText(geneTypeString, geneKindString, "name", "Entrez Gene ID", geneDTO.getGeneId()).getResultList();
	//			if (genes.size() == 0){
	//				logger.error("Gene not found");
	//			} else if (genes.size() > 1){
	//				logger.error("multiple genes found with the same gene ID");
	//				List<Long> thingIds = new ArrayList();
	//				for (LsThing lsThing : genes){
	//					thingIds.add(lsThing.getId());
	//					logger.error("Found this gene ID: " + lsThing.toJson());
	//
	//				}
	//				Collections.sort(thingIds);
	//				boolean first = true;
	//				for (Long geneId : thingIds){
	//					if (first){
	//						first = false;
	//						geneThing = LsThing.findLsThing(geneId);
	//					} else {
	//						logger.debug("ignored this gene thing ID " + geneId);
	//						LsThing queryGene = LsThing.findLsThing(geneId);
	//						queryGene.setIgnored(true);
	//						queryGene.merge();
	//					}
	//				}
	//
	//			} else {
	//				geneThing = genes.get(0);
	//				logger.debug("working with gene thing: " + geneThing.getCodeName() + "   linked to Gene ID: " + geneDTO.getGeneId());
	//			}	
	//
	//
	//			//check the modification date
	//			boolean isNewDate = false;
	//			logger.debug("working with gene thing: " + geneThing.getCodeName() + "   linked to Gene ID: " + geneDTO.getGeneId());
	//
	//			List<LsThingValue> dateValues = LsThingValue.findLsThingValuesByLsThingIDAndStateTypeKindAndValueTypeKind(geneThing.getId(), "metadata", "gene metadata", "dateValue", "modification date").getResultList();
	//			logger.debug("number of mod dates found: " + dateValues.size());
	//			if (dateValues.size() > 1) logger.error("Found more than 1 mod date: " + dateValues.size());
	//			for (LsThingValue dateValue : dateValues){
	//				if (geneDTO.getModificationDate().after(dateValue.getDateValue())){
	//					isNewDate = true;
	//					logger.debug("It is a recent update");
	//				} else {
	//					logger.debug("It is a not a recent update");
	//				}
	//			}
	//
	//
	//			if (isNewDate){
	//
	//				logger.debug("Updating the gene metadata");
	//				geneThing = updateGeneMetaData(geneThing, geneDTO, lsTransaction);
	//				// next add old gene IDs to the gene
	//				//geneThing = addOldGeneID(geneThing, geneDTO, singleDiscontinuedGene, lsTransaction);
	//
	//				//				geneThing = updateGene(geneDTO, entrezDiscontinuedGeneDTO, lsTransaction);
	//				//				saveGeneLabel(lsTransaction, geneThing, "name", "Entrez Gene ID", true, geneDTO.getGeneId(), entrezSeparator)
	//			}	
	//
	//			//for loop
	//			for (EntrezDiscontinuedGeneDTO discontinuedGene : discontinuedGeneList){
	//				logger.debug("adding old gene ID " + discontinuedGene.getDiscontinuedGeneId() + " to the gene: " + geneThing.getCodeName());
	//				geneThing = addOldGeneID(geneThing, discontinuedGene, lsTransaction);
	//
	//				//add reference gene to the old Gene as well
	//				logger.debug("adding reference gene ID " + geneDTO.getGeneId() + "   to the following old gene: " + discontinuedGene.getDiscontinuedGeneId());
	//				addReferenceGeneIdToDiscontinuedGene(geneDTO, discontinuedGene, lsTransaction);
	//			}
	//
	//
	//		}
	//
	//
	//
	//		return geneThing;
	//	}
	//




	//	private void addReferenceGeneIdToDiscontinuedGene(EntrezDbGeneDTO geneDTO, EntrezDiscontinuedGeneDTO discontinuedGene, LsTransaction lsTransaction) {
	//		List<LsThing> inactiveGenes = LsThing.findAllLsThingByPreferredLabelText("gene", "entrez gene", "name", "Entrez Gene ID", discontinuedGene.getDiscontinuedGeneId()).getResultList();
	//		logger.debug("@@@@@@@@@ number of inactive genes found: " + inactiveGenes.size() + "  for gene " + discontinuedGene.getDiscontinuedGeneId());
	//		for (LsThing inactiveGene : inactiveGenes){
	//
	//			boolean activeGene = false;
	//			List<LsThingLabel> lsThingLabels = LsThingLabel.findLsThingPreferredName(inactiveGene.getId(), "name", "Entrez Gene ID").getResultList();
	//			for (LsThingLabel lsThingLabel : lsThingLabels){
	//				if (lsThingLabel.getLabelText().equalsIgnoreCase(geneDTO.getGeneId())){
	//					activeGene = true;
	//				}
	//			}
	//
	//			if (!activeGene){
	//				logger.debug("current inactive gene: " + inactiveGene.getCodeName());
	//				if (!inactiveGene.isIgnored()){
	//					discontinueGene(inactiveGene, discontinuedGene, lsTransaction);
	//				}
	//				List<LsThingLabel> geneIdLabels = LsThingLabel.findLsThingLabelsByLsThingAndLsTypeAndLsKindAndLabelText(inactiveGene, "name", "Entrez Gene ID", geneDTO.getGeneId()).getResultList();
	//				if (geneIdLabels.size() == 0){
	//					saveGeneLabel(lsTransaction, inactiveGene, "name", "Entrez Gene ID", false, geneDTO.getGeneId());
	//					//					inactiveGene.getLsLabels().add(saveGeneLabel(lsTransaction, inactiveGene, "name", "Entrez Gene ID", false, geneDTO.getGeneId()));
	//				}
	//			}
	//		}
	//	}

	//	private LsThing addOldGeneID(LsThing geneThing, EntrezDiscontinuedGeneDTO discontinuedGene, LsTransaction lsTransaction) {
	//		List<LsThingLabel> geneIdLabels = LsThingLabel.findLsThingLabelsByLsThingAndLsTypeAndLsKindAndLabelText(geneThing, "name", "Entrez Gene ID", discontinuedGene.getDiscontinuedGeneId()).getResultList();
	//		if (geneIdLabels.size() == 0){
	//			geneThing.getLsLabels().add(saveGeneLabel(lsTransaction, geneThing, "name", "Entrez Gene ID", false, discontinuedGene.getDiscontinuedGeneId()));
	//		} 
	//
	//		//		else if (geneIdLabels.size() == 1){
	//		//			LsThingLabel lsThingLabel = geneIdLabels.get(0);
	//		//			if (!lsThingLabel.isPreferred()){
	//		//				lsThingLabel.setPreferred(false);
	//		//				lsThingLabel.setModifiedDate(new Date());
	//		//				lsThingLabel.merge();				
	//		//			}
	//		//		} else {
	//		//			logger.error("Found multiple gene ID labels");
	//		//		}
	//
	//		return geneThing;
	//	}

	//	public LsThing addOldGeneID(LsThing geneThing, EntrezDbGeneDTO geneDTO, EntrezDiscontinuedGeneDTO entrezDiscontinuedGeneDTO, LsTransaction lsTransaction) {
	//		if (entrezDiscontinuedGeneDTO.getDiscontinuedGeneId() != null)  geneThing.getLsLabels().add(saveGeneLabel(lsTransaction, geneThing, "name", "Entrez Gene ID", false, entrezDiscontinuedGeneDTO.getDiscontinuedGeneId()));
	//		if (entrezDiscontinuedGeneDTO.getDiscontinuedSymbol() != null)  geneThing.getLsLabels().add(saveGeneLabel(lsTransaction, geneThing, "name", "gene symbol", false, entrezDiscontinuedGeneDTO.getDiscontinuedSymbol()));
	//		return geneThing;
	//	}
	//


	//
	//
	//	public LsThing ignoreNonGeneIDLabels(LsTransaction lsTransaction, LsThing geneThing) {
	//		Set<LsThingLabel> lsLabels = new HashSet<LsThingLabel>();
	//		for (LsThingLabel geneLabel : geneThing.getLsLabels()){
	//			if (geneLabel.getLsType().equalsIgnoreCase("name") && geneLabel.getLsKind().equalsIgnoreCase("Entrez Gene ID")){
	//				// do nothing to the Entrez Gene IDs
	//			} else {
	//				// ignore all of the other existing labels
	//				geneLabel.setIgnored(true);
	//				//lsLabels.add(geneLabel);
	//			}
	//		}
	//
	//		//geneThing.setLsLabels(lsLabels);
	//		return geneThing;
	//	}
	//
	//	public LsThing updateGeneID(LsThing geneThing, EntrezDbGeneDTO geneDTO, EntrezDiscontinuedGeneDTO entrezDiscontinuedGeneDTO, LsTransaction lsTransaction) {
	//
	//		logger.debug("attempting to update the gene GeneID");
	//		logger.debug(geneThing.toJson());
	//
	//		// mark old geneId as not preferred
	//		// create new label
	//		for (LsThingLabel geneLabel : geneThing.getLsLabels()){
	//			if (geneLabel.getLsType().equalsIgnoreCase("name") && geneLabel.getLsKind().equalsIgnoreCase("Entrez Gene ID") 
	//					&& geneLabel.getLabelText().equalsIgnoreCase(entrezDiscontinuedGeneDTO.getDiscontinuedGeneId()) && geneLabel.isPreferred()){
	//				geneLabel.setPreferred(false);
	//			}
	//		}
	//
	//		LsThingLabel newGeneLabel = saveGeneLabel(lsTransaction, geneThing, "name", "Entrez Gene ID", true, entrezDiscontinuedGeneDTO.getGeneId());
	//		geneThing.getLsLabels().add(newGeneLabel);
	//
	//		// save discontinued metadata
	//		LsThingState geneState = new LsThingState();
	//		geneState.setLsThing(geneThing);
	//		geneState.setLsType("metadata");
	//		geneState.setLsKind("gene discontinued metadata");
	//		geneState.setLsTransaction(lsTransaction.getId());
	//		geneState.setRecordedBy("acas admin");
	//		geneState.persist();
	//
	//		String entrezSeparator = "\\|";
	//		geneState.getLsValues().addAll(saveGeneDescriptor(lsTransaction, geneState, "stringValue", "discontinued gene id", entrezDiscontinuedGeneDTO.getDiscontinuedGeneId(), entrezSeparator));
	//		geneState.getLsValues().addAll(saveGeneDescriptor(lsTransaction, geneState, "stringValue", "discontinued gene symbol", entrezDiscontinuedGeneDTO.getDiscontinuedSymbol(), entrezSeparator));
	//		geneState.getLsValues().add(saveGeneDescriptor(lsTransaction, geneState, "dateValue", "discontinued date", entrezDiscontinuedGeneDTO.getDiscontinuedDate()));
	//		geneThing.getLsStates().add(geneState);
	//
	//		return geneThing;
	//
	//
	//	}
	//
	//	public LsThing registerDiscontinuedGene(EntrezDiscontinuedGeneDTO geneDTO, LsTransaction lsTransaction) {
	//		logger.debug("create discontinued gene: " + geneDTO.getDiscontinuedGeneId());
	//		String geneTypeString = "gene";
	//		String geneKindString = "entrez gene";
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
	//
	//
	//		LsThing geneThing = new LsThing();
	//		geneThing.setCodeName(getGeneCodeName());
	//		geneThing.setLsType(geneTypeString);
	//		geneThing.setLsKind(geneKindString);
	//		geneThing.setLsTransaction(lsTransaction.getId());
	//		geneThing.setRecordedBy("acas admin");
	//		geneThing.persist();
	//
	//		String entrezSeparator = "\\|";
	//		saveGeneLabel(lsTransaction, geneThing, "name", "Entrez Gene ID", true, geneDTO.getDiscontinuedGeneId(), entrezSeparator);
	//		saveGeneLabel(lsTransaction, geneThing, "name", "gene symbol", false, geneDTO.getDiscontinuedSymbol(), entrezSeparator);
	//
	//		LsThingState geneState = new LsThingState();
	//		geneState.setLsThing(geneThing);
	//		geneState.setLsType("metadata");
	//		geneState.setLsKind("gene metadata");
	//		geneState.setLsTransaction(lsTransaction.getId());
	//		geneState.setRecordedBy("acas admin");
	//		geneState.persist();
	//
	//		saveGeneDescriptor(lsTransaction, geneState, "stringValue", "tax id", geneDTO.getTaxId(), entrezSeparator);
	//		saveGeneDescriptor(lsTransaction, geneState, "stringValue", "discontinued gene", "true", entrezSeparator);
	//		saveGeneDescriptor(lsTransaction, geneState, "stringValue", "discontinued gene id", geneDTO.getDiscontinuedGeneId(), entrezSeparator);
	//		saveGeneDescriptor(lsTransaction, geneState, "stringValue", "discontinued gene symbol", geneDTO.getDiscontinuedSymbol(), entrezSeparator);
	//		saveGeneDescriptor(lsTransaction, geneState, "dateValue", "discontinued date", geneDTO.getDiscontinuedDate());
	//
	//		return geneThing;
	//	}
	//
	//
	//	@SuppressWarnings("unchecked")
	//	public void processDiscontinuedGenes(MultiValueMap discontinuedGenes,
	//			String taxonomyId, LsTransaction lsTransaction) {
	//
	//		logger.info("########### PROCESSING DISCONTINUED GENES ##############");
	//		logger.info("number of discontinued genes:  " + discontinuedGenes.size());
	//
	//		//		EntrezDiscontinuedGeneDTO discontinuedGene;
	//		String geneTypeString = "gene";
	//		String geneKindString = "entrez gene";
	//		LsThing geneThing = null;
	//		int geneCount = 0;
	//		int batchSize = 25;
	//		Set<String> keys = discontinuedGenes.keySet();
	//		for (String key : keys){
	//
	//			logger.debug("the key is: " + key);
	//			List<EntrezDiscontinuedGeneDTO> discontinuedGeneSet = (List<EntrezDiscontinuedGeneDTO>) discontinuedGenes.get(key);
	//			for (EntrezDiscontinuedGeneDTO discontinuedGene : discontinuedGeneSet) {
	//				List<LsThing> inactiveGenes = LsThing.findAllLsThingByPreferredLabelText("gene", "entrez gene", "name", "Entrez Gene ID", discontinuedGene.getDiscontinuedGeneId()).getResultList();
	//				logger.debug("@@@@@@@@@ number of inactive genes found: " + inactiveGenes.size() + "  for gene " + discontinuedGene.getDiscontinuedGeneId());
	//				if (inactiveGenes.size() == 0){
	//					geneThing = createDiscontinuedGene(discontinuedGene, lsTransaction);
	//
	//				} else if (inactiveGenes.size() == 1){
	//					LsThing inactiveGene = inactiveGenes.get(0);
	//					logger.debug("current inactive gene: " + inactiveGene.getCodeName());
	//					//					geneThing = discontinueGene(discontinuedGene, lsTransaction);
	//					geneThing = discontinueGene(inactiveGene, discontinuedGene, lsTransaction);
	//
	//				} else {
	//					throw new RuntimeException("Found multiple genes -- expected 0 or 1");
	//				}
	//
	//				geneCount++;
	//				if ( geneCount % batchSize == 0 ) { // same as the JDBC batch size
	//					logger.info("-----------------------------------------------current discontinued gene update count: " + geneCount);
	//					if (geneThing != null){
	//						geneThing.flush();
	//						geneThing.clear();						
	//					}
	//				}
	//			}
	//
	//
	//		}
	//
	//	}
	//
	//	public LsThing createDiscontinuedGene(EntrezDiscontinuedGeneDTO geneDTO, LsTransaction lsTransaction) {
	//		logger.debug("create discontinued gene: " + geneDTO.getDiscontinuedGeneId());
	//		String geneTypeString = "gene";
	//		String geneKindString = "entrez gene";
	//		//		ThingType geneType = ThingType.getOrCreate(geneTypeString);
	//		//		ThingKind.getOrCreate(geneType, geneKindString);
	//		//
	//		//		LabelType labelType = LabelType.getOrCreate("name");
	//		//		LabelKind.getOrCreate(labelType, "gene code name");
	//		//		LabelKind.getOrCreate(labelType, "Entrez Gene ID");
	//		//		LabelKind.getOrCreate(labelType, "gene symbol");
	//		//
	//		//		StateType stateType = StateType.getOrCreate("metadata");
	//		//		StateKind.getOrCreate(stateType, "gene metadata");
	//		//
	//		//		ValueType valueType = ValueType.getOrCreate("stringValue");
	//		//		ValueKind.getOrCreate(valueType, "discontinued gene id");
	//		//		ValueKind.getOrCreate(valueType, "discontinued gene symbol");
	//		//		ValueKind.getOrCreate(valueType, "discontinued date");
	//		//		ValueKind.getOrCreate(valueType, "discontinued gene");
	//		//		ValueType valueTypeDate = ValueType.getOrCreate("dateValue");
	//		//		ValueKind.getOrCreate(valueTypeDate, "modification date");
	//
	//
	//		LsThing geneThing = new LsThing();
	//		geneThing.setCodeName(getGeneCodeName());
	//		geneThing.setLsType(geneTypeString);
	//		geneThing.setLsKind(geneKindString);
	//		geneThing.setLsTransaction(lsTransaction.getId());
	//		geneThing.setRecordedBy("acas admin");
	//		geneThing.persist();
	//
	//		String entrezSeparator = "\\|";
	//		saveGeneLabel(lsTransaction, geneThing, "name", "Entrez Gene ID", true, geneDTO.getDiscontinuedGeneId(), entrezSeparator);
	//		saveGeneLabel(lsTransaction, geneThing, "name", "gene symbol", false, geneDTO.getDiscontinuedSymbol(), entrezSeparator);
	//
	//		LsThingState geneState = new LsThingState();
	//		geneState.setLsThing(geneThing);
	//		geneState.setLsType("metadata");
	//		geneState.setLsKind("gene metadata");
	//		geneState.setLsTransaction(lsTransaction.getId());
	//		geneState.setRecordedBy("acas admin");
	//		geneState.persist();
	//
	//		saveGeneDescriptor(lsTransaction, geneState, "stringValue", "tax id", geneDTO.getTaxId(), entrezSeparator);
	//		saveGeneDescriptor(lsTransaction, geneState, "stringValue", "discontinued gene", "true", entrezSeparator);
	//		saveGeneDescriptor(lsTransaction, geneState, "stringValue", "discontinued gene id", geneDTO.getDiscontinuedGeneId(), entrezSeparator);
	//		saveGeneDescriptor(lsTransaction, geneState, "stringValue", "discontinued gene symbol", geneDTO.getDiscontinuedSymbol(), entrezSeparator);
	//		saveGeneDescriptor(lsTransaction, geneState, "dateValue", "discontinued date", geneDTO.getDiscontinuedDate());
	//
	//		return geneThing;
	//
	//	}


	//	public class GeneHistoryDTO {
	//		private MultiValueMap discontinuedGenes;
	//		private MultiValueMap updatedGenes;
	//		public MultiValueMap getDiscontinuedGenes() {
	//			return discontinuedGenes;
	//		}
	//		public void setDiscontinuedGenes(MultiValueMap discontinuedGenes) {
	//			this.discontinuedGenes = discontinuedGenes;
	//		}
	//		public MultiValueMap getUpdatedGenes() {
	//			return updatedGenes;
	//		}
	//		public void setUpdatedGenes(MultiValueMap updatedGenes) {
	//			this.updatedGenes = updatedGenes;
	//		}
	//	}

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

	//	@SuppressWarnings("unchecked")
	//	@Override
	//	public void fixDiscontinuedEntrezGeneIDs(String geneHistoryFile, String taxonomyId) throws IOException {
	//
	//		//get historyResults
	//		//look up discontinued genes
	//		// if multiple gene things are labeled with the same Gene ID
	//		// mark the gene with the lowest transaction ID as the original owner
	//		// set the old gene as preferred and non-ignored
	//		// mark the other labels as ignored (we will only refer to this gene entity by the old Gene ID
	//		// keep the discontinued gene id that was assigned to the new gene. (allows us to tie the genes together -- need to make changes in the preferred ID service)
	//
	//		// may want to remove all old labels and start fresh
	//
	//		LsTransaction lsTransaction = new LsTransaction();
	//		lsTransaction.setRecordedDate(new Date());
	//		lsTransaction.setComments("fixing gene ids");
	//		lsTransaction.persist();
	//
	//		GeneHistoryDTO historyResults = processGeneHistory(geneHistoryFile, taxonomyId);
	//		Set<String> updatedGeneKeys = historyResults.getUpdatedGenes().keySet();
	//
	//		Collection<String> allGeneIds = new HashSet<String>();
	//		Collection<String> discontinuedGeneIds = new HashSet<String>();
	//		Collection<String> activeGeneIds = new HashSet<String>();
	//
	//		for (String geneId : updatedGeneKeys){
	//			List<EntrezDiscontinuedGeneDTO> updatedGeneList = (List<EntrezDiscontinuedGeneDTO>) historyResults.getUpdatedGenes().get(geneId);
	//			for (EntrezDiscontinuedGeneDTO singleGene : updatedGeneList){
	//				discontinuedGeneIds.add(singleGene.getDiscontinuedGeneId());
	//				allGeneIds.add(singleGene.getDiscontinuedGeneId());
	//				allGeneIds.add(singleGene.getGeneId());
	//				activeGeneIds.add(singleGene.getGeneId());
	//			}
	//		}
	//
	//
	//		for (String geneId : allGeneIds){
	//			logger.info("gene ID: " + geneId);
	//
	//			List<LsThing> genes = LsThing.findLsThingByAllLabelText("gene", "entrez gene", "name", "Entrez Gene ID", geneId).getResultList();
	//			Map<Long, LsThing> genesMap = new HashMap<Long, LsThing>();
	//			logger.info("number of gene things found: " + genes.size());
	//
	//			if (genes.size() == 1) {
	//				setPreferredName(genes.get(0).getId(), geneId, lsTransaction);
	//				deleteDuplicateLabels(genes.get(0).getId());
	//			}
	//
	//			if (genes.size() > 2) throw new RuntimeException("found multiple gene things >2: " + genes.size());
	//
	//			if (genes.size() > 1){
	//				for (LsThing gene : genes){
	//					genesMap.put(gene.getId(), gene);
	//				}
	//				//					List<LsThingLabel> preferredGeneNames = LsThingLabel.findLsThingAllPreferredName(gene.getId(), "name", "Entrez Gene ID").getResultList();
	//				//					LsThingLabel preferredGeneName;
	//				//					if (preferredGeneNames.size() == 1){
	//				//						preferredGeneName = preferredGeneNames.get(0);
	//				//						if (preferredGeneName.isIgnored()){
	//				//							preferredGeneName.setIgnored(false);
	//				//							preferredGeneName.merge();
	//				//						}
	//				//						if (discontinuedGeneIds.contains(preferredGeneName.getLabelText())){
	//				//							if (!gene.isIgnored()){
	//				//								logger.info(" @@@@@@@@@@@@@@@@@@@@@ ----- ignoring the gene: " + gene.getId());
	//				//								gene.setIgnored(true);
	//				//								gene.merge();							
	//				//							}
	//				//						}
	//				//					} else if (preferredGeneNames.size() > 1) {
	//				//						logger.error("found many preferredGeneNames: " + preferredGeneNames.size());
	//				//					} else {
	//				//						logger.info("Preferred gene name not found: " + gene.getId());
	//				//					}
	//				//				}
	//
	//				List<LsThingLabel> labels = new ArrayList<LsThingLabel>();
	//				long lowestTransactionId = 0;
	//				long lowestGeneThingId = 0;
	//				long highestGeneThingId = 0;
	//
	//				LsThingLabel targetLabel = null;
	//				for (LsThing queryGene : genes){
	//					logger.debug("query Gene is: " + queryGene.getCodeName());
	//					labels.addAll(LsThingLabel.findLsThingLabelsByLsThing(queryGene).getResultList());
	//				}
	//				boolean firstPass = true;
	//				for (LsThingLabel label : labels){
	//					if (label.getLsType().equalsIgnoreCase("name") 
	//							&& label.getLsKind().equalsIgnoreCase("Entrez Gene ID")){
	//						//							&& label.getLabelText().equalsIgnoreCase(geneId)) {
	//
	//						logger.debug("&&&&&&&&&&&&&  entrez gene label: "  + label.toJson());
	//
	//						if (firstPass){
	//							lowestTransactionId = label.getLsTransaction();
	//							lowestGeneThingId = label.getLsThing().getId();
	//							targetLabel = label;
	//							firstPass = false;
	//						} else {
	//							if (label.getLsTransaction() < lowestTransactionId){
	//								lowestTransactionId = label.getLsTransaction();
	//								if (label.getLsThing().getId() < lowestGeneThingId){
	//									lowestGeneThingId = label.getLsThing().getId();
	//									targetLabel = label;									
	//								}
	//							}
	//						}
	//					}
	//				}
	//				logger.info("the best label is: " + targetLabel.toJson());
	//				logger.info("the lowestTransactionId: " + lowestTransactionId);
	//				logger.info("the lowestGeneThingId: " + lowestGeneThingId);
	//				genesMap.remove(lowestGeneThingId);
	//
	//				setPreferredName(lowestGeneThingId, geneId, lsTransaction);
	//
	//				//				Set<Long> geneIdKeys = genesMap.keySet();
	//				//				for (Long geneIdKey : geneIdKeys){
	//				//					logger.debug("the geneIdKey is : " + geneIdKey);
	//				//					highestGeneThingId = geneIdKey.longValue();
	//				//				}
	//
	//
	//				for (Long otherGeneThingId : genesMap.keySet()){
	//					logger.info("the other gene thing id is: " + otherGeneThingId);
	//					highestGeneThingId = otherGeneThingId;					
	//				}
	//
	//				curateOldGene(geneId, targetLabel, lowestGeneThingId, highestGeneThingId, activeGeneIds, lsTransaction);
	//
	//				if (discontinuedGeneIds.contains(geneId)){
	//					logger.info("---------- removing discontinued gene labels for gene id: " + geneId);
	//					removeOtherLabelsAndSetPreferredGeneId(targetLabel);
	//					if (logger.isDebugEnabled()){
	//						displayGeneLabels(targetLabel.getLsThing().getId());
	//					}
	//				} else {
	//					deleteDuplicateLabels(lowestGeneThingId);
	//					deleteDuplicateLabels(highestGeneThingId);
	//					logger.info("----------- removing old gene labels for gene id: " + geneId);
	//					removeOldGeneId(lowestGeneThingId, geneId);
	//					if (logger.isDebugEnabled()){
	//						logger.debug("----------- displaying lowest gene thing id: " + lowestGeneThingId + "    -----------------------");
	//						displayGeneLabels(lowestGeneThingId);
	//						logger.debug("----------- displaying highest gene thing id: " + highestGeneThingId + "    -----------------------");
	//						displayGeneLabels(highestGeneThingId);
	//					}
	//				}
	//
	//				for (LsThing gene : genes){
	//					List<LsThingLabel>preferredGeneNames = LsThingLabel.findLsThingPreferredName(gene.getId(), "name", "Entrez Gene ID").getResultList();
	//					if (preferredGeneNames.size() == 1){
	//						LsThingLabel preferredGeneName = preferredGeneNames.get(0);
	//						if (discontinuedGeneIds.contains(preferredGeneName.getLabelText())){
	//							if (!gene.isIgnored()){
	//								logger.info(" @@@@@@@@@@@@@@@@@@@@@ ----- ignoring the gene: " + gene.getId());
	//								gene.setIgnored(true);
	//								gene.merge();							
	//							}
	//						} else if (activeGeneIds.contains(preferredGeneName.getLabelText())){
	//							if (gene.isIgnored()){
	//								logger.info(" @@@@@@@@@@@@@@@@@@@@@ ----- UN-ignoring the gene: " + gene.getId());
	//								gene.setIgnored(false);
	//								gene.merge();	
	//							}
	//						}
	//					} else if (preferredGeneNames.size() > 1){
	//						logger.error("!!!!!!!! ERROR: Found multiple preferred names");
	//						for (LsThingLabel preferred : preferredGeneNames){
	//							logger.error(preferred.toJson());
	//						}
	//					}
	//
	//				}				
	//			}
	//		}
	//	}

	//	@SuppressWarnings("unchecked")
	//	private void curateOldGene(String geneId, LsThingLabel targetLabel, long lowestGeneThingId, long highestGeneThingId, Collection<String> activeGeneIds, LsTransaction lsTransaction) {
	//
	//		LsThing newGene = LsThing.findLsThing(lowestGeneThingId);
	//		String newPreferredId = targetLabel.getLabelText();
	//		List<LsThingLabel> newGeneLabels = LsThingLabel.findLsThingLabelsByLsThingAndLsTypeAndLsKind(newGene, "name", "Entrez Gene ID").getResultList();
	//		Collection<String> newGeneEntrezIDs = new HashSet<String>();
	//		for (LsThingLabel newGeneLabel : newGeneLabels){
	//			newGeneEntrezIDs.add(newGeneLabel.getLabelText());
	//		}
	//
	//		LsThing oldGene = LsThing.findLsThing(highestGeneThingId);
	//		deleteDuplicateLabels(oldGene.getId());
	//
	//		List<LsThingLabel> oldGeneLabels = LsThingLabel.findLsThingLabelsByLsThingAndLsTypeAndLsKind(oldGene, "name", "Entrez Gene ID").getResultList();
	//		String oldPreferredId = oldGeneLabels.get(0).getLabelText();
	//		logger.info("The original id is: " + oldPreferredId);
	//
	//		//ignore old gene thing
	//		if (!activeGeneIds.contains(oldPreferredId)){
	//			logger.info("@@@@@@@@@@@@  IGNORED GENE THING @@@@@@@@@@   " + highestGeneThingId);
	//			oldGene.setIgnored(true);
	//			oldGene.merge();
	//		}
	//
	//
	//
	//		//transfer the gene IDs to the other gene
	//
	//		for (LsThingLabel oldGeneLabel : oldGeneLabels){
	//			logger.debug(oldGeneLabel.toJson());
	//			if (oldGeneLabel.getLabelText().equalsIgnoreCase(oldPreferredId)){
	//				logger.debug("old Gene preferred ID is: " + oldGeneLabel.getLabelText());
	//				if (!newGeneEntrezIDs.contains(oldGeneLabel.getLabelText())){
	//					logger.debug("----------- Adding gene label to new Gene: " + oldGeneLabel.getLabelText());
	//					LsThingLabel newLabel = new LsThingLabel();
	//					newLabel.setLsType("name");
	//					newLabel.setLsKind("Entrez Gene ID");
	//					newLabel.setLabelText(oldGeneLabel.getLabelText());
	//					newLabel.setLsThing(newGene);
	//					newLabel.setPreferred(false);
	//					newLabel.setImageFile("Discontinued Entrez Gene ID");
	//					newLabel.setRecordedBy("acas admin");
	//					newLabel.setRecordedDate(new Date());
	//					newLabel.setLsTransaction(lsTransaction.getId());
	//					newLabel.persist();
	//				}
	//			} else if (oldGeneLabel.getLabelText().equalsIgnoreCase(newPreferredId)){
	//				if (oldGeneLabel.isPreferred()){
	//					oldGeneLabel.setPreferred(false);
	//					oldGeneLabel.setIgnored(false);
	//					oldGeneLabel.merge();
	//				}
	//			} else {
	//				if (!newGeneEntrezIDs.contains(oldGeneLabel.getLabelText())){
	//					logger.debug("----------- Adding gene label to new Gene: " + oldGeneLabel.getLabelText());
	//					LsThingLabel newLabel = new LsThingLabel();
	//					newLabel.setLsType("name");
	//					newLabel.setLsKind("Entrez Gene ID");
	//					newLabel.setLabelText(oldGeneLabel.getLabelText());
	//					newLabel.setLsThing(newGene);
	//					newLabel.setPreferred(false);
	//					newLabel.setImageFile("Discontinued Entrez Gene ID");
	//					newLabel.setRecordedBy("acas admin");
	//					newLabel.setRecordedDate(new Date());
	//					newLabel.setLsTransaction(lsTransaction.getId());
	//					newLabel.persist();
	//
	//					oldGeneLabel.setIgnored(true);
	//					oldGeneLabel.setDeleted(true);
	//					oldGeneLabel.setPreferred(false);
	//					oldGeneLabel.setImageFile("To be deleted - Entrez Gene ID");
	//					oldGeneLabel.merge();
	//				}
	//			}
	//
	//		}
	//
	//	}


	//	private void setPreferredName(long lowestGeneThingId, String geneId, LsTransaction lsTransaction) {
	//		logger.debug("Setting the preferred entrez gene ID for gene thing id: " + lowestGeneThingId);
	//		@SuppressWarnings("unchecked")
	//		List<LsThingLabel> thingLabels = LsThingLabel.findLsThingLabelsByLsThingAndLsTypeAndLsKindAndLabelText(LsThing.findLsThing(lowestGeneThingId), "name", "Entrez Gene ID", geneId).getResultList();
	//		LsThingLabel geneThingLabel = null;
	//		if (thingLabels.size() == 0){
	//			geneThingLabel = new LsThingLabel();
	//			geneThingLabel.setLsType("name");
	//			geneThingLabel.setLsKind("Entrez Gene ID");
	//			geneThingLabel.setLabelText(geneId);
	//			geneThingLabel.setLsThing(LsThing.findLsThing(lowestGeneThingId));
	//			geneThingLabel.setRecordedBy("acas admin");
	//			geneThingLabel.setRecordedDate(new Date());
	//			geneThingLabel.setLsTransaction(lsTransaction.getId());
	//			geneThingLabel.persist();			
	//		} else if (thingLabels.size() == 1) {
	//			geneThingLabel = thingLabels.get(0);
	//			if (geneThingLabel.isIgnored() || !geneThingLabel.isPreferred()){
	//				geneThingLabel.setIgnored(false);
	//				geneThingLabel.setPreferred(true);
	//				geneThingLabel.merge();
	//			}
	//		} else {
	//			logger.debug("number of entrez gene ids to sort through: " + thingLabels.size());
	//			List<LsThingLabel> otherThingLabels = LsThingLabel.findLsThingLabelsByLsThingAndLsTypeAndLsKind(LsThing.findLsThing(lowestGeneThingId), "name", "Entrez Gene ID").getResultList();
	//
	//			boolean firstPass = true;
	//			for (LsThingLabel thingLabel : otherThingLabels){
	//				if (firstPass && thingLabel.getLabelText().equalsIgnoreCase(geneId)){
	//					if (thingLabel.isIgnored() || !thingLabel.isPreferred()){
	//						if (!thingLabel.isDeleted()){
	//							thingLabel.setIgnored(false);
	//							thingLabel.setPreferred(true);
	//							thingLabel.merge();							
	//						}
	//					}
	//					firstPass = false;
	//				} else {
	//					if (thingLabel.getLabelText().equalsIgnoreCase(geneId)){
	//						logger.debug("@@@@@@@ REMOVING Preferred GENE ID @@@@@@@  " + thingLabel.toJson() );
	//						//thingLabel.remove();	
	//						thingLabel.setIgnored(true);
	//						thingLabel.setDeleted(true);
	//						thingLabel.merge();
	//					} else {
	//						if (!thingLabel.isDeleted()){
	//							thingLabel.setIgnored(false);
	//							thingLabel.setPreferred(false);
	//							thingLabel.merge();
	//						}
	//					}
	//				}
	//			}
	//		}		
	//	}

	//	private void deleteDuplicateLabels(long geneThingId) {
	//		Collection<LsThingLabel> labels = LsThingLabel.findLsThingLabelsByLsThing(LsThing.findLsThing(geneThingId)).getResultList();
	//		Map<String, LsThingLabel> labelMap = new HashMap<String, LsThingLabel>();
	//		String labelKey;
	//		for (LsThingLabel label : labels){
	//			labelKey = new StringBuilder().append(label.getLsType()).append("_").append(label.getLsKind()).append("_").append(label.getLabelText()).toString();
	//			if (labelMap.containsKey(labelKey)){
	//				if (label.getId() < labelMap.get(labelKey).getId()){
	//					logger.debug("deleting duplicate label: "  + labelKey);
	//					labelMap.get(labelKey).setIgnored(true);
	//					labelMap.get(labelKey).setDeleted(true);
	//					labelMap.get(labelKey).merge();
	//					labelMap.put(labelKey, label);
	//				} else {
	//					logger.debug("deleting duplicate label: "  + label.toJson());
	//					label.setIgnored(true);
	//					label.setDeleted(true);
	//					label.merge();
	//				}
	//			} else {
	//				labelMap.put(labelKey, label);
	//			}
	//		}
	//
	//		LsThingLabel queryLabel;
	//		for (String labelMapKey : labelMap.keySet()){
	//			queryLabel = labelMap.get(labelMapKey);
	//			if (queryLabel.isIgnored()){
	//				queryLabel.setIgnored(false);
	//				queryLabel.merge();
	//			}		
	//		}
	//	}



	//	private void removeOldGeneId(long geneThingId, String geneId) {
	//
	//		//		String oldGeneId = null;
	//		//		Collection<LsThingLabel> oldGeneLabels = LsThingLabel.findLsThingLabelsByLsThing(LsThing.findLsThing(highestGeneThingId)).getResultList();
	//		//		for (LsThingLabel label : oldGeneLabels){
	//		//			if (label.getLsType().equalsIgnoreCase("name")
	//		//					&& label.getLsKind().equalsIgnoreCase("Entrez Gene ID")) {
	//		//				if (label.getLabelText().equalsIgnoreCase(geneId)){
	//		//					label.setPreferred(false);
	//		//					label.setIgnored(false);
	//		//					label.merge();
	//		//					logger.debug("### ignoring the gene label: " + label.toJson());
	//		//
	//		//				} else {
	//		//					logger.info("%%%%%%%  old gene label  %%%%%%%%%%%%%%%%");
	//		//					logger.info(label.toJson());
	//		//					oldGeneId = label.getLabelText();					
	//		//				}
	//		//
	//		//			}
	//		//		}
	//
	//		Collection<LsThingLabel> newGeneLabels = LsThingLabel.findLsThingLabelsByLsThing(LsThing.findLsThing(geneThingId)).getResultList();
	//		for (LsThingLabel label : newGeneLabels){
	//			if (label.getLsType().equalsIgnoreCase("name")
	//					&& label.getLsKind().equalsIgnoreCase("Entrez Gene ID")){
	//				if (label.getLabelText().equalsIgnoreCase(geneId)){
	//					label.setPreferred(true);
	//					label.setIgnored(false);
	//					label.merge();
	//				} else {
	//					label.setPreferred(false);
	//					label.setIgnored(false);
	//					label.setImageFile("Discontinued Entrez Gene ID");
	//					label.merge();
	//				}
	//			}
	//
	//			//					&& label.getLabelText().equalsIgnoreCase(geneId)) {
	//			//				label.setPreferred(true);
	//			//				label.setIgnored(false);
	//			//				label.merge();
	//			//			} else if (label.getLsType().equalsIgnoreCase("name")
	//			//					&& label.getLsKind().equalsIgnoreCase("Entrez Gene ID")
	//			//					&& label.getLabelText().equalsIgnoreCase(oldGeneId)) {
	//			//				label.setPreferred(false);
	//			//				label.setIgnored(false);
	//			//				label.setImageFile("Discontinued Entrez Gene ID");
	//			//				label.merge();
	//			//			} 
	//		}
	//	}
	//
	//	private void removeOtherLabelsAndSetPreferredGeneId(LsThingLabel targetLabel) {
	//		logger.debug("#########  target label: " + targetLabel.toJson());
	//		LsThing gene = LsThing.findLsThing(targetLabel.getLsThing().getId());
	//		Collection<LsThingLabel> labels = LsThingLabel.findLsThingLabelsByLsThing(gene).getResultList();
	//		for (LsThingLabel label : labels){
	//			logger.debug("current label ID: " + label.getId() + "    label: " + label.getLabelText());
	//			if (label.getId().longValue() == targetLabel.getId().longValue()) {
	//				logger.debug("####### found mathching target label  " + targetLabel.getId());
	//				label.setPreferred(true);
	//				label.setIgnored(false);
	//				label.merge();
	//			} else if (label.getLsType().equalsIgnoreCase("name") && label.getLsKind().equalsIgnoreCase("Entrez Gene ID") 
	//					&& !label.getLabelText().equalsIgnoreCase(targetLabel.getLabelText().toString())) {
	//				logger.debug("### found another entrez gene id: " + label.toJson());
	//				label.setPreferred(false);
	//				label.setIgnored(false);
	//				label.setImageFile("other Entrez Gene ID");
	//				label.merge();
	//				logger.debug("### updated entrez gene id: " + label.toJson());
	//
	//			} else {
	//				logger.debug("### ignoring the gene label: " + label.toJson());
	//				label.setPreferred(false);
	//				label.setIgnored(true);		
	//				label.merge();
	//			}
	//		}
	//
	//	}

	//	public LsThing registerUpdatedGene(EntrezDbGeneDTO geneDTO,
	//			EntrezDiscontinuedGeneDTO entrezDiscontinuedGeneDTO,
	//			LsTransaction lsTransaction) {
	//		// TODO Auto-generated method stub
	//		logger.info("SHOULD NOT HIT THIS ################################################################################## !!!!!!!!!!!!!!!!");
	//		return null;
	//	}
	//
	//	public LsThing registerUpdatedGene(EntrezDbGeneDTO geneDTO,
	//			EntrezDiscontinuedGeneDTO entrezDiscontinuedGeneDTO,
	//			MultiValueMap discontinuedGenesList, LsTransaction lsTransaction) {
	//		// TODO Auto-generated method stub
	//		logger.info("SHOULD NOT HIT THIS ################################################################################## !!!!!!!!!!!!!!!!");
	//		return null;
	//	}

	private void displayGeneLabels(long geneThingId) {
		Collection<LsThingLabel> labels = LsThingLabel.findLsThingLabelsByLsThing(LsThing.findLsThing(geneThingId)).getResultList();
		logger.info("##################################################   " + geneThingId);
		for (LsThingLabel label : labels){
			logger.info(label.toJson());
		}
		logger.info("##################################################");

	}

	// ################ GENE ORTHOLOG SERVICE #####################################
	//// ** GENE ORTHOLOGS ** /////

	@Override
	@Transactional
	public LsThing saveOrthologEntity(String versionName, String testFileName, String orthologType, Long curationLevel, String description, String curator, String recordedBy) {

		setGeneOrthologDefaults();

		LsTransaction lsTransaction = new LsTransaction();
		lsTransaction.setComments("saving new orthologs");
		lsTransaction.setRecordedDate(new Date());
		lsTransaction.persist();

		if (recordedBy.isEmpty() || recordedBy.equalsIgnoreCase("")) recordedBy = "acas-admin";

		LsThing orthologThing = new LsThing();
		orthologThing.setLsType("ortholog");
		orthologThing.setLsKind("entrez gene");
		orthologThing.setRecordedBy(recordedBy);
		orthologThing.setCodeName(autoLabelService.getLsThingCodeName("ortholog_entrez gene"));
		orthologThing.setLsTransaction(lsTransaction.getId());
		orthologThing.persist();

		LsThingLabel thingLabel = new LsThingLabel();
		thingLabel.setLabelText(versionName);
		thingLabel.setLsType("name");
		thingLabel.setLsKind("ortholog name");
		thingLabel.setRecordedBy(recordedBy);
		thingLabel.setLsThing(orthologThing);
		thingLabel.setLsTransaction(lsTransaction.getId());
		thingLabel.persist();

		LsThingState thingState = new LsThingState();
		thingState.setLsType("metadata");
		thingState.setLsKind("ortholog");
		thingState.setRecordedBy(recordedBy);
		thingState.setLsThing(orthologThing);
		thingState.setLsTransaction(lsTransaction.getId());
		thingState.persist();

		LsThingValue thingValue;
		Set<LsThingValue> thingValues = new HashSet<LsThingValue>();
		thingValue = new LsThingValue();
		thingValue.setLsType("fileValue");
		thingValue.setLsKind("ortholog map");
		thingValue.setFileValue(testFileName);
		thingValue.setLsState(thingState);
		thingValue.setRecordedBy(recordedBy);
		thingValue.setLsTransaction(lsTransaction.getId());
		thingValue.persist();
		thingValues.add(thingValue);

		thingValue = new LsThingValue();
		thingValue.setLsType("stringValue");
		thingValue.setLsKind("ortholog type");
		thingValue.setStringValue(orthologType);
		thingValue.setLsState(thingState);
		thingValue.setRecordedBy(recordedBy);
		thingValue.setLsTransaction(lsTransaction.getId());
		thingValue.persist();
		thingValues.add(thingValue);

		thingValue = new LsThingValue();
		thingValue.setLsType("numericValue");
		thingValue.setLsKind("curation level");
		thingValue.setNumericValue(BigDecimal.valueOf(curationLevel));
		thingValue.setLsState(thingState);
		thingValue.setRecordedBy(recordedBy);
		thingValue.setLsTransaction(lsTransaction.getId());
		thingValue.persist();
		thingValues.add(thingValue);

		thingValue = new LsThingValue();
		thingValue.setLsType("clobValue");
		thingValue.setLsKind("description");
		thingValue.setClobValue(description);
		thingValue.setLsState(thingState);
		thingValue.setRecordedBy(recordedBy);
		thingValue.setLsTransaction(lsTransaction.getId());
		thingValue.persist();
		thingValues.add(thingValue);

		thingValue = new LsThingValue();
		thingValue.setLsType("stringValue");
		thingValue.setLsKind("curator");
		thingValue.setStringValue(recordedBy);
		thingValue.setLsState(thingState);
		thingValue.setRecordedBy(recordedBy);
		thingValue.setLsTransaction(lsTransaction.getId());
		thingValue.persist();
		thingValues.add(thingValue);

		thingState.setLsValues(thingValues);
		Set<LsThingState> thingStates = new HashSet<LsThingState>();
		thingStates.add(thingState);
		orthologThing.setLsStates(thingStates);;

		return orthologThing;

	}

	@Override
	public void registerGeneOrthologsFromCSV(String inputFile, String ortCodeName, String recordedBy, Long lsTransactionId) throws IOException{

		setGeneOrthologDefaults();

		if (recordedBy.isEmpty() || recordedBy.equalsIgnoreCase("")) recordedBy = "acas-admin";

		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		logger.info("read tab delimited file " + inputFile );

		// map header text to standard bean names
		HashMap<String, String> geneOrthologBeanMap = new HashMap<String, String>();
		geneOrthologBeanMap.put("RowId", "rowIndex");
		geneOrthologBeanMap.put("InputGeneId", "geneId");
		geneOrthologBeanMap.put("InputSymbol", "symbol");
		geneOrthologBeanMap.put("InputTaxId", "taxId");  // from Species
		geneOrthologBeanMap.put("InputGeneType", "typeOfGene");
		geneOrthologBeanMap.put("MappedGeneId", "mappedGeneId");
		geneOrthologBeanMap.put("MappedSymbol", "mappedSymbol");
		geneOrthologBeanMap.put("MappedTaxId", "mappedTaxId"); // mapped to Species
		geneOrthologBeanMap.put("MappedTypeOfGene", "mappedTypeOfGene");

		geneOrthologBeanMap.put("MappedScore", "mappedScore");
		geneOrthologBeanMap.put("MappedPerc", "mappedPerc");
		geneOrthologBeanMap.put("MappedHitLen", "mappedHitLen");
		geneOrthologBeanMap.put("MappedPercRatio", "mappedPercRatio");
		geneOrthologBeanMap.put("MappedLocalIndex", "mappedLocalIndex");
		geneOrthologBeanMap.put("MappedGeneIndex", "mappedGeneIndex");
		geneOrthologBeanMap.put("MappedIsoformIndex", "mappedIsoformIndex");

		ICsvBeanReader beanReader = null;
		beanReader = new CsvBeanReader(br, CsvPreference.STANDARD_PREFERENCE);
		String[] headers = beanReader.getHeader(true);

		List<String> headerList = new ArrayList<String>();
		int position = 0;
		for (String header : headers){
			logger.info("header column: " + position + "  " + header);
			if (geneOrthologBeanMap.get(header) != null){
				headerList.add(geneOrthologBeanMap.get(header));   				
			}
			position++;
		}


		logger.debug("size of header list  " + headerList.size());
		String[] headerArray = new String[headerList.size()];
		headerList.toArray(headerArray);

		for (String headerInd : headerArray){
			logger.warn("header column array : " + position + "  " + headerInd);
			position++;
		}

		final CellProcessor[] processors = getOrthologProcessors();

		GeneOrthologDTO geneOrthologDTO;

		int numberOfLines = SimpleUtil.countLines(inputFile);
		Long numberOfLabels = new Long(numberOfLines-1);

		logger.info("number of lines found: " + numberOfLines);

		LsTransaction lsTransaction;
		if (lsTransactionId == null){			
			lsTransaction = new LsTransaction();
			lsTransaction.setComments("saving gene ortholog info");
			lsTransaction.setRecordedDate(new Date());
			lsTransaction.persist();
			lsTransactionId = lsTransaction.getId();
		} else {
			lsTransaction = LsTransaction.findLsTransaction(lsTransactionId);
		}

		int i = 0;
		int batchSize = 25;
		LsThing geneThing = null;
		while( (geneOrthologDTO = beanReader.read(GeneOrthologDTO.class, headerArray, processors)) != null ) {
			//System.out.println(String.format("lineNo=%s, rowNo=%sd() + "  mapped Gene " + geneOrthologDTO.getMappedGeneId, bulkData=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), geneOrthologDTO));
			logger.info("current gene: " + geneOrthologDTO.getGeneId());

			LsThing targetGene = null;
			try {
				targetGene = LsThing.findLsThingByLabelText("gene", "entrez gene", "name", "Entrez Gene ID", geneOrthologDTO.getGeneId()).getSingleResult();
				logger.info("target gene: " + targetGene.getCodeName());				
			} catch (EmptyResultDataAccessException e) {
				logger.error("ERROR - did not find a gene with ID: " + geneOrthologDTO.getGeneId());
				throw new RuntimeException();
			}

			LsThing orthologGene = null;
			try {
				orthologGene = LsThing.findLsThingByLabelText("gene", "entrez gene", "name", "Entrez Gene ID", geneOrthologDTO.getMappedGeneId()).getSingleResult();
				logger.info("mapped ortholog gene: " + orthologGene.getCodeName());			
			} catch (EmptyResultDataAccessException e) {
				logger.error("ERROR - did not find a gene with ID: " + geneOrthologDTO.getMappedGeneId());
				throw new RuntimeException();
			}

			ItxLsThingLsThing itxOrtholog = new ItxLsThingLsThing();
			itxOrtholog.setFirstLsThing(targetGene);
			itxOrtholog.setSecondLsThing(orthologGene);
			itxOrtholog.setLsType("ortholog of");
			itxOrtholog.setLsKind("entrez gene_entrez gene");
			itxOrtholog.setRecordedBy(recordedBy);
			//			itxOrtholog.setCodeName(autoLabelService.getLsThingCodeName("ortholog of_entrez gene_entrez gene"));
			itxOrtholog.setCodeName(autoLabelService.getLsThingCodeName("interaction_ortholog"));
			itxOrtholog.setLsTransaction(lsTransactionId);
			itxOrtholog.persist();

			ItxLsThingLsThingState itxOrthologState = new ItxLsThingLsThingState();
			itxOrthologState.setLsType("metadata");
			itxOrthologState.setLsKind("ortholog");
			itxOrthologState.setItxLsThingLsThing(itxOrtholog);		
			itxOrthologState.setRecordedBy(recordedBy);
			itxOrthologState.setLsTransaction(lsTransactionId);
			itxOrthologState.persist();

			ItxLsThingLsThingValue savedValue;

			savedValue = saveItxStateCodeValue(lsTransaction, recordedBy, itxOrthologState, ortCodeName, "ortholog", "entrez gene", "ACAS LsThing", "codeValue", "ortholog");

			savedValue = saveItxStateValue(lsTransaction, recordedBy, itxOrthologState, "numericValue", "MappedScore", geneOrthologDTO.getMappedScore());
			savedValue = saveItxStateValue(lsTransaction, recordedBy, itxOrthologState, "numericValue", "MappedPerc", geneOrthologDTO.getMappedPerc());
			savedValue = saveItxStateValue(lsTransaction, recordedBy, itxOrthologState, "numericValue", "MappedHitLen", geneOrthologDTO.getMappedHitLen());
			savedValue = saveItxStateValue(lsTransaction, recordedBy, itxOrthologState, "numericValue", "MappedPercRatio", geneOrthologDTO.getMappedPercRatio());

			//			savedValue = saveItxStateValue(lsTransaction, recordedBy, itxOrthologState, "stringValue", "InputGeneId", geneOrthologDTO.getGeneId());
			//			savedValue = saveItxStateValue(lsTransaction, recordedBy, itxOrthologState, "stringValue", "InputSymbol", geneOrthologDTO.getSymbol());
			//			savedValue = saveItxStateValue(lsTransaction, recordedBy, itxOrthologState, "stringValue", "InputTaxId", geneOrthologDTO.getTaxId());
			//			savedValue = saveItxStateValue(lsTransaction, recordedBy, itxOrthologState, "stringValue", "MappedGeneId", geneOrthologDTO.getMappedGeneId());
			//			savedValue = saveItxStateValue(lsTransaction, recordedBy, itxOrthologState, "stringValue", "MappedSymbol", geneOrthologDTO.getMappedSymbol());
			//			savedValue = saveItxStateValue(lsTransaction, recordedBy, itxOrthologState, "stringValue", "MappedTaxId", geneOrthologDTO.getMappedTaxId());

			//			itxOrtholog.getLsStates().add(itxOrthologState);


			if ( i % batchSize == 0 ) { // same as the JDBC batch size
				savedValue.flush();
				savedValue.clear();
				itxOrtholog.flush();
				itxOrtholog.clear();
			}
			i++;
		}

	}



	private ItxLsThingLsThingValue saveItxStateCodeValue(LsTransaction lsTransaction, String recordedBy,
			ItxLsThingLsThingState itxOrthologState, String codeValue, String codeType, String codeKind, String codeOrigin,
			String lsType, String lsKind) {

		ItxLsThingLsThingValue savedValue = new ItxLsThingLsThingValue();
		savedValue.setLsType(lsType);
		savedValue.setLsKind(lsKind);
		savedValue.setLsTransaction(lsTransaction.getId());
		savedValue.setRecordedBy(recordedBy);
		savedValue.setLsState(itxOrthologState);
		savedValue.setCodeValue(codeValue);
		savedValue.setCodeType(codeType);
		savedValue.setCodeKind(codeKind);
		savedValue.setCodeOrigin(codeOrigin);		
		savedValue.persist();

		return savedValue;
	}

	public ItxLsThingLsThingValue saveItxStateValue(LsTransaction lsTransaction, String recordedBy, ItxLsThingLsThingState itxOrthologState, String stateType, String stateKind, Integer numericValue) {
		ItxLsThingLsThingValue savedValue = new ItxLsThingLsThingValue();
		savedValue.setLsType(stateType);
		savedValue.setLsKind(stateKind);
		savedValue.setLsTransaction(lsTransaction.getId());
		savedValue.setRecordedBy(recordedBy);
		savedValue.setLsState(itxOrthologState);
		savedValue.setNumericValue(BigDecimal.valueOf(numericValue));
		savedValue.persist();

		return savedValue;
	}

	public ItxLsThingLsThingValue saveItxStateValue(LsTransaction lsTransaction, String recordedBy, ItxLsThingLsThingState itxOrthologState, String stateType, String stateKind, Double numericValue) {
		ItxLsThingLsThingValue savedValue = new ItxLsThingLsThingValue();
		savedValue.setLsType(stateType);
		savedValue.setLsKind(stateKind);
		savedValue.setLsTransaction(lsTransaction.getId());
		savedValue.setRecordedBy(recordedBy);
		savedValue.setLsState(itxOrthologState);
		savedValue.setNumericValue(BigDecimal.valueOf(numericValue));
		savedValue.persist();

		return savedValue;
	}

	public ItxLsThingLsThingValue saveItxStateValue(LsTransaction lsTransaction, String recordedBy, ItxLsThingLsThingState itxOrthologState, String stateType,String stateKind, String stringValue) {
		ItxLsThingLsThingValue savedValue = new ItxLsThingLsThingValue();
		savedValue.setLsType(stateType);
		savedValue.setLsKind(stateKind);
		savedValue.setLsTransaction(lsTransaction.getId());
		savedValue.setRecordedBy(recordedBy);
		savedValue.setLsState(itxOrthologState);
		savedValue.setStringValue(stringValue);
		savedValue.persist();

		return savedValue;
	}


	public void setGeneOrthologDefaults() {

		//		itxOrtholog.setCodeName(autoLabelService.getLsThingCodeName("ortholog of_entrez gene_entrez gene"));


		String thingTypeAndKind = "ortholog_entrez gene";
		String labelTypeAndKind = "id_codeName";
		List<LabelSequence> labelSeqs = LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(thingTypeAndKind, labelTypeAndKind).getResultList();
		if (labelSeqs.size() == 0){
			LabelSequence newLabelSeq = new LabelSequence();
			newLabelSeq.setDigits(6);
			newLabelSeq.setLabelPrefix("ORT");
			newLabelSeq.setLabelSeparator("-");
			newLabelSeq.setStartingNumber(1L);
			newLabelSeq.setLabelTypeAndKind(labelTypeAndKind);
			newLabelSeq.setThingTypeAndKind(thingTypeAndKind);
			newLabelSeq.persist();
		} 

		InteractionType.getOrCreate("first ortholog of second", "ortholog of");
		InteractionKind.getOrCreate("ortholog of", "entrez gene_entrez gene");

		thingTypeAndKind = "interaction_ortholog";
		labelTypeAndKind = "id_codeName";
		List<LabelSequence> ortxLabelSeqs = LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(thingTypeAndKind, labelTypeAndKind).getResultList();
		if (ortxLabelSeqs.size() == 0){
			LabelSequence newLabelSeq = new LabelSequence();
			newLabelSeq.setDigits(6);
			newLabelSeq.setLabelPrefix("ORTX");
			newLabelSeq.setLabelSeparator("-");
			newLabelSeq.setStartingNumber(1L);
			newLabelSeq.setLabelTypeAndKind(labelTypeAndKind);
			newLabelSeq.setThingTypeAndKind(thingTypeAndKind);
			newLabelSeq.persist();
		} 

		StateType stateType = StateType.getOrCreate("metadata");
		StateKind.getOrCreate(stateType, "ortholog");

		ValueType valueType = ValueType.getOrCreate("numericValue");
		ValueKind.getOrCreate(valueType, "MappedScore");
		ValueKind.getOrCreate(valueType, "MappedPerc");
		ValueKind.getOrCreate(valueType, "MappedHitLen");
		ValueKind.getOrCreate(valueType, "MappedPercRatio");

		valueType = ValueType.getOrCreate("stringValue");
		ValueKind.getOrCreate(valueType, "InputGeneId");
		ValueKind.getOrCreate(valueType, "InputSymbol");
		ValueKind.getOrCreate(valueType, "InputTaxId");
		ValueKind.getOrCreate(valueType, "MappedGeneId");
		ValueKind.getOrCreate(valueType, "MappedSymbol");
		ValueKind.getOrCreate(valueType, "MappedTaxId");

	}


	public CellProcessor[] getOrthologProcessors() {
		//16
		final CellProcessor[] processors = new CellProcessor[] { 
				new Optional(new ParseInt()),  // rowId

				new Optional(),  // geneId
				new Optional(),  // symbol
				new Optional(),  // taxId
				new Optional(),  // typeOfGene

				new Optional(),  // mappedGeneId
				new Optional(),  // mappedSymbol
				new Optional(),  // mappedTaxId
				new Optional(), // mappedTypeOfGene

				new Optional(new ParseInt()), // 
				new Optional(new ParseInt()), // 
				new Optional(new ParseInt()), //
				new Optional(new ParseDouble()), //
				new Optional(new ParseInt()), //
				new Optional(new ParseInt()), //
				new Optional(new ParseInt())
		};

		return processors;
	}

	@Transactional
	@Override
	public List<GeneOrthologDTO> getOrthologsFromGeneID(String queryGeneID) throws IOException{

		List<LsThing> queryGenes = LsThing.findLsThingByLabelText("gene", "entrez gene", "name", "Entrez Gene ID", queryGeneID).getResultList();
		LsThing queryGene = queryGenes.get(0);
		logger.info("number of query genes found: " + queryGenes.size());
		//logger.info("query gene: " + queryGene.toJson());
		String lsType = "ortholog of";
		String lsKind = "entrez gene_entrez gene";
		List<ItxLsThingLsThing> itxOrthologs = ItxLsThingLsThing.findItxLsThingLsThingsByLsTypeEqualsAndLsKindEqualsAndFirstLsThingEquals(lsType, lsKind, queryGene).getResultList();

		List<GeneOrthologDTO> geneOrthologs = new ArrayList<GeneOrthologDTO>();
		GeneOrthologDTO geneOrtholog;
		LsThingLabel geneSymbol;

		for (ItxLsThingLsThing itxOrtholog : itxOrthologs){
			//logger.info("ortholog itx: " + itxOrtholog.toJson());

			if (checkValidOrtholog(itxOrtholog)){
				geneOrtholog = new GeneOrthologDTO();
				geneOrtholog.setId(itxOrtholog.getId());
				geneOrtholog.setRecordedBy(itxOrtholog.getRecordedBy());
				geneOrtholog.setGeneId(queryGeneID);
				geneOrtholog.setSymbol(findGeneSymbol(queryGene));
				geneOrtholog.setTaxId(findTaxonomyID(queryGene));
				geneOrtholog.setSpecies(findSpeciesName(geneOrtholog.getTaxId()));
				geneOrtholog.setOrthCode(findOrthologEntityCode(itxOrtholog));

				if (geneOrtholog.getOrthCode() != null){
					geneOrtholog = setOrthologInfo(geneOrtholog);
				} else {
					geneOrtholog.setCurator(findOrthologCurator(itxOrtholog));
					geneOrtholog.setCurationLevel(findCurationLevel(itxOrtholog));
					geneOrtholog.setVersionName(findVersionName(geneOrtholog));					
				}

				LsThing mappedOrthologGene = itxOrtholog.getSecondLsThing();
			//	logger.info("Mapped ortholog gene: " + mappedOrthologGene.toJson());
				geneOrtholog.setMappedGeneId(findGeneID(mappedOrthologGene));
				geneOrtholog.setMappedSymbol(findGeneSymbol(mappedOrthologGene));
				geneOrtholog.setMappedTaxId(findTaxonomyID(mappedOrthologGene));
				geneOrtholog.setMappedSpecies(findSpeciesName(geneOrtholog.getMappedTaxId()));

				geneOrtholog.setMappedPerc(findItxStateValueInteger(itxOrtholog, "MappedPerc"));
				geneOrtholog.setMappedScore(findItxStateValueInteger(itxOrtholog, "MappedScore"));
				geneOrtholog.setMappedHitLen(findItxStateValueInteger(itxOrtholog, "MappedHitLen"));
				geneOrtholog.setMappedPercRatio(findItxStateValueDouble(itxOrtholog, "MappedPercRatio"));

//				geneOrthologBeanMap.put("InputGeneType", "typeOfGene");
//				geneOrthologBeanMap.put("MappedTypeOfGene", "mappedTypeOfGene");
//				ValueKind.getOrCreate(valueType, "MappedScore");
//				ValueKind.getOrCreate(valueType, "MappedPerc");
//				ValueKind.getOrCreate(valueType, "MappedHitLen");
//				ValueKind.getOrCreate(valueType, "MappedPercRatio");
//				ValueKind.getOrCreate(valueType, "InputGeneId");
//				ValueKind.getOrCreate(valueType, "InputSymbol");
//				ValueKind.getOrCreate(valueType, "InputTaxId");
//				ValueKind.getOrCreate(valueType, "MappedGeneId");
//				ValueKind.getOrCreate(valueType, "MappedSymbol");
//				ValueKind.getOrCreate(valueType, "MappedTaxId");
				
				geneOrthologs.add(geneOrtholog);				
			}
		}

	//	logger.info(GeneOrthologDTO.toJsonArray(geneOrthologs));
		return geneOrthologs;

	}


	private String findItxStateValueString(ItxLsThingLsThing itxOrtholog, String valueKind) {
		String itxStateValue = null;
		String stateType = "metadata";
		String stateKind = "ortholog";
		String valueType = "stringValue";
		List<ItxLsThingLsThingValue> itxStateValues = ItxLsThingLsThingValue.findItxLsValueByItxThingAndStateTypeStateKindAndValueTypeKind(itxOrtholog.getId(), stateType, stateKind, valueType, valueKind).getResultList();
		if (itxStateValues.size() == 1){
			itxStateValue = itxStateValues.get(0).getStringValue();
		} else if (itxStateValues.size() > 1){
			logger.error("FOUND multiple " + valueKind );
		}
		return itxStateValue;
		}

	private Integer findItxStateValueInteger(ItxLsThingLsThing itxOrtholog, String valueKind) {
		Integer itxStateValue = null;
		String stateType = "metadata";
		String stateKind = "ortholog";
		String valueType = "numericValue";
		List<ItxLsThingLsThingValue> itxStateValues = ItxLsThingLsThingValue.findItxLsValueByItxThingAndStateTypeStateKindAndValueTypeKind(itxOrtholog.getId(), stateType, stateKind, valueType, valueKind).getResultList();

		if (itxStateValues.size() == 1){
			itxStateValue = new Integer(itxStateValues.get(0).getNumericValue().toString()).intValue();
		} else if (itxStateValues.size() > 1){
			logger.error("FOUND multiple " + valueKind );
	}
		return itxStateValue;
	}
	

	private Double findItxStateValueDouble(ItxLsThingLsThing itxOrtholog, String valueKind) {
		Double itxStateValue = null;
		String stateType = "metadata";
		String stateKind = "ortholog";
		String valueType = "numericValue";
		List<ItxLsThingLsThingValue> itxStateValues = ItxLsThingLsThingValue.findItxLsValueByItxThingAndStateTypeStateKindAndValueTypeKind(itxOrtholog.getId(), stateType, stateKind, valueType, valueKind).getResultList();

		if (itxStateValues.size() == 1){
			itxStateValue = new Double(itxStateValues.get(0).getNumericValue().toString());
		} else if (itxStateValues.size() > 1){
			logger.error("FOUND multiple " + valueKind );
		}
		return itxStateValue;
	}


	private GeneOrthologDTO setOrthologInfo(GeneOrthologDTO geneOrtholog) {
		LsThing orthologThing = LsThing.findLsThingsByCodeNameEquals(geneOrtholog.getOrthCode()).getSingleResult();
	//	logger.info("@@@@@@@@@@@@ ----- " + orthologThing.toJson());

		List<LsThingLabel> orthologNames = LsThingLabel.findLsThingLabelsByLsThingAndLsTypeAndLsKindNotIgnored(orthologThing, "name", "ortholog name", true).getResultList();
		if (orthologNames.size() == 1){
			geneOrtholog.setVersionName(orthologNames.get(0).getLabelText());
		} else if (orthologNames.size() > 1){
			logger.error("FOUND multiple Ortholog name values");
		} else {
			logger.error("ERROR: Did not find Ortholog name");
		}

		List<LsThingValue> curationLevels = LsThingValue.findLsThingValuesByLsThingIDAndStateTypeKindAndValueTypeKind(orthologThing.getId(), "metadata", "ortholog", "numericValue", "curation level").getResultList();

		if (curationLevels.size() == 1){
			Integer curationLevel = new Double(curationLevels.get(0).getNumericValue().toString()).intValue();
			geneOrtholog.setCurationLevel(curationLevel);
		} else if (curationLevels.size() > 1){
			logger.error("FOUND multiple curation levels");
		}

		List<LsThingValue> curators = LsThingValue.findLsThingValuesByLsThingIDAndStateTypeKindAndValueTypeKind(orthologThing.getId(), "metadata", "ortholog", "stringValue", "curator").getResultList();
		if (curators.size() == 1){
			geneOrtholog.setCurator(curators.get(0).getStringValue());
		} else if (curators.size() > 1){
			logger.error("FOUND multiple curators");
		}

		return geneOrtholog;
	}

	private boolean checkValidOrtholog(ItxLsThingLsThing itxOrtholog) {
		boolean validOrtholog = true;
		String orthologCode = null;
		String stateType = "metadata";
		String stateKind = "ortholog";
		String valueType = "codeValue";
		String valueKind = "ortholog";
		List<ItxLsThingLsThingValue> orthCodes = ItxLsThingLsThingValue.findItxLsValueByItxThingAndStateTypeStateKindAndValueTypeKind(itxOrtholog.getId(), stateType, stateKind, valueType, valueKind).getResultList();

		if (orthCodes.size() == 1){
			orthologCode = orthCodes.get(0).getCodeValue();
			LsThing orthThing = LsThing.findLsThingsByCodeNameEquals(orthologCode).getSingleResult();
			if (orthThing.isIgnored()){
				validOrtholog = false;
			}

		} else if (orthCodes.size() > 1){
			logger.error("FOUND multiple orth codes");
			validOrtholog = false;
		}

		return validOrtholog;
	}

	private String findVersionName(GeneOrthologDTO geneOrtholog) {
		String orthologName = null;
		if (geneOrtholog.getOrthCode() != null){
			LsThing orthThing = LsThing.findLsThingsByCodeNameEquals(geneOrtholog.getOrthCode()).getSingleResult();
			List<LsThingLabel> orthologNames = LsThingLabel.findLsThingLabelsByLsThingAndLsTypeAndLsKindNotIgnored(orthThing, "name", "ortholog name", true).getResultList();
			if (orthologNames.size() == 1){
				orthologName = orthologNames.get(0).getLabelText();
			} else if (orthologNames.size() > 1){
				logger.error("FOUND multiple Ortholog name values");
			} else {
				logger.error("ERROR: Did not find Ortholog name");
			}			
		}

		return orthologName;
	}

	private String findSpeciesName(String mappedTaxId) {
		String species = null;
		if (mappedTaxId.equalsIgnoreCase("9606")){
			species = "Homo sapiens";
		} else if (mappedTaxId.equalsIgnoreCase("7227")){
			species = "Drosophila melanogaster";
		} else if (mappedTaxId.equalsIgnoreCase("10090")){
			species = "Mus musculus";
		} else if (mappedTaxId.equalsIgnoreCase("10116")){
			species = "Rattus norvegicus";
		}

		return species;
	}



	private Integer findCurationLevel(ItxLsThingLsThing itxOrtholog) {
		Integer curationLevel = null;
		String stateType = "metadata";
		String stateKind = "ortholog";
		String valueType = "numericValue";
		String valueKind = "curation level";
		List<ItxLsThingLsThingValue> curationLevels = ItxLsThingLsThingValue.findItxLsValueByItxThingAndStateTypeStateKindAndValueTypeKind(itxOrtholog.getId(), stateType, stateKind, valueType, valueKind).getResultList();

		if (curationLevels.size() == 1){
			curationLevel = new Double(curationLevels.get(0).getNumericValue().toString()).intValue();
		} else if (curationLevels.size() > 1){
			logger.error("FOUND multiple curation levels");
		}
		return curationLevel;
	}

	private String findOrthologCurator(ItxLsThingLsThing itxOrtholog) {
		String curator = null;


		String stateType = "metadata";
		String stateKind = "ortholog";
		String valueType = "stringValue";
		String valueKind = "curator";
		List<ItxLsThingLsThingValue> curators = ItxLsThingLsThingValue.findItxLsValueByItxThingAndStateTypeStateKindAndValueTypeKind(itxOrtholog.getId(), stateType, stateKind, valueType, valueKind).getResultList();

		if (curators.size() == 1){
			curator = curators.get(0).getStringValue();
		} else if (curators.size() > 1){
			logger.error("FOUND multiple curators");
		}

		return curator;
	}

	private String findGeneID(LsThing mappedOrthologGene) {
		String geneID = null;		
		List<LsThingLabel> geneIDs = LsThingLabel.findLsThingPreferredName(mappedOrthologGene.getId(), "name", "Entrez Gene ID", true, true).getResultList();
		if (geneIDs.size() == 1){
			geneID = geneIDs.get(0).getLabelText();
		} else if (geneIDs.size() > 1){
			logger.error("FOUND multiple Gene ID values");
		} else {
			logger.error("ERROR: Did not find an Entrez Gene ID");
		}
		return geneID;
	}

	private String findOrthologEntityCode(ItxLsThingLsThing itxOrtholog) {
		String orthologCode = null;
		String stateType = "metadata";
		String stateKind = "ortholog";
		String valueType = "codeValue";
		String valueKind = "ortholog";
		List<ItxLsThingLsThingValue> orthCodes = ItxLsThingLsThingValue.findItxLsValueByItxThingAndStateTypeStateKindAndValueTypeKind(itxOrtholog.getId(), stateType, stateKind, valueType, valueKind).getResultList();

		if (orthCodes.size() == 1){
			orthologCode = orthCodes.get(0).getCodeValue();
		} else if (orthCodes.size() > 1){
			logger.error("FOUND multiple orth codes");
		}

		return orthologCode;
	}

	private String findTaxonomyID(LsThing queryGene) {
		String taxonomyId = null;
		List<LsThingValue> taxIdValues = LsThingValue.findLsThingValuesByLsThingIDAndStateTypeKindAndValueTypeKind(queryGene.getId(), "metadata", "gene metadata", "stringValue", "tax id").getResultList();
		if (taxIdValues.size() == 1){
			taxonomyId = taxIdValues.get(0).getStringValue();
		} else if (taxIdValues.size() > 1){
			logger.error("FOUND multiple tax Id values");
		} else {
			logger.error("ERROR: Did not find a tax id");
		}
		return taxonomyId;
	}

	private String findGeneSymbol(LsThing queryGene) {
		String geneSymbol = null;
		List<LsThingLabel> geneSymbols = LsThingLabel.findLsThingLabelsByLsThingAndLsTypeAndLsKindNotIgnored(queryGene, "name", "authoritative gene symbol", true).getResultList();
		if (geneSymbols.size() == 1){
			geneSymbol = geneSymbols.get(0).getLabelText();
		} else if (geneSymbols.size() > 1){
			logger.error("FOUND multiple Gene Symbol values");
		} else {
			logger.error("ERROR: Did not find authoritative gene symbol");
		}
		return geneSymbol;
	}

	@Override
	public Collection<GeneOrthologDTO> saveOrthologInteractions(Collection<GeneOrthologDTO> geneOrthologs) {

		Collection<GeneOrthologDTO> savedOrthologs = new ArrayList<GeneOrthologDTO>();
		for (GeneOrthologDTO geneOrtholog : geneOrthologs){
			geneOrtholog = saveOrthologInteraction(geneOrtholog);
			savedOrthologs.add(geneOrtholog);
		}
		
		return savedOrthologs;

	}
	
	@Override
	public GeneOrthologDTO saveOrthologInteraction(GeneOrthologDTO geneOrtholog) {

		if (geneOrtholog.getId() == null){
			//this is a new entry
			List<LsThing> queryGenes = LsThing.findLsThingByLabelText("gene", "entrez gene", "name", "Entrez Gene ID", geneOrtholog.getGeneId()).getResultList();
			LsThing queryGene = null;
			if (queryGenes.size() == 1){
				queryGene = queryGenes.get(0);
			} else if (queryGenes.size() > 1){
				logger.error("multiple genes found");
			} if (queryGenes.size() < 1){
				logger.error("no gene found");
			}

			List<LsThing> orthologGenes = LsThing.findLsThingByLabelText("gene", "entrez gene", "name", "Entrez Gene ID", geneOrtholog.getMappedGeneId()).getResultList();
			LsThing orthologGene = null;
			if (orthologGenes.size() == 1){
				orthologGene = orthologGenes.get(0);
			} else if (orthologGenes.size() > 1){
				logger.error("multiple ortholog genes found");
			} if (orthologGenes.size() < 1){
				logger.error("no ortholog gene found");
			}

			LsTransaction lsTransaction = new LsTransaction();
			lsTransaction.setComments("saving gene ortholog info");
			lsTransaction.setRecordedDate(new Date());
			lsTransaction.persist();

			ItxLsThingLsThing itxOrtholog = new ItxLsThingLsThing();
			itxOrtholog.setFirstLsThing(queryGene);
			itxOrtholog.setSecondLsThing(orthologGene);
			itxOrtholog.setRecordedBy(geneOrtholog.getRecordedBy());
			itxOrtholog.setRecordedDate(new Date());
			itxOrtholog.setLsType("ortholog of");
			itxOrtholog.setLsKind("entrez gene_entrez gene");
			itxOrtholog.setCodeName(autoLabelService.getLsThingCodeName("interaction_ortholog"));
			itxOrtholog.setLsTransaction(lsTransaction.getId());
			itxOrtholog.persist();

			ItxLsThingLsThingState itxOrthologState = new ItxLsThingLsThingState();
			itxOrthologState.setLsType("metadata");
			itxOrthologState.setLsKind("ortholog");
			itxOrthologState.setItxLsThingLsThing(itxOrtholog);		
			itxOrthologState.setRecordedBy(geneOrtholog.getRecordedBy());
			itxOrthologState.setLsTransaction(lsTransaction.getId());
			itxOrthologState.persist();

			ItxLsThingLsThingValue savedValue;
			savedValue = saveItxStateValue(lsTransaction, geneOrtholog.getRecordedBy(), itxOrthologState, "numericValue", "curation level", geneOrtholog.getCurationLevel());
			savedValue = saveItxStateValue(lsTransaction, geneOrtholog.getRecordedBy(), itxOrthologState, "stringValue", "curator", geneOrtholog.getCurator());
			if (geneOrtholog.getVersionName() != null){
				savedValue = saveItxStateValue(lsTransaction, geneOrtholog.getRecordedBy(), itxOrthologState, "stringValue", "version name", geneOrtholog.getVersionName());
			}

			geneOrtholog.setId(itxOrtholog.getId());

		} else {
			//update existing entry

		}

		return geneOrtholog;
	}

}
