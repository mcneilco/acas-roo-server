package com.labsynch.labseer.exceptions;

import java.util.List;

public class DupeParentException extends Exception {
	
	String dbCorpName;
	
	String sdfCorpName;
	
	List<String> aliasCorpNames;
	
    public DupeParentException() {
	// TODO Auto-generated constructor stub
    }
 
    public DupeParentException(String message) {
	super(message);
	// TODO Auto-generated constructor stub
    }
 
    public DupeParentException(Throwable cause) {
	super(cause);
	// TODO Auto-generated constructor stub
    }
 
    public DupeParentException(String message, Throwable cause) {
	super(message, cause);
	// TODO Auto-generated constructor stub
    }
    
    public DupeParentException(String message, String dbCorpName, String sdfCorpName, List<String> aliasCorpNames) {
    	super(message);
    	this.dbCorpName = dbCorpName;
    	this.sdfCorpName = sdfCorpName;
    	this.aliasCorpNames = aliasCorpNames;
        }
    
    public String getDbCorpName(){
    	return this.dbCorpName;
    }
    
    public String getSdfCorpName(){
    	return this.sdfCorpName;
    }
    
    public List<String> getAliasCorpNames(){
    	return this.aliasCorpNames;
    }

}
