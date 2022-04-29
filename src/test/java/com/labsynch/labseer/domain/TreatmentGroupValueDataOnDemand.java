package com.labsynch.labseer.domain;

import java.math.BigDecimal;
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

@Component
@Configurable
public class TreatmentGroupValueDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<TreatmentGroupValue> data;

	@Autowired
    TreatmentGroupStateDataOnDemand treatmentGroupStateDataOnDemand;

	public TreatmentGroupValue getNewTransientTreatmentGroupValue(int index) {
        TreatmentGroupValue obj = new TreatmentGroupValue();
        setBlobValue(obj, index);
        setClobValue(obj, index);
        setCodeKind(obj, index);
        setCodeOrigin(obj, index);
        setCodeType(obj, index);
        setCodeTypeAndKind(obj, index);
        setCodeValue(obj, index);
        setComments(obj, index);
        setConcUnit(obj, index);
        setConcentration(obj, index);
        setDateValue(obj, index);
        setDeleted(obj, index);
        setFileValue(obj, index);
        setIgnored(obj, index);
        setLsKind(obj, index);
        setLsState(obj, index);
        setLsTransaction(obj, index);
        setLsType(obj, index);
        setLsTypeAndKind(obj, index);
        setModifiedBy(obj, index);
        setModifiedDate(obj, index);
        setNumberOfReplicates(obj, index);
        setNumericValue(obj, index);
        setOperatorKind(obj, index);
        setOperatorType(obj, index);
        setOperatorTypeAndKind(obj, index);
        setPublicData(obj, index);
        setRecordedBy(obj, index);
        setRecordedDate(obj, index);
        setSigFigs(obj, index);
        setStringValue(obj, index);
        setUncertainty(obj, index);
        setUncertaintyType(obj, index);
        setUnitKind(obj, index);
        setUnitType(obj, index);
        setUnitTypeAndKind(obj, index);
        setUrlValue(obj, index);
        return obj;
    }

	public void setBlobValue(TreatmentGroupValue obj, int index) {
        byte[] blobValue = String.valueOf(index).getBytes();
        obj.setBlobValue(blobValue);
    }

	public void setClobValue(TreatmentGroupValue obj, int index) {
        String clobValue = "clobValue_" + index;
        obj.setClobValue(clobValue);
    }

	public void setCodeKind(TreatmentGroupValue obj, int index) {
        String codeKind = "codeKind_" + index;
        if (codeKind.length() > 255) {
            codeKind = codeKind.substring(0, 255);
        }
        obj.setCodeKind(codeKind);
    }

	public void setCodeOrigin(TreatmentGroupValue obj, int index) {
        String codeOrigin = "codeOrigin_" + index;
        if (codeOrigin.length() > 255) {
            codeOrigin = codeOrigin.substring(0, 255);
        }
        obj.setCodeOrigin(codeOrigin);
    }

	public void setCodeType(TreatmentGroupValue obj, int index) {
        String codeType = "codeType_" + index;
        if (codeType.length() > 255) {
            codeType = codeType.substring(0, 255);
        }
        obj.setCodeType(codeType);
    }

	public void setCodeTypeAndKind(TreatmentGroupValue obj, int index) {
        String codeTypeAndKind = "codeTypeAndKind_" + index;
        if (codeTypeAndKind.length() > 350) {
            codeTypeAndKind = codeTypeAndKind.substring(0, 350);
        }
        obj.setCodeTypeAndKind(codeTypeAndKind);
    }

	public void setCodeValue(TreatmentGroupValue obj, int index) {
        String codeValue = "codeValue_" + index;
        if (codeValue.length() > 255) {
            codeValue = codeValue.substring(0, 255);
        }
        obj.setCodeValue(codeValue);
    }

	public void setComments(TreatmentGroupValue obj, int index) {
        String comments = "comments_" + index;
        if (comments.length() > 512) {
            comments = comments.substring(0, 512);
        }
        obj.setComments(comments);
    }

	public void setConcUnit(TreatmentGroupValue obj, int index) {
        String concUnit = "concUnit_" + index;
        if (concUnit.length() > 25) {
            concUnit = concUnit.substring(0, 25);
        }
        obj.setConcUnit(concUnit);
    }

	public void setConcentration(TreatmentGroupValue obj, int index) {
        Double concentration = new Integer(index).doubleValue();
        obj.setConcentration(concentration);
    }

	public void setDateValue(TreatmentGroupValue obj, int index) {
        Date dateValue = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateValue(dateValue);
    }

	public void setDeleted(TreatmentGroupValue obj, int index) {
        Boolean deleted = true;
        obj.setDeleted(deleted);
    }

	public void setFileValue(TreatmentGroupValue obj, int index) {
        String fileValue = "fileValue_" + index;
        if (fileValue.length() > 512) {
            fileValue = fileValue.substring(0, 512);
        }
        obj.setFileValue(fileValue);
    }

	public void setIgnored(TreatmentGroupValue obj, int index) {
        Boolean ignored = true;
        obj.setIgnored(ignored);
    }

	public void setLsKind(TreatmentGroupValue obj, int index) {
        String lsKind = "lsKind_" + index;
        if (lsKind.length() > 255) {
            lsKind = lsKind.substring(0, 255);
        }
        obj.setLsKind(lsKind);
    }

	public void setLsState(TreatmentGroupValue obj, int index) {
        TreatmentGroupState lsState = treatmentGroupStateDataOnDemand.getRandomTreatmentGroupState();
        obj.setLsState(lsState);
    }

	public void setLsTransaction(TreatmentGroupValue obj, int index) {
        Long lsTransaction = new Integer(index).longValue();
        obj.setLsTransaction(lsTransaction);
    }

	public void setLsType(TreatmentGroupValue obj, int index) {
        String lsType = "lsType_" + index;
        if (lsType.length() > 64) {
            lsType = lsType.substring(0, 64);
        }
        obj.setLsType(lsType);
    }

	public void setLsTypeAndKind(TreatmentGroupValue obj, int index) {
        String lsTypeAndKind = "lsTypeAndKind_" + index;
        if (lsTypeAndKind.length() > 350) {
            lsTypeAndKind = lsTypeAndKind.substring(0, 350);
        }
        obj.setLsTypeAndKind(lsTypeAndKind);
    }

	public void setModifiedBy(TreatmentGroupValue obj, int index) {
        String modifiedBy = "modifiedBy_" + index;
        if (modifiedBy.length() > 255) {
            modifiedBy = modifiedBy.substring(0, 255);
        }
        obj.setModifiedBy(modifiedBy);
    }

	public void setModifiedDate(TreatmentGroupValue obj, int index) {
        Date modifiedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setModifiedDate(modifiedDate);
    }

	public void setNumberOfReplicates(TreatmentGroupValue obj, int index) {
        Integer numberOfReplicates = new Integer(index);
        obj.setNumberOfReplicates(numberOfReplicates);
    }

	public void setNumericValue(TreatmentGroupValue obj, int index) {
        BigDecimal numericValue = BigDecimal.valueOf(index);
        if (numericValue.compareTo(new BigDecimal("99999999999999999999.999999999999999999")) == 1) {
            numericValue = new BigDecimal("99999999999999999999.999999999999999999");
        }
        obj.setNumericValue(numericValue);
    }

	public void setOperatorKind(TreatmentGroupValue obj, int index) {
        String operatorKind = "operator_" + index;
        if (operatorKind.length() > 10) {
            operatorKind = operatorKind.substring(0, 10);
        }
        obj.setOperatorKind(operatorKind);
    }

	public void setOperatorType(TreatmentGroupValue obj, int index) {
        String operatorType = "operatorType_" + index;
        if (operatorType.length() > 25) {
            operatorType = operatorType.substring(0, 25);
        }
        obj.setOperatorType(operatorType);
    }

	public void setOperatorTypeAndKind(TreatmentGroupValue obj, int index) {
        String operatorTypeAndKind = "operatorTypeAndKind_" + index;
        if (operatorTypeAndKind.length() > 50) {
            operatorTypeAndKind = operatorTypeAndKind.substring(0, 50);
        }
        obj.setOperatorTypeAndKind(operatorTypeAndKind);
    }

	public void setPublicData(TreatmentGroupValue obj, int index) {
        Boolean publicData = true;
        obj.setPublicData(publicData);
    }

	public void setRecordedBy(TreatmentGroupValue obj, int index) {
        String recordedBy = "recordedBy_" + index;
        if (recordedBy.length() > 255) {
            recordedBy = recordedBy.substring(0, 255);
        }
        obj.setRecordedBy(recordedBy);
    }

	public void setRecordedDate(TreatmentGroupValue obj, int index) {
        Date recordedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setRecordedDate(recordedDate);
    }

	public void setSigFigs(TreatmentGroupValue obj, int index) {
        Integer sigFigs = new Integer(index);
        obj.setSigFigs(sigFigs);
    }

	public void setStringValue(TreatmentGroupValue obj, int index) {
        String stringValue = "stringValue_" + index;
        if (stringValue.length() > 255) {
            stringValue = stringValue.substring(0, 255);
        }
        obj.setStringValue(stringValue);
    }

	public void setUncertainty(TreatmentGroupValue obj, int index) {
        BigDecimal uncertainty = BigDecimal.valueOf(index);
        if (uncertainty.compareTo(new BigDecimal("99999999999999999999.999999999999999999")) == 1) {
            uncertainty = new BigDecimal("99999999999999999999.999999999999999999");
        }
        obj.setUncertainty(uncertainty);
    }

	public void setUncertaintyType(TreatmentGroupValue obj, int index) {
        String uncertaintyType = "uncertaintyType_" + index;
        if (uncertaintyType.length() > 255) {
            uncertaintyType = uncertaintyType.substring(0, 255);
        }
        obj.setUncertaintyType(uncertaintyType);
    }

	public void setUnitKind(TreatmentGroupValue obj, int index) {
        String unitKind = "unitKind_" + index;
        if (unitKind.length() > 25) {
            unitKind = unitKind.substring(0, 25);
        }
        obj.setUnitKind(unitKind);
    }

	public void setUnitType(TreatmentGroupValue obj, int index) {
        String unitType = "unitType_" + index;
        if (unitType.length() > 25) {
            unitType = unitType.substring(0, 25);
        }
        obj.setUnitType(unitType);
    }

	public void setUnitTypeAndKind(TreatmentGroupValue obj, int index) {
        String unitTypeAndKind = "unitTypeAndKind_" + index;
        if (unitTypeAndKind.length() > 55) {
            unitTypeAndKind = unitTypeAndKind.substring(0, 55);
        }
        obj.setUnitTypeAndKind(unitTypeAndKind);
    }

	public void setUrlValue(TreatmentGroupValue obj, int index) {
        String urlValue = "urlValue_" + index;
        if (urlValue.length() > 2000) {
            urlValue = urlValue.substring(0, 2000);
        }
        obj.setUrlValue(urlValue);
    }

	public TreatmentGroupValue getSpecificTreatmentGroupValue(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        TreatmentGroupValue obj = data.get(index);
        Long id = obj.getId();
        return TreatmentGroupValue.findTreatmentGroupValue(id);
    }

	public TreatmentGroupValue getRandomTreatmentGroupValue() {
        init();
        TreatmentGroupValue obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return TreatmentGroupValue.findTreatmentGroupValue(id);
    }

	public boolean modifyTreatmentGroupValue(TreatmentGroupValue obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = TreatmentGroupValue.findTreatmentGroupValueEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'TreatmentGroupValue' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<TreatmentGroupValue>();
        for (int i = 0; i < 10; i++) {
            TreatmentGroupValue obj = getNewTransientTreatmentGroupValue(i);
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
