// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.AuthProjectGroupsDTO;
import flexjson.JSONDeserializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect AuthProjectGroupsDTO_Roo_Json {
    
    public static AuthProjectGroupsDTO AuthProjectGroupsDTO.fromJsonToAuthProjectGroupsDTO(String json) {
        return new JSONDeserializer<AuthProjectGroupsDTO>()
        .use(null, AuthProjectGroupsDTO.class).deserialize(json);
    }
    
    public static Collection<AuthProjectGroupsDTO> AuthProjectGroupsDTO.fromJsonArrayToAuthProjectGroes(String json) {
        return new JSONDeserializer<List<AuthProjectGroupsDTO>>()
        .use("values", AuthProjectGroupsDTO.class).deserialize(json);
    }
    
}
