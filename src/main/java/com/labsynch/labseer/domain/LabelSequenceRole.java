package com.labsynch.labseer.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;


@RooJson
@RooJavaBean
@RooToString(excludeFields = { "labelSequenceEntry", "roleEntry", "version" })
@Table(name = "label_sequence_ls_role", uniqueConstraints = { @javax.persistence.UniqueConstraint(columnNames = { "label_sequence_id", "ls_role_id" }) })
@RooJpaActiveRecord(sequenceName = "LABELSEQ_ROLE_PKSEQ", finders = { "findLabelSequenceRolesByRoleEntryAndLabelSequenceEntry" })
public class LabelSequenceRole {
	
    @NotNull
    @ManyToOne
    @JoinColumn(name = "label_sequence_id", referencedColumnName = "id")
    private LabelSequence labelSequenceEntry;

    @NotNull
    @ManyToOne
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
}
