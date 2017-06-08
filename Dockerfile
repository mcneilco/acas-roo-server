FROM mcneilco/tomcat-maven:feature-inventory
WORKDIR /src
ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH
ADD 	pom.xml /src/pom.xml
RUN 	["mvn", "dependency:resolve"]
RUN		["mvn", "clean"]
ADD		. /src
RUN		mvn compile war:war
RUN		mv target/acas*.war $CATALINA_HOME/webapps/acas.war
RUN		mv target/acas* $CATALINA_HOME/webapps/acas
RUN		rm -rf /src
WORKDIR	$CATALINA_HOME
EXPOSE	8080
CMD		["catalina.sh", "run"]