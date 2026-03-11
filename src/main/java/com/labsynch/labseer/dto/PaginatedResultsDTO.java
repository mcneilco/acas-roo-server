package com.labsynch.labseer.dto;

import java.util.Collection;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import flexjson.JSONSerializer;

public class PaginatedResultsDTO<T> {

    private Collection<T> results;

    private long totalRecords;

    private int page;

    private int pageSize;

    private long totalPages;

    public PaginatedResultsDTO() {
        // empty constructor
    }

    public PaginatedResultsDTO(Collection<T> results, long totalRecords, int page, int pageSize) {
        this.results = results;
        this.totalRecords = totalRecords;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = pageSize > 0 ? (long) Math.ceil((double) totalRecords / pageSize) : 0;
    }

    public String toJson() {
        return new JSONSerializer().exclude("*.class").include("results").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer().include(fields).exclude("*.class").serialize(this);
    }

    public Collection<T> getResults() {
        return this.results;
    }

    public void setResults(Collection<T> results) {
        this.results = results;
    }

    public long getTotalRecords() {
        return this.totalRecords;
    }

    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalPages() {
        return this.totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
