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
@RooJpaActiveRecord(finders = { "findExperimentLabelsByLabelTextLikeAndLabelTypeAndKindEqualsAndPreferredNotAndIgnoredNot", "findSubjectLabelsBySubject" })
public class SubjectLabel extends AbstractLabel {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    public SubjectLabel() {
    }
    
    public SubjectLabel(com.labsynch.labseer.domain.SubjectLabel subjectLabel) {
        super.setLsType(subjectLabel.getLsType());
        super.setLsKind(subjectLabel.getLsKind());
        super.setLsTypeAndKind(subjectLabel.getLsType() + "_" + subjectLabel.getLsKind());
        super.setLabelText(subjectLabel.getLabelText());
        super.setPreferred(subjectLabel.isPreferred());
        super.setLsTransaction(subjectLabel.getLsTransaction());
        super.setRecordedBy(subjectLabel.getRecordedBy());
        super.setRecordedDate(subjectLabel.getRecordedDate());
        super.setPhysicallyLabled(subjectLabel.isPhysicallyLabled());
    }

    public static SubjectLabel update(SubjectLabel subjectLabel) {
    	SubjectLabel updatedSubjectLabel = SubjectLabel.findSubjectLabel(subjectLabel.getId());
    	updatedSubjectLabel.setLsType(subjectLabel.getLsType());
    	updatedSubjectLabel.setLsKind(subjectLabel.getLsKind());
    	updatedSubjectLabel.setLsTypeAndKind(subjectLabel.getLsType() + "_" + subjectLabel.getLsKind());
    	updatedSubjectLabel.setLabelText(subjectLabel.getLabelText());
    	updatedSubjectLabel.setPreferred(subjectLabel.isPreferred());
    	updatedSubjectLabel.setLsTransaction(subjectLabel.getLsTransaction());
    	updatedSubjectLabel.setRecordedBy(subjectLabel.getRecordedBy());
    	updatedSubjectLabel.setRecordedDate(subjectLabel.getRecordedDate());
    	updatedSubjectLabel.setModifiedDate(new Date());
    	updatedSubjectLabel.setPhysicallyLabled(subjectLabel.isPhysicallyLabled());
    	updatedSubjectLabel.setIgnored(subjectLabel.isIgnored());
    	updatedSubjectLabel.merge();
    	return updatedSubjectLabel;
    }   
    
	@Transactional
	public static int deleteByExperimentID(Long experimentId) {
		if (experimentId == null) return 0;
		EntityManager em = ItxSubjectContainer.entityManager();
		String deleteSQL = "DELETE FROM SubjectLabel oo WHERE id in (select o.id from SubjectLabel o where o.subject.treatmentGroup.analysisGroup.experiment.id = :experimentId)";
		Query q = em.createQuery(deleteSQL);
		q.setParameter("experimentId", experimentId);
		int numberOfDeletedEntities = q.executeUpdate();
		return numberOfDeletedEntities;
	}
	
    public static Collection<SubjectLabel> fromJsonArrayToSubjectLabels(String json) {
        return new JSONDeserializer<List<SubjectLabel>>().use(null, ArrayList.class).use("values", SubjectLabel.class).deserialize(json);
    }
    
    public static Collection<SubjectLabel> fromJsonArrayToSubjectLabels(Reader json) {
        return new JSONDeserializer<List<SubjectLabel>>().use(null, ArrayList.class).use("values", SubjectLabel.class).deserialize(json);
    }
    
}
