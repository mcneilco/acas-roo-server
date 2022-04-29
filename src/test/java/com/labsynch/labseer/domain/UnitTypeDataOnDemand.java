package com.labsynch.labseer.domain;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.dod.RooDataOnDemand;
import org.springframework.stereotype.Component;

@Configurable
@Component
@RooDataOnDemand(entity = UnitType.class)
public class UnitTypeDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<UnitType> data;

	public UnitType getNewTransientUnitType(int index) {
        UnitType obj = new UnitType();
        setTypeName(obj, index);
        return obj;
    }

	public void setTypeName(UnitType obj, int index) {
        String typeName = "typeName_" + index;
        if (typeName.length() > 64) {
            typeName = new Random().nextInt(10) + typeName.substring(1, 64);
        }
        obj.setTypeName(typeName);
    }

	public UnitType getSpecificUnitType(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        UnitType obj = data.get(index);
        Long id = obj.getId();
        return UnitType.findUnitType(id);
    }

	public UnitType getRandomUnitType() {
        init();
        UnitType obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return UnitType.findUnitType(id);
    }

	public boolean modifyUnitType(UnitType obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = UnitType.findUnitTypeEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'UnitType' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<UnitType>();
        for (int i = 0; i < 10; i++) {
            UnitType obj = getNewTransientUnitType(i);
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
