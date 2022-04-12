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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
public class ItxLsThingLsThingState extends AbstractState {
	
	private static final Logger logger = LoggerFactory.getLogger(ItxLsThingLsThingState.class);


    public ItxLsThingLsThingState(ItxLsThingLsThingState itxState) {
		super.setLsType(itxState.getLsType());
		super.setLsKind(itxState.getLsKind());
		super.setIgnored(itxState.isIgnored());
		super.setDeleted(itxState.isDeleted());
    	super.setRecordedBy(itxState.getRecordedBy());
		super.setRecordedDate(itxState.getRecordedDate());
		super.setLsTransaction(itxState.getLsTransaction());
		super.setModifiedBy(itxState.getModifiedBy());
		super.setModifiedDate(itxState.getModifiedDate());
		
    }
    
    public static ItxLsThingLsThingState update(ItxLsThingLsThingState itxState) {
    	ItxLsThingLsThingState updatedObject = ItxLsThingLsThingState.findItxLsThingLsThingState(itxState.getId());
    	updatedObject.setLsType(itxState.getLsType());
    	updatedObject.setLsKind(itxState.getLsKind());
    	updatedObject.setIgnored(itxState.isIgnored());
    	updatedObject.setDeleted(itxState.isDeleted());
    	updatedObject.setRecordedBy(itxState.getRecordedBy());
    	updatedObject.setRecordedDate(itxState.getRecordedDate());
    	updatedObject.setLsTransaction(itxState.getLsTransaction());
    	updatedObject.setModifiedDate(new Date());
    	updatedObject.merge();
        return updatedObject;
    }
    
//    public static ItxLsThingLsThingState update(ItxLsThingLsThingState object) {
//    	ItxLsThingLsThingState updatedObject = new JSONDeserializer<ItxLsThingLsThingState>().use(null, ItxLsThingLsThingState.class).
//        		deserializeInto(object.toJson(), 
//        				ItxLsThingLsThingState.findItxLsThingLsThingState(object.getId()));
//    	updatedObject.setModifiedDate(new Date());
//    	updatedObject.merge();
//        return updatedObject;
//    }
    
	public static ItxLsThingLsThingState updateNoMerge(
			ItxLsThingLsThingState object) {
		ItxLsThingLsThingState updatedObject = new JSONDeserializer<ItxLsThingLsThingState>().use(null, ItxLsThingLsThingState.class).
        		deserializeInto(object.toJson(), 
        				ItxLsThingLsThingState.findItxLsThingLsThingState(object.getId()));
    	updatedObject.setModifiedDate(new Date());
        return updatedObject;
	}


	@ManyToOne
	@JoinColumn(name = "itx_ls_thing_ls_thing")
    private ItxLsThingLsThing itxLsThingLsThing;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lsState",  fetch =  FetchType.LAZY)
    private Set<ItxLsThingLsThingValue> lsValues = new HashSet<ItxLsThingLsThingValue>();
    
    
	@Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }

	@Transactional
    public String toJsonStub() {
        return new JSONSerializer().exclude("*.class", "itxLsThingLsThing")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }

    public static ItxLsThingLsThingState fromJsonToItxLsThingLsThingState(String json) {
        return new JSONDeserializer<ItxLsThingLsThingState>().use(null, ItxLsThingLsThingState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
	@Transactional
    public static String toJsonArray(Collection<ItxLsThingLsThingState> collection) {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }

	@Transactional
    public static String toJsonArrayStub(Collection<ItxLsThingLsThingState> collection) {
        return new JSONSerializer().exclude("*.class", "itxLsThingLsThing")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }
    
    public static Collection<ItxLsThingLsThingState> fromJsonArrayToItxLsThingLsThingStates(String json) {
        return new JSONDeserializer<List<ItxLsThingLsThingState>>().use(null, ArrayList.class)
        		.use("values", ItxLsThingLsThingState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }

    public static Collection<ItxLsThingLsThingState> fromJsonArrayToItxLsThingLsThingStates(Reader json) {
        return new JSONDeserializer<List<ItxLsThingLsThingState>>().use(null, ArrayList.class)
        		.use("values", ItxLsThingLsThingState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }



}
