#!/bin/sh

if [ -f /etc/secrets/application.properties ]; then
  JAVA_OPTS="${JAVA_OPTS} -Dspring.config.location=/etc/secrets/application.properties"
fi

if [ -n "${APP_ENV}" ]; then
  JAVA_OPTS="${JAVA_OPTS} -Dspring.profiles.active=${APP_ENV}"
fi

# Java parameters for java8u192+
JAVA_OPTS="${JAVA_OPTS} -XX:+UnlockExperimentalVMOptions \
  -XX:+UseG1GC \
  -XX:+UseStringDeduplication \
  -XX:MinRAMPercentage=50.0 \
  -XX:MaxRAMPercentage=80.0 \
  -XshowSettings:vm"

# use app dir for tmp dir
JAVA_OPTS="${JAVA_OPTS} -Djava.io.tmpdir=/app/tmp"

# Is contrast enabled, yes or no
contrastassess_enabled=yes

# ENV for contrast assessment
contrastassess_env=qal
contrastassess_jar="/app/contrast/javaagent/contrast.jar"
if [ "${contrastassess_enabled}" = "yes" ] && [ $(echo "${APP_ENV}" | grep -q "${contrastassess_env}"; echo $?) == "0" ]; then
  JAVA_OPTS="${JAVA_OPTS} -javaagent:${contrastassess_jar}"
  JAVA_OPTS="${JAVA_OPTS} -Dcontrast.dir=/app/contrast/javaagent"
  JAVA_OPTS="${JAVA_OPTS} -Dcontrast.application.code=8722364171316486115"
  JAVA_OPTS="${JAVA_OPTS} -Dcontrast.standalone.appname=Intuit.intcollabs.notification.genericscheduler"
  JAVA_OPTS="${JAVA_OPTS} -Dcontrast.application.name=Intuit.intcollabs.notification.genericscheduler"
  JAVA_OPTS="${JAVA_OPTS} -Dcontrast.inspect.allclasses=false -Dcontrast.process.codesources=false"
  JAVA_OPTS="${JAVA_OPTS} -Dcontrast.inventory.libraries=false"
fi

jacocoagent_enabled=yes

# ENV for contrast assessment
jacocoagent_env=qal
jacocoagent_jar="/app/jacoco-agent/jacoco-agent-runtime.jar"
if [ "${jacocoagent_enabled}" = "yes" ] && [ $(echo "${APP_ENV}" | grep -q "${jacocoagent_env}"; echo $?) == "0" ]; then
  JAVA_OPTS="$JAVA_OPTS -javaagent:${jacocoagent_jar}=output=tcpserver,port=6300,address=*,includes=com/intuit/**,dumponexit=false"
fi

#When sidecar is injected, wait for sidecar to come up
if [[ "$MESH_ENABLED" == "true" ]]; then
until (echo >/dev/tcp/localhost/$MESH_SIDECAR_PORT) &>/dev/null ; do echo Waiting for Sidecar; sleep 3 ; done ; echo Sidecar available;
fi

#Is TLS termination done by service, yes or no
#Look in application.yml lines 27-38
service_layer_tls_termination=yes

if [ "${service_layer_tls_termination}" = "yes" ]; then
   export TLS_KEYSTORE_PASSWORD=$(tr -dc A-Za-z0-9 </dev/urandom | head -c 13 ; echo '')
   keytool -genkeypair -alias tomcat -keyalg RSA -keysize 2048   -validity 365\
        -dname "cn=JSK-MSaaS-based service, o=Intuit\, Inc., ou=Core Services \+ Experiences, ou=DevX, l=Paved Road entry.sh, st=CA, c=US"\
        -keypass ${TLS_KEYSTORE_PASSWORD} -keystore /app/tmp/keystore.pkcs12 -storetype PKCS12 -storepass ${TLS_KEYSTORE_PASSWORD}
   
fi

exec java $JAVA_OPTS -jar /app/scheduler-app.jar
