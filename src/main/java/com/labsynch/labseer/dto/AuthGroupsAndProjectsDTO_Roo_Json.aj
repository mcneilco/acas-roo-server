// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.AuthGroupsAndProjectsDTO;
import flexjson.JSONDeserializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect AuthGroupsAndProjectsDTO_Roo_Json {
    
    public static AuthGroupsAndProjectsDTO AuthGroupsAndProjectsDTO.fromJsonToAuthGroupsAndProjectsDTO(String json) {
        return new JSONDeserializer<AuthGroupsAndProjectsDTO>()
        .use(null, AuthGroupsAndProjectsDTO.class).deserialize(json);
    }
    
    public static Collection<AuthGroupsAndProjectsDTO> AuthGroupsAndProjectsDTO.fromJsonArrayToAuthGroupsAndProes(String json) {
        return new JSONDeserializer<List<AuthGroupsAndProjectsDTO>>()
        .use("values", AuthGroupsAndProjectsDTO.class).deserialize(json);
    }
    
}
