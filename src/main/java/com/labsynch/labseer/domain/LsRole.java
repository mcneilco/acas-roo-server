package com.labsynch.labseer.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.labsynch.labseer.dto.CodeTableDTO;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity
public class LsRole {

    @Size(max = 255)
    private String lsType;

    @Size(max = 255)
    private String lsKind;

    @NotNull
    @Size(min = 1)
    private String roleName;

    @Size(max = 200)
    private String roleDescription;

    @Size(max = 255)
    private String lsTypeAndKind;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleEntry", fetch = FetchType.LAZY)
    private Set<AuthorRole> authorRoles = new HashSet<AuthorRole>();

    @Id
    @SequenceGenerator(name = "lsRoleGen", sequenceName = "LSROLE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "lsRoleGen")
    @Column(name = "id")
    private Long id;

    @Version
    @Column(name = "version")
    private Integer version;

    public String toString() {
        return new StringBuilder().append(this.id).append(' ').append(this.lsType).append(' ').append(this.lsKind)
                .append(' ').append(this.roleName).toString();
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

    public static Collection<LsRole> getOrCreateRoles(Collection<LsRole> queryRoles) {
        Collection<LsRole> resultRoles = new ArrayList<LsRole>();
        for (LsRole queryRole : queryRoles) {
            LsRole resultRole = LsRole.getOrCreateRole(queryRole);
            resultRoles.add(resultRole);
        }
        return resultRoles;
    }

    public static LsRole getOrCreateRole(LsRole queryRole) {
        String lsType = queryRole.getLsType();
        String lsKind = queryRole.getLsKind();
        String roleName = queryRole.getRoleName();
        List<LsRole> lsRoles = LsRole
                .findLsRolesByLsTypeEqualsAndLsKindEqualsAndRoleNameEquals(lsType, lsKind, roleName).getResultList();
        if (lsRoles.size() == 0) {
            LsRole newRole = new LsRole();
            newRole.setLsType(lsType);
            newRole.setLsKind(lsKind);
            newRole.setRoleName(roleName);
            newRole.setRoleDescription(roleName + " autocreated by ACAS");
            newRole.persist();
            return newRole;
        } else {
            return lsRoles.get(0);
        }
    }

    public static com.labsynch.labseer.domain.LsRole getOrCreateRole(String roleName) {
        List<LsRole> lsRoles = LsRole.findLsRolesByRoleNameEquals(roleName).getResultList();
        if (lsRoles.size() == 0) {
            LsRole newRole = new LsRole();
            newRole.setRoleName(roleName);
            newRole.setRoleDescription(roleName);
            newRole.persist();
            return newRole;
        } else {
            return lsRoles.get(0);
        }
    }

    public static TypedQuery<com.labsynch.labseer.domain.LsRole> findLsRolesByRoleNameEquals(String roleName) {
        if (roleName == null || roleName.length() == 0)
            throw new IllegalArgumentException("The roleName argument is required");
        EntityManager em = LsRole.entityManager();
        TypedQuery<LsRole> q = em.createQuery("SELECT o FROM LsRole AS o WHERE o.roleName = :roleName", LsRole.class);
        q.setParameter("roleName", roleName);
        return q;
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.setLsTypeAndKind(new StringBuilder().append(this.lsType).append("_").append(this.lsKind).toString());
        this.entityManager.persist(this);
    }

    public static Collection<CodeTableDTO> toCodeTables(List<LsRole> lsRoles) {
        Collection<CodeTableDTO> codeTables = new ArrayList<CodeTableDTO>();
        for (LsRole lsRole : lsRoles) {
            CodeTableDTO codeTable = new CodeTableDTO();
            codeTable.setId(lsRole.getId());
            String code = lsRole.getLsType() + "_" + lsRole.getLsKind() + "_" + lsRole.getRoleName();
            codeTable.setCode(code);
            String name = lsRole.getLsType() + " : " + lsRole.getLsKind() + " : " + lsRole.getRoleName();
            codeTable.setName(name);
            codeTables.add(codeTable);
        }
        return codeTables;
    }

    @PersistenceContext
    transient EntityManager entityManager;

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("lsType", "lsKind",
            "roleName", "roleDescription", "lsTypeAndKind", "authorRoles", "id", "version");

    public static final EntityManager entityManager() {
        EntityManager em = new LsRole().entityManager;
        if (em == null)
            throw new IllegalStateException(
                    "Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countLsRoles() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LsRole o", Long.class).getSingleResult();
    }

    public static List<LsRole> findAllLsRoles() {
        return entityManager().createQuery("SELECT o FROM LsRole o", LsRole.class).getResultList();
    }

    public static List<LsRole> findAllLsRoles(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LsRole o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsRole.class).getResultList();
    }

    public static LsRole findLsRole(Long id) {
        if (id == null)
            return null;
        return entityManager().find(LsRole.class, id);
    }

    public static List<LsRole> findLsRoleEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LsRole o", LsRole.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public static List<LsRole> findLsRoleEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM LsRole o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsRole.class).setFirstResult(firstResult).setMaxResults(maxResults)
                .getResultList();
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            LsRole attached = LsRole.findLsRole(this.id);
            this.entityManager.remove(attached);
        }
    }

    @Transactional
    public void flush() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.entityManager.flush();
    }

    @Transactional
    public void clear() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        this.entityManager.clear();
    }

    @Transactional
    public LsRole merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        LsRole merged = this.entityManager.merge(this);
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

    public static LsRole fromJsonToLsRole(String json) {
        return new JSONDeserializer<LsRole>()
                .use(null, LsRole.class).deserialize(json);
    }

    public static String toJsonArray(Collection<LsRole> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<LsRole> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<LsRole> fromJsonArrayToLsRoles(String json) {
        return new JSONDeserializer<List<LsRole>>()
                .use("values", LsRole.class).deserialize(json);
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

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDescription() {
        return this.roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public String getLsTypeAndKind() {
        return this.lsTypeAndKind;
    }

    public void setLsTypeAndKind(String lsTypeAndKind) {
        this.lsTypeAndKind = lsTypeAndKind;
    }

    public Set<AuthorRole> getAuthorRoles() {
        return this.authorRoles;
    }

    public void setAuthorRoles(Set<AuthorRole> authorRoles) {
        this.authorRoles = authorRoles;
    }

    public static Long countFindLsRolesByLsKindEquals(String lsKind) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = LsRole.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LsRole AS o WHERE o.lsKind = :lsKind", Long.class);
        q.setParameter("lsKind", lsKind);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLsRolesByLsTypeEquals(String lsType) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = LsRole.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LsRole AS o WHERE o.lsType = :lsType", Long.class);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLsRolesByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = LsRole.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM LsRole AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind", Long.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLsRolesByLsTypeEqualsAndLsKindEqualsAndRoleNameEquals(String lsType, String lsKind,
            String roleName) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        if (roleName == null || roleName.length() == 0)
            throw new IllegalArgumentException("The roleName argument is required");
        EntityManager em = LsRole.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM LsRole AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind  AND o.roleName = :roleName",
                Long.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("roleName", roleName);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLsRolesByLsTypeEqualsAndRoleNameEquals(String lsType, String roleName) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (roleName == null || roleName.length() == 0)
            throw new IllegalArgumentException("The roleName argument is required");
        EntityManager em = LsRole.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM LsRole AS o WHERE o.lsType = :lsType  AND o.roleName = :roleName", Long.class);
        q.setParameter("lsType", lsType);
        q.setParameter("roleName", roleName);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLsRolesByRoleNameEquals(String roleName) {
        if (roleName == null || roleName.length() == 0)
            throw new IllegalArgumentException("The roleName argument is required");
        EntityManager em = LsRole.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LsRole AS o WHERE o.roleName = :roleName", Long.class);
        q.setParameter("roleName", roleName);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<LsRole> findLsRolesByLsKindEquals(String lsKind) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = LsRole.entityManager();
        TypedQuery<LsRole> q = em.createQuery("SELECT o FROM LsRole AS o WHERE o.lsKind = :lsKind", LsRole.class);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    public static TypedQuery<LsRole> findLsRolesByLsKindEquals(String lsKind, String sortFieldName, String sortOrder) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = LsRole.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LsRole AS o WHERE o.lsKind = :lsKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsRole> q = em.createQuery(queryBuilder.toString(), LsRole.class);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    public static TypedQuery<LsRole> findLsRolesByLsTypeEquals(String lsType) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = LsRole.entityManager();
        TypedQuery<LsRole> q = em.createQuery("SELECT o FROM LsRole AS o WHERE o.lsType = :lsType", LsRole.class);
        q.setParameter("lsType", lsType);
        return q;
    }

    public static TypedQuery<LsRole> findLsRolesByLsTypeEquals(String lsType, String sortFieldName, String sortOrder) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = LsRole.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LsRole AS o WHERE o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsRole> q = em.createQuery(queryBuilder.toString(), LsRole.class);
        q.setParameter("lsType", lsType);
        return q;
    }

    public static TypedQuery<LsRole> findLsRolesByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = LsRole.entityManager();
        TypedQuery<LsRole> q = em.createQuery(
                "SELECT o FROM LsRole AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind", LsRole.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    public static TypedQuery<LsRole> findLsRolesByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind,
            String sortFieldName, String sortOrder) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = LsRole.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM LsRole AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsRole> q = em.createQuery(queryBuilder.toString(), LsRole.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    public static TypedQuery<LsRole> findLsRolesByLsTypeEqualsAndLsKindEqualsAndRoleNameEquals(String lsType,
            String lsKind, String roleName) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        if (roleName == null || roleName.length() == 0)
            throw new IllegalArgumentException("The roleName argument is required");
        EntityManager em = LsRole.entityManager();
        TypedQuery<LsRole> q = em.createQuery(
                "SELECT o FROM LsRole AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind  AND o.roleName = :roleName",
                LsRole.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("roleName", roleName);
        return q;
    }

    public static TypedQuery<LsRole> findLsRolesByLsTypeEqualsAndLsKindEqualsAndRoleNameEquals(String lsType,
            String lsKind, String roleName, String sortFieldName, String sortOrder) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        if (roleName == null || roleName.length() == 0)
            throw new IllegalArgumentException("The roleName argument is required");
        EntityManager em = LsRole.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM LsRole AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind  AND o.roleName = :roleName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsRole> q = em.createQuery(queryBuilder.toString(), LsRole.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("roleName", roleName);
        return q;
    }

    public static TypedQuery<LsRole> findLsRolesByLsTypeEqualsAndRoleNameEquals(String lsType, String roleName) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (roleName == null || roleName.length() == 0)
            throw new IllegalArgumentException("The roleName argument is required");
        EntityManager em = LsRole.entityManager();
        TypedQuery<LsRole> q = em.createQuery(
                "SELECT o FROM LsRole AS o WHERE o.lsType = :lsType  AND o.roleName = :roleName", LsRole.class);
        q.setParameter("lsType", lsType);
        q.setParameter("roleName", roleName);
        return q;
    }

    public static TypedQuery<LsRole> findLsRolesByLsTypeEqualsAndRoleNameEquals(String lsType, String roleName,
            String sortFieldName, String sortOrder) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (roleName == null || roleName.length() == 0)
            throw new IllegalArgumentException("The roleName argument is required");
        EntityManager em = LsRole.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM LsRole AS o WHERE o.lsType = :lsType  AND o.roleName = :roleName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsRole> q = em.createQuery(queryBuilder.toString(), LsRole.class);
        q.setParameter("lsType", lsType);
        q.setParameter("roleName", roleName);
        return q;
    }

    public static TypedQuery<LsRole> findLsRolesByRoleNameEquals(String roleName, String sortFieldName,
            String sortOrder) {
        if (roleName == null || roleName.length() == 0)
            throw new IllegalArgumentException("The roleName argument is required");
        EntityManager em = LsRole.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LsRole AS o WHERE o.roleName = :roleName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsRole> q = em.createQuery(queryBuilder.toString(), LsRole.class);
        q.setParameter("roleName", roleName);
        return q;
    }
}
