// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.PreferredNameDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect PreferredNameDTO_Roo_Json {
    
    public String PreferredNameDTO.toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public static PreferredNameDTO PreferredNameDTO.fromJsonToPreferredNameDTO(String json) {
        return new JSONDeserializer<PreferredNameDTO>().use(null, PreferredNameDTO.class).deserialize(json);
    }
    
    public static String PreferredNameDTO.toJsonArray(Collection<PreferredNameDTO> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static Collection<PreferredNameDTO> PreferredNameDTO.fromJsonArrayToPreferredNameDTO(String json) {
        return new JSONDeserializer<List<PreferredNameDTO>>().use(null, ArrayList.class).use("values", PreferredNameDTO.class).deserialize(json);
    }
    
}