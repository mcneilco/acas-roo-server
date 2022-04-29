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
@RooJpaActiveRecord(sequenceName = "CODE_TYPE_PKSEQ", finders = { "findCodeTypesByTypeNameEquals" })
public class CodeType {

	private static final Logger logger = LoggerFactory.getLogger(CodeType.class);

	
	@NotNull
	@Column(unique = true)
	@Size(max = 128)
	private String typeName;

	public static CodeType getOrCreate(String name) {

		CodeType codeType = null;
		List<CodeType> codeTypes = CodeType.findCodeTypesByTypeNameEquals(name).getResultList();
		if (codeTypes.size() == 0){
			codeType = new CodeType();
			codeType.setTypeName(name);
			codeType.persist();
		} else if (codeTypes.size() == 1){
			codeType = codeTypes.get(0);
		} else if (codeTypes.size() > 1){
			logger.error("ERROR: multiple code types with the same name");
		}
		

		return codeType;
	}

	@Id
    @SequenceGenerator(name = "codeTypeGen", sequenceName = "CODE_TYPE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "codeTypeGen")
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

	public static CodeType fromJsonToCodeType(String json) {
        return new JSONDeserializer<CodeType>()
        .use(null, CodeType.class).deserialize(json);
    }

	public static String toJsonArray(Collection<CodeType> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<CodeType> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<CodeType> fromJsonArrayToCodeTypes(String json) {
        return new JSONDeserializer<List<CodeType>>()
        .use("values", CodeType.class).deserialize(json);
    }

	public static Long countFindCodeTypesByTypeNameEquals(String typeName) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = CodeType.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM CodeType AS o WHERE o.typeName = :typeName", Long.class);
        q.setParameter("typeName", typeName);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<CodeType> findCodeTypesByTypeNameEquals(String typeName) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = CodeType.entityManager();
        TypedQuery<CodeType> q = em.createQuery("SELECT o FROM CodeType AS o WHERE o.typeName = :typeName", CodeType.class);
        q.setParameter("typeName", typeName);
        return q;
    }

	public static TypedQuery<CodeType> findCodeTypesByTypeNameEquals(String typeName, String sortFieldName, String sortOrder) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = CodeType.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM CodeType AS o WHERE o.typeName = :typeName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<CodeType> q = em.createQuery(queryBuilder.toString(), CodeType.class);
        q.setParameter("typeName", typeName);
        return q;
    }

	public String getTypeName() {
        return this.typeName;
    }

	public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "typeName", "id", "version");

	public static final EntityManager entityManager() {
        EntityManager em = new CodeType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countCodeTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM CodeType o", Long.class).getSingleResult();
    }

	public static List<CodeType> findAllCodeTypes() {
        return entityManager().createQuery("SELECT o FROM CodeType o", CodeType.class).getResultList();
    }

	public static List<CodeType> findAllCodeTypes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM CodeType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, CodeType.class).getResultList();
    }

	public static CodeType findCodeType(Long id) {
        if (id == null) return null;
        return entityManager().find(CodeType.class, id);
    }

	public static List<CodeType> findCodeTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM CodeType o", CodeType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<CodeType> findCodeTypeEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM CodeType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, CodeType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            CodeType attached = CodeType.findCodeType(this.id);
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
    public CodeType merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        CodeType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
