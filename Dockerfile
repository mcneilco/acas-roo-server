FROM mcneilco/tomcat-maven

COPY	. /src
WORKDIR /src
ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH
RUN 	cp conf.properties $CATALINA_HOME
RUN 	mvn clean && \
		mvn clean && \
		mvn compile war:war && \
		mv target/acas*.war $CATALINA_HOME/webapps/acas.war

WORKDIR	$CATALINA_HOME
EXPOSE	8080
CMD		["catalina.sh", "run"]
