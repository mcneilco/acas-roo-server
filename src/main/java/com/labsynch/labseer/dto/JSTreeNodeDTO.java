package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.domain.LsTag;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
public class JSTreeNodeDTO {
	
	private String id; // use codeName
	private String parent; //ref node parent id -- use # to set as root
	private String text; // node text
	private String description; // node text
	private Set<LsTag> lsTags = new HashSet<LsTag>();


	public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

	public static JSTreeNodeDTO fromJsonToJSTreeNodeDTO(String json) {
        return new JSONDeserializer<JSTreeNodeDTO>().use(null, JSTreeNodeDTO.class).deserialize(json);
    }


	public static String toJsonArray(Collection<JSTreeNodeDTO> collection) {
        return toJsonArray(collection, false);
    }
	
	public static String toPrettyJsonArray(Collection<JSTreeNodeDTO> collection) {
        return toJsonArray(collection, true);
    }

	public static String toJsonArray(Collection<JSTreeNodeDTO> collection, boolean pretty) {
        return new JSONSerializer().
        		prettyPrint(pretty).
        		include("lsTags").
        		exclude("*.class").
        		serialize(collection);
    }

	public static Collection<JSTreeNodeDTO> fromJsonArrayToJSTreeNoes(String json) {
        return new JSONDeserializer<List<JSTreeNodeDTO>>().use(null, ArrayList.class).use("values", JSTreeNodeDTO.class).deserialize(json);
    }
	
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            append(id).
            append(parent).
            toHashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof JSTreeNodeDTO))
            return false;

        JSTreeNodeDTO rhs = (JSTreeNodeDTO) obj;
        return new EqualsBuilder().
            append(id, rhs.id).
            append(parent, rhs.parent).
            isEquals();
    }
}


