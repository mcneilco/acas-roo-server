// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.CurveFitDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect CurveFitDTO_Roo_Json {
    
    public String CurveFitDTO.toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public static CurveFitDTO CurveFitDTO.fromJsonToCurveFitDTO(String json) {
        return new JSONDeserializer<CurveFitDTO>().use(null, CurveFitDTO.class).deserialize(json);
    }
    
    public static String CurveFitDTO.toJsonArray(Collection<CurveFitDTO> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static Collection<CurveFitDTO> CurveFitDTO.fromJsonArrayToCurveFitDTO(String json) {
        return new JSONDeserializer<List<CurveFitDTO>>().use(null, ArrayList.class).use("values", CurveFitDTO.class).deserialize(json);
    }
    
}