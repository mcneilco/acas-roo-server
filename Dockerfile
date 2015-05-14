FROM mcneilco/tomcat-maven

COPY	. /src
WORKDIR /src
ENV		$CATALINA_HOME=/usr/local/tomcat
RUN 	cp config.properties $CATALINA_HOME
RUN 	mvn clean && \
		mvn clean && \
		mvn compile war:war && \
		mv target/acas*.war $CATALINA_HOME/webapps/acas.war

WORKDIR	$CATALINA_HOME
EXPOSE	8080
CMD		["catalina.sh", "run"]
