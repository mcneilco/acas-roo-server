package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class ErrorList {

    private ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();



	public String toJson() {
        return new JSONSerializer().include("errors").exclude("*.class").serialize(this);
    }

	public static ErrorList fromJsonToErrorList(String json) {
        return new JSONDeserializer<ErrorList>().use(null, ErrorList.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ErrorList> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

	public static Collection<ErrorList> fromJsonArrayToErrorLists(String json) {
        return new JSONDeserializer<List<ErrorList>>().use(null, ArrayList.class).use("values", ErrorList.class).deserialize(json);
    }
}
