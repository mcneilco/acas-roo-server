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
public class InteractionKindDataOnDemand {

    private Random rnd = new SecureRandom();

    private List<InteractionKind> data;

    @Autowired
    InteractionTypeDataOnDemand interactionTypeDataOnDemand;

    public InteractionKind getNewTransientInteractionKind(int index) {
        InteractionKind obj = new InteractionKind();
        setKindName(obj, index);
        setLsType(obj, index);
        setLsTypeAndKind(obj, index);
        return obj;
    }

    public void setKindName(InteractionKind obj, int index) {
        String kindName = "kindName_" + index;
        if (kindName.length() > 255) {
            kindName = kindName.substring(0, 255);
        }
        obj.setKindName(kindName);
    }

    public void setLsType(InteractionKind obj, int index) {
        InteractionType lsType = interactionTypeDataOnDemand.getRandomInteractionType();
        obj.setLsType(lsType);
    }

    public void setLsTypeAndKind(InteractionKind obj, int index) {
        String lsTypeAndKind = "lsTypeAndKind_" + index;
        if (lsTypeAndKind.length() > 255) {
            lsTypeAndKind = new Random().nextInt(10) + lsTypeAndKind.substring(1, 255);
        }
        obj.setLsTypeAndKind(lsTypeAndKind);
    }

    public InteractionKind getSpecificInteractionKind(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        InteractionKind obj = data.get(index);
        Long id = obj.getId();
        return InteractionKind.findInteractionKind(id);
    }

    public InteractionKind getRandomInteractionKind() {
        init();
        InteractionKind obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return InteractionKind.findInteractionKind(id);
    }

    public boolean modifyInteractionKind(InteractionKind obj) {
        return false;
    }

    public void init() {
        int from = 0;
        int to = 10;
        data = InteractionKind.findInteractionKindEntries(from, to);
        if (data == null) {
            throw new IllegalStateException(
                    "Find entries implementation for 'InteractionKind' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }

        data = new ArrayList<InteractionKind>();
        for (int i = 0; i < 10; i++) {
            InteractionKind obj = getNewTransientInteractionKind(i);
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
