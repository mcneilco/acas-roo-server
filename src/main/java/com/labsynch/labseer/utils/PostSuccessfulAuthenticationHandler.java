package com.labsynch.labseer.utils;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

public class PostSuccessfulAuthenticationHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	static Logger logger = LoggerFactory.getLogger(PostSuccessfulAuthenticationHandler.class);

	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		/*
		 * Add post authentication logic in the trackUseLogin method of userService;
		 */
		logger.debug("principal info: " + authentication.getPrincipal());
		Object principal = authentication.getPrincipal();
		if (principal instanceof LdapUserDetails) {
			LdapUserDetails ldapPrincipal = (LdapUserDetails) principal;
			String principalUserName = ldapPrincipal.getUsername();
			logger.debug("username: " + principalUserName);
			String dn = ldapPrincipal.getDn();
			int beginIndex = dn.indexOf("cn=") + 3;
			int endIndex = dn.indexOf(",");
			String fullName = dn.substring(beginIndex, endIndex);
			logger.debug("fullName: " + fullName);
			SecurityUtil.updateUserInfo(principalUserName, fullName);
		}
		SecurityUtil.updateUserInfo(authentication.getName());
		super.onAuthenticationSuccess(request, response, authentication);
	}

	protected String getRedirectUrl(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {

			SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, null);
			// (SavedRequest) session.getAttribute(HttpSessionRequestCache.SAVED_REQUEST);
			if (savedRequest != null) {
				return savedRequest.getRedirectUrl();
			}
		}

		/* return a sane default in case data isn't there */
		return request.getContextPath() + "/";
	}

}
