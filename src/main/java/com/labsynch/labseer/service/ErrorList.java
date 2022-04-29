package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


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

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public ArrayList<ErrorMessage> getErrors() {
        return this.errors;
    }

	public void setErrors(ArrayList<ErrorMessage> errors) {
        this.errors = errors;
    }
}
