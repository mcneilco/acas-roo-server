package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerLabel;
import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ContainerValue;
import com.labsynch.labseer.domain.ItxContainerContainer;
import com.labsynch.labseer.domain.ItxContainerContainerState;
import com.labsynch.labseer.domain.ItxContainerContainerValue;
import com.labsynch.labseer.domain.ItxSubjectContainer;
import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectLabel;
import com.labsynch.labseer.dto.AutoLabelDTO;
import com.labsynch.labseer.dto.CodeLabelDTO;
import com.labsynch.labseer.dto.CodeTableDTO;
import com.labsynch.labseer.dto.ContainerBatchCodeDTO;
import com.labsynch.labseer.dto.ContainerBrowserQueryDTO;
import com.labsynch.labseer.dto.ContainerCodeNameStateDTO;
import com.labsynch.labseer.dto.ContainerDependencyCheckDTO;
import com.labsynch.labseer.dto.ContainerErrorMessageDTO;
import com.labsynch.labseer.dto.ContainerLocationDTO;
import com.labsynch.labseer.dto.ContainerLocationTreeDTO;
import com.labsynch.labseer.dto.ContainerQueryDTO;
import com.labsynch.labseer.dto.ContainerRequestDTO;
import com.labsynch.labseer.dto.ContainerSearchRequestDTO;
import com.labsynch.labseer.dto.ContainerValueRequestDTO;
import com.labsynch.labseer.dto.ContainerWellCodeDTO;
import com.labsynch.labseer.dto.CreatePlateRequestDTO;
import com.labsynch.labseer.dto.ErrorMessageDTO;
import com.labsynch.labseer.dto.ItxQueryDTO;
import com.labsynch.labseer.dto.LabelQueryDTO;
import com.labsynch.labseer.dto.PlateStubDTO;
import com.labsynch.labseer.dto.PlateWellDTO;
import com.labsynch.labseer.dto.PreferredNameDTO;
import com.labsynch.labseer.dto.PreferredNameRequestDTO;
import com.labsynch.labseer.dto.PreferredNameResultsDTO;
import com.labsynch.labseer.dto.ValueQueryDTO;
import com.labsynch.labseer.dto.WellContentDTO;
import com.labsynch.labseer.dto.WellStubDTO;
import com.labsynch.labseer.exceptions.ErrorMessage;
import com.labsynch.labseer.exceptions.UniqueNameException;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;
import com.labsynch.labseer.utils.SimpleUtil.DbType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONTokener;

@Service
@Transactional
public class ContainerServiceImpl implements ContainerService {

	private static final Logger logger = LoggerFactory.getLogger(ContainerServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Autowired
	private AutoLabelService autoLabelService;

	private JdbcTemplate jdbcTemplate;

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Collection<Container> saveLsContainersFile(String jsonFile) {
		Collection<Container> savedContainers = new ArrayList<Container>();
		int batchSize = propertiesUtilService.getBatchSize();
		int i = 0;

		try {
			BufferedReader br = new BufferedReader(new FileReader(jsonFile));
			JSONTokener jsonTokens = new JSONTokener(br);
			Object token;
			char delimiter;
			char END_OF_ARRAY = ']';
			while (jsonTokens.more()) {
				delimiter = jsonTokens.nextClean();
				if (delimiter != END_OF_ARRAY) {
					token = jsonTokens.nextValue();
					Container container = saveContainer(token.toString());
					savedContainers.add(container);
					if (i % batchSize == 0) {
						container.flush();
						container.clear();
					}
					i++;
				} else {
					break;
				}
			}
		} catch (Exception e) {
			logger.error("ERROR: " + e);
		}

		return savedContainers;

	}

	@Override
	public Collection<Container> saveLsContainersParse(String json) {
		Collection<Container> savedContainers = new ArrayList<Container>();
		int batchSize = propertiesUtilService.getBatchSize();
		int i = 0;

		try {
			StringReader sr = new StringReader(json);
			BufferedReader br = new BufferedReader(sr);

			for (Container container : Container.fromJsonArrayToContainers(br)) {
				savedContainers.add(container);
				if (i % batchSize == 0) {
					container.flush();
					container.clear();
				}
				i++;
			}

		} catch (Exception e) {
			logger.error("ERROR: " + e);
		}

		return savedContainers;

	}

	private Container saveContainer(String json) {
		Container container = Container.fromJsonToContainer(json);
		return saveContainer(container);
	}

	private Container saveContainer(Container container) {
		Container newContainer = new Container(container);
		newContainer.persist();
		if (logger.isDebugEnabled())
			logger.debug("container: " + container.toJson());
		if (container.getLsLabels() != null) {
			for (ContainerLabel containerLabel : container.getLsLabels()) {
				ContainerLabel newContainerLabel = new ContainerLabel(containerLabel);
				newContainerLabel.setContainer(newContainer);
				if (logger.isDebugEnabled())
					logger.debug("here is the newcontainerLabel before save: " + newContainerLabel.toJson());
				newContainerLabel.persist();
			}
		} else {
			logger.info("No container labels to save");
		}

		if (container.getLsStates() != null) {
			for (ContainerState LsState : container.getLsStates()) {
				ContainerState newLsState = new ContainerState(LsState);
				newLsState.setContainer(newContainer);
				if (logger.isDebugEnabled())
					logger.debug("here is the newLsState before save: " + newLsState.toJson());
				newLsState.persist();
				if (logger.isDebugEnabled())
					logger.debug("persisted the newLsState: " + newLsState.toJson());
				if (LsState.getLsValues() != null) {
					for (ContainerValue containerValue : LsState.getLsValues()) {
						if (logger.isDebugEnabled())
							logger.debug("containerValue: " + containerValue.toJson());
						containerValue.setLsState(newLsState);
						containerValue.persist();
						if (logger.isDebugEnabled())
							logger.debug("persisted the containerValue: " + containerValue.toJson());
					}
				} else {
					if (logger.isDebugEnabled())
						logger.debug("No container values to save");
				}
			}
		}
		return newContainer;
	}

	@Override
	public Collection<Container> saveLsContainers(String json) {
		Collection<Container> savedContainers = new ArrayList<Container>();
		int batchSize = propertiesUtilService.getBatchSize();
		int i = 0;
		StringReader sr = new StringReader(json);
		BufferedReader br = new BufferedReader(sr);

		for (Container container : Container.fromJsonArrayToContainers(br)) {
			Container newContainer = saveContainer(container);
			savedContainers.add(newContainer);
			if (i % batchSize == 0) {
				newContainer.flush();
				newContainer.clear();
			}
			i++;

		}

		return savedContainers;
	}

	@Override
	public Container saveLsContainer(Container container) {
		if (logger.isDebugEnabled()) {
			logger.debug("incoming meta container: " + container.toJson() + "\n");
		}
		Container newContainer = new Container(container);
		if (newContainer.getCodeName() == null) {
			if (newContainer.getLsTypeAndKind() == null)
				newContainer.setLsTypeAndKind(newContainer.getLsType() + "_" + newContainer.getLsKind());
			newContainer.setCodeName(autoLabelService.getContainerCodeName());
		}
		newContainer.persist();
		if (container.getLsLabels() != null) {
			Set<ContainerLabel> lsLabels = new HashSet<ContainerLabel>();
			for (ContainerLabel containerLabel : container.getLsLabels()) {
				ContainerLabel newContainerLabel = new ContainerLabel(containerLabel);
				newContainerLabel.setContainer(newContainer);
				if (logger.isDebugEnabled()) {
					logger.debug("here is the newcontainerLabel before save: " + newContainerLabel.toJson());
				}
				newContainerLabel.persist();
				lsLabels.add(newContainerLabel);
			}
			newContainer.setLsLabels(lsLabels);
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("No container labels to save");
			}
		}

		if (container.getLsStates() != null) {
			Set<ContainerState> lsStates = new HashSet<ContainerState>();
			for (ContainerState lsState : container.getLsStates()) {
				ContainerState newLsState = new ContainerState(lsState);
				newLsState.setContainer(newContainer);
				if (logger.isDebugEnabled()) {
					logger.debug("here is the newLsState before save: " + newLsState.toJson());
				}
				newLsState.persist();
				if (logger.isDebugEnabled()) {
					logger.debug("persisted the newLsState: " + newLsState.toJson());
				}
				if (lsState.getLsValues() != null) {
					Set<ContainerValue> lsValues = new HashSet<ContainerValue>();
					for (ContainerValue containerValue : lsState.getLsValues()) {
						if (logger.isDebugEnabled()) {
							logger.debug("containerValue: " + containerValue.toJson());
						}
						ContainerValue newContainerValue = ContainerValue.create(containerValue);
						newContainerValue.setLsState(newLsState);
						newContainerValue.persist();
						lsValues.add(newContainerValue);
						if (logger.isDebugEnabled()) {
							logger.debug("persisted the containerValue: " + newContainerValue.toJson());
						}
					}
					newLsState.setLsValues(lsValues);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("No container values to save");
					}
				}
				lsStates.add(newLsState);
			}
			newContainer.setLsStates(lsStates);
		}

		if (container.getFirstContainers() != null) {
			Set<ItxContainerContainer> firstContainers = new HashSet<ItxContainerContainer>();
			for (ItxContainerContainer itxContainerContainer : container.getFirstContainers()) {
				if (itxContainerContainer.getId() == null) {
					if (logger.isDebugEnabled()) {
						logger.debug("saving new itxContainerContainer: " + itxContainerContainer.toJson());
					}
					if (itxContainerContainer.getFirstContainer().getId() == null) {
						if (logger.isDebugEnabled()) {
							logger.debug("saving new nested Container"
									+ itxContainerContainer.getFirstContainer().toJson());
						}
						Container nestedContainer = saveLsContainer(itxContainerContainer.getFirstContainer());
						itxContainerContainer.setFirstContainer(nestedContainer);
					}
					itxContainerContainer.setSecondContainer(newContainer);
					ItxContainerContainer newItxContainerContainer = saveItxContainerContainer(itxContainerContainer);
					firstContainers.add(newItxContainerContainer);
				} else {
					firstContainers.add(itxContainerContainer);
				}
			}
			newContainer.setFirstContainers(firstContainers);
		}

		if (container.getSecondContainers() != null) {
			Set<ItxContainerContainer> secondContainers = new HashSet<ItxContainerContainer>();
			for (ItxContainerContainer itxContainerContainer : container.getSecondContainers()) {
				if (itxContainerContainer.getId() == null) {
					if (logger.isDebugEnabled()) {
						logger.debug("saving new itxContainerContainer: " + itxContainerContainer.toJson());
					}
					if (itxContainerContainer.getSecondContainer().getId() == null) {
						if (logger.isDebugEnabled()) {
							logger.debug("saving new nested Container: "
									+ itxContainerContainer.getSecondContainer().toJson());
						}
						Container nestedContainer = saveContainer(itxContainerContainer.getSecondContainer());
						itxContainerContainer.setSecondContainer(nestedContainer);
					}
					itxContainerContainer.setFirstContainer(newContainer);
					ItxContainerContainer newItxContainerContainer = saveItxContainerContainer(itxContainerContainer);
					secondContainers.add(newItxContainerContainer);
				} else {
					secondContainers.add(itxContainerContainer);
				}
			}
			newContainer.setSecondContainers(secondContainers);
		}

