package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.AnalysisGroupState;
import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class AnalysisGroupStatePathDTO {

	public AnalysisGroupStatePathDTO() {
	}

	private String idOrCodeName;

	private String stateType;
	
	private String stateKind;
	
	private Collection<AnalysisGroupState> states;
	
	public String toJson() {
        return new JSONSerializer().include("states.lsValues").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<AnalysisGroupStatePathDTO> collection) {
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

	public Collection<AnalysisGroupState> getStates() {
        return this.states;
    }

	public void setStates(Collection<AnalysisGroupState> states) {
        this.states = states;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static AnalysisGroupStatePathDTO fromJsonToAnalysisGroupStatePathDTO(String json) {
        return new JSONDeserializer<AnalysisGroupStatePathDTO>()
        .use(null, AnalysisGroupStatePathDTO.class).deserialize(json);
    }

	public static Collection<AnalysisGroupStatePathDTO> fromJsonArrayToAnalysisGroes(String json) {
        return new JSONDeserializer<List<AnalysisGroupStatePathDTO>>()
        .use("values", AnalysisGroupStatePathDTO.class).deserialize(json);
    }
}


