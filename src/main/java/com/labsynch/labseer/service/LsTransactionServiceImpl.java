package com.labsynch.labseer.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import com.labsynch.labseer.domain.LsTransaction;
import com.labsynch.labseer.domain.LsTransactionStatus;
import com.labsynch.labseer.domain.LsTransactionType;
import com.labsynch.labseer.dto.LsTransactionQueryDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LsTransactionServiceImpl implements LsTransactionService {

	private static final Logger logger = LoggerFactory.getLogger(LsTransactionServiceImpl.class);

	@Override
	public LsTransactionQueryDTO searchLsTransactions(
			LsTransactionQueryDTO query) throws Exception {
		List<Long> idList = new ArrayList<Long>();
		EntityManager em = LsTransaction.entityManager();
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
		Root<LsTransaction> transaction = criteria.from(LsTransaction.class);

		criteria.select(transaction.<Long>get("id"));
		criteria.distinct(true);
		Predicate[] predicates = new Predicate[0];
		List<Predicate> predicateList = new ArrayList<Predicate>();
		if (query.getRecordedDateGreaterThan() != null && query.getRecordedDateLessThan() != null) {
			try {
				Predicate createdDateBetween = criteriaBuilder.between(transaction.<Date>get("recordedDate"),
						query.getRecordedDateGreaterThan(), query.getRecordedDateLessThan());
				predicateList.add(createdDateBetween);
			} catch (Exception e) {
				logger.error("Caught exception trying to parse " + query.getRecordedDateGreaterThan() + " or "
						+ query.getRecordedDateLessThan() + " as a date.", e);
				throw new Exception("Caught exception trying to parse " + query.getRecordedDateGreaterThan() + " or "
						+ query.getRecordedDateLessThan() + " as a date.", e);
			}
		} else if (query.getRecordedDateGreaterThan() != null) {
			try {
				Predicate createdDateFrom = criteriaBuilder.greaterThanOrEqualTo(transaction.<Date>get("recordedDate"),
						query.getRecordedDateGreaterThan());
				predicateList.add(createdDateFrom);
			} catch (Exception e) {
				logger.error("Caught exception trying to parse " + query.getRecordedDateGreaterThan() + " as a date.",
						e);
				throw new Exception(
						"Caught exception trying to parse " + query.getRecordedDateGreaterThan() + " as a date.", e);
			}
		} else if (query.getRecordedDateLessThan() != null) {
			try {
				Predicate createdDateTo = criteriaBuilder.lessThanOrEqualTo(transaction.<Date>get("recordedDate"),
						query.getRecordedDateLessThan());
				predicateList.add(createdDateTo);
			} catch (Exception e) {
				logger.error("Caught exception trying to parse " + query.getRecordedDateLessThan() + " as a date.", e);
				throw new Exception(
						"Caught exception trying to parse " + query.getRecordedDateLessThan() + " as a date.", e);
			}
		}
		if (query.getRecordedBy() != null) {
			Predicate recordedBy = criteriaBuilder.like(transaction.<String>get("recordedBy"),
					'%' + query.getRecordedBy() + '%');
			predicateList.add(recordedBy);
		}
		if (query.getType() != null) {
			Predicate type = criteriaBuilder.equal(transaction.<LsTransactionType>get("type"),
					LsTransactionType.valueOf(query.getType()));
			predicateList.add(type);
		}
		if (query.getStatus() != null) {
			Predicate type = criteriaBuilder.equal(transaction.<LsTransactionStatus>get("status"),
					LsTransactionStatus.valueOf(query.getStatus()));
			predicateList.add(type);
		}

		predicates = predicateList.toArray(predicates);
		criteria.where(criteriaBuilder.and(predicates));
		TypedQuery<Long> q = em.createQuery(criteria);
		logger.debug(q.unwrap(org.hibernate.query.Query.class).getQueryString());
		idList = q.getResultList();
		logger.debug("Found " + idList.size() + " results.");
		query.setNumberOfResults(idList.size());
		if (query.getMaxResults() != null && query.getNumberOfResults() > query.getMaxResults()) {
			return query;
		} else {
			Collection<LsTransaction> results = new ArrayList<LsTransaction>();
			for (Long id : idList) {
				LsTransaction result = LsTransaction.findLsTransaction(id);
				results.add(result);
			}
			query.setResults(results);
			return query;
		}
	}

}
