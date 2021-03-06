// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.UnitKind;
import com.labsynch.labseer.domain.UnitKindDataOnDemand;
import com.labsynch.labseer.domain.UnitType;
import com.labsynch.labseer.domain.UnitTypeDataOnDemand;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

privileged aspect UnitKindDataOnDemand_Roo_DataOnDemand {
    
    declare @type: UnitKindDataOnDemand: @Component;
    
    private Random UnitKindDataOnDemand.rnd = new SecureRandom();
    
    private List<UnitKind> UnitKindDataOnDemand.data;
    
    @Autowired
    UnitTypeDataOnDemand UnitKindDataOnDemand.unitTypeDataOnDemand;
    
    public UnitKind UnitKindDataOnDemand.getNewTransientUnitKind(int index) {
        UnitKind obj = new UnitKind();
        setKindName(obj, index);
        setLsType(obj, index);
        setLsTypeAndKind(obj, index);
        return obj;
    }
    
    public void UnitKindDataOnDemand.setKindName(UnitKind obj, int index) {
        String kindName = "kindName_" + index;
        if (kindName.length() > 64) {
            kindName = kindName.substring(0, 64);
        }
        obj.setKindName(kindName);
    }
    
    public void UnitKindDataOnDemand.setLsType(UnitKind obj, int index) {
        UnitType lsType = unitTypeDataOnDemand.getRandomUnitType();
        obj.setLsType(lsType);
    }
    
    public void UnitKindDataOnDemand.setLsTypeAndKind(UnitKind obj, int index) {
        String lsTypeAndKind = "lsTypeAndKind_" + index;
        if (lsTypeAndKind.length() > 255) {
            lsTypeAndKind = new Random().nextInt(10) + lsTypeAndKind.substring(1, 255);
        }
        obj.setLsTypeAndKind(lsTypeAndKind);
    }
    
    public UnitKind UnitKindDataOnDemand.getSpecificUnitKind(int index) {
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
    
    public UnitKind UnitKindDataOnDemand.getRandomUnitKind() {
        init();
        UnitKind obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return UnitKind.findUnitKind(id);
    }
    
    public boolean UnitKindDataOnDemand.modifyUnitKind(UnitKind obj) {
        return false;
    }
    
    public void UnitKindDataOnDemand.init() {
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
