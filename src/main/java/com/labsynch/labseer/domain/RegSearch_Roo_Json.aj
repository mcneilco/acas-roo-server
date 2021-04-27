// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.RegSearch;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect RegSearch_Roo_Json {
    
    public String RegSearch.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String RegSearch.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static RegSearch RegSearch.fromJsonToRegSearch(String json) {
        return new JSONDeserializer<RegSearch>()
        .use(null, RegSearch.class).deserialize(json);
    }
    
    public static String RegSearch.toJsonArray(Collection<RegSearch> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String RegSearch.toJsonArray(Collection<RegSearch> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<RegSearch> RegSearch.fromJsonArrayToRegSearches(String json) {
        return new JSONDeserializer<List<RegSearch>>()
        .use("values", RegSearch.class).deserialize(json);
    }
    
}