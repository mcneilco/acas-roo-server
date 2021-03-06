// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.AuthorLabel;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;

privileged aspect AuthorLabel_Roo_Json {
    
    public String AuthorLabel.toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String AuthorLabel.toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static AuthorLabel AuthorLabel.fromJsonToAuthorLabel(String json) {
        return new JSONDeserializer<AuthorLabel>()
        .use(null, AuthorLabel.class).deserialize(json);
    }
    
    public static String AuthorLabel.toJsonArray(Collection<AuthorLabel> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String AuthorLabel.toJsonArray(Collection<AuthorLabel> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
}
