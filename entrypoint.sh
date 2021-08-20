#!/bin/bash
#!/bin/sh
# ... do some setup ...
# then run the CMD passed as command-line arguments
node $ACAS_HOME/src/javascripts/BuildUtilities/PrepareConfigFiles.js
exec "$@"