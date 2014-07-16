package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.LabelSequence;
import com.labsynch.labseer.dto.AutoLabelDTO;
import com.labsynch.labseer.dto.LabelSequenceDTO;

@Service
@Transactional
public class AutoLabelServiceImpl implements AutoLabelService {

	private static final Logger logger = LoggerFactory.getLogger(AutoLabelServiceImpl.class);

	@Override
	public List<AutoLabelDTO> getAutoLabels(String json) {

		LabelSequenceDTO lsDTO = LabelSequenceDTO.fromJsonToLabelSequenceDTO(json);
		logger.info("incoming label seq: " + lsDTO.toJson());

		return getAutoLabels(lsDTO.getThingTypeAndKind(), lsDTO.getLabelTypeAndKind(), lsDTO.getNumberOfLabels());
	}

	@Override
	public List<AutoLabelDTO> getAutoLabels(String thingTypeAndKind, String labelTypeAndKind, Long numberOfLabels) throws NonUniqueResultException {

		List<LabelSequence> labelSequences = LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(thingTypeAndKind, labelTypeAndKind).getResultList();
		LabelSequence labelSequence;
		if(labelSequences.size() == 0) {
			logger.info("Label sequence does not exist! Create new Sequence if possible.");
			labelSequence = createLabelSequence(thingTypeAndKind, labelTypeAndKind);
			if (labelSequence == null){
				throw new NoResultException();
			}
		} else if (labelSequences.size() > 1) {
			logger.error("found duplicate sequences!!!");
			throw new NonUniqueResultException();
		} else {
			labelSequence = LabelSequence.findLabelSequence(labelSequences.get(0).getId());           	
		}

		long currentLastNumber = labelSequence.getLatestNumber();

		labelSequence.setLatestNumber(labelSequence.getLatestNumber() + numberOfLabels);
		labelSequence.setModifiedDate(new Date());
		labelSequence.merge();

		long startingNumber = currentLastNumber + 1L;
		long endingNumber = currentLastNumber + numberOfLabels;

		long labelNumber = startingNumber;
		String formatLabelNumber = "%";
		formatLabelNumber = formatLabelNumber.concat("0").concat(labelSequence.getDigits().toString()).concat("d");
		logger.info("format corpNumber: " + formatLabelNumber);
		List<AutoLabelDTO> autoLabels = new ArrayList<AutoLabelDTO>();
		while (labelNumber <= endingNumber) {
			String label = labelSequence.getLabelPrefix().concat(labelSequence.getLabelSeparator()).concat(String.format(formatLabelNumber, labelNumber));
			logger.info("new label: " + label);
			AutoLabelDTO autoLabel = new AutoLabelDTO();
			autoLabel.setAutoLabel(label);
			autoLabels.add(autoLabel);
			labelNumber++;
		}

		return autoLabels;
	}

	private LabelSequence createLabelSequence(String thingTypeAndKind, String labelTypeAndKind) {
		
		if (thingTypeAndKind.equals("") && labelTypeAndKind.equals("")){
			
			return createDataDictionarySequence();
			
		} else {
			
			return null;
		}
		

	}

	private LabelSequence createDataDictionarySequence() {
		LabelSequence labelSequence = new LabelSequence();
		labelSequence.setThingTypeAndKind("document_datadictionary");
		labelSequence.setLabelTypeAndKind("id_codeName");
		labelSequence.setDigits(6);
		labelSequence.setGroupDigits(false);
		labelSequence.setIgnored(false);
		labelSequence.setLabelPrefix("DDICT");
		labelSequence.setLabelSeparator("-");
		labelSequence.setLatestNumber(0L);
		labelSequence.setModifiedDate((new Date()));
		labelSequence.persist();

		return labelSequence;
	}


	@Transactional
	@Override
	public String getExperimentCodeName() {
		String thingTypeAndKind = "document_experiment";
		String labelTypeAndKind = "id_codeName";
		Long numberOfLabels = 1L;
		List<AutoLabelDTO> labels = getAutoLabels(thingTypeAndKind, labelTypeAndKind, numberOfLabels );
		return labels.get(0).getAutoLabel();		
	}

	@Transactional
	@Override
	public String getAnalysisGroupCodeName() {
		String thingTypeAndKind = "document_analysis group";
		String labelTypeAndKind = "id_codeName";
		Long numberOfLabels = 1L;
		List<AutoLabelDTO> labels = getAutoLabels(thingTypeAndKind, labelTypeAndKind, numberOfLabels );
		return labels.get(0).getAutoLabel();		
	}

	@Transactional
	@Override
	public String getTreatmentGroupCodeName() {
		String thingTypeAndKind = "document_treatment group";
		String labelTypeAndKind = "id_codeName";
		Long numberOfLabels = 1L;
		List<AutoLabelDTO> labels = getAutoLabels(thingTypeAndKind, labelTypeAndKind, numberOfLabels );
		return labels.get(0).getAutoLabel();		
	}

	@Transactional
	@Override
	public String getSubjectCodeName() {
		String thingTypeAndKind = "document_subject";
		String labelTypeAndKind = "id_codeName";
		Long numberOfLabels = 1L;
		List<AutoLabelDTO> labels = getAutoLabels(thingTypeAndKind, labelTypeAndKind, numberOfLabels );
		return labels.get(0).getAutoLabel();		
	}

}
