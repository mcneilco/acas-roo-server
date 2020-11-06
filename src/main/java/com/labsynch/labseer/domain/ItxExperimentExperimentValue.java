package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
@Table(name="ITX_EXPT_EXPT_VALUE")
public class ItxExperimentExperimentValue extends AbstractValue {
		
    @ManyToOne
    private ItxExperimentExperimentState lsState;
    
    public static ItxExperimentExperimentValue update(ItxExperimentExperimentValue object) {
    	ItxExperimentExperimentValue updatedObject = new JSONDeserializer<ItxExperimentExperimentValue>().use(null, ItxExperimentExperimentValue.class).
        		deserializeInto(object.toJson(), 
        				ItxExperimentExperimentValue.findItxExperimentExperimentValue(object.getId()));
    	updatedObject.setModifiedDate(new Date());
    	updatedObject.merge();
        return updatedObject;
    }
    
    
    public static ItxExperimentExperimentValue fromJsonToItxExperimentExperimentValue(String json) {
        return new JSONDeserializer<ItxExperimentExperimentValue>().
        		use(null, ItxExperimentExperimentValue.class).
        		
        		deserialize(json);
    }
    
    public static Collection<ItxExperimentExperimentValue> fromJsonArrayToItxExperimentExperimentValues(String json) {
        return new JSONDeserializer<List<ItxExperimentExperimentValue>>().
        		use(null, ArrayList.class).
        		use("values", ItxExperimentExperimentValue.class).
        		
        		deserialize(json);
    }
    
    public static Collection<ItxExperimentExperimentValue> fromJsonArrayToItxExperimentExperimentValues(Reader json) {
        return new JSONDeserializer<List<ItxExperimentExperimentValue>>().
        		use(null, ArrayList.class).
        		use("values", ItxExperimentExperimentValue.class).
        		
        		deserialize(json);
    }
    
	@Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }
    
	@Transactional
    public static String toJsonArray(Collection<ItxExperimentExperimentValue> collection) {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }

	@Transactional
    public static String toJsonArrayStub(Collection<ItxExperimentExperimentValue> collection) {
        return new JSONSerializer().exclude("*.class", "lsState")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }
	
	public static ItxExperimentExperimentValue create(ItxExperimentExperimentValue lsThingValue) {
        ItxExperimentExperimentValue newItxExperimentExperimentValue = new JSONDeserializer<ItxExperimentExperimentValue>().use(null, ItxExperimentExperimentValue.class).deserializeInto(lsThingValue.toJson(), new ItxExperimentExperimentValue());
        return newItxExperimentExperimentValue;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public ItxExperimentExperimentState getLsState() {
        return this.lsState;
    }

	public void setLsState(ItxExperimentExperimentState lsState) {
        this.lsState = lsState;
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("lsState");

	public static long countItxExperimentExperimentValues() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ItxExperimentExperimentValue o", Long.class).getSingleResult();
    }

	public static List<ItxExperimentExperimentValue> findAllItxExperimentExperimentValues() {
        return entityManager().createQuery("SELECT o FROM ItxExperimentExperimentValue o", ItxExperimentExperimentValue.class).getResultList();
    }

	public static List<ItxExperimentExperimentValue> findAllItxExperimentExperimentValues(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxExperimentExperimentValue o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxExperimentExperimentValue.class).getResultList();
    }

	public static ItxExperimentExperimentValue findItxExperimentExperimentValue(Long id) {
        if (id == null) return null;
        return entityManager().find(ItxExperimentExperimentValue.class, id);
    }

	public static List<ItxExperimentExperimentValue> findItxExperimentExperimentValueEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ItxExperimentExperimentValue o", ItxExperimentExperimentValue.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<ItxExperimentExperimentValue> findItxExperimentExperimentValueEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxExperimentExperimentValue o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxExperimentExperimentValue.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public ItxExperimentExperimentValue merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ItxExperimentExperimentValue merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
