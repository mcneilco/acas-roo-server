package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
}
