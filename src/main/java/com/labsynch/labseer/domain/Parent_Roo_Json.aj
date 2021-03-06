// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.Parent;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect Parent_Roo_Json {
    
    public String Parent.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String Parent.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static Parent Parent.fromJsonToParent(String json) {
        return new JSONDeserializer<Parent>()
        .use(null, Parent.class).deserialize(json);
    }
    
    public static String Parent.toJsonArray(Collection<Parent> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String Parent.toJsonArray(Collection<Parent> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<Parent> Parent.fromJsonArrayToParents(String json) {
        return new JSONDeserializer<List<Parent>>()
        .use("values", Parent.class).deserialize(json);
    }
    
}
