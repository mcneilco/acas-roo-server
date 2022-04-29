package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class ProtocolErrorMessageDTO {

	String protocolCodeName;
	
	String level;
	
	String message;
	
	Protocol protocol;
		
	@Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class").include("protocol.lsTags", "protocol.lsLabels", "protocol.lsStates.lsValues").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toJsonStub() {
        return new JSONSerializer().exclude("*.class", "protocol.lsStates").include("protocol.lsTags", "protocol.lsLabels").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public static String toJsonArray(Collection<ProtocolErrorMessageDTO> collection) {
        return new JSONSerializer().exclude("*.class").include("protocol.lsTags", "protocol.lsLabels", "protocol.lsStates.lsValues").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStub(Collection<ProtocolErrorMessageDTO> collection) {
        return new JSONSerializer().exclude("*.class", "protocol.lsStates").include("protocol.lsTags", "protocol.lsLabels").transform(new ExcludeNulls(), void.class).serialize(collection);
    }
    
	public ProtocolErrorMessageDTO(){
	}
	
	public ProtocolErrorMessageDTO(String protocolCodeName, Protocol protocol){
		this.protocolCodeName = protocolCodeName;
		this.protocol = protocol;
	}
	

	public String getProtocolCodeName() {
        return this.protocolCodeName;
    }

	public void setProtocolCodeName(String protocolCodeName) {
        this.protocolCodeName = protocolCodeName;
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

	public Protocol getProtocol() {
        return this.protocol;
    }

	public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

	public static ProtocolErrorMessageDTO fromJsonToProtocolErrorMessageDTO(String json) {
        return new JSONDeserializer<ProtocolErrorMessageDTO>()
        .use(null, ProtocolErrorMessageDTO.class).deserialize(json);
    }

	public static Collection<ProtocolErrorMessageDTO> fromJsonArrayToProtocolErroes(String json) {
        return new JSONDeserializer<List<ProtocolErrorMessageDTO>>()
        .use("values", ProtocolErrorMessageDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}