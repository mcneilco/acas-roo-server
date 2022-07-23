package com.labsynch.labseer.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.LsTransaction;

@Service
@Transactional
public class AssayServiceImpl implements AssayService {

	@Autowired
	private ProtocolService protocolService;

	@Autowired
	private ExperimentService experimentService;

	@Autowired
	private AnalysisGroupService analysisGroupService;

	@Autowired
	private TreatmentGroupService treatmentGroupService;

	@Autowired
	private SubjectService subjectService;

	@Override
	@Transactional
	public int renameBatchCode(String oldCode, String newCode, String modifiedByUser) {
		
		// Create an LS transaction for this
		LsTransaction lsTransaction = new LsTransaction();
		lsTransaction.setRecordedDate(new Date());
		lsTransaction.setRecordedBy(modifiedByUser);
		lsTransaction.setComments("Rename batch code " + oldCode + " to " + newCode);
		lsTransaction.persist();

		int protocolValuesAffected = protocolService.renameBatchCode(oldCode, newCode, modifiedByUser, lsTransaction.getId());
		int experimentValuesAffected = experimentService.renameBatchCode(oldCode, newCode, modifiedByUser, lsTransaction.getId());
		int analysisGroupValuesAffected = analysisGroupService.renameBatchCode(oldCode, newCode, modifiedByUser, lsTransaction.getId());
		int treatmentGroupValuesAffected = treatmentGroupService.renameBatchCode(oldCode, newCode, modifiedByUser, lsTransaction.getId());
		int subjectValuesAffected = subjectService.renameBatchCode(oldCode, newCode, modifiedByUser, lsTransaction.getId());
		
		return protocolValuesAffected + experimentValuesAffected + analysisGroupValuesAffected + treatmentGroupValuesAffected + subjectValuesAffected;
	}

}
