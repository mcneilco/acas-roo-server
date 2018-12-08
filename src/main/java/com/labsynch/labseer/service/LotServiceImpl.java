package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.CorpName;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.Operator;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.PhysicalState;
import com.labsynch.labseer.domain.Project;
import com.labsynch.labseer.domain.PurityMeasuredBy;
import com.labsynch.labseer.domain.SaltForm;
import com.labsynch.labseer.domain.Scientist;
import com.labsynch.labseer.domain.SolutionUnit;
import com.labsynch.labseer.domain.Unit;
import com.labsynch.labseer.domain.Vendor;
import com.labsynch.labseer.dto.LotDTO;
import com.labsynch.labseer.dto.configuration.MainConfigDTO;
import com.labsynch.labseer.utils.Configuration;
import com.labsynch.labseer.utils.SecurityUtil;


@Service
public class LotServiceImpl implements LotService {

	@Autowired
	private ChemStructureService chemService;

	@Autowired
	private SaltFormService saltFormService;

	@Autowired
	public LotAliasService lotAliasService;

	private static final MainConfigDTO mainConfig = Configuration.getConfigInfo();

	private static final Logger logger = LoggerFactory.getLogger(LotServiceImpl.class);
	
	
	// updates saltForm weight and lot weight
	@Override
	public Lot updateLotWeight(Lot lot) {
		saltFormService.updateSaltWeight(lot.getSaltForm());
		lot.setModifiedBy(SecurityUtil.getLoginUser().getUserName());
		lot.setLotMolWeight(Lot.calculateLotMolWeight(lot));
		lot.setModifiedDate(new Date());
		lot.merge();
		logger.debug("updated lot weight for "+lot.getCorpName()+" to: "+lot.getLotMolWeight());
		return Lot.findLot(lot.getId());
	}


	@Override
	@Transactional
	public Lot reparentLot(String lotCorpName, String parentCorpName, String modifiedByUser) {

		// incoming lot
		// name of new adoptive parent
		// logic to deal with lot numbering  -- just increment existing lot number of adoptive parent
		// note -- need to refactor to deal with more complicated parent/SaltForm/lot setups versus simpler parent/lot

		Author modifiedUser = Author.findAuthorsByUserName(modifiedByUser).getSingleResult();		

		Parent adoptiveParent = Parent.findParentsByCorpNameEquals(parentCorpName).getSingleResult();
		//renumber lot -- just increment to next number
		Integer newLotNumber = Lot.getMaxParentLotNumber(adoptiveParent)+1;
		logger.debug("next lot number for adoptive parent: " + newLotNumber);
		
		Lot queryLot = Lot.findLotsByCorpNameEquals(lotCorpName).getSingleResult();

		//associate saltForm to new adoptive parent
		SaltForm saltForm = queryLot.getSaltForm();
		saltForm.setParent(adoptiveParent);
		saltForm.setCorpName(CorpName.generateSaltFormCorpName(adoptiveParent.getCorpName(), saltForm.getIsoSalts()));
		saltForm.merge();

		// recalculate salt form weight
		saltFormService.updateSaltWeight(saltForm);

		logger.debug("new lot number before merge: " + newLotNumber);
		queryLot.setLotNumber(newLotNumber);
		logger.debug("new lot number: " + queryLot.getLotNumber());
		
		//rename lot corp name if not cas style
		MainConfigDTO mainConfig = Configuration.getConfigInfo();
		if (!mainConfig.getServerSettings().getCorpBatchFormat().equalsIgnoreCase("cas_style_format")){
			queryLot.setCorpName(queryLot.generateCorpName());
			//TODO: may want to save the old name as an alias
			// also need to deal with data that may be registered to the old corp name
		}
		logger.info("new lot corp name: " + queryLot.getCorpName());		
		// recalculate lot weight
		queryLot.setLotMolWeight(Lot.calculateLotMolWeight(queryLot));
		queryLot.setModifiedBy(modifiedUser.getUserName());
		queryLot.setModifiedDate(new Date());
		queryLot.merge();

		return queryLot;
	}

	@Override
	public String updateLotMetaArray(String jsonArray, String modifiedByUser) {
		Collection<LotDTO> lotDTOCollection = LotDTO.fromJsonArrayToLoes(jsonArray);
		int lotCounter = 0;
		for (LotDTO lotDTO : lotDTOCollection){
			updateLotMeta(lotDTO, modifiedByUser);
			lotCounter++;
		}

		return "updated " + lotCounter + " lots";
	}

