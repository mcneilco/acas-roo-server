package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.domain.Parent;
import com.labsynch.labseer.domain.ParentAlias;
import com.labsynch.labseer.domain.SaltForm;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ParentLotAssayDTO;
import com.labsynch.labseer.dto.ParentLotCodeDTO;
import com.labsynch.labseer.service.LotServiceImpl.LotOrphanLevel;

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

	/**
	 * Deletes a lot from the database including it's children and any orphaned iso salts, saltforms and parents.
	 * @param lot The lot to delete
	 * @return
	 */
	@Override
	public void deleteLot(Lot lot) {

		LotOrphanLevel orphanLevel = lotService.checkLotOrphanLevel(lot);
		if(orphanLevel == LotOrphanLevel.Parent) {
			// Delete parent (including salt forms and lots and all dependencies)
			logger.info("Deleting parent (corpName:'"+lot.getParent().getCorpName()+"', id:"+lot.getParent().getId()+")");
			parentService.deleteParent(lot.getParent());
		} else {
			logger.info("Not deleting parent (corpName:'"+lot.getParent().getCorpName()+"', id:"+lot.getParent().getId()+")");
			if(orphanLevel == LotOrphanLevel.SaltForm) {
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

	/**
	 * Get assay linked to the Lot/Parent corporate name. 
	 * 
	 * @param corporateName Unique corporate name of the parent or the lot.
	 * @return
	 */
	@Override
	@Transactional
	public List<ParentLotAssayDTO> getAssayLinkedToParentLot(String corporateName) {
		// TODO: Specific to Lot matches.
		// TypedQuery<Lot> q1 = Lot.findLotsByCorpNameEquals(corporateName);
		// List<Lot> lots = q1.getResultList();
		EntityManager em = Lot.entityManager();
		Query q = em.createNativeQuery("select paagr.tested_lot as lot_corp_name, "
			+ "lot.project as lot_project, "
			+ "ap.code_name as protocol_code, "
			+ "ap.label_text as protocol_name, "
			+ "ae.code_name as experiment_code, "
			+ "ae.label_text as experiment_name, "
			+ "ae.project as experiment_project, "
			+ "paagr.ls_type as agv_type, "
			+ "paagr.ls_kind as result_type, "
			+ "paagr.unit_kind as units, "
			+ "paagr.operator_kind as operator, "
			+ "paagr.numeric_value, "
			+ "paagr.string_value, "
			+ "paagr.public_data "
			+ "from p_api_analysis_group_results paagr "
			+ "join api_experiment ae on paagr.experiment_id = ae.id "
			+ "join api_protocol ap on ae.protocol_id = ap.protocol_id "
			+ "join lot on paagr.tested_lot = lot.corp_name "
			+ "where paagr.tested_lot = :corporateName "
			+ "order by paagr.ls_kind; ", "ParentLotAssayResult");
		q.setParameter("corporateName", corporateName);
		List<ParentLotAssayDTO> parentLotAssayDTOs = (List<ParentLotAssayDTO>) q.getResultList();
		return parentLotAssayDTOs;
	}
}
