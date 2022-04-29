package com.labsynch.labseer.exceptions;

public class StructureSaveException extends Exception {

	public StructureSaveException(Exception e) {
		super(e.getMessage());
		this.setStackTrace(e.getStackTrace());
	}

	public StructureSaveException() {
		// TODO Auto-generated constructor stub
	}

	public StructureSaveException(String message) {
		super(message);
	}

}
