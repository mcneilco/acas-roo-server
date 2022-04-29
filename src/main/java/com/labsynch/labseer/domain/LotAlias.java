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
import org.springframework.transaction.annotation.Transactional;
import com.labsynch.labseer.dto.LotAliasDTO;
import com.labsynch.labseer.exceptions.LotNotFoundException;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;

@Entity
@Configurable
public class LotAlias {

	private static final Logger logger = LoggerFactory.getLogger(LotAlias.class);
	
	@ManyToOne
    @org.hibernate.annotations.Index(name="LotAlias_Parent_IDX")
	@JoinColumn(name = "lot")
	private Lot lot;
	
    private String lsType;

    private String lsKind;
    
    private String aliasName;
    
	private boolean preferred;
	
	private boolean ignored;
	
	private boolean deleted;
    
	public LotAlias(){
	}
	
	public LotAlias(Lot lot, String lsType, String lsKind, String aliasName, boolean preferred){
		this.lot = lot;
		this.lsType = lsType;
		this.lsKind = lsKind;
		this.aliasName = aliasName;
		this.preferred = preferred;
	}
	
	public LotAlias(LotAliasDTO lotAliasDTO) throws LotNotFoundException{
		try{
			Lot lot = Lot.findLotsByCorpNameEquals(lotAliasDTO.getLotCorpName()).getSingleResult();
			this.lot = lot;
		}catch (Exception e){
			logger.error("Lot "+lotAliasDTO.getLotCorpName()+" could not be found.",e);
			throw new LotNotFoundException("Lot "+lotAliasDTO.getLotCorpName()+" could not be found.");
		}
		this.lsType = lotAliasDTO.getLsType();
		this.lsKind = lotAliasDTO.getLsKind();
		this.aliasName = lotAliasDTO.getAliasName();
		this.preferred = lotAliasDTO.isPreferred();
	}

	public Lot getLot() {
        return this.lot;
    }

	public void setLot(Lot lot) {
        this.lot = lot;
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

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static LotAlias fromJsonToLotAlias(String json) {
        return new JSONDeserializer<LotAlias>()
        .use(null, LotAlias.class).deserialize(json);
    }

	public static String toJsonArray(Collection<LotAlias> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<LotAlias> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<LotAlias> fromJsonArrayToLotAliases(String json) {
        return new JSONDeserializer<List<LotAlias>>()
        .use("values", LotAlias.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
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

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "lot", "lsType", "lsKind", "aliasName", "preferred", "ignored", "deleted");

	public static final EntityManager entityManager() {
        EntityManager em = new LotAlias().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countLotAliases() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LotAlias o", Long.class).getSingleResult();
    }

	public static List<LotAlias> findAllLotAliases() {
        return entityManager().createQuery("SELECT o FROM LotAlias o", LotAlias.class).getResultList();
    }

	public static List<LotAlias> findAllLotAliases(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LotAlias o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LotAlias.class).getResultList();
    }

	public static LotAlias findLotAlias(Long id) {
        if (id == null) return null;
        return entityManager().find(LotAlias.class, id);
    }

	public static List<LotAlias> findLotAliasEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LotAlias o", LotAlias.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<LotAlias> findLotAliasEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LotAlias o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LotAlias.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            LotAlias attached = LotAlias.findLotAlias(this.id);
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
    public LotAlias merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        LotAlias merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static Long countFindLotAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(String aliasName, String lsType, String lsKind) {
        if (aliasName == null || aliasName.length() == 0) throw new IllegalArgumentException("The aliasName argument is required");
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = LotAlias.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LotAlias AS o WHERE o.aliasName = :aliasName  AND o.lsType = :lsType  AND o.lsKind = :lsKind", Long.class);
        q.setParameter("aliasName", aliasName);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindLotAliasesByLot(Lot lot) {
        if (lot == null) throw new IllegalArgumentException("The lot argument is required");
        EntityManager em = LotAlias.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LotAlias AS o WHERE o.lot = :lot", Long.class);
        q.setParameter("lot", lot);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindLotAliasesByLotAndLsTypeEqualsAndLsKindEquals(Lot lot, String lsType, String lsKind) {
        if (lot == null) throw new IllegalArgumentException("The lot argument is required");
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = LotAlias.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LotAlias AS o WHERE o.lot = :lot AND o.lsType = :lsType  AND o.lsKind = :lsKind", Long.class);
        q.setParameter("lot", lot);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<LotAlias> findLotAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(String aliasName, String lsType, String lsKind) {
        if (aliasName == null || aliasName.length() == 0) throw new IllegalArgumentException("The aliasName argument is required");
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = LotAlias.entityManager();
        TypedQuery<LotAlias> q = em.createQuery("SELECT o FROM LotAlias AS o WHERE o.aliasName = :aliasName  AND o.lsType = :lsType  AND o.lsKind = :lsKind", LotAlias.class);
        q.setParameter("aliasName", aliasName);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }

	public static TypedQuery<LotAlias> findLotAliasesByAliasNameEqualsAndLsTypeEqualsAndLsKindEquals(String aliasName, String lsType, String lsKind, String sortFieldName, String sortOrder) {
        if (aliasName == null || aliasName.length() == 0) throw new IllegalArgumentException("The aliasName argument is required");
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = LotAlias.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LotAlias AS o WHERE o.aliasName = :aliasName  AND o.lsType = :lsType  AND o.lsKind = :lsKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LotAlias> q = em.createQuery(queryBuilder.toString(), LotAlias.class);
        q.setParameter("aliasName", aliasName);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }

	public static TypedQuery<LotAlias> findLotAliasesByLot(Lot lot) {
        if (lot == null) throw new IllegalArgumentException("The lot argument is required");
        EntityManager em = LotAlias.entityManager();
        TypedQuery<LotAlias> q = em.createQuery("SELECT o FROM LotAlias AS o WHERE o.lot = :lot", LotAlias.class);
        q.setParameter("lot", lot);
        return q;
    }

	public static TypedQuery<LotAlias> findLotAliasesByLot(Lot lot, String sortFieldName, String sortOrder) {
        if (lot == null) throw new IllegalArgumentException("The lot argument is required");
        EntityManager em = LotAlias.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LotAlias AS o WHERE o.lot = :lot");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LotAlias> q = em.createQuery(queryBuilder.toString(), LotAlias.class);
        q.setParameter("lot", lot);
        return q;
    }

	public static TypedQuery<LotAlias> findLotAliasesByLotAndLsTypeEqualsAndLsKindEquals(Lot lot, String lsType, String lsKind) {
        if (lot == null) throw new IllegalArgumentException("The lot argument is required");
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = LotAlias.entityManager();
        TypedQuery<LotAlias> q = em.createQuery("SELECT o FROM LotAlias AS o WHERE o.lot = :lot AND o.lsType = :lsType  AND o.lsKind = :lsKind", LotAlias.class);
        q.setParameter("lot", lot);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }

	public static TypedQuery<LotAlias> findLotAliasesByLotAndLsTypeEqualsAndLsKindEquals(Lot lot, String lsType, String lsKind, String sortFieldName, String sortOrder) {
        if (lot == null) throw new IllegalArgumentException("The lot argument is required");
        if (lsType == null || lsType.length() == 0) throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0) throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = LotAlias.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LotAlias AS o WHERE o.lot = :lot AND o.lsType = :lsType  AND o.lsKind = :lsKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LotAlias> q = em.createQuery(queryBuilder.toString(), LotAlias.class);
        q.setParameter("lot", lot);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }
}
