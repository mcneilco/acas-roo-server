package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;

import flexjson.JSONDeserializer;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findProtocolValuesByLsState", "findProtocolValuesByLsTransactionEquals" })
public class ProtocolValue extends AbstractValue {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "protocol_state_id")
    private ProtocolState lsState;
    
    public static ProtocolValue fromJsonToProtocolValue(String json) {
        return new JSONDeserializer<ProtocolValue>().
        		use(null, ProtocolValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
        
    public static Collection<ProtocolValue> fromJsonArrayToProtocolValues(String json) {
        return new JSONDeserializer<List<ProtocolValue>>().
        		use(null, ArrayList.class).
        		use("values", ProtocolValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }
    
    public static Collection<ProtocolValue> fromJsonArrayToProtocolValues(Reader json) {
        return new JSONDeserializer<List<ProtocolValue>>().
        		use(null, ArrayList.class).
        		use("values", ProtocolValue.class).
        		use(BigDecimal.class, new CustomBigDecimalFactory()).
        		deserialize(json);
    }


	public static ProtocolValue update(ProtocolValue protocolValue) {
		ProtocolValue updatedProtocolValue = new JSONDeserializer<ProtocolValue>().
		use(null, ProtocolValue.class).
		use(BigDecimal.class, new CustomBigDecimalFactory()).
		deserializeInto(protocolValue.toJson(), ProtocolValue.findProtocolValue(protocolValue.getId()));
		updatedProtocolValue.merge();
		return updatedProtocolValue;
	}
}
