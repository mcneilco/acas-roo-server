// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.SubjectValueDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect SubjectValueDTO_Roo_Json {
    
    public String SubjectValueDTO.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String SubjectValueDTO.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static SubjectValueDTO SubjectValueDTO.fromJsonToSubjectValueDTO(String json) {
        return new JSONDeserializer<SubjectValueDTO>()
        .use(null, SubjectValueDTO.class).deserialize(json);
    }
    
    public static String SubjectValueDTO.toJsonArray(Collection<SubjectValueDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String SubjectValueDTO.toJsonArray(Collection<SubjectValueDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<SubjectValueDTO> SubjectValueDTO.fromJsonArrayToSubjectValueDTO(String json) {
        return new JSONDeserializer<List<SubjectValueDTO>>()
        .use("values", SubjectValueDTO.class).deserialize(json);
    }
    
}
