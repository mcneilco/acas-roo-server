package com.labsynch.labseer.domain;

import java.io.Reader;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;

import com.labsynch.labseer.utils.ByteArrayTransformer;
import com.labsynch.labseer.utils.ExcludeNulls;
import com.labsynch.labseer.utils.SimpleUtil;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@Entity
@Configurable
public class LsThing extends AbstractThing {

    private static final Logger logger = LoggerFactory.getLogger(LsThing.class);

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "lsThing")
    private Set<LsThingState> lsStates = new HashSet<LsThingState>();

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "lsThing")
    private Set<LsThingLabel> lsLabels = new HashSet<LsThingLabel>();

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "LSTHING_TAG", joinColumns = { @JoinColumn(name = "lsthing_id") }, inverseJoinColumns = {
            @JoinColumn(name = "tag_id") })
    private Set<LsTag> lsTags = new HashSet<LsTag>();

    @OneToMany(cascade = {}, mappedBy = "secondLsThing", fetch = FetchType.LAZY)
    private Set<ItxLsThingLsThing> firstLsThings = new HashSet<ItxLsThingLsThing>();

    @OneToMany(cascade = {}, mappedBy = "firstLsThing", fetch = FetchType.LAZY)
    private Set<ItxLsThingLsThing> secondLsThings = new HashSet<ItxLsThingLsThing>();

    public LsThing(com.labsynch.labseer.domain.LsThing lsThing) {
        this.setRecordedBy(lsThing.getRecordedBy());
        this.setRecordedDate(lsThing.getRecordedDate());
        this.setLsTransaction(lsThing.getLsTransaction());
        this.setModifiedBy(lsThing.getModifiedBy());
        this.setModifiedDate(lsThing.getModifiedDate());
        this.setIgnored(lsThing.isIgnored());
        this.setCodeName(lsThing.getCodeName());
        this.setLsType(lsThing.getLsType());
        this.setLsKind(lsThing.getLsKind());
        this.setLsTypeAndKind(lsThing.getLsTypeAndKind());
        if (lsThing.getLsTags() != null) {
            for (LsTag lsTag : lsThing.getLsTags()) {
                List<LsTag> queryTags = LsTag.findLsTagsByTagTextEquals(lsTag.getTagText()).getResultList();
                if (queryTags.size() < 1) {
                    LsTag newLsTag = new LsTag(lsTag);
                    newLsTag.persist();
                    this.getLsTags().add(newLsTag);
                } else {
                    this.getLsTags().add(queryTags.get(0));
                }
            }
        }
    }

    public LsThing() {

    }

    @Transactional
    public String toJson() {
        return new JSONSerializer().exclude("*.class").include("lsTags", "lsLabels", "lsStates.lsValues")
                .transform(new ExcludeNulls(), void.class).serialize(this);
    }

    public static com.labsynch.labseer.domain.LsThing fromJsonToProtocol(String json) {
        return new JSONDeserializer<LsThing>().use(null, LsThing.class).deserialize(json);
    }

    @Transactional
    public static String toJsonArray(Collection<com.labsynch.labseer.domain.LsThing> collection) {
        return new JSONSerializer().exclude("*.class").include("lsTags", "lsLabels", "lsStates.lsValues")
                .transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    public static Collection<com.labsynch.labseer.domain.LsThing> fromJsonArrayToProtocols(String json) {
        return new JSONDeserializer<List<LsThing>>().use(null, ArrayList.class).use("values", LsThing.class)
                .deserialize(json);
    }

    public static Collection<com.labsynch.labseer.domain.LsThing> fromJsonArrayToProtocols(Reader json) {
        return new JSONDeserializer<List<LsThing>>().use(null, ArrayList.class).use("values", LsThing.class)
                .deserialize(json);
    }

    @Transactional 
    public String toJsonNoNestedAttributes() {
        return new JSONSerializer()
            .exclude("*.class", "lsStates", "lsValues", "lsTags", "lsLabels", "firstLsThings", "secondLsThings")
            .transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toJsonWithNestedStubs() {
        return new JSONSerializer()
                .exclude("*.class", "lsStates.lsValues.lsState", "lsStates.lsThing", "lsLabels.lsThing")
                .include("lsTags", "lsLabels", "lsStates.lsValues", "firstLsThings.firstLsThing.lsLabels",
                        "secondLsThings.secondLsThing.lsLabels")
                .transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toJsonWithNestedStubsWithBlobValues() {
        return new JSONSerializer()
                .exclude("*.class", "lsStates.lsValues.lsState", "lsStates.lsThing", "lsLabels.lsThing")
                .include("lsTags", "lsLabels", "lsStates.lsValues", "firstLsThings.firstLsThing.lsLabels",
                        "secondLsThings.secondLsThing.lsLabels")
                .transform(new ExcludeNulls(), void.class)
                .transform(new ByteArrayTransformer(), "lsStates.lsValues.blobValue").serialize(this);
    }

    @Transactional
    public String toJsonWithNestedFull() {
        return new JSONSerializer()
                .exclude("*.class", "lsStates.lsValues.lsState", "lsStates.lsThing", "lsLabels.lsThing")
                .include("lsTags", "lsLabels", "lsStates.lsValues", "firstLsThings.firstLsThing.lsStates.lsValues",
                        "firstLsThings.firstLsThing.lsLabels", "secondLsThings.secondLsThing.lsStates.lsValues",
                        "secondLsThings.secondLsThing.lsLabels", "firstLsThings.lsStates.lsValues",
                        "firstLsThings.lsLabels", "secondLsThings.lsStates.lsValues", "secondLsThings.lsLabels")
                .transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toPrettyJson() {
        return new JSONSerializer()
                .exclude("*.class", "lsStates.lsValues.lsState", "lsStates.lsThing", "lsLabels.lsThing")
                .include("lsTags", "lsLabels", "lsStates.lsValues").prettyPrint(true)
                .transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public String toJsonStub() {
        return new JSONSerializer().exclude("*.class", "lsStates").include("lsTags", "lsLabels").prettyPrint(false)
                .transform(new ExcludeNulls(), void.class).serialize(this);
    }

    @Transactional
    public static String toJsonArrayWithNestedStubs(Collection<com.labsynch.labseer.domain.LsThing> collection) {
        return new JSONSerializer()
                .exclude("*.class", "lsStates.lsValues.lsState", "lsStates.lsThing", "lsLabels.lsThing")
                .include("lsTags", "lsLabels", "lsStates.lsValues", "firstLsThings.firstLsThing.lsLabels",
                        "secondLsThings.secondLsThing.lsLabels")
                .transform(new ExcludeNulls(), void.class).serialize(collection);

    }

    @Transactional
    public static String toJsonArrayWithNestedFull(Collection<com.labsynch.labseer.domain.LsThing> collection) {
        return new JSONSerializer()
                .exclude("*.class", "lsStates.lsValues.lsState", "lsStates.lsThing", "lsLabels.lsThing")
                .include("lsTags", "lsLabels", "lsStates.lsValues", "firstLsThings.firstLsThing.lsStates.lsValues",
                        "secondLsThings.secondLsThing.lsStates.lsValues", "firstLsThings.firstLsThing.lsLabels",
                        "secondLsThings.secondLsThing.lsLabels", "firstLsThings.lsStates.lsValues",
                        "secondLsThings.lsStates.lsValues", "firstLsThings.lsLabels", "secondLsThings.lsLabels")
                .transform(new ExcludeNulls(), void.class).serialize(collection);

    }

    @Transactional
    public static String toJsonArrayWithNestedFirstLsThings(
            Collection<com.labsynch.labseer.domain.LsThing> collection) {
        return new JSONSerializer().exclude("*.class")
                .include("lsTags", "lsLabels", "lsStates.lsValues", "firstLsThings.firstLsThing.lsStates.lsValues",
                        "firstLsThings.firstLsThing.lsLabels", "firstLsThings.lsStates.lsValues",
                        "firstLsThings.lsLabels")
                .transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArrayWithNestedSecondLsThings(
            Collection<com.labsynch.labseer.domain.LsThing> collection) {
        return new JSONSerializer().exclude("*.class")
                .include("lsTags", "lsLabels", "lsStates.lsValues", "secondLsThings.secondLsThing.lsStates.lsValues",
                        "secondLsThings.secondLsThing.lsLabels", "secondLsThings.lsStates.lsValues",
                        "secondLsThings.lsLabels")
                .transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArrayPretty(Collection<com.labsynch.labseer.domain.LsThing> collection) {
        return new JSONSerializer()
                .exclude("*.class", "lsStates.lsValues.lsState", "lsStates.lsThing", "lsLabels.lsThing")
                .include("lsTags", "lsLabels", "lsStates.lsValues").prettyPrint(true)
                .transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public static String toJsonArrayStub(Collection<com.labsynch.labseer.domain.LsThing> collection) {
        return new JSONSerializer().exclude("*.class", "lsStates").include("lsTags", "lsLabels").prettyPrint(false)
                .transform(new ExcludeNulls(), void.class).serialize(collection);
    }

    @Transactional
    public String toPrettyJsonStub() {
        return new JSONSerializer()
                .exclude("*.class", "lsStates.lsValues.lsState", "lsStates.lsThing", "lsLabels.lsThing")
                .include("lsTags", "lsLabels", "lsStates.lsValues")
                .prettyPrint(true).transform(new ExcludeNulls(), void.class).serialize(this);
    }

    public static TypedQuery<LsThing> findLsThing(String thingType, String thingKind) {
        if (thingType == null || thingType.length() == 0)
            throw new IllegalArgumentException("The thingType argument is required");
        if (thingKind == null || thingKind.length() == 0)
            throw new IllegalArgumentException("The thingKind argument is required");

        boolean ignored = true;

        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT o FROM LsThing o " +
                "WHERE o.ignored IS NOT :ignored " +
                "AND o.lsType = :thingType " +
                "AND o.lsKind = :thingKind ";

        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("ignored", ignored);

        return q;
    }

    // public static TypedQuery<LsThing> findLsThing(String thingType, String
    // thingKind, String labelText) {
    // if (thingType == null || thingType.length() == 0) throw new
    // IllegalArgumentException("The thingType argument is required");
    // if (thingKind == null || thingKind.length() == 0) throw new
    // IllegalArgumentException("The thingKind argument is required");
    // if (labelText == null || labelText.length() == 0) throw new
    // IllegalArgumentException("The labelText argument is required");
    //
    // boolean ignored = true;
    //
    // EntityManager em = LsThing.entityManager();
    // String query = "SELECT o FROM LsThing o " +
    // "JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.labelText =
    // :labelText " +
    // "WHERE o.ignored IS NOT :ignored " +
    // "AND o.lsType = :thingType " +
    // "AND o.lsKind = :thingKind ";
    //
    // TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
    // q.setParameter("thingType", thingType);
    // q.setParameter("thingKind", thingKind);
    // q.setParameter("labelText", labelText);
    // q.setParameter("ignored", ignored);
    //
    // return q;
    // }

    public static TypedQuery<LsThing> findLsThingByLabelText(String labelText) {
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");

        boolean ignored = true;

        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT o FROM LsThing o " +
                "JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.labelText = :labelText " +
                "WHERE o.ignored IS NOT :ignored ";

        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);

        return q;
    }

    public static TypedQuery<LsThing> findLsThingByLabelTextAndLsKind(String labelText, String lsKind) {
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");

        boolean ignored = true;

        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT o FROM LsThing o " +
                "JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.labelText = :labelText " +
                "WHERE o.ignored IS NOT :ignored " +
                "AND o.lsKind = :lsKind ";

        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("labelText", labelText);
        q.setParameter("lsKind", lsKind);
        q.setParameter("ignored", ignored);

        return q;
    }

    public static TypedQuery<LsThing> findLsThingByLabelTextList(List<String> labelTextList) {
        if (labelTextList == null || labelTextList.size() == 0)
            throw new IllegalArgumentException("The labelText argument is required");

        boolean ignored = true;

        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT o FROM LsThing o " +
                "JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.labelText in (:labelTextList) " +
                "WHERE o.ignored IS NOT :ignored ";

        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("labelTextList", labelTextList);
        q.setParameter("ignored", ignored);

        return q;
    }

    public static TypedQuery<LsThing> findLsThingByLabelText(String thingType, String thingKind, String labelText) {
        if (thingType == null || thingType.length() == 0)
            throw new IllegalArgumentException("The thingType argument is required");
        if (thingKind == null || thingKind.length() == 0)
            throw new IllegalArgumentException("The thingKind argument is required");
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");

        boolean ignored = true;
        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT o FROM LsThing o " +
                "JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.labelText = :labelText " +
                "WHERE o.ignored IS NOT :ignored " +
                "AND o.lsType = :thingType " +
                "AND o.lsKind = :thingKind ";

        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);

        return q;
    }

    public static TypedQuery<LsThing> findLsThingByLabelText(String thingType, String thingKind, String labelType,
            String labelKind, String labelText) {
        if (thingType == null || thingType.length() == 0)
            throw new IllegalArgumentException("The thingType argument is required");
        if (thingKind == null || thingKind.length() == 0)
            throw new IllegalArgumentException("The thingKind argument is required");
        if (labelType == null || labelType.length() == 0)
            throw new IllegalArgumentException("The labelType argument is required");
        if (labelKind == null || labelKind.length() == 0)
            throw new IllegalArgumentException("The labelKind argument is required");
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");

        boolean ignored = true;

        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT o FROM LsThing o " +
                "JOIN o.lsLabels ll " +
                "WHERE o.ignored IS NOT :ignored " +
                "AND o.lsType = :thingType " +
                "AND o.lsKind = :thingKind " +
                "AND ll.ignored IS NOT :ignored AND ll.lsType = :labelType AND ll.lsKind = :labelKind AND ll.labelText = :labelText";

        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("labelType", labelType);
        q.setParameter("labelKind", labelKind);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);

        return q;
    }

    public static TypedQuery<LsThing> findAllLsThingByLabelText(String thingType, String thingKind, String labelType,
            String labelKind, String labelText) {
        if (thingType == null || thingType.length() == 0)
            throw new IllegalArgumentException("The thingType argument is required");
        if (thingKind == null || thingKind.length() == 0)
            throw new IllegalArgumentException("The thingKind argument is required");
        if (labelType == null || labelType.length() == 0)
            throw new IllegalArgumentException("The labelType argument is required");
        if (labelKind == null || labelKind.length() == 0)
            throw new IllegalArgumentException("The labelKind argument is required");
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");

        boolean ignored = true;

        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT o FROM LsThing o " +
                "JOIN o.lsLabels ll " +
                "WHERE o.lsType = :thingType " +
                "AND o.lsKind = :thingKind " +
                "AND ll.ignored IS NOT :ignored AND ll.lsType = :labelType AND ll.lsKind = :labelKind AND ll.labelText = :labelText";

        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("labelType", labelType);
        q.setParameter("labelKind", labelKind);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);

        return q;
    }

    // public static TypedQuery<LsThing> findLsThingByLabelText(String thingType,
    // String thingKind, String labelType, String labelKind,
    // Object batchCode, int firstResult, int resultSetSize) {
    // // TODO Auto-generated method stub
    // return null;
    // }

    public static TypedQuery<LsThing> findLsThingByLabelTypeAndLabelText(
            String thingType, String thingKind, String labelType,
            String labelText) {
        if (thingType == null || thingType.length() == 0)
            throw new IllegalArgumentException("The thingType argument is required");
        if (thingKind == null || thingKind.length() == 0)
            throw new IllegalArgumentException("The thingKind argument is required");
        if (labelType == null || labelType.length() == 0)
            throw new IllegalArgumentException("The labelType argument is required");
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");

        boolean ignored = true;

        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT o FROM LsThing o " +
                "JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.lsType = :labelType AND ll.labelText = :labelText "
                +
                "WHERE o.ignored IS NOT :ignored " +
                "AND o.lsType = :thingType " +
                "AND o.lsKind = :thingKind ";

        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("labelType", labelType);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);

        return q;
    }

    public static TypedQuery<LsThing> findLsThingByLabelKindAndLabelText(
            String thingType, String thingKind, String labelKind,
            String labelText) {
        if (thingType == null || thingType.length() == 0)
            throw new IllegalArgumentException("The thingType argument is required");
        if (thingKind == null || thingKind.length() == 0)
            throw new IllegalArgumentException("The thingKind argument is required");
        if (labelKind == null || labelKind.length() == 0)
            throw new IllegalArgumentException("The labelKind argument is required");
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");

        boolean ignored = true;

        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT o FROM LsThing o " +
                "JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.lsKind = :labelKind AND ll.labelText = :labelText "
                +
                "WHERE o.ignored IS NOT :ignored " +
                "AND o.lsType = :thingType " +
                "AND o.lsKind = :thingKind ";

        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("labelKind", labelKind);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);

        return q;
    }

    public static TypedQuery<LsThing> findLsThingByLabelTypeAndKind(
            String thingType, String thingKind, String labelType,
            String labelKind) {
        if (thingType == null || thingType.length() == 0)
            throw new IllegalArgumentException("The thingType argument is required");
        if (thingKind == null || thingKind.length() == 0)
            throw new IllegalArgumentException("The thingKind argument is required");
        if (labelType == null || labelType.length() == 0)
            throw new IllegalArgumentException("The labelType argument is required");
        if (labelKind == null || labelKind.length() == 0)
            throw new IllegalArgumentException("The labelKind argument is required");

        boolean ignored = true;

        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT o FROM LsThing o " +
                "JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.lsType = :labelType AND ll.lsKind = :labelKind "
                +
                "WHERE o.ignored IS NOT :ignored " +
                "AND o.lsType = :thingType " +
                "AND o.lsKind = :thingKind ";

        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("labelType", labelType);
        q.setParameter("labelKind", labelKind);
        q.setParameter("ignored", ignored);

        return q;
    }

    public static TypedQuery<LsThing> findLsThingByLabelTypeAndKindAndLabelText(
            String thingType, String thingKind, String labelType,
            String labelKind, String labelText) {
        if (thingType == null || thingType.length() == 0)
            throw new IllegalArgumentException("The thingType argument is required");
        if (thingKind == null || thingKind.length() == 0)
            throw new IllegalArgumentException("The thingKind argument is required");
        if (labelType == null || labelType.length() == 0)
            throw new IllegalArgumentException("The labelType argument is required");
        if (labelKind == null || labelKind.length() == 0)
            throw new IllegalArgumentException("The labelKind argument is required");
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");

        boolean ignored = true;

        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT o FROM LsThing o " +
                "JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.lsType = :labelType AND ll.lsKind = :labelKind "
                +
                "WHERE o.ignored IS NOT :ignored " +
                "AND o.lsType = :thingType " +
                "AND o.lsKind = :thingKind " +
                "AND ll.labelText = :labelText ";

        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("labelType", labelType);
        q.setParameter("labelKind", labelKind);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);

        return q;
    }

    public static TypedQuery<LsThing> findAllLsThingByLabelTypeAndKindAndLabelText(
            String thingType, String thingKind, String labelType,
            String labelKind, String labelText) {
        if (thingType == null || thingType.length() == 0)
            throw new IllegalArgumentException("The thingType argument is required");
        if (thingKind == null || thingKind.length() == 0)
            throw new IllegalArgumentException("The thingKind argument is required");
        if (labelType == null || labelType.length() == 0)
            throw new IllegalArgumentException("The labelType argument is required");
        if (labelKind == null || labelKind.length() == 0)
            throw new IllegalArgumentException("The labelKind argument is required");
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");

        boolean ignored = true;

        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT o FROM LsThing o " +
                "JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.lsType = :labelType AND ll.lsKind = :labelKind "
                +
                "WHERE o.deleted IS NOT :deleted " +
                "AND o.lsType = :thingType " +
                "AND o.lsKind = :thingKind " +
                "AND ll.labelText = :labelText ";

        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("labelType", labelType);
        q.setParameter("labelKind", labelKind);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        q.setParameter("deleted", true);

        return q;
    }

    public static TypedQuery<LsThing> findLsThingByAllLabelTypeAndKindAndLabelText(
            String thingType, String thingKind, String labelType,
            String labelKind, String labelText) {
        if (thingType == null || thingType.length() == 0)
            throw new IllegalArgumentException("The thingType argument is required");
        if (thingKind == null || thingKind.length() == 0)
            throw new IllegalArgumentException("The thingKind argument is required");
        if (labelType == null || labelType.length() == 0)
            throw new IllegalArgumentException("The labelType argument is required");
        if (labelKind == null || labelKind.length() == 0)
            throw new IllegalArgumentException("The labelKind argument is required");
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");

        boolean ignored = true;

        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT o FROM LsThing o " +
                "JOIN o.lsLabels ll with ll.lsType = :labelType AND ll.lsKind = :labelKind " +
                "WHERE o.ignored IS NOT :ignored " +
                "AND o.lsType = :thingType " +
                "AND o.lsKind = :thingKind " +
                "AND ll.labelText = :labelText ";

        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("labelType", labelType);
        q.setParameter("labelKind", labelKind);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);

        return q;
    }

    public static TypedQuery<LsThing> findLsThingByLabelKind(String thingType,
            String thingKind, String labelKind) {
        if (thingType == null || thingType.length() == 0)
            throw new IllegalArgumentException("The thingType argument is required");
        if (thingKind == null || thingKind.length() == 0)
            throw new IllegalArgumentException("The thingKind argument is required");
        if (labelKind == null || labelKind.length() == 0)
            throw new IllegalArgumentException("The labelKind argument is required");

        boolean ignored = true;

        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT o FROM LsThing o " +
                "JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.lsKind = :labelKind " +
                "WHERE o.ignored IS NOT :ignored " +
                "AND o.lsType = :thingType " +
                "AND o.lsKind = :thingKind ";

        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("labelKind", labelKind);
        q.setParameter("ignored", ignored);

        return q;
    }

    public static TypedQuery<LsThing> findLsThingByLabelType(String thingType,
            String thingKind, String labelType) {
        if (thingType == null || thingType.length() == 0)
            throw new IllegalArgumentException("The thingType argument is required");
        if (thingKind == null || thingKind.length() == 0)
            throw new IllegalArgumentException("The thingKind argument is required");
        if (labelType == null || labelType.length() == 0)
            throw new IllegalArgumentException("The labelType argument is required");

        boolean ignored = true;

        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT o FROM LsThing o " +
                "JOIN o.lsLabels ll with ll.ignored IS NOT :ignored AND ll.lsType = :labelType " +
                "WHERE o.ignored IS NOT :ignored " +
                "AND o.lsType = :thingType " +
                "AND o.lsKind = :thingKind ";

        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("labelType", labelType);
        q.setParameter("ignored", ignored);

        return q;
    }

    public static TypedQuery<LsThing> findFirstLsThingsByItxTypeKindEqualsAndSecondLsThingEquals(String lsType,
            String lsKind, LsThing secondLsThing) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        if (secondLsThing == null)
            throw new IllegalArgumentException("The secondLsThing argument is required");

        boolean ignored = true;

        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT itx.firstLsThing FROM LsThing secondLsThing " +
                "JOIN secondLsThing.firstLsThings itx with itx.ignored IS NOT :ignored AND itx.lsType = :lsType AND itx.lsKind = :lsKind "
                +
                "WHERE secondLsThing.ignored IS NOT :ignored " +
                "AND secondLsThing = :secondLsThing ";

        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("secondLsThing", secondLsThing);
        q.setParameter("ignored", ignored);

        return q;
    }

    public static TypedQuery<LsThing> findSecondLsThingsByItxTypeKindEqualsAndFirstLsThingEquals(String lsType,
            String lsKind, LsThing firstLsThing) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        if (firstLsThing == null)
            throw new IllegalArgumentException("The firstLsThing argument is required");

        boolean ignored = true;

        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT itx.secondLsThing FROM LsThing firstLsThing " +
                "JOIN firstLsThing.secondLsThings itx with itx.ignored IS NOT :ignored AND itx.lsType = :lsType AND itx.lsKind = :lsKind "
                +
                "WHERE firstLsThing.ignored IS NOT :ignored " +
                "AND firstLsThing = :firstLsThing ";

        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("firstLsThing", firstLsThing);
        q.setParameter("ignored", ignored);

        return q;
    }

    public static TypedQuery<LsThing> findFirstLsThingsByItxTypeKindEqualsAndSecondLsThingEqualsAndOrderEquals(
            String lsType,
            String lsKind, LsThing secondLsThing, int order) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        if (secondLsThing == null)
            throw new IllegalArgumentException("The secondLsThing argument is required");

        boolean ignored = true;

        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT itx.firstLsThing FROM LsThing secondLsThing " +
                "JOIN secondLsThing.firstLsThings itx with itx.ignored IS NOT :ignored AND itx.lsType = :lsType AND itx.lsKind = :lsKind "
                +
                "JOIN itx.lsStates itxstate JOIN itxstate.lsValues itxvalue WITH itxvalue.lsKind = 'order' " +
                "WHERE secondLsThing.ignored IS NOT :ignored " +
                "AND itxvalue.numericValue = :order " +
                "AND secondLsThing = :secondLsThing ";

        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("order", order);
        q.setParameter("secondLsThing", secondLsThing);
        q.setParameter("ignored", ignored);

        return q;
    }

    public static TypedQuery<LsThing> findSecondLsThingsByItxTypeKindEqualsAndFirstLsThingEqualsAndOrderEquals(
            String lsType,
            String lsKind, LsThing firstLsThing, int order) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        if (firstLsThing == null)
            throw new IllegalArgumentException("The firstLsThing argument is required");

        boolean ignored = true;

        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT itx.secondLsThing FROM LsThing firstLsThing " +
                "JOIN firstLsThing.secondLsThings itx with itx.ignored IS NOT :ignored AND itx.lsType = :lsType AND itx.lsKind = :lsKind "
                +
                "JOIN itx.lsStates itxstate JOIN itxstate.lsValues itxvalue WITH itxvalue.lsKind = 'order' " +
                "WHERE firstLsThing.ignored IS NOT :ignored " +
                "AND itxvalue.numericValue = :order " +
                "AND firstLsThing = :firstLsThing ";

        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        q.setParameter("order", order);
        q.setParameter("firstLsThing", firstLsThing);
        q.setParameter("ignored", ignored);

        return q;
    }

    public static com.labsynch.labseer.domain.LsThing update(com.labsynch.labseer.domain.LsThing lsThing)
            throws StaleObjectStateException {
        LsThing updatedLsThing = LsThing.findLsThing(lsThing.getId());
        if (lsThing.getVersion() == null) {
            logger.warn(
                    "LsThing with id: " + lsThing.getId().toString() + " passed in with no version - skipping update");
            return updatedLsThing;
        }
        if (!lsThing.getVersion().equals(updatedLsThing.getVersion())) {
            logger.debug(
                    "incoming version: " + lsThing.getVersion() + " existing version: " + updatedLsThing.getVersion());
            throw new StaleObjectStateException("LsThing", lsThing.getId());
        }
        updatedLsThing.setRecordedBy(lsThing.getRecordedBy());
        updatedLsThing.setRecordedDate(lsThing.getRecordedDate());
        updatedLsThing.setLsTransaction(lsThing.getLsTransaction());
        updatedLsThing.setModifiedBy(lsThing.getModifiedBy());
        updatedLsThing.setModifiedDate(new Date());
        updatedLsThing.setCodeName(lsThing.getCodeName());
        updatedLsThing.setLsType(lsThing.getLsType());
        updatedLsThing.setLsKind(lsThing.getLsKind());
        updatedLsThing.setLsTypeAndKind(lsThing.getLsTypeAndKind());
        updatedLsThing.setIgnored(lsThing.isIgnored());
        if (updatedLsThing.getLsTags() != null) {
            updatedLsThing.getLsTags().clear();
        }
        if (lsThing.getLsTags() != null) {
            for (LsTag lsTag : lsThing.getLsTags()) {
                List<LsTag> queryTags = LsTag.findLsTagsByTagTextEquals(lsTag.getTagText()).getResultList();
                if (queryTags.size() < 1) {
                    LsTag newLsTag = new LsTag(lsTag);
                    newLsTag.persist();
                    updatedLsThing.getLsTags().add(newLsTag);
                } else {
                    updatedLsThing.getLsTags().add(queryTags.get(0));
                }
            }
        }
        updatedLsThing.merge();
        return updatedLsThing;
    }

    public static com.labsynch.labseer.domain.LsThing updateNoMerge(com.labsynch.labseer.domain.LsThing lsThing) {
        LsThing updatedLsThing = LsThing.findLsThing(lsThing.getId());
        updatedLsThing.setRecordedBy(lsThing.getRecordedBy());
        updatedLsThing.setRecordedDate(lsThing.getRecordedDate());
        updatedLsThing.setLsTransaction(lsThing.getLsTransaction());
        updatedLsThing.setModifiedBy(lsThing.getModifiedBy());
        updatedLsThing.setModifiedDate(new Date());
        updatedLsThing.setCodeName(lsThing.getCodeName());
        updatedLsThing.setLsType(lsThing.getLsType());
        updatedLsThing.setLsKind(lsThing.getLsKind());
        updatedLsThing.setLsTypeAndKind(lsThing.getLsTypeAndKind());
        updatedLsThing.setIgnored(lsThing.isIgnored());
        if (updatedLsThing.getLsTags() != null) {
            updatedLsThing.getLsTags().clear();
        }
        if (lsThing.getLsTags() != null) {
            for (LsTag lsTag : lsThing.getLsTags()) {
                List<LsTag> queryTags = LsTag.findLsTagsByTagTextEquals(lsTag.getTagText()).getResultList();
                if (queryTags.size() < 1) {
                    LsTag newLsTag = new LsTag(lsTag);
                    newLsTag.persist();
                    updatedLsThing.getLsTags().add(newLsTag);
                } else {
                    updatedLsThing.getLsTags().add(queryTags.get(0));
                }
            }
        }
        return updatedLsThing;
    }

    public static TypedQuery<LsThing> findFirstLsThingsByItxTypeEqualsAndSecondLsThingEquals(
            String lsType, LsThing secondLsThing) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (secondLsThing == null)
            throw new IllegalArgumentException("The secondLsThing argument is required");

        boolean ignored = true;

        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT itx.firstLsThing FROM LsThing secondLsThing " +
                "JOIN secondLsThing.firstLsThings itx with itx.ignored IS NOT :ignored AND itx.lsType = :lsType " +
                "WHERE secondLsThing.ignored IS NOT :ignored " +
                "AND secondLsThing = :secondLsThing ";

        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("lsType", lsType);
        q.setParameter("secondLsThing", secondLsThing);
        q.setParameter("ignored", ignored);

        return q;
    }

    public LsThingLabel pickBestCorpName() {
        Collection<LsThingLabel> labels = this.getLsLabels();
        if (labels.isEmpty())
            return null;
        Collection<LsThingLabel> corpNames = new HashSet<LsThingLabel>();
        for (LsThingLabel label : labels) {
            if (label.getLsType().equals("corpName"))
                corpNames.add(label);
        }
        Collection<LsThingLabel> preferredCorpNameLabels = new HashSet<LsThingLabel>();
        for (LsThingLabel label : corpNames) {
            if (label.isPreferred())
                preferredCorpNameLabels.add(label);
        }
        if (!preferredCorpNameLabels.isEmpty()) {
            LsThingLabel bestLabel = preferredCorpNameLabels.iterator().next();
            for (LsThingLabel preferredLabel : preferredCorpNameLabels) {
                if (preferredLabel.getRecordedDate().compareTo(bestLabel.getRecordedDate()) > 0)
                    bestLabel = preferredLabel;
            }
            return bestLabel;
        } else if (!corpNames.isEmpty()) {
            LsThingLabel bestLabel = corpNames.iterator().next();
            for (LsThingLabel preferredLabel : corpNames) {
                if (preferredLabel.getRecordedDate().compareTo(bestLabel.getRecordedDate()) > 0)
                    bestLabel = preferredLabel;
            }
            return bestLabel;
        } else {
            return null;
        }
    }

    public LsThingLabel pickBestName() {
        Collection<LsThingLabel> labels = this.getLsLabels();
        if (labels.isEmpty())
            return null;
        Collection<LsThingLabel> names = new HashSet<LsThingLabel>();
        for (LsThingLabel label : labels) {
            if (label.getLsType().equals("name"))
                names.add(label);
        }
        Collection<LsThingLabel> preferredNameLabels = new HashSet<LsThingLabel>();
        for (LsThingLabel label : names) {
            if (label.isPreferred())
                preferredNameLabels.add(label);
        }
        if (!preferredNameLabels.isEmpty()) {
            LsThingLabel bestLabel = preferredNameLabels.iterator().next();
            for (LsThingLabel preferredLabel : preferredNameLabels) {
                if (preferredLabel.getRecordedDate().compareTo(bestLabel.getRecordedDate()) > 0)
                    bestLabel = preferredLabel;
            }
            return bestLabel;
        } else if (!names.isEmpty()) {
            LsThingLabel bestLabel = names.iterator().next();
            for (LsThingLabel preferredLabel : names) {
                if (preferredLabel.getRecordedDate().compareTo(bestLabel.getRecordedDate()) > 0)
                    bestLabel = preferredLabel;
            }
            return bestLabel;
        } else {
            return null;
        }
    }

    public static Collection<LsThing> findLsThingsByCodeNamesIn(List<String> codeNames) {
        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT lsThing FROM LsThing lsThing " +
                "WHERE lsThing.ignored IS NOT :ignored AND ";
        TypedQuery<LsThing> q = (TypedQuery<LsThing>) SimpleUtil.addHqlInClause(em, query, "lsThing.codeName",
                codeNames);
        q.setParameter("ignored", true);
        return q.getResultList();
    }

    public static Collection<LsThing> findLsThingsByIdsIn(Collection<Long> thingIds) {
        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT lsThing FROM LsThing lsThing " +
                "WHERE lsThing.ignored IS NOT :ignored AND lsThing.id in (:ids)";
        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("ids", thingIds);
        q.setParameter("ignored", true);
        return q.getResultList();
    }

    public static TypedQuery<LsThing> findLsThingByPreferredLabelText(String thingType,
            String thingKind, String labelType, String labelKind,
            String labelText) {
        if (thingType == null || thingType.length() == 0)
            throw new IllegalArgumentException("The thingType argument is required");
        if (thingKind == null || thingKind.length() == 0)
            throw new IllegalArgumentException("The thingKind argument is required");
        if (labelType == null || labelType.length() == 0)
            throw new IllegalArgumentException("The labelType argument is required");
        if (labelKind == null || labelKind.length() == 0)
            throw new IllegalArgumentException("The labelKind argument is required");
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");

        boolean ignored = true;
        boolean preferred = true;

        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT o FROM LsThing o " +
                "JOIN o.lsLabels ll " +
                "WHERE o.ignored IS NOT :ignored " +
                "AND o.lsType = :thingType " +
                "AND o.lsKind = :thingKind " +
                "AND ll.ignored IS NOT :ignored AND ll.lsType = :labelType " +
                "AND ll.lsKind = :labelKind AND ll.labelText = :labelText " +
                "AND ll.preferred = :preferred";

        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("labelType", labelType);
        q.setParameter("labelKind", labelKind);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        q.setParameter("preferred", preferred);

        return q;

    }

    public static TypedQuery<LsThing> findAllLsThingByPreferredLabelText(String thingType,
            String thingKind, String labelType, String labelKind,
            String labelText) {
        if (thingType == null || thingType.length() == 0)
            throw new IllegalArgumentException("The thingType argument is required");
        if (thingKind == null || thingKind.length() == 0)
            throw new IllegalArgumentException("The thingKind argument is required");
        if (labelType == null || labelType.length() == 0)
            throw new IllegalArgumentException("The labelType argument is required");
        if (labelKind == null || labelKind.length() == 0)
            throw new IllegalArgumentException("The labelKind argument is required");
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");

        boolean ignored = true;
        boolean deleted = true;
        boolean preferred = true;

        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT o FROM LsThing o " +
                "JOIN o.lsLabels ll " +
                "WHERE o.deleted IS NOT :deleted " +
                "AND o.lsType = :thingType " +
                "AND o.lsKind = :thingKind " +
                "AND ll.ignored IS NOT :ignored AND ll.lsType = :labelType " +
                "AND ll.lsKind = :labelKind AND ll.labelText = :labelText " +
                "AND ll.preferred = :preferred";

        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("labelType", labelType);
        q.setParameter("labelKind", labelKind);
        q.setParameter("labelText", labelText);
        q.setParameter("ignored", ignored);
        q.setParameter("deleted", deleted);
        q.setParameter("preferred", preferred);

        return q;

    }

    public static TypedQuery<LsThing> findLsThingByAllLabelText(String thingType,
            String thingKind, String labelType, String labelKind,
            String labelText) {
        if (thingType == null || thingType.length() == 0)
            throw new IllegalArgumentException("The thingType argument is required");
        if (thingKind == null || thingKind.length() == 0)
            throw new IllegalArgumentException("The thingKind argument is required");
        if (labelType == null || labelType.length() == 0)
            throw new IllegalArgumentException("The labelType argument is required");
        if (labelKind == null || labelKind.length() == 0)
            throw new IllegalArgumentException("The labelKind argument is required");
        if (labelText == null || labelText.length() == 0)
            throw new IllegalArgumentException("The labelText argument is required");

        EntityManager em = LsThing.entityManager();
        String query = "SELECT DISTINCT o FROM LsThing o " +
                "JOIN o.lsLabels ll " +
                "WHERE o.deleted IS NOT :deleted " +
                "AND o.lsType = :thingType " +
                "AND o.lsKind = :thingKind " +
                "AND ll.lsType = :labelType " +
                "AND ll.lsKind = :labelKind AND ll.labelText = :labelText ";

        TypedQuery<LsThing> q = em.createQuery(query, LsThing.class);
        q.setParameter("thingType", thingType);
        q.setParameter("thingKind", thingKind);
        q.setParameter("labelType", labelType);
        q.setParameter("labelKind", labelKind);
        q.setParameter("labelText", labelText);
        q.setParameter("deleted", true);

        return q;

    }

    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("logger", "lsStates",
            "lsLabels", "lsTags", "firstLsThings", "secondLsThings");

    public static long countLsThings() {
        return entityManager().createQuery("SELECT COUNT(o) FROM LsThing o", Long.class).getSingleResult();
    }

    public static List<LsThing> findAllLsThings() {
        return entityManager().createQuery("SELECT o FROM LsThing o", LsThing.class).getResultList();
    }

    public static List<LsThing> findAllLsThings(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM LsThing o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsThing.class).getResultList();
    }

    public static LsThing findLsThing(Long id) {
        if (id == null)
            return null;
        return entityManager().find(LsThing.class, id);
    }

    public static List<LsThing> findLsThingEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM LsThing o", LsThing.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    public static List<LsThing> findLsThingEntries(int firstResult, int maxResults, String sortFieldName,
            String sortOrder) {
        String jpaQuery = "SELECT o FROM LsThing o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, LsThing.class).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
    }

    @Transactional
    public LsThing merge() {
        if (this.entityManager == null)
            this.entityManager = entityManager();
        LsThing merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

    public static LsThing fromJsonToLsThing(String json) {
        return new JSONDeserializer<LsThing>()
                .use(null, LsThing.class).deserialize(json);
    }

    public static Collection<LsThing> fromJsonArrayToLsThings(String json) {
        return new JSONDeserializer<List<LsThing>>()
                .use("values", LsThing.class).deserialize(json);
    }

    public String toString() {
        return new ReflectionToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .setExcludeFieldNames("lsTags", "lsStates", "lsLabels").toString();
    }

    public static Long countFindLsThingsByCodeNameEquals(String codeName) {
        if (codeName == null || codeName.length() == 0)
            throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = LsThing.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LsThing AS o WHERE o.codeName = :codeName", Long.class);
        q.setParameter("codeName", codeName);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLsThingsByCodeNameLike(String codeName) {
        if (codeName == null || codeName.length() == 0)
            throw new IllegalArgumentException("The codeName argument is required");
        codeName = codeName.replace('*', '%');
        if (codeName.charAt(0) != '%') {
            codeName = "%" + codeName;
        }
        if (codeName.charAt(codeName.length() - 1) != '%') {
            codeName = codeName + "%";
        }
        EntityManager em = LsThing.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LsThing AS o WHERE LOWER(o.codeName) LIKE LOWER(:codeName)",
                Long.class);
        q.setParameter("codeName", codeName);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLsThingsByLsKindEquals(String lsKind) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = LsThing.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LsThing AS o WHERE o.lsKind = :lsKind", Long.class);
        q.setParameter("lsKind", lsKind);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLsThingsByLsKindLike(String lsKind) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        lsKind = lsKind.replace('*', '%');
        if (lsKind.charAt(0) != '%') {
            lsKind = "%" + lsKind;
        }
        if (lsKind.charAt(lsKind.length() - 1) != '%') {
            lsKind = lsKind + "%";
        }
        EntityManager em = LsThing.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LsThing AS o WHERE LOWER(o.lsKind) LIKE LOWER(:lsKind)",
                Long.class);
        q.setParameter("lsKind", lsKind);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLsThingsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = LsThing.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LsThing AS o WHERE o.lsTransaction = :lsTransaction",
                Long.class);
        q.setParameter("lsTransaction", lsTransaction);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLsThingsByLsTypeAndKindEquals(String lsTypeAndKind) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0)
            throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = LsThing.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LsThing AS o WHERE o.lsTypeAndKind = :lsTypeAndKind",
                Long.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLsThingsByLsTypeEquals(String lsType) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = LsThing.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LsThing AS o WHERE o.lsType = :lsType", Long.class);
        q.setParameter("lsType", lsType);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLsThingsByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = LsThing.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM LsThing AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind", Long.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLsThingsByRecordedByLike(String recordedBy) {
        if (recordedBy == null || recordedBy.length() == 0)
            throw new IllegalArgumentException("The recordedBy argument is required");
        recordedBy = recordedBy.replace('*', '%');
        if (recordedBy.charAt(0) != '%') {
            recordedBy = "%" + recordedBy;
        }
        if (recordedBy.charAt(recordedBy.length() - 1) != '%') {
            recordedBy = recordedBy + "%";
        }
        EntityManager em = LsThing.entityManager();
        TypedQuery q = em.createQuery(
                "SELECT COUNT(o) FROM LsThing AS o WHERE LOWER(o.recordedBy) LIKE LOWER(:recordedBy)", Long.class);
        q.setParameter("recordedBy", recordedBy);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLsThingsByRecordedDateGreaterThan(Date recordedDate) {
        if (recordedDate == null)
            throw new IllegalArgumentException("The recordedDate argument is required");
        EntityManager em = LsThing.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LsThing AS o WHERE o.recordedDate > :recordedDate",
                Long.class);
        q.setParameter("recordedDate", recordedDate);
        return ((Long) q.getSingleResult());
    }

    public static Long countFindLsThingsByRecordedDateLessThan(Date recordedDate) {
        if (recordedDate == null)
            throw new IllegalArgumentException("The recordedDate argument is required");
        EntityManager em = LsThing.entityManager();
        TypedQuery q = em.createQuery("SELECT COUNT(o) FROM LsThing AS o WHERE o.recordedDate < :recordedDate",
                Long.class);
        q.setParameter("recordedDate", recordedDate);
        return ((Long) q.getSingleResult());
    }

    public static TypedQuery<LsThing> findLsThingsByCodeNameEquals(String codeName) {
        if (codeName == null || codeName.length() == 0)
            throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = LsThing.entityManager();
        TypedQuery<LsThing> q = em.createQuery("SELECT o FROM LsThing AS o WHERE o.codeName = :codeName",
                LsThing.class);
        q.setParameter("codeName", codeName);
        return q;
    }

    public static TypedQuery<LsThing> findLsThingsByCodeNameEquals(String codeName, String sortFieldName,
            String sortOrder) {
        if (codeName == null || codeName.length() == 0)
            throw new IllegalArgumentException("The codeName argument is required");
        EntityManager em = LsThing.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LsThing AS o WHERE o.codeName = :codeName");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsThing> q = em.createQuery(queryBuilder.toString(), LsThing.class);
        q.setParameter("codeName", codeName);
        return q;
    }

    public static TypedQuery<LsThing> findLsThingsByCodeNameLike(String codeName) {
        if (codeName == null || codeName.length() == 0)
            throw new IllegalArgumentException("The codeName argument is required");
        codeName = codeName.replace('*', '%');
        if (codeName.charAt(0) != '%') {
            codeName = "%" + codeName;
        }
        if (codeName.charAt(codeName.length() - 1) != '%') {
            codeName = codeName + "%";
        }
        EntityManager em = LsThing.entityManager();
        TypedQuery<LsThing> q = em
                .createQuery("SELECT o FROM LsThing AS o WHERE LOWER(o.codeName) LIKE LOWER(:codeName)", LsThing.class);
        q.setParameter("codeName", codeName);
        return q;
    }

    public static TypedQuery<LsThing> findLsThingsByCodeNameLike(String codeName, String sortFieldName,
            String sortOrder) {
        if (codeName == null || codeName.length() == 0)
            throw new IllegalArgumentException("The codeName argument is required");
        codeName = codeName.replace('*', '%');
        if (codeName.charAt(0) != '%') {
            codeName = "%" + codeName;
        }
        if (codeName.charAt(codeName.length() - 1) != '%') {
            codeName = codeName + "%";
        }
        EntityManager em = LsThing.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM LsThing AS o WHERE LOWER(o.codeName) LIKE LOWER(:codeName)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsThing> q = em.createQuery(queryBuilder.toString(), LsThing.class);
        q.setParameter("codeName", codeName);
        return q;
    }

    public static TypedQuery<LsThing> findLsThingsByLsKindEquals(String lsKind) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = LsThing.entityManager();
        TypedQuery<LsThing> q = em.createQuery("SELECT o FROM LsThing AS o WHERE o.lsKind = :lsKind", LsThing.class);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    public static TypedQuery<LsThing> findLsThingsByLsKindEquals(String lsKind, String sortFieldName,
            String sortOrder) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = LsThing.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LsThing AS o WHERE o.lsKind = :lsKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsThing> q = em.createQuery(queryBuilder.toString(), LsThing.class);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    public static TypedQuery<LsThing> findLsThingsByLsKindLike(String lsKind) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        lsKind = lsKind.replace('*', '%');
        if (lsKind.charAt(0) != '%') {
            lsKind = "%" + lsKind;
        }
        if (lsKind.charAt(lsKind.length() - 1) != '%') {
            lsKind = lsKind + "%";
        }
        EntityManager em = LsThing.entityManager();
        TypedQuery<LsThing> q = em.createQuery("SELECT o FROM LsThing AS o WHERE LOWER(o.lsKind) LIKE LOWER(:lsKind)",
                LsThing.class);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    public static TypedQuery<LsThing> findLsThingsByLsKindLike(String lsKind, String sortFieldName, String sortOrder) {
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        lsKind = lsKind.replace('*', '%');
        if (lsKind.charAt(0) != '%') {
            lsKind = "%" + lsKind;
        }
        if (lsKind.charAt(lsKind.length() - 1) != '%') {
            lsKind = lsKind + "%";
        }
        EntityManager em = LsThing.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM LsThing AS o WHERE LOWER(o.lsKind) LIKE LOWER(:lsKind)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsThing> q = em.createQuery(queryBuilder.toString(), LsThing.class);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    public static TypedQuery<LsThing> findLsThingsByLsTransactionEquals(Long lsTransaction) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = LsThing.entityManager();
        TypedQuery<LsThing> q = em.createQuery("SELECT o FROM LsThing AS o WHERE o.lsTransaction = :lsTransaction",
                LsThing.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

    public static TypedQuery<LsThing> findLsThingsByLsTransactionEquals(Long lsTransaction, String sortFieldName,
            String sortOrder) {
        if (lsTransaction == null)
            throw new IllegalArgumentException("The lsTransaction argument is required");
        EntityManager em = LsThing.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM LsThing AS o WHERE o.lsTransaction = :lsTransaction");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsThing> q = em.createQuery(queryBuilder.toString(), LsThing.class);
        q.setParameter("lsTransaction", lsTransaction);
        return q;
    }

    public static TypedQuery<LsThing> findLsThingsByLsTypeAndKindEquals(String lsTypeAndKind) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0)
            throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = LsThing.entityManager();
        TypedQuery<LsThing> q = em.createQuery("SELECT o FROM LsThing AS o WHERE o.lsTypeAndKind = :lsTypeAndKind",
                LsThing.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return q;
    }

    public static TypedQuery<LsThing> findLsThingsByLsTypeAndKindEquals(String lsTypeAndKind, String sortFieldName,
            String sortOrder) {
        if (lsTypeAndKind == null || lsTypeAndKind.length() == 0)
            throw new IllegalArgumentException("The lsTypeAndKind argument is required");
        EntityManager em = LsThing.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM LsThing AS o WHERE o.lsTypeAndKind = :lsTypeAndKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsThing> q = em.createQuery(queryBuilder.toString(), LsThing.class);
        q.setParameter("lsTypeAndKind", lsTypeAndKind);
        return q;
    }

    public static TypedQuery<LsThing> findLsThingsByLsTypeEquals(String lsType) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = LsThing.entityManager();
        TypedQuery<LsThing> q = em.createQuery("SELECT o FROM LsThing AS o WHERE o.lsType = :lsType", LsThing.class);
        q.setParameter("lsType", lsType);
        return q;
    }

    public static TypedQuery<LsThing> findLsThingsByLsTypeEquals(String lsType, String sortFieldName,
            String sortOrder) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        EntityManager em = LsThing.entityManager();
        StringBuilder queryBuilder = new StringBuilder("SELECT o FROM LsThing AS o WHERE o.lsType = :lsType");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsThing> q = em.createQuery(queryBuilder.toString(), LsThing.class);
        q.setParameter("lsType", lsType);
        return q;
    }

    public static TypedQuery<LsThing> findLsThingsByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = LsThing.entityManager();
        TypedQuery<LsThing> q = em.createQuery(
                "SELECT o FROM LsThing AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind", LsThing.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    public static TypedQuery<LsThing> findLsThingsByLsTypeEqualsAndLsKindEquals(String lsType, String lsKind,
            String sortFieldName, String sortOrder) {
        if (lsType == null || lsType.length() == 0)
            throw new IllegalArgumentException("The lsType argument is required");
        if (lsKind == null || lsKind.length() == 0)
            throw new IllegalArgumentException("The lsKind argument is required");
        EntityManager em = LsThing.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM LsThing AS o WHERE o.lsType = :lsType  AND o.lsKind = :lsKind");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsThing> q = em.createQuery(queryBuilder.toString(), LsThing.class);
        q.setParameter("lsType", lsType);
        q.setParameter("lsKind", lsKind);
        return q;
    }

    public static TypedQuery<LsThing> findLsThingsByRecordedByLike(String recordedBy) {
        if (recordedBy == null || recordedBy.length() == 0)
            throw new IllegalArgumentException("The recordedBy argument is required");
        recordedBy = recordedBy.replace('*', '%');
        if (recordedBy.charAt(0) != '%') {
            recordedBy = "%" + recordedBy;
        }
        if (recordedBy.charAt(recordedBy.length() - 1) != '%') {
            recordedBy = recordedBy + "%";
        }
        EntityManager em = LsThing.entityManager();
        TypedQuery<LsThing> q = em.createQuery(
                "SELECT o FROM LsThing AS o WHERE LOWER(o.recordedBy) LIKE LOWER(:recordedBy)", LsThing.class);
        q.setParameter("recordedBy", recordedBy);
        return q;
    }

    public static TypedQuery<LsThing> findLsThingsByRecordedByLike(String recordedBy, String sortFieldName,
            String sortOrder) {
        if (recordedBy == null || recordedBy.length() == 0)
            throw new IllegalArgumentException("The recordedBy argument is required");
        recordedBy = recordedBy.replace('*', '%');
        if (recordedBy.charAt(0) != '%') {
            recordedBy = "%" + recordedBy;
        }
        if (recordedBy.charAt(recordedBy.length() - 1) != '%') {
            recordedBy = recordedBy + "%";
        }
        EntityManager em = LsThing.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM LsThing AS o WHERE LOWER(o.recordedBy) LIKE LOWER(:recordedBy)");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsThing> q = em.createQuery(queryBuilder.toString(), LsThing.class);
        q.setParameter("recordedBy", recordedBy);
        return q;
    }

    public static TypedQuery<LsThing> findLsThingsByRecordedDateGreaterThan(Date recordedDate) {
        if (recordedDate == null)
            throw new IllegalArgumentException("The recordedDate argument is required");
        EntityManager em = LsThing.entityManager();
        TypedQuery<LsThing> q = em.createQuery("SELECT o FROM LsThing AS o WHERE o.recordedDate > :recordedDate",
                LsThing.class);
        q.setParameter("recordedDate", recordedDate);
        return q;
    }

    public static TypedQuery<LsThing> findLsThingsByRecordedDateGreaterThan(Date recordedDate, String sortFieldName,
            String sortOrder) {
        if (recordedDate == null)
            throw new IllegalArgumentException("The recordedDate argument is required");
        EntityManager em = LsThing.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM LsThing AS o WHERE o.recordedDate > :recordedDate");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsThing> q = em.createQuery(queryBuilder.toString(), LsThing.class);
        q.setParameter("recordedDate", recordedDate);
        return q;
    }

    public static TypedQuery<LsThing> findLsThingsByRecordedDateLessThan(Date recordedDate) {
        if (recordedDate == null)
            throw new IllegalArgumentException("The recordedDate argument is required");
        EntityManager em = LsThing.entityManager();
        TypedQuery<LsThing> q = em.createQuery("SELECT o FROM LsThing AS o WHERE o.recordedDate < :recordedDate",
                LsThing.class);
        q.setParameter("recordedDate", recordedDate);
        return q;
    }

    public static TypedQuery<LsThing> findLsThingsByRecordedDateLessThan(Date recordedDate, String sortFieldName,
            String sortOrder) {
        if (recordedDate == null)
            throw new IllegalArgumentException("The recordedDate argument is required");
        EntityManager em = LsThing.entityManager();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o FROM LsThing AS o WHERE o.recordedDate < :recordedDate");
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            queryBuilder.append(" ORDER BY ").append(sortFieldName);
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                queryBuilder.append(" ").append(sortOrder);
            }
        }
        TypedQuery<LsThing> q = em.createQuery(queryBuilder.toString(), LsThing.class);
        q.setParameter("recordedDate", recordedDate);
        return q;
    }

    public Set<LsThingState> getLsStates() {
        return this.lsStates;
    }

    public void setLsStates(Set<LsThingState> lsStates) {
        this.lsStates = lsStates;
    }

    public Set<LsThingLabel> getLsLabels() {
        return this.lsLabels;
    }

    public void setLsLabels(Set<LsThingLabel> lsLabels) {
        this.lsLabels = lsLabels;
    }

    public Set<LsTag> getLsTags() {
        return this.lsTags;
    }

    public void setLsTags(Set<LsTag> lsTags) {
        this.lsTags = lsTags;
    }

    public Set<ItxLsThingLsThing> getFirstLsThings() {
        return this.firstLsThings;
    }

    public void setFirstLsThings(Set<ItxLsThingLsThing> firstLsThings) {
        this.firstLsThings = firstLsThings;
    }

    public Set<ItxLsThingLsThing> getSecondLsThings() {
        return this.secondLsThings;
    }

    public void setSecondLsThings(Set<ItxLsThingLsThing> secondLsThings) {
        this.secondLsThings = secondLsThings;
    }
}
