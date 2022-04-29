package com.labsynch.labseer.chemclasses.jchem;

import org.springframework.stereotype.Component;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.labseer.chemclasses.jchem.CmpdRegMoleculeJChemImpl;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

import chemaxon.formats.MolFormatException;

@Component
public class CmpdRegMoleculeFactoryJChemImpl implements CmpdRegMoleculeFactory {

	@Override
	public CmpdRegMolecule getCmpdRegMolecule(String molStructure) throws CmpdRegMolFormatException {
		try {
			return new CmpdRegMoleculeJChemImpl(molStructure);
		} catch (MolFormatException e) {
			throw new CmpdRegMolFormatException(e);
		}
	}

}
