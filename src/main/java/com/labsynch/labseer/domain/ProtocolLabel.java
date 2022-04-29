package com.labsynch.labseer.domain;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity

public class ProtocolLabel extends AbstractLabel {

    private static final Logger logger = LoggerFactory.getLogger(ProtocolLabel.class);

    @NotNull
    @ManyToOne
    @JoinColumn(name = "protocol_id")
    private Protocol protocol;

    public ProtocolLabel(com.labsynch.labseer.domain.ProtocolLabel protocolLabel) {
        super.setLsType(protocolLabel.getLsType());
        super.setLsKind(protocolLabel.getLsKind());
        super.setLsTypeAndKind(protocolLabel.getLsType() + "_" + protocolLabel.getLsKind());
        super.setLabelText(protocolLabel.getLabelText());
        super.setPreferred(protocolLabel.isPreferred());
        super.setLsTransaction(protocolLabel.getLsTransaction());
        super.setRecordedBy(protocolLabel.getRecordedBy());
        super.setRecordedDate(protocolLabel.getRecordedDate());
        super.setPhysicallyLabled(protocolLabel.isPhysicallyLabled());
    }

    public static com.labsynch.labseer.domain.ProtocolLabel update(
            com.labsynch.labseer.domain.ProtocolLabel protocolLabel) {
        ProtocolLabel updatedProtocolLabel = ProtocolLabel.findProtocolLabel(protocolLabel.getId());
        updatedProtocolLabel.setLsType(protocolLabel.getLsType());
        updatedProtocolLabel.setLsKind(protocolLabel.getLsKind());
        updatedProtocolLabel.setLsTypeAndKind(protocolLabel.getLsType() + "_" + protocolLabel.getLsKind());
        updatedProtocolLabel.setLabelText(protocolLabel.getLabelText());
        updatedProtocolLabel.setPreferred(protocolLabel.isPreferred());
        updatedProtocolLabel.setLsTransaction(protocolLabel.getLsTransaction());
        updatedProtocolLabel.setRecordedBy(protocolLabel.getRecordedBy());
        updatedProtocolLabel.setRecordedDate(protocolLabel.getRecordedDate());
        updatedProtocolLabel.setModifiedDate(new Date());
        updatedProtocolLabel.setPhysicallyLabled(protocolLabel.isPhysicallyLabled());
        updatedProtocolLabel.setIgnored(protocolLabel.isIgnored());
        updatedProtocolLabel.merge();
        return updatedProtocolLabel;
    }

    public static ProtocolLabel findProtocolLabel(Long id) {
        if (id == null)
            return null;
        return entityManager().find(ProtocolLabel.class, id);
    }

    public static TypedQuery<ProtocolLabel> findProtocolPreferredName(Long protocolId) {

        if (protocolId == null || protocolId == 0) {
            return null;
        }

        String labelType = "name";
        String labelKind = "protocol name";
        boolean preferred = true;
        boolean ignored = true;

        TypedQuery<ProtocolLabel> q = findProtocolPreferredName(protocolId, labelType, labelKind, preferred, ignored);
        return q;
    }

    private static TypedQuery<ProtocolLabel> findProtocolPreferredName(
            Long protocolId, String labelType, String labelKind,
            boolean preferred, boolean ignored) {

        if (protocolId == null || protocolId == 0)
            throw new IllegalArgumentException("The protocolId argument is required");
        if (labelType == null || labelType.length() == 0)
            throw new IllegalArgumentException("The labelType argument is required");
        if (labelKind == null || labelKind.length() == 0)
            throw new IllegalArgumentException("The labelKind argument is required");

        EntityManager em = ProtocolLabel.entityManager();
        String query = "SELECT o FROM ProtocolLabel AS o WHERE o.lsType = :labelType  "
                + "AND o.lsKind = :labelKind "
                + "AND o.protocol.id = :protocolId "
                + "AND o.preferred = :preferred AND o.ignored IS NOT :ignored";
        logger.debug("sql query " + query);
        TypedQuery<ProtocolLabel> q = em.createQuery(query, ProtocolLabel.class);
        q.setParameter("protocolId", protocolId);
        q.setParameter("labelType", labelType);
        q.setParameter("labelKind", labelKind);
        q.setParameter("preferred", preferred);
        q.setParameter("ignored", ignored);
        return q;
    }

    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

