package com.labsynch.labseer.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity

public class Author extends AbstractThing {

	
    @NotNull
    @Size(max = 255)
    private String firstName;

    @NotNull
    @Size(max = 255)
    private String lastName;

    @NotNull
//    @Column(unique = true)
    @Size(max = 255)
    private String userName;

    @NotNull
//    @Column(unique = true)
    @Size(max = 255)
    private String emailAddress;
    
    @Size(max = 255)
    private String password;
    
    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date activationDate;

    /**
     */
    private String activationKey;

    /**
     */
    private Boolean enabled;

    /**
     */
    private Boolean locked;
        
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "userEntry", fetch =  FetchType.LAZY)
	private Set<AuthorRole> authorRoles = new HashSet<AuthorRole>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
    private Set<AuthorState> lsStates = new HashSet<AuthorState>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
    private Set<AuthorLabel> lsLabels = new HashSet<AuthorLabel>();
	
	public String toString() {
		 return new StringBuilder().append(this.getId()).append(' ').append(this.userName).toString();
//        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("password", "authorRoles").toString();
    }

//	@Id
//    @SequenceGenerator(name = "authorGen", sequenceName = "AUTHOR_PKSEQ")
//    @GeneratedValue(strategy = GenerationType.AUTO, generator = "authorGen")
//    @Column(name = "id")
//    private Long id;
//
//	@Version
//    @Column(name = "version")
//    private Integer version;

