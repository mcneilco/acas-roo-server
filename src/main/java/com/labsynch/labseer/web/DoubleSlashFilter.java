package com.labsynch.labseer.web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filter that normalizes URLs containing double slashes (e.g., //protocoltypes -> /protocoltypes)
 * This prevents 404 errors when client-side code incorrectly constructs URLs with double slashes.
 */
public class DoubleSlashFilter implements Filter {
    
    private static final Logger logger = LoggerFactory.getLogger(DoubleSlashFilter.class);
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("DoubleSlashFilter initialized");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String uri = httpRequest.getRequestURI();
            
            // Check if URI contains double slashes
            if (uri.contains("//")) {
                // Normalize the URI by replacing multiple consecutive slashes with a single slash
                String normalizedUri = uri.replaceAll("/{2,}", "/");
                logger.debug("Normalizing URI from '{}' to '{}'", uri, normalizedUri);
                
                // Create a wrapper that returns the normalized URI
                HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(httpRequest) {
                    @Override
                    public String getRequestURI() {
                        return normalizedUri;
                    }
                    
                    @Override
                    public StringBuffer getRequestURL() {
                        StringBuffer url = new StringBuffer();
                        url.append(getScheme()).append("://").append(getServerName());
                        if ((getScheme().equals("http") && getServerPort() != 80) ||
                            (getScheme().equals("https") && getServerPort() != 443)) {
                            url.append(":").append(getServerPort());
                        }
                        url.append(normalizedUri);
                        return url;
                    }
                };
                
                chain.doFilter(wrappedRequest, response);
                return;
            }
        }
        
        // No double slashes found, continue with original request
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        logger.info("DoubleSlashFilter destroyed");
    }
}