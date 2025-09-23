#!/bin/bash
# harden_tomcat_runtime.sh
# Runtime security hardening for Tomcat in container
# This script runs as part of the container startup to apply additional security measures

set -e

TOMCAT_HOME="/usr/local/tomcat"

# Function to log messages
log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] SECURITY: $1"
}

# Set secure file permissions
secure_permissions() {
    log "Setting secure file permissions..."
    
    # Set restrictive permissions on configuration files
    find "$TOMCAT_HOME/conf" -type f -exec chmod 640 {} \;
    find "$TOMCAT_HOME/conf" -type d -exec chmod 750 {} \;
    
    # Ensure logs directory has proper permissions
    mkdir -p "$TOMCAT_HOME/logs"
    chmod 750 "$TOMCAT_HOME/logs"
    
    # Make sure webapps directory is secure
    find "$TOMCAT_HOME/webapps" -type d -exec chmod 755 {} \;
    find "$TOMCAT_HOME/webapps" -name "*.jsp" -exec chmod 644 {} \;
    find "$TOMCAT_HOME/webapps" -name "*.html" -exec chmod 644 {} \;
    
    log "File permissions secured"
}

# Verify default webapps are removed
verify_webapp_removal() {
    log "Verifying default webapps are removed..."
    
    local removed_count=0
    for webapp in "docs" "examples" "host-manager" "manager"; do
        if [[ ! -d "$TOMCAT_HOME/webapps/$webapp" ]]; then
            ((removed_count++))
        else
            log "WARNING: Default webapp $webapp still exists"
        fi
    done
    
    log "Verified $removed_count/4 default webapps removed"
}

# Configure JVM security properties
configure_jvm_security() {
    log "Configuring JVM security properties..."
    
    # Add security-focused JVM options to CATALINA_OPTS
    export CATALINA_OPTS="$CATALINA_OPTS -Djava.security.egd=file:/dev/./urandom"
    export CATALINA_OPTS="$CATALINA_OPTS -Djava.awt.headless=true"
    export CATALINA_OPTS="$CATALINA_OPTS -Dfile.encoding=UTF-8"
    export CATALINA_OPTS="$CATALINA_OPTS -Dserver.info.header=false"
    
    log "JVM security properties configured"
}

# Main execution
main() {
    log "Starting runtime security hardening..."
    
    secure_permissions
    verify_webapp_removal
    configure_jvm_security
    
    log "Runtime security hardening completed"
}

# Run hardening if script is executed directly
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    main "$@"
fi