	@Override
	public Lot updateLotMeta(LotDTO lotDTO, String modifiedByUser) {

		// Fields not to edit
		//	    private String asDrawnStruct;
		//	    private int lotAsDrawnCdId;
		//	    private long buid;
		//	    private Parent parent;
		//	    private BulkLoadFile bulkLoadFile;
		//	    private Set<FileList> fileLists = new HashSet<FileList>();
		//	    private Double lotMolWeight;
		//	    private SaltForm saltForm;

		Lot lot = null;
		if (lotDTO.getId() != null){
			lot = Lot.findLot(lotDTO.getId());
		} else {
			List<Lot> lots = Lot.findLotsByCorpNameEquals(lotDTO.getLotCorpName()).getResultList();
			if (lots.size() == 0){
				String errorMessage = "ERROR: Did not find requested query lot: " + lotDTO.getLotCorpName();
				logger.error(errorMessage);
				throw new RuntimeException("ERROR");
			} else if (lots.size() > 1){
				String errorMessage = "ERROR: found multiple lots for: " + lotDTO.getLotCorpName();		
				logger.error(errorMessage);			 
			} else {
				lot = lots.get(0);
			}
		}

		Author modifiedUser = Author.findAuthorsByUserName(modifiedByUser).getSingleResult();	

		try{

			if (lotDTO.getLotNumber() != null) lot.setLotNumber(lotDTO.getLotNumber());

			if (lotDTO.getColor() != null) lot.setColor(lotDTO.getColor());
			if (lotDTO.getBarcode() != null) lot.setBarcode(lotDTO.getBarcode());
			if (lotDTO.getComments() != null) lot.setComments(lotDTO.getComments());
			if (lotDTO.getNotebookPage() != null) lot.setNotebookPage(lotDTO.getNotebookPage());
			if (lotDTO.getAmount() != null) lot.setAmount(lotDTO.getAmount());
			if (lotDTO.getSolutionAmount() != null) lot.setSolutionAmount(lotDTO.getSolutionAmount());

			if (lotDTO.getSupplier() != null) lot.setSupplier(lotDTO.getSupplier());
			if (lotDTO.getSupplierID() != null) lot.setSupplierID(lotDTO.getSupplierID());
			if (lotDTO.getPurity() != null) lot.setPurity(lotDTO.getPurity());
			if (lotDTO.getPercentEE() != null) lot.setPercentEE(lotDTO.getPercentEE());
			if (lotDTO.getIsVirtual() != null) lot.setIsVirtual(lotDTO.getIsVirtual());
			if (lotDTO.getRetain() != null) lot.setRetain(lotDTO.getRetain());
			if (lotDTO.getRetainLocation() != null) lot.setRetainLocation(lotDTO.getRetainLocation());

			if (lotDTO.getMeltingPoint() != null) lot.setMeltingPoint(lotDTO.getMeltingPoint());
			if (lotDTO.getBoilingPoint() != null) lot.setBoilingPoint(lotDTO.getBoilingPoint());
			if (lotDTO.getSupplierLot() != null) lot.setSupplierLot(lotDTO.getSupplierLot());
			if (lotDTO.getLambda() != null) lot.setLambda(lotDTO.getLambda());
			if (lotDTO.getAbsorbance() != null) lot.setAbsorbance(lotDTO.getAbsorbance());
			if (lotDTO.getStockSolvent() != null) lot.setStockSolvent(lotDTO.getStockSolvent());
			if (lotDTO.getStockLocation() != null) lot.setStockLocation(lotDTO.getStockLocation());

			if (lotDTO.getSynthesisDate() != null) lot.setSynthesisDate(lotDTO.getSynthesisDate());
			if (lotDTO.getRegistrationDate() != null) lot.setRegistrationDate(lotDTO.getRegistrationDate());

			if (lotDTO.getProject() != null && lotDTO.getProject().length() > 0) lot.setProject(Project.findProjectsByCodeEquals(lotDTO.getProject()).getSingleResult());
			if (lotDTO.getChemist() != null && lotDTO.getChemist().length() > 0) lot.setChemist(Author.findAuthorsByUserName(lotDTO.getChemist()).getSingleResult().getUserName());
			if (lotDTO.getLotRegisteredBy() != null && lotDTO.getLotRegisteredBy().length() > 0) lot.setRegisteredBy(Author.findAuthorsByUserName(lotDTO.getLotRegisteredBy()).getSingleResult().getUserName());
			if (lotDTO.getPurityMeasuredByCode() != null && lotDTO.getPurityMeasuredByCode().length() > 0) lot.setPurityMeasuredBy(PurityMeasuredBy.findPurityMeasuredBysByNameEquals(lotDTO.getPurityMeasuredByCode()).getSingleResult());
			if (lotDTO.getPhysicalStateCode() != null && lotDTO.getPhysicalStateCode().length() > 0) lot.setPhysicalState(PhysicalState.findPhysicalStatesByNameEquals(lotDTO.getPhysicalStateCode()).getSingleResult());
			if (lotDTO.getVendorCode() != null && lotDTO.getVendorCode().length() > 0)  lot.setVendor(Vendor.findVendorsByCodeEquals(lotDTO.getVendorCode()).getSingleResult());
			if (lotDTO.getPurityOperatorCode() != null && lotDTO.getPurityOperatorCode().length() > 0) lot.setPurityOperator(Operator.findOperatorsByCodeEquals(lotDTO.getPurityOperatorCode()).getSingleResult());
			if (lotDTO.getAmountUnitsCode() != null && lotDTO.getAmountUnitsCode().length() > 0) lot.setAmountUnits(Unit.findUnitsByCodeEquals(lotDTO.getAmountUnitsCode()).getSingleResult());
			if (lotDTO.getRetainUnitsCode() != null && lotDTO.getRetainUnitsCode().length() > 0) lot.setRetainUnits(Unit.findUnitsByCodeEquals(lotDTO.getRetainUnitsCode()).getSingleResult());
			if (lotDTO.getSolutionAmountUnitsCode() != null && lotDTO.getSolutionAmountUnitsCode().length() > 0) lot.setSolutionAmountUnits(SolutionUnit.findSolutionUnitsByCodeEquals(lotDTO.getSolutionAmountUnitsCode()).getSingleResult());

			if (lotDTO.getLotAliases() != null && lotDTO.getLotAliases().length() > 0){
				lot = lotAliasService.updateLotDefaultAlias(lot, lotDTO.getLotAliases());
			}

		}catch (Exception e){
			logger.error("Caught error udating up lot property "+e);
			throw new RuntimeException("An error has occurred updating lot property ");
		}

		lot.setModifiedDate(new Date());
		lot.setModifiedBy(modifiedUser.getUserName());
		lot.merge();

		return Lot.findLot(lot.getId());
	}


}
