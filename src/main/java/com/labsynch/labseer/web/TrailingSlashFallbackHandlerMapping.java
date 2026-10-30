package com.labsynch.labseer.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.ServletRequestPathUtils;

/**
 * Fallback handler mapping that lets a request with a trailing slash (e.g.
 * {@code /api/v1/authors/}) match a mapping declared without one
 * ({@code /api/v1/authors}).
 *
 * Spring Framework 7 removed {@code RequestMappingHandlerMapping#setUseTrailingSlashMatch},
 * which ACAS relied on through a secondary, lowest-precedence handler mapping.
 * This class restores that behaviour: the primary mapping registered by
 * {@code <mvc:annotation-driven/>} is tried first (so mappings that are declared
 * with a trailing slash keep matching exactly), and only if nothing matched is
 * the request retried here with the trailing slash removed.
 */
public class TrailingSlashFallbackHandlerMapping extends RequestMappingHandlerMapping {

	@Override
	protected HandlerMethod getHandlerInternal(HttpServletRequest request) throws Exception {
		String uri = request.getRequestURI();
		if (uri == null || uri.length() <= 1 || !uri.endsWith("/")) {
			return null;
		}
		String contextPath = request.getContextPath();
		if (uri.equals(contextPath + "/")) {
			return null;
		}
		HttpServletRequest stripped = new TrailingSlashStrippedRequest(request);
		Object originalPath = request.getAttribute(ServletRequestPathUtils.PATH_ATTRIBUTE);
		try {
			ServletRequestPathUtils.parseAndCache(stripped);
			return super.getHandlerInternal(stripped);
		} finally {
			if (originalPath != null) {
				request.setAttribute(ServletRequestPathUtils.PATH_ATTRIBUTE, originalPath);
			} else {
				request.removeAttribute(ServletRequestPathUtils.PATH_ATTRIBUTE);
			}
		}
	}

	private static String stripTrailingSlashes(String value) {
		if (value == null) {
			return null;
		}
		int end = value.length();
		while (end > 1 && value.charAt(end - 1) == '/') {
			end--;
		}
		return value.substring(0, end);
	}

	private static final class TrailingSlashStrippedRequest extends HttpServletRequestWrapper {

		TrailingSlashStrippedRequest(HttpServletRequest request) {
			super(request);
		}

		@Override
		public String getRequestURI() {
			return stripTrailingSlashes(super.getRequestURI());
		}

		@Override
		public String getServletPath() {
			return stripTrailingSlashes(super.getServletPath());
		}

		@Override
		public String getPathInfo() {
			return stripTrailingSlashes(super.getPathInfo());
		}

		@Override
		public StringBuffer getRequestURL() {
			return new StringBuffer(stripTrailingSlashes(super.getRequestURL().toString()));
		}
	}
}
