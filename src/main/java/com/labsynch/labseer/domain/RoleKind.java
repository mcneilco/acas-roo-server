package com.labsynch.labseer.domain;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
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
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;


@Configurable
@Entity
@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "ROLE_KIND_PKSEQ", finders={"findRoleKindsByLsType", 
		"findRoldKindsByKindNameEquals", "findRoleKindsByKindNameEqualsAndLsType"})
@RooJson
public class RoleKind {
	
    @NotNull
    @ManyToOne
    @JoinColumn(name = "ls_type")
    private RoleType lsType;

    @NotNull
    @Size(max = 255)
    private String kindName;
    
    @Column(unique = true)
    @Size(max = 255)
    private String lsTypeAndKind;
    
    @Transactional
	public void persist() {
		if (this.entityManager == null) this.entityManager = entityManager();
		this.setLsTypeAndKind(new StringBuilder().append(this.lsType.getTypeName()).append("_").append(this.kindName).toString());
		this.entityManager.persist(this);
	}
 

	public RoleType getLsType() {
        return this.lsType;
    }

	public void setLsType(RoleType lsType) {
        this.lsType = lsType;
    }

	public String getKindName() {
        return this.kindName;
    }

	public void setKindName(String kindName) {
        this.kindName = kindName;
    }

	public String getLsTypeAndKind() {
        return this.lsTypeAndKind;
    }

	public void setLsTypeAndKind(String lsTypeAndKind) {
        this.lsTypeAndKind = lsTypeAndKind;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static RoleKind fromJsonToRoleKind(String json) {
        return new JSONDeserializer<RoleKind>()
        .use(null, RoleKind.class).deserialize(json);
    }

	public static String toJsonArray(Collection<RoleKind> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<RoleKind> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<RoleKind> fromJsonArrayToRoleKinds(String json) {
        return new JSONDeserializer<List<RoleKind>>()
        .use("values", RoleKind.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("lsType", "kindName", "lsTypeAndKind");

	public static final EntityManager entityManager() {
        EntityManager em = new RoleKind().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countRoleKinds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM RoleKind o", Long.class).getSingleResult();
    }

	public static List<RoleKind> findAllRoleKinds() {
        return entityManager().createQuery("SELECT o FROM RoleKind o", RoleKind.class).getResultList();
    }

	public static List<RoleKind> findAllRoleKinds(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM RoleKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, RoleKind.class).getResultList();
    }

	public static RoleKind findRoleKind(Long id) {
        if (id == null) return null;
        return entityManager().find(RoleKind.class, id);
    }

	public static List<RoleKind> findRoleKindEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM RoleKind o", RoleKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<RoleKind> findRoleKindEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM RoleKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, RoleKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            RoleKind attached = RoleKind.findRoleKind(this.id);
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
    public RoleKind merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        RoleKind merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static Long countFindRoleKindsByKindNameEqualsAndLsType(String kindName, RoleType lsType) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = RoleKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM RoleKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType", Long.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindRoleKindsByLsType(RoleType lsType) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = RoleKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM RoleKind AS o WHERE o.lsType = :lsType", Long.class);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<RoleKind> findRoleKindsByKindNameEqualsAndLsType(String kindName, RoleType lsType) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = RoleKind.entityManager();
        TypedQuery<RoleKind> q = em.createQuery("SELECT o FROM RoleKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType", RoleKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }

	public static TypedQuery<RoleKind> findRoleKindsByKindNameEqualsAndLsType(String kindName, RoleType lsType, String sortFieldName, String sortOrder) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = RoleKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM RoleKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<RoleKind> q = em.createQuery(queryBuilder.toString(), RoleKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }

	public static TypedQuery<RoleKind> findRoleKindsByLsType(RoleType lsType) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = RoleKind.entityManager();
        TypedQuery<RoleKind> q = em.createQuery("SELECT o FROM RoleKind AS o WHERE o.lsType = :lsType", RoleKind.class);
        q.setParameter("lsType", lsType);
        return q;
    }

	public static TypedQuery<RoleKind> findRoleKindsByLsType(RoleType lsType, String sortFieldName, String sortOrder) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = RoleKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM RoleKind AS o WHERE o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<RoleKind> q = em.createQuery(queryBuilder.toString(), RoleKind.class);
        q.setParameter("lsType", lsType);
        return q;
    }

	@Id
    @SequenceGenerator(name = "roleKindGen", sequenceName = "ROLE_KIND_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "roleKindGen")
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
}
