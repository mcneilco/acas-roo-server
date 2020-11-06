package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class RegSearchDTO {

    private String asDrawnStructure;
    
    private String asDrawnImage;
    
    private double asDrawnMolWeight;
    
    private double asDrawnExactMass;
    
    private String asDrawnMolFormula;

    private Set<ParentDTO> parents = new HashSet<ParentDTO>();

	public String toJson() {
        return new JSONSerializer().include("parents.saltForms.isosalts", "parents.parentAliases").exclude("*.class", "parents.parent", "parents.saltForms.isosalts.saltForm").serialize(this);
//  "parents.saltForms.saltForm.parent"
	}
	
	public static RegSearchDTO fromJsonToRegSearchDTO(String json) {
        return new JSONDeserializer<RegSearchDTO>().use(null, RegSearchDTO.class).deserialize(json);
    }

	public static String toJsonArray(Collection<RegSearchDTO> collection) {
        return new JSONSerializer().include("parents.parentAliases").exclude("*.class", "parents.saltForms.saltForm.parent", "parents.saltForms.isosalts.saltForm").serialize(collection);
    }

	public static Collection<RegSearchDTO> fromJsonArrayToRegSearchDTO(String json) {
        return new JSONDeserializer<List<RegSearchDTO>>().use(null, ArrayList.class).use("values", RegSearchDTO.class).deserialize(json);
    }

	public String getAsDrawnStructure() {
        return this.asDrawnStructure;
    }

	public void setAsDrawnStructure(String asDrawnStructure) {
        this.asDrawnStructure = asDrawnStructure;
    }

	public String getAsDrawnImage() {
        return this.asDrawnImage;
    }

	public void setAsDrawnImage(String asDrawnImage) {
        this.asDrawnImage = asDrawnImage;
    }

	public double getAsDrawnMolWeight() {
        return this.asDrawnMolWeight;
    }

	public void setAsDrawnMolWeight(double asDrawnMolWeight) {
        this.asDrawnMolWeight = asDrawnMolWeight;
    }

	public double getAsDrawnExactMass() {
        return this.asDrawnExactMass;
    }

	public void setAsDrawnExactMass(double asDrawnExactMass) {
        this.asDrawnExactMass = asDrawnExactMass;
    }

	public String getAsDrawnMolFormula() {
        return this.asDrawnMolFormula;
    }

	public void setAsDrawnMolFormula(String asDrawnMolFormula) {
        this.asDrawnMolFormula = asDrawnMolFormula;
    }

	public Set<ParentDTO> getParents() {
        return this.parents;
    }

	public void setParents(Set<ParentDTO> parents) {
        this.parents = parents;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
