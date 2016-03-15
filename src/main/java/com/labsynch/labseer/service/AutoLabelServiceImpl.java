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
import com.labsynch.labseer.domain.LsSeqContainer;
import com.labsynch.labseer.domain.LsSeqExpt;
import com.labsynch.labseer.domain.LsSeqItxCntrCntr;
import com.labsynch.labseer.domain.LsSeqItxExperimentExperiment;
import com.labsynch.labseer.domain.LsSeqItxProtocolProtocol;
import com.labsynch.labseer.domain.LsSeqItxSubjCntr;
import com.labsynch.labseer.domain.LsSeqProtocol;
import com.labsynch.labseer.domain.LsSeqSubject;
import com.labsynch.labseer.domain.LsSeqTrtGrp;
import com.labsynch.labseer.domain.LsSeqAnlGrp;
import com.labsynch.labseer.dto.AutoLabelDTO;
import com.labsynch.labseer.dto.LabelSequenceDTO;

@Service
@Transactional
public class AutoLabelServiceImpl implements AutoLabelService {

	private static final Logger logger = LoggerFactory.getLogger(AutoLabelServiceImpl.class);

	@Override
	public List<AutoLabelDTO> getAutoLabels(String json) {

		LabelSequenceDTO lsDTO = LabelSequenceDTO.fromJsonToLabelSequenceDTO(json);
		logger.debug("incoming label seq: " + lsDTO.toJson());

		return getAutoLabels(lsDTO.getThingTypeAndKind(), lsDTO.getLabelTypeAndKind(), lsDTO.getNumberOfLabels());
	}
	
	@Override
	public List<AutoLabelDTO> getAutoLabels(LabelSequenceDTO lsDTO) {

		logger.debug("incoming label seq: " + lsDTO.toJson());

		return getAutoLabels(lsDTO.getThingTypeAndKind(), lsDTO.getLabelTypeAndKind(), lsDTO.getNumberOfLabels());
	}

