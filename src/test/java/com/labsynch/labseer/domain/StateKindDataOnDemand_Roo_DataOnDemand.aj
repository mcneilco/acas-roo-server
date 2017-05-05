// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.StateKind;
import com.labsynch.labseer.domain.StateKindDataOnDemand;
import com.labsynch.labseer.domain.StateType;
import com.labsynch.labseer.domain.StateTypeDataOnDemand;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect StateKindDataOnDemand_Roo_DataOnDemand {
    
    declare @type: StateKindDataOnDemand: @Component;
    
    private Random StateKindDataOnDemand.rnd = new SecureRandom();
    
    private List<StateKind> StateKindDataOnDemand.data;
    
    @Autowired
    StateTypeDataOnDemand StateKindDataOnDemand.stateTypeDataOnDemand;
    
    public StateKind StateKindDataOnDemand.getNewTransientStateKind(int index) {
        StateKind obj = new StateKind();
        setKindName(obj, index);
        setLsType(obj, index);
        setLsTypeAndKind(obj, index);
        return obj;
    }
    
    public void StateKindDataOnDemand.setKindName(StateKind obj, int index) {
        String kindName = "kindName_" + index;
        if (kindName.length() > 64) {
            kindName = kindName.substring(0, 64);
        }
        obj.setKindName(kindName);
    }
    
    public void StateKindDataOnDemand.setLsType(StateKind obj, int index) {
        StateType lsType = stateTypeDataOnDemand.getRandomStateType();
        obj.setLsType(lsType);
    }
    
    public void StateKindDataOnDemand.setLsTypeAndKind(StateKind obj, int index) {
        String lsTypeAndKind = "lsTypeAndKind_" + index;
        if (lsTypeAndKind.length() > 255) {
            lsTypeAndKind = new Random().nextInt(10) + lsTypeAndKind.substring(1, 255);
        }
        obj.setLsTypeAndKind(lsTypeAndKind);
    }
    
    public StateKind StateKindDataOnDemand.getSpecificStateKind(int index) {
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
    
    public StateKind StateKindDataOnDemand.getRandomStateKind() {
        init();
        StateKind obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return StateKind.findStateKind(id);
    }
    
    public boolean StateKindDataOnDemand.modifyStateKind(StateKind obj) {
        return false;
    }
    
    public void StateKindDataOnDemand.init() {
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
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}