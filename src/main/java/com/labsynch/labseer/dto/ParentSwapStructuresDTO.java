package com.labsynch.labseer.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class ParentSwapStructuresDTO {

    private String corpName1;

    private String corpName2;

    private String username;

    public String getCorpName1() {
        return this.corpName1;
    }

    public void setCorpName1(String corpName1) {
        this.corpName1 = corpName1;
    }

    public String getCorpName2() {
        return this.corpName2;
    }

    public void setCorpName2(String corpName2) {
        this.corpName2 = corpName2;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String toJson() {
        return new JSONSerializer().include("corpName1", "corpName2", "username").serialize(this);
    }

    public static ParentSwapStructuresDTO fromJSONToParentSwapStructuresDTO(String json) {
        return new JSONDeserializer<ParentSwapStructuresDTO>().use(null, ParentSwapStructuresDTO.class).deserialize(json);
    }
}
