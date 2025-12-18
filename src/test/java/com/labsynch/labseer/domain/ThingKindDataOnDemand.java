package com.labsynch.labseer.domain;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class ThingKindDataOnDemand {

    private Random rnd = new SecureRandom();

    private List<ThingKind> data;

    @Autowired
    ThingTypeDataOnDemand thingTypeDataOnDemand;

    public ThingKind getNewTransientThingKind(int index) {
        ThingKind obj = new ThingKind();
        setKindName(obj, index);
        setLsType(obj, index);
        setLsTypeAndKind(obj, index);
        return obj;
    }

    public void setKindName(ThingKind obj, int index) {
        String kindName = "kindName_" + index;
        if (kindName.length() > 255) {
            kindName = kindName.substring(0, 255);
        }
        obj.setKindName(kindName);
    }

    public void setLsType(ThingKind obj, int index) {
        ThingType lsType = thingTypeDataOnDemand.getRandomThingType();
        obj.setLsType(lsType);
    }

    public void setLsTypeAndKind(ThingKind obj, int index) {
        String lsTypeAndKind = "lsTypeAndKind_" + index;
        if (lsTypeAndKind.length() > 255) {
            lsTypeAndKind = new Random().nextInt(10) + lsTypeAndKind.substring(1, 255);
        }
        obj.setLsTypeAndKind(lsTypeAndKind);
    }

    public ThingKind getSpecificThingKind(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ThingKind obj = data.get(index);
        Long id = obj.getId();
        return ThingKind.findThingKind(id);
    }

    public ThingKind getRandomThingKind() {
        init();
        ThingKind obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ThingKind.findThingKind(id);
    }

    public boolean modifyThingKind(ThingKind obj) {
        return false;
    }

    public void init() {
        int from = 0;
        int to = 10;
        data = ThingKind.findThingKindEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ThingKind' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }

        data = new ArrayList<ThingKind>();
        for (int i = 0; i < 10; i++) {
            ThingKind obj = getNewTransientThingKind(i);
            try {
                obj.persist();
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".")
                            .append(cv.getPropertyPath()).append(": ").append(cv.getMessage())
                            .append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
}
