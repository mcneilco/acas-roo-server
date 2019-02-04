package com.labsynch.labseer.chemclasses.indigo;

import org.springframework.stereotype.Component;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.labseer.chemclasses.indigo.CmpdRegMoleculeIndigoImpl;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

@Component
public class CmpdRegMoleculeFactoryIndigoImpl implements CmpdRegMoleculeFactory {
	
	public CmpdRegMolecule getCmpdRegMolecule(String molStructure) throws CmpdRegMolFormatException {
		return new CmpdRegMoleculeIndigoImpl(molStructure);
	}

}