    public static ProtocolLabel fromJsonToProtocolLabel(String json) {
        return new JSONDeserializer<ProtocolLabel>().use(null, ProtocolLabel.class).deserialize(json);
    }

    public static String toJsonArray(Collection<ProtocolLabel> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

    public static Collection<ProtocolLabel> fromJsonArrayToProtocolLabels(String json) {
        return new JSONDeserializer<List<ProtocolLabel>>().use(null, ArrayList.class).use("values", ProtocolLabel.class)
                .deserialize(json);
    }

    public static Collection<ProtocolLabel> fromJsonArrayToProtocolLabels(Reader json) {
        return new JSONDeserializer<List<ProtocolLabel>>().use(null, ArrayList.class).use("values", ProtocolLabel.class)
                .deserialize(json);
    }

    public static TypedQuery<com.labsynch.labseer.domain.ProtocolLabel> findProtocolNames(Protocol protocol) {
        String labelType = "name";
        String labelKind = "protocol name";
        boolean preferred = true;
        if (protocol == null)
            throw new IllegalArgumentException("The protocol argument is required");
        EntityManager em = ProtocolLabel.entityManager();
        TypedQuery<ProtocolLabel> q = em.createQuery("SELECT o FROM ProtocolLabel AS o WHERE o.protocol = :protocol "
                + "AND o.lsType = :labelType " + "AND o.lsKind = :labelKind " + "AND o.preferred = :preferred",
                ProtocolLabel.class);
        q.setParameter("protocol", protocol);
        q.setParameter("labelType", labelType);
        q.setParameter("labelKind", labelKind);
        q.setParameter("preferred", preferred);
        return q;
    }

    public static TypedQuery<com.labsynch.labseer.domain.ProtocolLabel> findProtocolNames(Protocol protocol,
            boolean ignored) {
        String labelType = "name";
        String labelKind = "protocol name";
        boolean preferred = true;
        if (protocol == null)
            throw new IllegalArgumentException("The protocol argument is required");
        EntityManager em = ProtocolLabel.entityManager();
        TypedQuery<ProtocolLabel> q = em.createQuery(
                "SELECT o FROM ProtocolLabel AS o WHERE o.protocol = :protocol " + "AND o.lsType = :labelType "
                        + "AND o.lsKind = :labelKind " + "AND o.preferred = :preferred AND o.ignored IS NOT :ignored",
                ProtocolLabel.class);
        q.setParameter("protocol", protocol);
        q.setParameter("labelType", labelType);
        q.setParameter("labelKind", labelKind);
        q.setParameter("preferred", preferred);
        q.setParameter("ignored", ignored);
        return q;
    }

    public static TypedQuery<com.labsynch.labseer.domain.ProtocolLabel> findProtocolLabelsByName(String labelText) {
        boolean ignored = true;
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");
        EntityManager em = ProtocolLabel.entityManager();
        TypedQuery<ProtocolLabel> q = em.createQuery(
                "SELECT o FROM ProtocolLabel AS o WHERE o.labelText = :labelText " + "AND o.ignored IS NOT :ignored",
                ProtocolLabel.class);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        return q;
    }

    public static TypedQuery<com.labsynch.labseer.domain.ProtocolLabel> findProtocolLabelsByName(String labelText,
            String labelType, String labelKind, boolean preferred, boolean ignored) {
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");
        if (labelType == null || labelType.length() == 0)
            throw new IllegalArgumentException("The labelType argument is required");
        if (labelKind == null || labelKind.length() == 0)
            throw new IllegalArgumentException("The labelKind argument is required");
        EntityManager em = ProtocolLabel.entityManager();
        TypedQuery<ProtocolLabel> q = em.createQuery("SELECT o FROM ProtocolLabel AS o WHERE o.lsType = :labelType  "
                + "AND o.lsKind = :labelKind " + "AND o.labelText = :labelText "
                + "AND o.preferred = :preferred AND o.ignored IS NOT :ignored", ProtocolLabel.class);
        q.setParameter("labelText", labelText);
        q.setParameter("labelType", labelType);
        q.setParameter("labelKind", labelKind);
        q.setParameter("preferred", preferred);
        q.setParameter("ignored", ignored);
        return q;
    }

    public static TypedQuery<com.labsynch.labseer.domain.ProtocolLabel> findProtocolLabelsByNameLike(String labelText) {
        String labelType = "name";
        String labelKind = "protocol name";
        boolean preferred = true;
        boolean ignored = true;
        TypedQuery<ProtocolLabel> q = findProtocolLabelsByNameLike(labelText, labelType, labelKind, preferred, ignored);
        return q;
    }

    public static TypedQuery<ProtocolLabel> findProtocolLabelsByNameLike(String labelText, String labelType,
            String labelKind, boolean preferred, boolean ignored) {
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");
        if (labelType == null || labelType.length() == 0)
            throw new IllegalArgumentException("The labelType argument is required");
        if (labelKind == null || labelKind.length() == 0)
            throw new IllegalArgumentException("The labelKind argument is required");
        EntityManager em = ProtocolLabel.entityManager();

        labelText = labelText.replace('*', '%');
        if (labelText.charAt(0) != '%') {
            labelText = "%" + labelText;
        }
        if (labelText.charAt(labelText.length() - 1) != '%') {
            labelText = labelText + "%";
        }

        TypedQuery<ProtocolLabel> q = em.createQuery("SELECT o FROM ProtocolLabel AS o WHERE o.lsType = :labelType  "
                + "AND o.lsKind = :labelKind "
                + "AND LOWER(o.labelText) LIKE LOWER(:labelText) "
                + "AND o.preferred = :preferred AND o.ignored IS NOT :ignored", ProtocolLabel.class);
        q.setParameter("labelText", labelText);
        q.setParameter("labelType", labelType);
        q.setParameter("labelKind", labelKind);
        q.setParameter("preferred", preferred);
        q.setParameter("ignored", ignored);
        return q;
    }

    public Protocol getProtocol() {
        return this.protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public ProtocolLabel() {
        super();
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "protocol");

    public static long countProtocolLabels() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ProtocolLabel o", Long.class).getSingleResult();
    }

    public static List<ProtocolLabel> findAllProtocolLabels() {
        return entityManager().createQuery("SELECT o FROM ProtocolLabel o", ProtocolLabel.class).getResultList();
    }

    public static List<ProtocolLabel> findAllProtocolLabels(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ProtocolLabel o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ProtocolLabel.class).getResultList();
    }

