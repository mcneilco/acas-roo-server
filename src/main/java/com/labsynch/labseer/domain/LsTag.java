package com.labsynch.labseer.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString(excludeFields = { "experiments", "protocols", "recordedDate", "version" })
@RooJson
@RooJpaActiveRecord(sequenceName = "LS_TAG_PKSEQ", finders = { "findLsTagsByTagTextEquals", "findLsTagsByTagTextLike" })
public class LsTag {

    @Size(max = 255)
    private String tagText;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Date recordedDate;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "lsTags")
    private Set<Experiment> experiments = new HashSet<Experiment>();

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "lsTags")
    private Set<Protocol> protocols = new HashSet<Protocol>();

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "lsTags")
    private Set<LsThing> lsThings = new HashSet<LsThing>();
    
    public LsTag(com.labsynch.labseer.domain.LsTag lsTag) {
        this.tagText = lsTag.getTagText();
        if (lsTag.getRecordedDate() == null) {
            this.recordedDate = new Date();
        } else {
            this.recordedDate = lsTag.getRecordedDate();
        }
    }

    public LsTag() {
    }

    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

    public static com.labsynch.labseer.domain.LsTag fromJsonToLsTag(String json) {
        return new JSONDeserializer<LsTag>().use(null, LsTag.class).deserialize(json);
    }

    public static String toJsonArray(Collection<com.labsynch.labseer.domain.LsTag> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

    public static Collection<com.labsynch.labseer.domain.LsTag> fromJsonArrayToLsTags(String json) {
        return new JSONDeserializer<List<LsTag>>().use(null, ArrayList.class).use("values", LsTag.class).deserialize(json);
    }
}
