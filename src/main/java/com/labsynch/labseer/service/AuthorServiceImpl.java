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

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.domain.DDictValue;
import com.labsynch.labseer.dto.AutoLabelDTO;
import com.labsynch.labseer.dto.CodeTableDTO;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

	@Autowired
	private AutoLabelService autoLabelService;

	private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);
	

	@Override
	public CodeTableDTO getAuthorCodeTable(Author author) {
		CodeTableDTO codeTable = new CodeTableDTO();
		codeTable.setName(author.getFirstName() + " " + author.getLastName());
		codeTable.setCode(author.getUserName());
		codeTable.setIgnored(false);
		return codeTable;
	}


	@Override
	public List<CodeTableDTO> convertToCodeTables(List<Author> authors) {
		List<CodeTableDTO> codeTableList = new ArrayList<CodeTableDTO>();
		for (Author author : authors) {
			CodeTableDTO codeTable = getAuthorCodeTable(author);
			codeTableList.add(codeTable);
		}
		return codeTableList;	
	}
}
