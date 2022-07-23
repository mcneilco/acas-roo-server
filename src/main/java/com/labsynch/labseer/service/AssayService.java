package com.labsynch.labseer.service;

import org.springframework.stereotype.Service;

@Service
public interface AssayService {
    
	int renameBatchCode(String oldCode, String newCode, String modifiedByUser);

}
