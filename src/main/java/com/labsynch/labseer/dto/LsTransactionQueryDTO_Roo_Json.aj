// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.dto;

import com.labsynch.labseer.dto.LsTransactionQueryDTO;
import flexjson.JSONDeserializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect LsTransactionQueryDTO_Roo_Json {
    
    public static LsTransactionQueryDTO LsTransactionQueryDTO.fromJsonToLsTransactionQueryDTO(String json) {
        return new JSONDeserializer<LsTransactionQueryDTO>().use(null, LsTransactionQueryDTO.class).deserialize(json);
    }
    
    public static Collection<LsTransactionQueryDTO> LsTransactionQueryDTO.fromJsonArrayToLsTransactioes(String json) {
        return new JSONDeserializer<List<LsTransactionQueryDTO>>().use(null, ArrayList.class).use("values", LsTransactionQueryDTO.class).deserialize(json);
    }
    
}
