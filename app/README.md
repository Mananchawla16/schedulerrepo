### Intuit Services Platform (ISP) Reference Service built on [Spring Boot MVC](https://spring.io/guides/gs/serving-web-content/) and [JSK](https://github.intuit.com/services-java/what-is-jsk)

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li><a href="#about-the-example">About the Example</a></li>
    <li><a href="#prerequisites">Prerequisites</a></li>
     <li><a href="#how-to-run-the-application-locally">How to run the application locally</a></li>
       <li><a href="#how-to-run-unit-tests">How to run unit tests</a></li>
        <li><a href="#how-to-run-karate-tests">How to run karate tests</a></li>
         <li><a href="#how-to-run-gatling-performance-tests">How to run Gatling Performance tests</a></li>
            <li><a href="#authentication">Securing your routes</a></li>
           <li><a href="#authentication-via-intuit-cookies">Authentication via Intuit cookies</a></li>
             <li><a href="#tls-termination-in-iks">TLS termination in IKS</a></li>
              <li><a href="#how-to-use-openAPI/swagger-playground">How to use Swagger Documentation</a></li>
     </ol>
     
</details>

### About the Example

**Hello-World Service** is a simple [REST](https://en.wikipedia.org/wiki/Representational_state_transfer) service that stores and retrieves documents in-memory. 

### Prerequisites
* [Java 1.8 OR later](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) 

* [Maven 3.1 OR later](https://maven.apache.org/download.cgi) 

#### How to run the application locally

From *Terminal* run this command:

```
 mvn clean spring-boot:run -s settings.xml -Dspring-boot.run.profiles=local
```

To make sure the application is running, check its health


```
curl https://localhost:8443/health/full

```

#### How to run unit tests

```
  mvn test -Punit
```

#### How to run karate tests

run this command for testing against locally running server with e2e downstream services

```
  mvn test -Pkarate -Dkarate.env=local
```

run this command for testing against locally running server with mocked downstream services

```
  mvn test -Pmock -Dkarate.env=mock
```

The `api-mock.feature` file is an example of how you can setup a mock api for any downstream dependencies. In addition to setting up mock endpoints and responses in the `api-mock.feature` file, you'll also have to set the url for that downstream dependency in the `karate-config.js` file. Instead of pointing to the actual url for the downstream dependency, it should point to your localhost and the karate mock port. By default, the karate mock port is randomized, but you can specify a port in the argline if you so choose. See below:

```
  mvn test -Pmock -Dkarate.env=mock -DargLine='-Dkarate.mock.port=9000'
``` 

To test with other environments, the name of the environment can be passed via `karate.env` variable. The configurations related to the selected environment needs to be updated in the `karate-config.js`


#### How to run Gatling Performance tests
Karate tests can be configured to run as performance tests. The perf tests can be run against any environment using the `-Dkarate.env=<env name>`

```
  mvn gatling:test -Pperf -Dkarate.env=<environment name>
``` 

#### **Securing your routes**

Your service checks *Autorization* header to validate authentication. That means that a caller needs to provide a proper *Autorization* header when making a call to path that's protected by [**jsk-spring-security**](https://github.intuit.com/services-java/jsk-spring-security). For example, this is how one can call */v1/documents* endpoint:

```
curl -k  -v https://localhost:8443/v1/documents\
     -H 'Authorization: Intuit_IAM_Authentication intuit_token_type=IAM-Ticket,intuit_appid=CLIENT_APPID, intuit_app_secret=CLIENT_APPSECRET,intuit_userid=INTUIT_USERID, intuit_token=INTUIT_USERTOKEN'
 
```

#### **Authentication via Intuit cookies**

If you would like to leverage authentication via cookies you may need to change to 'true' the following in [application-local.yml](src/main/resources/application-local.yml)

```
security:
  intuit:
    cookies:
     enabled: false
```

In addition you need to open your /etc/hosts file

```
sudo <your favorite editor> /etc/hosts

```
And add these entries 

```	
127.0.0.1    localhost.intuit.com
::1          localhost.intuit.com
```

Note that for local development the service uses a pregenerated
**keystore** that is located in **local-keystore** directory. It should not used except in local development environment.

#### **TLS termination in IKS**

Services at Intuit are required to run in **Transport Layer Security(TLS)** mode in *IKS*. Developers at Intuit have couple of options when in comes to *TLS*:

 * **Terminate TLS via a proxy such as [NGINX](https://nginx.com) or [Envoy](https://www.envoyproxy.io) running along-side the service**

 * **Terminate TLS in the service**

 
This example uses the second approach. It generates a keystore containing a self-signed certificate as part of the service launch. We run Java [keytool](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html) to generate the keystore every time the service is about to launch. The service uses the following configuration for *TLS*:

```
server:
  port: 8443
  ssl:
    enabled: true
    enabled-protocols: TLSv1.2
    key-alias: tomcat
    key-store: /app/tmp/keystore.pkc12
    key-store-password: ${TLS_KEYSTORE_PASSWORD}
    key-store-type: PKCS12

```
Makes sure the port specified above matches your deployment configuration's **containerPort** setting.

#### How to use OpenAPI/Swagger Playground 

From your web browser navigate [here](https://localhost:8443/swagger-ui.html)
