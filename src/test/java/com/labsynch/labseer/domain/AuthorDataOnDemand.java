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

@Component
@Configurable
public class AuthorDataOnDemand {

    private Random rnd = new SecureRandom();

    private List<Author> data;

    public Author getNewTransientAuthor(int index) {
        Author obj = new Author();
        setActivationDate(obj, index);
        setActivationKey(obj, index);
        setCodeName(obj, index);
        setDeleted(obj, index);
        setEmailAddress(obj, index);
        setEnabled(obj, index);
        setFirstName(obj, index);
        setIgnored(obj, index);
        setLastName(obj, index);
        setLocked(obj, index);
        setLsKind(obj, index);
        setLsTransaction(obj, index);
        setLsType(obj, index);
        setLsTypeAndKind(obj, index);
        setModifiedBy(obj, index);
        setModifiedDate(obj, index);
        setPassword(obj, index);
        setRecordedBy(obj, index);
        setRecordedDate(obj, index);
        setUserName(obj, index);
        return obj;
    }

    public void setActivationDate(Author obj, int index) {
        Date activationDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE),
                Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setActivationDate(activationDate);
    }

    public void setActivationKey(Author obj, int index) {
        String activationKey = "activationKey_" + index;
        obj.setActivationKey(activationKey);
    }

    public void setCodeName(Author obj, int index) {
        String codeName = "codeName_" + index;
        if (codeName.length() > 255) {
            codeName = new Random().nextInt(10) + codeName.substring(1, 255);
        }
        obj.setCodeName(codeName);
    }

    public void setDeleted(Author obj, int index) {
        Boolean deleted = true;
        obj.setDeleted(deleted);
    }

    public void setEmailAddress(Author obj, int index) {
        String emailAddress = "foo" + index + "@bar.com";
        if (emailAddress.length() > 255) {
            emailAddress = emailAddress.substring(0, 255);
        }
        obj.setEmailAddress(emailAddress);
    }

    public void setEnabled(Author obj, int index) {
        Boolean enabled = Boolean.TRUE;
        obj.setEnabled(enabled);
    }

    public void setFirstName(Author obj, int index) {
        String firstName = "firstName_" + index;
        if (firstName.length() > 255) {
            firstName = firstName.substring(0, 255);
        }
        obj.setFirstName(firstName);
    }

    public void setIgnored(Author obj, int index) {
        Boolean ignored = true;
        obj.setIgnored(ignored);
    }

    public void setLastName(Author obj, int index) {
        String lastName = "lastName_" + index;
        if (lastName.length() > 255) {
            lastName = lastName.substring(0, 255);
        }
        obj.setLastName(lastName);
    }

    public void setLocked(Author obj, int index) {
        Boolean locked = Boolean.TRUE;
        obj.setLocked(locked);
    }

    public void setLsKind(Author obj, int index) {
        String lsKind = "lsKind_" + index;
        if (lsKind.length() > 255) {
            lsKind = lsKind.substring(0, 255);
        }
        obj.setLsKind(lsKind);
    }

    public void setLsTransaction(Author obj, int index) {
        Long lsTransaction = new Integer(index).longValue();
        obj.setLsTransaction(lsTransaction);
    }

    public void setLsType(Author obj, int index) {
        String lsType = "lsType_" + index;
        if (lsType.length() > 255) {
            lsType = lsType.substring(0, 255);
        }
        obj.setLsType(lsType);
    }

    public void setLsTypeAndKind(Author obj, int index) {
        String lsTypeAndKind = "lsTypeAndKind_" + index;
        if (lsTypeAndKind.length() > 255) {
            lsTypeAndKind = lsTypeAndKind.substring(0, 255);
        }
        obj.setLsTypeAndKind(lsTypeAndKind);
    }

    public void setModifiedBy(Author obj, int index) {
        String modifiedBy = "modifiedBy_" + index;
        if (modifiedBy.length() > 255) {
            modifiedBy = modifiedBy.substring(0, 255);
        }
        obj.setModifiedBy(modifiedBy);
    }

    public void setModifiedDate(Author obj, int index) {
        Date modifiedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE),
                Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setModifiedDate(modifiedDate);
    }

    public void setPassword(Author obj, int index) {
        String password = "password_" + index;
        if (password.length() > 255) {
            password = password.substring(0, 255);
        }
        obj.setPassword(password);
    }

    public void setRecordedBy(Author obj, int index) {
        String recordedBy = "recordedBy_" + index;
        if (recordedBy.length() > 255) {
            recordedBy = recordedBy.substring(0, 255);
        }
        obj.setRecordedBy(recordedBy);
    }

    public void setRecordedDate(Author obj, int index) {
        Date recordedDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE),
                Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setRecordedDate(recordedDate);
    }

    public void setUserName(Author obj, int index) {
        String userName = "userName_" + index;
        if (userName.length() > 255) {
            userName = userName.substring(0, 255);
        }
        obj.setUserName(userName);
    }

    public Author getSpecificAuthor(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Author obj = data.get(index);
        Long id = obj.getId();
        return Author.findAuthor(id);
    }

    public Author getRandomAuthor() {
        init();
        Author obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Author.findAuthor(id);
    }

    public boolean modifyAuthor(Author obj) {
        return false;
    }

    public void init() {
        int from = 0;
        int to = 10;
        data = Author.findAuthorEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Author' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }

        data = new ArrayList<Author>();
        for (int i = 0; i < 10; i++) {
            Author obj = getNewTransientAuthor(i);
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
