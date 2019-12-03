// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.AuthorQueryDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect AuthorQueryDTO_Roo_Json {
    
    public String AuthorQueryDTO.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String AuthorQueryDTO.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static AuthorQueryDTO AuthorQueryDTO.fromJsonToAuthorQueryDTO(String json) {
        return new JSONDeserializer<AuthorQueryDTO>()
        .use(null, AuthorQueryDTO.class).deserialize(json);
    }
    
    public static String AuthorQueryDTO.toJsonArray(Collection<AuthorQueryDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String AuthorQueryDTO.toJsonArray(Collection<AuthorQueryDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<AuthorQueryDTO> AuthorQueryDTO.fromJsonArrayToAuthoes(String json) {
        return new JSONDeserializer<List<AuthorQueryDTO>>()
        .use("values", AuthorQueryDTO.class).deserialize(json);
    }
    
}