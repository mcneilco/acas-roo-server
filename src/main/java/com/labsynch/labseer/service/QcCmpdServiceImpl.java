package com.labsynch.labseer.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriter;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriterFactory;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.ParentAlias;
import com.labsynch.labseer.domain.QcCompound;
import com.labsynch.labseer.dto.configuration.MainConfigDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.utils.Configuration;
import com.labsynch.labseer.utils.MoleculeUtil;

@Service
public class QcCmpdServiceImpl implements QcCmpdService {

	Logger logger = LoggerFactory.getLogger(QcCmpdServiceImpl.class);

	public static final MainConfigDTO mainConfig = Configuration.getConfigInfo();

	@Autowired
	public ChemStructureService chemStructureService;

	@Autowired
	public ParentLotService parentLotService;

	@Autowired
	public ParentStructureService parentStructureService;

	@Autowired
	public ParentAliasService parentAliasService;
	
	@Autowired
	CmpdRegSDFWriterFactory cmpdRegSDFWriterFactory;
	
	@Autowired
	CmpdRegMoleculeFactory cmpdRegMoleculeFactory;

	public void saveQcCmpdStructure(QcCompound qcCmpd){
		Integer cdId = chemStructureService.saveStructure(qcCmpd.getMolStructure(), "QC_Cmpd_Structure");
		if (cdId == -1){
			logger.error("Bad molformat. Please fix the molfile: " + qcCmpd.getMolStructure());
		} else {
			qcCmpd.setCdId(cdId);
		}
	}


	@Override
	public void exportQCReport(String outputFilePathName, String exportType) throws IOException, CmpdRegMolFormatException{
		List<Long> qcIds = QcCompound.findPotentialQcCmpds().getResultList();
		if (exportType.equalsIgnoreCase("csv")){
			exportQcCompoundsToCsv(qcIds, outputFilePathName);
		} else {
			exportQcCompoundsToSdf(qcIds, outputFilePathName);
		}
	}


	private void exportQcCompoundsToSdf(List<Long> qcIds, String sdfFilePathName) throws IllegalArgumentException, IOException, CmpdRegMolFormatException {
		CmpdRegSDFWriter qcReportMolExporter = cmpdRegSDFWriterFactory.getCmpdRegSDFWriter(sdfFilePathName);
		QcCompound qcCmpd;
		CmpdRegMolecule mol;
		for (Long qcId : qcIds){
			qcCmpd = QcCompound.findQcCompound(qcId);
			mol = cmpdRegMoleculeFactory.getCmpdRegMolecule(qcCmpd.getMolStructure());
			mol.setProperty("id", Long.toString(qcCmpd.getId()));
			mol.setProperty("parent_id", Long.toString(qcCmpd.getParentId()));
			mol.setProperty("corp_name", qcCmpd.getCorpName());
			mol.setProperty("stereo_category", qcCmpd.getStereoCategory());
			mol.setProperty("stereo_comment", qcCmpd.getStereoComment());
			mol.setProperty("display_change", Boolean.toString(qcCmpd.isDisplayChange()));
			mol.setProperty("dupe_count", Integer.toString(qcCmpd.getDupeCount()));
			mol.setProperty("dupe_corp_name", qcCmpd.getDupeCorpName());
			mol.setProperty("alias", qcCmpd.getAlias());
			mol.setProperty("comment", qcCmpd.getComment());
			qcReportMolExporter.writeMol(mol);
		}
		qcReportMolExporter.close();
	}


