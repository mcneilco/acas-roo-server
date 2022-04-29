package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;


public class ContainerLocationTreeDTO {
	
	private String codeName;
	private String parentCodeName;
	private String labelText;
	private String codeTree;
	private String labelTree;
	private Integer level;
	private String rootCodeName;
	private String codeNameBreadcrumb;
	private String labelTextBreadcrumb;
	private String lsType;
	private String lsKind;
	
	public ContainerLocationTreeDTO (String codeName, String parentCodeName, String labelText, String codeTree, String labelTree, 
			Integer level, String rootCodeName, String codeNameBreadcrumb, String labelTextBreadcrumb, String lsType, String lsKind) {
		this.codeName = codeName;
		this.parentCodeName = parentCodeName;
		this.labelText = labelText;
		this.codeTree = codeTree;
		this.labelTree = labelTree;
		this.level =  level;
		this.rootCodeName = rootCodeName;
		this.codeNameBreadcrumb = codeNameBreadcrumb;
		this.labelTextBreadcrumb = labelTextBreadcrumb;
		this.lsType = lsType;
		this.lsKind = lsKind;
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

	public static ContainerLocationTreeDTO fromJsonToContainerLocationTreeDTO(String json) {
        return new JSONDeserializer<ContainerLocationTreeDTO>()
        .use(null, ContainerLocationTreeDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ContainerLocationTreeDTO> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ContainerLocationTreeDTO> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ContainerLocationTreeDTO> fromJsonArrayToContainerLocatioes(String json) {
        return new JSONDeserializer<List<ContainerLocationTreeDTO>>()
        .use("values", ContainerLocationTreeDTO.class).deserialize(json);
    }

	public String getCodeName() {
        return this.codeName;
    }

	public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

	public String getParentCodeName() {
        return this.parentCodeName;
    }

	public void setParentCodeName(String parentCodeName) {
        this.parentCodeName = parentCodeName;
    }

	public String getLabelText() {
        return this.labelText;
    }

	public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

	public String getCodeTree() {
        return this.codeTree;
    }

	public void setCodeTree(String codeTree) {
        this.codeTree = codeTree;
    }

	public String getLabelTree() {
        return this.labelTree;
    }

	public void setLabelTree(String labelTree) {
        this.labelTree = labelTree;
    }

	public Integer getLevel() {
        return this.level;
    }

	public void setLevel(Integer level) {
        this.level = level;
    }

	public String getRootCodeName() {
        return this.rootCodeName;
    }

	public void setRootCodeName(String rootCodeName) {
        this.rootCodeName = rootCodeName;
    }

	public String getCodeNameBreadcrumb() {
        return this.codeNameBreadcrumb;
    }

	public void setCodeNameBreadcrumb(String codeNameBreadcrumb) {
        this.codeNameBreadcrumb = codeNameBreadcrumb;
    }

	public String getLabelTextBreadcrumb() {
        return this.labelTextBreadcrumb;
    }

	public void setLabelTextBreadcrumb(String labelTextBreadcrumb) {
        this.labelTextBreadcrumb = labelTextBreadcrumb;
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
}
