package com.labsynch.labseer.service;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.TreatmentGroup;

@Service
public interface TreatmentGroupService {

	TreatmentGroup updateTreatmentGroup(TreatmentGroup treatmentGroup);

	TreatmentGroup saveLsTreatmentGroup(TreatmentGroup treatmentGroup);
}
