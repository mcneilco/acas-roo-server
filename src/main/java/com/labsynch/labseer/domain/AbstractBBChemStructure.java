package com.labsynch.labseer.domain;

import java.util.BitSet;
import java.util.Date;
import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.labsynch.labseer.chemclasses.CmpdRegMolecule;

import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(sequenceName = "BBCHEM_STRUCTURE_PKSEQ", inheritanceType = "TABLE_PER_CLASS")
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

    @NotNull
    @Column(columnDefinition = "CHAR(40)")
    private String preReg;

    @NotNull
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
        this.setRecordedDate(updatedBbChemStructure.getRecordedDate());
    } 

}
