// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.labsynch.labseer.domain;

import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentDataOnDemand;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.ProtocolDataOnDemand;
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
import org.springframework.stereotype.Component;

privileged aspect ExperimentDataOnDemand_Roo_DataOnDemand {
    
    declare @type: ExperimentDataOnDemand: @Component;
    
    private Random ExperimentDataOnDemand.rnd = new SecureRandom();
    
    private List<Experiment> ExperimentDataOnDemand.data;
    
    @Autowired
    ProtocolDataOnDemand ExperimentDataOnDemand.protocolDataOnDemand;
    
    public Experiment ExperimentDataOnDemand.getNewTransientExperiment(int index) {
        Experiment obj = new Experiment();
        setCodeName(obj, index);
        setDeleted(obj, index);
        setIgnored(obj, index);
        setLsKind(obj, index);
        setLsTransaction(obj, index);
        setLsType(obj, index);
        setLsTypeAndKind(obj, index);
        setModifiedBy(obj, index);
        setModifiedDate(obj, index);
        setProtocol(obj, index);
        setRecordedBy(obj, index);
        setRecordedDate(obj, index);
        setShortDescription(obj, index);
        return obj;
    }
    
    public void ExperimentDataOnDemand.setCodeName(Experiment obj, int index) {
        String codeName = "codeName_" + index;
        if (codeName.length() > 255) {
            codeName = new Random().nextInt(10) + codeName.substring(1, 255);
        }
        obj.setCodeName(codeName);
    }
    
    public void ExperimentDataOnDemand.setDeleted(Experiment obj, int index) {
        Boolean deleted = true;
        obj.setDeleted(deleted);
    }
    
    public void ExperimentDataOnDemand.setIgnored(Experiment obj, int index) {
        Boolean ignored = true;
        obj.setIgnored(ignored);
    }
    
    public void ExperimentDataOnDemand.setLsKind(Experiment obj, int index) {
        String lsKind = "lsKind_" + index;
        if (lsKind.length() > 255) {
            lsKind = lsKind.substring(0, 255);
        }
        obj.setLsKind(lsKind);
    }
    
    public void ExperimentDataOnDemand.setLsTransaction(Experiment obj, int index) {
        Long lsTransaction = new Integer(index).longValue();
        obj.setLsTransaction(lsTransaction);
    }
    
    public void ExperimentDataOnDemand.setLsType(Experiment obj, int index) {
        String lsType = "lsType_" + index;
        if (lsType.length() > 255) {
            lsType = lsType.substring(0, 255);
        }
        obj.setLsType(lsType);
    }
    
    public void ExperimentDataOnDemand.setLsTypeAndKind(Experiment obj, int index) {
        String lsTypeAndKind = "lsTypeAndKind_" + index;
        if (lsTypeAndKind.length() > 255) {
            lsTypeAndKind = lsTypeAndKind.substring(0, 255);
        }
        obj.setLsTypeAndKind(lsTypeAndKind);
    }
    
    public void ExperimentDataOnDemand.setModifiedBy(Experiment obj, int index) {
        String modifiedBy = "modifiedBy_" + index;
        if (modifiedBy.length() > 255) {
            modifiedBy = modifiedBy.substring(0, 255);
        }
        obj.setModifiedBy(modifiedBy);
    }
    
    public void ExperimentDataOnDemand.setModifiedDate(Experiment obj, int index) {
        Date modifiedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setModifiedDate(modifiedDate);
    }
    
    public void ExperimentDataOnDemand.setProtocol(Experiment obj, int index) {
        Protocol protocol = protocolDataOnDemand.getRandomProtocol();
        obj.setProtocol(protocol);
    }
    
    public void ExperimentDataOnDemand.setRecordedBy(Experiment obj, int index) {
        String recordedBy = "recordedBy_" + index;
        if (recordedBy.length() > 255) {
            recordedBy = recordedBy.substring(0, 255);
        }
        obj.setRecordedBy(recordedBy);
    }
    
    public void ExperimentDataOnDemand.setRecordedDate(Experiment obj, int index) {
        Date recordedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setRecordedDate(recordedDate);
    }
    
    public void ExperimentDataOnDemand.setShortDescription(Experiment obj, int index) {
        String shortDescription = "shortDescription_" + index;
        if (shortDescription.length() > 1024) {
            shortDescription = shortDescription.substring(0, 1024);
        }
        obj.setShortDescription(shortDescription);
    }
    
    public Experiment ExperimentDataOnDemand.getSpecificExperiment(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Experiment obj = data.get(index);
        Long id = obj.getId();
        return Experiment.findExperiment(id);
    }
    
    public Experiment ExperimentDataOnDemand.getRandomExperiment() {
        init();
        Experiment obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Experiment.findExperiment(id);
    }
    
    public boolean ExperimentDataOnDemand.modifyExperiment(Experiment obj) {
        return false;
    }
    
    public void ExperimentDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Experiment.findExperimentEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Experiment' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Experiment>();
        for (int i = 0; i < 10; i++) {
            Experiment obj = getNewTransientExperiment(i);
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
