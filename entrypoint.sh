#!/bin/bash
#!/bin/sh
# ... do some setup ...
# then run the CMD passed as command-line arguments
coffee $ACAS_HOME/src/javascripts/BuildUtilities/PrepareConfigFiles.coffee
exec "$@"