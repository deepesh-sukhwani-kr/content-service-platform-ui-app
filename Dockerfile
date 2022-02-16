FROM krogertechnology-docker-prod.jfrog.io/library/java-openjre:8-latest
ARG JAR_FILE=./*-server/target/contentservicesplatform-ui-app-server-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} ./csp-ui-app.jar
ENV JAVA_OPTS="-XX:+UseG1GC -Xms1g -Xmx1g -XX:+UseStringDeduplication -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/tmp/heapdump.bin"
ENV SPRING_PROFILES_ACTIVE="local"
EXPOSE 8080
CMD ["java", "-Dlog4j2.formatMsgNoLookups=true", "-jar", "/csp-ui-app.jar"]
