package com.labsynch.labseer.utils;

public class Response {

    private int id;
    private String responseBody;
    private int responseCode;
    
    public Response(int id,int responseCode, String responseBody) {
        this.id = id;
        this.responseBody = responseBody;
        this.responseCode = responseCode;
    }
    
    public int getId() {
        return id;
    }
    
    public int getResponseCode() {
        return responseCode;
    }
    
    public String getResponseBody() {
        return responseBody;
    }
}