	@Override
	public List<AutoLabelDTO> getAutoLabels(String thingTypeAndKind, String labelTypeAndKind, Long numberOfLabels) throws NonUniqueResultException {

		List<LabelSequence> labelSequences = LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(thingTypeAndKind, labelTypeAndKind).getResultList();
		LabelSequence labelSequence;
		if(labelSequences.size() == 0) {
			logger.info("Label sequence does not exist! Create new Sequence if possible." + thingTypeAndKind + "  " + labelTypeAndKind);
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
				
		List<AutoLabelDTO> autoLabels = new ArrayList<AutoLabelDTO>();

		if (labelSequence.getThingTypeAndKind().equalsIgnoreCase("document_subject") && labelSequence.getLabelTypeAndKind().equalsIgnoreCase("id_codeName")){
			autoLabels = generateSubjectLabels(labelSequence, numberOfLabels);			

		} else if (labelSequence.getThingTypeAndKind().equalsIgnoreCase("document_treatment group") && labelSequence.getLabelTypeAndKind().equalsIgnoreCase("id_codeName")){
			autoLabels = generateTreatmentGroupLabels(labelSequence, numberOfLabels);

		} else if (labelSequence.getThingTypeAndKind().equalsIgnoreCase("document_analysis group") && labelSequence.getLabelTypeAndKind().equalsIgnoreCase("id_codeName")){
			autoLabels = generateAnalysisGroupLabels(labelSequence, numberOfLabels);
			
		} else if (labelSequence.getThingTypeAndKind().equalsIgnoreCase("document_experiment") && labelSequence.getLabelTypeAndKind().equalsIgnoreCase("id_codeName")){
			autoLabels = generateExperimentLabels(labelSequence, numberOfLabels);
			
		} else if (labelSequence.getThingTypeAndKind().equalsIgnoreCase("document_protocol") && labelSequence.getLabelTypeAndKind().equalsIgnoreCase("id_codeName")){
			autoLabels = generateProtocolLabels(labelSequence, numberOfLabels);

		} else if (labelSequence.getThingTypeAndKind().equalsIgnoreCase("material_container") && labelSequence.getLabelTypeAndKind().equalsIgnoreCase("id_codeName")){
			autoLabels = generateContainerLabels(labelSequence, numberOfLabels);
			
		} else if (labelSequence.getThingTypeAndKind().equalsIgnoreCase("interaction_containerContainer") && labelSequence.getLabelTypeAndKind().equalsIgnoreCase("id_codeName")){
			autoLabels = generateItxCntrCntrLabels(labelSequence, numberOfLabels);

		} else if (labelSequence.getThingTypeAndKind().equalsIgnoreCase("interaction_subjectContainer") && labelSequence.getLabelTypeAndKind().equalsIgnoreCase("id_codeName")){
			autoLabels = generateItxSubjCntrLabels(labelSequence, numberOfLabels);

		} else if (labelSequence.getThingTypeAndKind().equalsIgnoreCase("interaction_protocolProtocol") && labelSequence.getLabelTypeAndKind().equalsIgnoreCase("id_codeName")){
			autoLabels = generateItxProtProtLabels(labelSequence, numberOfLabels);
			
		} else if (labelSequence.getThingTypeAndKind().equalsIgnoreCase("interaction_experimentExperiment") && labelSequence.getLabelTypeAndKind().equalsIgnoreCase("id_codeName")){
			autoLabels = generateItxExptExptLabels(labelSequence, numberOfLabels);
				
		} else {		
			autoLabels = generateAutoLabels(labelSequence, numberOfLabels);
		}
		

		return autoLabels;
	}

	private List<AutoLabelDTO> generateItxExptExptLabels(LabelSequence labelSequence, Long numberOfLabels) {
		List<AutoLabelDTO> autoLabels = new ArrayList<AutoLabelDTO>();
		int counter = 0;
		while (counter < numberOfLabels) {
			LsSeqItxExperimentExperiment lsSeqObject = new LsSeqItxExperimentExperiment();
			lsSeqObject.persist();
			Long labelNumber = lsSeqObject.getId();
			String label = generateLabel(labelSequence, labelNumber); 
			logger.debug("new label: " + label);
			AutoLabelDTO autoLabel = new AutoLabelDTO();
			autoLabel.setAutoLabel(label);
			autoLabels.add(autoLabel);
			counter++;
		}
		
		return autoLabels;
	}

	private List<AutoLabelDTO> generateItxProtProtLabels(LabelSequence labelSequence, Long numberOfLabels) {
		List<AutoLabelDTO> autoLabels = new ArrayList<AutoLabelDTO>();
		int counter = 0;
		while (counter < numberOfLabels) {
			LsSeqItxProtocolProtocol lsSeqObject = new LsSeqItxProtocolProtocol();
			lsSeqObject.persist();
			Long labelNumber = lsSeqObject.getId();
			String label = generateLabel(labelSequence, labelNumber); 
			logger.debug("new label: " + label);
			AutoLabelDTO autoLabel = new AutoLabelDTO();
			autoLabel.setAutoLabel(label);
			autoLabels.add(autoLabel);
			counter++;
		}
		
		return autoLabels;
	}

	private List<AutoLabelDTO> generateItxSubjCntrLabels(LabelSequence labelSequence, Long numberOfLabels) {
		List<AutoLabelDTO> autoLabels = new ArrayList<AutoLabelDTO>();
		int counter = 0;
		while (counter < numberOfLabels) {
			LsSeqItxSubjCntr lsSeqItxSubjCntr = new LsSeqItxSubjCntr();
			lsSeqItxSubjCntr.persist();
			Long labelNumber = lsSeqItxSubjCntr.getId();
			String label = generateLabel(labelSequence, labelNumber); 
			logger.debug("new label: " + label);
			AutoLabelDTO autoLabel = new AutoLabelDTO();
			autoLabel.setAutoLabel(label);
			autoLabels.add(autoLabel);
			counter++;
		}
		
		return autoLabels;
	}

	private List<AutoLabelDTO> generateItxCntrCntrLabels(LabelSequence labelSequence, Long numberOfLabels) {
		List<AutoLabelDTO> autoLabels = new ArrayList<AutoLabelDTO>();
		int counter = 0;
		while (counter < numberOfLabels) {
			LsSeqItxCntrCntr lsSeqObject = new LsSeqItxCntrCntr();
			lsSeqObject.persist();
			Long labelNumber = lsSeqObject.getId();
			String label = generateLabel(labelSequence, labelNumber); 
			logger.debug("new label: " + label);
			AutoLabelDTO autoLabel = new AutoLabelDTO();
			autoLabel.setAutoLabel(label);
			autoLabels.add(autoLabel);
			counter++;
		}
		
		return autoLabels;
	}

	private List<AutoLabelDTO> generateContainerLabels(LabelSequence labelSequence, Long numberOfLabels) {
		List<AutoLabelDTO> autoLabels = new ArrayList<AutoLabelDTO>();
		int counter = 0;
		while (counter < numberOfLabels) {
			LsSeqContainer lsSeqObject = new LsSeqContainer();
			lsSeqObject.persist();
			Long labelNumber = lsSeqObject.getId();
			String label = generateLabel(labelSequence, labelNumber); 
			logger.debug("new label: " + label);
			AutoLabelDTO autoLabel = new AutoLabelDTO();
			autoLabel.setAutoLabel(label);
			autoLabels.add(autoLabel);
			counter++;
		}
		
		return autoLabels;
	}

	private String generateLabel(LabelSequence labelSequence, Long labelNumber) {
		
		String formatLabelNumber = "%";
		formatLabelNumber = formatLabelNumber.concat("0").concat(labelSequence.getDigits().toString()).concat("d");
		logger.debug("format corpNumber: " + formatLabelNumber);
		String label = labelSequence.getLabelPrefix().concat(labelSequence.getLabelSeparator()).concat(String.format(formatLabelNumber, labelNumber));

		return label;
		
	}
	
	private List<AutoLabelDTO> generateProtocolLabels(LabelSequence labelSequence, Long numberOfLabels) {
		
		List<AutoLabelDTO> autoLabels = new ArrayList<AutoLabelDTO>();
		int counter = 0;
		while (counter < numberOfLabels) {
			LsSeqProtocol lsSeqObject = new LsSeqProtocol();
			lsSeqObject.persist();
			Long labelNumber = lsSeqObject.getId();
			String label = generateLabel(labelSequence, labelNumber); 
			logger.debug("new label: " + label);
			AutoLabelDTO autoLabel = new AutoLabelDTO();
			autoLabel.setAutoLabel(label);
			autoLabels.add(autoLabel);
			counter++;
		}
		
		return autoLabels;
		
	}

	
	private List<AutoLabelDTO> generateExperimentLabels(LabelSequence labelSequence, Long numberOfLabels) {
		
		List<AutoLabelDTO> autoLabels = new ArrayList<AutoLabelDTO>();
		int counter = 0;
		while (counter < numberOfLabels) {
			LsSeqExpt lsSeqObject = new LsSeqExpt();
			lsSeqObject.persist();
			Long labelNumber = lsSeqObject.getId();
			String label = generateLabel(labelSequence, labelNumber); 
			logger.debug("new label: " + label);
			AutoLabelDTO autoLabel = new AutoLabelDTO();
			autoLabel.setAutoLabel(label);
			autoLabels.add(autoLabel);
			counter++;
		}
		return autoLabels;
		
	}


	private List<AutoLabelDTO> generateAnalysisGroupLabels(LabelSequence labelSequence, Long numberOfLabels) {
		
		List<AutoLabelDTO> autoLabels = new ArrayList<AutoLabelDTO>();
		int counter = 0;
		while (counter < numberOfLabels) {
			LsSeqAnlGrp lsSeqObject = new LsSeqAnlGrp();
			lsSeqObject.persist();
			Long labelNumber = lsSeqObject.getId();
			String label = generateLabel(labelSequence, labelNumber); 
			logger.debug("new label: " + label);
			AutoLabelDTO autoLabel = new AutoLabelDTO();
			autoLabel.setAutoLabel(label);
			autoLabels.add(autoLabel);
			counter++;
		}
		return autoLabels;
		
	}

	
	private List<AutoLabelDTO> generateTreatmentGroupLabels(LabelSequence labelSequence, Long numberOfLabels) {
		
		List<AutoLabelDTO> autoLabels = new ArrayList<AutoLabelDTO>();
		int counter = 0;
		while (counter < numberOfLabels) {
			LsSeqTrtGrp lsSeqObject = new LsSeqTrtGrp();
			lsSeqObject.persist();
			Long labelNumber = lsSeqObject.getId();
			String label = generateLabel(labelSequence, labelNumber); 
			logger.debug("new label: " + label);
			AutoLabelDTO autoLabel = new AutoLabelDTO();
			autoLabel.setAutoLabel(label);
			autoLabels.add(autoLabel);
			counter++;
		}
		return autoLabels;
		
	}
	
	private List<AutoLabelDTO> generateSubjectLabels(LabelSequence labelSequence, Long numberOfLabels) {
		
		List<AutoLabelDTO> autoLabels = new ArrayList<AutoLabelDTO>();
		int counter = 0;
		while (counter < numberOfLabels) {
			LsSeqSubject lsSeqSubject = new LsSeqSubject();
			lsSeqSubject.persist();
			Long labelNumber = lsSeqSubject.getId();
			String label = generateLabel(labelSequence, labelNumber); 
			logger.debug("new label: " + label);
			AutoLabelDTO autoLabel = new AutoLabelDTO();
			autoLabel.setAutoLabel(label);
			autoLabels.add(autoLabel);
			counter++;
		}
		return autoLabels;
		
	}

	private List<AutoLabelDTO> generateAutoLabels(LabelSequence labelSequence, Long numberOfLabels) {

		List<AutoLabelDTO> autoLabels = new ArrayList<AutoLabelDTO>();

		long currentLastNumber = labelSequence.getLatestNumber();

		labelSequence.setLatestNumber(labelSequence.getLatestNumber() + numberOfLabels);
		labelSequence.setModifiedDate(new Date());
		labelSequence.merge();

		long startingNumber = currentLastNumber + 1L;
		long endingNumber = currentLastNumber + numberOfLabels;

		long labelNumber = startingNumber;
		String formatLabelNumber = "%";
		formatLabelNumber = formatLabelNumber.concat("0").concat(labelSequence.getDigits().toString()).concat("d");
		logger.debug("format corpNumber: " + formatLabelNumber);
		while (labelNumber <= endingNumber) {
			String label = labelSequence.getLabelPrefix().concat(labelSequence.getLabelSeparator()).concat(String.format(formatLabelNumber, labelNumber));
			logger.debug("new label: " + label);
			AutoLabelDTO autoLabel = new AutoLabelDTO();
			autoLabel.setAutoLabel(label);
			autoLabels.add(autoLabel);
			labelNumber++;
		}
		
		return autoLabels;		

	}

	private LabelSequence createLabelSequence(String thingTypeAndKind, String labelTypeAndKind) {
		
		if (thingTypeAndKind.equals("document_datadictionary") && labelTypeAndKind.equals("id_codeName")){
			
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
	public String getDataDictionaryCodeName() {
		String thingTypeAndKind = "document_datadictionary";
		String labelTypeAndKind = "id_codeName";
		Long numberOfLabels = 1L;
		List<AutoLabelDTO> labels = getAutoLabels(thingTypeAndKind, labelTypeAndKind, numberOfLabels );
		return labels.get(0).getAutoLabel();		
	}
	
	@Transactional
	@Override
	public String getProtocolCodeName() {
		String thingTypeAndKind = "document_protocol";
		String labelTypeAndKind = "id_codeName";
		Long numberOfLabels = 1L;
		List<AutoLabelDTO> labels = getAutoLabels(thingTypeAndKind, labelTypeAndKind, numberOfLabels );
		return labels.get(0).getAutoLabel();		
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

	@Override
	public String getLsThingCodeName(String lsTypeAndKind) {
		String labelTypeAndKind = "id_codeName";
		Long numberOfLabels = 1L;
		List<AutoLabelDTO> labels = getAutoLabels(lsTypeAndKind, labelTypeAndKind, numberOfLabels );
		return labels.get(0).getAutoLabel();
	}
	
	@Override
	public AutoLabelDTO getLastLabel(String thingTypeAndKind, String labelTypeAndKind) throws NonUniqueResultException {
		List<LabelSequence> labelSequences = LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(thingTypeAndKind, labelTypeAndKind).getResultList();
		LabelSequence labelSequence;
		if(labelSequences.size() == 0) {
			logger.info("Label sequence does not exist! Create new Sequence if possible." + thingTypeAndKind + "  " + labelTypeAndKind);
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
		
		List<AutoLabelDTO> autoLabels = new ArrayList<AutoLabelDTO>();

		long currentLastNumber = labelSequence.getLatestNumber();

		String formatLabelNumber = "%";
		formatLabelNumber = formatLabelNumber.concat("0").concat(labelSequence.getDigits().toString()).concat("d");
		logger.debug("format corpNumber: " + formatLabelNumber);
		String label = labelSequence.getLabelPrefix().concat(labelSequence.getLabelSeparator()).concat(String.format(formatLabelNumber, currentLastNumber));
		logger.debug("last label: " + label);
		AutoLabelDTO autoLabel = new AutoLabelDTO();
		autoLabel.setAutoLabel(label);
		
		return autoLabel;		
	}
	
	@Override
	public void decrementLabelSequence(String thingTypeAndKind, String labelTypeAndKind) throws NonUniqueResultException {
		List<LabelSequence> labelSequences = LabelSequence.findLabelSequencesByThingTypeAndKindEqualsAndLabelTypeAndKindEquals(thingTypeAndKind, labelTypeAndKind).getResultList();
		LabelSequence labelSequence;
		if(labelSequences.size() == 0) {
			logger.info("Label sequence does not exist! Create new Sequence if possible." + thingTypeAndKind + "  " + labelTypeAndKind);
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
		
		List<AutoLabelDTO> autoLabels = new ArrayList<AutoLabelDTO>();

		long currentLastNumber = labelSequence.getLatestNumber();
		labelSequence.setLatestNumber(labelSequence.getLatestNumber() - 1);
		labelSequence.setModifiedDate(new Date());
		labelSequence.merge();
	}

}
