package com.labsynch.labseer.service;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.AnalysisGroup;

@Service
public interface AnalysisGroupService {

	AnalysisGroup saveLsAnalysisGroup(AnalysisGroup analysisGroup);

	AnalysisGroup updateLsAnalysisGroup(AnalysisGroup analysisGroup);
}
