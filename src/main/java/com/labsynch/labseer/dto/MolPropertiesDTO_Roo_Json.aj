// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.MolPropertiesDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect MolPropertiesDTO_Roo_Json {
    
    public String MolPropertiesDTO.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String MolPropertiesDTO.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static MolPropertiesDTO MolPropertiesDTO.fromJsonToMolPropertiesDTO(String json) {
        return new JSONDeserializer<MolPropertiesDTO>()
        .use(null, MolPropertiesDTO.class).deserialize(json);
    }
    
    public static String MolPropertiesDTO.toJsonArray(Collection<MolPropertiesDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String MolPropertiesDTO.toJsonArray(Collection<MolPropertiesDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<MolPropertiesDTO> MolPropertiesDTO.fromJsonArrayToMolProes(String json) {
        return new JSONDeserializer<List<MolPropertiesDTO>>()
        .use("values", MolPropertiesDTO.class).deserialize(json);
    }
    
}
