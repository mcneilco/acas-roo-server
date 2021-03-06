// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.ExperimentCsvDataDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect ExperimentCsvDataDTO_Roo_Json {
    
    public String ExperimentCsvDataDTO.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String ExperimentCsvDataDTO.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static ExperimentCsvDataDTO ExperimentCsvDataDTO.fromJsonToExperimentCsvDataDTO(String json) {
        return new JSONDeserializer<ExperimentCsvDataDTO>()
        .use(null, ExperimentCsvDataDTO.class).deserialize(json);
    }
    
    public static String ExperimentCsvDataDTO.toJsonArray(Collection<ExperimentCsvDataDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String ExperimentCsvDataDTO.toJsonArray(Collection<ExperimentCsvDataDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<ExperimentCsvDataDTO> ExperimentCsvDataDTO.fromJsonArrayToExperimentCsvDataDTO(String json) {
        return new JSONDeserializer<List<ExperimentCsvDataDTO>>()
        .use("values", ExperimentCsvDataDTO.class).deserialize(json);
    }
    
}
