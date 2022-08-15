package com.labsynch.labseer.service;

import flexjson.JSONSerializer;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import java.net.MalformedURLException;

import javax.persistence.TypedQuery;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.labseer.chemclasses.CmpdRegSDFReader;
import com.labsynch.labseer.chemclasses.CmpdRegSDFReaderFactory;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriterFactory;
import com.labsynch.labseer.chemclasses.CmpdRegSDFWriter;

import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.IsoSalt;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.SaltForm;
import com.labsynch.labseer.domain.LotAliasKind;
import com.labsynch.labseer.domain.LotAliasType;
import com.labsynch.labseer.domain.ParentAliasKind;
import com.labsynch.labseer.domain.ParentAliasType;
import com.labsynch.labseer.dto.LotAliasDTO;
import com.labsynch.labseer.dto.ParentAliasDTO;

import com.labsynch.labseer.dto.BatchCodeDependencyDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.DependencyCheckDTO;
import com.labsynch.labseer.dto.CmpdRegBatchCodeDTO;
import com.labsynch.labseer.dto.ContainerBatchCodeDTO;
import com.labsynch.labseer.service.BulkLoadService;
import com.labsynch.labseer.dto.PurgeSaltDependencyCheckResponseDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;
import com.labsynch.labseer.service.AssayService;
import com.labsynch.labseer.service.ContainerService;
import com.labsynch.labseer.service.LotService;
import com.labsynch.labseer.service.ChemStructureService.StructureType;

