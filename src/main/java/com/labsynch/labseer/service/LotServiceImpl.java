package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.FileList;
import com.labsynch.labseer.domain.IsoSalt;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.LotAlias;
import com.labsynch.labseer.domain.Operator;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.ParentAlias;
import com.labsynch.labseer.domain.PhysicalState;
import com.labsynch.labseer.domain.PurityMeasuredBy;
import com.labsynch.labseer.domain.SaltForm;
import com.labsynch.labseer.domain.SolutionUnit;
import com.labsynch.labseer.domain.Unit;
import com.labsynch.labseer.domain.Vendor;
import com.labsynch.labseer.dto.CmpdRegBatchCodeDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ContainerBatchCodeDTO;
import com.labsynch.labseer.dto.LotDTO;
import com.labsynch.labseer.dto.LotsByProjectDTO;
import com.labsynch.labseer.dto.PreferredNameDTO;
import com.labsynch.labseer.dto.ReparentLotResponseDTO;
import com.labsynch.labseer.exceptions.DupeLotException;
import com.labsynch.labseer.utils.PropertiesUtilService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LotServiceImpl implements LotService {

	@Autowired
	private CorpNameService corpNameService;

	@Autowired
	private SaltFormService saltFormService;

	@Autowired
	public LotAliasService lotAliasService;

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Autowired
	private AssayService assayService;

	@Autowired
	private ContainerService containerService;

	@Autowired
	private ExperimentService experimentService;

	@Autowired
	private LotService lotService;

	@Autowired
	private ParentService parentService;

	private static final Logger logger = LoggerFactory.getLogger(LotServiceImpl.class);

	// updates saltForm weight and lot weight
	@Override
	public Lot updateLotWeight(Lot lot) {
		SaltForm saltForm = lot.getSaltForm();
		saltFormService.updateSaltWeight(saltForm);
		saltForm.merge();
		lot.setLotMolWeight(Lot.calculateLotMolWeight(lot));
		lot.setModifiedDate(new Date());
		lot.merge();
		logger.debug("updated lot weight for " + lot.getCorpName() + " to: " + lot.getLotMolWeight());
		return Lot.findLot(lot.getId());
	}

	public Integer predictReparentLotNumber(Parent parent, Lot lot) {
		// With a lot and parent, predict what the lot number would be
		Integer newLotNumber;
		if(propertiesUtilService.getMaxAutoLotNumber() != null && lot.getLotNumber() > propertiesUtilService.getMaxAutoLotNumber()){
			newLotNumber = lot.getLotNumber();
		} else {
			newLotNumber = Lot.getMaxParentLotNumber(parent, propertiesUtilService.getMaxAutoLotNumber()) + 1;
		}
		return newLotNumber;
	}

	@Override
	@Transactional
	public ReparentLotResponseDTO reparentLot(String lotCorpName, String parentCorpName, String modifiedByUser, Boolean updateInventory, Boolean updateAssayData, Boolean dryRun) throws DupeLotException{

		logger.info("Got request to reparent lot: " + lotCorpName + " to parent: " + parentCorpName);
		
		ReparentLotResponseDTO reparentLotResponseDTO = new ReparentLotResponseDTO();
		reparentLotResponseDTO.setOriginalLotCorpName(lotCorpName);
		reparentLotResponseDTO.setModifiedBy(modifiedByUser);

		Lot queryLot = Lot.findLotsByCorpNameEquals(lotCorpName).getSingleResult();
		reparentLotResponseDTO.setOriginalLotNumber(queryLot.getLotNumber());

		SaltForm saltForm = queryLot.getSaltForm();
		Parent adoptiveParent = Parent.findParentsByCorpNameEquals(parentCorpName).getSingleResult();
		Set<IsoSalt> isoSalts = saltForm.getIsoSalts();
		if(dryRun) {
			// Detaching an entity closes it's session and detaches it from the persistence context, allowing us to query and modify the item but the changes will not be reflected
			// to the database.
			Lot.entityManager().detach(queryLot);

			// Before detaching saltForm in dry run mode, we need to initialize the iso salt collection.  Calling ".size()" is the easiest way I found to do this.
			// Without initializing the iso salts they can't be used to generateSaltFormCorpName and give the error:
			//    org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role: com.labsynch.labseer.domain.SaltForm.isoSalts, could not initialize proxy - no Session
			isoSalts.size();

			SaltForm.entityManager().detach(saltForm);
			Parent.entityManager().detach(adoptiveParent);

		}

		// LotService.checkLotOrphanLevel function calculates what level of the compound would be orphaned if the lot were to be removed.
		// It will return Parent if the lot is moved, and there are no other SaltForms on the Parent that have lots (leaving Parnent as an orphan)
		// It will return SaltForm if the lot is moved, and there are other SaltForms on the Parent (leaving SaltForm as an orphan)
		// We only need to take action if the LotOrphanLevel is Parent because this tells us we should also delete the Parent.
		// We don't  need to take action if the LotOrphanLevel is SaltForm, because reparent lot also moves the lot salt form.
		LotOrphanLevel lotOrphanLevel = lotService.checkLotOrphanLevel(queryLot);

		Boolean deleteOriginalParentAfterReparent = false;
		Long originalParentId = queryLot.getParent().getId();
		if (lotOrphanLevel.equals(LotOrphanLevel.Parent)) {
			deleteOriginalParentAfterReparent = true;
		}
		
		// renumber lot -- just increment to next number
		Integer newLotNumber = predictReparentLotNumber(adoptiveParent, queryLot);
		logger.debug("next lot number for adoptive parent: " + newLotNumber);

		reparentLotResponseDTO.setOriginalParentCorpName(queryLot.getParent().getCorpName());

		// associate saltForm to new adoptive parent
		saltForm.setParent(adoptiveParent);
		saltForm.setCorpName(
				corpNameService.generateSaltFormCorpName(adoptiveParent.getCorpName(), isoSalts));
		saltFormService.updateSaltWeight(saltForm);
		queryLot.setLotNumber(newLotNumber);
		logger.debug("new lot number: " + queryLot.getLotNumber());

		// rename lot corp name if not cas style
		if (!propertiesUtilService.getCorpBatchFormat().equalsIgnoreCase("cas_style_format")) {
			String newCorpName = this.generateCorpName(queryLot);
			if(Lot.findLotsByCorpNameEquals(newCorpName).getResultList().size() > 0){
				logger.error("Lot corp name already exists: " + newCorpName);
				throw new DupeLotException("Lot corp name already exists: " + newCorpName);
			}
			queryLot.setCorpName(newCorpName);
			// TODO: may want to save the old name as an alias
		}
		logger.info("new lot corp name: " + queryLot.getCorpName());

		// recalculate lot weight
		queryLot.setLotMolWeight(Lot.calculateLotMolWeight(queryLot));
		queryLot.setModifiedBy(modifiedByUser);
		queryLot.setModifiedDate(new Date());

		reparentLotResponseDTO.setNewLot(queryLot);

		if(deleteOriginalParentAfterReparent) {
			reparentLotResponseDTO.setOriginalParentDeleted(true);
		} else {
			reparentLotResponseDTO.setOriginalParentDeleted(false);
		}

		if(!dryRun) {
			saltForm.merge();

			queryLot.merge();
			if(deleteOriginalParentAfterReparent) {
				// Without fetching a fresh copy of the parent, the delete parent failed as it still had it's salt form attached
				// This delete would fail because a conflict.
				Parent originalParent = Parent.findParent(originalParentId);
				Parent.entityManager().refresh(originalParent);
				parentService.deleteParent(originalParent);
			}
			// Null transaction id which gets populated if used
			Long transactionId = null;
			if (updateAssayData) {
				transactionId = assayService.renameBatchCode(lotCorpName, queryLot.getCorpName(), modifiedByUser);
			}
			if (updateInventory) {
				containerService.renameBatchCode(lotCorpName, queryLot.getCorpName(), modifiedByUser, transactionId);
			}
		}
		return reparentLotResponseDTO;
	}

	@Override
	public String updateLotMetaArray(String jsonArray) {
		Collection<LotDTO> lotDTOCollection = LotDTO.fromJsonArrayToLoes(jsonArray);
		int lotCounter = 0;
		for (LotDTO lotDTO : lotDTOCollection) {
			updateLotMeta(lotDTO);
			lotCounter++;
		}

		return "updated " + lotCounter + " lots";
	}

	@Override
	public Lot updateLotMeta(LotDTO lotDTO) {

		// Fields not to edit
		// private String asDrawnStruct;
		// private int lotAsDrawnCdId;
		// private long buid;
		// private Parent parent;
		// private BulkLoadFile bulkLoadFile;
		// private Set<FileList> fileLists = new HashSet<FileList>();
		// private Double lotMolWeight;
		// private SaltForm saltForm;

		Lot lot = null;
		if (lotDTO.getId() != null) {
			lot = Lot.findLot(lotDTO.getId());
		} else {
			List<Lot> lots = Lot.findLotsByCorpNameEquals(lotDTO.getLotCorpName()).getResultList();
			if (lots.size() == 0) {
				String errorMessage = "ERROR: Did not find requested query lot: " + lotDTO.getLotCorpName();
				logger.error(errorMessage);
				throw new RuntimeException("ERROR");
			} else if (lots.size() > 1) {
				String errorMessage = "ERROR: found multiple lots for: " + lotDTO.getLotCorpName();
				logger.error(errorMessage);
			} else {
				lot = lots.get(0);
			}
		}

		try {

			if (lotDTO.getLotNumber() != null)
				lot.setLotNumber(lotDTO.getLotNumber());

			if (lotDTO.getColor() != null)
				lot.setColor(lotDTO.getColor());
			if (lotDTO.getBarcode() != null)
				lot.setBarcode(lotDTO.getBarcode());
			if (lotDTO.getComments() != null)
				lot.setComments(lotDTO.getComments());
			if (lotDTO.getNotebookPage() != null)
				lot.setNotebookPage(lotDTO.getNotebookPage());
			if (lotDTO.getAmount() != null)
				lot.setAmount(lotDTO.getAmount());
			if (lotDTO.getSolutionAmount() != null)
				lot.setSolutionAmount(lotDTO.getSolutionAmount());

			if (lotDTO.getSupplier() != null)
				lot.setSupplier(lotDTO.getSupplier());
			if (lotDTO.getSupplierID() != null)
				lot.setSupplierID(lotDTO.getSupplierID());
			if (lotDTO.getPurity() != null)
				lot.setPurity(lotDTO.getPurity());
			if (lotDTO.getPercentEE() != null)
				lot.setPercentEE(lotDTO.getPercentEE());
			if (lotDTO.getIsVirtual() != null)
				lot.setIsVirtual(lotDTO.getIsVirtual());
			if (lotDTO.getRetain() != null)
				lot.setRetain(lotDTO.getRetain());
			if (lotDTO.getRetainLocation() != null)
				lot.setRetainLocation(lotDTO.getRetainLocation());

			if (lotDTO.getMeltingPoint() != null)
				lot.setMeltingPoint(lotDTO.getMeltingPoint());
			if (lotDTO.getBoilingPoint() != null)
				lot.setBoilingPoint(lotDTO.getBoilingPoint());
			if (lotDTO.getSupplierLot() != null)
				lot.setSupplierLot(lotDTO.getSupplierLot());
			if (lotDTO.getLambda() != null)
				lot.setLambda(lotDTO.getLambda());
			if (lotDTO.getAbsorbance() != null)
				lot.setAbsorbance(lotDTO.getAbsorbance());
			if (lotDTO.getStockSolvent() != null)
				lot.setStockSolvent(lotDTO.getStockSolvent());
			if (lotDTO.getStockLocation() != null)
				lot.setStockLocation(lotDTO.getStockLocation());

			if (lotDTO.getSynthesisDate() != null)
				lot.setSynthesisDate(lotDTO.getSynthesisDate());
			if (lotDTO.getRegistrationDate() != null)
				lot.setRegistrationDate(lotDTO.getRegistrationDate());

			if (lotDTO.getProject() != null && lotDTO.getProject().length() > 0)
				lot.setProject(lotDTO.getProject());
			if (lotDTO.getChemist() != null && lotDTO.getChemist().length() > 0)
				lot.setChemist(Author.findAuthorsByUserName(lotDTO.getChemist()).getSingleResult().getUserName());
			if (lotDTO.getLotRegisteredBy() != null && lotDTO.getLotRegisteredBy().length() > 0)
				lot.setRegisteredBy(
						Author.findAuthorsByUserName(lotDTO.getLotRegisteredBy()).getSingleResult().getUserName());
			if (lotDTO.getPurityMeasuredByCode() != null && lotDTO.getPurityMeasuredByCode().length() > 0)
				lot.setPurityMeasuredBy(PurityMeasuredBy
						.findPurityMeasuredBysByNameEquals(lotDTO.getPurityMeasuredByCode()).getSingleResult());
			if (lotDTO.getPhysicalStateCode() != null && lotDTO.getPhysicalStateCode().length() > 0)
				lot.setPhysicalState(
						PhysicalState.findPhysicalStatesByNameEquals(lotDTO.getPhysicalStateCode()).getSingleResult());
			if (lotDTO.getVendorCode() != null && lotDTO.getVendorCode().length() > 0)
				lot.setVendor(Vendor.findVendorsByCodeEquals(lotDTO.getVendorCode()).getSingleResult());
			if (lotDTO.getPurityOperatorCode() != null && lotDTO.getPurityOperatorCode().length() > 0)
				lot.setPurityOperator(
						Operator.findOperatorsByCodeEquals(lotDTO.getPurityOperatorCode()).getSingleResult());
			if (lotDTO.getAmountUnitsCode() != null && lotDTO.getAmountUnitsCode().length() > 0)
				lot.setAmountUnits(Unit.findUnitsByCodeEquals(lotDTO.getAmountUnitsCode()).getSingleResult());
			if (lotDTO.getRetainUnitsCode() != null && lotDTO.getRetainUnitsCode().length() > 0)
				lot.setRetainUnits(Unit.findUnitsByCodeEquals(lotDTO.getRetainUnitsCode()).getSingleResult());
			if (lotDTO.getSolutionAmountUnitsCode() != null && lotDTO.getSolutionAmountUnitsCode().length() > 0)
				lot.setSolutionAmountUnits(SolutionUnit
						.findSolutionUnitsByCodeEquals(lotDTO.getSolutionAmountUnitsCode()).getSingleResult());
			if (lotDTO.getModifiedBy() != null && lotDTO.getModifiedBy().length() > 0)
				lot.setModifiedBy(lotDTO.getModifiedBy());

			if (lotDTO.getLotAliases() != null && lotDTO.getLotAliases().length() > 0) {
				lot = lotAliasService.updateLotDefaultAlias(lot, lotDTO.getLotAliases());
			}

		} catch (Exception e) {
			logger.error("Caught error udating up lot property " + e);
			throw new RuntimeException("An error has occurred updating lot property ");
		}

		lot.setModifiedDate(new Date());
		lot.merge();

		return Lot.findLot(lot.getId());
	}

	@Override
	public Collection<LotsByProjectDTO> getLotsByProjectsList(List<String> projects) {
		Collection<LotsByProjectDTO> lots = Lot.findLotsByProjectsList(projects).getResultList();
		return (lots);
	}

	// NEW
	@Override
	public String generateSaltFormLotName(Lot lot) {
		String corpName = lot.getSaltForm().getCorpName();
		logger.debug("Salt corpName = " + corpName);
		int lotNumber = this.generateSaltFormLotNumber(lot);
		lot.setLotNumber(lotNumber);

		String batchFormat = "%0" + propertiesUtilService.getFormatBatchDigits() + "d";
		corpName = corpName.concat(propertiesUtilService.getBatchSeparator())
				.concat(String.format(batchFormat, lotNumber));
		logger.debug("corpName: " + corpName);
		return corpName;
	}

	@Override
	public String appendSaltCode(String corpName, Lot lot) {
		List<IsoSalt> isoSalts = IsoSalt.findIsoSaltsBySaltForm(lot.getSaltForm()).getResultList();
		logger.debug("number of isoSalts: " + isoSalts.size());
		if (isoSalts.size() == 0) {
			corpName = corpName.concat(propertiesUtilService.getNoSaltCode());
		} else {
			for (IsoSalt isoSalt : isoSalts) {
				if (isoSalt.getType().equalsIgnoreCase("salt")) {
					corpName = corpName.concat(isoSalt.getSalt().getAbbrev());
				} else if (isoSalt.getType().equalsIgnoreCase("isotope")) {
					corpName = corpName.concat(isoSalt.getIsotope().getAbbrev());
				}
			}
		}
		return corpName;
	}

	@Override
	public String generateParentLotName(Lot lot) {

		logger.debug("generating the new lot corp name");
		String corpName = lot.getSaltForm().getParent().getCorpName();
		logger.debug("Parent corpName = " + corpName);
		int lotNumber = this.generateParentLotNumber(lot);
		lot.setLotNumber(lotNumber);

		return generateLotCorpNameFromBaseCorpName(corpName, lot);
	}

	public String generateLotCorpNameFromBaseCorpName(String corpName, Lot lot) {
		int lotNumber = lot.getLotNumber();
		if (propertiesUtilService.getFormatBatchDigits() == 0) {
			logger.error("formatBatchDigits is set to " + propertiesUtilService.getFormatBatchDigits());
		}
		String batchFormat = "%0" + propertiesUtilService.getFormatBatchDigits() + "d";
		logger.debug("batch format is " + batchFormat);
		if (propertiesUtilService.getCorpBatchFormat().equalsIgnoreCase("corp_saltcode_batch")) {
			corpName = corpName.concat(propertiesUtilService.getSaltSeparator());
			corpName = appendSaltCode(corpName, lot);
			corpName = corpName.concat(propertiesUtilService.getBatchSeparator())
					.concat(String.format(batchFormat, lotNumber));
		} else if (propertiesUtilService.getCorpBatchFormat().equalsIgnoreCase("corp_batch_saltcode")) {
			corpName = corpName.concat(propertiesUtilService.getBatchSeparator())
					.concat(String.format(batchFormat, lotNumber));
			if (propertiesUtilService.getAppendSaltCodeToLotName()) {
				corpName = corpName.concat(propertiesUtilService.getSaltSeparator());
				corpName = appendSaltCode(corpName, lot);
			}
		} else {
			corpName = corpName.concat(propertiesUtilService.getBatchSeparator())
					.concat(String.format(batchFormat, lotNumber));
		}

		logger.debug("corpName: " + corpName);
		return corpName;
	}

	@Override
	public String generateCasStyleLotName(Lot lot) {
		List seqList = lot.generateCustomLotSequence();
		String inputSequence = seqList.get(0).toString();
		int casCheckDigit = generateCasCheckDigit(inputSequence);
		String fullName = String.format("%09d", Long.parseLong(inputSequence));
		logger.info(fullName);
		StringBuilder sb = new StringBuilder();
		sb = sb.append(propertiesUtilService.getCorpPrefix()).append(propertiesUtilService.getCorpSeparator());

		String regexPattern = "(\\d{3})(\\d{3})(\\d{3})";
		Pattern p = Pattern.compile(regexPattern);
		Matcher m = p.matcher(fullName);
		if (m.find()) {
			sb.append(m.group(1)).append(propertiesUtilService.getCorpSeparator());
			sb.append(m.group(2)).append(propertiesUtilService.getCorpSeparator());
			sb.append(m.group(3)).append(propertiesUtilService.getCorpSeparator());
			sb.append(Integer.toString(casCheckDigit));
		}
		String lotName = sb.toString();
		logger.info(lotName);

		// set lot number
		int lotNumber = this.generateParentLotNumber(lot);
		lot.setLotNumber(lotNumber);

		return lotName;
	}

	@Override
	public int generateSaltFormLotNumber(Lot lot) {
		Integer lotNumber = 0;
		if (lot.getIsVirtual()) {
			lotNumber = 0;
		} else {
			if (lot.getLotNumber() != null && lot.getLotNumber() > 0) {
				lotNumber = lot.getLotNumber();
			} else {
				int lotCount = 0;
				if (Lot.getMaxSaltFormLotNumber(lot.getSaltForm()) == null) {
					logger.error("this is a null pointer exception. Set lotCount = 0");
				} else {
					lotCount = Lot.getMaxSaltFormLotNumber(lot.getSaltForm());
				}
				logger.debug("Lot Count = " + lotCount);
				lotNumber = lotCount + 1;
			}
		}
		logger.debug("Lot Number = " + lotNumber);
		return lotNumber;
	}

	@Override
	public int generateCasCheckDigit(String inputCasLabel) {
		int checkDigit = 0;
		logger.info("" + inputCasLabel.length());
		int sum = 0;
		for (int index = 0; index < inputCasLabel.length(); index++) {
			logger.info("current index: " + index);
			sum += (inputCasLabel.length() - index) * Integer.parseInt(inputCasLabel.substring(index, index + 1));
			logger.info("sum: " + sum);
		}
		checkDigit = sum % 10;
		return checkDigit;
	}

	@Override
	public String generateCorpName(Lot lot) {
		logger.info("corp batch format is: " + propertiesUtilService.getCorpBatchFormat());

		String corpName = null;
		if (propertiesUtilService.getCorpBatchFormat().equalsIgnoreCase("cas_style_format")) {
			corpName = generateCasStyleLotName(lot);
		} else if (propertiesUtilService.getSaltBeforeLot()) {
			corpName = generateSaltFormLotName(lot);
		} else {
			corpName = generateParentLotName(lot);
		}
		return corpName;
	}

	@Override
	public int generateLotNumber(Lot lot) {

		int lotNumber = 0;
		if (propertiesUtilService.getSaltBeforeLot()) {
			lotNumber = generateSaltFormLotNumber(lot);
		} else {
			lotNumber = generateParentLotNumber(lot);
		}
		return lotNumber;
	}

	@Override
	public int generateParentLotNumber(Lot lot) {
		// set lot number
		Integer lotNumber = lot.getLotNumber();
		if (lot.getIsVirtual()) {
			lotNumber = 0;
		} else {
			if (lot.getLotNumber() != null && lot.getLotNumber() > 0) {
				lotNumber = lot.getLotNumber();
			} else {
				int lotCount = 0;
				if (lot.getParent().getId() == null) {
					logger.debug("Setting lotCount of new parent = 0");
				} else {
					Integer maxLotNumber = Lot.getMaxParentLotNumber(lot.getParent(), propertiesUtilService.getMaxAutoLotNumber());
					if (maxLotNumber == null) {
						logger.debug("this is a null pointer exception. Set lotCount = 0");
					} else {
						lotCount = maxLotNumber;
					}
				}
				logger.debug("Lot Count = " + lotCount);
				lotNumber = lotCount + 1;
			}
		}
		logger.debug("Lot Number = " + lotNumber);
		return lotNumber;
	}

	private String findPreferredLotNameByParentAliasLotName(String inputString) {

		// Get parent aliases where the input string starts with an alias name
		List<ParentAlias> parentAliasesMatchingString = ParentAlias
				.findParentAliasesByStringStartsWithAliasName(inputString).getResultList();

		// Remove ignored or deleted aliases
		parentAliasesMatchingString.removeIf(alias -> alias.isIgnored() | alias.isDeleted());

		// Setup the default return value
		String preferredName = "";

		// If we don't have only 1 parent alais then return ""
		if (parentAliasesMatchingString.size() != 1) {
			if (parentAliasesMatchingString.size() == 0) {
				logger.debug("No parent aliases found for input string: " + inputString);
			} else {
				logger.error("More than one parent alias found for input string: " + inputString);
			}
			return preferredName;
		}

		String requestedParentAliasName = parentAliasesMatchingString.get(0).getAliasName();
		Parent parent = parentAliasesMatchingString.get(0).getParent();

		Set<SaltForm> saltForms = parent.getSaltForms();
		for (SaltForm saltForm : saltForms) {
			Set<Lot> lots = saltForm.getLots();
			for (Lot lot : lots) {
				// Generate a the parent alias corp name using the parent alias name instead of
				// the parent corp name
				String parentAliasLotCorpName = generateLotCorpNameFromBaseCorpName(requestedParentAliasName, lot);
				// Check if the input string matches the input string
				if (parentAliasLotCorpName.equals(inputString)) {
					logger.debug("Found a matching lot for the requested name: \""
							+ inputString + "\"");
					preferredName = lot.getCorpName();
					break;
				}
			}
		}

		return preferredName;
	}

	@Override
	public Collection<PreferredNameDTO> getPreferredNames(Collection<PreferredNameDTO> preferredNameDTOs) {
		for (PreferredNameDTO preferredNameDTO : preferredNameDTOs) {
			String preferredName;
			try {
				// First try and search by lot corp name
				preferredName = Lot.findLotsByCorpNameEquals(preferredNameDTO.getRequestName()).getSingleResult()
						.getCorpName();
			} catch (NoResultException e) {
				if (propertiesUtilService.getAllowParentAliasLotNames()) {
					preferredName = findPreferredLotNameByParentAliasLotName(preferredNameDTO.getRequestName());
				} else {
					preferredName = "";
				}
			}
			preferredNameDTO.setPreferredName(preferredName);
		}
		return preferredNameDTOs;
	}

	@Override
	public CmpdRegBatchCodeDTO checkForDependentData(Set<String> lotCorpNames) {
		CmpdRegBatchCodeDTO cmpdRegBatchCodeDTO = new CmpdRegBatchCodeDTO(lotCorpNames);

		cmpdRegBatchCodeDTO.checkForDependentData();

		if(cmpdRegBatchCodeDTO.getLinkedDataExists()) {
			List<String> batchCodeList = new ArrayList<String>();
			batchCodeList.addAll(lotCorpNames);
			cmpdRegBatchCodeDTO.setLinkedContainers(containerService.getContainerDTOsByBatchCodes(batchCodeList));
		}
		return cmpdRegBatchCodeDTO;
	}

	@Override
	@Transactional
	public void deleteLots(Collection<Lot> lots ) {

		//
		// Delete the lots
		for (Lot lot : lots) {
			deleteLot(lot);
		}
		
	}

	/**
	 * Delete the lot and all of its children. This method is specific for the lot and does not clean up the orphaned salt forms , iso salts or parents. See ParentLotServiceImpl.deleteLot for the recursive delete of the parent lot.
	 * 
	 * @param lot
	 */
	@Override
	@Transactional
	public void deleteLot(Lot lot) {


		// Check for dependent data
		Set<String> batchCodeSet = new HashSet<String>();
		batchCodeSet.add(lot.getCorpName());
		CmpdRegBatchCodeDTO batchDTO = checkForDependentData(batchCodeSet);

		//Containers
		if (batchDTO.getLinkedContainers() != null && batchDTO.getLinkedContainers().size() > 0) {
			List<String> containerCodeNameList = new ArrayList<String>();
			for (ContainerBatchCodeDTO containerBatchCodeDTO : batchDTO.getLinkedContainers()) {
				containerCodeNameList.add(containerBatchCodeDTO.getContainerCodeName());
			}
			containerService.logicalDeleteContainerCodesArray(containerCodeNameList);
		}

		// Experiments
		if (batchDTO.getLinkedExperiments() != null && batchDTO.getLinkedExperiments().size() > 0) {
			for (CodeTableDTO experimentCodeTableDTO : batchDTO.getLinkedExperiments()) {
				experimentService.deleteExperimentDataByBatchCode(experimentCodeTableDTO.getCode(), lot.getCorpName());
			}
		}


		// Lot files
		Collection<FileList> fileLists = lot.getFileLists();
		fileLists.addAll(FileList.findFileListsByLot(lot).getResultList());
		if (fileLists != null) {
			for (FileList fileList : fileLists) {
				fileList.remove();
			}
		}

		// Lot Aliases
		Set<LotAlias> lotAliases = lot.getLotAliases();
		if (lotAliases != null) {
			for (LotAlias lotAlias : lotAliases) {
				lotAlias.remove();
			}
		}

		// Lot
		lot.remove();
	}

	public enum LotOrphanLevel {
		Parent, SaltForm, NONE
	}

	@Override
	public LotOrphanLevel checkLotOrphanLevel(Lot lot) {
		// Checks to see if the lot would be orphaned and if so, returns the level of the orphan.
		Parent parent = Parent.findParent(lot.getParent().getId());
		Boolean canDeleteSaltForm = true;
		Boolean canDeleteParent = true;
		for(SaltForm saltForm : parent.getSaltForms()) {
			if(saltForm.getId().equals(lot.getSaltForm().getId())) {
				Set<Lot> lots = saltForm.getLots();
				for(Lot lotToDelete : lots) {
					if(!lotToDelete.getId().equals(lot.getId())) {
						canDeleteSaltForm = false;
						canDeleteParent = false;
						logger.info("Found dependent lot ("+lot.getCorpName()+") on salt form (corpName:'"+saltForm.getCorpName()+"', id:"+saltForm.getId()+")");
					}
				}
			} else {
				canDeleteParent = false;
				logger.info("Found dependent salt form ("+saltForm.getCorpName()+") on parent (corpName:'"+parent.getCorpName()+"', id:"+parent.getId()+")");
			}
		}

		if(canDeleteParent) {
			return LotOrphanLevel.Parent;
		} else if(canDeleteSaltForm) {
			return LotOrphanLevel.SaltForm;
		} else {
			return LotOrphanLevel.NONE;
		}
	}

}
