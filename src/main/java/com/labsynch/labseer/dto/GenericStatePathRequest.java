package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class GenericStatePathRequest {

	public GenericStatePathRequest() {
	}

	private String idOrCodeName;

	private String stateType;
	
	private String stateKind;
	
	public String toJson() {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<GenericStatePathRequest> collection) {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getIdOrCodeName() {
        return this.idOrCodeName;
    }

	public void setIdOrCodeName(String idOrCodeName) {
        this.idOrCodeName = idOrCodeName;
    }

	public String getStateType() {
        return this.stateType;
    }

	public void setStateType(String stateType) {
        this.stateType = stateType;
    }

	public String getStateKind() {
        return this.stateKind;
    }

	public void setStateKind(String stateKind) {
        this.stateKind = stateKind;
    }

	public static GenericStatePathRequest fromJsonToGenericStatePathRequest(String json) {
        return new JSONDeserializer<GenericStatePathRequest>()
        .use(null, GenericStatePathRequest.class).deserialize(json);
    }

	public static Collection<GenericStatePathRequest> fromJsonArrayToGenericStatePathRequests(String json) {
        return new JSONDeserializer<List<GenericStatePathRequest>>()
        .use("values", GenericStatePathRequest.class).deserialize(json);
    }
}


