// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.TsvLoaderResponseDTO;
import flexjson.JSONDeserializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect TsvLoaderResponseDTO_Roo_Json {
    
    public static TsvLoaderResponseDTO TsvLoaderResponseDTO.fromJsonToTsvLoaderResponseDTO(String json) {
        return new JSONDeserializer<TsvLoaderResponseDTO>()
        .use(null, TsvLoaderResponseDTO.class).deserialize(json);
    }
    
    public static Collection<TsvLoaderResponseDTO> TsvLoaderResponseDTO.fromJsonArrayToTsvLoaderRespoes(String json) {
        return new JSONDeserializer<List<TsvLoaderResponseDTO>>()
        .use("values", TsvLoaderResponseDTO.class).deserialize(json);
    }
    
}
