// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.AuthUserDTO;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect AuthUserDTO_Roo_Json {
    
    public String AuthUserDTO.toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public static AuthUserDTO AuthUserDTO.fromJsonToAuthUserDTO(String json) {
        return new JSONDeserializer<AuthUserDTO>().use(null, AuthUserDTO.class).deserialize(json);
    }
    
    public static String AuthUserDTO.toJsonArray(Collection<AuthUserDTO> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static Collection<AuthUserDTO> AuthUserDTO.fromJsonArrayToAuthUserDTO(String json) {
        return new JSONDeserializer<List<AuthUserDTO>>().use(null, ArrayList.class).use("values", AuthUserDTO.class).deserialize(json);
    }
    
}
