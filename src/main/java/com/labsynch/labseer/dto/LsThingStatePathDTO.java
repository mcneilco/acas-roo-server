package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.labsynch.labseer.domain.LsThingState;
import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class LsThingStatePathDTO {

	public LsThingStatePathDTO() {
	}

	private String idOrCodeName;

	private String stateType;
	
	private String stateKind;
	
	private Collection<LsThingState> states;
	
	public String toJson() {
        return new JSONSerializer().include("states.lsValues").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<LsThingStatePathDTO> collection) {
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

	public Collection<LsThingState> getStates() {
        return this.states;
    }

	public void setStates(Collection<LsThingState> states) {
        this.states = states;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static LsThingStatePathDTO fromJsonToLsThingStatePathDTO(String json) {
        return new JSONDeserializer<LsThingStatePathDTO>()
        .use(null, LsThingStatePathDTO.class).deserialize(json);
    }

	public static Collection<LsThingStatePathDTO> fromJsonArrayToLsThingStatePathDTO(String json) {
        return new JSONDeserializer<List<LsThingStatePathDTO>>()
        .use("values", LsThingStatePathDTO.class).deserialize(json);
    }
}


