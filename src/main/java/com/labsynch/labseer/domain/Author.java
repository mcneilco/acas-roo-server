package com.labsynch.labseer.domain;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.transaction.annotation.Transactional;

@Entity
@RooJson
@RooJavaBean
//@RooToString(excludeFields = { "password", "modifiedDate", "activationKey", "activationDate", "emailAddress", "recordedDate", "modifiedDate", "enabled", "locked" })
//@RooToString(excludeFields = { "password", "authorRoles"})

@RooJpaActiveRecord(sequenceName = "AUTHOR_PKSEQ", finders = { "findAuthorsByUserName", "findAuthorsByEmailAddress", "findAuthorsByActivationKeyAndEmailAddress" })
public class Author {

    @NotNull
    @Size(max = 255)
    private String firstName;

    @NotNull
    @Size(max = 255)
    private String lastName;

    @NotNull
    @Column(unique = true)
    @Size(max = 255)
    private String userName;

    @NotNull
    @Column(unique = true)
    @Size(max = 255)
    private String emailAddress;
    
    @NotNull
    @Size(max = 255)
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Date recordedDate;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "MM")
    private Date modifiedDate;
	

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



	public String toString() {
		 return new StringBuilder().append(this.id).append(' ').append(this.userName).toString();
//        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).setExcludeFieldNames("password", "authorRoles").toString();
    }

	@Id
    @SequenceGenerator(name = "authorGen", sequenceName = "AUTHOR_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "authorGen")
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	public String toJson() {
        return new JSONSerializer().exclude("*.class", "password").include("authorRoles").serialize(this);
    }

	public static Author fromJsonToAuthor(String json) {
        return new JSONDeserializer<Author>().use(null, Author.class).deserialize(json);
    }

	public static String toJsonArray(Collection<Author> collection) {
        return new JSONSerializer().exclude("*.class", "password").include("authorRoles").serialize(collection);
    }

	public static Collection<Author> fromJsonArrayToAuthors(String json) {
        return new JSONDeserializer<List<Author>>().use(null, ArrayList.class).use("values", Author.class).deserialize(json);
    }

	@Transactional
	public static TypedQuery<Author> findAuthorsByActivationKeyAndEmailAddress(String activationKey, String emailAddress) {
        if (activationKey == null || activationKey.length() == 0) throw new IllegalArgumentException("The activationKey argument is required");
        if (emailAddress == null || emailAddress.length() == 0) throw new IllegalArgumentException("The emailAddress argument is required");
        EntityManager em = Author.entityManager();
        TypedQuery<Author> q = em.createQuery("SELECT o FROM Author AS o WHERE o.activationKey = :activationKey AND o.emailAddress = :emailAddress", Author.class);
        q.setParameter("activationKey", activationKey);
        q.setParameter("emailAddress", emailAddress);
        return q;
    }

	@Transactional
	public static TypedQuery<Author> findAuthorsByEmailAddress(String emailAddress) {
        if (emailAddress == null || emailAddress.length() == 0) throw new IllegalArgumentException("The emailAddress argument is required");
        EntityManager em = Author.entityManager();
        TypedQuery<Author> q = em.createQuery("SELECT o FROM Author AS o WHERE o.emailAddress = :emailAddress", Author.class);
        q.setParameter("emailAddress", emailAddress);
        return q;
    }

	@Transactional
	public static TypedQuery<Author> findAuthorsByUserName(String userName) {
        if (userName == null || userName.length() == 0) throw new IllegalArgumentException("The userName argument is required");
        EntityManager em = Author.entityManager();
        TypedQuery<Author> q = em.createQuery("SELECT o FROM Author AS o WHERE o.userName = :userName", Author.class);
        q.setParameter("userName", userName);
        return q;
    }
	
}
