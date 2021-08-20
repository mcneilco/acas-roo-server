ARG 	ACAS_IMAGE=mcneilco/acas-oss:1.13.6.3

ARG 	CHEMISTRY_PACKAGE=jchem
ARG 	TOMCAT_IMAGE=mcneilco/tomcat-maven:1.4-openjdk8

FROM 	${TOMCAT_IMAGE} as dependencies
ARG     CHEMISTRY_PACKAGE
ENV     CHEMISTRY_PACKAGE=${CHEMISTRY_PACKAGE}

FROM 	${ACAS_IMAGE} AS acas-src

FROM 	dependencies as jchem
ADD 	lib/jchem-16.4.25.0.jar /lib/jchem-16.4.25.0.jar
RUN     mvn install:install-file -Dfile=/lib/jchem-16.4.25.0.jar -DartifactId=jchem -DgroupId=com.chemaxon -Dversion=16.4.25.0 -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true

FROM 	dependencies as indigo

FROM 	${CHEMISTRY_PACKAGE} as compile
WORKDIR /src
ADD 	pom.xml /src/pom.xml
RUN 	mvn dependency:resolve -P ${CHEMISTRY_PACKAGE}
ADD 	. /src
RUN 	mvn clean && \
        mvn compile war:war -P ${CHEMISTRY_PACKAGE}
RUN     mv target/acas-1.13-BUILD-SNAPSHOT.war $CATALINA_HOME/webapps/acas.war && \
        mv target/acas-1.13-BUILD-SNAPSHOT/ $CATALINA_HOME/webapps/acas/

FROM 	${TOMCAT_IMAGE} as build
COPY 	--from=compile /usr/local/tomcat/webapps/acas.war $CATALINA_HOME/webapps/acas.war
WORKDIR $CATALINA_HOME
EXPOSE 	8080

COPY --from=acas-src --chown=runner:runner /home/runner/build/src/javascripts/BuildUtilities/PrepareConfigFiles.js /home/runner/build//src/javascripts/BuildUtilities/PrepareConfigFiles.js
COPY entrypoint.sh /entrypoint.sh
ENTRYPOINT ["/bin/bash", "/entrypoint.sh"] 
CMD 	["catalina.sh", "run"]