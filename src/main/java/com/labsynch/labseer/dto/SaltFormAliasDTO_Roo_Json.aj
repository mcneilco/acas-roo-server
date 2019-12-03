// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.SaltFormAliasDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect SaltFormAliasDTO_Roo_Json {
    
    public String SaltFormAliasDTO.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String SaltFormAliasDTO.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static SaltFormAliasDTO SaltFormAliasDTO.fromJsonToSaltFormAliasDTO(String json) {
        return new JSONDeserializer<SaltFormAliasDTO>()
        .use(null, SaltFormAliasDTO.class).deserialize(json);
    }
    
    public static String SaltFormAliasDTO.toJsonArray(Collection<SaltFormAliasDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String SaltFormAliasDTO.toJsonArray(Collection<SaltFormAliasDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<SaltFormAliasDTO> SaltFormAliasDTO.fromJsonArrayToSaltFoes(String json) {
        return new JSONDeserializer<List<SaltFormAliasDTO>>()
        .use("values", SaltFormAliasDTO.class).deserialize(json);
    }
    
}