FROM docker.intuit.com/oicp/standard/maven/amzn-maven-corretto11:latest AS build

# USER root needed for build, since CPD does not allow root user for gold images.
# intermediate containers will be discarded at final stage, runtime image will be executed with non root user
USER root

# The following ARG and 2 LABEL are used by Jenkinsfile command
# to identify this intermediate container, for extraction of
# code coverage and other reported values.
ARG build
LABEL build=${build}
LABEL image=build
ARG MVN_SETTINGS=settings.xml
ARG appVersion=local
COPY package /usr/src/package
COPY package /usr/src/processingapp
COPY project.properties /usr/src/project.properties
COPY app/pom.xml /usr/src/app/pom.xml
COPY pom.xml /usr/src/pom.xml
COPY ${MVN_SETTINGS} /usr/src/settings.xml
COPY core /usr/src/core
COPY app /usr/src/app
# '-U' to avoid missing updated SNAPSHOTs; see MSAASINT-4291.
RUN mvn -U -f /usr/src/pom.xml -s /usr/src/settings.xml -Drevision=${appVersion} clean install
# chmod in build layer before copy to final to avoid extra layer(s) in final image
RUN chmod 644 /usr/src/app/target/scheduler-app.jar /usr/src/package/target/build.params.json /usr/src/package/entry.sh

FROM docker.intuit.com/oicp/standard/java/amzn-corretto-jdk11:latest
ARG DOCKER_TAGS=latest
# ARG JIRA_PROJECT=https://jira.intuit.com/projects/<CHANGE_ME>
ARG DOCKER_IMAGE_NAME=docker.intuit.com/intcollabs-notification/generic-scheduler/service/scheduler:${DOCKER_TAGS}
ARG SERVICE_LINK=https://devportal.intuit.com/app/dp/resource/8722364171316486115

LABEL maintainer=firstname_lastname@intuit.com \
      app=scheduler \
      app-scope=runtime \
      build=${build}

# Switch to root for installation and some other operations
USER root
RUN install -d -m 0755 -o appuser -g appuser /app/tmp

COPY --from=build --chown=appuser:appuser /usr/src/app/target/scheduler-app.jar /usr/src/package/entry.sh /app/
COPY --from=build --chown=appuser:appuser /usr/src/package/target/build.params.json /build.params.json

RUN curl -o /app/contrast/javaagent/contrast.jar https://artifact.intuit.com/artifactory/generic-local/dev/security/ssdlc/contrast/java/latest/contrast.jar \
    && curl -o /app/jacoco-agent/jacoco-agent-runtime.jar --create-dirs https://artifact.intuit.com/artifactory/maven-proxy/org/jacoco/org.jacoco.agent/0.8.7/org.jacoco.agent-0.8.7-runtime.jar \
    && chown -R appuser:appuser /app/contrast /app/jacoco-agent

# Remove unnecessary tools
RUN ["/home/appuser/post_harden.sh"]

USER appuser
CMD ["/bin/sh", "/app/entry.sh"]
