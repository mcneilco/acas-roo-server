package com.labsynch.labseer.chemclasses.bbchem;

import static java.lang.Math.toIntExact;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule.RegistrationStatus;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule.StandardizationStatus;
import com.labsynch.labseer.domain.AbstractBBChemStructure;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.domain.SaltForm;
import com.labsynch.labseer.dto.MolConvertOutputDTO;
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
			serviceBBChemStructure = bbChemStructureService.getProcessedStructure(molfile, false);
		} else {
			// We need to do fingerprint matching for these searches so pass true here
			serviceBBChemStructure = bbChemStructureService.getProcessedStructure(molfile, true);
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
				bbChemStructure = bbChemStructureService.getProcessedStructure(bbChemStructure.getMol(), true);
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
			BBChemParentStructure bbChemStructure = bbChemStructureService.getProcessedStructure(molfile, true);
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
		try {
			BBChemParentStructure bbChemStructure = bbChemStructureService.getProcessedStructure(molStructure, true);
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
				bbChemStructure = bbChemStructureService.getProcessedStructure(bbChemStructure.getMol(), true);
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
		return bbChemStructureService.getProcessedStructure(molStructure, false).getMolecularFormula();
	}

	@Override
	public boolean checkForSalt(String molfile) throws CmpdRegMolFormatException {
		boolean foundNonCovalentSalt = false;
		// Get all fragments
		List<String> allFrags = bbChemStructureService.getMolFragments(molfile);
		if (allFrags.size() > 1.0) {
			foundNonCovalentSalt = true;
		}
		return foundNonCovalentSalt;
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
		BBChemParentStructure bbChemStructure = bbChemStructureService.getProcessedStructure(molStructure, false);
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
		// Call bbchem to conver the structure to the
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
		List<String> allFrags = bbChemStructureService.getMolFragments(inputStructure.getMolStructure());

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
				.getProcessedStructures(structures, true);

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
		// {
		// "error_code": "4004",
		// "error_msg": "No atoms present"
		// }
		// ]
		try {
			JsonNode responseNode = bbChemStructureService.postToProcessService(molFile);
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
	public StandardizerSettingsConfigDTO getStandardizerSettings() throws StandardizerException {
		// Read the preprocessor settings as json
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		try {
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
				+ " b on p.cd_id=b.id where p.mol_structure is not null and b.id is null");
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
				.getProcessedStructures(structureIdParentMolStructureMap, true);
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
				.getProcessedStructures(structureIdSaltFormMolStructureMap, true);
		logger.info("Finished processing " + structureIdSaltFormMolStructureMap.size() + " "
				+ StructureType.SALT_FORM + " structures");
		logger.info("Started saving " + processedStructures.size() + " " + StructureType.SALT_FORM + " structures");
		// Loop through parents
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

	@Override
	public void fillMissingStructures() throws CmpdRegMolFormatException {

		// Parents
		fillMissingParentStructures();

		// Salt Forms
		fillMissingSaltFormStructures();

	}

}
