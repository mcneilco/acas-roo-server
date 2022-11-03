package com.labsynch.labseer.chemclasses.jchem;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.Salt;
import com.labsynch.labseer.domain.StandardizationHistory;
import com.labsynch.labseer.dto.MolConvertOutputDTO;
import com.labsynch.labseer.dto.StandardizationSettingsConfigCheckResponseDTO;
import com.labsynch.labseer.dto.StrippedSaltDTO;
import com.labsynch.labseer.dto.configuration.StandardizerSettingsConfigDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.StandardizerException;
import com.labsynch.labseer.service.ChemStructureService;
import com.labsynch.labseer.utils.PropertiesUtilService;

import chemaxon.calculations.cip.CIPStereoCalculator;
import chemaxon.calculations.clean.Cleaner;
import chemaxon.calculations.hydrogenize.Hydrogenize;
import chemaxon.enumeration.supergraph.SupergraphException;
import chemaxon.formats.MolExporter;
import chemaxon.formats.MolFormatException;
import chemaxon.formats.MolImporter;
import chemaxon.jchem.db.CacheRegistrationUtil;
import chemaxon.jchem.db.DatabaseProperties;
import chemaxon.jchem.db.DatabaseSearchException;
import chemaxon.jchem.db.JChemSearch;
//import chemaxon.jchem.db.PropertyNotSetException;
import chemaxon.jchem.db.StructureTableOptions;
import chemaxon.jchem.db.UpdateHandler;
import chemaxon.license.LicenseException;
import chemaxon.sss.SearchConstants;
import chemaxon.sss.search.JChemSearchOptions;
import chemaxon.sss.search.MolSearch;
import chemaxon.sss.search.MolSearchOptions;
import chemaxon.sss.search.SearchException;
import chemaxon.sss.search.StandardizedMolSearch;
import chemaxon.standardizer.Standardizer;
import chemaxon.struc.CIPStereoDescriptorIface;
import chemaxon.struc.Molecule;
import chemaxon.struc.MoleculeGraph;
import chemaxon.util.ConnectionHandler;
import chemaxon.util.HitColoringAndAlignmentOptions;
import chemaxon.util.MolHandler;

import org.apache.commons.io.FileUtils;
import java.util.UUID;

@Component
public class ChemStructureServiceJChemImpl implements ChemStructureService {

