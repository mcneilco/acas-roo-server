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
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
public class BBChemDryRunStructure extends AbstractBBChemStructure {


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

	public static BBChemDryRunStructure fromJsonToBBChemDryRunStructure(String json) {
        return new JSONDeserializer<BBChemDryRunStructure>()
        .use(null, BBChemDryRunStructure.class).deserialize(json);
    }

	public static Collection<BBChemDryRunStructure> fromJsonArrayToBBChemDryRunStructures(String json) {
        return new JSONDeserializer<List<BBChemDryRunStructure>>()
        .use("values", BBChemDryRunStructure.class).deserialize(json);
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static long countBBChemDryRunStructures() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BBChemDryRunStructure o", Long.class).getSingleResult();
    }

	public static List<BBChemDryRunStructure> findAllBBChemDryRunStructures() {
        return entityManager().createQuery("SELECT o FROM BBChemDryRunStructure o", BBChemDryRunStructure.class).getResultList();
    }

	public static List<BBChemDryRunStructure> findAllBBChemDryRunStructures(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM BBChemDryRunStructure o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, BBChemDryRunStructure.class).getResultList();
    }

	public static BBChemDryRunStructure findBBChemDryRunStructure(Long id) {
        if (id == null) return null;
        return entityManager().find(BBChemDryRunStructure.class, id);
    }

	public static List<BBChemDryRunStructure> findBBChemDryRunStructureEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM BBChemDryRunStructure o", BBChemDryRunStructure.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<BBChemDryRunStructure> findBBChemDryRunStructureEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM BBChemDryRunStructure o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, BBChemDryRunStructure.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public BBChemDryRunStructure merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        BBChemDryRunStructure merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static Long countFindBBChemDryRunStructuresByPreRegEquals(String preReg) {
        if (preReg == null || preReg.length() == 0) throw new IllegalArgumentException("The preReg argument is required");
        EntityManager em = BBChemDryRunStructure.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM BBChemDryRunStructure AS o WHERE o.preReg = :preReg", Long.class);
        q.setParameter("preReg", preReg);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindBBChemDryRunStructuresByRegEquals(String reg) {
        if (reg == null || reg.length() == 0) throw new IllegalArgumentException("The reg argument is required");
        EntityManager em = BBChemDryRunStructure.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM BBChemDryRunStructure AS o WHERE o.reg = :reg", Long.class);
        q.setParameter("reg", reg);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<BBChemDryRunStructure> findBBChemDryRunStructuresByPreRegEquals(String preReg) {
        if (preReg == null || preReg.length() == 0) throw new IllegalArgumentException("The preReg argument is required");
        EntityManager em = BBChemDryRunStructure.entityManager();
        TypedQuery<BBChemDryRunStructure> q = em.createQuery("SELECT o FROM BBChemDryRunStructure AS o WHERE o.preReg = :preReg", BBChemDryRunStructure.class);
        q.setParameter("preReg", preReg);
        return q;
    }

	public static TypedQuery<BBChemDryRunStructure> findBBChemDryRunStructuresByPreRegEquals(String preReg, String sortFieldName, String sortOrder) {
        if (preReg == null || preReg.length() == 0) throw new IllegalArgumentException("The preReg argument is required");
        EntityManager em = BBChemDryRunStructure.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM BBChemDryRunStructure AS o WHERE o.preReg = :preReg");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<BBChemDryRunStructure> q = em.createQuery(queryBuilder.toString(), BBChemDryRunStructure.class);
        q.setParameter("preReg", preReg);
        return q;
    }

	public static TypedQuery<BBChemDryRunStructure> findBBChemDryRunStructuresByRegEquals(String reg) {
        if (reg == null || reg.length() == 0) throw new IllegalArgumentException("The reg argument is required");
        EntityManager em = BBChemDryRunStructure.entityManager();
        TypedQuery<BBChemDryRunStructure> q = em.createQuery("SELECT o FROM BBChemDryRunStructure AS o WHERE o.reg = :reg", BBChemDryRunStructure.class);
        q.setParameter("reg", reg);
        return q;
    }

	public static TypedQuery<BBChemDryRunStructure> findBBChemDryRunStructuresByRegEquals(String reg, String sortFieldName, String sortOrder) {
        if (reg == null || reg.length() == 0) throw new IllegalArgumentException("The reg argument is required");
        EntityManager em = BBChemDryRunStructure.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM BBChemDryRunStructure AS o WHERE o.reg = :reg");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<BBChemDryRunStructure> q = em.createQuery(queryBuilder.toString(), BBChemDryRunStructure.class);
        q.setParameter("reg", reg);
        return q;
    }
}
