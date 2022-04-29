package com.labsynch.labseer.chemclasses.bbchem;

import com.labsynch.labseer.domain.AbstractBBChemStructure;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(sequenceName = "BBCHEM_STRUCTURE_PKSEQ", finders = { "findBBChemStandardizationDryRunStructuresByRegEquals", "findBBChemStandardizationDryRunStructuresByPreRegEquals"})
public class BBChemStandardizationDryRunStructure extends AbstractBBChemStructure {


	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static Long countFindBBChemStandardizationDryRunStructuresByPreRegEquals(String preReg) {
        if (preReg == null || preReg.length() == 0) throw new IllegalArgumentException("The preReg argument is required");
        EntityManager em = BBChemStandardizationDryRunStructure.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM BBChemStandardizationDryRunStructure AS o WHERE o.preReg = :preReg", Long.class);
        q.setParameter("preReg", preReg);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindBBChemStandardizationDryRunStructuresByRegEquals(String reg) {
        if (reg == null || reg.length() == 0) throw new IllegalArgumentException("The reg argument is required");
        EntityManager em = BBChemStandardizationDryRunStructure.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM BBChemStandardizationDryRunStructure AS o WHERE o.reg = :reg", Long.class);
        q.setParameter("reg", reg);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<BBChemStandardizationDryRunStructure> findBBChemStandardizationDryRunStructuresByPreRegEquals(String preReg) {
        if (preReg == null || preReg.length() == 0) throw new IllegalArgumentException("The preReg argument is required");
        EntityManager em = BBChemStandardizationDryRunStructure.entityManager();
        TypedQuery<BBChemStandardizationDryRunStructure> q = em.createQuery("SELECT o FROM BBChemStandardizationDryRunStructure AS o WHERE o.preReg = :preReg", BBChemStandardizationDryRunStructure.class);
        q.setParameter("preReg", preReg);
        return q;
    }

	public static TypedQuery<BBChemStandardizationDryRunStructure> findBBChemStandardizationDryRunStructuresByPreRegEquals(String preReg, String sortFieldName, String sortOrder) {
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

	public static TypedQuery<BBChemStandardizationDryRunStructure> findBBChemStandardizationDryRunStructuresByRegEquals(String reg) {
        if (reg == null || reg.length() == 0) throw new IllegalArgumentException("The reg argument is required");
        EntityManager em = BBChemStandardizationDryRunStructure.entityManager();
        TypedQuery<BBChemStandardizationDryRunStructure> q = em.createQuery("SELECT o FROM BBChemStandardizationDryRunStructure AS o WHERE o.reg = :reg", BBChemStandardizationDryRunStructure.class);
        q.setParameter("reg", reg);
        return q;
    }

	public static TypedQuery<BBChemStandardizationDryRunStructure> findBBChemStandardizationDryRunStructuresByRegEquals(String reg, String sortFieldName, String sortOrder) {
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

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static long countBBChemStandardizationDryRunStructures() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BBChemStandardizationDryRunStructure o", Long.class).getSingleResult();
    }

	public static List<BBChemStandardizationDryRunStructure> findAllBBChemStandardizationDryRunStructures() {
        return entityManager().createQuery("SELECT o FROM BBChemStandardizationDryRunStructure o", BBChemStandardizationDryRunStructure.class).getResultList();
    }

	public static List<BBChemStandardizationDryRunStructure> findAllBBChemStandardizationDryRunStructures(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM BBChemStandardizationDryRunStructure o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, BBChemStandardizationDryRunStructure.class).getResultList();
    }

	public static BBChemStandardizationDryRunStructure findBBChemStandardizationDryRunStructure(Long id) {
        if (id == null) return null;
        return entityManager().find(BBChemStandardizationDryRunStructure.class, id);
    }

	public static List<BBChemStandardizationDryRunStructure> findBBChemStandardizationDryRunStructureEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM BBChemStandardizationDryRunStructure o", BBChemStandardizationDryRunStructure.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<BBChemStandardizationDryRunStructure> findBBChemStandardizationDryRunStructureEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM BBChemStandardizationDryRunStructure o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, BBChemStandardizationDryRunStructure.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public BBChemStandardizationDryRunStructure merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        BBChemStandardizationDryRunStructure merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static BBChemStandardizationDryRunStructure fromJsonToBBChemStandardizationDryRunStructure(String json) {
        return new JSONDeserializer<BBChemStandardizationDryRunStructure>()
        .use(null, BBChemStandardizationDryRunStructure.class).deserialize(json);
    }

	public static Collection<BBChemStandardizationDryRunStructure> fromJsonArrayToBBChemStandardizationDryRunStructures(String json) {
        return new JSONDeserializer<List<BBChemStandardizationDryRunStructure>>()
        .use("values", BBChemStandardizationDryRunStructure.class).deserialize(json);
    }
}