		return Container.findContainer(newContainer.getId());
	}

	@Override
	@Transactional
	public Container updateContainer(Container container) {
		if (logger.isDebugEnabled()) {
			logger.debug("incoming meta container: " + container.toJson() + "\n");
		}
		Container updatedContainer = Container.update(container);
		if (container.getLsLabels() != null) {
			for (ContainerLabel containerLabel : container.getLsLabels()) {
				if (containerLabel.getId() == null) {
					ContainerLabel newContainerLabel = new ContainerLabel(containerLabel);
					newContainerLabel.setContainer(updatedContainer);
					newContainerLabel.persist();
					updatedContainer.getLsLabels().add(newContainerLabel);
				} else {
					ContainerLabel updatedLabel = ContainerLabel.update(containerLabel);
					updatedContainer.getLsLabels().add(updatedLabel);
				}
			}
		} else {
			logger.info("No container labels to update");
		}
		updateLsStates(container, updatedContainer);

		Set<ItxContainerContainer> firstContainers = new HashSet<ItxContainerContainer>();
		// firstContainers.addAll(updatedContainer.getFirstContainers());
		if (logger.isDebugEnabled()) {
			logger.debug("found number of first interactions: " + firstContainers.size());
		}
		if (container.getFirstContainers() != null) {
			// there are itx's
			for (ItxContainerContainer itxContainerContainer : container.getFirstContainers()) {
				ItxContainerContainer updatedItxContainerContainer;
				if (itxContainerContainer.getId() == null) {
					// need to save a new itx
					if (logger.isDebugEnabled()) {
						logger.debug("saving new itxContainerContainer: " + itxContainerContainer.toJson());
					}
					updateNestedFirstContainer(itxContainerContainer);
					itxContainerContainer.setSecondContainer(updatedContainer);
					updatedItxContainerContainer = saveItxContainerContainer(itxContainerContainer);
					firstContainers.add(updatedItxContainerContainer);
				} else {
					// old itx needs to be updated
					updateNestedFirstContainer(itxContainerContainer);
					itxContainerContainer.setSecondContainer(updatedContainer);
					updatedItxContainerContainer = ItxContainerContainer.update(itxContainerContainer);
					updateItxLsStates(itxContainerContainer, updatedItxContainerContainer);
					firstContainers.add(updatedItxContainerContainer);
				}
			}
			updatedContainer.setFirstContainers(firstContainers);
		}

		Set<ItxContainerContainer> secondContainers = new HashSet<ItxContainerContainer>();
		// secondContainers.addAll(updatedContainer.getSecondContainers());
		// secondContainers.addAll(ItxContainerContainer.findItxContainerContainersBySecondContainer(updatedContainer).getResultList());
		if (logger.isDebugEnabled()) {
			logger.debug("found number of second interactions: " + secondContainers.size());
		}

		if (container.getSecondContainers() != null) {
			// there are itx's
			for (ItxContainerContainer itxContainerContainer : container.getSecondContainers()) {
				if (logger.isDebugEnabled()) {
					logger.debug("updating itxContainerContainer");
				}
				ItxContainerContainer updatedItxContainerContainer;
				if (itxContainerContainer.getId() == null) {
					// need to save a new itx
					if (logger.isDebugEnabled()) {
						logger.debug("saving new itxContainerContainer: " + itxContainerContainer.toJson());
					}
					updateNestedSecondContainer(itxContainerContainer);
					itxContainerContainer.setFirstContainer(updatedContainer);
					updatedItxContainerContainer = saveItxContainerContainer(itxContainerContainer);
					secondContainers.add(updatedItxContainerContainer);
				} else {
					// old itx needs to be updated
					updateNestedSecondContainer(itxContainerContainer);
					itxContainerContainer.setFirstContainer(updatedContainer);
					updatedItxContainerContainer = ItxContainerContainer.update(itxContainerContainer);
					updateItxLsStates(itxContainerContainer, updatedItxContainerContainer);
					secondContainers.add(updatedItxContainerContainer);
				}
			}
			updatedContainer.setSecondContainers(secondContainers);
		}

		return Container.findContainer(updatedContainer.getId());
	}

	@Override
	public Container saveLsContainer(String json) {
		return saveLsContainer(Container.fromJsonToContainer(json));
	}

	@Override
	public Collection<Container> saveLsContainers(Collection<Container> containers) {
		Collection<Container> savedContainers = new HashSet<Container>();
		for (Container container : containers) {
			Container savedContainer = saveLsContainer(container);
			savedContainers.add(savedContainer);
		}
		return savedContainers;
	}

	@Transactional
	@Override
	public List<ContainerCodeNameStateDTO> saveContainerCodeNameStateDTOArray(
			List<ContainerCodeNameStateDTO> stateDTOs) throws SQLException {
		List<ContainerCodeNameStateDTO> savedDTOs = new ArrayList<ContainerCodeNameStateDTO>();
		// collect all the states to be saved into a List
		List<ContainerState> statesToSave = new ArrayList<ContainerState>();
		for (ContainerCodeNameStateDTO stateDTO : stateDTOs) {
			statesToSave.add(stateDTO.getLsState());
		}
		// save all the states at once
		List<Long> savedStateIds = insertContainerStates(statesToSave);
		// fill in the saved state ids
		int i = 0;
		for (ContainerState state : statesToSave) {
			state.setId(savedStateIds.get(i));
			state.setVersion(0);
			i++;
		}
		// collect all the values to be saved into a List
		List<ContainerValue> valuesToSave = new ArrayList<ContainerValue>();
		for (ContainerState savedState : statesToSave) {
			for (ContainerValue valueToSave : savedState.getLsValues()) {
				valueToSave.setLsState(savedState);
				valuesToSave.add(valueToSave);
			}
		}
		// save all the values at once
		List<Long> savedValueIds = insertContainerValues(valuesToSave);
		// fill in the saved value ids
		int j = 0;
		for (ContainerValue value : valuesToSave) {
			value.setId(savedValueIds.get(j));
			value.setVersion(0);
			j++;
		}
		// re-associate the values with the correct states
		Map<Long, Set<ContainerValue>> stateIdToValuesMap = new HashMap<Long, Set<ContainerValue>>();
		for (ContainerValue savedValue : valuesToSave) {
			Long stateId = savedValue.getLsState().getId();
			if (stateIdToValuesMap.containsKey(stateId)) {
				Set<ContainerValue> values = stateIdToValuesMap.get(stateId);
				values.add(savedValue);
				stateIdToValuesMap.put(stateId, values);
			} else {
				Set<ContainerValue> values = new HashSet<ContainerValue>();
				values.add(savedValue);
				stateIdToValuesMap.put(stateId, values);
			}
		}
		for (ContainerState savedState : statesToSave) {
			Set<ContainerValue> savedValues = stateIdToValuesMap.get(savedState.getId());
			savedState.setLsValues(savedValues);
			ContainerCodeNameStateDTO savedDTO = new ContainerCodeNameStateDTO(savedState.getContainer().getCodeName(),
					savedState);
			savedDTOs.add(savedDTO);
		}

		return savedDTOs;
	}

	@Override
	public Collection<ContainerLocationDTO> getContainersInLocation(Collection<String> locationCodeNames) {
		return getContainersInLocation(locationCodeNames, null, null);
	}

	@Override
	public Collection<ContainerLocationDTO> getContainersInLocation(Collection<String> locationCodeNames,
			String containerType, String containerKind) {
		EntityManager em = Container.entityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ContainerLocationDTO> cq = cb.createQuery(ContainerLocationDTO.class);
		Root<Container> location = cq.from(Container.class);
		Join<Container, ItxContainerContainer> firstItx = location.join("firstContainers");
		Join<Container, ItxContainerContainer> container = firstItx.join("firstContainer");
		Join<Container, ContainerLabel> barcode = container.join("lsLabels", JoinType.LEFT);

		Predicate[] predicates = new Predicate[0];
		List<Predicate> predicateList = new ArrayList<Predicate>();
		Expression<String> locationCodeName = location.<String>get("codeName");
		Predicate locationCodeNameEquals = locationCodeName.in(locationCodeNames);
		predicateList.add(locationCodeNameEquals);
		Predicate itxType = cb.equal(firstItx.<String>get("lsType"), "moved to");
		predicateList.add(itxType);
		Predicate barcodePreferred = cb.isTrue(barcode.<Boolean>get("preferred"));
		predicateList.add(barcodePreferred);

		// optional container type/kind
		if (containerType != null && containerType.length() > 0) {
			Predicate containerTypeEquals = cb.equal(container.<String>get("lsType"), containerType);
			predicateList.add(containerTypeEquals);
		}
		if (containerKind != null && containerKind.length() > 0) {
			Predicate containerKindEquals = cb.equal(container.<String>get("lsKind"), containerKind);
			predicateList.add(containerKindEquals);
		}
		// not ignored predicates
		Predicate locationNotIgnored = cb.not(location.<Boolean>get("ignored"));
		Predicate firstItxNotIgnored = cb.not(firstItx.<Boolean>get("ignored"));
		Predicate containerNotIgnored = cb.not(container.<Boolean>get("ignored"));
		Predicate barcodeNotIgnored = cb.not(barcode.<Boolean>get("ignored"));
		predicateList.add(locationNotIgnored);
		predicateList.add(firstItxNotIgnored);
		predicateList.add(containerNotIgnored);
		predicateList.add(barcodeNotIgnored);

		predicates = predicateList.toArray(predicates);
		cq.where(cb.and(predicates));
		cq.multiselect(location.<String>get("codeName"), container.<String>get("codeName"),
				barcode.<String>get("labelText"));
		TypedQuery<ContainerLocationDTO> q = em.createQuery(cq);
		// if (logger.isDebugEnabled())
		// logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
		Collection<ContainerLocationDTO> results = q.getResultList();
		return results;
	}

	@Override
	public ArrayList<ErrorMessage> validateContainer(Container container) {
		ArrayList<ErrorMessage> errors = new ArrayList<ErrorMessage>();
		try {
			checkContainerUniqueName(container);
		} catch (UniqueNameException e) {
			logger.error("Caught UniqueNameException validating Container: " + e.getMessage().toString()
					+ " whole message  " + e.toString());
			ErrorMessage error = new ErrorMessage();
			error.setErrorLevel("error");
			error.setMessage(e.getMessage());
			errors.add(error);
		}

		return errors;
	}

	private void checkContainerUniqueName(Container container) throws UniqueNameException {
		Set<ContainerLabel> containerLabels = container.getLsLabels();
		for (ContainerLabel containerLabel : containerLabels) {
			if (!containerLabel.isIgnored() && containerLabel.getLsType().equals("name")) {
				String labelText = containerLabel.getLabelText();
				Collection<Container> foundContainers = new HashSet<Container>();
				Collection<ContainerLabel> foundContainerLabels = new HashSet<ContainerLabel>();
				try {
					foundContainerLabels = ContainerLabel
							.findContainerLabelsByLsTypeEqualsAndLabelTextEqualsAndIgnoredNot(
									containerLabel.getLsType(), labelText, true)
							.getResultList();
				} catch (NoResultException e) {
					// found nothing
				}
				if (!foundContainerLabels.isEmpty()) {
					for (ContainerLabel foundLabel : foundContainerLabels) {
						foundContainers.add(foundLabel.getContainer());
					}
				}
				if (!foundContainers.isEmpty()) {
					for (Container foundContainer : foundContainers) {
						if (container.getId() == null || container.getId().compareTo(foundContainer.getId()) != 0) {
							// we found an container that is not the same as the one being validated that
							// has the same label
							throw new UniqueNameException("Container with the name " + labelText + " already exists! "
									+ foundContainer.getCodeName());
						}
					}
				}
			}
		}

	}

	private ItxContainerContainer saveItxContainerContainer(ItxContainerContainer itxContainerContainer) {
		ItxContainerContainer newItxContainerContainer = new ItxContainerContainer(itxContainerContainer);
		newItxContainerContainer.persist();
		if (itxContainerContainer.getLsStates() != null) {
			Set<ItxContainerContainerState> lsStates = new HashSet<ItxContainerContainerState>();
			for (ItxContainerContainerState itxContainerContainerState : itxContainerContainer.getLsStates()) {
				ItxContainerContainerState newItxContainerContainerState = new ItxContainerContainerState(
						itxContainerContainerState);
				newItxContainerContainerState.setItxContainerContainer(newItxContainerContainer);
				if (logger.isDebugEnabled()) {
					logger.debug("here is the newItxContainerContainerState before save: "
							+ newItxContainerContainerState.toJson());
				}
				newItxContainerContainerState.persist();
				if (logger.isDebugEnabled()) {
					logger.debug(
							"persisted the newItxContainerContainerState: " + newItxContainerContainerState.toJson());
				}
				if (itxContainerContainerState.getLsValues() != null) {
					Set<ItxContainerContainerValue> lsValues = new HashSet<ItxContainerContainerValue>();
					for (ItxContainerContainerValue itxContainerContainerValue : itxContainerContainerState
							.getLsValues()) {
						if (logger.isDebugEnabled()) {
							logger.debug("itxContainerContainerValue: " + itxContainerContainerValue.toJson());
						}
						ItxContainerContainerValue newItxContainerContainerValue = new ItxContainerContainerValue(
								itxContainerContainerValue);
						newItxContainerContainerValue.setLsState(newItxContainerContainerState);
						newItxContainerContainerValue.persist();
						lsValues.add(newItxContainerContainerValue);
						if (logger.isDebugEnabled()) {
							logger.debug("persisted the itxContainerContainerValue: "
									+ newItxContainerContainerValue.toJson());
						}
					}
					newItxContainerContainerState.setLsValues(lsValues);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("No itxContainerContainer values to save");
					}
				}
				lsStates.add(newItxContainerContainerState);
			}
			newItxContainerContainer.setLsStates(lsStates);
		}
		return newItxContainerContainer;
	}

	public void updateLsStates(Container jsonContainer, Container updatedContainer) {
		if (jsonContainer.getLsStates() != null) {
			for (ContainerState lsThingState : jsonContainer.getLsStates()) {
				ContainerState updatedContainerState;
				if (lsThingState.getId() == null) {
					updatedContainerState = new ContainerState(lsThingState);
					updatedContainerState.setContainer(updatedContainer);
					updatedContainerState.persist();
					updatedContainer.getLsStates().add(updatedContainerState);
					if (logger.isDebugEnabled()) {
						logger.debug("persisted new lsThing state " + updatedContainerState.getId());
					}

				} else {
					updatedContainerState = ContainerState.update(lsThingState);
					updatedContainer.getLsStates().add(updatedContainerState);

					if (logger.isDebugEnabled()) {
						logger.debug("updated lsThing state " + updatedContainerState.getId());
					}

				}
				if (lsThingState.getLsValues() != null) {
					for (ContainerValue lsThingValue : lsThingState.getLsValues()) {
						if (lsThingValue.getLsState() == null)
							lsThingValue.setLsState(updatedContainerState);
						ContainerValue updatedContainerValue;
						if (lsThingValue.getId() == null) {
							updatedContainerValue = ContainerValue.create(lsThingValue);
							updatedContainerValue
									.setLsState(ContainerState.findContainerState(updatedContainerState.getId()));
							updatedContainerValue.persist();
							updatedContainerState.getLsValues().add(updatedContainerValue);
						} else {
							updatedContainerValue = ContainerValue.update(lsThingValue);
							if (logger.isDebugEnabled()) {
								logger.debug("updated lsThing value " + updatedContainerValue.getId());
							}
						}
						if (logger.isDebugEnabled()) {
							logger.debug("checking lsThingValue " + updatedContainerValue.toJson());
						}

					}
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("No lsThing values to update");
					}
				}
			}
		}
	}

	private void updateNestedFirstContainer(ItxContainerContainer itxContainerContainer) {
		Container updatedNestedContainer;
		if (itxContainerContainer.getFirstContainer().getId() == null) {
			// need to save a new nested lsthing
			if (logger.isDebugEnabled()) {
				logger.debug("saving new nested Container" + itxContainerContainer.getFirstContainer().toJson());
			}
			updatedNestedContainer = saveContainer(itxContainerContainer.getFirstContainer());
			itxContainerContainer.setFirstContainer(updatedNestedContainer);
		} else {
			// just need to update the old nested lsThing inside the new itx
			updatedNestedContainer = Container.update(itxContainerContainer.getFirstContainer());
			updateLsStates(itxContainerContainer.getFirstContainer(), updatedNestedContainer);
			itxContainerContainer.setFirstContainer(updatedNestedContainer);
		}
	}

	private void updateNestedSecondContainer(ItxContainerContainer itxContainerContainer) {
		Container updatedNestedContainer;
		if (itxContainerContainer.getSecondContainer().getId() == null) {
			// need to save a new nested lsthing
			if (logger.isDebugEnabled()) {
				logger.debug("saving new nested Container" + itxContainerContainer.getSecondContainer().toJson());
			}
			updatedNestedContainer = saveContainer(itxContainerContainer.getSecondContainer());
			itxContainerContainer.setSecondContainer(updatedNestedContainer);
		} else {
			// just need to update the old nested lsThing inside the new itx
			updatedNestedContainer = Container.update(itxContainerContainer.getSecondContainer());
			updateLsStates(itxContainerContainer.getSecondContainer(), updatedNestedContainer);
			itxContainerContainer.setSecondContainer(updatedNestedContainer);
		}
	}

	private void updateItxLsStates(ItxContainerContainer jsonItxContainerContainer,
			ItxContainerContainer updatedItxContainerContainer) {
		if (jsonItxContainerContainer.getLsStates() != null) {
			for (ItxContainerContainerState itxContainerContainerState : jsonItxContainerContainer.getLsStates()) {
				ItxContainerContainerState updatedItxContainerContainerState;
				if (itxContainerContainerState.getId() == null) {
					updatedItxContainerContainerState = new ItxContainerContainerState(itxContainerContainerState);
					updatedItxContainerContainerState.setItxContainerContainer(updatedItxContainerContainer);
					updatedItxContainerContainerState.persist();
					updatedItxContainerContainer.getLsStates().add(updatedItxContainerContainerState);
				} else {
					updatedItxContainerContainerState = ItxContainerContainerState.update(itxContainerContainerState);
					updatedItxContainerContainerState.setItxContainerContainer(updatedItxContainerContainer);
					updatedItxContainerContainerState.merge();
					updatedItxContainerContainer.getLsStates().add(updatedItxContainerContainerState);
					if (logger.isDebugEnabled()) {
						logger.debug(
								"updated itxContainerContainer state " + updatedItxContainerContainerState.getId());
					}

				}
				if (itxContainerContainerState.getLsValues() != null) {
					for (ItxContainerContainerValue itxContainerContainerValue : itxContainerContainerState
							.getLsValues()) {
						ItxContainerContainerValue updatedItxContainerContainerValue;
						if (itxContainerContainerValue.getId() == null) {
							updatedItxContainerContainerValue = ItxContainerContainerValue
									.create(itxContainerContainerValue);
							updatedItxContainerContainerValue.setLsState(ItxContainerContainerState
									.findItxContainerContainerState(updatedItxContainerContainerState.getId()));
							updatedItxContainerContainerValue.persist();
							updatedItxContainerContainerState.getLsValues().add(updatedItxContainerContainerValue);
						} else {
							updatedItxContainerContainerValue = ItxContainerContainerValue
									.update(itxContainerContainerValue);
							updatedItxContainerContainerValue.setLsState(updatedItxContainerContainerState);
							updatedItxContainerContainerValue.merge();
							updatedItxContainerContainerState.getLsValues().add(updatedItxContainerContainerValue);
							if (logger.isDebugEnabled()) {
								logger.debug("updated itxContainerContainer value "
										+ updatedItxContainerContainerValue.getId());
							}
						}
					}
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("No itxContainerContainer values to update");
					}
				}
			}
		}
	}

	@Override
	public Collection<PlateWellDTO> getWellCodesByPlateBarcodes(List<String> plateBarcodes) {
		EntityManager em = Container.entityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<PlateWellDTO> cq = cb.createQuery(PlateWellDTO.class);
		Root<Container> plate = cq.from(Container.class);
		Join<Container, ItxContainerContainer> secondItx = plate.join("secondContainers");
		Join<Container, ItxContainerContainer> well = secondItx.join("secondContainer");
		Join<Container, ContainerLabel> plateBarcode = plate.join("lsLabels");
		Join<Container, ContainerLabel> wellLabel = well.join("lsLabels");

		Predicate[] predicates = new Predicate[0];
		List<Predicate> predicateList = new ArrayList<Predicate>();
		Predicate plateBarcodeLsType = cb.equal(plateBarcode.<String>get("lsType"), "barcode");
		Predicate plateBarcodeLsKind = cb.equal(plateBarcode.<String>get("lsKind"), "barcode");
		predicateList.add(plateBarcodeLsType);
		predicateList.add(plateBarcodeLsKind);
		Expression<String> plateBarcodeLabelText = plateBarcode.<String>get("labelText");
		Predicate plateBarcodeEquals = plateBarcodeLabelText.in(plateBarcodes);
		predicateList.add(plateBarcodeEquals);
		Predicate itxType = cb.equal(secondItx.<String>get("lsType"), "has member");
		Predicate itxKind = cb.equal(secondItx.<String>get("lsKind"), "container_well");
		predicateList.add(itxType);
		predicateList.add(itxKind);
		Predicate wellLabelLsType = cb.equal(wellLabel.<String>get("lsType"), "name");
		Predicate wellLabelLsKind = cb.equal(wellLabel.<String>get("lsKind"), "well name");
		predicateList.add(wellLabelLsType);
		predicateList.add(wellLabelLsKind);

		// not ignored predicates
		Predicate plateNotIgnored = cb.not(plate.<Boolean>get("ignored"));
		Predicate secondItxNotIgnored = cb.not(secondItx.<Boolean>get("ignored"));
		Predicate wellNotIgnored = cb.not(well.<Boolean>get("ignored"));
		Predicate plateBarcodeNotIgnored = cb.not(plateBarcode.<Boolean>get("ignored"));
		Predicate wellBarcodeNotIgnored = cb.not(plateBarcode.<Boolean>get("ignored"));
		predicateList.add(plateNotIgnored);
		predicateList.add(secondItxNotIgnored);
		predicateList.add(wellNotIgnored);
		predicateList.add(plateBarcodeNotIgnored);
		predicateList.add(wellBarcodeNotIgnored);

		predicates = predicateList.toArray(predicates);
		cq.where(cb.and(predicates));
		cq.multiselect(plateBarcode.<String>get("labelText"), plate.<String>get("codeName"),
				well.<String>get("codeName"), wellLabel.<String>get("labelText"));
		TypedQuery<PlateWellDTO> q = em.createQuery(cq);
		// if (logger.isDebugEnabled())
		// logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
		Collection<PlateWellDTO> results = q.getResultList();
		return results;
	}

	@Override
	public Collection<CodeLabelDTO> getContainerCodesByLabels(List<String> labelTexts, String containerType,
			String containerKind, String labelType, String labelKind, Boolean like, Boolean rightLike) {
		EntityManager em = Container.entityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
		Root<Container> container = cq.from(Container.class);
		Join<Container, ContainerLabel> label = container.join("lsLabels");

		Predicate[] predicates = new Predicate[0];
		List<Predicate> predicateList = new ArrayList<Predicate>();
		Expression<String> containerLabelText = label.<String>get("labelText");
		if (like != null && like) {
			List<Predicate> containerLabelTextsLike = new ArrayList<Predicate>();
			for (String labelText : labelTexts) {
				Predicate containerLabelTextLike;
				if (rightLike != null && rightLike) {
					containerLabelTextLike = cb.like(containerLabelText, labelText + "%");
				} else {
					containerLabelTextLike = cb.like(containerLabelText, "%" + labelText + "%");
				}
				containerLabelTextsLike.add(containerLabelTextLike);
			}
			Predicate[] labelPredicates = new Predicate[0];
			Predicate containerLabelTextLikePredicate = cb.or(containerLabelTextsLike.toArray(labelPredicates));
			predicateList.add(containerLabelTextLikePredicate);
		} else {
			Predicate containerLabelTextEquals = SimpleUtil.buildInPredicate(cb, containerLabelText, labelTexts);
			predicateList.add(containerLabelTextEquals);
		}

		// optional container type/kind and label type/kind
		if (containerType != null && containerType.length() > 0) {
			Predicate containerTypeEquals = cb.equal(container.<String>get("lsType"), containerType);
			predicateList.add(containerTypeEquals);
		}
		if (containerKind != null && containerKind.length() > 0) {
			Predicate containerKindEquals = cb.equal(container.<String>get("lsKind"), containerKind);
			predicateList.add(containerKindEquals);
		}
		if (labelType != null && labelType.length() > 0) {
			Predicate containerLabelTypeEquals = cb.equal(label.<String>get("lsType"), labelType);
			predicateList.add(containerLabelTypeEquals);
		}
		if (labelKind != null && labelKind.length() > 0) {
			Predicate containerLabelKindEquals = cb.equal(label.<String>get("lsKind"), labelKind);
			predicateList.add(containerLabelKindEquals);
		}
		// not ignored predicates
		Predicate containerNotIgnored = cb.not(container.<Boolean>get("ignored"));
		Predicate labelNotIgnored = cb.not(label.<Boolean>get("ignored"));
		predicateList.add(containerNotIgnored);
		predicateList.add(labelNotIgnored);

		predicates = predicateList.toArray(predicates);
		cq.where(cb.and(predicates));
		cq.multiselect(container.<String>get("codeName").alias("foundCodeName"),
				label.<String>get("labelText").alias("requestLabel"));
		Query q = em.createQuery(cq);
		// if (logger.isDebugEnabled())
		// logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
		List<Tuple> resultTuples = q.getResultList();
		Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
		for (Tuple tuple : resultTuples) {
			String requestLabel = (String) tuple.get("requestLabel");
			String foundCodeName = (String) tuple.get("foundCodeName");
			if (!resultMap.containsKey(requestLabel)) {
				List<String> foundCodeNames = new ArrayList<String>();
				foundCodeNames.add(foundCodeName);
				resultMap.put(requestLabel, foundCodeNames);
			} else {
				resultMap.get(requestLabel).add(foundCodeName);
			}
		}
		Collection<CodeLabelDTO> results = new ArrayList<CodeLabelDTO>();
		if (like != null && like) {
			for (String foundLabel : resultMap.keySet()) {
				CodeLabelDTO result = new CodeLabelDTO();
				result.setRequestLabel(foundLabel);
				result.setFoundCodeNames(resultMap.get(foundLabel));
				results.add(result);
			}
		} else {
			for (String requestLabel : labelTexts) {
				CodeLabelDTO result = new CodeLabelDTO();
				result.setRequestLabel(requestLabel);
				if (resultMap.containsKey(requestLabel)) {
					result.setFoundCodeNames(resultMap.get(requestLabel));
				} else {
					result.setFoundCodeNames(new ArrayList<String>());
				}
				results.add(result);
			}
		}

		return results;
	}

	@Override
	public Collection<WellContentDTO> getWellContent(List<String> wellCodes) {
		if (wellCodes.isEmpty())
			return new ArrayList<WellContentDTO>();
		EntityManager em = Container.entityManager();
		String queryString = "SELECT new com.labsynch.labseer.dto.WellContentDTO( ";
		queryString += "well.codeName, ";
		queryString += "wellName.labelText, ";
		queryString += "well.rowIndex, well.columnIndex, ";
		queryString += "well.recordedBy, well.recordedDate, ";
		queryString += "statusContentState.recordedDate, ";
		queryString += "amountValue.numericValue, amountValue.unitKind,  ";
		queryString += " batchCodeValue.codeValue, batchCodeValue.concentration, batchCodeValue.concUnit,  ";
		queryString += " solventCodeValue.codeValue,  ";
		queryString += " physicalStateValue.codeValue  ";
		queryString += " )  ";
		queryString += " from Container as well ";
		queryString += SimpleUtil.makeInnerJoinHql("well.lsStates", "statusContentState", "status", "content");
		queryString += SimpleUtil.makeLeftJoinHql("well.lsLabels", "wellName", "name", "well name");
		queryString += SimpleUtil.makeLeftJoinHql("statusContentState.lsValues", "amountValue", "numericValue",
				"amount");
		queryString += SimpleUtil.makeLeftJoinHql("statusContentState.lsValues", "batchCodeValue", "codeValue",
				"batch code");
		queryString += SimpleUtil.makeLeftJoinHql("statusContentState.lsValues", "solventCodeValue", "codeValue",
				"solvent code");
		queryString += SimpleUtil.makeLeftJoinHql("statusContentState.lsValues", "physicalStateValue", "codeValue",
				"physical state");
		queryString += "where ( well.ignored <> true ) and ";
		Collection<Query> queries = SimpleUtil.splitHqlInClause(em, queryString, "well.codeName", wellCodes);
		Collection<WellContentDTO> results = new HashSet<WellContentDTO>();
		logger.debug("Querying for " + wellCodes.size() + " well codeNames");
		for (Query q : queries) {
			if (logger.isDebugEnabled()) {
				logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
			}
			results.addAll(q.getResultList());
		}
		// diff request with results to find codeNames that could not be found
		Map<String, WellContentDTO> resultMap = new HashMap<String, WellContentDTO>();
		HashSet<String> requestWellCodeNames = new HashSet<String>();
		requestWellCodeNames.addAll(wellCodes);
		HashSet<String> foundWellCodeNames = new HashSet<String>();
		for (WellContentDTO result : results) {
			foundWellCodeNames.add(result.getContainerCodeName());
			resultMap.put(result.getContainerCodeName(), result);
		}
		requestWellCodeNames.removeAll(foundWellCodeNames);
		logger.debug(requestWellCodeNames.size() + " not found with content");
		if (!requestWellCodeNames.isEmpty()) {
			// smaller query to look for empty wells that still do exist
			List<String> notFoundWellCodes = new ArrayList<String>();
			notFoundWellCodes.addAll(requestWellCodeNames);
			String emptyWellsQuery = "SELECT new com.labsynch.labseer.dto.WellContentDTO( ";
			emptyWellsQuery += "well.codeName, ";
			emptyWellsQuery += "wellName.labelText, ";
			emptyWellsQuery += "well.rowIndex, well.columnIndex, ";
			emptyWellsQuery += "well.recordedBy, well.recordedDate ";
			emptyWellsQuery += " )  ";
			emptyWellsQuery += " from Container as well ";
			emptyWellsQuery += SimpleUtil.makeLeftJoinHql("well.lsLabels", "wellName", "name", "well name");
			emptyWellsQuery += "where ( well.ignored <> true ) and ";
			Collection<Query> emptyWellQueries = SimpleUtil.splitHqlInClause(em, emptyWellsQuery, "well.codeName",
					notFoundWellCodes);
			Collection<WellContentDTO> emptyWellResults = new HashSet<WellContentDTO>();
			for (Query q : emptyWellQueries) {
				emptyWellResults.addAll(q.getResultList());
			}
			for (WellContentDTO result : emptyWellResults) {
				foundWellCodeNames.add(result.getContainerCodeName());
				resultMap.put(result.getContainerCodeName(), result);
			}
			requestWellCodeNames.removeAll(foundWellCodeNames);
			logger.debug(emptyWellResults.size() + " found with no content, " + requestWellCodeNames.size()
					+ " not found at all");
			for (String notFoundCodeName : requestWellCodeNames) {
				WellContentDTO notFoundDTO = new WellContentDTO();
				notFoundDTO.setContainerCodeName(notFoundCodeName);
				notFoundDTO.setLevel("error");
				notFoundDTO.setMessage("containerCodeName not found");
				results.add(notFoundDTO);
				resultMap.put(notFoundDTO.getContainerCodeName(), notFoundDTO);
			}
		}

		// sort results to match input wellCode order
		Collection<WellContentDTO> sortedResults = new ArrayList<WellContentDTO>();
		for (String wellCode : wellCodes) {
			sortedResults.add(resultMap.get(wellCode));
		}
		return sortedResults;
	}

	@Override
	public ContainerDependencyCheckDTO checkDependencies(Container container) {
		ContainerDependencyCheckDTO result = new ContainerDependencyCheckDTO();
		result.setQueryContainer(container);
		Collection<ItxContainerContainer> movedToItxs = ItxContainerContainer
				.findItxContainerContainersByLsTypeEqualsAndSecondContainerEquals("moved to", container)
				.getResultList();
		if (movedToItxs != null && !movedToItxs.isEmpty()) {
			for (ItxContainerContainer movedToItx : movedToItxs) {
				Container contents = movedToItx.getFirstContainer();
				if (!contents.isIgnored()) {
					result.getDependentCorpNames().add(contents.getCodeName());
				}
			}
		}
		Collection<ItxContainerContainer> hasMemberItxs = ItxContainerContainer
				.findItxContainerContainersByLsTypeEqualsAndFirstContainerEquals("has member", container)
				.getResultList();
		Collection<Container> members = new HashSet<Container>();
		if (hasMemberItxs != null && !hasMemberItxs.isEmpty()) {
			for (ItxContainerContainer hasMemberItx : hasMemberItxs) {
				Container contents = hasMemberItx.getSecondContainer();
				if (!contents.isIgnored()) {
					members.add(contents);
				}
			}
		}
		if (!result.getDependentCorpNames().isEmpty())
			result.setLinkedDataExists(true);
		logger.debug("finished checking for linked containers. Now checking for dependent experimental data");
		result.checkForDependentData(result.getQueryContainer(), members);
		return result;
	}

	@Override
	public PreferredNameResultsDTO getCodeNameFromName(String containerType, String containerKind, String labelType,
			String labelKind, PreferredNameRequestDTO requestDTO) {
		logger.info("number of requests: " + requestDTO.getRequests().size());
		Collection<PreferredNameDTO> requests = requestDTO.getRequests();

		PreferredNameResultsDTO responseOutput = new PreferredNameResultsDTO();
		Collection<ErrorMessageDTO> errors = new HashSet<ErrorMessageDTO>();

		for (PreferredNameDTO request : requests) {
			request.setPreferredName("");
			request.setReferenceName("");
			List<Container> lsThings = new ArrayList<Container>();
			if (labelType == null || labelKind == null || labelType.length() == 0 || labelKind.length() == 0) {
				lsThings = Container.findContainerByLabelText(containerType, containerKind, request.getRequestName())
						.getResultList();

			} else {
				lsThings = Container.findContainerByLabelText(containerType, containerKind, labelType, labelKind,
						request.getRequestName()).getResultList();
			}
			if (lsThings.size() == 1) {
				request.setPreferredName(pickBestLabel(lsThings.get(0)));
				request.setReferenceName(lsThings.get(0).getCodeName());
			} else if (lsThings.size() > 1) {
				responseOutput.setError(true);
				ErrorMessageDTO error = new ErrorMessageDTO();
				error.setLevel("MULTIPLE RESULTS");
				error.setMessage("FOUND MULTIPLE LSTHINGS WITH THE SAME NAME: " + request.getRequestName());
				logger.error("FOUND MULTIPLE LSTHINGS WITH THE SAME NAME: " + request.getRequestName());
				errors.add(error);
			} else {
				try {
					Container codeNameMatch = Container.findContainerByCodeNameEquals(request.getRequestName());
					if (codeNameMatch.getLsKind().equals(containerKind)
							&& codeNameMatch.getLsType().equals(containerType)) {
						logger.info("Made it to the codeMatch");
						request.setPreferredName(pickBestLabel(codeNameMatch));
						request.setReferenceName(codeNameMatch.getCodeName());
					} else {
						logger.info("Did not find a Container with the requested name: " + request.getRequestName());
					}
				} catch (NoResultException e) {
					logger.info("Did not find a Container with the requested name: " + request.getRequestName());
				}
			}
		}
		responseOutput.setResults(requests);
		responseOutput.setErrorMessages(errors);

		return responseOutput;
	}

	@Override
	public String pickBestLabel(Container container) {
		Collection<ContainerLabel> labels = container.getLsLabels();
		if (labels.isEmpty())
			return null;
		return ContainerLabel.pickBestLabel(labels).getLabelText();
	}

	@Override
	public Collection<ContainerErrorMessageDTO> throwInTrash(Collection<ContainerRequestDTO> containersToTrash)
			throws Exception {
		Collection<ContainerErrorMessageDTO> results = new HashSet<ContainerErrorMessageDTO>();
		for (ContainerRequestDTO dto : containersToTrash) {
			ContainerErrorMessageDTO result = new ContainerErrorMessageDTO();
			result.setContainerCodeName(dto.getContainerCodeName());
			results.add(result);
			Container container;
			try {
				container = Container.findContainerByCodeNameEquals(dto.getContainerCodeName());
			} catch (Exception e) {
				result.setLevel("error");
				result.setMessage("containerCodeName not found");
				continue;
			}
			// ignore the old movedTo interaction to preserve history
			try {
				ItxContainerContainer movedTo = ItxContainerContainer
						.findItxContainerContainersByLsTypeEqualsAndLsKindEqualsAndFirstContainerEquals("moved to",
								"container_location", container)
						.getSingleResult();
				movedTo.setIgnored(true);
				movedTo.setModifiedBy(dto.getModifiedBy());
				movedTo.setModifiedDate(dto.getModifiedDate());
				movedTo.merge();
			} catch (Exception e) {
				result.setLevel("error");
				result.setMessage("Error finding 'moved to'/'container_location' interaction to ignore.");
				continue;
			}

			// create trash interaction to show container has been moved to the trash
			try {
				ItxContainerContainer trashItx = new ItxContainerContainer();
				trashItx.setLsType("moved to");
				trashItx.setLsKind("container_location");
				trashItx.setRecordedBy(dto.getModifiedBy());
				trashItx.setRecordedDate(dto.getModifiedDate());
				trashItx.setFirstContainer(container);
				trashItx.setSecondContainer(getOrCreateTrash(dto.getModifiedBy()));
				trashItx.persist();
			} catch (Exception e) {
				result.setLevel("error");
				result.setMessage("Error creating new interaction to trash");
				continue;
			}
			// ignore container since it is now in the trash -- decided NOT to ignore the
			// container as of 04-05-2016. Still a valid container, just in the trash
			// try{
			// container.setIgnored(true);
			// container.setModifiedBy(dto.getModifiedBy());
			// container.setModifiedDate(dto.getModifiedDate());
			// container.merge();
			// }catch (Exception e){
			// result.setLevel("error");
			// result.setMessage("Error ignoring container");
			// continue;
			// }
		}
		return results;
	}

	@Override
	public Container getOrCreateTrash(String recordedBy) throws Exception {
		String configuredTrashLabel = propertiesUtilService.getTrashLocationLabel();
		if (configuredTrashLabel == null || configuredTrashLabel.length() < 1)
			configuredTrashLabel = "trash";
		Container trash = getOrCreateLocation(configuredTrashLabel, "autocreated by throwInTrash service", recordedBy,
				null);
		String configuredRootLabel = propertiesUtilService.getRootLocationLabel();
		if (configuredRootLabel != null && configuredRootLabel.length() > 0) {
			if (trash.getSecondContainers().isEmpty()) {
				try {
					Container root = getOrCreateLocation(configuredRootLabel, "autocreated by throwInTrash service",
							recordedBy, null);
					ContainerLocationDTO moveDTO = new ContainerLocationDTO();
					moveDTO.setLocationCodeName(root.getCodeName());
					moveDTO.setContainerCodeName(trash.getCodeName());
					moveDTO.setModifiedBy(recordedBy);
					moveDTO.setModifiedDate(new Date());
					Collection<ContainerLocationDTO> requests = new ArrayList<ContainerLocationDTO>();
					requests.add(moveDTO);
					moveToLocation(requests);
				} catch (Exception e) {
					logger.error("Caught error putting autocreated trash into root location", e);
				}
			}
		}
		return trash;
	}

	@Override
	public Collection<ContainerErrorMessageDTO> updateAmountInWell(Collection<ContainerRequestDTO> wellsToUpdate) {
		Collection<ContainerErrorMessageDTO> results = new HashSet<ContainerErrorMessageDTO>();
		for (ContainerRequestDTO dto : wellsToUpdate) {
			ContainerErrorMessageDTO result = new ContainerErrorMessageDTO();
			result.setContainerCodeName(dto.getContainerCodeName());
			results.add(result);
			Container container;
			try {
				container = Container.findContainerByCodeNameEquals(dto.getContainerCodeName());
			} catch (Exception e) {
				result.setLevel("error");
				result.setMessage("containerCodeName not found");
				continue;
			}
			// find amount value and update
			ContainerValue amountValue;
			try {
				for (ContainerState state : container.getLsStates()) {
					if (state.getLsType().equals("status") && state.getLsKind().equals("content")) {
						for (ContainerValue value : state.getLsValues()) {
							if (value.getLsKind().equals("amount")) {
								amountValue = value;
								amountValue.setNumericValue(dto.getAmount());
								amountValue.setUnitKind(dto.getAmountUnits());
								amountValue.setModifiedBy(dto.getModifiedBy());
								amountValue.setModifiedDate(dto.getModifiedDate());
								amountValue.merge();
							}
						}
					}
				}
			} catch (Exception e) {
				result.setLevel("error");
				result.setMessage("Amount value not found");
				continue;
			}
		}
		return results;
	}

	@Override
	public Collection<PlateStubDTO> createPlates(Collection<CreatePlateRequestDTO> plateRequests) throws Exception {
		Collection<PlateStubDTO> results = new ArrayList<PlateStubDTO>();
		for (CreatePlateRequestDTO plateRequest : plateRequests) {
			PlateStubDTO result = createPlate(plateRequest);
			results.add(result);
		}
		return results;
	}

	@Override
	public Collection<PlateStubDTO> createTubes(Collection<CreatePlateRequestDTO> plateRequests) throws Exception {
		Collection<PlateStubDTO> results = new ArrayList<PlateStubDTO>();
		for (CreatePlateRequestDTO plateRequest : plateRequests) {
			PlateStubDTO result = createTube(plateRequest);
			results.add(result);
		}
		return results;
	}

	@Override
	public PlateStubDTO createPlate(CreatePlateRequestDTO plateRequest) throws Exception {
		return createPlate(plateRequest, "plate");
	}

	@Override
	public PlateStubDTO createTube(CreatePlateRequestDTO plateRequest) throws Exception {
		return createPlate(plateRequest, "tube");
	}

	@Override
	@Transactional
	public PlateStubDTO createPlate(CreatePlateRequestDTO plateRequest, String containerKind) throws Exception {
		Container definition;
		try {
			definition = Container.findContainerByCodeNameEquals(plateRequest.getDefinition());
		} catch (Exception e) {
			throw new Exception("Error finding definition: " + plateRequest.getDefinition());
		}
		LsTransaction lsTransaction = new LsTransaction();
		lsTransaction.setRecordedDate(new Date());
		lsTransaction.persist();
		Container plate = new Container();
		plate.setCodeName(autoLabelService.getContainerCodeName());
		plate.setRecordedBy(plateRequest.getRecordedBy());
		plate.setRecordedDate(new Date());
		plate.setLsType("container");
		plate.setLsKind(containerKind);
		plate.setLsTransaction(lsTransaction.getId());
		List<Container> plateList = new ArrayList<Container>();
		plateList.add(plate);
		plate.setId(insertContainers(plateList).get(0));

		ContainerLabel plateBarcode = new ContainerLabel();
		plateBarcode.setRecordedBy(plate.getRecordedBy());
		plateBarcode.setRecordedDate(plate.getRecordedDate());
		plateBarcode.setLsType("barcode");
		plateBarcode.setLsKind("barcode");
		plateBarcode.setLabelText(plateRequest.getBarcode());
		plateBarcode.setPreferred(true);
		plateBarcode.setLsTransaction(plate.getLsTransaction());
		plateBarcode.setContainer(plate);
		plate.getLsLabels().add(plateBarcode);
		List<ContainerLabel> plateBarcodeList = new ArrayList<ContainerLabel>();
		plateBarcodeList.add(plateBarcode);
		plateBarcode.setId(insertContainerLabels(plateBarcodeList).get(0));

		ContainerState metadataState = new ContainerState();
		metadataState.setRecordedBy(plate.getRecordedBy());
		metadataState.setRecordedDate(plate.getRecordedDate());
		metadataState.setLsType("metadata");
		metadataState.setLsKind("information");
		metadataState.setLsTransaction(plate.getLsTransaction());
		metadataState.setContainer(plate);
		List<ContainerState> plateStateList = new ArrayList<ContainerState>();
		plateStateList.add(metadataState);
		metadataState.setId(insertContainerStates(plateStateList).get(0));

		Set<ContainerValue> values = new HashSet<ContainerValue>();
		List<ContainerValue> plateValueList = new ArrayList<ContainerValue>();

		ContainerValue createdUser = new ContainerValue();
		createdUser.setRecordedBy(plate.getRecordedBy());
		createdUser.setRecordedDate(plate.getRecordedDate());
		createdUser.setLsType("codeValue");
		createdUser.setLsKind("created user");
		if (plateRequest.getCreatedUser() != null)
			createdUser.setCodeValue(plateRequest.getCreatedUser());
		else
			createdUser.setCodeValue(plate.getRecordedBy());
		createdUser.setLsTransaction(plate.getLsTransaction());
		createdUser.setLsState(metadataState);
		values.add(createdUser);
		plateValueList.add(createdUser);

		ContainerValue createdDate = new ContainerValue();
		createdDate.setRecordedBy(plate.getRecordedBy());
		createdDate.setRecordedDate(plate.getRecordedDate());
		createdDate.setLsType("dateValue");
		createdDate.setLsKind("created date");
		if (plateRequest.getCreatedDate() != null)
			createdDate.setDateValue(plateRequest.getCreatedDate());
		else
			createdDate.setDateValue(new Date());
		createdDate.setLsTransaction(plate.getLsTransaction());
		createdDate.setLsState(metadataState);
		values.add(createdDate);
		plateValueList.add(createdDate);

		ContainerValue description = new ContainerValue();
		description.setRecordedBy(plate.getRecordedBy());
		description.setRecordedDate(plate.getRecordedDate());
		description.setLsType("stringValue");
		description.setLsKind("description");
		description.setStringValue(plateRequest.getDescription());
		description.setLsTransaction(plate.getLsTransaction());
		description.setLsState(metadataState);

		if (plateRequest.getDescription() != null) {
			values.add(description);
			plateValueList.add(description);
		}
		metadataState.setLsValues(values);

		List<Long> valueIds = insertContainerValues(plateValueList);
		createdUser.setId(valueIds.get(0));
		createdDate.setId(valueIds.get(1));
		if (plateRequest.getDescription() != null)
			description.setId(valueIds.get(2));

		plate.getLsStates().add(metadataState);
		plate.getLsLabels().add(plateBarcode);
		ItxContainerContainer defines = makeItxContainerContainer("defines", definition, plate,
				plateRequest.getRecordedBy(), lsTransaction.getId());
		List<ItxContainerContainer> definesList = new ArrayList<ItxContainerContainer>();
		definesList.add(defines);
		insertItxContainerContainers(definesList);
		// getOrCreate location
		Container location = getOrCreateBench(plateRequest.getRecordedBy(), lsTransaction);
		// move plate to location
		ContainerLocationDTO moveRequest = new ContainerLocationDTO(location.getCodeName(), plate.getCodeName(),
				plateRequest.getBarcode());
		moveRequest.setModifiedBy(plateRequest.getRecordedBy());
		moveRequest.setModifiedDate(new Date());
		Collection<ContainerLocationDTO> moveRequests = new ArrayList<ContainerLocationDTO>();
		moveRequests.add(moveRequest);
		moveToLocation(moveRequests);
		// create and populate wells
		try {
			Map<String, List<?>> wellsAndNames = createWellsFromDefinition(plate, definition);
			List<Container> wells = (List<Container>) wellsAndNames.get("wells");
			List<ContainerLabel> wellNames = (List<ContainerLabel>) wellsAndNames.get("wellNames");
			// fill in recorded by for all wells to update
			Collection<WellContentDTO> wellDTOs = new ArrayList<WellContentDTO>();
			if (plateRequest.getWells() != null && !plateRequest.getWells().isEmpty()) {
				for (WellContentDTO wellDTO : plateRequest.getWells()) {
					if (wellDTO.getRecordedBy() == null)
						wellDTO.setRecordedBy(plate.getRecordedBy());
				}
				wellDTOs = lookUpWellCodesByWellNames(wells, wellNames, plateRequest.getWells());
			}
			if (plateRequest.getPhysicalState() != null || plateRequest.getBatchConcentrationUnits() != null) {
				// fill in empty wellContentDTOs for new wells not passed in via
				// plateRequest.getWells()
				Set<String> suppliedWellCodes = new HashSet<String>();
				if (!wellDTOs.isEmpty()) {
					for (WellContentDTO suppliedWellDTO : wellDTOs) {
						suppliedWellCodes.add(suppliedWellDTO.getContainerCodeName());
					}
				}
				Set<String> allWellCodes = new HashSet<String>();
				for (Container well : wells) {
					allWellCodes.add(well.getCodeName());
				}
				allWellCodes.removeAll(suppliedWellCodes);
				for (String wellCode : allWellCodes) {
					// populate wellContentDTO for unsupplied codes to pass in default physical
					// state and/or batch concentration units
					WellContentDTO notSuppliedWell = new WellContentDTO();
					notSuppliedWell.setContainerCodeName(wellCode);
					notSuppliedWell.setPhysicalState(plateRequest.getPhysicalState());
					notSuppliedWell.setBatchConcUnits(plateRequest.getBatchConcentrationUnits());
					notSuppliedWell.setRecordedBy(plate.getRecordedBy());
					wellDTOs.add(notSuppliedWell);
				}
			}
			logger.debug("Size of wellDTOs is: " + wellDTOs.size());
			if (!wellDTOs.isEmpty()) {
				try {
					logger.debug("Updating well content" + wellDTOs.size());
					updateWellContent(wellDTOs, true);
				} catch (Exception e) {
					logger.error("Caught exception updating well content", e);
				}
			}
			// TODO: do something with templates
			PlateStubDTO result = new PlateStubDTO();
			result.setBarcode(plateRequest.getBarcode());
			result.setCodeName(plate.getCodeName());
			Collection<WellStubDTO> wellStubs = WellStubDTO.convertToWellStubDTOs(wells, wellNames);
			result.setWells(wellStubs);
			return result;
		} catch (Exception e) {
			logger.error("Error creating wells from definition", e);
			throw new Exception("Error creating wells from definition", e);
		}
	}

	private Container getOrCreateLocation(String labelText, String description, String recordedBy,
			LsTransaction lsTransaction) throws SQLException {
		PreferredNameDTO request = new PreferredNameDTO(labelText, null, null);
		Collection<PreferredNameDTO> requests = new ArrayList<PreferredNameDTO>();
		requests.add(request);
		PreferredNameRequestDTO requestDTO = new PreferredNameRequestDTO();
		requestDTO.setRequests(requests);
		PreferredNameResultsDTO resultsDTO = getCodeNameFromName("location", "default", "name", "common", requestDTO);
		String locationCodeName = resultsDTO.getResults().iterator().next().getReferenceName();
		if (locationCodeName != null && locationCodeName.length() > 0) {
			Container location = Container.findContainerByCodeNameEquals(locationCodeName);
			return location;
		} else {
			logger.warn("location not found: " + labelText + ". Creating new location.");
			Container newLocation = new Container();
			newLocation.setCodeName(autoLabelService.getContainerCodeName());
			newLocation.setRecordedBy(recordedBy);
			newLocation.setRecordedDate(new Date());
			newLocation.setLsType("location");
			newLocation.setLsKind("default");
			if (lsTransaction == null) {
				lsTransaction = new LsTransaction();
				lsTransaction.setRecordedBy(recordedBy);
				lsTransaction.setRecordedDate(new Date());
				lsTransaction.persist();
			}
			newLocation.setLsTransaction(lsTransaction.getId());
			List<Container> newLocationList = new ArrayList<Container>();
			newLocationList.add(newLocation);
			newLocation.setId(insertContainers(newLocationList).get(0));

			ContainerLabel newLocationName = new ContainerLabel();
			newLocationName.setRecordedBy(newLocation.getRecordedBy());
			newLocationName.setRecordedDate(newLocation.getRecordedDate());
			newLocationName.setLsType("name");
			newLocationName.setLsKind("common");
			newLocationName.setLabelText(labelText);
			newLocationName.setPreferred(true);
			newLocationName.setLsTransaction(newLocation.getLsTransaction());
			newLocationName.setContainer(newLocation);
			newLocation.getLsLabels().add(newLocationName);
			List<ContainerLabel> newLocationNameList = new ArrayList<ContainerLabel>();
			newLocationNameList.add(newLocationName);
			newLocationName.setId(insertContainerLabels(newLocationNameList).get(0));

			ContainerState metadataState = new ContainerState();
			metadataState.setRecordedBy(newLocation.getRecordedBy());
			metadataState.setRecordedDate(newLocation.getRecordedDate());
			metadataState.setLsType("metadata");
			metadataState.setLsKind("information");
			metadataState.setLsTransaction(newLocation.getLsTransaction());
			metadataState.setContainer(newLocation);
			List<ContainerState> newLocationStateList = new ArrayList<ContainerState>();
			newLocationStateList.add(metadataState);
			metadataState.setId(insertContainerStates(newLocationStateList).get(0));

			Set<ContainerValue> values = new HashSet<ContainerValue>();
			List<ContainerValue> newLocationValueList = new ArrayList<ContainerValue>();

			ContainerValue descriptionVal = new ContainerValue();
			descriptionVal.setRecordedBy(newLocation.getRecordedBy());
			descriptionVal.setRecordedDate(newLocation.getRecordedDate());
			descriptionVal.setLsType("stringValue");
			descriptionVal.setLsKind("description");
			descriptionVal.setStringValue(description);
			descriptionVal.setLsTransaction(newLocation.getLsTransaction());
			descriptionVal.setLsState(metadataState);
			values.add(descriptionVal);
			newLocationValueList.add(descriptionVal);

			ContainerValue createdUser = new ContainerValue();
			createdUser.setRecordedBy(newLocation.getRecordedBy());
			createdUser.setRecordedDate(newLocation.getRecordedDate());
			createdUser.setLsType("codeValue");
			createdUser.setLsKind("created user");
			createdUser.setCodeValue(recordedBy);
			createdUser.setLsTransaction(newLocation.getLsTransaction());
			createdUser.setLsState(metadataState);
			values.add(createdUser);
			newLocationValueList.add(createdUser);

			ContainerValue createdDate = new ContainerValue();
			createdDate.setRecordedBy(newLocation.getRecordedBy());
			createdDate.setRecordedDate(newLocation.getRecordedDate());
			createdDate.setLsType("dateValue");
			createdDate.setLsKind("created date");
			createdDate.setDateValue(new Date());
			createdDate.setLsTransaction(newLocation.getLsTransaction());
			createdDate.setLsState(metadataState);
			values.add(createdDate);
			newLocationValueList.add(createdDate);

			metadataState.setLsValues(values);
			List<Long> valueIds = insertContainerValues(newLocationValueList);
			descriptionVal.setId(valueIds.get(0));
			createdUser.setId(valueIds.get(1));
			createdDate.setId(valueIds.get(2));

			newLocation.getLsStates().add(metadataState);
			newLocation.getLsLabels().add(newLocationName);

			return newLocation;
		}
	}

	@Override
	public Container getOrCreateBench(String recordedBy, LsTransaction lsTransaction) throws SQLException {
		Container bench = getOrCreateLocation(recordedBy, recordedBy + "'s bench", recordedBy, lsTransaction);
		String configuredBenchesLabel = propertiesUtilService.getBenchesLocationLabel();
		if (configuredBenchesLabel == null || configuredBenchesLabel.length() < 1)
			configuredBenchesLabel = "Benches";
		if (bench.getSecondContainers().isEmpty()) {
			try {
				// getOrCreate the benches location
				Container benches = getOrCreateLocation(configuredBenchesLabel, "autocreated by createPlate service",
						recordedBy, null);
				ContainerLocationDTO benchMoveDTO = new ContainerLocationDTO();
				benchMoveDTO.setLocationCodeName(benches.getCodeName());
				benchMoveDTO.setContainerCodeName(bench.getCodeName());
				benchMoveDTO.setModifiedBy(recordedBy);
				benchMoveDTO.setModifiedDate(new Date());
				Collection<ContainerLocationDTO> requests = new ArrayList<ContainerLocationDTO>();
				requests.add(benchMoveDTO);
				String configuredRootLabel = propertiesUtilService.getRootLocationLabel();
				if (benches.getSecondContainers().isEmpty() && configuredRootLabel != null
						&& configuredRootLabel.length() > 0) {
					// put the benches location into the root location
					Container root = getOrCreateLocation(configuredRootLabel, "autocreated by createPlate service",
							recordedBy, null);
					ContainerLocationDTO benchesMoveDTO = new ContainerLocationDTO();
					benchesMoveDTO.setLocationCodeName(root.getCodeName());
					benchesMoveDTO.setContainerCodeName(benches.getCodeName());
					benchesMoveDTO.setModifiedBy(recordedBy);
					benchesMoveDTO.setModifiedDate(new Date());
					requests.add(benchesMoveDTO);
				}
				moveToLocation(requests);
			} catch (Exception e) {
				logger.error("Caught error putting autocreated trash into root location", e);
			}
		}

		return bench;
	}

	private Collection<WellContentDTO> lookUpWellCodesByWellNames(List<Container> wells, List<ContainerLabel> wellNames,
			Collection<WellContentDTO> wellDTOs) {
		List<String> wellNameList = new ArrayList<String>();
		for (ContainerLabel wellName : wellNames) {
			wellNameList.add(wellName.getLabelText());
		}
		for (WellContentDTO wellDTO : wellDTOs) {
			String wellName = wellDTO.getWellName();
			List<String> possibleWellNames = new ArrayList<String>();
			if (SimpleUtil.isNumeric(wellName)) {
				possibleWellNames.add(wellName);
			} else {
				// well name must be A1, A01, or A001 format
				String letterPart = wellName.replaceAll("[0-9]+", "");
				Integer numberPart = Integer.valueOf(wellName.replaceAll("[^0-9]+", ""));
				try {
					possibleWellNames.add(letterPart + Integer.toString(numberPart));
				} catch (Exception e) {
				}
				try {
					possibleWellNames.add(letterPart + String.format("%02d", numberPart));
				} catch (Exception e) {
				}
				try {
					possibleWellNames.add(letterPart + String.format("%03d", numberPart));
				} catch (Exception e) {
				}
			}
			for (String possibleWellName : possibleWellNames) {
				if (wellNameList.indexOf(possibleWellName) != -1) {
					wellDTO.setContainerCodeName(wells.get(wellNameList.indexOf(possibleWellName)).getCodeName());
					break;
				}
			}
			// for (Container well : wells){
			// for (ContainerLabel wellLabel : well.getLsLabels()){
			// if (possibleWellNames.contains(wellLabel.getLabelText())){
			// wellDTO.setContainerCodeName(well.getCodeName());
			// break;
			// }
			// }
			// }
		}
		return wellDTOs;
	}

	public Map<String, List<?>> createWellsFromDefinition(Container plate, Container definition) throws Exception {
		List<Container> wells = new ArrayList<Container>();
		List<ContainerLabel> wellNames = new ArrayList<ContainerLabel>();
		List<ItxContainerContainer> itxContainerContainers = new ArrayList<ItxContainerContainer>();
		String wellFormat = getWellFormat(definition);
		Integer numberOfWells = getDefinitionIntegerValue(definition, "wells");
		Integer numberOfColumns = getDefinitionIntegerValue(definition, "columns");
		if (wellFormat != null) {
			int n = 0;
			List<AutoLabelDTO> wellCodeNames = autoLabelService.getAutoLabels("material_container", "id_codeName",
					numberOfWells.longValue());
			logger.debug("wellFormat: " + wellFormat);
			logger.debug("numberOfWells: " + numberOfWells.toString());
			logger.debug("numberOfColumns: " + numberOfColumns.toString());
			while (n < numberOfWells) {
				String letter = "";
				String number = "";
				int rowNum = n / numberOfColumns;
				int colNum = n % numberOfColumns;
				if (wellFormat.equals("1"))
					number = Integer.toString(n + 1);
				else if (wellFormat.equals("A1")) {
					letter = SimpleUtil.toAlphabetic(rowNum);
					number = Integer.toString(colNum + 1);
				} else if (wellFormat.equals("A01")) {
					letter = SimpleUtil.toAlphabetic(rowNum);
					number = String.format("%02d", colNum + 1);
				} else if (wellFormat.equals("A001")) {
					letter = SimpleUtil.toAlphabetic(rowNum);
					number = String.format("%03d", colNum + 1);
				}
				// create well Container object
				Container well = new Container();
				well.setCodeName(wellCodeNames.get(n).getAutoLabel());
				well.setRecordedBy(plate.getRecordedBy());
				well.setRecordedDate(new Date());
				well.setLsType("well");
				well.setLsKind("default");
				well.setRowIndex(rowNum + 1);
				well.setColumnIndex(colNum + 1);
				well.setLsTransaction(plate.getLsTransaction());
				wells.add(well);
				ContainerLabel wellName = new ContainerLabel();
				wellName.setRecordedBy(plate.getRecordedBy());
				wellName.setRecordedDate(new Date());
				wellName.setLsType("name");
				wellName.setLsKind("well name");
				wellName.setLabelText(letter + number);
				wellName.setLsTransaction(plate.getLsTransaction());
				wellNames.add(wellName);

				// create ItxContainerContainer "has member" to link plate and well
				ItxContainerContainer hasMemberItx = makeItxContainerContainer("has member", plate, well,
						plate.getRecordedBy(), plate.getLsTransaction());
				itxContainerContainers.add(hasMemberItx);
				n++;
			}
			List<Long> wellIds = insertContainers(wells);
			int i = 0;
			for (Long wellId : wellIds) {
				Container fakeWell = new Container();
				fakeWell.setId(wellId);
				wellNames.get(i).setContainer(fakeWell);
				itxContainerContainers.get(i).setSecondContainer(fakeWell);
				i++;
			}
			insertContainerLabels(wellNames);
			insertItxContainerContainers(itxContainerContainers);
		} else {
			throw new Exception("Well format could not be found for definition: " + definition.getCodeName());
		}
		Map<String, List<?>> result = new HashMap<String, List<?>>();
		result.put("wells", wells);
		result.put("wellNames", wellNames);
		return result;
	}

	private String getWellFormat(Container definition) {
		for (ContainerState state : definition.getLsStates()) {
			if (state.getLsType().equals("constants") && state.getLsKind().equals("format")) {
				for (ContainerValue value : state.getLsValues()) {
					if (value.getLsKind().equals("subcontainer naming convention")) {
						return value.getCodeValue();
					}
				}
			}
		}
		return null;
	}

	private Integer getDefinitionIntegerValue(Container definition, String lsKind) {
		for (ContainerState state : definition.getLsStates()) {
			if (state.getLsType().equals("constants") && state.getLsKind().equals("format")) {
				logger.debug(state.getLsTypeAndKind());
				for (ContainerValue value : state.getLsValues()) {
					logger.debug(value.getLsTypeAndKind());
					if (value.getLsKind().equals(lsKind)) {
						return Integer.valueOf(value.getNumericValue().intValue());
					}
				}
			}
		}
		return null;
	}

	private BigDecimal getDefinitionNumericValue(Container definition, String lsKind) {
		for (ContainerState state : definition.getLsStates()) {
			if (state.getLsType().equals("constants") && state.getLsKind().equals("format")) {
				logger.debug(state.getLsTypeAndKind());
				for (ContainerValue value : state.getLsValues()) {
					logger.debug(value.getLsTypeAndKind());
					if (value.getLsKind().equals(lsKind)) {
						return value.getNumericValue();
					}
				}
			}
		}
		return null;
	}

	private ItxContainerContainer makeItxContainerContainer(String lsType, Container firstContainer,
			Container secondContainer, String recordedBy, Long transactionId) {
		ItxContainerContainer itxContainerContainer = new ItxContainerContainer();
		itxContainerContainer.setLsType(lsType);
		itxContainerContainer.setLsKind(firstContainer.getLsType() + "_" + secondContainer.getLsType());
		itxContainerContainer.setLsTransaction(transactionId);
		itxContainerContainer.setFirstContainer(firstContainer);
		itxContainerContainer.setSecondContainer(secondContainer);
		itxContainerContainer.setRecordedBy(recordedBy);
		itxContainerContainer.setRecordedDate(new Date());
		return itxContainerContainer;
	}

	@Override
	public Collection<WellContentDTO> getWellContentByPlateBarcode(String plateBarcode) {
		List<String> plateBarcodes = new ArrayList<String>();
		plateBarcodes.add(plateBarcode);
		Collection<PlateWellDTO> wellCodes = getWellCodesByPlateBarcodes(plateBarcodes);
		List<String> requestWellCodes = new ArrayList<String>();
		for (PlateWellDTO wellCode : wellCodes) {
			requestWellCodes.add(wellCode.getWellCodeName());
		}
		Collection<WellContentDTO> results = getWellContent(requestWellCodes);
		return results;
	}

	@Override
	@Transactional
	public Collection<ContainerErrorMessageDTO> updateWellContent(Collection<WellContentDTO> wellsToUpdate,
			boolean copyPreviousValues) throws Exception {
		Long start = (new Date()).getTime();
		Collection<ContainerErrorMessageDTO> results = new ArrayList<ContainerErrorMessageDTO>();
		LsTransaction lsTransaction = new LsTransaction();
		lsTransaction.setRecordedDate(new Date());
		lsTransaction.persist();
		// get current well status in bulk, make into map
		List<String> wellCodes = new ArrayList<String>();
		for (WellContentDTO well : wellsToUpdate) {
			wellCodes.add(well.getContainerCodeName());
		}
		Collection<WellContentDTO> currentWellContents = getWellContent(wellCodes);
		Map<String, WellContentDTO> currentWellContentsMap = new HashMap<String, WellContentDTO>();
		for (WellContentDTO currentWellContent : currentWellContents) {
			currentWellContentsMap.put(currentWellContent.getContainerCodeName(), currentWellContent);
		}
		Long afterBuildMap = (new Date()).getTime();
		logger.debug("time to get content and build map: " + String.valueOf(afterBuildMap - start));
		logger.debug("time per well to get content and build map: "
				+ String.valueOf((afterBuildMap - start) / currentWellContents.size()));
		Map<String, Long[]> wellCodeToWellIdStateId = getWellAndStatusContentStateIdsByCodeNames(wellCodes);
		Long afterGetIds = (new Date()).getTime();
		logger.debug("time to get ids and build map: " + String.valueOf(afterGetIds - afterBuildMap));
		logger.debug("time per well to get ids and build map: "
				+ String.valueOf((afterGetIds - afterBuildMap) / wellCodeToWellIdStateId.size()));
		List<ContainerState> oldContentStates = new ArrayList<ContainerState>();
		List<ContainerState> newContentStates = new ArrayList<ContainerState>();
		List<List<ContainerValue>> newContentValues = new ArrayList<List<ContainerValue>>();
		Long beforeWells = (new Date()).getTime();
		for (WellContentDTO wellToUpdate : wellsToUpdate) {
			ContainerErrorMessageDTO result = new ContainerErrorMessageDTO();
			result.setContainerCodeName(wellToUpdate.getContainerCodeName());
			if (wellCodeToWellIdStateId.get(wellToUpdate.getContainerCodeName()) == null) {
				result.setLevel("error");
				result.setMessage("containerCodeName not found");
				results.add(result);
				continue;
			} else if (wellCodeToWellIdStateId.get(wellToUpdate.getContainerCodeName()).length == 2) {
				ContainerState oldStatusContentState = new ContainerState();
				oldStatusContentState.setId(wellCodeToWellIdStateId.get(wellToUpdate.getContainerCodeName())[1]);
				oldStatusContentState.setIgnored(true);
				oldStatusContentState.setModifiedBy(wellToUpdate.getRecordedBy());
				oldStatusContentState.setModifiedDate(new Date());
				oldContentStates.add(oldStatusContentState);
			}
			Container fakeWell = new Container();
			fakeWell.setCodeName(wellToUpdate.getContainerCodeName());
			fakeWell.setId(wellCodeToWellIdStateId.get(wellToUpdate.getContainerCodeName())[0]);

			WellContentDTO oldContent = currentWellContentsMap.get(wellToUpdate.getContainerCodeName());
			BigDecimal oldAmount = oldContent.getAmount();
			String oldAmountUnits = oldContent.getAmountUnits();
			String oldBatchCode = oldContent.getBatchCode();
			Double oldBatchConcentration = oldContent.getBatchConcentration();
			String oldBatchConcUnits = oldContent.getBatchConcUnits();
			String oldPhysicalState = oldContent.getPhysicalState();
			String oldSolventCode = oldContent.getSolventCode();

			// Long extractedOldValues = (new Date()).getTime();
			// logger.debug("time to extract old values: "+String.valueOf(extractedOldValues
			// - afterGetByCodeName));
			// create a new state and new values
			ContainerState newStatusContentState = new ContainerState();
			newStatusContentState.setRecordedBy(wellToUpdate.getRecordedBy());
			newStatusContentState.setRecordedDate(new Date());
			newStatusContentState.setLsType("status");
			newStatusContentState.setLsKind("content");
			newStatusContentState.setLsTransaction(lsTransaction.getId());
			newStatusContentState.setContainer(fakeWell);
			newContentStates.add(newStatusContentState);
			// fill in new values from DTO. if they are null, use the old value
			BigDecimal newAmount = wellToUpdate.getAmount();
			String newAmountUnits = wellToUpdate.getAmountUnits();
			String newBatchCode = wellToUpdate.getBatchCode();
			Double newBatchConcentration = wellToUpdate.getBatchConcentration();
			String newBatchConcUnits = wellToUpdate.getBatchConcUnits();
			String newPhysicalState = wellToUpdate.getPhysicalState();
			String newSolventCode = wellToUpdate.getSolventCode();
			if (copyPreviousValues) {
				if (newAmount == null)
					newAmount = oldAmount;
				if (newAmountUnits == null)
					newAmountUnits = oldAmountUnits;
				if (newBatchCode == null)
					newBatchCode = oldBatchCode;
				if (newBatchConcentration == null)
					newBatchConcentration = oldBatchConcentration;
				if (newBatchConcUnits == null)
					newBatchConcUnits = oldBatchConcUnits;
				if (newPhysicalState == null)
					newPhysicalState = oldPhysicalState;
				if (newSolventCode == null)
					newSolventCode = oldSolventCode;
			}
			// create new values for attributes that exist
			List<ContainerValue> newValues = new ArrayList<ContainerValue>();
			if (newAmount != null || newAmountUnits != null) {
				ContainerValue amount = new ContainerValue();
				amount.setRecordedBy(wellToUpdate.getRecordedBy());
				amount.setRecordedDate(new Date());
				amount.setLsType("numericValue");
				amount.setLsKind("amount");
				amount.setNumericValue(newAmount);
				amount.setUnitKind(newAmountUnits);
				amount.setLsTransaction(lsTransaction.getId());
				newValues.add(amount);
			}
			if (newBatchCode != null || newBatchConcentration != null || newBatchConcUnits != null) {
				ContainerValue batchCode = new ContainerValue();
				batchCode.setRecordedBy(wellToUpdate.getRecordedBy());
				batchCode.setRecordedDate(new Date());
				batchCode.setLsType("codeValue");
				batchCode.setLsKind("batch code");
				batchCode.setCodeValue(newBatchCode);
				batchCode.setConcentration(newBatchConcentration);
				batchCode.setConcUnit(newBatchConcUnits);
				batchCode.setLsTransaction(lsTransaction.getId());
				newValues.add(batchCode);
			}
			if (newPhysicalState != null) {
				ContainerValue physicalState = new ContainerValue();
				physicalState.setRecordedBy(wellToUpdate.getRecordedBy());
				physicalState.setRecordedDate(new Date());
				physicalState.setLsType("codeValue");
				physicalState.setLsKind("physical state");
				physicalState.setCodeValue(newPhysicalState);
				physicalState.setLsTransaction(lsTransaction.getId());
				newValues.add(physicalState);
			}
			if (newSolventCode != null) {
				ContainerValue solventCode = new ContainerValue();
				solventCode.setRecordedBy(wellToUpdate.getRecordedBy());
				solventCode.setRecordedDate(new Date());
				solventCode.setLsType("codeValue");
				solventCode.setLsKind("solvent code");
				solventCode.setCodeValue(newSolventCode);
				solventCode.setLsTransaction(lsTransaction.getId());
				newValues.add(solventCode);
			}
			newContentValues.add(newValues);
			results.add(result);
		}
		Long finishedWells = (new Date()).getTime();
		logger.debug("total time for all wells: " + String.valueOf(finishedWells - beforeWells));
		logger.debug("time per well to prepare for inserts: "
				+ String.valueOf((finishedWells - beforeWells) / wellsToUpdate.size()));
		// save everything in batches, being careful with ids
		try {
			Long beforeDbActions = (new Date()).getTime();
			logger.debug("states to ignore:" + oldContentStates.size());
			ignoreContainerStates(oldContentStates);
			Long afterIgnore = (new Date()).getTime();
			logger.debug("new states to create:" + newContentStates.size());
			List<Long> newStateIds = insertContainerStates(newContentStates);
			logger.debug("new state ids created:" + newStateIds.size());
			Long afterInsertState = (new Date()).getTime();
			int i = 0;
			logger.debug("groups of values to create:" + newContentValues.size());
			List<ContainerValue> flattenedNewValues = new ArrayList<ContainerValue>();
			for (Long newStateId : newStateIds) {
				List<ContainerValue> values = newContentValues.get(i);
				ContainerState fakeState = new ContainerState();
				fakeState.setId(newStateId);
				for (ContainerValue value : values) {
					value.setLsState(fakeState);
				}
				flattenedNewValues.addAll(values);
				i++;
			}
			logger.debug("total number of values to create:" + newContentValues.size());
			insertContainerValues(flattenedNewValues);
			Long afterInsertValue = (new Date()).getTime();
			logger.debug("time to ignore states: " + String.valueOf(afterIgnore - beforeDbActions));
			logger.debug("time to insert states: " + String.valueOf(afterInsertState - afterIgnore));
			logger.debug("time to insert values: " + String.valueOf(afterInsertValue - afterInsertState));

		} catch (SQLException e) {
			logger.error("Caught error updating well content", e);
			if (e.getNextException() != null) {
				e = e.getNextException();
				logger.error("Caught SQL Exception", e);
			}
		}

		Long finishedAll = (new Date()).getTime();
		logger.debug("total time: " + String.valueOf(finishedAll - start));
		return results;
	}

	private Map<String, Long[]> getWellAndStatusContentStateIdsByCodeNames(List<String> wellCodes) throws Exception {
		EntityManager em = ContainerValue.entityManager();
		String queryString = "SELECT new map( well.codeName as wellCode, well.id as wellId, state.id as stateId) "
				+ "from Container as well ";
		queryString += SimpleUtil.makeInnerJoinHql("well.lsStates", "state", "status", "content");
		queryString += "where ( well.ignored <> true ) and ";
		Collection<Query> queries = SimpleUtil.splitHqlInClause(em, queryString, "well.codeName", wellCodes);
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		for (Query q : queries) {
			results.addAll(q.getResultList());
		}
		// aggregate results
		Map<String, Long[]> resultMap = new HashMap<String, Long[]>();
		for (Map<String, Object> result : results) {
			String containerCodeName = (String) result.get("wellCode");
			Long wellId = (Long) result.get("wellId");
			Long stateId = (Long) result.get("stateId");
			if (!resultMap.containsKey(containerCodeName)) {
				Long[] wellIdStateId = new Long[2];
				wellIdStateId[0] = wellId;
				wellIdStateId[1] = stateId;
				resultMap.put(containerCodeName, wellIdStateId);
			} else {
				throw new Exception("Found more than one status content state for well.");
			}
		}
		// diff request with results to find codeNames that could not be found
		HashSet<String> requestCodeNames = new HashSet<String>();
		requestCodeNames.addAll(wellCodes);
		HashSet<String> foundCodeNames = new HashSet<String>();
		foundCodeNames.addAll(resultMap.keySet());
		requestCodeNames.removeAll(foundCodeNames);
		logger.debug(requestCodeNames.size() + " not found with content");
		if (!requestCodeNames.isEmpty()) {
			List<String> notFoundWellCodes = new ArrayList<String>();
			notFoundWellCodes.addAll(requestCodeNames);
			String emptyWellQueryString = "SELECT new map( well.codeName as wellCode, well.id as wellId) "
					+ "from Container as well ";
			emptyWellQueryString += "where ( well.ignored <> true ) and ";
			Collection<Query> emptyWellQueries = SimpleUtil.splitHqlInClause(em, emptyWellQueryString, "well.codeName",
					notFoundWellCodes);
			List<Map<String, Object>> emptyWellResults = new ArrayList<Map<String, Object>>();
			for (Query q : emptyWellQueries) {
				emptyWellResults.addAll(q.getResultList());
			}
			for (Map<String, Object> result : emptyWellResults) {
				String containerCodeName = (String) result.get("wellCode");
				Long wellId = (Long) result.get("wellId");
				Long[] wellIdArray = new Long[1];
				wellIdArray[0] = wellId;
				foundCodeNames.add(containerCodeName);
				resultMap.put(containerCodeName, wellIdArray);
			}
			requestCodeNames.removeAll(foundCodeNames);
			logger.debug(emptyWellResults.size() + " found with no content, " + requestCodeNames.size()
					+ " not found at all");
		}
		if (!requestCodeNames.isEmpty()) {
			for (String notFoundCodeName : requestCodeNames) {
				resultMap.put(notFoundCodeName, null);
			}
		}
		return resultMap;
	}

	@Override
	public PlateStubDTO getPlateTypeByPlateBarcode(String plateBarcode) {
		EntityManager em = Container.entityManager();
		String queryString = "SELECT new com.labsynch.labseer.dto.PlateStubDTO( ";
		queryString += "barcode.labelText, ";
		queryString += "plate.codeName, ";
		queryString += "plateType.codeValue  ";
		queryString += " )  ";
		queryString += " from Container as plate ";
		queryString += SimpleUtil.makeInnerJoinHql("plate.lsLabels", "barcode", "barcode", "barcode");
		queryString += SimpleUtil.makeLeftJoinHql("plate.lsStates", "metadataInformationState", "metadata",
				"information");
		queryString += SimpleUtil.makeLeftJoinHql("metadataInformationState.lsValues", "plateType", "codeValue",
				"plate type");
		queryString += "where barcode.labelText = :plateBarcode";
		TypedQuery<PlateStubDTO> q = em.createQuery(queryString, PlateStubDTO.class);
		q.setParameter("plateBarcode", plateBarcode);
		try {
			PlateStubDTO result = q.getSingleResult();
			return result;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	@Transactional
	public Collection<ContainerErrorMessageDTO> getContainersByCodeNames(List<String> codeNames) {
		if (codeNames.isEmpty()) {
			return new ArrayList<ContainerErrorMessageDTO>();
		}
		EntityManager em = Container.entityManager();
		String queryString = "SELECT new com.labsynch.labseer.dto.ContainerErrorMessageDTO( " + "container.codeName, "
				+ "container )" + " FROM Container container ";
		queryString += "where ( container.ignored <> true ) and ";
		Collection<Query> queries = SimpleUtil.splitHqlInClause(em, queryString, "container.codeName", codeNames);
		Collection<ContainerErrorMessageDTO> results = new HashSet<ContainerErrorMessageDTO>();
		for (Query q : queries) {
			results.addAll(q.getResultList());
		}
		// diff request with results to find codeNames that could not be found
		HashSet<String> requestCodeNames = new HashSet<String>();
		requestCodeNames.addAll(codeNames);
		HashSet<String> foundCodeNames = new HashSet<String>();
		for (ContainerErrorMessageDTO result : results) {
			foundCodeNames.add(result.getContainerCodeName());
		}
		requestCodeNames.removeAll(foundCodeNames);
		if (!requestCodeNames.isEmpty()) {
			for (String notFoundCodeName : requestCodeNames) {
				ContainerErrorMessageDTO notFoundDTO = new ContainerErrorMessageDTO();
				notFoundDTO.setContainerCodeName(notFoundCodeName);
				notFoundDTO.setLevel("error");
				notFoundDTO.setMessage("containerCodeName not found");
				results.add(notFoundDTO);
			}
		}

		return results;
	}

	@Override
	public Collection<ContainerErrorMessageDTO> getDefinitionContainersByContainerCodeNames(List<String> codeNames) {
		if (codeNames.isEmpty()) {
			return new ArrayList<ContainerErrorMessageDTO>();
		}
		EntityManager em = Container.entityManager();
		String queryString = "SELECT new com.labsynch.labseer.dto.ContainerErrorMessageDTO( " + "container.codeName, "
				+ "definition )" + " FROM Container container ";
		queryString += SimpleUtil.makeInnerJoinHql("container.firstContainers", "itx", "defines",
				"definition container_container");
		queryString += SimpleUtil.makeInnerJoinHql("itx.firstContainer", "definition", "definition container");
		queryString += "where ( container.ignored <> true ) and ";
		Collection<Query> queries = SimpleUtil.splitHqlInClause(em, queryString, "container.codeName", codeNames);
		Collection<ContainerErrorMessageDTO> results = new HashSet<ContainerErrorMessageDTO>();
		for (Query q : queries) {
			results.addAll(q.getResultList());
		}
		// diff request with results to find codeNames that could not be found
		HashSet<String> requestCodeNames = new HashSet<String>();
		requestCodeNames.addAll(codeNames);
		HashSet<String> foundCodeNames = new HashSet<String>();
		for (ContainerErrorMessageDTO result : results) {
			result.setDefinition(result.getContainer());
			result.setContainer(null);
			foundCodeNames.add(result.getContainerCodeName());
		}
		requestCodeNames.removeAll(foundCodeNames);
		if (!requestCodeNames.isEmpty()) {
			for (String notFoundCodeName : requestCodeNames) {
				ContainerErrorMessageDTO notFoundDTO = new ContainerErrorMessageDTO();
				notFoundDTO.setContainerCodeName(notFoundCodeName);
				notFoundDTO.setLevel("error");
				notFoundDTO.setMessage("containerCodeName not found");
				results.add(notFoundDTO);
			}
		}

		return results;
	}

	public Collection<Map<String, Long>> getItxIdsByContainerCodeNamesAndItxTypeAndKind(List<String> codeNames,
			String itxType, String itxKind) {
		if (codeNames.isEmpty())
			return new ArrayList<Map<String, Long>>();
		EntityManager em = Container.entityManager();
		String queryString = "SELECT new map(container.codeName as containerCodeName, itx.id as itxId)"
				+ " FROM Container container ";
		queryString += SimpleUtil.makeInnerJoinHql("container.secondContainers", "itx", itxType, itxKind);
		queryString += "where ( container.ignored <> true ) and ";
		Collection<Query> queries = SimpleUtil.splitHqlInClause(em, queryString, "container.codeName", codeNames);
		Collection<Map<String, Long>> results = new HashSet<Map<String, Long>>();
		for (Query q : queries) {
			for (Object rawResult : q.getResultList()) {
				Map<String, Object> result = (Map<String, Object>) rawResult;
				String containerCodeName = (String) result.get("containerCodeName");
				Long itxId = (Long) result.get("itxId");
				Map<String, Long> codeNameIdMap = new HashMap<String, Long>();
				codeNameIdMap.put(containerCodeName, itxId);
				results.add(codeNameIdMap);
			}
		}
		return results;
	}

	@Override
	public Collection<ContainerWellCodeDTO> getWellCodesByContainerCodes(List<String> codeNames) {
		if (codeNames.isEmpty())
			return new ArrayList<ContainerWellCodeDTO>();
		EntityManager em = Container.entityManager();
		String queryString = "SELECT new map( " + "container.codeName as containerCodeName, "
				+ "well.codeName as wellCodeName )" + " FROM Container container ";
		queryString += SimpleUtil.makeInnerJoinHql("container.secondContainers", "itx", "has member");
		queryString += SimpleUtil.makeInnerJoinHql("itx.secondContainer", "well", "well");
		queryString += "where ( container.ignored <> true ) and ( well.ignored <> true) and ";
		Collection<Query> queries = SimpleUtil.splitHqlInClause(em, queryString, "container.codeName", codeNames);
		Collection<Map<String, String>> results = new HashSet<Map<String, String>>();
		for (Query q : queries) {
			results.addAll(q.getResultList());
		}
		// aggregate results
		Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
		for (Map<String, String> result : results) {
			String containerCodeName = result.get("containerCodeName");
			String wellCodeName = result.get("wellCodeName");
			if (!resultMap.containsKey(containerCodeName)) {
				List<String> foundWellCodes = new ArrayList<String>();
				foundWellCodes.add(wellCodeName);
				resultMap.put(containerCodeName, foundWellCodes);
			} else {
				resultMap.get(containerCodeName).add(wellCodeName);
			}
		}
		// diff request with results to find codeNames that could not be found
		HashSet<String> requestCodeNames = new HashSet<String>();
		requestCodeNames.addAll(codeNames);
		HashSet<String> foundCodeNames = new HashSet<String>();
		foundCodeNames.addAll(resultMap.keySet());
		requestCodeNames.removeAll(foundCodeNames);
		if (!requestCodeNames.isEmpty()) {
			for (String notFoundCodeName : requestCodeNames) {
				resultMap.put(notFoundCodeName, new ArrayList<String>());
			}
		}
		Collection<ContainerWellCodeDTO> sortedResults = new ArrayList<ContainerWellCodeDTO>();
		for (String requestCodeName : codeNames) {
			ContainerWellCodeDTO result = new ContainerWellCodeDTO(requestCodeName, resultMap.get(requestCodeName));
			sortedResults.add(result);
		}
		return sortedResults;
	}

	@Override
	public Collection<ContainerLocationDTO> moveToLocation(Collection<ContainerLocationDTO> requests) {
		logger.debug("Total requests " + requests.size());
		// check modified by and modified Date for all
		List<ContainerLocationDTO> requestsWithErrors = new ArrayList<ContainerLocationDTO>();
		List<ContainerLocationDTO> requestsWithoutErrors = new ArrayList<ContainerLocationDTO>();
		Map<String, ContainerLocationDTO> mapByContainerCodeName = new HashMap<String, ContainerLocationDTO>();
		List<String> requestContainerCodeNames = new ArrayList<String>();
		List<String> requestLocationCodeNames = new ArrayList<String>();
		// Mark errors for missing modifiedBy and modifiedDate and gather container
		// codenames and location codenames and do two bulk GETs
		for (ContainerLocationDTO request : requests) {
			if (request.getModifiedBy() == null) {
				request.setLevel("error");
				request.setMessage("modifiedBy must be provided");
				requestsWithErrors.add(request);
				continue;
			}
			if (request.getModifiedDate() == null) {
				request.setLevel("error");
				request.setMessage("modifiedDate must be provided");
				requestsWithErrors.add(request);
				continue;
			} else {
				// filter out only found pairs
				requestContainerCodeNames.add(request.getContainerCodeName());
				requestLocationCodeNames.add(request.getLocationCodeName());
				mapByContainerCodeName.put(request.getContainerCodeName(), request);
				requestsWithoutErrors.add(request);
			}
		}
		logger.debug("Requests without missing property errors " + requestsWithoutErrors.size());
		// Bulk GET of containers and locations
		Collection<ContainerErrorMessageDTO> foundContainerDTOs = getContainersByCodeNames(requestContainerCodeNames);
		Collection<ContainerErrorMessageDTO> foundLocationDTOs = getContainersByCodeNames(requestLocationCodeNames);
		Map<String, Container> foundContainers = new HashMap<String, Container>();
		Map<String, Container> foundLocations = new HashMap<String, Container>();
		for (ContainerErrorMessageDTO foundContainerDTO : foundContainerDTOs) {
			if (foundContainerDTO.getLevel() != null) {
				ContainerLocationDTO notFoundResult = mapByContainerCodeName
						.get(foundContainerDTO.getContainerCodeName());
				if (notFoundResult != null) {
					requestsWithErrors.add(notFoundResult);
					requestsWithoutErrors.remove(notFoundResult);
				}
			} else {
				foundContainers.put(foundContainerDTO.getContainerCodeName(), foundContainerDTO.getContainer());
			}
		}
		for (ContainerErrorMessageDTO foundLocationDTO : foundLocationDTOs) {
			if (foundLocationDTO.getLevel() == null) {
				foundLocations.put(foundLocationDTO.getContainerCodeName(), foundLocationDTO.getContainer());
			}
		}
		// Bulk GET of old ITX's IDs
		List<String> foundContainerCodeNames = new ArrayList<String>();
		foundContainerCodeNames.addAll(foundContainers.keySet());
		Collection<Map<String, Long>> oldItxIdMaps = getItxIdsByContainerCodeNamesAndItxTypeAndKind(
				foundContainerCodeNames, "moved to", "container_location");
		List<ItxContainerContainer> oldItxs = new ArrayList<ItxContainerContainer>();
		for (Map<String, Long> oldItxIdMap : oldItxIdMaps) {
			ItxContainerContainer oldItxStub = new ItxContainerContainer();
			String containerCodeName = oldItxIdMap.keySet().iterator().next();
			Long oldItxId = oldItxIdMap.get(containerCodeName);
			ContainerLocationDTO request = mapByContainerCodeName.get(containerCodeName);
			oldItxStub.setId(oldItxId);
			oldItxStub.setModifiedBy(request.getModifiedBy());
			oldItxStub.setModifiedDate(request.getModifiedDate());
			oldItxs.add(oldItxStub);
		}
		try {
			// Bulk Update/Ignore of old ITX's
			ignoreItxContainerContainers(oldItxs);
		} catch (SQLException e) {
			logger.error("Caught error trying to ignore old \"moved to\" interactions", e);
		}
		// Make bulk array of new ITX's
		List<ItxContainerContainer> newMovedToItxs = new ArrayList<ItxContainerContainer>();
		LsTransaction lsTransaction = new LsTransaction();
		lsTransaction.setRecordedDate(new Date());
		lsTransaction.persist();
		for (ContainerLocationDTO request : requestsWithoutErrors) {
			Container container = foundContainers.get(request.getContainerCodeName());
			Container location = foundLocations.get(request.getLocationCodeName());
			ItxContainerContainer newMovedToItx = new ItxContainerContainer();
			newMovedToItx.setLsType("moved to");
			newMovedToItx.setLsKind(container.getLsType() + "_" + location.getLsType());
			newMovedToItx.setRecordedBy(request.getModifiedBy());
			newMovedToItx.setRecordedDate(request.getModifiedDate());
			newMovedToItx.setLsTransaction(lsTransaction.getId());
			newMovedToItx.setFirstContainer(container);
			newMovedToItx.setSecondContainer(location);
			newMovedToItxs.add(newMovedToItx);
		}
		// Save bulk array of new itx's
		try {
			insertItxContainerContainers(newMovedToItxs);
		} catch (SQLException e) {
			logger.error("Caught error trying to insert new \"moved to\" interactions", e);
		}

		return requests;
	}

	@Override
	@Transactional
	public List<Long> insertContainerStates(final List<ContainerState> states) throws SQLException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		final List<Long> idList = SimpleUtil.getIdsFromSequence(jdbcTemplate, "state_pkseq", states.size());
		String sql = "INSERT INTO CONTAINER_STATE"
				+ "(id, comments, deleted, ignored, ls_kind, ls_transaction, ls_type, ls_type_and_kind, modified_by, modified_date, recorded_by, recorded_date, version, container_id) VALUES"
				+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ContainerState state = states.get(i);
				ps.setLong(1, idList.get(i));
				ps.setString(2, state.getComments());
				ps.setBoolean(3, state.isDeleted());
				ps.setBoolean(4, state.isIgnored());
				ps.setString(5, state.getLsKind());
				ps.setLong(6, state.getLsTransaction());
				ps.setString(7, state.getLsType());
				ps.setString(8, state.getLsType() + "_" + state.getLsKind());
				ps.setString(9, state.getModifiedBy());
				if (state.getModifiedDate() != null)
					ps.setTimestamp(10, new java.sql.Timestamp(state.getModifiedDate().getTime()));
				else
					ps.setTimestamp(10, null);
				ps.setString(11, state.getRecordedBy());
				ps.setTimestamp(12, new java.sql.Timestamp(state.getRecordedDate().getTime()));
				ps.setInt(13, 0);
				ps.setLong(14, state.getContainer().getId());
			}

			@Override
			public int getBatchSize() {
				return states.size();
			}
		});
		return idList;
	}

	@Override
	@Transactional
	public List<Long> insertContainers(final List<Container> containers) throws SQLException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		final List<Long> idList = SimpleUtil.getIdsFromSequence(jdbcTemplate, "thing_pkseq", containers.size());
		String sql = "INSERT INTO CONTAINER"
				+ "(id, code_name, deleted, ignored, ls_kind, ls_transaction, ls_type, ls_type_and_kind, modified_by, modified_date, recorded_by, recorded_date, version, column_index, location_id, row_index) VALUES"
				+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Container container = containers.get(i);
				ps.setLong(1, idList.get(i));
				ps.setString(2, container.getCodeName());
				ps.setBoolean(3, container.isDeleted());
				ps.setBoolean(4, container.isIgnored());
				ps.setString(5, container.getLsKind());
				ps.setLong(6, container.getLsTransaction());
				ps.setString(7, container.getLsType());
				ps.setString(8, container.getLsType() + "_" + container.getLsKind());
				ps.setString(9, container.getModifiedBy());
				if (container.getModifiedDate() != null)
					ps.setTimestamp(10, new java.sql.Timestamp(container.getModifiedDate().getTime()));
				else
					ps.setTimestamp(10, null);
				ps.setString(11, container.getRecordedBy());
				ps.setTimestamp(12, new java.sql.Timestamp(container.getRecordedDate().getTime()));
				ps.setInt(13, 0);
				if (container.getColumnIndex() != null)
					ps.setInt(14, container.getColumnIndex());
				else
					ps.setNull(14, java.sql.Types.INTEGER);
				if (container.getLocationId() != null)
					ps.setLong(15, container.getLocationId());
				else
					ps.setNull(15, java.sql.Types.BIGINT);
				if (container.getRowIndex() != null)
					ps.setInt(16, container.getRowIndex());
				else
					ps.setNull(16, java.sql.Types.INTEGER);
			}

			@Override
			public int getBatchSize() {
				return containers.size();
			}
		});
		return idList;
	}

	@Override
	@Transactional
	public List<Long> insertContainerLabels(final List<ContainerLabel> labels) throws SQLException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		final List<Long> idList = SimpleUtil.getIdsFromSequence(jdbcTemplate, "label_pkseq", labels.size());
		String sql = "INSERT INTO CONTAINER_LABEL"
				+ "(id, deleted, ignored, image_file, label_text, ls_kind, ls_transaction, ls_type, ls_type_and_kind, modified_date, physically_labled, preferred, recorded_by, recorded_date, version, container_id) VALUES"
				+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ContainerLabel label = labels.get(i);
				ps.setLong(1, idList.get(i));
				ps.setBoolean(2, label.isDeleted());
				ps.setBoolean(3, label.isIgnored());
				ps.setString(4, label.getImageFile());
				ps.setString(5, label.getLabelText());
				ps.setString(6, label.getLsKind());
				ps.setLong(7, label.getLsTransaction());
				ps.setString(8, label.getLsType());
				ps.setString(9, label.getLsType() + "_" + label.getLsKind());
				if (label.getModifiedDate() != null)
					ps.setTimestamp(10, new java.sql.Timestamp(label.getModifiedDate().getTime()));
				else
					ps.setTimestamp(10, null);
				ps.setBoolean(11, label.isPhysicallyLabled());
				ps.setBoolean(12, label.isPreferred());
				ps.setString(13, label.getRecordedBy());
				ps.setTimestamp(14, new java.sql.Timestamp(label.getRecordedDate().getTime()));
				ps.setInt(15, 0);
				ps.setLong(16, label.getContainer().getId());
			}

			@Override
			public int getBatchSize() {
				return labels.size();
			}
		});
		return idList;
	}

	@Override
	@Transactional
	public List<Long> insertItxContainerContainers(final List<ItxContainerContainer> itxContainerContainers)
			throws SQLException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		final List<Long> idList = SimpleUtil.getIdsFromSequence(jdbcTemplate, "thing_pkseq",
				itxContainerContainers.size());
		String sql = "INSERT INTO ITX_CONTAINER_CONTAINER"
				+ "(id, code_name, deleted, ignored, ls_kind, ls_transaction, ls_type, ls_type_and_kind, modified_by, modified_date, recorded_by, recorded_date, version, first_container_id, second_container_id) VALUES"
				+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ItxContainerContainer itxContainerContainer = itxContainerContainers.get(i);
				ps.setLong(1, idList.get(i));
				ps.setString(2, itxContainerContainer.getCodeName());
				ps.setBoolean(3, itxContainerContainer.isDeleted());
				ps.setBoolean(4, itxContainerContainer.isIgnored());
				ps.setString(5, itxContainerContainer.getLsKind());
				ps.setLong(6, itxContainerContainer.getLsTransaction());
				ps.setString(7, itxContainerContainer.getLsType());
				ps.setString(8, itxContainerContainer.getLsType() + "_" + itxContainerContainer.getLsKind());
				ps.setString(9, itxContainerContainer.getModifiedBy());
				if (itxContainerContainer.getModifiedDate() != null)
					ps.setTimestamp(10, new java.sql.Timestamp(itxContainerContainer.getModifiedDate().getTime()));
				else
					ps.setTimestamp(10, null);
				ps.setString(11, itxContainerContainer.getRecordedBy());
				ps.setTimestamp(12, new java.sql.Timestamp(itxContainerContainer.getRecordedDate().getTime()));
				ps.setInt(13, 0);
				ps.setLong(14, itxContainerContainer.getFirstContainer().getId());
				ps.setLong(15, itxContainerContainer.getSecondContainer().getId());
			}

			@Override
			public int getBatchSize() {
				return itxContainerContainers.size();
			}
		});
		return idList;
	}

	@Override
	@Transactional
	public void ignoreContainerStates(final List<ContainerState> states) throws SQLException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "UPDATE CONTAINER_STATE "
				+ "set ignored = ?, modified_by = ?, modified_date = ?, version = version + 1 WHERE id = ?";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ContainerState state = states.get(i);
				ps.setBoolean(1, true);
				ps.setString(2, state.getModifiedBy());
				if (state.getModifiedDate() != null)
					ps.setTimestamp(3, new java.sql.Timestamp(state.getModifiedDate().getTime()));
				else
					ps.setTimestamp(3, null);
				ps.setLong(4, state.getId());
			}

			@Override
			public int getBatchSize() {
				return states.size();
			}
		});
	}

	@Override
	@Transactional
	public void ignoreItxContainerContainers(final List<ItxContainerContainer> itxContainerContainers)
			throws SQLException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		String sql = "UPDATE ITX_CONTAINER_CONTAINER "
				+ "set ignored = ?, modified_by = ?, modified_date = ?, version = version + 1 WHERE id = ?";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ItxContainerContainer itx = itxContainerContainers.get(i);
				ps.setBoolean(1, true);
				ps.setString(2, itx.getModifiedBy());
				if (itx.getModifiedDate() != null)
					ps.setTimestamp(3, new java.sql.Timestamp(itx.getModifiedDate().getTime()));
				else
					ps.setTimestamp(3, null);
				ps.setLong(4, itx.getId());
			}

			@Override
			public int getBatchSize() {
				return itxContainerContainers.size();
			}
		});
	}

	@Override
	@Transactional
	public List<Long> insertContainerValues(final List<ContainerValue> values) throws SQLException {
		jdbcTemplate = new JdbcTemplate(dataSource);
		final List<Long> idList = SimpleUtil.getIdsFromSequence(jdbcTemplate, "value_pkseq", values.size());
		String sql = "INSERT INTO CONTAINER_VALUE "
				+ "(id, blob_value, clob_value, code_kind, code_origin, code_type, code_type_and_kind, code_value, comments, conc_unit, concentration, "
				+ " date_value, deleted, file_value, ignored, ls_kind, ls_transaction, ls_type, ls_type_and_kind, modified_by, modified_date, "
				+ "number_of_replicates, numeric_value, operator_kind, operator_type, operator_type_and_kind, public_data, recorded_by, recorded_date, "
				+ "sig_figs, string_value, uncertainty, uncertainty_type, unit_kind, unit_type, url_value, version, container_state_id) VALUES "
				+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ContainerValue value = values.get(i);
				ps.setLong(1, idList.get(i));
				ps.setBytes(2, value.getBlobValue());
				ps.setString(3, value.getClobValue());
				// ps.setNull(2, java.sql.Types.BLOB);
				// ps.setNull(3, java.sql.Types.CLOB);
				// TODO: fix blobs and clobs
				ps.setString(4, value.getCodeKind());
				ps.setString(5, value.getCodeOrigin());
				ps.setString(6, value.getCodeType());
				ps.setString(7, value.getCodeType() + "_" + value.getCodeKind());
				ps.setString(8, value.getCodeValue());
				ps.setString(9, value.getComments());
				ps.setString(10, value.getConcUnit());
				if (value.getConcentration() != null)
					ps.setDouble(11, value.getConcentration());
				else
					ps.setNull(11, java.sql.Types.DOUBLE);
				if (value.getDateValue() != null)
					ps.setTimestamp(12, new java.sql.Timestamp(value.getDateValue().getTime()));
				else
					ps.setTimestamp(12, null);
				ps.setBoolean(13, value.getDeleted());
				ps.setString(14, value.getFileValue());
				ps.setBoolean(15, value.getIgnored());
				ps.setString(16, value.getLsKind());
				ps.setLong(17, value.getLsTransaction());
				ps.setString(18, value.getLsType());
				ps.setString(19, value.getLsType() + "_" + value.getLsKind());
				ps.setString(20, value.getModifiedBy());
				if (value.getModifiedDate() != null)
					ps.setTimestamp(21, new java.sql.Timestamp(value.getModifiedDate().getTime()));
				else
					ps.setTimestamp(21, null);
				if (value.getNumberOfReplicates() != null)
					ps.setInt(22, value.getNumberOfReplicates());
				else
					ps.setNull(22, java.sql.Types.INTEGER);
				ps.setBigDecimal(23, value.getNumericValue());
				ps.setString(24, value.getOperatorKind());
				ps.setString(25, value.getOperatorType());
				ps.setString(26, value.getOperatorType() + "_" + value.getOperatorKind());
				ps.setBoolean(27, value.getPublicData());
				ps.setString(28, value.getRecordedBy());
				if (value.getRecordedDate() != null)
					ps.setTimestamp(29, new java.sql.Timestamp(value.getRecordedDate().getTime()));
				else
					ps.setTimestamp(29, null);
				if (value.getSigFigs() != null)
					ps.setInt(30, value.getSigFigs());
				else
					ps.setNull(30, java.sql.Types.INTEGER);
				ps.setString(31, value.getStringValue());
				ps.setBigDecimal(32, value.getUncertainty());
				ps.setString(33, value.getUncertaintyType());
				ps.setString(34, value.getUnitKind());
				ps.setString(35, value.getUnitType());
				ps.setString(36, value.getUrlValue());
				ps.setInt(37, 0);
				ps.setLong(38, value.getLsState().getId());
			}

			@Override
			public int getBatchSize() {
				return values.size();
			}
		});
		return idList;
	}

	@Override
	@Transactional
	public List<CodeTableDTO> convertToCodeTables(List<Container> containers) {
		List<CodeTableDTO> codeTables = new ArrayList<CodeTableDTO>();
		for (Container container : containers) {
			CodeTableDTO codeTable = new CodeTableDTO();
			codeTable.setId(container.getId());
			codeTable.setCode(container.getCodeName());
			codeTable.setName(pickBestLabel(container));
			codeTable.setIgnored(container.isIgnored());
			codeTables.add(codeTable);
		}
		return codeTables;
	}

	@Override
	@Transactional
	public Collection<Container> searchContainers(ContainerSearchRequestDTO searchRequest) {
		List<Container> containerList = new ArrayList<Container>();
		EntityManager em = Container.entityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Container> criteria = cb.createQuery(Container.class);
		Root<Container> containerRoot = criteria.from(Container.class);

		criteria.select(containerRoot);
		criteria.distinct(true);
		Predicate[] predicates = new Predicate[0];
		List<Predicate> predicateList = new ArrayList<Predicate>();

		// barcode
		if (searchRequest.getBarcode() != null) {
			String barcode = searchRequest.getBarcode().replaceAll("\\*", "%");
			Join<Container, ContainerLabel> containerLabel = containerRoot.join("lsLabels");
			Predicate barcodeType = cb.equal(containerLabel.<String>get("lsType"), "barcode");
			Predicate barcodeKind = cb.equal(containerLabel.<String>get("lsKind"), "barcode");
			Predicate barcodeLabelPredicate = cb.like(containerLabel.<String>get("labelText"), '%' + barcode + '%');
			Predicate containerLabelNotIgnored = cb.not(containerLabel.<Boolean>get("ignored"));
			Predicate barcodePredicate = cb.and(barcodeType, barcodeKind, barcodeLabelPredicate,
					containerLabelNotIgnored);
			predicateList.add(barcodePredicate);
		}
		// description
		if (searchRequest.getDescription() != null || searchRequest.getCreatedUser() != null
				|| searchRequest.getStatus() != null || searchRequest.getType() != null) {
			Join<Container, ContainerState> containerState = containerRoot.join("lsStates");
			Predicate stateType = cb.equal(containerState.<String>get("lsType"), "metadata");
			Predicate stateKind = cb.equal(containerState.<String>get("lsKind"), "information");
			Predicate stateNotIgnored = cb.not(containerState.<Boolean>get("ignored"));
			Predicate statePredicate = cb.and(stateType, stateKind, stateNotIgnored);
			predicateList.add(statePredicate);
			if (searchRequest.getDescription() != null) {
				String description = searchRequest.getDescription().replaceAll("\\*", "%");
				;
				Join<ContainerState, ContainerValue> descriptionValue = containerState.join("lsValues");
				Predicate descriptionStringValue = cb.like(descriptionValue.<String>get("stringValue"),
						'%' + description + '%');
				Predicate descriptionType = cb.equal(descriptionValue.<String>get("lsType"), "stringValue");
				Predicate descriptionKind = cb.equal(descriptionValue.<String>get("lsKind"), "description");
				Predicate descriptionNotIgnored = cb.not(descriptionValue.<Boolean>get("ignored"));
				Predicate descriptionPredicate = cb.and(descriptionStringValue, descriptionType, descriptionKind,
						descriptionNotIgnored);
				predicateList.add(descriptionPredicate);
			}
			if (searchRequest.getCreatedUser() != null) {
				String createdUser = searchRequest.getCreatedUser().replaceAll("\\*", "%");
				;
				Join<ContainerState, ContainerValue> createdUserValue = containerState.join("lsValues");
				Predicate createdUserStringValue = cb.like(createdUserValue.<String>get("codeValue"),
						'%' + createdUser + '%');
				Predicate createdUserType = cb.equal(createdUserValue.<String>get("lsType"), "codeValue");
				Predicate createdUserKind = cb.equal(createdUserValue.<String>get("lsKind"), "created user");
				Predicate createdUserNotIgnored = cb.not(createdUserValue.<Boolean>get("ignored"));
				Predicate createdUserPredicate = cb.and(createdUserStringValue, createdUserType, createdUserKind,
						createdUserNotIgnored);
				predicateList.add(createdUserPredicate);
			}
			if (searchRequest.getStatus() != null) {
				String status = searchRequest.getStatus().replaceAll("\\*", "%");
				;
				Join<ContainerState, ContainerValue> statusValue = containerState.join("lsValues");
				Predicate statusStringValue = cb.like(statusValue.<String>get("codeValue"), '%' + status + '%');
				Predicate statusType = cb.equal(statusValue.<String>get("lsType"), "codeValue");
				Predicate statusKind = cb.equal(statusValue.<String>get("lsKind"), "status");
				Predicate statusNotIgnored = cb.not(statusValue.<Boolean>get("ignored"));
				Predicate statusPredicate = cb.and(statusStringValue, statusType, statusKind, statusNotIgnored);
				predicateList.add(statusPredicate);
			}
			if (searchRequest.getType() != null) {
				String type = searchRequest.getType().replaceAll("\\*", "%");
				;
				Join<ContainerState, ContainerValue> typeValue = containerState.join("lsValues");
				Predicate typeStringValue = cb.like(typeValue.<String>get("codeValue"), '%' + type + '%');
				Predicate typeType = cb.equal(typeValue.<String>get("lsType"), "codeValue");
				Predicate typeKind = cb.equal(typeValue.<String>get("lsKind"), "type");
				Predicate typeNotIgnored = cb.not(typeValue.<Boolean>get("ignored"));
				Predicate typePredicate = cb.and(typeStringValue, typeType, typeKind, typeNotIgnored);
				predicateList.add(typePredicate);
			}
		}
		// definition
		if (searchRequest.getDefinition() != null) {
			Join<Container, ItxContainerContainer> definesItx = containerRoot.join("firstContainers");
			Join<ItxContainerContainer, Container> definitionContainer = definesItx.join("firstContainer");
			Predicate definesType = cb.equal(definesItx.<String>get("lsType"), "defines");
			Predicate definesKind = cb.equal(definesItx.<String>get("lsKind"), "definition container_container");
			Predicate definitionCode = cb.equal(definitionContainer.<String>get("codeName"),
					searchRequest.getDefinition());
			Predicate definitionPredicate = cb.and(definesType, definesKind, definitionCode);
			predicateList.add(definitionPredicate);
		}
		// lsType
		if (searchRequest.getLsType() != null) {
			Predicate containerType = cb.equal(containerRoot.<String>get("lsType"), searchRequest.getLsType());
			predicateList.add(containerType);
		}
		// lsKind
		if (searchRequest.getLsKind() != null) {
			Predicate containerKind = cb.equal(containerRoot.<String>get("lsKind"), searchRequest.getLsKind());
			predicateList.add(containerKind);
		}
		//// requestId
		// container not ignored
		Predicate containerNotIgnored = cb.not(containerRoot.<Boolean>get("ignored"));
		predicateList.add(containerNotIgnored);
		predicates = predicateList.toArray(predicates);
		criteria.where(cb.and(predicates));
		TypedQuery<Container> q = em.createQuery(criteria);
		q.setMaxResults(propertiesUtilService.getContainerInventorySearchMaxResult());
		containerList = q.getResultList();
		return containerList;
	}

	@Override
	public Collection<String> getContainersByContainerValue(ContainerValueRequestDTO requestDTO, Boolean like,
			Boolean rightLike) throws Exception {
		// validate request
		if (requestDTO.getContainerType() == null)
			throw new Exception("Container type must be specified");
		if (requestDTO.getContainerKind() == null)
			throw new Exception("Container kind must be specified");
		if (requestDTO.getStateType() == null)
			throw new Exception("State type must be specified");
		if (requestDTO.getStateKind() == null)
			throw new Exception("State kind must be specified");
		if (requestDTO.getValueType() == null)
			throw new Exception("Value type must be specified");
		if (requestDTO.getValueKind() == null)
			throw new Exception("Value kind must be specified");
		else if (!requestDTO.getValueType().equals("stringValue") && !requestDTO.getValueType().equals("codeValue"))
			throw new Exception("Only value types of stringValue or codeValue are accepted");
		if (requestDTO.getValue() == null)
			throw new Exception("Value must be specified");

		if (like != null && like) {
			String fullQuery = "SELECT DISTINCT container.codeName FROM Container container "
					+ "JOIN container.lsStates containerState " + "JOIN containerState.lsValues containerValue "
					+ "WHERE container.lsType = " + "'" + requestDTO.getContainerType() + "'" + " "
					+ "AND container.lsKind = " + "'" + requestDTO.getContainerKind() + "'" + " "
					+ "AND container.ignored <> true " + "AND containerState.lsType = " + "'"
					+ requestDTO.getStateType() + "'" + " " + "AND containerState.lsKind = " + "'"
					+ requestDTO.getStateKind() + "'" + " " + "AND containerState.ignored <> true "
					+ "AND containerValue.lsType = " + "'" + requestDTO.getValueType() + "'" + " "
					+ "AND containerValue.lsKind = " + "'" + requestDTO.getValueKind() + "'" + " "
					+ "AND containerValue.ignored <> true ";
			if (requestDTO.getValueType().equals("stringValue")) {
				if (like != null && like) {
					if (rightLike != null && rightLike) {
						fullQuery += "AND containerValue.stringValue LIKE " + "'" + requestDTO.getValue() + "%'";
					} else {
						fullQuery += "AND containerValue.stringValue LIKE " + "'%" + requestDTO.getValue() + "%'";
					}
				} else {
					fullQuery += "AND containerValue.stringValue = " + "'" + requestDTO.getValue() + "'";
				}
			} else if (requestDTO.getValueType().equals("codeValue")) {
				if (like != null && like) {
					if (rightLike != null && rightLike) {
						fullQuery += "AND containerValue.codeValue LIKE " + "'" + requestDTO.getValue() + "%'";
					} else {
						fullQuery += "AND containerValue.codeValue LIKE " + "'%" + requestDTO.getValue() + "%'";
					}
				} else {
					fullQuery += "AND containerValue.codeValue = " + "'" + requestDTO.getValue() + "'";
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug(fullQuery);
			}
			EntityManager em = Container.entityManager();
			TypedQuery<String> q = em.createQuery(fullQuery, String.class);

			return q.getResultList();
		} else {
			EntityManager em = Container.entityManager();
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<String> criteria = cb.createQuery(String.class);
			Root<Container> containerRoot = criteria.from(Container.class);
			criteria.select(containerRoot.<String>get("codeName"));
			criteria.distinct(true);
			Predicate[] predicates = new Predicate[0];
			List<Predicate> predicateList = new ArrayList<Predicate>();

			Predicate containerType = cb.equal(containerRoot.<String>get("lsType"), requestDTO.getContainerType());
			Predicate containerKind = cb.equal(containerRoot.<String>get("lsKind"), requestDTO.getContainerKind());
			Predicate containerNotIgnored = cb.not(containerRoot.<Boolean>get("ignored"));
			Predicate containerPredicate = cb.and(containerType, containerKind, containerNotIgnored);
			predicateList.add(containerPredicate);

			Join<Container, ContainerState> containerState = containerRoot.join("lsStates");
			Predicate stateType = cb.equal(containerState.<String>get("lsType"), requestDTO.getStateType());
			Predicate stateKind = cb.equal(containerState.<String>get("lsKind"), requestDTO.getStateKind());
			Predicate stateNotIgnored = cb.not(containerState.<Boolean>get("ignored"));
			Predicate statePredicate = cb.and(stateType, stateKind, stateNotIgnored);
			predicateList.add(statePredicate);

			Join<ContainerState, ContainerValue> containerValue = containerState.join("lsValues");
			Predicate valueType = cb.equal(containerValue.<String>get("lsType"), requestDTO.getValueType());
			Predicate valueKind = cb.equal(containerValue.<String>get("lsKind"), requestDTO.getValueKind());
			Predicate valueNotIgnored = cb.not(containerValue.<Boolean>get("ignored"));
			if (requestDTO.getValueType().equals("stringValue")) {
				Predicate stringValue;
				if (like != null && like) {
					if (rightLike != null && rightLike) {
						stringValue = cb.like(containerValue.<String>get("stringValue"), requestDTO.getValue() + "%");
					} else {
						stringValue = cb.like(containerValue.<String>get("stringValue"),
								"%" + requestDTO.getValue() + "%");
					}
				} else {
					stringValue = cb.equal(containerValue.<String>get("stringValue"), requestDTO.getValue());
				}
				Predicate valuePredicate = cb.and(valueType, valueKind, valueNotIgnored, stringValue);
				predicateList.add(valuePredicate);
			} else if (requestDTO.getValueType().equals("codeValue")) {
				Predicate codeValue;
				if (like != null && like) {
					if (rightLike != null && rightLike) {
						codeValue = cb.like(containerValue.<String>get("codeValue"), requestDTO.getValue() + "%");
					} else {
						codeValue = cb.like(containerValue.<String>get("codeValue"), "%" + requestDTO.getValue() + "%");
					}
				} else {
					codeValue = cb.equal(containerValue.<String>get("codeValue"), requestDTO.getValue());
				}
				Predicate valuePredicate = cb.and(valueType, valueKind, valueNotIgnored, codeValue);
				predicateList.add(valuePredicate);
			}
			predicates = predicateList.toArray(predicates);
			criteria.where(cb.and(predicates));
			TypedQuery<String> q = em.createQuery(criteria);

			return q.getResultList();
		}
	}

	@Override
	public Collection<Container> getContainersByIds(Collection<Long> containerIds) {
		Collection<Container> containers = new ArrayList<Container>();
		for (Long id : containerIds) {
			containers.add(Container.findContainer(id));
		}
		return containers;
	}

	@Override
	public Collection<Long> searchContainerIdsByQueryDTO(ContainerQueryDTO query) throws Exception {
		List<Long> containerIdList = new ArrayList<Long>();
		EntityManager em = Container.entityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
		Root<Container> thing = criteria.from(Container.class);
		List<Predicate> predicateList = buildPredicatesForQueryDTO(criteriaBuilder, criteria, thing, query);
		if (query.getLsType() != null) {
			Predicate thingType = criteriaBuilder.equal(thing.<String>get("lsType"), query.getLsType());
			predicateList.add(thingType);
		}
		if (query.getLsKind() != null) {
			Predicate thingKind = criteriaBuilder.equal(thing.<String>get("lsKind"), query.getLsKind());
			predicateList.add(thingKind);
		}
		Predicate[] predicates = new Predicate[0];
		// gather all predicates
		predicates = predicateList.toArray(predicates);
		criteria.where(criteriaBuilder.and(predicates));
		TypedQuery<Long> q = em.createQuery(criteria);
		logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
		containerIdList = q.getResultList();
		logger.debug("Found " + containerIdList.size() + " results.");
		return containerIdList;
	}

	private List<Predicate> buildPredicatesForQueryDTO(CriteriaBuilder criteriaBuilder, CriteriaQuery<Long> criteria,
			Root<Container> thing, ContainerQueryDTO query) throws Exception {

		criteria.select(thing.<Long>get("id"));
		criteria.distinct(true);
		List<Predicate> predicateList = new ArrayList<Predicate>();
		// root container properties
		predicateList.add(criteriaBuilder.not(thing.<Boolean>get("ignored")));

		// recordedDates
		if (query.getRecordedDateGreaterThan() != null && query.getRecordedDateLessThan() != null) {
			try {
				Predicate createdDateBetween = criteriaBuilder.between(thing.<Date>get("recordedDate"),
						query.getRecordedDateGreaterThan(), query.getRecordedDateLessThan());
				predicateList.add(createdDateBetween);
			} catch (Exception e) {
				logger.error("Caught exception trying to parse " + query.getRecordedDateGreaterThan() + " or "
						+ query.getRecordedDateLessThan() + " as a date.", e);
				throw new Exception("Caught exception trying to parse " + query.getRecordedDateGreaterThan() + " or "
						+ query.getRecordedDateLessThan() + " as a date.", e);
			}
		} else if (query.getRecordedDateGreaterThan() != null) {
			try {
				Predicate createdDateFrom = criteriaBuilder.greaterThanOrEqualTo(thing.<Date>get("recordedDate"),
						query.getRecordedDateGreaterThan());
				predicateList.add(createdDateFrom);
			} catch (Exception e) {
				logger.error("Caught exception trying to parse " + query.getRecordedDateGreaterThan() + " as a date.",
						e);
				throw new Exception(
						"Caught exception trying to parse " + query.getRecordedDateGreaterThan() + " as a date.", e);
			}
		} else if (query.getRecordedDateLessThan() != null) {
			try {
				Predicate createdDateTo = criteriaBuilder.lessThanOrEqualTo(thing.<Date>get("recordedDate"),
						query.getRecordedDateLessThan());
				predicateList.add(createdDateTo);
			} catch (Exception e) {
				logger.error("Caught exception trying to parse " + query.getRecordedDateLessThan() + " as a date.", e);
				throw new Exception(
						"Caught exception trying to parse " + query.getRecordedDateLessThan() + " as a date.", e);
			}
		}
		if (query.getRecordedBy() != null) {
			Predicate recordedBy = criteriaBuilder.like(thing.<String>get("recordedBy"),
					'%' + query.getRecordedBy() + '%');
			predicateList.add(recordedBy);
		}

		// interactions
		if (query.getFirstInteractions() != null) {
			for (ItxQueryDTO interaction : query.getFirstInteractions()) {
				Join<Container, ItxContainerContainer> firstItx = thing.join("firstContainers");
				Join<ItxContainerContainer, Container> firstContainer = firstItx.join("firstContainer");
				Join<Container, ContainerLabel> firstContainerLabel = firstContainer.join("lsLabels", JoinType.LEFT);
				List<Predicate> firstItxPredicates = buildInteractionPredicatesForQueryDTO(criteriaBuilder, firstItx,
						firstContainer, firstContainerLabel, interaction);
				// gather interactions, AND between each within group, then OR on the two, then
				// add to outer predicates
				Predicate[] firstPredicates = new Predicate[0];
				firstPredicates = firstItxPredicates.toArray(firstPredicates);
				Predicate firstItxAndPredicates = criteriaBuilder.and(firstPredicates);
				predicateList.add(firstItxAndPredicates);
			}
		}
		if (query.getSecondInteractions() != null) {
			for (ItxQueryDTO interaction : query.getSecondInteractions()) {
				Join<Container, ItxContainerContainer> secondItx = thing.join("secondContainers");
				Join<ItxContainerContainer, Container> secondContainer = secondItx.join("secondContainer");
				Join<Container, ContainerLabel> secondContainerLabel = secondContainer.join("lsLabels", JoinType.LEFT);
				List<Predicate> secondItxPredicates = buildInteractionPredicatesForQueryDTO(criteriaBuilder, secondItx,
						secondContainer, secondContainerLabel, interaction);
				// gather interactions, AND between each within group, then OR on the two, then
				// add to outer predicates
				Predicate[] secondPredicates = new Predicate[0];
				secondPredicates = secondItxPredicates.toArray(secondPredicates);
				Predicate secondItxAndPredicates = criteriaBuilder.and(secondPredicates);
				predicateList.add(secondItxAndPredicates);
			}
		}

		// subjects
		if (query.getSubjects() != null) {
			for (ItxQueryDTO interaction : query.getSubjects()) {
				Join<Container, ItxSubjectContainer> subjectItx = thing.join("subjects");
				Join<ItxSubjectContainer, Subject> subject = subjectItx.join("subject");
				Join<Subject, SubjectLabel> subjectLabel = subject.join("lsLabels", JoinType.LEFT);
				List<Predicate> subjectItxPredicates = buildInteractionPredicatesForQueryDTO(criteriaBuilder,
						subjectItx, subject, subjectLabel, interaction);
				// gather interactions, AND between each within group, then OR on the two, then
				// add to outer predicates
				Predicate[] subjectPredicates = new Predicate[0];
				subjectPredicates = subjectItxPredicates.toArray(subjectPredicates);
				Predicate subjectItxAndPredicates = criteriaBuilder.and(subjectPredicates);
				predicateList.add(subjectItxAndPredicates);
			}
		}

		// values
		if (query.getValues() != null) {
			for (ValueQueryDTO valueQuery : query.getValues()) {
				Predicate valuePredicate = buildValuePredicateForQueryDTO(criteriaBuilder, valueQuery, thing);
				predicateList.add(valuePredicate);
			}
		}

		// labels
		if (query.getLabels() != null) {
			for (LabelQueryDTO queryLabel : query.getLabels()) {
				Join<Container, ContainerLabel> label = thing.join("lsLabels");
				List<Predicate> labelPredicatesList = new ArrayList<Predicate>();
				labelPredicatesList.add(criteriaBuilder.not(label.<Boolean>get("ignored")));
				if (queryLabel.getLabelType() != null) {
					Predicate labelType = criteriaBuilder.equal(label.<String>get("lsType"), queryLabel.getLabelType());
					labelPredicatesList.add(labelType);
				}
				if (queryLabel.getLabelKind() != null) {
					Predicate labelKind = criteriaBuilder.equal(label.<String>get("lsKind"), queryLabel.getLabelKind());
					labelPredicatesList.add(labelKind);
				}
				if (queryLabel.getLabelText() != null) {
					Predicate labelText = criteriaBuilder.like(label.<String>get("labelText"),
							'%' + queryLabel.getLabelText() + '%');
					labelPredicatesList.add(labelText);
				}
				// gather labels
				Predicate[] labelPredicates = new Predicate[0];
				labelPredicates = labelPredicatesList.toArray(labelPredicates);
				predicateList.add(criteriaBuilder.and(labelPredicates));
			}
		}
		return predicateList;
	}

	private List<Predicate> buildInteractionPredicatesForQueryDTO(CriteriaBuilder criteriaBuilder, From itx,
			From interactingThing, From interactingThingLabel, ItxQueryDTO interaction) throws Exception {
		List<Predicate> itxPredicates = new ArrayList<Predicate>();
		Predicate itxNotIgn = criteriaBuilder.isFalse(itx.<Boolean>get("ignored"));
		Predicate containerNotIgn = criteriaBuilder.isFalse(interactingThing.<Boolean>get("ignored"));
		Predicate containerLabelNotIgn = criteriaBuilder.isFalse(interactingThingLabel.<Boolean>get("ignored"));
		itxPredicates.add(itxNotIgn);
		itxPredicates.add(containerNotIgn);
		itxPredicates.add(containerLabelNotIgn);
		if (interaction.getInteractionType() != null) {
			Predicate itxType = criteriaBuilder.equal(itx.<String>get("lsType"), interaction.getInteractionType());
			itxPredicates.add(itxType);
		}
		if (interaction.getInteractionKind() != null) {
			Predicate itxKind = criteriaBuilder.equal(itx.<String>get("lsKind"), interaction.getInteractionKind());
			itxPredicates.add(itxKind);
		}
		if (interaction.getThingType() != null) {
			Predicate containerType = criteriaBuilder.equal(interactingThing.<String>get("lsType"),
					interaction.getThingType());
			itxPredicates.add(containerType);
		}
		if (interaction.getThingKind() != null) {
			Predicate containerKind = criteriaBuilder.equal(interactingThing.<String>get("lsKind"),
					interaction.getThingKind());
			itxPredicates.add(containerKind);
		}
		if (interaction.getThingCodeName() != null) {
			Predicate containerCodeName = criteriaBuilder.equal(interactingThing.<String>get("codeName"),
					interaction.getThingCodeName());
			itxPredicates.add(containerCodeName);
		}
		if (interaction.getThingLabelType() != null) {
			Predicate containerLabelType = criteriaBuilder.equal(interactingThingLabel.<String>get("lsType"),
					interaction.getThingLabelType());
			itxPredicates.add(containerLabelType);
		}
		if (interaction.getThingLabelKind() != null) {
			Predicate containerLabelKind = criteriaBuilder.equal(interactingThingLabel.<String>get("lsKind"),
					interaction.getThingLabelKind());
			itxPredicates.add(containerLabelKind);
		}
		if (interaction.getThingLabelText() != null) {
			if (interaction.getOperator() != null) {
				if (interaction.getOperator().equals("=")) {
					Predicate interactingThingLabelEquals = criteriaBuilder
							.equal(interactingThingLabel.<String>get("labelText"), interaction.getThingLabelText());
					itxPredicates.add(interactingThingLabelEquals);
				} else if (interaction.getOperator().equals("!=")) {
					Predicate interactingThingLabelNotEquals = criteriaBuilder
							.notEqual(interactingThingLabel.<String>get("labelText"), interaction.getThingLabelText());
					itxPredicates.add(interactingThingLabelNotEquals);
				} else if (interaction.getOperator().equals("~")) {
					Predicate interactingThingLabelLike = criteriaBuilder
							.like(interactingThingLabel.<String>get("labelText"), interaction.getThingLabelText());
					itxPredicates.add(interactingThingLabelLike);
				} else if (interaction.getOperator().equals("!~")) {
					Predicate interactingThingLabelNotLike = criteriaBuilder
							.notLike(interactingThingLabel.<String>get("labelText"), interaction.getThingLabelText());
					itxPredicates.add(interactingThingLabelNotLike);
				} else if (interaction.getOperator().equals(">")) {
					Predicate interactingThingLabelGreaterThan = criteriaBuilder.greaterThan(
							interactingThingLabel.<String>get("labelText"), interaction.getThingLabelText());
					itxPredicates.add(interactingThingLabelGreaterThan);
				} else if (interaction.getOperator().equals(">=")) {
					Predicate interactingThingLabelGreaterThan = criteriaBuilder.greaterThanOrEqualTo(
							interactingThingLabel.<String>get("labelText"), interaction.getThingLabelText());
					itxPredicates.add(interactingThingLabelGreaterThan);
				} else if (interaction.getOperator().equals("<")) {
					Predicate interactingThingLabelLessThan = criteriaBuilder
							.lessThan(interactingThingLabel.<String>get("labelText"), interaction.getThingLabelText());
					itxPredicates.add(interactingThingLabelLessThan);
				} else if (interaction.getOperator().equals("<=")) {
					Predicate interactingThingLabelLessThan = criteriaBuilder.lessThanOrEqualTo(
							interactingThingLabel.<String>get("labelText"), interaction.getThingLabelText());
					itxPredicates.add(interactingThingLabelLessThan);
				} else {
					Predicate interactingThingLabelLike = criteriaBuilder.like(
							interactingThingLabel.<String>get("labelText"),
							'%' + interaction.getThingLabelText() + '%');
					itxPredicates.add(interactingThingLabelLike);
				}
			} else {
				Predicate interactingThingLabelLike = criteriaBuilder.like(
						interactingThingLabel.<String>get("labelText"), '%' + interaction.getThingLabelText() + '%');
				itxPredicates.add(interactingThingLabelLike);
			}
		}
		if (interaction.getThingValues() != null) {
			for (ValueQueryDTO valueQuery : interaction.getThingValues()) {
				Predicate valuePredicate = buildValuePredicateForQueryDTO(criteriaBuilder, valueQuery,
						interactingThing);
				itxPredicates.add(valuePredicate);
			}
		}
		return itxPredicates;
	}

	private Predicate buildValuePredicateForQueryDTO(CriteriaBuilder criteriaBuilder, ValueQueryDTO valueQuery,
			From thing) throws Exception {
		List<Predicate> valuePredicatesList = new ArrayList<Predicate>();
		Join<Container, ContainerState> state = thing.join("lsStates");
		Join<ContainerState, ContainerValue> value = state.join("lsValues");

		Predicate stateNotIgn = criteriaBuilder.isFalse(state.<Boolean>get("ignored"));
		Predicate valueNotIgn = criteriaBuilder.isFalse(value.<Boolean>get("ignored"));
		valuePredicatesList.add(stateNotIgn);
		valuePredicatesList.add(valueNotIgn);

		if (valueQuery.getStateType() != null) {
			Predicate stateType = criteriaBuilder.equal(state.<String>get("lsType"), valueQuery.getStateType());
			valuePredicatesList.add(stateType);
		}
		if (valueQuery.getStateKind() != null) {
			Predicate stateKind = criteriaBuilder.equal(state.<String>get("lsKind"), valueQuery.getStateKind());
			valuePredicatesList.add(stateKind);
		}
		if (valueQuery.getValueType() != null) {
			Predicate valueType = criteriaBuilder.equal(value.<String>get("lsType"), valueQuery.getValueType());
			valuePredicatesList.add(valueType);
		}
		if (valueQuery.getValueKind() != null) {
			Predicate valueKind = criteriaBuilder.equal(value.<String>get("lsKind"), valueQuery.getValueKind());
			valuePredicatesList.add(valueKind);
		}
		if (valueQuery.getValue() != null) {
			if (valueQuery.getValueType() == null) {
				logger.error("valueType must be specified if value is specified!");
				throw new Exception("valueType must be specified if value is specified!");
			} else if (valueQuery.getValueType().equalsIgnoreCase("dateValue")) {
				Calendar cal = Calendar.getInstance(); // locale-specific
				boolean parsedTime = false;
				if (SimpleUtil.isNumeric(valueQuery.getValue())) {
					cal.setTimeInMillis(Long.valueOf(valueQuery.getValue()));
					parsedTime = true;
				} else {
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
						cal.setTime(sdf.parse(valueQuery.getValue()));
						parsedTime = true;
					} catch (Exception e) {
						logger.warn("Failed to parse date in LsThing generic query for value", e);
					}
				}
				if (parsedTime) {
					if (valueQuery.getOperator() != null && valueQuery.getOperator().equals(">")) {
						Predicate valueGreaterThan = criteriaBuilder.greaterThan(value.<Date>get("dateValue"),
								new Date(cal.getTimeInMillis()));
						valuePredicatesList.add(valueGreaterThan);
					} else if (valueQuery.getOperator() != null && valueQuery.getOperator().equals("<")) {
						Predicate valueLessThan = criteriaBuilder.lessThan(value.<Date>get("dateValue"),
								new Date(cal.getTimeInMillis()));
						valuePredicatesList.add(valueLessThan);
					} else {
						String postgresTimeUnit = "day";
						Expression<Date> dateTruncExpr = criteriaBuilder.function("date_trunc", Date.class,
								criteriaBuilder.literal(postgresTimeUnit), value.<Date>get("dateValue"));

						cal.set(Calendar.HOUR_OF_DAY, 0);
						cal.set(Calendar.MINUTE, 0);
						cal.set(Calendar.SECOND, 0);
						cal.set(Calendar.MILLISECOND, 0);
						long time = cal.getTimeInMillis();
						Date queryDate = new Date(time);
						Predicate valueLike = criteriaBuilder.equal(dateTruncExpr, queryDate);
						valuePredicatesList.add(valueLike);
					}
				}
			} else if (valueQuery.getValueType().equalsIgnoreCase("numericValue")) {
				if (valueQuery.getOperator() != null && valueQuery.getOperator().equals(">")) {
					Predicate valueGreaterThan = criteriaBuilder.greaterThan(value.<BigDecimal>get("numericValue"),
							new BigDecimal(valueQuery.getValue()));
					valuePredicatesList.add(valueGreaterThan);
				} else if (valueQuery.getOperator() != null && valueQuery.getOperator().equals("<=")) {
					Predicate valueGreaterThan = criteriaBuilder.greaterThanOrEqualTo(
							value.<BigDecimal>get("numericValue"), new BigDecimal(valueQuery.getValue()));
					valuePredicatesList.add(valueGreaterThan);
				} else if (valueQuery.getOperator() != null && valueQuery.getOperator().equals("<")) {
					Predicate valueLessThan = criteriaBuilder.lessThan(value.<BigDecimal>get("numericValue"),
							new BigDecimal(valueQuery.getValue()));
					valuePredicatesList.add(valueLessThan);
				} else if (valueQuery.getOperator() != null && valueQuery.getOperator().equals("<=")) {
					Predicate valueLessThan = criteriaBuilder.lessThanOrEqualTo(value.<BigDecimal>get("numericValue"),
							new BigDecimal(valueQuery.getValue()));
					valuePredicatesList.add(valueLessThan);
				} else {
					Predicate valueEquals = criteriaBuilder.equal(value.<BigDecimal>get("numericValue"),
							new BigDecimal(valueQuery.getValue()));
					valuePredicatesList.add(valueEquals);
				}
			} else {
				// string value types: stringValue, codeValue, fileValue, clobValue
				if (valueQuery.getOperator() != null && valueQuery.getOperator().equals("=")) {
					Predicate valueEquals = criteriaBuilder.equal(value.<String>get(valueQuery.getValueType()),
							valueQuery.getValue());
					valuePredicatesList.add(valueEquals);
				} else if (valueQuery.getOperator() != null && valueQuery.getOperator().equals("!=")) {
					Predicate valueNotEquals = criteriaBuilder.notEqual(value.<String>get(valueQuery.getValueType()),
							valueQuery.getValue());
					valuePredicatesList.add(valueNotEquals);
				} else if (valueQuery.getOperator() != null && valueQuery.getOperator().equals("~")) {
					Predicate valueLike = criteriaBuilder.like(value.<String>get(valueQuery.getValueType()),
							valueQuery.getValue());
					valuePredicatesList.add(valueLike);
				} else if (valueQuery.getOperator() != null && valueQuery.getOperator().equals("!~")) {
					Predicate valueNotLike = criteriaBuilder.notLike(value.<String>get(valueQuery.getValueType()),
							valueQuery.getValue());
					valuePredicatesList.add(valueNotLike);
				} else if (valueQuery.getOperator() != null && valueQuery.getOperator().equals(">")) {
					Predicate valueGreaterThan = criteriaBuilder
							.greaterThan(value.<String>get(valueQuery.getValueType()), valueQuery.getValue());
					valuePredicatesList.add(valueGreaterThan);
				} else if (valueQuery.getOperator() != null && valueQuery.getOperator().equals(">=")) {
					Predicate valueGreaterThan = criteriaBuilder
							.greaterThanOrEqualTo(value.<String>get(valueQuery.getValueType()), valueQuery.getValue());
					valuePredicatesList.add(valueGreaterThan);
				} else if (valueQuery.getOperator() != null && valueQuery.getOperator().equals("<")) {
					Predicate valueLessThan = criteriaBuilder.lessThan(value.<String>get(valueQuery.getValueType()),
							valueQuery.getValue());
					valuePredicatesList.add(valueLessThan);
				} else if (valueQuery.getOperator() != null && valueQuery.getOperator().equals("<=")) {
					Predicate valueLessThan = criteriaBuilder
							.lessThanOrEqualTo(value.<String>get(valueQuery.getValueType()), valueQuery.getValue());
					valuePredicatesList.add(valueLessThan);
				} else {
					Predicate valueLike = criteriaBuilder.like(value.<String>get(valueQuery.getValueType()),
							'%' + valueQuery.getValue() + '%');
					valuePredicatesList.add(valueLike);
				}
			}
		}
		// gather predicates with AND
		Predicate[] valuePredicates = new Predicate[0];
		valuePredicates = valuePredicatesList.toArray(valuePredicates);
		Predicate valuePredicate = criteriaBuilder.and(valuePredicates);
		return valuePredicate;
	}

	@Override
	public Collection<Long> searchContainerIdsByBrowserQueryDTO(ContainerBrowserQueryDTO query) throws Exception {
		List<Long> containerIdList = new ArrayList<Long>();
		EntityManager em = Container.entityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
		Root<Container> thing = criteria.from(Container.class);
		List<Predicate> metaPredicateList = new ArrayList<Predicate>();
		// split query string into terms
		String queryString = query.getQueryString().replaceAll("\\*", "%");
		List<String> splitQuery = SimpleUtil.splitSearchString(queryString);
		logger.debug("Number of search terms: " + splitQuery.size());
		// for each search term, construct a queryDTO with that term filled in every
		// search position of the passed in queryDTO
		for (String searchTerm : splitQuery) {
			ContainerQueryDTO queryDTO = new ContainerQueryDTO(query.getQueryDTO());
			if (queryDTO.getFirstInteractions() != null) {
				for (ItxQueryDTO itx : queryDTO.getFirstInteractions()) {
					itx.setThingLabelText(searchTerm);
				}
			}
			if (queryDTO.getSecondInteractions() != null) {
				for (ItxQueryDTO itx : queryDTO.getSecondInteractions()) {
					itx.setThingLabelText(searchTerm);
				}
			}
			if (queryDTO.getSubjects() != null) {
				for (ItxQueryDTO itx : queryDTO.getSubjects()) {
					itx.setThingLabelText(searchTerm);
				}
			}
			if (queryDTO.getValues() != null) {
				for (ValueQueryDTO value : queryDTO.getValues()) {
					value.setValue(searchTerm);
				}
			}
			if (queryDTO.getLabels() != null) {
				for (LabelQueryDTO label : queryDTO.getLabels()) {
					label.setLabelText(searchTerm);
				}
			}
			// get a list of predicates for that queryDTO, OR them all together, then add to
			// the meta list
			List<Predicate> predicateList = buildPredicatesForQueryDTO(criteriaBuilder, criteria, thing, queryDTO);
			Predicate[] predicates = new Predicate[0];
			predicates = predicateList.toArray(predicates);
			Predicate searchTermPredicate = criteriaBuilder.or(predicates);
			metaPredicateList.add(searchTermPredicate);
		}
		// add in thingType and thingKind as required at top level
		if (query.getQueryDTO().getLsType() != null) {
			Predicate thingType = criteriaBuilder.equal(thing.<String>get("lsType"), query.getQueryDTO().getLsType());
			metaPredicateList.add(thingType);
		}
		if (query.getQueryDTO().getLsKind() != null) {
			Predicate thingKind = criteriaBuilder.equal(thing.<String>get("lsKind"), query.getQueryDTO().getLsKind());
			metaPredicateList.add(thingKind);
		}
		// gather the predicates for each search term, and AND them all together
		Predicate[] metaPredicates = new Predicate[0];
		metaPredicates = metaPredicateList.toArray(metaPredicates);
		criteria.where(criteriaBuilder.and(metaPredicates));
		TypedQuery<Long> q = em.createQuery(criteria);
		logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
		containerIdList = q.getResultList();
		logger.debug("Found " + containerIdList.size() + " results.");
		return containerIdList;
	}

	@Override
	public Collection<CodeTableDTO> convertToCodeTables(Collection<Container> containers) {
		Collection<CodeTableDTO> codeTables = new ArrayList<CodeTableDTO>();
		for (Container container : containers) {
			CodeTableDTO codeTable = new CodeTableDTO();
			codeTable.setId(container.getId());
			codeTable.setCode(container.getCodeName());
			codeTable.setName(pickBestLabel(container));
			codeTable.setIgnored(container.isIgnored());
			codeTables.add(codeTable);
		}

		return codeTables;
	}

	@Override
	public Collection<CodeTableDTO> convertToCodeTables(Collection<Container> containers, String labelType) {
		Collection<CodeTableDTO> codeTables = new ArrayList<CodeTableDTO>();
		for (Container container : containers) {
			CodeTableDTO codeTable = new CodeTableDTO();
			codeTable.setId(container.getId());
			codeTable.setCode(container.getCodeName());
			codeTable.setName(pickBestLabel(container, labelType));
			codeTable.setIgnored(container.isIgnored());
			codeTables.add(codeTable);
		}

		return codeTables;
	}

	private String pickBestLabel(Container container, String labelType) {
		Collection<ContainerLabel> labels = container.getLsLabels();
		if (labels.isEmpty())
			return null;
		Collection<ContainerLabel> filteredLabels = new ArrayList<ContainerLabel>();
		for (ContainerLabel label : labels) {
			if (label.getLsType().equals(labelType)) {
				filteredLabels.add(label);
			}
		}
		return ContainerLabel.pickBestLabel(filteredLabels).getLabelText();
	}

	@Override
	public Collection<ContainerBatchCodeDTO> getContainerDTOsByBatchCodes(List<String> batchCodes) {
		EntityManager em = Container.entityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ContainerBatchCodeDTO> cq = cb.createQuery(ContainerBatchCodeDTO.class);
		Root<Container> container = cq.from(Container.class);
		Join<Container, ItxContainerContainer> secondItx = container.join("secondContainers");
		Join<Container, ItxContainerContainer> well = secondItx.join("secondContainer");
		Join<Container, ContainerState> wellContentState = well.join("lsStates");
		Join<Container, ContainerState> batchCodeValue = wellContentState.join("lsValues");
		Join<Container, ContainerLabel> containerBarcode = container.join("lsLabels");
		Join<Container, ContainerLabel> wellLabel = well.join("lsLabels");

		Predicate[] predicates = new Predicate[0];
		List<Predicate> predicateList = new ArrayList<Predicate>();
		predicateList.add(cb.not(container.<Boolean>get("ignored")));
		predicateList.add(cb.not(well.<Boolean>get("ignored")));
		predicateList.add(cb.equal(secondItx.<String>get("lsType"), "has member"));
		predicateList.add(cb.equal(secondItx.<String>get("lsKind"), "container_well"));
		predicateList.add(cb.not(secondItx.<Boolean>get("ignored")));
		predicateList.add(cb.equal(wellContentState.get("lsType"), "status"));
		predicateList.add(cb.equal(wellContentState.get("lsKind"), "content"));
		predicateList.add(cb.not(wellContentState.<Boolean>get("ignored")));
		predicateList.add(cb.equal(batchCodeValue.get("lsType"), "codeValue"));
		predicateList.add(cb.equal(batchCodeValue.get("lsKind"), "batch code"));
		predicateList.add(cb.not(batchCodeValue.<Boolean>get("ignored")));
		predicateList.add(cb.equal(containerBarcode.<String>get("lsType"), "barcode"));
		predicateList.add(cb.equal(containerBarcode.<String>get("lsKind"), "barcode"));
		predicateList.add(cb.not(containerBarcode.<Boolean>get("ignored")));
		predicateList.add(cb.equal(wellLabel.<String>get("lsType"), "name"));
		predicateList.add(cb.equal(wellLabel.<String>get("lsKind"), "well name"));
		predicateList.add(cb.not(wellLabel.<Boolean>get("ignored")));

		Expression<String> batchCode = batchCodeValue.<String>get("codeValue");
		predicateList.add(batchCode.in(batchCodes));

		predicates = predicateList.toArray(predicates);
		cq.where(cb.and(predicates));
		cq.multiselect(batchCodeValue.<String>get("codeValue"), container.<String>get("codeName"),
				containerBarcode.<String>get("labelText"), well.<String>get("codeName"),
				wellLabel.<String>get("labelText"));
		TypedQuery<ContainerBatchCodeDTO> q = em.createQuery(cq);
		return q.getResultList();
	}

	@Override
	@Transactional
	public void logicalDeleteContainerArray(Collection<Container> foundContainers) {
		for (Container container : foundContainers) {
			container.logicalDelete();
		}
	}

	@Override
	@Transactional
	public void logicalDeleteContainerCodesArray(List<String> containerCodes) {
		for(String containerCode : containerCodes) {
			Container container = Container.findContainerByCodeNameEquals(containerCode);
			container.logicalDelete();
		}
	}

	@Override
	public List<ContainerLocationTreeDTO> getLocationTreeByRootLabel(String rootLabel, Boolean withContainers)
			throws SQLException {
		return getLocationTreeDTO(rootLabel, null, null, withContainers);
	}

	@Override
	public List<ContainerLocationTreeDTO> getLocationCodeByLabelBreadcrumbByRecursiveQuery(String rootLabel,
			List<String> breadcrumbList) throws SQLException {
		return getLocationTreeDTO(rootLabel, null, breadcrumbList, true);
	}

	@Override
	public List<ContainerLocationTreeDTO> getLocationTreeByRootCodeName(String rootCodeName, Boolean withContainers)
			throws SQLException {
		return getLocationTreeDTO(null, rootCodeName, null, withContainers);
	}

	@Override
	@Transactional
	public List<ContainerLocationTreeDTO> getLocationTreeDTO(String rootLabel, String rootCodeName,
			List<String> breadcrumbList, Boolean withContainers) throws SQLException {
		boolean withRootLabel = (rootLabel != null && rootLabel.length() > 0);
		boolean withRootCodeName = (rootCodeName != null && rootCodeName.length() > 0);
		boolean withBreadcrumbList = (breadcrumbList != null && breadcrumbList.size() > 0);
		if (withContainers == null) {
			withContainers = false;
		}
		EntityManager em = Container.entityManager();
		String queryString = null;
		DbType dbType = SimpleUtil.getDatabaseType(dataSource.getConnection().getMetaData());
		if (dbType == DbType.POSTGRES) {
			queryString = "WITH RECURSIVE t1 ( \n"
					+ "    code_name, \n"
					+ "    parent_code_name, \n"
					+ "    label_text, \n"
					+ "    lvl, \n"
					+ "    root_code_name, \n"
					+ "    code_name_bread_crumb, \n"
					+ "    label_text_bread_crumb \n"
					+ ") AS ( \n"
					+ "  -- Anchor member. \n"
					+ "    SELECT \n"
					+ "        code_name, \n"
					+ "        CAST ( NULL AS text) AS parent_code_name, \n"
					+ "        label_text, \n"
					+ "        1 AS lvl, \n"
					+ "        code_name AS root_code_name, \n"
					+ "        CAST ( code_name AS text) AS code_name_bread_crumb, \n"
					+ "        CAST ( label_text AS text) AS label_text_bread_crumb, \n"
					+ "        ls_type, \n"
					+ "        ls_kind \n"
					+ "    FROM \n"
					+ "        (SELECT \n"
					+ "        c.code_name, \n"
					+ "        cl.label_text, \n"
					+ "        c.ls_type ,\n"
					+ "        c.ls_kind  \n"
					+ "    FROM \n"
					+ "        container c \n"
					+ "        JOIN container_label cl ON \n"
					+ "            c.id = cl.container_id \n"
					+ "        AND \n"
					+ "            cl.ignored = '0' \n"
					+ "        AND \n"
					+ "            cl.deleted = '0' \n"
					+ "    WHERE \n"
					+ "            c.deleted = '0' \n";
			if (withRootLabel) {
				queryString += "        AND \n"
						+ "            cl.label_text LIKE :rootLabel ";
			}
			if (withRootCodeName) {
				queryString += "        AND \n"
						+ "            c.code_name = :rootCodeName ";
			}
			queryString += " ) as anchord \n"
					+ "    UNION ALL \n"
					+ "  -- Recursive member. \n"
					+ "     SELECT \n"
					+ "        interactions.code_name, \n"
					+ "        interactions.parent_code_name, \n"
					+ "        interactions.label_text, \n"
					+ "        lvl + 1, \n"
					+ "        t1.root_code_name, \n"
					+ "        t1.code_name_bread_crumb \n"
					+ "         || '>' \n"
					+ "         || interactions.code_name AS code_name_bread_crumb, \n"
					+ "        t1.label_text_bread_crumb \n"
					+ "         || '>' \n"
					+ "         || interactions.label_text AS label_text_bread_crumb, \n"
					+ "        interactions.ls_type, \n"
					+ "         interactions.ls_kind "
					+ "    FROM \n"
					+ "        (SELECT c1.code_name AS code_name, \n"
					+ "        cl1.label_text AS label_text, \n"
					+ "        c2.code_name AS parent_code_name, \n"
					+ "    	   c1.ls_type, \n"
					+ "    	   c1.ls_kind \n"
					+ "    FROM \n"
					+ "        itx_container_container itx \n"
					+ "        JOIN container c1 ON \n"
					+ "            itx.first_container_id = c1.id \n"
					+ "        AND \n"
					+ "            c1.ignored = '0' \n"
					+ "        AND \n"
					+ "            c1.deleted = '0' \n"
					+ "        JOIN container c2 ON \n"
					+ "            itx.second_container_id = c2.id \n"
					+ "        AND \n"
					+ "            c2.ignored = '0' \n"
					+ "        AND \n"
					+ "            c2.deleted = '0' \n"
					+ "        JOIN container_label cl1 ON \n"
					+ "            c1.id = cl1.container_id \n"
					+ "        AND \n"
					+ "            cl1.ignored = '0' \n"
					+ "        AND \n"
					+ "            cl1.deleted = '0' \n"
					+ "    WHERE \n"
					+ "            itx.ls_type = 'moved to' \n"
					+ "        AND \n"
					+ "            itx.ignored = '0' \n";
			if (!withContainers) {
				queryString += "       AND\n"
						+ "	    c1.ls_type = 'location'";
			}
			queryString += "        AND \n"
					+ "            itx.deleted = '0') as interactions, \n"
					+ "        t1 \n"
					+ "    WHERE \n"
					+ "        interactions.parent_code_name = t1.code_name \n"
					+ ") \n"
					+ "SELECT \n"
					+ "    code_name, \n"
					+ "    parent_code_name, \n"
					+ "    label_text, \n"
					+ "    rpad( \n"
					+ "        '.', \n"
					+ "        (lvl - 1) * 2, \n"
					+ "        '.' \n"
					+ "    ) \n"
					+ "     || code_name AS code_tree, \n"
					+ "    rpad( \n"
					+ "        '.', \n"
					+ "        (lvl - 1) * 2, \n"
					+ "        '.' \n"
					+ "    ) \n"
					+ "     || label_text AS label_tree, \n"
					+ "    lvl, \n"
					+ "    root_code_name, \n"
					+ "    code_name_bread_crumb, \n"
					+ "    label_text_bread_crumb, \n"
					+ "    ls_type, \n"
					+ "    ls_kind \n"
					+ "FROM \n"
					+ "    t1 ";
			if (withBreadcrumbList) {
				queryString += "WHERE label_text_bread_crumb IN :breadcrumbList \n";
			}
			queryString += ";";
		} else if (dbType == DbType.ORACLE) {
			queryString = "WITH interactions AS ( \n"
					+ "    SELECT c1.code_name AS code_name, \n"
					+ "        cl1.label_text AS label_text, \n"
					+ "        c2.code_name AS parent_code_name, \n"
					+ "        c1.ls_type, \n"
					+ "        c1.ls_kind \n"
					+ "    FROM \n"
					+ "        itx_container_container itx \n"
					+ "        JOIN container c1 ON \n"
					+ "            itx.first_container_id = c1.id \n"
					+ "        AND \n"
					+ "            c1.ignored = 0 \n"
					+ "        AND \n"
					+ "            c1.deleted = 0 \n"
					+ "        JOIN container c2 ON \n"
					+ "            itx.second_container_id = c2.id \n"
					+ "        AND \n"
					+ "            c2.ignored = 0 \n"
					+ "        AND \n"
					+ "            c2.deleted = 0 \n"
					+ "        JOIN container_label cl1 ON \n"
					+ "            c1.id = cl1.container_id \n"
					+ "        AND \n"
					+ "            cl1.ignored = 0 \n"
					+ "        AND \n"
					+ "            cl1.deleted = 0 \n"
					+ "    WHERE \n"
					+ "            itx.ls_type = 'moved to' \n"
					+ "        AND \n"
					+ "            itx.ignored = 0 \n"
					+ "        AND \n"
					+ "            itx.deleted = 0 \n";
			if (!withContainers) {
				queryString += "       AND\n"
						+ "	    c1.ls_type = 'location'";
			}
			queryString += "),anchors AS ( \n"
					+ "    SELECT \n"
					+ "        c.code_name, \n"
					+ "        cl.label_text, \n"
					+ "          c.ls_type, \n"
					+ "          c.ls_kind \n"
					+ "    FROM \n"
					+ "        container c \n"
					+ "        JOIN container_label cl ON \n"
					+ "            c.id = cl.container_id \n"
					+ "        AND \n"
					+ "            cl.ignored = 0 \n"
					+ "        AND \n"
					+ "            cl.deleted = 0 \n"
					+ "    WHERE \n"
					+ "            c.deleted = 0 \n";
			if (withRootLabel) {
				queryString += "        AND \n"
						+ "            cl.label_text LIKE :rootLabel ";
			}
			if (withRootCodeName) {
				queryString += "        AND \n"
						+ "            c.code_name = :rootCodeName ";
			}
			queryString += "),t1 ( \n"
					+ "    code_name, \n"
					+ "    parent_code_name, \n"
					+ "    label_text, \n"
					+ "    lvl, \n"
					+ "    root_code_name, \n"
					+ "    code_name_bread_crumb, \n"
					+ "    label_text_bread_crumb, \n"
					+ "    ls_type, \n"
					+ "    ls_kind \n"
					+ ") AS ( \n"
					+ "  -- Anchor member. \n"
					+ "    SELECT \n"
					+ "        code_name, \n"
					+ "        NULL AS parent_code_name, \n"
					+ "        label_text, \n"
					+ "        1 AS lvl, \n"
					+ "        code_name AS root_code_name, \n"
					+ "        code_name AS code_name_bread_crumb, \n"
					+ "        label_text AS label_text_bread_crumb, \n"
					+ "        ls_type, \n"
					+ "        ls_kind \n"
					+ "    FROM \n"
					+ "        anchors \n"
					+ "    UNION ALL \n"
					+ "  -- Recursive member. \n"
					+ "     SELECT \n"
					+ "        interactions.code_name, \n"
					+ "        interactions.parent_code_name, \n"
					+ "        interactions.label_text, \n"
					+ "        lvl + 1, \n"
					+ "        t1.root_code_name, \n"
					+ "        t1.code_name_bread_crumb \n"
					+ "         || '>' \n"
					+ "         || interactions.code_name AS code_name_bread_crumb, \n"
					+ "        t1.label_text_bread_crumb \n"
					+ "         || '>' \n"
					+ "         || interactions.label_text AS label_text_bread_crumb, \n"
					+ "        interactions.ls_type, \n"
					+ "        interactions.ls_kind \n"
					+ "    FROM \n"
					+ "        interactions, \n"
					+ "        t1 \n"
					+ "    WHERE \n"
					+ "        interactions.parent_code_name = t1.code_name \n"
					+ ") \n"
					+ "    SEARCH DEPTH FIRST BY code_name SET order1 \n"
					+ "    CYCLE code_name SET cycle TO 1 DEFAULT 0 \n"
					+ "SELECT \n"
					+ "    code_name, \n"
					+ "    parent_code_name, \n"
					+ "    label_text, \n"
					+ "    rpad( \n"
					+ "        '.', \n"
					+ "        (lvl - 1) * 2, \n"
					+ "        '.' \n"
					+ "    ) \n"
					+ "     || code_name AS code_tree, \n"
					+ "    rpad( \n"
					+ "        '.', \n"
					+ "        (lvl - 1) * 2, \n"
					+ "        '.' \n"
					+ "    ) \n"
					+ "     || label_text AS label_tree, \n"
					+ "    lvl, \n"
					+ "    root_code_name, \n"
					+ "    code_name_bread_crumb, \n"
					+ "    label_text_bread_crumb, \n"
					+ "    ls_type, \n"
					+ "    ls_kind, \n"
					+ "    cycle \n"
					+ "FROM \n"
					+ "    t1 ";
			if (withBreadcrumbList) {
				queryString += "WHERE label_text_bread_crumb IN :breadcrumbList \n";
			}
		}
		logger.debug(queryString);
		Query q = em.createNativeQuery(queryString, "ContainerLocationTreeDTOResult");
		if (withRootLabel) {
			q.setParameter("rootLabel", rootLabel);
		}
		if (withRootCodeName) {
			q.setParameter("rootCodeName", rootCodeName);
		}
		if (withBreadcrumbList) {
			q.setParameter("breadcrumbList", breadcrumbList);
		}
		List<ContainerLocationTreeDTO> results = q.getResultList();
		return results;
	}

}
