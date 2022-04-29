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
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
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
@RooJpaActiveRecord(sequenceName = "BBCHEM_STRUCTURE_PKSEQ", finders = { "findBBChemParentStructuresByRegEquals", "findBBChemParentStructuresByPreRegEquals"})
public class BBChemParentStructure extends AbstractBBChemStructure {

    public static List<BBChemParentStructure> findBBChemParentStructuresBySubstructure(BitSet substructure, int maxResults) {
        if (substructure == null || substructure.length() == 0) throw new IllegalArgumentException("The substructure argument is required");
        EntityManager em = BBChemParentStructure.entityManager();
        String fingerprintString = SimpleUtil.bitSetToString(substructure);
        Query q = em.createNativeQuery("SELECT o.* FROM bbchem_parent_structure AS o WHERE (o.substructure \\& CAST(:fingerprintString AS bit(2048))) = CAST(:fingerprintString AS bit(2048))  ", BBChemParentStructure.class);
        q.setParameter("fingerprintString", fingerprintString);
        if(maxResults > -1) {
			q.setMaxResults(maxResults);
		}
        return q.getResultList();
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static BBChemParentStructure fromJsonToBBChemParentStructure(String json) {
        return new JSONDeserializer<BBChemParentStructure>()
        .use(null, BBChemParentStructure.class).deserialize(json);
    }

	public static Collection<BBChemParentStructure> fromJsonArrayToBBChemParentStructures(String json) {
        return new JSONDeserializer<List<BBChemParentStructure>>()
        .use("values", BBChemParentStructure.class).deserialize(json);
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("");

	public static long countBBChemParentStructures() {
        return entityManager().createQuery("SELECT COUNT(o) FROM BBChemParentStructure o", Long.class).getSingleResult();
    }

	public static List<BBChemParentStructure> findAllBBChemParentStructures() {
        return entityManager().createQuery("SELECT o FROM BBChemParentStructure o", BBChemParentStructure.class).getResultList();
    }

	public static List<BBChemParentStructure> findAllBBChemParentStructures(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM BBChemParentStructure o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, BBChemParentStructure.class).getResultList();
    }

	public static BBChemParentStructure findBBChemParentStructure(Long id) {
        if (id == null) return null;
        return entityManager().find(BBChemParentStructure.class, id);
    }

	public static List<BBChemParentStructure> findBBChemParentStructureEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM BBChemParentStructure o", BBChemParentStructure.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<BBChemParentStructure> findBBChemParentStructureEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM BBChemParentStructure o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, BBChemParentStructure.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public BBChemParentStructure merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        BBChemParentStructure merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static Long countFindBBChemParentStructuresByPreRegEquals(String preReg) {
        if (preReg == null || preReg.length() == 0) throw new IllegalArgumentException("The preReg argument is required");
        EntityManager em = BBChemParentStructure.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM BBChemParentStructure AS o WHERE o.preReg = :preReg", Long.class);
        q.setParameter("preReg", preReg);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindBBChemParentStructuresByRegEquals(String reg) {
        if (reg == null || reg.length() == 0) throw new IllegalArgumentException("The reg argument is required");
        EntityManager em = BBChemParentStructure.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM BBChemParentStructure AS o WHERE o.reg = :reg", Long.class);
        q.setParameter("reg", reg);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<BBChemParentStructure> findBBChemParentStructuresByPreRegEquals(String preReg) {
        if (preReg == null || preReg.length() == 0) throw new IllegalArgumentException("The preReg argument is required");
        EntityManager em = BBChemParentStructure.entityManager();
        TypedQuery<BBChemParentStructure> q = em.createQuery("SELECT o FROM BBChemParentStructure AS o WHERE o.preReg = :preReg", BBChemParentStructure.class);
        q.setParameter("preReg", preReg);
        return q;
    }

	public static TypedQuery<BBChemParentStructure> findBBChemParentStructuresByPreRegEquals(String preReg, String sortFieldName, String sortOrder) {
        if (preReg == null || preReg.length() == 0) throw new IllegalArgumentException("The preReg argument is required");
        EntityManager em = BBChemParentStructure.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM BBChemParentStructure AS o WHERE o.preReg = :preReg");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<BBChemParentStructure> q = em.createQuery(queryBuilder.toString(), BBChemParentStructure.class);
        q.setParameter("preReg", preReg);
        return q;
    }

	public static TypedQuery<BBChemParentStructure> findBBChemParentStructuresByRegEquals(String reg) {
        if (reg == null || reg.length() == 0) throw new IllegalArgumentException("The reg argument is required");
        EntityManager em = BBChemParentStructure.entityManager();
        TypedQuery<BBChemParentStructure> q = em.createQuery("SELECT o FROM BBChemParentStructure AS o WHERE o.reg = :reg", BBChemParentStructure.class);
        q.setParameter("reg", reg);
        return q;
    }

	public static TypedQuery<BBChemParentStructure> findBBChemParentStructuresByRegEquals(String reg, String sortFieldName, String sortOrder) {
        if (reg == null || reg.length() == 0) throw new IllegalArgumentException("The reg argument is required");
        EntityManager em = BBChemParentStructure.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM BBChemParentStructure AS o WHERE o.reg = :reg");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<BBChemParentStructure> q = em.createQuery(queryBuilder.toString(), BBChemParentStructure.class);
        q.setParameter("reg", reg);
        return q;
    }
}
