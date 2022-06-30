package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.NoResultException;

import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.ParentAlias;
import com.labsynch.labseer.domain.SaltForm;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ParentLotCodeDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParentLotServiceImpl implements ParentLotService {

	Logger logger = LoggerFactory.getLogger(ParentLotServiceImpl.class);

	@Autowired
	private ParentService parentService;

	@Autowired
	private SaltFormService saltFormService;

	@Autowired
	private LotService lotService;

	@Override
	@Transactional
	public Collection<CodeTableDTO> getCodeTableLotsByParentCorpName(String parentCorpName) throws NoResultException {
		Parent parent = Parent.findParentsByCorpNameEquals(parentCorpName).getSingleResult();
		Collection<Lot> lots = new HashSet<Lot>();
		for (SaltForm saltForm : parent.getSaltForms()) {
			lots.addAll(saltForm.getLots());
		}
		Collection<CodeTableDTO> codeTableLots = new HashSet<CodeTableDTO>();
		for (Lot lot : lots) {
			CodeTableDTO codeTable = new CodeTableDTO();
			codeTable.setCode(lot.getCorpName());
			codeTable.setName(lot.getCorpName());
			codeTable.setComments(lot.getComments());
			codeTableLots.add(codeTable);
		}
		return codeTableLots;
	}

	@Override
	@Transactional
	public Collection<Lot> getLotsByParentCorpName(String parentCorpName) throws NoResultException {
		Parent parent = Parent.findParentsByCorpNameEquals(parentCorpName).getSingleResult();
		Collection<Lot> lots = new HashSet<Lot>();
		for (SaltForm saltForm : parent.getSaltForms()) {
			lots.addAll(saltForm.getLots());
		}
		return lots;
	}

	@Override
	@Transactional
	public Collection<ParentLotCodeDTO> getLotCodesByParentAlias(
			Collection<ParentLotCodeDTO> requestDTOs) {
		Collection<ParentLotCodeDTO> responseDTOs = new HashSet<ParentLotCodeDTO>();
		for (ParentLotCodeDTO requestDTO : requestDTOs) {
			Collection<Parent> foundParents = new HashSet<Parent>();
			try {
				Collection<ParentAlias> foundAliases = ParentAlias
						.findParentAliasesByAliasNameEquals(requestDTO.getRequestName()).getResultList();
				for (ParentAlias foundAlias : foundAliases) {
					if (foundAlias.getParent() != null)
						foundParents.add(foundAlias.getParent());
				}
				foundParents.addAll(Parent.findParentsByCorpNameEquals(requestDTO.getRequestName()).getResultList());
			} catch (Exception e) {
				// do nothing
			}
			if (!foundParents.isEmpty()) {
				for (Parent foundParent : foundParents) {
					ParentLotCodeDTO responseDTO = new ParentLotCodeDTO();
					responseDTO.setRequestName(requestDTO.getRequestName());
					responseDTO.setReferenceCode(foundParent.getCorpName());
					responseDTO.setLotCodes(new HashSet<String>());
					for (SaltForm saltForm : foundParent.getSaltForms()) {
						for (Lot lot : saltForm.getLots()) {
							responseDTO.getLotCodes().add(lot.getCorpName());
						}
					}
					responseDTOs.add(responseDTO);
				}
			} else {
				ParentLotCodeDTO responseDTO = new ParentLotCodeDTO();
				responseDTO.setRequestName(requestDTO.getRequestName());
				responseDTOs.add(responseDTO);
			}
		}
		return responseDTOs;
	}

	@Transactional
	public void updateLot() {

		// What does the service do?
		// input: json lot object -- metalot object?
		// All fields except compound structure and salts should be editable.
		// If property is not passed in, property will remain the same.
		// Check for parent uniqueness in consideration of edits, throw error if changes
		// to parent will conflict with existing parent.

	}

	@Override
	public void deleteLot(Lot lot) {
		// Deletes lot and orphan salt forms and orphan parents of the lot
		Parent parent = Parent.findParent(lot.getParent().getId());
		Boolean canDeleteSaltForm = true;
		Boolean canDeleteParent = true;
		for(SaltForm saltForm : parent.getSaltForms()) {
			if(saltForm.getId().equals(lot.getSaltForm().getId())) {
				Set<Lot> lots = saltForm.getLots();
				for(Lot lotToDelete : lots) {
					if(!lotToDelete.getId().equals(lot.getId())) {
						canDeleteSaltForm = false;
						canDeleteParent = false;
						logger.info("Found dependent lot ("+lot.getCorpName()+") on salt form (corpName:'"+saltForm.getCorpName()+"', id:"+saltForm.getId()+")");
					}
				}
			} else {
				canDeleteParent = false;
				logger.info("Found dependent salt form ("+saltForm.getCorpName()+") on parent (corpName:'"+parent.getCorpName()+"', id:"+parent.getId()+")");
			}
		}
		if(canDeleteParent) {
			// Delete parent (including salt forms and lots and all dependencies)
			logger.info("Deleting parent (corpName:'"+parent.getCorpName()+"', id:"+parent.getId()+")");
			parentService.deleteParent(parent);
		} else {
			logger.info("Not deleting parent (corpName:'"+parent.getCorpName()+"', id:"+parent.getId()+")");
			if(canDeleteSaltForm) {
				// Delete the salt form (including lots and all dependencies)
				logger.info("Deleting salt form (corpName:'"+lot.getSaltForm().getCorpName()+"', id:"+lot.getSaltForm().getId()+")");
				saltFormService.deleteSaltForm(lot.getSaltForm());
			} else {
				// Delete just the lot (including all dependencies)
				logger.info("Not deleting salt form (corpName:'"+lot.getSaltForm().getCorpName()+"', id:"+lot.getSaltForm().getId()+")");
				lotService.deleteLot(lot);
			}
		}
	}
}
