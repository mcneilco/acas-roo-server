package com.labsynch.labseer.chemclasses.rdkit;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;


@Component
@Configurable
public class CmpdRegMoleculeFactoryRDKitImpl implements CmpdRegMoleculeFactory{

    @Autowired
    private BBChemStructureService bbChemStructureService;

    @Override
    public CmpdRegMolecule getCmpdRegMolecule(String molStructure) throws CmpdRegMolFormatException {
      return new CmpdRegMoleculeRDKitImpl(molStructure, bbChemStructureService);
    }
	

}
