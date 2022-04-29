package com.labsynch.labseer.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.dto.FlatThingCsvDTO;
import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable

@Transactional
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


	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("author", "lsValues");

	public static long countAuthorStates() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AuthorState o", Long.class).getSingleResult();
    }

	public static List<AuthorState> findAllAuthorStates() {
        return entityManager().createQuery("SELECT o FROM AuthorState o", AuthorState.class).getResultList();
    }

	public static List<AuthorState> findAllAuthorStates(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM AuthorState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, AuthorState.class).getResultList();
    }

	public static AuthorState findAuthorState(Long id) {
        if (id == null) return null;
        return entityManager().find(AuthorState.class, id);
    }

	public static List<AuthorState> findAuthorStateEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AuthorState o", AuthorState.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<AuthorState> findAuthorStateEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM AuthorState o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, AuthorState.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public AuthorState merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AuthorState merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public AuthorState() {
        super();
    }

	public Author getAuthor() {
        return this.author;
    }

	public void setAuthor(Author author) {
        this.author = author;
    }

	public Set<AuthorValue> getLsValues() {
        return this.lsValues;
    }

	public void setLsValues(Set<AuthorValue> lsValues) {
        this.lsValues = lsValues;
    }

	public static Long countFindAuthorStatesByAuthor(Author author) {
        if (author == null) throw new IllegalArgumentException("The author argument is required");
        EntityManager em = AuthorState.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM AuthorState AS o WHERE o.author = :author", Long.class);
        q.setParameter("author", author);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindAuthorStatesByAuthorAndLsTypeEqualsAndLsKindEqualsAndIgnoredNot(Author author, String lsType, String lsKind, boolean ignored) {
        if (author == null) throw new IllegalArgumentException("The author argument is required");
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = AuthorState.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM AuthorState AS o WHERE o.author = :author AND o.lsType = :lsType  AND o.lsKind = :lsKind  AND o.ignored IS NOT :ignored", Long.class);
        q.setParameter("author", author);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindAuthorStatesByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = AuthorState.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM AuthorState AS o WHERE o.lsTransaction = :lsTransaction", Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindAuthorStatesByLsTypeAndKindEquals(String lsTypeAndKind) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = AuthorState.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM AuthorState AS o WHERE o.lsTypeAndKind = :lsTypeAndKind", Long.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindAuthorStatesByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = AuthorState.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM AuthorState AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind", Long.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<AuthorState> findAuthorStatesByAuthor(Author author) {
        if (author == null) throw new IllegalArgumentException("The author argument is required");
        EntityManager em = AuthorState.entityManager();
        TypedQuery<AuthorState> q = em.createQuery("SELECT o FROM AuthorState AS o WHERE o.author = :author", AuthorState.class);
        q.setParameter("author", author);
        return q;
    }

	public static TypedQuery<AuthorState> findAuthorStatesByAuthor(Author author, String sortFieldName, String sortOrder) {
        if (author == null) throw new IllegalArgumentException("The author argument is required");
        EntityManager em = AuthorState.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AuthorState AS o WHERE o.author = :author");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AuthorState> q = em.createQuery(queryBuilder.toString(), AuthorState.class);
        q.setParameter("author", author);
        return q;
    }

	public static TypedQuery<AuthorState> findAuthorStatesByAuthorAndLsTypeEqualsAndLsKindEqualsAndIgnoredNot(Author author, String lsType, String lsKind, boolean ignored) {
        if (author == null) throw new IllegalArgumentException("The author argument is required");
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = AuthorState.entityManager();
        TypedQuery<AuthorState> q = em.createQuery("SELECT o FROM AuthorState AS o WHERE o.author = :author AND o.lsType = :lsType  AND o.lsKind = :lsKind  AND o.ignored IS NOT :ignored", AuthorState.class);
        q.setParameter("author", author);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("ignored", ignored);
        return q;
    }

	public static TypedQuery<AuthorState> findAuthorStatesByAuthorAndLsTypeEqualsAndLsKindEqualsAndIgnoredNot(Author author, String lsType, String lsKind, boolean ignored, String sortFieldName, String sortOrder) {
        if (author == null) throw new IllegalArgumentException("The author argument is required");
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = AuthorState.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AuthorState AS o WHERE o.author = :author AND o.lsType = :lsType  AND o.lsKind = :lsKind  AND o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AuthorState> q = em.createQuery(queryBuilder.toString(), AuthorState.class);
        q.setParameter("author", author);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("ignored", ignored);
        return q;
    }

	public static TypedQuery<AuthorState> findAuthorStatesByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = AuthorState.entityManager();
        TypedQuery<AuthorState> q = em.createQuery("SELECT o FROM AuthorState AS o WHERE o.lsTransaction = :lsTransaction", AuthorState.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public static TypedQuery<AuthorState> findAuthorStatesByLsTransactionEquals(Long lsTransaction, String sortFieldName, String sortOrder) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = AuthorState.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AuthorState AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AuthorState> q = em.createQuery(queryBuilder.toString(), AuthorState.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public static TypedQuery<AuthorState> findAuthorStatesByLsTypeAndKindEquals(String lsTypeAndKind) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = AuthorState.entityManager();
        TypedQuery<AuthorState> q = em.createQuery("SELECT o FROM AuthorState AS o WHERE o.lsTypeAndKind = :lsTypeAndKind", AuthorState.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return q;
    }

	public static TypedQuery<AuthorState> findAuthorStatesByLsTypeAndKindEquals(String lsTypeAndKind, String sortFieldName, String sortOrder) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = AuthorState.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AuthorState AS o WHERE o.lsTypeAndKind = :lsTypeAndKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AuthorState> q = em.createQuery(queryBuilder.toString(), AuthorState.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return q;
    }

	public static TypedQuery<AuthorState> findAuthorStatesByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = AuthorState.entityManager();
        TypedQuery<AuthorState> q = em.createQuery("SELECT o FROM AuthorState AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind", AuthorState.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }

	public static TypedQuery<AuthorState> findAuthorStatesByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind, String sortFieldName, String sortOrder) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = AuthorState.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AuthorState AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AuthorState> q = em.createQuery(queryBuilder.toString(), AuthorState.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
