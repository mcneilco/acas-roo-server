// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.MolCleanInputDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect MolCleanInputDTO_Roo_Json {
    
    public String MolCleanInputDTO.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String MolCleanInputDTO.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static MolCleanInputDTO MolCleanInputDTO.fromJsonToMolCleanInputDTO(String json) {
        return new JSONDeserializer<MolCleanInputDTO>()
        .use(null, MolCleanInputDTO.class).deserialize(json);
    }
    
    public static String MolCleanInputDTO.toJsonArray(Collection<MolCleanInputDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String MolCleanInputDTO.toJsonArray(Collection<MolCleanInputDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<MolCleanInputDTO> MolCleanInputDTO.fromJsonArrayToMoes(String json) {
        return new JSONDeserializer<List<MolCleanInputDTO>>()
        .use("values", MolCleanInputDTO.class).deserialize(json);
    }
    
}
