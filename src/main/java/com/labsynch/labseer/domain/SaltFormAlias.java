package com.labsynch.labseer.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
import com.labsynch.labseer.dto.SaltFormAliasDTO;
import com.labsynch.labseer.exceptions.ParentNotFoundException;
import com.labsynch.labseer.exceptions.SaltFormNotFoundException;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;

@Entity
@Configurable
@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders={"findSaltFormAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals", "findSaltFormAliasesBySaltForm"})
public class SaltFormAlias {

	private static final Logger logger = LoggerFactory.getLogger(SaltFormAlias.class);
	
	@ManyToOne
    @org.hibernate.annotations.Index(name="SaltFormAlias_SaltForm_IDX")
	@JoinColumn(name = "salt_form")
	private SaltForm saltForm;
	
    private String lsType;

    private String lsKind;
    
    private String aliasName;
    
	private boolean preferred;
	
	private boolean ignored;
	
	private boolean deleted;
    
	public SaltFormAlias(){
	}
	
	public SaltFormAlias(SaltForm saltForm, String lsType, String lsKind, String aliasName, boolean preferred){
		this.saltForm = saltForm;
		this.lsType = lsType;
		this.lsKind = lsKind;
		this.aliasName = aliasName;
		this.preferred = preferred;
	}
	
	public SaltFormAlias(SaltFormAliasDTO saltFormAliasDTO) throws SaltFormNotFoundException{
		try{
			SaltForm saltForm = SaltForm.findSaltFormsByCorpNameEquals(saltFormAliasDTO.getSaltFormCorpName()).getSingleResult();
			this.saltForm = saltForm;
		}catch (Exception e){
			logger.error("SaltForm"+saltFormAliasDTO.getSaltFormCorpName()+" could not be found.",e);
			throw new SaltFormNotFoundException("SaltForm "+saltFormAliasDTO.getSaltFormCorpName()+" could not be found.");
		}
		this.lsType = saltFormAliasDTO.getLsType();
		this.lsKind = saltFormAliasDTO.getLsKind();
		this.aliasName = saltFormAliasDTO.getAliasName();
		this.preferred = saltFormAliasDTO.isPreferred();
	}

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static SaltFormAlias fromJsonToSaltFormAlias(String json) {
        return new JSONDeserializer<SaltFormAlias>()
        .use(null, SaltFormAlias.class).deserialize(json);
    }

	public static String toJsonArray(Collection<SaltFormAlias> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<SaltFormAlias> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<SaltFormAlias> fromJsonArrayToSaltFormAliases(String json) {
        return new JSONDeserializer<List<SaltFormAlias>>()
        .use("values", SaltFormAlias.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static Long countFindSaltFormAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(String aliasName, String lsType, String lsKind) {
        if (aliasName == null || aliasName.length() == 0) throw new IllegalArgumentException("The aliasName argument is required");
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = SaltFormAlias.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM SaltFormAlias AS o WHERE o.aliasName = :aliasName  AND o.lsType = :lsType  AND o.lsKind = :lsKind", Long.class);
        q.setParameter("aliasName", aliasName);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindSaltFormAliasesBySaltForm(SaltForm saltForm) {
        if (saltForm == null) throw new IllegalArgumentException("The saltForm argument is required");
        EntityManager em = SaltFormAlias.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM SaltFormAlias AS o WHERE o.saltForm = :saltForm", Long.class);
        q.setParameter("saltForm", saltForm);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<SaltFormAlias> findSaltFormAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(String aliasName, String lsType, String lsKind) {
        if (aliasName == null || aliasName.length() == 0) throw new IllegalArgumentException("The aliasName argument is required");
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = SaltFormAlias.entityManager();
        TypedQuery<SaltFormAlias> q = em.createQuery("SELECT o FROM SaltFormAlias AS o WHERE o.aliasName = :aliasName  AND o.lsType = :lsType  AND o.lsKind = :lsKind", SaltFormAlias.class);
        q.setParameter("aliasName", aliasName);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }

	public static TypedQuery<SaltFormAlias> findSaltFormAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(String aliasName, String lsType, String lsKind, String sortFieldName, String sortOrder) {
        if (aliasName == null || aliasName.length() == 0) throw new IllegalArgumentException("The aliasName argument is required");
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = SaltFormAlias.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM SaltFormAlias AS o WHERE o.aliasName = :aliasName  AND o.lsType = :lsType  AND o.lsKind = :lsKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<SaltFormAlias> q = em.createQuery(queryBuilder.toString(), SaltFormAlias.class);
        q.setParameter("aliasName", aliasName);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }

	public static TypedQuery<SaltFormAlias> findSaltFormAliasesBySaltForm(SaltForm saltForm) {
        if (saltForm == null) throw new IllegalArgumentException("The saltForm argument is required");
        EntityManager em = SaltFormAlias.entityManager();
        TypedQuery<SaltFormAlias> q = em.createQuery("SELECT o FROM SaltFormAlias AS o WHERE o.saltForm = :saltForm", SaltFormAlias.class);
        q.setParameter("saltForm", saltForm);
        return q;
    }

	public static TypedQuery<SaltFormAlias> findSaltFormAliasesBySaltForm(SaltForm saltForm, String sortFieldName, String sortOrder) {
        if (saltForm == null) throw new IllegalArgumentException("The saltForm argument is required");
        EntityManager em = SaltFormAlias.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM SaltFormAlias AS o WHERE o.saltForm = :saltForm");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<SaltFormAlias> q = em.createQuery(queryBuilder.toString(), SaltFormAlias.class);
        q.setParameter("saltForm", saltForm);
        return q;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "saltForm", "lsType", "lsKind", "aliasName", "preferred", "ignored", "deleted");

	public static final EntityManager entityManager() {
        EntityManager em = new SaltFormAlias().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSaltFormAliases() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SaltFormAlias o", Long.class).getSingleResult();
    }

	public static List<SaltFormAlias> findAllSaltFormAliases() {
        return entityManager().createQuery("SELECT o FROM SaltFormAlias o", SaltFormAlias.class).getResultList();
    }

	public static List<SaltFormAlias> findAllSaltFormAliases(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM SaltFormAlias o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, SaltFormAlias.class).getResultList();
    }

	public static SaltFormAlias findSaltFormAlias(Long id) {
        if (id == null) return null;
        return entityManager().find(SaltFormAlias.class, id);
    }

	public static List<SaltFormAlias> findSaltFormAliasEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SaltFormAlias o", SaltFormAlias.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<SaltFormAlias> findSaltFormAliasEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM SaltFormAlias o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, SaltFormAlias.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            SaltFormAlias attached = SaltFormAlias.findSaltFormAlias(this.id);
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
    public SaltFormAlias merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SaltFormAlias merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
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

	public SaltForm getSaltForm() {
        return this.saltForm;
    }

	public void setSaltForm(SaltForm saltForm) {
        this.saltForm = saltForm;
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
}
