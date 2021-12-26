package com.labsynch.labseer.utils;

public class Response {

    private String id;
    private String responseBody;
    private int responseCode;
    
    public Response(String id,int responseCode, String responseBody) {
        this.id = id;
        this.responseBody = responseBody;
        this.responseCode = responseCode;
    }
    
    public String getId() {
        return id;
    }
    
    public int getResponseCode() {
        return responseCode;
    }
    
    public String getResponseBody() {
        return responseBody;
    }
}