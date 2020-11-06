package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.labsynch.labseer.domain.ProtocolState;
import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class ProtocolStatePathDTO {

	public ProtocolStatePathDTO() {
	}

	private String idOrCodeName;

	private String stateType;
	
	private String stateKind;
	
	private Collection<ProtocolState> states;
	
	public String toJson() {
        return new JSONSerializer().include("states.lsValues").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<ProtocolStatePathDTO> collection) {
        return new JSONSerializer().include("states.lsValues").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
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

	public Collection<ProtocolState> getStates() {
        return this.states;
    }

	public void setStates(Collection<ProtocolState> states) {
        this.states = states;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static ProtocolStatePathDTO fromJsonToProtocolStatePathDTO(String json) {
        return new JSONDeserializer<ProtocolStatePathDTO>()
        .use(null, ProtocolStatePathDTO.class).deserialize(json);
    }

	public static Collection<ProtocolStatePathDTO> fromJsonArrayToProtocoes(String json) {
        return new JSONDeserializer<List<ProtocolStatePathDTO>>()
        .use("values", ProtocolStatePathDTO.class).deserialize(json);
    }
}


