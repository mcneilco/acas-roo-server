package com.labsynch.labseer.exceptions;

public class DupeLotException extends Exception {

    public DupeLotException() {
        // TODO Auto-generated constructor stub
    }

    public DupeLotException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public DupeLotException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    public DupeLotException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    private String lotCorpName;

    public DupeLotException(String message, String lotCorpName) {
        super(message);
        this.lotCorpName = lotCorpName;
    }

    public String getLotCorpName() {
        return this.lotCorpName;
    }

}
