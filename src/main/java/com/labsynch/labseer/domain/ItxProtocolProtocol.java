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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RooJpaActiveRecord(finders = { "findItxProtocolProtocolsByLsTransactionEquals", "findItxProtocolProtocolsByFirstProtocol" , "findItxProtocolProtocolsBySecondProtocol"})
public class ItxProtocolProtocol extends AbstractThing {
	
	private static final Logger logger = LoggerFactory.getLogger(ItxProtocolProtocol.class);

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "first_protocol_id")
    private Protocol firstProtocol;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "second_protocol_id")
    private Protocol secondProtocol;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itxProtocolProtocol", fetch = FetchType.LAZY)
    private Set<ItxProtocolProtocolState> lsStates = new HashSet<ItxProtocolProtocolState>();

    public ItxProtocolProtocol(com.labsynch.labseer.domain.ItxProtocolProtocol itxProtocol) {
    	this.setRecordedBy(itxProtocol.getRecordedBy());
    	this.setRecordedDate(itxProtocol.getRecordedDate());
    	this.setLsTransaction(itxProtocol.getLsTransaction());
    	this.setModifiedBy(itxProtocol.getModifiedBy());
    	this.setModifiedDate(itxProtocol.getModifiedDate());
    	this.setCodeName(itxProtocol.getCodeName());
    	this.setLsType(itxProtocol.getLsType());
    	this.setLsKind(itxProtocol.getLsKind());
    	this.setLsTypeAndKind(itxProtocol.getLsTypeAndKind());
        this.firstProtocol = itxProtocol.getFirstProtocol();
        this.secondProtocol = itxProtocol.getSecondProtocol();
    }
    
    public static ItxProtocolProtocol update(ItxProtocolProtocol object) {
    	ItxProtocolProtocol updatedObject = new JSONDeserializer<ItxProtocolProtocol>().use(null, ItxProtocolProtocol.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(object.toJson(), 
        				ItxProtocolProtocol.findItxProtocolProtocol(object.getId()));
    	updatedObject.setModifiedDate(new Date());
    	updatedObject.merge();
        return updatedObject;
    }
    
    public String toJson() {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }
    
    public static ItxProtocolProtocol fromJsonToItxProtocolProtocol(String json) {
        return new JSONDeserializer<ItxProtocolProtocol>()
        		.use(null, ItxProtocolProtocol.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
    public static String toJsonArray(Collection<ItxProtocolProtocol> collection) {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }
    
    public static Collection<ItxProtocolProtocol> fromJsonArrayToItxProtocolProtocols(String json) {
        return new JSONDeserializer<List<ItxProtocolProtocol>>().use(null, ArrayList.class)
        		.use("values", ItxProtocolProtocol.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
    public static Collection<ItxProtocolProtocol> fromJsonArrayToItxProtocolProtocols(Reader json) {
        return new JSONDeserializer<List<ItxProtocolProtocol>>().use(null, ArrayList.class)
        		.use("values", ItxProtocolProtocol.class)
        		.use(BigDecimal.class, new CustomBigDecimalFactory())
        		.deserialize(json);
    }
    
    public static ItxProtocolProtocol updateNoStates(ItxProtocolProtocol itxProtocolProtocol) {
    	ItxProtocolProtocol updatedItxProtocolProtocol = ItxProtocolProtocol.findItxProtocolProtocol(itxProtocolProtocol.getId());
    	updatedItxProtocolProtocol.setRecordedBy(itxProtocolProtocol.getRecordedBy());
    	updatedItxProtocolProtocol.setRecordedDate(itxProtocolProtocol.getRecordedDate());
    	updatedItxProtocolProtocol.setIgnored(itxProtocolProtocol.isIgnored());
    	updatedItxProtocolProtocol.setDeleted(itxProtocolProtocol.isDeleted());
    	updatedItxProtocolProtocol.setLsTransaction(itxProtocolProtocol.getLsTransaction());
    	updatedItxProtocolProtocol.setModifiedBy(itxProtocolProtocol.getModifiedBy());
    	updatedItxProtocolProtocol.setCodeName(itxProtocolProtocol.getCodeName());
    	updatedItxProtocolProtocol.setLsType(itxProtocolProtocol.getLsType());
    	updatedItxProtocolProtocol.setLsKind(itxProtocolProtocol.getLsKind());
    	updatedItxProtocolProtocol.setLsTypeAndKind(itxProtocolProtocol.getLsTypeAndKind());
    	updatedItxProtocolProtocol.firstProtocol = Protocol.findProtocol(itxProtocolProtocol.getFirstProtocol().getId());
    	updatedItxProtocolProtocol.secondProtocol = Protocol.findProtocol(itxProtocolProtocol.getSecondProtocol().getId());    	
    	updatedItxProtocolProtocol.setModifiedDate(new Date());
    	updatedItxProtocolProtocol.merge();
    	
    	logger.debug("------------ Just updated the itxProtocolProtocol: ");
    	if(logger.isDebugEnabled()) logger.debug(updatedItxProtocolProtocol.toJson());
    	
        return updatedItxProtocolProtocol;
    }
}
