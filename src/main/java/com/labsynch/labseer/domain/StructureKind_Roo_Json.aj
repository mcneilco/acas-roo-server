// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.StructureKind;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect StructureKind_Roo_Json {
    
    public String StructureKind.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String StructureKind.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static StructureKind StructureKind.fromJsonToStructureKind(String json) {
        return new JSONDeserializer<StructureKind>()
        .use(null, StructureKind.class).deserialize(json);
    }
    
    public static String StructureKind.toJsonArray(Collection<StructureKind> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String StructureKind.toJsonArray(Collection<StructureKind> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<StructureKind> StructureKind.fromJsonArrayToStructureKinds(String json) {
        return new JSONDeserializer<List<StructureKind>>()
        .use("values", StructureKind.class).deserialize(json);
    }
    
}