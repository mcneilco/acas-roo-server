package com.labsynch.labseer.service;

import com.labsynch.labseer.dto.LsTransactionQueryDTO;

import org.springframework.stereotype.Service;

@Service
public interface LsTransactionService {

	LsTransactionQueryDTO searchLsTransactions(LsTransactionQueryDTO query) throws Exception;

	
}
