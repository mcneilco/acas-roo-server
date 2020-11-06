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
@RooJpaActiveRecord(sequenceName = "STATE_TYPE_PKSEQ", finders = { "findStateTypesByTypeNameEquals" })
@RooJson
public class StateType {

    private static final Logger logger = LoggerFactory.getLogger(StateType.class);

    @NotNull
    @Column(unique = true)
    @Size(max = 64)
    private String typeName;

    public static com.labsynch.labseer.domain.StateType getOrCreate(String name) {
        StateType lsType = null;
        List<StateType> lsTypes = StateType.findStateTypesByTypeNameEquals(name).getResultList();
        if (lsTypes.size() == 0) {
            lsType = new StateType();
            lsType.setTypeName(name);
            lsType.persist();
        } else if (lsTypes.size() == 1) {
            lsType = lsTypes.get(0);
        } else if (lsTypes.size() > 1) {
            logger.error("ERROR: multiple Label types with the same name");
        }
        return lsType;
    }

	@Id
    @SequenceGenerator(name = "stateTypeGen", sequenceName = "STATE_TYPE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "stateTypeGen")
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

	public String getTypeName() {
        return this.typeName;
    }

	public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "typeName", "id", "version");

	public static final EntityManager entityManager() {
        EntityManager em = new StateType().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countStateTypes() {
        return entityManager().createQuery("SELECT COUNT(o) FROM StateType o", Long.class).getSingleResult();
    }

	public static List<StateType> findAllStateTypes() {
        return entityManager().createQuery("SELECT o FROM StateType o", StateType.class).getResultList();
    }

	public static List<StateType> findAllStateTypes(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM StateType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, StateType.class).getResultList();
    }

	public static StateType findStateType(Long id) {
        if (id == null) return null;
        return entityManager().find(StateType.class, id);
    }

	public static List<StateType> findStateTypeEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM StateType o", StateType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<StateType> findStateTypeEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM StateType o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, StateType.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            StateType attached = StateType.findStateType(this.id);
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
    public StateType merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        StateType merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static Long countFindStateTypesByTypeNameEquals(String typeName) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = StateType.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM StateType AS o WHERE o.typeName = :typeName", Long.class);
        q.setParameter("typeName", typeName);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<StateType> findStateTypesByTypeNameEquals(String typeName) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = StateType.entityManager();
        TypedQuery<StateType> q = em.createQuery("SELECT o FROM StateType AS o WHERE o.typeName = :typeName", StateType.class);
        q.setParameter("typeName", typeName);
        return q;
    }

	public static TypedQuery<StateType> findStateTypesByTypeNameEquals(String typeName, String sortFieldName, String sortOrder) {
        if (typeName == null || typeName.length() == 0) throw new IllegalArgumentException("The typeName argument is required");
        EntityManager em = StateType.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM StateType AS o WHERE o.typeName = :typeName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<StateType> q = em.createQuery(queryBuilder.toString(), StateType.class);
        q.setParameter("typeName", typeName);
        return q;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static StateType fromJsonToStateType(String json) {
        return new JSONDeserializer<StateType>()
        .use(null, StateType.class).deserialize(json);
    }

	public static String toJsonArray(Collection<StateType> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<StateType> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<StateType> fromJsonArrayToStateTypes(String json) {
        return new JSONDeserializer<List<StateType>>()
        .use("values", StateType.class).deserialize(json);
    }
}
