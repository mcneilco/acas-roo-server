package com.labsynch.labseer.dto.configuration;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
public class MainConfigDTO {

    private ServerConnectionConfigDTO serverConnection;

    private ClientUILabelsDTO clientUILabels;
    
    private MarvinDTO marvin;

    private MetaLotDTO metaLot;
    
    private ServerSettingsConfigDTO serverSettings;
    
    private BulkLoadSettingsConfigDTO bulkLoadSettings;


	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static MainConfigDTO fromJsonToMainConfigDTO(String json) {
        return new JSONDeserializer<MainConfigDTO>()
        .use(null, MainConfigDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<MainConfigDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<MainConfigDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<MainConfigDTO> fromJsonArrayToMainCoes(String json) {
        return new JSONDeserializer<List<MainConfigDTO>>()
        .use("values", MainConfigDTO.class).deserialize(json);
    }

	public ServerConnectionConfigDTO getServerConnection() {
        return this.serverConnection;
    }

	public void setServerConnection(ServerConnectionConfigDTO serverConnection) {
        this.serverConnection = serverConnection;
    }

	public ClientUILabelsDTO getClientUILabels() {
        return this.clientUILabels;
    }

	public void setClientUILabels(ClientUILabelsDTO clientUILabels) {
        this.clientUILabels = clientUILabels;
    }

	public MarvinDTO getMarvin() {
        return this.marvin;
    }

	public void setMarvin(MarvinDTO marvin) {
        this.marvin = marvin;
    }

	public MetaLotDTO getMetaLot() {
        return this.metaLot;
    }

	public void setMetaLot(MetaLotDTO metaLot) {
        this.metaLot = metaLot;
    }

	public ServerSettingsConfigDTO getServerSettings() {
        return this.serverSettings;
    }

	public void setServerSettings(ServerSettingsConfigDTO serverSettings) {
        this.serverSettings = serverSettings;
    }

	public BulkLoadSettingsConfigDTO getBulkLoadSettings() {
        return this.bulkLoadSettings;
    }

	public void setBulkLoadSettings(BulkLoadSettingsConfigDTO bulkLoadSettings) {
        this.bulkLoadSettings = bulkLoadSettings;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
