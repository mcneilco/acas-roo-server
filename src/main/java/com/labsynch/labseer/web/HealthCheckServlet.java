package com.labsynch.labseer.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@WebServlet("/api/health")
public class HealthCheckServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
            
            StringBuilder json = new StringBuilder();
            json.append("{\n");
            json.append("  \"status\": \"OK\",\n");
            json.append("  \"contextPath\": \"").append(request.getContextPath()).append("\",\n");
            json.append("  \"springContextLoaded\": ").append(context != null).append(",\n");
            
            if (context != null) {
                json.append("  \"springSecurityFilterChain\": ").append(context.containsBean("springSecurityFilterChain")).append(",\n");
                json.append("  \"beanCount\": ").append(context.getBeanDefinitionCount()).append(",\n");
            }
            
            json.append("  \"expectedSecurityEndpoint\": \"").append(request.getContextPath()).append("/resources/j_spring_security_check\"\n");
            json.append("}\n");
            
            response.getWriter().write(json.toString());
            
        } catch (Exception e) {
            response.setStatus(500);
            response.getWriter().write("{\"status\": \"ERROR\", \"error\": \"" + e.getMessage() + "\"}");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}
