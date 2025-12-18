package com.labsynch.labseer.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import com.labsynch.labseer.domain.Lot;
import com.labsynch.labseer.utils.SimpleUtil;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class BatchProjectDTO {

    private static final Logger logger = LoggerFactory.getLogger(BatchProjectDTO.class);

    private String requestName;

    private String projectCode;

    public BatchProjectDTO() {

    }

    public BatchProjectDTO(String requestName, String projectCode) {
        this.requestName = requestName;
        this.projectCode = projectCode;
    }

    public static Collection<BatchProjectDTO> getBatchProjects(Collection<BatchProjectDTO> requestDTOs) {
        EntityManager em = Lot.entityManager();
        List<String> batchCodes = new ArrayList<String>();
        for (BatchProjectDTO requestDTO : requestDTOs) {
            batchCodes.add(requestDTO.getRequestName());
        }
        String queryString = "Select new com.labsynch.labseer.dto.BatchProjectDTO(lot.corpName, lot.project) "
                + "FROM Lot lot "
                + "WHERE ";
        Collection<Query> queries = SimpleUtil.splitHqlInClause(em, queryString, "lot.corpName", batchCodes);
        Collection<BatchProjectDTO> results = new ArrayList<BatchProjectDTO>();
        logger.debug("Querying for " + batchCodes.size() + " lot corpnames");
        for (Query q : queries) {
            if (logger.isDebugEnabled())
                logger.debug(q.unwrap(org.hibernate.Query.class).getQueryString());
            results.addAll(q.getResultList());
        }
        for (BatchProjectDTO result : results) {
            if (result.getProjectCode() == null) {
                result.setProjectCode("");
            }
        }
        return results;
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static BatchProjectDTO fromJsonToBatchProjectDTO(String json) {
        return new JSONDeserializer<BatchProjectDTO>()
                .use(null, BatchProjectDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<BatchProjectDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<BatchProjectDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<BatchProjectDTO> fromJsonArrayToBatchProes(String json) {
        return new JSONDeserializer<List<BatchProjectDTO>>()
                .use("values", BatchProjectDTO.class).deserialize(json);
    }

    public String getRequestName() {
        return this.requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public String getProjectCode() {
        return this.projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
