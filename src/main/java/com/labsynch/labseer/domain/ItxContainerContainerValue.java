package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.ManyToOne;

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
public class ItxContainerContainerValue extends AbstractValue {
		
    @ManyToOne
    private ItxContainerContainerState lsState;
    
    public ItxContainerContainerValue(
			ItxContainerContainerValue itxContainerContainerValue) {
    	super.setBlobValue(itxContainerContainerValue.getBlobValue());
        super.setClobValue(itxContainerContainerValue.getClobValue());
        super.setCodeKind(itxContainerContainerValue.getCodeKind());
        super.setCodeOrigin(itxContainerContainerValue.getCodeOrigin());
        super.setCodeType(itxContainerContainerValue.getCodeType());
        super.setCodeTypeAndKind(itxContainerContainerValue.getCodeTypeAndKind());
        super.setCodeValue(itxContainerContainerValue.getCodeValue());
        super.setComments(itxContainerContainerValue.getComments());
        super.setConcentration(itxContainerContainerValue.getConcentration());
        super.setConcUnit(itxContainerContainerValue.getConcUnit());
        super.setDateValue(itxContainerContainerValue.getDateValue());
        super.setDeleted(itxContainerContainerValue.isDeleted());
        super.setFileValue(itxContainerContainerValue.getFileValue());
        super.setIgnored(itxContainerContainerValue.isIgnored());
        super.setLsKind(itxContainerContainerValue.getLsKind());
        super.setLsTransaction(itxContainerContainerValue.getLsTransaction());
        super.setLsType(itxContainerContainerValue.getLsType());
        super.setLsTypeAndKind(itxContainerContainerValue.getLsTypeAndKind());
        super.setModifiedBy(itxContainerContainerValue.getModifiedBy());
        super.setModifiedDate(itxContainerContainerValue.getModifiedDate());
        super.setNumberOfReplicates(itxContainerContainerValue.getNumberOfReplicates());
        super.setNumericValue(itxContainerContainerValue.getNumericValue());
        super.setOperatorKind(itxContainerContainerValue.getOperatorKind());
        super.setOperatorType(itxContainerContainerValue.getOperatorType());
        super.setOperatorTypeAndKind(itxContainerContainerValue.getOperatorTypeAndKind());
        super.setPublicData(itxContainerContainerValue.isPublicData());
        super.setRecordedBy(itxContainerContainerValue.getRecordedBy());
        super.setRecordedDate(itxContainerContainerValue.getRecordedDate());
        super.setSigFigs(itxContainerContainerValue.getSigFigs());
        super.setStringValue(itxContainerContainerValue.getStringValue());
        super.setUncertainty(itxContainerContainerValue.getUncertainty());
        super.setUncertaintyType(itxContainerContainerValue.getUncertaintyType());
        super.setUnitKind(itxContainerContainerValue.getUnitKind());
        super.setUnitType(itxContainerContainerValue.getUnitType());
        super.setUnitTypeAndKind(itxContainerContainerValue.getUnitTypeAndKind());
        super.setUrlValue(itxContainerContainerValue.getUrlValue());
        super.setVersion(itxContainerContainerValue.getVersion());
	}


	public ItxContainerContainerValue() {
		// TODO Auto-generated constructor stub
	}


	public static ItxContainerContainerValue update(ItxContainerContainerValue object) {
    	ItxContainerContainerValue updatedObject = new JSONDeserializer<ItxContainerContainerValue>().use(null, ItxContainerContainerValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(object.toJson(), 
        				ItxContainerContainerValue.findItxContainerContainerValue(object.getId()));
    	updatedObject.setModifiedDate(new Date());
    	updatedObject.merge();
        return updatedObject;
    }
    
    
    public static ItxContainerContainerValue fromJsonToItxContainerContainerValue(String json) {
        return new JSONDeserializer<ItxContainerContainerValue>().
        		use(null, ItxContainerContainerValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
    
    public static Collection<ItxContainerContainerValue> fromJsonArrayToItxContainerContainerValues(String json) {
        return new JSONDeserializer<List<ItxContainerContainerValue>>().
        		use(null, ArrayList.class).
        		use("values", ItxContainerContainerValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
    
    public static Collection<ItxContainerContainerValue> fromJsonArrayToItxContainerContainerValues(Reader json) {
        return new JSONDeserializer<List<ItxContainerContainerValue>>().
        		use(null, ArrayList.class).
        		use("values", ItxContainerContainerValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
    
	@Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }
    
	@Transactional
    public static String toJsonArray(Collection<ItxContainerContainerValue> collection) {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }

	@Transactional
    public static String toJsonArrayStub(Collection<ItxContainerContainerValue> collection) {
        return new JSONSerializer().exclude("*.class", "lsState")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }
}
