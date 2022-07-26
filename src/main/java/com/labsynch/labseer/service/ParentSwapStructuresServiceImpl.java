package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.dto.ParentSwapStructuresDTO;
import com.labsynch.labseer.dto.ParentValidationDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.service.ChemStructureService.StructureType;

@Service
public class ParentSwapStructuresServiceImpl implements ParentSwapStructuresService {

    Logger logger = LoggerFactory.getLogger(ParentSwapStructuresServiceImpl.class);

    @Autowired
    public ParentService parentService;

	@Autowired
	public ChemStructureService chemStructureService;

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

		// Check if there are existing errors or duplicates.
		ParentValidationDTO validationDTO1;
		ParentValidationDTO validationDTO2;
		try {
			validationDTO1 = parentService.validateUniqueParent(parent1);
			validationDTO2 = parentService.validateUniqueParent(parent2);
		} catch (Exception e) {
			logger.error("Caught error while trying to validate parent", e);
			return false;
		}
		if (validationDTO1.hasErrors()) {
			logger.error(String.format("%s has existing errors: %s", corpName1, validationDTO1.getErrors()));
			return false;
		} else if (validationDTO2.hasErrors()) {
			logger.error(String.format("%s has existing errors: %s", corpName2, validationDTO2.getErrors()));
			return false;
		} else if (!validationDTO1.isParentUnique() || !validationDTO2.isParentUnique()) {
			logger.error(String.format("%s or %s have existing duplicates.", corpName1, corpName2));
			return false;
		}

		// Check if duplicates get introduced on swapping the structures.
		HashSet<String> dupeParents1;
		HashSet<String> dupeParents2;
		try {
			dupeParents1 = getParents(parent2.getMolStructure(), parent1.getStereoCategory().getCode(), parent1.getStereoComment());
			dupeParents2 = getParents(parent1.getMolStructure(), parent2.getStereoCategory().getCode(), parent2.getStereoComment());
		} catch (Exception e) {
			// In case of any exceptions, abort the swapping.
			logger.error("Error finding parents to check for duplication", e);
			return false;
		}
		// Duplicate with the items being swapped is okay.
		dupeParents1.remove(corpName1);
		dupeParents1.remove(corpName2);
		dupeParents2.remove(corpName1);
		dupeParents2.remove(corpName2);
		if (dupeParents1.isEmpty() && dupeParents2.isEmpty()) {
			String mol = parent1.getMolStructure();
			parent1.setMolStructure(parent2.getMolStructure());
			parent1.setModifiedDate(new Date());
			parent1.setModifiedBy(parentSwapStructuresDTO.getUsername());
			parent2.setMolStructure(mol);
			parent2.setModifiedDate(new Date());
			parent2.setModifiedBy(parentSwapStructuresDTO.getUsername());
			logger.info(String.format("Swapping corpName1=%s & corpName2=%s swap successful.", corpName1, corpName2));
			return true;
		} else {
			logger.error(String.format("Swapping corpName1=%s & corpName2=%s creates duplicates.", corpName1, corpName2));
			return false;
		}
    }

	private HashSet<String> getParents(String molStructure, String stereoCategory, String stereoComment) throws CmpdRegMolFormatException {
		ArrayList<Parent> matchingParents = new ArrayList<>();
		int[] parentCdIds = chemStructureService.checkDupeMol(molStructure, StructureType.PARENT);
		for (int parentCdId : parentCdIds) {
			for (Parent parent : Parent.findParentsByCdId(parentCdId).getResultList()) {
				String parentStereoCategory = parent.getStereoCategory().getCode();
				if (!parentStereoCategory.equalsIgnoreCase(stereoCategory)) {
					continue;
				}
				String parentStereoComment = parent.getStereoComment();
				if (
					(parentStereoComment == null || parentStereoComment.isEmpty()) &&
					(stereoComment == null || stereoComment.isEmpty()) ||
					(parentStereoComment != null && stereoComment != null && parentStereoComment.equalsIgnoreCase(stereoComment))) {
					matchingParents.add(parent);
				}
			}
		}
		HashSet<String> matchingParentCorpNames = new HashSet<>();
		for (Parent parent : matchingParents) {
			matchingParentCorpNames.add(parent.getCorpName());
		}
		return matchingParentCorpNames;
	}
}
