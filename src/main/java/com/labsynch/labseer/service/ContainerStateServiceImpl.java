package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.Container;
import com.labsynch.labseer.domain.ContainerState;
import com.labsynch.labseer.domain.ContainerValue;
import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.domain.Subject;
import com.labsynch.labseer.domain.SubjectState;
import com.labsynch.labseer.domain.UpdateLog;
import com.labsynch.labseer.utils.PropertiesUtilService;

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
		for (ContainerState containerState: containerStates) {
			containerState = saveContainerState(containerState);
		}
		return containerStates;
	}



}
