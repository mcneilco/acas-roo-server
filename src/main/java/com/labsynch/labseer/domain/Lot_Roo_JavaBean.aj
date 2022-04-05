// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.BulkLoadFile;
import com.labsynch.labseer.domain.FileList;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.LotAlias;
import com.labsynch.labseer.domain.Operator;
import com.labsynch.labseer.domain.PhysicalState;
import com.labsynch.labseer.domain.PurityMeasuredBy;
import com.labsynch.labseer.domain.SolutionUnit;
import com.labsynch.labseer.domain.Unit;
import com.labsynch.labseer.domain.Vendor;
import java.util.Date;
import java.util.Set;

privileged aspect Lot_Roo_JavaBean {
    
    public String Lot.getAsDrawnStruct() {
        return this.asDrawnStruct;
    }
    
    public void Lot.setAsDrawnStruct(String asDrawnStruct) {
        this.asDrawnStruct = asDrawnStruct;
    }
    
    public int Lot.getLotAsDrawnCdId() {
        return this.lotAsDrawnCdId;
    }
    
    public void Lot.setLotAsDrawnCdId(int lotAsDrawnCdId) {
        this.lotAsDrawnCdId = lotAsDrawnCdId;
    }
    
    public void Lot.setBuid(Long buid) {
        this.buid = buid;
    }
    
    public String Lot.getCorpName() {
        return this.corpName;
    }
    
    public void Lot.setCorpName(String corpName) {
        this.corpName = corpName;
    }
    
    public Integer Lot.getLotNumber() {
        return this.lotNumber;
    }
    
    public void Lot.setLotNumber(Integer lotNumber) {
        this.lotNumber = lotNumber;
    }
    
    public Double Lot.getLotMolWeight() {
        return this.lotMolWeight;
    }
    
    public void Lot.setLotMolWeight(Double lotMolWeight) {
        this.lotMolWeight = lotMolWeight;
    }
    
    public Date Lot.getSynthesisDate() {
        return this.synthesisDate;
    }
    
    public void Lot.setSynthesisDate(Date synthesisDate) {
        this.synthesisDate = synthesisDate;
    }
    
    public Date Lot.getRegistrationDate() {
        return this.registrationDate;
    }
    
    public void Lot.setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    public String Lot.getRegisteredBy() {
        return this.registeredBy;
    }
    
    public void Lot.setRegisteredBy(String registeredBy) {
        this.registeredBy = registeredBy;
    }
    
    public Date Lot.getModifiedDate() {
        return this.modifiedDate;
    }
    
    public void Lot.setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    
    public String Lot.getModifiedBy() {
        return this.modifiedBy;
    }
    
    public void Lot.setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
    
    public String Lot.getBarcode() {
        return this.barcode;
    }
    
    public void Lot.setBarcode(String barcode) {
        this.barcode = barcode;
    }
    
    public String Lot.getColor() {
        return this.color;
    }
    
    public void Lot.setColor(String color) {
        this.color = color;
    }
    
    public String Lot.getNotebookPage() {
        return this.notebookPage;
    }
    
    public void Lot.setNotebookPage(String notebookPage) {
        this.notebookPage = notebookPage;
    }
    
    public Double Lot.getAmount() {
        return this.amount;
    }
    
    public void Lot.setAmount(Double amount) {
        this.amount = amount;
    }
    
    public Unit Lot.getAmountUnits() {
        return this.amountUnits;
    }
    
    public void Lot.setAmountUnits(Unit amountUnits) {
        this.amountUnits = amountUnits;
    }
    
    public Double Lot.getSolutionAmount() {
        return this.solutionAmount;
    }
    
    public void Lot.setSolutionAmount(Double solutionAmount) {
        this.solutionAmount = solutionAmount;
    }
    
    public SolutionUnit Lot.getSolutionAmountUnits() {
        return this.solutionAmountUnits;
    }
    
    public void Lot.setSolutionAmountUnits(SolutionUnit solutionAmountUnits) {
        this.solutionAmountUnits = solutionAmountUnits;
    }
    
    public String Lot.getSupplier() {
        return this.supplier;
    }
    
    public void Lot.setSupplier(String supplier) {
        this.supplier = supplier;
    }
    
    public String Lot.getSupplierID() {
        return this.supplierID;
    }
    
    public void Lot.setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }
    
    public Double Lot.getPurity() {
        return this.purity;
    }
    
    public void Lot.setPurity(Double purity) {
        this.purity = purity;
    }
    
    public Operator Lot.getPurityOperator() {
        return this.purityOperator;
    }
    
    public void Lot.setPurityOperator(Operator purityOperator) {
        this.purityOperator = purityOperator;
    }
    
    public PurityMeasuredBy Lot.getPurityMeasuredBy() {
        return this.purityMeasuredBy;
    }
    
    public void Lot.setPurityMeasuredBy(PurityMeasuredBy purityMeasuredBy) {
        this.purityMeasuredBy = purityMeasuredBy;
    }
    
    public String Lot.getChemist() {
        return this.chemist;
    }
    
    public void Lot.setChemist(String chemist) {
        this.chemist = chemist;
    }
    
    public Double Lot.getPercentEE() {
        return this.percentEE;
    }
    
    public void Lot.setPercentEE(Double percentEE) {
        this.percentEE = percentEE;
    }
    
    public String Lot.getComments() {
        return this.comments;
    }
    
    public void Lot.setComments(String comments) {
        this.comments = comments;
    }
    
    public Boolean Lot.getIsVirtual() {
        return this.isVirtual;
    }
    
    public void Lot.setIsVirtual(Boolean isVirtual) {
        this.isVirtual = isVirtual;
    }
    
    public Boolean Lot.getIgnore() {
        return this.ignore;
    }
    
    public void Lot.setIgnore(Boolean ignore) {
        this.ignore = ignore;
    }
    
    public PhysicalState Lot.getPhysicalState() {
        return this.physicalState;
    }
    
    public void Lot.setPhysicalState(PhysicalState physicalState) {
        this.physicalState = physicalState;
    }
    
    public Vendor Lot.getVendor() {
        return this.vendor;
    }
    
    public void Lot.setVendor(Vendor vendor) {
        this.vendor = vendor;
    }
    
    public String Lot.getVendorId() {
        return this.vendorId;
    }
    
    public void Lot.setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }
    
    public Set<FileList> Lot.getFileLists() {
        return this.fileLists;
    }
    
    public void Lot.setFileLists(Set<FileList> fileLists) {
        this.fileLists = fileLists;
    }
    
    public Double Lot.getRetain() {
        return this.retain;
    }
    
    public void Lot.setRetain(Double retain) {
        this.retain = retain;
    }
    
    public Unit Lot.getRetainUnits() {
        return this.retainUnits;
    }
    
    public void Lot.setRetainUnits(Unit retainUnits) {
        this.retainUnits = retainUnits;
    }
    
    public String Lot.getRetainLocation() {
        return this.retainLocation;
    }
    
    public void Lot.setRetainLocation(String retainLocation) {
        this.retainLocation = retainLocation;
    }
    
    public Double Lot.getMeltingPoint() {
        return this.meltingPoint;
    }
    
    public void Lot.setMeltingPoint(Double meltingPoint) {
        this.meltingPoint = meltingPoint;
    }
    
    public Double Lot.getBoilingPoint() {
        return this.boilingPoint;
    }
    
    public void Lot.setBoilingPoint(Double boilingPoint) {
        this.boilingPoint = boilingPoint;
    }
    
    public String Lot.getSupplierLot() {
        return this.supplierLot;
    }
    
    public void Lot.setSupplierLot(String supplierLot) {
        this.supplierLot = supplierLot;
    }
    
    public String Lot.getProject() {
        return this.project;
    }
    
    public void Lot.setProject(String project) {
        this.project = project;
    }
    
    public BulkLoadFile Lot.getBulkLoadFile() {
        return this.bulkLoadFile;
    }
    
    public void Lot.setBulkLoadFile(BulkLoadFile bulkLoadFile) {
        this.bulkLoadFile = bulkLoadFile;
    }
    
    public Double Lot.getLambda() {
        return this.lambda;
    }
    
    public void Lot.setLambda(Double lambda) {
        this.lambda = lambda;
    }
    
    public Double Lot.getAbsorbance() {
        return this.absorbance;
    }
    
    public void Lot.setAbsorbance(Double absorbance) {
        this.absorbance = absorbance;
    }
    
    public String Lot.getStockSolvent() {
        return this.stockSolvent;
    }
    
    public void Lot.setStockSolvent(String stockSolvent) {
        this.stockSolvent = stockSolvent;
    }
    
    public String Lot.getStockLocation() {
        return this.stockLocation;
    }
    
    public void Lot.setStockLocation(String stockLocation) {
        this.stockLocation = stockLocation;
    }
    
    public Double Lot.getObservedMassOne() {
        return this.observedMassOne;
    }
    
    public void Lot.setObservedMassOne(Double observedMassOne) {
        this.observedMassOne = observedMassOne;
    }
    
    public Double Lot.getObservedMassTwo() {
        return this.observedMassTwo;
    }
    
    public void Lot.setObservedMassTwo(Double observedMassTwo) {
        this.observedMassTwo = observedMassTwo;
    }
    
    public Double Lot.getTareWeight() {
        return this.tareWeight;
    }
    
    public void Lot.setTareWeight(Double tareWeight) {
        this.tareWeight = tareWeight;
    }
    
    public Unit Lot.getTareWeightUnits() {
        return this.tareWeightUnits;
    }
    
    public void Lot.setTareWeightUnits(Unit tareWeightUnits) {
        this.tareWeightUnits = tareWeightUnits;
    }
    
    public Double Lot.getTotalAmountStored() {
        return this.totalAmountStored;
    }
    
    public void Lot.setTotalAmountStored(Double totalAmountStored) {
        this.totalAmountStored = totalAmountStored;
    }
    
    public Unit Lot.getTotalAmountStoredUnits() {
        return this.totalAmountStoredUnits;
    }
    
    public void Lot.setTotalAmountStoredUnits(Unit totalAmountStoredUnits) {
        this.totalAmountStoredUnits = totalAmountStoredUnits;
    }
    
    public Set<LotAlias> Lot.getLotAliases() {
        return this.lotAliases;
    }
    
    public void Lot.setLotAliases(Set<LotAlias> lotAliases) {
        this.lotAliases = lotAliases;
    }
    
}
