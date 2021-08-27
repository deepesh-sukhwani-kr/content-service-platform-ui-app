FROM docker-remote.registry.kroger.com/maven:3.6.3-openjdk-8 as build
ARG CUSTOM_MVN_SETTINGS=https://artifactory.kroger.com/artifactory/kroger-alm/maven/config/settings.xml
ENV MAVEN_OPTS="-Xmx2g -Xss128M -XX:MetaspaceSize=512M -XX:MaxMetaspaceSize=1024M -XX:+CMSClassUnloadingEnabled"
ENV JAVA_OPTS="-XX:+UseG1GC -Xms1g -Xmx2g -XX:+UseParallelGC -XX:+UseStringDeduplication -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/tmp/heapdump.bin"
ADD ${CUSTOM_MVN_SETTINGS} /usr/share/maven/conf/settings.xml
RUN mkdir -p /
COPY [".","/"]
WORKDIR /
RUN mvn clean package

FROM docker-prod.registry.kroger.com/library/java-openjre:8-latest
ARG JAR_FILE=./*-server/target/contentservicesplatform-ui-app-server-0.0.1-SNAPSHOT.jar
COPY --from=build ${JAR_FILE} ./csp-ui-app.jar
ENV JAVA_OPTS="-XX:+UseG1GC -Xms1g -Xmx1g -XX:+UseStringDeduplication -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/tmp/heapdump.bin"
ENV SPRING_PROFILES_ACTIVE="local"
EXPOSE 8080
CMD ["java", "-jar", "/csp-ui-app.jar"]
