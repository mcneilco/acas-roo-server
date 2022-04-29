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
public class ItxContainerContainerDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<ItxContainerContainer> data;

	@Autowired
    ContainerDataOnDemand containerDataOnDemand;

	public ItxContainerContainer getNewTransientItxContainerContainer(int index) {
        ItxContainerContainer obj = new ItxContainerContainer();
        setCodeName(obj, index);
        setDeleted(obj, index);
        setFirstContainer(obj, index);
        setIgnored(obj, index);
        setLsKind(obj, index);
        setLsTransaction(obj, index);
        setLsType(obj, index);
        setLsTypeAndKind(obj, index);
        setModifiedBy(obj, index);
        setModifiedDate(obj, index);
        setRecordedBy(obj, index);
        setRecordedDate(obj, index);
        setSecondContainer(obj, index);
        return obj;
    }

	public void setCodeName(ItxContainerContainer obj, int index) {
        String codeName = "codeName_" + index;
        if (codeName.length() > 255) {
            codeName = new Random().nextInt(10) + codeName.substring(1, 255);
        }
        obj.setCodeName(codeName);
    }

	public void setDeleted(ItxContainerContainer obj, int index) {
        Boolean deleted = true;
        obj.setDeleted(deleted);
    }

	public void setFirstContainer(ItxContainerContainer obj, int index) {
        Container firstContainer = containerDataOnDemand.getRandomContainer();
        obj.setFirstContainer(firstContainer);
    }

	public void setIgnored(ItxContainerContainer obj, int index) {
        Boolean ignored = true;
        obj.setIgnored(ignored);
    }

	public void setLsKind(ItxContainerContainer obj, int index) {
        String lsKind = "lsKind_" + index;
        if (lsKind.length() > 255) {
            lsKind = lsKind.substring(0, 255);
        }
        obj.setLsKind(lsKind);
    }

	public void setLsTransaction(ItxContainerContainer obj, int index) {
        Long lsTransaction = new Integer(index).longValue();
        obj.setLsTransaction(lsTransaction);
    }

	public void setLsType(ItxContainerContainer obj, int index) {
        String lsType = "lsType_" + index;
        if (lsType.length() > 255) {
            lsType = lsType.substring(0, 255);
        }
        obj.setLsType(lsType);
    }

	public void setLsTypeAndKind(ItxContainerContainer obj, int index) {
        String lsTypeAndKind = "lsTypeAndKind_" + index;
        if (lsTypeAndKind.length() > 255) {
            lsTypeAndKind = lsTypeAndKind.substring(0, 255);
        }
        obj.setLsTypeAndKind(lsTypeAndKind);
    }

	public void setModifiedBy(ItxContainerContainer obj, int index) {
        String modifiedBy = "modifiedBy_" + index;
        if (modifiedBy.length() > 255) {
            modifiedBy = modifiedBy.substring(0, 255);
        }
        obj.setModifiedBy(modifiedBy);
    }

	public void setModifiedDate(ItxContainerContainer obj, int index) {
        Date modifiedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setModifiedDate(modifiedDate);
    }

	public void setRecordedBy(ItxContainerContainer obj, int index) {
        String recordedBy = "recordedBy_" + index;
        if (recordedBy.length() > 255) {
            recordedBy = recordedBy.substring(0, 255);
        }
        obj.setRecordedBy(recordedBy);
    }

	public void setRecordedDate(ItxContainerContainer obj, int index) {
        Date recordedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setRecordedDate(recordedDate);
    }

	public void setSecondContainer(ItxContainerContainer obj, int index) {
        Container secondContainer = containerDataOnDemand.getRandomContainer();
        obj.setSecondContainer(secondContainer);
    }

	public ItxContainerContainer getSpecificItxContainerContainer(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ItxContainerContainer obj = data.get(index);
        Long id = obj.getId();
        return ItxContainerContainer.findItxContainerContainer(id);
    }

	public ItxContainerContainer getRandomItxContainerContainer() {
        init();
        ItxContainerContainer obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ItxContainerContainer.findItxContainerContainer(id);
    }

	public boolean modifyItxContainerContainer(ItxContainerContainer obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = ItxContainerContainer.findItxContainerContainerEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ItxContainerContainer' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ItxContainerContainer>();
        for (int i = 0; i < 10; i++) {
            ItxContainerContainer obj = getNewTransientItxContainerContainer(i);
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
