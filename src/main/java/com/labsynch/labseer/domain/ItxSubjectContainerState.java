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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
public class ItxSubjectContainerState extends AbstractState {

    public ItxSubjectContainerState() {
    }
    
    public ItxSubjectContainerState(ItxSubjectContainerState itxState) {
		super.setRecordedBy(itxState.getRecordedBy());
		super.setRecordedDate(itxState.getRecordedDate());
		super.setLsTransaction(itxState.getLsTransaction());
		super.setModifiedBy(itxState.getModifiedBy());
		super.setModifiedDate(itxState.getModifiedDate());
//		this.itxSubjectContainer = itxState.getItxSubjectContainer();
//		this.itxSubjectContainerValues = itxState.getItxSubjectContainerValues();		
	}

    public static ItxSubjectContainerState update(ItxSubjectContainerState object) {
    	ItxSubjectContainerState updatedObject = new JSONDeserializer<ItxSubjectContainerState>().use(null, ItxSubjectContainerState.class).
        		deserializeInto(object.toJson(), 
        				ItxSubjectContainerState.findItxSubjectContainerState(object.getId()));
    	updatedObject.setModifiedDate(new Date());
    	updatedObject.merge();
        return updatedObject;
    }
  
	@ManyToOne
    private ItxSubjectContainer itxSubjectContainer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lsState")
    private Set<ItxSubjectContainerValue> lsValues = new HashSet<ItxSubjectContainerValue>();
    
	@Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }
    
    public static ItxSubjectContainerState fromJsonToItxSubjectContainerState(String json) {
        return new JSONDeserializer<ItxSubjectContainerState>().use(null, ItxSubjectContainerState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
	@Transactional
    public static String toJsonArray(Collection<ItxSubjectContainerState> collection) {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }

	@Transactional
    public static String toJsonArrayStub(Collection<ItxSubjectContainerState> collection) {
        return new JSONSerializer().exclude("*.class", "itxSubjectContainer")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }

	
    public static Collection<ItxSubjectContainerState> fromJsonArrayToItxSubjectContainerStates(String json) {
        return new JSONDeserializer<List<ItxSubjectContainerState>>()
        		.use(null, ArrayList.class)
        		.use("values", ItxSubjectContainerState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
    public static Collection<ItxSubjectContainerState> fromJsonArrayToItxSubjectContainerStates(Reader json) {
        return new JSONDeserializer<List<ItxSubjectContainerState>>()
        		.use(null, ArrayList.class)
        		.use("values", ItxSubjectContainerState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }

	@Transactional
	public static int deleteByExperimentID(Long experimentId) {
		if (experimentId == null) return 0;
		EntityManager em = TreatmentGroupValue.entityManager();
		String deleteSQL = "DELETE FROM ItxSubjectContainerState oo WHERE id in (select o.id from ItxSubjectContainerState o where o.itxSubjectContainer.subject.treatmentGroup.analysisGroup.experiment.id = :experimentId)";

		Query q = em.createQuery(deleteSQL);
		q.setParameter("experimentId", experimentId);
		int numberOfDeletedEntities = q.executeUpdate();
		return numberOfDeletedEntities;
	}
}
