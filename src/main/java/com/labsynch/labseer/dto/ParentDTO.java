package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;

import com.labsynch.labseer.domain.CompoundType;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.ParentAnnotation;
import com.labsynch.labseer.domain.StereoCategory;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.service.ChemStructureService;
import com.labsynch.labseer.utils.MoleculeUtil;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class ParentDTO {

    private Long id;

    private String corpName;

    private String chemist;

    private String commonName;

    private StereoCategory stereoCategory;

    private String stereoComment;

    private String molStructure;

    private Double exactMass;

    private String molFormula;

    private int CdId;

    private Double molWeight;

    private Boolean ignore;

    private ParentAnnotation parentAnnotation;

    private CompoundType compoundType;

    private String comment;

    private Boolean isMixture;

    private Set<SaltFormDTO> saltForms = new HashSet<SaltFormDTO>();

    private Set<ParentAliasDTO> parentAliases = new HashSet<ParentAliasDTO>();

    public void setParent(Parent parent) {

        this.setChemist(parent.getChemist());
        this.setCommonName(parent.getCommonName());
        this.setCorpName(parent.getCorpName());
        this.setIgnore(parent.getIgnore());
        this.setCompoundType(parent.getCompoundType());
        this.setParentAnnotation(parent.getParentAnnotation());
        this.setComment(parent.getComment());
        this.setIsMixture(parent.getIsMixture());
        // this may be commented out in when we display by id
        this.setMolStructure(parent.getMolStructure());
        try {
            this.setMolFormula(MoleculeUtil.getMolFormula(parent.getMolStructure()));
        } catch (CmpdRegMolFormatException e) {
            // leave mol formula blank
        }
        this.setCdId(parent.getCdId());
        this.setMolWeight(parent.getMolWeight());
        this.setExactMass(parent.getExactMass());
        this.setStereoCategory(parent.getStereoCategory());
        this.setStereoComment(parent.getStereoComment());
        this.setId(parent.getId());
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

    public static ParentDTO fromJsonToParentDTO(String json) {
        return new JSONDeserializer<ParentDTO>()
                .use(null, ParentDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<ParentDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<ParentDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<ParentDTO> fromJsonArrayToParentDTO(String json) {
        return new JSONDeserializer<List<ParentDTO>>()
                .use("values", ParentDTO.class).deserialize(json);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCorpName() {
        return this.corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getChemist() {
        return this.chemist;
    }

    public void setChemist(String chemist) {
        this.chemist = chemist;
    }

    public String getCommonName() {
        return this.commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public StereoCategory getStereoCategory() {
        return this.stereoCategory;
    }

    public void setStereoCategory(StereoCategory stereoCategory) {
        this.stereoCategory = stereoCategory;
    }

    public String getStereoComment() {
        return this.stereoComment;
    }

    public void setStereoComment(String stereoComment) {
        this.stereoComment = stereoComment;
    }

    public String getMolStructure() {
        return this.molStructure;
    }

    public void setMolStructure(String molStructure) {
        this.molStructure = molStructure;
    }

    public Double getExactMass() {
        return this.exactMass;
    }

    public void setExactMass(Double exactMass) {
        this.exactMass = exactMass;
    }

    public String getMolFormula() {
        return this.molFormula;
    }

    public void setMolFormula(String molFormula) {
        this.molFormula = molFormula;
    }

    public int getCdId() {
        return this.CdId;
    }

    public void setCdId(int CdId) {
        this.CdId = CdId;
    }

    public Double getMolWeight() {
        return this.molWeight;
    }

    public void setMolWeight(Double molWeight) {
        this.molWeight = molWeight;
    }

    public Boolean getIgnore() {
        return this.ignore;
    }

    public void setIgnore(Boolean ignore) {
        this.ignore = ignore;
    }

    public ParentAnnotation getParentAnnotation() {
        return this.parentAnnotation;
    }

    public void setParentAnnotation(ParentAnnotation parentAnnotation) {
        this.parentAnnotation = parentAnnotation;
    }

    public CompoundType getCompoundType() {
        return this.compoundType;
    }

    public void setCompoundType(CompoundType compoundType) {
        this.compoundType = compoundType;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getIsMixture() {
        return this.isMixture;
    }

    public void setIsMixture(Boolean isMixture) {
        this.isMixture = isMixture;
    }

    public Set<SaltFormDTO> getSaltForms() {
        return this.saltForms;
    }

    public void setSaltForms(Set<SaltFormDTO> saltForms) {
        this.saltForms = saltForms;
    }

    public Set<ParentAliasDTO> getParentAliases() {
        return this.parentAliases;
    }

    public void setParentAliases(Set<ParentAliasDTO> parentAliases) {
        this.parentAliases = parentAliases;
    }
}
