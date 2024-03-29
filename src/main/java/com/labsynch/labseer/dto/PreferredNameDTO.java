package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import javax.persistence.NoResultException;

import com.labsynch.labseer.domain.Parent;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class PreferredNameDTO {

	public PreferredNameDTO() {
	}

	public PreferredNameDTO(
			String requestName,
			String preferredName,
			String referenceName) {

		this.requestName = requestName;
		this.preferredName = preferredName;
		this.referenceName = referenceName;
	}

	private String requestName;

	private String preferredName;

	private String referenceName;

	public static Collection<PreferredNameDTO> getParentPreferredNames(Collection<PreferredNameDTO> preferredNameDTOs) {
		for (PreferredNameDTO preferredNameDTO : preferredNameDTOs) {
			String preferredName;
			try {
				preferredName = Parent.findParentsByCorpNameEquals(preferredNameDTO.getRequestName()).getSingleResult()
						.getCorpName();
			} catch (NoResultException e) {
				preferredName = "";
			}
			preferredNameDTO.setPreferredName(preferredName);
		}
		return preferredNameDTOs;
	}

	public String toJson() {
		return new JSONSerializer()
				.exclude("*.class").serialize(this);
	}

	public String toJson(String[] fields) {
		return new JSONSerializer()
				.include(fields).exclude("*.class").serialize(this);
	}

	public static PreferredNameDTO fromJsonToPreferredNameDTO(String json) {
		return new JSONDeserializer<PreferredNameDTO>()
				.use(null, PreferredNameDTO.class).deserialize(json);
	}

	public static String toJsonArray(Collection<PreferredNameDTO> collection) {
		return new JSONSerializer()
				.exclude("*.class").serialize(collection);
	}

	public static String toJsonArray(Collection<PreferredNameDTO> collection, String[] fields) {
		return new JSONSerializer()
				.include(fields).exclude("*.class").serialize(collection);
	}

	public static Collection<PreferredNameDTO> fromJsonArrayToPreferredNameDTO(String json) {
		return new JSONDeserializer<List<PreferredNameDTO>>()
				.use("values", PreferredNameDTO.class).deserialize(json);
	}

	public String getRequestName() {
		return this.requestName;
	}

	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}

	public String getPreferredName() {
		return this.preferredName;
	}

	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}

	public String getReferenceName() {
		return this.referenceName;
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
