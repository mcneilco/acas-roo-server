package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class ExperimentErrorMessageDTO {

	String experimentCodeName;
	
	String level;
	
	String message;
	
	Experiment experiment;
		
	@Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class", "experiment.lsStates.lsValues.lsState", "experiment.lsStates.experiment", "experiment.analysisGroups.experiment", "experiment.lsLabels.experiment").include("experiment.lsTags", "experiment.lsLabels", "experiment.lsStates.lsValues", "experiment.analysisGroups.lsStates.lsValues", "experiment.analysisGroups.lsLabels", "experiment.analysisGroups.treatmentGroups.lsStates.lsValues", "experiment.analysisGroups.treatmentGroups.lsLabels", "experiment.analysisGroups.treatmentGroups.subjects.lsStates.lsValues", "experiment.analysisGroups.treatmentGroups.subjects.lsLabels").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toPrettyJson() {
        return new JSONSerializer().exclude("*.class", "experiment.lsStates.lsValues.lsState", "experiment.lsStates.experiment", "experiment.analysisGroups.experiments", "experiment.lsLabels.experiment").include("experiment.lsTags", "experiment.lsLabels", "experiment.lsStates.lsValues", "experiment.analysisGroups", "experiment.analysisGroups.lsStates.lsValues", "experiment.analysisGroups.lsLabels", "experiment.analysisGroups.treatmentGroups.lsStates.lsValues", "experiment.analysisGroups.treatmentGroups.lsLabels", "experiment.analysisGroups.treatmentGroups.subjects.lsStates.lsValues", "experiment.analysisGroups.treatmentGroups.subjects.lsLabels").prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toPrettyJsonStub() {
        return new JSONSerializer().exclude("*.class", "experiment.lsStates.lsValues.lsState", "experiment.lsStates.experiment", "experiment.analysisGroups", "experiment.lsLabels.experiment").include("experiment.lsTags", "experiment.lsLabels", "experiment.lsStates.lsValues").prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toJsonStub() {
        return new JSONSerializer().exclude("*.class", "experiment.lsStates.lsValues.lsState", "experiment.lsStates.experiment", "experiment.analysisGroups", "experiment.lsLabels.experiment").include("experiment.lsTags", "experiment.lsLabels", "experiment.lsStates.lsValues").prettyPrint(false).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toJsonStubWithAnalysisGroups() {
        return new JSONSerializer().exclude("*.class", "experiment.lsStates.lsValues.lsState", "experiment.lsStates.experiment", "experiment.lsLabels.experiment", "experiment.analysisGroups.subjects", "experiment.analysisGroups.experiment").include("experiment.lsTags", "experiment.lsLabels", "experiment.lsStates.lsValues", "experiment.analysisGroups").prettyPrint(false).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toJsonStubWithAnalysisGroupValues() {
        return new JSONSerializer().exclude("*.class", "experiment.lsStates.lsValues.lsState", "experiment.lsStates.experiment", "experiment.lsLabels.experiment", "experiment.analysisGroups.subjects", "experiment.analysisGroups.experiment").include("experiment.lsTags", "experiment.lsLabels", "experiment.lsStates.lsValues", "experiment.analysisGroups.lsStates.lsValues").prettyPrint(false).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toJsonStubWithAnalysisGroupStates() {
        return new JSONSerializer().exclude("*.class", "experiment.lsStates.lsValues.lsState", "experiment.lsStates.experiment", "experiment.lsLabels.experiment", "experiment.analysisGroups.subjects", "experiment.analysisGroups.experiment").include("experiment.lsTags", "experiment.lsLabels", "experiment.lsStates.lsValues", "experiment.analysisGroups.lsStates").prettyPrint(false).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public static String toJsonArray(Collection<ExperimentErrorMessageDTO> collection) {
        return new JSONSerializer().include("experiment.lsTags", "experiment.lsLabels", "experiment.lsStates.lsValues", "experiment.analysisGroups.lsStates.lsValues", "experiment.analysisGroups.lsLabels", "experiment.analysisGroups.treatmentGroups.lsStates.lsValues", "experiment.analysisGroups.treatmentGroups.lsLabels", "experiment.analysisGroups.treatmentGroups.subjects.lsStates.lsValues", "experiment.analysisGroups.treatmentGroups.subjects.lsLabels").exclude("*.class", "experiment.lsStates.lsValues.lsState", "experiment.lsStates.experiment", "experiment.analysisGroups.experiment", "experiment.analysisGroups.subject", "experiment.lsLabels.experiment").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArrayPretty(Collection<ExperimentErrorMessageDTO> collection) {
        return new JSONSerializer().include("experiment.lsTags", "experiment.lsLabels", "experiment.lsStates.lsValues", "experiment.analysisGroups.lsStates.lsValues", "experiment.analysisGroups.lsLabels", "experiment.analysisGroups.treatmentGroups.lsStates.lsValues", "experiment.analysisGroups.treatmentGroups.lsLabels", "experiment.analysisGroups.treatmentGroups.subjects.lsStates.lsValues", "experiment.analysisGroups.treatmentGroups.subjects.lsLabels").exclude("*.class", "experiment.lsStates.lsValues.lsState", "experiment.lsStates.experiment", "experiment.analysisGroups.experiment", "experiment.analysisGroups.subject", "experiment.lsLabels.experiment").prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStub(Collection<ExperimentErrorMessageDTO> collection) {
        return new JSONSerializer().include("experiment.lsTags", "experiment.lsLabels", "experiment.lsStates.lsValues").exclude("*.class", "experiment.analysisGroups", "experiment.lsStates.lsValues.lsState", "experiment.lsStates.experiment", "experiment.analysisGroups.experiment", "experiment.lsLabels.experiment").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
    
    @Transactional
        public static String toJsonArrayStubWithProt(Collection<ExperimentErrorMessageDTO> collection) {
            return new JSONSerializer().include("experiment.lsTags", "experiment.lsLabels", "experiment.lsStates.lsValues", "experiment.protocol.lsLabels").exclude("*.class", "experiment.analysisGroups", "experiment.lsStates.lsValues.lsState", "experiment.lsStates.experiment", "experiment.analysisGroups.experiment", "experiment.lsLabels.experiment").transform(new ExcludeNulls(), void.class).serialize(collection);
        }

    @Transactional
    public static String toJsonArrayStubWithAG(Collection<ExperimentErrorMessageDTO> collection) {
        return new JSONSerializer().include("experiment.lsTags", "experiment.lsLabels", "experiment.lsStates.lsValues", "experiment.analysisGroups").exclude("*.class", "experiment.lsStates.lsValues.lsState", "experiment.lsStates.experiment", "experiment.analysisGroups.experiment", "experiment.analysisGroups.subjects", "experiment.lsLabels.experiment").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStubWithAGStates(Collection<ExperimentErrorMessageDTO> collection) {
        return new JSONSerializer().include("experiment.lsTags", "experiment.lsLabels", "experiment.lsStates.lsValues", "experiment.analysisGroups.lsStates").exclude("*.class", "experiment.lsStates.lsValues.lsState", "experiment.lsStates.experiment", "experiment.analysisGroups.experiment", "experiment.analysisGroups.subjects", "experiment.lsLabels.experiment").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStubWithAGValues(Collection<ExperimentErrorMessageDTO> collection) {
        return new JSONSerializer().include("experiment.lsTags", "experiment.lsLabels", "experiment.lsStates.lsValues", "experiment.analysisGroups.lsStates.lsValues").exclude("*.class", "experiment.lsStates.lsValues.lsState", "experiment.lsStates.experiment", "experiment.analysisGroups.experiment", "experiment.analysisGroups.subjects", "experiment.lsLabels.experiment").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStubPretty(Collection<ExperimentErrorMessageDTO> collection) {
        return new JSONSerializer().include("experiment.lsTags", "experiment.lsLabels", "experiment.lsStates.lsValues").exclude("*.class", "experiment.analysisGroups", "experiment.lsStates.lsValues.lsState", "experiment.lsStates.experiment", "experiment.analysisGroups.experiment", "experiment.lsLabels.experiment").prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(collection);
    }
    
	public ExperimentErrorMessageDTO(){
	}
	
	public ExperimentErrorMessageDTO(String experimentCodeName, Experiment experiment){
		this.experimentCodeName = experimentCodeName;
		this.experiment = experiment;
	}
	

	public static ExperimentErrorMessageDTO fromJsonToExperimentErrorMessageDTO(String json) {
        return new JSONDeserializer<ExperimentErrorMessageDTO>()
        .use(null, ExperimentErrorMessageDTO.class).deserialize(json);
    }

	public static Collection<ExperimentErrorMessageDTO> fromJsonArrayToExperimentErroes(String json) {
        return new JSONDeserializer<List<ExperimentErrorMessageDTO>>()
        .use("values", ExperimentErrorMessageDTO.class).deserialize(json);
    }

	public String getExperimentCodeName() {
        return this.experimentCodeName;
    }

	public void setExperimentCodeName(String experimentCodeName) {
        this.experimentCodeName = experimentCodeName;
    }

	public String getLevel() {
        return this.level;
    }

	public void setLevel(String level) {
        this.level = level;
    }

	public String getMessage() {
        return this.message;
    }

	public void setMessage(String message) {
        this.message = message;
    }

	public Experiment getExperiment() {
        return this.experiment;
    }

	public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}