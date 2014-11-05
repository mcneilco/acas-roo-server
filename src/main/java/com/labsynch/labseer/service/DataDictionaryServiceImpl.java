package com.labsynch.labseer.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.labsynch.labseer.domain.DDictValue;
import com.labsynch.labseer.dto.AutoLabelDTO;
import com.labsynch.labseer.dto.CodeTableDTO;

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
			dDict.setCodeName(autoLabelService.getDataDictionaryCodeName());
		}
		return dDict;
	}


	@Override
	public List<CodeTableDTO> getDataDictionaryCodeTableListByTypeKind(String lsType, String lsKind) {
		List<CodeTableDTO> codeTableList = new ArrayList<CodeTableDTO>();
		for (DDictValue val : DDictValue.findDDictValuesByLsTypeEqualsAndLsKindEquals(lsType, lsKind).getResultList()) {
			if (!val.isIgnored()) {
				CodeTableDTO codeTable = new CodeTableDTO();
				codeTable.setName(val.getLabelText());
				codeTable.setCode(val.getShortName());
				codeTable.setIgnored(val.isIgnored());
				codeTable.setDisplayOrder(val.getDisplayOrder());
				codeTableList.add(codeTable);
			}
		}
		return codeTableList;
	}

	@Override
	public List<CodeTableDTO> getDataDictionaryCodeTableListByType(String lsType) {
		List<CodeTableDTO> codeTableList = new ArrayList<CodeTableDTO>();
		for (DDictValue val : DDictValue.findDDictValuesByLsTypeEquals(lsType).getResultList()) {
			if (!val.isIgnored()) {
				CodeTableDTO codeTable = new CodeTableDTO();
				codeTable.setName(val.getLabelText());
				codeTable.setCode(val.getShortName());
				codeTable.setIgnored(val.isIgnored());
				codeTable.setDisplayOrder(val.getDisplayOrder());
				codeTableList.add(codeTable);
			}
		}
		return codeTableList;
	}
	

	@Override
	public CodeTableDTO getDataDictionaryCodeTable(DDictValue dDictValue) {
		CodeTableDTO codeTable = new CodeTableDTO();
		codeTable.setName(dDictValue.getLabelText());
		codeTable.setCode(dDictValue.getShortName());
		codeTable.setIgnored(dDictValue.isIgnored());
		codeTable.setDisplayOrder(dDictValue.getDisplayOrder());
		return codeTable;
	}


	@Override
	public String getCsvList(List<DDictValue> dDictValues) {
		StringWriter outFile = new StringWriter();
		ICsvBeanWriter beanWriter = null;
		try {
			beanWriter = new CsvBeanWriter(outFile, CsvPreference.STANDARD_PREFERENCE);
			final String[] header = DDictValue.getColumns();
			final CellProcessor[] processors = DDictValue.getProcessors();
			beanWriter.writeHeader(header);
			for (final DDictValue dDictValue : dDictValues) {
				beanWriter.write(dDictValue, header, processors);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (beanWriter != null) {
				try {
					beanWriter.close();
					outFile.flush();
					outFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return outFile.toString();
	}


	@Override
	public List<CodeTableDTO> convertToCodeTables(List<DDictValue> dDictResults) {
		List<CodeTableDTO> codeTableList = new ArrayList<CodeTableDTO>();
		for (DDictValue val : dDictResults) {
			CodeTableDTO codeTable = new CodeTableDTO();
			codeTable.setName(val.getLabelText());
			codeTable.setCode(val.getShortName());
			codeTable.setIgnored(val.isIgnored());
			codeTable.setDisplayOrder(val.getDisplayOrder());
			codeTable.setCodeName(val.getCodeName());
			codeTable.setId(val.getId());
			codeTableList.add(codeTable);
		}
		return codeTableList;	
	}


	@Override
	public CodeTableDTO saveCodeTableValue(String lsType, String lsKind, String json) {
		//TODO: fix this method or service to guard against creating duplicate entries
		CodeTableDTO codeTableValue = CodeTableDTO.fromJsonToCodeTableDTO(json);	
		return saveCodeTableValue(lsType, lsKind, codeTableValue);
	}

	@Override
	public CodeTableDTO saveCodeTableValue(String lsType, String lsKind, CodeTableDTO codeTableValue) {
		//TODO: fix this method or service to guard against creating duplicate entries
		DDictValue dDictVal = new DDictValue();
		dDictVal.setLsType(lsType);
		dDictVal.setLsKind(lsKind);
		dDictVal.setShortName(codeTableValue.getCode());
		dDictVal.setLabelText(codeTableValue.getName());
		dDictVal.persist();
		
		return new CodeTableDTO(dDictVal);
	}
	

	@Override
	public Collection<CodeTableDTO> saveCodeTableValueArray(String lsType, String lsKind, String json) {
		Collection<CodeTableDTO> codeTableValues = CodeTableDTO.fromJsonArrayToCoes(json);
		Collection<CodeTableDTO> newCodeTableValues = new HashSet<CodeTableDTO>();
		for (CodeTableDTO codeTableValue : codeTableValues){
			CodeTableDTO newCodeTableValue = saveCodeTableValue(lsType, lsKind, codeTableValue);
			newCodeTableValues.add(newCodeTableValue);
		}
		return newCodeTableValues;
	}


	@Override
	public CodeTableDTO updateCodeTableValue(String lsType, String lsKind, String json) {
		CodeTableDTO codeTableValue = CodeTableDTO.fromJsonToCodeTableDTO(json);	
		return updateCodeTableValue(lsType, lsKind, codeTableValue);
	}


	@Override
	public CodeTableDTO updateCodeTableValue(String lsType, String lsKind, CodeTableDTO codeTableValue) {	
		DDictValue oldDDictValue = DDictValue.findDDictValue(codeTableValue.getId());
		if (oldDDictValue == null){
			return null;
		} else {
			oldDDictValue.setShortName(codeTableValue.getCode());
			oldDDictValue.setLabelText(codeTableValue.getName());
			oldDDictValue.merge();
			return new CodeTableDTO(oldDDictValue);			
		}
	}


	@Override
	public Collection<CodeTableDTO> updateCodeTableValueArray(String lsType, String lsKind, String json) {
		Collection<CodeTableDTO> codeTableValues = CodeTableDTO.fromJsonArrayToCoes(json);
		Collection<CodeTableDTO> updatedCodeTableValues = new HashSet<CodeTableDTO>();
		for (CodeTableDTO codeTableValue : codeTableValues){
			CodeTableDTO updatedCodeTableValue = updateCodeTableValue(lsType, lsKind, codeTableValue);
			updatedCodeTableValues.add(updatedCodeTableValue);
		}
		return updatedCodeTableValues;
	}



}
