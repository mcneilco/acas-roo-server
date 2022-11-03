package com.labsynch.labseer.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.ParentAlias;
import com.labsynch.labseer.dto.ParentSwapStructuresDTO;
import com.labsynch.labseer.dto.ParentValidationDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.exceptions.ParentNotFoundException;
import com.labsynch.labseer.service.ChemStructureService.StructureType;

@Service
public class ParentSwapStructuresServiceImpl implements ParentSwapStructuresService {

    Logger logger = LoggerFactory.getLogger(ParentSwapStructuresServiceImpl.class);

    @Autowired
    public ParentService parentService;

	@Autowired
	public ParentStructureService parentStructureService;

	@Autowired
	public ChemStructureService chemStructureService;

	/**
	 * Get parent matching the name. If no parent is found then check if the name
	 * matches a unique alias and if a match is found then return the parent.
	 *
	 * @param name Parent corporate name or parent alias name.
	 * @return   Parent object.
	 */
	private Parent getParent(String name) throws ParentNotFoundException {
		// Check if a parent with the corporate name exists.
		try {
			return Parent.findParentsByCorpNameEquals(name).getSingleResult();
		} catch (Exception e) {
			logger.warn(String.format("No parent found for corporate id - '%s'", name), e);
		}

		// Check if a unique alias with the name exists.
		List<ParentAlias> parentAliases = new ArrayList<>();
		try {
			parentAliases = ParentAlias.findParentAliasesByAliasNameEquals(name).getResultList();
		} catch (Exception e) {
			logger.error(String.format("No parent aliases for alias name - '%s'", name), e);
		}

		if (parentAliases.size() == 1) {
			ParentAlias parentAlias = parentAliases.get(0);
			logger.info("Alias name '{}' uniquely maps to corporate id '{}'", name, parentAlias.getParent().getCorpName());
			return parentAlias.getParent();
		}

		String errorMsg;
		if (parentAliases.size() == 0) {
			errorMsg = String.format("'%s' was not found as a corporate id or as an alias", name);
		} else {
			ArrayList<String> corporateIDs = new ArrayList<>();
			for (ParentAlias parentAlias : parentAliases) {
				corporateIDs.add(parentAlias.getParent().getCorpName());
			}
			Collections.sort(corporateIDs);
			errorMsg = String.format("Alias name '%s' has multiple corporate id matches: %s", name, String.join(", ", corporateIDs));
		}
		throw new ParentNotFoundException(errorMsg);
	}

    @Override
    public String swapParentStructures(ParentSwapStructuresDTO parentSwapStructuresDTO) {
		String corpName1 = parentSwapStructuresDTO.getCorpName1();
		String corpName2 = parentSwapStructuresDTO.getCorpName2();

		Parent parent1;
		Parent parent2;
		try {
			parent1 = this.getParent(corpName1);
			parent2 = this.getParent(corpName2);
		} catch (Exception e) {
			String msg = e.getMessage();
			logger.error(msg, e);
			return msg;
		}

		corpName1 = parent1.getCorpName();
		corpName2 = parent2.getCorpName();

		// Check if there are existing errors or duplicates.
		ParentValidationDTO validationDTO1;
		ParentValidationDTO validationDTO2;
		try {
			validationDTO1 = parentService.validateUniqueParent(parent1);
			validationDTO2 = parentService.validateUniqueParent(parent2);
		} catch (Exception e) {
			String msg = "Caught error while trying to validate parent";
			logger.error(msg, e);
			return msg;
		}

		if (validationDTO1.hasErrors()) {
			return String.format("%s has existing errors: %s", corpName1, validationDTO1.getErrors());
		} else if (validationDTO2.hasErrors()) {
			return String.format("%s has existing errors: %s", corpName2, validationDTO2.getErrors());
		} else if (!validationDTO1.isParentUnique() || !validationDTO2.isParentUnique()) {
			return String.format("%s or %s have existing duplicates.", corpName1, corpName2);
		}

		// Check if duplicates get introduced on swapping the structures.
		HashSet<String> dupeParents1;
		HashSet<String> dupeParents2;
		try {
			dupeParents1 = getParents(parent2.getMolStructure(), parent1.getStereoCategory().getCode(), parent1.getStereoComment());
			dupeParents2 = getParents(parent1.getMolStructure(), parent2.getStereoCategory().getCode(), parent2.getStereoComment());
		} catch (Exception e) {
			// In case of any exceptions, abort the swapping.
			String msg = "Error finding parents to check for duplication";
			logger.error(msg, e);
			return msg;
		}
		// Duplicate with the items being swapped is okay.
		dupeParents1.remove(corpName1);
		dupeParents1.remove(corpName2);
		dupeParents2.remove(corpName1);
		dupeParents2.remove(corpName2);
		if (dupeParents1.isEmpty() && dupeParents2.isEmpty()) {
			// Swap the structures
			String mol = parent1.getMolStructure();
			parent1.setMolStructure(parent2.getMolStructure());
			parent2.setMolStructure(mol);
			// Follow through with updates to formula, mol weight, lot mol weights, and structure tables
			parent1 = parentStructureService.update(parent1);
			parent2 = parentStructureService.update(parent2);
			logger.info(String.format("Swapping corpName1=%s & corpName2=%s swap successful.", corpName1, corpName2));
			return "";
		} else {
			String msg = String.format("Swapping corpName1=%s & corpName2=%s creates duplicates.", corpName1, corpName2);
			logger.error(msg);
			return msg;
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
