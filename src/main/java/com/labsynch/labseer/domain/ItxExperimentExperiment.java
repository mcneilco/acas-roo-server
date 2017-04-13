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
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
}
