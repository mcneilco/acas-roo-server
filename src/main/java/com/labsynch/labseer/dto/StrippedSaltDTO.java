package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.domain.Salt;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class StrippedSaltDTO {
		
	private Map<Salt, Integer> saltCounts;
	
	private Set< ? extends CmpdRegMolecule> unidentifiedFragments;
	
	public StrippedSaltDTO(){
		
	}
	
	public String toJson() {
        return new JSONSerializer()
        .include("lotCodes")
        .exclude("*.class").serialize(this);
    }
	
	public static String toJsonArray(Collection<StrippedSaltDTO> collection) {
        return new JSONSerializer()
        .include("lotCodes")
        .exclude("*.class").serialize(collection);
    }

}
