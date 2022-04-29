package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.labsynch.labseer.domain.LsThing;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class LsThingValidationDTO {

    public LsThingValidationDTO() {

    }

    private LsThing lsThing;

    private boolean uniqueName;

    private boolean uniqueInteractions;

    private boolean orderMatters;

    private boolean forwardAndReverseAreSame;

    private Set<ValueRuleDTO> valueRules;

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static LsThingValidationDTO fromJsonToLsThingValidationDTO(String json) {
        return new JSONDeserializer<LsThingValidationDTO>()
                .use(null, LsThingValidationDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<LsThingValidationDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<LsThingValidationDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<LsThingValidationDTO> fromJsonArrayToLsThingValidatioes(String json) {
        return new JSONDeserializer<List<LsThingValidationDTO>>()
                .use("values", LsThingValidationDTO.class).deserialize(json);
    }

    public LsThing getLsThing() {
        return this.lsThing;
    }

    public void setLsThing(LsThing lsThing) {
        this.lsThing = lsThing;
    }

    public boolean isUniqueName() {
        return this.uniqueName;
    }

    public void setUniqueName(boolean uniqueName) {
        this.uniqueName = uniqueName;
    }

    public boolean isUniqueInteractions() {
        return this.uniqueInteractions;
    }

    public void setUniqueInteractions(boolean uniqueInteractions) {
        this.uniqueInteractions = uniqueInteractions;
    }

    public boolean isOrderMatters() {
        return this.orderMatters;
    }

    public void setOrderMatters(boolean orderMatters) {
        this.orderMatters = orderMatters;
    }

    public boolean isForwardAndReverseAreSame() {
        return this.forwardAndReverseAreSame;
    }

    public void setForwardAndReverseAreSame(boolean forwardAndReverseAreSame) {
        this.forwardAndReverseAreSame = forwardAndReverseAreSame;
    }

    public Set<ValueRuleDTO> getValueRules() {
        return this.valueRules;
    }

    public void setValueRules(Set<ValueRuleDTO> valueRules) {
        this.valueRules = valueRules;
    }
}
