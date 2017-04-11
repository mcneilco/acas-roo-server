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
public class ItxProtocolProtocolState extends AbstractState {

    public ItxProtocolProtocolState(ItxProtocolProtocolState itxState) {
		this.setRecordedBy(itxState.getRecordedBy());
		this.setRecordedDate(itxState.getRecordedDate());
		this.setLsTransaction(itxState.getLsTransaction());
		this.setModifiedBy(itxState.getModifiedBy());
		this.setModifiedDate(itxState.getModifiedDate());
		
    }
    
    public static ItxProtocolProtocolState update(ItxProtocolProtocolState object) {
    	ItxProtocolProtocolState updatedObject = new JSONDeserializer<ItxProtocolProtocolState>().use(null, ItxProtocolProtocolState.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(object.toJson(), 
        				ItxProtocolProtocolState.findItxProtocolProtocolState(object.getId()));
    	updatedObject.setModifiedDate(new Date());
    	updatedObject.merge();
        return updatedObject;
    }

	@ManyToOne
    private ItxProtocolProtocol itxProtocolProtocol;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lsState",  fetch =  FetchType.LAZY)
    private Set<ItxProtocolProtocolValue> lsValues = new HashSet<ItxProtocolProtocolValue>();
    
    
	@Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }

	@Transactional
    public String toJsonStub() {
        return new JSONSerializer().exclude("*.class", "itxProtocolProtocol")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }

    public static ItxProtocolProtocolState fromJsonToItxContainerContainerState(String json) {
        return new JSONDeserializer<ItxProtocolProtocolState>().use(null, ItxProtocolProtocolState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
	@Transactional
    public static String toJsonArray(Collection<ItxProtocolProtocolState> collection) {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }

	@Transactional
    public static String toJsonArrayStub(Collection<ItxProtocolProtocolState> collection) {
        return new JSONSerializer().exclude("*.class", "itxProtocolProtocol")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }
    
    public static Collection<ItxProtocolProtocolState> fromJsonArrayToItxProtocolProtocolStates(String json) {
        return new JSONDeserializer<List<ItxProtocolProtocolState>>().use(null, ArrayList.class)
        		.use("values", ItxProtocolProtocolState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }

    public static Collection<ItxProtocolProtocolState> fromJsonArrayToItxProtocolProtocolStates(Reader json) {
        return new JSONDeserializer<List<ItxProtocolProtocolState>>().use(null, ArrayList.class)
        		.use("values", ItxProtocolProtocolState.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }


}