import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.service.ChemStructureService.SearchType;
import com.labsynch.labseer.service.ChemStructureService.StructureType;
import com.labsynch.labseer.utils.MoleculeUtil;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaltServiceImpl implements SaltService {

	Logger logger = LoggerFactory.getLogger(SaltServiceImpl.class);

	@Autowired
	private ChemStructureService chemStructServ;

	@Autowired
	CmpdRegMoleculeFactory cmpdRegMoleculeFactory;

	@Autowired
	CmpdRegSDFReaderFactory cmpdRegSDFReaderFactory;

	@Autowired
	public CmpdRegSDFWriterFactory cmpdRegSDFWriterFactory;

	@Autowired
	private AssayService assayService;

	@Autowired
	private ContainerService containerService;

	@Autowired 
	private LotService lotService;

	@Autowired
	private SaltService saltService;

	@Autowired
	private SaltStructureService saltStructureService;

	@Autowired
	public CmpdRegSDFWriterFactory sdfWriterFactory;

	@Autowired
	PropertiesUtilService propertiesUtilService;

	@Transactional
	@Override
	public int loadSalts(String saltSD_fileName) {
		// simple utility to load salts
		// fileName = "src/test/resources/Initial_Salts.sdf";
		int savedSaltCount = 0;
		try {
			// Open an input stream
			CmpdRegSDFReader mi = cmpdRegSDFReaderFactory.getCmpdRegSDFReader(saltSD_fileName);
			CmpdRegMolecule mol = null;
			Long saltCount = 0L;
			while ((mol = mi.readNextMol()) != null) {
				// save salt if no existing salt with the same Abbrev -- could do match by other
				// properties
				saltCount = Salt.countFindSaltsByAbbrevLike(MoleculeUtil.getMolProperty(mol, "code"));
				if (saltCount < 1) {
					saveSalt(mol);
					savedSaltCount++;
				}
			}
			mi.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmpdRegMolFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return savedSaltCount;
	}

	@Transactional
	private void saveSalt(CmpdRegMolecule mol) throws IOException, CmpdRegMolFormatException {
		Salt salt = new Salt();
		salt.setMolStructure(MoleculeUtil.exportMolAsText(mol, "mol"));
		salt.setOriginalStructure(MoleculeUtil.exportMolAsText(mol, "mol"));
		salt.setAbbrev(MoleculeUtil.getMolProperty(mol, "code"));
		salt.setName(MoleculeUtil.getMolProperty(mol, "Name"));
		salt.setFormula(mol.getFormula());
		salt.setMolWeight(mol.getMass());

		logger.debug("salt code: " + salt.getAbbrev());
		logger.debug("salt name: " + salt.getName());
		logger.debug("salt structure: " + salt.getMolStructure());

		int[] queryHits = chemStructServ.searchMolStructures(salt.getMolStructure(), StructureType.SALT,
				SearchType.DUPLICATE_NO_TAUTOMER);
		Integer cdId = 0;
		if (queryHits.length > 0) {
			cdId = 0;
		} else {
			cdId = chemStructServ.saveStructure(salt.getMolStructure(), StructureType.SALT);
		}
		salt.setCdId(cdId);

		if (salt.getCdId() > 0 && salt.getCdId() != -1) {
			salt.persist();
		} else {
			logger.error("Could not save the salt: " + salt.getAbbrev());
		}
	}

	@Transactional
	public String exportSalts() {
		CmpdRegSDFWriter sdfWriter = null;
		try {
			sdfWriter = sdfWriterFactory.getCmpdRegSDFBufferWriter();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Get List of Salts 
		List<Salt> allSaltList = Salt.findAllSalts();

		for (Salt salt : allSaltList) {
			try {
				CmpdRegMolecule currSalt = cmpdRegMoleculeFactory.getCmpdRegMolecule(salt.getMolStructure());
				currSalt.setProperty("name", salt.getName());
				currSalt.setProperty("abbrev", salt.getAbbrev());
				currSalt.setProperty("formula", salt.getFormula());
				currSalt.setProperty("molWeight", String.valueOf(salt.getMolWeight()));
				currSalt.setProperty("charge", String.valueOf(salt.getCharge()));
				sdfWriter.writeMol(currSalt);
			} catch (CmpdRegMolFormatException e) {
				logger.error("bad structure error: " + salt.getMolStructure());
			} catch (IOException e) {
				logger.error("IO error reading in molfile");
				e.printStackTrace();
			}

		}
		try {
			sdfWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.debug("ou	tput SDF: " + sdfWriter.getBufferString());
		return sdfWriter.getBufferString();
	}

	public boolean isValidSaltEdit(Salt oldSalt, Salt newSalt)
	{
		boolean validSalt = true;
		List<Salt> saltsByName = Salt.findSaltsByNameEquals(newSalt.getName()).getResultList();
		if (saltsByName.size() > 1) {
			validSalt = false;
		}
		List<Salt> saltsByAbbrev = Salt.findSaltsByAbbrevEquals(newSalt.getAbbrev()).getResultList();
		if (saltsByAbbrev.size() > 1) {
			validSalt = false;
		}
		return validSalt;
	}

	public ArrayList<ErrorMessage> validateSaltEdit(Salt oldSalt, Salt newSalt)
	{
		ArrayList<ErrorMessage> warnings = new ArrayList<ErrorMessage>();

		if(!newSalt.getAbbrev().equals(oldSalt.getAbbrev())){
			ErrorMessage warning = new ErrorMessage();
			warning.setLevel("warning");
			warning.setMessage("The abbreviation of this salt will be changed.");
			warnings.add(warning);
		}
		if(!newSalt.getName().equals(oldSalt.getName())){
			ErrorMessage warning = new ErrorMessage();
			warning.setLevel("warning");
			warning.setMessage("The name of this salt will be changed.");
			warnings.add(warning);
		}

		newSalt.setAbbrev(newSalt.getAbbrev().trim());
		newSalt.setName(newSalt.getName().trim());
		newSalt.setFormula(saltStructureService.calculateFormula(newSalt));
		newSalt.setMolWeight(saltStructureService.calculateWeight(newSalt));
		newSalt.setCharge(saltStructureService.calculateCharge(newSalt));

		if(newSalt.getMolWeight() != oldSalt.getMolWeight()){
			ErrorMessage warning = new ErrorMessage();
			warning.setLevel("warning");
			warning.setMessage("The weight of this salt has changed.");
			warnings.add(warning);
		}

		if(newSalt.getCharge() != oldSalt.getCharge()){
			ErrorMessage warning = new ErrorMessage();
			warning.setLevel("warning");
			warning.setMessage("The charge of this salt has changed.");
			warnings.add(warning);
		}

		List<Salt> saltsByName = Salt.findSaltsByNameEquals(newSalt.getName()).getResultList();
		if (saltsByName.size() > 1) {
			logger.error("Number of salts found: " + saltsByName.size());
			ErrorMessage error = new ErrorMessage();
			error.setLevel("error");
			error.setMessage("Duplicate salt name. Another salt exist with the same name.");
			warnings.add(error);
		}
		List<Salt> saltsByAbbrev = Salt.findSaltsByAbbrevEquals(newSalt.getAbbrev()).getResultList();
		if (saltsByAbbrev.size() > 1) {
			logger.error("Number of salts found: " + saltsByAbbrev.size());
			ErrorMessage error = new ErrorMessage();
			error.setLevel("error");
			error.setMessage("Duplicate salt abbreviation. Another salt exist with the same abbreviation.");
			warnings.add(error);
		}
		List<Salt> saltsByFormula = Salt.findSaltsByFormulaEquals(newSalt.getFormula()).getResultList();
		if (saltsByFormula.size() > 1) {
			logger.error("Number of salts found: " + saltsByName.size());
			ErrorMessage error = new ErrorMessage();
			error.setLevel("warning");
			error.setMessage("Duplicate salt formula. Another salt exist with the same formula.");
			warnings.add(error);
		}
			
		// Get IsoSalts From Salt 
		TypedQuery<IsoSalt> isoSaltQuery = IsoSalt.findIsoSaltsBySalt(oldSalt);
		Collection<IsoSalt> isoSalts =  isoSaltQuery.getResultList(); 
		Set<IsoSalt> isoSaltsSet = new HashSet<IsoSalt>(isoSalts);

		Set<SaltForm> saltFormSet = new HashSet<SaltForm>();
		// Getter of IsoSalt to SaltForm 

		for (IsoSalt isoSalt : isoSaltsSet)
		{
			SaltForm tempSaltForm = isoSalt.getSaltForm();
			saltFormSet.add(tempSaltForm);
		}

		// Getter of Lots from IsoSalt 

		Set<Lot> lotSet = new HashSet<Lot>(); 
		for (SaltForm saltForm : saltFormSet)
		{
			Set<Lot> tempLotSet = saltForm.getLots();
			lotSet.addAll(tempLotSet);
		}

		int dependencyLotSize = lotSet.size();
		if (dependencyLotSize > 0)
		{
			ErrorMessage error = new ErrorMessage();
			error.setLevel("warning");
			error.setMessage("This salt is referenced by " + String.valueOf(dependencyLotSize) + " lots");
			warnings.add(error);

			// Check for linked containers
			Set<String> batchCodeSet = new HashSet<String>();
			for (Lot lot : lotSet)
			{
				batchCodeSet.add(lot.getCorpName());
			}
			CmpdRegBatchCodeDTO batchDTO = lotService.checkForDependentData(batchCodeSet);

			if(batchDTO.getBatchCodes() != null && batchDTO.getBatchCodes().size() > 0)
			{
				// Adds Associated Batch Codes to Warning Report 
				error = new ErrorMessage();
				error.setLevel("warning");
				error.setMessage("Associated Batch Codes: " + batchDTO.getBatchCodes().toString());
				warnings.add(error);
			}

			if(batchDTO.getLinkedExperiments() != null && batchDTO.getLinkedExperiments().size() > 0)
			{
				// Adds Linked Experiments to Warning Report 
				error = new ErrorMessage();
				error.setLevel("warning");
				error.setMessage("Associated Experiments: " + batchDTO.getLinkedExperiments().toString());
				warnings.add(error);
			}
			
			if( batchDTO.getLinkedContainers() != null  && batchDTO.getLinkedContainers().size() > 0)
			{
				error = new ErrorMessage();
				error.setLevel("warning");
				error.setMessage("Associated Containers: " + batchDTO.getLinkedContainers().toString());
				warnings.add(error);
			}

			// Adds Summary As Warning Message
			error = new ErrorMessage();
			error.setLevel("warning");
			error.setMessage("Dependency Report Summary: " + batchDTO.getSummary());
			warnings.add(error);

			// Check CReg Config to See If Salt Abbrev in Lab Corp Name
			if(!newSalt.getAbbrev().equals(oldSalt.getAbbrev())){
				if (!propertiesUtilService.getCorpBatchFormat().equalsIgnoreCase("cas_style_format"))
				{
					error = new ErrorMessage();
					error.setLevel("warning");
					error.setMessage("Lot corp names references salt abbreviations. Any associated lot corp names will be updated.");
					warnings.add(error);
				}

			}
		}


		return warnings;
	}

	public void updateDependencies(Salt salt)
	{
		

		// Get IsoSalts From Salt 
		TypedQuery<IsoSalt> isoSaltQuery = IsoSalt.findIsoSaltsBySalt(salt);
		Collection<IsoSalt> isoSalts =  isoSaltQuery.getResultList(); 
		Set<IsoSalt> isoSaltsSet = new HashSet<IsoSalt>(isoSalts);

		Set<SaltForm> saltFormSet = new HashSet<SaltForm>();

		// Getter of IsoSalt to SaltForm 
		for (IsoSalt isoSalt : isoSaltsSet)
		{
			SaltForm tempSaltForm = isoSalt.getSaltForm();
			saltFormSet.add(tempSaltForm);
		}

		// Getter of Lots from IsoSalt 

		Set<Lot> lotSet = new HashSet<Lot>(); 
		for (SaltForm saltForm : saltFormSet)
		{
			Set<Lot> tempLotSet = saltForm.getLots();
			lotSet.addAll(tempLotSet);
		}

		for (Lot lot : lotSet){
			// Update Lot Corp Name If Format Uses Salt Abbrev
			if (!propertiesUtilService.getCorpBatchFormat().equalsIgnoreCase("cas_style_format")) {
				String newCorpName = lotService.generateCorpName(lot);
				lot.setCorpName(newCorpName);
				 // If Lot Corp Name Change, Cascade the Udpdate to “Batch Code” Values Within Analysis Groups and Lot Inventory Containers	
				assayService.renameBatchCode(lot.getCorpName(), newCorpName, "SaltService");
                containerService.renameBatchCode(lot.getCorpName(), newCorpName, "SaltService", null);

			}
			logger.info("new lot corp name: " + lot.getCorpName());
			// Recalculate Lot Molecular Weights
			lot.setLotMolWeight(Lot.calculateLotMolWeight(lot));		
		}
	}

	public ArrayList<Long> getAllParentIDs(Salt salt)
	{
		Set<Salt> saltSet = new HashSet<Salt>();
		saltSet.add(salt);

		ArrayList<Long> parentIDs = new ArrayList<Long>();
		Collection<Parent> parents = new HashSet<Parent>();

		try
		{
			// Get All IsoSalts From Salt
			if(IsoSalt.countIsoSalts() > 0){
				TypedQuery<IsoSalt> isoSaltQuery = IsoSalt.findIsoSaltsBySalt(salt);
				Collection<IsoSalt> isoSalts =  isoSaltQuery.getResultList(); 
				Set<IsoSalt> isoSaltsSet = new HashSet<IsoSalt>(isoSalts);
				
				if(SaltForm.countSaltForms() > 0)
				{
					// Get All SaltFormsFrom IsoSalts 
					TypedQuery<SaltForm> saltFormQuery = SaltForm.findSaltFormsByAnyIsoSalts(isoSaltsSet);
					Collection<SaltForm> saltForms = saltFormQuery.getResultList();
					Set<SaltForm> saltFormsSet = new HashSet<SaltForm>(saltForms);

					if(Parent.countParents() > 0)
					{
						// findParentsBySaltForms(Set<SaltForm> saltForms)
						TypedQuery<Parent> parentQuery = Parent.findParentsByAnySaltForms(saltFormsSet);
						parents = parentQuery.getResultList();
					}
				}
				
			}
			// Returns a List of All Parents 

			for (Parent parent : parents)
			{
				parentIDs.add(parent.getId());
			}
	
			return parentIDs;
		}
		catch (NullPointerException e)
		{
			// Query Result Empty
			e.printStackTrace();
			return new ArrayList<Long>();
		}
	}

	// Helper Method Used to Check Dependencies Within ACAS
	private Map<String, HashSet<String>> checkACASDependencies(
		Map<String, HashSet<String>> acasDependencies) throws MalformedURLException, IOException {
			String url = propertiesUtilService.getAcasURL() + "compounds/checkBatchDependencies";
			BatchCodeDependencyDTO request = new BatchCodeDependencyDTO(acasDependencies.keySet());
			String json = request.toJson();
			String responseJson = SimpleUtil.postRequestToExternalServer(url, json, null);
			BatchCodeDependencyDTO responseDTO = BatchCodeDependencyDTO.fromJsonToBatchCodeDependencyDTO(responseJson);
			if (responseDTO.getLinkedDataExists()) {
				for (CodeTableDTO experimentCodeTable : responseDTO.getLinkedExperiments()) {
					String experimentCodeAndName = experimentCodeTable.getCode() + " ( " + experimentCodeTable.getName()
							+ " )";
					if (acasDependencies.containsKey(experimentCodeTable.getComments())) {
						acasDependencies.get(experimentCodeTable.getComments()).add(experimentCodeAndName);
					} else {
						HashSet<String> codes = new HashSet<String>();
						codes.add(experimentCodeAndName);
						acasDependencies.put(experimentCodeTable.getComments(), codes);
					}
				}
			}
			return acasDependencies;
		}

	// Helper Method to Check Dependent Containers; Utilized in Method Below It

	// batchCodes == Lot Corp Name
	private Collection<ContainerBatchCodeDTO> checkDependentACASContainers(Set<String> batchCodes)
		throws MalformedURLException, IOException {
			String url = propertiesUtilService.getAcasURL() + "containers/getContainerDTOsByBatchCodes";
			String json = (new JSONSerializer()).serialize(batchCodes);
			String responseJson = SimpleUtil.postRequestToExternalServer(url, json, null);
			Collection<ContainerBatchCodeDTO> responseDTOs = ContainerBatchCodeDTO
					.fromJsonArrayToContainerBatchCoes(responseJson);
			return responseDTOs;
		}

	// Method to Check for Dependent Data 
	public PurgeSaltDependencyCheckResponseDTO checkDependentData(Salt salt)
	{
		// Get IsoSalts From Salt 
		TypedQuery<IsoSalt> isoSaltQuery = IsoSalt.findIsoSaltsBySalt(salt);
		Collection<IsoSalt> isoSalts =  isoSaltQuery.getResultList(); 
		Set<IsoSalt> isoSaltsSet = new HashSet<IsoSalt>(isoSalts);

		// Getter of IsoSalt to SaltForm 

		Set<SaltForm> saltFormSet = new HashSet<SaltForm>();
		for (IsoSalt isoSalt : isoSaltsSet)
		{
			SaltForm tempSaltForm = isoSalt.getSaltForm();
			saltFormSet.add(tempSaltForm);
		}

		// Getter of Lots from IsoSalt 

		Set<Lot> lotSet = new HashSet<Lot>(); 
		for (SaltForm saltForm : saltFormSet)
		{
			Set<Lot> tempLotSet = saltForm.getLots();
			lotSet.addAll(tempLotSet);
		}

		int dependencyLotSize = lotSet.size();

		if (dependencyLotSize  > 0) {
			String summary = "This salt is referenced by " + String.valueOf(dependencyLotSize) + " lots. It cannot be deleted.";
			return new PurgeSaltDependencyCheckResponseDTO(summary, false);
		} else {
			String summary = "There were no lot dependencies found for this salt.";
			return new PurgeSaltDependencyCheckResponseDTO(summary, true);
		}
	}
}
