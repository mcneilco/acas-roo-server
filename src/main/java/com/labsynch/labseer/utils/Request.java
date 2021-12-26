package com.labsynch.labseer.utils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Request implements Callable<Response> {

    private HttpURLConnection con;
    private URL obj;
    private String response;
    
    private String url;
    private String id;
    private String json;
    
    public Request(String id, String url, String json) {
        this.id = id;
        this.url = url;
        this.json = json;
    }
    
    @Override
    public Response call() {
        try {
            obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
    
            int responseCode = con.getResponseCode();
            if(responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer stringBuffer = new StringBuffer();
    
                while ((inputLine = in.readLine()) != null) {
                    stringBuffer.append(inputLine);
                }
                in.close();
                response = stringBuffer.toString();
                return new Response(id, responseCode, response);
            }
            else {
                response = "{\"response\":\"some error occurred\"}";
                return new Response(id, responseCode, response);
            }
    
        } catch (IOException e) {
            response = "{\"output\":\"some error occurred\"}";
            return new Response(id, 404, response);
        }
    }
}