package com.labsynch.labseer.chemclasses.rdkit;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

import org.springframework.stereotype.Component;


@Component
public class CmpdRegMoleculeFactoryRDKitImpl implements CmpdRegMoleculeFactory{

    @Override
    public CmpdRegMolecule getCmpdRegMolecule(String molStructure) throws CmpdRegMolFormatException {
		return new CmpdRegMoleculeRDKitImpl(molStructure);
    }
	

}
