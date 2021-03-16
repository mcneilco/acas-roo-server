// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.Lot;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect Lot_Roo_Json {
    
    public String Lot.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String Lot.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static Lot Lot.fromJsonToLot(String json) {
        return new JSONDeserializer<Lot>()
        .use(null, Lot.class).deserialize(json);
    }
    
    public static String Lot.toJsonArray(Collection<Lot> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String Lot.toJsonArray(Collection<Lot> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<Lot> Lot.fromJsonArrayToLots(String json) {
        return new JSONDeserializer<List<Lot>>()
        .use("values", Lot.class).deserialize(json);
    }
    
}