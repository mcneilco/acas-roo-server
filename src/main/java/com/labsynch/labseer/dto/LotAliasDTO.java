package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.NoResultException;

import com.labsynch.labseer.domain.LotAlias;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class LotAliasDTO {

    private String lotCorpName;

    private String lsType;

    private String lsKind;

    private String aliasName;

    private boolean preferred;

    public LotAliasDTO() {

    }

    public LotAliasDTO(LotAlias lotAlias) {
        this.lotCorpName = lotAlias.getLot().getCorpName();
        this.lsType = lotAlias.getLsType();
        this.lsKind = lotAlias.getLsKind();
        this.aliasName = lotAlias.getAliasName();
        this.preferred = lotAlias.isPreferred();
    }

    public boolean getPreferred() {
        return this.preferred;
    }

    public static LotAliasDTO getLotByAlias(LotAliasDTO lotAliasDTO) {
        LotAlias lotAlias;
        try {
            lotAlias = LotAlias.findLotAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(
                    lotAliasDTO.getAliasName(), lotAliasDTO.getLsType(), lotAliasDTO.getLsKind()).getSingleResult();
        } catch (NoResultException e) {
            lotAlias = null;
        }
        return new LotAliasDTO(lotAlias);
    }

    public static Collection<LotAliasDTO> getLotsByAlias(Collection<LotAliasDTO> lotAliasDTOs) {
        Collection<LotAliasDTO> results = new HashSet<LotAliasDTO>();
        Collection<LotAlias> lotAliases = null;
        for (LotAliasDTO lotAliasDTO : lotAliasDTOs) {
            lotAliases = LotAlias.findLotAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(
                    lotAliasDTO.getAliasName(), lotAliasDTO.getLsType(), lotAliasDTO.getLsKind()).getResultList();
            for (LotAlias alias : lotAliases) {
                results.add(new LotAliasDTO(alias));
            }
        }
        return results;
    }

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

    public static LotAliasDTO fromJsonToLotAliasDTO(String json) {
        return new JSONDeserializer<LotAliasDTO>()
                .use(null, LotAliasDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<LotAliasDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<LotAliasDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<LotAliasDTO> fromJsonArrayToLoes(String json) {
        return new JSONDeserializer<List<LotAliasDTO>>()
                .use("values", LotAliasDTO.class).deserialize(json);
    }

    public String getLotCorpName() {
        return this.lotCorpName;
    }

    public void setLotCorpName(String lotCorpName) {
        this.lotCorpName = lotCorpName;
    }

    public String getLsType() {
        return this.lsType;
    }

    public void setLsType(String lsType) {
        this.lsType = lsType;
    }

    public String getLsKind() {
        return this.lsKind;
    }

    public void setLsKind(String lsKind) {
        this.lsKind = lsKind;
    }

    public String getAliasName() {
        return this.aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public boolean isPreferred() {
        return this.preferred;
    }

    public void setPreferred(boolean preferred) {
        this.preferred = preferred;
    }
}
