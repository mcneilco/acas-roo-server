package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Query;

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
@RooJpaActiveRecord
@RooJson
public class ItxSubjectContainerValue extends AbstractValue {

    @ManyToOne
	@JoinColumn(name = "ls_state")
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
}
