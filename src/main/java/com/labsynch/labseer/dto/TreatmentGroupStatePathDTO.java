package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.TreatmentGroupState;
import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class TreatmentGroupStatePathDTO {

	public TreatmentGroupStatePathDTO() {
	}

	private String idOrCodeName;

	private String stateType;
	
	private String stateKind;
	
	private Collection<TreatmentGroupState> states;
	
	public String toJson() {
        return new JSONSerializer().include("states.lsValues").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<TreatmentGroupStatePathDTO> collection) {
        return new JSONSerializer().include("states.lsValues").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

	public static TreatmentGroupStatePathDTO fromJsonToTreatmentGroupStatePathDTO(String json) {
        return new JSONDeserializer<TreatmentGroupStatePathDTO>()
        .use(null, TreatmentGroupStatePathDTO.class).deserialize(json);
    }

	public static Collection<TreatmentGroupStatePathDTO> fromJsonArrayToTreatmentGroes(String json) {
        return new JSONDeserializer<List<TreatmentGroupStatePathDTO>>()
        .use("values", TreatmentGroupStatePathDTO.class).deserialize(json);
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

	public Collection<TreatmentGroupState> getStates() {
        return this.states;
    }

	public void setStates(Collection<TreatmentGroupState> states) {
        this.states = states;
    }
}


