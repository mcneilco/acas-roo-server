// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.IdCollectionDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect IdCollectionDTO_Roo_Json {
    
    public String IdCollectionDTO.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String IdCollectionDTO.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static IdCollectionDTO IdCollectionDTO.fromJsonToIdCollectionDTO(String json) {
        return new JSONDeserializer<IdCollectionDTO>()
        .use(null, IdCollectionDTO.class).deserialize(json);
    }
    
    public static String IdCollectionDTO.toJsonArray(Collection<IdCollectionDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String IdCollectionDTO.toJsonArray(Collection<IdCollectionDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<IdCollectionDTO> IdCollectionDTO.fromJsonArrayToIdCollectioes(String json) {
        return new JSONDeserializer<List<IdCollectionDTO>>()
        .use("values", IdCollectionDTO.class).deserialize(json);
    }
    
}
