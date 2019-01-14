FROM mcneilco/tomcat-maven:openjdk8
ARG CHEMAXON_USERNAME
ARG CHEMAXON_PASSWORD
ENV CHEMAXON_USERNAME ${CHEMAXON_USERNAME}
ENV CHEMAXON_PASSWORD ${CHEMAXON_PASSWORD}
WORKDIR /src
ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH
ADD 	settings.xml /src/settings.xml
#RUN 	["mvn", "install:install-file","-Dfile=/lib/jchem-16.4.25.0.jar","-DartifactId=jchem","-DgroupId=com.chemaxon","-Dversion=16.4.25.0","-Dpackaging=jar","-DgeneratePom=true","-DcreateChecksum=true"]
#RUN 	["mvn", "dependency:get","-s","./settings.xml","-DrepoUrl=https://hub.chemaxon.com/artifactory/libs-release","-Dartifact=com.chemaxon:jchem-main:17.24.1","-Dtransitive=true"]
#RUN 	["mvn", "dependency:get","-s","./settings.xml","-DrepoUrl=https://hub.chemaxon.com/artifactory/libs-release","-DartifactId=jchem-main","-DgroupId=com.chemaxon","-Dversion=17.24.1","-Dpackaging=jar","-DgeneratePom=true","-DcreateChecksum=true","-Dtransitive=true"]
RUN		mvn dependency:get -s ./settings.xml -DrepoUrl=https://hub.chemaxon.com/artifactory/libs-release -Dartifact=com.chemaxon:jchem-main:17.24.1 -Dtransitive=true
ADD 	pom.xml /src/pom.xml
RUN    ["mvn", "dependency:resolve", "-P", "default"]
RUN		["mvn", "clean"]
ADD		. /src
RUN		mvn compile war:war -P default
RUN		mv target/acas*.war $CATALINA_HOME/webapps/acas.war
RUN		mv target/acas* $CATALINA_HOME/webapps/acas
RUN		rm -rf /src
WORKDIR	$CATALINA_HOME
EXPOSE	8080
CMD		["catalina.sh", "run"]