package com.labsynch.labseer.domain;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.TypedQuery;
import jakarta.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Configurable
@Entity

public class ContainerLabel extends AbstractLabel {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "container_id")
    private Container container;

    public ContainerLabel(com.labsynch.labseer.domain.ContainerLabel containerLabel) {
        super.setLsType(containerLabel.getLsType());
        super.setLsKind(containerLabel.getLsKind());
        super.setLsTypeAndKind(containerLabel.getLsType() + "_" + containerLabel.getLsKind());
        super.setLabelText(containerLabel.getLabelText());
        super.setPreferred(containerLabel.isPreferred());
        super.setLsTransaction(containerLabel.getLsTransaction());
        super.setRecordedBy(containerLabel.getRecordedBy());
        super.setRecordedDate(containerLabel.getRecordedDate());
        super.setPhysicallyLabled(containerLabel.isPhysicallyLabled());
    }

    public static ContainerLabel update(ContainerLabel containerLabel) {
        ContainerLabel updatedContainerLabel = ContainerLabel.findContainerLabel(containerLabel.getId());
        updatedContainerLabel.setLsType(containerLabel.getLsType());
        updatedContainerLabel.setLsKind(containerLabel.getLsKind());
        updatedContainerLabel.setLsTypeAndKind(containerLabel.getLsType() + "_" + containerLabel.getLsKind());
        updatedContainerLabel.setLabelText(containerLabel.getLabelText());
        updatedContainerLabel.setPreferred(containerLabel.isPreferred());
        updatedContainerLabel.setLsTransaction(containerLabel.getLsTransaction());
        updatedContainerLabel.setRecordedBy(containerLabel.getRecordedBy());
        updatedContainerLabel.setRecordedDate(containerLabel.getRecordedDate());
        updatedContainerLabel.setModifiedDate(new Date());
        updatedContainerLabel.setPhysicallyLabled(containerLabel.isPhysicallyLabled());
        updatedContainerLabel.setIgnored(containerLabel.isIgnored());
        updatedContainerLabel.merge();
        return updatedContainerLabel;
    }

    public static TypedQuery<ContainerLabel> findContainerLabelsByContainerAndIgnoredNot(Container container,
            boolean ignored) {
        if (container == null)
            throw new IllegalArgumentException("The container argument is required");
        EntityManager em = ContainerLabel.entityManager();
        TypedQuery<ContainerLabel> q = em.createQuery(
                "SELECT o FROM ContainerLabel AS o WHERE o.container = :container AND o.ignored IS NOT :ignored",
                ContainerLabel.class);
        q.setParameter("container", container);
        q.setParameter("ignored", ignored);
        return q;
    }

    public static TypedQuery<ContainerLabel> findContainerLabelsByLabelTextEqualsAndIgnoredNot(String labelText,
            boolean ignored) {
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");
        EntityManager em = ContainerLabel.entityManager();
        TypedQuery<ContainerLabel> q = em.createQuery(
                "SELECT o FROM ContainerLabel AS o WHERE o.labelText = :labelText  AND o.ignored IS NOT :ignored",
                ContainerLabel.class);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        return q;
    }

    public String toJson() {
        return new JSONSerializer().exclude("*.class").serialize(this);
    }

    public static ContainerLabel fromJsonToContainerLabel(String json) {
        return new JSONDeserializer<ContainerLabel>().use(null, ContainerLabel.class).deserialize(json);
    }

    public static String toJsonArray(Collection<ContainerLabel> collection) {
        return new JSONSerializer().exclude("*.class").serialize(collection);
    }

    public static Collection<ContainerLabel> fromJsonArrayToContainerLabels(String json) {
        return new JSONDeserializer<List<ContainerLabel>>().use(null, ArrayList.class)
                .use("values", ContainerLabel.class).deserialize(json);
    }

    public static Collection<ContainerLabel> fromJsonArrayToContainerLabels(Reader json) {
        return new JSONDeserializer<List<ContainerLabel>>().use(null, ArrayList.class)
                .use("values", ContainerLabel.class).deserialize(json);
    }

    public static ContainerLabel pickBestLabel(Collection<ContainerLabel> labels) {
        if (labels.isEmpty())
            return null;
        Collection<ContainerLabel> preferredLabels = new HashSet<ContainerLabel>();
        for (ContainerLabel label : labels) {
            if (label.isPreferred())
                preferredLabels.add(label);
        }
        if (!preferredLabels.isEmpty()) {
            ContainerLabel bestLabel = preferredLabels.iterator().next();
            for (ContainerLabel preferredLabel : preferredLabels) {
                if (preferredLabel.getRecordedDate().compareTo(bestLabel.getRecordedDate()) > 0)
                    bestLabel = preferredLabel;
            }
            return bestLabel;
        } else {
            Collection<ContainerLabel> nameLabels = new HashSet<ContainerLabel>();
            for (ContainerLabel label : labels) {
                if (label.getLsType().equals("name"))
                    nameLabels.add(label);
            }
            if (!nameLabels.isEmpty()) {
                ContainerLabel bestLabel = nameLabels.iterator().next();
                for (ContainerLabel nameLabel : nameLabels) {
                    if (nameLabel.getRecordedDate().compareTo(bestLabel.getRecordedDate()) > 0)
                        bestLabel = nameLabel;
                }
                return bestLabel;
            } else {
                Collection<ContainerLabel> notIgnoredLabels = new HashSet<ContainerLabel>();
                for (ContainerLabel label : labels) {
                    if (!label.isIgnored())
                        notIgnoredLabels.add(label);
                }
                if (!notIgnoredLabels.isEmpty()) {
                    ContainerLabel bestLabel = notIgnoredLabels.iterator().next();
                    for (ContainerLabel notIgnoredLabel : notIgnoredLabels) {
                        if (notIgnoredLabel.getRecordedDate().compareTo(bestLabel.getRecordedDate()) > 0)
                            bestLabel = notIgnoredLabel;
                    }
                    return bestLabel;
                } else {
                    return labels.iterator().next();
                }
            }
        }
    }

    public Container getContainer() {
        return this.container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public static Long countFindContainerLabelsByContainerAndIgnoredNot(Container container, boolean ignored) {
        if (container == null)
            throw new IllegalArgumentException("The container argument is required");
        EntityManager em = ContainerLabel.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM ContainerLabel AS o WHERE o.container = :container AND o.ignored IS NOT :ignored",
                Long.class);
        q.setParameter("container", container);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindContainerLabelsByLabelTextEqualsAndIgnoredNot(String labelText, boolean ignored) {
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");
        EntityManager em = ContainerLabel.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM ContainerLabel AS o WHERE o.labelText = :labelText  AND o.ignored IS NOT :ignored",
                Long.class);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindContainerLabelsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ContainerLabel.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ContainerLabel AS o WHERE o.lsTransaction = :lsTransaction",
                Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindContainerLabelsByLsTypeEqualsAndLabelTextEqualsAndIgnoredNot(String lsType,
            String labelText, boolean ignored) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");
        EntityManager em = ContainerLabel.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM ContainerLabel AS o WHERE o.lsType = :lsType  AND o.labelText = :labelText  AND o.ignored IS NOT :ignored",
                Long.class);
        q.setParameter("lsType", lsType);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<ContainerLabel> findContainerLabelsByContainerAndIgnoredNot(Container container,
            boolean ignored, String sortFieldName, String sortOrder) {
        if (container == null)
            throw new IllegalArgumentException("The container argument is required");
        EntityManager em = ContainerLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM ContainerLabel AS o WHERE o.container = :container AND o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ContainerLabel> q = em.createQuery(queryBuilder.toString(), ContainerLabel.class);
        q.setParameter("container", container);
        q.setParameter("ignored", ignored);
        return q;
    }

    public static TypedQuery<ContainerLabel> findContainerLabelsByLabelTextEqualsAndIgnoredNot(String labelText,
            boolean ignored, String sortFieldName, String sortOrder) {
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");
        EntityManager em = ContainerLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM ContainerLabel AS o WHERE o.labelText = :labelText  AND o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ContainerLabel> q = em.createQuery(queryBuilder.toString(), ContainerLabel.class);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        return q;
    }

    public static TypedQuery<ContainerLabel> findContainerLabelsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ContainerLabel.entityManager();
        TypedQuery<ContainerLabel> q = em.createQuery(
                "SELECT o FROM ContainerLabel AS o WHERE o.lsTransaction = :lsTransaction", ContainerLabel.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

    public static TypedQuery<ContainerLabel> findContainerLabelsByLsTransactionEquals(Long lsTransaction,
            String sortFieldName, String sortOrder) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ContainerLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM ContainerLabel AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ContainerLabel> q = em.createQuery(queryBuilder.toString(), ContainerLabel.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

    public static TypedQuery<ContainerLabel> findContainerLabelsByLsTypeEqualsAndLabelTextEqualsAndIgnoredNot(
            String lsType, String labelText, boolean ignored) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");
        EntityManager em = ContainerLabel.entityManager();
        TypedQuery<ContainerLabel> q = em.createQuery(
                "SELECT o FROM ContainerLabel AS o WHERE o.lsType = :lsType  AND o.labelText = :labelText  AND o.ignored IS NOT :ignored",
                ContainerLabel.class);
        q.setParameter("lsType", lsType);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        return q;
    }

    public static TypedQuery<ContainerLabel> findContainerLabelsByLsTypeEqualsAndLabelTextEqualsAndIgnoredNot(
            String lsType, String labelText, boolean ignored, String sortFieldName, String sortOrder) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");
        EntityManager em = ContainerLabel.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM ContainerLabel AS o WHERE o.lsType = :lsType  AND o.labelText = :labelText  AND o.ignored IS NOT :ignored");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ContainerLabel> q = em.createQuery(queryBuilder.toString(), ContainerLabel.class);
        q.setParameter("lsType", lsType);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        return q;
    }

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("container");

    public static long countContainerLabels() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ContainerLabel o", Long.class).getSingleResult();
    }

    public static List<ContainerLabel> findAllContainerLabels() {
        return entityManager().createQuery("SELECT o FROM ContainerLabel o", ContainerLabel.class).getResultList();
    }

    public static List<ContainerLabel> findAllContainerLabels(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ContainerLabel o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ContainerLabel.class).getResultList();
    }

    public static ContainerLabel findContainerLabel(Long id) {
        if (id == null)
            return null;
        return entityManager().find(ContainerLabel.class, id);
    }

    public static List<ContainerLabel> findContainerLabelEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ContainerLabel o", ContainerLabel.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    public static List<ContainerLabel> findContainerLabelEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM ContainerLabel o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ContainerLabel.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public ContainerLabel merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        ContainerLabel merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public ContainerLabel() {
        super();
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
