package com.labsynch.labseer.domain;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
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
@RooJson
@RooJpaActiveRecord(finders = {"findQcCompoundsByCdId" })
public class QcCompound {

	// id, runNumber, qcDate, parentId, corpName, dupeCount, dupeCorpName, asDrawnStruct, preMolStruct, postMolStruct, comment
	private int runNumber;

	private Date qcDate;

	private Long parentId;

	private String corpName;

	private boolean displayChange;

	private int dupeCount;

	private String dupeCorpName;

	private String alias;

	private String stereoCategory;

	private String stereoComment;

	private int CdId;

	@Column(columnDefinition = "text")
	private String molStructure;

	private String comment;

	private boolean ignore;

	public QcCompound() {
	}

	public static List<Long> getAllIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	public static TypedQuery<Long> findAllIds() {
		return QcCompound.entityManager().createQuery("SELECT o.id FROM QcCompound o", Long.class);
	}

	@Transactional
	public static void truncateTable() {
		int output = QcCompound.entityManager().createNativeQuery("TRUNCATE qc_compound").executeUpdate();
	}

	public static TypedQuery<Integer> findMaxRunNumber() {
		return QcCompound.entityManager().createQuery("SELECT max(o.runNumber) FROM QcCompound o", Integer.class);
	}

	public static TypedQuery<Long> findPotentialQcCmpds() {
		String querySQL = "SELECT o.id FROM QcCompound o WHERE displayChange = true OR dupeCount > 0";
		return QcCompound.entityManager().createQuery(querySQL, Long.class);
	}
	
	public static TypedQuery<Long> findParentsWithDisplayChanges() {
		String querySQL = "SELECT o.parentId FROM QcCompound o WHERE displayChange = true";
		return QcCompound.entityManager().createQuery(querySQL, Long.class);
	}

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static QcCompound fromJsonToQcCompound(String json) {
        return new JSONDeserializer<QcCompound>()
        .use(null, QcCompound.class).deserialize(json);
    }

	public static String toJsonArray(Collection<QcCompound> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<QcCompound> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<QcCompound> fromJsonArrayToQcCompounds(String json) {
        return new JSONDeserializer<List<QcCompound>>()
        .use("values", QcCompound.class).deserialize(json);
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

	public static Long countFindQcCompoundsByCdId(int CdId) {
        EntityManager em = QcCompound.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM QcCompound AS o WHERE o.CdId = :CdId", Long.class);
        q.setParameter("CdId", CdId);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<QcCompound> findQcCompoundsByCdId(int CdId) {
        EntityManager em = QcCompound.entityManager();
        TypedQuery<QcCompound> q = em.createQuery("SELECT o FROM QcCompound AS o WHERE o.CdId = :CdId", QcCompound.class);
        q.setParameter("CdId", CdId);
        return q;
    }

	public static TypedQuery<QcCompound> findQcCompoundsByCdId(int CdId, String sortFieldName, String sortOrder) {
        EntityManager em = QcCompound.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM QcCompound AS o WHERE o.CdId = :CdId");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<QcCompound> q = em.createQuery(queryBuilder.toString(), QcCompound.class);
        q.setParameter("CdId", CdId);
        return q;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("runNumber", "qcDate", "parentId", "corpName", "displayChange", "dupeCount", "dupeCorpName", "alias", "stereoCategory", "stereoComment", "CdId", "molStructure", "comment", "ignore");

	public static final EntityManager entityManager() {
        EntityManager em = new QcCompound().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countQcCompounds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM QcCompound o", Long.class).getSingleResult();
    }

	public static List<QcCompound> findAllQcCompounds() {
        return entityManager().createQuery("SELECT o FROM QcCompound o", QcCompound.class).getResultList();
    }

	public static List<QcCompound> findAllQcCompounds(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM QcCompound o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, QcCompound.class).getResultList();
    }

	public static QcCompound findQcCompound(Long id) {
        if (id == null) return null;
        return entityManager().find(QcCompound.class, id);
    }

	public static List<QcCompound> findQcCompoundEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM QcCompound o", QcCompound.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<QcCompound> findQcCompoundEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM QcCompound o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, QcCompound.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            QcCompound attached = QcCompound.findQcCompound(this.id);
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
    public QcCompound merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        QcCompound merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public int getRunNumber() {
        return this.runNumber;
    }

	public void setRunNumber(int runNumber) {
        this.runNumber = runNumber;
    }

	public Date getQcDate() {
        return this.qcDate;
    }

	public void setQcDate(Date qcDate) {
        this.qcDate = qcDate;
    }

	public Long getParentId() {
        return this.parentId;
    }

	public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

	public String getCorpName() {
        return this.corpName;
    }

	public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

	public boolean isDisplayChange() {
        return this.displayChange;
    }

	public void setDisplayChange(boolean displayChange) {
        this.displayChange = displayChange;
    }

	public int getDupeCount() {
        return this.dupeCount;
    }

	public void setDupeCount(int dupeCount) {
        this.dupeCount = dupeCount;
    }

	public String getDupeCorpName() {
        return this.dupeCorpName;
    }

	public void setDupeCorpName(String dupeCorpName) {
        this.dupeCorpName = dupeCorpName;
    }

	public String getAlias() {
        return this.alias;
    }

	public void setAlias(String alias) {
        this.alias = alias;
    }

	public String getStereoCategory() {
        return this.stereoCategory;
    }

	public void setStereoCategory(String stereoCategory) {
        this.stereoCategory = stereoCategory;
    }

	public String getStereoComment() {
        return this.stereoComment;
    }

	public void setStereoComment(String stereoComment) {
        this.stereoComment = stereoComment;
    }

	public int getCdId() {
        return this.CdId;
    }

	public void setCdId(int CdId) {
        this.CdId = CdId;
    }

	public String getMolStructure() {
        return this.molStructure;
    }

	public void setMolStructure(String molStructure) {
        this.molStructure = molStructure;
    }

	public String getComment() {
        return this.comment;
    }

	public void setComment(String comment) {
        this.comment = comment;
    }

	public boolean isIgnore() {
        return this.ignore;
    }

	public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }
}
