// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.ContainerType;
import com.labsynch.labseer.domain.ContainerTypeDataOnDemand;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;

privileged aspect ContainerTypeDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ContainerTypeDataOnDemand: @Component;
    
    private Random ContainerTypeDataOnDemand.rnd = new SecureRandom();
    
    private List<ContainerType> ContainerTypeDataOnDemand.data;
    
    public ContainerType ContainerTypeDataOnDemand.getNewTransientContainerType(int index) {
        ContainerType obj = new ContainerType();
        setTypeName(obj, index);
        return obj;
    }
    
    public void ContainerTypeDataOnDemand.setTypeName(ContainerType obj, int index) {
        String typeName = "typeName_" + index;
        if (typeName.length() > 128) {
            typeName = new Random().nextInt(10) + typeName.substring(1, 128);
        }
        obj.setTypeName(typeName);
    }
    
    public ContainerType ContainerTypeDataOnDemand.getSpecificContainerType(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ContainerType obj = data.get(index);
        Long id = obj.getId();
        return ContainerType.findContainerType(id);
    }
    
    public ContainerType ContainerTypeDataOnDemand.getRandomContainerType() {
        init();
        ContainerType obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ContainerType.findContainerType(id);
    }
    
    public boolean ContainerTypeDataOnDemand.modifyContainerType(ContainerType obj) {
        return false;
    }
    
    public void ContainerTypeDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = ContainerType.findContainerTypeEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ContainerType' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ContainerType>();
        for (int i = 0; i < 10; i++) {
            ContainerType obj = getNewTransientContainerType(i);
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
