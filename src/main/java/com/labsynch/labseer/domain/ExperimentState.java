package com.labsynch.labseer.domain;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.TypedQuery;
import jakarta.validation.constraints.NotNull;

import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity

public class ExperimentState extends AbstractState {

	@NotNull
	@ManyToOne
	@JoinColumn(name = "experiment_id")
	private Experiment experiment;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "lsState", fetch = FetchType.LAZY)
	private Set<ExperimentValue> lsValues = new HashSet<ExperimentValue>();

	public ExperimentState() {
	}

	// constructor to instantiate a new experimentState from nested json objects
	public ExperimentState(ExperimentState experimentState) {

		super.setRecordedBy(experimentState.getRecordedBy());
		super.setRecordedDate(experimentState.getRecordedDate());
		super.setLsTransaction(experimentState.getLsTransaction());
		super.setModifiedBy(experimentState.getModifiedBy());
		super.setModifiedDate(experimentState.getModifiedDate());
		super.setLsType(experimentState.getLsType());
		super.setLsKind(experimentState.getLsKind());

	}

	public static ExperimentState update(ExperimentState experimentState) {
		ExperimentState updatedExperimentState = ExperimentState.findExperimentState(experimentState.getId());
		updatedExperimentState.setRecordedBy(experimentState.getRecordedBy());
		updatedExperimentState.setRecordedDate(experimentState.getRecordedDate());
		updatedExperimentState.setLsTransaction(experimentState.getLsTransaction());
		updatedExperimentState.setModifiedBy(experimentState.getModifiedBy());
		updatedExperimentState.setModifiedDate(new Date());
		updatedExperimentState.setLsType(experimentState.getLsType());
		updatedExperimentState.setLsKind(experimentState.getLsKind());
		updatedExperimentState.setIgnored(experimentState.isIgnored());
		updatedExperimentState.merge();
		return updatedExperimentState;
	}

	@Transactional
	public String toJson() {
		return new JSONSerializer().include("lsValues").exclude("*.class", "experiment")
				.transform(new ExcludeNulls(), void.class).serialize(this);
	}

	public static ExperimentState fromJsonToExperimentState(String json) {
		return new JSONDeserializer<ExperimentState>().use(null, ExperimentState.class).deserialize(json);
	}

	public static String toJsonArray(Collection<ExperimentState> collection) {
		return new JSONSerializer().exclude("*.class")
				.transform(new ExcludeNulls(), void.class)
				.serialize(collection);
	}

	public static Collection<ExperimentState> fromJsonArrayToExperimentStates(String json) {
		return new JSONDeserializer<List<ExperimentState>>().use(null, ArrayList.class)
				.use("values", ExperimentState.class).deserialize(json);
	}

	public static Collection<ExperimentState> fromJsonArrayToExperimentStates(Reader json) {
		return new JSONDeserializer<List<ExperimentState>>().use(null, ArrayList.class)
				.use("values", ExperimentState.class).deserialize(json);
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public static TypedQuery<ExperimentState> findExperimentStatesByExptIDAndStateTypeKind(Long experimentId,
			String stateType,
			String stateKind) {
		if (stateType == null || stateKind.length() == 0)
			throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0)
			throw new IllegalArgumentException("The stateKind argument is required");

		EntityManager em = entityManager();
		String hsqlQuery = "SELECT evs FROM ExperimentState AS evs " +
				"JOIN evs.experiment exp " +
				"WHERE evs.lsType = :stateType AND evs.lsKind = :stateKind AND evs.ignored != :ignored " +
				"AND exp.id = :experimentId ";
		TypedQuery<ExperimentState> q = em.createQuery(hsqlQuery, ExperimentState.class);
		q.setParameter("experimentId", experimentId);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("ignored", true);
		return q;
	}

	public static TypedQuery<ExperimentState> findExperimentStatesByExperimentCodeNameAndStateTypeKind(
			String experimentCodeName, String stateType, String stateKind) {
		if (stateType == null || stateKind.length() == 0)
			throw new IllegalArgumentException("The stateType argument is required");
		if (stateKind == null || stateKind.length() == 0)
			throw new IllegalArgumentException("The stateKind argument is required");

		EntityManager em = entityManager();
		String hsqlQuery = "SELECT evs FROM ExperimentState AS evs " +
				"JOIN evs.experiment exp " +
				"WHERE evs.lsType = :stateType AND evs.lsKind = :stateKind AND evs.ignored != :ignored " +
				"AND exp.codeName = :experimentCodeName ";
		TypedQuery<ExperimentState> q = em.createQuery(hsqlQuery, ExperimentState.class);
		q.setParameter("experimentCodeName", experimentCodeName);
		q.setParameter("stateType", stateType);
		q.setParameter("stateKind", stateKind);
		q.setParameter("ignored", true);
		return q;
	}

	public static Long countFindExperimentStatesByExperiment(Experiment experiment) {
		if (experiment == null)
			throw new IllegalArgumentException("The experiment argument is required");
		EntityManager em = ExperimentState.entityManager();
		TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ExperimentState AS o WHERE o.experiment = :experiment",
				Long.class);
		q.setParameter("experiment", experiment);
		return ((Long) q.getSingleResult());
	}

	public static TypedQuery<ExperimentState> findExperimentStatesByExperiment(Experiment experiment) {
		if (experiment == null)
			throw new IllegalArgumentException("The experiment argument is required");
		EntityManager em = ExperimentState.entityManager();
		TypedQuery<ExperimentState> q = em.createQuery(
				"SELECT o FROM ExperimentState AS o WHERE o.experiment = :experiment", ExperimentState.class);
		q.setParameter("experiment", experiment);
		return q;
	}

	public static TypedQuery<ExperimentState> findExperimentStatesByExperiment(Experiment experiment,
			String sortFieldName, String sortOrder) {
		if (experiment == null)
			throw new IllegalArgumentException("The experiment argument is required");
		EntityManager em = ExperimentState.entityManager();
		StringBuilder queryBuilder = new StringBuilder(
				"SELECT o FROM ExperimentState AS o WHERE o.experiment = :experiment");
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			queryBuilder.append(" ORDER BY ").append(sortFieldName);
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				queryBuilder.append(" ").append(sortOrder);
			}
		}
		TypedQuery<ExperimentState> q = em.createQuery(queryBuilder.toString(), ExperimentState.class);
		q.setParameter("experiment", experiment);
		return q;
	}

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("experiment", "lsValues");

	public static long countExperimentStates() {
		return entityManager().createQuery("SELECT COUNT(o) FROM ExperimentState o", Long.class).getSingleResult();
	}

	public static List<ExperimentState> findAllExperimentStates() {
		return entityManager().createQuery("SELECT o FROM ExperimentState o", ExperimentState.class).getResultList();
	}

	public static List<ExperimentState> findAllExperimentStates(String sortFieldName, String sortOrder) {
		String jpaQuery = "SELECT o FROM ExperimentState o";
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				jpaQuery = jpaQuery + " " + sortOrder;
			}
		}
		return entityManager().createQuery(jpaQuery, ExperimentState.class).getResultList();
	}

	public static ExperimentState findExperimentState(Long id) {
		if (id == null)
			return null;
		return entityManager().find(ExperimentState.class, id);
	}

	public static List<ExperimentState> findExperimentStateEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM ExperimentState o", ExperimentState.class)
				.setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

	public static List<ExperimentState> findExperimentStateEntries(int firstResult, int maxResults,
			String sortFieldName, String sortOrder) {
		String jpaQuery = "SELECT o FROM ExperimentState o";
		if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
			jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
			if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
				jpaQuery = jpaQuery + " " + sortOrder;
			}
		}
		return entityManager().createQuery(jpaQuery, ExperimentState.class).setFirstResult(firstResult)
				.setMaxResults(maxResults).getResultList();
	}

	@Transactional
	public ExperimentState merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		ExperimentState merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}

	public Experiment getExperiment() {
		return this.experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	public Set<ExperimentValue> getLsValues() {
		return this.lsValues;
	}

	public void setLsValues(Set<ExperimentValue> lsValues) {
		this.lsValues = lsValues;
	}
}
