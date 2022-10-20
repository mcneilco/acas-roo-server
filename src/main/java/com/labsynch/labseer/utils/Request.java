package com.labsynch.labseer.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.io.IOUtils;

public class Request implements Callable<Response> {

	Logger logger = LoggerFactory.getLogger(Request.class);

    private HttpURLConnection con;
    private URL obj;
    private String response;

    private String url;
    private int id;
    private String json;

    public Request(int id, String url, String json) {
        this.id = id;
        this.url = url;
        this.json = json;
    }

    @Override
    public Response call() {
        try {
            obj = new URL(url);
            con = (HttpURLConnection) obj.openConnection();
            String charset = "UTF-8";
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Accept-Charset", charset);
            con.setRequestProperty("Content-Type", "application/json");
            OutputStream output = con.getOutputStream();
            output.write(json.getBytes());

            int responseCode = con.getResponseCode();
            InputStream inputStream = null;
            if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = con.getInputStream();
            } else {
                inputStream = con.getErrorStream();
            }
            String body = IOUtils.toString(inputStream);
            return new Response(id, responseCode, body);
        } catch (IOException e) {
            logger.error("Error in Request", e);
            response = "{\"output\":\"some error occurred\"}";
            return new Response(id, 404, response);
        }
    }
}