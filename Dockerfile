FROM mcneilco/tomcat-maven:1.3-openjdk8
WORKDIR /src
ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH
ADD 	pom.xml /src/pom.xml
ADD    lib/jchem-16.4.25.0.jar /lib/jchem-16.4.25.0.jar
RUN    ["mvn", "install:install-file","-Dfile=/lib/jchem-16.4.25.0.jar","-DartifactId=jchem","-DgroupId=com.chemaxon","-Dversion=16.4.25.0","-Dpackaging=jar","-DgeneratePom=true","-DcreateChecksum=true"]
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