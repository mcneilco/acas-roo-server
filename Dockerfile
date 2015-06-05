FROM mcneilco/tomcat-maven

COPY	pom.xml /src/pom.xml
WORKDIR /src
ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH
RUN 	mvn clean && mvn dependency:resolve
COPY	. /src
RUN		mvn compile war:war && \
		mv target/acas*.war $CATALINA_HOME/webapps/acas.war

WORKDIR	$CATALINA_HOME
EXPOSE	8080
CMD		["catalina.sh", "run"]
