package com.labsynch.labseer.dto;

import java.util.Collection;

import flexjson.JSONSerializer;

public class ParentLotAssayDTO {
    private String lotCorpName;
    private String lotProject;
    private String protocolCodeName;
    private String protocolName;
    private String experimentCodeName;
    private String experimentName;
    private String experimentProject;
    private String analysisGroupType;
    private String analysisGroupKind;
    private String analysisGroupUnit;
    private String analysisGroupOperator;
    private float analysisGroupNumericValue;
    private String analysisGroupStringValue;
    private boolean analysisGroupPublicData;


    public String getLotCorpName() {
        return this.lotCorpName;
    }

    public void setLotCorpName(String lotCorpName) {
        this.lotCorpName = lotCorpName;
    }

    public String getLotProject() {
        return this.lotProject;
    }

    public void setLotProject(String lotProject) {
        this.lotProject = lotProject;
    }

    public String getProtocolCodeName() {
        return this.protocolCodeName;
    }

    public void setProtocolCodeName(String protocolCodeName) {
        this.protocolCodeName = protocolCodeName;
    }

    public String getProtocolName() {
        return this.protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public String getExperimentCodeName() {
        return this.experimentCodeName;
    }

    public void setExperimentCodeName(String experimentCodeName) {
        this.experimentCodeName = experimentCodeName;
    }

    public String getExperimentName() {
        return this.experimentName;
    }

    public void setExperimentName(String experimentName) {
        this.experimentName = experimentName;
    }

    public String getExperimentProject() {
        return this.experimentProject;
    }

    public void setExperimentProject(String experimentProject) {
        this.experimentProject = experimentProject;
    }

    public String getAnalysisGroupType() {
        return this.analysisGroupType;
    }

    public void setAnalysisGroupType(String analysisGroupType) {
        this.analysisGroupType = analysisGroupType;
    }

    public String getAnalysisGroupKind() {
        return this.analysisGroupKind;
    }

    public void setAnalysisGroupKind(String analysisGroupKind) {
        this.analysisGroupKind = analysisGroupKind;
    }

    public String getAnalysisGroupUnit() {
        return this.analysisGroupUnit;
    }

    public void setAnalysisGroupUnit(String analysisGroupUnit) {
        this.analysisGroupUnit = analysisGroupUnit;
    }

    public String getAnalysisGroupOperator() {
        return this.analysisGroupOperator;
    }

    public void setAnalysisGroupOperator(String analysisGroupOperator) {
        this.analysisGroupOperator = analysisGroupOperator;
    }

    public float getAnalysisGroupNumericValue() {
        return this.analysisGroupNumericValue;
    }

    public void setAnalysisGroupNumericValue(float analysisGroupNumericValue) {
        this.analysisGroupNumericValue = analysisGroupNumericValue;
    }

    public String getAnalysisGroupStringValue() {
        return this.analysisGroupStringValue;
    }

    public void setAnalysisGroupStringValue(String analysisGroupStringValue) {
        this.analysisGroupStringValue = analysisGroupStringValue;
    }

    public boolean getAnalysisGroupPublicData() {
        return this.analysisGroupPublicData;
    }

    public void setAnalysisGroupPublicData(boolean analysisGroupPublicData) {
        this.analysisGroupPublicData = analysisGroupPublicData;
    }

    public static String toJsonArray(Collection<ParentLotAssayDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<ParentLotAssayDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }
}
