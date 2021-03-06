// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.BatchCodeDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect BatchCodeDTO_Roo_Json {
    
    public String BatchCodeDTO.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String BatchCodeDTO.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static BatchCodeDTO BatchCodeDTO.fromJsonToBatchCodeDTO(String json) {
        return new JSONDeserializer<BatchCodeDTO>()
        .use(null, BatchCodeDTO.class).deserialize(json);
    }
    
    public static String BatchCodeDTO.toJsonArray(Collection<BatchCodeDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String BatchCodeDTO.toJsonArray(Collection<BatchCodeDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<BatchCodeDTO> BatchCodeDTO.fromJsonArrayToBatchCoes(String json) {
        return new JSONDeserializer<List<BatchCodeDTO>>()
        .use("values", BatchCodeDTO.class).deserialize(json);
    }
    
}
