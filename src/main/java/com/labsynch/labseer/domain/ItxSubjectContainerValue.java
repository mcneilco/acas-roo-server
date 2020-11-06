package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
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

@Configurable
@Entity
@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
public class ItxSubjectContainerValue extends AbstractValue {

    @ManyToOne
    private ItxSubjectContainerState lsState;
    
    public static ItxSubjectContainerValue update(ItxSubjectContainerValue object) {
    	ItxSubjectContainerValue updatedObject = new JSONDeserializer<ItxSubjectContainerValue>().use(null, ItxSubjectContainerValue.class).
        		deserializeInto(object.toJson(), 
        				ItxSubjectContainerValue.findItxSubjectContainerValue(object.getId()));
    	updatedObject.setModifiedDate(new Date());
    	updatedObject.merge();
        return updatedObject;
    }
  
    
    public static ItxSubjectContainerValue fromJsonToItxSubjectContainerValue(String json) {
        return new JSONDeserializer<ItxSubjectContainerValue>().
        		use(null, ItxSubjectContainerValue.class).
        		
        		deserialize(json);
    }
    
    public static Collection<ItxSubjectContainerValue> fromJsonArrayToItxSubjectContainerValues(String json) {
        return new JSONDeserializer<List<ItxSubjectContainerValue>>().
        		use(null, ArrayList.class).
        		use("values", ItxSubjectContainerValue.class).
        		
        		deserialize(json);
    }
    
    public static Collection<ItxSubjectContainerValue> fromJsonArrayToItxSubjectContainerValues(Reader json) {
        return new JSONDeserializer<List<ItxSubjectContainerValue>>().
        		use(null, ArrayList.class).
        		use("values", ItxSubjectContainerValue.class).
        		
        		deserialize(json);
    }
    
    @Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }
    
    @Transactional
    public static String toJsonArray(Collection<ItxSubjectContainerValue> collection) {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStub(Collection<ItxSubjectContainerValue> collection) {
        return new JSONSerializer().exclude("*.class", "lsState")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }

	@Transactional
	public static int deleteByExperimentID(Long experimentId) {
		if (experimentId == null) return 0;
		EntityManager em = TreatmentGroupValue.entityManager();
		String deleteSQL = "DELETE FROM ItxSubjectContainerValue oo WHERE id in (select o.id from ItxSubjectContainerValue o where o.lsState.itxSubjectContainer.subject.treatmentGroup.analysisGroup.experiment.id = :experimentId)";

		Query q = em.createQuery(deleteSQL);
		q.setParameter("experimentId", experimentId);
		int numberOfDeletedEntities = q.executeUpdate();
		return numberOfDeletedEntities;
	}


	public static ItxSubjectContainerValue create(
			ItxSubjectContainerValue itxSubjectContainerValue) {
		ItxSubjectContainerValue newItxSubjectContainerValue = new JSONDeserializer<ItxSubjectContainerValue>().use(null, ItxSubjectContainerValue.class).deserializeInto(itxSubjectContainerValue.toJson(), new ItxSubjectContainerValue());
        return newItxSubjectContainerValue;
	}

	public ItxSubjectContainerState getLsState() {
        return this.lsState;
    }

	public void setLsState(ItxSubjectContainerState lsState) {
        this.lsState = lsState;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("lsState");

	public static long countItxSubjectContainerValues() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ItxSubjectContainerValue o", Long.class).getSingleResult();
    }

	public static List<ItxSubjectContainerValue> findAllItxSubjectContainerValues() {
        return entityManager().createQuery("SELECT o FROM ItxSubjectContainerValue o", ItxSubjectContainerValue.class).getResultList();
    }

	public static List<ItxSubjectContainerValue> findAllItxSubjectContainerValues(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxSubjectContainerValue o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxSubjectContainerValue.class).getResultList();
    }

	public static ItxSubjectContainerValue findItxSubjectContainerValue(Long id) {
        if (id == null) return null;
        return entityManager().find(ItxSubjectContainerValue.class, id);
    }

	public static List<ItxSubjectContainerValue> findItxSubjectContainerValueEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ItxSubjectContainerValue o", ItxSubjectContainerValue.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<ItxSubjectContainerValue> findItxSubjectContainerValueEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxSubjectContainerValue o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxSubjectContainerValue.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public ItxSubjectContainerValue merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ItxSubjectContainerValue merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
