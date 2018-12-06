package com.labsynch.labseer.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.IsoSalt;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.LotAlias;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.ParentAlias;
import com.labsynch.labseer.domain.PhysicalState;
import com.labsynch.labseer.domain.SaltForm;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.service.ChemStructureService;
import com.labsynch.labseer.utils.MoleculeUtil;

@RooJavaBean
@RooToString
//@RooEntity
@RooJson
public class LotDTO {
	private Long id;
	private Double absorbance;
	private Double amount;
	private String amountUnitsCode;
	private String barcode;
	private Double boilingPoint;
	private Long buid;
	private String bulkLoadFile;
	private String chemist;
	private String color;
	private String comments;
	private String lotCorpName;
	private Boolean isVirtual;
	private Double lambda;
	private String lotAliases;
	private Double lotMolWeight;
	private Integer lotNumber;
	private Double meltingPoint;
	private String modifiedBy;
	private Date modifiedDate;
	private String notebookPage;
	private Double observedMassOne;
	private Double observedMassTwo;
	private Double percentEE;
	private String physicalStateCode;
	private String project;
	private Double purity;
	private String purityMeasuredByCode;
	private String purityOperatorCode;
	private Date registrationDate;
	private Double retain;
	private String retainLocation;
	private String retainUnitsCode;
	private String lotRegisteredBy;
	private Double solutionAmount;
	private String solutionAmountUnitsCode;
	private String stockLocation;
	private String stockSolvent;
	private String supplier;
	private String supplierID;
	private String supplierLot;
	private Date synthesisDate;
	private Double tareWeight;
	private String tareWeightUnitsCode;
	private Double totalAmountStored;
	private String totalAmountStoredUnitsCode;
	private String vendorCode;
	private String vendorID;
	private String saltFormCorpName;
	private String casNumber;
	private String saltAbbrevs;
	private String saltEquivalents;
	private String parentCorpName;
	private long parentNumber;
	private String parentCommonName;
	private String parentStereoCategory;
	private String parentStereoComment;
	private String parentStructure;
	private Double parentMolWeight;
	private Double parentExactMass;
	private String parentMolFormula;
	private Date parentRegistrationDate;
	private String parentRegisteredBy;
	private Date parentModifiedDate;
	private String parentModifiedBy;
	private String parentAliases;
	private String parentAnnotationCode;
	private String parentCompoundTypeCode;
	private String parentComment;
	private Boolean parentIsMixture;
//	private Set<LotAliasDTO> lotAliasSet = new HashSet<LotAliasDTO>();
	
