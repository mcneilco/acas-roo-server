// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.ParentAliasDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect ParentAliasDTO_Roo_Json {
    
    public String ParentAliasDTO.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String ParentAliasDTO.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static ParentAliasDTO ParentAliasDTO.fromJsonToParentAliasDTO(String json) {
        return new JSONDeserializer<ParentAliasDTO>()
        .use(null, ParentAliasDTO.class).deserialize(json);
    }
    
    public static String ParentAliasDTO.toJsonArray(Collection<ParentAliasDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String ParentAliasDTO.toJsonArray(Collection<ParentAliasDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<ParentAliasDTO> ParentAliasDTO.fromJsonArrayToParentAliasDTO(String json) {
        return new JSONDeserializer<List<ParentAliasDTO>>()
        .use("values", ParentAliasDTO.class).deserialize(json);
    }
    
}
