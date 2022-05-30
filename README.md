# scheduler

Java SpringMVC Starter app for MSaaS

[![Build Status](https://build.intuit.com/growth/buildStatus/buildIcon?job=intcollabs-notification/scheduler/scheduler/master)](https://build.intuit.com/growth/job/intcollabs-notification/job/scheduler/job/scheduler/job/master/)
[![Code Coverage](https://build.intuit.com/growth/buildStatus/coverageIcon?job=intcollabs-notification/scheduler/scheduler/master)](https://build.intuit.com/growth//job/intcollabs-notificationjob/scheduler/job/scheduler/job/master/)

## Usage
This starter app is a simple Java SpringMVC service

[//]: # (local development) 

## Getting Started with Local Development

#### Prerequisites
* [Amazon Corretto Java 11](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/what-is-corretto-11.html)

* [Maven 3.6.3 OR later](https://maven.apache.org/download.cgi)

#### How to run the application locally

First, either type this terminal/shell command, or put it in your `~/.bashrc` or `~/.zshrc` file:

```
export NEXUS_PROXY_URL=https://nexus.intuit.com/nexus
```

From Terminal line run this command:
```
   mvn clean spring-boot:run -s settings.xml  -Dspring-boot.run.profiles=local
```

To make sure the application is running, check its health
```
   curl https://localhost:8443/health/full
```

(Add `-k` to ignore the fact that a self-signed cert is used.)

#### How to run unit tests

```
    mvn test -Punit
```

#### How to run karate tests

Run this command for testing against locally running server with e2e downstream services

```
    mvn test -Pkarate -Dkarate.env=local
```

Run this command for testing against locally running server with mocked downstream services

```
    mvn test -Pmock -Dkarate.env=mock
```

The `api-mock.feature` file is an example of how you can setup a mock api for any downstream dependencies. In addition to setting up mock endpoints and responses in the `api-mock.feature` file, you'll also have to set the url for that downstream dependency in the `karate-config.js` file. Instead of pointing to the actual url for the downstream dependency, it should point to your localhost and the karate mock port. By default, the karate mock port is randomized, but you can specify a port in the argline if you so choose. See below:

```
    mvn test -Pmock -Dkarate.env=mock -DargLine='-Dkarate.mock.port=9000'
``` 

To test with other environments, the name of the environment can be passed via `karate.env` variable. The configurations related to the selected environment needs to be updated in the `karate-config.js`


#### How to run gatling perf tests
Karate tests can be configured to run as perf tests. The perf tests can be run against any environment using the `-Dkarate.env=<env name>`

```
    mvn gatling:test -Pperf -Dkarate.env=<environment name>
``` 

#### How to use Swagger Documentation
From your web browser navigate [here](http://localhost:8080/swagger-ui.html)


#### Note about included certificate

The packaged certificate is OK for local development. However, we encourage you to use Intuit CA-signed or generate your own self-signed certificate for **TLS** termination.


## Technologies Used
- Spring Boot MVC
- JSK
- Karate
- Gatling

[Learn more](https://github.intuit.com/pages/kubernetes/modern-saas-docs/overview/) about all the technologies MSaaS Services use!

## Contributing

Eager to contribute to this service? Check out our [Contribution Guidelines](./CONTRIBUTING.md)! 

Learn more about code contributions: [Intuit's InnerSource Guidelines](http://in/innersource).

## Builds, Environments, and Deployments

- [IBP Job](https://build.intuit.com/growth/job/intcollabs-notification/job/scheduler/job/scheduler/job/master/)
- [MSaaS Environments - DevPortal](https://devportal.intuit.com/app/dp/resource/8722364171316486115/configuration/environment)

## Monitoring

- *Pre-Production and Production Logs* are automatically configured and can be found on the Modern SaaS Environments configuration of the [DevPortal asset for this repo](https://devportal.intuit.com/app/dp/resource/8722364171316486115/configuration/environment).

[Learn more](https://github.intuit.com/pages/kubernetes/modern-saas-docs/iks/iks_logging/) about MSaaS Logging.

## Support
For support related to the [MSaaS architecture](https://github.intuit.com/pages/kubernetes/modern-saas-docs/overview/), check out [StackOverflow](https://stackoverflow.intuit.com/questions/tagged/3).
