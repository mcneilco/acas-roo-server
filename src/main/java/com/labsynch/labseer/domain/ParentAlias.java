package com.labsynch.labseer.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;
import com.labsynch.labseer.dto.ParentAliasDTO;
import com.labsynch.labseer.exceptions.ParentNotFoundException;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;

@Configurable
@Entity
@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders={"findParentAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals", 
		"findParentAliasesByParent", "findParentAliasesByAliasNameEquals", 
		"findParentAliasesByParentAndLsTypeEqualsAndLsKindEquals",
		"findParentAliasesByParentAndLsTypeEqualsAndLsKindEqualsAndAliasNameEquals"})
public class ParentAlias {

	private static final Logger logger = LoggerFactory.getLogger(ParentAlias.class);
	
	@ManyToOne
    @org.hibernate.annotations.Index(name="ParentAlias_Parent_IDX")
	private Parent parent;
	
    private String lsType;

    private String lsKind;
    
    private String aliasName;
    
	private boolean preferred;
	
	private boolean ignored;
	
	private boolean deleted;
	
	private Integer sortId;
	

	public ParentAlias(){
	}
	
	public ParentAlias(Parent parent, String lsType, String lsKind, String aliasName, boolean preferred){
		this.parent = parent;
		this.lsType = lsType;
		this.lsKind = lsKind;
		this.aliasName = aliasName;
		this.preferred = preferred;
	}
	
	public ParentAlias(ParentAliasDTO parentAliasDTO) throws ParentNotFoundException{
		try{
			Parent parent = Parent.findParentsByCorpNameEquals(parentAliasDTO.getParentCorpName()).getSingleResult();
			this.parent = parent;
		}catch (Exception e){
			logger.error("Parent "+parentAliasDTO.getParentCorpName()+" could not be found.",e);
			throw new ParentNotFoundException("Parent "+parentAliasDTO.getParentCorpName()+" could not be found.");
		}
		this.lsType = parentAliasDTO.getLsType();
		this.lsKind = parentAliasDTO.getLsKind();
		this.aliasName = parentAliasDTO.getAliasName();
		this.preferred = parentAliasDTO.isPreferred();
		this.ignored = parentAliasDTO.getIgnored();
		this.setId(parentAliasDTO.getId());
		this.setVersion(parentAliasDTO.getVersion());
	}

