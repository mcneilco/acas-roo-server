# Tomcat Security Hardening Implementation

This document describes the security hardening measures implemented in the ACAS application to address IT security requirements.

## Security Measures Implemented

### 1. Default Webapp Removal
- **What**: Removed default Tomcat webapps (docs, examples, manager, host-manager)
- **Where**: Dockerfile-multistage (lines removing default webapps)
- **Why**: These default applications can expose server information and provide attack vectors

### 2. Security Headers
- **What**: Added HTTP security headers via HttpHeaderSecurityFilter
- **Where**: src/main/webapp/WEB-INF/web.xml
- **Headers Applied**:
  - `X-Frame-Options: DENY` - Prevents clickjacking attacks
  - `X-XSS-Protection: 1; mode=block` - Enables XSS protection
  - `Content-Security-Policy` - Restricts resource loading
  - `Strict-Transport-Security` - Enforces HTTPS (when enabled)

### 3. HTTP Method Restrictions
- **What**: Disabled dangerous HTTP methods (TRACE, OPTIONS)
- **Where**: src/main/webapp/WEB-INF/web.xml
- **Why**: TRACE method can be used for cross-site tracing attacks

### 4. Custom Error Pages
- **What**: Custom error pages that don't reveal server information
- **Where**: src/main/webapp/error.html
- **Why**: Default error pages can leak server version and configuration details

### 5. Server Information Hiding
- **What**: Removed server version from HTTP headers
- **Where**: src/main/resources/tomcat-config/server.xml (server="" attribute)
- **Why**: Server version information can help attackers identify vulnerabilities

### 6. File Permissions
- **What**: Secure file permissions set at runtime
- **Where**: harden_tomcat_runtime.sh
- **Permissions**:
  - Configuration files: 640
  - Directories: 750
  - Web content: 644/755

### 7. JVM Security Configuration
- **What**: Security-focused JVM options
- **Where**: harden_tomcat_runtime.sh
- **Options**:
  - Headless mode enabled
  - Secure random number generation
  - UTF-8 encoding enforced

## Files Modified/Created

### Modified Files:
1. `src/main/webapp/WEB-INF/web.xml` - Added security filters and constraints
2. `Dockerfile-multistage` - Removed default webapps, added configurations
3. `entrypoint.sh` - Added runtime hardening execution

### New Files:
1. `src/main/webapp/error.html` - Custom error page
2. `src/main/resources/tomcat-config/server.xml` - Hardened server configuration
3. `harden_tomcat_runtime.sh` - Runtime security hardening script
4. `SECURITY.md` - This documentation

## Configuration Details

### Security Headers Configuration
The HttpHeaderSecurityFilter is configured with the following parameters:
- Anti-clickjacking protection enabled with DENY policy
- XSS protection enabled with blocking mode
- Content Security Policy restricting resource origins
- HTTP status codes hidden in error pages
- HSTS header for secure transport (when HTTPS is enabled)

### Content Security Policy
The CSP is configured to:
- Allow resources only from same origin ('self')
- Allow inline scripts and styles (required for some legacy functionality)
- Allow data: URLs for images
- Allow self-hosted fonts

### HTTP Method Restrictions
The following HTTP methods are explicitly blocked:
- TRACE (can be used for cross-site tracing attacks)
- OPTIONS (can reveal server capabilities)

## Testing Security Implementation

### 1. Verify Default Webapps Removal
```bash
# These should return 404
curl -I http://localhost:8080/manager/
curl -I http://localhost:8080/docs/
curl -I http://localhost:8080/examples/
```

### 2. Check Security Headers
```bash
curl -I http://localhost:8080/
# Should include:
# X-Frame-Options: DENY
# X-XSS-Protection: 1; mode=block
# Content-Security-Policy: ...
```

### 3. Test HTTP Method Restrictions
```bash
# Should return 403 Forbidden
curl -X TRACE http://localhost:8080/
curl -X OPTIONS http://localhost:8080/
```

### 4. Verify Error Pages
```bash
# Should show custom error page without server details
curl http://localhost:8080/nonexistent-page
```

### 5. Check Server Header
```bash
curl -I http://localhost:8080/
# Server header should be empty or not present
```

## Maintenance Notes

- Security headers can be adjusted in web.xml if application requirements change
- CSP policy may need updates when adding new external resources
- Error pages should be kept generic to avoid information disclosure
- Regularly review and update security configurations as new threats emerge

## Compliance

This implementation addresses common security requirements including:
- OWASP Top 10 recommendations
- Security headers best practices
- Information disclosure prevention
- Attack surface reduction
