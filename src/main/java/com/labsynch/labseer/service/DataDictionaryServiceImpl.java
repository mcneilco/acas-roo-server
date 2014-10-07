package com.labsynch.labseer.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
			codeTableList.add(codeTable);
		}
		return codeTableList;	
	}
}
