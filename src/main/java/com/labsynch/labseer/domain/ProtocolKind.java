package com.labsynch.labseer.domain;

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
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@Entity
@RooJavaBean
@RooToString
@RooJpaActiveRecord(sequenceName = "PROTOCOL_KIND_PKSEQ", finders={"findProtocolKindsByLsTypeEqualsAndKindNameEquals"})
@RooJson
public class ProtocolKind {

    @Id
    @SequenceGenerator(name = "protocolKindGen", sequenceName = "PROTOCOL_KIND_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "protocolKindGen")
    @Column(name = "id")
    private Long id;

    @Version
    @Column(name = "version")
    private Integer version;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "ls_type")
    private ProtocolType lsType;

    @NotNull
    @Size(max = 255)
    private String kindName;

    @Size(max = 255)
    @Column(unique = true)
    private String lsTypeAndKind;

    @PersistenceContext
    transient EntityManager entityManager;

    public static final EntityManager entityManager() {
        EntityManager em = new ProtocolKind().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

    public static long countProtocolKinds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ProtocolKind o", Long.class).getSingleResult();
    }

    public static List<com.labsynch.labseer.domain.ProtocolKind> findAllProtocolKinds() {
        return entityManager().createQuery("SELECT o FROM ProtocolKind o", ProtocolKind.class).getResultList();
    }

    public static com.labsynch.labseer.domain.ProtocolKind findProtocolKind(Long id) {
        if (id == null) return null;
        return entityManager().find(ProtocolKind.class, id);
    }

    public static List<com.labsynch.labseer.domain.ProtocolKind> findProtocolKindEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ProtocolKind o", ProtocolKind.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.lsType = ProtocolType.findProtocolType(this.getLsType().getId());
        this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
        this.entityManager.persist(this);
    }

    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            ProtocolKind attached = ProtocolKind.findProtocolKind(this.id);
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
    public com.labsynch.labseer.domain.ProtocolKind merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.lsTypeAndKind = new StringBuilder().append(this.getLsType().getTypeName()).append('_').append(this.getKindName()).toString();
        ProtocolKind merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
