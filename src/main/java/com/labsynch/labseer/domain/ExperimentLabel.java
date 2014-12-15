package com.labsynch.labseer.domain;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.transaction.annotation.Transactional;

@RooJavaBean
@RooToString
@RooJson
@RooJpaActiveRecord(finders = { "findExperimentLabelsByLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot", "findExperimentLabelsByLabelTextLikeAndLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot", "findExperimentLabelsByExperiment", "findExperimentLabelsByLabelTextLike", "findExperimentLabelsByExperimentAndIgnoredNot" })
public class ExperimentLabel extends AbstractLabel {

    private static final Logger logger = LoggerFactory.getLogger(ExperimentLabel.class);

    @NotNull
    @ManyToOne
    @JoinColumn(name = "experiment_id")
    private Experiment experiment;

    public ExperimentLabel(com.labsynch.labseer.domain.ExperimentLabel experimentLabel) {
        super.setLsType(experimentLabel.getLsType());
        super.setLsKind(experimentLabel.getLsKind());
        super.setLsTypeAndKind(experimentLabel.getLsType() + "_" + experimentLabel.getLsKind());
        super.setLabelText(experimentLabel.getLabelText());
        super.setPreferred(experimentLabel.isPreferred());
        super.setLsTransaction(experimentLabel.getLsTransaction());
        super.setRecordedBy(experimentLabel.getRecordedBy());
        if (experimentLabel.getRecordedDate() == null) {
            super.setRecordedDate(new Date());
        } else {
            super.setRecordedDate(experimentLabel.getRecordedDate());
        }
        super.setPhysicallyLabled(experimentLabel.isPhysicallyLabled());
        super.setIgnored(experimentLabel.isIgnored());
        super.setImageFile(experimentLabel.getImageFile());
    }

    public static com.labsynch.labseer.domain.ExperimentLabel update(com.labsynch.labseer.domain.ExperimentLabel experimentLabel) {
        ExperimentLabel updatedLabel = ExperimentLabel.findExperimentLabel(experimentLabel.getId());
        updatedLabel.setLsType(experimentLabel.getLsType());
        updatedLabel.setLsKind(experimentLabel.getLsKind());
        updatedLabel.setLsTypeAndKind(experimentLabel.getLsType() + "_" + experimentLabel.getLsKind());
        updatedLabel.setLabelText(experimentLabel.getLabelText());
        updatedLabel.setPreferred(experimentLabel.isPreferred());
        updatedLabel.setLsTransaction(experimentLabel.getLsTransaction());
        updatedLabel.setRecordedBy(experimentLabel.getRecordedBy());
        updatedLabel.setRecordedDate(experimentLabel.getRecordedDate());
        updatedLabel.setPhysicallyLabled(experimentLabel.isPhysicallyLabled());
        updatedLabel.setModifiedDate(new Date());
        updatedLabel.setIgnored(experimentLabel.isIgnored());
        updatedLabel.setImageFile(experimentLabel.getImageFile());
        updatedLabel.merge();
        return updatedLabel;
    }

    public static TypedQuery<com.labsynch.labseer.domain.ExperimentLabel> findExperimentPreferredName(Long experimentId) {
        String labelType = "name";
        String labelKind = "experiment name";
        boolean preferred = true;
        boolean ignored = true;
        TypedQuery<ExperimentLabel> q = findExperimentPreferredName(experimentId, labelType, labelKind, preferred, ignored);
        return q;
    }

    private static TypedQuery<com.labsynch.labseer.domain.ExperimentLabel> findExperimentPreferredName(Long experimentId, String labelType, String labelKind, boolean preferred, boolean ignored) {
        if (experimentId == null || experimentId == 0) throw new IllegalArgumentException("The experimentId argument is required");
        if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
        if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        String query = "SELECT o FROM ExperimentLabel AS o WHERE o.lsType = :labelType  " + "AND o.lsKind = :labelKind " + "AND o.experiment.id = :experimentId " + "AND o.preferred = :preferred AND o.ignored IS NOT :ignored";
        logger.debug("sql query " + query);
        TypedQuery<ExperimentLabel> q = em.createQuery(query, ExperimentLabel.class);
        q.setParameter("experimentId", experimentId);
        q.setParameter("labelType", labelType);
        q.setParameter("labelKind", labelKind);
        q.setParameter("preferred", preferred);
        q.setParameter("ignored", ignored);
        return q;
    }

