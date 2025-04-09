package com.labsynch.labseer.chemclasses.bbchem;

import static java.lang.Math.toIntExact;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule.RegistrationStatus;
import com.labsynch.labseer.domain.AbstractBBChemStructure;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.domain.SaltForm;
import com.labsynch.labseer.domain.StandardizationDryRunCompound;
import com.labsynch.labseer.domain.StandardizationDryRunCompound.SyncStatus;
import com.labsynch.labseer.domain.StandardizationHistory;
import com.labsynch.labseer.dto.MolConvertOutputDTO;
import com.labsynch.labseer.dto.StandardizationSettingsConfigCheckResponseDTO;
import com.labsynch.labseer.dto.StrippedSaltDTO;
import com.labsynch.labseer.dto.configuration.StandardizerSettingsConfigDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.StandardizerException;
import com.labsynch.labseer.service.ChemStructureService;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
	public CmpdRegMolecule[] searchMols(String molfile, StructureType structureType, int[] cdHitList,
			SearchType searchType, Float simlarityPercent) throws CmpdRegMolFormatException {
		int maxResults = propertiesUtilService.getMaxSearchResults();
		return searchMols(molfile, structureType, cdHitList, searchType, simlarityPercent, maxResults);
	}

	private List<? extends AbstractBBChemStructure> searchBBChemStructures(String molfile, StructureType structureType,
			int[] inputCdIdHitList, SearchType searchType, Float simlarityPercent, int maxResults)
			throws CmpdRegMolFormatException {

		BBChemParentStructure serviceBBChemStructure = null;
		if (searchType == SearchType.FULL_TAUTOMER | searchType == SearchType.DUPLICATE_TAUTOMER
				| searchType == SearchType.EXACT) {
			// We don't need to do fingerprint matching for these searches so pass false
			// here
			serviceBBChemStructure = bbChemStructureService.getProcessedStructure(molfile, false, false);
		} else {
			// We need to do fingerprint matching for these searches so pass true here
			serviceBBChemStructure = bbChemStructureService.getProcessedStructure(molfile, true, false);
		}
		if (serviceBBChemStructure.getRegistrationStatus() == RegistrationStatus.ERROR) {
			throw new CmpdRegMolFormatException(serviceBBChemStructure.getRegistrationComment());
		}
		return searchBBChemStructures(serviceBBChemStructure, structureType, inputCdIdHitList, searchType,
				simlarityPercent, maxResults);
	}

	private List<? extends AbstractBBChemStructure> searchBBChemStructures(BBChemParentStructure bbchemStructure,
			StructureType structureType, int[] inputCdIdHitList, SearchType searchType, Float simlarityPercent,
			int maxResults) throws CmpdRegMolFormatException {

		// Create empty list
		List<? extends AbstractBBChemStructure> bbChemStructures = new ArrayList<AbstractBBChemStructure>();

		if (searchType == SearchType.FULL_TAUTOMER | searchType == SearchType.DUPLICATE_TAUTOMER
				| searchType == SearchType.EXACT) {
			// Don't need to calculate fingerprint if not doing similarity or substructure
			TypedQuery<? extends AbstractBBChemStructure> query;

			if (searchType == SearchType.FULL_TAUTOMER) {
				// FULL TAUTOMER hashes are stored on the pre reg hash column
				if (structureType == StructureType.PARENT) {
					query = BBChemParentStructure.findBBChemParentStructuresByPreRegEquals(bbchemStructure.getPreReg());
				} else if (structureType == StructureType.SALT) {
					query = BBChemSaltStructure.findBBChemSaltStructuresByPreRegEquals(bbchemStructure.getPreReg());
				} else if (structureType == StructureType.SALT_FORM) {
					query = BBChemSaltFormStructure
							.findBBChemSaltFormStructuresByPreRegEquals(bbchemStructure.getPreReg());
				} else if (structureType == StructureType.DRY_RUN) {
					query = BBChemDryRunStructure.findBBChemDryRunStructuresByPreRegEquals(bbchemStructure.getPreReg());
				} else if (structureType == StructureType.STANDARDIZATION_DRY_RUN) {
					query = BBChemStandardizationDryRunStructure
							.findBBChemStandardizationDryRunStructuresByPreRegEquals(bbchemStructure.getPreReg());
				} else {
					throw new CmpdRegMolFormatException(
							"Structure type not implemented for BBChem searches on " + structureType);
				}
			} else if (searchType == SearchType.DUPLICATE_TAUTOMER | searchType == SearchType.EXACT) {
				// DUPLICATE TAUTOMER hashes are stored on the reg column
				if (structureType == StructureType.PARENT) {
					query = BBChemParentStructure.findBBChemParentStructuresByRegEquals(bbchemStructure.getReg());
				} else if (structureType == StructureType.SALT) {
					query = BBChemSaltStructure.findBBChemSaltStructuresByRegEquals(bbchemStructure.getReg());
				} else if (structureType == StructureType.SALT_FORM) {
					query = BBChemSaltFormStructure.findBBChemSaltFormStructuresByRegEquals(bbchemStructure.getReg());
				} else if (structureType == StructureType.DRY_RUN) {
					query = BBChemDryRunStructure.findBBChemDryRunStructuresByRegEquals(bbchemStructure.getReg());
				} else if (structureType == StructureType.STANDARDIZATION_DRY_RUN) {
					query = BBChemStandardizationDryRunStructure
							.findBBChemStandardizationDryRunStructuresByRegEquals(bbchemStructure.getReg());
				} else {
					throw new CmpdRegMolFormatException(
							"Structure type not implemented for BBChem searches on " + structureType);
				}
			} else {
				throw new CmpdRegMolFormatException(
						"Structure type not implemented for BBChem searches on " + structureType);
			}

			// int is not nullable so you either have a valid maxResults passed in
			// or -1 to express unlimited
			if (maxResults > -1) {
				query.setMaxResults(maxResults);
			}

			bbChemStructures = query.getResultList();

		} else if (searchType == SearchType.SUBSTRUCTURE | searchType == SearchType.SIMILARITY) {
			if (searchType == SearchType.SUBSTRUCTURE) {
				if (structureType == StructureType.PARENT) {
					// Get basic fingerprint substructure matchs
					List<BBChemParentStructure> fingerprintMatchBBChemStructures = BBChemParentStructure
							.findBBChemParentStructuresBySubstructure(bbchemStructure.getSubstructure(), maxResults);

					// Narrow list down to those that match a true subgraph substructure search
					HashMap<? extends AbstractBBChemStructure, Boolean> bbchemSubstructureMatchMap = bbChemStructureService
							.substructureMatch(bbchemStructure.getMol(), fingerprintMatchBBChemStructures);

					// Loop through the llist of all and only keep those which have a true match
					List<BBChemParentStructure> bbchemMatchStructures = new ArrayList<BBChemParentStructure>();
					for (Map.Entry<? extends AbstractBBChemStructure, Boolean> v : bbchemSubstructureMatchMap
							.entrySet()) {
						if (v.getValue()) {
							bbchemMatchStructures.add((BBChemParentStructure) v.getKey());
						}
					}
					bbChemStructures = bbchemMatchStructures;
				} else if (structureType == StructureType.SALT_FORM) {
					// Get basic fingerprint substructure matchs
					List<BBChemSaltFormStructure> fingerprintMatchBBChemStructures = BBChemSaltFormStructure
							.findBBChemSaltFormStructuresBySubstructure(bbchemStructure.getSubstructure(), maxResults);

					// Narrow list down to those that match a true subgraph substructure search
					HashMap<? extends AbstractBBChemStructure, Boolean> bbchemSubstructureMatchMap = bbChemStructureService
							.substructureMatch(bbchemStructure.getMol(), fingerprintMatchBBChemStructures);

					// Loop through the llist of all and only keep those which have a true match
					List<BBChemSaltFormStructure> bbchemMatchStructures = new ArrayList<BBChemSaltFormStructure>();
					for (Map.Entry<? extends AbstractBBChemStructure, Boolean> v : bbchemSubstructureMatchMap
							.entrySet()) {
						if (v.getValue()) {
							bbchemMatchStructures.add((BBChemSaltFormStructure) v.getKey());
						}
					}
					bbChemStructures = bbchemMatchStructures;
				} else {
					throw new CmpdRegMolFormatException(
							"Structure type not implemented for BBChem searches " + structureType);
				}

			} else {
				throw new CmpdRegMolFormatException(
						"Structure type not implemented for BBChem searches " + structureType);
			}

		} else {
			throw new CmpdRegMolFormatException("Search type not implemented for BBChem searches on " + searchType);
		}

		return (bbChemStructures);
	}

	@Override
	public CmpdRegMolecule[] searchMols(String molfile, StructureType structureType, int[] inputCdIdHitList,
			SearchType searchType, Float simlarityPercent, int maxResults)
			throws CmpdRegMolFormatException {

		List<? extends AbstractBBChemStructure> bbChemStructures = searchBBChemStructures(molfile, structureType,
				inputCdIdHitList, searchType, simlarityPercent, maxResults);

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
	public int[] searchMolStructures(String molfile, StructureType structureType, SearchType searchType)
			throws CmpdRegMolFormatException {
		return searchMolStructures(molfile, structureType, searchType, -1F, -1);

	}

	@Override
	public int[] searchMolStructures(String molfile, StructureType structureType, SearchType searchType,
			Float simlarityPercent) throws CmpdRegMolFormatException {
		return searchMolStructures(molfile, structureType, searchType, simlarityPercent, -1);
	}

	@Override
	public int[] searchMolStructures(String molfile, StructureType structureType, SearchType searchType,
			Float simlarityPercent, int maxResults) throws CmpdRegMolFormatException {

		List<? extends AbstractBBChemStructure> bbChemStructures = searchBBChemStructures(molfile, structureType,
				new int[0], searchType, simlarityPercent, maxResults);

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
		if (structureType == StructureType.PARENT) {
			plainTable = "bbchem_parent_structure";
		} else if (structureType == StructureType.SALT_FORM) {
			plainTable = "bbchem_salt_form_structure";
		} else if (structureType == StructureType.SALT) {
			plainTable = "bbchem_salt_structure";
		} else if (structureType == StructureType.DRY_RUN) {
			plainTable = "bbchem_dry_run_structure";
		} else if (structureType == StructureType.STANDARDIZATION_DRY_RUN) {
			plainTable = "bbchem_standardization_dry_run_structure";
		}
		return (plainTable);
	}

	@Override
	@Transactional
	public boolean truncateStructureTable(StructureType structureType) {
		String truncateStatement = "TRUNCATE TABLE " + getBBChemStructureTableNameFromStructureType(structureType);
		BBChemParentStructure.entityManager().createNativeQuery(truncateStatement).executeUpdate();
		return true;
	}

	private int saveStructure(BBChemParentStructure bbChemStructure, StructureType structureType,
			boolean checkForDupes) {
		Long cdId = 0L;
		try {
			if (bbChemStructure.getRegistrationStatus() != RegistrationStatus.ERROR
					&& (bbChemStructure.getReg() == null || bbChemStructure.getSubstructure() == null)) {
				logger.info(
						"Reg or Substructure is null for bbchem structure so calling processStructure to generate them before saving");
				bbChemStructure = bbChemStructureService.getProcessedStructure(bbChemStructure.getMol(), true, false);
			}

			if (structureType == StructureType.PARENT) {
				if (checkForDupes) {
					List<BBChemParentStructure> bbChemStructures = BBChemParentStructure
							.findBBChemParentStructuresByRegEquals(bbChemStructure.getReg()).getResultList();
					if (bbChemStructures.size() > 0) {
						logger.error("BBChem structure for " + structureType + " type already exists with id "
								+ bbChemStructures.get(0).getId());
						return 0;
					}
				}
				bbChemStructure.persist();
				cdId = bbChemStructure.getId();
			} else if (structureType == StructureType.SALT_FORM) {
				BBChemSaltFormStructure bbChemSaltFormStructure = new BBChemSaltFormStructure();
				bbChemSaltFormStructure.updateStructureInfo(bbChemStructure);

				if (checkForDupes) {
					List<BBChemSaltFormStructure> bbChemStructures = BBChemSaltFormStructure
							.findBBChemSaltFormStructuresByRegEquals(bbChemSaltFormStructure.getReg()).getResultList();
					if (bbChemStructures.size() > 0) {
						logger.error("Salt form structure already exists with id " + bbChemStructures.get(0).getId());
						return 0;
					}
				}
				bbChemSaltFormStructure.persist();
				cdId = bbChemSaltFormStructure.getId();
			} else if (structureType == StructureType.SALT) {
				BBChemSaltStructure bbChemStructureSalt = new BBChemSaltStructure();
				bbChemStructureSalt.updateStructureInfo(bbChemStructure);

				if (checkForDupes) {
					List<BBChemSaltStructure> bbChemStructures = BBChemSaltStructure
							.findBBChemSaltStructuresByRegEquals(bbChemStructureSalt.getReg()).getResultList();
					if (bbChemStructures.size() > 0) {
						logger.error("Salt structure already exists with id " + bbChemStructures.get(0).getId());
						return 0;
					}
				}
				bbChemStructureSalt.persist();
				cdId = bbChemStructureSalt.getId();
			} else if (structureType == StructureType.DRY_RUN) {
				// Can't type cast from subclass to superclass so we go to json and back
				BBChemDryRunStructure bbChemStructureDryRun = new BBChemDryRunStructure();
				bbChemStructureDryRun.updateStructureInfo(bbChemStructure);

				if (checkForDupes) {
					List<BBChemDryRunStructure> bbChemStructures = BBChemDryRunStructure
							.findBBChemDryRunStructuresByRegEquals(bbChemStructureDryRun.getReg()).getResultList();
					if (bbChemStructures.size() > 0) {
						logger.error("DryRun structure already exists with id " + bbChemStructures.get(0).getId());
						return 0;
					}
				}
				bbChemStructureDryRun.persist();
				cdId = bbChemStructureDryRun.getId();
			} else if (structureType == StructureType.STANDARDIZATION_DRY_RUN) {
				// Can't type cast from subclass to superclass so we go to json and back
				BBChemStandardizationDryRunStructure bbChemStructureStandardizationDryRun = new BBChemStandardizationDryRunStructure();
				bbChemStructureStandardizationDryRun.updateStructureInfo(bbChemStructure);

				if (checkForDupes) {
					List<BBChemStandardizationDryRunStructure> bbChemStructures = BBChemStandardizationDryRunStructure
							.findBBChemStandardizationDryRunStructuresByRegEquals(
									bbChemStructureStandardizationDryRun.getReg())
							.getResultList();
					if (bbChemStructures.size() > 0) {
						logger.error("StandardizationDryRun structure already exists with id "
								+ bbChemStructures.get(0).getId());
						return 0;
					}
				}
				bbChemStructureStandardizationDryRun.persist();
				cdId = bbChemStructureStandardizationDryRun.getId();
			}
			logger.debug("Saved structure with id " + cdId);
			return toIntExact(cdId);
		} catch (CmpdRegMolFormatException e) {
			logger.error("Error saving structure: " + e.getMessage());
			return -1;
		}
	}

	@Override
	public int saveStructure(CmpdRegMolecule cmpdRegMolecule, StructureType structureType, boolean checkForDupes) {
		// Process the molfile and calculate fingerprints = true
		CmpdRegMoleculeBBChemImpl bbchemCmpdRegMolecule = (CmpdRegMoleculeBBChemImpl) cmpdRegMolecule;
		BBChemParentStructure bbChemParentStructure = bbchemCmpdRegMolecule.getMolecule();

		// Save the structure
		return saveStructure(bbChemParentStructure, structureType, checkForDupes);
	}

	@Override
	public int saveStructure(String molfile, StructureType structureType, boolean checkForDupes) {

		try {
			// Process the molfile and calculate fingerprints = true
			BBChemParentStructure bbChemStructure = bbChemStructureService.getProcessedStructure(molfile, true, false);
			// Save the structure
			return saveStructure(bbChemStructure, structureType, checkForDupes);
		} catch (CmpdRegMolFormatException e) {
			logger.error("Error processing molfile: " + e.getMessage());
			return -1;
		}
	}

	@Override
	public double getMolWeight(String molStructure) throws CmpdRegMolFormatException {
		// Calculates the average molecular weight of a molecule
		return bbChemStructureService.getProcessedStructure(molStructure, false, false).getAverageMolWeight();
	}

	@Override
	public CmpdRegMolecule toMolecule(String molStructure) throws CmpdRegMolFormatException {
		return new CmpdRegMoleculeBBChemImpl(molStructure, bbChemStructureService);
	}

	@Override
	public String toMolfile(String molStructure) throws CmpdRegMolFormatException {
		return bbChemStructureService.getProcessedStructure(molStructure, false, false).getMol();
	}

	@Override
	public String toSmiles(String molStructure) throws CmpdRegMolFormatException {
		return bbChemStructureService.getProcessedStructure(molStructure, false, false).getSmiles();
	}

	@Override
	public int[] checkDupeMol(String molStructure, StructureType structureType)
			throws CmpdRegMolFormatException {
		return searchMolStructures(molStructure, structureType, SearchType.DUPLICATE_TAUTOMER);
	}

	@Override
	public String toInchi(String molStructure) {
		try {
			return bbChemStructureService.getProcessedStructure(molStructure, false, false).getInchi();
		} catch (CmpdRegMolFormatException e) {
			logger.error("Error calculating inchi: ", e);
			return null;
		}
	}

	@Override
	public boolean updateStructure(String molStructure, StructureType structureType, int cdId) {
		try {
			BBChemParentStructure bbChemStructure = bbChemStructureService.getProcessedStructure(molStructure, true, false);
			return updateStructure(bbChemStructure, structureType, cdId);
		} catch (CmpdRegMolFormatException e) {
			logger.error("Error processing molfile: ");
			return false;
		}
	}

	private Boolean updateStructure(BBChemParentStructure bbChemStructure, StructureType structureType, int cdId) {
		Long id = new Long(cdId);
		if (bbChemStructure.getRegistrationStatus() != RegistrationStatus.ERROR
				&& (bbChemStructure.getReg() == null || bbChemStructure.getSubstructure() == null)) {
			logger.info(
					"Reg or Substructure is null for bbchem structure so calling processStructure to generate them before saving");
			try {
				bbChemStructure = bbChemStructureService.getProcessedStructure(bbChemStructure.getMol(), true, false);
			} catch (CmpdRegMolFormatException e) {
				logger.error("Error processing molfile: " + e.getMessage());
				return false;
			}
		}
		if (structureType == StructureType.PARENT) {
			BBChemParentStructure bbChemStructureSaved = BBChemParentStructure.findBBChemParentStructure(id);
			if (bbChemStructureSaved == null) {
				return false;
			}
			bbChemStructureSaved.updateStructureInfo(bbChemStructure);
			bbChemStructureSaved.persist();
		} else if (structureType == StructureType.SALT_FORM) {
			BBChemSaltFormStructure bbChemSaltFormStructureSaved = BBChemSaltFormStructure
					.findBBChemSaltFormStructure(id);
			if (bbChemSaltFormStructureSaved == null) {
				return false;
			}
			bbChemSaltFormStructureSaved.updateStructureInfo(bbChemStructure);
			bbChemSaltFormStructureSaved.persist();
		} else if (structureType == StructureType.SALT) {
			BBChemSaltStructure bbChemSaltStructureSaved = BBChemSaltStructure.findBBChemSaltStructure(id);
			if (bbChemSaltStructureSaved == null) {
				return false;
			}
			bbChemSaltStructureSaved.updateStructureInfo(bbChemStructure);
			bbChemSaltStructureSaved.persist();
		} else if (structureType == StructureType.DRY_RUN) {
			BBChemDryRunStructure bbChemDryRunStructureSaved = BBChemDryRunStructure.findBBChemDryRunStructure(id);
			if (bbChemDryRunStructureSaved == null) {
				return false;
			}
			bbChemDryRunStructureSaved.updateStructureInfo(bbChemStructure);
			bbChemDryRunStructureSaved.persist();
		} else if (structureType == StructureType.STANDARDIZATION_DRY_RUN) {
			BBChemStandardizationDryRunStructure bbChemStandardizationDryRunStructureSaved = BBChemStandardizationDryRunStructure
					.findBBChemStandardizationDryRunStructure(id);
			if (bbChemStandardizationDryRunStructureSaved == null) {
				return false;
			}
			bbChemStandardizationDryRunStructureSaved.updateStructureInfo(bbChemStructure);
			bbChemStandardizationDryRunStructureSaved.persist();
		}
		return true;
	}

	@Override
	public String getMolFormula(String molStructure) throws CmpdRegMolFormatException {
		return bbChemStructureService.getProcessedStructure(molStructure, false, false).getMolecularFormula();
	}

	@Override
	public boolean checkForSalt(String molfile) throws CmpdRegMolFormatException {
		boolean foundNonCovalentSalt = false;
		// Get all fragments
		List<String> allFrags = bbChemStructureService.getMolFragments(Arrays.asList(molfile)).get(0);
		if (allFrags.size() > 1.0) {
			foundNonCovalentSalt = true;
		}
		return foundNonCovalentSalt;
	}

	@Override
	public List<Boolean> checkForSalts(Collection<String> molfiles) throws CmpdRegMolFormatException {
		List<Boolean> hasSaltList = new ArrayList<Boolean>();
		List<List<String>> fragmentsList = bbChemStructureService.getMolFragments(new ArrayList(molfiles));
		for (List<String> allFrags : fragmentsList) {
			if (allFrags.size() > 1.0) {
				hasSaltList.add(true);
			}
			else{
				hasSaltList.add(false);
			}
		}
		return hasSaltList;
	}

	@Override
	public boolean updateStructure(CmpdRegMolecule cmpdRegMolecule, StructureType structureType, int cdId) {
		BBChemParentStructure bbchemStructure = ((CmpdRegMoleculeBBChemImpl) cmpdRegMolecule).getMolecule();
		return updateStructure(bbchemStructure, structureType, cdId);
	}

	@Override
	public double getExactMass(String molStructure) throws CmpdRegMolFormatException {
		// Processor doesn't return exact mass so we need to populate it and then return
		// it
		BBChemParentStructure bbChemStructure = bbChemStructureService.getProcessedStructure(molStructure, false, false);
		return bbChemStructure.getExactMolWeight();
	}

	@Override
	public boolean deleteStructure(StructureType structureType, int cdId) {
		Long id = new Long(cdId);
		if (structureType == StructureType.PARENT) {
			BBChemParentStructure bbChemStructure = BBChemParentStructure.findBBChemParentStructure(id);
			if (bbChemStructure == null) {
				return false;
			}
			bbChemStructure.remove();
		} else if (structureType == StructureType.SALT_FORM) {
			BBChemSaltFormStructure bbChemSaltFormStructure = BBChemSaltFormStructure.findBBChemSaltFormStructure(id);
			if (bbChemSaltFormStructure == null) {
				return false;
			}
			bbChemSaltFormStructure.remove();
		} else if (structureType == StructureType.SALT) {
			BBChemSaltStructure bbChemSaltStructure = BBChemSaltStructure.findBBChemSaltStructure(id);
			if (bbChemSaltStructure == null) {
				return false;
			}
			bbChemSaltStructure.remove();
		} else if (structureType == StructureType.DRY_RUN) {
			BBChemDryRunStructure bbChemDryRunStructure = BBChemDryRunStructure.findBBChemDryRunStructure(id);
			if (bbChemDryRunStructure == null) {
				return false;
			}
			bbChemDryRunStructure.remove();
		} else if (structureType == StructureType.STANDARDIZATION_DRY_RUN) {
			BBChemStandardizationDryRunStructure bbChemStandardizationDryRunStructure = BBChemStandardizationDryRunStructure
					.findBBChemStandardizationDryRunStructure(id);
			if (bbChemStandardizationDryRunStructure == null) {
				return false;
			}
			bbChemStandardizationDryRunStructure.remove();
		}
		return true;
	}

	@Override
	public MolConvertOutputDTO toFormat(String structure, String inputFormat, String outputFormat)
			throws IOException, CmpdRegMolFormatException {
		// Call bbchem to conver the structure to the structure to the output format
		MolConvertOutputDTO output = new MolConvertOutputDTO();

		output.setStructure(bbChemStructureService.convert(structure, inputFormat, outputFormat));
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
		// Not implmented
		throw new CmpdRegMolFormatException("Hydrogenization not implemented");
	}

	@Override
	public String getCipStereo(String structure) throws IOException, CmpdRegMolFormatException {
		// Jchem implementation returns something like
		// e.g. [TH: 1{0,3,2} S]
		// Indigo implementaiton returns the mol file after running some Indigo function
		// No implementing this function because its not used anywhere that I can find
		// Perhaps it was used as a utility function at somepoint
		throw new CmpdRegMolFormatException("Hydrogenization not implemented");

	}

	@Override
	public StrippedSaltDTO stripSalts(CmpdRegMolecule inputStructure) throws CmpdRegMolFormatException {

		// Get all fragments
		List<String> allFrags = bbChemStructureService.getMolFragments(Arrays.asList(inputStructure.getMolStructure())).get(0);

		// Loop through the fragments and search for salts that match
		// If a fragment matches, add it to the salt counts
		// If a fragment doesn't match, then add it to the unidentified fragments
		Map<Salt, Integer> saltCounts = new HashMap<Salt, Integer>();
		Set<CmpdRegMoleculeBBChemImpl> unidentifiedFragments = new HashSet<CmpdRegMoleculeBBChemImpl>();
		for (String fragment : allFrags) {
			int[] cdIdMatches = searchMolStructures(fragment, StructureType.SALT, SearchType.DUPLICATE_TAUTOMER);
			if (cdIdMatches.length > 0) {
				Salt foundSalt = Salt.findSaltsByCdId(cdIdMatches[0]).getSingleResult();
				if (saltCounts.containsKey(foundSalt))
					saltCounts.put(foundSalt, saltCounts.get(foundSalt) + 1);
				else
					saltCounts.put(foundSalt, 1);
			} else {
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
		for (Salt salt : saltCounts.keySet()) {
			logger.debug("Salt Abbrev: " + salt.getAbbrev());
			logger.debug("Salt Count: " + saltCounts.get(salt));
		}
		return resultDTO;
	}

	@Override
	public HashMap<String, CmpdRegMolecule> standardizeStructures(HashMap<String, String> structures)
			throws CmpdRegMolFormatException {

		// Get processed structures (hashes and include fingerprints)
		HashMap<String, BBChemParentStructure> standardizedStructures = bbChemStructureService
				.getProcessedStructures(structures, true, false);

		// Return hashmap
		HashMap<String, CmpdRegMolecule> result = new HashMap<String, CmpdRegMolecule>();
		for (String key : standardizedStructures.keySet()) {
			BBChemParentStructure bbchemStructure = standardizedStructures.get(key);
			CmpdRegMolecule molStructure = new CmpdRegMoleculeBBChemImpl(bbchemStructure, bbChemStructureService);
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
		HashMap<String, CmpdRegMolecule> standardizedStructureHashMap = standardizeStructures(inputStructures);

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
			BBChemParentStructure queryStructure = bbChemStructureService.getProcessedStructure(queryMol, false, false);
			BBChemParentStructure targetStructure = bbChemStructureService.getProcessedStructure(targetMol, false, false);
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
		// {
		// "error_code": "4004",
		// "error_msg": "No atoms present"
		// }
		// ]
		try {
			JsonNode responseNode = bbChemStructureService.postToProcessService(molFile, false);
			JsonNode errorCodeNode = responseNode.get(0).get("error_code");
			if (errorCodeNode != null && errorCodeNode.asText().equals("4004")) {
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
	public StandardizerSettingsConfigDTO getStandardizerSettings(Boolean appliedSettings) throws StandardizerException {
		// If appliedSettings is true, then get the settings that are currently applied rather than the settings from the configuration file
		// This is applicable to bbchem because we want the fixed bbchem settings which are being applied during standardization which can be different than those
		// in the configuration file

		// Create new settings json node
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode settings = mapper.createObjectNode();

		try {
			JsonNode preprocessorSettings = bbChemStructureService.getPreprocessorSettings();

			// Get standardizer actions from the config file if appliedSettings is false
			if (appliedSettings) {
				settings.replace("standardizer_actions", propertiesUtilService.getStandardizerActions());

			} else {
				settings.replace("standardizer_actions", preprocessorSettings.get("standardizer_actions"));
			}

			// Hash Scheme from configs
			settings.put("hash_scheme", preprocessorSettings.get("process_options").get("hash_scheme").asText());

			// Call the health endpoint to get the version of preprocessor and schrodinger suite version we are standardizing with
			// This is used on upgrades of BBCHem to determine whether the system needs re-standardizing
			JsonNode bbchemHealth = bbChemStructureService.health();
			settings.put("schrodinger_suite_version", bbchemHealth.get("suite_version").asText());
			settings.put("preprocessor_version", bbchemHealth.get("preprocessor_version").asText());

		} catch (IOException e) {
			logger.error("Error parsing preprocessor settings json", e);
			throw new StandardizerException("Error parsing preprocessor settings json");
		}

		StandardizerSettingsConfigDTO standardizationConfigDTO = new StandardizerSettingsConfigDTO();
		standardizationConfigDTO.setSettings(settings.toString());
		standardizationConfigDTO.setType("bbchem");
		
		standardizationConfigDTO.setShouldStandardize(true);
		return standardizationConfigDTO;

	}

	@Override
	public HashMap<String, Integer> saveStructures(HashMap<String, CmpdRegMolecule> structures,
			StructureType structureType, Boolean checkForDupes) {
		// return hash
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		for (String key : structures.keySet()) {
			CmpdRegMolecule cmpdRegMolecule = structures.get(key);
			BBChemParentStructure bbchemStructure = ((CmpdRegMoleculeBBChemImpl) cmpdRegMolecule).getMolecule();
			result.put(key, saveStructure(bbchemStructure, structureType, checkForDupes));
		}
		return result;
	}

	@Override
	public HashMap<String, CmpdRegMolecule> getCmpdRegMolecules(HashMap<String, Integer> keyIdToStructureId,
			StructureType structureType) throws CmpdRegMolFormatException {

		// For each structure, get the mol structure
		HashMap<String, CmpdRegMolecule> result = new HashMap<String, CmpdRegMolecule>();
		for (String key : keyIdToStructureId.keySet()) {
			Integer structureId = keyIdToStructureId.get(key);
			AbstractBBChemStructure structure = null;
			if (structureType == StructureType.PARENT) {
				structure = BBChemParentStructure.findBBChemParentStructure(structureId.longValue());
			} else if (structureType == StructureType.SALT) {
				structure = BBChemSaltStructure.findBBChemSaltStructure(structureId.longValue());
			} else if (structureType == StructureType.SALT_FORM) {
				structure = BBChemSaltFormStructure.findBBChemSaltFormStructure(structureId.longValue());
			} else if (structureType == StructureType.DRY_RUN) {
				structure = BBChemDryRunStructure.findBBChemDryRunStructure(structureId.longValue());
			} else if (structureType == StructureType.STANDARDIZATION_DRY_RUN) {
				structure = BBChemStandardizationDryRunStructure
						.findBBChemStandardizationDryRunStructure(structureId.longValue());
			}
			BBChemParentStructure bbchemParentStructure = new BBChemParentStructure();
			bbchemParentStructure.updateStructureInfo(structure);
			result.put(key, new CmpdRegMoleculeBBChemImpl(bbchemParentStructure, bbChemStructureService));
		}
		return result;
	}

	@Override
	public int[] searchMolStructures(CmpdRegMolecule cmpdRegMolecule, StructureType structureType,
			SearchType searchType, Float simlarityPercent, int maxResults) throws CmpdRegMolFormatException {

		// Get the mol structure
		CmpdRegMoleculeBBChemImpl bbChemCmpdRegMolecule = (CmpdRegMoleculeBBChemImpl) cmpdRegMolecule;
		BBChemParentStructure structure = bbChemCmpdRegMolecule.getMolecule();

		// Search for the structure
		List<? extends AbstractBBChemStructure> bbChemStructures = searchBBChemStructures(structure, structureType,
				new int[0], searchType, simlarityPercent, maxResults);

		// Create empty int hit list
		int[] hits = new int[bbChemStructures.size()];
		for (int i = 0; i < hits.length; i++) {
			hits[i] = toIntExact(bbChemStructures.get(i).getId());
		}
		return hits;
	}

	List<BigInteger> getStructureTypeIdsMissingStructures(StructureType structureType) {

		EntityManager em = BBChemParentStructure.entityManager();
		Query q = em.createNativeQuery("SELECT p.id FROM " + structureType.name() + " p LEFT JOIN "
				+ getBBChemStructureTableNameFromStructureType(structureType)
				+ " b on p.cd_id=b.id where p.mol_structure is not null and p.mol_structure != '' and b.id is null");
		return q.getResultList();
	}

	public void fillMissingParentStructures() throws CmpdRegMolFormatException {
		// Parents
		List<BigInteger> missingIds = getStructureTypeIdsMissingStructures(StructureType.PARENT);
		if (missingIds.size() > 0) {
			logger.warn("Found " + missingIds.size() + " " + StructureType.PARENT
					+ " ids missing structure representations");
		} else {
			logger.info("No " + StructureType.PARENT + " ids missing structure representations");
			return;
		}

		int batchSize = propertiesUtilService.getStandardizationBatchSize();

		List<List<Long>> groups = SimpleUtil.splitIntArrayIntoGroups(missingIds, batchSize);
		Long startTime = new Date().getTime();
		Long currentTime = new Date().getTime();
		int totalCount = missingIds.size();
		float percent = 0;
		int p = 0;
		float previousPercent = percent;
		for (List<Long> group : groups) {
			// Fill missing parent structures in the group
			p = p + fillMissingParentStructuresByParentIDList(group);
			
			// Compute your percentage.
			percent = (float) Math.floor(p * 100f / totalCount);
			if (percent != previousPercent) {
				currentTime = new Date().getTime();
				// Output if different from the last time.
				logger.info("filling " + " " + StructureType.PARENT + " structure representations " + percent
						+ "% complete (" + p + " of "
						+ totalCount + ") average speed (rows/min):"
						+ (p / ((currentTime - startTime) / 60.0 / 1000.0)));
				currentTime = new Date().getTime();
				logger.debug("Time Elapsed:" + (currentTime - startTime));
			}
			// Update the percentage.
			previousPercent = percent;
		}
	}
	
	@Transactional
	public int fillMissingParentStructuresByParentIDList(List<Long> group) throws CmpdRegMolFormatException {
		int p = 0;
		Boolean checkForDupes = false;
		logger.info("Started fetching " + group.size() + " " + StructureType.PARENT + " molstructures to save");
		HashMap<String, String> structureIdParentMolStructureMap = new HashMap<String, String>();
		EntityManager em = BBChemParentStructure.entityManager();
		List<Parent> parents = em.createQuery("SELECT p FROM Parent p where id in (:ids)", Parent.class)
				.setParameter("ids", group)
				.getResultList();
		for (Parent parent : parents) {
			structureIdParentMolStructureMap.put(String.valueOf(parent.getCdId()), parent.getMolStructure());
		}
		logger.info("Finished fetching " + group.size() + " " + StructureType.PARENT + " molstructures to save");
		logger.info("Started processing " + structureIdParentMolStructureMap.size() + " parent structures");
		HashMap<String, BBChemParentStructure> processedStructures = bbChemStructureService
				.getProcessedStructures(structureIdParentMolStructureMap, true, false);
		logger.info("Finished processing " + structureIdParentMolStructureMap.size() + " " + StructureType.PARENT
				+ " structures");
		logger.info("Started saving " + processedStructures.size() + " " + StructureType.PARENT + " structures");
		// Loop through parents
		for (Parent parent : parents) {
			String originalCdId = String.valueOf(parent.getCdId());
			BBChemParentStructure processedParentStructure = processedStructures.get(originalCdId);
			int savedCdId = saveStructure(processedParentStructure, StructureType.PARENT, checkForDupes);
			parent.setCdId(savedCdId);
			parent.persist();
			logger.info("CdId for parent corp name " + parent.getCorpName() + " " + StructureType.PARENT
					+ " changed from " + originalCdId + " to " + savedCdId);
			p++;
		}

		logger.info("Finished saving " + processedStructures.size() + " " + StructureType.PARENT + " structures");
		return p;
	}

	public void fillMissingSaltFormStructures() throws CmpdRegMolFormatException {
		// Salt Forms
		List<BigInteger> missingIds = getStructureTypeIdsMissingStructures(StructureType.SALT_FORM);
		if (missingIds.size() > 0) {
			logger.warn("Found " + missingIds.size() + " " + StructureType.SALT_FORM
					+ " ids missing structure representations");
		} else {
			logger.info("No " + StructureType.SALT_FORM + " ids missing structure representations");
			return;
		}

		int batchSize = propertiesUtilService.getStandardizationBatchSize();

		List<List<Long>> groups = SimpleUtil.splitIntArrayIntoGroups(missingIds, batchSize);
		Long startTime = new Date().getTime();
		Long currentTime = new Date().getTime();
		int totalCount = missingIds.size();
		float percent = 0;
		int p = 0;
		float previousPercent = percent;

		for (List<Long> group : groups) {
			
			p = p + fillMissingSaltFormStructuresBySaltFormIDList(group);
			// Compute your percentage.
			percent = (float) Math.floor(p * 100f / totalCount);
			if (percent != previousPercent) {
				currentTime = new Date().getTime();
				// Output if different from the last time.
				logger.info("filling " + " " + StructureType.SALT_FORM + " structure representations " + percent
						+ "% complete (" + p + " of "
						+ totalCount + ") average speed (rows/min):"
						+ (p / ((currentTime - startTime) / 60.0 / 1000.0)));
				currentTime = new Date().getTime();
				logger.debug("Time Elapsed:" + (currentTime - startTime));
			}
			// Update the percentage.
			previousPercent = percent;
		}
	}

	@Transactional
	public int fillMissingSaltFormStructuresBySaltFormIDList(List<Long> group) throws CmpdRegMolFormatException {
		int p = 0;
		EntityManager em = BBChemSaltFormStructure.entityManager();
		Boolean checkForDupes = false;

		logger.info("Started fetching " + group.size() + " " + StructureType.SALT_FORM + " molstructures to save");
		HashMap<String, String> structureIdSaltFormMolStructureMap = new HashMap<String, String>();
		List<SaltForm> saltForms = em.createQuery("SELECT p FROM SaltForm p where id in (:ids)", SaltForm.class)
				.setParameter("ids", group)
				.getResultList();
		for (SaltForm saltForm : saltForms) {
			structureIdSaltFormMolStructureMap.put(String.valueOf(saltForm.getCdId()), saltForm.getMolStructure());
		}
		logger.info("Finished fetching " + group.size() + " " + StructureType.SALT_FORM + " molstructures to save");
		logger.info("Started processing " + structureIdSaltFormMolStructureMap.size() + " "
				+ StructureType.SALT_FORM + " structures");
		HashMap<String, BBChemParentStructure> processedStructures = bbChemStructureService
				.getProcessedStructures(structureIdSaltFormMolStructureMap, true, false);
		logger.info("Finished processing " + structureIdSaltFormMolStructureMap.size() + " "
				+ StructureType.SALT_FORM + " structures");
		logger.info("Started saving " + processedStructures.size() + " " + StructureType.SALT_FORM + " structures");
		// Loop through salt forms
		for (SaltForm saltForm : saltForms) {
			String originalCdId = String.valueOf(saltForm.getCdId());
			BBChemParentStructure processedParentStructure = processedStructures.get(originalCdId);
			int savedCdId = saveStructure(processedParentStructure, StructureType.SALT_FORM, checkForDupes);
			saltForm.setCdId(savedCdId);
			saltForm.persist();
			logger.info("CdId for corp name " + saltForm.getCorpName() + " " + StructureType.SALT_FORM
					+ " changed from " + originalCdId + " to " + savedCdId);
			p++;
		}

		logger.info("Finished saving " + processedStructures.size() + " structures");

		return p;
	}


	public void fillMissingSaltStructures() throws CmpdRegMolFormatException {
		// Salt Forms
		List<BigInteger> missingIds = getStructureTypeIdsMissingStructures(StructureType.SALT);
		if (missingIds.size() > 0) {
			logger.warn("Found " + missingIds.size() + " " + StructureType.SALT
					+ " ids missing structure representations");
		} else {
			logger.info("No " + StructureType.SALT + " ids missing structure representations");
			return;
		}

		int batchSize = propertiesUtilService.getStandardizationBatchSize();

		List<List<Long>> groups = SimpleUtil.splitIntArrayIntoGroups(missingIds, batchSize);
		Long startTime = new Date().getTime();
		Long currentTime = new Date().getTime();
		int totalCount = missingIds.size();
		float percent = 0;
		int p = 0;
		float previousPercent = percent;

		for (List<Long> group : groups) {
			
			p = p + fillMissingSaltStructuresBySaltIDList(group);
			// Compute your percentage.
			percent = (float) Math.floor(p * 100f / totalCount);
			if (percent != previousPercent) {
				currentTime = new Date().getTime();
				// Output if different from the last time.
				logger.info("filling " + " " + StructureType.SALT + " structure representations " + percent
						+ "% complete (" + p + " of "
						+ totalCount + ") average speed (rows/min):"
						+ (p / ((currentTime - startTime) / 60.0 / 1000.0)));
				currentTime = new Date().getTime();
				logger.debug("Time Elapsed:" + (currentTime - startTime));
			}
			// Update the percentage.
			previousPercent = percent;
		}
	}

	@Transactional
	public int fillMissingSaltStructuresBySaltIDList(List<Long> group) throws CmpdRegMolFormatException {
		int p = 0;
		EntityManager em = BBChemSaltStructure.entityManager();
		Boolean checkForDupes = false;

		logger.info("Started fetching " + group.size() + " " + StructureType.SALT + " molstructures to save");
		HashMap<String, String> structureIdSaltMolStructureMap = new HashMap<String, String>();
		List<Salt> salts = em.createQuery("SELECT p FROM Salt p where id in (:ids)", Salt.class)
				.setParameter("ids", group)
				.getResultList();
		for (Salt salt : salts) {
			structureIdSaltMolStructureMap.put(String.valueOf(salt.getCdId()), salt.getMolStructure());
		}
		logger.info("Finished fetching " + group.size() + " " + StructureType.SALT + " molstructures to save");
		logger.info("Started processing " + structureIdSaltMolStructureMap.size() + " "
				+ StructureType.SALT + " structures");
		HashMap<String, BBChemParentStructure> processedStructures = bbChemStructureService
				.getProcessedStructures(structureIdSaltMolStructureMap, true, false);
		logger.info("Finished processing " + structureIdSaltMolStructureMap.size() + " "
				+ StructureType.SALT + " structures");
		logger.info("Started saving " + processedStructures.size() + " " + StructureType.SALT + " structures");
		// Loop through parents
		for (Salt salt : salts) {
			String originalCdId = String.valueOf(salt.getCdId());
			BBChemParentStructure processedParentStructure = processedStructures.get(originalCdId);
			int savedCdId = saveStructure(processedParentStructure, StructureType.SALT, checkForDupes);
			salt.setCdId(savedCdId);
			salt.persist();
			logger.info("CdId for " + StructureType.SALT + " name '" + salt.getName() + "' abbrev '" + salt.getAbbrev()
					+ "' changed from " + originalCdId + " to " + savedCdId);
			p++;
		}

		logger.info("Finished saving " + processedStructures.size() + " structures");

		return p;
	}


	@Override
	public void fillMissingStructures() throws CmpdRegMolFormatException {

		// Parents
		fillMissingParentStructures();

		// Salt Forms
		fillMissingSaltFormStructures();

		// Salt
		fillMissingSaltStructures();

	}

	private ObjectNode parseSettingsToObjectNode(String settingsJsonString) {
		// This functions parses settings json from the ACAS configs or from the standardization history table
		// which have slightly different formats (the history table has a "settings" key) nested in the json
		// into a uniform object node with defaults for missing keys (schrodingerSuite and preprocessorVersion)
		// had, in previous versions, not been stored in the history table.
		if(settingsJsonString == null || settingsJsonString.isEmpty()) {
			return null;
		}
		// Empty mapper
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode inputSettings = mapper.createObjectNode();

		ObjectNode returnNode = mapper.createObjectNode();
		try {
			inputSettings = (ObjectNode) mapper.readTree(settingsJsonString);
			ObjectNode config = mapper.createObjectNode();
			String hashScheme = "TAUTOMER_INSENSITIVE_LAYERS";
			String schrodingerSuite = "unknown";
			String processorVersion = "unknown";

			if(inputSettings.get("settings") != null) {
				inputSettings = (ObjectNode) mapper.readTree(inputSettings.get("settings").textValue());
			}

			if(inputSettings.get("standardizer_actions") != null) {
				config = (ObjectNode) inputSettings.get("standardizer_actions");
			}
			returnNode.replace("config", config);

			if(inputSettings.get("schrodinger_suite_version") != null) {
				schrodingerSuite = inputSettings.get("schrodinger_suite_version").asText();
			}
			returnNode.put("schrodinger_suite_version", schrodingerSuite);

			if(inputSettings.get("preprocessor_version") != null) {
				processorVersion = inputSettings.get("preprocessor_version").asText();
			}
			returnNode.put("preprocessor_version", processorVersion);

			if(inputSettings.get("hash_scheme") != null) {
				hashScheme = inputSettings.get("hash_scheme").asText();
			}
			returnNode.put("hash_scheme", hashScheme);
			
			return returnNode;

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse lsThingJson", e);
        }



	}

	@Override
	public StandardizationSettingsConfigCheckResponseDTO checkStandardizerSettings(StandardizationHistory mostRecentStandardizationHistory, StandardizerSettingsConfigDTO standardizationSettingsConfigDTO) {
		// This functions checks the most recent standardization history settings against the current configured settings)
		// to check if settings are valid, if we need to re standardize and why (reasons).

		// Parse the Standardization history and current configs into a common format
		StandardizationSettingsConfigCheckResponseDTO newConfigCheck = new StandardizationSettingsConfigCheckResponseDTO();
		ObjectNode oldSettingsNode = parseSettingsToObjectNode(mostRecentStandardizationHistory.getSettings());
		ObjectNode newSettingsNode = parseSettingsToObjectNode(standardizationSettingsConfigDTO.getSettings());

		try {

			// Check if the configs are valid and get the reasons why they are not and set the applied standardizer actions to the validated response
			// Using the configured settings, call the BBChem fix endpoint to get a valididated setting configurations
			StandardizationSettingsConfigCheckResponseDTO configFixResponse = bbChemStructureService.configFix(newSettingsNode.get("config"));
			ObjectMapper mapper = new ObjectMapper();
			newConfigCheck.setValid(configFixResponse.getValid());
			newConfigCheck.setInvalidReasons(configFixResponse.getInvalidReasons());

			// We only suggest configuration changes if the configurations are valid because listing invalid reasons next to a list of suggested
			// changes is confusing.  If an administrator fixes their configs to be valid they will then be presented with a list of suggestions
			if(newConfigCheck.getValid()) {
				newConfigCheck.setSuggestedConfigurationChanges(configFixResponse.getSuggestedConfigurationChanges());
			}

			propertiesUtilService.setStandardizerActions(mapper.readTree(configFixResponse.getValidatedSettings()));

			// Logic to determine whether we need to restandardize
			if(oldSettingsNode == null || oldSettingsNode.isEmpty()) {
				// If we can find a standardization history, then check if there are any parents on the system
				long parentCount = Parent.countParents();
				if(parentCount > 0) {
					// If there are parents on the system and we have no history, then we need to restandardize
					logger.warn("Found no standardization history, and there are " + parentCount + " parents on the system. Setting needs restandardization to true.");
					newConfigCheck.setNeedsRestandardization(true);
					newConfigCheck.setNeedsRestandardizationReasons("No record of any standardized structures but there are " + parentCount + " parents on the system.");
				} else {
					// If there are 0 parents then we mark it as not needing restandardization
					logger.warn("Old settings are empty, and there are no parents on the system. Setting needs restandardization to false");
					newConfigCheck.setNeedsRestandardization(false);
				}

			} else {
				// We have a previous standardization history record, so check if the settings are the same
				StandardizationSettingsConfigCheckResponseDTO configCheckResponse = bbChemStructureService.configCheck(
					oldSettingsNode.get("config").toString(), 
					configFixResponse.getValidatedSettings(), 
					oldSettingsNode.get("hash_scheme").asText(),
					newSettingsNode.get("hash_scheme").asText(), 
					oldSettingsNode.get("preprocessor_version").asText(), 
					oldSettingsNode.get("schrodinger_suite_version").asText()
				);
				newConfigCheck.setNeedsRestandardization(configCheckResponse.getNeedsRestandardization());
				newConfigCheck.addNeedsRestandardizationReasons(configCheckResponse.getNeedsRestandardizationReasons());
			}

			return newConfigCheck;
		} catch (IOException e) {
			throw new RuntimeException("Failed call to bbchem config fix service: " + newSettingsNode.get("config").asText(), e);
		}
	}


	@Transactional
	@Override
	public void populateDuplicateChangedStructures(int batchSize) {
		// Update existing, new and changed structures
		updateExistingDuplicates();
		updateNewDuplicates();
		updateChangedStructure();
	}
	
	@Transactional
	public static void updateExistingDuplicates() {
		// Existing Duplicates: List of parent corp names that were already the same structure as the standardized structure taking into account tautomers, stereo comments and stereo categories.
		EntityManager em = StandardizationDryRunCompound.entityManager();
	
		String sql = "WITH old_duplicates AS ( " +
					 "    SELECT " +
					 "        p1.id AS parent_id, " +
					 "        COUNT(DISTINCT p2.corp_name) AS duplicate_count, " +
					 "        STRING_AGG(DISTINCT p2.corp_name, ';') AS duplicate_corp_names " +
					 "    FROM " +
					 "        parent p1 " +
					 "    JOIN " +
					 "        bbchem_parent_structure bps1 ON p1.cd_id = bps1.id " +
					 "    JOIN " +
					 "        bbchem_parent_structure bps2 ON bps1.reg = bps2.reg " +
					 "    JOIN " +
					 "        parent p2 ON bps2.id = p2.cd_id " +
					 "    WHERE " +
					 "        p1.corp_name != p2.corp_name " +
					 "        AND p1.stereo_category = p2.stereo_category " +
					 "        AND ( " +
					 "            (p1.stereo_comment IS NULL AND p2.stereo_comment IS NULL) OR " +
					 "            (p1.stereo_comment IS NOT NULL AND p2.stereo_comment IS NOT NULL AND " +
					 "             LOWER(p1.stereo_comment) = LOWER(p2.stereo_comment)) " +
					 "        ) " +
					 "    GROUP BY " +
					 "        p1.id " +
					 ") " +
					 "UPDATE standardization_dry_run_compound sdr " +
					 "SET " +
					 "    existing_duplicate_count = old_duplicates.duplicate_count, " +
					 "    existing_duplicates = old_duplicates.duplicate_corp_names " +
					 "FROM " +
					 "    old_duplicates " +
					 "WHERE " +
					 "    sdr.parent_id = old_duplicates.parent_id " +
					 "    AND sdr.existing_duplicate_count IS NULL;";
		em.createNativeQuery(sql).executeUpdate();
	}
	
	@Transactional
	public static void updateNewDuplicates() {
		// New Duplicates: List of parent corp names that are now the same structure as the standardized structure taking into account tautomers, stereo comments and stereo categories.
		EntityManager em = StandardizationDryRunCompound.entityManager();
	
		String newDuplicatesSql = "WITH new_duplicates AS ( " +
								  "    SELECT " +
								  "        p1.id AS parent_id, " +
								  "        COUNT(DISTINCT p2.corp_name) AS duplicate_count, " +
								  "        STRING_AGG(DISTINCT p2.corp_name, ';') AS duplicate_corp_names " +
								  "    FROM " +
								  "        parent p1 " +
								  "    JOIN " +
								  "        standardization_dry_run_compound sdr1 ON p1.id = sdr1.parent_id " +
								  "    JOIN " +
								  "        bbchem_standardization_dry_run_structure bss1 ON sdr1.cd_id = bss1.id " +
								  "    JOIN " +
								  "        bbchem_standardization_dry_run_structure bss2 ON bss1.reg = bss2.reg " +
								  "    JOIN " +
								  "        standardization_dry_run_compound sdr2 ON bss2.id = sdr2.cd_id " +
								  "    JOIN " +
								  "        parent p2 ON sdr2.parent_id = p2.id " +
								  "    WHERE " +
								  "        p1.corp_name != p2.corp_name " +
								  "        AND p1.stereo_category = p2.stereo_category " +
								  "        AND ( " +
								  "            (p1.stereo_comment IS NULL AND p2.stereo_comment IS NULL) OR " +
								  "            (p1.stereo_comment IS NOT NULL AND p2.stereo_comment IS NOT NULL AND " +
								  "             LOWER(p1.stereo_comment) = LOWER(p2.stereo_comment)) " +
								  "        ) " +
								  "    GROUP BY " +
								  "        p1.id " +
								  ") " +
								  "UPDATE standardization_dry_run_compound sdr " +
								  "SET " +
								  "    new_duplicate_count = new_duplicates.duplicate_count, " +
								  "    new_duplicates = new_duplicates.duplicate_corp_names " +
								  "FROM " +
								  "    new_duplicates " +
								  "WHERE " +
								  "    sdr.parent_id = new_duplicates.parent_id " +
								  "    AND sdr.new_duplicate_count IS NULL;";
		em.createNativeQuery(newDuplicatesSql).executeUpdate();
	}
	
	@Transactional
	public static void updateChangedStructure() {
		// # Structures Changed: Standardized structure is no longer the same as it's parent structure taking into account tautomers.
		// i.e. reg hashes no longer match
		EntityManager em = StandardizationDryRunCompound.entityManager();
		String changedStructureSql = "WITH structure_comparison AS ( " +
									 "    SELECT " +
									 "        sdr.id AS dry_run_id, " +
									 "        CASE " +
									 "            WHEN bss1.reg = bps1.reg THEN false " +
									 "            ELSE true " +
									 "        END AS changed_structure " +
									 "    FROM " +
									 "        standardization_dry_run_compound sdr " +
									 "    JOIN " +
									 "        parent p1 ON sdr.parent_id = p1.id " +
									 "    JOIN " +
									 "        bbchem_parent_structure bps1 ON p1.cd_id = bps1.id " + // Existing parent structure
									 "    JOIN " +
									 "        bbchem_standardization_dry_run_structure bss1 ON sdr.cd_id = bss1.id " + // New dry run structure
									 ") " +
									 "UPDATE standardization_dry_run_compound sdr " +
									 "SET " +
									 "    changed_structure = structure_comparison.changed_structure, " +
									 "    sync_status = '" + SyncStatus.READY + "' " +
									 "FROM " +
									 "    structure_comparison " +
									 "WHERE " +
									 "    sdr.id = structure_comparison.dry_run_id " +
									 "    AND structure_comparison.changed_structure = true " + // Only update if structure has changed
									 "    AND sdr.changed_structure IS NULL;"; // Skip rows already processed
		em.createNativeQuery(changedStructureSql).executeUpdate();
	}

}
