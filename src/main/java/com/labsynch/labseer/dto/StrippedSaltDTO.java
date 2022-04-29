package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.domain.Salt;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class StrippedSaltDTO {

    private Map<Salt, Integer> saltCounts;

    private Set<? extends CmpdRegMolecule> unidentifiedFragments;

    public StrippedSaltDTO() {

    }

    public String toJson() {
        return new JSONSerializer()
                .include("lotCodes")
                .exclude("*.class").serialize(this);
    }

    public static String toJsonArray(Collection<StrippedSaltDTO> collection) {
        return new JSONSerializer()
                .include("lotCodes")
                .exclude("*.class").serialize(collection);
    }

    public Map<Salt, Integer> getSaltCounts() {
        return this.saltCounts;
    }

    public void setSaltCounts(Map<Salt, Integer> saltCounts) {
        this.saltCounts = saltCounts;
    }

    public Set<? extends CmpdRegMolecule> getUnidentifiedFragments() {
        return this.unidentifiedFragments;
    }

    public void setUnidentifiedFragments(Set<? extends CmpdRegMolecule> unidentifiedFragments) {
        this.unidentifiedFragments = unidentifiedFragments;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static StrippedSaltDTO fromJsonToStrippedSaltDTO(String json) {
        return new JSONDeserializer<StrippedSaltDTO>()
                .use(null, StrippedSaltDTO.class).deserialize(json);
    }

    public static Collection<StrippedSaltDTO> fromJsonArrayToStrippedSaltDTO(String json) {
        return new JSONDeserializer<List<StrippedSaltDTO>>()
                .use("values", StrippedSaltDTO.class).deserialize(json);
    }
}
