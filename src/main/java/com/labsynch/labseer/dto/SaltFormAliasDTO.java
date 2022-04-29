package com.labsynch.labseer.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.labsynch.labseer.domain.ParentAlias;
import com.labsynch.labseer.domain.SaltFormAlias;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;

public class SaltFormAliasDTO {

    private String saltFormCorpName;

    private String lsType;

    private String lsKind;

    private String aliasName;

    private boolean preferred;

    public SaltFormAliasDTO() {

    }

    public SaltFormAliasDTO(SaltFormAlias saltFormAlias) {
        this.saltFormCorpName = saltFormAlias.getSaltForm().getCorpName();
        this.lsType = saltFormAlias.getLsType();
        this.lsKind = saltFormAlias.getLsKind();
        this.aliasName = saltFormAlias.getAliasName();
        this.preferred = saltFormAlias.isPreferred();
    }

    public boolean getPreferred() {
        return this.preferred;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String getSaltFormCorpName() {
        return this.saltFormCorpName;
    }

    public void setSaltFormCorpName(String saltFormCorpName) {
        this.saltFormCorpName = saltFormCorpName;
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

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static SaltFormAliasDTO fromJsonToSaltFormAliasDTO(String json) {
        return new JSONDeserializer<SaltFormAliasDTO>()
                .use(null, SaltFormAliasDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<SaltFormAliasDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<SaltFormAliasDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<SaltFormAliasDTO> fromJsonArrayToSaltFoes(String json) {
        return new JSONDeserializer<List<SaltFormAliasDTO>>()
                .use("values", SaltFormAliasDTO.class).deserialize(json);
    }
}
