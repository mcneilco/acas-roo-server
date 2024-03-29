package com.labsynch.labseer.service;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.DDictValue;
import com.labsynch.labseer.dto.CodeTableDTO;

import org.springframework.stereotype.Service;

@Service
public interface DataDictionaryService {

	public DDictValue saveDataDictionaryValue(DDictValue dDict, Boolean createTypeAndKind);

	public List<CodeTableDTO> getDataDictionaryCodeTableListByTypeKind(String lsType, String lsKind);

	public List<CodeTableDTO> getDataDictionaryCodeTableListByType(String lsType);

	public String getCsvList(List<DDictValue> dDictResults);

	public CodeTableDTO getDataDictionaryCodeTable(DDictValue dDictValue);

	public List<CodeTableDTO> convertToCodeTables(List<DDictValue> dDictResults);

	public CodeTableDTO updateCodeTableValue(CodeTableDTO codeTableValue);

	public List<DDictValue> saveDataDictionaryValues(Collection<DDictValue> dDictValues, Boolean createTypeAndKind);

	public List<CodeTableDTO> updateCodeTableValueArray(List<CodeTableDTO> codeTableDTOs);

	CodeTableDTO saveCodeTableValue(CodeTableDTO codeTableValue, Boolean createTypeAndKind);

	List<CodeTableDTO> saveCodeTableValueArray(List<CodeTableDTO> codeTableDTOs, Boolean createTypeAndKind);

	List<CodeTableDTO> getOrCreateCodeTableArray(List<CodeTableDTO> codeTables,
			Boolean createTypeKind);

	CodeTableDTO getOrCreateCodeTable(CodeTableDTO codeTable,
			Boolean createTypeKind);

}
