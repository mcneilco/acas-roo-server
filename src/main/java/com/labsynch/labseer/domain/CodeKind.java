package com.labsynch.labseer.domain;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity

public class CodeKind {

	private static final Logger logger = LoggerFactory.getLogger(CodeKind.class);

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ls_type")
    private CodeType lsType;

    @NotNull
    @Size(max = 255)
    private String kindName;

    @Column(unique = true)
    @Size(max = 255)
    private String lsTypeAndKind;

    @Id
    @SequenceGenerator(name = "codeKindGen", sequenceName = "CODE_KIND_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "codeKindGen")
    @Column(name = "id")
    private Long id;

    @Version
    @Column(name = "version")
    private Integer version;

    @PersistenceContext
    transient EntityManager entityManager;

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

    public static final EntityManager entityManager() {
        EntityManager em = new CodeKind().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countCodeKinds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM CodeKind o", Long.class).getSingleResult();
    }

    public static List<com.labsynch.labseer.domain.CodeKind> findAllCodeKinds() {
        return entityManager().createQuery("SELECT o FROM CodeKind o", CodeKind.class).getResultList();
    }

    public static com.labsynch.labseer.domain.CodeKind findCodeKind(Long id) {
        if (id == null) return null;
        return entityManager().find(CodeKind.class, id);
    }

    public static List<com.labsynch.labseer.domain.CodeKind> findCodeKindEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM CodeKind o", CodeKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.lsType = CodeType.findCodeType(this.getLsType().getId());
        this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            CodeKind attached = CodeKind.findCodeKind(this.id);
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
    public com.labsynch.labseer.domain.CodeKind merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
        CodeKind merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static CodeKind getOrCreate(CodeType codeType, String kindName) {
		
		CodeKind codeKind = null;
		List<CodeKind> codeKinds = CodeKind.findCodeKindsByKindNameEqualsAndLsType(kindName, codeType).getResultList();
		
		if (codeKinds.size() == 0){
			codeKind = new CodeKind();
			codeKind.setKindName(kindName);
			codeKind.setLsType(codeType);
			codeKind.persist();
		} else if (codeKinds.size() == 1){
			codeKind = codeKinds.get(0);
		} else if (codeKinds.size() > 1){
			logger.error("ERROR: multiple code kinds with the same name and type");
		}
		
		return codeKind;
	}

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "lsType", "kindName", "lsTypeAndKind", "id", "version", "entityManager");

	public static List<CodeKind> findAllCodeKinds(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM CodeKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, CodeKind.class).getResultList();
    }

	public static List<CodeKind> findCodeKindEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM CodeKind o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, CodeKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static CodeKind fromJsonToCodeKind(String json) {
        return new JSONDeserializer<CodeKind>()
        .use(null, CodeKind.class).deserialize(json);
    }

	public static String toJsonArray(Collection<CodeKind> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<CodeKind> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<CodeKind> fromJsonArrayToCodeKinds(String json) {
        return new JSONDeserializer<List<CodeKind>>()
        .use("values", CodeKind.class).deserialize(json);
    }

	public CodeType getLsType() {
        return this.lsType;
    }

	public void setLsType(CodeType lsType) {
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

	public static Long countFindCodeKindsByKindNameEqualsAndLsType(String kindName, CodeType lsType) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = CodeKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM CodeKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType", Long.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindCodeKindsByLsType(CodeType lsType) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = CodeKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM CodeKind AS o WHERE o.lsType = :lsType", Long.class);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindCodeKindsByLsTypeAndKindEquals(String lsTypeAndKind) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = CodeKind.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM CodeKind AS o WHERE o.lsTypeAndKind = :lsTypeAndKind", Long.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<CodeKind> findCodeKindsByKindNameEqualsAndLsType(String kindName, CodeType lsType) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = CodeKind.entityManager();
        TypedQuery<CodeKind> q = em.createQuery("SELECT o FROM CodeKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType", CodeKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }

	public static TypedQuery<CodeKind> findCodeKindsByKindNameEqualsAndLsType(String kindName, CodeType lsType, String sortFieldName, String sortOrder) {
        if (kindName == null || kindName.length() == 0) throw new IllegalArgumentException("The kindName argument is required");
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = CodeKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM CodeKind AS o WHERE o.kindName = :kindName  AND o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<CodeKind> q = em.createQuery(queryBuilder.toString(), CodeKind.class);
        q.setParameter("kindName", kindName);
        q.setParameter("lsType", lsType);
        return q;
    }

	public static TypedQuery<CodeKind> findCodeKindsByLsType(CodeType lsType) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = CodeKind.entityManager();
        TypedQuery<CodeKind> q = em.createQuery("SELECT o FROM CodeKind AS o WHERE o.lsType = :lsType", CodeKind.class);
        q.setParameter("lsType", lsType);
        return q;
    }

	public static TypedQuery<CodeKind> findCodeKindsByLsType(CodeType lsType, String sortFieldName, String sortOrder) {
        if (lsType == null) throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = CodeKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM CodeKind AS o WHERE o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<CodeKind> q = em.createQuery(queryBuilder.toString(), CodeKind.class);
        q.setParameter("lsType", lsType);
        return q;
    }

	public static TypedQuery<CodeKind> findCodeKindsByLsTypeAndKindEquals(String lsTypeAndKind) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = CodeKind.entityManager();
        TypedQuery<CodeKind> q = em.createQuery("SELECT o FROM CodeKind AS o WHERE o.lsTypeAndKind = :lsTypeAndKind", CodeKind.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return q;
    }

	public static TypedQuery<CodeKind> findCodeKindsByLsTypeAndKindEquals(String lsTypeAndKind, String sortFieldName, String sortOrder) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = CodeKind.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM CodeKind AS o WHERE o.lsTypeAndKind = :lsTypeAndKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<CodeKind> q = em.createQuery(queryBuilder.toString(), CodeKind.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return q;
    }
}
