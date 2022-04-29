package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegMoleculeFactory;
import com.labsynch.labseer.domain.IsoSalt;
import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.ParentAlias;
import com.labsynch.labseer.domain.SaltForm;
import com.labsynch.labseer.dto.ParentAliasDTO;
import com.labsynch.labseer.dto.ParentDTO;
import com.labsynch.labseer.dto.RegSearchDTO;
import com.labsynch.labseer.dto.RegSearchParamsDTO;
import com.labsynch.labseer.dto.SaltFormDTO;
import com.labsynch.labseer.exceptions.CmpdRegMolFormatException;
import com.labsynch.labseer.service.ChemStructureService.SearchType;
import com.labsynch.labseer.service.ChemStructureService.StructureType;
import com.labsynch.labseer.utils.MoleculeUtil;
import com.labsynch.labseer.utils.PropertiesUtilService;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegSearchServiceImpl implements RegSearchService {

	Logger logger = LoggerFactory.getLogger(RegSearchServiceImpl.class);

	@Autowired
	private ChemStructureService chemStructureService;

	@Autowired
	CmpdRegMoleculeFactory cmpdRegMoleculeFactory;

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Autowired
	private CorpNameService corpNameService;

	@Override
	@Transactional
	public RegSearchDTO getParentsbyParams(String searchParamsString) throws IOException, CmpdRegMolFormatException {
		RegSearchParamsDTO searchParams = RegSearchParamsDTO.fromJsonToRegSearchParamsDTO(searchParamsString);
		String corpName = searchParams.getCorpName();
		String molStructure = searchParams.getMolStructure();
		logger.debug("search corpName: " + corpName);
		logger.debug("search molStructure: " + molStructure);
		logger.debug("using the /regsearches/parent controller service implementation");

		RegSearchDTO regSearchDTO = new RegSearchDTO();
		List<Parent> parents = new ArrayList<Parent>();
		if (corpName.equalsIgnoreCase("null") || corpName.equalsIgnoreCase("")) {
			// corpName not supplied -- so search by structure
			System.out.println("search with structure: " + molStructure);

			CmpdRegMolecule mol = cmpdRegMoleculeFactory.getCmpdRegMolecule(molStructure);

			String format = "png";
			String wSize = "600";
			String hSize = "300";

			regSearchDTO.setAsDrawnImage(convertToBase64(mol, format, hSize, wSize));
			regSearchDTO.setAsDrawnStructure(mol.getMolStructure());
			regSearchDTO.setAsDrawnMolFormula(mol.getFormula());
			regSearchDTO.setAsDrawnExactMass(mol.getExactMass());
			if (propertiesUtilService.getUseExactMass()) {
				regSearchDTO.setAsDrawnMolWeight(mol.getExactMass());

			} else {
				regSearchDTO.setAsDrawnMolWeight(mol.getMass());

			}
			int[] parentHits = chemStructureService.searchMolStructures(MoleculeUtil.exportMolAsText(mol, "mol"),
					StructureType.PARENT, SearchType.FULL_TAUTOMER);
			logger.debug("number of parents found: " + parentHits.length);

			if (parentHits.length > 0) {
				for (int hit : parentHits) {
					List<Parent> hitList = Parent.findParentsByCdId(hit).getResultList();
					parents.addAll(hitList);
				}
			}

			regSearchDTO = this.populateFromParents(regSearchDTO, parents);

		} else {
			System.out.println("search with corpName: " + corpName);
			regSearchDTO.setAsDrawnStructure("");

			boolean saltBeforeLot = propertiesUtilService.getSaltBeforeLot();

			List<Parent> parentList;
			if (corpNameService.checkParentCorpName(corpName)) {
				logger.debug("corpName looks like a parent corpName: " + corpName);

				parentList = Parent.findParentsByCorpNameEquals(corpName).getResultList();
				regSearchDTO = this.populateFromParents(regSearchDTO, parentList);

			} else if (corpNameService.checkCorpNumber(corpName)) {
				logger.debug("corpName looks like a corp number: " + corpName);

				corpName = corpNameService.convertCorpNameNumber(corpName);
				parentList = Parent.findParentsByCorpNameEquals(corpName).getResultList();
				regSearchDTO = this.populateFromParents(regSearchDTO, parentList);

			} else if (corpNameService.checkSaltFormCorpName(corpName) && saltBeforeLot) {
				logger.debug("corpName looks like a saltForm: " + corpName);
				SaltForm saltForm = SaltForm.findSaltFormsByCorpNameEquals(corpName).getSingleResult();
				regSearchDTO = this.populateFromSaltForm(regSearchDTO, saltForm);

			} else if (corpNameService.checkLotCorpName(corpName)) {
				logger.debug("corpName looks like a lot: " + corpName);
				List<Lot> lots = Lot.findLotsByCorpNameEquals(corpName.trim()).getResultList();
				logger.debug("number of lots found: " + lots.size());
				Lot lot = lots.get(0);
				SaltForm saltForm = lot.getSaltForm();
				regSearchDTO = this.populateFromSaltForm(regSearchDTO, saltForm);

			} else {
				logger.debug("corpName looks like other parent corpName: " + corpName);
				parentList = Parent.findParentsByCorpNameEquals(corpName).getResultList();
				regSearchDTO = this.populateFromParents(regSearchDTO, parentList);
			}

			logger.debug("Converted corpName = " + corpName);

		}

		return regSearchDTO;
	}

	private boolean isNumber(String corpName) {
		try {
			Integer.parseInt(corpName.trim());
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	private String convertToBase64(CmpdRegMolecule mol, String imageFormat, String hSize, String wSize)
			throws IOException {
		byte[] imageData = mol.toBinary(mol, imageFormat, hSize, wSize);
		byte[] encodedBytes = Base64.encodeBase64(imageData);
		return new String(encodedBytes);
	}

	private String convertToBase64(byte[] image_data) {
		byte[] encodedBytes = Base64.encodeBase64(image_data);
		return new String(encodedBytes);
	}

	private RegSearchDTO populateFromSaltForm(RegSearchDTO regSearchDTO, SaltForm saltForm) {
		Parent parent = saltForm.getParent();
		ParentDTO parentDTO = new ParentDTO();
		parentDTO.setParent(parent);
		Set<ParentAlias> parentAliases = parent.getParentAliases();
		for (ParentAlias parentAlias : parentAliases) {
			ParentAliasDTO parentAliasDTO = new ParentAliasDTO(parentAlias);
			parentDTO.getParentAliases().add(parentAliasDTO);
		}
		List<IsoSalt> isosalts = IsoSalt.findIsoSaltsBySaltForm(saltForm).getResultList();
		SaltFormDTO saltFormDTO = new SaltFormDTO();
		saltFormDTO.setSaltForm(saltForm);
		saltFormDTO.getIsosalts().addAll(isosalts);
		parentDTO.getSaltForms().add(saltFormDTO);
		regSearchDTO.getParents().add(parentDTO);
		return regSearchDTO;
	}

	private RegSearchDTO populateFromParents(RegSearchDTO regSearchDTO, List<Parent> parentList) {
		logger.debug("number of parents found: " + parentList.size());
		for (Parent parent : parentList) {
			System.out.println("name of parents found: " + parent.getCorpName());
			ParentDTO parentDTO = new ParentDTO();
			parentDTO.setParent(parent);
			List<SaltForm> saltForms = SaltForm.findSaltFormsByParent(parent).getResultList();
			for (SaltForm saltForm : saltForms) {
				List<IsoSalt> isosalts = IsoSalt.findIsoSaltsBySaltForm(saltForm).getResultList();
				SaltFormDTO saltFormDTO = new SaltFormDTO();
				saltFormDTO.setSaltForm(saltForm);
				saltFormDTO.getIsosalts().addAll(isosalts);
				parentDTO.getSaltForms().add(saltFormDTO);
			}
			Set<ParentAlias> parentAliases = parent.getParentAliases();
			for (ParentAlias parentAlias : parentAliases) {
				ParentAliasDTO parentAliasDTO = new ParentAliasDTO(parentAlias);
				parentDTO.getParentAliases().add(parentAliasDTO);
			}
			regSearchDTO.getParents().add(parentDTO);
		}

		return regSearchDTO;
	}

}
