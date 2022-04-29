package com.labsynch.labseer.service;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

import com.labsynch.labseer.domain.ItxProtocolProtocol;
import com.labsynch.labseer.domain.ItxProtocolProtocolState;
import com.labsynch.labseer.domain.ItxProtocolProtocolValue;
import com.labsynch.labseer.domain.Protocol;
import com.labsynch.labseer.utils.PropertiesUtilService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ItxProtocolProtocolServiceImpl implements ItxProtocolProtocolService {

	private static final Logger logger = LoggerFactory.getLogger(ItxProtocolProtocolServiceImpl.class);

	@Autowired
	private PropertiesUtilService propertiesUtilService;

	@Override
	@Transactional
	public ItxProtocolProtocol saveLsItxProtocol(ItxProtocolProtocol itxProtocol) throws Exception {
		logger.debug("incoming meta itxProtocolProtocol: " + itxProtocol.toJson() + "\n");
		try {
			itxProtocol.setFirstProtocol(Protocol.findProtocol(itxProtocol.getFirstProtocol().getId()));
			itxProtocol.setSecondProtocol(Protocol.findProtocol(itxProtocol.getSecondProtocol().getId()));
		} catch (Exception e) {
			throw new Exception("One or both of the provided protocol do not exist");
		}
		int i = 0;
		int j = 0;
		int batchSize = propertiesUtilService.getBatchSize();
		ItxProtocolProtocol newItxProtocol = new ItxProtocolProtocol(itxProtocol);
		newItxProtocol.persist();

		if (itxProtocol.getLsStates() != null) {
			for (ItxProtocolProtocolState itxState : itxProtocol.getLsStates()) {
				ItxProtocolProtocolState newItxState = new ItxProtocolProtocolState(itxState);
				newItxState.setItxProtocolProtocol(newItxProtocol);
				newItxState.persist();
				if (j % batchSize == 0) { // same as the JDBC batch size
					newItxState.flush();
					newItxState.clear();
				}
				j++;
				if (itxState.getLsValues() != null) {
					for (ItxProtocolProtocolValue itxValue : itxState.getLsValues()) {
						itxValue.setLsState(newItxState);
						itxValue.persist();
						if (i % batchSize == 0) { // same as the JDBC batch size
							itxValue.flush();
							itxValue.clear();
						}
						i++;
					}
				}
			}
		}

		return newItxProtocol;
	}

	@Override
	@Transactional
	public Collection<ItxProtocolProtocol> saveLsItxProtocols(String json) {
		int i = 0;
		int j = 0;
		int batchSize = propertiesUtilService.getBatchSize();
		Collection<ItxProtocolProtocol> savedItxProtocolProtocols = new ArrayList<ItxProtocolProtocol>();

		StringReader sr = new StringReader(json);
		BufferedReader br = new BufferedReader(sr);
		for (ItxProtocolProtocol itxProtocol : ItxProtocolProtocol.fromJsonArrayToItxProtocolProtocols(br)) {
			ItxProtocolProtocol newItxProtocol = new ItxProtocolProtocol(itxProtocol);
			newItxProtocol.persist();
			savedItxProtocolProtocols.add(newItxProtocol);
			if (itxProtocol.getLsStates() != null) {
				for (ItxProtocolProtocolState itxState : itxProtocol.getLsStates()) {
					ItxProtocolProtocolState newItxState = new ItxProtocolProtocolState(itxState);
					newItxState.setItxProtocolProtocol(newItxProtocol);
					newItxState.persist();
					if (j % batchSize == 0) { // same as the JDBC batch size
						newItxState.flush();
						newItxState.clear();
					}
					j++;
					if (itxState.getLsValues() != null) {
						for (ItxProtocolProtocolValue itxValue : itxState.getLsValues()) {
							itxValue.setLsState(newItxState);
							itxValue.persist();
							if (i % batchSize == 0) {
								itxValue.flush();
								itxValue.clear();
							}
							i++;
						}
					}
				}
			}
		}

		return savedItxProtocolProtocols;
	}

	@Override
	public Collection<ItxProtocolProtocol> saveLsItxProtocols(
			Collection<ItxProtocolProtocol> itxProtocolProtocols) throws Exception {
		Collection<ItxProtocolProtocol> savedItxProtocolProtocols = new ArrayList<ItxProtocolProtocol>();
		for (ItxProtocolProtocol itxProtocolProtocol : itxProtocolProtocols) {
			savedItxProtocolProtocols.add(saveLsItxProtocol(itxProtocolProtocol));
		}
		return savedItxProtocolProtocols;
	}

	@Override
	@Transactional
	public ItxProtocolProtocol updateItxProtocolProtocol(ItxProtocolProtocol jsonItxProtocolProtocol) {

		ItxProtocolProtocol updatedItxProtocolProtocol = ItxProtocolProtocol.updateNoStates(jsonItxProtocolProtocol);
		updatedItxProtocolProtocol.merge();
		logger.debug("here is the updated itx: " + updatedItxProtocolProtocol.toJson());
		logger.debug("----------------- here is the itx id " + updatedItxProtocolProtocol.getId() + "   -----------");

		if (jsonItxProtocolProtocol.getLsStates() != null) {
			for (ItxProtocolProtocolState itxProtocolProtocolState : jsonItxProtocolProtocol.getLsStates()) {
				logger.debug("-------- current itxProtocolProtocolState ID: " + itxProtocolProtocolState.getId());
				ItxProtocolProtocolState updatedItxProtocolProtocolState;
				if (itxProtocolProtocolState.getId() == null) {
					updatedItxProtocolProtocolState = new ItxProtocolProtocolState(itxProtocolProtocolState);
					updatedItxProtocolProtocolState.setItxProtocolProtocol(
							ItxProtocolProtocol.findItxProtocolProtocol(updatedItxProtocolProtocol.getId()));
					updatedItxProtocolProtocolState.persist();
					updatedItxProtocolProtocol.getLsStates().add(updatedItxProtocolProtocolState);
				} else {

					if (itxProtocolProtocolState.getItxProtocolProtocol() == null)
						itxProtocolProtocolState.setItxProtocolProtocol(updatedItxProtocolProtocol);
					updatedItxProtocolProtocolState = ItxProtocolProtocolState.update(itxProtocolProtocolState);
					updatedItxProtocolProtocol.getLsStates().add(updatedItxProtocolProtocolState);
					logger.debug("updated itxProtocolProtocol state " + updatedItxProtocolProtocolState.getId());

				}
				if (itxProtocolProtocolState.getLsValues() != null) {
					for (ItxProtocolProtocolValue itxProtocolProtocolValue : itxProtocolProtocolState.getLsValues()) {
						ItxProtocolProtocolValue updatedItxProtocolProtocolValue;
						if (itxProtocolProtocolValue.getId() == null) {
							updatedItxProtocolProtocolValue = ItxProtocolProtocolValue.create(itxProtocolProtocolValue);
							updatedItxProtocolProtocolValue.setLsState(updatedItxProtocolProtocolState);
							updatedItxProtocolProtocolValue.persist();
							updatedItxProtocolProtocolState.getLsValues().add(updatedItxProtocolProtocolValue);

						} else {
							// itxProtocolProtocolValue.setLsState(updatedItxProtocolProtocolState);
							itxProtocolProtocolValue.setLsState(updatedItxProtocolProtocolState);
							updatedItxProtocolProtocolValue = ItxProtocolProtocolValue.update(itxProtocolProtocolValue);
							updatedItxProtocolProtocolState.getLsValues().add(updatedItxProtocolProtocolValue);
						}
					}
				} else {
					logger.debug("No itxProtocolProtocol values to update");
				}
			}
		}

		return updatedItxProtocolProtocol;
	}

	@Override
	@Transactional
	public Collection<ItxProtocolProtocol> findItxProtocolProtocolsByFirstProtocol(
			Long firstProtocolId) throws Exception {
		Protocol firstProtocol;
		try {
			firstProtocol = Protocol.findProtocol(firstProtocolId);
		} catch (Exception e) {
			logger.error("Error in findItxProtocolProtocolsByFirstProtocol: firstProtocol " + firstProtocolId.toString()
					+ " not found");
			throw new Exception("First Protocol " + firstProtocolId + " not found");
		}
		Collection<ItxProtocolProtocol> itxProtocolProtocols = ItxProtocolProtocol
				.findItxProtocolProtocolsByFirstProtocol(firstProtocol).getResultList();
		for (ItxProtocolProtocol itx : itxProtocolProtocols) {
			logger.debug(itx.getCodeName() + " " + itx.getId().toString());
			logger.debug(itx.toJson());
		}
		return itxProtocolProtocols;
	}
}
