package com.labsynch.labseer.exceptions;

@SuppressWarnings("serial")
public class UniqueInteractionsException extends Exception {
	
	private String matchingThingCodeName;
	
	private String matchingThingCorpName;
	
	public UniqueInteractionsException() {
    }
 
    public UniqueInteractionsException(String message) {
	super(message);
    }
    
    public UniqueInteractionsException(String message, String matchingThingCodeName, String matchingThingCorpName) {
    	super(message);
    	this.matchingThingCodeName = matchingThingCodeName;
    	this.matchingThingCorpName = matchingThingCorpName;
        }
 
    public UniqueInteractionsException(Throwable cause) {
	super(cause);
    }
 
    public UniqueInteractionsException(String message, Throwable cause) {
	super(message, cause);
    }

    public String getMatchingThingCodeName(){
    	return this.matchingThingCodeName;
    }
    
    public String getMatchingThingCorpName(){
    	return this.matchingThingCorpName;
    }

}
