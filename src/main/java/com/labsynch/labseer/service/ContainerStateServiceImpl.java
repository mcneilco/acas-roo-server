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

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

@Service
@Transactional
public class ContainerStateServiceImpl implements ContainerStateService {

	private static final Logger logger = LoggerFactory.getLogger(ContainerStateServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	public Collection<ContainerState> ignoreAllContainerStates(Collection<ContainerState> containerStates) {
		//mark ContainerStates and values as ignore 
		int i = 0;
		int j = 0;
		int batchSize = propertiesUtilService.getBatchSize();
		Collection<ContainerState> contianerStateSet = new HashSet<ContainerState>();
		for (ContainerState queryContainerState : containerStates){
			ContainerState containerState = ContainerState.findContainerState(queryContainerState.getId());			
			for(ContainerValue containerValue : ContainerValue.findContainerValuesByLsState(containerState).getResultList()){
				containerValue.setIgnored(true);
				containerValue.merge();
				if ( i % batchSize == 0 ) { // same as the JDBC batch size
					containerValue.flush();
					containerValue.clear();
				}
				i++;
			}
			containerState.setIgnored(true);
			containerState.merge();
			if ( j % batchSize == 0 ) { // same as the JDBC batch size
				containerState.flush();
				containerState.clear();
			}
			j++;
			contianerStateSet.add(ContainerState.findContainerState(containerState.getId()));
		}

		return(contianerStateSet);

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
			for (Container container : Container.fromJsonArrayToContainers(br)){
					List<ContainerState> containerStates = ContainerState.findContainerStatesByContainerAndLsKindEqualsAndIgnoredNot(Container.findContainer(container.getId()), lsKind, ignored).getResultList();
					logger.debug("number of containerStates found: " + containerStates.size());
					for (ContainerState containerState : containerStates) {
						uplog = new UpdateLog();
						uplog.setThing(containerState.getId());
						uplog.setLsTransaction(lst.getId());
						uplog.setUpdateAction("ignore");
						uplog.setComments("mark states to ignore");
						uplog.setRecordedDate(new Date());
						uplog.persist();
						if ( i % batchSize == 0 ) { // same as the JDBC batch size
							uplog.flush();
							uplog.clear();
						}
						i++;
					}						
			}
			int results = ContainerState.ignoreStates(lst.getId());
			logger.debug("number of states marked to ignore: " + results);
		} catch (Exception e){
			logger.error("ERROR: " + e);
			throw new Exception(e.toString());
		}
		return lst;
	}

	@Override
	public ContainerState updateContainerState(ContainerState containerState) {
		containerState.setVersion(ContainerState.findContainerState(containerState.getId()).getVersion());
		containerState.merge();
		return containerState;
	}

	@Override
	public Collection<ContainerState> updateContainerStates(
			Collection<ContainerState> containerStates) {
		for (ContainerState containerState : containerStates){
			containerState = updateContainerState(containerState);
		}
		return null;
	}

	@Override
	public ContainerState saveContainerState(ContainerState containerState) {
		containerState.setContainer(Container.findContainer(containerState.getContainer().getId()));		
		containerState.persist();
		return containerState;
	}

	@Override
	public Collection<ContainerState> saveContainerStates(
			Collection<ContainerState> containerStates) {
		Collection<ContainerState> savedContainerStates = new ArrayList<ContainerState>();
		for (ContainerState containerState: containerStates) {
			ContainerState savedContainerState = saveContainerState(containerState);
			savedContainerStates.add(savedContainerState);
		}
		return savedContainerStates;
	}
	
	@Override
	public ContainerState getContainerState(String idOrCodeName,
			String stateType, String stateKind) {
		ContainerState state = null;
		try{
			Collection<ContainerState> states = getContainerStates(idOrCodeName, stateType, stateKind);
			state = states.iterator().next();
		}catch (Exception e){
			logger.error("Caught error "+e.toString()+" trying to find a state.",e);
			state = null;
		}
		return state;
	}

	@Override
	public Collection<ContainerStatePathDTO> getContainerStates(
			Collection<GenericStatePathRequest> genericRequests) {
		Collection<ContainerStatePathDTO> results = new ArrayList<ContainerStatePathDTO>();
		for (GenericStatePathRequest request : genericRequests){
			ContainerStatePathDTO result = new ContainerStatePathDTO();
			result.setIdOrCodeName(request.getIdOrCodeName());
			result.setStateType(request.getStateType());
			result.setStateKind(request.getStateKind());
			result.setStates(getContainerStates(request.getIdOrCodeName(), request.getStateType(), request.getStateKind()));
			results.add(result);
		}
		return results;
	}
	
