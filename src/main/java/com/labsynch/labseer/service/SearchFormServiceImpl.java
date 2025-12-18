package com.labsynch.labseer.service;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.TypedQuery;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.LotAlias;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.ParentAlias;
import com.labsynch.labseer.domain.SaltForm;
import com.labsynch.labseer.dto.LotAliasDTO;
import com.labsynch.labseer.dto.ParentAliasDTO;
import com.labsynch.labseer.dto.SearchCdIdReturnDTO;
import com.labsynch.labseer.dto.SearchCompoundReturnDTO;
import com.labsynch.labseer.dto.SearchFormDTO;
import com.labsynch.labseer.dto.SearchFormReturnDTO;
import com.labsynch.labseer.dto.SearchLotDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.service.ChemStructureService.SearchType;
import com.labsynch.labseer.service.ChemStructureService.StructureType;
import com.labsynch.labseer.utils.PropertiesUtilService;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SearchFormServiceImpl implements SearchFormService {

	Logger logger = LoggerFactory.getLogger(SearchFormServiceImpl.class);

	@Autowired
	private CorpNameService corpNameService;

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Autowired
	private ChemStructureService structureService;

	@Override
	@Transactional
	public String findParentIds(String molStructure,
			int maxResults, Float similarity, SearchType searchType,
			String outputFormat) throws IOException, CmpdRegMolFormatException {

		// options for outputFormat -- corpname, cdid, corpname-cdid, sdf ; default is
		// cdid

		logger.info("incoming searchType is: " + searchType);

		int[] parentStructureHits = structureService.searchMolStructures(molStructure, StructureType.PARENT, searchType,
				similarity, maxResults);

		logger.debug("Number of parentStructureHits = " + parentStructureHits.length);

		Collection<SearchCdIdReturnDTO> resultList = new ArrayList<SearchCdIdReturnDTO>();
		SearchCdIdReturnDTO result;
		for (int hit : parentStructureHits) {
			result = new SearchCdIdReturnDTO();
			result.setCdId(hit);

			logger.debug("query cdId is " + hit);
			List<Parent> parents = Parent.findParentsByCdId(hit).getResultList();
			logger.debug("number of parents found: " + parents.size());
			if (parents.size() > 0) {
				for (Parent parent : parents) {
					if (parent.getIgnore() == null || !parent.getIgnore()) {
						result.setCorpName(parent.getCorpName());
						resultList.add(result);
					}
				}
			}
		}

		StringWriter outputWriter = new StringWriter();

		try {

			// write header
			String headerLine;

			if (outputFormat.equalsIgnoreCase("corpname")) {
				headerLine = "Corporate_Name\n";
			} else if (outputFormat.equalsIgnoreCase("corpname-cdid")) {
				headerLine = "Corporate_Name,cd_id\n";
			} else {
				headerLine = "cd_id\n";
			}

			outputWriter.write(headerLine);

			logger.info("number of search results found: " + resultList.size());

			for (SearchCdIdReturnDTO searchResult : resultList) {
				logger.info(searchResult.toJson());
				if (outputFormat.equalsIgnoreCase("corpname")) {
					outputWriter.append(searchResult.getCorpName());
					outputWriter.append("\n");
				} else if (outputFormat.equalsIgnoreCase("corpname-cdid")) {
					outputWriter.append(searchResult.getCorpName());
					outputWriter.append(",");
					outputWriter.append(Integer.toString(searchResult.getCdId()));
					outputWriter.append("\n");
				} else {
					outputWriter.append(Integer.toString(searchResult.getCdId()));
					outputWriter.append("\n");
				}

			}

		} finally {
			outputWriter.close();

		}

		return outputWriter.toString();
	}

	@Override
	@Transactional
	public SearchFormReturnDTO findQuerySaltForms(SearchFormDTO searchParams) throws CmpdRegMolFormatException {

		logger.debug("incoming search params: " + searchParams);

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		if (!searchParams.getDateFrom().equals("")) {
			try {
				searchParams.setMinSynthDate(df.parse(searchParams.getDateFrom()));
				logger.debug("Min Synth Date = " + searchParams.getMinSynthDate());
				searchParams.setValuesSet(true);
			} catch (ParseException e) {
				logger.error("Could not parse the fromDate" + searchParams.getDateFrom());
				e.printStackTrace();
			}
		}
		if (!searchParams.getDateTo().equals("")) {
			try {
				searchParams.setMaxSynthDate(df.parse(searchParams.getDateTo()));
				searchParams.setValuesSet(true);
			} catch (ParseException e) {
				logger.error("Could not parse the toDate" + searchParams.getDateTo());
				e.printStackTrace();
			}
		}

		SearchType matchedSearchType = SearchType.getIfPresent(searchParams.getSearchType()).orElse(SearchType.DEFAULT);
		if (matchedSearchType == SearchType.SIMILARITY) {
			float similarity = searchParams.getPercentSimilarity();
			logger.debug("original similarity = " + similarity);

			if (similarity < 10) {
				similarity = 10;
			}

			similarity = 100.0f - similarity;
			logger.debug("dissimilarity = " + similarity);
			similarity = similarity * 0.01f;
			logger.debug("dissimilarity = " + similarity);
			searchParams.setPercentSimilarity(similarity);
		}

		List<SaltForm> saltForms = new ArrayList<SaltForm>();
		List<SearchCompoundReturnDTO> foundCompounds = null;
		HashMap<String, SaltForm> saltFormMols = new HashMap<String, SaltForm>();
		HashMap<String, SaltForm> parentMols = new HashMap<String, SaltForm>();

		logger.debug("here is the self built query string: " + this.buildMetaQuery(searchParams, StructureType.PARENT));

		if (!searchParams.getCorpNameList().equals("")) {
			logger.info("got a corp name list search!");
			String[] inputListArray = searchParams.getCorpNameList().split("[\\s,;\\n\\r]+");
			HashSet<String> uniqueCorpNames = new HashSet<>();
			for (String corpName : inputListArray) {
				uniqueCorpNames.add(corpName);
				// Remove the prefix, lot.
				String parentNumber = corpNameService.parseParentNumber(corpName).toString();
				uniqueCorpNames.add(parentNumber);
			}
			List<String> corpNames = new ArrayList<String>(uniqueCorpNames);
			logger.info(corpNames.toString());
			searchParams.setFormattedCorpNameList(corpNames);
		}
		// Check if corpNameFrom and corpNameTo are both set -- will search for a range
		// of parents if both set
		// Check if single corpName is submitted. Convert corpName to either parent,
		// saltForm, or Lot name

		if (!searchParams.getCorpNameFrom().equals("") && !searchParams.getCorpNameTo().equals("")) {
			searchParams.setValuesSet(true);
			logger.debug("parentFrom: check corpName " + searchParams.getCorpNameFrom());
			logger.debug("parentTo: check corpName " + searchParams.getCorpNameTo());

			Long minParent = 0L;
			if (corpNameService.checkCorpNumber(searchParams.getCorpNameFrom())) {
				logger.debug("this look like a number: " + searchParams.getCorpNameFrom());
				minParent = corpNameService.parseCorpNumber(searchParams.getCorpNameFrom());
				searchParams.setMinParentNumber(minParent);
			}
			if (corpNameService.checkBuidNumber(searchParams.getCorpNameFrom())) {
				logger.debug("Oops: trying to search for a range of BUID numbers. Not supported. "
						+ searchParams.getCorpNameFrom());
				// force the search on the bad corp name to prevent a cartesian search
				searchParams.setParentCorpName(searchParams.getCorpNameFrom());
			} else {
				String corpName = searchParams.getCorpNameFrom();
				minParent = corpNameService.convertToParentNumber(corpName);
				searchParams.setMinParentNumber(minParent);
			}
			logger.debug("parentFrom: " + minParent);

			Long maxParent = 0L;
			String corpNameTo = searchParams.getCorpNameTo();
			// searchParams.setCorpNameTo(""); //null the corpNameTo value ---> only
			// implement parent number range

			if (minParent != 0) {
				searchParams.setMaxParentNumber(minParent);
				if (corpNameService.checkCorpNumber(corpNameTo)) {
					logger.debug("corpNameTo looks like a number. " + searchParams.getCorpNameTo());
					maxParent = corpNameService.parseCorpNumber(corpNameTo);
					logger.debug("parentTo: " + maxParent);
					searchParams.setMaxParentNumber(maxParent);
				} else {
					String corpName = searchParams.getCorpNameTo();
					logger.debug("query corpNameTo: " + searchParams.getCorpNameTo());
					maxParent = corpNameService.convertToParentNumber(corpName);
					searchParams.setMaxParentNumber(maxParent);
					logger.debug("parentTo: " + searchParams.getMaxParentNumber());

				}

			}
			logger.debug("parentTo: " + maxParent);

		} else if (!searchParams.getCorpNameFrom().equals("") && searchParams.getCorpNameTo().equals("")) {
			logger.debug("searching with From corpname: " + searchParams.getCorpNameFrom());
			searchParams.setValuesSet(true);
			String corpName = searchParams.getCorpNameFrom();
			Long minParent = 0L;
			if (corpNameService.checkCorpNumber(searchParams.getCorpNameFrom())) {
				logger.debug("this look like a number: " + searchParams.getCorpNameFrom());
				minParent = corpNameService.parseCorpNumber(searchParams.getCorpNameFrom());
				searchParams.setMinParentNumber(minParent);
				searchParams.setMaxParentNumber(minParent);
			} else if (corpNameService.checkBuidNumber(corpName)) {
				Long buidNumber = corpNameService.convertToBuidNumber(corpName);
				searchParams.setBuidNumber(buidNumber);
				logger.debug("Converted BUID Number = " + corpName);
			} else {
				if (searchParams.getCorpNameFrom().matches("^.*?" + propertiesUtilService.getCorpSeparator()
						+ "([0-9]{1,9})" + propertiesUtilService.getBatchSeparator() + ".*?$")) {
					searchParams.setLotCorpName(searchParams.getCorpNameFrom());
				} else if (searchParams.getCorpNameFrom().startsWith(propertiesUtilService.getCorpPrefix())
						|| isNumber(searchParams.getCorpNameFrom())) {
					Long corpNumber = corpNameService.convertToParentNumber(corpName);
					corpName = corpNameService.convertCorpNameNumber(corpNumber.toString());
					searchParams.setParentCorpName(corpName);
					logger.debug("Converted corpName = " + corpName);
				} else {
					searchParams.setParentCorpName(searchParams.getCorpNameFrom().toUpperCase().trim());
				}
			}

		} else if (searchParams.getCorpNameFrom().equals("") && !searchParams.getCorpNameTo().equals("")) {
			searchParams.setValuesSet(true);
			String corpName = searchParams.getCorpNameTo();
			Long corpNumber = corpNameService.convertToParentNumber(corpName);
			corpName = corpNameService.convertCorpNameNumber(corpNumber.toString());
			searchParams.setParentCorpName(corpName);
			logger.debug("Converted corpName = " + corpName);
		}

		if (!searchParams.getAlias().trim().equalsIgnoreCase("")) {
			searchParams.setValuesSet(true);
		}

		if (searchParams.getChemist() != null && !searchParams.getChemist().equals("anyone")) {
			searchParams.setValuesSet(true);
		}

		// 2. All other meta params will flow into the searches
		// if no search structure, we will only search with the meta data

		if (searchParams.getMolStructure() == null || searchParams.getMolStructure().equals("")) {
			foundCompounds = new ArrayList<SearchCompoundReturnDTO>();
			// search by meta only
			List<SaltForm> saltFormsFound = SaltForm.findSaltFormsByMeta(searchParams).getResultList();
			for (SaltForm saltForm : saltFormsFound) {
				if (!saltForms.contains(saltForm)) {
					saltForms.add(saltForm);
				}
			}
			//
			logger.debug("Number of saltForms found:  " + saltFormsFound.size());
			logger.debug("Number of unique saltForms found:  " + saltForms.size());

			logger.debug("search params: ---- " + searchParams.toJson());

			for (SaltForm saltForm : saltForms) {
				SearchCompoundReturnDTO searchCompound = new SearchCompoundReturnDTO();
				if (saltForm.getCdId() == 0) {
					if (propertiesUtilService.getSaltBeforeLot()) {
						searchCompound.setCorpName(saltForm.getCorpName());
						searchCompound.setCorpNameType("SaltForm");
					} else {
						searchCompound.setCorpName(saltForm.getParent().getCorpName());
						searchCompound.setCorpNameType("Parent");
					}
					List<ParentAliasDTO> parentAliases = new ArrayList<ParentAliasDTO>();
					for (ParentAlias parentAlias : saltForm.getParent().getParentAliases()) {
						if (!parentAlias.isIgnored()) {
							ParentAliasDTO parentAliasDTO = new ParentAliasDTO(parentAlias);
							parentAliases.add(parentAliasDTO);
						}
					}
					searchCompound.setParentAliases(parentAliases);
					searchCompound.setMolStructure(saltForm.getParent().getMolStructure());
				} else {
					searchCompound.setCorpName(saltForm.getCorpName());
					searchCompound.setCorpNameType("SaltForm");
					searchCompound.setMolStructure(saltForm.getMolStructure());
				}
				logger.debug("current query saltForm: " + saltForm.toJson());
				// logger.debug("current query searchParams: " + searchParams.toJson());

				List<Lot> lotsFound = Lot.findLotsBySaltFormAndMeta(saltForm, searchParams).getResultList();
				List<Lot> lots = new ArrayList<Lot>();
				for (Lot lot : lotsFound) {
					if (!lots.contains(lot))
						lots.add(lot);
				}
				logger.debug("current saltForm: " + saltForm.getCorpName() + "  " + saltForm.getId());
				logger.debug("number of lots found: " + lotsFound.size());
				logger.debug("number of unique lots found: " + lots.size());
				for (Lot lot : lots) {
					logger.debug("found lot: " + lot.toJson());
					SearchLotDTO searchLot = new SearchLotDTO();
					searchLot.setCorpName(lot.getCorpName());
					searchLot.setLotNumber(lot.getLotNumber());
					searchLot.setRegistrationDate(lot.getRegistrationDate());
					searchLot.setSynthesisDate(lot.getSynthesisDate());
					searchCompound.getLotIDs().add(searchLot);
				}
				if (saltForm.getParent().getStereoCategory() != null) {
					if (saltForm.getParent().getStereoCategory().getName() != null) {
						searchCompound.setStereoCategoryName(saltForm.getParent().getStereoCategory().getName());
					}
				}
				if (saltForm.getParent().getStereoComment() != null) {
					searchCompound.setStereoComment(saltForm.getParent().getStereoComment());
				}
				foundCompounds.add(searchCompound);
			}

			logger.debug("found Compounds from MetaSearch: " + foundCompounds.size());
			// logger.debug("found Compounds: " +
			// SearchCompoundReturnDTO.toJsonArray(foundCompounds));

		} else {
			// do the structure search --> saltForm followed by parent structures
			// get saltFormCdIds to search on
			// get parentCdIds to search on
			// the mol search will return the mols in search order
			// loop through the set of found parent mols first (larger set)
			// then loop through the results of the saltForm structure search
			foundCompounds = new ArrayList<SearchCompoundReturnDTO>();
			int[] filterCdIds = null;

			List<Integer> saltFormCdIds = SaltForm.findSaltFormCdIdsByMeta(searchParams);
			if (saltFormCdIds != null && saltFormCdIds.size() > 0) {
				filterCdIds = ArrayUtils.toPrimitive(saltFormCdIds.toArray(new Integer[saltFormCdIds.size()]));
				logger.debug("list of filter saltForm cdIds found: " + saltFormCdIds.toString());
			} else if (searchParams.isValuesSet() && saltFormCdIds.size() == 0) {
				saltFormCdIds.add(0);
				filterCdIds = ArrayUtils.toPrimitive(saltFormCdIds.toArray(new Integer[saltFormCdIds.size()]));
			} else {
				filterCdIds = null;
			}
			logger.debug(StructureType.SALT_FORM + "   search type:= " + searchParams.getSearchType());
			// logger.debug("Query Structure Smiles: " +
			// structureService.toSmiles(searchParams.getMolStructure()));

			CmpdRegMolecule[] saltFormStructureHits;
			if (searchParams.getMaxResults() != null) {
				saltFormStructureHits = structureService.searchMols(searchParams.getMolStructure(),
						StructureType.SALT_FORM, filterCdIds,
						matchedSearchType, searchParams.getPercentSimilarity(), searchParams.getMaxResults());
			} else {
				saltFormStructureHits = structureService.searchMols(searchParams.getMolStructure(),
						StructureType.SALT_FORM, filterCdIds,
						matchedSearchType, searchParams.getPercentSimilarity());
			}

			logger.debug("found: " + saltFormStructureHits.length + " saltForm structure hits");

			for (CmpdRegMolecule hitMol : saltFormStructureHits) {
				logger.debug("did we find a saltForm hit: " + hitMol.toString());
				int hitCdId = Integer.parseInt(hitMol.getProperty("cd_id").trim());
				SaltForm saltForm = SaltForm.findSaltFormsByCdId(hitCdId).getSingleResult();
				// saltForm.setMolStructure(hitMol.toFormat("mrv"));
				saltForm.setMolStructure(hitMol.getSmiles());
				saltFormMols.put(saltForm.getCorpName(), saltForm);
				saltForms.add(saltForm);
			}

			List<Integer> parentCdIds = Parent.findParentCdIdsByMeta(searchParams);

			if (parentCdIds != null && parentCdIds.size() > 0) {
				filterCdIds = ArrayUtils.toPrimitive(parentCdIds.toArray(new Integer[parentCdIds.size()]));
				;
				logger.debug("list of parent filter cdIds found: " + parentCdIds.toString());
			} else if (searchParams.isValuesSet() && parentCdIds.size() == 0) {
				parentCdIds.add(0);
				filterCdIds = ArrayUtils.toPrimitive(parentCdIds.toArray(new Integer[parentCdIds.size()]));
				;
			} else {
				logger.debug("no filter cdIds found");
				filterCdIds = null;
			}

			logger.debug(StructureType.PARENT + "   search type:= " + searchParams.getSearchType());

			CmpdRegMolecule[] parentStructureHits;
			if (searchParams.getMaxResults() != null) {
				parentStructureHits = structureService.searchMols(searchParams.getMolStructure(), StructureType.PARENT,
						filterCdIds,
						matchedSearchType, searchParams.getPercentSimilarity(), searchParams.getMaxResults());
			} else {
				parentStructureHits = structureService.searchMols(searchParams.getMolStructure(), StructureType.PARENT,
						filterCdIds,
						matchedSearchType, searchParams.getPercentSimilarity());
			}

			if (parentStructureHits != null) {

				logger.debug("found: " + parentStructureHits.length + " parent structure hits");

				for (CmpdRegMolecule hitMol : parentStructureHits) {
					int hitCdId = Integer.parseInt(hitMol.getProperty("cd_id").trim());
					logger.debug("query parent hitCdId  " + hitCdId);
					List<Parent> parents = Parent.findParentsByCdId(hitCdId).getResultList();
					logger.debug("number of parents found: " + parents.size());
					Parent parent = parents.get(0);
					logger.debug("found this parent: " + parent.getCorpName());
					logger.debug("search Parms are: " + searchParams.toJson());
					List<Long> saltFormIds = SaltForm.findSaltFormIDsByParentAndMeta(parent, searchParams);
					logger.debug("number of saltForms found: " + saltFormIds.size());
					// logger.debug("saltForm IDs found: " + saltFormIds.toString());

					for (Long saltFormId : saltFormIds) {
						SaltForm saltForm = SaltForm.findSaltForm(saltFormId);
						logger.debug("found: " + saltForm.getCorpName());
						logger.debug("found: " + saltForm.getParent().getCorpName());
						logger.debug("found: " + ParentAlias.toJsonArray(saltForm.getParent().getParentAliases()));
						logger.debug("found: " + saltForm.getParent().getStereoCategory());
						logger.debug("found: " + saltForm.getParent().getStereoCategory().getName());

						parentMols.put(saltForm.getCorpName(), saltForm);
						SearchCompoundReturnDTO searchCompound = new SearchCompoundReturnDTO();
						// searchCompound.setCorpName(saltForm.getParent().getCorpName());
						List<ParentAliasDTO> parentAliases = new ArrayList<ParentAliasDTO>();
						for (ParentAlias parentAlias : saltForm.getParent().getParentAliases()) {
							if (!parentAlias.isIgnored()) {
								ParentAliasDTO parentAliasDTO = new ParentAliasDTO(parentAlias);
								parentAliases.add(parentAliasDTO);
							}
						}
						searchCompound.setParentAliases(parentAliases);
						if (saltForm.getParent().getStereoCategory() != null) {
							if (saltForm.getParent().getStereoCategory().getCode() != null) {
								searchCompound
										.setStereoCategoryName(saltForm.getParent().getStereoCategory().getName());
							}
						}
						if (saltForm.getParent().getStereoComment() != null) {
							searchCompound.setStereoComment(saltForm.getParent().getStereoComment());
						}
						if (saltFormMols.containsKey(saltForm.getCorpName())) {
							searchCompound.setCorpName(saltForm.getCorpName());
							searchCompound.setCorpNameType("SaltForm");
							searchCompound.setMolStructure(saltFormMols.get(saltForm.getCorpName()).getMolStructure());
						} else {
							// searchCompound.setMolStructure(hitMol.toFormat("mrv"));
							if (propertiesUtilService.getSaltBeforeLot()) {
								searchCompound.setCorpName(saltForm.getCorpName());
								searchCompound.setCorpNameType("SaltForm");
							} else {
								searchCompound.setCorpName(saltForm.getParent().getCorpName());
								searchCompound.setCorpNameType("Parent");
							}
							searchCompound.setMolStructure(hitMol.getMolStructure());
						}
						List<Lot> lotsFound = Lot.findLotsBySaltFormAndMeta(saltForm, searchParams).getResultList();
						List<Lot> lots = new ArrayList<Lot>();
						for (Lot lot : lotsFound) {
							if (!lots.contains(lot))
								lots.add(lot);
						}
						logger.debug("current saltForm: " + saltForm.getCorpName() + "  " + saltForm.getId());
						logger.debug("number of lots found: " + lotsFound.size());
						logger.debug("number of unique lots found: " + lots.size());
						for (Lot lot : lots) {
							// logger.debug("current lot:" + lot.getCorpName());
							SearchLotDTO searchLot = new SearchLotDTO();
							searchLot.setCorpName(lot.getCorpName());
							searchLot.setLotNumber(lot.getLotNumber());
							searchLot.setBuid(lot.getBuid());
							searchLot.setRegistrationDate(lot.getRegistrationDate());
							searchLot.setSynthesisDate(lot.getSynthesisDate());
							List<LotAliasDTO> lotAliases = new ArrayList<LotAliasDTO>();
							for (LotAlias lotAlias : lot.getLotAliases()) {
								if (!lotAlias.isIgnored()) {
									LotAliasDTO lotAliasDTO = new LotAliasDTO(lotAlias);
									lotAliases.add(lotAliasDTO);
								}
							}
							searchLot.setLotAliases(lotAliases);
							searchCompound.getLotIDs().add(searchLot);
						}
						foundCompounds.add(searchCompound);
					}
				}
			}

			// now add in any hits from the saltForm query that were not picked up in the
			// parent query
			if (saltFormCdIds != null) {
				logger.debug("number of saltFormCdIds found: " + saltFormCdIds.size());
			}

			logger.debug("number of saltForm structures found: " + saltFormMols.size());

			for (SaltForm saltForm : saltForms) {
				if (!parentMols.containsKey(saltForm.getCorpName())) {
					// logger.debug("----NEW SALTFORM ADDING TO LIST:" + saltForm.getCorpName());
					// logger.debug("current saltForm " + saltForm.toJson());
					SearchCompoundReturnDTO searchCompound = new SearchCompoundReturnDTO();
					searchCompound.setCorpName(saltForm.getCorpName());
					searchCompound.setCorpNameType("SaltForm");
					// logger.debug("current saltForm corpName " + searchCompound.getCorpName());

					if (saltForm.getParent().getStereoCategory() != null) {
						if (saltForm.getParent().getStereoCategory().getName() != null) {
							searchCompound.setStereoCategoryName(saltForm.getParent().getStereoCategory().getName());
						}
					}
					if (saltForm.getParent().getStereoComment() != null) {
						searchCompound.setStereoComment(saltForm.getParent().getStereoComment());
					}

					// SaltForm blah = saltFormMols.get(saltForm.getCorpName());
					// logger.debug("number of mol count " + saltFormMols.size());
					// logger.debug("current query mol structrue: " + blah.getMolStructure());

					searchCompound.setMolStructure(saltFormMols.get(saltForm.getCorpName()).getMolStructure());
					// logger.debug("current query structrue: " + searchCompound.getMolStructure());
					List<Lot> lotsFound = Lot.findLotsBySaltFormAndMeta(saltForm, searchParams).getResultList();
					List<Lot> lots = new ArrayList<Lot>();
					for (Lot lot : lotsFound) {
						if (!lots.contains(lot))
							lots.add(lot);
					}
					logger.debug("current saltForm: " + saltForm.getCorpName() + "  " + saltForm.getId());
					logger.debug("number of lots found: " + lotsFound.size());
					logger.debug("number of unique lots found: " + lots.size());
					for (Lot lot : lots) {
						// logger.debug("current lot: " + lot.toJson());
						SearchLotDTO searchLot = new SearchLotDTO();
						searchLot.setCorpName(lot.getCorpName());
						searchLot.setLotNumber(lot.getLotNumber());
						searchLot.setRegistrationDate(lot.getRegistrationDate());
						searchLot.setSynthesisDate(lot.getSynthesisDate());
						searchCompound.getLotIDs().add(searchLot);
					}
					foundCompounds.add(searchCompound);
				}

			}

			logger.debug("found Compounds from structure search: " + foundCompounds.size());

			// logger.debug("found Compounds: " +
			// SearchCompoundReturnDTO.toJsonArray(foundCompounds));

		}

		// Construct wrapper object and filter out results by project
		SearchFormReturnDTO results = new SearchFormReturnDTO();
		results.setFoundCompounds(foundCompounds);
		if (propertiesUtilService.getProjectRestrictions())
			results = filterSearchResultsByProject(results, searchParams.getProjects());

		return results;

	}

	private SearchFormReturnDTO filterSearchResultsByProject(SearchFormReturnDTO searchResults, List<String> projects) {
		if (!searchResults.getFoundCompounds().isEmpty()) {
			Collection<SearchCompoundReturnDTO> filteredFoundCompounds = new ArrayList<SearchCompoundReturnDTO>();
			for (SearchCompoundReturnDTO foundCompound : searchResults.getFoundCompounds()) {
				List<SearchLotDTO> filteredFoundLots = new ArrayList<SearchLotDTO>();
				for (SearchLotDTO foundLot : foundCompound.getLotIDs()) {
					String lotProject = Lot.findLotsByCorpNameEquals(foundLot.getCorpName()).getSingleResult()
							.getProject();
					if (projects.contains(lotProject))
						filteredFoundLots.add(foundLot);
				}
				if (!filteredFoundLots.isEmpty()) {
					foundCompound.setLotIDs(filteredFoundLots);
					filteredFoundCompounds.add(foundCompound);
				}
			}
			if (filteredFoundCompounds.isEmpty())
				searchResults.setLotsWithheld(true);
			searchResults.setFoundCompounds(filteredFoundCompounds);
		}
		return searchResults;
	}

	private boolean isNumber(String corpNameFrom) {
		try {
			Integer.parseInt(corpNameFrom.trim());
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	private String buildMetaQuery(SearchFormDTO searchParams, StructureType structureType) {

		SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
		boolean isFirst = true;

		StringBuilder query = new StringBuilder("SELECT cd_id from " + structureType.entityTable);

		if (searchParams.getMinSynthDate() != null) {
			if (isFirst) {
				query.append(" where sythesis_date >= '" + searchParams.getDateFrom() + "'");
			} else {
				query.append(" and sythesis_date >= '" + searchParams.getDateFrom() + "'");
			}
			isFirst = false;
		}

		if (searchParams.getMaxSynthDate() != null) {
			if (isFirst) {
				query.append(" where date <= '" + searchParams.getDateTo() + "'");
			} else {
				query.append(" and date <= '" + searchParams.getDateTo() + "'");
			}
			isFirst = false;
		}

		query.append(" order by date");

		return query.toString();
	}

	@Override
	public List<Integer> findParentIds(String molStructure, Integer maxResults, Float similarity, SearchType searchType,
			List<String> projects) throws IOException, CmpdRegMolFormatException {

		// We don't want to limit the max results when searching for structures so set
		// the number really high becuse
		// none of the searchMolStructures implementations allow an unlimited option for
		// max results.

		int[] parentStructureHits = structureService.searchMolStructures(molStructure, StructureType.PARENT, searchType,
				similarity, 999999999);
		List<Integer> cdIdslist = Arrays.stream(parentStructureHits).boxed().collect(Collectors.toList());
		TypedQuery<Integer> parentQuery = Parent.findParentIdsByCdIdInAndProjectIn(cdIdslist, projects);

		// We limit max results when searching for parents
		if (maxResults != null && maxResults > -1) {
			parentQuery.setMaxResults(maxResults);
		}

		return parentQuery.getResultList();
	}

}
