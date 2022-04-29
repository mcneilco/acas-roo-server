package com.labsynch.labseer.exceptions;

@SuppressWarnings("serial")
public class UniqueNameException extends Exception {

    private String matchingThingCodeName;

    private String matchingThingCorpName;

    public UniqueNameException() {
    }

    public UniqueNameException(String message) {
        super(message);
    }

    public UniqueNameException(String message, String matchingThingCodeName, String matchingThingCorpName) {
        super(message);
        this.matchingThingCodeName = matchingThingCodeName;
        this.matchingThingCorpName = matchingThingCorpName;
    }

    public UniqueNameException(Throwable cause) {
        super(cause);
    }

    public UniqueNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getMatchingThingCodeName() {
        return this.matchingThingCodeName;
    }

    public String getMatchingThingCorpName() {
        return this.matchingThingCorpName;
    }

}
