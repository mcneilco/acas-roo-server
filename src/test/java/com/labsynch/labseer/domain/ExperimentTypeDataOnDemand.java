package com.labsynch.labseer.domain;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Component
@Configurable
public class ExperimentTypeDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<ExperimentType> data;

	public ExperimentType getNewTransientExperimentType(int index) {
        ExperimentType obj = new ExperimentType();
        setTypeName(obj, index);
        return obj;
    }

	public void setTypeName(ExperimentType obj, int index) {
        String typeName = "typeName_" + index;
        if (typeName.length() > 128) {
            typeName = new Random().nextInt(10) + typeName.substring(1, 128);
        }
        obj.setTypeName(typeName);
    }

	public ExperimentType getSpecificExperimentType(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ExperimentType obj = data.get(index);
        Long id = obj.getId();
        return ExperimentType.findExperimentType(id);
    }

	public ExperimentType getRandomExperimentType() {
        init();
        ExperimentType obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ExperimentType.findExperimentType(id);
    }

	public boolean modifyExperimentType(ExperimentType obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = ExperimentType.findExperimentTypeEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ExperimentType' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ExperimentType>();
        for (int i = 0; i < 10; i++) {
            ExperimentType obj = getNewTransientExperimentType(i);
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
