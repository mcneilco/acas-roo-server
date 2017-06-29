package com.labsynch.labseer.service;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import com.labsynch.labseer.utils.SecurityUtils;

public class PostSuccessfulAuthenticationHandler extends  SimpleUrlAuthenticationSuccessHandler {

	static Logger logger = LoggerFactory.getLogger(PostSuccessfulAuthenticationHandler.class);

	private RequestCache requestCache = new HttpSessionRequestCache();

	@Autowired
	private SecurityUtils securityUtils;
	
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		/*
		 *  Add post authentication logic in the trackUseLogin method of userService;
		 */
		logger.info("principal info: " + authentication.getPrincipal());
		super.onAuthenticationSuccess(request, response, authentication);
		
		securityUtils.updateAuthorInfo(authentication);
		
		SecurityContext context = SecurityContextHolder.getContext();
		logger.info("context is " + context);
		Authentication auth = context.getAuthentication();
		logger.info("authentication: " + auth);

		Collection<? extends GrantedAuthority> auths = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		for (GrantedAuthority ga : auths){
			logger.info("granted auth: " + ga);
		}

		
	}
	
	protected String getRedirectUrl(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        logger.info("authentication was successful. ");
        
        if (savedRequest == null) {
            clearAuthenticationAttributes(request);
            logger.info("null savedRequest. ");

            return request.getContextPath() + "/";
        }
        
        final String targetUrlParameter = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl() || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
            requestCache.removeRequest(request, response);
            clearAuthenticationAttributes(request);
            return request.getContextPath() + "/";
        }

        clearAuthenticationAttributes(request);

        // Use the DefaultSavedRequest URL
        // final String targetUrl = savedRequest.getRedirectUrl();
        // logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
        // getRedirectStrategy().sendRedirect(request, response, targetUrl);
        
	    /* return a sane default in case data isn't there */
	    return request.getContextPath() + "/";
	}
	
	public String onAuthenticationSuccessOld(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException, Exception {
		
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        logger.info("authentication was successful. ");
        
        if (savedRequest == null) {
            clearAuthenticationAttributes(request);
            logger.info("null savedRequest. ");

            return request.getContextPath() + "/";
        }
        final String targetUrlParameter = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl() || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
            requestCache.removeRequest(request, response);
            clearAuthenticationAttributes(request);
            return request.getContextPath() + "/";
        }

        clearAuthenticationAttributes(request);

        // Use the DefaultSavedRequest URL
        // final String targetUrl = savedRequest.getRedirectUrl();
        // logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
        // getRedirectStrategy().sendRedirect(request, response, targetUrl);
        
	    /* return a sane default in case data isn't there */
	    return request.getContextPath() + "/";
	}


	public void setRequestCache(RequestCache requestCache) throws Exception {
		this.requestCache = requestCache;

	}


}