    public static TypedQuery<com.labsynch.labseer.domain.ExperimentLabel> findExperimentLabelsByName(String labelText) {
        String labelType = "name";
        String labelKind = "experiment name";
        boolean preferred = true;
        boolean ignored = true;
        TypedQuery<ExperimentLabel> q = findExperimentLabelsByName(labelText, labelType, labelKind, preferred, ignored);
        return q;
    }

    public static TypedQuery<com.labsynch.labseer.domain.ExperimentLabel> findExperimentLabelsByName(String labelText, String labelType, String labelKind, boolean preferred, boolean ignored) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
        if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");
        EntityManager em = ProtocolLabel.entityManager();
        String query = "SELECT o FROM ExperimentLabel AS o WHERE o.lsType = :labelType  " + "AND o.lsKind = :labelKind " + "AND o.labelText = :labelText " + "AND o.preferred = :preferred AND o.ignored IS NOT :ignored";
        logger.debug("sql query " + query);
        TypedQuery<ExperimentLabel> q = em.createQuery(query, ExperimentLabel.class);
        logger.debug("query label text is " + labelText);
        q.setParameter("labelText", labelText);
        q.setParameter("labelType", labelType);
        q.setParameter("labelKind", labelKind);
        q.setParameter("preferred", preferred);
        q.setParameter("ignored", ignored);
        return q;
    }

    public static TypedQuery<com.labsynch.labseer.domain.ExperimentLabel> findExperimentLabelsByNameAndProtocol(String labelText, long protocolId) {
        String labelType = "name";
        String labelKind = "experiment name";
        boolean preferred = true;
        boolean ignored = true;
        return findExperimentLabelsByNameAndProtocol(labelText, labelType, labelKind, preferred, ignored, protocolId);
    }

    public static TypedQuery<com.labsynch.labseer.domain.ExperimentLabel> findExperimentLabelsByNameAndProtocol(String labelText, String labelType, String labelKind, boolean preferred, boolean ignored, long protocolId) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        if (labelType == null || labelType.length() == 0) throw new IllegalArgumentException("The labelType argument is required");
        if (labelKind == null || labelKind.length() == 0) throw new IllegalArgumentException("The labelKind argument is required");
        if (protocolId == 0) throw new IllegalArgumentException("The protocolId argument is required");
        EntityManager em = ProtocolLabel.entityManager();
        String query = "SELECT o FROM ExperimentLabel AS o WHERE o.lsType = :labelType  " + "AND o.lsKind = :labelKind " + "AND o.labelText = :labelText " + "AND o.preferred = :preferred AND o.ignored IS NOT :ignored " + "AND o.experiment.protocol.id = :protocolId";
        logger.debug("sql query " + query);
        TypedQuery<ExperimentLabel> q = em.createQuery(query, ExperimentLabel.class);
        logger.debug("query label text is " + labelText);
        q.setParameter("labelText", labelText);
        q.setParameter("labelType", labelType);
        q.setParameter("labelKind", labelKind);
        q.setParameter("preferred", preferred);
        q.setParameter("ignored", ignored);
        q.setParameter("protocolId", protocolId);
        return q;
    }

    @Transactional
    public void logicalDelete() {
        if (!this.isIgnored()) this.setIgnored(true);
    }

    public String toJson() {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    public String toJsonStub() {
        return new JSONSerializer().exclude("*.class", "experiment").transform(new ExcludeNulls(), void.class).serialize(this);
    }

    public static com.labsynch.labseer.domain.ExperimentLabel fromJsonToExperimentLabel(String json) {
        return new JSONDeserializer<ExperimentLabel>().use(null, ExperimentLabel.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }

    public static String toJsonArray(Collection<com.labsynch.labseer.domain.ExperimentLabel> collection) {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    public static String toJsonArrayStub(Collection<com.labsynch.labseer.domain.ExperimentLabel> collection) {
        return new JSONSerializer().exclude("*.class", "experiment").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    public static Collection<com.labsynch.labseer.domain.ExperimentLabel> fromJsonArrayToExperimentLabels(String json) {
        return new JSONDeserializer<List<ExperimentLabel>>().use(null, ArrayList.class).use("values", ExperimentLabel.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.ExperimentLabel> fromJsonArrayToExperimentLabels(Reader json) {
        return new JSONDeserializer<List<ExperimentLabel>>().use(null, ArrayList.class).use("values", ExperimentLabel.class).use(BigDecimal.class, new CustomBigDecimalFactory()).deserialize(json);
    }
}
