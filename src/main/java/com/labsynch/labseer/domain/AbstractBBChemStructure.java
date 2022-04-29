package com.labsynch.labseer.domain;

import java.util.BitSet;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PersistenceContext;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule.RegistrationStatus;
import com.labsynch.labseer.chemclasses.CmpdRegMolecule.StandardizationStatus;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

public abstract class AbstractBBChemStructure {

    @Id
    @SequenceGenerator(name = "bbChemStructureGen", sequenceName = "BBCHEM_STRUCTURE_PKSEQ")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "bbChemStructureGen")
    private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    @Column(columnDefinition = "CHAR(40)")
    private String preReg;

    @Column(columnDefinition = "CHAR(40)")
    private String reg;

    @NotNull
    @Column(columnDefinition = "text")
    private String mol;

    @Column
    @Type(type = "com.labsynch.labseer.utils.BitSetUserType")
    private BitSet substructure;

    @Column
    @Type(type = "com.labsynch.labseer.utils.BitSetUserType")
    private BitSet similarity;

    @NotNull
    @DateTimeFormat(style="M-")
    private Date recordedDate;

	@Enumerated(EnumType.STRING)
	private CmpdRegMolecule.StandardizationStatus standardizationStatus;

	private String standardizationComment;

	@Enumerated(EnumType.STRING)
	private CmpdRegMolecule.RegistrationStatus registrationStatus;

	private String registrationComment;

    @Transient
    private Double exactMolWeight;

    @Transient
    private Double averageMolWeight;

    @Transient
    private Integer totalCharge;

    @Transient
    private String smiles;

    @Transient
    private String inchi;

    @Transient
    private String molecularFormula;

    @Transient
    private HashMap<String, String> properties =  new HashMap<>();

    public void updateStructureInfo(AbstractBBChemStructure updatedBbChemStructure) {
        this.setMol(updatedBbChemStructure.getMol());
        this.setReg(updatedBbChemStructure.getReg());
        this.setPreReg(updatedBbChemStructure.getPreReg());
        this.setSubstructure(updatedBbChemStructure.getSubstructure());
        this.setSimilarity(updatedBbChemStructure.getSimilarity());
        this.setExactMolWeight(updatedBbChemStructure.getExactMolWeight());
        this.setAverageMolWeight(updatedBbChemStructure.getAverageMolWeight());
        this.setTotalCharge(updatedBbChemStructure.getTotalCharge());
        this.setSmiles(updatedBbChemStructure.getSmiles());
        this.setMolecularFormula(updatedBbChemStructure.getMolecularFormula());
        this.setStandardizationStatus(updatedBbChemStructure.getStandardizationStatus());
        this.setStandardizationComment(updatedBbChemStructure.getStandardizationComment());
        this.setRegistrationStatus(updatedBbChemStructure.getRegistrationStatus());
        this.setRegistrationComment(updatedBbChemStructure.getRegistrationComment());
        this.setRecordedDate(updatedBbChemStructure.getRecordedDate());
    } 


	@PersistenceContext
    protected
    transient EntityManager entityManager;

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("id", "preReg", "reg", "mol", "substructure", "similarity", "recordedDate", "standardizationStatus", "standardizationComment", "registrationStatus", "registrationComment", "exactMolWeight", "averageMolWeight", "totalCharge", "smiles", "inchi", "molecularFormula", "properties");

	public static final EntityManager entityManager() {
        EntityManager em = new AbstractBBChemStructure() {
        }.entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countAbstractBBChemStructures() {
        return entityManager().createQuery("SELECT COUNT(o) FROM AbstractBBChemStructure o", Long.class).getSingleResult();
    }

	public static List<AbstractBBChemStructure> findAllAbstractBBChemStructures() {
        return entityManager().createQuery("SELECT o FROM AbstractBBChemStructure o", AbstractBBChemStructure.class).getResultList();
    }

	public static List<AbstractBBChemStructure> findAllAbstractBBChemStructures(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM AbstractBBChemStructure o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, AbstractBBChemStructure.class).getResultList();
    }

	public static AbstractBBChemStructure findAbstractBBChemStructure(Long id) {
        if (id == null) return null;
        return entityManager().find(AbstractBBChemStructure.class, id);
    }

	public static List<AbstractBBChemStructure> findAbstractBBChemStructureEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM AbstractBBChemStructure o", AbstractBBChemStructure.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<AbstractBBChemStructure> findAbstractBBChemStructureEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM AbstractBBChemStructure o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, AbstractBBChemStructure.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            AbstractBBChemStructure attached = AbstractBBChemStructure.findAbstractBBChemStructure(this.id);
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
    public AbstractBBChemStructure merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        AbstractBBChemStructure merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String getPreReg() {
        return this.preReg;
    }

	public void setPreReg(String preReg) {
        this.preReg = preReg;
    }

	public String getReg() {
        return this.reg;
    }

	public void setReg(String reg) {
        this.reg = reg;
    }

	public String getMol() {
        return this.mol;
    }

	public void setMol(String mol) {
        this.mol = mol;
    }

	public BitSet getSubstructure() {
        return this.substructure;
    }

	public void setSubstructure(BitSet substructure) {
        this.substructure = substructure;
    }

	public BitSet getSimilarity() {
        return this.similarity;
    }

	public void setSimilarity(BitSet similarity) {
        this.similarity = similarity;
    }

	public Date getRecordedDate() {
        return this.recordedDate;
    }

	public void setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }

	public StandardizationStatus getStandardizationStatus() {
        return this.standardizationStatus;
    }

	public void setStandardizationStatus(StandardizationStatus standardizationStatus) {
        this.standardizationStatus = standardizationStatus;
    }

	public String getStandardizationComment() {
        return this.standardizationComment;
    }

	public void setStandardizationComment(String standardizationComment) {
        this.standardizationComment = standardizationComment;
    }

	public RegistrationStatus getRegistrationStatus() {
        return this.registrationStatus;
    }

	public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

	public String getRegistrationComment() {
        return this.registrationComment;
    }

	public void setRegistrationComment(String registrationComment) {
        this.registrationComment = registrationComment;
    }

	public Double getExactMolWeight() {
        return this.exactMolWeight;
    }

	public void setExactMolWeight(Double exactMolWeight) {
        this.exactMolWeight = exactMolWeight;
    }

	public Double getAverageMolWeight() {
        return this.averageMolWeight;
    }

	public void setAverageMolWeight(Double averageMolWeight) {
        this.averageMolWeight = averageMolWeight;
    }

	public Integer getTotalCharge() {
        return this.totalCharge;
    }

	public void setTotalCharge(Integer totalCharge) {
        this.totalCharge = totalCharge;
    }

	public String getSmiles() {
        return this.smiles;
    }

	public void setSmiles(String smiles) {
        this.smiles = smiles;
    }

	public String getInchi() {
        return this.inchi;
    }

	public void setInchi(String inchi) {
        this.inchi = inchi;
    }

	public String getMolecularFormula() {
        return this.molecularFormula;
    }

	public void setMolecularFormula(String molecularFormula) {
        this.molecularFormula = molecularFormula;
    }

	public HashMap<String, String> getProperties() {
        return this.properties;
    }

	public void setProperties(HashMap<String, String> properties) {
        this.properties = properties;
    }

	public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }

	public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }

	public static AbstractBBChemStructure fromJsonToAbstractBBChemStructure(String json) {
        return new JSONDeserializer<AbstractBBChemStructure>()
        .use(null, AbstractBBChemStructure.class).deserialize(json);
    }

	public static String toJsonArray(Collection<AbstractBBChemStructure> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }

	public static String toJsonArray(Collection<AbstractBBChemStructure> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }

	public static Collection<AbstractBBChemStructure> fromJsonArrayToAbstractBBChemStructures(String json) {
        return new JSONDeserializer<List<AbstractBBChemStructure>>()
        .use("values", AbstractBBChemStructure.class).deserialize(json);
    }

	@Version
    @Column(name = "version")
    private Integer version;

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
