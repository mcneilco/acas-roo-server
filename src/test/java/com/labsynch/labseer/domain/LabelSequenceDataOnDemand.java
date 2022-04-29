package com.labsynch.labseer.domain;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Component
@Configurable
public class LabelSequenceDataOnDemand {

    private Random rnd = new SecureRandom();

    private List<LabelSequence> data;

    public LabelSequence getNewTransientLabelSequence(int index) {
        LabelSequence obj = new LabelSequence();
        setDbSequence(obj, index);
        setDigits(obj, index);
        setGroupDigits(obj, index);
        setIgnored(obj, index);
        setLabelPrefix(obj, index);
        setLabelSeparator(obj, index);
        setLabelTypeAndKind(obj, index);
        setModifiedDate(obj, index);
        setStartingNumber(obj, index);
        setThingTypeAndKind(obj, index);
        return obj;
    }

    public void setDbSequence(LabelSequence obj, int index) {
        String dbSequence = "dbSequence_" + index;
        obj.setDbSequence(dbSequence);
    }

    public void setDigits(LabelSequence obj, int index) {
        Integer digits = new Integer(index);
        obj.setDigits(digits);
    }

    public void setGroupDigits(LabelSequence obj, int index) {
        Boolean groupDigits = true;
        obj.setGroupDigits(groupDigits);
    }

    public void setIgnored(LabelSequence obj, int index) {
        Boolean ignored = true;
        obj.setIgnored(ignored);
    }

    public void setLabelPrefix(LabelSequence obj, int index) {
        String labelPrefix = "labelPrefix_" + index;
        if (labelPrefix.length() > 50) {
            labelPrefix = labelPrefix.substring(0, 50);
        }
        obj.setLabelPrefix(labelPrefix);
    }

    public void setLabelSeparator(LabelSequence obj, int index) {
        String labelSeparator = "labelSep_" + index;
        if (labelSeparator.length() > 10) {
            labelSeparator = labelSeparator.substring(0, 10);
        }
        obj.setLabelSeparator(labelSeparator);
    }

    public void setLabelTypeAndKind(LabelSequence obj, int index) {
        String labelTypeAndKind = "labelTypeAndKind_" + index;
        if (labelTypeAndKind.length() > 255) {
            labelTypeAndKind = labelTypeAndKind.substring(0, 255);
        }
        obj.setLabelTypeAndKind(labelTypeAndKind);
    }

    public void setModifiedDate(LabelSequence obj, int index) {
        Date modifiedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE),
                Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setModifiedDate(modifiedDate);
    }

    public void setStartingNumber(LabelSequence obj, int index) {
        Long startingNumber = new Integer(index).longValue();
        obj.setStartingNumber(startingNumber);
    }

    public void setThingTypeAndKind(LabelSequence obj, int index) {
        String thingTypeAndKind = "thingTypeAndKind_" + index;
        if (thingTypeAndKind.length() > 255) {
            thingTypeAndKind = thingTypeAndKind.substring(0, 255);
        }
        obj.setThingTypeAndKind(thingTypeAndKind);
    }

    public LabelSequence getSpecificLabelSequence(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        LabelSequence obj = data.get(index);
        Long id = obj.getId();
        return LabelSequence.findLabelSequence(id);
    }

    public LabelSequence getRandomLabelSequence() {
        init();
        LabelSequence obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return LabelSequence.findLabelSequence(id);
    }

    public boolean modifyLabelSequence(LabelSequence obj) {
        return false;
    }

    public void init() {
        int from = 0;
        int to = 10;
        data = LabelSequence.findLabelSequenceEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'LabelSequence' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }

        data = new ArrayList<LabelSequence>();
        for (int i = 0; i < 10; i++) {
            LabelSequence obj = getNewTransientLabelSequence(i);
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
