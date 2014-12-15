package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.DDictValue;
import com.labsynch.labseer.dto.CodeTableDTO;

@Service
public interface DataDictionaryService {
	
	
	public DDictValue saveDataDictionaryValue(DDictValue dDict, Boolean createTypeAndKind);

	public List<CodeTableDTO> getDataDictionaryCodeTableListByTypeKind(String lsType, String lsKind);
	
	public List<CodeTableDTO> getDataDictionaryCodeTableListByType(String lsType);

	public String getCsvList(List<DDictValue> dDictResults);

	public CodeTableDTO getDataDictionaryCodeTable(DDictValue dDictValue);

	public List<CodeTableDTO> convertToCodeTables(List<DDictValue> dDictResults);

	public CodeTableDTO saveCodeTableValue(String lsType, String lsKind, CodeTableDTO codeTableValue, Boolean createTypeAndKind);

	public CodeTableDTO updateCodeTableValue(String lsType, String lsKind, CodeTableDTO codeTableValue);

	public Collection<CodeTableDTO> saveCodeTableValueArray(String lsType,
			String lsKind, List<CodeTableDTO> codeTableDTOs, Boolean createTypeAndKind);
	
	public Collection<DDictValue> saveDataDictionaryValues(Collection<DDictValue> dDictValues, Boolean createTypeAndKind);

	public Collection<CodeTableDTO> updateCodeTableValueArray(String lsType,
			String lsKind, List<CodeTableDTO> codeTableDTOs);




}