	private void exportQcCompoundsToCsv(List<Long> qcIds, String fileName) throws IOException, CmpdRegMolFormatException  {
		PrintWriter csvWriter = new PrintWriter(new File(fileName)) ;
		char DEFAULT_SEPARATOR = ',';

		StringBuilder sbHeader = new StringBuilder();
		sbHeader.append("id").append(DEFAULT_SEPARATOR);
		sbHeader.append("parent_id").append(DEFAULT_SEPARATOR);
		sbHeader.append("corp_name").append(DEFAULT_SEPARATOR);
		sbHeader.append("stereo_category").append(DEFAULT_SEPARATOR);
		sbHeader.append("stereo_comment").append(DEFAULT_SEPARATOR);
		sbHeader.append("display_change").append(DEFAULT_SEPARATOR);
		sbHeader.append("dupe_count").append(DEFAULT_SEPARATOR);
		sbHeader.append("dupe_corp_name").append(DEFAULT_SEPARATOR);
		sbHeader.append("alias").append(DEFAULT_SEPARATOR);
		sbHeader.append("mol_smiles").append(DEFAULT_SEPARATOR);
		sbHeader.append("comment");
		csvWriter.println(sbHeader.toString());

		StringBuilder sbData;
		QcCompound qcCmpd;
		for (Long qcId : qcIds){
			qcCmpd = QcCompound.findQcCompound(qcId);
			sbData = new StringBuilder();
			sbData.append(qcCmpd.getId()).append(DEFAULT_SEPARATOR);
			sbData.append(qcCmpd.getParentId()).append(DEFAULT_SEPARATOR);
			sbData.append(qcCmpd.getCorpName()).append(DEFAULT_SEPARATOR);
			sbData.append(qcCmpd.getStereoCategory()).append(DEFAULT_SEPARATOR);
			sbData.append(qcCmpd.getStereoComment()).append(DEFAULT_SEPARATOR);
			sbData.append(qcCmpd.isDisplayChange()).append(DEFAULT_SEPARATOR);
			sbData.append(qcCmpd.getDupeCount()).append(DEFAULT_SEPARATOR);
			sbData.append(qcCmpd.getDupeCorpName()).append(DEFAULT_SEPARATOR);
			sbData.append(qcCmpd.getAlias()).append(DEFAULT_SEPARATOR);
			CmpdRegMolecule mol = cmpdRegMoleculeFactory.getCmpdRegMolecule(qcCmpd.getMolStructure());
			sbData.append(mol.getSmiles()).append(DEFAULT_SEPARATOR);
			sbData.append(qcCmpd.getComment());
			csvWriter.println(sbData.toString());
		}
		csvWriter.close();
	}

	// code to check the structures -- original as drawn mol --> standardized versus the original struct
	// may need a qc_compound table to store the mols and a list of identified dupes
	// id, runNumber, date, parent_id, corpName, dupeCount, dupeCorpName, asDrawnStruct, preMolStruct, postMolStruct, comment

	@Transactional
	@Override
	public int qcCheckParentStructures() throws CmpdRegMolFormatException, IOException{
		List<Long> parentIds = Parent.getParentIds();
		Parent parent;
		QcCompound qcCompound;
		int nonMatchingCmpds = 0;
		logger.info("number of parents to check: " + parentIds.size());
		Date qcDate = new Date();
		String asDrawnStruct;
		Integer cdId = 0 ;
		List<Lot> queryLots;
		Integer runNumber = QcCompound.findMaxRunNumber().getSingleResult();
		if (runNumber == null){
			runNumber = 1;
		} else {
			runNumber++;
		}

		for  (Long parentId : parentIds){
			parent = Parent.findParent(parentId);
			qcCompound = new QcCompound();
			qcCompound.setRunNumber(runNumber);
			qcCompound.setQcDate(qcDate);
			qcCompound.setParentId(parent.getId());
			qcCompound.setCorpName(parent.getCorpName());
			qcCompound.setAlias(getParentAlias(parent));
			qcCompound.setStereoCategory(parent.getStereoCategory().getName());
			qcCompound.setStereoComment(parent.getStereoComment());
			queryLots = Lot.findLotByParentAndLowestLotNumber(parent).getResultList();
			if (queryLots.size() != 1) logger.error("!!!!!!!!!!!!  odd lot number size   !!!!!!!!!  " + queryLots.size() + "  saltForm: " + parent.getId());
			if (queryLots.size() > 0 && queryLots.get(0).getAsDrawnStruct() != null){
				asDrawnStruct = queryLots.get(0).getAsDrawnStruct();
			} else {
				asDrawnStruct = parent.getMolStructure();
			}
			logger.debug("attempting to standardize: " + parentId + "   " + asDrawnStruct);
			qcCompound.setMolStructure(chemStructureService.standardizeStructure(asDrawnStruct));				
			boolean matching = chemStructureService.compareStructures(asDrawnStruct, qcCompound.getMolStructure(), "DUPLICATE");
			if (!matching){
				qcCompound.setDisplayChange(true);
				logger.info("the compounds are NOT matching: " + parent.getCorpName());
				nonMatchingCmpds++;
			}
			logger.debug("time to save the struture");
			cdId = chemStructureService.saveStructure(qcCompound.getMolStructure(), "QC_Compound_Structure", false);
			if (cdId == -1){
				logger.error("Bad molformat. Please fix the molfile: " + qcCompound.getMolStructure());
			} else {
				logger.debug("here is the cdId: " + cdId);
				qcCompound.setCdId(cdId);
				qcCompound.persist();
				//				qcCompound.flush();
				//				qcCompound.clear();
			}
		}
		logger.info("total number of nonMatching compounds: " + nonMatchingCmpds);
		return (nonMatchingCmpds);
	}

