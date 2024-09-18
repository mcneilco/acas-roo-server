package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;

public class SearchFormReturnDTO {

	private Collection<SearchCompoundReturnDTO> foundCompounds = new ArrayList<SearchCompoundReturnDTO>();

	private boolean lotsWithheld;

	public String toJson() {
		return new JSONSerializer().exclude("*.class")
				.include("foundCompounds", "foundCompounds.lotIDs", "foundCompounds.parentAliases")
				.transform(new DateTransformer("MM/dd/yyyy"), Date.class)
				.serialize(this);
	}

	public static SearchFormReturnDTO fromJsonToSearchFormReturnDTO(String json) {
		return new JSONDeserializer<SearchFormReturnDTO>().use(null, SearchFormReturnDTO.class)
				.use(Date.class, new DateTransformer("MM/dd/yyyy"))
				.deserialize(json);
	}

	public static String toJsonArray(Collection<SearchFormReturnDTO> collection) {
		return new JSONSerializer().exclude("*.class")
				.include("foundCompounds", "foundCompounds.lotIDs", "foundCompounds.parentAliases")
				.transform(new DateTransformer("MM/dd/yyyy"), Date.class)
				.serialize(collection);
	}

	public static Collection<SearchFormReturnDTO> fromJsonArrayToSearchFoes(String json) {
		return new JSONDeserializer<List<SearchFormReturnDTO>>().use(null, ArrayList.class)
				.use("values", SearchFormReturnDTO.class)
				.use(Date.class, new DateTransformer("MM/dd/yyyy"))
				.deserialize(json);
	}

	public Collection<SearchCompoundReturnDTO> getFoundCompounds() {
		return this.foundCompounds;
	}

	public void setFoundCompounds(Collection<SearchCompoundReturnDTO> foundCompounds) {
		this.foundCompounds = foundCompounds;
	}

	public boolean isLotsWithheld() {
		return this.lotsWithheld;
	}

	public void setLotsWithheld(boolean lotsWithheld) {
		this.lotsWithheld = lotsWithheld;
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
