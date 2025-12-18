package com.labsynch.labseer.domain;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class ThingPageArchiveDataOnDemand {

    private Random rnd = new SecureRandom();

    private List<ThingPageArchive> data;

    public ThingPageArchive getNewTransientThingPageArchive(int index) {
        ThingPageArchive obj = new ThingPageArchive();
        setArchived(obj, index);
        setCurrentEditor(obj, index);
        setIgnored(obj, index);
        setLsTransaction(obj, index);
        setModifiedBy(obj, index);
        setModifiedDate(obj, index);
        setPageContent(obj, index);
        setPageName(obj, index);
        setPageVersion(obj, index);
        setRecordedBy(obj, index);
        setRecordedDate(obj, index);
        setThing(obj, index);
        return obj;
    }

    public void setArchived(ThingPageArchive obj, int index) {
        Boolean archived = true;
        obj.setArchived(archived);
    }

    public void setCurrentEditor(ThingPageArchive obj, int index) {
        String currentEditor = "currentEditor_" + index;
        if (currentEditor.length() > 255) {
            currentEditor = currentEditor.substring(0, 255);
        }
        obj.setCurrentEditor(currentEditor);
    }

    public void setIgnored(ThingPageArchive obj, int index) {
        Boolean ignored = true;
        obj.setIgnored(ignored);
    }

    public void setLsTransaction(ThingPageArchive obj, int index) {
        Long lsTransaction = new Integer(index).longValue();
        obj.setLsTransaction(lsTransaction);
    }

    public void setModifiedBy(ThingPageArchive obj, int index) {
        String modifiedBy = "modifiedBy_" + index;
        if (modifiedBy.length() > 255) {
            modifiedBy = modifiedBy.substring(0, 255);
        }
        obj.setModifiedBy(modifiedBy);
    }

    public void setModifiedDate(ThingPageArchive obj, int index) {
        Date modifiedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE),
                Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setModifiedDate(modifiedDate);
    }

    public void setPageContent(ThingPageArchive obj, int index) {
        String pageContent = "pageContent_" + index;
        obj.setPageContent(pageContent);
    }

    public void setPageName(ThingPageArchive obj, int index) {
        String pageName = "pageName_" + index;
        if (pageName.length() > 255) {
            pageName = pageName.substring(0, 255);
        }
        obj.setPageName(pageName);
    }

    public void setPageVersion(ThingPageArchive obj, int index) {
        Integer pageVersion = new Integer(index);
        obj.setPageVersion(pageVersion);
    }

    public void setRecordedBy(ThingPageArchive obj, int index) {
        String recordedBy = "recordedBy_" + index;
        if (recordedBy.length() > 255) {
            recordedBy = recordedBy.substring(0, 255);
        }
        obj.setRecordedBy(recordedBy);
    }

    public void setRecordedDate(ThingPageArchive obj, int index) {
        Date recordedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE),
                Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setRecordedDate(recordedDate);
    }

    public void setThing(ThingPageArchive obj, int index) {
        Long thing = new Integer(index).longValue();
        obj.setThing(thing);
    }

    public ThingPageArchive getSpecificThingPageArchive(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ThingPageArchive obj = data.get(index);
        Long id = obj.getId();
        return ThingPageArchive.findThingPageArchive(id);
    }

    public ThingPageArchive getRandomThingPageArchive() {
        init();
        ThingPageArchive obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ThingPageArchive.findThingPageArchive(id);
    }

    public boolean modifyThingPageArchive(ThingPageArchive obj) {
        return false;
    }

    public void init() {
        int from = 0;
        int to = 10;
        data = ThingPageArchive.findThingPageArchiveEntries(from, to);
        if (data == null) {
            throw new IllegalStateException(
                    "Find entries implementation for 'ThingPageArchive' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }

        data = new ArrayList<ThingPageArchive>();
        for (int i = 0; i < 10; i++) {
            ThingPageArchive obj = getNewTransientThingPageArchive(i);
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
