package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

public class SearchCompoundReturnDTO {

    private String corpName;

    private String corpNameType;

    private List<ParentAliasDTO> parentAliases;

    private String stereoCategoryName;

    private String stereoComment;

    private List<SearchLotDTO> lotIDs = new ArrayList<SearchLotDTO>();

    private String molStructure;

    public String toJson() {
        return new JSONSerializer().exclude("*.class")
                .transform(new DateTransformer("MM/dd/yyyy"), Date.class)
                .serialize(this);
    }

    public static SearchCompoundReturnDTO fromJsonToSearchCompoundReturnDTO(String json) {
        return new JSONDeserializer<SearchCompoundReturnDTO>().use(null, SearchCompoundReturnDTO.class)
                .use(Date.class, new DateTransformer("MM/dd/yyyy"))
                .deserialize(json);
    }

    public static String toJsonArray(Collection<SearchCompoundReturnDTO> collection) {
        return new JSONSerializer().exclude("*.class").include("lotIDs", "parentAliases")
                .transform(new DateTransformer("MM/dd/yyyy"), Date.class)
                .serialize(collection);
    }

    public static Collection<SearchCompoundReturnDTO> fromJsonArrayToSearchCompoes(String json) {
        return new JSONDeserializer<List<SearchCompoundReturnDTO>>().use(null, ArrayList.class)
                .use("values", SearchCompoundReturnDTO.class)
                .use(Date.class, new DateTransformer("MM/dd/yyyy"))
                .deserialize(json);
    }

    public String getCorpName() {
        return this.corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCorpNameType() {
        return this.corpNameType;
    }

    public void setCorpNameType(String corpNameType) {
        this.corpNameType = corpNameType;
    }

    public List<ParentAliasDTO> getParentAliases() {
        return this.parentAliases;
    }

    public void setParentAliases(List<ParentAliasDTO> parentAliases) {
        this.parentAliases = parentAliases;
    }

    public String getStereoCategoryName() {
        return this.stereoCategoryName;
    }

    public void setStereoCategoryName(String stereoCategoryName) {
        this.stereoCategoryName = stereoCategoryName;
    }

    public String getStereoComment() {
        return this.stereoComment;
    }

    public void setStereoComment(String stereoComment) {
        this.stereoComment = stereoComment;
    }

    public List<SearchLotDTO> getLotIDs() {
        return this.lotIDs;
    }

    public void setLotIDs(List<SearchLotDTO> lotIDs) {
        this.lotIDs = lotIDs;
    }

    public String getMolStructure() {
        return this.molStructure;
    }

    public void setMolStructure(String molStructure) {
        this.molStructure = molStructure;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
