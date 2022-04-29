package com.labsynch.labseer.dto;

import java.util.Collection;
import java.util.List;

import com.labsynch.labseer.domain.Author;
import com.labsynch.labseer.utils.ExcludeNulls;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.transaction.annotation.Transactional;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class AuthorQueryResultDTO {

    Integer maxResults;

    Integer numberOfResults;

    Collection<Author> results;

    public AuthorQueryResultDTO() {

    }

    @Transactional
    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class", "results.password").include("results.lsTags", "results.lsLabels",
                        "results.lsStates.lsValues", "results.authorRoles.roleEntry")
                .transform(new ExcludeNulls(), void.class).serialize(this);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public Integer getMaxResults() {
        return this.maxResults;
    }

    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }

    public Integer getNumberOfResults() {
        return this.numberOfResults;
    }

    public void setNumberOfResults(Integer numberOfResults) {
        this.numberOfResults = numberOfResults;
    }

    public Collection<Author> getResults() {
        return this.results;
    }

    public void setResults(Collection<Author> results) {
        this.results = results;
    }

    public static AuthorQueryResultDTO fromJsonToAuthorQueryResultDTO(String json) {
        return new JSONDeserializer<AuthorQueryResultDTO>()
                .use(null, AuthorQueryResultDTO.class).deserialize(json);
    }

    public static String toJsonArray(Collection<AuthorQueryResultDTO> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<AuthorQueryResultDTO> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<AuthorQueryResultDTO> fromJsonArrayToAuthoes(String json) {
        return new JSONDeserializer<List<AuthorQueryResultDTO>>()
                .use("values", AuthorQueryResultDTO.class).deserialize(json);
    }
}
