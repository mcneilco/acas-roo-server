package com.labsynch.labseer.domain;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@Entity
@RooJson
@RooJavaBean
@RooToString(excludeFields = { "userEntry", "roleEntry", "version" })
@Table(name = "author_role", uniqueConstraints = { @javax.persistence.UniqueConstraint(columnNames = { "author_id", "lsrole_id" }) })
@RooJpaActiveRecord(sequenceName = "AUTH_ROLE_PKSEQ", finders = { "findAuthorRolesByUserEntry", "findAuthorRolesByRoleEntry", "findAuthorRolesByRoleEntryAndUserEntry" })
public class AuthorRole {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Author userEntry;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "lsrole_id", referencedColumnName = "id")
    private LsRole roleEntry;

    @Id
    @SequenceGenerator(name = "authorRoleGen", sequenceName = "AUTH_ROLE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "authorRoleGen")
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

    public String toJson() {
        return new JSONSerializer().exclude("*.class", "userEntry.password").serialize(this);
    }

    public static com.labsynch.labseer.domain.AuthorRole fromJsonToAuthorRole(String json) {
        return new JSONDeserializer<AuthorRole>().use(null, AuthorRole.class).deserialize(json);
    }

    public static String toJsonArray(Collection<com.labsynch.labseer.domain.AuthorRole> collection) {
        return new JSONSerializer().exclude("*.class", "userEntry.password").serialize(collection);
    }

    public static Collection<com.labsynch.labseer.domain.AuthorRole> fromJsonArrayToAuthorRoles(String json) {
        return new JSONDeserializer<List<AuthorRole>>().use(null, ArrayList.class).use("values", AuthorRole.class).deserialize(json);
    }

	public static AuthorRole getOrCreateAuthorRole(Author author, LsRole userRole) {
		List<AuthorRole> authorRoles = AuthorRole.findAuthorRolesByRoleEntryAndUserEntry(userRole, author).getResultList();
		if (authorRoles.size() == 0){
			AuthorRole newAuthorRole = new AuthorRole();
			newAuthorRole.setUserEntry(author);
			newAuthorRole.setRoleEntry(userRole);
			newAuthorRole.persist();
			return newAuthorRole;
		} else {
			return authorRoles.get(0);
		}
	}
}
