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
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findItxSubjectContainersBySubject", "findItxSubjectContainersByCodeNameEquals", "findItxSubjectContainersByContainer", "findItxSubjectContainersByLsTransactionEquals" })
public class ItxSubjectContainer extends AbstractThing {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "container_id")
    private Container container;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itxSubjectContainer", fetch = FetchType.LAZY)
    private Set<ItxSubjectContainerState> lsStates = new HashSet<ItxSubjectContainerState>();

    public ItxSubjectContainer() {
    }
    
    public ItxSubjectContainer(com.labsynch.labseer.domain.ItxSubjectContainer itxSubjectContainer) {
        super.setRecordedBy(itxSubjectContainer.getRecordedBy());
        super.setRecordedDate(itxSubjectContainer.getRecordedDate());
        super.setLsTransaction(itxSubjectContainer.getLsTransaction());
        super.setModifiedBy(itxSubjectContainer.getModifiedBy());
        super.setModifiedDate(itxSubjectContainer.getModifiedDate());
        super.setCodeName(itxSubjectContainer.getCodeName());
        super.setLsType(itxSubjectContainer.getLsType());
        super.setLsKind(itxSubjectContainer.getLsKind());
        super.setLsTypeAndKind(itxSubjectContainer.getLsTypeAndKind());
        this.subject = itxSubjectContainer.getSubject();
        this.container = itxSubjectContainer.getContainer();
    }
    
    public static ItxSubjectContainer update(ItxSubjectContainer object) {
    	ItxSubjectContainer updatedObject = new JSONDeserializer<ItxSubjectContainer>().use(null, ItxSubjectContainer.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(object.toJson(), 
        				ItxSubjectContainer.findItxSubjectContainer(object.getId()));
    	updatedObject.setModifiedDate(new Date());
    	updatedObject.merge();
        return updatedObject;
    }
  

	public static long countItxSubjectContainers() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ItxSubjectContainer o", Long.class).getSingleResult();
    }

	public static List<ItxSubjectContainer> findAllItxSubjectContainers() {
        return entityManager().createQuery("SELECT o FROM ItxSubjectContainer o", ItxSubjectContainer.class).getResultList();
    }

	public static ItxSubjectContainer findItxSubjectContainer(Long id) {
        if (id == null) return null;
        return entityManager().find(ItxSubjectContainer.class, id);
    }
	
	public static List<ItxSubjectContainer> findItxSubjectContainerByExperimentID(Long experimentId) {
        if (experimentId == null) return null;
		String selectSQL = "SELECT oo FROM ItxSubjectContainer oo WHERE oo.id in (select o.id from ItxSubjectContainer o where o.subject.treatmentGroup.analysisGroup.experiment.id = :experimentId)";
		EntityManager em = ItxSubjectContainer.entityManager();
		TypedQuery<ItxSubjectContainer> q = em.createQuery(selectSQL, ItxSubjectContainer.class);
		q.setParameter("experimentId", experimentId);
		return q.getResultList();
    }

	public static List<ItxSubjectContainer> findItxSubjectContainerEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ItxSubjectContainer o", ItxSubjectContainer.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public ItxSubjectContainer merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ItxSubjectContainer merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
	
	@Transactional
	public static int deleteByExperimentID(Long experimentId) {
		if (experimentId == null) return 0;
		EntityManager em = ItxSubjectContainer.entityManager();
		String deleteSQL = "DELETE FROM ItxSubjectContainer oo WHERE id in (select o.id from ItxSubjectContainer o where o.subject.treatmentGroup.analysisGroup.experiment.id = :experimentId)";
		Query q = em.createQuery(deleteSQL);
		q.setParameter("experimentId", experimentId);
		int numberOfDeletedEntities = q.executeUpdate();
		return numberOfDeletedEntities;
	}
	
    public String toJson() {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
            	.serialize(this);
    }
    
    public static ItxSubjectContainer fromJsonToItxSubjectContainer(String json) {
        return new JSONDeserializer<ItxSubjectContainer>().use(null, ItxSubjectContainer.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
    public static String toJsonArray(Collection<ItxSubjectContainer> collection) {
        return new JSONSerializer().exclude("*.class")            	
        		.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }
    
    
    public static Collection<ItxSubjectContainer> fromJsonArrayToItxSubjectContainers(String json) {
        return new JSONDeserializer<List<ItxSubjectContainer>>().use(null, ArrayList.class).use("values", ItxSubjectContainer.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }

    public static Collection<ItxSubjectContainer> fromJsonArrayToItxSubjectContainers(Reader json) {
        return new JSONDeserializer<List<ItxSubjectContainer>>().use(null, ArrayList.class).use("values", ItxSubjectContainer.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }

}
