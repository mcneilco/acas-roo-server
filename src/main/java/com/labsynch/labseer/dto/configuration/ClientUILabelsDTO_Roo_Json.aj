// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto.configuration;

import com.labsynch.labseer.dto.configuration.ClientUILabelsDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect ClientUILabelsDTO_Roo_Json {
    
    public String ClientUILabelsDTO.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String ClientUILabelsDTO.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static ClientUILabelsDTO ClientUILabelsDTO.fromJsonToClientUILabelsDTO(String json) {
        return new JSONDeserializer<ClientUILabelsDTO>()
        .use(null, ClientUILabelsDTO.class).deserialize(json);
    }
    
    public static String ClientUILabelsDTO.toJsonArray(Collection<ClientUILabelsDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String ClientUILabelsDTO.toJsonArray(Collection<ClientUILabelsDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<ClientUILabelsDTO> ClientUILabelsDTO.fromJsonArrayToClientUILabelsDTO(String json) {
        return new JSONDeserializer<List<ClientUILabelsDTO>>()
        .use("values", ClientUILabelsDTO.class).deserialize(json);
    }
    
}