	public LotDTO(Lot lot){
		if (lot.getId() != null) this.id = lot.getId();
		this.absorbance = lot.getAbsorbance();
		this.amount = lot.getAmount();
		if (lot.getAmountUnits() != null) this.amountUnitsCode = lot.getAmountUnits().getCode();
		this.barcode = lot.getBarcode();
		this.boilingPoint = lot.getBoilingPoint();
		this.buid = lot.getBuid();
		if (lot.getBulkLoadFile() != null) this.bulkLoadFile = lot.getBulkLoadFile().getFileName();
		if (lot.getChemist() != null) this.chemist = lot.getChemist().getCode();
		this.color = lot.getColor();
		this.comments = lot.getComments();
		if (lot.getCorpName() != null) this.lotCorpName = lot.getCorpName();
		this.isVirtual = lot.getIsVirtual();
		this.lambda = lot.getLambda();
		this.lotMolWeight = lot.getLotMolWeight();
		this.lotNumber = lot.getLotNumber();
		this.meltingPoint = lot.getMeltingPoint();
		if (lot.getModifiedBy() != null) this.modifiedBy = lot.getModifiedBy().getCode();
		this.modifiedDate = lot.getModifiedDate();
		this.observedMassOne = lot.getObservedMassOne();
		this.observedMassTwo = lot.getObservedMassTwo();
		this.notebookPage = lot.getNotebookPage();
		this.percentEE = lot.getPercentEE();
		if (lot.getPhysicalState() != null) this.physicalStateCode = lot.getPhysicalState().getCode();
		if (lot.getProject() != null) this.project = lot.getProject().getCode();
		this.purity = lot.getPurity();
		if (lot.getPurityMeasuredBy() != null) this.purityMeasuredByCode = lot.getPurityMeasuredBy().getCode();
		if (lot.getPurityOperator() != null) this.purityOperatorCode = lot.getPurityOperator().getCode();
		this.registrationDate = lot.getRegistrationDate();
		this.retain = lot.getRetain();
		this.retainLocation = lot.getRetainLocation();
		if (lot.getRetainUnits() != null) this.retainUnitsCode = lot.getRetainUnits().getCode();
		if (lot.getRegisteredBy() != null) this.lotRegisteredBy = lot.getRegisteredBy().getCode();
		this.solutionAmount = lot.getSolutionAmount();
		if (lot.getSolutionAmountUnits() != null) this.solutionAmountUnitsCode = lot.getSolutionAmountUnits().getCode();
		this.stockLocation = lot.getStockLocation();
		this.stockSolvent = lot.getStockSolvent();
		this.supplier = lot.getSupplier();
		this.supplierID = lot.getSupplierID();
		this.supplierLot = lot.getSupplierLot();
		this.synthesisDate = lot.getSynthesisDate();
		this.tareWeight = lot.getTareWeight();
		if (lot.getTareWeightUnits() != null) this.tareWeightUnitsCode = lot.getTareWeightUnits().getCode();
		this.totalAmountStored = lot.getTotalAmountStored();
		if (lot.getTotalAmountStoredUnits() != null) this.totalAmountStoredUnitsCode = lot.getTotalAmountStoredUnits().getCode();
		if (lot.getVendor() != null) this.vendorCode = lot.getVendor().getCode();
		if (!lot.getLotAliases().isEmpty()){
			String lotAliases = "";
			for (LotAlias lotAlias : lot.getLotAliases()){
				if (lotAliases.length() > 0) lotAliases += ";";
				lotAliases += lotAlias.getAliasName();
			}
			this.lotAliases = lotAliases;
		}
		SaltForm saltForm = lot.getSaltForm();
		this.saltFormCorpName = saltForm.getCorpName();
		this.casNumber = saltForm.getCasNumber();
		if (!saltForm.getIsoSalts().isEmpty()){
			String isoSaltAbbrevs = "";
			String isoSaltEquivalents = "";
			for (IsoSalt isoSalt : saltForm.getIsoSalts()){
				if (isoSaltAbbrevs.length() > 0) isoSaltAbbrevs+=";";
				if (isoSaltEquivalents.length() > 0) isoSaltAbbrevs+=";";
				if (isoSalt.getType().equalsIgnoreCase("salt")) isoSaltAbbrevs += isoSalt.getSalt().getAbbrev();
				else if (isoSalt.getType().equalsIgnoreCase("isotope")) isoSaltAbbrevs += isoSalt.getIsotope().getAbbrev();
				isoSaltEquivalents += isoSalt.getEquivalents().toString();
			}
			this.saltAbbrevs = isoSaltAbbrevs;
			this.saltEquivalents = isoSaltEquivalents;
		}
		Parent parent = saltForm.getParent();
		this.parentCorpName = parent.getCorpName();
		this.parentNumber = parent.getParentNumber();
		this.parentCommonName = parent.getCommonName();
		if (parent.getStereoCategory() != null) this.parentStereoCategory = parent.getStereoCategory().getCode();
		this.parentStereoComment = parent.getStereoComment();
		this.parentStructure = parent.getMolStructure();
		this.parentMolWeight = parent.getMolWeight();
		this.parentExactMass = parent.getExactMass();
		try{
			this.parentMolFormula = MoleculeUtil.getMolFormula(parent.getMolStructure());
		}catch (CmpdRegMolFormatException e) {
			//leave mol formula blank
		}
		this.parentRegistrationDate = parent.getRegistrationDate();
		if (parent.getRegisteredBy() != null) this.parentRegisteredBy = parent.getRegisteredBy().getCode();
		this.parentModifiedDate = parent.getModifiedDate();
		if (parent.getModifiedBy() != null) this.parentModifiedBy = parent.getModifiedBy().getCode();
		if (!parent.getParentAliases().isEmpty()){
			String parentAliases = "";
			for (ParentAlias parentAlias : parent.getParentAliases()){
				if (parentAliases.length() > 0) parentAliases += ";";
				parentAliases += parentAlias.getAliasName();
			}
			this.parentAliases = parentAliases;
		}
		if (parent.getParentAnnotation() != null) this.parentAnnotationCode = parent.getParentAnnotation().getCode();
		if (parent.getCompoundType() != null) this.parentCompoundTypeCode = parent.getCompoundType().getCode();
		this.parentComment = parent.getComment();
		
	}

	public LotDTO() {
		// Empty constructor
	}
}
