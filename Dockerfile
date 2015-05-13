FROM mcneilco/tomcat-maven

COPY . /src
RUN cd /src && \
	mvn clean && \
	mvn clean && \
	mvn compile war:war && \
	mv target/acas*.war /usr/local/tomcat/webapps

WORKDIR $CATALINA_HOME
EXPOSE 8080
CMD ["catalina.sh", "run"]
