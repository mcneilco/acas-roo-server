// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.ParentAnnotation;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect ParentAnnotation_Roo_Json {
    
    public String ParentAnnotation.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String ParentAnnotation.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static ParentAnnotation ParentAnnotation.fromJsonToParentAnnotation(String json) {
        return new JSONDeserializer<ParentAnnotation>()
        .use(null, ParentAnnotation.class).deserialize(json);
    }
    
    public static String ParentAnnotation.toJsonArray(Collection<ParentAnnotation> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String ParentAnnotation.toJsonArray(Collection<ParentAnnotation> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<ParentAnnotation> ParentAnnotation.fromJsonArrayToParentAnnotations(String json) {
        return new JSONDeserializer<List<ParentAnnotation>>()
        .use("values", ParentAnnotation.class).deserialize(json);
    }
    
}
