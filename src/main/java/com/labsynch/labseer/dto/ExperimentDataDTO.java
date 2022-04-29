package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.labsynch.labseer.domain.LsTag;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class ExperimentDataDTO {

	private String protocolName;

	private String protocolCodeName;

	private Long protocolId;

	private String assayFolderRule;

	private String protocolShortDescription;

	private String experimentName;

	private String experimentCodeName;

	private Long experimentId;

	private String experimentShortDescription;

	private String displayResults;

	private String renderData;

	private Set<LsTag> lsTags = new HashSet<LsTag>();

	private Set<AnalysisGroupValueDTO> analysisGroupValues = new HashSet<AnalysisGroupValueDTO>();

	public String toJson() {
		return new JSONSerializer().exclude("*.class", "analysisGroupValues.experimentName",
				"analysisGroupValues.experimentCodeName",
				"analysisGroupValues.protocolId",
				"analysisGroupValues.stateId",
				"analysisGroupValues.experimentId",
				"analysisGroupValues.geneId",
				"analysisGroupValues.testedLot").include("lsTags", "analysisGroupValues").serialize(this);
	}

	public static ExperimentDataDTO fromJsonToExperimentDataDTO(String json) {
		return new JSONDeserializer<ExperimentDataDTO>().use(null, ExperimentDataDTO.class).deserialize(json);
	}

	public static String toJsonArray(Collection<ExperimentDataDTO> collection) {
		return new JSONSerializer().exclude("*.class").include("lsTags", "analysisGroupValues")
				.exclude("*.class", "analysisGroupValues.experimentName",
						"analysisGroupValues.experimentCodeName",
						"analysisGroupValues.protocolId",
						"analysisGroupValues.stateId",
						"analysisGroupValues.experimentId",
						"analysisGroupValues.geneId",
						"analysisGroupValues.testedLot")
				.serialize(collection);
	}

	public static Collection<ExperimentDataDTO> fromJsonArrayToExperimentDataDTO(String json) {
		return new JSONDeserializer<List<ExperimentDataDTO>>().use(null, ArrayList.class)
				.use("values", ExperimentDataDTO.class).deserialize(json);
	}

	public String getProtocolName() {
		return this.protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}

	public String getProtocolCodeName() {
		return this.protocolCodeName;
	}

	public void setProtocolCodeName(String protocolCodeName) {
		this.protocolCodeName = protocolCodeName;
	}

	public Long getProtocolId() {
		return this.protocolId;
	}

	public void setProtocolId(Long protocolId) {
		this.protocolId = protocolId;
	}

	public String getAssayFolderRule() {
		return this.assayFolderRule;
	}

	public void setAssayFolderRule(String assayFolderRule) {
		this.assayFolderRule = assayFolderRule;
	}

	public String getProtocolShortDescription() {
		return this.protocolShortDescription;
	}

	public void setProtocolShortDescription(String protocolShortDescription) {
		this.protocolShortDescription = protocolShortDescription;
	}

	public String getExperimentName() {
		return this.experimentName;
	}

	public void setExperimentName(String experimentName) {
		this.experimentName = experimentName;
	}

	public String getExperimentCodeName() {
		return this.experimentCodeName;
	}

	public void setExperimentCodeName(String experimentCodeName) {
		this.experimentCodeName = experimentCodeName;
	}

	public Long getExperimentId() {
		return this.experimentId;
	}

	public void setExperimentId(Long experimentId) {
		this.experimentId = experimentId;
	}

	public String getExperimentShortDescription() {
		return this.experimentShortDescription;
	}

	public void setExperimentShortDescription(String experimentShortDescription) {
		this.experimentShortDescription = experimentShortDescription;
	}

	public String getDisplayResults() {
		return this.displayResults;
	}

	public void setDisplayResults(String displayResults) {
		this.displayResults = displayResults;
	}

	public String getRenderData() {
		return this.renderData;
	}

	public void setRenderData(String renderData) {
		this.renderData = renderData;
	}

	public Set<LsTag> getLsTags() {
		return this.lsTags;
	}

	public void setLsTags(Set<LsTag> lsTags) {
		this.lsTags = lsTags;
	}

	public Set<AnalysisGroupValueDTO> getAnalysisGroupValues() {
		return this.analysisGroupValues;
	}

	public void setAnalysisGroupValues(Set<AnalysisGroupValueDTO> analysisGroupValues) {
		this.analysisGroupValues = analysisGroupValues;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
