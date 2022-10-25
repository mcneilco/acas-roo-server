package com.labsynch.labseer.chemclasses.indigo;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.epam.indigo.Indigo;
import com.epam.indigo.IndigoException;
import com.epam.indigo.IndigoObject;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.domain.StandardizationHistory;
import com.labsynch.labseer.dto.configuration.StandardizerSettingsConfigDTO;
import com.labsynch.labseer.dto.MolConvertOutputDTO;
import com.labsynch.labseer.dto.StandardizationSettingsConfigCheckResponseDTO;
import com.labsynch.labseer.dto.StandardizationSettingsConfigCheckResponseDTO;
import com.labsynch.labseer.dto.StrippedSaltDTO;

import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.StandardizerException;
import com.labsynch.labseer.service.ChemStructureService;
import com.labsynch.labseer.utils.PropertiesUtilService;

@Component
public class ChemStructureServiceIndigoImpl implements ChemStructureService {

	Logger logger = LoggerFactory.getLogger(ChemStructureServiceIndigoImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	private boolean shouldCloseConnection = false;

	public void setShouldCloseConnection(boolean shouldCloseConnection) {
		this.shouldCloseConnection = shouldCloseConnection;
	}

	public boolean getShouldCloseConnection() {
		return shouldCloseConnection;
	}

	@Autowired
	private JdbcTemplate basicJdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.basicJdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate() {
		return basicJdbcTemplate;
	}

	private Indigo indigo = new Indigo();

	@Override
	public boolean compareStructures(String preMolStruct, String postMolStruct, SearchType searchType) {

		// logger.info("SearchType is: " + searchType);
		boolean compoundsMatch = false;
		try {
			IndigoObject queryMol = indigo.loadMolecule(preMolStruct);
			IndigoObject targetMol = indigo.loadMolecule(preMolStruct);

			compoundsMatch = (indigo.exactMatch(queryMol, targetMol, "ALL") != null);
			if (!compoundsMatch) {
				logger.info(queryMol.smiles());
				logger.info(targetMol.smiles());
			}

		} catch (IndigoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return compoundsMatch;
	}

	@Override
	public String standardizeStructure(String molfile) throws CmpdRegMolFormatException {
		// Call method below after parsing molfile
		CmpdRegMoleculeIndigoImpl molWrapper = new CmpdRegMoleculeIndigoImpl(molfile);
		return standardizeMolecule(molWrapper.molecule).molfile();
	}

	public IndigoObject standardizeMolecule(IndigoObject molecule) {
		// Indigo standardizer configs
		logger.error("Standardizer with Indigo chemistry services is not implemented!");
		// indigo.setOption("standardize-stereo", true);
		molecule.standardize();
		return molecule;
	}

	@Override
	public int saveStructure(String molfile, StructureType structureType) {
		boolean checkForDupes = false;
		return saveStructure(molfile, structureType, checkForDupes);
	}

	@Override
	public int saveStructure(String molfile, StructureType structureType, boolean checkForDupes) {
		return (int) Parent.countParents() + 1;
	}

	@Override
	public void closeConnection() {
		// do nothing
	}

	@Override
	public int[] searchMolStructures(String molfile, StructureType structureType, SearchType searchType)
			throws CmpdRegMolFormatException {
		return searchMolStructures(molfile, structureType, searchType, 0f);
	}

	@Override
	public int[] searchMolStructures(String molfile, StructureType structureType, SearchType searchType,
			Float simlarityPercent) throws CmpdRegMolFormatException {
		int maxResultCount = propertiesUtilService.getMaxSearchResults();
		return searchMolStructures(molfile, structureType, searchType, simlarityPercent, maxResultCount);

	}

	@Override
	@Transactional
	public int[] searchMolStructures(String molfile, StructureType structureType, SearchType searchType,
			Float simlarityPercent, int maxResults) throws CmpdRegMolFormatException {

		Connection conn = DataSourceUtils.getConnection(basicJdbcTemplate.getDataSource());
		try {
			conn.setAutoCommit(true);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		long maxTime = propertiesUtilService.getMaxSearchTime();
		int maxResultCount = maxResults;
		// Indigo: the structures in the plainTable are being used. Ignore
		// structureTable from here on out.
		logger.debug("Search table is  " + structureType.entityTable);
		logger.debug("Search type is  " + searchType);
		logger.debug("Max number of results is  " + maxResults);

		logger.debug("Dissimilarity similarity is  " + simlarityPercent);

		float indSimilarity = 1.0f - simlarityPercent;
		logger.debug("Indigo search similarity is  " + indSimilarity);

		if (searchType == SearchType.EXACT) {
			searchType = propertiesUtilService.getExactMatchDef();
		}

		try {
			CmpdRegMoleculeIndigoImpl molWrapper = new CmpdRegMoleculeIndigoImpl(molfile);
			IndigoObject mol = molWrapper.molecule;

			String baseQuery = "SELECT cd_id FROM " + structureType.entityTable + " WHERE mol_structure @ ";
			String bingoFunction = null;
			String orderBy = " ORDER BY cd_id";

			if (propertiesUtilService.getUseExternalStandardizerConfig()) {
				mol = standardizeMolecule(mol);
				mol.dearomatize();
			} else {
				mol.dearomatize();
			}

			logger.debug("definition of exact search: " + propertiesUtilService.getExactMatchDef());
			logger.debug("selected search type: " + searchType);

			if (searchType == SearchType.SUBSTRUCTURE) {
				bingoFunction = "cast( ( :queryMol , :parameters ) as bingo.sub)";
			} else if (searchType == SearchType.SIMILARITY) {
				bingoFunction = "cast( ( :minSimilarity , :maxSimilarity , :queryMol , :metric ) as bingo.sim)";
				orderBy = " ORDER BY " + bingoFunction;
			} else {
				bingoFunction = "cast(( :queryMol , :parameters ) as bingo.exact)";
			}

			EntityManager em = Parent.entityManager();
			Query query = em.createNativeQuery(baseQuery + bingoFunction + orderBy);

			query.setParameter("queryMol", mol.molfile());
			query.setMaxResults(maxResults);

			// May need additional research / decisions around which options to use
			// Basic Indigo search types corresponding to JChem search types
			// CReg: "DUPLICATE" or "DUPLICATE_NO_TAUTOMER" :: JChem: "DUPLICATE",
			// "TAUTOMER_SEARCH_OFF" :: Bingo.Exact, "ALL"
			// CReg: "DUPLICATE_TAUTOMER" :: JChem: "DUPLICATE", "TAUTOMER_SEARCH_ON" ::
			// Bingo.Exact, "TAU"
			// CReg: "STEREO_IGNORE" :: JChem: "STEREO_IGNORE" :: Bingo.Exact "ALL -STE"
			// (NOT SURE ABOUT THIS) CReg: "FULL_TAUTOMER", or default/unrecognized
			// searchType :: JChem: "FULL", "CHARGE_MATCHING_IGNORE",
			// "ISOTOPE_MATCHING_IGNORE", "STEREO_IGNORE", "TAUTOMER_SEARCH_ON" ::
			// Bingo.Exact "TAU ALL -ELE -MAS -STE"
			// CReg: "SUBSTRUCTURE" :: JChem: "SUBSTRUCTURE" :: Bingo.Sub
			// CReg: "SIMILARITY" :: JChem "SIMILARITY" :: Bingo.Sim > $min
			// CReg: "FULL" :: JChem "FULL", "CHARGE_MATCHING_IGNORE",
			// "ISOTOPE_MATCHING_IGNORE", "STEREO_IGNORE", "TAUTOMER_SEARCH_OFF" ::
			// Bingo.Exact "ALL -ELE -MAS -STE"

			if (searchType == SearchType.SUBSTRUCTURE) {
				query.setParameter("parameters", "");
			} else if (searchType == SearchType.SIMILARITY) {
				query.setParameter("minSimilarity", indSimilarity);
				query.setParameter("maxSimilarity", 1.0);
				query.setParameter("metric", "Tanimoto");
			} else {
				String parameters = null;
				switch (searchType) {
					case DUPLICATE:
						parameters = "ALL";
						break;
					case DUPLICATE_TAUTOMER:
						parameters = "TAU";
						break;
					case STEREO_IGNORE:
						parameters = "ALL -STE";
						break;
					case FULL_TAUTOMER:
						parameters = "TAU";
						break;
					case FULL:
						parameters = "ALL -ELE -MAS -STE";
						break;
					default:
						// TODO: this should match FULL_TAUTOMER if possible
						parameters = "ALL";
						break;
				}
				query.setParameter("parameters", parameters);
			}
			if (logger.isDebugEnabled())
				logger.debug(query.unwrap(org.hibernate.Query.class).getQueryString());
			// TODO: should do an audit of the search types being used by CReg.

			List<Integer> hitListList = query.getResultList();

			// if we are searching in DUPLICATE_TAUTOMER mode and the above TAU search
			// returned results
			// we need to also check the stereochemistry matches since we can't do both at
			// once.
			// the overall results should be the intersection of both queries
			if (hitListList.size() > 0 && mol.countAtoms() > 0 && searchType == SearchType.DUPLICATE_TAUTOMER) {
				query.setParameter("parameters", "STE");
				List<Integer> stereoHitListList = query.getResultList();
				hitListList.retainAll(stereoHitListList);
			}

			if (hitListList.size() > 0) {
				logger.debug("found a matching molecule!!!  " + hitListList.size());
			}

			int[] hitList = ArrayUtils.toPrimitive(hitListList.toArray(new Integer[hitListList.size()]));

			return hitList;

		} catch (JpaSystemException | PersistenceException sqlException) {
			logger.error("Caught search error", sqlException);
			Exception rootCause = new Exception(ExceptionUtils.getRootCause(sqlException).getMessage(),
					ExceptionUtils.getRootCause(sqlException));
			throw new CmpdRegMolFormatException(rootCause);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean checkForSalt(String molfile) throws CmpdRegMolFormatException {
		IndigoObject mol = indigo.loadMolecule(molfile);
		int fragCount = mol.countComponents();
		boolean foundNonCovalentSalt = false;
		if (fragCount > 1) {
			foundNonCovalentSalt = true;
		}

		return foundNonCovalentSalt;

	}

	@Override
	public StrippedSaltDTO stripSalts(CmpdRegMolecule inputStructure) throws CmpdRegMolFormatException {
		CmpdRegMoleculeIndigoImpl molWrapper = (CmpdRegMoleculeIndigoImpl) inputStructure;
		IndigoObject rawMolecule = molWrapper.molecule;
		IndigoObject clone = rawMolecule.clone();
		List<CmpdRegMoleculeIndigoImpl> allFrags = new ArrayList<CmpdRegMoleculeIndigoImpl>();
		for (IndigoObject component : clone.iterateComponents()) {
			CmpdRegMoleculeIndigoImpl fragMolecule = new CmpdRegMoleculeIndigoImpl(component.clone());
			allFrags.add(fragMolecule);
		}
		Map<Salt, Integer> saltCounts = new HashMap<Salt, Integer>();
		Set<CmpdRegMoleculeIndigoImpl> unidentifiedFragments = new HashSet<CmpdRegMoleculeIndigoImpl>();
		for (CmpdRegMoleculeIndigoImpl fragment : allFrags) {
			int[] cdIdMatches = searchMolStructures(fragment.getMolStructure(), StructureType.SALT,
					SearchType.DUPLICATE_TAUTOMER);
			if (cdIdMatches.length > 0) {
				Salt foundSalt = Salt.findSaltsByCdId(cdIdMatches[0]).getSingleResult();
				if (saltCounts.containsKey(foundSalt))
					saltCounts.put(foundSalt, saltCounts.get(foundSalt) + 1);
				else
					saltCounts.put(foundSalt, 1);
			} else {
				unidentifiedFragments.add(fragment);
			}
		}
		StrippedSaltDTO resultDTO = new StrippedSaltDTO();
		resultDTO.setSaltCounts(saltCounts);
		resultDTO.setUnidentifiedFragments(unidentifiedFragments);
		logger.debug("Identified stripped salts:");
		for (Salt salt : saltCounts.keySet()) {
			logger.debug("Salt Abbrev: " + salt.getAbbrev());
			logger.debug("Salt Count: " + saltCounts.get(salt));
		}
		return resultDTO;
	}

	@Override
	@Transactional
	public CmpdRegMolecule[] searchMols(String molfile, StructureType structureType, int[] inputCdIdHitList,
			SearchType searchType, Float simlarityPercent) throws CmpdRegMolFormatException {
		int maxResults = propertiesUtilService.getMaxSearchResults();
		return searchMols(molfile, structureType, inputCdIdHitList, searchType, simlarityPercent, maxResults);
	}

	@Override
	@Transactional
	public boolean truncateStructureTable(StructureType structureType) {
		// TODO: Implment validation mode for indigo builds
		return true;
	}

	@Override
	@Transactional
	public CmpdRegMolecule[] searchMols(String molfile, StructureType structureType, int[] inputCdIdHitList,
			SearchType searchType, Float simlarityPercent, int maxResults) throws CmpdRegMolFormatException {

		Connection conn = DataSourceUtils.getConnection(basicJdbcTemplate.getDataSource());
		try {
			conn.setAutoCommit(true);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		long maxTime = propertiesUtilService.getMaxSearchTime();
		int maxResultCount = maxResults;
		// Indigo: the structures in the plainTable are being used. Ignore
		// structureTable from here on out.
		logger.debug("Search table is  " + structureType.entityTable);
		logger.debug("Search type is  " + searchType);
		logger.debug("Max number of results is  " + maxResults);

		logger.debug("Dissimilarity similarity is  " + simlarityPercent);

		float indSimilarity = 1.0f - simlarityPercent;
		logger.debug("Indigo search similarity is  " + indSimilarity);

		if (searchType == SearchType.EXACT) {
			searchType = propertiesUtilService.getExactMatchDef();
		}

		List<CmpdRegMoleculeIndigoImpl> moleculeList = new ArrayList<CmpdRegMoleculeIndigoImpl>();
		try {
			CmpdRegMoleculeIndigoImpl molWrapper = new CmpdRegMoleculeIndigoImpl(molfile);
			IndigoObject mol = molWrapper.molecule;

			String baseQuery = "SELECT cd_id, mol_structure FROM " + structureType.entityTable
					+ " WHERE mol_structure @ ";
			String bingoFunction = null;
			String orderBy = " ORDER BY cd_id";
			String filterIdsClause = "";

			if (propertiesUtilService.getUseExternalStandardizerConfig()) {
				mol = standardizeMolecule(mol);
				mol.aromatize();
			} else {
				mol.aromatize();
			}

			logger.debug("definition of exact search: " + propertiesUtilService.getExactMatchDef());
			logger.debug("selected search type: " + searchType);

			if (searchType == SearchType.SUBSTRUCTURE) {
				bingoFunction = "cast(( :queryMol , :parameters ) as bingo.sub) ";
			} else if (searchType == SearchType.SIMILARITY) {
				bingoFunction = "cast( ( :minSimilarity , :maxSimilarity , :queryMol , :metric ) as bingo.sim)";
				orderBy = " ORDER BY " + bingoFunction;
			} else {
				bingoFunction = "cast( ( :queryMol , :parameters ) as bingo.exact)";
			}

			if (inputCdIdHitList != null && inputCdIdHitList.length > 0)
				filterIdsClause = " AND cd_id IN :filterCdIds ";

			EntityManager em = Parent.entityManager();
			Query query = em.createNativeQuery(baseQuery + bingoFunction + filterIdsClause + orderBy);

			query.setParameter("queryMol", mol.molfile());
			query.setMaxResults(maxResults);

			if (inputCdIdHitList != null && inputCdIdHitList.length > 0)
				query.setParameter("filterCdIds", Arrays.asList(ArrayUtils.toObject(inputCdIdHitList)));

			// May need additional research / decisions around which options to use
			// Basic Indigo search types corresponding to JChem search types
			// CReg: "DUPLICATE" or "DUPLICATE_NO_TAUTOMER" :: JChem: "DUPLICATE",
			// "TAUTOMER_SEARCH_OFF" :: Bingo.Exact, "ALL"
			// CReg: "DUPLICATE_TAUTOMER" :: JChem: "DUPLICATE", "TAUTOMER_SEARCH_ON" ::
			// Bingo.Exact, "TAU"
			// CReg: "STEREO_IGNORE" :: JChem: "STEREO_IGNORE" :: Bingo.Exact "ALL -STE"
			// (NOT SURE ABOUT THIS) CReg: "FULL_TAUTOMER", or default/unrecognized
			// searchType :: JChem: "FULL", "CHARGE_MATCHING_IGNORE",
			// "ISOTOPE_MATCHING_IGNORE", "STEREO_IGNORE", "TAUTOMER_SEARCH_ON" ::
			// Bingo.Exact "TAU ALL -ELE -MAS -STE"
			// CReg: "SUBSTRUCTURE" :: JChem: "SUBSTRUCTURE" :: Bingo.Sub
			// CReg: "SIMILARITY" :: JChem "SIMILARITY" :: Bingo.Sim > $min
			// CReg: "FULL" :: JChem "FULL", "CHARGE_MATCHING_IGNORE",
			// "ISOTOPE_MATCHING_IGNORE", "STEREO_IGNORE", "TAUTOMER_SEARCH_OFF" ::
			// Bingo.Exact "ALL -ELE -MAS -STE"

			if (searchType == SearchType.SUBSTRUCTURE) {
				query.setParameter("parameters", "");
			} else if (searchType == SearchType.SIMILARITY) {
				query.setParameter("minSimilarity", indSimilarity);
				query.setParameter("maxSimilarity", 1.0);
				query.setParameter("metric", "Tanimoto");
			} else {
				String parameters = null;
				switch (searchType) {
					case DUPLICATE:
						parameters = "ALL";
						break;
					case DUPLICATE_TAUTOMER:
						parameters = "TAU";
						break;
					case STEREO_IGNORE:
						parameters = "ALL -STE";
						break;
					case FULL_TAUTOMER:
						parameters = "TAU";
						break;
					case FULL:
						parameters = "ALL -ELE -MAS -STE";
						break;
					default:
						// TODO: this should match FULL_TAUTOMER if possible
						parameters = "ALL";
						break;
				}
				query.setParameter("parameters", parameters);
			}
			if (logger.isDebugEnabled())
				logger.debug(query.unwrap(org.hibernate.Query.class).getQueryString());
			// TODO: should do an audit of the search types being used by CReg.

			// list of maps of {"cdId": ###, "molStructure": "molfile string"}
			List<Object[]> hitListList = query.getResultList();
			for (Object[] hit : hitListList) {
				Integer cdId = (Integer) hit[0];
				String molStructure = (String) hit[1];
				CmpdRegMoleculeIndigoImpl molecule = new CmpdRegMoleculeIndigoImpl(molStructure);
				molecule.setProperty("cd_id", String.valueOf(cdId));
				moleculeList.add(molecule);
			}

			if (moleculeList.size() > 0) {
				logger.debug("found a matching molecule!!!  " + moleculeList.size());
			}

			if (this.shouldCloseConnection) {
				conn.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JpaSystemException sqlException) {
			logger.error("Caught search error", sqlException);
			Exception rootCause = new Exception(ExceptionUtils.getRootCause(sqlException).getMessage(),
					ExceptionUtils.getRootCause(sqlException));
			throw new CmpdRegMolFormatException(rootCause);
		}

		CmpdRegMoleculeIndigoImpl[] moleculeHits = moleculeList.toArray(new CmpdRegMoleculeIndigoImpl[0]);

		return moleculeHits;
	}

	@Override
	public CmpdRegMolecule toMolecule(String molStructure) {
		boolean badStructureFlag = false;
		IndigoObject mol = null;
		String lineEnd = System.getProperty("line.separator");
		try {
			mol = indigo.loadMolecule(molStructure);
		} catch (IndigoException e1) {
			logger.debug("failed first attempt: bad mol structure: " + molStructure);
			// clean up the molString and try again
			try {
				molStructure = new StringBuilder().append(lineEnd).append(molStructure).append(lineEnd).toString();
				mol = indigo.loadMolecule(molStructure);
			} catch (IndigoException e2) {
				logger.debug("failed second attempt: bad mol structure: " + molStructure);
				badStructureFlag = true;
				logger.error("bad mol structure: " + molStructure);
			}
		}

		if (!badStructureFlag) {
			return new CmpdRegMoleculeIndigoImpl(mol);
		} else {
			return null;
		}
	}

	@Override
	public String toMolfile(String molStructure) throws CmpdRegMolFormatException {
		CmpdRegMoleculeIndigoImpl molecule = new CmpdRegMoleculeIndigoImpl(molStructure);
		IndigoObject rawMolecule = molecule.molecule;
		rawMolecule.dearomatize();
		return rawMolecule.molfile();

	}

	@Override
	public String getCipStereo(String structure) throws IOException, CmpdRegMolFormatException {
		CmpdRegMoleculeIndigoImpl molWrapper = new CmpdRegMoleculeIndigoImpl(structure);
		IndigoObject molecule = molWrapper.molecule;
		indigo.setOption("molfile-saving-add-stereo-desc", "1");
		return molecule.molfile();
	}

	@Override
	public String hydrogenizeMol(String structure, String inputFormat, String method)
			throws IOException, CmpdRegMolFormatException {
		CmpdRegMoleculeIndigoImpl molWrapper = new CmpdRegMoleculeIndigoImpl(structure);
		IndigoObject molecule = molWrapper.molecule;
		molecule.unfoldHydrogens();
		return molecule.molfile();
	}

	@Override
	public MolConvertOutputDTO cleanStructure(String structure, int dim, String opts)
			throws IOException, CmpdRegMolFormatException {
		CmpdRegMoleculeIndigoImpl molWrapper = new CmpdRegMoleculeIndigoImpl(structure);
		IndigoObject molecule = molWrapper.molecule;
		molecule.layout();
		MolConvertOutputDTO output = new MolConvertOutputDTO();
		output.setFormat("mol");
		output.setStructure(molecule.molfile());
		return output;
	}

	@Override
	public MolConvertOutputDTO toFormat(String structure, String inputFormat, String outputFormat)
			throws IOException, CmpdRegMolFormatException {
		CmpdRegMoleculeIndigoImpl molWrapper = new CmpdRegMoleculeIndigoImpl(structure);
		IndigoObject molecule = molWrapper.molecule;
		if (inputFormat.equalsIgnoreCase("smiles"))
			molecule.layout();
		MolConvertOutputDTO output = new MolConvertOutputDTO();
		output.setFormat("mol");
		output.setStructure(molecule.molfile());
		return output;
	}

	@Override
	public String toInchi(String molStructure) {
		// boolean badStructureFlag = false;
		// IndigoObject mol = null;
		// try {
		// mol = indigo.loadMolecule(molStructure);
		// } catch (IndigoException e) {
		// badStructureFlag = true;
		// }
		//
		// if (!badStructureFlag){
		// return mol.toFormat("inchi");
		// } else {
		// return molStructure;
		// }
		logger.error("inChi not implemented with Indigo Services");
		return molStructure;
	}

	@Override
	public String toSmiles(String molStructure) {
		boolean badStructureFlag = false;
		IndigoObject mol = null;
		try {
			mol = indigo.loadMolecule(molStructure);
		} catch (IndigoException e) {
			badStructureFlag = true;
		}

		if (!badStructureFlag) {
			return mol.smiles();
		} else {
			return molStructure;
		}
	}

	@Override
	public boolean isEmpty(String molStructure) throws CmpdRegMolFormatException {
		// Return empty if no atoms bonds or SGroups
		CmpdRegMoleculeIndigoImpl molWrapper = new CmpdRegMoleculeIndigoImpl(molStructure);
		IndigoObject mol = molWrapper.molecule;
		Boolean hasAtoms = mol.countAtoms() == 0.0;
		Boolean hasBonds = mol.countBonds() == 0.0;
		Boolean hasSGroups = mol.countGenericSGroups() == 0.0 && mol.countDataSGroups() == 0.0;
		return !hasAtoms && !hasBonds && !hasSGroups;
	}

	@Override
	public double getMolWeight(String molStructure) throws CmpdRegMolFormatException {
		CmpdRegMoleculeIndigoImpl mol = new CmpdRegMoleculeIndigoImpl(molStructure);
		return mol.getMass();
	}

	@Override
	public double getExactMass(String molStructure) throws CmpdRegMolFormatException {
		CmpdRegMoleculeIndigoImpl mol = new CmpdRegMoleculeIndigoImpl(molStructure);
		return mol.getExactMass();
	}

	@Override
	public String getMolFormula(String molStructure) throws CmpdRegMolFormatException {
		CmpdRegMoleculeIndigoImpl mol = new CmpdRegMoleculeIndigoImpl(molStructure);
		return mol.getFormula();
	}

	@Override
	public int[] checkDupeMol(String molStructure, StructureType structureType) throws CmpdRegMolFormatException {

		return searchMolStructures(molStructure, structureType, SearchType.DUPLICATE_TAUTOMER);
	}

	@Override
	public boolean updateStructure(String molStructure, StructureType structureType, int cdId) {
		// no-op
		return true;
	}

	@Override
	public boolean updateStructure(CmpdRegMolecule mol, StructureType structureType, int cdId) {
		// no-op
		return true;
	}

	@Override
	public boolean deleteStructure(StructureType structureType, int cdId) {
		// no-op
		return true;
	}

	@Override
	public boolean standardizedMolCompare(String queryMol, String targetMol) throws CmpdRegMolFormatException {
		IndigoObject queryMolecule = (new CmpdRegMoleculeIndigoImpl(queryMol)).molecule;
		queryMolecule.standardize();
		IndigoObject targetMolecule = (new CmpdRegMoleculeIndigoImpl(targetMol)).molecule;
		targetMolecule.standardize();
		return (indigo.exactMatch(queryMolecule, targetMolecule) != null);
	}

	@Override
	public StandardizerSettingsConfigDTO getStandardizerSettings() throws StandardizerException {
		// There are no standardizer settings for Indigo implmented so returning empty
		// string
		StandardizerSettingsConfigDTO standardizationConfigDTO = new StandardizerSettingsConfigDTO();
		standardizationConfigDTO.setSettings("");
		standardizationConfigDTO.setType("indigo");
		return standardizationConfigDTO;
	}

	@Override
	public HashMap<String, CmpdRegMolecule> standardizeStructures(HashMap<String, String> structures)
			throws CmpdRegMolFormatException, StandardizerException {
		HashMap<String, CmpdRegMolecule> standardizedStructures = new HashMap<String, CmpdRegMolecule>();
		for (String structureKey : structures.keySet()) {
			String structure = structures.get(structureKey);
			try {
				String standardizedStructure = standardizeStructure(structure);
				CmpdRegMoleculeIndigoImpl standardizedCmpdRegMolecule = new CmpdRegMoleculeIndigoImpl(
						standardizedStructure);
				standardizedStructures.put(structureKey, standardizedCmpdRegMolecule);
			} catch (Exception e) {
				logger.error("Got error trying to standardize structure " + structure, e);
				throw new CmpdRegMolFormatException(e);
			}
		}
		return standardizedStructures;
	}

	@Override
	public int saveStructure(CmpdRegMolecule cmpdregMolecule, StructureType structureType, boolean checkForDupes) {
		try {
			return saveStructure(cmpdregMolecule.getMolStructure(), structureType, checkForDupes);
		} catch (CmpdRegMolFormatException e) {
			logger.error("Got error trying to save structure", e);
			return -1;
		}
	}

	@Override
	public HashMap<String, Integer> saveStructures(HashMap<String, CmpdRegMolecule> structures,
			StructureType structureType, Boolean checkForDupes) {
		// return hash
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		for (String key : structures.keySet()) {
			CmpdRegMolecule cmpdRegMolecule = structures.get(key);
			result.put(key, saveStructure(cmpdRegMolecule, structureType, checkForDupes));
		}
		return result;
	}

	@Override
	public HashMap<String, CmpdRegMolecule> getCmpdRegMolecules(HashMap<String, Integer> keyIdToStructureId,
			StructureType structureType) throws CmpdRegMolFormatException {

		HashMap<String, CmpdRegMolecule> result = new HashMap<String, CmpdRegMolecule>();
		String baseQuery = "SELECT mol_structure FROM " + structureType.entityTable + " WHERE cd_id = :cd_id";
		EntityManager em = Parent.entityManager();
		Query query = em.createNativeQuery(baseQuery);
		for (String key : keyIdToStructureId.keySet()) {
			query.setParameter("cd_id", keyIdToStructureId.get(key));
			String molStructure = (String) query.getSingleResult();
			CmpdRegMoleculeIndigoImpl cmpdRegMolecule = new CmpdRegMoleculeIndigoImpl(molStructure);
			result.put(key, cmpdRegMolecule);
		}
		return result;
	}

	@Override
	public int[] searchMolStructures(CmpdRegMolecule cmpdRegMolecule, StructureType structureType,
			SearchType searchType, Float simlarityPercent, int maxResults) throws CmpdRegMolFormatException {
		return searchMolStructures(cmpdRegMolecule.getMolStructure(), structureType, searchType, simlarityPercent,
				maxResults);
	}

	@Override
	public void fillMissingStructures() throws CmpdRegMolFormatException {
		// Not currently implemented for indigo as there is no use case currently to
		// switch to indigo from another chemistry engine

	}


	@Override
	public StandardizationSettingsConfigCheckResponseDTO checkStandardizerSettings(StandardizationHistory mostRecentStandardizationHistory, StandardizerSettingsConfigDTO standardizationSettingsConfigDTO) {
		// Not currently implemented for indigo as standardization is not implmented so just returning no stanardization required
		StandardizationSettingsConfigCheckResponseDTO returnSettings = new StandardizationSettingsConfigCheckResponseDTO();
		returnSettings.setNeedsRestandardization(false);
		returnSettings.setValid(true);
		returnSettings.setValidatedSettings(standardizationSettingsConfigDTO.getSettings());
		return returnSettings;
	}
}
