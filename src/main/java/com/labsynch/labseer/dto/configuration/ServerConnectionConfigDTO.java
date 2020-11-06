package com.labsynch.labseer.dto.configuration;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ServerConnectionConfigDTO {

    private boolean connectToServer;

    private String baseServerURL;
    
    private String logoutURL;
    
    private String acasURL;

	private String acasAppURL;


	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static ServerConnectionConfigDTO fromJsonToServerConnectionConfigDTO(String json) {
        return new JSONDeserializer<ServerConnectionConfigDTO>()
        .use(null, ServerConnectionConfigDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ServerConnectionConfigDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ServerConnectionConfigDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ServerConnectionConfigDTO> fromJsonArrayToServerConnectionCoes(String json) {
        return new JSONDeserializer<List<ServerConnectionConfigDTO>>()
        .use("values", ServerConnectionConfigDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public boolean isConnectToServer() {
        return this.connectToServer;
    }

	public void setConnectToServer(boolean connectToServer) {
        this.connectToServer = connectToServer;
    }

	public String getBaseServerURL() {
        return this.baseServerURL;
    }

	public void setBaseServerURL(String baseServerURL) {
        this.baseServerURL = baseServerURL;
    }

	public String getLogoutURL() {
        return this.logoutURL;
    }

	public void setLogoutURL(String logoutURL) {
        this.logoutURL = logoutURL;
    }

	public String getAcasURL() {
        return this.acasURL;
    }

	public void setAcasURL(String acasURL) {
        this.acasURL = acasURL;
    }

	public String getAcasAppURL() {
        return this.acasAppURL;
    }

	public void setAcasAppURL(String acasAppURL) {
        this.acasAppURL = acasAppURL;
    }
}