	private Collection<ContainerState> getContainerStates(String idOrCodeName, String stateType, String stateKind){
		if (SimpleUtil.isNumeric(idOrCodeName)){
			Long id = Long.valueOf(idOrCodeName);
			return ContainerState.findContainerStatesByContainerIDAndStateTypeKind(id, stateType, stateKind).getResultList();
		}else{
			return ContainerState.findContainerStatesByContainerCodeNameAndStateTypeKind(idOrCodeName, stateType, stateKind).getResultList();
		}
	}
	
	@Override
	public List<ContainerState> getContainerStatesByContainerIdAndStateTypeKind(Long containerId, String stateType, 
			String stateKind) {	
		
		List<ContainerState> containerStates = ContainerState.findContainerStatesByContainerIDAndStateTypeKind(containerId, stateType, stateKind).getResultList();

		return containerStates;
	}
	
	@Override
	public ContainerState createContainerStateByContainerIdAndStateTypeKind(Long containerId, String stateType, String stateKind) {
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
	public ContainerState createContainerStateByContainerIdAndStateTypeKindAndRecordedBy(Long containerId, String stateType, String stateKind, String recordedBy) {
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
			ContainerValueRequestDTO query) throws Exception {
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
		
		if (query.getStateType() != null){
			Predicate stateType = criteriaBuilder.equal(state.<String>get("lsType"),query.getStateType());
			predicateList.add(stateType);
		}
		if (query.getStateKind() != null){
			Predicate stateKind = criteriaBuilder.equal(state.<String>get("lsKind"),query.getStateKind());
			predicateList.add(stateKind);
		}
		if (query.getValueType() != null){
			Predicate valueType = criteriaBuilder.equal(value.<String>get("lsType"),query.getValueType());
			predicateList.add(valueType);
		}
		if (query.getValueKind() != null){
			Predicate valueKind = criteriaBuilder.equal(value.<String>get("lsKind"),query.getValueKind());
			predicateList.add(valueKind);
		}
		if (query.getValue() != null){
			if (query.getValueType() == null){
				logger.error("valueType must be specified if value is specified!");
				throw new Exception("valueType must be specified if value is specified!");
			}else if (query.getValueType().equalsIgnoreCase("dateValue")){
				String postgresTimeUnit = "day";
				Expression<Date> dateTruncExpr = criteriaBuilder.function("date_trunc", Date.class, criteriaBuilder.literal(postgresTimeUnit), value.<Date>get("dateValue"));
				Calendar cal = Calendar.getInstance(); // locale-specific
				boolean parsedTime = false;
				if (SimpleUtil.isNumeric(query.getValue())){
					cal.setTimeInMillis(Long.valueOf(query.getValue()));
					parsedTime = true;
				}else{
					try{
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
						cal.setTime(sdf.parse(query.getValue()));
						parsedTime = true;
					}catch (Exception e){
						logger.warn("Failed to parse date in LsThing generic query for value",e);
					}
				}
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				long time = cal.getTimeInMillis();
				Date queryDate = new Date(time);
				Predicate valueLike = criteriaBuilder.equal(dateTruncExpr, queryDate);
				if (parsedTime) predicateList.add(valueLike);
			}else{
				//only works with string value types: stringValue, codeValue, fileValue, clobValue
				Predicate valueLike = criteriaBuilder.like(value.<String>get(query.getValueType()), '%' + query.getValue() + '%');
				predicateList.add(valueLike);
			}
		}
		
		if (query.getContainerType() != null){
			Predicate thingType = criteriaBuilder.equal(container.<String>get("lsType"), query.getContainerType());
			predicateList.add(thingType);
		}
		if (query.getContainerKind() != null){
			Predicate thingKind = criteriaBuilder.equal(container.<String>get("lsKind"), query.getContainerKind());
			predicateList.add(thingKind);
		}
		
		//gather predicates with AND
		Predicate[] predicates = new Predicate[0];
		predicates = predicateList.toArray(predicates);
		criteria.where(criteriaBuilder.and(predicates));
		TypedQuery<ContainerState> q = em.createQuery(criteria);
		logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
		return q.getResultList();
	}



}
