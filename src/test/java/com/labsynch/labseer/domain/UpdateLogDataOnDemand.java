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
import org.springframework.roo.addon.dod.RooDataOnDemand;
import org.springframework.stereotype.Component;

@Configurable
@Component
@RooDataOnDemand(entity = UpdateLog.class)
public class UpdateLogDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<UpdateLog> data;

	public UpdateLog getNewTransientUpdateLog(int index) {
        UpdateLog obj = new UpdateLog();
        setComments(obj, index);
        setLsTransaction(obj, index);
        setRecordedBy(obj, index);
        setRecordedDate(obj, index);
        setThing(obj, index);
        setUpdateAction(obj, index);
        return obj;
    }

	public void setComments(UpdateLog obj, int index) {
        String comments = "comments_" + index;
        if (comments.length() > 512) {
            comments = comments.substring(0, 512);
        }
        obj.setComments(comments);
    }

	public void setLsTransaction(UpdateLog obj, int index) {
        Long lsTransaction = new Integer(index).longValue();
        obj.setLsTransaction(lsTransaction);
    }

	public void setRecordedBy(UpdateLog obj, int index) {
        String recordedBy = "recordedBy_" + index;
        if (recordedBy.length() > 255) {
            recordedBy = recordedBy.substring(0, 255);
        }
        obj.setRecordedBy(recordedBy);
    }

	public void setRecordedDate(UpdateLog obj, int index) {
        Date recordedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setRecordedDate(recordedDate);
    }

	public void setThing(UpdateLog obj, int index) {
        Long thing = new Integer(index).longValue();
        obj.setThing(thing);
    }

	public void setUpdateAction(UpdateLog obj, int index) {
        String updateAction = "updateAction_" + index;
        if (updateAction.length() > 255) {
            updateAction = updateAction.substring(0, 255);
        }
        obj.setUpdateAction(updateAction);
    }

	public UpdateLog getSpecificUpdateLog(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        UpdateLog obj = data.get(index);
        Long id = obj.getId();
        return UpdateLog.findUpdateLog(id);
    }

	public UpdateLog getRandomUpdateLog() {
        init();
        UpdateLog obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return UpdateLog.findUpdateLog(id);
    }

	public boolean modifyUpdateLog(UpdateLog obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = UpdateLog.findUpdateLogEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'UpdateLog' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<UpdateLog>();
        for (int i = 0; i < 10; i++) {
            UpdateLog obj = getNewTransientUpdateLog(i);
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
