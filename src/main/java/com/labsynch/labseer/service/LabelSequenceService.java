package com.labsynch.labseer.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.LabelSequence;

@Service
public interface LabelSequenceService {

	Collection<LabelSequence> saveLabelSequenceArray(
			Collection<LabelSequence> labelSequences);


}
