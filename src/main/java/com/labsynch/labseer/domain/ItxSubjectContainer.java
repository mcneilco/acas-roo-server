package com.labsynch.labseer.domain;

import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import com.labsynch.labseer.utils.CustomBigDecimalFactory;
import com.labsynch.labseer.utils.ExcludeNulls;

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

public class ItxSubjectContainer extends AbstractThing {

    private static final Logger logger = LoggerFactory.getLogger(ItxSubjectContainer.class);

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "container_id")
    private Container container;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itxSubjectContainer", fetch = FetchType.LAZY)
    private Set<ItxSubjectContainerState> lsStates = new HashSet<ItxSubjectContainerState>();

    public ItxSubjectContainer() {
    }

    public ItxSubjectContainer(com.labsynch.labseer.domain.ItxSubjectContainer itxSubjectContainer) {
        super.setRecordedBy(itxSubjectContainer.getRecordedBy());
        super.setRecordedDate(itxSubjectContainer.getRecordedDate());
        super.setLsTransaction(itxSubjectContainer.getLsTransaction());
        super.setModifiedBy(itxSubjectContainer.getModifiedBy());
        super.setModifiedDate(itxSubjectContainer.getModifiedDate());
        super.setCodeName(itxSubjectContainer.getCodeName());
        super.setLsType(itxSubjectContainer.getLsType());
        super.setLsKind(itxSubjectContainer.getLsKind());
        super.setLsTypeAndKind(itxSubjectContainer.getLsTypeAndKind());
        this.subject = itxSubjectContainer.getSubject();
        this.container = itxSubjectContainer.getContainer();
    }

    public static ItxSubjectContainer update(ItxSubjectContainer object) {
        ItxSubjectContainer updatedObject = new JSONDeserializer<ItxSubjectContainer>()
                .use(null, ItxSubjectContainer.class).deserializeInto(object.toJson(),
                        ItxSubjectContainer.findItxSubjectContainer(object.getId()));
        updatedObject.setModifiedDate(new Date());
        updatedObject.merge();
        return updatedObject;
    }

    public static long countItxSubjectContainers() {
        return entityManager().createQuery("SELECT COUNT(o) FROM ItxSubjectContainer o", Long.class).getSingleResult();
    }

    public static List<ItxSubjectContainer> findAllItxSubjectContainers() {
        return entityManager().createQuery("SELECT o FROM ItxSubjectContainer o", ItxSubjectContainer.class)
                .getResultList();
    }

    public static ItxSubjectContainer findItxSubjectContainer(Long id) {
        if (id == null)
            return null;
        return entityManager().find(ItxSubjectContainer.class, id);
    }

    public static List<ItxSubjectContainer> findItxSubjectContainerByExperimentID(Long experimentId) {
        if (experimentId == null)
            return null;
        String selectSQL = "SELECT oo FROM ItxSubjectContainer oo WHERE oo.id in (select o.id from ItxSubjectContainer o where o.subject.treatmentGroup.analysisGroup.experiment.id = :experimentId)";
        EntityManager em = ItxSubjectContainer.entityManager();
        TypedQuery<ItxSubjectContainer> q = em.createQuery(selectSQL, ItxSubjectContainer.class);
        q.setParameter("experimentId", experimentId);
        return q.getResultList();
    }

    public static List<ItxSubjectContainer> findItxSubjectContainerEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM ItxSubjectContainer o", ItxSubjectContainer.class)
                .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public ItxSubjectContainer merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        ItxSubjectContainer merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    @Transactional
    public static int deleteByExperimentID(Long experimentId) {
        if (experimentId == null)
            return 0;
        EntityManager em = ItxSubjectContainer.entityManager();
        String deleteSQL = "DELETE FROM ItxSubjectContainer oo WHERE id in (select o.id from ItxSubjectContainer o where o.subject.treatmentGroup.analysisGroup.experiment.id = :experimentId)";
        Query q = em.createQuery(deleteSQL);
        q.setParameter("experimentId", experimentId);
        int numberOfDeletedEntities = q.executeUpdate();
        return numberOfDeletedEntities;
    }

    public String toJson() {
        return new JSONSerializer().exclude("*.class")
                .transform(new ExcludeNulls(), void.class)
                .serialize(this);
    }

    public static ItxSubjectContainer fromJsonToItxSubjectContainer(String json) {
        return new JSONDeserializer<ItxSubjectContainer>().use(null, ItxSubjectContainer.class)
                .use(BigDecimal.class, new CustomBigDecimalFactory())
                .deserialize(json);
    }

    public static String toJsonArray(Collection<ItxSubjectContainer> collection) {
        return new JSONSerializer().exclude("*.class")
                .transform(new ExcludeNulls(), void.class)
                .serialize(collection);
    }

    public static Collection<ItxSubjectContainer> fromJsonArrayToItxSubjectContainers(String json) {
        return new JSONDeserializer<List<ItxSubjectContainer>>().use(null, ArrayList.class)
                .use("values", ItxSubjectContainer.class)
                .use(BigDecimal.class, new CustomBigDecimalFactory())
                .deserialize(json);
    }

    public static Collection<ItxSubjectContainer> fromJsonArrayToItxSubjectContainers(Reader json) {
        return new JSONDeserializer<List<ItxSubjectContainer>>().use(null, ArrayList.class)
                .use("values", ItxSubjectContainer.class)
                .use(BigDecimal.class, new CustomBigDecimalFactory())
                .deserialize(json);
    }

    public static ItxSubjectContainer updateNoStates(ItxSubjectContainer itxSubjectContainer) {
        ItxSubjectContainer updatedItxSubjectContainer = ItxSubjectContainer
                .findItxSubjectContainer(itxSubjectContainer.getId());
        updatedItxSubjectContainer.setRecordedBy(itxSubjectContainer.getRecordedBy());
        updatedItxSubjectContainer.setRecordedDate(itxSubjectContainer.getRecordedDate());
        updatedItxSubjectContainer.setIgnored(itxSubjectContainer.isIgnored());
        updatedItxSubjectContainer.setDeleted(itxSubjectContainer.isDeleted());
        updatedItxSubjectContainer.setLsTransaction(itxSubjectContainer.getLsTransaction());
        updatedItxSubjectContainer.setModifiedBy(itxSubjectContainer.getModifiedBy());
        updatedItxSubjectContainer.setCodeName(itxSubjectContainer.getCodeName());
        updatedItxSubjectContainer.setLsType(itxSubjectContainer.getLsType());
        updatedItxSubjectContainer.setLsKind(itxSubjectContainer.getLsKind());
        updatedItxSubjectContainer.setLsTypeAndKind(itxSubjectContainer.getLsTypeAndKind());
        updatedItxSubjectContainer.subject = Subject.findSubject(itxSubjectContainer.getSubject().getId());
        updatedItxSubjectContainer.container = Container.findContainer(itxSubjectContainer.getContainer().getId());
        updatedItxSubjectContainer.setModifiedDate(new Date());
        updatedItxSubjectContainer.merge();

        logger.debug("------------ Just updated the itxSubjectContainer: ");
        if (logger.isDebugEnabled())
            logger.debug(updatedItxSubjectContainer.toJson());

        return updatedItxSubjectContainer;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Container getContainer() {
        return this.container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public Set<ItxSubjectContainerState> getLsStates() {
        return this.lsStates;
    }

    public void setLsStates(Set<ItxSubjectContainerState> lsStates) {
        this.lsStates = lsStates;
    }

    public static Long countFindItxSubjectContainersByCodeNameEquals(String codeName) {
        if (codeName == null || codeName.length() == 0)
            throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = ItxSubjectContainer.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ItxSubjectContainer AS o WHERE o.codeName = :codeName",
                Long.class);
        q.setParameter("codeName", codeName);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindItxSubjectContainersByContainer(Container container) {
        if (container == null)
            throw new IllegalArgumentException("The container argument is required");
        EntityManager em = ItxSubjectContainer.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ItxSubjectContainer AS o WHERE o.container = :container",
                Long.class);
        q.setParameter("container", container);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindItxSubjectContainersByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ItxSubjectContainer.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM ItxSubjectContainer AS o WHERE o.lsTransaction = :lsTransaction", Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindItxSubjectContainersBySubject(Subject subject) {
        if (subject == null)
            throw new IllegalArgumentException("The subject argument is required");
        EntityManager em = ItxSubjectContainer.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM ItxSubjectContainer AS o WHERE o.subject = :subject",
                Long.class);
        q.setParameter("subject", subject);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<ItxSubjectContainer> findItxSubjectContainersByCodeNameEquals(String codeName) {
        if (codeName == null || codeName.length() == 0)
            throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = ItxSubjectContainer.entityManager();
        TypedQuery<ItxSubjectContainer> q = em.createQuery(
                "SELECT o FROM ItxSubjectContainer AS o WHERE o.codeName = :codeName", ItxSubjectContainer.class);
        q.setParameter("codeName", codeName);
        return q;
    }

    public static TypedQuery<ItxSubjectContainer> findItxSubjectContainersByCodeNameEquals(String codeName,
            String sortFieldName, String sortOrder) {
        if (codeName == null || codeName.length() == 0)
            throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = ItxSubjectContainer.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM ItxSubjectContainer AS o WHERE o.codeName = :codeName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ItxSubjectContainer> q = em.createQuery(queryBuilder.toString(), ItxSubjectContainer.class);
        q.setParameter("codeName", codeName);
        return q;
    }

    public static TypedQuery<ItxSubjectContainer> findItxSubjectContainersByContainer(Container container) {
        if (container == null)
            throw new IllegalArgumentException("The container argument is required");
        EntityManager em = ItxSubjectContainer.entityManager();
        TypedQuery<ItxSubjectContainer> q = em.createQuery(
                "SELECT o FROM ItxSubjectContainer AS o WHERE o.container = :container", ItxSubjectContainer.class);
        q.setParameter("container", container);
        return q;
    }

    public static TypedQuery<ItxSubjectContainer> findItxSubjectContainersByContainer(Container container,
            String sortFieldName, String sortOrder) {
        if (container == null)
            throw new IllegalArgumentException("The container argument is required");
        EntityManager em = ItxSubjectContainer.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM ItxSubjectContainer AS o WHERE o.container = :container");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ItxSubjectContainer> q = em.createQuery(queryBuilder.toString(), ItxSubjectContainer.class);
        q.setParameter("container", container);
        return q;
    }

    public static TypedQuery<ItxSubjectContainer> findItxSubjectContainersByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ItxSubjectContainer.entityManager();
        TypedQuery<ItxSubjectContainer> q = em.createQuery(
                "SELECT o FROM ItxSubjectContainer AS o WHERE o.lsTransaction = :lsTransaction",
                ItxSubjectContainer.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

    public static TypedQuery<ItxSubjectContainer> findItxSubjectContainersByLsTransactionEquals(Long lsTransaction,
            String sortFieldName, String sortOrder) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = ItxSubjectContainer.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM ItxSubjectContainer AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ItxSubjectContainer> q = em.createQuery(queryBuilder.toString(), ItxSubjectContainer.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

    public static TypedQuery<ItxSubjectContainer> findItxSubjectContainersBySubject(Subject subject) {
        if (subject == null)
            throw new IllegalArgumentException("The subject argument is required");
        EntityManager em = ItxSubjectContainer.entityManager();
        TypedQuery<ItxSubjectContainer> q = em.createQuery(
                "SELECT o FROM ItxSubjectContainer AS o WHERE o.subject = :subject", ItxSubjectContainer.class);
        q.setParameter("subject", subject);
        return q;
    }

    public static TypedQuery<ItxSubjectContainer> findItxSubjectContainersBySubject(Subject subject,
            String sortFieldName, String sortOrder) {
        if (subject == null)
            throw new IllegalArgumentException("The subject argument is required");
        EntityManager em = ItxSubjectContainer.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM ItxSubjectContainer AS o WHERE o.subject = :subject");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<ItxSubjectContainer> q = em.createQuery(queryBuilder.toString(), ItxSubjectContainer.class);
        q.setParameter("subject", subject);
        return q;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "subject",
            "container", "lsStates");

    public static List<ItxSubjectContainer> findAllItxSubjectContainers(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxSubjectContainer o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxSubjectContainer.class).getResultList();
    }

    public static List<ItxSubjectContainer> findItxSubjectContainerEntries(int firstResult, int maxResults,
            String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM ItxSubjectContainer o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, ItxSubjectContainer.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }
}
