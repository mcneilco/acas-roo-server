package com.labsynch.labseer.domain;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJson
@Transactional
@RooJpaActiveRecord(finders = { "findTreatmentGroupLabelsByTreatmentGroup", "findTreatmentGroupLabelsByLsTransactionEquals" })
public class TreatmentGroupLabel extends AbstractLabel {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "treatment_group_id")
    private TreatmentGroup treatmentGroup;

    public TreatmentGroupLabel() {
    }
    
    public TreatmentGroupLabel(com.labsynch.labseer.domain.TreatmentGroupLabel treatmentGroupLabel) {
        super.setLsType(treatmentGroupLabel.getLsType());
        super.setLsKind(treatmentGroupLabel.getLsKind());
        super.setLsTypeAndKind(treatmentGroupLabel.getLsType() + "_" + treatmentGroupLabel.getLsKind());
        super.setLabelText(treatmentGroupLabel.getLabelText());
        super.setPreferred(treatmentGroupLabel.isPreferred());
        super.setLsTransaction(treatmentGroupLabel.getLsTransaction());
        super.setRecordedBy(treatmentGroupLabel.getRecordedBy());
        super.setRecordedDate(treatmentGroupLabel.getRecordedDate());
        super.setPhysicallyLabled(treatmentGroupLabel.isPhysicallyLabled());
    }
    
    public static TreatmentGroupLabel update(TreatmentGroupLabel treatmentGroupLabel) {
    	TreatmentGroupLabel updatedTreatmentGroupLabel = TreatmentGroupLabel.findTreatmentGroupLabel(treatmentGroupLabel.getId());
    	updatedTreatmentGroupLabel.setLsType(treatmentGroupLabel.getLsType());
    	updatedTreatmentGroupLabel.setLsKind(treatmentGroupLabel.getLsKind());
    	updatedTreatmentGroupLabel.setLsTypeAndKind(treatmentGroupLabel.getLsType() + "_" + treatmentGroupLabel.getLsKind());
    	updatedTreatmentGroupLabel.setLabelText(treatmentGroupLabel.getLabelText());
    	updatedTreatmentGroupLabel.setPreferred(treatmentGroupLabel.isPreferred());
    	updatedTreatmentGroupLabel.setLsTransaction(treatmentGroupLabel.getLsTransaction());
    	updatedTreatmentGroupLabel.setRecordedBy(treatmentGroupLabel.getRecordedBy());
    	updatedTreatmentGroupLabel.setRecordedDate(treatmentGroupLabel.getRecordedDate());
    	updatedTreatmentGroupLabel.setModifiedDate(new Date());
    	updatedTreatmentGroupLabel.setPhysicallyLabled(treatmentGroupLabel.isPhysicallyLabled());
    	updatedTreatmentGroupLabel.merge();
    	return updatedTreatmentGroupLabel;
    }
    
    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }
    
    public static TreatmentGroupLabel fromJsonToTreatmentGroupLabel(String json) {
        return new JSONDeserializer<TreatmentGroupLabel>().use(null, TreatmentGroupLabel.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<TreatmentGroupLabel> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }
    
    public static Collection<TreatmentGroupLabel> fromJsonArrayToTreatmentGroupLabels(String json) {
        return new JSONDeserializer<List<TreatmentGroupLabel>>().use(null, ArrayList.class).use("values", TreatmentGroupLabel.class).deserialize(json);
    }

    public static Collection<TreatmentGroupLabel> fromJsonArrayToTreatmentGroupLabels(Reader json) {
        return new JSONDeserializer<List<TreatmentGroupLabel>>().use(null, ArrayList.class).use("values", TreatmentGroupLabel.class).deserialize(json);
    }

	public static int deleteByExperimentID(Long experimentId) {
		if (experimentId == null) return 0;
		EntityManager em = SubjectValue.entityManager();
		String deleteSQL = "DELETE FROM TreatmentGroupLabel oo WHERE id in (select o.id from TreatmentGroupLabel o where o.treatmentGroup.analysisGroup.experiment.id = :experimentId)";

		Query q = em.createQuery(deleteSQL);
		q.setParameter("experimentId", experimentId);
		int numberOfDeletedEntities = q.executeUpdate();
		return numberOfDeletedEntities;
	}
}
