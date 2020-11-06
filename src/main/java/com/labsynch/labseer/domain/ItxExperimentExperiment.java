package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;
import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable
@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findItxExperimentExperimentsByLsTransactionEquals", "findItxExperimentExperimentsByFirstExperiment" , "findItxExperimentExperimentsBySecondExperiment"})
@Table(name="ITX_EXPT_EXPT")
public class ItxExperimentExperiment extends AbstractThing {

	private static final Logger logger = LoggerFactory.getLogger(ItxExperimentExperiment.class);

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "first_experiment_id")
    private Experiment firstExperiment;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "second_experiment_id")
    private Experiment secondExperiment;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itxExperimentExperiment", fetch = FetchType.LAZY)
    private Set<ItxExperimentExperimentState> lsStates = new HashSet<ItxExperimentExperimentState>();

    public ItxExperimentExperiment(com.labsynch.labseer.domain.ItxExperimentExperiment itxExperiment) {
    	this.setRecordedBy(itxExperiment.getRecordedBy());
    	this.setRecordedDate(itxExperiment.getRecordedDate());
    	this.setLsTransaction(itxExperiment.getLsTransaction());
    	this.setModifiedBy(itxExperiment.getModifiedBy());
    	this.setModifiedDate(itxExperiment.getModifiedDate());
    	this.setCodeName(itxExperiment.getCodeName());
    	this.setLsType(itxExperiment.getLsType());
    	this.setLsKind(itxExperiment.getLsKind());
    	this.setLsTypeAndKind(itxExperiment.getLsTypeAndKind());
        this.firstExperiment = itxExperiment.getFirstExperiment();
        this.secondExperiment = itxExperiment.getSecondExperiment();
    }
    
    public static ItxExperimentExperiment update(ItxExperimentExperiment object) {
    	ItxExperimentExperiment updatedObject = new JSONDeserializer<ItxExperimentExperiment>().use(null, ItxExperimentExperiment.class).
        		deserializeInto(object.toJson(), 
        				ItxExperimentExperiment.findItxExperimentExperiment(object.getId()));
    	updatedObject.setModifiedDate(new Date());
    	updatedObject.merge();
        return updatedObject;
    }
    
    public String toJson() {
        return new JSONSerializer()
        		.exclude("*.class")
        		.include("firstExperiment.lsLabels", "secondExperiment.lsLabels")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }
    
    public static ItxExperimentExperiment fromJsonToItxExperimentExperiment(String json) {
        return new JSONDeserializer<ItxExperimentExperiment>()
        		.use(null, ItxExperimentExperiment.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
    public static String toJsonArray(Collection<ItxExperimentExperiment> collection) {
        return new JSONSerializer().exclude("*.class")
        		.include("firstExperiment.lsLabels", "secondExperiment.lsLabels")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }
    
    public static Collection<ItxExperimentExperiment> fromJsonArrayToItxExperimentExperiments(String json) {
        return new JSONDeserializer<List<ItxExperimentExperiment>>().use(null, ArrayList.class)
        		.use("values", ItxExperimentExperiment.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
    public static Collection<ItxExperimentExperiment> fromJsonArrayToItxExperimentExperiments(Reader json) {
        return new JSONDeserializer<List<ItxExperimentExperiment>>().use(null, ArrayList.class)
        		.use("values", ItxExperimentExperiment.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
    public static ItxExperimentExperiment updateNoStates(ItxExperimentExperiment itxExperimentExperiment) {
    	ItxExperimentExperiment updatedItxExperimentExperiment = ItxExperimentExperiment.findItxExperimentExperiment(itxExperimentExperiment.getId());
    	updatedItxExperimentExperiment.setRecordedBy(itxExperimentExperiment.getRecordedBy());
    	updatedItxExperimentExperiment.setRecordedDate(itxExperimentExperiment.getRecordedDate());
    	updatedItxExperimentExperiment.setIgnored(itxExperimentExperiment.isIgnored());
    	updatedItxExperimentExperiment.setDeleted(itxExperimentExperiment.isDeleted());
    	updatedItxExperimentExperiment.setLsTransaction(itxExperimentExperiment.getLsTransaction());
    	updatedItxExperimentExperiment.setModifiedBy(itxExperimentExperiment.getModifiedBy());
    	updatedItxExperimentExperiment.setCodeName(itxExperimentExperiment.getCodeName());
    	updatedItxExperimentExperiment.setLsType(itxExperimentExperiment.getLsType());
    	updatedItxExperimentExperiment.setLsKind(itxExperimentExperiment.getLsKind());
    	updatedItxExperimentExperiment.setLsTypeAndKind(itxExperimentExperiment.getLsTypeAndKind());
    	updatedItxExperimentExperiment.firstExperiment = Experiment.findExperiment(itxExperimentExperiment.getFirstExperiment().getId());
    	updatedItxExperimentExperiment.secondExperiment = Experiment.findExperiment(itxExperimentExperiment.getSecondExperiment().getId());    	
    	updatedItxExperimentExperiment.setModifiedDate(new Date());
    	updatedItxExperimentExperiment.merge();
    	
    	logger.debug("------------ Just updated the itxExperimentExperiment: ");
    	if(logger.isDebugEnabled()) logger.debug(updatedItxExperimentExperiment.toJson());
    	
        return updatedItxExperimentExperiment;
    }

	public ItxExperimentExperiment() {
        super();
    }

	public static Long countFindItxExperimentExperimentsByFirstExperiment(Experiment firstExperiment) {
        if (firstExperiment == null) throw new IllegalArgumentException("The firstExperiment argument is required");
        EntityManager em = ItxExperimentExperiment.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ItxExperimentExperiment AS o WHERE o.firstExperiment = :firstExperiment", Long.class);
        q.setParameter("firstExperiment", firstExperiment);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindItxExperimentExperimentsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ItxExperimentExperiment.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ItxExperimentExperiment AS o WHERE o.lsTransaction = :lsTransaction", Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindItxExperimentExperimentsBySecondExperiment(Experiment secondExperiment) {
        if (secondExperiment == null) throw new IllegalArgumentException("The secondExperiment argument is required");
        EntityManager em = ItxExperimentExperiment.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ItxExperimentExperiment AS o WHERE o.secondExperiment = :secondExperiment", Long.class);
        q.setParameter("secondExperiment", secondExperiment);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<ItxExperimentExperiment> findItxExperimentExperimentsByFirstExperiment(Experiment firstExperiment) {
        if (firstExperiment == null) throw new IllegalArgumentException("The firstExperiment argument is required");
        EntityManager em = ItxExperimentExperiment.entityManager();
        TypedQuery<ItxExperimentExperiment> q = em.createQuery("SELECT o FROM ItxExperimentExperiment AS o WHERE o.firstExperiment = :firstExperiment", ItxExperimentExperiment.class);
        q.setParameter("firstExperiment", firstExperiment);
        return q;
    }

	public static TypedQuery<ItxExperimentExperiment> findItxExperimentExperimentsByFirstExperiment(Experiment firstExperiment, String sortFieldName, String sortOrder) {
        if (firstExperiment == null) throw new IllegalArgumentException("The firstExperiment argument is required");
        EntityManager em = ItxExperimentExperiment.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ItxExperimentExperiment AS o WHERE o.firstExperiment = :firstExperiment");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ItxExperimentExperiment> q = em.createQuery(queryBuilder.toString(), ItxExperimentExperiment.class);
        q.setParameter("firstExperiment", firstExperiment);
        return q;
    }

	public static TypedQuery<ItxExperimentExperiment> findItxExperimentExperimentsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ItxExperimentExperiment.entityManager();
        TypedQuery<ItxExperimentExperiment> q = em.createQuery("SELECT o FROM ItxExperimentExperiment AS o WHERE o.lsTransaction = :lsTransaction", ItxExperimentExperiment.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public static TypedQuery<ItxExperimentExperiment> findItxExperimentExperimentsByLsTransactionEquals(Long lsTransaction, String sortFieldName, String sortOrder) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ItxExperimentExperiment.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ItxExperimentExperiment AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ItxExperimentExperiment> q = em.createQuery(queryBuilder.toString(), ItxExperimentExperiment.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public static TypedQuery<ItxExperimentExperiment> findItxExperimentExperimentsBySecondExperiment(Experiment secondExperiment) {
        if (secondExperiment == null) throw new IllegalArgumentException("The secondExperiment argument is required");
        EntityManager em = ItxExperimentExperiment.entityManager();
        TypedQuery<ItxExperimentExperiment> q = em.createQuery("SELECT o FROM ItxExperimentExperiment AS o WHERE o.secondExperiment = :secondExperiment", ItxExperimentExperiment.class);
        q.setParameter("secondExperiment", secondExperiment);
        return q;
    }

	public static TypedQuery<ItxExperimentExperiment> findItxExperimentExperimentsBySecondExperiment(Experiment secondExperiment, String sortFieldName, String sortOrder) {
        if (secondExperiment == null) throw new IllegalArgumentException("The secondExperiment argument is required");
        EntityManager em = ItxExperimentExperiment.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ItxExperimentExperiment AS o WHERE o.secondExperiment = :secondExperiment");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ItxExperimentExperiment> q = em.createQuery(queryBuilder.toString(), ItxExperimentExperiment.class);
        q.setParameter("secondExperiment", secondExperiment);
        return q;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public Experiment getFirstExperiment() {
        return this.firstExperiment;
    }

	public void setFirstExperiment(Experiment firstExperiment) {
        this.firstExperiment = firstExperiment;
    }

	public Experiment getSecondExperiment() {
        return this.secondExperiment;
    }

	public void setSecondExperiment(Experiment secondExperiment) {
        this.secondExperiment = secondExperiment;
    }

	public Set<ItxExperimentExperimentState> getLsStates() {
        return this.lsStates;
    }

	public void setLsStates(Set<ItxExperimentExperimentState> lsStates) {
        this.lsStates = lsStates;
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "firstExperiment", "secondExperiment", "lsStates");

	public static long countItxExperimentExperiments() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ItxExperimentExperiment o", Long.class).getSingleResult();
    }

	public static List<ItxExperimentExperiment> findAllItxExperimentExperiments() {
        return entityManager().createQuery("SELECT o FROM ItxExperimentExperiment o", ItxExperimentExperiment.class).getResultList();
    }

	public static List<ItxExperimentExperiment> findAllItxExperimentExperiments(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxExperimentExperiment o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxExperimentExperiment.class).getResultList();
    }

	public static ItxExperimentExperiment findItxExperimentExperiment(Long id) {
        if (id == null) return null;
        return entityManager().find(ItxExperimentExperiment.class, id);
    }

	public static List<ItxExperimentExperiment> findItxExperimentExperimentEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ItxExperimentExperiment o", ItxExperimentExperiment.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<ItxExperimentExperiment> findItxExperimentExperimentEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxExperimentExperiment o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxExperimentExperiment.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public ItxExperimentExperiment merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ItxExperimentExperiment merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
