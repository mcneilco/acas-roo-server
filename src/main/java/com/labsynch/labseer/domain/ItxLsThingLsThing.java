package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord
public class ItxLsThingLsThing extends AbstractThing {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_ls_thing_id")
    private LsThing firstLsThing;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_ls_thing_id")
    private Container secondLsThing;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itxLsThingLsThing", fetch = FetchType.LAZY)
    private Set<ItxLsThingLsThingState> lsStates = new HashSet<ItxLsThingLsThingState>();

    public ItxLsThingLsThing(com.labsynch.labseer.domain.ItxLsThingLsThing itxLsThingLsThing) {
    	this.setRecordedBy(itxLsThingLsThing.getRecordedBy());
    	this.setRecordedDate(itxLsThingLsThing.getRecordedDate());
    	this.setLsTransaction(itxLsThingLsThing.getLsTransaction());
    	this.setModifiedBy(itxLsThingLsThing.getModifiedBy());
    	this.setModifiedDate(itxLsThingLsThing.getModifiedDate());
    	this.setCodeName(itxLsThingLsThing.getCodeName());
    	this.setLsType(itxLsThingLsThing.getLsType());
    	this.setLsKind(itxLsThingLsThing.getLsKind());
    	this.setLsTypeAndKind(itxLsThingLsThing.getLsTypeAndKind());
        this.firstLsThing = itxLsThingLsThing.getFirstLsThing();
        this.secondLsThing = itxLsThingLsThing.getSecondLsThing();
    }
    
    public static ItxLsThingLsThing update(ItxLsThingLsThing object) {
    	ItxLsThingLsThing updatedObject = new JSONDeserializer<ItxLsThingLsThing>().use(null, ItxLsThingLsThing.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(object.toJson(), 
        				ItxLsThingLsThing.findItxLsThingLsThing(object.getId()));
    	updatedObject.setModifiedDate(new Date());
    	updatedObject.merge();
        return updatedObject;
    }
    
    public String toJson() {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }
    
    public static ItxLsThingLsThing fromJsonToItxLsThingLsThing(String json) {
        return new JSONDeserializer<ItxLsThingLsThing>()
        		.use(null, ItxLsThingLsThing.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
    public static String toJsonArray(Collection<ItxLsThingLsThing> collection) {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }
    
    public static Collection<ItxLsThingLsThing> fromJsonArrayToItxLsThingLsThings(String json) {
        return new JSONDeserializer<List<ItxLsThingLsThing>>().use(null, ArrayList.class)
        		.use("values", ItxLsThingLsThing.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
    public static Collection<ItxLsThingLsThing> fromJsonArrayToItxLsThingLsThings(Reader json) {
        return new JSONDeserializer<List<ItxLsThingLsThing>>().use(null, ArrayList.class)
        		.use("values", ItxLsThingLsThing.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
}
