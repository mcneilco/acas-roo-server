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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class ThingPageDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<ThingPage> data;

	@Autowired
    LsTransactionDataOnDemand lsTransactionDataOnDemand;

	public ThingPage getNewTransientThingPage(int index) {
        ThingPage obj = new ThingPage();
        setArchived(obj, index);
        setCurrentEditor(obj, index);
        setIgnored(obj, index);
        setModifiedBy(obj, index);
        setModifiedDate(obj, index);
        setPageContent(obj, index);
        setPageName(obj, index);
        setRecordedBy(obj, index);
        setRecordedDate(obj, index);
        setThing(obj, index);
        return obj;
    }

	public void setArchived(ThingPage obj, int index) {
        Boolean archived = true;
        obj.setArchived(archived);
    }

	public void setCurrentEditor(ThingPage obj, int index) {
        String currentEditor = "currentEditor_" + index;
        if (currentEditor.length() > 255) {
            currentEditor = currentEditor.substring(0, 255);
        }
        obj.setCurrentEditor(currentEditor);
    }

	public void setIgnored(ThingPage obj, int index) {
        Boolean ignored = true;
        obj.setIgnored(ignored);
    }

	public void setModifiedBy(ThingPage obj, int index) {
        String modifiedBy = "modifiedBy_" + index;
        if (modifiedBy.length() > 255) {
            modifiedBy = modifiedBy.substring(0, 255);
        }
        obj.setModifiedBy(modifiedBy);
    }

	public void setModifiedDate(ThingPage obj, int index) {
        Date modifiedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setModifiedDate(modifiedDate);
    }

	public void setPageContent(ThingPage obj, int index) {
        String pageContent = "pageContent_" + index;
        obj.setPageContent(pageContent);
    }

	public void setPageName(ThingPage obj, int index) {
        String pageName = "pageName_" + index;
        if (pageName.length() > 255) {
            pageName = pageName.substring(0, 255);
        }
        obj.setPageName(pageName);
    }

	public void setRecordedBy(ThingPage obj, int index) {
        String recordedBy = "recordedBy_" + index;
        if (recordedBy.length() > 255) {
            recordedBy = recordedBy.substring(0, 255);
        }
        obj.setRecordedBy(recordedBy);
    }

	public void setRecordedDate(ThingPage obj, int index) {
        Date recordedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setRecordedDate(recordedDate);
    }

	public void setThing(ThingPage obj, int index) {
        AbstractThing thing = null;
        obj.setThing(thing);
    }

	public ThingPage getSpecificThingPage(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ThingPage obj = data.get(index);
        Long id = obj.getId();
        return ThingPage.findThingPage(id);
    }

	public ThingPage getRandomThingPage() {
        init();
        ThingPage obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ThingPage.findThingPage(id);
    }

	public boolean modifyThingPage(ThingPage obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = ThingPage.findThingPageEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ThingPage' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ThingPage>();
        for (int i = 0; i < 10; i++) {
            ThingPage obj = getNewTransientThingPage(i);
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
