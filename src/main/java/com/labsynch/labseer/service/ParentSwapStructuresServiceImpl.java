package com.labsynch.labseer.service;

import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.dto.ParentDTO;
import com.labsynch.labseer.dto.ParentSwapStructuresDTO;
import com.labsynch.labseer.dto.ParentValidationDTO;

@Service
public class ParentSwapStructuresServiceImpl implements ParentSwapStructuresService {

    Logger logger = LoggerFactory.getLogger(ParentSwapStructuresServiceImpl.class);

    @Autowired
    public ParentService parentService;

    @Override
    public boolean swapParentStructures(ParentSwapStructuresDTO parentSwapStructuresDTO) {
        String corpName1 = parentSwapStructuresDTO.getCorpName1();
		String corpName2 = parentSwapStructuresDTO.getCorpName2();

		Parent parent1;
		Parent parent2;
		try {
			parent1 = Parent.findParentsByCorpNameEquals(corpName1).getSingleResult();
			parent2 = Parent.findParentsByCorpNameEquals(corpName2).getSingleResult();
		} catch (Exception e) {
			logger.error("Caught error while fetching parent", e);
            return false;
		}

		ParentValidationDTO validationDTO1;
		ParentValidationDTO validationDTO2;
		try {
			validationDTO1 = parentService.validateUniqueParent(parent1);
			validationDTO2 = parentService.validateUniqueParent(parent2);
		} catch (Exception e) {
			logger.error("Caught error trying to validate parent", e);
			return false;
		}

		// In case of any valdation errors, abort.
		if (validationDTO1.hasErrors()) {
			logger.error("validationDTO1 has errors", validationDTO1.getErrors());
			return false;
		} else if (validationDTO2.hasErrors()) {
			logger.error("validationDTO2 has errors", validationDTO2.getErrors());
			return false;
		}

		// Check if they are duplicates of each other.
		Collection<ParentDTO> dupeParents1 = validationDTO1.getDupeParents();
		Collection<ParentDTO> dupeParents2 = validationDTO1.getDupeParents();

		boolean canSwap = false;
		if (validationDTO1.isParentUnique() && validationDTO2.isParentUnique()) {
			canSwap = true;
		} else if (
			dupeParents1.size() == 1 &&
			dupeParents2.size() == 1 &&
			dupeParents1.iterator().next().getCorpName() == dupeParents2.iterator().next().getCorpName()) {
			canSwap = true;
		}

		if (canSwap) {
			String mol = parent1.getMolStructure();
			parent1.setMolStructure(parent2.getMolStructure());
			parent2.setMolStructure(mol);
		} else {
			logger.error(String.format("corpName1=%s & corpName2=%s have other duplicates.", corpName1, corpName2));
			return false;
		}

		if (canSwap) {
			try {
				parentService.updateParent(parent1);
				parentService.updateParent(parent2);
			} catch (Exception e) {
				logger.error("Caught error trying to update parents", e);
				return false;
			}
		}
		return canSwap;
    }

}
