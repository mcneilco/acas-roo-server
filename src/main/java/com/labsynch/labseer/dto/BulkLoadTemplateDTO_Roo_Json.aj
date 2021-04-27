// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.BulkLoadTemplateDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect BulkLoadTemplateDTO_Roo_Json {
    
    public String BulkLoadTemplateDTO.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String BulkLoadTemplateDTO.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static BulkLoadTemplateDTO BulkLoadTemplateDTO.fromJsonToBulkLoadTemplateDTO(String json) {
        return new JSONDeserializer<BulkLoadTemplateDTO>()
        .use(null, BulkLoadTemplateDTO.class).deserialize(json);
    }
    
    public static String BulkLoadTemplateDTO.toJsonArray(Collection<BulkLoadTemplateDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String BulkLoadTemplateDTO.toJsonArray(Collection<BulkLoadTemplateDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<BulkLoadTemplateDTO> BulkLoadTemplateDTO.fromJsonArrayToBulkLoes(String json) {
        return new JSONDeserializer<List<BulkLoadTemplateDTO>>()
        .use("values", BulkLoadTemplateDTO.class).deserialize(json);
    }
    
}