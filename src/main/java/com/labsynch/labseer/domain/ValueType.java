package com.labsynch.labseer.domain;

import java.util.List;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "VALUE_TYPE_PKSEQ", finders = { "findValueTypesByTypeNameEquals" })
@RooJson
public class ValueType {

    private static final Logger logger = LoggerFactory.getLogger(ValueType.class);

    @NotNull
    @Column(unique = true)
    @Size(max = 64)
    private String typeName;

	public static ValueType getOrCreate(String name) {
		ValueType lsType = null;
        List<ValueType> lsTypes = ValueType.findValueTypesByTypeNameEquals(name).getResultList();
        if (lsTypes.size() == 0) {
            lsType = new ValueType();
            lsType.setTypeName(name);
            lsType.persist();
        } else if (lsTypes.size() == 1) {
            lsType = lsTypes.get(0);
        } else if (lsTypes.size() > 1) {
            logger.error("ERROR: multiple Label types with the same name");
        }
        return lsType;
	}
}
