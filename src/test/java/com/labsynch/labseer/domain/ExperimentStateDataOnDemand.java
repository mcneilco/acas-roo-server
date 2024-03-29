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
public class ExperimentStateDataOnDemand {

    private Random rnd = new SecureRandom();

    private List<ExperimentState> data;

    @Autowired
    ExperimentDataOnDemand experimentDataOnDemand;

    public ExperimentState getNewTransientExperimentState(int index) {
        ExperimentState obj = new ExperimentState();
        setComments(obj, index);
        setDeleted(obj, index);
        setExperiment(obj, index);
        setIgnored(obj, index);
        setLsKind(obj, index);
        setLsTransaction(obj, index);
        setLsType(obj, index);
        setLsTypeAndKind(obj, index);
        setModifiedBy(obj, index);
        setModifiedDate(obj, index);
        setRecordedBy(obj, index);
        setRecordedDate(obj, index);
        return obj;
    }

    public void setComments(ExperimentState obj, int index) {
        String comments = "comments_" + index;
        if (comments.length() > 512) {
            comments = comments.substring(0, 512);
        }
        obj.setComments(comments);
    }

    public void setDeleted(ExperimentState obj, int index) {
        Boolean deleted = true;
        obj.setDeleted(deleted);
    }

    public void setExperiment(ExperimentState obj, int index) {
        Experiment experiment = experimentDataOnDemand.getRandomExperiment();
        obj.setExperiment(experiment);
    }

    public void setIgnored(ExperimentState obj, int index) {
        Boolean ignored = true;
        obj.setIgnored(ignored);
    }

    public void setLsKind(ExperimentState obj, int index) {
        String lsKind = "lsKind_" + index;
        if (lsKind.length() > 255) {
            lsKind = lsKind.substring(0, 255);
        }
        obj.setLsKind(lsKind);
    }

    public void setLsTransaction(ExperimentState obj, int index) {
        Long lsTransaction = new Integer(index).longValue();
        obj.setLsTransaction(lsTransaction);
    }

    public void setLsType(ExperimentState obj, int index) {
        String lsType = "lsType_" + index;
        if (lsType.length() > 64) {
            lsType = lsType.substring(0, 64);
        }
        obj.setLsType(lsType);
    }

    public void setLsTypeAndKind(ExperimentState obj, int index) {
        String lsTypeAndKind = "lsTypeAndKind_" + index;
        if (lsTypeAndKind.length() > 255) {
            lsTypeAndKind = lsTypeAndKind.substring(0, 255);
        }
        obj.setLsTypeAndKind(lsTypeAndKind);
    }

    public void setModifiedBy(ExperimentState obj, int index) {
        String modifiedBy = "modifiedBy_" + index;
        if (modifiedBy.length() > 255) {
            modifiedBy = modifiedBy.substring(0, 255);
        }
        obj.setModifiedBy(modifiedBy);
    }

    public void setModifiedDate(ExperimentState obj, int index) {
        Date modifiedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE),
                Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setModifiedDate(modifiedDate);
    }

    public void setRecordedBy(ExperimentState obj, int index) {
        String recordedBy = "recordedBy_" + index;
        if (recordedBy.length() > 255) {
            recordedBy = recordedBy.substring(0, 255);
        }
        obj.setRecordedBy(recordedBy);
    }

    public void setRecordedDate(ExperimentState obj, int index) {
        Date recordedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE),
                Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setRecordedDate(recordedDate);
    }

    public ExperimentState getSpecificExperimentState(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ExperimentState obj = data.get(index);
        Long id = obj.getId();
        return ExperimentState.findExperimentState(id);
    }

    public ExperimentState getRandomExperimentState() {
        init();
        ExperimentState obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ExperimentState.findExperimentState(id);
    }

    public boolean modifyExperimentState(ExperimentState obj) {
        return false;
    }

    public void init() {
        int from = 0;
        int to = 10;
        data = ExperimentState.findExperimentStateEntries(from, to);
        if (data == null) {
            throw new IllegalStateException(
                    "Find entries implementation for 'ExperimentState' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }

        data = new ArrayList<ExperimentState>();
        for (int i = 0; i < 10; i++) {
            ExperimentState obj = getNewTransientExperimentState(i);
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
