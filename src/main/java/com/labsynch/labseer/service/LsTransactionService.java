package com.labsynch.labseer.service;

import org.springframework.stereotype.Service;

import com.labsynch.labseer.dto.LsTransactionQueryDTO;

@Service
public interface LsTransactionService {

	LsTransactionQueryDTO searchLsTransactions(LsTransactionQueryDTO query) throws Exception;

	
}
