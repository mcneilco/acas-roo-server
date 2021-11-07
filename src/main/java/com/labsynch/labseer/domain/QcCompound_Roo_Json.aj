// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.QcCompound;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect QcCompound_Roo_Json {
    
    public String QcCompound.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String QcCompound.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static QcCompound QcCompound.fromJsonToQcCompound(String json) {
        return new JSONDeserializer<QcCompound>()
        .use(null, QcCompound.class).deserialize(json);
    }
    
    public static String QcCompound.toJsonArray(Collection<QcCompound> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String QcCompound.toJsonArray(Collection<QcCompound> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<QcCompound> QcCompound.fromJsonArrayToQcCompounds(String json) {
        return new JSONDeserializer<List<QcCompound>>()
        .use("values", QcCompound.class).deserialize(json);
    }
    
}