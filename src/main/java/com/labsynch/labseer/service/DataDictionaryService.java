package com.labsynch.labseer.service;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.domain.DDictValue;;

@Service
public interface DataDictionaryService {
	
	public DDictValue saveDataDictionaryValue(DDictValue dDict);

	public DDictValue getDataDictionaryValue(DDictValue dDict);

	public DDictValue updateDataDictionaryValue(DDictValue dDict); 
}
