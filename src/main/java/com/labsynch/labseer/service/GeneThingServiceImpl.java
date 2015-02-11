package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import com.labsynch.labseer.utils.SimpleUtil;

@Service
public class GeneThingServiceImpl implements GeneThingService {

	private static final Logger logger = LoggerFactory.getLogger(GeneThingServiceImpl.class);

	@Autowired
	private AutoLabelService autoLabelService;


	@Override
	public void RegisterGeneThingsFromCSV(String inputFile) throws IOException{

		logger.info("read tab delimited file");
		//		String testFileName = "sample_genes.txt";
		//		InputStream is = CreateGeneIDsFromCSVFileTests.class.getClassLoader().getResourceAsStream(testFileName);
		//		InputStream is = IOUtils.toInputStream(inputFile);
		//		br = new BufferedReader(new FileReader("C:\\testing.txt"));
		//
		//		InputStreamReader isr = new InputStreamReader(is);  
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		//		String headerLine = br.readLine();
		//		logger.debug("HeaderLine: " + headerLine);
		//
		//		StringWriter csvFileOut = new StringWriter();
		//		ICsvMapWriter writer = new CsvMapWriter(csvFileOut, CsvPreference.EXCEL_PREFERENCE);
		//		

		String nameMapping;
		//		EntrezDbGeneDTO gene = new EntrezDbGeneDTO();
		//		beanReader.read(Experiment, nameMapping)


		// final CellProcessor[] processors = getProcessors();

		//		BulkTransferPart1DTO clazz = new BulkTransferPart1DTO();
		//		BulkTransferPart1DTO ick = beanReader.read(BulkTransferPart1DTO.class, header, processors);

		ICsvBeanReader beanReader = null;
		try {
			LsTransaction lsTransaction = new LsTransaction();
			lsTransaction.setComments("saving entrez gene id info");
			lsTransaction.setRecordedDate(new Date());
			lsTransaction.persist();
			beanReader = new CsvBeanReader(br, CsvPreference.TAB_PREFERENCE);
			String[] headerText = beanReader.getHeader(true);


			//set up gene type and kinds (check if already exists)

			String geneTypeString = "gene";
			String geneKindString = "entrez gene";
			ThingType geneType = ThingType.getOrCreate(geneTypeString);
			ThingKind.getOrCreate(geneType, geneKindString);

			LabelType labelType = LabelType.findLabelTypesByTypeNameEquals("name").getSingleResult();
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


			final CellProcessor[] processors = getProcessors();

			EntrezDbGeneDTO geneDTO;

			StateType stateType = StateType.getOrCreate("metadata");
			StateKind.getOrCreate(stateType, "gene metadata");

			ValueType valueType = ValueType.getOrCreate("stringValue");
			ValueKind.getOrCreate(valueType, "tax id");
			ValueKind.getOrCreate(valueType, "locus tag");
			ValueKind.getOrCreate(valueType, "dbXrefs");
			ValueKind.getOrCreate(valueType, "chromosome");
			ValueKind.getOrCreate(valueType, "map location");
			ValueKind.getOrCreate(valueType, "description");
			ValueKind.getOrCreate(valueType, "type of gene");
			ValueKind.getOrCreate(valueType, "nomenclature status");

			ValueType valueTypeDate = ValueType.getOrCreate("dateValue");
			ValueKind.getOrCreate(valueTypeDate, "modification date");

			int numberOfLines = SimpleUtil.countLines(inputFile);
			Long numberOfLabels = new Long(numberOfLines-1);
			
			List<AutoLabelDTO> thingCodes = autoLabelService.getAutoLabels(thingTypeAndKind, labelTypeAndKind, numberOfLabels);
			
			int i = 0;
			int batchSize = 25;
			LsThing geneThing = null;
			while( (geneDTO = beanReader.read(EntrezDbGeneDTO.class, header, processors)) != null ) {
				System.out.println(String.format("lineNo=%s, rowNo=%s, bulkData=%s", beanReader.getLineNumber(), beanReader.getRowNumber(), geneDTO));
				logger.info("current gene: " + geneDTO.getGeneId());

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
			if (value.equalsIgnoreCase("-")){
				logger.info("skipping gene descriptor: " + value);
			} else {
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
	}

	private void saveGeneLabel(LsTransaction lsTransaction, LsThing geneThing, String labelType, String labelKind, boolean preferred, String labels, String entrezSeparator) {
		String[] labelList = labels.split(entrezSeparator);
		for (String label : labelList){
			if (label.equalsIgnoreCase("-")){
				logger.info("skipping gene label: " + label);
			} else {
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
	}

	private static CellProcessor[] getProcessors() {

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

}
