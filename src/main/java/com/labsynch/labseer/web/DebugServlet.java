package com.labsynch.labseer.web;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/debug/info")
public class DebugServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Debug Info</title></head><body>");
        out.println("<h1>Debug Information</h1>");
        
        out.println("<h2>Request Information</h2>");
        out.println("<p>Context Path: " + request.getContextPath() + "</p>");
        out.println("<p>Servlet Path: " + request.getServletPath() + "</p>");
        out.println("<p>Request URI: " + request.getRequestURI() + "</p>");
        out.println("<p>Request URL: " + request.getRequestURL() + "</p>");
        out.println("<p>Server Name: " + request.getServerName() + "</p>");
        out.println("<p>Server Port: " + request.getServerPort() + "</p>");
        
        out.println("<h2>Expected Security Endpoints</h2>");
        out.println("<p>Full Login URL: " + request.getScheme() + "://" + 
                   request.getServerName() + ":" + request.getServerPort() + 
                   request.getContextPath() + "/resources/j_spring_security_check</p>");
        
        out.println("<h2>Application Context</h2>");
        out.println("<p>Web Application deployed at: " + request.getContextPath() + "</p>");
        
        out.println("</body></html>");
    }
}
