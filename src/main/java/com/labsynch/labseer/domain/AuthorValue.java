package com.labsynch.labseer.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity

public class AuthorValue extends AbstractValue {

    private static final Logger logger = LoggerFactory.getLogger(AuthorValue.class);

    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_state_id")
    private AuthorState lsState;

//	public Long getStateId() {
//		return this.lsState.getId();
//	}
//	
//	public boolean getIgnored() {
//		return this.isIgnored();
//	}
//	
//	public boolean getDeleted() {
//		return this.isDeleted();
//	}
//	
//	public boolean getPublicData() {
//		return this.isPublicData();
//	}
//	
//	public String getStateType() {
//		return this.lsState.getLsType();
//	}
//	
//	public String getStateKind() {
//		return this.lsState.getLsKind();
//	}
//	
//	public Long getAuthorId() {
//		return this.lsState.getAuthor().getId();
//	}
	
//	public String getAuthorCode() {
//		return this.lsState.getAuthor().getCodeName();
//	}
	

    public static long countAuthorValues() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AuthorValue o", Long.class).getSingleResult();
    }
    
    public static AuthorValue create(AuthorValue authorValue) {
    	AuthorValue newAuthorValue = new JSONDeserializer<AuthorValue>().use(null, AuthorValue.class).
        		deserializeInto(authorValue.toJson(), 
        				new AuthorValue());	
    
        return newAuthorValue;
    }
    
    public static AuthorValue update(AuthorValue authorValue) {
        AuthorValue updatedAuthorValue = new JSONDeserializer<AuthorValue>().use(null, ArrayList.class).use("values", AuthorValue.class).deserializeInto(authorValue.toJson(), AuthorValue.findAuthorValue(authorValue.getId()));
        updatedAuthorValue.setModifiedDate(new Date());
        updatedAuthorValue.merge();
        return updatedAuthorValue;
    }
	

	public static Long countFindAuthorValuesByCodeValueEquals(String codeValue) {
        if (codeValue == null || codeValue.length() == 0) throw new IllegalArgumentException("The codeValue argument is required");
        EntityManager em = AuthorValue.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM AuthorValue AS o WHERE o.codeValue = :codeValue", Long.class);
        q.setParameter("codeValue", codeValue);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindAuthorValuesByIgnoredNotAndCodeValueEquals(boolean ignored, String codeValue) {
        if (codeValue == null || codeValue.length() == 0) throw new IllegalArgumentException("The codeValue argument is required");
        EntityManager em = AuthorValue.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM AuthorValue AS o WHERE o.ignored IS NOT :ignored  AND o.codeValue = :codeValue", Long.class);
        q.setParameter("ignored", ignored);
        q.setParameter("codeValue", codeValue);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindAuthorValuesByLsState(AuthorState lsState) {
        if (lsState == null) throw new IllegalArgumentException("The lsState argument is required");
        EntityManager em = AuthorValue.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM AuthorValue AS o WHERE o.lsState = :lsState", Long.class);
        q.setParameter("lsState", lsState);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindAuthorValuesByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = AuthorValue.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM AuthorValue AS o WHERE o.lsTransaction = :lsTransaction", Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindAuthorValuesByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = AuthorValue.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM AuthorValue AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind", Long.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindAuthorValuesByLsTypeEqualsAndLsKindEqualsAndStringValueLikeAndIgnoredNot(String lsType, String lsKind, String stringValue, boolean ignored) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (stringValue == null || stringValue.length() == 0) throw new IllegalArgumentException("The stringValue argument is required");
        stringValue = stringValue.replace('*', '%');
        if (stringValue.charAt(0) != '%') {
            stringValue = "%" + stringValue;
        }
        if (stringValue.charAt(stringValue.length() - 1) != '%') {
            stringValue = stringValue + "%";
        }
        EntityManager em = AuthorValue.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM AuthorValue AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind  AND LOWER(o.stringValue) LIKE LOWER(:stringValue)  AND o.ignored IS NOT :ignored", Long.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("stringValue", stringValue);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<AuthorValue> findAuthorValuesByCodeValueEquals(String codeValue) {
        if (codeValue == null || codeValue.length() == 0) throw new IllegalArgumentException("The codeValue argument is required");
        EntityManager em = AuthorValue.entityManager();
        TypedQuery<AuthorValue> q = em.createQuery("SELECT o FROM AuthorValue AS o WHERE o.codeValue = :codeValue", AuthorValue.class);
        q.setParameter("codeValue", codeValue);
        return q;
    }

	public static TypedQuery<AuthorValue> findAuthorValuesByCodeValueEquals(String codeValue, String sortFieldName, String sortOrder) {
        if (codeValue == null || codeValue.length() == 0) throw new IllegalArgumentException("The codeValue argument is required");
        EntityManager em = AuthorValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AuthorValue AS o WHERE o.codeValue = :codeValue");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AuthorValue> q = em.createQuery(queryBuilder.toString(), AuthorValue.class);
        q.setParameter("codeValue", codeValue);
        return q;
    }

	public static TypedQuery<AuthorValue> findAuthorValuesByIgnoredNotAndCodeValueEquals(boolean ignored, String codeValue) {
        if (codeValue == null || codeValue.length() == 0) throw new IllegalArgumentException("The codeValue argument is required");
        EntityManager em = AuthorValue.entityManager();
        TypedQuery<AuthorValue> q = em.createQuery("SELECT o FROM AuthorValue AS o WHERE o.ignored IS NOT :ignored  AND o.codeValue = :codeValue", AuthorValue.class);
        q.setParameter("ignored", ignored);
        q.setParameter("codeValue", codeValue);
        return q;
    }

	public static TypedQuery<AuthorValue> findAuthorValuesByIgnoredNotAndCodeValueEquals(boolean ignored, String codeValue, String sortFieldName, String sortOrder) {
        if (codeValue == null || codeValue.length() == 0) throw new IllegalArgumentException("The codeValue argument is required");
        EntityManager em = AuthorValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AuthorValue AS o WHERE o.ignored IS NOT :ignored  AND o.codeValue = :codeValue");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AuthorValue> q = em.createQuery(queryBuilder.toString(), AuthorValue.class);
        q.setParameter("ignored", ignored);
        q.setParameter("codeValue", codeValue);
        return q;
    }

	public static TypedQuery<AuthorValue> findAuthorValuesByLsState(AuthorState lsState) {
        if (lsState == null) throw new IllegalArgumentException("The lsState argument is required");
        EntityManager em = AuthorValue.entityManager();
        TypedQuery<AuthorValue> q = em.createQuery("SELECT o FROM AuthorValue AS o WHERE o.lsState = :lsState", AuthorValue.class);
        q.setParameter("lsState", lsState);
        return q;
    }

	public static TypedQuery<AuthorValue> findAuthorValuesByLsState(AuthorState lsState, String sortFieldName, String sortOrder) {
        if (lsState == null) throw new IllegalArgumentException("The lsState argument is required");
        EntityManager em = AuthorValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AuthorValue AS o WHERE o.lsState = :lsState");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AuthorValue> q = em.createQuery(queryBuilder.toString(), AuthorValue.class);
        q.setParameter("lsState", lsState);
        return q;
    }

	public static TypedQuery<AuthorValue> findAuthorValuesByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = AuthorValue.entityManager();
        TypedQuery<AuthorValue> q = em.createQuery("SELECT o FROM AuthorValue AS o WHERE o.lsTransaction = :lsTransaction", AuthorValue.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public static TypedQuery<AuthorValue> findAuthorValuesByLsTransactionEquals(Long lsTransaction, String sortFieldName, String sortOrder) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = AuthorValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AuthorValue AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AuthorValue> q = em.createQuery(queryBuilder.toString(), AuthorValue.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public static TypedQuery<AuthorValue> findAuthorValuesByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = AuthorValue.entityManager();
        TypedQuery<AuthorValue> q = em.createQuery("SELECT o FROM AuthorValue AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind", AuthorValue.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }

	public static TypedQuery<AuthorValue> findAuthorValuesByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind, String sortFieldName, String sortOrder) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = AuthorValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AuthorValue AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AuthorValue> q = em.createQuery(queryBuilder.toString(), AuthorValue.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }

	public static TypedQuery<AuthorValue> findAuthorValuesByLsTypeEqualsAndLsKindEqualsAndStringValueLikeAndIgnoredNot(String lsType, String lsKind, String stringValue, boolean ignored) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (stringValue == null || stringValue.length() == 0) throw new IllegalArgumentException("The stringValue argument is required");
        stringValue = stringValue.replace('*', '%');
        if (stringValue.charAt(0) != '%') {
            stringValue = "%" + stringValue;
        }
        if (stringValue.charAt(stringValue.length() - 1) != '%') {
            stringValue = stringValue + "%";
        }
        EntityManager em = AuthorValue.entityManager();
        TypedQuery<AuthorValue> q = em.createQuery("SELECT o FROM AuthorValue AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind  AND LOWER(o.stringValue) LIKE LOWER(:stringValue)  AND o.ignored IS NOT :ignored", AuthorValue.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("stringValue", stringValue);
        q.setParameter("ignored", ignored);
        return q;
    }

	public static TypedQuery<AuthorValue> findAuthorValuesByLsTypeEqualsAndLsKindEqualsAndStringValueLikeAndIgnoredNot(String lsType, String lsKind, String stringValue, boolean ignored, String sortFieldName, String sortOrder) {
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (stringValue == null || stringValue.length() == 0) throw new IllegalArgumentException("The stringValue argument is required");
        stringValue = stringValue.replace('*', '%');
        if (stringValue.charAt(0) != '%') {
            stringValue = "%" + stringValue;
        }
        if (stringValue.charAt(stringValue.length() - 1) != '%') {
            stringValue = stringValue + "%";
        }
        EntityManager em = AuthorValue.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AuthorValue AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind  AND LOWER(o.stringValue) LIKE LOWER(:stringValue)  AND o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AuthorValue> q = em.createQuery(queryBuilder.toString(), AuthorValue.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("stringValue", stringValue);
        q.setParameter("ignored", ignored);
        return q;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "lsState");

	public static List<AuthorValue> findAllAuthorValues() {
        return entityManager().createQuery("SELECT o FROM AuthorValue o", AuthorValue.class).getResultList();
    }

	public static List<AuthorValue> findAllAuthorValues(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM AuthorValue o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, AuthorValue.class).getResultList();
    }

	public static AuthorValue findAuthorValue(Long id) {
        if (id == null) return null;
        return entityManager().find(AuthorValue.class, id);
    }

	public static List<AuthorValue> findAuthorValueEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AuthorValue o", AuthorValue.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<AuthorValue> findAuthorValueEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM AuthorValue o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, AuthorValue.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public AuthorValue merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AuthorValue merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static AuthorValue fromJsonToAuthorValue(String json) {
        return new JSONDeserializer<AuthorValue>()
        .use(null, AuthorValue.class).deserialize(json);
    }

	public static String toJsonArray(Collection<AuthorValue> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<AuthorValue> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<AuthorValue> fromJsonArrayToAuthorValues(String json) {
        return new JSONDeserializer<List<AuthorValue>>()
        .use("values", AuthorValue.class).deserialize(json);
    }

	public AuthorState getLsState() {
        return this.lsState;
    }

	public void setLsState(AuthorState lsState) {
        this.lsState = lsState;
    }
}
