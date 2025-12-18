package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ContainerValue;
import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.domain.UpdateLog;
import com.labsynch.labseer.dto.ContainerStatePathDTO;
import com.labsynch.labseer.dto.ContainerValueRequestDTO;
import com.labsynch.labseer.dto.GenericStatePathRequest;
import com.labsynch.labseer.utils.PropertiesUtilService;
import com.labsynch.labseer.utils.SimpleUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ContainerStateServiceImpl implements ContainerStateService {

	private static final Logger logger = LoggerFactory.getLogger(ContainerStateServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	public Collection<ContainerState> ignoreAllContainerStates(Collection<ContainerState> containerStates) {
		// mark ContainerStates and values as ignore
		int i = 0;
		int j = 0;
		int batchSize = propertiesUtilService.getBatchSize();
		Collection<ContainerState> contianerStateSet = new HashSet<ContainerState>();
		for (ContainerState queryContainerState : containerStates) {
			ContainerState containerState = ContainerState.findContainerState(queryContainerState.getId());
			for (ContainerValue containerValue : ContainerValue.findContainerValuesByLsState(containerState)
					.getResultList()) {
				containerValue.setIgnored(true);
				containerValue.merge();
				if (i % batchSize == 0) { // same as the JDBC batch size
					containerValue.flush();
					containerValue.clear();
				}
				i++;
			}
			containerState.setIgnored(true);
			containerState.merge();
			if (j % batchSize == 0) { // same as the JDBC batch size
				containerState.flush();
				containerState.clear();
			}
			j++;
			contianerStateSet.add(ContainerState.findContainerState(containerState.getId()));
		}

		return (contianerStateSet);

	}

	@Override
	public LsTransaction ignoreByContainer(String json, String lsKind) throws Exception {
		LsTransaction lst = new LsTransaction();
		lst.setComments("mark states to ignore");
		lst.setRecordedDate(new Date());
		lst.persist();

		int batchSize = propertiesUtilService.getBatchSize();
		int i = 0;
		try {
			StringReader sr = new StringReader(json);
			BufferedReader br = new BufferedReader(sr);
			UpdateLog uplog;
			boolean ignored = true;
			for (Container container : Container.fromJsonArrayToContainers(br)) {
				List<ContainerState> containerStates = ContainerState
						.findContainerStatesByContainerAndLsKindEqualsAndIgnoredNot(
								Container.findContainer(container.getId()), lsKind, ignored)
						.getResultList();
				logger.debug("number of containerStates found: " + containerStates.size());
				for (ContainerState containerState : containerStates) {
					uplog = new UpdateLog();
					uplog.setThing(containerState.getId());
					uplog.setLsTransaction(lst.getId());
					uplog.setUpdateAction("ignore");
					uplog.setComments("mark states to ignore");
					uplog.setRecordedDate(new Date());
					uplog.persist();
					if (i % batchSize == 0) { // same as the JDBC batch size
						uplog.flush();
						uplog.clear();
					}
					i++;
				}
			}
			int results = ContainerState.ignoreStates(lst.getId());
			logger.debug("number of states marked to ignore: " + results);
		} catch (Exception e) {
			logger.error("ERROR: " + e);
			throw new Exception(e.toString());
		}
		return lst;
	}

	@Transactional
	@Override
	public ContainerState updateContainerState(ContainerState containerState) {
		ContainerState updatedContainerState;
		if (containerState.getId() == null) {
			updatedContainerState = new ContainerState(containerState);
			updatedContainerState.setContainer(Container.findContainer(containerState.getContainer().getId()));
			updatedContainerState.persist();
			if (logger.isDebugEnabled())
				logger.debug("persisted new container state " + updatedContainerState.getId());

		} else {
			updatedContainerState = ContainerState.update(containerState);

			if (logger.isDebugEnabled())
				logger.debug("updated container state " + updatedContainerState.getId());

		}
		if (containerState.getLsValues() != null) {
			for (ContainerValue lsThingValue : containerState.getLsValues()) {
				if (lsThingValue.getLsState() == null)
					lsThingValue.setLsState(updatedContainerState);
				ContainerValue updatedContainerValue;
				if (lsThingValue.getId() == null) {
					updatedContainerValue = ContainerValue.create(lsThingValue);
					updatedContainerValue.setLsState(ContainerState.findContainerState(updatedContainerState.getId()));
					updatedContainerValue.persist();
					updatedContainerState.getLsValues().add(updatedContainerValue);
				} else {
					updatedContainerValue = ContainerValue.update(lsThingValue);
					if (logger.isDebugEnabled())
						logger.debug("updated container value " + updatedContainerValue.getId());
				}
				if (logger.isDebugEnabled())
					logger.debug("checking container " + updatedContainerValue.toJson());

			}
		} else {
			if (logger.isDebugEnabled())
				logger.debug("No container values to update");
		}
		updatedContainerState.flush();
		return ContainerState.findContainerState(updatedContainerState.getId());
	}

	@Transactional
	@Override
	public Collection<ContainerState> updateContainerStates(
			Collection<ContainerState> containerStates) {
		Collection<ContainerState> updatedStates = new ArrayList<ContainerState>();
		for (ContainerState containerState : containerStates) {
			ContainerState updatedContainerState = updateContainerState(containerState);
			updatedStates.add(updatedContainerState);
		}
		return updatedStates;
	}

	@Transactional
	@Override
	public ContainerState saveContainerState(ContainerState lsState) {
		if (logger.isDebugEnabled())
			logger.debug("incoming meta container: " + lsState.toJson() + "\n");
		ContainerState newLsState = new ContainerState(lsState);
		newLsState.setContainer(Container.findContainer(lsState.getContainer().getId()));
		if (logger.isDebugEnabled())
			logger.debug("here is the newLsState before save: " + newLsState.toJson());
		newLsState.persist();
		if (logger.isDebugEnabled())
			logger.debug("persisted the newLsState: " + newLsState.toJson());
		if (lsState.getLsValues() != null) {
			Set<ContainerValue> lsValues = new HashSet<ContainerValue>();
			for (ContainerValue containerValue : lsState.getLsValues()) {
				if (logger.isDebugEnabled())
					logger.debug("containerValue: " + containerValue.toJson());
				ContainerValue newContainerValue = ContainerValue.create(containerValue);
				newContainerValue.setLsState(newLsState);
				newContainerValue.persist();
				lsValues.add(newContainerValue);
				if (logger.isDebugEnabled())
					logger.debug("persisted the containerValue: " + newContainerValue.toJson());
			}
			newLsState.setLsValues(lsValues);
		} else {
			if (logger.isDebugEnabled())
				logger.debug("No container values to save");
		}
		newLsState.flush();
		return ContainerState.findContainerState(newLsState.getId());
	}

	@Transactional
	@Override
	public Collection<ContainerState> saveContainerStates(
			Collection<ContainerState> containerStates) {
		Collection<ContainerState> savedContainerStates = new ArrayList<ContainerState>();
		for (ContainerState containerState : containerStates) {
			ContainerState savedContainerState = saveContainerState(containerState);
			savedContainerStates.add(savedContainerState);
		}
		return savedContainerStates;
	}

	@Override
	public ContainerState getContainerState(String idOrCodeName,
			String stateType, String stateKind) {
		ContainerState state = null;
		try {
			Collection<ContainerState> states = getContainerStates(idOrCodeName, stateType, stateKind);
			state = states.iterator().next();
		} catch (Exception e) {
			logger.error("Caught error " + e.toString() + " trying to find a state.", e);
			state = null;
		}
		return state;
	}

	@Override
	public Collection<ContainerStatePathDTO> getContainerStates(
			Collection<GenericStatePathRequest> genericRequests) {
		Collection<ContainerStatePathDTO> results = new ArrayList<ContainerStatePathDTO>();
		for (GenericStatePathRequest request : genericRequests) {
			ContainerStatePathDTO result = new ContainerStatePathDTO();
			result.setIdOrCodeName(request.getIdOrCodeName());
			result.setStateType(request.getStateType());
			result.setStateKind(request.getStateKind());
			result.setStates(
					getContainerStates(request.getIdOrCodeName(), request.getStateType(), request.getStateKind()));
			results.add(result);
		}
		return results;
	}

	private Collection<ContainerState> getContainerStates(String idOrCodeName, String stateType, String stateKind) {
		if (SimpleUtil.isNumeric(idOrCodeName)) {
			Long id = Long.valueOf(idOrCodeName);
			return ContainerState.findContainerStatesByContainerIDAndStateTypeKind(id, stateType, stateKind)
					.getResultList();
		} else {
			return ContainerState
					.findContainerStatesByContainerCodeNameAndStateTypeKind(idOrCodeName, stateType, stateKind)
					.getResultList();
		}
	}

	@Override
	public List<ContainerState> getContainerStatesByContainerIdAndStateTypeKind(Long containerId, String stateType,
			String stateKind) {

		List<ContainerState> containerStates = ContainerState
				.findContainerStatesByContainerIDAndStateTypeKind(containerId, stateType, stateKind).getResultList();

		return containerStates;
	}

	@Override
	public ContainerState createContainerStateByContainerIdAndStateTypeKind(Long containerId, String stateType,
			String stateKind) {
		ContainerState containerState = new ContainerState();
		Container container = Container.findContainer(containerId);
		containerState.setContainer(container);
		containerState.setLsType(stateType);
		containerState.setLsKind(stateKind);
		containerState.setRecordedBy("default");
		containerState.persist();
		return containerState;
	}

	@Override
	public ContainerState createContainerStateByContainerIdAndStateTypeKindAndRecordedBy(Long containerId,
			String stateType, String stateKind, String recordedBy) {
		ContainerState containerState = new ContainerState();
		Container container = Container.findContainer(containerId);
		containerState.setContainer(container);
		containerState.setLsType(stateType);
		containerState.setLsKind(stateKind);
		containerState.setRecordedBy(recordedBy);
		containerState.persist();
		return containerState;
	}

	@Override
	public Collection<ContainerState> getContainerStatesByContainerValue(
			ContainerValueRequestDTO requestDTO, Boolean like, Boolean rightLike) throws Exception {
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
		if (requestDTO.getValue() == null)
			throw new Exception("Value must be specified");

		if (like != null && like) {
			String fullQuery = "SELECT DISTINCT containerState FROM ContainerState containerState "
					+ "JOIN FETCH containerState.container container "
					+ "JOIN FETCH containerState.lsValues containerValue "
					+ "LEFT JOIN FETCH container.lsLabels containerLabel "
					+ "LEFT JOIN FETCH containerState.lsValues otherContainerValues "
					+ "WHERE container.lsType = " + "'" + requestDTO.getContainerType() + "'" + " "
					+ "AND container.lsKind = " + "'" + requestDTO.getContainerKind() + "'" + " "
					+ "AND container.ignored <> true "
					+ "AND containerState.lsType = " + "'" + requestDTO.getStateType() + "'" + " "
					+ "AND containerState.lsKind = " + "'" + requestDTO.getStateKind() + "'" + " "
					+ "AND containerState.ignored <> true "
					+ "AND containerValue.lsType = " + "'" + requestDTO.getValueType() + "'" + " "
					+ "AND containerValue.lsKind = " + "'" + requestDTO.getValueKind() + "'" + " "
					+ "AND containerValue.ignored <> true "
					+ "AND otherContainerValues.id != containerValue.id ";
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
			if (logger.isDebugEnabled())
				logger.debug(fullQuery);
			EntityManager em = Container.entityManager();
			TypedQuery<ContainerState> q = em.createQuery(fullQuery, ContainerState.class);

			return q.getResultList();
		} else {
			EntityManager em = ContainerState.entityManager();
			CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			CriteriaQuery<ContainerState> criteria = criteriaBuilder.createQuery(ContainerState.class);
			Root<ContainerState> state = criteria.from(ContainerState.class);
			List<Predicate> predicateList = new ArrayList<Predicate>();
			criteria.distinct(true);

			Join<ContainerState, Container> container = state.join("container");
			Join<ContainerState, ContainerValue> value = state.join("lsValues");

			Predicate stateNotIgn = criteriaBuilder.isFalse(state.<Boolean>get("ignored"));
			Predicate valueNotIgn = criteriaBuilder.isFalse(value.<Boolean>get("ignored"));
			predicateList.add(stateNotIgn);
			predicateList.add(valueNotIgn);

			if (requestDTO.getStateType() != null) {
				Predicate stateType = criteriaBuilder.equal(state.<String>get("lsType"), requestDTO.getStateType());
				predicateList.add(stateType);
			}
			if (requestDTO.getStateKind() != null) {
				Predicate stateKind = criteriaBuilder.equal(state.<String>get("lsKind"), requestDTO.getStateKind());
				predicateList.add(stateKind);
			}
			if (requestDTO.getValueType() != null) {
				Predicate valueType = criteriaBuilder.equal(value.<String>get("lsType"), requestDTO.getValueType());
				predicateList.add(valueType);
			}
			if (requestDTO.getValueKind() != null) {
				Predicate valueKind = criteriaBuilder.equal(value.<String>get("lsKind"), requestDTO.getValueKind());
				predicateList.add(valueKind);
			}
			if (requestDTO.getValue() != null) {
				if (requestDTO.getValueType() == null) {
					logger.error("valueType must be specified if value is specified!");
					throw new Exception("valueType must be specified if value is specified!");
				} else if (requestDTO.getValueType().equalsIgnoreCase("dateValue")) {
					String postgresTimeUnit = "day";
					Expression<Date> dateTruncExpr = criteriaBuilder.function("date_trunc", Date.class,
							criteriaBuilder.literal(postgresTimeUnit), value.<Date>get("dateValue"));
					Calendar cal = Calendar.getInstance(); // locale-specific
					boolean parsedTime = false;
					if (SimpleUtil.isNumeric(requestDTO.getValue())) {
						cal.setTimeInMillis(Long.valueOf(requestDTO.getValue()));
						parsedTime = true;
					} else {
						try {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
							cal.setTime(sdf.parse(requestDTO.getValue()));
							parsedTime = true;
						} catch (Exception e) {
							logger.warn("Failed to parse date in LsThing generic query for value", e);
						}
					}
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);
					long time = cal.getTimeInMillis();
					Date queryDate = new Date(time);
					Predicate valueLike = criteriaBuilder.equal(dateTruncExpr, queryDate);
					if (parsedTime)
						predicateList.add(valueLike);
				} else {
					// only works with string value types: stringValue, codeValue, fileValue,
					// clobValue
					// because of big if/else we know we are in "equals" mode
					Predicate valueEquals = criteriaBuilder.equal(value.<String>get(requestDTO.getValueType()),
							requestDTO.getValue());
					predicateList.add(valueEquals);
				}
			}

			if (requestDTO.getContainerType() != null) {
				Predicate thingType = criteriaBuilder.equal(container.<String>get("lsType"),
						requestDTO.getContainerType());
				predicateList.add(thingType);
			}
			if (requestDTO.getContainerKind() != null) {
				Predicate thingKind = criteriaBuilder.equal(container.<String>get("lsKind"),
						requestDTO.getContainerKind());
				predicateList.add(thingKind);
			}

			// gather predicates with AND
			Predicate[] predicates = new Predicate[0];
			predicates = predicateList.toArray(predicates);
			criteria.where(criteriaBuilder.and(predicates));
			TypedQuery<ContainerState> q = em.createQuery(criteria);
			logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
			return q.getResultList();
		}
	}

}
