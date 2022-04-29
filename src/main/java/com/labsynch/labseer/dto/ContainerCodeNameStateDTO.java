package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class ContainerCodeNameStateDTO {
	
	 public ContainerCodeNameStateDTO() {
	 }
	
    public ContainerCodeNameStateDTO(String containerCodeName, ContainerState state) {
    		this.containerCodeName = containerCodeName;
    		this.lsState = state;
    }

	private String containerCodeName;
	
	private ContainerState lsState;
	
	public String toJson() {
        return new JSONSerializer().include("lsState.lsValues").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<ContainerCodeNameStateDTO> collection) {
        return new JSONSerializer().include("lsState.lsValues").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }


	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getContainerCodeName() {
        return this.containerCodeName;
    }

	public void setContainerCodeName(String containerCodeName) {
        this.containerCodeName = containerCodeName;
    }

	public ContainerState getLsState() {
        return this.lsState;
    }

	public void setLsState(ContainerState lsState) {
        this.lsState = lsState;
    }

	public static ContainerCodeNameStateDTO fromJsonToContainerCodeNameStateDTO(String json) {
        return new JSONDeserializer<ContainerCodeNameStateDTO>()
        .use(null, ContainerCodeNameStateDTO.class).deserialize(json);
    }

	public static Collection<ContainerCodeNameStateDTO> fromJsonArrayToContainerCoes(String json) {
        return new JSONDeserializer<List<ContainerCodeNameStateDTO>>()
        .use("values", ContainerCodeNameStateDTO.class).deserialize(json);
    }
}