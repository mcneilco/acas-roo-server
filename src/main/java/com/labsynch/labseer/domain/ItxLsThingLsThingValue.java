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
public class ItxLsThingLsThingValue extends AbstractValue {
		
    @ManyToOne
    private ItxLsThingLsThingState lsState;
    
    public ItxLsThingLsThingValue(ItxLsThingLsThingValue itxLsThingLsThingValue) {
    	super.setBlobValue(itxLsThingLsThingValue.getBlobValue());
        super.setClobValue(itxLsThingLsThingValue.getClobValue());
        super.setCodeKind(itxLsThingLsThingValue.getCodeKind());
        super.setCodeOrigin(itxLsThingLsThingValue.getCodeOrigin());
        super.setCodeType(itxLsThingLsThingValue.getCodeType());
        super.setCodeTypeAndKind(itxLsThingLsThingValue.getCodeTypeAndKind());
        super.setCodeValue(itxLsThingLsThingValue.getCodeValue());
        super.setComments(itxLsThingLsThingValue.getComments());
        super.setConcentration(itxLsThingLsThingValue.getConcentration());
        super.setConcUnit(itxLsThingLsThingValue.getConcUnit());
        super.setDateValue(itxLsThingLsThingValue.getDateValue());
        super.setDeleted(itxLsThingLsThingValue.isDeleted());
        super.setFileValue(itxLsThingLsThingValue.getFileValue());
        super.setIgnored(itxLsThingLsThingValue.isIgnored());
        super.setLsKind(itxLsThingLsThingValue.getLsKind());
        super.setLsTransaction(itxLsThingLsThingValue.getLsTransaction());
        super.setLsType(itxLsThingLsThingValue.getLsType());
        super.setLsTypeAndKind(itxLsThingLsThingValue.getLsTypeAndKind());
        super.setModifiedBy(itxLsThingLsThingValue.getModifiedBy());
        super.setModifiedDate(itxLsThingLsThingValue.getModifiedDate());
        super.setNumberOfReplicates(itxLsThingLsThingValue.getNumberOfReplicates());
        super.setNumericValue(itxLsThingLsThingValue.getNumericValue());
        super.setOperatorKind(itxLsThingLsThingValue.getOperatorKind());
        super.setOperatorType(itxLsThingLsThingValue.getOperatorType());
        super.setOperatorTypeAndKind(itxLsThingLsThingValue.getOperatorTypeAndKind());
        super.setPublicData(itxLsThingLsThingValue.isPublicData());
        super.setRecordedBy(itxLsThingLsThingValue.getRecordedBy());
        super.setRecordedDate(itxLsThingLsThingValue.getRecordedDate());
        super.setSigFigs(itxLsThingLsThingValue.getSigFigs());
        super.setStringValue(itxLsThingLsThingValue.getStringValue());
        super.setUncertainty(itxLsThingLsThingValue.getUncertainty());
        super.setUncertaintyType(itxLsThingLsThingValue.getUncertaintyType());
        super.setUnitKind(itxLsThingLsThingValue.getUnitKind());
        super.setUnitType(itxLsThingLsThingValue.getUnitType());
        super.setUnitTypeAndKind(itxLsThingLsThingValue.getUnitTypeAndKind());
        super.setUrlValue(itxLsThingLsThingValue.getUrlValue());
        super.setVersion(itxLsThingLsThingValue.getVersion());
	}
    
    public ItxLsThingLsThingValue() {
    }


	public static ItxLsThingLsThingValue update(ItxLsThingLsThingValue object) {
    	ItxLsThingLsThingValue updatedObject = new JSONDeserializer<ItxLsThingLsThingValue>().use(null, ItxLsThingLsThingValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(object.toJson(), 
        				ItxLsThingLsThingValue.findItxLsThingLsThingValue(object.getId()));
    	updatedObject.setModifiedDate(new Date());
    	updatedObject.merge();
        return updatedObject;
    }
    
	public static ItxLsThingLsThingValue updateNoMerge(
			ItxLsThingLsThingValue object) {
		ItxLsThingLsThingValue updatedObject = new JSONDeserializer<ItxLsThingLsThingValue>().use(null, ItxLsThingLsThingValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(object.toJson(), 
        				ItxLsThingLsThingValue.findItxLsThingLsThingValue(object.getId()));
    	updatedObject.setModifiedDate(new Date());
        return updatedObject;
	}
    
    public static ItxLsThingLsThingValue fromJsonToItxLsThingLsThingValue(String json) {
        return new JSONDeserializer<ItxLsThingLsThingValue>().
        		use(null, ItxLsThingLsThingValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
    
    public static Collection<ItxLsThingLsThingValue> fromJsonArrayToItxLsThingLsThingValues(String json) {
        return new JSONDeserializer<List<ItxLsThingLsThingValue>>().
        		use(null, ArrayList.class).
        		use("values", ItxLsThingLsThingValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
    
    public static Collection<ItxLsThingLsThingValue> fromJsonArrayToItxLsThingLsThingValues(Reader json) {
        return new JSONDeserializer<List<ItxLsThingLsThingValue>>().
        		use(null, ArrayList.class).
        		use("values", ItxLsThingLsThingValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
    
	@Transactional
    public String toJson() {
        return new JSONSerializer()
        		.exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(this);
    }
    
	@Transactional
    public static String toJsonArray(Collection<ItxLsThingLsThingValue> collection) {
        return new JSONSerializer().exclude("*.class")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }

	@Transactional
    public static String toJsonArrayStub(Collection<ItxLsThingLsThingValue> collection) {
        return new JSONSerializer().exclude("*.class", "lsState")
            	.transform(new ExcludeNulls(), void.class)
        		.serialize(collection);
    }
	
	public static com.labsynch.labseer.domain.ItxLsThingLsThingValue create(com.labsynch.labseer.domain.ItxLsThingLsThingValue lsThingValue) {
        ItxLsThingLsThingValue newItxLsThingLsThingValue = new JSONDeserializer<ItxLsThingLsThingValue>().use(null, ItxLsThingLsThingValue.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserializeInto(lsThingValue.toJson(), new ItxLsThingLsThingValue());
        return newItxLsThingLsThingValue;
    }


}
