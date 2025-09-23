#!/bin/bash
#!/bin/sh
# ... do some setup ...
# Prepare configuration files
coffee $ACAS_HOME/src/javascripts/BuildUtilities/PrepareConfigFiles.coffee

# Apply runtime security hardening
source ./harden_tomcat_runtime.sh

# then run the CMD passed as command-line arguments
exec "$@"