//	public Long getId() {
//        return this.id;
//    }
//
//	public void setId(Long id) {
//        this.id = id;
//    }
//
//	public Integer getVersion() {
//        return this.version;
//    }
//
//	public void setVersion(Integer version) {
//        this.version = version;
//    }

	public Author (Author author){
		this.setRecordedBy(author.getRecordedBy());
		this.setRecordedDate(author.getRecordedDate());
		this.setLsTransaction(author.getLsTransaction());
		this.setModifiedBy(author.getModifiedBy());
		this.setModifiedDate(author.getModifiedDate());
		this.setCodeName(author.getCodeName());
		this.setLsKind(author.getLsKind());
		this.setLsType(author.getLsType());
		this.setLsTypeAndKind(author.getLsTypeAndKind());
		this.setFirstName(author.getFirstName());
		this.setLastName(author.getLastName());
		this.setUserName(author.getUserName());
		this.setEmailAddress(author.getEmailAddress());
		this.setPassword(author.getPassword());
		this.setActivationDate(author.getActivationDate());
		this.setActivationKey(author.getActivationKey());
		this.setEnabled(author.getEnabled());
		this.setLocked(author.getLocked());
	}
	
	public String toJson() {
        return new JSONSerializer().exclude("*.class", "password").include("lsStates.lsValues","authorRoles","lsLabels").transform(new ExcludeNulls(), void.class).serialize(this);
    }

	public static Author fromJsonToAuthor(String json) {
        return new JSONDeserializer<Author>().use(null, Author.class).deserialize(json);
    }

	public static String toJsonArray(Collection<Author> collection) {
        return new JSONSerializer().exclude("*.class", "password").include("authorRoles","lsLabels","lsStates.lsValues").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

	public static Collection<Author> fromJsonArrayToAuthors(String json) {
        return new JSONDeserializer<List<Author>>().use(null, ArrayList.class).use("values", Author.class).deserialize(json);
    }

	@Transactional
	public static TypedQuery<Author> findAuthorsByActivationKeyAndEmailAddress(String activationKey, String emailAddress) {
        if (activationKey == null || activationKey.length() == 0) throw new IllegalArgumentException("The activationKey argument is required");
        if (emailAddress == null || emailAddress.length() == 0) throw new IllegalArgumentException("The emailAddress argument is required");
        EntityManager em = Author.entityManager();
        TypedQuery<Author> q = em.createQuery("SELECT o FROM Author AS o WHERE o.ignored = FALSE AND o.activationKey = :activationKey AND o.emailAddress = :emailAddress", Author.class);
        q.setParameter("activationKey", activationKey);
        q.setParameter("emailAddress", emailAddress);
        return q;
    }

	@Transactional
	public static TypedQuery<Author> findAuthorsByEmailAddress(String emailAddress) {
        if (emailAddress == null || emailAddress.length() == 0) throw new IllegalArgumentException("The emailAddress argument is required");
        EntityManager em = Author.entityManager();
        TypedQuery<Author> q = em.createQuery("SELECT o FROM Author AS o WHERE o.ignored = FALSE AND o.emailAddress = :emailAddress", Author.class);
        q.setParameter("emailAddress", emailAddress);
        return q;
    }

	@Transactional
	public static TypedQuery<Author> findAuthorsByUserName(String userName) {
        if (userName == null || userName.length() == 0) throw new IllegalArgumentException("The userName argument is required");
        EntityManager em = Author.entityManager();
        TypedQuery<Author> q = em.createQuery("SELECT o FROM Author AS o WHERE o.ignored = FALSE AND LOWER(o.userName) = LOWER(:userName)", Author.class);
        q.setParameter("userName", userName);
        return q;
    }

	public static Author update(Author author) {
		Author updatedAuthor = Author.findAuthor(author.getId());
		updatedAuthor.setRecordedBy(author.getRecordedBy());
		updatedAuthor.setRecordedDate(author.getRecordedDate());
		updatedAuthor.setLsTransaction(author.getLsTransaction());
		updatedAuthor.setModifiedBy(author.getModifiedBy());
		updatedAuthor.setModifiedDate(author.getModifiedDate());
		updatedAuthor.setCodeName(author.getCodeName());
		updatedAuthor.setLsKind(author.getLsKind());
		updatedAuthor.setLsType(author.getLsType());
		updatedAuthor.setLsTypeAndKind(author.getLsTypeAndKind());
		updatedAuthor.setFirstName(author.getFirstName());
		updatedAuthor.setLastName(author.getLastName());
		updatedAuthor.setUserName(author.getUserName());
		updatedAuthor.setEmailAddress(author.getEmailAddress());
		if (author.getPassword() != null) updatedAuthor.setPassword(author.getPassword());
		updatedAuthor.setActivationDate(author.getActivationDate());
		updatedAuthor.setActivationKey(author.getActivationKey());
		updatedAuthor.setEnabled(author.getEnabled());
		updatedAuthor.setLocked(author.getLocked());
		updatedAuthor.merge();
		return updatedAuthor;
	}
	

	public Author() {
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("firstName", "lastName", "userName", "emailAddress", "password", "activationDate", "activationKey", "enabled", "locked", "authorRoles", "lsStates", "lsLabels");

	public static long countAuthors() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Author o", Long.class).getSingleResult();
    }

	public static List<Author> findAllAuthors() {
        return entityManager().createQuery("SELECT o FROM Author o", Author.class).getResultList();
    }

	public static List<Author> findAllAuthors(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Author o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Author.class).getResultList();
    }

	public static Author findAuthor(Long id) {
        if (id == null) return null;
        return entityManager().find(Author.class, id);
    }

	public static List<Author> findAuthorEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Author o", Author.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<Author> findAuthorEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Author o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Author.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public Author merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Author merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String getFirstName() {
        return this.firstName;
    }

	public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

	public String getLastName() {
        return this.lastName;
    }

	public void setLastName(String lastName) {
        this.lastName = lastName;
    }

	public String getUserName() {
        return this.userName;
    }

	public void setUserName(String userName) {
        this.userName = userName;
    }

	public String getEmailAddress() {
        return this.emailAddress;
    }

	public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

	public String getPassword() {
        return this.password;
    }

	public void setPassword(String password) {
        this.password = password;
    }

	public Date getActivationDate() {
        return this.activationDate;
    }

	public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

	public String getActivationKey() {
        return this.activationKey;
    }

	public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

	public Boolean getEnabled() {
        return this.enabled;
    }

	public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

	public Boolean getLocked() {
        return this.locked;
    }

	public void setLocked(Boolean locked) {
        this.locked = locked;
    }

	public Set<AuthorRole> getAuthorRoles() {
        return this.authorRoles;
    }

	public void setAuthorRoles(Set<AuthorRole> authorRoles) {
        this.authorRoles = authorRoles;
    }

	public Set<AuthorState> getLsStates() {
        return this.lsStates;
    }

	public void setLsStates(Set<AuthorState> lsStates) {
        this.lsStates = lsStates;
    }

	public Set<AuthorLabel> getLsLabels() {
        return this.lsLabels;
    }

	public void setLsLabels(Set<AuthorLabel> lsLabels) {
        this.lsLabels = lsLabels;
    }

	public static Long countFindAuthorsByActivationKeyAndEmailAddress(String activationKey, String emailAddress) {
        if (activationKey == null || activationKey.length() == 0) throw new IllegalArgumentException("The activationKey argument is required");
        if (emailAddress == null || emailAddress.length() == 0) throw new IllegalArgumentException("The emailAddress argument is required");
        EntityManager em = Author.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Author AS o WHERE o.activationKey = :activationKey AND o.emailAddress = :emailAddress", Long.class);
        q.setParameter("activationKey", activationKey);
        q.setParameter("emailAddress", emailAddress);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindAuthorsByEmailAddress(String emailAddress) {
        if (emailAddress == null || emailAddress.length() == 0) throw new IllegalArgumentException("The emailAddress argument is required");
        EntityManager em = Author.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Author AS o WHERE o.emailAddress = :emailAddress", Long.class);
        q.setParameter("emailAddress", emailAddress);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindAuthorsByUserName(String userName) {
        if (userName == null || userName.length() == 0) throw new IllegalArgumentException("The userName argument is required");
        EntityManager em = Author.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM Author AS o WHERE o.userName = :userName", Long.class);
        q.setParameter("userName", userName);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<Author> findAuthorsByActivationKeyAndEmailAddress(String activationKey, String emailAddress, String sortFieldName, String sortOrder) {
        if (activationKey == null || activationKey.length() == 0) throw new IllegalArgumentException("The activationKey argument is required");
        if (emailAddress == null || emailAddress.length() == 0) throw new IllegalArgumentException("The emailAddress argument is required");
        EntityManager em = Author.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Author AS o WHERE o.activationKey = :activationKey AND o.emailAddress = :emailAddress");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Author> q = em.createQuery(queryBuilder.toString(), Author.class);
        q.setParameter("activationKey", activationKey);
        q.setParameter("emailAddress", emailAddress);
        return q;
    }

	public static TypedQuery<Author> findAuthorsByEmailAddress(String emailAddress, String sortFieldName, String sortOrder) {
        if (emailAddress == null || emailAddress.length() == 0) throw new IllegalArgumentException("The emailAddress argument is required");
        EntityManager em = Author.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Author AS o WHERE o.emailAddress = :emailAddress");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Author> q = em.createQuery(queryBuilder.toString(), Author.class);
        q.setParameter("emailAddress", emailAddress);
        return q;
    }

	public static TypedQuery<Author> findAuthorsByUserName(String userName, String sortFieldName, String sortOrder) {
        if (userName == null || userName.length() == 0) throw new IllegalArgumentException("The userName argument is required");
        EntityManager em = Author.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM Author AS o WHERE o.userName = :userName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<Author> q = em.createQuery(queryBuilder.toString(), Author.class);
        q.setParameter("userName", userName);
        return q;
    }
}
