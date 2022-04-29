package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

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
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


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
	private String vendorId;
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
		if (lot.getChemist() != null) this.chemist = lot.getChemist();
		this.color = lot.getColor();
		this.comments = lot.getComments();
		if (lot.getCorpName() != null) this.lotCorpName = lot.getCorpName();
		this.isVirtual = lot.getIsVirtual();
		this.lambda = lot.getLambda();
		this.lotMolWeight = lot.getLotMolWeight();
		this.lotNumber = lot.getLotNumber();
		this.meltingPoint = lot.getMeltingPoint();
		if (lot.getModifiedBy() != null) this.modifiedBy = lot.getModifiedBy();
		this.modifiedDate = lot.getModifiedDate();
		this.observedMassOne = lot.getObservedMassOne();
		this.observedMassTwo = lot.getObservedMassTwo();
		this.notebookPage = lot.getNotebookPage();
		this.percentEE = lot.getPercentEE();
		if (lot.getPhysicalState() != null) this.physicalStateCode = lot.getPhysicalState().getCode();
		if (lot.getProject() != null) this.project = lot.getProject();
		this.purity = lot.getPurity();
		if (lot.getPurityMeasuredBy() != null) this.purityMeasuredByCode = lot.getPurityMeasuredBy().getCode();
		if (lot.getPurityOperator() != null) this.purityOperatorCode = lot.getPurityOperator().getCode();
		this.registrationDate = lot.getRegistrationDate();
		this.retain = lot.getRetain();
		this.retainLocation = lot.getRetainLocation();
		if (lot.getRetainUnits() != null) this.retainUnitsCode = lot.getRetainUnits().getCode();
		if (lot.getRegisteredBy() != null) this.lotRegisteredBy = lot.getRegisteredBy();
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
		if (parent.getRegisteredBy() != null) this.parentRegisteredBy = parent.getRegisteredBy();
		this.parentModifiedDate = parent.getModifiedDate();
		if (parent.getModifiedBy() != null) this.parentModifiedBy = parent.getModifiedBy();
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

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Double getAbsorbance() {
        return this.absorbance;
    }

	public void setAbsorbance(Double absorbance) {
        this.absorbance = absorbance;
    }

	public Double getAmount() {
        return this.amount;
    }

	public void setAmount(Double amount) {
        this.amount = amount;
    }

	public String getAmountUnitsCode() {
        return this.amountUnitsCode;
    }

	public void setAmountUnitsCode(String amountUnitsCode) {
        this.amountUnitsCode = amountUnitsCode;
    }

	public String getBarcode() {
        return this.barcode;
    }

	public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

	public Double getBoilingPoint() {
        return this.boilingPoint;
    }

	public void setBoilingPoint(Double boilingPoint) {
        this.boilingPoint = boilingPoint;
    }

	public Long getBuid() {
        return this.buid;
    }

	public void setBuid(Long buid) {
        this.buid = buid;
    }

	public String getBulkLoadFile() {
        return this.bulkLoadFile;
    }

	public void setBulkLoadFile(String bulkLoadFile) {
        this.bulkLoadFile = bulkLoadFile;
    }

	public String getChemist() {
        return this.chemist;
    }

	public void setChemist(String chemist) {
        this.chemist = chemist;
    }

	public String getColor() {
        return this.color;
    }

	public void setColor(String color) {
        this.color = color;
    }

	public String getComments() {
        return this.comments;
    }

	public void setComments(String comments) {
        this.comments = comments;
    }

	public String getLotCorpName() {
        return this.lotCorpName;
    }

	public void setLotCorpName(String lotCorpName) {
        this.lotCorpName = lotCorpName;
    }

	public Boolean getIsVirtual() {
        return this.isVirtual;
    }

	public void setIsVirtual(Boolean isVirtual) {
        this.isVirtual = isVirtual;
    }

	public Double getLambda() {
        return this.lambda;
    }

	public void setLambda(Double lambda) {
        this.lambda = lambda;
    }

	public String getLotAliases() {
        return this.lotAliases;
    }

	public void setLotAliases(String lotAliases) {
        this.lotAliases = lotAliases;
    }

	public Double getLotMolWeight() {
        return this.lotMolWeight;
    }

	public void setLotMolWeight(Double lotMolWeight) {
        this.lotMolWeight = lotMolWeight;
    }

	public Integer getLotNumber() {
        return this.lotNumber;
    }

	public void setLotNumber(Integer lotNumber) {
        this.lotNumber = lotNumber;
    }

	public Double getMeltingPoint() {
        return this.meltingPoint;
    }

	public void setMeltingPoint(Double meltingPoint) {
        this.meltingPoint = meltingPoint;
    }

	public String getModifiedBy() {
        return this.modifiedBy;
    }

	public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

	public Date getModifiedDate() {
        return this.modifiedDate;
    }

	public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

	public String getNotebookPage() {
        return this.notebookPage;
    }

	public void setNotebookPage(String notebookPage) {
        this.notebookPage = notebookPage;
    }

	public Double getObservedMassOne() {
        return this.observedMassOne;
    }

	public void setObservedMassOne(Double observedMassOne) {
        this.observedMassOne = observedMassOne;
    }

	public Double getObservedMassTwo() {
        return this.observedMassTwo;
    }

	public void setObservedMassTwo(Double observedMassTwo) {
        this.observedMassTwo = observedMassTwo;
    }

	public Double getPercentEE() {
        return this.percentEE;
    }

	public void setPercentEE(Double percentEE) {
        this.percentEE = percentEE;
    }

	public String getPhysicalStateCode() {
        return this.physicalStateCode;
    }

	public void setPhysicalStateCode(String physicalStateCode) {
        this.physicalStateCode = physicalStateCode;
    }

	public String getProject() {
        return this.project;
    }

	public void setProject(String project) {
        this.project = project;
    }

	public Double getPurity() {
        return this.purity;
    }

	public void setPurity(Double purity) {
        this.purity = purity;
    }

	public String getPurityMeasuredByCode() {
        return this.purityMeasuredByCode;
    }

	public void setPurityMeasuredByCode(String purityMeasuredByCode) {
        this.purityMeasuredByCode = purityMeasuredByCode;
    }

	public String getPurityOperatorCode() {
        return this.purityOperatorCode;
    }

	public void setPurityOperatorCode(String purityOperatorCode) {
        this.purityOperatorCode = purityOperatorCode;
    }

	public Date getRegistrationDate() {
        return this.registrationDate;
    }

	public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

	public Double getRetain() {
        return this.retain;
    }

	public void setRetain(Double retain) {
        this.retain = retain;
    }

	public String getRetainLocation() {
        return this.retainLocation;
    }

	public void setRetainLocation(String retainLocation) {
        this.retainLocation = retainLocation;
    }

	public String getRetainUnitsCode() {
        return this.retainUnitsCode;
    }

	public void setRetainUnitsCode(String retainUnitsCode) {
        this.retainUnitsCode = retainUnitsCode;
    }

	public String getLotRegisteredBy() {
        return this.lotRegisteredBy;
    }

	public void setLotRegisteredBy(String lotRegisteredBy) {
        this.lotRegisteredBy = lotRegisteredBy;
    }

	public Double getSolutionAmount() {
        return this.solutionAmount;
    }

	public void setSolutionAmount(Double solutionAmount) {
        this.solutionAmount = solutionAmount;
    }

	public String getSolutionAmountUnitsCode() {
        return this.solutionAmountUnitsCode;
    }

	public void setSolutionAmountUnitsCode(String solutionAmountUnitsCode) {
        this.solutionAmountUnitsCode = solutionAmountUnitsCode;
    }

	public String getStockLocation() {
        return this.stockLocation;
    }

	public void setStockLocation(String stockLocation) {
        this.stockLocation = stockLocation;
    }

	public String getStockSolvent() {
        return this.stockSolvent;
    }

	public void setStockSolvent(String stockSolvent) {
        this.stockSolvent = stockSolvent;
    }

	public String getSupplier() {
        return this.supplier;
    }

	public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

	public String getSupplierID() {
        return this.supplierID;
    }

	public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

	public String getSupplierLot() {
        return this.supplierLot;
    }

	public void setSupplierLot(String supplierLot) {
        this.supplierLot = supplierLot;
    }

	public Date getSynthesisDate() {
        return this.synthesisDate;
    }

	public void setSynthesisDate(Date synthesisDate) {
        this.synthesisDate = synthesisDate;
    }

	public Double getTareWeight() {
        return this.tareWeight;
    }

	public void setTareWeight(Double tareWeight) {
        this.tareWeight = tareWeight;
    }

	public String getTareWeightUnitsCode() {
        return this.tareWeightUnitsCode;
    }

	public void setTareWeightUnitsCode(String tareWeightUnitsCode) {
        this.tareWeightUnitsCode = tareWeightUnitsCode;
    }

	public Double getTotalAmountStored() {
        return this.totalAmountStored;
    }

	public void setTotalAmountStored(Double totalAmountStored) {
        this.totalAmountStored = totalAmountStored;
    }

	public String getTotalAmountStoredUnitsCode() {
        return this.totalAmountStoredUnitsCode;
    }

	public void setTotalAmountStoredUnitsCode(String totalAmountStoredUnitsCode) {
        this.totalAmountStoredUnitsCode = totalAmountStoredUnitsCode;
    }

	public String getVendorCode() {
        return this.vendorCode;
    }

	public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

	public String getVendorId() {
        return this.vendorId;
    }

	public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

	public String getSaltFormCorpName() {
        return this.saltFormCorpName;
    }

	public void setSaltFormCorpName(String saltFormCorpName) {
        this.saltFormCorpName = saltFormCorpName;
    }

	public String getCasNumber() {
        return this.casNumber;
    }

	public void setCasNumber(String casNumber) {
        this.casNumber = casNumber;
    }

	public String getSaltAbbrevs() {
        return this.saltAbbrevs;
    }

	public void setSaltAbbrevs(String saltAbbrevs) {
        this.saltAbbrevs = saltAbbrevs;
    }

	public String getSaltEquivalents() {
        return this.saltEquivalents;
    }

	public void setSaltEquivalents(String saltEquivalents) {
        this.saltEquivalents = saltEquivalents;
    }

	public String getParentCorpName() {
        return this.parentCorpName;
    }

	public void setParentCorpName(String parentCorpName) {
        this.parentCorpName = parentCorpName;
    }

	public long getParentNumber() {
        return this.parentNumber;
    }

	public void setParentNumber(long parentNumber) {
        this.parentNumber = parentNumber;
    }

	public String getParentCommonName() {
        return this.parentCommonName;
    }

	public void setParentCommonName(String parentCommonName) {
        this.parentCommonName = parentCommonName;
    }

	public String getParentStereoCategory() {
        return this.parentStereoCategory;
    }

	public void setParentStereoCategory(String parentStereoCategory) {
        this.parentStereoCategory = parentStereoCategory;
    }

	public String getParentStereoComment() {
        return this.parentStereoComment;
    }

	public void setParentStereoComment(String parentStereoComment) {
        this.parentStereoComment = parentStereoComment;
    }

	public String getParentStructure() {
        return this.parentStructure;
    }

	public void setParentStructure(String parentStructure) {
        this.parentStructure = parentStructure;
    }

	public Double getParentMolWeight() {
        return this.parentMolWeight;
    }

	public void setParentMolWeight(Double parentMolWeight) {
        this.parentMolWeight = parentMolWeight;
    }

	public Double getParentExactMass() {
        return this.parentExactMass;
    }

	public void setParentExactMass(Double parentExactMass) {
        this.parentExactMass = parentExactMass;
    }

	public String getParentMolFormula() {
        return this.parentMolFormula;
    }

	public void setParentMolFormula(String parentMolFormula) {
        this.parentMolFormula = parentMolFormula;
    }

	public Date getParentRegistrationDate() {
        return this.parentRegistrationDate;
    }

	public void setParentRegistrationDate(Date parentRegistrationDate) {
        this.parentRegistrationDate = parentRegistrationDate;
    }

	public String getParentRegisteredBy() {
        return this.parentRegisteredBy;
    }

	public void setParentRegisteredBy(String parentRegisteredBy) {
        this.parentRegisteredBy = parentRegisteredBy;
    }

	public Date getParentModifiedDate() {
        return this.parentModifiedDate;
    }

	public void setParentModifiedDate(Date parentModifiedDate) {
        this.parentModifiedDate = parentModifiedDate;
    }

	public String getParentModifiedBy() {
        return this.parentModifiedBy;
    }

	public void setParentModifiedBy(String parentModifiedBy) {
        this.parentModifiedBy = parentModifiedBy;
    }

	public String getParentAliases() {
        return this.parentAliases;
    }

	public void setParentAliases(String parentAliases) {
        this.parentAliases = parentAliases;
    }

	public String getParentAnnotationCode() {
        return this.parentAnnotationCode;
    }

	public void setParentAnnotationCode(String parentAnnotationCode) {
        this.parentAnnotationCode = parentAnnotationCode;
    }

	public String getParentCompoundTypeCode() {
        return this.parentCompoundTypeCode;
    }

	public void setParentCompoundTypeCode(String parentCompoundTypeCode) {
        this.parentCompoundTypeCode = parentCompoundTypeCode;
    }

	public String getParentComment() {
        return this.parentComment;
    }

	public void setParentComment(String parentComment) {
        this.parentComment = parentComment;
    }

	public Boolean getParentIsMixture() {
        return this.parentIsMixture;
    }

	public void setParentIsMixture(Boolean parentIsMixture) {
        this.parentIsMixture = parentIsMixture;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static LotDTO fromJsonToLotDTO(String json) {
        return new JSONDeserializer<LotDTO>()
        .use(null, LotDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<LotDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<LotDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<LotDTO> fromJsonArrayToLoes(String json) {
        return new JSONDeserializer<List<LotDTO>>()
        .use("values", LotDTO.class).deserialize(json);
    }
}
