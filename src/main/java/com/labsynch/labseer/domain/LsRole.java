package com.labsynch.labseer.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.dto.CodeTableDTO;

@RooJson
@RooJavaBean
@RooJpaActiveRecord(sequenceName = "LSROLE_PKSEQ", finders = { 
		"findLsRolesByRoleNameEquals", 
		"findLsRolesByLsTypeEqualsAndLsKindEqualsAndRoleNameEquals", 
		"findLsRolesByLsTypeEqualsAndRoleNameEquals", 
		"findLsRolesByLsTypeEqualsAndLsKindEquals",
		"findLsRolesByLsTypeEquals",
		"findLsRolesByLsKindEquals"})

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
    
    // @Column(unique = true)
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
        return new StringBuilder().append(this.id).append(' ').append(this.lsType).append(' ').append(this.lsKind).append(' ').append(this.roleName).toString();
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
    public static Collection<LsRole> getOrCreateRoles(Collection<LsRole> queryRoles){
    	Collection<LsRole> resultRoles = new ArrayList<LsRole>();
    	for (LsRole queryRole : queryRoles){
    		LsRole resultRole = LsRole.getOrCreateRole(queryRole);
    		resultRoles.add(resultRole);
    	}
    	return resultRoles;
    }
    public static LsRole getOrCreateRole(LsRole queryRole) {
    	String lsType = queryRole.getLsType();
    	String lsKind = queryRole.getLsKind();
    	String roleName = queryRole.getRoleName();
        List<LsRole> lsRoles = LsRole.findLsRolesByLsTypeEqualsAndLsKindEqualsAndRoleNameEquals(lsType, lsKind, roleName).getResultList();
        if (lsRoles.size() == 0) {
            LsRole newRole = new LsRole();
            newRole.setLsType(lsType);
            newRole.setLsKind(lsKind);
            newRole.setRoleName(roleName);
            newRole.setRoleDescription(roleName+" autocreated by ACAS");
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
        if (roleName == null || roleName.length() == 0) throw new IllegalArgumentException("The roleName argument is required");
        EntityManager em = LsRole.entityManager();
        TypedQuery<LsRole> q = em.createQuery("SELECT o FROM LsRole AS o WHERE o.roleName = :roleName", LsRole.class);
        q.setParameter("roleName", roleName);
        return q;
    }
    
    @Transactional
	public void persist() {
		if (this.entityManager == null) this.entityManager = entityManager();
		this.setLsTypeAndKind(new StringBuilder().append(this.lsType).append("_").append(this.lsKind).toString());
		this.entityManager.persist(this);
	}

	public static Collection<CodeTableDTO> toCodeTables(List<LsRole> lsRoles) {
		Collection<CodeTableDTO> codeTables = new ArrayList<CodeTableDTO>();
		for (LsRole lsRole: lsRoles) {
			CodeTableDTO codeTable = new CodeTableDTO();
			codeTable.setId(lsRole.getId());
			String code = lsRole.getLsType()+"_"+lsRole.getLsKind()+"_"+lsRole.getRoleName();
			codeTable.setCode(code);
			String name = lsRole.getLsType()+" : "+lsRole.getLsKind()+" : "+lsRole.getRoleName();
			codeTable.setName(name);
			codeTables.add(codeTable);
		}
		return codeTables;
	}
}
