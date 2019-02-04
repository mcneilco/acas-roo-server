package com.labsynch.labseer.chemclasses;

import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;


public interface CmpdRegMoleculeFactory {
	
	public CmpdRegMolecule getCmpdRegMolecule(String molStructure) throws CmpdRegMolFormatException;

}
