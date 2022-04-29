package com.labsynch.labseer.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable
@Table(name = "author_role", uniqueConstraints = { @javax.persistence.UniqueConstraint(columnNames = { "author_id", "lsrole_id" }) })
public class AuthorRole {
	
    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Author userEntry;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "lsrole_id", referencedColumnName = "id")
    private LsRole roleEntry;

    @Id
    @SequenceGenerator(name = "authorRoleGen", sequenceName = "AUTH_ROLE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "authorRoleGen")
    @Column(name = "id")
    private Long id;

    @Version
    @Column(name = "version")
    private Integer version;

    public String toString() {
        return new StringBuilder().append(this.id).toString();
    }

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
        return new JSONSerializer().exclude("*.class", "userEntry.password").serialize(this);
    }

    public static com.labsynch.labseer.domain.AuthorRole fromJsonToAuthorRole(String json) {
        return new JSONDeserializer<AuthorRole>().use(null, AuthorRole.class).deserialize(json);
    }

    public static String toJsonArray(Collection<com.labsynch.labseer.domain.AuthorRole> collection) {
        return new JSONSerializer().exclude("*.class", "userEntry.password").serialize(collection);
    }

    public static Collection<com.labsynch.labseer.domain.AuthorRole> fromJsonArrayToAuthorRoles(String json) {
        return new JSONDeserializer<List<AuthorRole>>().use(null, ArrayList.class).use("values", AuthorRole.class).deserialize(json);
    }

	public static AuthorRole getOrCreateAuthorRole(Author author, LsRole userRole) {
		List<AuthorRole> authorRoles = AuthorRole.findAuthorRolesByRoleEntryAndUserEntry(userRole, author).getResultList();
		if (authorRoles.size() == 0){
			AuthorRole newAuthorRole = new AuthorRole();
			newAuthorRole.setUserEntry(author);
			newAuthorRole.setRoleEntry(userRole);
			newAuthorRole.persist();
			return newAuthorRole;
		} else {
			return authorRoles.get(0);
		}
	}

	public static Long countFindAuthorRolesByRoleEntry(LsRole roleEntry) {
        if (roleEntry == null) throw new IllegalArgumentException("The roleEntry argument is required");
        EntityManager em = AuthorRole.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM AuthorRole AS o WHERE o.roleEntry = :roleEntry", Long.class);
        q.setParameter("roleEntry", roleEntry);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindAuthorRolesByRoleEntryAndUserEntry(LsRole roleEntry, Author userEntry) {
        if (roleEntry == null) throw new IllegalArgumentException("The roleEntry argument is required");
        if (userEntry == null) throw new IllegalArgumentException("The userEntry argument is required");
        EntityManager em = AuthorRole.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM AuthorRole AS o WHERE o.roleEntry = :roleEntry AND o.userEntry = :userEntry", Long.class);
        q.setParameter("roleEntry", roleEntry);
        q.setParameter("userEntry", userEntry);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindAuthorRolesByUserEntry(Author userEntry) {
        if (userEntry == null) throw new IllegalArgumentException("The userEntry argument is required");
        EntityManager em = AuthorRole.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM AuthorRole AS o WHERE o.userEntry = :userEntry", Long.class);
        q.setParameter("userEntry", userEntry);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<AuthorRole> findAuthorRolesByRoleEntry(LsRole roleEntry) {
        if (roleEntry == null) throw new IllegalArgumentException("The roleEntry argument is required");
        EntityManager em = AuthorRole.entityManager();
        TypedQuery<AuthorRole> q = em.createQuery("SELECT o FROM AuthorRole AS o WHERE o.roleEntry = :roleEntry", AuthorRole.class);
        q.setParameter("roleEntry", roleEntry);
        return q;
    }

	public static TypedQuery<AuthorRole> findAuthorRolesByRoleEntry(LsRole roleEntry, String sortFieldName, String sortOrder) {
        if (roleEntry == null) throw new IllegalArgumentException("The roleEntry argument is required");
        EntityManager em = AuthorRole.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AuthorRole AS o WHERE o.roleEntry = :roleEntry");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AuthorRole> q = em.createQuery(queryBuilder.toString(), AuthorRole.class);
        q.setParameter("roleEntry", roleEntry);
        return q;
    }

	public static TypedQuery<AuthorRole> findAuthorRolesByRoleEntryAndUserEntry(LsRole roleEntry, Author userEntry) {
        if (roleEntry == null) throw new IllegalArgumentException("The roleEntry argument is required");
        if (userEntry == null) throw new IllegalArgumentException("The userEntry argument is required");
        EntityManager em = AuthorRole.entityManager();
        TypedQuery<AuthorRole> q = em.createQuery("SELECT o FROM AuthorRole AS o WHERE o.roleEntry = :roleEntry AND o.userEntry = :userEntry", AuthorRole.class);
        q.setParameter("roleEntry", roleEntry);
        q.setParameter("userEntry", userEntry);
        return q;
    }

	public static TypedQuery<AuthorRole> findAuthorRolesByRoleEntryAndUserEntry(LsRole roleEntry, Author userEntry, String sortFieldName, String sortOrder) {
        if (roleEntry == null) throw new IllegalArgumentException("The roleEntry argument is required");
        if (userEntry == null) throw new IllegalArgumentException("The userEntry argument is required");
        EntityManager em = AuthorRole.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AuthorRole AS o WHERE o.roleEntry = :roleEntry AND o.userEntry = :userEntry");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AuthorRole> q = em.createQuery(queryBuilder.toString(), AuthorRole.class);
        q.setParameter("roleEntry", roleEntry);
        q.setParameter("userEntry", userEntry);
        return q;
    }

	public static TypedQuery<AuthorRole> findAuthorRolesByUserEntry(Author userEntry) {
        if (userEntry == null) throw new IllegalArgumentException("The userEntry argument is required");
        EntityManager em = AuthorRole.entityManager();
        TypedQuery<AuthorRole> q = em.createQuery("SELECT o FROM AuthorRole AS o WHERE o.userEntry = :userEntry", AuthorRole.class);
        q.setParameter("userEntry", userEntry);
        return q;
    }

	public static TypedQuery<AuthorRole> findAuthorRolesByUserEntry(Author userEntry, String sortFieldName, String sortOrder) {
        if (userEntry == null) throw new IllegalArgumentException("The userEntry argument is required");
        EntityManager em = AuthorRole.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM AuthorRole AS o WHERE o.userEntry = :userEntry");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<AuthorRole> q = em.createQuery(queryBuilder.toString(), AuthorRole.class);
        q.setParameter("userEntry", userEntry);
        return q;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("userEntry", "roleEntry", "id", "version");

	public static final EntityManager entityManager() {
        EntityManager em = new AuthorRole().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countAuthorRoles() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AuthorRole o", Long.class).getSingleResult();
    }

	public static List<AuthorRole> findAllAuthorRoles() {
        return entityManager().createQuery("SELECT o FROM AuthorRole o", AuthorRole.class).getResultList();
    }

	public static List<AuthorRole> findAllAuthorRoles(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM AuthorRole o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, AuthorRole.class).getResultList();
    }

	public static AuthorRole findAuthorRole(Long id) {
        if (id == null) return null;
        return entityManager().find(AuthorRole.class, id);
    }

	public static List<AuthorRole> findAuthorRoleEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AuthorRole o", AuthorRole.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<AuthorRole> findAuthorRoleEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM AuthorRole o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, AuthorRole.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            AuthorRole attached = AuthorRole.findAuthorRole(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public AuthorRole merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AuthorRole merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public Author getUserEntry() {
        return this.userEntry;
    }

	public void setUserEntry(Author userEntry) {
        this.userEntry = userEntry;
    }

	public LsRole getRoleEntry() {
        return this.roleEntry;
    }

	public void setRoleEntry(LsRole roleEntry) {
        this.roleEntry = roleEntry;
    }
}
