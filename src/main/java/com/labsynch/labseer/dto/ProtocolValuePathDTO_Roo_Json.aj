// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.ProtocolValuePathDTO;
import flexjson.JSONDeserializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect ProtocolValuePathDTO_Roo_Json {
    
    public static ProtocolValuePathDTO ProtocolValuePathDTO.fromJsonToProtocolValuePathDTO(String json) {
        return new JSONDeserializer<ProtocolValuePathDTO>()
        .use(null, ProtocolValuePathDTO.class).deserialize(json);
    }
    
    public static Collection<ProtocolValuePathDTO> ProtocolValuePathDTO.fromJsonArrayToProtocoes(String json) {
        return new JSONDeserializer<List<ProtocolValuePathDTO>>()
        .use("values", ProtocolValuePathDTO.class).deserialize(json);
    }
    
}
