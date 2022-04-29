package com.labsynch.labseer.chemclasses.bbchem;

import java.util.BitSet;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.labsynch.labseer.domain.AbstractBBChemStructure;
import com.labsynch.labseer.utils.SimpleUtil;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable

public class BBChemSaltFormStructure  extends AbstractBBChemStructure {

    public static List<BBChemSaltFormStructure> findBBChemSaltFormStructuresBySubstructure(BitSet substructure, int maxResults) {
        if (substructure == null || substructure.length() == 0) throw new IllegalArgumentException("The substructure argument is required");
        EntityManager em = BBChemSaltFormStructure.entityManager();
        String fingerprintString = SimpleUtil.bitSetToString(substructure);
        Query q = em.createNativeQuery("SELECT o.* FROM bbchem_salt_form_structure AS o WHERE (o.substructure \\& CAST(:fingerprintString AS bit(2048))) = CAST(:fingerprintString AS bit(2048)) ", BBChemSaltFormStructure.class);
        q.setParameter("fingerprintString", fingerprintString);
       if(maxResults > -1) {
			q.setMaxResults(maxResults);
		}
        return q.getResultList();
    }


	public static Long countFindBBChemSaltFormStructuresByPreRegEquals(String preReg) {
        if (preReg == null || preReg.length() == 0) throw new IllegalArgumentException("The preReg argument is required");
        EntityManager em = BBChemSaltFormStructure.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM BBChemSaltFormStructure AS o WHERE o.preReg = :preReg", Long.class);
        q.setParameter("preReg", preReg);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindBBChemSaltFormStructuresByRegEquals(String reg) {
        if (reg == null || reg.length() == 0) throw new IllegalArgumentException("The reg argument is required");
        EntityManager em = BBChemSaltFormStructure.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM BBChemSaltFormStructure AS o WHERE o.reg = :reg", Long.class);
        q.setParameter("reg", reg);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<BBChemSaltFormStructure> findBBChemSaltFormStructuresByPreRegEquals(String preReg) {
        if (preReg == null || preReg.length() == 0) throw new IllegalArgumentException("The preReg argument is required");
        EntityManager em = BBChemSaltFormStructure.entityManager();
        TypedQuery<BBChemSaltFormStructure> q = em.createQuery("SELECT o FROM BBChemSaltFormStructure AS o WHERE o.preReg = :preReg", BBChemSaltFormStructure.class);
        q.setParameter("preReg", preReg);
        return q;
    }

	public static TypedQuery<BBChemSaltFormStructure> findBBChemSaltFormStructuresByPreRegEquals(String preReg, String sortFieldName, String sortOrder) {
        if (preReg == null || preReg.length() == 0) throw new IllegalArgumentException("The preReg argument is required");
        EntityManager em = BBChemSaltFormStructure.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM BBChemSaltFormStructure AS o WHERE o.preReg = :preReg");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<BBChemSaltFormStructure> q = em.createQuery(queryBuilder.toString(), BBChemSaltFormStructure.class);
        q.setParameter("preReg", preReg);
        return q;
    }

	public static TypedQuery<BBChemSaltFormStructure> findBBChemSaltFormStructuresByRegEquals(String reg) {
        if (reg == null || reg.length() == 0) throw new IllegalArgumentException("The reg argument is required");
        EntityManager em = BBChemSaltFormStructure.entityManager();
        TypedQuery<BBChemSaltFormStructure> q = em.createQuery("SELECT o FROM BBChemSaltFormStructure AS o WHERE o.reg = :reg", BBChemSaltFormStructure.class);
        q.setParameter("reg", reg);
        return q;
    }

	public static TypedQuery<BBChemSaltFormStructure> findBBChemSaltFormStructuresByRegEquals(String reg, String sortFieldName, String sortOrder) {
        if (reg == null || reg.length() == 0) throw new IllegalArgumentException("The reg argument is required");
        EntityManager em = BBChemSaltFormStructure.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM BBChemSaltFormStructure AS o WHERE o.reg = :reg");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<BBChemSaltFormStructure> q = em.createQuery(queryBuilder.toString(), BBChemSaltFormStructure.class);
        q.setParameter("reg", reg);
        return q;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static long countBBChemSaltFormStructures() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BBChemSaltFormStructure o", Long.class).getSingleResult();
    }

	public static List<BBChemSaltFormStructure> findAllBBChemSaltFormStructures() {
        return entityManager().createQuery("SELECT o FROM BBChemSaltFormStructure o", BBChemSaltFormStructure.class).getResultList();
    }

	public static List<BBChemSaltFormStructure> findAllBBChemSaltFormStructures(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM BBChemSaltFormStructure o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, BBChemSaltFormStructure.class).getResultList();
    }

	public static BBChemSaltFormStructure findBBChemSaltFormStructure(Long id) {
        if (id == null) return null;
        return entityManager().find(BBChemSaltFormStructure.class, id);
    }

	public static List<BBChemSaltFormStructure> findBBChemSaltFormStructureEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM BBChemSaltFormStructure o", BBChemSaltFormStructure.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<BBChemSaltFormStructure> findBBChemSaltFormStructureEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM BBChemSaltFormStructure o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, BBChemSaltFormStructure.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public BBChemSaltFormStructure merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        BBChemSaltFormStructure merged = this.entityManager.merge(this);
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

	public static BBChemSaltFormStructure fromJsonToBBChemSaltFormStructure(String json) {
        return new JSONDeserializer<BBChemSaltFormStructure>()
        .use(null, BBChemSaltFormStructure.class).deserialize(json);
    }

	public static Collection<BBChemSaltFormStructure> fromJsonArrayToBBChemSaltFormStructures(String json) {
        return new JSONDeserializer<List<BBChemSaltFormStructure>>()
        .use("values", BBChemSaltFormStructure.class).deserialize(json);
    }
}
