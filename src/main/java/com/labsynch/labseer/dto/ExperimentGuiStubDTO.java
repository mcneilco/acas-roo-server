package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.domain.Protocol;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class ExperimentGuiStubDTO {
	
	private static final Logger logger = LoggerFactory.getLogger(ExperimentGuiStubDTO.class);
	
	private Long experimentId;

	private Long protocolId;

	private String protocolName;

	private String code;
	
	private String name;
	
	private String lsType;
	
	private String lsKind;
	
	private String scientist;

	private Date completionDate;

	private String notebook;

	private String notebookPage;

	private String status;

	private String analysisStatus;

	private String shortDescription;

	private String longDescription;


	public ExperimentGuiStubDTO(){
		//empty constructor
	}
	
	public ExperimentGuiStubDTO(Experiment experiment) {
		// TODO Auto-generated constructor stub
		this.experimentId = experiment.getId();
		this.protocolId = experiment.getProtocol().getId();
		this.protocolName = Protocol.findProtocol(protocolId).findPreferredName();
		this.lsType = experiment.getLsType();
		this.lsKind = experiment.getLsKind();
		this.code = experiment.getCodeName();
		this.name = Experiment.findExperiment(experiment.getId()).findPreferredName();
		this.shortDescription = experiment.getShortDescription();

		//get experiment meta data
		Set<ExperimentState> states = experiment.getLsStates();
		for (ExperimentState state : states){
			if (state.getLsType().equals("metadata") && state.getLsKind().equals("experiment metadata") && !state.isIgnored()){
				Set<ExperimentValue> values = state.getLsValues();
				for (ExperimentValue value : values){
					if (value.getLsType().equals("codeValue") && value.getLsKind().equals("scientist") && !value.isIgnored()){
						this.scientist = value.getCodeValue();
					} else if (value.getLsType().equals("dateValue") && value.getLsKind().equals("completion date") && !value.isIgnored()){
						this.completionDate = value.getDateValue();
					} else if (value.getLsType().equals("stringValue") && value.getLsKind().equals("status") && !value.isIgnored()){
						this.status = value.getStringValue();
					} else if (value.getLsType().equals("stringValue") && value.getLsKind().equals("analysis status") && !value.isIgnored()){
						this.analysisStatus = value.getStringValue();
					} else if (value.getLsType().equals("stringValue") && value.getLsKind().equals("notebook") && !value.isIgnored()){
						this.notebook = value.getStringValue();
					} else if (value.getLsType().equals("stringValue") && value.getLsKind().equals("notebook page") && !value.isIgnored()){
						this.notebookPage = value.getStringValue();
					} else if (value.getLsType().equals("clobValue") && value.getLsKind().equals("long description") && !value.isIgnored()){
						this.longDescription = value.getClobValue();
					}				
				}
			}
		}
	}




	public Long getExperimentId() {
        return this.experimentId;
    }

	public void setExperimentId(Long experimentId) {
        this.experimentId = experimentId;
    }

	public Long getProtocolId() {
        return this.protocolId;
    }

	public void setProtocolId(Long protocolId) {
        this.protocolId = protocolId;
    }

	public String getProtocolName() {
        return this.protocolName;
    }

	public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

	public String getCode() {
        return this.code;
    }

	public void setCode(String code) {
        this.code = code;
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public String getLsType() {
        return this.lsType;
    }

	public void setLsType(String lsType) {
        this.lsType = lsType;
    }

	public String getLsKind() {
        return this.lsKind;
    }

	public void setLsKind(String lsKind) {
        this.lsKind = lsKind;
    }

	public String getScientist() {
        return this.scientist;
    }

	public void setScientist(String scientist) {
        this.scientist = scientist;
    }

	public Date getCompletionDate() {
        return this.completionDate;
    }

	public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

	public String getNotebook() {
        return this.notebook;
    }

	public void setNotebook(String notebook) {
        this.notebook = notebook;
    }

	public String getNotebookPage() {
        return this.notebookPage;
    }

	public void setNotebookPage(String notebookPage) {
        this.notebookPage = notebookPage;
    }

	public String getStatus() {
        return this.status;
    }

	public void setStatus(String status) {
        this.status = status;
    }

	public String getAnalysisStatus() {
        return this.analysisStatus;
    }

	public void setAnalysisStatus(String analysisStatus) {
        this.analysisStatus = analysisStatus;
    }

	public String getShortDescription() {
        return this.shortDescription;
    }

	public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

	public String getLongDescription() {
        return this.longDescription;
    }

	public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static ExperimentGuiStubDTO fromJsonToExperimentGuiStubDTO(String json) {
        return new JSONDeserializer<ExperimentGuiStubDTO>()
        .use(null, ExperimentGuiStubDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ExperimentGuiStubDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ExperimentGuiStubDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ExperimentGuiStubDTO> fromJsonArrayToExperimentGuiStubDTO(String json) {
        return new JSONDeserializer<List<ExperimentGuiStubDTO>>()
        .use("values", ExperimentGuiStubDTO.class).deserialize(json);
    }
}


