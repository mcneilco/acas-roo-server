ARG 	CHEMISTRY_PACKAGE=jchem
FROM maven:3-openjdk-8 as builder

FROM 	builder as dependencies
ARG     CHEMISTRY_PACKAGE
ENV     CHEMISTRY_PACKAGE=${CHEMISTRY_PACKAGE}

FROM 	dependencies as jchem
ADD 	lib/jchem-16.4.25.0.jar /lib/jchem-16.4.25.0.jar
RUN     mvn install:install-file -Dfile=/lib/jchem-16.4.25.0.jar -DartifactId=jchem -DgroupId=com.chemaxon -Dversion=16.4.25.0 -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true

FROM  ${CHEMISTRY_PACKAGE} as compile

ADD  pom.xml /src/pom.xml
WORKDIR /src
COPY  .m2/repository /root/.m2/repository
RUN  mvn dependency:resolve -P ${CHEMISTRY_PACKAGE}
ADD  . /src
RUN  mvn clean && \
        mvn compile war:war -P ${CHEMISTRY_PACKAGE}
COPY  /root/.m2/repository .m2/repository

FROM tomcat:9.0.62-jre8-openjdk-slim-buster

RUN apt-get update && \
    apt-get install -y openssl libfontconfig libfreetype6 curl

RUN  sed -i 's/<Connector port="8080"/<Connector address="${listen.address}" port="8080"/' conf/server.xml

# Add nodejs for prepare config files
ENV NPM_CONFIG_LOGLEVEL warn
ENV NODE_VERSION 14.x
RUN curl -fsSL https://deb.nodesource.com/setup_$NODE_VERSION | bash - && \
  apt-get install -y nodejs && \
  npm install -g coffeescript@2.5.1 properties@1.2.1 underscore@1.12.0 underscore-deep-extend@1.1.5 properties-parser@0.3.1 flat@5.0.2 glob@7.1.6
ENV NODE_PATH /usr/lib/node_modules

# Add runner user so we don't run as root
RUN	useradd -u 1000 -ms /bin/bash runner

# Allow certificates to be added by runner
RUN chgrp runner /usr/local/openjdk-8/lib/security/cacerts && \
    chmod g+w /usr/local/openjdk-8/lib/security/cacerts && \
    mkdir -p /usr/lib/jvm/java/jre/lib/security && \
    ln -s /usr/local/openjdk-8/lib/security/cacerts /usr/lib/jvm/java/jre/lib/security/cacerts

# Get acas-roo-server compiled code
COPY 	--chown=runner:runner --from=compile /src/target/acas*.war /usr/local/tomcat/webapps/acas.war
COPY 	--chown=runner:runner --from=compile /src/target/acas* /usr/local/tomcat/webapps/acas/

# Wait for it startup script
COPY 	--chown=runner:runner wait-for-it.sh ./wait-for-it.sh
RUN 	chmod 755 wait-for-it.sh

WORKDIR $CATALINA_HOME
ENV    ACAS_HOME=/home/runner/build
ENV    CATALINA_OPTS="-Xms512M -Xmx1536M -XX:MaxPermSize=512m"

# Prepare config files
COPY --chown=runner:runner ./PrepareConfigFiles.coffee /home/runner/build/src/javascripts/BuildUtilities/PrepareConfigFiles.coffee

# Become runner user so we don't run as root
USER runner

# Setup entrypoint
COPY entrypoint.sh /entrypoint.sh
ENTRYPOINT ["/bin/bash", "/entrypoint.sh"] 
CMD 	["catalina.sh", "run"]
