package com.labsynch.labseer.chemclasses.bbchem;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.domain.AbstractBBChemStructure;
import com.labsynch.labseer.domain.BBChemDryRunStructure;
import com.labsynch.labseer.domain.BBChemSaltFormStructure;
import com.labsynch.labseer.domain.BBChemSaltStructure;
import com.labsynch.labseer.domain.BBChemParentStructure;
import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.dto.MolConvertOutputDTO;
import com.labsynch.labseer.dto.StrippedSaltDTO;
import com.labsynch.labseer.dto.configuration.StandardizerSettingsConfigDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.StandardizerException;
import com.labsynch.labseer.service.ChemStructureService;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

import org.springframework.jdbc.core.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import java.util.UUID;

import javax.persistence.TypedQuery;

import static java.lang.Math.toIntExact;
@Component
public class ChemStructureServiceBBChemImpl implements ChemStructureService {

	Logger logger = LoggerFactory.getLogger(ChemStructureServiceBBChemImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Autowired
	private BBChemStructureService bbChemStructureService;

	@Autowired
	private JdbcTemplate basicJdbcTemplate;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.basicJdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate() {
		return basicJdbcTemplate;
	}

	@Override
	public CmpdRegMolecule[] searchMols(String molfile, StructureType structureType, int[] cdHitList, SearchType searchType, Float simlarityPercent) throws CmpdRegMolFormatException {
		int maxResults = propertiesUtilService.getMaxSearchResults();
		return searchMols(molfile, structureType, cdHitList, searchType, simlarityPercent, maxResults);
	}

	private List<? extends AbstractBBChemStructure> searchBBChemStructures(String molfile, StructureType structureType, int[] inputCdIdHitList, SearchType searchType, Float simlarityPercent, int maxResults) throws CmpdRegMolFormatException {


		// Create empty list
		List<? extends AbstractBBChemStructure> bbChemStructures = new ArrayList<AbstractBBChemStructure>();

		if (searchType == SearchType.FULL_TAUTOMER | searchType == SearchType.DUPLICATE_TAUTOMER | searchType == SearchType.EXACT){
			// Don't need to calculate fingerprint if not doing similarity or substructure
			BBChemParentStructure serviceBBChemStructure = bbChemStructureService.getProcessedStructure(molfile, false);
			TypedQuery<? extends AbstractBBChemStructure> query;

			if (searchType == SearchType.FULL_TAUTOMER){
				// FULL TAUTOMER hashes are stored on the pre reg hash column
				if(structureType == StructureType.PARENT) {
					query = BBChemParentStructure.findBBChemParentStructuresByPreRegEquals(serviceBBChemStructure.getPreReg());
				} else if (structureType == StructureType.SALT) {
					query = BBChemSaltStructure.findBBChemSaltStructuresByPreRegEquals(serviceBBChemStructure.getPreReg());
				} else if (structureType == StructureType.SALT_FORM) {
					query = BBChemSaltFormStructure.findBBChemSaltFormStructuresByPreRegEquals(serviceBBChemStructure.getPreReg());
				} else if (structureType == StructureType.DRY_RUN) {
					query = BBChemDryRunStructure.findBBChemDryRunStructuresByPreRegEquals(serviceBBChemStructure.getPreReg());
				} else {
					throw new CmpdRegMolFormatException("Structure type not implemented for BBChem searches on " + structureType);
				}
			} else if (searchType == SearchType.DUPLICATE_TAUTOMER | searchType == SearchType.EXACT){
				// DUPLICATE TAUTOMER hashes are stored on the reg column
				if(structureType == StructureType.PARENT) {
					query = BBChemParentStructure.findBBChemParentStructuresByRegEquals(serviceBBChemStructure.getReg());
				} else if (structureType == StructureType.SALT) {
					query = BBChemSaltStructure.findBBChemSaltStructuresByRegEquals(serviceBBChemStructure.getReg());
				} else if (structureType == StructureType.SALT_FORM) {
					query = BBChemSaltFormStructure.findBBChemSaltFormStructuresByRegEquals(serviceBBChemStructure.getReg());
				} else if (structureType == StructureType.DRY_RUN) {
					query = BBChemDryRunStructure.findBBChemDryRunStructuresByRegEquals(serviceBBChemStructure.getReg());
				} else {
					throw new CmpdRegMolFormatException("Structure type not implemented for BBChem searches on " + structureType);
				}
			} else {
				throw new CmpdRegMolFormatException("Structure type not implemented for BBChem searches on " + structureType);
			}

			// int is not nullable so you either have a valid maxResults passed in
			// or -1 to express unlimited
			if(maxResults > -1) {
				query.setMaxResults(maxResults);
			}

			bbChemStructures = query.getResultList();
			
		} else if (searchType == SearchType.SUBSTRUCTURE | searchType == SearchType.SIMILARITY){
			// Calculate fingerprint
			BBChemParentStructure serviceBBChemStructure = bbChemStructureService.getProcessedStructure(molfile, true);
			if(searchType == SearchType.SUBSTRUCTURE) {
				if(structureType == StructureType.PARENT) {
					// Get basic fingerprint substructure matchs
					List<BBChemParentStructure> fingerprintMatchBBChemStructures = BBChemParentStructure.findBBChemParentStructuresBySubstructure(serviceBBChemStructure.getSubstructure(), maxResults);
					
					// Narrow list down to those that match a true subgraph substructure search
					HashMap<? extends AbstractBBChemStructure, Boolean> bbchemSubstructureMatchMap = bbChemStructureService.substructureMatch(serviceBBChemStructure.getMol(), fingerprintMatchBBChemStructures);

					// Loop through the llist of all and only keep those which have a true match
					List<BBChemParentStructure> bbchemMatchStructures = new ArrayList<BBChemParentStructure>();
					for(Map.Entry<? extends AbstractBBChemStructure, Boolean> v : bbchemSubstructureMatchMap.entrySet()) {
						if(v.getValue()) {
							bbchemMatchStructures.add((BBChemParentStructure) v.getKey());
						}
					}
					bbChemStructures = bbchemMatchStructures;
				} else if (structureType == StructureType.SALT_FORM) {
					// Get basic fingerprint substructure matchs
					List<BBChemSaltFormStructure> fingerprintMatchBBChemStructures = BBChemSaltFormStructure.findBBChemSaltFormStructuresBySubstructure(serviceBBChemStructure.getSubstructure(), maxResults);

					// Narrow list down to those that match a true subgraph substructure search
					HashMap<? extends AbstractBBChemStructure, Boolean> bbchemSubstructureMatchMap = bbChemStructureService.substructureMatch(serviceBBChemStructure.getMol(), fingerprintMatchBBChemStructures);

					// Loop through the llist of all and only keep those which have a true match
					List<BBChemSaltFormStructure> bbchemMatchStructures = new ArrayList<BBChemSaltFormStructure>();
					for(Map.Entry<? extends AbstractBBChemStructure, Boolean> v : bbchemSubstructureMatchMap.entrySet()) {
						if(v.getValue()) {
							bbchemMatchStructures.add((BBChemSaltFormStructure) v.getKey());
						}
					}
					bbChemStructures = bbchemMatchStructures;
				} else {
					throw new CmpdRegMolFormatException("Structure type not implemented for BBChem searches " + structureType);
				}

			} else {
				throw new CmpdRegMolFormatException("Structure type not implemented for BBChem searches " + structureType);
			}

		} else {
			throw new CmpdRegMolFormatException("Search type not implemented for BBChem searches on " + searchType);
		}
		
		return(bbChemStructures);
	}

	@Override
	public CmpdRegMolecule[] searchMols(String molfile, StructureType structureType, int[] inputCdIdHitList, SearchType searchType, Float simlarityPercent, int maxResults)
			throws CmpdRegMolFormatException {
		
			List<? extends AbstractBBChemStructure>  bbChemStructures = searchBBChemStructures(molfile, structureType, inputCdIdHitList, searchType, simlarityPercent, maxResults);

			List<CmpdRegMolecule> resultList = new ArrayList<CmpdRegMolecule>();
			for (AbstractBBChemStructure hit : bbChemStructures) {
				CmpdRegMoleculeBBChemImpl molecule = new CmpdRegMoleculeBBChemImpl(hit.getMol(), bbChemStructureService);
				molecule.setProperty("cd_id", String.valueOf(hit.getId()));
				resultList.add(molecule);
			}
			CmpdRegMolecule[] resultArray = new CmpdRegMolecule[resultList.size()];
			
			return resultList.toArray(resultArray);
	}


	@Override
	public int[] searchMolStructures(String molfile, StructureType structureType, SearchType searchType) throws CmpdRegMolFormatException {
		return searchMolStructures(molfile, structureType, searchType, -1F, -1);

	}

	@Override
	public int[] searchMolStructures(String molfile, StructureType structureType, SearchType searchType, Float simlarityPercent) throws CmpdRegMolFormatException {
		return searchMolStructures(molfile, structureType, searchType, simlarityPercent, -1);	
	}

	@Override
	public int[] searchMolStructures(String molfile, StructureType structureType, SearchType searchType, Float simlarityPercent, int maxResults) throws CmpdRegMolFormatException {

		List<? extends AbstractBBChemStructure> bbChemStructures = searchBBChemStructures(molfile, structureType,  new int[0] , searchType, simlarityPercent, maxResults);

		// Create empty int hit list
		int[] hits = new int[bbChemStructures.size()];
		for (int i = 0; i < hits.length; i++) {
			hits[i] = toIntExact(bbChemStructures.get(i).getId());
		}
		return hits;
	}

	@Override
	public int saveStructure(String molfile, StructureType structureType) {
		boolean checkForDupes = false;
		return saveStructure(molfile, structureType, checkForDupes);
	}

	@Override
	public void closeConnection() {
		// Not used for this implementation
	}

	public String getBBChemStructureTableNameFromStructureType(StructureType structureType) {
		String plainTable = null;
		if (structureType == StructureType.PARENT){
			plainTable = "bbchem_parent_structure";
		} else if (structureType == StructureType.SALT_FORM){
			plainTable = "bbchem_salt_form_structure";
		} else if (structureType == StructureType.SALT){
			plainTable = "bbchem_salt_structure";
		} else if (structureType == StructureType.DRY_RUN){
			plainTable = "bbchem_dry_run_structure";
		}
		return(plainTable);
	}

	@Override
	@Transactional
	public boolean truncateStructureTable(StructureType structureType) {
		String truncateStatement = "TRUNCATE TABLE " + getBBChemStructureTableNameFromStructureType(structureType);
		BBChemParentStructure.entityManager().createNativeQuery(truncateStatement).executeUpdate();
		return true;
	}

	@Override
	public int saveStructure(CmpdRegMolecule cmpdRegMolecule, StructureType structureType, boolean checkForDupes) {
			// Process the molfile and calculate fingerprints = true
			BBChemParentStructure bbChemStructure = cmpdRegMolecule.molecule;


	}

	@Override
	public int saveStructure(String molfile, StructureType structureType, boolean checkForDupes) {

		Long cdId=0L;
		try {
			// Process the molfile and calculate fingerprints = true
			BBChemParentStructure bbChemStructure = bbChemStructureService.getProcessedStructure(molfile, true);

			if (structureType == StructureType.PARENT){
				if(checkForDupes){
					List<BBChemParentStructure> bbChemStructures =  BBChemParentStructure.findBBChemParentStructuresByRegEquals(bbChemStructure.getReg()).getResultList();
					if(bbChemStructures.size() > 0){
						logger.error("BBChem structure for " + structureType + " type already exists with id "+ bbChemStructures.get(0).getId());
						return 0;
					}
				}
				bbChemStructure.persist();
				cdId = bbChemStructure.getId();
			} else if (structureType == StructureType.SALT_FORM){
				BBChemSaltFormStructure bbChemSaltFormStructure = new BBChemSaltFormStructure();
				bbChemSaltFormStructure.updateStructureInfo(bbChemStructure);

				if(checkForDupes){
					List<BBChemSaltFormStructure> bbChemStructures =  BBChemSaltFormStructure.findBBChemSaltFormStructuresByRegEquals(bbChemSaltFormStructure.getReg()).getResultList();
					if(bbChemStructures.size() > 0){
						logger.error("Salt form structure already exists with id "+ bbChemStructures.get(0).getId());
						return 0;
					}
				}
				bbChemSaltFormStructure.persist();
				cdId = bbChemSaltFormStructure.getId();
			} else if (structureType == StructureType.SALT){
				BBChemSaltStructure bbChemStructureSalt = new BBChemSaltStructure();
				bbChemStructureSalt.updateStructureInfo(bbChemStructure);

				if(checkForDupes){
					List<BBChemSaltStructure> bbChemStructures =  BBChemSaltStructure.findBBChemSaltStructuresByRegEquals(bbChemStructureSalt.getReg()).getResultList();
					if(bbChemStructures.size() > 0){
						logger.error("Salt structure already exists with id "+ bbChemStructures.get(0).getId());
						return 0;
					}
				}
				bbChemStructureSalt.persist();
				cdId = bbChemStructureSalt.getId();
			} else if (structureType == StructureType.DRY_RUN){
				// Can't type cast from subclass to superclass so we go to json and back
				BBChemDryRunStructure bbChemStructureDryRun = new BBChemDryRunStructure();
				bbChemStructureDryRun.updateStructureInfo(bbChemStructure);

				if(checkForDupes){
					List<BBChemDryRunStructure> bbChemStructures =  BBChemDryRunStructure.findBBChemDryRunStructuresByRegEquals(bbChemStructureDryRun.getReg()).getResultList();
					if(bbChemStructures.size() > 0){
						logger.error("DryRun structure already exists with id "+ bbChemStructures.get(0).getId());
						return 0;
					}
				}
				bbChemStructureDryRun.persist();
				cdId = bbChemStructureDryRun.getId();
			}

			return toIntExact(cdId);
		} catch (CmpdRegMolFormatException e) {
			logger.error("Error saving structure: " + e.getMessage());
			return -1;
		}
	}

	@Override
	public double getMolWeight(String molStructure) throws CmpdRegMolFormatException {
		// Calculates the average molecular weight of a molecule
		return bbChemStructureService.getProcessedStructure(molStructure, false).getAverageMolWeight();
	}

	@Override
	public CmpdRegMolecule toMolecule(String molStructure) throws CmpdRegMolFormatException {
		return new CmpdRegMoleculeBBChemImpl(molStructure, bbChemStructureService);
	}

	@Override
	public String toMolfile(String molStructure) throws CmpdRegMolFormatException {
		return bbChemStructureService.getProcessedStructure(molStructure, false).getMol();
	}

	@Override
	public String toSmiles(String molStructure) throws CmpdRegMolFormatException {
		return bbChemStructureService.getProcessedStructure(molStructure, false).getSmiles();
	}

	@Override
	public int[] checkDupeMol(String molStructure, StructureType structureType)
			throws CmpdRegMolFormatException {
		return searchMolStructures(molStructure, structureType, SearchType.DUPLICATE_TAUTOMER); 
	}

	@Override
	public String toInchi(String molStructure) {
		try {
			return bbChemStructureService.getProcessedStructure(molStructure, false).getInchi();
		} catch (CmpdRegMolFormatException e) {
			logger.error("Error calculating inchi: ", e);
			return null;
		}
	}

	@Override
	public boolean updateStructure(String molStructure, StructureType structureType, int cdId) {
		Long id= new Long(cdId);
		try {
			BBChemParentStructure bbChemStructureUpdated = bbChemStructureService.getProcessedStructure(molStructure, true);
			if (structureType == StructureType.PARENT){
				BBChemParentStructure bbChemStructureSaved = BBChemParentStructure.findBBChemParentStructure(id);
				if(bbChemStructureSaved == null) {
					return false;
				}
				bbChemStructureSaved.updateStructureInfo(bbChemStructureUpdated);
				bbChemStructureSaved.persist();
			} else if (structureType == StructureType.SALT_FORM){
				BBChemSaltFormStructure bbChemSaltFormStructureSaved = BBChemSaltFormStructure.findBBChemSaltFormStructure(id);
				if(bbChemSaltFormStructureSaved == null) {
					return false;
				}
				bbChemSaltFormStructureSaved.updateStructureInfo(bbChemStructureUpdated);
				bbChemSaltFormStructureSaved.persist();
			} else if (structureType == StructureType.SALT){
				BBChemSaltStructure bbChemSaltStructureSaved = BBChemSaltStructure.findBBChemSaltStructure(id);
				if(bbChemSaltStructureSaved == null) {
					return false;
				}
				bbChemSaltStructureSaved.updateStructureInfo(bbChemStructureUpdated);
				bbChemSaltStructureSaved.persist();
			} else if (structureType == StructureType.DRY_RUN){
				BBChemDryRunStructure bbChemDryRunStructureSaved = BBChemDryRunStructure.findBBChemDryRunStructure(id);
				if(bbChemDryRunStructureSaved == null) {
					return false;
				}
				bbChemDryRunStructureSaved.updateStructureInfo(bbChemStructureUpdated);
				bbChemDryRunStructureSaved.persist();
			}
		} catch (CmpdRegMolFormatException e) {
			logger.error("Error updating structure: ", e);
			return false;
		}
		return true;
	}

	@Override
	public String getMolFormula(String molStructure) throws CmpdRegMolFormatException {
		return bbChemStructureService.getProcessedStructure(molStructure, false).getMolecularFormula();
	}

	@Override
	public boolean checkForSalt(String molfile) throws CmpdRegMolFormatException {
		boolean foundNonCovalentSalt = false;
		// Get all fragments
		List<String> allFrags = bbChemStructureService.getMolFragments(molfile);
		if(allFrags.size() > 1.0) {
			foundNonCovalentSalt = true;
		}
		return foundNonCovalentSalt;
	}

	@Override
	public boolean updateStructure(CmpdRegMolecule mol, StructureType structureType, int cdId) {
		try {
			updateStructure(mol.getMolStructure(), structureType, cdId);
		} catch (CmpdRegMolFormatException e) {
			logger.error("Error updating structure: " + e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public double getExactMass(String molStructure) throws CmpdRegMolFormatException {
		// Processor doesn't return exact mass so we need to populate it and then return it
		BBChemParentStructure bbChemStructure = bbChemStructureService.getProcessedStructure(molStructure, false);
		return bbChemStructure.getExactMolWeight();
	}

	@Override
	public boolean deleteStructure(StructureType structureType, int cdId) {
		Long id = new Long(cdId);
		if (structureType == StructureType.PARENT){
			BBChemParentStructure bbChemStructure = BBChemParentStructure.findBBChemParentStructure(id);
			bbChemStructure.remove();
		} else if (structureType == StructureType.SALT_FORM){
			BBChemSaltFormStructure bbChemSaltFormStructure = BBChemSaltFormStructure.findBBChemSaltFormStructure(id);
			bbChemSaltFormStructure.remove();
		} else if (structureType == StructureType.SALT){
			BBChemSaltStructure bbChemSaltStructure = BBChemSaltStructure.findBBChemSaltStructure(id);
			bbChemSaltStructure.remove();
		} else if (structureType == StructureType.DRY_RUN){
			BBChemDryRunStructure bbChemDryRunStructure = BBChemDryRunStructure.findBBChemDryRunStructure(id);
			bbChemDryRunStructure.remove();
		}
		return true;
	}

	private String getServiceFormat(String format) {
		String serviceFormat = null;
		if (format == null || format.equalsIgnoreCase("mol")) {
			serviceFormat = "sdf";
		} else if (format.equalsIgnoreCase("sdf")) {
			serviceFormat = "sdf";
		} else if (format.equalsIgnoreCase("smi")) {
			serviceFormat = "smiles";
		}
		return serviceFormat;
	}

	@Override
	public MolConvertOutputDTO toFormat(String structure, String inputFormat, String outputFormat) throws IOException, CmpdRegMolFormatException {
		// Calls preprocessor URL and gets the standardized structure from the preprocessor URL
		MolConvertOutputDTO output = new MolConvertOutputDTO();

		// Read the preprocessor settings as json
		JsonNode jsonNode = bbChemStructureService.getPreprocessorSettings();

		// Extract the url to call
		JsonNode urlNode = jsonNode.get("converterURL");
		if(urlNode == null || urlNode.isNull()) {
			logger.error("Missing preprocessorSettings converterURL!!");
		}

		String url = urlNode.asText();

		// Create the request format
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode requestData = mapper.createObjectNode();

		requestData.put("input_format", getServiceFormat(inputFormat));
		requestData.put("output_format", getServiceFormat(outputFormat));

		ArrayNode inputsNode = mapper.createArrayNode();
		inputsNode.add(structure);
		requestData.put("inputs", inputsNode);		

		// Post to the service
		String postResponse = SimpleUtil.postRequestToExternalServer(url, requestData.toString(), logger);
		logger.info("Got response: "+ postResponse);

		// Parse the response json
		ObjectMapper responseMapper = new ObjectMapper();
		JsonNode responseNode = responseMapper.readTree(postResponse);
		output.setStructure(responseNode.get(0).asText());
		output.setFormat(outputFormat);
		String contentUrl = "TO DO: Download Link";
		output.setContentUrl(contentUrl);
		return output;
	}

	@Override
	public MolConvertOutputDTO cleanStructure(String structure, int dim, String opts)
			throws IOException, CmpdRegMolFormatException {
			throw new CmpdRegMolFormatException("Hydrogenization not implemented");
	}

	@Override
	public String hydrogenizeMol(String structure, String inputFormat, String method)
			throws IOException, CmpdRegMolFormatException {
			//Not implmented
			throw new CmpdRegMolFormatException("Hydrogenization not implemented");
	}

	@Override
	public String getCipStereo(String structure) throws IOException, CmpdRegMolFormatException {
		//Jchem implementation returns something like
		//e.g. [TH: 1{0,3,2} S]
		//Indigo implementaiton returns the mol file after running some Indigo function
		//No implementing this function because its not used anywhere that I can find
		//Perhaps it was used as a utility function at somepoint
		throw new CmpdRegMolFormatException("Hydrogenization not implemented");

	}

	@Override
	public StrippedSaltDTO stripSalts(CmpdRegMolecule inputStructure) throws CmpdRegMolFormatException {

		// Get all fragments
		List<String> allFrags = bbChemStructureService.getMolFragments(inputStructure.getMolStructure());
	
		// Loop through the fragments and search for salts that match
		// If a fragment matches, add it to the salt counts
		// If a fragment doesn't match, then add it to the unidentified fragments
		Map<Salt, Integer> saltCounts = new HashMap<Salt, Integer>();
		Set<CmpdRegMoleculeBBChemImpl> unidentifiedFragments = new HashSet<CmpdRegMoleculeBBChemImpl>();
		for (String fragment : allFrags){
			int[] cdIdMatches = searchMolStructures(fragment, StructureType.SALT, SearchType.DUPLICATE_TAUTOMER);
			if (cdIdMatches.length>0){
				Salt foundSalt = Salt.findSaltsByCdId(cdIdMatches[0]).getSingleResult();
				if (saltCounts.containsKey(foundSalt)) saltCounts.put(foundSalt, saltCounts.get(foundSalt)+1);
				else saltCounts.put(foundSalt, 1);
			}else{
				CmpdRegMoleculeBBChemImpl fragWrapper = new CmpdRegMoleculeBBChemImpl(fragment, bbChemStructureService);
				unidentifiedFragments.add(fragWrapper);
			}
		}

		// Add the unidentified fragments and identified salts to the output
		StrippedSaltDTO resultDTO = new StrippedSaltDTO();
		resultDTO.setSaltCounts(saltCounts);
		resultDTO.setUnidentifiedFragments(unidentifiedFragments);

		// Some debug lines to prnt identified salts
		logger.debug("Identified stripped salts:");
		for (Salt salt : saltCounts.keySet()){
			logger.debug("Salt Abbrev: "+salt.getAbbrev());
			logger.debug("Salt Count: "+ saltCounts.get(salt));
		}
		return resultDTO;
	}

	@Override
	public HashMap<String, CmpdRegMolecule> standardizeStructures(HashMap<String, String> structures) throws CmpdRegMolFormatException {

		// Get preprocessed structures (standardized mol structures)
		HashMap<String, String> preprocessedStructures = new HashMap<String, String>();
		try {
			preprocessedStructures = bbChemStructureService.getPreprocessedStructures(structures);
		} catch (IOException e) {
			logger.error("Error getting preprocessed structures: "+e.getMessage());
			throw new CmpdRegMolFormatException("Error getting preprocessed structures: "+e.getMessage());
		}

		// Get processed structures (hashes and include fingerprints)
		HashMap<String, BBChemParentStructure> standardizedStructures = bbChemStructureService.getProcessedStructures(preprocessedStructures, true);

		// Return hashmap
		HashMap<String, CmpdRegMolecule> result = new HashMap<String, CmpdRegMolecule>();
		for(String key : standardizedStructures.keySet()){
			BBChemParentStructure bbchemStructure = standardizedStructures.get(key);
			CmpdRegMolecule molStructure = new CmpdRegMoleculeBBChemImpl(bbchemStructure);
			result.put(key, molStructure);
		}
		return result;
	} 

	@Override
	public String standardizeStructure(String molfile) throws CmpdRegMolFormatException, IOException {
		
		// Create input hashmap
		HashMap<String, String> inputStructures = new HashMap<String, String>();
		inputStructures.put("molfile", molfile);
		
		// Call the service
		HashMap<String, CmpdRegMolecule>  standardizedStructureHashMap = standardizeStructures(inputStructures);

		// Get the standardized structure
		CmpdRegMolecule standardizedMolecule = standardizedStructureHashMap.get("molfile");

		return standardizedMolecule.getMolStructure();
	}

	@Override
	public boolean compareStructures(String preMolStruct, String postMolStruct, SearchType searchType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean standardizedMolCompare(String queryMol, String targetMol) throws CmpdRegMolFormatException {
		try {
			BBChemParentStructure queryStructure = bbChemStructureService.getProcessedStructure(queryMol, false);
			BBChemParentStructure targetStructure = bbChemStructureService.getProcessedStructure(targetMol, false);
			return queryStructure.getReg() == targetStructure.getReg();
		} catch (Exception e) {
			logger.error("Error in standardizedMolCompare: ", e);
			throw new CmpdRegMolFormatException(e);
		}
	}

	@Override
	public boolean isEmpty(String molFile) throws CmpdRegMolFormatException {
		// Response from service looks like this if the mol is empty:
		// [
		// 	{
		// 		"error_code": "4004",
		// 		"error_msg": "No atoms present"
		// 	}
		// ]
		try{
			JsonNode responseNode = bbChemStructureService.postToProcessService(molFile);
			JsonNode errorCodeNode = responseNode.get(0).get("error_code");
			if( errorCodeNode != null && errorCodeNode.getTextValue().equals("4004")){
				return true;
			} else {
				return false;
			}

		} catch (IOException e) {
			logger.error("Error in isEmpty: ", e);
			throw new CmpdRegMolFormatException(e);
		}
	}

	@Override
	public StandardizerSettingsConfigDTO getStandardizerSettings() throws StandardizerException{
		// Read the preprocessor settings as json
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		try{
			jsonNode = bbChemStructureService.getPreprocessorSettings().get("standardizer_actions");
		} catch (IOException e) {
			logger.error("Error parsing preprocessor settings json", e);
			throw new StandardizerException("Error parsing preprocessor settings json");
		}

		StandardizerSettingsConfigDTO standardizationConfigDTO = new StandardizerSettingsConfigDTO();
		standardizationConfigDTO.setSettings(jsonNode.toString());
		standardizationConfigDTO.setType("bbchem");
		standardizationConfigDTO.setShouldStandardize(true);
		return standardizationConfigDTO;

	}

}

