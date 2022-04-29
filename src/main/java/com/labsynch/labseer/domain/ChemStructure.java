package com.labsynch.labseer.domain;

import java.util.Collection;
import java.util.Date;
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
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable

public class ChemStructure {

    private static final Logger logger = LoggerFactory.getLogger(ChemStructure.class);

    @Id
    @SequenceGenerator(name = "structureGen", sequenceName = "CHEM_STRUCTURE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "structureGen")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(unique = true)
    private String codeName;

    @Version
    @Column(name = "version")
    private Integer version;

    @NotNull
    @Column(columnDefinition = "text")
    private String molStructure;

    @Size(max = 1000)
    private String smiles;

    @Size(max = 255)
    private String lsType;

    @Size(max = 255)
    private String lsKind;

    @Size(max = 255)
    private String lsTypeAndKind;

    @NotNull
    private boolean ignored;

    @NotNull
    private boolean deleted;

    @NotNull
    private String recordedBy;

    @NotNull
    private Date recordedDate;

    private String modifiedBy;

    private Date modifiedDate;

    private Long lsTransaction;

    public ChemStructure(com.labsynch.labseer.domain.ChemStructure structure) {
        this.setCodeName(structure.getCodeName());
        this.setLsType(structure.getLsType());
        this.setLsKind(structure.getLsKind());
        this.setMolStructure(structure.getMolStructure());
        this.setSmiles(structure.getSmiles());
        this.setIgnored(structure.isIgnored());
        this.setDeleted(structure.isDeleted());
        this.setCodeName(structure.getCodeName());
        this.setRecordedBy(structure.getRecordedBy());
        this.setRecordedDate(structure.getRecordedDate());
        this.setModifiedBy(structure.getModifiedBy());
        this.setModifiedDate(structure.getModifiedDate());
        this.setLsTransaction(structure.getLsTransaction());
    }

    public static com.labsynch.labseer.domain.ChemStructure update(com.labsynch.labseer.domain.ChemStructure structure) {
        ChemStructure updatedStructure = ChemStructure.findChemStructure(structure.getId());
        updatedStructure.setMolStructure(structure.getMolStructure());
        updatedStructure.setSmiles(structure.getSmiles());
        updatedStructure.setIgnored(structure.isIgnored());
        updatedStructure.setDeleted(structure.isDeleted());
        updatedStructure.setCodeName(structure.getCodeName());
        updatedStructure.setLsType(structure.getLsType());
        updatedStructure.setLsKind(structure.getLsKind());
        updatedStructure.setModifiedBy(structure.getModifiedBy());
        updatedStructure.setModifiedDate(structure.getModifiedDate());
        updatedStructure.setLsTransaction(structure.getLsTransaction());
        updatedStructure.merge();
        return structure;
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

    public static com.labsynch.labseer.domain.ChemStructure findStructureByCodeName(String codeName) {
    	ChemStructure result = ChemStructure.findChemStructuresByCodeNameEquals(codeName).getSingleResult();
    	return result;
    }

	public static Long countFindChemStructuresByCodeNameEquals(String codeName) {
        if (codeName == null || codeName.length() == 0) throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = ChemStructure.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ChemStructure AS o WHERE o.codeName = :codeName", Long.class);
        q.setParameter("codeName", codeName);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<ChemStructure> findChemStructuresByCodeNameEquals(String codeName) {
        if (codeName == null || codeName.length() == 0) throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = ChemStructure.entityManager();
        TypedQuery<ChemStructure> q = em.createQuery("SELECT o FROM ChemStructure AS o WHERE o.codeName = :codeName", ChemStructure.class);
        q.setParameter("codeName", codeName);
        return q;
    }

	public static TypedQuery<ChemStructure> findChemStructuresByCodeNameEquals(String codeName, String sortFieldName, String sortOrder) {
        if (codeName == null || codeName.length() == 0) throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = ChemStructure.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ChemStructure AS o WHERE o.codeName = :codeName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ChemStructure> q = em.createQuery(queryBuilder.toString(), ChemStructure.class);
        q.setParameter("codeName", codeName);
        return q;
    }

	public String getCodeName() {
        return this.codeName;
    }

	public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

	public String getMolStructure() {
        return this.molStructure;
    }

	public void setMolStructure(String molStructure) {
        this.molStructure = molStructure;
    }

	public String getSmiles() {
        return this.smiles;
    }

	public void setSmiles(String smiles) {
        this.smiles = smiles;
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

	public String getLsTypeAndKind() {
        return this.lsTypeAndKind;
    }

	public void setLsTypeAndKind(String lsTypeAndKind) {
        this.lsTypeAndKind = lsTypeAndKind;
    }

	public boolean isIgnored() {
        return this.ignored;
    }

	public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

	public boolean isDeleted() {
        return this.deleted;
    }

	public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

	public String getRecordedBy() {
        return this.recordedBy;
    }

	public void setRecordedBy(String recordedBy) {
        this.recordedBy = recordedBy;
    }

	public Date getRecordedDate() {
        return this.recordedDate;
    }

	public void setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }

	public String getModifiedBy() {
        return this.modifiedBy;
    }

	public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

	public Date getModifiedDate() {
        return this.modifiedDate;
    }

	public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

	public Long getLsTransaction() {
        return this.lsTransaction;
    }

	public void setLsTransaction(Long lsTransaction) {
        this.lsTransaction = lsTransaction;
    }

	public ChemStructure() {
        super();
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "id", "codeName", "version", "molStructure", "smiles", "lsType", "lsKind", "lsTypeAndKind", "ignored", "deleted", "recordedBy", "recordedDate", "modifiedBy", "modifiedDate", "lsTransaction");

	public static final EntityManager entityManager() {
        EntityManager em = new ChemStructure().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countChemStructures() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ChemStructure o", Long.class).getSingleResult();
    }

	public static List<ChemStructure> findAllChemStructures() {
        return entityManager().createQuery("SELECT o FROM ChemStructure o", ChemStructure.class).getResultList();
    }

	public static List<ChemStructure> findAllChemStructures(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ChemStructure o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ChemStructure.class).getResultList();
    }

	public static ChemStructure findChemStructure(Long id) {
        if (id == null) return null;
        return entityManager().find(ChemStructure.class, id);
    }

	public static List<ChemStructure> findChemStructureEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ChemStructure o", ChemStructure.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<ChemStructure> findChemStructureEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ChemStructure o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ChemStructure.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            ChemStructure attached = ChemStructure.findChemStructure(this.id);
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
    public ChemStructure merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ChemStructure merged = this.entityManager.merge(this);
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

	public static ChemStructure fromJsonToChemStructure(String json) {
        return new JSONDeserializer<ChemStructure>()
        .use(null, ChemStructure.class).deserialize(json);
    }

	public static String toJsonArray(Collection<ChemStructure> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<ChemStructure> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<ChemStructure> fromJsonArrayToChemStructures(String json) {
        return new JSONDeserializer<List<ChemStructure>>()
        .use("values", ChemStructure.class).deserialize(json);
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
