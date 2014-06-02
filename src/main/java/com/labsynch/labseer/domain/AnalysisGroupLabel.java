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

@RooJavaBean
@RooToString
@RooJson
@Transactional
@RooJpaActiveRecord(finders = { "findAnalysisGroupLabelsByAnalysisGroup", "findAnalysisGroupLabelsByLsTransactionEquals" })
public class AnalysisGroupLabel extends AbstractLabel {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "analysis_group_id")
    private AnalysisGroup analysisGroup;

    public AnalysisGroupLabel(AnalysisGroupLabel analysisGroupLabel) {
        super.setLsType(analysisGroupLabel.getLsType());
        super.setLsKind(analysisGroupLabel.getLsKind());
        super.setLsTypeAndKind(analysisGroupLabel.getLsType() + "_" + analysisGroupLabel.getLsKind());
        super.setLabelText(analysisGroupLabel.getLabelText());
        super.setPreferred(analysisGroupLabel.isPreferred());
        super.setLsTransaction(analysisGroupLabel.getLsTransaction());
        super.setRecordedBy(analysisGroupLabel.getRecordedBy());
        super.setRecordedDate(analysisGroupLabel.getRecordedDate());
        super.setPhysicallyLabled(analysisGroupLabel.isPhysicallyLabled());
    }
 
    public static AnalysisGroupLabel update(AnalysisGroupLabel analysisGroupLabel) {
    	AnalysisGroupLabel updatedAnalysisGroupLabel = AnalysisGroupLabel.findAnalysisGroupLabel(analysisGroupLabel.getId());
    	updatedAnalysisGroupLabel.setLsType(analysisGroupLabel.getLsType());
    	updatedAnalysisGroupLabel.setLsKind(analysisGroupLabel.getLsKind());
    	updatedAnalysisGroupLabel.setLsTypeAndKind(analysisGroupLabel.getLsType() + "_" + analysisGroupLabel.getLsKind());
    	updatedAnalysisGroupLabel.setLabelText(analysisGroupLabel.getLabelText());
    	updatedAnalysisGroupLabel.setPreferred(analysisGroupLabel.isPreferred());
    	updatedAnalysisGroupLabel.setLsTransaction(analysisGroupLabel.getLsTransaction());
    	updatedAnalysisGroupLabel.setRecordedBy(analysisGroupLabel.getRecordedBy());
    	updatedAnalysisGroupLabel.setRecordedDate(analysisGroupLabel.getRecordedDate());
    	updatedAnalysisGroupLabel.setModifiedDate(new Date());
    	updatedAnalysisGroupLabel.setPhysicallyLabled(analysisGroupLabel.isPhysicallyLabled());
    	updatedAnalysisGroupLabel.merge();
    	return updatedAnalysisGroupLabel;
    }
    
    public static Collection<AnalysisGroupLabel> fromJsonArrayToAnalysisGroupLabels(String json) {
        return new JSONDeserializer<List<AnalysisGroupLabel>>().use(null, ArrayList.class).use("values", AnalysisGroupLabel.class).deserialize(json);
    }
    
    public static Collection<AnalysisGroupLabel> fromJsonArrayToAnalysisGroupLabels(Reader json) {
        return new JSONDeserializer<List<AnalysisGroupLabel>>().use(null, ArrayList.class).use("values", AnalysisGroupLabel.class).deserialize(json);
    }

	public static int deleteByExperimentID(Long experimentId) {
		if (experimentId == null) return 0;
		EntityManager em = SubjectValue.entityManager();
		String deleteSQL = "DELETE FROM AnalysisGroupLabel oo WHERE id in (select o.id from AnalysisGroupLabel o where o.analysisGroup.experiment.id = :experimentId)";

		Query q = em.createQuery(deleteSQL);
		q.setParameter("experimentId", experimentId);
		int numberOfDeletedEntities = q.executeUpdate();
		return numberOfDeletedEntities;
	}
    
    
}
