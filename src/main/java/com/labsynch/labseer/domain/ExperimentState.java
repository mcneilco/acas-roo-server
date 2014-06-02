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

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
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
@RooJpaActiveRecord(finders = { "findExperimentStatesByExperiment" })
@RooJson
public class ExperimentState extends AbstractState {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "experiment_id")
    private Experiment experiment;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lsState",  fetch =  FetchType.LAZY)
    private Set<ExperimentValue> lsValues = new HashSet<ExperimentValue>();

    public ExperimentState() {
    }
    
//constructor to instantiate a new experimentState from nested json objects
public ExperimentState(ExperimentState experimentState) {
	
	super.setRecordedBy(experimentState.getRecordedBy());
	super.setRecordedDate(experimentState.getRecordedDate());
	super.setLsTransaction(experimentState.getLsTransaction());
	super.setModifiedBy(experimentState.getModifiedBy());
	super.setModifiedDate(experimentState.getModifiedDate());
	super.setLsType(experimentState.getLsType());
	super.setLsKind(experimentState.getLsKind());

}

public static ExperimentState update(ExperimentState experimentState) {
	ExperimentState updatedExperimentState = ExperimentState.findExperimentState(experimentState.getId());
	updatedExperimentState.setRecordedBy(experimentState.getRecordedBy());
	updatedExperimentState.setRecordedDate(experimentState.getRecordedDate());
	updatedExperimentState.setLsTransaction(experimentState.getLsTransaction());
	updatedExperimentState.setModifiedBy(experimentState.getModifiedBy());
	updatedExperimentState.setModifiedDate(new Date());
	updatedExperimentState.setLsType(experimentState.getLsType());
	updatedExperimentState.setLsKind(experimentState.getLsKind());	
	updatedExperimentState.setIgnored(experimentState.isIgnored());
	updatedExperimentState.merge();
	return updatedExperimentState;
}

public String toJson() {
    return new JSONSerializer().exclude("*.class")
			.transform(new ExcludeNulls(), void.class)
    		.serialize(this);
}

public static ExperimentState fromJsonToExperimentState(String json) {
    return new JSONDeserializer<ExperimentState>().use(null, ExperimentState.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
}

public static String toJsonArray(Collection<ExperimentState> collection) {
    return new JSONSerializer().exclude("*.class")
			.transform(new ExcludeNulls(), void.class)
    		.serialize(collection);
}


public static Collection<ExperimentState> fromJsonArrayToExperimentStates(String json) {
    return new JSONDeserializer<List<ExperimentState>>().use(null, ArrayList.class).use("values", ExperimentState.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
}

public static Collection<ExperimentState> fromJsonArrayToExperimentStates(Reader json) {
    return new JSONDeserializer<List<ExperimentState>>().use(null, ArrayList.class).use("values", ExperimentState.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
}


	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
