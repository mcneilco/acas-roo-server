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
import org.springframework.roo.addon.dod.RooDataOnDemand;
import org.springframework.stereotype.Component;

@Component
@Configurable
@RooDataOnDemand(entity = SubjectState.class)
public class SubjectStateDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<SubjectState> data;

	@Autowired
    SubjectDataOnDemand subjectDataOnDemand;

	public SubjectState getNewTransientSubjectState(int index) {
        SubjectState obj = new SubjectState();
        setComments(obj, index);
        setDeleted(obj, index);
        setIgnored(obj, index);
        setLsKind(obj, index);
        setLsTransaction(obj, index);
        setLsType(obj, index);
        setLsTypeAndKind(obj, index);
        setModifiedBy(obj, index);
        setModifiedDate(obj, index);
        setRecordedBy(obj, index);
        setRecordedDate(obj, index);
        setSubject(obj, index);
        return obj;
    }

	public void setComments(SubjectState obj, int index) {
        String comments = "comments_" + index;
        if (comments.length() > 512) {
            comments = comments.substring(0, 512);
        }
        obj.setComments(comments);
    }

	public void setDeleted(SubjectState obj, int index) {
        Boolean deleted = true;
        obj.setDeleted(deleted);
    }

	public void setIgnored(SubjectState obj, int index) {
        Boolean ignored = true;
        obj.setIgnored(ignored);
    }

	public void setLsKind(SubjectState obj, int index) {
        String lsKind = "lsKind_" + index;
        if (lsKind.length() > 255) {
            lsKind = lsKind.substring(0, 255);
        }
        obj.setLsKind(lsKind);
    }

	public void setLsTransaction(SubjectState obj, int index) {
        Long lsTransaction = new Integer(index).longValue();
        obj.setLsTransaction(lsTransaction);
    }

	public void setLsType(SubjectState obj, int index) {
        String lsType = "lsType_" + index;
        if (lsType.length() > 64) {
            lsType = lsType.substring(0, 64);
        }
        obj.setLsType(lsType);
    }

	public void setLsTypeAndKind(SubjectState obj, int index) {
        String lsTypeAndKind = "lsTypeAndKind_" + index;
        if (lsTypeAndKind.length() > 255) {
            lsTypeAndKind = lsTypeAndKind.substring(0, 255);
        }
        obj.setLsTypeAndKind(lsTypeAndKind);
    }

	public void setModifiedBy(SubjectState obj, int index) {
        String modifiedBy = "modifiedBy_" + index;
        if (modifiedBy.length() > 255) {
            modifiedBy = modifiedBy.substring(0, 255);
        }
        obj.setModifiedBy(modifiedBy);
    }

	public void setModifiedDate(SubjectState obj, int index) {
        Date modifiedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setModifiedDate(modifiedDate);
    }

	public void setRecordedBy(SubjectState obj, int index) {
        String recordedBy = "recordedBy_" + index;
        if (recordedBy.length() > 255) {
            recordedBy = recordedBy.substring(0, 255);
        }
        obj.setRecordedBy(recordedBy);
    }

	public void setRecordedDate(SubjectState obj, int index) {
        Date recordedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setRecordedDate(recordedDate);
    }

	public void setSubject(SubjectState obj, int index) {
        Subject subject = subjectDataOnDemand.getRandomSubject();
        obj.setSubject(subject);
    }

	public SubjectState getSpecificSubjectState(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        SubjectState obj = data.get(index);
        Long id = obj.getId();
        return SubjectState.findSubjectState(id);
    }

	public SubjectState getRandomSubjectState() {
        init();
        SubjectState obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return SubjectState.findSubjectState(id);
    }

	public boolean modifySubjectState(SubjectState obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = SubjectState.findSubjectStateEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'SubjectState' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<SubjectState>();
        for (int i = 0; i < 10; i++) {
            SubjectState obj = getNewTransientSubjectState(i);
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