	Logger logger = LoggerFactory.getLogger(ChemStructureServiceJChemImpl.class);

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

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	public boolean compareStructures(String preMolStruct, String postMolStruct, SearchType searchType) {

		// logger.info("SearchType is: " + searchType);
		boolean compoundsMatch = false;
		try {
			MolSearch molSearch = new MolSearch();
			MolHandler mhQuery = new MolHandler(preMolStruct);
			Molecule queryMol = mhQuery.getMolecule();
			MolHandler mhTarget = new MolHandler(postMolStruct);
			Molecule targetMol = mhTarget.getMolecule();

			molSearch.setQuery(queryMol);
			molSearch.setTarget(targetMol);
			MolSearchOptions options = new MolSearchOptions(MolSearchOptions.DUPLICATE);
			options.setTautomerSearch(MolSearchOptions.TAUTOMER_SEARCH_OFF);
			options.setChargeMatching(MolSearchOptions.CHARGE_MATCHING_EXACT);
			molSearch.setSearchOptions(options);
			compoundsMatch = molSearch.isMatching();
			if (!compoundsMatch) {
				logger.info(queryMol.toFormat("smiles"));
				logger.info(targetMol.toFormat("smiles"));
			}

		} catch (MolFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return compoundsMatch;
	}

	@Override
	public String standardizeStructure(String molfile) throws LicenseException, MolFormatException, IOException {
		// service to standardize input structure
		// return standardized structre
		// error conditions?
		// throw or catch errors
		// create Standardizer based on a XML configuration file
		String molOut = null;
		try {
			Standardizer standardizer = new Standardizer(
					new File(propertiesUtilService.getStandardizerConfigFilePath()));
			MolHandler mh = new MolHandler(molfile);
			Molecule molecule = mh.getMolecule();
			// standardize molecule
			standardizer.standardize(molecule);
			// export standardized molecule
			molOut = MolExporter.exportToFormat(molecule, "mol");

		} catch (MolFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return molOut;
	}

	public Molecule standardizeMolecule(Molecule molecule) {
		// service to standardize input structure
		// return standardized structure
		// create Standardizer based on a XML configuration file
		Standardizer standardizer = new Standardizer(new File(propertiesUtilService.getStandardizerConfigFilePath()));
		try {
			// standardize molecule
			standardizer.standardize(molecule);
			// export standardized molecule
		} catch (LicenseException e) {
			e.printStackTrace();
		}

		return molecule;
	}

	@Override
	public int saveStructure(String molfile, StructureType structureType) {
		boolean checkForDupes = false;
		return saveStructure(molfile, structureType, checkForDupes);
	}

	@Override
	public int saveStructure(String molfile, StructureType structureType, boolean checkForDupes) {
		String structureTable = getJchemStructureTableFromStructureType(structureType);
		if (logger.isDebugEnabled())
			logger.debug("saving structure " + molfile);
		if (logger.isDebugEnabled())
			logger.debug("saving structure to table " + structureTable);

		Connection conn = DataSourceUtils.getConnection(basicJdbcTemplate.getDataSource());
		ConnectionHandler ch = new ConnectionHandler();
		CacheRegistrationUtil cru = null;

		try {
			if (logger.isDebugEnabled())
				logger.debug("the connection closed: " + conn.isClosed());
			conn.setAutoCommit(true);
			ch.setConnection(conn);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			logger.error("the connection is closed");
			e1.printStackTrace();
		}

		UpdateHandler uh2;

		int cdId = 0;
		boolean badStructureFlag = false;

		MolHandler mh = null;
		try {
			mh = new MolHandler(molfile);
		} catch (MolFormatException e) {
			badStructureFlag = true;
		}

		boolean foundDupe = false;
		if (checkForDupes) {
			int[] hitCount = this.searchMolStructures(molfile, structureType, SearchType.DUPLICATE_TAUTOMER);
			if (hitCount.length > 0) {
				foundDupe = true;
			}
		}

		try {

			cru = new CacheRegistrationUtil(ch);
			if (structureType == StructureType.SALT_FORM) {
				String cacheIdentifier = "labsynch_saltform_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);

			} else if (structureType == StructureType.PARENT) {
				String cacheIdentifier = "labsynch_cmpd_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);

			} else if (structureType == StructureType.COMPOUND) {
				String cacheIdentifier = "labsynch_cmpd_dupe_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			} else if (structureType == StructureType.SALT) {
				String cacheIdentifier = "labsynch_salt_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			} else if (structureType == StructureType.STANDARDIZATION_DRY_RUN) {
				String cacheIdentifier = "labsync_standardization_dry_run_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			}

			String cacheID = CacheRegistrationUtil.getCacheID();
			if (logger.isDebugEnabled())
				logger.debug("current cache ID: " + cacheID);
			if (logger.isDebugEnabled())
				logger.debug("cache status: " + cru.isCacheIDRegistered(cacheID));

			uh2 = new UpdateHandler(ch,
					UpdateHandler.INSERT, structureTable, "");

			if (!badStructureFlag && !foundDupe) {
				Molecule mol = mh.getMolecule();
				mol.aromatize();
				uh2.setStructure(MolExporter.exportToFormat(mol, "mol"));
				uh2.setEmptyStructuresAllowed(true);

				cdId = uh2.execute(true);
				uh2.saveUpdateLogs();
			} else if (foundDupe) {
				cdId = 0;
			} else {
				if (logger.isDebugEnabled())
					logger.debug("offending molformat:  " + molfile);
				cdId = -1;
			}

			uh2.close();

			if (this.shouldCloseConnection) {
				ch.close();
				conn.close();
			}

		} catch (SQLException e) {
			logger.error("Caught SQLException saving structure to " + structureTable + " table.", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cdId;
	}

	@Override
	public void closeConnection() {

		if (this.shouldCloseConnection) {

			Connection conn = DataSourceUtils.getConnection(basicJdbcTemplate.getDataSource());
			ConnectionHandler ch = new ConnectionHandler();
			ch.setConnection(conn);

			try {
				ch.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("closed the connection");
		}

	}

	@Override
	public int[] searchMolStructures(String molfile, StructureType structureType, SearchType searchType) {
		return searchMolStructures(molfile, structureType, searchType, 0f);
	}

	@Override
	public int[] searchMolStructures(String molfile, StructureType structureType, SearchType searchType,
			Float simlarityPercent) {
		int maxResultCount = propertiesUtilService.getMaxSearchResults();
		return searchMolStructures(molfile, structureType, searchType, simlarityPercent, maxResultCount);

	}

	@Override
	@Transactional
	public int[] searchMolStructures(String molfile, StructureType structureType, SearchType searchType,
			Float simlarityPercent, int maxResults) {

		Connection conn = DataSourceUtils.getConnection(basicJdbcTemplate.getDataSource());
		try {
			conn.setAutoCommit(true);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ConnectionHandler ch = new ConnectionHandler();
		ch.setConnection(conn);

		CacheRegistrationUtil cru = null;
		long maxTime = propertiesUtilService.getMaxSearchTime();
		int maxResultCount = maxResults;
		String structureTable = getJchemStructureTableFromStructureType(structureType);
		if (logger.isDebugEnabled())
			logger.debug("Search table is  " + structureTable);
		if (logger.isDebugEnabled())
			logger.debug("Search type is  " + searchType);
		if (logger.isDebugEnabled())
			logger.debug("Max number of results is  " + maxResults);

		if (searchType == SearchType.EXACT) {
			searchType = propertiesUtilService.getExactMatchDef();
		}

		MolHandler mh = null;
		JChemSearch searcher = null;

		try {
			cru = new CacheRegistrationUtil(ch);
			if (structureType == StructureType.SALT_FORM) {
				String cacheIdentifier = "labsynch_saltform_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			} else if (structureType == StructureType.SALT) {
				String cacheIdentifier = "labsynch_salt_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			} else if (structureType == StructureType.COMPOUND) {
				String cacheIdentifier = "labsynch_cmpd_dupe_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			} else if (structureType == StructureType.DRY_RUN) {
				String cacheIdentifier = "labsynch_dr_cmpd_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			} else if (structureType == StructureType.STANDARDIZATION_DRY_RUN) {
				String cacheIdentifier = "labsynch_standardization_dry_run_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			} else {
				String cacheIdentifier = "labsynch_cmpd_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			}

			String cacheID = CacheRegistrationUtil.getCacheID();
			if (logger.isDebugEnabled())
				logger.debug("current cache ID: " + cacheID);
			if (logger.isDebugEnabled())
				logger.debug("cache status: " + cru.isCacheIDRegistered(cacheID));

			mh = new MolHandler(molfile);
			Molecule mol = mh.getMolecule();

			if (propertiesUtilService.getUseExternalStandardizerConfig()) {
				mol = standardizeMolecule(mol);
				mol.aromatize();
			} else {
				mol.aromatize();
			}

			searcher = new JChemSearch();
			searcher.setQueryStructure(mol);
			searcher.setConnectionHandler(ch);
			searcher.setStructureTable(structureTable);

			if (logger.isDebugEnabled())
				logger.debug("definition of exact search: " + propertiesUtilService.getExactMatchDef());
			if (logger.isDebugEnabled())
				logger.debug("selected search type: " + searchType);

			JChemSearchOptions searchOptions = null;
			if (searchType == SearchType.DUPLICATE) {
				if (logger.isDebugEnabled())
					logger.debug("search type is DUPLICATE  ");
				searchOptions = new JChemSearchOptions(SearchConstants.DUPLICATE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_OFF);

			} else if (searchType == SearchType.DUPLICATE_TAUTOMER) {
				if (logger.isDebugEnabled())
					logger.debug("Search type is DUPLICATE_TAUTOMER");
				searchOptions = new JChemSearchOptions(SearchConstants.DUPLICATE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_ON);

			} else if (searchType == SearchType.DUPLICATE_NO_TAUTOMER) {
				if (logger.isDebugEnabled())
					logger.debug("Search type is DUPLICATE_NO_TAUTOMER");
				searchOptions = new JChemSearchOptions(SearchConstants.DUPLICATE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_OFF);

			} else if (searchType == SearchType.STEREO_IGNORE) {
				if (logger.isDebugEnabled())
					logger.debug("Search type is no stereo");
				searchOptions = new JChemSearchOptions(SearchConstants.STEREO_IGNORE);
				searchOptions.setStereoSearchType(JChemSearchOptions.STEREO_IGNORE);

			} else if (searchType == SearchType.FULL_TAUTOMER) {
				if (logger.isDebugEnabled())
					logger.debug("Search type is exact FULL_TAUTOMER");
				searchOptions = new JChemSearchOptions(SearchConstants.FULL);
				searchOptions.setChargeMatching(JChemSearchOptions.CHARGE_MATCHING_IGNORE);
				searchOptions.setIsotopeMatching(JChemSearchOptions.ISOTOPE_MATCHING_IGNORE);
				searchOptions.setStereoSearchType(JChemSearchOptions.STEREO_IGNORE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_ON);

			} else if (searchType == SearchType.SUBSTRUCTURE) {
				if (logger.isDebugEnabled())
					logger.debug("Search type is substructure");
				searchOptions = new JChemSearchOptions(SearchConstants.SUBSTRUCTURE);

			} else if (searchType == SearchType.SIMILARITY) {
				searchOptions = new JChemSearchOptions(SearchConstants.SIMILARITY);
				searchOptions.setDissimilarityThreshold(simlarityPercent);
				// searchOptions.setMaxTime(maxTime);
			} else if (searchType == SearchType.FULL) {
				if (logger.isDebugEnabled())
					logger.debug("Selected Search type is full with no tautomer search");
				searchOptions = new JChemSearchOptions(SearchConstants.FULL);
				searchOptions.setChargeMatching(JChemSearchOptions.CHARGE_MATCHING_IGNORE);
				searchOptions.setIsotopeMatching(JChemSearchOptions.ISOTOPE_MATCHING_IGNORE);
				searchOptions.setStereoSearchType(JChemSearchOptions.STEREO_IGNORE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_OFF);
			} else {
				if (logger.isDebugEnabled())
					logger.debug("Default Search type is full with tautomer search");
				searchOptions = new JChemSearchOptions(SearchConstants.FULL);
				searchOptions.setChargeMatching(JChemSearchOptions.CHARGE_MATCHING_IGNORE);
				searchOptions.setIsotopeMatching(JChemSearchOptions.ISOTOPE_MATCHING_IGNORE);
				searchOptions.setStereoSearchType(JChemSearchOptions.STEREO_IGNORE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_ON);
			}

			if (searchType == SearchType.SUBSTRUCTURE) {
				searchOptions.setFilterQuery("select cd_id from " + structureType.entityTable + " where id > 0");
			}
			if (maxResultCount > -1) {
				searchOptions.setMaxResultCount(maxResultCount);
			}
			searcher.setOrder(JChemSearch.ORDERING_BY_ID_OR_SIMILARITY);
			searcher.setSearchOptions(searchOptions);
			searcher.setRunMode(JChemSearch.RUN_MODE_SYNCH_COMPLETE);
			searcher.run();

			if (this.shouldCloseConnection) {
				ch.close();
				conn.close();
			}

		} catch (MolFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabaseSearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			e.printStackTrace();
		}

		int[] hitList = searcher.getResults();

		if (hitList.length > 0) {
			if (logger.isDebugEnabled())
				logger.debug("found a matching molecule!!!  " + hitList.length);
		}

		return hitList;
	}

	@Override
	public boolean checkForSalt(String molfile) throws CmpdRegMolFormatException {
		try {
			MolHandler mh = new MolHandler(molfile);
			Molecule mol = mh.getMolecule();
			int fragCount = mol.getFragCount(MoleculeGraph.FRAG_BASIC);
			boolean foundNonCovalentSalt = false;
			if (fragCount > 1) {
				foundNonCovalentSalt = true;
			}

			return foundNonCovalentSalt;
		} catch (Exception e) {
			throw new CmpdRegMolFormatException(e);
		}
	}

	@Override
	public StrippedSaltDTO stripSalts(CmpdRegMolecule inputStructure) {
		CmpdRegMoleculeJChemImpl molWrapper = (CmpdRegMoleculeJChemImpl) inputStructure;
		Molecule mol = molWrapper.molecule;
		Molecule clone = mol.clone();
		Molecule[] rawFrags = clone.findFrags(Molecule.class, MoleculeGraph.FRAG_BASIC);
		List<Molecule> allFrags = new ArrayList<Molecule>();
		for (Molecule fragment : rawFrags) {
			allFrags.add(fragment);
		}
		Map<Salt, Integer> saltCounts = new HashMap<Salt, Integer>();
		Set<Molecule> unidentifiedFragments = new HashSet<Molecule>();
		for (Molecule fragment : allFrags) {
			int[] cdIdMatches = searchMolStructures(fragment.toFormat("mol"), StructureType.SALT,
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

		Set<CmpdRegMolecule> unidentifiedFragmentMolecules = new HashSet<CmpdRegMolecule>();
		for (Molecule fragment : unidentifiedFragments) {
			CmpdRegMoleculeJChemImpl fragMol = new CmpdRegMoleculeJChemImpl(fragment);
			unidentifiedFragmentMolecules.add(fragMol);
		}

		StrippedSaltDTO resultDTO = new StrippedSaltDTO();
		resultDTO.setSaltCounts(saltCounts);
		resultDTO.setUnidentifiedFragments(unidentifiedFragmentMolecules);
		if (logger.isDebugEnabled())
			logger.debug("Identified stripped salts:");
		for (Salt salt : saltCounts.keySet()) {
			if (logger.isDebugEnabled())
				logger.debug("Salt Abbrev: " + salt.getAbbrev());
			if (logger.isDebugEnabled())
				logger.debug("Salt Count: " + saltCounts.get(salt));
		}
		return resultDTO;
	}

	@Override
	@Transactional
	public CmpdRegMolecule[] searchMols(String molfile, StructureType structureType, int[] inputCdIdHitList,
			SearchType searchType, Float simlarityPercent) {

		Connection conn = DataSourceUtils.getConnection(basicJdbcTemplate.getDataSource());
		ConnectionHandler ch = new ConnectionHandler();
		ch.setConnection(conn);
		try {
			conn.setAutoCommit(true);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		CacheRegistrationUtil cru = null;

		long maxTime = propertiesUtilService.getMaxSearchTime();
		int maxResultCount = propertiesUtilService.getMaxSearchResults();
		String structureTable = getJchemStructureTableFromStructureType(structureType);
		if (logger.isDebugEnabled())
			logger.debug("Search table is  " + structureTable);
		if (logger.isDebugEnabled())
			logger.debug("Search type is  " + searchType);
		if (logger.isDebugEnabled())
			logger.debug("search mol is: " + molfile);

		if (searchType == SearchType.EXACT) {
			searchType = propertiesUtilService.getExactMatchDef();
		}

		MolHandler mh = null;
		JChemSearch searcher = null;
		Molecule[] hitList = null;

		try {
			cru = new CacheRegistrationUtil(ch);
			if (structureType == StructureType.SALT_FORM) {
				String cacheIdentifier = "labsynch_saltform_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			} else if (structureType == StructureType.SALT) {
				String cacheIdentifier = "labsynch_salt_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			} else if (structureType == StructureType.STANDARDIZATION_DRY_RUN) {
				String cacheIdentifier = "labsynch_standardization_dry_run_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			} else {
				String cacheIdentifier = "labsynch_cmpd_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			}

			String cacheID = CacheRegistrationUtil.getCacheID();
			if (logger.isDebugEnabled())
				logger.debug("current cache ID: " + cacheID);
			if (logger.isDebugEnabled())
				logger.debug("cache status: " + cru.isCacheIDRegistered(cacheID));

			mh = new MolHandler(molfile);
			Molecule mol = mh.getMolecule();
			mol.aromatize();
			searcher = new JChemSearch();
			searcher.setQueryStructure(mol);
			searcher.setConnectionHandler(ch);
			searcher.setStructureTable(structureTable);
			JChemSearchOptions searchOptions = null;

			if (logger.isDebugEnabled())
				logger.debug("definition of exact search: " + propertiesUtilService.getExactMatchDef());
			if (logger.isDebugEnabled())
				logger.debug("selected search type: " + searchType);

			HitColoringAndAlignmentOptions hitColorOptions = null;
			if (searchType == SearchType.DUPLICATE) {
				searchOptions = new JChemSearchOptions(SearchConstants.DUPLICATE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_OFF);
				if (logger.isDebugEnabled())
					logger.debug("selected DUPLICATE search for " + searchType);

			} else if (searchType == SearchType.DUPLICATE_TAUTOMER) {
				System.out.println("Search type is DUPLICATE_TAUTOMER");
				searchOptions = new JChemSearchOptions(SearchConstants.DUPLICATE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_ON);

			} else if (searchType == SearchType.DUPLICATE_NO_TAUTOMER) {
				System.out.println("Search type is DUPLICATE_NO_TAUTOMER");
				searchOptions = new JChemSearchOptions(SearchConstants.DUPLICATE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_OFF);

			} else if (searchType == SearchType.STEREO_IGNORE) {
				System.out.println("Search type is  no stereo");
				searchOptions = new JChemSearchOptions(SearchConstants.STEREO_IGNORE);
				searchOptions.setStereoSearchType(JChemSearchOptions.STEREO_IGNORE);

			} else if (searchType == SearchType.FULL_TAUTOMER) {
				System.out.println("Search type is exact FULL_TAUTOMER");
				searchOptions = new JChemSearchOptions(SearchConstants.FULL);
				searchOptions.setChargeMatching(JChemSearchOptions.CHARGE_MATCHING_IGNORE);
				searchOptions.setIsotopeMatching(JChemSearchOptions.ISOTOPE_MATCHING_IGNORE);
				searchOptions.setStereoSearchType(JChemSearchOptions.STEREO_IGNORE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_ON);

			} else if (searchType == SearchType.SUBSTRUCTURE) {
				System.out.println("Search type is substructure");
				searchOptions = new JChemSearchOptions(SearchConstants.SUBSTRUCTURE);
				// One can also specify coloring and alignment options
				hitColorOptions = new HitColoringAndAlignmentOptions();
				boolean coloringEnabled = true;
				Color hitColor = Color.blue;
				Color nonHitColor = Color.black;
				hitColorOptions.coloring = true;
				hitColorOptions.hitColor = hitColor;
				hitColorOptions.nonHitColor = nonHitColor;
				// hitColorOptions.setColoringEnabled(coloringEnabled);
				// hitColorOptions.setHitColor(hitColor);
				// hitColorOptions.setNonHitColor(nonHitColor);

			} else if (searchType == SearchType.SIMILARITY) {
				searchOptions = new JChemSearchOptions(SearchConstants.SIMILARITY);
				searchOptions.setDissimilarityThreshold(simlarityPercent);
				// searchOptions.setMaxTime(maxTime);
				hitColorOptions = new HitColoringAndAlignmentOptions();
				boolean coloringEnabled = true;
				Color hitColor = Color.blue;
				Color nonHitColor = Color.black;
				// hitColorOptions.coloring = true;
				// hitColorOptions.hitColor = hitColor;
				// hitColorOptions.nonHitColor = nonHitColor;
				hitColorOptions.setColoringEnabled(coloringEnabled);
				hitColorOptions.setHitColor(hitColor);
				hitColorOptions.setNonHitColor(nonHitColor);

			} else if (searchType == SearchType.FULL) {
				if (logger.isDebugEnabled())
					logger.debug("Default Search type is full with no tautomer search");
				searchOptions = new JChemSearchOptions(SearchConstants.FULL);
				searchOptions.setChargeMatching(JChemSearchOptions.CHARGE_MATCHING_IGNORE);
				searchOptions.setIsotopeMatching(JChemSearchOptions.ISOTOPE_MATCHING_IGNORE);
				searchOptions.setStereoSearchType(JChemSearchOptions.STEREO_IGNORE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_OFF);
			} else {
				if (logger.isDebugEnabled())
					logger.debug("Default Search type is exact");
				searchOptions = new JChemSearchOptions(SearchConstants.FULL);
				searchOptions.setChargeMatching(JChemSearchOptions.CHARGE_MATCHING_IGNORE);
				searchOptions.setIsotopeMatching(JChemSearchOptions.ISOTOPE_MATCHING_IGNORE);
				searchOptions.setStereoSearchType(JChemSearchOptions.STEREO_IGNORE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_OFF);
			}

			if (inputCdIdHitList != null && inputCdIdHitList.length > 0) {
				searcher.setFilterIDList(inputCdIdHitList);
			} else if (inputCdIdHitList == null && searchType == SearchType.SUBSTRUCTURE) {
				searchOptions.setFilterQuery("select cd_id from " + structureType.entityTable + " where id > 0");
			}

			if (logger.isDebugEnabled())
				logger.debug("max result count is: " + maxResultCount);
			if (logger.isDebugEnabled())
				logger.debug("JChemSearch.ORDERING_BY_ID_OR_SIMILARITY: " + JChemSearch.ORDERING_BY_ID_OR_SIMILARITY);
			if (logger.isDebugEnabled())
				logger.debug("JChemSearch.RUN_MODE_SYNCH_COMPLETE: " + JChemSearch.RUN_MODE_SYNCH_COMPLETE);

			searchOptions.setMaxResultCount(maxResultCount);
			searcher.setOrder(JChemSearch.ORDERING_BY_ID_OR_SIMILARITY);
			searcher.setSearchOptions(searchOptions);
			searcher.setRunMode(JChemSearch.RUN_MODE_SYNCH_COMPLETE);
			searcher.run();

			// Specifying database fields to retrieve
			List<String> fieldNames = new ArrayList<String>();
			fieldNames.add("cd_id");
			fieldNames.add("cd_formula");
			fieldNames.add("cd_molweight");

			// ArrayList for returned database field values
			// List<Object[]> fieldValues = new ArrayList<Object[]>();
			List<Object[]> fieldValues = new ArrayList<Object[]>();

			// Retrieving result molecules // filedValues will be also filled!

			int[] hitList_cdId = searcher.getResults();

			logger.info("Found number of hits: " + hitList_cdId.length);

			hitList = searcher.getHitsAsMolecules(hitList_cdId, hitColorOptions, fieldNames, fieldValues);

			for (int i = 0; i < hitList_cdId.length; i++) {
				System.out.println("ID: " + hitList_cdId[i]);
				hitList[i].setProperty("cd_id", Integer.toString(hitList_cdId[i]));
				Object[] values = (Object[]) fieldValues.get(i);
				for (int j = 0; j < values.length; j++) {
					System.out.println(fieldNames.get(j) + ": " + values[j]);
				}
				System.out.println();
			}

			if (hitList.length > 0) {
				if (logger.isDebugEnabled())
					logger.debug("found a matching molecule!!!  " + hitList.length);
			}

			if (this.shouldCloseConnection) {
				ch.close();
				conn.close();
			}

		} catch (MolFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabaseSearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SupergraphException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<CmpdRegMolecule> resultList = new ArrayList<CmpdRegMolecule>();
		for (Molecule hit : hitList) {
			resultList.add(new CmpdRegMoleculeJChemImpl(hit));
		}
		CmpdRegMolecule[] resultArray = new CmpdRegMolecule[resultList.size()];

		return resultList.toArray(resultArray);
	}

	@Override
	@Transactional
	public CmpdRegMolecule[] searchMols(String molfile, StructureType structureType, int[] inputCdIdHitList,
			SearchType searchType, Float simlarityPercent, int maxResults) {

		Connection conn = DataSourceUtils.getConnection(basicJdbcTemplate.getDataSource());
		ConnectionHandler ch = new ConnectionHandler();
		ch.setConnection(conn);
		try {
			conn.setAutoCommit(true);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		CacheRegistrationUtil cru = null;

		long maxTime = propertiesUtilService.getMaxSearchTime();
		int maxResultCount = maxResults;

		String structureTable = getJchemStructureTableFromStructureType(structureType);
		if (logger.isDebugEnabled())
			logger.debug("Search table is  " + structureTable);
		if (logger.isDebugEnabled())
			logger.debug("Search type is  " + searchType);
		if (logger.isDebugEnabled())
			logger.debug("search mol is: " + molfile);

		if (searchType == SearchType.EXACT) {
			searchType = propertiesUtilService.getExactMatchDef();
		}

		MolHandler mh = null;
		JChemSearch searcher = null;
		Molecule[] hitList = null;

		try {
			cru = new CacheRegistrationUtil(ch);
			if (structureType == StructureType.SALT_FORM) {
				String cacheIdentifier = "labsynch_saltform_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			} else if (structureType == StructureType.SALT) {
				String cacheIdentifier = "labsynch_salt_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			} else if (structureType == StructureType.STANDARDIZATION_DRY_RUN) {
				String cacheIdentifier = "labsynch_standardization_dry_run_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			} else {
				String cacheIdentifier = "labsynch_cmpd_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			}

			String cacheID = CacheRegistrationUtil.getCacheID();
			if (logger.isDebugEnabled())
				logger.debug("current cache ID: " + cacheID);
			if (logger.isDebugEnabled())
				logger.debug("cache status: " + cru.isCacheIDRegistered(cacheID));

			mh = new MolHandler(molfile);
			Molecule mol = mh.getMolecule();
			mol.aromatize();
			searcher = new JChemSearch();
			searcher.setQueryStructure(mol);
			searcher.setConnectionHandler(ch);
			searcher.setStructureTable(structureTable);
			JChemSearchOptions searchOptions = null;

			if (logger.isDebugEnabled())
				logger.debug("definition of exact search: " + propertiesUtilService.getExactMatchDef());
			if (logger.isDebugEnabled())
				logger.debug("selected search type: " + searchType);

			HitColoringAndAlignmentOptions hitColorOptions = null;
			if (searchType == SearchType.DUPLICATE) {
				searchOptions = new JChemSearchOptions(SearchConstants.DUPLICATE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_OFF);
				if (logger.isDebugEnabled())
					logger.debug("selected DUPLICATE search for " + searchType);

			} else if (searchType == SearchType.DUPLICATE_TAUTOMER) {
				System.out.println("Search type is DUPLICATE_TAUTOMER");
				searchOptions = new JChemSearchOptions(SearchConstants.DUPLICATE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_ON);

			} else if (searchType == SearchType.DUPLICATE_NO_TAUTOMER) {
				System.out.println("Search type is DUPLICATE_NO_TAUTOMER");
				searchOptions = new JChemSearchOptions(SearchConstants.DUPLICATE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_OFF);

			} else if (searchType == SearchType.STEREO_IGNORE) {
				System.out.println("Search type is  no stereo");
				searchOptions = new JChemSearchOptions(SearchConstants.STEREO_IGNORE);
				searchOptions.setStereoSearchType(JChemSearchOptions.STEREO_IGNORE);

			} else if (searchType == SearchType.FULL_TAUTOMER) {
				System.out.println("Search type is exact FULL_TAUTOMER");
				searchOptions = new JChemSearchOptions(SearchConstants.FULL);
				searchOptions.setChargeMatching(JChemSearchOptions.CHARGE_MATCHING_IGNORE);
				searchOptions.setIsotopeMatching(JChemSearchOptions.ISOTOPE_MATCHING_IGNORE);
				searchOptions.setStereoSearchType(JChemSearchOptions.STEREO_IGNORE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_ON);

			} else if (searchType == SearchType.SUBSTRUCTURE) {
				System.out.println("Search type is substructure");
				searchOptions = new JChemSearchOptions(SearchConstants.SUBSTRUCTURE);
				// One can also specify coloring and alignment options
				hitColorOptions = new HitColoringAndAlignmentOptions();
				boolean coloringEnabled = true;
				Color hitColor = Color.blue;
				Color nonHitColor = Color.black;
				hitColorOptions.coloring = true;
				hitColorOptions.hitColor = hitColor;
				hitColorOptions.nonHitColor = nonHitColor;
				// hitColorOptions.setColoringEnabled(coloringEnabled);
				// hitColorOptions.setHitColor(hitColor);
				// hitColorOptions.setNonHitColor(nonHitColor);

			} else if (searchType == SearchType.SIMILARITY) {
				searchOptions = new JChemSearchOptions(SearchConstants.SIMILARITY);
				searchOptions.setDissimilarityThreshold(simlarityPercent);
				// searchOptions.setMaxTime(maxTime);
				hitColorOptions = new HitColoringAndAlignmentOptions();
				boolean coloringEnabled = true;
				Color hitColor = Color.blue;
				Color nonHitColor = Color.black;
				// hitColorOptions.coloring = true;
				// hitColorOptions.hitColor = hitColor;
				// hitColorOptions.nonHitColor = nonHitColor;
				hitColorOptions.setColoringEnabled(coloringEnabled);
				hitColorOptions.setHitColor(hitColor);
				hitColorOptions.setNonHitColor(nonHitColor);

			} else if (searchType == SearchType.FULL) {
				if (logger.isDebugEnabled())
					logger.debug("Default Search type is full with no tautomer search");
				searchOptions = new JChemSearchOptions(SearchConstants.FULL);
				searchOptions.setChargeMatching(JChemSearchOptions.CHARGE_MATCHING_IGNORE);
				searchOptions.setIsotopeMatching(JChemSearchOptions.ISOTOPE_MATCHING_IGNORE);
				searchOptions.setStereoSearchType(JChemSearchOptions.STEREO_IGNORE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_OFF);
			} else {
				if (logger.isDebugEnabled())
					logger.debug("Default Search type is exact");
				searchOptions = new JChemSearchOptions(SearchConstants.FULL);
				searchOptions.setChargeMatching(JChemSearchOptions.CHARGE_MATCHING_IGNORE);
				searchOptions.setIsotopeMatching(JChemSearchOptions.ISOTOPE_MATCHING_IGNORE);
				searchOptions.setStereoSearchType(JChemSearchOptions.STEREO_IGNORE);
				searchOptions.setTautomerSearch(SearchConstants.TAUTOMER_SEARCH_OFF);
			}

			if (inputCdIdHitList != null && inputCdIdHitList.length > 0) {
				searcher.setFilterIDList(inputCdIdHitList);
			} else if (inputCdIdHitList == null && searchType == SearchType.SUBSTRUCTURE) {
				searchOptions.setFilterQuery("select cd_id from " + structureType.entityTable + " where id > 0");
			}

			if (logger.isDebugEnabled())
				logger.debug("max result count is: " + maxResultCount);
			if (logger.isDebugEnabled())
				logger.debug("JChemSearch.ORDERING_BY_ID_OR_SIMILARITY: " + JChemSearch.ORDERING_BY_ID_OR_SIMILARITY);
			if (logger.isDebugEnabled())
				logger.debug("JChemSearch.RUN_MODE_SYNCH_COMPLETE: " + JChemSearch.RUN_MODE_SYNCH_COMPLETE);

			searchOptions.setMaxResultCount(maxResultCount);
			searcher.setOrder(JChemSearch.ORDERING_BY_ID_OR_SIMILARITY);
			searcher.setSearchOptions(searchOptions);
			searcher.setRunMode(JChemSearch.RUN_MODE_SYNCH_COMPLETE);
			searcher.run();

			// Specifying database fields to retrieve
			List<String> fieldNames = new ArrayList<String>();
			fieldNames.add("cd_id");
			fieldNames.add("cd_formula");
			fieldNames.add("cd_molweight");

			// ArrayList for returned database field values
			// List<Object[]> fieldValues = new ArrayList<Object[]>();
			List<Object[]> fieldValues = new ArrayList<Object[]>();

			// Retrieving result molecules // filedValues will be also filled!

			int[] hitList_cdId = searcher.getResults();

			if (hitList_cdId.length > 0) {
				hitList = searcher.getHitsAsMolecules(hitList_cdId, hitColorOptions, fieldNames, fieldValues);
			}

			for (int i = 0; i < hitList_cdId.length; i++) {
				System.out.println("ID: " + hitList_cdId[i]);
				hitList[i].setProperty("cd_id", Integer.toString(hitList_cdId[i]));
				Object[] values = (Object[]) fieldValues.get(i);
				for (int j = 0; j < values.length; j++) {
					System.out.println(fieldNames.get(j) + ": " + values[j]);
				}
				System.out.println();
			}

			if (hitList != null && hitList.length > 0) {
				if (logger.isDebugEnabled())
					logger.debug("found a matching molecule!!!  " + hitList.length);
			}

			if (this.shouldCloseConnection) {
				ch.close();
				conn.close();
			}

		} catch (MolFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabaseSearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SearchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SupergraphException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<CmpdRegMolecule> resultList = new ArrayList<CmpdRegMolecule>();
		if (hitList != null) {
			for (Molecule hit : hitList) {
				resultList.add(new CmpdRegMoleculeJChemImpl(hit));
			}
		}
		CmpdRegMolecule[] resultArray = new CmpdRegMolecule[resultList.size()];

		return resultList.toArray(resultArray);
	}

	@Override
	public CmpdRegMoleculeJChemImpl toMolecule(String molStructure) {
		MolHandler mh = null;
		boolean badStructureFlag = false;
		Molecule mol = null;
		String lineEnd = System.getProperty("line.separator");
		try {
			mh = new MolHandler(molStructure);
			mol = mh.getMolecule();
		} catch (MolFormatException e1) {
			if (logger.isDebugEnabled())
				logger.debug("failed first attempt: bad mol structure: " + molStructure);
			// clean up the molString and try again
			try {
				molStructure = new StringBuilder().append(lineEnd).append(molStructure).append(lineEnd).toString();
				mh = new MolHandler(molStructure);
				mol = mh.getMolecule();
			} catch (MolFormatException e2) {
				if (logger.isDebugEnabled())
					logger.debug("failed second attempt: bad mol structure: " + molStructure);
				badStructureFlag = true;
				logger.error("bad mol structure: " + molStructure);
			}
		}

		if (!badStructureFlag) {
			return new CmpdRegMoleculeJChemImpl(mol);
		} else {
			return null;
		}
	}

	@Override
	public String toMolfile(String molStructure) {
		if (logger.isDebugEnabled())
			logger.debug("here is the incoming molStructure: " + molStructure);
		MolHandler mh = null;
		boolean badStructureFlag = false;
		Molecule mol = null;
		String lineEnd = System.getProperty("line.separator");
		try {
			// molStructure = new
			// StringBuilder().append(lineEnd).append(molStructure).append(lineEnd).toString();
			mh = new MolHandler(molStructure);
			mol = mh.getMolecule();
			mol.dearomatize();
		} catch (MolFormatException e1) {
			if (logger.isDebugEnabled())
				logger.debug("failed first attempt: bad mol structure: " + molStructure);
			// clean up the molString and try again
			try {
				molStructure = new StringBuilder().append(lineEnd).append(molStructure).append(lineEnd).toString();
				mh = new MolHandler(molStructure);
				mol = mh.getMolecule();
				mol.dearomatize();
			} catch (MolFormatException e2) {
				if (logger.isDebugEnabled())
					logger.debug("failed second attempt: bad mol structure: " + molStructure);
				badStructureFlag = true;
				logger.error("bad mol structure: " + molStructure);
			}
		}
		if (logger.isDebugEnabled())
			logger.debug("The badStructureFlag " + badStructureFlag);
		if (logger.isDebugEnabled())
			logger.debug("The molfile " + mol.toFormat("mol"));

		if (!badStructureFlag) {
			return mol.toFormat("mol");
		} else {
			return molStructure;
		}
	}

	@Override
	public String getCipStereo(String structure) throws IOException {
		MolHandler mh = new MolHandler(structure);
		Molecule mol = mh.getMolecule();
		Collection<CIPStereoDescriptorIface> output = CIPStereoCalculator.calculateCIPStereoDescriptors(mol);

		if (logger.isDebugEnabled()) {
			for (CIPStereoDescriptorIface single : output) {
				if (logger.isDebugEnabled())
					logger.debug(single.toString());
			}
		}

		return output.toString();
	}

	@Override
	public String hydrogenizeMol(String structure, String inputFormat, String method) throws IOException {
		MolHandler mh = new MolHandler(structure);
		Molecule mol = mh.getMolecule();
		if (method.equalsIgnoreCase("HYDROGENIZE")) {
			Hydrogenize.convertImplicitHToExplicit(mol);
		} else {
			Hydrogenize.convertExplicitHToImplicit(mol);
		}
		return MolExporter.exportToFormat(mol, "mrv");
	}

	@Override
	public MolConvertOutputDTO cleanStructure(String structure, int dim, String opts) throws IOException {
		boolean badStructureFlag = false;
		MolConvertOutputDTO output = new MolConvertOutputDTO();
		Molecule mol = null;
		try {
			mol = MolImporter.importMol(structure);
		} catch (MolFormatException e) {
			badStructureFlag = true;
		}
		if (!badStructureFlag) {
			Cleaner.clean(mol, dim, opts);
			output.setStructure(MolExporter.exportToFormat(mol, "mrv"));
			output.setFormat("mrv");
		}
		return output;
	}

	@Override
	public MolConvertOutputDTO toFormat(String structure, String inputFormat, String outputFormat) throws IOException {
		boolean badStructureFlag = false;
		Molecule mol = null;
		try {
			mol = MolImporter.importMol(structure, inputFormat);
		} catch (MolFormatException e) {
			badStructureFlag = true;
		}

		MolConvertOutputDTO output = new MolConvertOutputDTO();
		if (!badStructureFlag) {
			output.setStructure(MolExporter.exportToFormat(mol, outputFormat));
			output.setFormat(outputFormat);
			String contentUrl = "TO DO: Download Link";
			output.setContentUrl(contentUrl);
		}

		return output;
	}

	@Override
	public String toInchi(String molStructure) {
		MolHandler mh = null;
		boolean badStructureFlag = false;
		Molecule mol = null;
		try {
			mh = new MolHandler(molStructure);
			mol = mh.getMolecule();
		} catch (MolFormatException e) {
			badStructureFlag = true;
		}

		if (!badStructureFlag) {
			return mol.toFormat("inchi");
		} else {
			return molStructure;
		}
	}

	@Override
	public String toSmiles(String molStructure) {
		MolHandler mh = null;
		boolean badStructureFlag = false;
		Molecule mol = null;
		try {
			mh = new MolHandler(molStructure);
			mol = mh.getMolecule();
		} catch (MolFormatException e) {
			badStructureFlag = true;
		}

		if (!badStructureFlag) {
			return mol.toFormat("smiles");
		} else {
			return molStructure;
		}
	}

	@Override
	public double getMolWeight(String molStructure) {
		MolHandler mh = null;
		boolean badStructureFlag = false;
		Molecule mol = null;
		try {
			mh = new MolHandler(molStructure);
			mol = mh.getMolecule();
		} catch (MolFormatException e) {
			badStructureFlag = true;
		}

		if (!badStructureFlag) {
			return mol.getMass();
		} else {
			return 0d;
		}
	}

	@Override
	public double getExactMass(String molStructure) {
		MolHandler mh = null;
		boolean badStructureFlag = false;
		Molecule mol = null;
		try {
			mh = new MolHandler(molStructure);
			mol = mh.getMolecule();
		} catch (MolFormatException e) {
			badStructureFlag = true;
		}

		if (!badStructureFlag) {
			return mol.getExactMass();
		} else {
			return 0d;
		}
	}

	@Override
	public String getMolFormula(String molStructure) {
		MolHandler mh = null;
		boolean badStructureFlag = false;
		Molecule mol = null;
		try {
			mh = new MolHandler(molStructure);
			mol = mh.getMolecule();
		} catch (MolFormatException e) {
			badStructureFlag = true;
		}

		if (!badStructureFlag) {
			return mol.getFormula();
		} else {
			return null;
		}
	}

	public boolean createJChemTable(String tableName, boolean tautomerDupe) {
		boolean tableCreated = false;
		Connection conn = DataSourceUtils.getConnection(basicJdbcTemplate.getDataSource());
		ConnectionHandler ch = new ConnectionHandler();
		ch.setConnection(conn);
		try {
			conn.setAutoCommit(true);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		StructureTableOptions options = new StructureTableOptions(tableName,
				StructureTableOptions.TABLE_TYPE_MOLECULES);
		options.setTautomerDuplicateChecking(tautomerDupe);

		try {
			String[] tables = UpdateHandler.getStructureTables(ch);
			List<String> tableList = Arrays.asList(tables);
			if (!tableList.contains(tableName)) {
				UpdateHandler.createStructureTable(ch, options);
				logger.info("created the Jchem structure table " + tableName);
				tableCreated = true;
			}
		} catch (SQLException e) {
			logger.error("SQL error. Unable to create the Jchem structure table " + tableName);
			e.printStackTrace();
		}

		return tableCreated;
	}

	@Transactional
	public boolean dropJChemTable(String tableName) {
		boolean tableDropped = false;
		Connection conn = DataSourceUtils.getConnection(basicJdbcTemplate.getDataSource());
		ConnectionHandler ch = new ConnectionHandler();
		ch.setConnection(conn);
		logger.info("is the connection active: " + ch.isConnected());
		try {
			conn.setAutoCommit(true);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			String[] tables = UpdateHandler.getStructureTables(ch);
			List<String> tableList = Arrays.asList(tables);
			for (String table : tableList) {
				logger.info("tables: " + table);
			}
			String matchingTable = null;
			for (String t : tables) {
				if (t.toLowerCase().contains(tableName.toLowerCase())) {
					matchingTable = t;
					break;
				}
			}
			if (matchingTable != null) {
				UpdateHandler.dropStructureTable(ch, matchingTable);
			} else {
				logger.info(tableName + " does not exist");
			}
			tables = UpdateHandler.getStructureTables(ch);
			tableList = Arrays.asList(tables);
			if (tableList.contains(tableName)) {
				tableDropped = false;
			} else {
				tableDropped = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return tableDropped;
	}

	public String getJchemStructureTableFromStructureType(StructureType structureType) {
		String plainTable = null;
		if (structureType == StructureType.PARENT) {
			plainTable = "PARENT_STRUCTURE";
		} else if (structureType == StructureType.SALT_FORM) {
			plainTable = "SALTFORM_STRUCTURE";
		} else if (structureType == StructureType.SALT) {
			plainTable = "SALT_STRUCTURE";
		} else if (structureType == StructureType.DRY_RUN) {
			plainTable = "DRY_RUN_COMPOUND_STRUCTURE";
		} else if (structureType == StructureType.STANDARDIZATION_DRY_RUN) {
			plainTable = "STANDARDIZATION_DRY_RUN_STRUCTURE";
		}
		return (plainTable);
	}

	@Override
	public boolean truncateStructureTable(StructureType structureType) {
		String tableName = getJchemStructureTableFromStructureType(structureType);
		Boolean dropTable = dropJChemTable(tableName);
		if (!dropTable) {
			logger.info("Unable to drop jchem table " + tableName);
		}
		return createJChemTable(tableName, true);
	}

	@Override
	public int[] checkDupeMol(String molStructure, StructureType structureType) {

		return searchMolStructures(molStructure, structureType, SearchType.DUPLICATE_TAUTOMER);
	}

	@Override
	public boolean updateStructure(String molStructure, StructureType structureType, int cdId) {

		Connection conn = DataSourceUtils.getConnection(basicJdbcTemplate.getDataSource());
		ConnectionHandler ch = new ConnectionHandler();
		CacheRegistrationUtil cru = null;

		boolean updatedStructure = false;

		try {
			logger.debug("the connection closed: " + conn.isClosed());
			conn.setAutoCommit(true);
			ch.setConnection(conn);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			logger.error("the connection is closed");
			e1.printStackTrace();
		}

		UpdateHandler uh2;
		boolean badStructureFlag = false;

		MolHandler mh = null;
		String lineEnd = System.getProperty("line.separator");

		try {
			mh = new MolHandler(toMolfile(molStructure));
		} catch (MolFormatException e) {
			try {
				logger.info("adding more new lines");
				molStructure = lineEnd + lineEnd + molStructure;
				mh = new MolHandler(molStructure);
			} catch (MolFormatException e2) {
				badStructureFlag = true;
			}

		}

		if (logger.isDebugEnabled())
			logger.debug("attempt to update structure: " + molStructure);

		try {

			cru = new CacheRegistrationUtil(ch);
			if (structureType == StructureType.SALT_FORM) {
				String cacheIdentifier = "labsynch_saltform_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);

			} else if (structureType == StructureType.PARENT) {
				String cacheIdentifier = "labsynch_cmpd_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);

			} else if (structureType == StructureType.SALT) {
				String cacheIdentifier = "labsynch_salt_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			} else if (structureType == StructureType.STANDARDIZATION_DRY_RUN) {
				String cacheIdentifier = "labsynch_standardization_dry_run_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			}

			String cacheID = CacheRegistrationUtil.getCacheID();
			if (logger.isDebugEnabled())
				logger.debug("current cache ID: " + cacheID);
			if (logger.isDebugEnabled())
				logger.debug("cache status: " + cru.isCacheIDRegistered(cacheID));

			String structureTable = getJchemStructureTableFromStructureType(structureType);
			uh2 = new UpdateHandler(ch,
					UpdateHandler.UPDATE, structureTable, "");

			if (!badStructureFlag) {
				Molecule mol = mh.getMolecule();
				mol.aromatize();
				uh2.setStructure(MolExporter.exportToFormat(mol, "mol"));
				uh2.setEmptyStructuresAllowed(true);
				uh2.setID(cdId);
				uh2.execute(true);
				uh2.saveUpdateLogs();
				updatedStructure = true;
			} else {
				if (logger.isDebugEnabled())
					logger.debug("offending molformat:  " + molStructure);
				updatedStructure = false;
			}

			uh2.close();
			if (this.shouldCloseConnection) {
				ch.close();
				conn.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return updatedStructure;
	}

	@Override
	public boolean updateStructure(CmpdRegMolecule molecule, StructureType structureType, int cdId) {
		CmpdRegMoleculeJChemImpl molWrapper = (CmpdRegMoleculeJChemImpl) molecule;
		Molecule mol = molWrapper.molecule;
		Connection conn = DataSourceUtils.getConnection(basicJdbcTemplate.getDataSource());
		ConnectionHandler ch = new ConnectionHandler();
		CacheRegistrationUtil cru = null;
		String structureTable = getJchemStructureTableFromStructureType(structureType);
		boolean updatedStructure = false;

		try {
			logger.debug("the connection closed: " + conn.isClosed());
			conn.setAutoCommit(true);
			ch.setConnection(conn);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			logger.error("the connection is closed");
			e1.printStackTrace();
		}

		UpdateHandler uh2;
		boolean badStructureFlag = false;

		try {

			cru = new CacheRegistrationUtil(ch);
			if (structureType == StructureType.SALT_FORM) {
				String cacheIdentifier = "labsynch_saltform_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			} else if (structureType == StructureType.PARENT) {
				String cacheIdentifier = "labsynch_cmpd_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			} else if (structureType == StructureType.SALT) {
				String cacheIdentifier = "labsynch_salt_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			} else if (structureType == StructureType.STANDARDIZATION_DRY_RUN) {
				String cacheIdentifier = "labsynch_standardization_dry_run_cache";
				if (!cru.isCacheIDRegistered(cacheIdentifier)) {
					cru.registerPermanentCache(cacheIdentifier);
				}
				CacheRegistrationUtil.setPermanentCacheID(cacheIdentifier);
			}

			String cacheID = CacheRegistrationUtil.getCacheID();
			if (logger.isDebugEnabled())
				logger.debug("current cache ID: " + cacheID);
			if (logger.isDebugEnabled())
				logger.debug("cache status: " + cru.isCacheIDRegistered(cacheID));

			uh2 = new UpdateHandler(ch,
					UpdateHandler.UPDATE, structureTable, "");

			if (!badStructureFlag) {
				mol.aromatize();
				uh2.setStructure(MolExporter.exportToFormat(mol, "mol"));
				uh2.setEmptyStructuresAllowed(true);
				uh2.setID(cdId);

				// This does not throw and error if the structure id does not exist
				uh2.execute(true);
				uh2.saveUpdateLogs();

				// Hashmap to fetch structure id
				// Verify that the structure exists

				HashMap<String, Integer> structureIdMap = new HashMap<String, Integer>();
				structureIdMap.put("structureId", cdId);
				try {
					HashMap<String, CmpdRegMolecule> cmpdRegMolecule = getCmpdRegMolecules(structureIdMap,
							structureType);
					// Verify that the structure exists
					if (cmpdRegMolecule.size() > 0) {
						updatedStructure = true;
					} else {
						updatedStructure = false;
					}
				} catch (Exception e) {
					logger.error("Error in retrieving structure id: " + e.getMessage());
				}
			} else {
				if (logger.isDebugEnabled())
					logger.debug("offending molformat:  " + MolExporter.exportToFormat(mol, "mol"));
				updatedStructure = false;
			}

			uh2.close();
			if (this.shouldCloseConnection) {
				ch.close();
				conn.close();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return updatedStructure;
	}

	@Override
	public boolean deleteStructure(StructureType structureType, int cdId) {
		Connection conn = DataSourceUtils.getConnection(basicJdbcTemplate.getDataSource());
		ConnectionHandler ch = new ConnectionHandler();
		CacheRegistrationUtil cru = null;

		boolean deleteSuccessful = false;

		try {
			logger.debug("the connection closed: " + conn.isClosed());
			conn.setAutoCommit(true);
			ch.setConnection(conn);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			logger.error("the connection is closed");
			e1.printStackTrace();
		}
		String structureTable = getJchemStructureTableFromStructureType(structureType);
		try {
			UpdateHandler.deleteRow(ch, structureTable, cdId);
			deleteSuccessful = true;
		} catch (Exception e) {
			logger.error("Caught exception trying to delete structure from " + structureTable + " with cdId " + cdId,
					e);
			deleteSuccessful = false;
		}
		return deleteSuccessful;
	}

	@Override
	public boolean isEmpty(String molStructure) {
		MolHandler mh = null;
		boolean badStructureFlag = false;
		Molecule mol = null;
		try {
			mh = new MolHandler(molStructure);
			mol = mh.getMolecule();
		} catch (MolFormatException e) {
			badStructureFlag = true;
		}

		if (!badStructureFlag) {
			return mol.isEmpty();
		} else {
			return true;
		}
	}

	@Override
	public boolean standardizedMolCompare(String queryMol, String targetMol) throws CmpdRegMolFormatException {
		try {
			// If both mols are empty then they are equal
			if (isEmpty(queryMol) && isEmpty(targetMol))
				return true;
			CmpdRegMoleculeJChemImpl queryMolWrapper = new CmpdRegMoleculeJChemImpl(queryMol);
			CmpdRegMoleculeJChemImpl targetMolWrapper = new CmpdRegMoleculeJChemImpl(queryMol);
			StandardizedMolSearch molSearch = new StandardizedMolSearch();
			molSearch.setQuery(queryMolWrapper.molecule);
			molSearch.setTarget(targetMolWrapper.molecule);
			int[][] hits = null;
			hits = molSearch.findAll();
			return false;
		} catch (MolFormatException e) {
			throw new CmpdRegMolFormatException(e);
		} catch (SearchException e2) {
			// TODO: throw better error
			throw new CmpdRegMolFormatException(e2);
		}
	}

	@Override
	public StandardizerSettingsConfigDTO getStandardizerSettings(Boolean appliedSettings) throws StandardizerException {
		// Applied settings are always the same as the configured settings because we don't have a validation step
		// which can be applied to the settings for the JChem standardizer
		StandardizerSettingsConfigDTO standardizationConfigDTO = new StandardizerSettingsConfigDTO();
		standardizationConfigDTO.setType("jchem");
		standardizationConfigDTO.setShouldStandardize(false);

		// Check if standardizer turned on
		if (propertiesUtilService.getUseExternalStandardizerConfig()) {
			standardizationConfigDTO.setShouldStandardize(true);
			File standardizationSettingsFile = new File(propertiesUtilService.getStandardizerConfigFilePath());
			String standardizerSettingsFileContents = "";
			try {
				standardizerSettingsFileContents = FileUtils.readFileToString(standardizationSettingsFile);
				standardizationConfigDTO.setSettings(standardizerSettingsFileContents);
			} catch (IOException e) {
				throw new StandardizerException("Got error trying to read jchem standardizer file at "
						+ standardizationSettingsFile.getAbsolutePath(), e);
			}
		}
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
				CmpdRegMoleculeJChemImpl standardizedCmpdRegMolecule = new CmpdRegMoleculeJChemImpl(
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
	@Transactional
	public HashMap<String, CmpdRegMolecule> getCmpdRegMolecules(HashMap<String, Integer> keyIdToStructureId,
			StructureType structureType) throws CmpdRegMolFormatException {

		// Get hashmap values as int array
		int[] cdIds = new int[keyIdToStructureId.size()];
		int c = 0;
		for (Integer cdId : keyIdToStructureId.values()) {
			cdIds[c] = cdId;
			c++;
		}

		Connection conn = DataSourceUtils.getConnection(basicJdbcTemplate.getDataSource());
		ConnectionHandler ch = new ConnectionHandler();
		ch.setConnection(conn);
		CacheRegistrationUtil cru = null;

		// Search for cdids and fetch cooresponding molecules
		HashMap<String, CmpdRegMolecule> result = new HashMap<String, CmpdRegMolecule>();
		JChemSearch searcher = new JChemSearch();
		String structureTable = getJchemStructureTableFromStructureType(structureType);
		searcher.setStructureTable(structureTable);
		searcher.setFilterIDList(cdIds);
		searcher.setConnectionHandler(ch);

		try {

			searcher.run();
			int[] hitList = searcher.getResults();
			HitColoringAndAlignmentOptions hitColorOptions = null;
			List<String> fieldNames = new ArrayList<String>();
			fieldNames.add("cd_id");
			fieldNames.add("cd_formula");
			fieldNames.add("cd_molweight");
			List<Object[]> fieldValues = new ArrayList<Object[]>();
			String[] keys = keyIdToStructureId.keySet().toArray(new String[0]);
			Molecule[] molecules;

			molecules = searcher.getHitsAsMolecules(hitList, hitColorOptions, fieldNames, fieldValues);
			for (int i = 0; i < hitList.length; i++) {
				molecules[i].setProperty("cd_id", Integer.toString(hitList[i]));
				result.put(keys[i], new CmpdRegMoleculeJChemImpl(molecules[i]));
			}

			return result;
		} catch (SQLException | IOException | SearchException | SupergraphException | DatabaseSearchException e) {
			throw new CmpdRegMolFormatException(e);
		} finally {
			if (this.shouldCloseConnection) {
				try {
					ch.close();
					conn.close();
				} catch (SQLException e) {
					logger.error("Got error trying to close connection", e);
				}
			}
		}
	}

	@Override
	public int[] searchMolStructures(CmpdRegMolecule cmpdRegMolecule, StructureType structureType,
			SearchType searchType, Float simlarityPercent, int maxResults) throws CmpdRegMolFormatException {
		return searchMolStructures(cmpdRegMolecule.getMolStructure(), structureType, searchType, simlarityPercent,
				maxResults);
	}

	@Override
	public void fillMissingStructures() throws CmpdRegMolFormatException {
		// Not currently implemented for jchem as there is no use case currently to
		// switch to jchem from another chemistry engine

	}

	@Override
	public StandardizationSettingsConfigCheckResponseDTO checkStandardizerSettings(StandardizationHistory mostRecentStandardizationHistory, StandardizerSettingsConfigDTO standardizationSettingsConfigDTO) {
			StandardizationSettingsConfigCheckResponseDTO returnSettings = new StandardizationSettingsConfigCheckResponseDTO();
			Boolean hashesMatch = mostRecentStandardizationHistory.getSettingsHash() == standardizationSettingsConfigDTO.hashCode();
			returnSettings.setNeedsRestandardization(hashesMatch);
			if(returnSettings.getNeedsRestandardization()) {
				returnSettings.addNeedsRestandardizationReason(("Settings hash changed from " + mostRecentStandardizationHistory.getSettingsHash() + " to " + standardizationSettingsConfigDTO.hashCode()));
			}
			returnSettings.setValid(true);
			returnSettings.setValidatedSettings(standardizationSettingsConfigDTO.getSettings());
			return returnSettings;
	}

}
