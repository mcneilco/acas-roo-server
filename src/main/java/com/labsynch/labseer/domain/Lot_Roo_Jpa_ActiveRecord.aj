// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.Lot;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

privileged aspect Lot_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext
    transient EntityManager Lot.entityManager;
    
    public static final List<String> Lot.fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "mainConfig", "formatBatchDigits", "appendSaltCodeToLotName", "noSaltCode", "corpBatchFormat", "corpPrefix", "corpSeparator", "asDrawnStruct", "lotAsDrawnCdId", "buid", "corpName", "lotNumber", "lotMolWeight", "synthesisDate", "registrationDate", "registeredBy", "modifiedDate", "modifiedBy", "barcode", "color", "notebookPage", "amount", "amountUnits", "solutionAmount", "solutionAmountUnits", "supplier", "supplierID", "purity", "purityOperator", "purityMeasuredBy", "chemist", "percentEE", "comments", "isVirtual", "ignore", "physicalState", "vendor", "vendorID", "saltForm", "fileLists", "retain", "retainUnits", "retainLocation", "meltingPoint", "boilingPoint", "supplierLot", "project", "parent", "bulkLoadFile", "lambda", "absorbance", "stockSolvent", "stockLocation", "observedMassOne", "observedMassTwo", "tareWeight", "tareWeightUnits", "totalAmountStored", "totalAmountStoredUnits", "lotAliases", "storageLocation");
    
    public static final EntityManager Lot.entityManager() {
        EntityManager em = new Lot().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long Lot.countLots() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Lot o", Long.class).getSingleResult();
    }
    
    public static List<Lot> Lot.findAllLots(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Lot o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Lot.class).getResultList();
    }
    
    public static List<Lot> Lot.findLotEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Lot o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Lot.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void Lot.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void Lot.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Lot attached = Lot.findLot(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void Lot.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void Lot.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Lot Lot.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Lot merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
