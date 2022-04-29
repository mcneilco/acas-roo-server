package com.labsynch.labseer.domain;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity

@Transactional
public class TreatmentGroupLabel extends AbstractLabel {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "treatment_group_id")
    private TreatmentGroup treatmentGroup;

    public TreatmentGroupLabel() {
    }
    
    public TreatmentGroupLabel(com.labsynch.labseer.domain.TreatmentGroupLabel treatmentGroupLabel) {
        super.setLsType(treatmentGroupLabel.getLsType());
        super.setLsKind(treatmentGroupLabel.getLsKind());
        super.setLsTypeAndKind(treatmentGroupLabel.getLsType() + "_" + treatmentGroupLabel.getLsKind());
        super.setLabelText(treatmentGroupLabel.getLabelText());
        super.setPreferred(treatmentGroupLabel.isPreferred());
        super.setLsTransaction(treatmentGroupLabel.getLsTransaction());
        super.setRecordedBy(treatmentGroupLabel.getRecordedBy());
        super.setRecordedDate(treatmentGroupLabel.getRecordedDate());
        super.setPhysicallyLabled(treatmentGroupLabel.isPhysicallyLabled());
    }
    
    public static TreatmentGroupLabel update(TreatmentGroupLabel treatmentGroupLabel) {
    	TreatmentGroupLabel updatedTreatmentGroupLabel = TreatmentGroupLabel.findTreatmentGroupLabel(treatmentGroupLabel.getId());
    	updatedTreatmentGroupLabel.setLsType(treatmentGroupLabel.getLsType());
    	updatedTreatmentGroupLabel.setLsKind(treatmentGroupLabel.getLsKind());
    	updatedTreatmentGroupLabel.setLsTypeAndKind(treatmentGroupLabel.getLsType() + "_" + treatmentGroupLabel.getLsKind());
    	updatedTreatmentGroupLabel.setLabelText(treatmentGroupLabel.getLabelText());
    	updatedTreatmentGroupLabel.setPreferred(treatmentGroupLabel.isPreferred());
    	updatedTreatmentGroupLabel.setLsTransaction(treatmentGroupLabel.getLsTransaction());
    	updatedTreatmentGroupLabel.setRecordedBy(treatmentGroupLabel.getRecordedBy());
    	updatedTreatmentGroupLabel.setRecordedDate(treatmentGroupLabel.getRecordedDate());
    	updatedTreatmentGroupLabel.setModifiedDate(new Date());
    	updatedTreatmentGroupLabel.setPhysicallyLabled(treatmentGroupLabel.isPhysicallyLabled());
    	updatedTreatmentGroupLabel.merge();
    	return updatedTreatmentGroupLabel;
    }
    
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public static TreatmentGroupLabel fromJsonToTreatmentGroupLabel(String json) {
        return new JSONDeserializer<TreatmentGroupLabel>().use(null, TreatmentGroupLabel.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<TreatmentGroupLabel> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static Collection<TreatmentGroupLabel> fromJsonArrayToTreatmentGroupLabels(String json) {
        return new JSONDeserializer<List<TreatmentGroupLabel>>().use(null, ArrayList.class).use("values", TreatmentGroupLabel.class).deserialize(json);
    }

    public static Collection<TreatmentGroupLabel> fromJsonArrayToTreatmentGroupLabels(Reader json) {
        return new JSONDeserializer<List<TreatmentGroupLabel>>().use(null, ArrayList.class).use("values", TreatmentGroupLabel.class).deserialize(json);
    }

	public static int deleteByExperimentID(Long experimentId) {
		if (experimentId == null) return 0;
		EntityManager em = SubjectValue.entityManager();
		String deleteSQL = "DELETE FROM TreatmentGroupLabel oo WHERE id in (select o.id from TreatmentGroupLabel o where o.treatmentGroup.analysisGroup.experiment.id = :experimentId)";

		Query q = em.createQuery(deleteSQL);
		q.setParameter("experimentId", experimentId);
		int numberOfDeletedEntities = q.executeUpdate();
		return numberOfDeletedEntities;
	}

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static Long countFindTreatmentGroupLabelsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = TreatmentGroupLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM TreatmentGroupLabel AS o WHERE o.lsTransaction = :lsTransaction", Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindTreatmentGroupLabelsByTreatmentGroup(TreatmentGroup treatmentGroup) {
        if (treatmentGroup == null) throw new IllegalArgumentException("The treatmentGroup argument is required");
        EntityManager em = TreatmentGroupLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM TreatmentGroupLabel AS o WHERE o.treatmentGroup = :treatmentGroup", Long.class);
        q.setParameter("treatmentGroup", treatmentGroup);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<TreatmentGroupLabel> findTreatmentGroupLabelsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = TreatmentGroupLabel.entityManager();
        TypedQuery<TreatmentGroupLabel> q = em.createQuery("SELECT o FROM TreatmentGroupLabel AS o WHERE o.lsTransaction = :lsTransaction", TreatmentGroupLabel.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public static TypedQuery<TreatmentGroupLabel> findTreatmentGroupLabelsByLsTransactionEquals(Long lsTransaction, String sortFieldName, String sortOrder) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = TreatmentGroupLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM TreatmentGroupLabel AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<TreatmentGroupLabel> q = em.createQuery(queryBuilder.toString(), TreatmentGroupLabel.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public static TypedQuery<TreatmentGroupLabel> findTreatmentGroupLabelsByTreatmentGroup(TreatmentGroup treatmentGroup) {
        if (treatmentGroup == null) throw new IllegalArgumentException("The treatmentGroup argument is required");
        EntityManager em = TreatmentGroupLabel.entityManager();
        TypedQuery<TreatmentGroupLabel> q = em.createQuery("SELECT o FROM TreatmentGroupLabel AS o WHERE o.treatmentGroup = :treatmentGroup", TreatmentGroupLabel.class);
        q.setParameter("treatmentGroup", treatmentGroup);
        return q;
    }

	public static TypedQuery<TreatmentGroupLabel> findTreatmentGroupLabelsByTreatmentGroup(TreatmentGroup treatmentGroup, String sortFieldName, String sortOrder) {
        if (treatmentGroup == null) throw new IllegalArgumentException("The treatmentGroup argument is required");
        EntityManager em = TreatmentGroupLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM TreatmentGroupLabel AS o WHERE o.treatmentGroup = :treatmentGroup");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<TreatmentGroupLabel> q = em.createQuery(queryBuilder.toString(), TreatmentGroupLabel.class);
        q.setParameter("treatmentGroup", treatmentGroup);
        return q;
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("treatmentGroup");

	public static long countTreatmentGroupLabels() {
        return entityManager().createQuery("SELECT COUNT(o) FROM TreatmentGroupLabel o", Long.class).getSingleResult();
    }

	public static List<TreatmentGroupLabel> findAllTreatmentGroupLabels() {
        return entityManager().createQuery("SELECT o FROM TreatmentGroupLabel o", TreatmentGroupLabel.class).getResultList();
    }

	public static List<TreatmentGroupLabel> findAllTreatmentGroupLabels(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM TreatmentGroupLabel o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, TreatmentGroupLabel.class).getResultList();
    }

	public static TreatmentGroupLabel findTreatmentGroupLabel(Long id) {
        if (id == null) return null;
        return entityManager().find(TreatmentGroupLabel.class, id);
    }

	public static List<TreatmentGroupLabel> findTreatmentGroupLabelEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM TreatmentGroupLabel o", TreatmentGroupLabel.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<TreatmentGroupLabel> findTreatmentGroupLabelEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM TreatmentGroupLabel o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, TreatmentGroupLabel.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public TreatmentGroupLabel merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        TreatmentGroupLabel merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public TreatmentGroup getTreatmentGroup() {
        return this.treatmentGroup;
    }

	public void setTreatmentGroup(TreatmentGroup treatmentGroup) {
        this.treatmentGroup = treatmentGroup;
    }
}
