package com.labsynch.labseer.domain;

import java.io.Reader;
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
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity

@Transactional
public class AuthorLabel extends AbstractLabel {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    public AuthorLabel(AuthorLabel authorLabel) {
        super.setLsType(authorLabel.getLsType());
        super.setLsKind(authorLabel.getLsKind());
        super.setLsTypeAndKind(authorLabel.getLsType() + "_" + authorLabel.getLsKind());
        super.setLabelText(authorLabel.getLabelText());
        super.setPreferred(authorLabel.isPreferred());
        super.setLsTransaction(authorLabel.getLsTransaction());
        super.setRecordedBy(authorLabel.getRecordedBy());
        super.setRecordedDate(authorLabel.getRecordedDate());
        super.setPhysicallyLabled(authorLabel.isPhysicallyLabled());
    }
 
    public static AuthorLabel update(AuthorLabel authorLabel) {
    	AuthorLabel updatedAuthorLabel = AuthorLabel.findAuthorLabel(authorLabel.getId());
    	updatedAuthorLabel.setLsType(authorLabel.getLsType());
    	updatedAuthorLabel.setLsKind(authorLabel.getLsKind());
    	updatedAuthorLabel.setLsTypeAndKind(authorLabel.getLsType() + "_" + authorLabel.getLsKind());
    	updatedAuthorLabel.setLabelText(authorLabel.getLabelText());
    	updatedAuthorLabel.setPreferred(authorLabel.isPreferred());
    	updatedAuthorLabel.setLsTransaction(authorLabel.getLsTransaction());
    	updatedAuthorLabel.setRecordedBy(authorLabel.getRecordedBy());
    	updatedAuthorLabel.setRecordedDate(authorLabel.getRecordedDate());
    	updatedAuthorLabel.setModifiedDate(new Date());
    	updatedAuthorLabel.setPhysicallyLabled(authorLabel.isPhysicallyLabled());
    	updatedAuthorLabel.merge();
    	return updatedAuthorLabel;
    }
    
    public static Collection<AuthorLabel> fromJsonArrayToAuthorLabels(String json) {
        return new JSONDeserializer<List<AuthorLabel>>().use(null, ArrayList.class).use("values", AuthorLabel.class).deserialize(json);
    }
    
    public static Collection<AuthorLabel> fromJsonArrayToAuthorLabels(Reader json) {
        return new JSONDeserializer<List<AuthorLabel>>().use(null, ArrayList.class).use("values", AuthorLabel.class).deserialize(json);
    }
    
    

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static AuthorLabel fromJsonToAuthorLabel(String json) {
        return new JSONDeserializer<AuthorLabel>()
        .use(null, AuthorLabel.class).deserialize(json);
    }

	public static String toJsonArray(Collection<AuthorLabel> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<AuthorLabel> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Long countFindAuthorLabelsByAuthor(Author author) {
        if (author == null) throw new IllegalArgumentException("The author argument is required");
        EntityManager em = AuthorLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM AuthorLabel AS o WHERE o.author = :author", Long.class);
        q.setParameter("author", author);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindAuthorLabelsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = AuthorLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM AuthorLabel AS o WHERE o.lsTransaction = :lsTransaction", Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<AuthorLabel> findAuthorLabelsByAuthor(Author author) {
        if (author == null) throw new IllegalArgumentException("The author argument is required");
        EntityManager em = AuthorLabel.entityManager();
        TypedQuery<AuthorLabel> q = em.createQuery("SELECT o FROM AuthorLabel AS o WHERE o.author = :author", AuthorLabel.class);
        q.setParameter("author", author);
        return q;
    }

	public static TypedQuery<AuthorLabel> findAuthorLabelsByAuthor(Author author, String sortFieldName, String sortOrder) {
        if (author == null) throw new IllegalArgumentException("The author argument is required");
        EntityManager em = AuthorLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AuthorLabel AS o WHERE o.author = :author");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AuthorLabel> q = em.createQuery(queryBuilder.toString(), AuthorLabel.class);
        q.setParameter("author", author);
        return q;
    }

	public static TypedQuery<AuthorLabel> findAuthorLabelsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = AuthorLabel.entityManager();
        TypedQuery<AuthorLabel> q = em.createQuery("SELECT o FROM AuthorLabel AS o WHERE o.lsTransaction = :lsTransaction", AuthorLabel.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public static TypedQuery<AuthorLabel> findAuthorLabelsByLsTransactionEquals(Long lsTransaction, String sortFieldName, String sortOrder) {
        if (lsTransaction == null) throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = AuthorLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AuthorLabel AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AuthorLabel> q = em.createQuery(queryBuilder.toString(), AuthorLabel.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

	public AuthorLabel() {
        super();
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("author");

	public static long countAuthorLabels() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AuthorLabel o", Long.class).getSingleResult();
    }

	public static List<AuthorLabel> findAllAuthorLabels() {
        return entityManager().createQuery("SELECT o FROM AuthorLabel o", AuthorLabel.class).getResultList();
    }

	public static List<AuthorLabel> findAllAuthorLabels(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM AuthorLabel o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, AuthorLabel.class).getResultList();
    }

	public static AuthorLabel findAuthorLabel(Long id) {
        if (id == null) return null;
        return entityManager().find(AuthorLabel.class, id);
    }

	public static List<AuthorLabel> findAuthorLabelEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AuthorLabel o", AuthorLabel.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<AuthorLabel> findAuthorLabelEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM AuthorLabel o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, AuthorLabel.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public AuthorLabel merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AuthorLabel merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public Author getAuthor() {
        return this.author;
    }

	public void setAuthor(Author author) {
        this.author = author;
    }
}
