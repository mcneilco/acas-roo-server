package com.labsynch.labseer.domain;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class InteractionTypeDataOnDemand {

    private Random rnd = new SecureRandom();

    private List<InteractionType> data;

    public InteractionType getNewTransientInteractionType(int index) {
        InteractionType obj = new InteractionType();
        setTypeName(obj, index);
        setTypeVerb(obj, index);
        return obj;
    }

    public void setTypeName(InteractionType obj, int index) {
        String typeName = "typeName_" + index;
        if (typeName.length() > 128) {
            typeName = new Random().nextInt(10) + typeName.substring(1, 128);
        }
        obj.setTypeName(typeName);
    }

    public void setTypeVerb(InteractionType obj, int index) {
        String typeVerb = "typeVerb_" + index;
        if (typeVerb.length() > 128) {
            typeVerb = typeVerb.substring(0, 128);
        }
        obj.setTypeVerb(typeVerb);
    }

    public InteractionType getSpecificInteractionType(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        InteractionType obj = data.get(index);
        Long id = obj.getId();
        return InteractionType.findInteractionType(id);
    }

    public InteractionType getRandomInteractionType() {
        init();
        InteractionType obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return InteractionType.findInteractionType(id);
    }

    public boolean modifyInteractionType(InteractionType obj) {
        return false;
    }

    public void init() {
        int from = 0;
        int to = 10;
        data = InteractionType.findInteractionTypeEntries(from, to);
        if (data == null) {
            throw new IllegalStateException(
                    "Find entries implementation for 'InteractionType' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }

        data = new ArrayList<InteractionType>();
        for (int i = 0; i < 10; i++) {
            InteractionType obj = getNewTransientInteractionType(i);
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
