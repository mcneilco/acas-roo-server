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
public class MetaLotDTO {

    private boolean saltBeforeLot;

    private boolean lotCalledBatch;

    private boolean showBuid;
    
    private boolean useExactMass;


	public boolean isSaltBeforeLot() {
        return this.saltBeforeLot;
    }

	public void setSaltBeforeLot(boolean saltBeforeLot) {
        this.saltBeforeLot = saltBeforeLot;
    }

	public boolean isLotCalledBatch() {
        return this.lotCalledBatch;
    }

	public void setLotCalledBatch(boolean lotCalledBatch) {
        this.lotCalledBatch = lotCalledBatch;
    }

	public boolean isShowBuid() {
        return this.showBuid;
    }

	public void setShowBuid(boolean showBuid) {
        this.showBuid = showBuid;
    }

	public boolean isUseExactMass() {
        return this.useExactMass;
    }

	public void setUseExactMass(boolean useExactMass) {
        this.useExactMass = useExactMass;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static MetaLotDTO fromJsonToMetaLotDTO(String json) {
        return new JSONDeserializer<MetaLotDTO>()
        .use(null, MetaLotDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<MetaLotDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<MetaLotDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<MetaLotDTO> fromJsonArrayToMetaLoes(String json) {
        return new JSONDeserializer<List<MetaLotDTO>>()
        .use("values", MetaLotDTO.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
