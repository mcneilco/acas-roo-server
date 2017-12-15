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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
public class ItxContainerContainerState extends AbstractState {

    public ItxContainerContainerState(ItxContainerContainerState itxState) {
    		super.setLsType(itxState.getLsType());
    		super.setLsKind(itxState.getLsKind());
		super.setRecordedBy(itxState.getRecordedBy());
		super.setRecordedDate(itxState.getRecordedDate());
		super.setLsTransaction(itxState.getLsTransaction());
		super.setModifiedBy(itxState.getModifiedBy());
		super.setModifiedDate(itxState.getModifiedDate());
		
    }
    
    public static ItxContainerContainerState update(ItxContainerContainerState object) {
    	ItxContainerContainerState updatedObject = new JSONDeserializer<ItxContainerContainerState>().use(null, ItxContainerContainerState.class).
        		deserializeInto(object.toJson(), 
        				ItxContainerContainerState.findItxContainerContainerState(object.getId()));
    	updatedObject.setModifiedDate(new Date());
    	updatedObject.merge();
        return updatedObject;
    }

	@ManyToOne
    private ItxContainerContainer itxContainerContainer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lsState",  fetch =  FetchType.LAZY)
    private Set<ItxContainerContainerValue> lsValues = new HashSet<ItxContainerContainerValue>();
    
    
	@Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }

	@Transactional
    public String toJsonStub() {
        return new JSONSerializer().exclude("*.class", "itxContainerContainer")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }

    public static ItxContainerContainerState fromJsonToItxContainerContainerState(String json) {
        return new JSONDeserializer<ItxContainerContainerState>().use(null, ItxContainerContainerState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
	@Transactional
    public static String toJsonArray(Collection<ItxContainerContainerState> collection) {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }

	@Transactional
    public static String toJsonArrayStub(Collection<ItxContainerContainerState> collection) {
        return new JSONSerializer().exclude("*.class", "itxContainerContainer")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }
    
    public static Collection<ItxContainerContainerState> fromJsonArrayToItxContainerContainerStates(String json) {
        return new JSONDeserializer<List<ItxContainerContainerState>>().use(null, ArrayList.class)
        		.use("values", ItxContainerContainerState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }

    public static Collection<ItxContainerContainerState> fromJsonArrayToItxContainerContainerStates(Reader json) {
        return new JSONDeserializer<List<ItxContainerContainerState>>().use(null, ArrayList.class)
        		.use("values", ItxContainerContainerState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }


}
