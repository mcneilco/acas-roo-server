package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity
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
        boolean ignored = true;
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        EntityManager em = ProtocolLabel.entityManager();
        String query = "SELECT o FROM ExperimentLabel AS o WHERE o.labelText = :labelText " + "AND o.ignored IS NOT :ignored";
        logger.debug("sql query " + query);
        TypedQuery<ExperimentLabel> q = em.createQuery(query, ExperimentLabel.class);
        logger.debug("query label text is " + labelText);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
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
    
    public static ExperimentLabel pickBestLabel(Collection<ExperimentLabel> labels) {
		if (labels.isEmpty()) return null;
		Collection<ExperimentLabel> preferredLabels = new HashSet<ExperimentLabel>();
		for (ExperimentLabel label : labels){
			if (label.isPreferred()) preferredLabels.add(label);
		}
		if (!preferredLabels.isEmpty()){
			ExperimentLabel bestLabel = preferredLabels.iterator().next();
			for (ExperimentLabel preferredLabel : preferredLabels){
				if (preferredLabel.getRecordedDate().compareTo(bestLabel.getRecordedDate()) > 0) bestLabel = preferredLabel;
			}
			return bestLabel;
		} else {
			Collection<ExperimentLabel> nameLabels = new HashSet<ExperimentLabel>();
			for (ExperimentLabel label : labels){
				if (label.getLsType().equals("name")) nameLabels.add(label);
			}
			if (!nameLabels.isEmpty()){
				ExperimentLabel bestLabel = nameLabels.iterator().next();
				for (ExperimentLabel nameLabel : nameLabels){
					if (nameLabel.getRecordedDate().compareTo(bestLabel.getRecordedDate()) > 0) bestLabel = nameLabel;
				}
				return bestLabel;
			} else {
				Collection<ExperimentLabel> notIgnoredLabels = new HashSet<ExperimentLabel>();
				for (ExperimentLabel label : labels){
					if (!label.isIgnored()) notIgnoredLabels.add(label);
				}
				if (!notIgnoredLabels.isEmpty()){
					ExperimentLabel bestLabel = notIgnoredLabels.iterator().next();
					for (ExperimentLabel notIgnoredLabel : notIgnoredLabels){
						if (notIgnoredLabel.getRecordedDate().compareTo(bestLabel.getRecordedDate()) > 0) bestLabel = notIgnoredLabel;
					}
					return bestLabel;
				} else {
					return labels.iterator().next();
				}
			}
		}
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
        return new JSONDeserializer<ExperimentLabel>().use(null, ExperimentLabel.class).deserialize(json);
    }

    public static String toJsonArray(Collection<com.labsynch.labseer.domain.ExperimentLabel> collection) {
        return new JSONSerializer().exclude("*.class").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    public static String toJsonArrayStub(Collection<com.labsynch.labseer.domain.ExperimentLabel> collection) {
        return new JSONSerializer().exclude("*.class", "experiment").transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    public static Collection<com.labsynch.labseer.domain.ExperimentLabel> fromJsonArrayToExperimentLabels(String json) {
        return new JSONDeserializer<List<ExperimentLabel>>().use(null, ArrayList.class).use("values", ExperimentLabel.class).deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.ExperimentLabel> fromJsonArrayToExperimentLabels(Reader json) {
        return new JSONDeserializer<List<ExperimentLabel>>().use(null, ArrayList.class).use("values", ExperimentLabel.class).deserialize(json);
    }

	public ExperimentLabel() {
        super();
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public Experiment getExperiment() {
        return this.experiment;
    }

	public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

	public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "experiment");

	public static long countExperimentLabels() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ExperimentLabel o", Long.class).getSingleResult();
    }

	public static List<ExperimentLabel> findAllExperimentLabels() {
        return entityManager().createQuery("SELECT o FROM ExperimentLabel o", ExperimentLabel.class).getResultList();
    }

	public static List<ExperimentLabel> findAllExperimentLabels(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ExperimentLabel o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ExperimentLabel.class).getResultList();
    }

	public static ExperimentLabel findExperimentLabel(Long id) {
        if (id == null) return null;
        return entityManager().find(ExperimentLabel.class, id);
    }

	public static List<ExperimentLabel> findExperimentLabelEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ExperimentLabel o", ExperimentLabel.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public static List<ExperimentLabel> findExperimentLabelEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ExperimentLabel o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ExperimentLabel.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public ExperimentLabel merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        ExperimentLabel merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static Long countFindExperimentLabelsByExperiment(Experiment experiment) {
        if (experiment == null) throw new IllegalArgumentException("The experiment argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ExperimentLabel AS o WHERE o.experiment = :experiment", Long.class);
        q.setParameter("experiment", experiment);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindExperimentLabelsByExperimentAndIgnoredNot(Experiment experiment, boolean ignored) {
        if (experiment == null) throw new IllegalArgumentException("The experiment argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ExperimentLabel AS o WHERE o.experiment = :experiment AND o.ignored IS NOT :ignored", Long.class);
        q.setParameter("experiment", experiment);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindExperimentLabelsByLabelTextLike(String labelText) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        labelText = labelText.replace('*', '%');
        if (labelText.charAt(0) != '%') {
            labelText = "%" + labelText;
        }
        if (labelText.charAt(labelText.length() - 1) != '%') {
            labelText = labelText + "%";
        }
        EntityManager em = ExperimentLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ExperimentLabel AS o WHERE LOWER(o.labelText) LIKE LOWER(:labelText)", Long.class);
        q.setParameter("labelText", labelText);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindExperimentLabelsByLabelTextLikeAndLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(String labelText, String lsTypeAndKind, boolean preferred, boolean ignored) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        labelText = labelText.replace('*', '%');
        if (labelText.charAt(0) != '%') {
            labelText = "%" + labelText;
        }
        if (labelText.charAt(labelText.length() - 1) != '%') {
            labelText = labelText + "%";
        }
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ExperimentLabel AS o WHERE LOWER(o.labelText) LIKE LOWER(:labelText)  AND o.lsTypeAndKind = :lsTypeAndKind  AND o.preferred IS NOT :preferred  AND o.ignored IS NOT :ignored", Long.class);
        q.setParameter("labelText", labelText);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        q.setParameter("preferred", preferred);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }

	public static Long countFindExperimentLabelsByLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(String lsTypeAndKind, boolean preferred, boolean ignored) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ExperimentLabel AS o WHERE o.lsTypeAndKind = :lsTypeAndKind  AND o.preferred IS NOT :preferred  AND o.ignored IS NOT :ignored", Long.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        q.setParameter("preferred", preferred);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }

	public static TypedQuery<ExperimentLabel> findExperimentLabelsByExperiment(Experiment experiment) {
        if (experiment == null) throw new IllegalArgumentException("The experiment argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        TypedQuery<ExperimentLabel> q = em.createQuery("SELECT o FROM ExperimentLabel AS o WHERE o.experiment = :experiment", ExperimentLabel.class);
        q.setParameter("experiment", experiment);
        return q;
    }

	public static TypedQuery<ExperimentLabel> findExperimentLabelsByExperiment(Experiment experiment, String sortFieldName, String sortOrder) {
        if (experiment == null) throw new IllegalArgumentException("The experiment argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ExperimentLabel AS o WHERE o.experiment = :experiment");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ExperimentLabel> q = em.createQuery(queryBuilder.toString(), ExperimentLabel.class);
        q.setParameter("experiment", experiment);
        return q;
    }

	public static TypedQuery<ExperimentLabel> findExperimentLabelsByExperimentAndIgnoredNot(Experiment experiment, boolean ignored) {
        if (experiment == null) throw new IllegalArgumentException("The experiment argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        TypedQuery<ExperimentLabel> q = em.createQuery("SELECT o FROM ExperimentLabel AS o WHERE o.experiment = :experiment AND o.ignored IS NOT :ignored", ExperimentLabel.class);
        q.setParameter("experiment", experiment);
        q.setParameter("ignored", ignored);
        return q;
    }

	public static TypedQuery<ExperimentLabel> findExperimentLabelsByExperimentAndIgnoredNot(Experiment experiment, boolean ignored, String sortFieldName, String sortOrder) {
        if (experiment == null) throw new IllegalArgumentException("The experiment argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ExperimentLabel AS o WHERE o.experiment = :experiment AND o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ExperimentLabel> q = em.createQuery(queryBuilder.toString(), ExperimentLabel.class);
        q.setParameter("experiment", experiment);
        q.setParameter("ignored", ignored);
        return q;
    }

	public static TypedQuery<ExperimentLabel> findExperimentLabelsByLabelTextLike(String labelText) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        labelText = labelText.replace('*', '%');
        if (labelText.charAt(0) != '%') {
            labelText = "%" + labelText;
        }
        if (labelText.charAt(labelText.length() - 1) != '%') {
            labelText = labelText + "%";
        }
        EntityManager em = ExperimentLabel.entityManager();
        TypedQuery<ExperimentLabel> q = em.createQuery("SELECT o FROM ExperimentLabel AS o WHERE LOWER(o.labelText) LIKE LOWER(:labelText)", ExperimentLabel.class);
        q.setParameter("labelText", labelText);
        return q;
    }

	public static TypedQuery<ExperimentLabel> findExperimentLabelsByLabelTextLike(String labelText, String sortFieldName, String sortOrder) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        labelText = labelText.replace('*', '%');
        if (labelText.charAt(0) != '%') {
            labelText = "%" + labelText;
        }
        if (labelText.charAt(labelText.length() - 1) != '%') {
            labelText = labelText + "%";
        }
        EntityManager em = ExperimentLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ExperimentLabel AS o WHERE LOWER(o.labelText) LIKE LOWER(:labelText)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ExperimentLabel> q = em.createQuery(queryBuilder.toString(), ExperimentLabel.class);
        q.setParameter("labelText", labelText);
        return q;
    }

	public static TypedQuery<ExperimentLabel> findExperimentLabelsByLabelTextLikeAndLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(String labelText, String lsTypeAndKind, boolean preferred, boolean ignored) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        labelText = labelText.replace('*', '%');
        if (labelText.charAt(0) != '%') {
            labelText = "%" + labelText;
        }
        if (labelText.charAt(labelText.length() - 1) != '%') {
            labelText = labelText + "%";
        }
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        TypedQuery<ExperimentLabel> q = em.createQuery("SELECT o FROM ExperimentLabel AS o WHERE LOWER(o.labelText) LIKE LOWER(:labelText)  AND o.lsTypeAndKind = :lsTypeAndKind  AND o.preferred IS NOT :preferred  AND o.ignored IS NOT :ignored", ExperimentLabel.class);
        q.setParameter("labelText", labelText);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        q.setParameter("preferred", preferred);
        q.setParameter("ignored", ignored);
        return q;
    }

	public static TypedQuery<ExperimentLabel> findExperimentLabelsByLabelTextLikeAndLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(String labelText, String lsTypeAndKind, boolean preferred, boolean ignored, String sortFieldName, String sortOrder) {
        if (labelText == null || labelText.length() == 0) throw new IllegalArgumentException("The labelText argument is required");
        labelText = labelText.replace('*', '%');
        if (labelText.charAt(0) != '%') {
            labelText = "%" + labelText;
        }
        if (labelText.charAt(labelText.length() - 1) != '%') {
            labelText = labelText + "%";
        }
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ExperimentLabel AS o WHERE LOWER(o.labelText) LIKE LOWER(:labelText)  AND o.lsTypeAndKind = :lsTypeAndKind  AND o.preferred IS NOT :preferred  AND o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ExperimentLabel> q = em.createQuery(queryBuilder.toString(), ExperimentLabel.class);
        q.setParameter("labelText", labelText);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        q.setParameter("preferred", preferred);
        q.setParameter("ignored", ignored);
        return q;
    }

	public static TypedQuery<ExperimentLabel> findExperimentLabelsByLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(String lsTypeAndKind, boolean preferred, boolean ignored) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        TypedQuery<ExperimentLabel> q = em.createQuery("SELECT o FROM ExperimentLabel AS o WHERE o.lsTypeAndKind = :lsTypeAndKind  AND o.preferred IS NOT :preferred  AND o.ignored IS NOT :ignored", ExperimentLabel.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        q.setParameter("preferred", preferred);
        q.setParameter("ignored", ignored);
        return q;
    }

	public static TypedQuery<ExperimentLabel> findExperimentLabelsByLsTypeAndKindEqualsAndPreferredNotAndIgnoredNot(String lsTypeAndKind, boolean preferred, boolean ignored, String sortFieldName, String sortOrder) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0) throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = ExperimentLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ExperimentLabel AS o WHERE o.lsTypeAndKind = :lsTypeAndKind  AND o.preferred IS NOT :preferred  AND o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ExperimentLabel> q = em.createQuery(queryBuilder.toString(), ExperimentLabel.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        q.setParameter("preferred", preferred);
        q.setParameter("ignored", ignored);
        return q;
    }
}
