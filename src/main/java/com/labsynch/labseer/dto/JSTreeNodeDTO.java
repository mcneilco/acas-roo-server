package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.labsynch.labseer.domain.LsTag;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class JSTreeNodeDTO {

    private String id; // use codeName
    private String parent; // ref node parent id -- use # to set as root
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
        return new JSONSerializer().prettyPrint(pretty).include("lsTags").exclude("*.class").serialize(collection);
    }

    public static Collection<JSTreeNodeDTO> fromJsonArrayToJSTreeNoes(String json) {
        return new JSONDeserializer<List<JSTreeNodeDTO>>().use(null, ArrayList.class).use("values", JSTreeNodeDTO.class)
                .deserialize(json);
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
                append(id).append(parent).toHashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (!(obj instanceof JSTreeNodeDTO))
            return false;

        JSTreeNodeDTO rhs = (JSTreeNodeDTO) obj;
        return new EqualsBuilder().append(id, rhs.id).append(parent, rhs.parent).isEquals();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return this.parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<LsTag> getLsTags() {
        return this.lsTags;
    }

    public void setLsTags(Set<LsTag> lsTags) {
        this.lsTags = lsTags;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