	@Transactional
	private String getParentAlias(Parent parent) {
		StringBuilder aliasSB = new StringBuilder();
		Set<ParentAlias> parentAliases = parent.getParentAliases();
		boolean firstAlias = true;
		for (ParentAlias parentAlias : parentAliases){
			if (firstAlias){
				aliasSB.append(parentAlias.getAliasName());
				firstAlias = false;
			} else {
				aliasSB.append(";").append(parentAlias.getAliasName());
			}
		}
		return aliasSB.toString();
	}


	@Override
	public int dupeCheckQCStructures() throws CmpdRegMolFormatException{
		List<Long> qcIds = QcCompound.findAllIds().getResultList();
		logger.info("number of qcCompounds found: " + qcIds.size());
		int totalDupeCount = 0;
		if (qcIds.size() > 0){
			int[] hits;	
			QcCompound qcCompound;
			String dupeCorpNames = "";
			int dupeCount = 0;
			for (Long qcId : qcIds){
				boolean firstDupeHit = true;
				qcCompound = QcCompound.findQcCompound(qcId);
				logger.debug("query compound: " + qcCompound.getCorpName());
				hits = chemStructureService.searchMolStructures(qcCompound.getMolStructure(), "QC_Compound_Structure", "DUPLICATE_TAUTOMER");
				dupeCount = hits.length;
				logger.debug("current dupeCount: " + dupeCount);
				for (int hit:hits){
					List<QcCompound> searchResults = QcCompound.findQcCompoundsByCdId(hit).getResultList();
					for (QcCompound searchResult : searchResults){
						if (searchResult.getCorpName().equalsIgnoreCase(qcCompound.getCorpName())){
							dupeCount = dupeCount-1;
						} else {
							if (!firstDupeHit) dupeCorpNames = dupeCorpNames.concat(";");
							dupeCorpNames = dupeCorpNames.concat(searchResult.getCorpName());
							firstDupeHit = false;
							if (searchResult.getStereoCategory() == qcCompound.getStereoCategory()
									&& searchResult.getStereoComment().equalsIgnoreCase(qcCompound.getStereoComment())){
								logger.info("found dupe parents");
								logger.info("query: " + qcCompound.getCorpName() + "     dupe: " + searchResult.getCorpName());
								totalDupeCount++;
							} else {
								logger.info("found different stereo codes and comments");
							}
						}
					}
				}
				qcCompound.setDupeCount(dupeCount);
				qcCompound.setDupeCorpName(dupeCorpNames);
				qcCompound.merge();
				dupeCorpNames = "";
			}
		}
		return (totalDupeCount);
	}


}

