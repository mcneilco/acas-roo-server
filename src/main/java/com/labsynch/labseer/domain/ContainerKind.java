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

@Entity
@Configurable
@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "CONTAINER_KIND_PKSEQ", finders={"findContainerKindsByKindNameEqualsAndLsType"})
@RooJson
public class ContainerKind {

	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "ls_type")
	private ContainerType lsType;

	@NotNull
	@Size(max = 255)
	private String kindName;

	@Column(unique = true)
	@Size(max = 255)
	private String lsTypeAndKind;

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new ContainerKind().entityManager;
		if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	@Transactional
	public void persist() {
		if (this.entityManager == null) this.entityManager = entityManager();
		this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
		this.entityManager.persist(this);
	}

	@Transactional
	public ContainerKind merge() {
		if (this.entityManager == null) this.entityManager = entityManager();
		this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
		ContainerKind merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}


	public static Long countFindContainerKindsByKindNameEqualsAndLsType(String kindName, ContainerType lsType) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = ContainerKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ContainerKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType", Long.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<ContainerKind> findContainerKindsByKindNameEqualsAndLsType(String kindName, ContainerType lsType) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = ContainerKind.entityManager();
        TypedQuery<ContainerKind> q = em.createQuery("SELECT o FROM ContainerKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType", ContainerKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }

	public static TypedQuery<ContainerKind> findContainerKindsByKindNameEqualsAndLsType(String kindName, ContainerType lsType, String sortFieldName, String sortOrder) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = ContainerKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ContainerKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ContainerKind> q = em.createQuery(queryBuilder.toString(), ContainerKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }

	@Id
    @SequenceGenerator(name = "containerKindGen", sequenceName = "CONTAINER_KIND_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "containerKindGen")
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
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static ContainerKind fromJsonToContainerKind(String json) {
        return new JSONDeserializer<ContainerKind>()
        .use(null, ContainerKind.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ContainerKind> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ContainerKind> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ContainerKind> fromJsonArrayToContainerKinds(String json) {
        return new JSONDeserializer<List<ContainerKind>>()
        .use("values", ContainerKind.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("lsType", "kindName", "lsTypeAndKind", "entityManager");

	public static long countContainerKinds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ContainerKind o", Long.class).getSingleResult();
    }

	public static List<ContainerKind> findAllContainerKinds() {
        return entityManager().createQuery("SELECT o FROM ContainerKind o", ContainerKind.class).getResultList();
    }

	public static List<ContainerKind> findAllContainerKinds(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ContainerKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ContainerKind.class).getResultList();
    }

	public static ContainerKind findContainerKind(Long id) {
        if (id == null) return null;
        return entityManager().find(ContainerKind.class, id);
    }

	public static List<ContainerKind> findContainerKindEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ContainerKind o", ContainerKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<ContainerKind> findContainerKindEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ContainerKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ContainerKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            ContainerKind attached = ContainerKind.findContainerKind(this.id);
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

	public ContainerType getLsType() {
        return this.lsType;
    }

	public void setLsType(ContainerType lsType) {
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
}
