package com.labsynch.labseer.domain;

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
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.dto.FlatThingCsvDTO;
import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
@Transactional
@RooJpaActiveRecord(finders = { "findAuthorStatesByLsTypeAndKindEquals", "findAuthorStatesByAuthor", "findAuthorStatesByLsTransactionEquals", "findAuthorStatesByLsTypeEqualsAndLsKindEquals", "findAuthorStatesByAuthorAndLsTypeEqualsAndLsKindEqualsAndIgnoredNot" })
public class AuthorState extends AbstractState {

	@NotNull
	@ManyToOne
	@JoinColumn(name = "author_id")
	private Author author;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "lsState", fetch = FetchType.LAZY)
	private Set<AuthorValue> lsValues = new HashSet<AuthorValue>();

	public AuthorState(com.labsynch.labseer.domain.AuthorState authorState) {
		this.setRecordedBy(authorState.getRecordedBy());
		this.setRecordedDate(authorState.getRecordedDate());
		this.setLsTransaction(authorState.getLsTransaction());
		this.setModifiedBy(authorState.getModifiedBy());
		this.setModifiedDate(authorState.getModifiedDate());
		this.setLsType(authorState.getLsType());
		this.setLsKind(authorState.getLsKind());
	}

	public AuthorState(FlatThingCsvDTO authorDTO) {
		this.setRecordedBy(authorDTO.getRecordedBy());
		this.setRecordedDate(authorDTO.getRecordedDate());
		this.setLsTransaction(authorDTO.getLsTransaction());
		this.setModifiedBy(authorDTO.getModifiedBy());
		this.setModifiedDate(authorDTO.getModifiedDate());
		this.setLsType(authorDTO.getStateType());
		this.setLsKind(authorDTO.getStateKind());
	}

	public static com.labsynch.labseer.domain.AuthorState update(com.labsynch.labseer.domain.AuthorState authorState) {
		AuthorState updatedAuthorState = AuthorState.findAuthorState(authorState.getId());
		updatedAuthorState.setRecordedBy(authorState.getRecordedBy());
		updatedAuthorState.setRecordedDate(authorState.getRecordedDate());
		updatedAuthorState.setLsTransaction(authorState.getLsTransaction());
		updatedAuthorState.setModifiedBy(authorState.getModifiedBy());
		updatedAuthorState.setModifiedDate(new Date());
		updatedAuthorState.setLsType(authorState.getLsType());
		updatedAuthorState.setLsKind(authorState.getLsKind());
		updatedAuthorState.merge();
		return updatedAuthorState;
	}

	public static com.labsynch.labseer.domain.AuthorState fromJsonToAuthorState(String json) {
		return new JSONDeserializer<AuthorState>().use(null, AuthorState.class).deserialize(json);
	}

	public static String toJsonArrayStub(Collection<com.labsynch.labseer.domain.AuthorState> collection) {
		return new JSONSerializer().exclude("*.class", "author").transform(new ExcludeNulls(), void.class).serialize(collection);
	}

	public static Collection<com.labsynch.labseer.domain.AuthorState> fromJsonArrayToAuthorStates(String json) {
		return new JSONDeserializer<List<AuthorState>>().use(null, ArrayList.class).use("values", AuthorState.class).deserialize(json);
	}
	
	public String toJson() {
        return new JSONSerializer().exclude("*.class").include("lsValues").serialize(this);
    }
    
    public static String toJsonArray(Collection<AuthorState> collection) {
        return new JSONSerializer().exclude("*.class").include("lsValues").serialize(collection);
    }
	
	public static TypedQuery<AuthorState> findAuthorStatesByAuthorIDAndStateTypeKind(Long authorId, 
			String stateType, 
			String stateKind) {
			if (stateType == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateType argument is required");
			if (stateKind == null || stateKind.length() == 0) throw new IllegalArgumentException("The stateKind argument is required");
			
			EntityManager em = entityManager();
			String hsqlQuery = "SELECT ags FROM AuthorState AS ags " +
			"JOIN ags.author ag " +
			"WHERE ags.lsType = :stateType AND ags.lsKind = :stateKind AND ags.ignored IS NOT :ignored " +
			"AND ag.id = :authorId ";
			TypedQuery<AuthorState> q = em.createQuery(hsqlQuery, AuthorState.class);
			q.setParameter("authorId", authorId);
			q.setParameter("stateType", stateType);
			q.setParameter("stateKind", stateKind);
			q.setParameter("ignored", true);
			return q;
		}

}
