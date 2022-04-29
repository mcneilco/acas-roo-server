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

@Component
@Configurable
public class UnitKindDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<UnitKind> data;

	@Autowired
    UnitTypeDataOnDemand unitTypeDataOnDemand;

	public UnitKind getNewTransientUnitKind(int index) {
        UnitKind obj = new UnitKind();
        setKindName(obj, index);
        setLsType(obj, index);
        setLsTypeAndKind(obj, index);
        return obj;
    }

	public void setKindName(UnitKind obj, int index) {
        String kindName = "kindName_" + index;
        if (kindName.length() > 64) {
            kindName = kindName.substring(0, 64);
        }
        obj.setKindName(kindName);
    }

	public void setLsType(UnitKind obj, int index) {
        UnitType lsType = unitTypeDataOnDemand.getRandomUnitType();
        obj.setLsType(lsType);
    }

	public void setLsTypeAndKind(UnitKind obj, int index) {
        String lsTypeAndKind = "lsTypeAndKind_" + index;
        if (lsTypeAndKind.length() > 255) {
            lsTypeAndKind = new Random().nextInt(10) + lsTypeAndKind.substring(1, 255);
        }
        obj.setLsTypeAndKind(lsTypeAndKind);
    }

	public UnitKind getSpecificUnitKind(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        UnitKind obj = data.get(index);
        Long id = obj.getId();
        return UnitKind.findUnitKind(id);
    }

	public UnitKind getRandomUnitKind() {
        init();
        UnitKind obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return UnitKind.findUnitKind(id);
    }

	public boolean modifyUnitKind(UnitKind obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = UnitKind.findUnitKindEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'UnitKind' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<UnitKind>();
        for (int i = 0; i < 10; i++) {
            UnitKind obj = getNewTransientUnitKind(i);
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
