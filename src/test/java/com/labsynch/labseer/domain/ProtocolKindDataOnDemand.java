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
public class ProtocolKindDataOnDemand {

    private Random rnd = new SecureRandom();

    private List<ProtocolKind> data;

    @Autowired
    ProtocolTypeDataOnDemand protocolTypeDataOnDemand;

    public ProtocolKind getNewTransientProtocolKind(int index) {
        ProtocolKind obj = new ProtocolKind();
        setKindName(obj, index);
        setLsType(obj, index);
        setLsTypeAndKind(obj, index);
        return obj;
    }

    public void setKindName(ProtocolKind obj, int index) {
        String kindName = "kindName_" + index;
        if (kindName.length() > 255) {
            kindName = kindName.substring(0, 255);
        }
        obj.setKindName(kindName);
    }

    public void setLsType(ProtocolKind obj, int index) {
        ProtocolType lsType = protocolTypeDataOnDemand.getRandomProtocolType();
        obj.setLsType(lsType);
    }

    public void setLsTypeAndKind(ProtocolKind obj, int index) {
        String lsTypeAndKind = "lsTypeAndKind_" + index;
        if (lsTypeAndKind.length() > 255) {
            lsTypeAndKind = new Random().nextInt(10) + lsTypeAndKind.substring(1, 255);
        }
        obj.setLsTypeAndKind(lsTypeAndKind);
    }

    public ProtocolKind getSpecificProtocolKind(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ProtocolKind obj = data.get(index);
        Long id = obj.getId();
        return ProtocolKind.findProtocolKind(id);
    }

    public ProtocolKind getRandomProtocolKind() {
        init();
        ProtocolKind obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ProtocolKind.findProtocolKind(id);
    }

    public boolean modifyProtocolKind(ProtocolKind obj) {
        return false;
    }

    public void init() {
        int from = 0;
        int to = 10;
        data = ProtocolKind.findProtocolKindEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ProtocolKind' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }

        data = new ArrayList<ProtocolKind>();
        for (int i = 0; i < 10; i++) {
            ProtocolKind obj = getNewTransientProtocolKind(i);
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
