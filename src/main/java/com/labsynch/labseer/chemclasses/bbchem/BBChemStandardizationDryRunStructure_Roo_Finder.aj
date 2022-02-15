// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.chemclasses.bbchem;

import com.labsynch.labseer.chemclasses.bbchem.BBChemStandardizationDryRunStructure;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

privileged aspect BBChemStandardizationDryRunStructure_Roo_Finder {
    
    public static Long BBChemStandardizationDryRunStructure.countFindBBChemStandardizationDryRunStructuresByPreRegEquals(String preReg) {
        if (preReg == null || preReg.length() == 0) throw new IllegalArgumentException("The preReg argument is required");
        EntityManager em = BBChemStandardizationDryRunStructure.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM BBChemStandardizationDryRunStructure AS o WHERE o.preReg = :preReg", Long.class);
        q.setParameter("preReg", preReg);
        return ((Long) q.getSingleResult());
    }
    
    public static Long BBChemStandardizationDryRunStructure.countFindBBChemStandardizationDryRunStructuresByRegEquals(String reg) {
        if (reg == null || reg.length() == 0) throw new IllegalArgumentException("The reg argument is required");
        EntityManager em = BBChemStandardizationDryRunStructure.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM BBChemStandardizationDryRunStructure AS o WHERE o.reg = :reg", Long.class);
        q.setParameter("reg", reg);
        return ((Long) q.getSingleResult());
    }
    
    public static TypedQuery<BBChemStandardizationDryRunStructure> BBChemStandardizationDryRunStructure.findBBChemStandardizationDryRunStructuresByPreRegEquals(String preReg) {
        if (preReg == null || preReg.length() == 0) throw new IllegalArgumentException("The preReg argument is required");
        EntityManager em = BBChemStandardizationDryRunStructure.entityManager();
        TypedQuery<BBChemStandardizationDryRunStructure> q = em.createQuery("SELECT o FROM BBChemStandardizationDryRunStructure AS o WHERE o.preReg = :preReg", BBChemStandardizationDryRunStructure.class);
        q.setParameter("preReg", preReg);
        return q;
    }
    
    public static TypedQuery<BBChemStandardizationDryRunStructure> BBChemStandardizationDryRunStructure.findBBChemStandardizationDryRunStructuresByPreRegEquals(String preReg, String sortFieldName, String sortOrder) {
        if (preReg == null || preReg.length() == 0) throw new IllegalArgumentException("The preReg argument is required");
        EntityManager em = BBChemStandardizationDryRunStructure.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM BBChemStandardizationDryRunStructure AS o WHERE o.preReg = :preReg");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<BBChemStandardizationDryRunStructure> q = em.createQuery(queryBuilder.toString(), BBChemStandardizationDryRunStructure.class);
        q.setParameter("preReg", preReg);
        return q;
    }
    
    public static TypedQuery<BBChemStandardizationDryRunStructure> BBChemStandardizationDryRunStructure.findBBChemStandardizationDryRunStructuresByRegEquals(String reg) {
        if (reg == null || reg.length() == 0) throw new IllegalArgumentException("The reg argument is required");
        EntityManager em = BBChemStandardizationDryRunStructure.entityManager();
        TypedQuery<BBChemStandardizationDryRunStructure> q = em.createQuery("SELECT o FROM BBChemStandardizationDryRunStructure AS o WHERE o.reg = :reg", BBChemStandardizationDryRunStructure.class);
        q.setParameter("reg", reg);
        return q;
    }
    
    public static TypedQuery<BBChemStandardizationDryRunStructure> BBChemStandardizationDryRunStructure.findBBChemStandardizationDryRunStructuresByRegEquals(String reg, String sortFieldName, String sortOrder) {
        if (reg == null || reg.length() == 0) throw new IllegalArgumentException("The reg argument is required");
        EntityManager em = BBChemStandardizationDryRunStructure.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM BBChemStandardizationDryRunStructure AS o WHERE o.reg = :reg");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<BBChemStandardizationDryRunStructure> q = em.createQuery(queryBuilder.toString(), BBChemStandardizationDryRunStructure.class);
        q.setParameter("reg", reg);
        return q;
    }
    
}