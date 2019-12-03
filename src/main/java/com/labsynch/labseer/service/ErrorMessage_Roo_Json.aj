// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.service;

import com.labsynch.labseer.service.ErrorMessage;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

privileged aspect ErrorMessage_Roo_Json {
    
    public String ErrorMessage.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String ErrorMessage.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static ErrorMessage ErrorMessage.fromJsonToErrorMessage(String json) {
        return new JSONDeserializer<ErrorMessage>()
        .use(null, ErrorMessage.class).deserialize(json);
    }
    
    public static String ErrorMessage.toJsonArray(Collection<ErrorMessage> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String ErrorMessage.toJsonArray(Collection<ErrorMessage> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<ErrorMessage> ErrorMessage.fromJsonArrayToErrorMessages(String json) {
        return new JSONDeserializer<List<ErrorMessage>>()
        .use("values", ErrorMessage.class).deserialize(json);
    }
    
}