	public static Long countFindParentAliasesByAliasNameEquals(String aliasName) {
        if (aliasName == null || aliasName.length() == 0) throw new IllegalArgumentException("The aliasName argument is required");
        EntityManager em = ParentAlias.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ParentAlias AS o WHERE o.aliasName = :aliasName", Long.class);
        q.setParameter("aliasName", aliasName);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindParentAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(String aliasName, String lsType, String lsKind) {
        if (aliasName == null || aliasName.length() == 0) throw new IllegalArgumentException("The aliasName argument is required");
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = ParentAlias.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ParentAlias AS o WHERE o.aliasName = :aliasName  AND o.lsType = :lsType  AND o.lsKind = :lsKind", Long.class);
        q.setParameter("aliasName", aliasName);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindParentAliasesByParent(Parent parent) {
        if (parent == null) throw new IllegalArgumentException("The parent argument is required");
        EntityManager em = ParentAlias.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ParentAlias AS o WHERE o.parent = :parent", Long.class);
        q.setParameter("parent", parent);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindParentAliasesByParentAndLsTypeEqualsAndLsKindEquals(Parent parent, String lsType, String lsKind) {
        if (parent == null) throw new IllegalArgumentException("The parent argument is required");
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = ParentAlias.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ParentAlias AS o WHERE o.parent = :parent AND o.lsType = :lsType  AND o.lsKind = :lsKind", Long.class);
        q.setParameter("parent", parent);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindParentAliasesByParentAndLsTypeEqualsAndLsKindEqualsAndAliasNameEquals(Parent parent, String lsType, String lsKind, String aliasName) {
        if (parent == null) throw new IllegalArgumentException("The parent argument is required");
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (aliasName == null || aliasName.length() == 0) throw new IllegalArgumentException("The aliasName argument is required");
        EntityManager em = ParentAlias.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ParentAlias AS o WHERE o.parent = :parent AND o.lsType = :lsType  AND o.lsKind = :lsKind  AND o.aliasName = :aliasName", Long.class);
        q.setParameter("parent", parent);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("aliasName", aliasName);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<ParentAlias> findParentAliasesByAliasNameEquals(String aliasName) {
        if (aliasName == null || aliasName.length() == 0) throw new IllegalArgumentException("The aliasName argument is required");
        EntityManager em = ParentAlias.entityManager();
        TypedQuery<ParentAlias> q = em.createQuery("SELECT o FROM ParentAlias AS o WHERE o.aliasName = :aliasName", ParentAlias.class);
        q.setParameter("aliasName", aliasName);
        return q;
    }

	public static TypedQuery<ParentAlias> findParentAliasesByAliasNameEquals(String aliasName, String sortFieldName, String sortOrder) {
        if (aliasName == null || aliasName.length() == 0) throw new IllegalArgumentException("The aliasName argument is required");
        EntityManager em = ParentAlias.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ParentAlias AS o WHERE o.aliasName = :aliasName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ParentAlias> q = em.createQuery(queryBuilder.toString(), ParentAlias.class);
        q.setParameter("aliasName", aliasName);
        return q;
    }

	public static TypedQuery<ParentAlias> findParentAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(String aliasName, String lsType, String lsKind) {
        if (aliasName == null || aliasName.length() == 0) throw new IllegalArgumentException("The aliasName argument is required");
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = ParentAlias.entityManager();
        TypedQuery<ParentAlias> q = em.createQuery("SELECT o FROM ParentAlias AS o WHERE o.aliasName = :aliasName  AND o.lsType = :lsType  AND o.lsKind = :lsKind", ParentAlias.class);
        q.setParameter("aliasName", aliasName);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }

	public static TypedQuery<ParentAlias> findParentAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(String aliasName, String lsType, String lsKind, String sortFieldName, String sortOrder) {
        if (aliasName == null || aliasName.length() == 0) throw new IllegalArgumentException("The aliasName argument is required");
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = ParentAlias.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ParentAlias AS o WHERE o.aliasName = :aliasName  AND o.lsType = :lsType  AND o.lsKind = :lsKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ParentAlias> q = em.createQuery(queryBuilder.toString(), ParentAlias.class);
        q.setParameter("aliasName", aliasName);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }

	public static TypedQuery<ParentAlias> findParentAliasesByParent(Parent parent) {
        if (parent == null) throw new IllegalArgumentException("The parent argument is required");
        EntityManager em = ParentAlias.entityManager();
        TypedQuery<ParentAlias> q = em.createQuery("SELECT o FROM ParentAlias AS o WHERE o.parent = :parent", ParentAlias.class);
        q.setParameter("parent", parent);
        return q;
    }

	public static TypedQuery<ParentAlias> findParentAliasesByParent(Parent parent, String sortFieldName, String sortOrder) {
        if (parent == null) throw new IllegalArgumentException("The parent argument is required");
        EntityManager em = ParentAlias.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ParentAlias AS o WHERE o.parent = :parent");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ParentAlias> q = em.createQuery(queryBuilder.toString(), ParentAlias.class);
        q.setParameter("parent", parent);
        return q;
    }

	public static TypedQuery<ParentAlias> findParentAliasesByParentAndLsTypeEqualsAndLsKindEquals(Parent parent, String lsType, String lsKind) {
        if (parent == null) throw new IllegalArgumentException("The parent argument is required");
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = ParentAlias.entityManager();
        TypedQuery<ParentAlias> q = em.createQuery("SELECT o FROM ParentAlias AS o WHERE o.parent = :parent AND o.lsType = :lsType  AND o.lsKind = :lsKind", ParentAlias.class);
        q.setParameter("parent", parent);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }

	public static TypedQuery<ParentAlias> findParentAliasesByParentAndLsTypeEqualsAndLsKindEquals(Parent parent, String lsType, String lsKind, String sortFieldName, String sortOrder) {
        if (parent == null) throw new IllegalArgumentException("The parent argument is required");
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = ParentAlias.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ParentAlias AS o WHERE o.parent = :parent AND o.lsType = :lsType  AND o.lsKind = :lsKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ParentAlias> q = em.createQuery(queryBuilder.toString(), ParentAlias.class);
        q.setParameter("parent", parent);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }

	public static TypedQuery<ParentAlias> findParentAliasesByParentAndLsTypeEqualsAndLsKindEqualsAndAliasNameEquals(Parent parent, String lsType, String lsKind, String aliasName) {
        if (parent == null) throw new IllegalArgumentException("The parent argument is required");
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (aliasName == null || aliasName.length() == 0) throw new IllegalArgumentException("The aliasName argument is required");
        EntityManager em = ParentAlias.entityManager();
        TypedQuery<ParentAlias> q = em.createQuery("SELECT o FROM ParentAlias AS o WHERE o.parent = :parent AND o.lsType = :lsType  AND o.lsKind = :lsKind  AND o.aliasName = :aliasName", ParentAlias.class);
        q.setParameter("parent", parent);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("aliasName", aliasName);
        return q;
    }

	public static TypedQuery<ParentAlias> findParentAliasesByParentAndLsTypeEqualsAndLsKindEqualsAndAliasNameEquals(Parent parent, String lsType, String lsKind, String aliasName, String sortFieldName, String sortOrder) {
        if (parent == null) throw new IllegalArgumentException("The parent argument is required");
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        if (aliasName == null || aliasName.length() == 0) throw new IllegalArgumentException("The aliasName argument is required");
        EntityManager em = ParentAlias.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ParentAlias AS o WHERE o.parent = :parent AND o.lsType = :lsType  AND o.lsKind = :lsKind  AND o.aliasName = :aliasName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ParentAlias> q = em.createQuery(queryBuilder.toString(), ParentAlias.class);
        q.setParameter("parent", parent);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("aliasName", aliasName);
        return q;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "parent", "lsType", "lsKind", "aliasName", "preferred", "ignored", "deleted", "sortId");

	public static final EntityManager entityManager() {
        EntityManager em = new ParentAlias().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countParentAliases() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ParentAlias o", Long.class).getSingleResult();
    }

	public static List<ParentAlias> findAllParentAliases() {
        return entityManager().createQuery("SELECT o FROM ParentAlias o", ParentAlias.class).getResultList();
    }

	public static List<ParentAlias> findAllParentAliases(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ParentAlias o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ParentAlias.class).getResultList();
    }

	public static ParentAlias findParentAlias(Long id) {
        if (id == null) return null;
        return entityManager().find(ParentAlias.class, id);
    }

	public static List<ParentAlias> findParentAliasEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ParentAlias o", ParentAlias.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<ParentAlias> findParentAliasEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ParentAlias o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ParentAlias.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            ParentAlias attached = ParentAlias.findParentAlias(this.id);
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
    public ParentAlias merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ParentAlias merged = this.entityManager.merge(this);
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

	public static ParentAlias fromJsonToParentAlias(String json) {
        return new JSONDeserializer<ParentAlias>()
        .use(null, ParentAlias.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ParentAlias> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ParentAlias> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ParentAlias> fromJsonArrayToParentAliases(String json) {
        return new JSONDeserializer<List<ParentAlias>>()
        .use("values", ParentAlias.class).deserialize(json);
    }

	public Parent getParent() {
        return this.parent;
    }

	public void setParent(Parent parent) {
        this.parent = parent;
    }

	public String getLsType() {
        return this.lsType;
    }

	public void setLsType(String lsType) {
        this.lsType = lsType;
    }

	public String getLsKind() {
        return this.lsKind;
    }

	public void setLsKind(String lsKind) {
        this.lsKind = lsKind;
    }

	public String getAliasName() {
        return this.aliasName;
    }

	public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

	public boolean isPreferred() {
        return this.preferred;
    }

	public void setPreferred(boolean preferred) {
        this.preferred = preferred;
    }

	public boolean isIgnored() {
        return this.ignored;
    }

	public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

	public boolean isDeleted() {
        return this.deleted;
    }

	public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

	public Integer getSortId() {
        return this.sortId;
    }

	public void setSortId(Integer sortId) {
        this.sortId = sortId;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