    public static List<ProtocolLabel> findProtocolLabelEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ProtocolLabel o", ProtocolLabel.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<ProtocolLabel> findProtocolLabelEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM ProtocolLabel o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ProtocolLabel.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public ProtocolLabel merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        ProtocolLabel merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public static Long countFindProtocolLabelsByLabelTextEqualsAndIgnoredNot(String labelText, boolean ignored) {
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");
        EntityManager em = ProtocolLabel.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM ProtocolLabel AS o WHERE o.labelText = :labelText  AND o.ignored IS NOT :ignored",
                Long.class);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindProtocolLabelsByLabelTextLike(String labelText) {
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");
        labelText = labelText.replace('*', '%');
        if (labelText.charAt(0) != '%') {
            labelText = "%" + labelText;
        }
        if (labelText.charAt(labelText.length() - 1) != '%') {
            labelText = labelText + "%";
        }
        EntityManager em = ProtocolLabel.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM ProtocolLabel AS o WHERE LOWER(o.labelText) LIKE LOWER(:labelText)", Long.class);
        q.setParameter("labelText", labelText);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindProtocolLabelsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ProtocolLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ProtocolLabel AS o WHERE o.lsTransaction = :lsTransaction",
                Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindProtocolLabelsByProtocol(Protocol protocol) {
        if (protocol == null)
            throw new IllegalArgumentException("The protocol argument is required");
        EntityManager em = ProtocolLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ProtocolLabel AS o WHERE o.protocol = :protocol",
                Long.class);
        q.setParameter("protocol", protocol);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<ProtocolLabel> findProtocolLabelsByLabelTextEqualsAndIgnoredNot(String labelText,
            boolean ignored) {
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");
        EntityManager em = ProtocolLabel.entityManager();
        TypedQuery<ProtocolLabel> q = em.createQuery(
                "SELECT o FROM ProtocolLabel AS o WHERE o.labelText = :labelText  AND o.ignored IS NOT :ignored",
                ProtocolLabel.class);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        return q;
    }

    public static TypedQuery<ProtocolLabel> findProtocolLabelsByLabelTextEqualsAndIgnoredNot(String labelText,
            boolean ignored, String sortFieldName, String sortOrder) {
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");
        EntityManager em = ProtocolLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM ProtocolLabel AS o WHERE o.labelText = :labelText  AND o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ProtocolLabel> q = em.createQuery(queryBuilder.toString(), ProtocolLabel.class);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        return q;
    }

    public static TypedQuery<ProtocolLabel> findProtocolLabelsByLabelTextLike(String labelText) {
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");
        labelText = labelText.replace('*', '%');
        if (labelText.charAt(0) != '%') {
            labelText = "%" + labelText;
        }
        if (labelText.charAt(labelText.length() - 1) != '%') {
            labelText = labelText + "%";
        }
        EntityManager em = ProtocolLabel.entityManager();
        TypedQuery<ProtocolLabel> q = em.createQuery(
                "SELECT o FROM ProtocolLabel AS o WHERE LOWER(o.labelText) LIKE LOWER(:labelText)",
                ProtocolLabel.class);
        q.setParameter("labelText", labelText);
        return q;
    }

    public static TypedQuery<ProtocolLabel> findProtocolLabelsByLabelTextLike(String labelText, String sortFieldName,
            String sortOrder) {
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");
        labelText = labelText.replace('*', '%');
        if (labelText.charAt(0) != '%') {
            labelText = "%" + labelText;
        }
        if (labelText.charAt(labelText.length() - 1) != '%') {
            labelText = labelText + "%";
        }
        EntityManager em = ProtocolLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM ProtocolLabel AS o WHERE LOWER(o.labelText) LIKE LOWER(:labelText)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ProtocolLabel> q = em.createQuery(queryBuilder.toString(), ProtocolLabel.class);
        q.setParameter("labelText", labelText);
        return q;
    }

    public static TypedQuery<ProtocolLabel> findProtocolLabelsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ProtocolLabel.entityManager();
        TypedQuery<ProtocolLabel> q = em.createQuery(
                "SELECT o FROM ProtocolLabel AS o WHERE o.lsTransaction = :lsTransaction", ProtocolLabel.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

    public static TypedQuery<ProtocolLabel> findProtocolLabelsByLsTransactionEquals(Long lsTransaction,
            String sortFieldName, String sortOrder) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ProtocolLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM ProtocolLabel AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ProtocolLabel> q = em.createQuery(queryBuilder.toString(), ProtocolLabel.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

    public static TypedQuery<ProtocolLabel> findProtocolLabelsByProtocol(Protocol protocol) {
        if (protocol == null)
            throw new IllegalArgumentException("The protocol argument is required");
        EntityManager em = ProtocolLabel.entityManager();
        TypedQuery<ProtocolLabel> q = em.createQuery("SELECT o FROM ProtocolLabel AS o WHERE o.protocol = :protocol",
                ProtocolLabel.class);
        q.setParameter("protocol", protocol);
        return q;
    }

    public static TypedQuery<ProtocolLabel> findProtocolLabelsByProtocol(Protocol protocol, String sortFieldName,
            String sortOrder) {
        if (protocol == null)
            throw new IllegalArgumentException("The protocol argument is required");
        EntityManager em = ProtocolLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM ProtocolLabel AS o WHERE o.protocol = :protocol");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ProtocolLabel> q = em.createQuery(queryBuilder.toString(), ProtocolLabel.class);
        q.setParameter("protocol", protocol);
        return q;
    }
}
