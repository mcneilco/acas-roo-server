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
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findItxExperimentExperimentsByLsTransactionEquals", "findItxExperimentExperimentsByFirstExperiment" , "findItxExperimentExperimentsBySecondExperiment"})
public class ItxExperimentExperiment extends AbstractThing {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "first_experiment_id")
    private Experiment firstExperiment;

    @NotNull
    @ManyToOne
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
        		use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(object.toJson(), 
        				ItxExperimentExperiment.findItxExperimentExperiment(object.getId()));
    	updatedObject.setModifiedDate(new Date());
    	updatedObject.merge();
        return updatedObject;
    }
    
    public String toJson() {
        return new JSONSerializer().exclude("*.class")
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
}
