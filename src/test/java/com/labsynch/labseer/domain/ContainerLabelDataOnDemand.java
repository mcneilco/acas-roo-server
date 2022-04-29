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
public class ContainerLabelDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<ContainerLabel> data;

	@Autowired
    ContainerDataOnDemand containerDataOnDemand;

	public ContainerLabel getNewTransientContainerLabel(int index) {
        ContainerLabel obj = new ContainerLabel();
        setContainer(obj, index);
        setDeleted(obj, index);
        setIgnored(obj, index);
        setImageFile(obj, index);
        setLabelText(obj, index);
        setLsKind(obj, index);
        setLsTransaction(obj, index);
        setLsType(obj, index);
        setLsTypeAndKind(obj, index);
        setModifiedDate(obj, index);
        setPhysicallyLabled(obj, index);
        setPreferred(obj, index);
        setRecordedBy(obj, index);
        setRecordedDate(obj, index);
        return obj;
    }

	public void setContainer(ContainerLabel obj, int index) {
        Container container = containerDataOnDemand.getRandomContainer();
        obj.setContainer(container);
    }

	public void setDeleted(ContainerLabel obj, int index) {
        Boolean deleted = true;
        obj.setDeleted(deleted);
    }

	public void setIgnored(ContainerLabel obj, int index) {
        Boolean ignored = true;
        obj.setIgnored(ignored);
    }

	public void setImageFile(ContainerLabel obj, int index) {
        String imageFile = "imageFile_" + index;
        if (imageFile.length() > 255) {
            imageFile = imageFile.substring(0, 255);
        }
        obj.setImageFile(imageFile);
    }

	public void setLabelText(ContainerLabel obj, int index) {
        String labelText = "labelText_" + index;
        if (labelText.length() > 255) {
            labelText = labelText.substring(0, 255);
        }
        obj.setLabelText(labelText);
    }

	public void setLsKind(ContainerLabel obj, int index) {
        String lsKind = "lsKind_" + index;
        if (lsKind.length() > 255) {
            lsKind = lsKind.substring(0, 255);
        }
        obj.setLsKind(lsKind);
    }

	public void setLsTransaction(ContainerLabel obj, int index) {
        Long lsTransaction = new Integer(index).longValue();
        obj.setLsTransaction(lsTransaction);
    }

	public void setLsType(ContainerLabel obj, int index) {
        String lsType = "lsType_" + index;
        if (lsType.length() > 64) {
            lsType = lsType.substring(0, 64);
        }
        obj.setLsType(lsType);
    }

	public void setLsTypeAndKind(ContainerLabel obj, int index) {
        String lsTypeAndKind = "lsTypeAndKind_" + index;
        if (lsTypeAndKind.length() > 255) {
            lsTypeAndKind = lsTypeAndKind.substring(0, 255);
        }
        obj.setLsTypeAndKind(lsTypeAndKind);
    }

	public void setModifiedDate(ContainerLabel obj, int index) {
        Date modifiedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setModifiedDate(modifiedDate);
    }

	public void setPhysicallyLabled(ContainerLabel obj, int index) {
        Boolean physicallyLabled = true;
        obj.setPhysicallyLabled(physicallyLabled);
    }

	public void setPreferred(ContainerLabel obj, int index) {
        Boolean preferred = true;
        obj.setPreferred(preferred);
    }

	public void setRecordedBy(ContainerLabel obj, int index) {
        String recordedBy = "recordedBy_" + index;
        if (recordedBy.length() > 255) {
            recordedBy = recordedBy.substring(0, 255);
        }
        obj.setRecordedBy(recordedBy);
    }

	public void setRecordedDate(ContainerLabel obj, int index) {
        Date recordedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setRecordedDate(recordedDate);
    }

	public ContainerLabel getSpecificContainerLabel(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        ContainerLabel obj = data.get(index);
        Long id = obj.getId();
        return ContainerLabel.findContainerLabel(id);
    }

	public ContainerLabel getRandomContainerLabel() {
        init();
        ContainerLabel obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return ContainerLabel.findContainerLabel(id);
    }

	public boolean modifyContainerLabel(ContainerLabel obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = ContainerLabel.findContainerLabelEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'ContainerLabel' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<ContainerLabel>();
        for (int i = 0; i < 10; i++) {
            ContainerLabel obj = getNewTransientContainerLabel(i);
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
