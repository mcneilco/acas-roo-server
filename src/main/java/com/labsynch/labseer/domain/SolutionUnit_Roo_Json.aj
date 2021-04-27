// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.SolutionUnit;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect SolutionUnit_Roo_Json {
    
    public String SolutionUnit.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String SolutionUnit.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static SolutionUnit SolutionUnit.fromJsonToSolutionUnit(String json) {
        return new JSONDeserializer<SolutionUnit>()
        .use(null, SolutionUnit.class).deserialize(json);
    }
    
    public static String SolutionUnit.toJsonArray(Collection<SolutionUnit> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String SolutionUnit.toJsonArray(Collection<SolutionUnit> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<SolutionUnit> SolutionUnit.fromJsonArrayToSolutionUnits(String json) {
        return new JSONDeserializer<List<SolutionUnit>>()
        .use("values", SolutionUnit.class).deserialize(json);
    }
    
}