package com.labsynch.labseer.domain;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
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


@Entity
@Configurable
@Table(name = "label_sequence_ls_role", uniqueConstraints = { @javax.persistence.UniqueConstraint(columnNames = { "label_sequence_id", "ls_role_id" }) })
public class LabelSequenceRole {
	
    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "label_sequence_id", referencedColumnName = "id")
    private LabelSequence labelSequenceEntry;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ls_role_id", referencedColumnName = "id")
    private LsRole roleEntry;

    @Id
    @SequenceGenerator(name = "labelSequenceRoleGen", sequenceName = "LABELSEQ_ROLE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "labelSequenceRoleGen")
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

	public static LabelSequenceRole getOrCreateLabelSequenceRole(LabelSequence labelSequence, LsRole userRole) {
		List<LabelSequenceRole> labelSequenceRoles = LabelSequenceRole.findLabelSequenceRolesByRoleEntryAndLabelSequenceEntry(userRole, labelSequence).getResultList();
		if (labelSequenceRoles.size() == 0){
			LabelSequenceRole newLabelSequenceRole = new LabelSequenceRole();
			newLabelSequenceRole.setLabelSequenceEntry(labelSequence);
			newLabelSequenceRole.setRoleEntry(userRole);
			newLabelSequenceRole.persist();
			return newLabelSequenceRole;
		} else {
			return labelSequenceRoles.get(0);
		}
	}

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("labelSequenceEntry", "roleEntry", "id", "version");

	public static final EntityManager entityManager() {
        EntityManager em = new LabelSequenceRole().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countLabelSequenceRoles() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LabelSequenceRole o", Long.class).getSingleResult();
    }

	public static List<LabelSequenceRole> findAllLabelSequenceRoles() {
        return entityManager().createQuery("SELECT o FROM LabelSequenceRole o", LabelSequenceRole.class).getResultList();
    }

	public static List<LabelSequenceRole> findAllLabelSequenceRoles(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LabelSequenceRole o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LabelSequenceRole.class).getResultList();
    }

	public static LabelSequenceRole findLabelSequenceRole(Long id) {
        if (id == null) return null;
        return entityManager().find(LabelSequenceRole.class, id);
    }

	public static List<LabelSequenceRole> findLabelSequenceRoleEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LabelSequenceRole o", LabelSequenceRole.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<LabelSequenceRole> findLabelSequenceRoleEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LabelSequenceRole o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LabelSequenceRole.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            LabelSequenceRole attached = LabelSequenceRole.findLabelSequenceRole(this.id);
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
    public LabelSequenceRole merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        LabelSequenceRole merged = this.entityManager.merge(this);
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

	public static LabelSequenceRole fromJsonToLabelSequenceRole(String json) {
        return new JSONDeserializer<LabelSequenceRole>()
        .use(null, LabelSequenceRole.class).deserialize(json);
    }

	public static String toJsonArray(Collection<LabelSequenceRole> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<LabelSequenceRole> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<LabelSequenceRole> fromJsonArrayToLabelSequenceRoles(String json) {
        return new JSONDeserializer<List<LabelSequenceRole>>()
        .use("values", LabelSequenceRole.class).deserialize(json);
    }

	public static Long countFindLabelSequenceRolesByRoleEntryAndLabelSequenceEntry(LsRole roleEntry, LabelSequence labelSequenceEntry) {
        if (roleEntry == null) throw new IllegalArgumentException("The roleEntry argument is required");
        if (labelSequenceEntry == null) throw new IllegalArgumentException("The labelSequenceEntry argument is required");
        EntityManager em = LabelSequenceRole.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LabelSequenceRole AS o WHERE o.roleEntry = :roleEntry AND o.labelSequenceEntry = :labelSequenceEntry", Long.class);
        q.setParameter("roleEntry", roleEntry);
        q.setParameter("labelSequenceEntry", labelSequenceEntry);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<LabelSequenceRole> findLabelSequenceRolesByRoleEntryAndLabelSequenceEntry(LsRole roleEntry, LabelSequence labelSequenceEntry) {
        if (roleEntry == null) throw new IllegalArgumentException("The roleEntry argument is required");
        if (labelSequenceEntry == null) throw new IllegalArgumentException("The labelSequenceEntry argument is required");
        EntityManager em = LabelSequenceRole.entityManager();
        TypedQuery<LabelSequenceRole> q = em.createQuery("SELECT o FROM LabelSequenceRole AS o WHERE o.roleEntry = :roleEntry AND o.labelSequenceEntry = :labelSequenceEntry", LabelSequenceRole.class);
        q.setParameter("roleEntry", roleEntry);
        q.setParameter("labelSequenceEntry", labelSequenceEntry);
        return q;
    }

	public static TypedQuery<LabelSequenceRole> findLabelSequenceRolesByRoleEntryAndLabelSequenceEntry(LsRole roleEntry, LabelSequence labelSequenceEntry, String sortFieldName, String sortOrder) {
        if (roleEntry == null) throw new IllegalArgumentException("The roleEntry argument is required");
        if (labelSequenceEntry == null) throw new IllegalArgumentException("The labelSequenceEntry argument is required");
        EntityManager em = LabelSequenceRole.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LabelSequenceRole AS o WHERE o.roleEntry = :roleEntry AND o.labelSequenceEntry = :labelSequenceEntry");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LabelSequenceRole> q = em.createQuery(queryBuilder.toString(), LabelSequenceRole.class);
        q.setParameter("roleEntry", roleEntry);
        q.setParameter("labelSequenceEntry", labelSequenceEntry);
        return q;
    }

	public LabelSequence getLabelSequenceEntry() {
        return this.labelSequenceEntry;
    }

	public void setLabelSequenceEntry(LabelSequence labelSequenceEntry) {
        this.labelSequenceEntry = labelSequenceEntry;
    }

	public LsRole getRoleEntry() {
        return this.roleEntry;
    }

	public void setRoleEntry(LsRole roleEntry) {
        this.roleEntry = roleEntry;
    }
}
