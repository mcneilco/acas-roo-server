package com.labsynch.labseer.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.labsynch.labseer.domain.AnalysisGroup;
import com.labsynch.labseer.domain.AnalysisGroupValue;
import com.labsynch.labseer.domain.DDictValue;
import com.labsynch.labseer.domain.Experiment;
import com.labsynch.labseer.domain.ExperimentLabel;
import com.labsynch.labseer.domain.ExperimentState;
import com.labsynch.labseer.domain.ExperimentValue;
import com.labsynch.labseer.domain.ItxProtocolProtocol;
import com.labsynch.labseer.domain.LsTag;
import com.labsynch.labseer.domain.LsThing;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.domain.ProtocolLabel;
import com.labsynch.labseer.domain.ProtocolValue;
import com.labsynch.labseer.dto.AnalysisGroupValueDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.DateValueComparisonRequest;
import com.labsynch.labseer.dto.ErrorMessageDTO;
import com.labsynch.labseer.dto.ExperimentDataDTO;
import com.labsynch.labseer.dto.ExperimentErrorMessageDTO;
import com.labsynch.labseer.dto.ExperimentFilterDTO;
import com.labsynch.labseer.dto.ExperimentFilterSearchDTO;
import com.labsynch.labseer.dto.ExperimentSearchRequestDTO;
import com.labsynch.labseer.dto.JSTreeNodeDTO;
import com.labsynch.labseer.dto.PreferredNameDTO;
import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.dto.SELColOrderDTO;
import com.labsynch.labseer.dto.StringCollectionDTO;
import com.labsynch.labseer.dto.ValueTypeKindDTO;
import com.labsynch.labseer.exceptions.NotFoundException;
import com.labsynch.labseer.exceptions.TooManyResultsException;
import com.labsynch.labseer.exceptions.UniqueNameException;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ExperimentServiceImpl implements ExperimentService {

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Autowired
	private AnalysisGroupValueService analysisGroupValueService;

	@Autowired
	private ProtocolService protocolService;

	@Autowired
	private AutoLabelService autoLabelService;

	@Autowired
	private ExperimentValueService experimentValueService;


	@Autowired
	private AuthorService authorService;

	private static final Logger logger = LoggerFactory.getLogger(ExperimentServiceImpl.class);

	@Override
	public void deleteLsExperiment(Experiment experiment) {
		if (logger.isDebugEnabled())
			logger.debug("incoming meta experiment: " + experiment.toJson());

	}

	@Override
	@Transactional
	public Experiment updateExperiment(Experiment jsonExperiment) throws UniqueNameException {
		// logger.debug("incoming meta experiment: " + jsonExperiment.toPrettyJson());
		if (logger.isDebugEnabled())
			logger.debug("recorded by: " + jsonExperiment.getRecordedBy());

		boolean checkExperimentName = propertiesUtilService.getUniqueExperimentName();
		if (checkExperimentName) {
			boolean experimentExists = false;
			Set<ExperimentLabel> exptLabels = jsonExperiment.getLsLabels();
			for (ExperimentLabel label : exptLabels) {
				String labelText = label.getLabelText();
				if (logger.isDebugEnabled())
					logger.debug("Searching for labelText: " + labelText);
				List<ExperimentLabel> experimentLabels = ExperimentLabel.findExperimentLabelsByName(labelText)
						.getResultList();
				if (logger.isDebugEnabled())
					logger.debug("Found " + experimentLabels.size() + " labels");
				for (ExperimentLabel el : experimentLabels) {
					Experiment exp = el.getExperiment();
					if (logger.isDebugEnabled())
						logger.debug("Found same label on experiment: " + exp.getId().toString()
								+ " while experiment to update is: " + jsonExperiment.getId().toString());
					// if the experiment is not hard deleted or soft deleted, there is a name
					// conflict
					if (!exp.isIgnored() && !el.isIgnored() && exp.getId().compareTo(jsonExperiment.getId()) != 0) {
						experimentExists = true;
					}
				}
			}

			if (experimentExists) {
				throw new UniqueNameException("Experiment with the same name exists");
			}
		}

		Experiment updatedExperiment = Experiment.update(jsonExperiment);

		if (jsonExperiment.getLsLabels() != null) {
			for (ExperimentLabel experimentLabel : jsonExperiment.getLsLabels()) {
				if (logger.isDebugEnabled())
					logger.debug("Label in hand: " + experimentLabel.getLabelText());
				if (experimentLabel.getId() == null) {
					ExperimentLabel newExperimentLabel = new ExperimentLabel(experimentLabel);
					newExperimentLabel.setExperiment(updatedExperiment);
					newExperimentLabel.persist();
					updatedExperiment.getLsLabels().add(newExperimentLabel);
				} else {
					ExperimentLabel updatedLabel = ExperimentLabel.update(experimentLabel);
					if (logger.isDebugEnabled())
						logger.debug("updated experiment label " + updatedLabel.getId());
				}
			}
		} else {
			if (logger.isDebugEnabled())
				logger.debug("No experiment labels to update");
		}

		if (jsonExperiment.getLsStates() != null) {
			for (ExperimentState experimentState : jsonExperiment.getLsStates()) {
				ExperimentState updatedExperimentState;
				if (experimentState.getId() == null) {
					updatedExperimentState = new ExperimentState(experimentState);
					updatedExperimentState.setExperiment(updatedExperiment);
					updatedExperimentState.persist();
					updatedExperiment.getLsStates().add(updatedExperimentState);
				} else {
					updatedExperimentState = ExperimentState.update(experimentState);
					if (logger.isDebugEnabled())
						logger.debug("updated experiment state " + experimentState.getId());

				}
				if (experimentState.getLsValues() != null) {
					for (ExperimentValue experimentValue : experimentState.getLsValues()) {
						ExperimentValue updatedExperimentValue;
						if (experimentValue.getId() == null) {
							updatedExperimentValue = ExperimentValue.create(experimentValue);
							updatedExperimentValue.setLsState(updatedExperimentState);
							updatedExperimentValue.persist();
							updatedExperimentState.getLsValues().add(updatedExperimentValue);
						} else {
							experimentValue.setLsState(updatedExperimentState);
							updatedExperimentValue = ExperimentValue.update(experimentValue);
							if (logger.isDebugEnabled())
								logger.debug("updated experiment value " + updatedExperimentValue.getId());
						}
					}
				} else {
					if (logger.isDebugEnabled())
						logger.debug("No experiment values to update");
				}
			}
		}

		// if (logger.isDebugEnabled()) logger.debug("updatedExperiment: " +
		// updatedExperiment.toPrettyJson());
		return updatedExperiment;

	}

	@Override
	@Transactional
	public Collection<Experiment> saveLsExperiments(
			Collection<Experiment> experiments) throws UniqueNameException, NotFoundException {
		Collection<Experiment> savedExperiments = new ArrayList<Experiment>();
		for (Experiment experiment : experiments) {
			Experiment savedExperiment = saveLsExperiment(experiment);
			savedExperiments.add(savedExperiment);
		}
		return savedExperiments;
	}

	@Override
	@Transactional
	public Experiment saveLsExperiment(Experiment experiment) throws UniqueNameException, NotFoundException {
		if (logger.isDebugEnabled())
			logger.debug("incoming meta experiment: " + experiment.toJson());

		// check if experiment with the same name exists
		boolean checkExperimentName = propertiesUtilService.getUniqueExperimentName();
		if (checkExperimentName) {
			boolean experimentExists = false;
			Set<ExperimentLabel> exptLabels = experiment.getLsLabels();
			for (ExperimentLabel label : exptLabels) {
				String labelText = label.getLabelText();
				List<ExperimentLabel> experimentLabels = ExperimentLabel.findExperimentLabelsByName(labelText)
						.getResultList();
				for (ExperimentLabel el : experimentLabels) {
					Experiment exp = el.getExperiment();
					// if the experiment is not hard deleted or soft deleted, there is a name
					// conflict
					if (!exp.isIgnored() && !el.isIgnored()) {
						experimentExists = true;
					}
				}
			}

			if (experimentExists) {
				throw new UniqueNameException("Experiment with the same name exists");
			}
		}

		Experiment newExperiment = new Experiment(experiment);

		newExperiment.setProtocol(Protocol.findProtocol(experiment.getProtocol().getId()));
		if (newExperiment.getCodeName() == null) {
			newExperiment.setCodeName(autoLabelService.getExperimentCodeName());
		}
		newExperiment.persist();
		// newExperiment.flush();

		if (experiment.getLsLabels() != null) {
			Set<ExperimentLabel> lsLabels = new HashSet<ExperimentLabel>();
			for (ExperimentLabel experimentLabel : experiment.getLsLabels()) {
				ExperimentLabel newExperimentLabel = new ExperimentLabel(experimentLabel);
				newExperimentLabel.setExperiment(newExperiment);
				newExperimentLabel.persist();
				lsLabels.add(newExperimentLabel);
			}
			newExperiment.setLsLabels(lsLabels);
		} else {
			if (logger.isDebugEnabled())
				logger.debug("No experiment labels to save");
		}

		if (experiment.getLsStates() != null) {
			Set<ExperimentState> lsStates = new HashSet<ExperimentState>();
			for (ExperimentState experimentState : experiment.getLsStates()) {
				ExperimentState newExperimentState = new ExperimentState(experimentState);
				newExperimentState.setExperiment(newExperiment);
				newExperimentState.persist();
				if (experimentState.getLsValues() != null) {
					Set<ExperimentValue> lsValues = new HashSet<ExperimentValue>();
					for (ExperimentValue experimentValue : experimentState.getLsValues()) {
						experimentValue.setLsState(newExperimentState);
						experimentValue.persist();
						lsValues.add(experimentValue);
					}
					newExperimentState.setLsValues(lsValues);
				} else {
					if (logger.isDebugEnabled())
						logger.debug("No experiment values to save");
				}
				lsStates.add(newExperimentState);
			}
			newExperiment.setLsStates(lsStates);
		}

		// if(experiment.getAnalysisGroups() != null){
		// Set<AnalysisGroup> inputAnalysisGroups = new HashSet<AnalysisGroup>();
		// for(AnalysisGroup analysisGroup : experiment.getAnalysisGroups()){
		// inputAnalysisGroups.add(analysisGroup);
		// }
		//
		// for(AnalysisGroup analysisGroup : inputAnalysisGroups){
		// analysisGroup.getExperiments().add(newExperiment);
		// analysisGroupService.saveLsAnalysisGroup(analysisGroup);
		// }
		// Set<AnalysisGroup> analysisGroups = new HashSet<AnalysisGroup>();
		// for(AnalysisGroup analysisGroup : inputAnalysisGroups){
		// analysisGroup.getExperiments().add(newExperiment);
		// AnalysisGroup newAnalysisGroup = null;
		// try {
		// newAnalysisGroup = analysisGroupService.saveLsAnalysisGroup(analysisGroup);
		//
		// } catch (Exception e){
		// logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		// logger.error("Hit an error with Analysis Group: " + analysisGroup.getId());
		// logger.error("Problem AG: " +
		// AnalysisGroup.findAnalysisGroup(analysisGroup.getId()).getId());
		// logger.error(e.toString());
		//
		// try {
		// TimeUnit.SECONDS.sleep(5);
		// newAnalysisGroup = analysisGroupService.saveLsAnalysisGroup(analysisGroup);
		//
		// } catch (Exception e2){
		//
		// logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		// logger.error("Hit an error with Analysis Group: " + analysisGroup.getId());
		// logger.error("Problem AG: " +
		// AnalysisGroup.findAnalysisGroup(analysisGroup.getId()).getId());
		// logger.error(e2.toString());
		//
		// try {
		// TimeUnit.SECONDS.sleep(10);
		// newAnalysisGroup = analysisGroupService.saveLsAnalysisGroup(analysisGroup);
		//
		// } catch (Exception e3){
		//
		// logger.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		// logger.error("Hit an error with Analysis Group: " + analysisGroup.getId());
		// logger.error("Problem AG: " +
		// AnalysisGroup.findAnalysisGroup(analysisGroup.getId()).getId());
		// logger.error(e3.toString());
		// }
		// }
		// }
		// if (newAnalysisGroup != null){
		// analysisGroups.add(newAnalysisGroup);
		//// if (logger.isDebugEnabled()) logger.debug("persisted the newAnalysisGroup:
		// " + newAnalysisGroup.toJson());
		// } else{
		// if (logger.isDebugEnabled()) logger.debug("the analysis group is NULL");
		// throw new NotFoundException("AnalysisGroup not found: " +
		// analysisGroup.getId());
		// }
		// }
		// newExperiment.setAnalysisGroups(analysisGroups);
		// }

		return newExperiment;
	}

	@Override
	public Experiment getFullExperiment(Experiment queryExperiment) {
		Experiment experiment = Experiment.findExperiment(queryExperiment.getId());
		Set<ExperimentLabel> lsLabels = new HashSet<ExperimentLabel>();
		for (ExperimentLabel experimentLabel : ExperimentLabel
				.findExperimentLabelsByExperimentAndIgnoredNot(experiment, true).getResultList()) {
			lsLabels.add(experimentLabel);
		}
		experiment.setLsLabels(lsLabels);
		Set<ExperimentState> lsStates = new HashSet<ExperimentState>();
		for (ExperimentState experimentState : ExperimentState.findExperimentStatesByExperiment(experiment)
				.getResultList()) {
			for (ExperimentValue experimentValue : ExperimentValue.findExperimentValuesByLsState(experimentState)
					.getResultList()) {

			}
			lsStates.add(experimentState);
		}
		experiment.setLsStates(lsStates);
		Set<AnalysisGroup> analysisGroups = new HashSet<AnalysisGroup>();
		Set<Experiment> experiments = new HashSet<Experiment>();
		experiments.add(experiment);
		for (AnalysisGroup analysisGroup : AnalysisGroup.findAnalysisGroupsByExperiments(experiments).getResultList()) {
			analysisGroups.add(analysisGroup);
		}
		experiment.setAnalysisGroups(analysisGroups);

		return experiment;
	}

	@Override
	public Collection<ExperimentFilterDTO> getExperimentFilters(Collection<String> experimentCodes) {
		Collection<ExperimentFilterDTO> eftSet = new HashSet<ExperimentFilterDTO>();
		for (String experimentCode : experimentCodes) {
			if (logger.isDebugEnabled())
				logger.debug("searching for : " + experimentCode);
			List<Experiment> experiments = Experiment.findExperimentsByCodeNameEquals(experimentCode, false)
					.getResultList();
			Experiment experiment = null;
			if (experiments.size() == 1) {
				experiment = experiments.get(0);
				ExperimentFilterDTO eft = new ExperimentFilterDTO();
				eft.setExperimentId(experiment.getId());
				eft.setExperimentCode(experimentCode);
				List<ExperimentLabel> experimentNames = ExperimentLabel.findExperimentPreferredName(experiment.getId())
						.getResultList();
				if (experimentNames.size() == 1) {
					eft.setExperimentName(experimentNames.get(0).getLabelText());
				} else if (experimentNames.size() > 1) {
					logger.error("found mulitiple preferred names");
				} else {
					logger.error("no preferred names");
				}

				String stateType = "metadata";
				String stateKind = "data column order";
				List<ExperimentValue> experimentValues = ExperimentValue
						.findExperimentValuesByExptIDAndStateTypeKind(eft.getExperimentId(), stateType, stateKind)
						.getResultList();
				Collection<ValueTypeKindDTO> valueTypes = null;

				if (experimentValues.size() > 0) {
					HashMap<ExperimentState, SELColOrderDTO> valueStateMap = new HashMap<ExperimentState, SELColOrderDTO>();
					SELColOrderDTO selColOrderDTO;
					for (ExperimentValue ev : experimentValues) {
						if (valueStateMap.get(ev.getLsState()) != null) {
							selColOrderDTO = valueStateMap.get(ev.getLsState());
						} else {
							selColOrderDTO = new SELColOrderDTO();
						}

						if (ev.getLsKind().equalsIgnoreCase("column name")) {
							selColOrderDTO.setColumnName(ev.getStringValue());
							selColOrderDTO.setLsKind(ev.getStringValue());
						} else if (ev.getLsKind().equalsIgnoreCase("column type")) {
							selColOrderDTO.setLsType(ev.getStringValue());
						} else if (ev.getLsKind().equalsIgnoreCase("column order")) {
							selColOrderDTO.setColumnOrder(Double.valueOf(ev.getNumericValue().toString()).intValue());
						} else if (ev.getLsKind().equalsIgnoreCase("hide column")) {
							boolean displayColumn = true;
							if (ev.getStringValue().equalsIgnoreCase("true")) {
								displayColumn = false;
							}
							selColOrderDTO.setPublicData(displayColumn);
							selColOrderDTO.setColumnDisplay(displayColumn);
						}
						valueStateMap.put(ev.getLsState(), selColOrderDTO);
					}

					List<SELColOrderDTO> valueStateList = new ArrayList<SELColOrderDTO>();
					Set<ExperimentState> vsKeys = valueStateMap.keySet();
					for (ExperimentState vsKey : vsKeys) {
						SELColOrderDTO query = valueStateMap.get(vsKey);
						if (query.isPublicData()) {
							valueStateList.add(query);
						}
					}

					Collections.sort(valueStateList);
					valueTypes = new ArrayList<ValueTypeKindDTO>();
					for (SELColOrderDTO selColDTO : valueStateList) {
						ValueTypeKindDTO vtkDTO = new ValueTypeKindDTO();
						vtkDTO.setLsKind(selColDTO.getLsKind());
						vtkDTO.setLsType(selColDTO.getLsType());
						vtkDTO.setDisplayOrder(selColDTO.getColumnOrder());
						valueTypes.add(vtkDTO);
					}
				} else {
					valueTypes = AnalysisGroupValue.findAnalysisGroupValueTypeKindDTO(experimentCode).getResultList();

				}

				eft.setValueKinds(valueTypes);
				eftSet.add(eft);
			} else {
				logger.error("ERROR. Did not find a single experiment for : " + experimentCode + "  found: "
						+ experiments.size());
			}

		}

		if (logger.isDebugEnabled())
			logger.debug(ExperimentFilterDTO.toPrettyJsonArray(eftSet));

		return eftSet;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<JSTreeNodeDTO> getExperimentNodesMod(Collection<String> codeValues) {
		List<Experiment> experiments;
		if (codeValues == null || codeValues.size() == 0) {
			experiments = Experiment.findAllExperiments();
		} else {
			experiments = Experiment.findExperimentsByBatchCodes(codeValues).getResultList();
		}

		if (logger.isDebugEnabled())
			logger.debug("number of experiments found: " + experiments.size());

		Set<Protocol> protocols = new LinkedHashSet<Protocol>();
		Set<JSTreeNodeDTO> nodes = new LinkedHashSet<JSTreeNodeDTO>();
		for (Experiment exp : experiments) {
			protocols.add(Protocol.findProtocol(exp.getProtocol().getId()));

			String experimentLabel;
			List<ExperimentLabel> experimentNames = ExperimentLabel.findExperimentPreferredName(exp.getId())
					.getResultList();
			if (experimentNames.size() < 1) {
				String errorMessage = "expected a single preferred experiment name. Found " + experimentNames.size();
				logger.error(errorMessage);
				// throw new RuntimeException(errorMessage);
				experimentLabel = "NO PREFERRED EXPERIMENT NAME";
			} else if (experimentNames.size() > 1) {
				String errorMessage = "expected a single preferred experiment name. Found " + experimentNames.size();
				logger.error(errorMessage);
				// throw new RuntimeException(errorMessage);
				experimentLabel = "MULTIPLE PREFERRED EXPERIMENT NAME";
			} else {
				experimentLabel = experimentNames.get(0).getLabelText();
			}

			JSTreeNodeDTO node = new JSTreeNodeDTO();
			node.setDescription(exp.getShortDescription());
			node.setId(exp.getCodeName());
			node.setParent(exp.getProtocol().getCodeName());
			node.setLsTags(exp.getLsTags());
			node.setText(new StringBuilder().append(exp.getCodeName()).append(" ").append(experimentLabel).toString());
			nodes.add(node);

			StringBuilder allTagText = new StringBuilder();
			boolean firstTag = true;
			for (LsTag tag : exp.getLsTags()) {
				if (firstTag) {
					allTagText.append("Keywords: ").append(tag.getTagText());
					firstTag = false;
				} else {
					allTagText.append("; ").append(tag.getTagText());
				}
			}
			if (exp.getLsTags().size() > 0) {
				JSTreeNodeDTO tagNode = new JSTreeNodeDTO();
				tagNode.setId("tags_" + exp.getCodeName());
				tagNode.setParent(exp.getCodeName());
				tagNode.setText(allTagText.toString());
				nodes.add(tagNode);
			}

			// JSTreeNodeDTO descriptionNode = new JSTreeNodeDTO();
			// descriptionNode.setId("desc_" + exp.getCodeName());
			// descriptionNode.setParent(exp.getCodeName());
			// descriptionNode.setText(exp.getShortDescription());
			// nodes.add(descriptionNode);
		}

		JSTreeNodeDTO rootNode = new JSTreeNodeDTO();
		rootNode.setId("Root Node");
		rootNode.setParent("#");
		rootNode.setText("All Protocols");
		rootNode.setDescription("Root Node for All Protocols");
		nodes.add(rootNode);

		if (logger.isDebugEnabled())
			logger.debug("number of protocols found: " + protocols.size());
		for (Protocol prot : protocols) {

			String protocolLabel;
			List<ProtocolLabel> protocolNames = ProtocolLabel.findProtocolPreferredName(prot.getId()).getResultList();
			if (protocolNames.size() < 1) {
				String errorMessage = "expected a single preferred protocol name. Found " + protocolNames.size();
				logger.error(errorMessage);
				// throw new RuntimeException(errorMessage);
				protocolLabel = "NO PREFERRED EXPERIMENT NAME";
			} else if (protocolNames.size() > 1) {
				String errorMessage = "expected a single preferred protocol name. Found " + protocolNames.size();
				logger.error(errorMessage);
				// throw new RuntimeException(errorMessage);
				protocolLabel = "MULTIPLE PREFERRED PROTOCOL NAME";
			} else {
				protocolLabel = protocolNames.get(0).getLabelText();
			}

			JSTreeNodeDTO node = new JSTreeNodeDTO();
			node.setId(prot.getCodeName());
			node.setDescription(prot.getShortDescription());
			node.setParent(prot.getLsKind());
			node.setLsTags(prot.getLsTags());
			node.setText(new StringBuilder().append(protocolLabel).toString());

			nodes.add(node);

			JSTreeNodeDTO protocolKindNode = new JSTreeNodeDTO();
			protocolKindNode.setId(prot.getLsKind());
			protocolKindNode.setParent("Root Node");
			protocolKindNode.setText(prot.getLsKind());
			protocolKindNode.setDescription("Protocol Kind");
			nodes.add(protocolKindNode);
		}

		if (logger.isDebugEnabled())
			logger.debug("number of nodes made: " + nodes.size());
		if (logger.isDebugEnabled())
			logger.debug(JSTreeNodeDTO.toPrettyJsonArray(nodes));

		return nodes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<JSTreeNodeDTO> getExperimentNodes(Collection<String> codeValues) {
		List<Experiment> experiments;
		if (codeValues == null || codeValues.size() == 0) {
			experiments = Experiment.findAllExperiments(false);
		} else {
			experiments = Experiment.findExperimentsByBatchCodes(codeValues).getResultList();
		}

		if (logger.isDebugEnabled())
			logger.debug("number of experiments found: " + experiments.size());

		Set<Protocol> protocols = new LinkedHashSet<Protocol>();
		Set<JSTreeNodeDTO> nodes = new LinkedHashSet<JSTreeNodeDTO>();
		for (Experiment exp : experiments) {
			protocols.add(Protocol.findProtocol(exp.getProtocol().getId()));

			String experimentLabel;
			List<ExperimentLabel> experimentNames = ExperimentLabel.findExperimentPreferredName(exp.getId())
					.getResultList();
			if (experimentNames.size() < 1) {
				String errorMessage = "expected a single preferred experiment name. Found " + experimentNames.size();
				logger.error(errorMessage);
				// throw new RuntimeException(errorMessage);
				experimentLabel = "NO PREFERRED EXPERIMENT NAME";
			} else if (experimentNames.size() > 1) {
				String errorMessage = "expected a single preferred experiment name. Found " + experimentNames.size();
				logger.error(errorMessage);
				// throw new RuntimeException(errorMessage);
				experimentLabel = "MULTIPLE PREFERRED EXPERIMENT NAME";
			} else {
				experimentLabel = experimentNames.get(0).getLabelText();
			}

			JSTreeNodeDTO node = new JSTreeNodeDTO();
			node.setDescription(exp.getShortDescription());
			node.setId(exp.getCodeName());
			node.setParent(exp.getProtocol().getCodeName());
			node.setLsTags(exp.getLsTags());
			node.setText(new StringBuilder().append(exp.getCodeName()).append(" ").append(experimentLabel).toString());
			nodes.add(node);

			StringBuilder allTagText = new StringBuilder();
			boolean firstTag = true;
			for (LsTag tag : exp.getLsTags()) {
				if (firstTag) {
					allTagText.append("Keywords: ").append(tag.getTagText());
					firstTag = false;
				} else {
					allTagText.append("; ").append(tag.getTagText());
				}
			}
			if (exp.getLsTags().size() > 0) {
				JSTreeNodeDTO tagNode = new JSTreeNodeDTO();
				tagNode.setId("tags_" + exp.getCodeName());
				tagNode.setParent(exp.getCodeName());
				tagNode.setText(allTagText.toString());
				nodes.add(tagNode);
			}

			// JSTreeNodeDTO descriptionNode = new JSTreeNodeDTO();
			// descriptionNode.setId("desc_" + exp.getCodeName());
			// descriptionNode.setParent(exp.getCodeName());
			// descriptionNode.setText(exp.getShortDescription());
			// nodes.add(descriptionNode);
		}

		// JSTreeNodeDTO rootNode = new JSTreeNodeDTO();
		// rootNode.setId("Root Node");
		// rootNode.setParent("#");
		// rootNode.setText("All Protocols");
		// rootNode.setDescription("Root Node for All Protocols");
		// nodes.add(rootNode);
		//
		// JSTreeNodeDTO defaultNode = new JSTreeNodeDTO();
		// defaultNode.setId("Default Node");
		// defaultNode.setParent("Root Node");
		// defaultNode.setText("All Protocols Folder");
		// defaultNode.setDescription("Root Node for All Protocol Folders");
		// nodes.add(defaultNode);

		if (logger.isDebugEnabled())
			logger.debug("number of protocols found: " + protocols.size());
		for (Protocol prot : protocols) {

			String protocolLabel;
			List<ProtocolLabel> protocolNames = ProtocolLabel.findProtocolPreferredName(prot.getId()).getResultList();
			if (protocolNames.size() < 1) {
				String errorMessage = "expected a single preferred protocol name. Found " + protocolNames.size();
				logger.error(errorMessage);
				// throw new RuntimeException(errorMessage);
				protocolLabel = "NO PREFERRED EXPERIMENT NAME";
			} else if (protocolNames.size() > 1) {
				String errorMessage = "expected a single preferred protocol name. Found " + protocolNames.size();
				logger.error(errorMessage);
				// throw new RuntimeException(errorMessage);
				protocolLabel = "MULTIPLE PREFERRED PROTOCOL NAME";
			} else {
				protocolLabel = protocolNames.get(0).getLabelText();
			}

			// JSTreeNodeDTO protocolKindNode = new JSTreeNodeDTO();
			// protocolKindNode.setId(prot.getLsKind());
			// protocolKindNode.setParent("Root Node");
			// protocolKindNode.setText(prot.getLsKind());
			// protocolKindNode.setDescription("Protocol Kind");
			// nodes.add(protocolKindNode);
			//

			List<ProtocolValue> protocolValues = ProtocolValue
					.findProtocolValuesByProtocolIDAndStateTypeKindAndValueTypeKind(prot.getId(), "metadata",
							"protocol metadata", "stringValue", "assay tree rule")
					.getResultList();
			String assayFolderRule = "";

			for (ProtocolValue value : protocolValues) {
				if (logger.isDebugEnabled())
					logger.debug(value.toJson());
				if (value.getStringValue() == null) {
					logger.error("the assay folder rule is null");
				} else if (!value.getStringValue().equalsIgnoreCase("")) {
					assayFolderRule = value.getStringValue();
				}
				logger.info(value.toJson());
			}

			JSTreeNodeDTO node = new JSTreeNodeDTO();
			node.setId(prot.getCodeName());
			node.setDescription(prot.getShortDescription());
			node.setLsTags(prot.getLsTags());
			node.setText(new StringBuilder().append(protocolLabel).toString());

			boolean firstFolder = true;
			String[] assayFolderRules = assayFolderRule.split("/");
			if (assayFolderRules.length > 1) {
				for (int i = 1; i < assayFolderRules.length; i++) {
					String assayFolder = getAssayFolderPath(assayFolderRules, i);
					if (logger.isDebugEnabled())
						logger.debug("assay folder: " + i + " " + assayFolder);
					JSTreeNodeDTO assayFolderNode = new JSTreeNodeDTO();
					if (firstFolder) {
						assayFolderNode.setId(assayFolder);
						assayFolderNode.setText(assayFolderRules[i]);
						assayFolderNode.setDescription(assayFolderRules[i]);
						assayFolderNode.setParent("#");
						firstFolder = false;
						node.setParent(getAssayFolderPath(assayFolderRules, assayFolderRules.length - 1));
					} else {
						assayFolderNode.setId(assayFolder);
						assayFolderNode.setText(assayFolderRules[i]);
						assayFolderNode.setDescription(assayFolderRules[i]);
						// assayFolderNode.setParent(assayFolderRules[i-1]);
						assayFolderNode.setParent(getAssayFolderPath(assayFolderRules, i - 1));

					}
					nodes.add(assayFolderNode);
				}

			} else {
				node.setParent("#");
			}
			nodes.add(node);
		}

		if (logger.isDebugEnabled())
			logger.debug("number of nodes made: " + nodes.size());
		if (logger.isDebugEnabled())
			logger.debug(JSTreeNodeDTO.toPrettyJsonArray(nodes));

		return nodes;
	}

	private String getAssayFolderPath(String[] assayFolderRules, int numberOfRules) {
		StringBuilder sb = new StringBuilder();

		int count = numberOfRules;
		if (logger.isDebugEnabled())
			logger.debug("the counter is : " + count);
		for (int i = 0; i < count; i++) {
			sb.append("_");
			sb.append(assayFolderRules[i + 1]);
			if (logger.isDebugEnabled())
				logger.debug("building the assay folder: " + i + "  " + assayFolderRules[i + 1]);
		}

		return sb.toString();

	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<JSTreeNodeDTO> getExperimentNodesByProtocolTree(Collection<String> codeValues) {
		List<Experiment> experiments;
		if (codeValues == null || codeValues.size() == 0) {
			experiments = Experiment.findAllExperiments();
		} else {
			experiments = Experiment.findExperimentsByBatchCodes(codeValues).getResultList();
		}

		if (logger.isDebugEnabled())
			logger.debug("number of experiments found: " + experiments.size());

		Set<Protocol> protocols = new LinkedHashSet<Protocol>();
		Set<JSTreeNodeDTO> nodes = new LinkedHashSet<JSTreeNodeDTO>();
		for (Experiment exp : experiments) {
			protocols.add(Protocol.findProtocol(exp.getProtocol().getId()));

			String experimentLabel;
			List<ExperimentLabel> experimentNames = ExperimentLabel.findExperimentPreferredName(exp.getId())
					.getResultList();
			if (experimentNames.size() < 1) {
				String errorMessage = "expected a single preferred experiment name. Found " + experimentNames.size();
				logger.error(errorMessage);
				// throw new RuntimeException(errorMessage);
				experimentLabel = "NO PREFERRED EXPERIMENT NAME";
			} else if (experimentNames.size() > 1) {
				String errorMessage = "expected a single preferred experiment name. Found " + experimentNames.size();
				logger.error(errorMessage);
				// throw new RuntimeException(errorMessage);
				experimentLabel = "MULTIPLE PREFERRED EXPERIMENT NAME";
			} else {
				experimentLabel = experimentNames.get(0).getLabelText();
			}

			JSTreeNodeDTO node = new JSTreeNodeDTO();
			node.setDescription(exp.getShortDescription());
			node.setId(exp.getCodeName());
			node.setParent(exp.getProtocol().getCodeName());
			node.setLsTags(exp.getLsTags());
			node.setText(new StringBuilder().append(experimentLabel).toString());
			nodes.add(node);

			StringBuilder allTagText = new StringBuilder();
			boolean firstTag = true;
			for (LsTag tag : exp.getLsTags()) {
				if (firstTag) {
					allTagText.append("Keywords: ").append(tag.getTagText());
					firstTag = false;
				} else {
					allTagText.append("; ").append(tag.getTagText());
				}
			}
			if (exp.getLsTags().size() > 0) {
				JSTreeNodeDTO tagNode = new JSTreeNodeDTO();
				tagNode.setId("tags_" + exp.getCodeName());
				tagNode.setParent(exp.getCodeName());
				tagNode.setText(allTagText.toString());
				nodes.add(tagNode);
			}

		}

		JSTreeNodeDTO rootNode = new JSTreeNodeDTO();
		rootNode.setId("Root Node");
		rootNode.setParent("#");
		rootNode.setText("All Protocols");
		rootNode.setDescription("Root Node for All Protocols");
		nodes.add(rootNode);

		if (logger.isDebugEnabled())
			logger.debug("number of protocols found: " + protocols.size());
		for (Protocol prot : protocols) {

			String protocolLabel;
			List<ProtocolLabel> protocolNames = ProtocolLabel.findProtocolPreferredName(prot.getId()).getResultList();
			if (protocolNames.size() < 1) {
				String errorMessage = "expected a single preferred protocol name. Found " + protocolNames.size();
				logger.error(errorMessage);
				// throw new RuntimeException(errorMessage);
				protocolLabel = "NO PREFERRED EXPERIMENT NAME";
			} else if (protocolNames.size() > 1) {
				String errorMessage = "expected a single preferred protocol name. Found " + protocolNames.size();
				logger.error(errorMessage);
				// throw new RuntimeException(errorMessage);
				protocolLabel = "MULTIPLE PREFERRED PROTOCOL NAME";
			} else {
				protocolLabel = protocolNames.get(0).getLabelText();
			}

			if (ItxProtocolProtocol.findItxProtocolProtocolsBySecondProtocol(prot).getResultList().size() > 0) {
				List<String> protocolNodeList = new ArrayList<String>();
				List<String> nodeNameList = lookupProtocolTree(prot, protocolNodeList);
				int nodeIndex = 0;
				for (String nodeName : nodeNameList) {
					if (logger.isDebugEnabled())
						logger.debug("here is the nodeName: " + nodeName + "  index:" + nodeIndex);
					nodeIndex++; // incrementing to the next

					JSTreeNodeDTO protocolNode = new JSTreeNodeDTO();
					protocolNode.setId(nodeName);
					protocolNode.setDescription(prot.getShortDescription());
					protocolNode.setLsTags(prot.getLsTags());
					protocolNode.setText(new StringBuilder().append(protocolLabel).toString());
					if (nodeIndex < nodeNameList.size()) {
						protocolNode.setParent(nodeNameList.get(nodeIndex));
					} else {
						protocolNode.setParent("Root Node");
					}

					nodes.add(protocolNode);
				}
			} else {
				if (logger.isDebugEnabled())
					logger.debug("this protocol is not attached by a protocol tree. set to default node");
				JSTreeNodeDTO protocolNode = new JSTreeNodeDTO();
				protocolNode.setId(prot.getCodeName());
				protocolNode.setDescription(prot.getShortDescription());
				protocolNode.setLsTags(prot.getLsTags());
				protocolNode.setText(new StringBuilder().append(protocolLabel).toString());
				protocolNode.setParent("Root Node");
				nodes.add(protocolNode);

			}

			// JSTreeNodeDTO node = new JSTreeNodeDTO();
			// node.setId(prot.getCodeName());
			// node.setDescription(prot.getShortDescription());
			// node.setParent(prot.getLsKind());
			// node.setLsTags(prot.getLsTags());
			// node.setText(new StringBuilder().append(protocolLabel).toString());

			//
			// JSTreeNodeDTO protocolKindNode = new JSTreeNodeDTO();
			// protocolKindNode.setId(prot.getLsKind());
			// protocolKindNode.setParent("Root Node");
			// protocolKindNode.setText(prot.getLsKind());
			// protocolKindNode.setDescription("Protocol Kind");
			// nodes.add(protocolKindNode);
		}

		if (logger.isDebugEnabled())
			logger.debug("number of nodes made: " + nodes.size());
		if (logger.isDebugEnabled())
			logger.debug(JSTreeNodeDTO.toPrettyJsonArray(nodes));

		return nodes;
	}

	// a recursive function to walk up the protocol tree
	private List<String> lookupProtocolTree(Protocol protocol, List<String> protocolNodeList) {
		List<ItxProtocolProtocol> ipps = ItxProtocolProtocol.findItxProtocolProtocolsBySecondProtocol(protocol)
				.getResultList();
		if (ipps.size() > 1) {
			logger.error("ERROR: there is more than a single protocol interacation. " + ipps.size());
		}
		for (ItxProtocolProtocol ipp : ipps) {
			if (ipp.getFirstProtocol().getLsType().equalsIgnoreCase("protocolTree")) {
				protocolNodeList.add(ipp.getFirstProtocol().getLsKind());
				lookupProtocolTree(ipp.getFirstProtocol(), protocolNodeList);
			}
		}
		return protocolNodeList;
	}

	@SuppressWarnings({ "unchecked", "null" })
	@Override
	public List<AnalysisGroupValueDTO> getFilteredAGData(ExperimentSearchRequestDTO searchRequest,
			Boolean onlyPublicData) {
		// DataViewer route to get analysis group data (results) based on filter
		// parameters
		searchRequest.getBatchCodeList().removeAll(Collections.singleton(null));
		searchRequest.getExperimentCodeList().removeAll(Collections.singleton(null));

		Set<String> uniqueBatchCodes = new HashSet<String>();
		if (searchRequest.getBatchCodeList() != null && searchRequest.getBatchCodeList().size() > 0) {
			uniqueBatchCodes.addAll(searchRequest.getBatchCodeList());
			if (logger.isDebugEnabled())
				logger.debug("size of uniqueBatchCodes: " + uniqueBatchCodes.size());
		}

		List<String> batchCodes = null;
		Collection<String> collectionOfCodes = null;

		boolean filteredGeneData = false;

		if (searchRequest.getBooleanFilter() != null && searchRequest.getBooleanFilter().equalsIgnoreCase("ADVANCED")) {
			// DO SQL substitutions for now. Try to do something more elegant later
			collectionOfCodes = AnalysisGroupValue.findBatchCodeBySearchFilter(searchRequest.getAdvancedFilterSQL())
					.getResultList();
			if (uniqueBatchCodes.size() > 0) {
				collectionOfCodes = CollectionUtils.intersection(collectionOfCodes, uniqueBatchCodes);
			}
			collectionOfCodes.removeAll(Collections.singleton(null));
			filteredGeneData = true;
		} else if (searchRequest.getBooleanFilter() != null && searchRequest.getSearchFilters() != null
				&& searchRequest.getSearchFilters().size() > 0) {
			filteredGeneData = true;
			boolean firstPass = true;
			for (ExperimentFilterSearchDTO singleSearchFilter : searchRequest.getSearchFilters()) {
				if (firstPass) {
					collectionOfCodes = AnalysisGroupValue
							.findBatchCodeBySearchFilter(searchRequest.getBatchCodeList(),
									searchRequest.getExperimentCodeList(), singleSearchFilter, onlyPublicData)
							.getResultList();
					if (logger.isDebugEnabled())
						logger.debug("size of firstBatchCodes: " + collectionOfCodes.size());
					firstPass = false;
				} else {
					batchCodes = AnalysisGroupValue
							.findBatchCodeBySearchFilter(searchRequest.getBatchCodeList(),
									searchRequest.getExperimentCodeList(), singleSearchFilter, onlyPublicData)
							.getResultList();
					if (logger.isDebugEnabled())
						logger.debug("size of firstBatchCodes: " + collectionOfCodes.size());
					if (logger.isDebugEnabled())
						logger.debug("size of secondBatchCodes: " + batchCodes.size());
					if (searchRequest.getBooleanFilter().equalsIgnoreCase("AND")) {
						collectionOfCodes = CollectionUtils.intersection(collectionOfCodes, batchCodes);
					} else {
						collectionOfCodes = CollectionUtils.union(collectionOfCodes, batchCodes);
					}
					if (logger.isDebugEnabled())
						logger.debug("size of intersectCodes: " + collectionOfCodes.size());
				}
			}
		} else if (uniqueBatchCodes.size() > 0) {
			collectionOfCodes = uniqueBatchCodes;
		} else {
			collectionOfCodes = uniqueBatchCodes;
			logger.debug("Searching for Compound Batch Codes using experiment codes: "
					+ searchRequest.getExperimentCodeList());
			collectionOfCodes.addAll(findCompoundBatchCodes(searchRequest.getExperimentCodeList()));
			logger.debug("Found: " + collectionOfCodes.toString());
		}
		logger.debug("collected these batch codes:" + collectionOfCodes.toString());
		Set<String> finalUniqueBatchCodes = new HashSet<String>();

		if (collectionOfCodes != null) {
			if (logger.isDebugEnabled())
				logger.debug("size of all intersectCodes: " + collectionOfCodes.size());
			finalUniqueBatchCodes.addAll(collectionOfCodes);
			if (logger.isDebugEnabled())
				logger.debug("number of unique batchCodes found: " + finalUniqueBatchCodes.size());
		}

		finalUniqueBatchCodes.removeAll(Collections.singleton(null));

		List<AnalysisGroupValueDTO> agValues = null;
		if (finalUniqueBatchCodes.size() > 0) {
			if (logger.isDebugEnabled())
				logger.debug("looking by expriment codes and batch codes");
			if (onlyPublicData)
				agValues = AnalysisGroupValue.findAnalysisGroupValueDTO(finalUniqueBatchCodes,
						searchRequest.getExperimentCodeList(), onlyPublicData);
			else
				agValues = AnalysisGroupValue
						.findAnalysisGroupValueDTO(finalUniqueBatchCodes, searchRequest.getExperimentCodeList())
						.getResultList();
			if (logger.isDebugEnabled())
				logger.debug("number of agValues found: " + agValues.size());
		} else if (!filteredGeneData && finalUniqueBatchCodes.size() == 0) {
			if (logger.isDebugEnabled())
				logger.debug("looking by expriment codes only");
			if (onlyPublicData)
				agValues = AnalysisGroupValue
						.findAnalysisGroupValueDTOByExperiments(searchRequest.getExperimentCodeList(), onlyPublicData)
						.getResultList();
			else
				agValues = AnalysisGroupValue
						.findAnalysisGroupValueDTOByExperiments(searchRequest.getExperimentCodeList()).getResultList();
			if (logger.isDebugEnabled())
				logger.debug("number of agValues found: " + agValues.size());
		} else if (filteredGeneData && finalUniqueBatchCodes.size() == 0) {
			if (logger.isDebugEnabled())
				logger.debug("no results found with the search filters");
			agValues = new ArrayList<AnalysisGroupValueDTO>();
		}

		return agValues;

	}

	private Collection<String> findCompoundBatchCodes(Collection<String> experimentCodes) {
		EntityManager em = AnalysisGroupValue.entityManager();
		String sql = "SELECT agv.codeValue FROM AnalysisGroupValue agv "
				+ "JOIN agv.lsState as ags "
				+ "JOIN ags.analysisGroup as ag "
				+ "JOIN ag.experiments as e "
				+ "WHERE e.codeName in :experimentCodes "
				+ "AND agv.lsType = 'codeValue' "
				+ "AND agv.lsKind = 'batch code' ";
		TypedQuery<String> q = em.createQuery(sql, String.class);
		q.setParameter("experimentCodes", experimentCodes);
		Collection<String> results = q.getResultList();
		logger.debug("results are" + results.toString());
		return results;
	}

	@Override
	public Collection<Experiment> findExperimentsByGenericMetaDataSearch(String queryString, String userName)
			throws TooManyResultsException {
		Collection<Experiment> rawResults = findExperimentsByGenericMetaDataSearch(queryString);
		if (propertiesUtilService.getRestrictExperiments()) {
			Collection<LsThing> projects = authorService.getUserProjects(userName);
			List<String> allowedProjectCodeNames = new ArrayList<String>();
			allowedProjectCodeNames.add("unassigned");
			for (LsThing project : projects) {
				allowedProjectCodeNames.add(project.getCodeName());
			}
			Collection<Experiment> results = new HashSet<Experiment>();
			for (Experiment rawResult : rawResults) {
				String experimentProject = null;
				for (ExperimentState state : rawResult.getLsStates()) {
					for (ExperimentValue value : state.getLsValues()) {
						if (value.getLsKind().equals("project")) {
							experimentProject = value.getCodeValue();
							break;
						}
					}
				}
				if (allowedProjectCodeNames.contains(experimentProject)) {
					results.add(rawResult);
				}
			}
			return results;
		} else {
			return rawResults;
		}

	}

	@Override
	public Collection<Experiment> findExperimentsByGenericMetaDataSearch(String queryString, List<String> projects)
			throws TooManyResultsException {
		Collection<Experiment> rawResults = findExperimentsByGenericMetaDataSearch(queryString);
		if (propertiesUtilService.getRestrictExperiments()) {
			projects.add("unassigned");
			Collection<Experiment> results = new HashSet<Experiment>();
			for (Experiment rawResult : rawResults) {
				String experimentProject = null;
				for (ExperimentState state : rawResult.getLsStates()) {
					if (!state.isIgnored() && !state.isDeleted()) {
						for (ExperimentValue value : state.getLsValues()) {
							if (value.getLsKind().equals("project") && !value.getDeleted() && !value.getIgnored()) {
								experimentProject = value.getCodeValue();
								break;
							}
						}
					}
				}
				if (projects.contains(experimentProject)) {
					results.add(rawResult);
				}
			}
			return results;
		} else {
			return rawResults;
		}
	}

	public Collection<Experiment> findExperimentsByGenericMetaDataSearch(String queryString)
			throws TooManyResultsException {
		// make our HashSets: experimentIdList will be filled/cleared/refilled for each
		// term
		// experimentList is the final search result
		HashSet<Long> experimentIdList = new HashSet<Long>();
		HashSet<Long> experimentAllIdList = new HashSet<Long>();
		Collection<Experiment> experimentList = new HashSet<Experiment>();
		// Split the query up on spaces
		List<String> splitQuery = SimpleUtil.splitSearchString(queryString);
		if (logger.isDebugEnabled())
			logger.debug("Number of search terms: " + splitQuery.size());
		// Protection from searching * in a database with too many experiments:
		if (splitQuery.contains("*")) {
			logger.warn("Query for '*' detected. Determining if number of results is too many.");
			int experimentCount = (int) Experiment.countExperiments();
			if (logger.isDebugEnabled())
				logger.debug("Found " + experimentCount + " experiments.");
			if (experimentCount > 1000) {
				throw new TooManyResultsException("Too many experiments will be returned with the query: " + "*");
			}
		}
		// Make the Map of terms and HashSets of experiment id's then fill. We will run
		// intersect logic later.
		Map<String, HashSet<Long>> resultsByTerm = new HashMap<String, HashSet<Long>>();
		for (String term : splitQuery) {
			experimentIdList.addAll(findExperimentIdsByMetadata(term, "CODENAME"));
			experimentIdList.addAll(findExperimentIdsByMetadata(term, "NAME"));
			experimentIdList.addAll(findExperimentIdsByMetadata(term, "SCIENTIST"));
			// experimentIdList.addAll(findExperimentIdsByMetadata(term, "RECORDEDBY"));
			experimentIdList.addAll(findExperimentIdsByMetadata(term, "KIND"));
			experimentIdList.addAll(findExperimentIdsByMetadata(term, "STATUS"));
			experimentIdList.addAll(findExperimentIdsByMetadata(term, "ANALYSIS STATUS"));
			experimentIdList.addAll(findExperimentIdsByMetadata(term, "PROTOCOL TYPE"));
			experimentIdList.addAll(findExperimentIdsByMetadata(term, "PROTOCOL KIND"));
			experimentIdList.addAll(findExperimentIdsByMetadata(term, "PROTOCOL CODENAME"));
			experimentIdList.addAll(findExperimentIdsByMetadata(term, "PROTOCOL NAME"));
			experimentIdList.addAll(findExperimentIdsByMetadata(term, "DATE"));
			experimentIdList.addAll(findExperimentIdsByMetadata(term, "NOTEBOOK"));
			experimentIdList.addAll(findExperimentIdsByMetadata(term, "KEYWORD"));
			experimentIdList.addAll(findExperimentIdsByMetadata(term, "ASSAY ACTIVITY"));
			experimentIdList.addAll(findExperimentIdsByMetadata(term, "MOLECULAR TARGET"));
			experimentIdList.addAll(findExperimentIdsByMetadata(term, "ASSAY TYPE"));
			experimentIdList.addAll(findExperimentIdsByMetadata(term, "ASSAY TECHNOLOGY"));
			experimentIdList.addAll(findExperimentIdsByMetadata(term, "CELL LINE"));
			experimentIdList.addAll(findExperimentIdsByMetadata(term, "TARGET ORIGIN"));
			experimentIdList.addAll(findExperimentIdsByMetadata(term, "ASSAY STAGE"));

			resultsByTerm.put(term, new HashSet<Long>(experimentIdList));
			experimentAllIdList.addAll(experimentIdList);
			experimentIdList.clear();
		}
		// Here is the intersect logic
		for (String term : splitQuery) {
			experimentAllIdList.retainAll(resultsByTerm.get(term));
		}
		for (Long id : experimentAllIdList)
			experimentList.add(Experiment.findExperiment(id));

		// This method uses finders that will find everything, whether or not it is
		// ignored or deleted
		Collection<Experiment> result = new HashSet<Experiment>();
		for (Experiment experiment : experimentList) {
			// For Experiment Browser, we want to see soft deleted (ignored=true,
			// deleted=false), but not hard deleted (ignored=deleted=true)
			if (experiment.isDeleted()) {
				logger.debug("removing a deleted experiment from the results");
			} else {
				result.add(experiment);
			}
		}
		return result;
	}

	private Collection<Long> findExperimentIdsByMetadata(String queryString, String searchBy) {
		Collection<Long> experimentIdList = new HashSet<Long>();
		if (searchBy == "CODENAME") {
			List<Experiment> experiments = Experiment.findExperimentsByCodeNameLike(queryString).getResultList();
			if (!experiments.isEmpty()) {
				for (Experiment experiment : experiments) {
					experimentIdList.add(experiment.getId());
				}
			}
			experiments.clear();
		}
		if (searchBy == "TYPE") {
			List<Experiment> experiments = Experiment.findExperimentsByLsTypeLike(queryString).getResultList();
			if (!experiments.isEmpty()) {
				for (Experiment experiment : experiments) {
					experimentIdList.add(experiment.getId());
				}
			}
			experiments.clear();
		}
		if (searchBy == "KIND") {
			List<Experiment> experiments = Experiment.findExperimentsByLsKindLike(queryString).getResultList();
			if (!experiments.isEmpty()) {
				for (Experiment experiment : experiments) {
					experimentIdList.add(experiment.getId());
				}
			}
			experiments.clear();
		}
		if (searchBy == "NAME") {
			List<ExperimentLabel> experimentLabels = ExperimentLabel.findExperimentLabelsByLabelTextLike(queryString)
					.getResultList();
			if (!experimentLabels.isEmpty()) {
				for (ExperimentLabel experimentLabel : experimentLabels) {
					experimentIdList.add(experimentLabel.getExperiment().getId());
				}
			}
			experimentLabels.clear();
		}

		if (searchBy == "SCIENTIST") {
			Collection<ExperimentValue> experimentValues = ExperimentValue
					.findExperimentValuesByLsKindEqualsAndCodeValueLike("scientist", queryString).getResultList();
			if (!experimentValues.isEmpty()) {
				for (ExperimentValue experimentValue : experimentValues) {
					experimentIdList.add(experimentValue.getLsState().getExperiment().getId());
				}
			}
			experimentValues.clear();
		}
		if (searchBy == "RECORDEDBY") {
			List<Experiment> experiments = Experiment.findExperimentsByRecordedByLike(queryString).getResultList();
			if (!experiments.isEmpty()) {
				for (Experiment experiment : experiments) {
					experimentIdList.add(experiment.getId());
				}
			}
			experiments.clear();
		}
		if (searchBy == "STATUS") {
			Collection<ExperimentValue> experimentValues = ExperimentValue
					.findExperimentValuesByLsKindEqualsAndCodeValueLike("experiment status", queryString)
					.getResultList();
			if (!experimentValues.isEmpty()) {
				for (ExperimentValue experimentValue : experimentValues) {
					if (!experimentValue.isIgnored())
						experimentIdList.add(experimentValue.getLsState().getExperiment().getId());
				}
			}
			experimentValues.clear();
		}
		if (searchBy == "ANALYSIS STATUS") {
			Collection<ExperimentValue> experimentValues = ExperimentValue
					.findExperimentValuesByLsKindEqualsAndCodeValueLike("analysis status", queryString).getResultList();
			if (!experimentValues.isEmpty()) {
				for (ExperimentValue experimentValue : experimentValues) {
					if (!experimentValue.isIgnored())
						experimentIdList.add(experimentValue.getLsState().getExperiment().getId());
				}
			}
			experimentValues.clear();
		}
		if (searchBy == "PROTOCOL KIND") {
			Collection<Long> protocolIds = protocolService.findProtocolIdsByMetadata(queryString, "KIND");
			Set<Experiment> experiments = new HashSet<Experiment>();
			if (!protocolIds.isEmpty()) {
				for (Long id : protocolIds) {
					experiments.addAll(Experiment.findExperimentsByProtocol(Protocol.findProtocol(id)).getResultList());
				}
			}
			if (!experiments.isEmpty()) {
				for (Experiment experiment : experiments) {
					experimentIdList.add(experiment.getId());
				}
			}
			experiments.clear();
		}
		if (searchBy == "PROTOCOL NAME") {
			Collection<Long> protocolIds = protocolService.findProtocolIdsByMetadata(queryString, "NAME");
			Set<Experiment> experiments = new HashSet<Experiment>();
			if (!protocolIds.isEmpty()) {
				for (Long id : protocolIds) {
					experiments.addAll(Experiment.findExperimentsByProtocol(Protocol.findProtocol(id)).getResultList());
				}
			}
			if (!experiments.isEmpty()) {
				for (Experiment experiment : experiments) {
					experimentIdList.add(experiment.getId());
				}
			}
			experiments.clear();
		}
		if (searchBy == "PROTOCOL TYPE") {
			Collection<Long> protocolIds = protocolService.findProtocolIdsByMetadata(queryString, "TYPE");
			Set<Experiment> experiments = new HashSet<Experiment>();
			if (!protocolIds.isEmpty()) {
				for (Long id : protocolIds) {
					experiments.addAll(Experiment.findExperimentsByProtocol(Protocol.findProtocol(id)).getResultList());
				}
			}
			if (!experiments.isEmpty()) {
				for (Experiment experiment : experiments) {
					experimentIdList.add(experiment.getId());
				}
			}
			experiments.clear();
		}
		if (searchBy == "PROTOCOL CODENAME") {
			Collection<Long> protocolIds = protocolService.findProtocolIdsByMetadata(queryString, "CODENAME");
			Set<Experiment> experiments = new HashSet<Experiment>();
			if (!protocolIds.isEmpty()) {
				for (Long id : protocolIds) {
					experiments.addAll(Experiment.findExperimentsByProtocol(Protocol.findProtocol(id)).getResultList());
				}
			}
			if (!experiments.isEmpty()) {
				for (Experiment experiment : experiments) {
					experimentIdList.add(experiment.getId());
				}
			}
			experiments.clear();
		}
		if (searchBy == "DATE") {
			Collection<ExperimentValue> experimentValues = new HashSet<ExperimentValue>();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			DateFormat df2 = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
			try {
				Date date = df.parse(queryString);
				experimentValues = ExperimentValue
						.findExperimentValuesByLsKindEqualsAndDateValueLike("completion date", date).getResultList();
			} catch (Exception e) {
				try {
					Date date = df2.parse(queryString);
					experimentValues = ExperimentValue
							.findExperimentValuesByLsKindEqualsAndDateValueLike("completion date", date)
							.getResultList();
				} catch (Exception e2) {
					// do nothing
				}
			}
			if (!experimentValues.isEmpty()) {
				for (ExperimentValue experimentValue : experimentValues) {
					experimentIdList.add(experimentValue.getLsState().getExperiment().getId());
				}
			}
			experimentValues.clear();
		}
		if (searchBy == "NOTEBOOK") {
			Collection<ExperimentValue> experimentValues = ExperimentValue
					.findExperimentValuesByLsKindEqualsAndStringValueLike("notebook", queryString).getResultList();
			if (!experimentValues.isEmpty()) {
				for (ExperimentValue experimentValue : experimentValues) {
					experimentIdList.add(experimentValue.getLsState().getExperiment().getId());
				}
			}
			experimentValues.clear();
		}
		if (searchBy == "KEYWORD") {
			Collection<LsTag> tags = LsTag.findLsTagsByTagTextLike(queryString).getResultList();
			if (!tags.isEmpty()) {
				for (LsTag tag : tags) {
					Collection<Experiment> experiments = tag.getExperiments();
					if (!experiments.isEmpty()) {
						for (Experiment experiment : experiments) {
							experimentIdList.add(experiment.getId());
						}
					}
					experiments.clear();
				}
			}
			tags.clear();
		}
		if (searchBy == "ASSAY ACTIVITY" || searchBy == "MOLECULAR TARGET" || searchBy == "ASSAY TYPE"
				|| searchBy == "ASSAY TECHNOLOGY" || searchBy == "CELL LINE" || searchBy == "TARGET ORIGIN"
				|| searchBy == "ASSAY STAGE") {
			Collection<DDictValue> ddictValues = DDictValue.findDDictValuesByLabelTextLike(queryString).getResultList();
			if (!ddictValues.isEmpty()) {
				for (DDictValue ddictvalue : ddictValues) {
					if (ddictvalue.getShortName() != null) {
						Collection<ExperimentValue> experimentValues = ExperimentValue
								.findExperimentValuesByLsKindEqualsAndCodeValueLike(searchBy.toLowerCase(),
										ddictvalue.getShortName())
								.getResultList();
						if (!experimentValues.isEmpty()) {
							for (ExperimentValue experimentValue : experimentValues) {
								experimentIdList.add(experimentValue.getLsState().getExperiment().getId());
							}
						}
						experimentValues.clear();
					}
				}
			}
		}

		return experimentIdList;
	}

	@Override
	public Collection<Experiment> findExperimentsByMetadataJson(String json) {
		Collection<Experiment> experimentList = new HashSet<Experiment>();
		Collection<StringCollectionDTO> metaDataList = StringCollectionDTO.fromJsonArrayToStringCollectioes(json);
		for (StringCollectionDTO metaData : metaDataList) {
			Collection<Experiment> experiments = findExperimentByMetadata(metaData.getName());
			if (experiments.size() > 0) {
				experimentList.addAll(experiments);
			}
		}

		return experimentList;
	}

	@Override
	public Collection<Experiment> findExperimentsByMetadataJson(List<StringCollectionDTO> metaDataList) {
		Collection<Experiment> experimentList = new HashSet<Experiment>();
		for (StringCollectionDTO metaData : metaDataList) {
			Collection<Experiment> experiments = findExperimentByMetadata(metaData.getName());
			if (experiments.size() > 0) {
				experimentList.addAll(experiments);
			}
		}

		return experimentList;
	}

	private Collection<Experiment> findExperimentByMetadata(String queryString) {
		Collection<Experiment> experimentList = new HashSet<Experiment>();

		// find by experiment codeName
		List<Experiment> experiments = Experiment.findExperimentsByCodeNameEquals(queryString).getResultList();
		if (!experiments.isEmpty()) {
			experimentList.addAll(experiments);
		}

		// find by experiment name
		Collection<Experiment> experimentsByName = Experiment.findExperimentByName(queryString);
		if (!experimentsByName.isEmpty()) {
			experimentList.addAll(experimentsByName);
		}

		return experimentList;
	}

	public Collection<Experiment> findExperimentsByMetadata(String queryString, String searchBy) {
		Collection<Experiment> experimentList = new HashSet<Experiment>();
		Collection<Long> experimentIdList = findExperimentIdsByMetadata(queryString, searchBy);
		if (!experimentIdList.isEmpty()) {
			for (Long id : experimentIdList) {
				experimentList.add(Experiment.findExperiment(id));
			}
		}
		return experimentList;
	}

	@Override
	@Transactional
	public Set<Experiment> findExperimentsByRequestMetadata(
			Map<String, String> requestParams) {

		Set<Experiment> result = new HashSet<Experiment>();
		if (requestParams.isEmpty()) {
			List<Experiment> experiments = Experiment.findAllExperiments();
			result.addAll(experiments);
			return result;
		}

		Set<Experiment> resultByProtocolKind = new HashSet<Experiment>();
		Set<Experiment> resultByProtocolType = new HashSet<Experiment>();
		Set<Experiment> resultByProtocolName = new HashSet<Experiment>();
		Set<Experiment> resultByProtocolCodeName = new HashSet<Experiment>();
		Set<Experiment> resultByKind = new HashSet<Experiment>();
		Set<Experiment> resultByType = new HashSet<Experiment>();
		Set<Experiment> resultByName = new HashSet<Experiment>();
		Set<Experiment> resultByCodeName = new HashSet<Experiment>();
		if (requestParams.containsKey("protocolKind"))
			resultByProtocolKind.addAll(findExperimentsByMetadata(requestParams.get("protocolKind"), "PROTOCOL KIND"));
		if (requestParams.containsKey("protocolType"))
			resultByProtocolType.addAll(findExperimentsByMetadata(requestParams.get("protocolType"), "PROTOCOL TYPE"));
		if (requestParams.containsKey("protocolName"))
			resultByProtocolName.addAll(findExperimentsByMetadata(requestParams.get("protocolName"), "PROTOCOL NAME"));
		if (requestParams.containsKey("protocolCodeName"))
			resultByProtocolCodeName
					.addAll(findExperimentsByMetadata(requestParams.get("protocolCodeName"), "PROTOCOL CODENAME"));
		if (requestParams.containsKey("kind"))
			resultByKind.addAll(findExperimentsByMetadata(requestParams.get("kind"), "KIND"));
		if (requestParams.containsKey("type"))
			resultByType.addAll(findExperimentsByMetadata(requestParams.get("type"), "TYPE"));
		if (requestParams.containsKey("name"))
			resultByName.addAll(findExperimentsByMetadata(requestParams.get("name"), "NAME"));
		if (requestParams.containsKey("codeName"))
			resultByCodeName.addAll(findExperimentsByMetadata(requestParams.get("codeName"), "CODENAME"));

		result.addAll(resultByProtocolKind);
		result.addAll(resultByProtocolType);
		result.addAll(resultByProtocolName);
		result.addAll(resultByProtocolCodeName);
		result.addAll(resultByKind);
		result.addAll(resultByType);
		result.addAll(resultByName);
		result.addAll(resultByCodeName);

		if (requestParams.containsKey("protocolKind"))
			result.retainAll(resultByProtocolKind);
		if (requestParams.containsKey("protocolType"))
			result.retainAll(resultByProtocolType);
		if (requestParams.containsKey("protocolName"))
			result.retainAll(resultByProtocolName);
		if (requestParams.containsKey("protocolCodeName"))
			result.retainAll(resultByProtocolCodeName);
		if (requestParams.containsKey("kind"))
			result.retainAll(resultByKind);
		if (requestParams.containsKey("type"))
			result.retainAll(resultByType);
		if (requestParams.containsKey("name"))
			result.retainAll(resultByName);
		if (requestParams.containsKey("codeName"))
			result.retainAll(resultByCodeName);

		return result;
	}

	@Override
	public boolean isSoftDeleted(Experiment experiment) {
		boolean isSoftDeleted = false;
		if (experiment.isIgnored() && !experiment.isDeleted())
			isSoftDeleted = true;
		return isSoftDeleted;
		// Long experimentId = experiment.getId();
		// List<ExperimentValue> experimentValues =
		// experimentValueService.getExperimentValuesByExperimentIdAndStateTypeKindAndValueTypeKind(experimentId,
		// "metadata", "experiment metadata", "stringValue", "status");
		// boolean isSoftDeleted = false;
		// for (ExperimentValue experimentValue : experimentValues) {
		// if (experimentValue.getStringValue() != null &&
		// experimentValue.getStringValue().equals("Deleted")) isSoftDeleted = true;
		// }
		// return isSoftDeleted;
	}

	@Override
	@Transactional
	public String deleteExperimentDataByBatchCode(String experimentCode, String batchCode) {
		Experiment experiment = Experiment.findExperimentsByCodeNameEquals(experimentCode).getSingleResult();
		String csvString = null;
		if (experiment != null) {
			//Find analysis group values with this batch code
			//Set with batch code
			Set<String> batchCodes = new HashSet<String>();
			batchCodes.add(batchCode);

			// Set with experimentCode
			Set<String> experimentCodes = new HashSet<String>();
			experimentCodes.add(experimentCode);
			
			// Logical delete analysis groups
			List<AnalysisGroupValueDTO> analysisGroupValueDTOs = AnalysisGroupValue.findAnalysisGroupValueDTO(batchCodes, experimentCodes).getResultList();
			csvString = AnalysisGroupValueDTO.toCsv(analysisGroupValueDTOs);
			// Delete analysis group values
			for (AnalysisGroupValueDTO analysisGroupValueDTO : analysisGroupValueDTOs) {
				logger.info("Logically deleting analysis group: " + analysisGroupValueDTO.getAgId());
				AnalysisGroup.findAnalysisGroup(analysisGroupValueDTO.getAgId()).logicalDelete();
			}
		}
		return csvString;
	}

	@Override
	public boolean deleteAnalysisGroupsByExperiment(Experiment experiment) {
		// boolean successfullyDeleted = true;
		// try {
		// for (AnalysisGroup analysisGroup : experiment.getAnalysisGroups()){
		// analysisGroup.logicalDelete();
		// analysisGroup.merge();
		// }
		// } catch (Exception e) {
		// successfullyDeleted = false;
		// logger.error("Error in deleting analysis groups by experiment: " +
		// e.toString());
		// }
		// return successfullyDeleted;

		EntityManager em = Experiment.entityManager();
		String sqlQuery = "UPDATE AnalysisGroup AS ag SET ag.ignored = true, ag.deleted = true "
				+ "WHERE ag.id IN ( SELECT ag2.id FROM AnalysisGroup AS ag2 JOIN ag2.experiments e "
				+ "WHERE e.id = :experimentId ) ";
		Query q = em.createQuery(sqlQuery);
		q.setParameter("experimentId", experiment.getId());
		try {
			int numRows = q.executeUpdate();
			if (logger.isDebugEnabled())
				logger.debug(numRows + " AnalysisGroups logically deleted.");
		} catch (Exception e) {
			logger.error("Caught error deleting AnalysisGroups under experiment: " + experiment.getCodeName() + " "
					+ e.toString());
			return false;
		}
		return true;
	}

	@Override
	public List<ExperimentDataDTO> getExperimentData(String batchCode, boolean showOnlyPublicData) {
		List<ExperimentDataDTO> exptDataSet = new ArrayList<ExperimentDataDTO>();
		List<AnalysisGroupValueDTO> values = AnalysisGroupValue.findAnalysisGroupValueDTO(batchCode, showOnlyPublicData)
				.getResultList();

		Set<Long> protocolIds = new HashSet<Long>();
		Set<Long> experimentIds = new HashSet<Long>();
		for (AnalysisGroupValueDTO value : values) {
			protocolIds.add(value.getProtocolId());
			experimentIds.add(value.getExperimentId());
		}

		for (Long experimentId : experimentIds) {
			ExperimentDataDTO exptData = new ExperimentDataDTO();
			Experiment testExperiment = Experiment.findExperiment(experimentId);
			exptData.setExperimentId(experimentId);
			exptData.setExperimentCodeName(testExperiment.getCodeName());
			exptData.setExperimentName(testExperiment.findPreferredName());
			exptData.setExperimentShortDescription(testExperiment.getShortDescription());
			exptData.setLsTags(testExperiment.getLsTags());
			Protocol protocol = Protocol.findProtocol(testExperiment.getProtocol().getId());
			List<ProtocolLabel> protocolLabels = ProtocolLabel.findProtocolPreferredName(protocol.getId())
					.getResultList();
			if (protocolLabels.size() > 1)
				logger.error("found multiple protocol preferred names for " + protocol.getCodeName());
			for (ProtocolLabel protocolLabel : protocolLabels) {
				exptData.setProtocolName(protocolLabel.getLabelText());
			}
			exptData.setProtocolCodeName(protocol.getCodeName());
			exptData.setProtocolId(protocol.getId());
			exptData.setProtocolShortDescription(protocol.getShortDescription());
			List<ProtocolValue> assayTreeRules = ProtocolValue
					.findProtocolValuesByProtocolIDAndStateTypeKindAndValueTypeKind(protocol.getId(), "metadata",
							"protocol metadata", "stringValue", "assay tree rule")
					.getResultList();
			if (assayTreeRules.size() > 1)
				logger.warn("found multiple assay tree rules for " + protocol.getCodeName());
			// if (assayTreeRules.size() == 0) logger.error("no assay tree rules for " +
			// protocol.getCodeName());

			for (ProtocolValue assayTreeRule : assayTreeRules) {
				if (assayTreeRules.size() > 1)
					logger.warn(assayTreeRule.toJson());
				if (assayTreeRule.getStringValue() != null) {
					exptData.setAssayFolderRule(assayTreeRule.getStringValue());
				}
			}
			for (AnalysisGroupValueDTO value : values) {
				if (value.getExperimentId().longValue() == experimentId) {
					exptData.getAnalysisGroupValues().add(value);
				}
			}

			exptDataSet.add(exptData);
		}
		logger.debug("@@@@@@@@@@@@@@@@ printing the experiment metadata @@@@@@@@@@@@@");
		if (logger.isDebugEnabled())
			logger.debug(ExperimentDataDTO.toJsonArray(exptDataSet));

		return exptDataSet;

	}

	@Override
	public Collection<ExperimentErrorMessageDTO> findExperimentsByCodeNames(List<String> codeNames) {
		if (codeNames.isEmpty())
			return new ArrayList<ExperimentErrorMessageDTO>();
		EntityManager em = Experiment.entityManager();
		String queryString = "SELECT new com.labsynch.labseer.dto.ExperimentErrorMessageDTO( "
				+ "experiment.codeName, "
				+ "experiment )"
				+ " FROM Experiment experiment ";
		queryString += "where ( experiment.ignored <> true ) and ";
		Collection<Query> queries = SimpleUtil.splitHqlInClause(em, queryString, "experiment.codeName", codeNames);
		Collection<ExperimentErrorMessageDTO> results = new HashSet<ExperimentErrorMessageDTO>();
		for (Query q : queries) {
			results.addAll(q.getResultList());
		}
		// diff request with results to find codeNames that could not be found
		HashSet<String> requestCodeNames = new HashSet<String>();
		requestCodeNames.addAll(codeNames);
		HashSet<String> foundCodeNames = new HashSet<String>();
		for (ExperimentErrorMessageDTO result : results) {
			foundCodeNames.add(result.getExperimentCodeName());
		}
		requestCodeNames.removeAll(foundCodeNames);
		if (!requestCodeNames.isEmpty()) {
			for (String notFoundCodeName : requestCodeNames) {
				ExperimentErrorMessageDTO notFoundDTO = new ExperimentErrorMessageDTO();
				notFoundDTO.setExperimentCodeName(notFoundCodeName);
				notFoundDTO.setLevel("error");
				notFoundDTO.setMessage("experimentCodeName not found");
				results.add(notFoundDTO);
			}
		}

		return results;
	}

	@Override
	public List<CodeTableDTO> getExperimentsAsCodeTables(String lsType,
			String lsKind) {
		Collection<Experiment> experiments;
		if ((lsType != null && lsType.length() > 0) && (lsKind != null && lsKind.length() > 0)) {
			experiments = Experiment.findExperimentsByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList();
		} else if (lsType != null && lsType.length() > 0) {
			experiments = Experiment.findExperimentsByLsTypeEquals(lsType).getResultList();
		} else if (lsKind != null && lsKind.length() > 0) {
			experiments = Experiment.findExperimentsByLsKindEquals(lsKind).getResultList();
		} else {
			experiments = Experiment.findAllExperiments();
		}
		List<CodeTableDTO> codetables = convertExperimentsToCodeTables(experiments);
		codetables = CodeTableDTO.sortCodeTables(codetables);
		return codetables;
	}

	@Override
	public List<CodeTableDTO> convertExperimentsToCodeTables(Collection<Experiment> experiments) {
		List<CodeTableDTO> codetables = new ArrayList<CodeTableDTO>();
		for (Experiment experiment : experiments) {
			CodeTableDTO codetable = new CodeTableDTO(experiment);
			codetables.add(codetable);
		}
		return codetables;
	}

	@Override
	public PreferredNameResultsDTO getCodeNameFromName(String experimentType,
			String experimentKind, String labelType, String labelKind,
			PreferredNameRequestDTO requestDTO) {
		logger.info("number of requests: " + requestDTO.getRequests().size());
		Collection<PreferredNameDTO> requests = requestDTO.getRequests();

		PreferredNameResultsDTO responseOutput = new PreferredNameResultsDTO();
		Collection<ErrorMessageDTO> errors = new HashSet<ErrorMessageDTO>();

		for (PreferredNameDTO request : requests) {
			request.setPreferredName("");
			request.setReferenceName("");
			List<Experiment> experiments = new ArrayList<Experiment>();
			if (labelType == null || labelKind == null || labelType.length() == 0 || labelKind.length() == 0) {
				experiments = Experiment
						.findExperimentByLabelText(experimentType, experimentKind, request.getRequestName())
						.getResultList();

			} else {
				experiments = Experiment.findExperimentByLabelText(experimentType, experimentKind, labelType, labelKind,
						request.getRequestName()).getResultList();
			}
			if (experiments.size() == 1) {
				request.setPreferredName(pickBestLabel(experiments.get(0)));
				request.setReferenceName(experiments.get(0).getCodeName());
			} else if (experiments.size() > 1) {
				responseOutput.setError(true);
				ErrorMessageDTO error = new ErrorMessageDTO();
				error.setLevel("MULTIPLE RESULTS");
				error.setMessage("FOUND MULTIPLE EXPERIMENTS WITH THE SAME NAME: " + request.getRequestName());
				logger.error("FOUND MULTIPLE EXPERIMENTS WITH THE SAME NAME: " + request.getRequestName());
				errors.add(error);
			} else {
				try {
					Experiment codeNameMatch = Experiment.findExperimentsByCodeNameEquals(request.getRequestName())
							.getSingleResult();
					if (codeNameMatch.getLsKind().equals(experimentKind)
							&& codeNameMatch.getLsType().equals(experimentType)) {
						logger.info("Made it to the codeMatch");
						request.setPreferredName(pickBestLabel(codeNameMatch));
						request.setReferenceName(codeNameMatch.getCodeName());
					} else {
						logger.info("Did not find an EXPERIMENT WITH THE REQUESTED NAME: " + request.getRequestName());
					}
				} catch (NoResultException e) {
					logger.info("Did not find an EXPERIMENT WITH THE REQUESTED NAME: " + request.getRequestName());
				}
			}
		}
		responseOutput.setResults(requests);
		responseOutput.setErrorMessages(errors);

		return responseOutput;
	}

	private String pickBestLabel(Experiment experiment) {
		Collection<ExperimentLabel> labels = experiment.getLsLabels();
		if (labels.isEmpty())
			return null;
		return ExperimentLabel.pickBestLabel(labels).getLabelText();
	}

	@Override
	public Collection<String> getExperimentCodesByDateValueComparison(
			DateValueComparisonRequest requestDTO) throws Exception {
		if (requestDTO.getStateType() == null || requestDTO.getStateType().length() == 0)
			throw new Exception("Must provide stateType");
		if (requestDTO.getStateKind() == null || requestDTO.getStateKind().length() == 0)
			throw new Exception("Must provide stateKind");
		if (requestDTO.getValueKind() == null || requestDTO.getValueKind().length() == 0)
			throw new Exception("Must provide valueKind");
		if (requestDTO.getSecondsDelta() == null)
			throw new Exception("Must provide secondsDelta");
		if (requestDTO.getNewerThanModified() == null)
			requestDTO.setNewerThanModified(false);
		EntityManager em = Experiment.entityManager();
		String query = "SELECT new Map( experiment.codeName AS codeName, experiment.modifiedDate AS modifiedDate, experiment.recordedDate as recordedDate, value.dateValue AS dateValue )"
				+ "FROM Experiment AS experiment "
				+ "JOIN experiment.lsStates  state "
				+ "WITH state.ignored = false "
				+ "AND state.lsType = :stateType "
				+ "AND state.lsKind = :stateKind "
				+ "LEFT OUTER JOIN state.lsValues AS value "
				+ "WITH value.ignored = false "
				+ "AND value.lsType = :valueType "
				+ "AND value.lsKind = :valueKind "
				+ "WHERE experiment.ignored = false ";
		if (requestDTO.getLsType() != null && requestDTO.getLsType().length() > 0)
			query += "AND experiment.lsType = :experimentType ";
		if (requestDTO.getLsKind() != null && requestDTO.getLsKind().length() > 0)
			query += "AND experiment.lsKind = :experimentKind ";

		TypedQuery<Map> q = em.createQuery(query, Map.class);
		q.setParameter("stateType", requestDTO.getStateType());
		q.setParameter("stateKind", requestDTO.getStateKind());
		q.setParameter("valueType", "dateValue");
		q.setParameter("valueKind", requestDTO.getValueKind());
		if (requestDTO.getLsType() != null && requestDTO.getLsType().length() > 0)
			q.setParameter("experimentType", requestDTO.getLsType());
		if (requestDTO.getLsKind() != null && requestDTO.getLsKind().length() > 0)
			q.setParameter("experimentKind", requestDTO.getLsKind());

		Collection<Map> resultMaps = q.getResultList();
		Collection<String> experimentCodes = new HashSet<String>();
		for (Map resultMap : resultMaps) {
			if (resultMap.get("dateValue") == null)
				experimentCodes.add((String) resultMap.get("codeName"));
			else {
				Date dateValue = (Date) resultMap.get("dateValue");
				Date modifiedDate;
				if (resultMap.get("modifiedDate") == null) {
					modifiedDate = (Date) resultMap.get("recordedDate");
				} else {
					modifiedDate = (Date) resultMap.get("modifiedDate");
				}
				Integer secondsDelta = requestDTO.getSecondsDelta();
				logger.debug("dateValue: " + dateValue.getTime());
				logger.debug("modifiedDate: " + modifiedDate.getTime());
				if (requestDTO.getNewerThanModified()) {
					if (dateValue.getTime() - secondsDelta * 1000 > modifiedDate.getTime())
						experimentCodes.add((String) resultMap.get("codeName"));
				} else {
					if (dateValue.getTime() + secondsDelta * 1000 < modifiedDate.getTime())
						experimentCodes.add((String) resultMap.get("codeName"));
				}
			}
		}
		return experimentCodes;
	}

}
