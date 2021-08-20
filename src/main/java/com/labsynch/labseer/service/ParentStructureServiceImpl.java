package com.labsynch.labseer.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.MoleculeUtil;
import com.labsynch.labseer.utils.SecurityUtil;


@Service
@Transactional
public class ParentStructureServiceImpl implements ParentStructureService {

	@Autowired
	private ChemStructureService chemService;

	@Autowired
	private LotService lotService;
	
	Logger logger = LoggerFactory.getLogger(ParentStructureServiceImpl.class);


	// allows user to update the parent structure
	// update the parent.molstructure
	// update the parent_structure entity
	// update the parent.molweight
	// update the lot.molweight (debated retrieving it dynamically but downstream users may 
	//                           just want the lot molWeight info from the table)
	// update the lot.asDrawnStruct
	
	@Override
	public Parent update(Parent parent){

		try {
			parent = processAndUpdate(parent);
		} catch (CmpdRegMolFormatException e) {
			// TODO Auto-generated catch block
			logger.error("Bad molformat exception");
		} 

		return parent;

	}

	private Parent processAndUpdate(Parent inputParent) throws CmpdRegMolFormatException {
		
		Parent parent = Parent.update(inputParent);
		
		CmpdRegMolecule mol = chemService.toMolecule(inputParent.getMolStructure());
		parent.setMolStructure(mol.getMolStructure());
		parent.setMolFormula(chemService.getMolFormula(inputParent.getMolStructure()));
		boolean updateFlag = chemService.updateStructure(mol, "Parent_Structure", inputParent.getCdId());

		logger.debug("parent structure for " + parent.getCorpName() + "  was updated: " + updateFlag);
		parent.setModifiedDate(new Date());
		parent.setModifiedBy(inputParent.getModifiedBy());
		parent.setExactMass(chemService.getExactMass(inputParent.getMolStructure()));
		parent.setMolWeight(chemService.getMolWeight(inputParent.getMolStructure()));
		
		logger.debug("Parent weight: " + parent.getMolWeight());
		
		parent.merge();
		List<Lot> lots = Lot.findLotsByParent(parent).getResultList();
		logger.debug("number of lots found: " + lots.size());
		Lot updatedLot;
		for (Lot lot : lots){
			updatedLot = lotService.updateLotWeight(lot);
			updatedLot.setAsDrawnStruct(parent.getMolStructure());
			updatedLot.setModifiedDate(new Date());
			updatedLot.setModifiedBy(inputParent.getModifiedBy());
			updatedLot.merge();
		}		

		return parent;

	}


}
