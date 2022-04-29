package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.CompoundType;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.ParentAnnotation;
import com.labsynch.labseer.domain.StereoCategory;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class ParentEditDTO{

	private Long id;

	private String corpName;

	private String chemistCode;

	private String modifiedBy;

	private String commonName;

	private String stereoCategoryCode;

	private String stereoComment;

	private String molStructure;
	
	private Double exactMass;

	private String molFormula;

	private int CdId;

	private Double molWeight;

	private Boolean ignore;

	private String parentAnnotationCode;

	private String compoundTypeCode;

	private String comment;

	private Boolean isMixture;

	private String commonNameAliases;

	private String liveDesignAliases;

	private String defaultAliases;

//	private Set<SaltFormDTO> saltForms = new HashSet<SaltFormDTO>();
	
	private Set<ParentAliasDTO> parentAliases = new HashSet<ParentAliasDTO>();

	public void setParent() {

	}
	
	public void setParent(Parent parent) {
		
		this.setChemistCode(parent.getChemist());
		this.setCommonName(parent.getCommonName());
		this.setCorpName(parent.getCorpName());
		this.setIgnore(parent.getIgnore());
		this.setCompoundTypeCode(parent.getCompoundType().getCode());
//		this.setParentAnnotation(parent.getParentAnnotation());
		this.setComment(parent.getComment());
		this.setIsMixture(parent.getIsMixture());
//		//this may be commented out in when we display by id
//		this.setMolStructure(parent.getMolStructure());
//		this.setMolFormula(parent.getMolFormula());
//		this.setCdId(parent.getCdId());
//		this.setMolWeight(parent.getMolWeight());
//		this.setExactMass(parent.getExactMass());
//		this.setStereoCategory(parent.getStereoCategory());
//		this.setStereoComment(parent.getStereoComment());
//		this.setId(parent.getId());
	}


	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
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

	public String getChemistCode() {
        return this.chemistCode;
    }

	public void setChemistCode(String chemistCode) {
        this.chemistCode = chemistCode;
    }

	public String getModifiedBy() {
        return this.modifiedBy;
    }

	public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

	public String getCommonName() {
        return this.commonName;
    }

	public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

	public String getStereoCategoryCode() {
        return this.stereoCategoryCode;
    }

	public void setStereoCategoryCode(String stereoCategoryCode) {
        this.stereoCategoryCode = stereoCategoryCode;
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

	public String getParentAnnotationCode() {
        return this.parentAnnotationCode;
    }

	public void setParentAnnotationCode(String parentAnnotationCode) {
        this.parentAnnotationCode = parentAnnotationCode;
    }

	public String getCompoundTypeCode() {
        return this.compoundTypeCode;
    }

	public void setCompoundTypeCode(String compoundTypeCode) {
        this.compoundTypeCode = compoundTypeCode;
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

	public String getCommonNameAliases() {
        return this.commonNameAliases;
    }

	public void setCommonNameAliases(String commonNameAliases) {
        this.commonNameAliases = commonNameAliases;
    }

	public String getLiveDesignAliases() {
        return this.liveDesignAliases;
    }

	public void setLiveDesignAliases(String liveDesignAliases) {
        this.liveDesignAliases = liveDesignAliases;
    }

	public String getDefaultAliases() {
        return this.defaultAliases;
    }

	public void setDefaultAliases(String defaultAliases) {
        this.defaultAliases = defaultAliases;
    }

	public Set<ParentAliasDTO> getParentAliases() {
        return this.parentAliases;
    }

	public void setParentAliases(Set<ParentAliasDTO> parentAliases) {
        this.parentAliases = parentAliases;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static ParentEditDTO fromJsonToParentEditDTO(String json) {
        return new JSONDeserializer<ParentEditDTO>()
        .use(null, ParentEditDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ParentEditDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ParentEditDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ParentEditDTO> fromJsonArrayToParentEditDTO(String json) {
        return new JSONDeserializer<List<ParentEditDTO>>()
        .use("values", ParentEditDTO.class).deserialize(json);
    }
}
