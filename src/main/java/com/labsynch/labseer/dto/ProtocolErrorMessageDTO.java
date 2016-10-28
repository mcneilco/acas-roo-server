package com.labsynch.labseer.dto;

import java.util.Collection;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.utils.ExcludeNulls;

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
	
}