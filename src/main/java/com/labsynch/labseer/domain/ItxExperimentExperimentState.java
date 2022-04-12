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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
@Table(name="ITX_EXPT_EXPT_STATE")
public class ItxExperimentExperimentState extends AbstractState {

    public ItxExperimentExperimentState(ItxExperimentExperimentState itxState) {
		this.setRecordedBy(itxState.getRecordedBy());
		this.setRecordedDate(itxState.getRecordedDate());
		this.setLsTransaction(itxState.getLsTransaction());
		this.setModifiedBy(itxState.getModifiedBy());
		this.setModifiedDate(itxState.getModifiedDate());
		
    }
    
    public static ItxExperimentExperimentState update(ItxExperimentExperimentState object) {
    	ItxExperimentExperimentState updatedObject = new JSONDeserializer<ItxExperimentExperimentState>().use(null, ItxExperimentExperimentState.class).
        		deserializeInto(object.toJson(), 
        				ItxExperimentExperimentState.findItxExperimentExperimentState(object.getId()));
    	updatedObject.setModifiedDate(new Date());
    	updatedObject.merge();
        return updatedObject;
    }

	@ManyToOne
	@JoinColumn(name = "itx_experiment_experiment")
    private ItxExperimentExperiment itxExperimentExperiment;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lsState",  fetch =  FetchType.LAZY)
    private Set<ItxExperimentExperimentValue> lsValues = new HashSet<ItxExperimentExperimentValue>();
    
    
	@Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }

	@Transactional
    public String toJsonStub() {
        return new JSONSerializer().exclude("*.class", "itxExperimentExperiment")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }

    public static ItxExperimentExperimentState fromJsonToItxContainerContainerState(String json) {
        return new JSONDeserializer<ItxExperimentExperimentState>().use(null, ItxExperimentExperimentState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
	@Transactional
    public static String toJsonArray(Collection<ItxExperimentExperimentState> collection) {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }

	@Transactional
    public static String toJsonArrayStub(Collection<ItxExperimentExperimentState> collection) {
        return new JSONSerializer().exclude("*.class", "itxExperimentExperiment")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }
    
    public static Collection<ItxExperimentExperimentState> fromJsonArrayToItxExperimentExperimentStates(String json) {
        return new JSONDeserializer<List<ItxExperimentExperimentState>>().use(null, ArrayList.class)
        		.use("values", ItxExperimentExperimentState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }

    public static Collection<ItxExperimentExperimentState> fromJsonArrayToItxExperimentExperimentStates(Reader json) {
        return new JSONDeserializer<List<ItxExperimentExperimentState>>().use(null, ArrayList.class)
        		.use("values", ItxExperimentExperimentState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }


}
