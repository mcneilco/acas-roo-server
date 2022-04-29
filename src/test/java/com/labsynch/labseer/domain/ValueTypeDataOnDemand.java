package com.labsynch.labseer.domain;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Component
@Configurable
public class ValueTypeDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<ValueType> data;

	public ValueType getNewTransientValueType(int index) {
        ValueType obj = new ValueType();
        setTypeName(obj, index);
        return obj;
    }

	public void setTypeName(ValueType obj, int index) {
        String typeName = "typeName_" + index;
        if (typeName.length() > 64) {
            typeName = new Random().nextInt(10) + typeName.substring(1, 64);
        }
        obj.setTypeName(typeName);
    }

	public ValueType getSpecificValueType(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ValueType obj = data.get(index);
        Long id = obj.getId();
        return ValueType.findValueType(id);
    }

	public ValueType getRandomValueType() {
        init();
        ValueType obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ValueType.findValueType(id);
    }

	public boolean modifyValueType(ValueType obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = ValueType.findValueTypeEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ValueType' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ValueType>();
        for (int i = 0; i < 10; i++) {
            ValueType obj = getNewTransientValueType(i);
            try {
                obj.persist();
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
}
