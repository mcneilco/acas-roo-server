package com.labsynch.labseer.domain;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class ValueKindDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<ValueKind> data;

	@Autowired
    ValueTypeDataOnDemand valueTypeDataOnDemand;

	public ValueKind getNewTransientValueKind(int index) {
        ValueKind obj = new ValueKind();
        setKindName(obj, index);
        setLsType(obj, index);
        setLsTypeAndKind(obj, index);
        return obj;
    }

	public void setKindName(ValueKind obj, int index) {
        String kindName = "kindName_" + index;
        if (kindName.length() > 64) {
            kindName = kindName.substring(0, 64);
        }
        obj.setKindName(kindName);
    }

	public void setLsType(ValueKind obj, int index) {
        ValueType lsType = valueTypeDataOnDemand.getRandomValueType();
        obj.setLsType(lsType);
    }

	public void setLsTypeAndKind(ValueKind obj, int index) {
        String lsTypeAndKind = "lsTypeAndKind_" + index;
        if (lsTypeAndKind.length() > 255) {
            lsTypeAndKind = new Random().nextInt(10) + lsTypeAndKind.substring(1, 255);
        }
        obj.setLsTypeAndKind(lsTypeAndKind);
    }

	public ValueKind getSpecificValueKind(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ValueKind obj = data.get(index);
        Long id = obj.getId();
        return ValueKind.findValueKind(id);
    }

	public ValueKind getRandomValueKind() {
        init();
        ValueKind obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ValueKind.findValueKind(id);
    }

	public boolean modifyValueKind(ValueKind obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = ValueKind.findValueKindEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ValueKind' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ValueKind>();
        for (int i = 0; i < 10; i++) {
            ValueKind obj = getNewTransientValueKind(i);
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
