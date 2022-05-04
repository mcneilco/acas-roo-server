package com.labsynch.labseer.exceptions;

public class CmpdRegMolFormatException extends Exception {

	public CmpdRegMolFormatException(Exception e) {
		super(e.getMessage());
		this.setStackTrace(e.getStackTrace());
	}

	public CmpdRegMolFormatException() {
		// TODO Auto-generated constructor stub
	}

	public CmpdRegMolFormatException(String message) {
		super(message);
	}

}
