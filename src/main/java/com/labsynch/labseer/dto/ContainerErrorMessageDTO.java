package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class ContainerErrorMessageDTO {

	String containerCodeName;
	
	String level;
	
	String message;
	
	Container container;
	
	Container definition;
	
	public String toJson() {
        return new JSONSerializer().include("container.lsStates.lsValues","container.lsLabels","definition.lsStates.lsValues","definition.lsLabels").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }
	
	public static String toJsonArray(Collection<ContainerErrorMessageDTO> collection) {
        return new JSONSerializer().include("container.lsStates.lsValues","container.lsLabels","definition.lsStates.lsValues","definition.lsLabels").exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
	
	public ContainerErrorMessageDTO(){
	}
	
	public ContainerErrorMessageDTO(String containerCodeName, Container container){
		this.containerCodeName = containerCodeName;
		this.container = container;
	}
	

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static ContainerErrorMessageDTO fromJsonToContainerErrorMessageDTO(String json) {
        return new JSONDeserializer<ContainerErrorMessageDTO>()
        .use(null, ContainerErrorMessageDTO.class).deserialize(json);
    }

	public static Collection<ContainerErrorMessageDTO> fromJsonArrayToContainerErroes(String json) {
        return new JSONDeserializer<List<ContainerErrorMessageDTO>>()
        .use("values", ContainerErrorMessageDTO.class).deserialize(json);
    }

	public String getContainerCodeName() {
        return this.containerCodeName;
    }

	public void setContainerCodeName(String containerCodeName) {
        this.containerCodeName = containerCodeName;
    }

	public String getLevel() {
        return this.level;
    }

	public void setLevel(String level) {
        this.level = level;
    }

	public String getMessage() {
        return this.message;
    }

	public void setMessage(String message) {
        this.message = message;
    }

	public Container getContainer() {
        return this.container;
    }

	public void setContainer(Container container) {
        this.container = container;
    }

	public Container getDefinition() {
        return this.definition;
    }

	public void setDefinition(Container definition) {
        this.definition = definition;
    }
}
