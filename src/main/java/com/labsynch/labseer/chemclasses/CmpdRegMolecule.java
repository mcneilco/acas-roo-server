package com.labsynch.labseer.chemclasses;

import java.io.IOException;

import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

import org.springframework.stereotype.Component;

@Component
public interface CmpdRegMolecule {

	public void setProperty(String key, String value);

	public String getProperty(String key);

	public String[] getPropertyKeys();

	public String getPropertyType(String key);

	public String getMolStructure() throws CmpdRegMolFormatException;

	public String getFormula();

	public Double getExactMass();

	public Double getMass();

	public int getTotalCharge();

	public String getSmiles();

	public CmpdRegMolecule replaceStructure(String newStructure) throws CmpdRegMolFormatException;

	public String getMrvStructure();

	public byte[] toBinary(CmpdRegMolecule molecule, String imageFormat, String hSize, String wSize) throws IOException;

	public void dearomatize();

	public StandardizationStatus getStandardizationStatus();

	public enum StandardizationStatus {
		SUCCESS,
		ERROR,
		WARNING
	}

	public String getStandardizationComment();

	public RegistrationStatus getRegistrationStatus();

	public enum RegistrationStatus {
		SUCCESS,
		ERROR,
		WARNING
	}

	public String getRegistrationComment();

}
