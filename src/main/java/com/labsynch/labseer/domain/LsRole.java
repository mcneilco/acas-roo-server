package com.labsynch.labseer.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;

@Entity
@RooJson
@RooJavaBean
@RooJpaActiveRecord(sequenceName = "LSROLE_PKSEQ", finders = { "findLsRolesByRoleNameEquals" })
public class LsRole {

    @NotNull
    @Column(unique = true)
    @Size(min = 1)
    private String roleName;

    @NotNull
    @Size(max = 200)
    private String roleDescription;

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
        return new StringBuilder().append(this.id).append(' ').append(this.roleName).toString();
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

	public static LsRole getOrCreateRole(String roleName) {
		List<LsRole> lsRoles = LsRole.findLsRolesByRoleNameEquals(roleName).getResultList();
		if (lsRoles.size() == 0){
			LsRole newRole = new LsRole();
			newRole.setRoleName(roleName);
			newRole.persist();
			return newRole;
		} else {

			return lsRoles.get(0);
		}
		
	}
}
