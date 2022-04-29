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
@RooJpaActiveRecord(sequenceName = "BBCHEM_STRUCTURE_PKSEQ", finders = { "findBBChemSaltStructuresByRegEquals", "findBBChemSaltStructuresByPreRegEquals"})
public class BBChemSaltStructure extends AbstractBBChemStructure {


	public static Long countFindBBChemSaltStructuresByPreRegEquals(String preReg) {
        if (preReg == null || preReg.length() == 0) throw new IllegalArgumentException("The preReg argument is required");
        EntityManager em = BBChemSaltStructure.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM BBChemSaltStructure AS o WHERE o.preReg = :preReg", Long.class);
        q.setParameter("preReg", preReg);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindBBChemSaltStructuresByRegEquals(String reg) {
        if (reg == null || reg.length() == 0) throw new IllegalArgumentException("The reg argument is required");
        EntityManager em = BBChemSaltStructure.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM BBChemSaltStructure AS o WHERE o.reg = :reg", Long.class);
        q.setParameter("reg", reg);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<BBChemSaltStructure> findBBChemSaltStructuresByPreRegEquals(String preReg) {
        if (preReg == null || preReg.length() == 0) throw new IllegalArgumentException("The preReg argument is required");
        EntityManager em = BBChemSaltStructure.entityManager();
        TypedQuery<BBChemSaltStructure> q = em.createQuery("SELECT o FROM BBChemSaltStructure AS o WHERE o.preReg = :preReg", BBChemSaltStructure.class);
        q.setParameter("preReg", preReg);
        return q;
    }

	public static TypedQuery<BBChemSaltStructure> findBBChemSaltStructuresByPreRegEquals(String preReg, String sortFieldName, String sortOrder) {
        if (preReg == null || preReg.length() == 0) throw new IllegalArgumentException("The preReg argument is required");
        EntityManager em = BBChemSaltStructure.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM BBChemSaltStructure AS o WHERE o.preReg = :preReg");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<BBChemSaltStructure> q = em.createQuery(queryBuilder.toString(), BBChemSaltStructure.class);
        q.setParameter("preReg", preReg);
        return q;
    }

	public static TypedQuery<BBChemSaltStructure> findBBChemSaltStructuresByRegEquals(String reg) {
        if (reg == null || reg.length() == 0) throw new IllegalArgumentException("The reg argument is required");
        EntityManager em = BBChemSaltStructure.entityManager();
        TypedQuery<BBChemSaltStructure> q = em.createQuery("SELECT o FROM BBChemSaltStructure AS o WHERE o.reg = :reg", BBChemSaltStructure.class);
        q.setParameter("reg", reg);
        return q;
    }

	public static TypedQuery<BBChemSaltStructure> findBBChemSaltStructuresByRegEquals(String reg, String sortFieldName, String sortOrder) {
        if (reg == null || reg.length() == 0) throw new IllegalArgumentException("The reg argument is required");
        EntityManager em = BBChemSaltStructure.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM BBChemSaltStructure AS o WHERE o.reg = :reg");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<BBChemSaltStructure> q = em.createQuery(queryBuilder.toString(), BBChemSaltStructure.class);
        q.setParameter("reg", reg);
        return q;
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static long countBBChemSaltStructures() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BBChemSaltStructure o", Long.class).getSingleResult();
    }

	public static List<BBChemSaltStructure> findAllBBChemSaltStructures() {
        return entityManager().createQuery("SELECT o FROM BBChemSaltStructure o", BBChemSaltStructure.class).getResultList();
    }

	public static List<BBChemSaltStructure> findAllBBChemSaltStructures(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM BBChemSaltStructure o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, BBChemSaltStructure.class).getResultList();
    }

	public static BBChemSaltStructure findBBChemSaltStructure(Long id) {
        if (id == null) return null;
        return entityManager().find(BBChemSaltStructure.class, id);
    }

	public static List<BBChemSaltStructure> findBBChemSaltStructureEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM BBChemSaltStructure o", BBChemSaltStructure.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<BBChemSaltStructure> findBBChemSaltStructureEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM BBChemSaltStructure o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, BBChemSaltStructure.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public BBChemSaltStructure merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        BBChemSaltStructure merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
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

	public static BBChemSaltStructure fromJsonToBBChemSaltStructure(String json) {
        return new JSONDeserializer<BBChemSaltStructure>()
        .use(null, BBChemSaltStructure.class).deserialize(json);
    }

	public static Collection<BBChemSaltStructure> fromJsonArrayToBBChemSaltStructures(String json) {
        return new JSONDeserializer<List<BBChemSaltStructure>>()
        .use("values", BBChemSaltStructure.class).deserialize(json);
    }
}
