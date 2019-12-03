// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.ContainerQueryDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect ContainerQueryDTO_Roo_Json {
    
    public String ContainerQueryDTO.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String ContainerQueryDTO.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static ContainerQueryDTO ContainerQueryDTO.fromJsonToContainerQueryDTO(String json) {
        return new JSONDeserializer<ContainerQueryDTO>()
        .use(null, ContainerQueryDTO.class).deserialize(json);
    }
    
    public static String ContainerQueryDTO.toJsonArray(Collection<ContainerQueryDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String ContainerQueryDTO.toJsonArray(Collection<ContainerQueryDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<ContainerQueryDTO> ContainerQueryDTO.fromJsonArrayToCoes(String json) {
        return new JSONDeserializer<List<ContainerQueryDTO>>()
        .use("values", ContainerQueryDTO.class).deserialize(json);
    }
    
}