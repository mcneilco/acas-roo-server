package com.labsynch.labseer.service;

import java.util.Date;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.TreatmentGroup;

@Service
public interface TreatmentGroupService {

	TreatmentGroup updateTreatmentGroup(TreatmentGroup treatmentGroup);

	void saveLsTreatmentGroups(AnalysisGroup newAnalysisGroup, Set<TreatmentGroup> treatmentGroups, Date recordedDate);

	TreatmentGroup saveLsTreatmentGroup(TreatmentGroup treatmentGroup);

	TreatmentGroup saveLsTreatmentGroup(Set<AnalysisGroup> analysisGroups, TreatmentGroup treatmentGroup, Date recordedDate);

}
