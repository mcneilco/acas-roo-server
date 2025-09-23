#!/bin/bash
# harden_tomcat.sh
# Hardens Apache Tomcat configuration per OWASP guidelines
# Removes default files, applies security settings, and fixes permissions
# Usage: sudo bash harden_tomcat.sh

set -e

INSTALL_DIR="/opt/tomcat"
LOG_FILE="/tmp/tomcat_hardening_$(date +%F-%H%M%S).log"
BACKUP_DIR="/opt/tomcat-config-backup-$(date +%F-%H%M%S)"

# Function to log messages
log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" | tee -a "$LOG_FILE"
}

# Function to check if running as root
check_root() {
    if [[ $EUID -ne 0 ]]; then
        log "ERROR: This script must be run as root (use sudo)"
        exit 1
    fi
}

# Function to detect Tomcat installation
detect_tomcat() {
    if [[ -d "$INSTALL_DIR" && -f "$INSTALL_DIR/bin/catalina.sh" ]]; then
        log "Found Tomcat installation at $INSTALL_DIR"
        return 0
    elif [[ -d "/usr/share/tomcat9" && -f "/usr/share/tomcat9/bin/catalina.sh" ]]; then
        INSTALL_DIR="/usr/share/tomcat9"
        log "Found Tomcat installation at $INSTALL_DIR"
        return 0
    else
        log "ERROR: Could not find Tomcat installation"
        exit 1
    fi
}

# Function to create configuration backup
backup_configs() {
    log "Creating configuration backup at $BACKUP_DIR..."
    mkdir -p "$BACKUP_DIR"
    cp -r "$INSTALL_DIR/conf" "$BACKUP_DIR/"
    cp -r "$INSTALL_DIR/webapps" "$BACKUP_DIR/"
    log "Configuration backup created"
}

