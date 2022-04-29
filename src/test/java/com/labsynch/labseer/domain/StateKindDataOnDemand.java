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
import org.springframework.roo.addon.dod.RooDataOnDemand;
import org.springframework.stereotype.Component;

@Configurable
@Component
@RooDataOnDemand(entity = StateKind.class)
public class StateKindDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<StateKind> data;

	@Autowired
    StateTypeDataOnDemand stateTypeDataOnDemand;

	public StateKind getNewTransientStateKind(int index) {
        StateKind obj = new StateKind();
        setKindName(obj, index);
        setLsType(obj, index);
        setLsTypeAndKind(obj, index);
        return obj;
    }

	public void setKindName(StateKind obj, int index) {
        String kindName = "kindName_" + index;
        if (kindName.length() > 64) {
            kindName = kindName.substring(0, 64);
        }
        obj.setKindName(kindName);
    }

	public void setLsType(StateKind obj, int index) {
        StateType lsType = stateTypeDataOnDemand.getRandomStateType();
        obj.setLsType(lsType);
    }

	public void setLsTypeAndKind(StateKind obj, int index) {
        String lsTypeAndKind = "lsTypeAndKind_" + index;
        if (lsTypeAndKind.length() > 255) {
            lsTypeAndKind = new Random().nextInt(10) + lsTypeAndKind.substring(1, 255);
        }
        obj.setLsTypeAndKind(lsTypeAndKind);
    }

	public StateKind getSpecificStateKind(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        StateKind obj = data.get(index);
        Long id = obj.getId();
        return StateKind.findStateKind(id);
    }

	public StateKind getRandomStateKind() {
        init();
        StateKind obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return StateKind.findStateKind(id);
    }

	public boolean modifyStateKind(StateKind obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = StateKind.findStateKindEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'StateKind' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<StateKind>();
        for (int i = 0; i < 10; i++) {
            StateKind obj = getNewTransientStateKind(i);
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
