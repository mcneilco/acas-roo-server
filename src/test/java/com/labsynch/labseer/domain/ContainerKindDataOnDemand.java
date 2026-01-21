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

@Component
@Configurable
public class ContainerKindDataOnDemand {

    private Random rnd = new SecureRandom();

    private List<ContainerKind> data;

    @Autowired
    ContainerTypeDataOnDemand containerTypeDataOnDemand;

    public ContainerKind getNewTransientContainerKind(int index) {
        ContainerKind obj = new ContainerKind();
        setKindName(obj, index);
        setLsType(obj, index);
        setLsTypeAndKind(obj, index);
        return obj;
    }

    public void setKindName(ContainerKind obj, int index) {
        String kindName = "kindName_" + index;
        if (kindName.length() > 255) {
            kindName = kindName.substring(0, 255);
        }
        obj.setKindName(kindName);
    }

    public void setLsType(ContainerKind obj, int index) {
        ContainerType lsType = containerTypeDataOnDemand.getRandomContainerType();
        obj.setLsType(lsType);
    }

    public void setLsTypeAndKind(ContainerKind obj, int index) {
        String lsTypeAndKind = "lsTypeAndKind_" + index;
        if (lsTypeAndKind.length() > 255) {
            lsTypeAndKind = new Random().nextInt(10) + lsTypeAndKind.substring(1, 255);
        }
        obj.setLsTypeAndKind(lsTypeAndKind);
    }

    public ContainerKind getSpecificContainerKind(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ContainerKind obj = data.get(index);
        Long id = obj.getId();
        return ContainerKind.findContainerKind(id);
    }

    public ContainerKind getRandomContainerKind() {
        init();
        ContainerKind obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ContainerKind.findContainerKind(id);
    }

    public boolean modifyContainerKind(ContainerKind obj) {
        return false;
    }

    public void init() {
        int from = 0;
        int to = 10;
        data = ContainerKind.findContainerKindEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ContainerKind' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }

        data = new ArrayList<ContainerKind>();
        for (int i = 0; i < 10; i++) {
            ContainerKind obj = getNewTransientContainerKind(i);
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
