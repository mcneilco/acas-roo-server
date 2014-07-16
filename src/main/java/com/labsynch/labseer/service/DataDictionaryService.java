package com.labsynch.labseer.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.DDictValue;
import com.labsynch.labseer.dto.CodeTableDTO;

@Service
public interface DataDictionaryService {
	
	public DDictValue saveDataDictionaryValue(DDictValue dDict);

	public DDictValue getDataDictionaryValue(DDictValue dDict);

	public DDictValue updateDataDictionaryValue(DDictValue dDict); 
	
	public List<CodeTableDTO> getDataDictionaryCodeTableListByTypeKind(String lsType, String lsKind);
	
	public List<CodeTableDTO> getDataDictionaryCodeTableListByType(String lsType);
}
