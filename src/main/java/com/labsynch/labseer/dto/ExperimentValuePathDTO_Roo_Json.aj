// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.ExperimentValuePathDTO;
import flexjson.JSONDeserializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect ExperimentValuePathDTO_Roo_Json {
    
    public static ExperimentValuePathDTO ExperimentValuePathDTO.fromJsonToExperimentValuePathDTO(String json) {
        return new JSONDeserializer<ExperimentValuePathDTO>()
        .use(null, ExperimentValuePathDTO.class).deserialize(json);
    }
    
    public static Collection<ExperimentValuePathDTO> ExperimentValuePathDTO.fromJsonArrayToExperimentValuePathDTO(String json) {
        return new JSONDeserializer<List<ExperimentValuePathDTO>>()
        .use("values", ExperimentValuePathDTO.class).deserialize(json);
    }
    
}