package com.labsynch.labseer.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.domain.DDictValue;
import com.labsynch.labseer.dto.AutoLabelDTO;

@Service
@Transactional
public class DataDictionaryServiceImpl implements DataDictionaryService {
	
	@Autowired
	private AutoLabelService autoLabelService;
	
	private static final Logger logger = LoggerFactory.getLogger(DataDictionaryServiceImpl.class);


	@Override
	public DDictValue saveDataDictionaryValue(DDictValue dDict) {
		logger.debug("here is the incoming ddict value: " + dDict.toJson());
		if (dDict.getCodeName() == null){
			String thingTypeAndKind = "document_datadictionary";
			String labelTypeAndKind = "id_codeName";
			Long numberOfLabels = 1L;
			List<AutoLabelDTO> labels;
			labels = autoLabelService.getAutoLabels(thingTypeAndKind, labelTypeAndKind, numberOfLabels );
			dDict.setCodeName(labels.get(0).getAutoLabel());
		}
		dDict.persist();
		return dDict;
	}

	@Override
	public DDictValue getDataDictionaryValue(DDictValue dDict) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DDictValue updateDataDictionaryValue(DDictValue dDict) {
		// TODO Auto-generated method stub
		return null;
	}

}
