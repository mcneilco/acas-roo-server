// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.StandardizationHistory;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect StandardizationHistory_Roo_Json {
    
    public String StandardizationHistory.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String StandardizationHistory.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static StandardizationHistory StandardizationHistory.fromJsonToStandardizationHistory(String json) {
        return new JSONDeserializer<StandardizationHistory>()
        .use(null, StandardizationHistory.class).deserialize(json);
    }
    
    public static String StandardizationHistory.toJsonArray(Collection<StandardizationHistory> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String StandardizationHistory.toJsonArray(Collection<StandardizationHistory> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<StandardizationHistory> StandardizationHistory.fromJsonArrayToStandardizationHistorys(String json) {
        return new JSONDeserializer<List<StandardizationHistory>>()
        .use("values", StandardizationHistory.class).deserialize(json);
    }
    
}
