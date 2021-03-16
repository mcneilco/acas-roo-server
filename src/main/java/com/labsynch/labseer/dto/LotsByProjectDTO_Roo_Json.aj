// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.LotsByProjectDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect LotsByProjectDTO_Roo_Json {
    
    public String LotsByProjectDTO.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String LotsByProjectDTO.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static LotsByProjectDTO LotsByProjectDTO.fromJsonToLotsByProjectDTO(String json) {
        return new JSONDeserializer<LotsByProjectDTO>()
        .use(null, LotsByProjectDTO.class).deserialize(json);
    }
    
    public static String LotsByProjectDTO.toJsonArray(Collection<LotsByProjectDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String LotsByProjectDTO.toJsonArray(Collection<LotsByProjectDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<LotsByProjectDTO> LotsByProjectDTO.fromJsonArrayToLotsByProes(String json) {
        return new JSONDeserializer<List<LotsByProjectDTO>>()
        .use("values", LotsByProjectDTO.class).deserialize(json);
    }
    
}