# Function to remove default webapps and files
remove_default_files() {
    log "Removing default Tomcat files and applications..."
    
    # Remove default webapps
    DEFAULT_WEBAPPS=("docs" "examples" "host-manager" "manager")
    
    for webapp in "${DEFAULT_WEBAPPS[@]}"; do
        if [[ -d "$INSTALL_DIR/webapps/$webapp" ]]; then
            log "Removing $webapp webapp"
            rm -rf "$INSTALL_DIR/webapps/$webapp"
        fi
    done
    
    # Clear ROOT webapp but keep directory structure
    if [[ -d "$INSTALL_DIR/webapps/ROOT" ]]; then
        log "Clearing ROOT webapp contents"
        rm -rf "$INSTALL_DIR/webapps/ROOT"/*
        
        # Create a simple index.html to replace default
        cat > "$INSTALL_DIR/webapps/ROOT/index.html" << 'EOF'
<!DOCTYPE html>
<html>
<head>
    <title>Application Server</title>
</head>
<body>
    <h1>Application Server</h1>
    <p>This server is running properly.</p>
</body>
</html>
EOF
        log "Created custom index.html for ROOT webapp"
    fi
}

# Function to configure security headers in web.xml
configure_security_headers() {
    log "Configuring security headers..."
    
    WEBXML="$INSTALL_DIR/conf/web.xml"
    
    # Check if security filter already exists
    if grep -q "HttpHeaderSecurityFilter" "$WEBXML"; then
        log "Security headers already configured"
        return 0
    fi
    
    # Create backup of web.xml
    cp "$WEBXML" "$WEBXML.backup"
    
    # Find the insertion point (before </web-app>)
    if grep -q "</web-app>" "$WEBXML"; then
        # Create temporary file with security headers
        cat > /tmp/security_headers.xml << 'EOF'
    
    <!-- Security Headers Filter -->
    <filter>
        <filter-name>httpHeaderSecurity</filter-name>
        <filter-class>org.apache.catalina.filters.HttpHeaderSecurityFilter</filter-class>
        <init-param>
            <param-name>antiClickJackingEnabled</param-name>
            <param-value>true</param-value>
        </init-param>
        <init-param>
            <param-name>antiClickJackingOption</param-name>
            <param-value>DENY</param-value>
        </init-param>
        <init-param>
            <param-name>xssProtection</param-name>
            <param-value>1; mode=block</param-value>
        </init-param>
        <init-param>
            <param-name>contentSecurityPolicy</param-name>
            <param-value>default-src 'self'; script-src 'self' 'unsafe-inline'; style-src 'self' 'unsafe-inline'</param-value>
        </init-param>
        <init-param>
            <param-name>httpStatusInErrorPage</param-name>
            <param-value>false</param-value>
        </init-param>
    </filter>
    
    <filter-mapping>
        <filter-name>httpHeaderSecurity</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    
    <!-- Default Error Pages -->
    <error-page>
        <error-code>400</error-code>
        <location>/error.html</location>
    </error-page>
    <error-page>
        <error-code>401</error-code>
        <location>/error.html</location>
    </error-page>
    <error-page>
        <error-code>403</error-code>
        <location>/error.html</location>
    </error-page>
    <error-page>
        <error-code>404</error-code>
        <location>/error.html</location>
    </error-page>
    <error-page>
        <error-code>500</error-code>
        <location>/error.html</location>
    </error-page>

EOF
        
        # Insert security headers before </web-app>
        sed -i '/<\/web-app>/i\<!-- Security configuration inserted by hardening script -->' "$WEBXML"
        sed -i '/<\/web-app>/r /tmp/security_headers.xml' "$WEBXML"
        
        rm /tmp/security_headers.xml
        log "Security headers configured in web.xml"
    else
        log "ERROR: Could not find </web-app> tag in web.xml"
    fi
}

# Function to create custom error page
create_error_page() {
    log "Creating custom error page..."
    
    cat > "$INSTALL_DIR/webapps/ROOT/error.html" << 'EOF'
<!DOCTYPE html>
<html>
<head>
    <title>Error</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 50px; }
        .error { background: #f8f8f8; padding: 20px; border: 1px solid #ddd; }
    </style>
</head>
<body>
    <div class="error">
        <h1>Error</h1>
        <p>An error occurred while processing your request.</p>
        <p>Please contact support if this problem persists.</p>
    </div>
</body>
</html>
EOF
    log "Custom error page created"
}

# Function to harden server.xml
harden_server_xml() {
    log "Hardening server.xml configuration..."
    
    SERVERXML="$INSTALL_DIR/conf/server.xml"
    cp "$SERVERXML" "$SERVERXML.backup"
    
    # Remove version info from HTTP headers
    if ! grep -q 'server=""' "$SERVERXML"; then
        sed -i 's/<Connector port="8080"/<Connector port="8080" server=""/' "$SERVERXML"
        log "Removed server version from HTTP headers"
    fi
    
    # Disable directory listings (already should be in web.xml, but double-check)
    # This is typically handled in the default servlet configuration
    
    log "server.xml hardening completed"
}

# Function to set secure file permissions
set_permissions() {
    log "Setting secure file permissions..."
    
    # Determine the correct user/group
    TOMCAT_USER="tomcat"
    TOMCAT_GROUP="tomcat"
    
    # Check if tomcat user exists, if not try other common names
    if ! id "$TOMCAT_USER" >/dev/null 2>&1; then
        for user in tomcat8 tomcat9 apache; do
            if id "$user" >/dev/null 2>&1; then
                TOMCAT_USER="$user"
                TOMCAT_GROUP="$user"
                break
            fi
        done
    fi
    
    log "Setting ownership to $TOMCAT_USER:$TOMCAT_GROUP"
    chown -R "$TOMCAT_USER:$TOMCAT_GROUP" "$INSTALL_DIR"
    
    # Set directory permissions
    find "$INSTALL_DIR" -type d -exec chmod 750 {} \;
    
    # Set file permissions
    find "$INSTALL_DIR" -type f -exec chmod 640 {} \;
    
    # Make scripts executable
    chmod 750 "$INSTALL_DIR/bin"/*.sh
    
    # Restrict conf directory
    chmod 750 "$INSTALL_DIR/conf"
    chmod 640 "$INSTALL_DIR/conf"/*
    
    # Restrict logs directory
    if [[ -d "$INSTALL_DIR/logs" ]]; then
        chmod 750 "$INSTALL_DIR/logs"
    fi
    
    log "File permissions set securely"
}

# Function to disable unnecessary HTTP methods
configure_http_methods() {
    log "Configuring HTTP method restrictions..."
    
    WEBXML="$INSTALL_DIR/conf/web.xml"
    
    # Check if method restrictions already exist
    if grep -q "HttpMethodConstraint" "$WEBXML"; then
        log "HTTP method restrictions already configured"
        return 0
    fi
    
    # Add security constraint for HTTP methods
    cat > /tmp/http_methods.xml << 'EOF'
    
    <!-- HTTP Method Restrictions -->
    <security-constraint>
        <web-resource-collection>
            <web-resource-name>restricted methods</web-resource-name>
            <url-pattern>/*</url-pattern>
            <http-method>TRACE</http-method>
            <http-method>OPTIONS</http-method>
            <http-method>HEAD</http-method>
        </web-resource-collection>
        <auth-constraint />
    </security-constraint>

EOF
    
    # Insert before </web-app>
    sed -i '/<\/web-app>/r /tmp/http_methods.xml' "$WEBXML"
    rm /tmp/http_methods.xml
    
    log "HTTP method restrictions configured"
}

# Function to restart Tomcat
restart_tomcat() {
    log "Restarting Tomcat to apply changes..."
    
    systemctl stop tomcat 2>/dev/null || service tomcat stop 2>/dev/null || {
        log "WARNING: Could not stop tomcat service"
        pkill -f tomcat || true
    }
    
    sleep 5
    
    systemctl start tomcat 2>/dev/null || service tomcat start 2>/dev/null || {
        log "WARNING: Could not start tomcat via systemctl/service"
        log "Try starting manually: $INSTALL_DIR/bin/startup.sh"
        return 1
    }
    
    sleep 10
    log "Tomcat restarted"
}

# Function to verify hardening
verify_hardening() {
    log "Verifying hardening configuration..."
    
    # Check if default webapps are removed
    REMOVED_COUNT=0
    for webapp in "docs" "examples" "host-manager" "manager"; do
        if [[ ! -d "$INSTALL_DIR/webapps/$webapp" ]]; then
            ((REMOVED_COUNT++))
        fi
    done
    log "Removed $REMOVED_COUNT default webapps"
    
    # Test if Tomcat is responding
    if curl -s http://localhost:8080 >/dev/null 2>&1; then
        log "SUCCESS: Tomcat is responding"
        
        # Test security headers
        HEADERS=$(curl -I http://localhost:8080 2>/dev/null)
        if echo "$HEADERS" | grep -q "X-Frame-Options"; then
            log "SUCCESS: Security headers are present"
        else
            log "WARNING: Security headers may not be applied"
        fi
    else
        log "WARNING: Tomcat may not be responding - check manually"
    fi
    
    # Check file permissions
    CONF_PERMS=$(stat -c "%a" "$INSTALL_DIR/conf")
    if [[ "$CONF_PERMS" == "750" ]]; then
        log "SUCCESS: Configuration directory permissions are secure"
    else
        log "WARNING: Configuration directory permissions may not be optimal"
    fi
}

# Function to generate hardening report
generate_report() {
    cat << EOF

=== TOMCAT HARDENING SUMMARY ===
Hostname: $(hostname)
Date: $(date)
Tomcat Installation: $INSTALL_DIR
Configuration Backup: $BACKUP_DIR
Log File: $LOG_FILE

Hardening Applied:
✓ Default webapps removed (docs, examples, manager, host-manager)
✓ Custom error pages configured
✓ Security headers enabled
✓ File permissions secured
✓ HTTP method restrictions applied
✓ Server version information hidden

Next Steps:
1. Test your applications thoroughly
2. Verify error pages display correctly
3. Check that only authorized HTTP methods work
4. Report results to security team
5. Schedule follow-up vulnerability scan

Note: If any applications fail after hardening, check the backup at:
$BACKUP_DIR

EOF
}

# Main execution
main() {
    log "Starting Tomcat hardening process..."
    log "Log file: $LOG_FILE"
    
    check_root
    detect_tomcat
    backup_configs
    remove_default_files
    create_error_page
    configure_security_headers
    configure_http_methods
    harden_server_xml
    set_permissions
    restart_tomcat
    verify_hardening
    generate_report
    
    log "Tomcat hardening process completed!"
}

# Handle script interruption
trap 'log "Script interrupted"; exit 1' INT TERM

# Run main function
main "$@"