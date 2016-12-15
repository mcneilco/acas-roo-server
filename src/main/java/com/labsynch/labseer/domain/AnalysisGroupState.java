package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.dto.AnalysisGroupCsvDTO;
import com.labsynch.labseer.dto.FlatThingCsvDTO;
import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
@Transactional
@RooJpaActiveRecord(finders = { "findAnalysisGroupStatesByLsTypeAndKindEquals", "findAnalysisGroupStatesByAnalysisGroup", "findAnalysisGroupStatesByLsTransactionEquals", "findAnalysisGroupStatesByLsTypeEqualsAndLsKindEquals", "findAnalysisGroupStatesByAnalysisGroupAndLsTypeEqualsAndLsKindEqualsAndIgnoredNot" })
public class AnalysisGroupState extends AbstractState {

	@NotNull
	@ManyToOne
	@JoinColumn(name = "analysis_group_id")
	private AnalysisGroup analysisGroup;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "lsState", fetch = FetchType.LAZY)
	private Set<AnalysisGroupValue> lsValues = new HashSet<AnalysisGroupValue>();

	public AnalysisGroupState(com.labsynch.labseer.domain.AnalysisGroupState analysisGroupState) {
		this.setRecordedBy(analysisGroupState.getRecordedBy());
		this.setRecordedDate(analysisGroupState.getRecordedDate());
		this.setLsTransaction(analysisGroupState.getLsTransaction());
		this.setModifiedBy(analysisGroupState.getModifiedBy());
		this.setModifiedDate(analysisGroupState.getModifiedDate());
		this.setLsType(analysisGroupState.getLsType());
		this.setLsKind(analysisGroupState.getLsKind());
	}

	public AnalysisGroupState(AnalysisGroupCsvDTO analysisGroupDTO) {
		this.setRecordedBy(analysisGroupDTO.getRecordedBy());
		this.setRecordedDate(analysisGroupDTO.getRecordedDate());
		this.setLsTransaction(analysisGroupDTO.getLsTransaction());
		this.setModifiedBy(analysisGroupDTO.getModifiedBy());
		this.setModifiedDate(analysisGroupDTO.getModifiedDate());
		this.setLsType(analysisGroupDTO.getStateType());
		this.setLsKind(analysisGroupDTO.getStateKind());
	}

	public AnalysisGroupState(FlatThingCsvDTO analysisGroupDTO) {
		this.setRecordedBy(analysisGroupDTO.getRecordedBy());
		this.setRecordedDate(analysisGroupDTO.getRecordedDate());
		this.setLsTransaction(analysisGroupDTO.getLsTransaction());
		this.setModifiedBy(analysisGroupDTO.getModifiedBy());
		this.setModifiedDate(analysisGroupDTO.getModifiedDate());
		this.setLsType(analysisGroupDTO.getStateType());
		this.setLsKind(analysisGroupDTO.getStateKind());
	}

	public static com.labsynch.labseer.domain.AnalysisGroupState update(com.labsynch.labseer.domain.AnalysisGroupState analysisGroupState) {
		AnalysisGroupState updatedAnalysisGroupState = AnalysisGroupState.findAnalysisGroupState(analysisGroupState.getId());
		updatedAnalysisGroupState.setRecordedBy(analysisGroupState.getRecordedBy());
		updatedAnalysisGroupState.setRecordedDate(analysisGroupState.getRecordedDate());
		updatedAnalysisGroupState.setLsTransaction(analysisGroupState.getLsTransaction());
		updatedAnalysisGroupState.setModifiedBy(analysisGroupState.getModifiedBy());
		updatedAnalysisGroupState.setModifiedDate(new Date());
		updatedAnalysisGroupState.setLsType(analysisGroupState.getLsType());
		updatedAnalysisGroupState.setLsKind(analysisGroupState.getLsKind());
		updatedAnalysisGroupState.merge();
		return updatedAnalysisGroupState;
	}

	public String toJson() {
		return new JSONSerializer().include("lsValues").exclude("*.class", "analysisGroup.experiment").transform(new ExcludeNulls(), void.class).serialize(this);
	}

	public static com.labsynch.labseer.domain.AnalysisGroupState fromJsonToAnalysisGroupState(String json) {
		return new JSONDeserializer<AnalysisGroupState>().use(null, AnalysisGroupState.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
	}

	public static String toJsonArray(Collection<com.labsynch.labseer.domain.AnalysisGroupState> collection) {
		return new JSONSerializer().exclude("*.class", "analysisGroup.experiment").transform(new ExcludeNulls(), void.class).serialize(collection);
	}

	public static String toJsonArrayStub(Collection<com.labsynch.labseer.domain.AnalysisGroupState> collection) {
		return new JSONSerializer().exclude("*.class", "analysisGroup").transform(new ExcludeNulls(), void.class).serialize(collection);
	}

	public static Collection<com.labsynch.labseer.domain.AnalysisGroupState> fromJsonArrayToAnalysisGroupStates(String json) {
		return new JSONDeserializer<List<AnalysisGroupState>>().use(null, ArrayList.class).use("values", AnalysisGroupState.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
	}

	public static Collection<com.labsynch.labseer.domain.AnalysisGroupState> fromJsonArrayToAnalysisGroupStates(Reader json) {
		return new JSONDeserializer<List<AnalysisGroupState>>().use(null, ArrayList.class).use("values", AnalysisGroupState.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
	}

	public static int deleteByExperimentID(Long experimentId) {
		if (experimentId == null) return 0;
		EntityManager em = SubjectValue.entityManager();
		String deleteSQL = "DELETE FROM AnalysisGroupState oo WHERE id in (select o.id from AnalysisGroupState o where o.analysisGroup.experiment.id = :experimentId)";
		Query q = em.createQuery(deleteSQL);
		q.setParameter("experimentId", experimentId);
		int numberOfDeletedEntities = q.executeUpdate();
		return numberOfDeletedEntities;
	}
	
	public static TypedQuery<AnalysisGroupState> findAnalysisGroupStatesByAnalysisGroupIDAndStateTypeKind(Long analysisGroupId, 
			String stateType, 
			String stateKind) {
			if (stateType == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
			if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
			
			EntityManager em = entityManager();
			String hsqlQuery = "SELECT ags FROM AnalysisGroupState AS ags " +
			"JOIN ags.analysisGroup ag " +
			"WHERE ags.lsType = :stateType AND ags.lsKind = :stateKind AND ags.ignored IS NOT :ignored " +
			"AND ag.id = :analysisGroupId ";
			TypedQuery<AnalysisGroupState> q = em.createQuery(hsqlQuery, AnalysisGroupState.class);
			q.setParameter("analysisGroupId", analysisGroupId);
			q.setParameter("stateType", stateType);
			q.setParameter("stateKind", stateKind);
			q.setParameter("ignored", true);
			return q;
		}

}
