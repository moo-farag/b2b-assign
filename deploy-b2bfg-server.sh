#!/bin/bash
# Usage:
#        deploy-b2bfg-server.sh <ssh-target>
# Example:
#       deploy-b2bfg-server.sh root@127.0.0.1


SSH_TARGET=$1
WAR=$(ls build/libs/b2b-assign-*.war)

./banner.sh

echo "Cleaning old Deploy and copying new WAR"

# Creating Directory if missing (nothing happens if it exists)
ssh ${SSH_TARGET} mkdir -p /srv/apps/b2b-assign/dropzone/

# Cleaning old files
ssh ${SSH_TARGET} rm -rf /srv/apps/b2b-assign/dropzone/*
/usr/bin/scp ${WAR} ${SSH_TARGET}:/srv/apps/b2b-assign/dropzone/

echo "


=================================================================================================
"
echo "Finished Deployment Successfully."
echo "
=================================================================================================



"

# I would personally recommend adding update script on server that does the following:

# can add offline screen in nginx, something like "We are currently updating and will be back soon"
# Backup application and db
# Calls service to stop the running app
# Copies new received war in this script to its correct location
# Calls service to start app
# Remove nginx offline screen.

# Usage:
# ssh ${SSH_TARGET} "/srv/apps/b2b-assign/scripts/update.sh"

# Of course this is not the best practice, I'd personally recommend using something like Jenkins (pipelines) for effective CI/CD.
# For sure in AWS we can use CodePipeline & CodeDeploy
