package com.labsynch.labseer.domain;

import com.labsynch.labseer.dto.FlatThingCsvDTO;
import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findTreatmentGroupsByAnalysisGroups", "findTreatmentGroupsByLsTransactionEquals", "findTreatmentGroupsByCodeNameEquals" })
public class TreatmentGroup extends AbstractThing {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "treatmentGroup", fetch = FetchType.LAZY)
    private Set<TreatmentGroupLabel> lsLabels = new HashSet<TreatmentGroupLabel>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "treatmentGroup", fetch = FetchType.LAZY)
    private Set<TreatmentGroupState> lsStates = new HashSet<TreatmentGroupState>();

    @ManyToMany(cascade = { javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE }, mappedBy = "treatmentGroups")
    private Set<Subject> subjects = new HashSet<Subject>();

    @ManyToMany(cascade = { javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE }, fetch = FetchType.LAZY)
    @JoinTable(name = "ANALYSISGROUP_TREATMENTGROUP", joinColumns = { @javax.persistence.JoinColumn(name = "treatment_group_id") }, inverseJoinColumns = { @javax.persistence.JoinColumn(name = "analysis_group_id") })
    private Set<AnalysisGroup> analysisGroups = new HashSet<AnalysisGroup>();

    public TreatmentGroup() {
    }

    public TreatmentGroup(com.labsynch.labseer.domain.TreatmentGroup treatmentGroup) {
        super.setRecordedBy(treatmentGroup.getRecordedBy());
        super.setRecordedDate(treatmentGroup.getRecordedDate());
        super.setLsTransaction(treatmentGroup.getLsTransaction());
        super.setModifiedBy(treatmentGroup.getModifiedBy());
        super.setModifiedDate(treatmentGroup.getModifiedDate());
        super.setIgnored(treatmentGroup.isIgnored());
        super.setCodeName(treatmentGroup.getCodeName());
        super.setLsType(treatmentGroup.getLsType());
        super.setLsKind(treatmentGroup.getLsKind());
        super.setLsTypeAndKind(treatmentGroup.getLsTypeAndKind());
    }

    public TreatmentGroup(FlatThingCsvDTO treatmentGroupDTO) {
        this.setRecordedBy(treatmentGroupDTO.getRecordedBy());
        this.setRecordedDate(treatmentGroupDTO.getRecordedDate());
        this.setLsTransaction(treatmentGroupDTO.getLsTransaction());
        this.setModifiedBy(treatmentGroupDTO.getModifiedBy());
        this.setModifiedDate(treatmentGroupDTO.getModifiedDate());
        this.setCodeName(treatmentGroupDTO.getCodeName());
        this.setLsKind(treatmentGroupDTO.getLsKind());
        this.setLsType(treatmentGroupDTO.getLsType());
    }

    public static com.labsynch.labseer.domain.TreatmentGroup update(com.labsynch.labseer.domain.TreatmentGroup treatmentGroup) {
        TreatmentGroup updatedTreatmentGroup = TreatmentGroup.findTreatmentGroup(treatmentGroup.getId());
        updatedTreatmentGroup.setRecordedBy(treatmentGroup.getRecordedBy());
        updatedTreatmentGroup.setRecordedDate(treatmentGroup.getRecordedDate());
        updatedTreatmentGroup.setLsTransaction(treatmentGroup.getLsTransaction());
        updatedTreatmentGroup.setModifiedBy(treatmentGroup.getModifiedBy());
        updatedTreatmentGroup.setModifiedDate(new Date());
        updatedTreatmentGroup.setIgnored(treatmentGroup.isIgnored());
        updatedTreatmentGroup.setCodeName(treatmentGroup.getCodeName());
        updatedTreatmentGroup.setLsType(treatmentGroup.getLsType());
        updatedTreatmentGroup.setLsKind(treatmentGroup.getLsKind());
        updatedTreatmentGroup.setLsTypeAndKind(treatmentGroup.getLsTypeAndKind());
        updatedTreatmentGroup.merge();
        return updatedTreatmentGroup;
    }

    public String toJson() {
        return new JSONSerializer().include("lsLabels", "lsStates.lsValues", "subjects").exclude("*.class", "analysisGroups.experiments", "lsStates.treatmentGroup", "lsLabels.treatmentGroup", "subjects.treatmentGroups").serialize(this);
    }

    public static com.labsynch.labseer.domain.TreatmentGroup fromJsonToTreatmentGroup(String json) {
        return new JSONDeserializer<TreatmentGroup>().use(null, TreatmentGroup.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }

    public static String toJsonArray(Collection<com.labsynch.labseer.domain.TreatmentGroup> collection) {
        return new JSONSerializer().include("lsLabels", "lsStates.lsValues", "subjects").exclude("*.class", "analysisGroup.experiment", "lsStates.treatmentGroup", "lsLabels.treatmentGroup", "subjects.treatmentGroup").serialize(collection);
    }

    public static Collection<com.labsynch.labseer.domain.TreatmentGroup> fromJsonArrayToTreatmentGroups(String json) {
        return new JSONDeserializer<List<TreatmentGroup>>().use(null, ArrayList.class).use("values", TreatmentGroup.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.TreatmentGroup> fromJsonArrayToTreatmentGroups(Reader json) {
        return new JSONDeserializer<List<TreatmentGroup>>().use(null, ArrayList.class).use("values", TreatmentGroup.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }

    public static int deleteByExperimentID(Long experimentId) {
        if (experimentId == null) return 0;
        EntityManager em = SubjectValue.entityManager();
        String deleteSQL = "DELETE FROM TreatmentGroup t WHERE TreatmentGroup IN (SELECT t FROM TreatmentGroup t JOIN t.analysisGroups a JOIN a.experiments e WHERE e.id = :experimentId)";
        Query q = em.createQuery(deleteSQL);
        q.setParameter("experimentId", experimentId);
        return 0;
    }

    @Transactional
    public static Collection<java.lang.Long> removeOrphans(Collection<java.lang.Long> treatmentGroupIds) {
        Collection<Long> subjectIds = new HashSet<Long>();
        for (Long id : treatmentGroupIds) {
            TreatmentGroup treatmentGroup = TreatmentGroup.findTreatmentGroup(id);
            if (treatmentGroup.getAnalysisGroups().isEmpty()) {
                subjectIds.addAll(TreatmentGroup.removeCascadeAware(id));
            }
        }
        return subjectIds;
    }

    @Transactional
    private static Collection<java.lang.Long> removeCascadeAware(Long id) {
        TreatmentGroup treatmentGroup = findTreatmentGroup(id);
        Collection<Subject> subjects = treatmentGroup.getSubjects();
        Set<Long> subjectIds = new HashSet<Long>();
        for (Subject subject : subjects) {
            subjectIds.add(subject.getId());
        }
        subjects.clear();
        EntityManager em = TreatmentGroup.entityManager();
        Query q1 = em.createNativeQuery("DELETE FROM treatmentGroup_subject o WHERE o.treatment_group_id = :id", TreatmentGroup.class);
        q1.setParameter("id", id);
        q1.executeUpdate();
        treatmentGroup.remove();
        return subjectIds;
    }
}
