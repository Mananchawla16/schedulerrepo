# Perf Test Code Template


## The recommended approach to building a script. 

1. Finalize the performance characteristics of the application which you would like to test. Tips: Use Splunk or Wavefront to capture the usage pattern of your APIs. 
2. Create a collection of requests this Gatling template (HTTP only).
3. Enter common configurations like "common-test.properties" file and environment specific configuration in the environment specific config files. 
4. Use IDPS (as shown in this template) or Jenkins credential store to store your sensitive information. 
5. Develop Gatling scenarios as shown in this example project. 
6. Export commonly accessed properties as system properties. Use the file `src/test/scala/com/intuit/gatling/simulation/SimulationProperties.scala` to declare your system properties. These propeprties need to be declared in Frontline simulations as well to override the default values. 
7. Build test simulations by using the pre-configured scenarios. 
8. Setup assertions to validate if the test goals are met for critical metrics such as response time, throughput etc. 
9. Use the slo.yaml file to declare Wavefront queries to validate system metrics such as CPU, Pod Restart count etc. 
10. Use the pre-built perf Jenkins pipeline to trigger the test through Jenkins for automatic release-pipeline stage validations. 


## Auth Headers
The template has examples of 

 - Private Auth
 - Prviate Auth Plus
 - Browser Auth

Refer the template file `src/test/scala/com/intuit/gatling/common/helper/AuthHeaders.scala` 

Consume the auth headers based on the auth requirements in the Auth.scala file `src/test/scala/com/intuit/gatling/common/identity/Auth.scala` 

## Ticket Caching

We use Ticket caching to ensure that the load test does not stress the auth servers. 

Below is an example scenario which consumes the `AuthTicket.AccessTicket.login_and_cacheTicket(csvFile)` function to iterate all the records in `user.csv` file and generates ticket/tokens for each user in the file.  The cached tickets are then used throughout the duration of the load test.
Example: 
```
val FakeDocumentLifeCycle: ScenarioBuilder = scenario("FakeDocument_LifeCycle")
.exec(AccessTicket.login_and_cacheTicket(user_csv_file))
.doIf("#{userTicket.exists()}") {
(
exec(FakeDocs.getAllDocuments)
...
)

```

## CSV File
We recommend that CSV files web stored in environment specific folders. In the vended template we have used "*env*"  environment variable which can be set to "**perf**" or "**prod**".  There are data files in `src/test/resources/data/perf` and `src/test/resources/data/prod`. The value passed to the environment variable will decide which `user.csv` file gets picked up. 

We highly recommend using ```
csv("foo.csv").shard```  to auto automatically divide a large CSV to get more realistic cache hit on the server.  Consuming the same data file across multiple load generators with small set of test-records typically does not exercise the cache and database as it would in production. 

Refer: https://gatling.io/docs/gatling/reference/current/core/session/feeder/

*Dealing with large data files:* Gatling supports compressed CSV files. The official gatling feeder documentations has more information about it. 

## IDPS

Note that the vended IDPS configuration will not work for your application. You will have to use your own IDPS backend policy ID, resource ID.  

Requirement: 
 - Please stick to IDPS Client Version 3.84.0 in your Gatling POM file. Any version greater than that will not work due to a bouncy castle dependency conflict issue.
 - IDPS server is protected by Intuit firewall. You cannot use a PUBLIC load generator. Always pick private load generaors when you have IDPS dependency in your gatling script. To identify private load generators in Frontline, look for the substring "Private", "Priv". Do not use load generators with the tag "Public" or "Pub" . 

Store your API keys/secrets and any other sensitive information in the IDPS. 

Example:
```### Identity access a.k.a ticket service for auth 
ticket.appSecret="{secret}idps:/frontline-test/ticket.preprod.apiSecret" ## change this

### IDPS Configuration. Use local IDPS policy id during the development phase.

### Avoid using apiKeyId and pem/certs.
idps.endpoint= "vkm.ps.idps.a.intuit.com"
idps.policyId="p-z6sddynlvk70" ## Change this
idps.apiKeyId="v2-7a625cefb68e3" ## Change this
```

Store your IDPS configuration in the environment specific properties file `src/test/resources/<env>-test.properties`

Optionally you can store these tokens in Jenkins credentials store and pass the values Frontline using the IBP Jenkins Perf Job.  

**Can I use PEM files to authenticate with IDPS server rather than using IDPS Policy ID?**.  
This gatling template allows you to use PEM files. Export the base64 encoded PEM content using the environment variable `sensitive.gatling.idps.secretKeyContentB64`. Set this as a system property in the Frontline simulation. Note that the PEM file approach is only recommended when you store the PEM content in Jenkins credentials store and run the gatling simulation through the Gatling Jenkins plugin. Storing the PEM file in the git repository not recommended at all and will likely get flagged by the security bot.  

> NOTE: If you have a modularized maven project with parent poms referring to a common IDPS SDK version higher than 3.84.0, you will run into a BC Prov (bouncy castle) conflict. We recomomend you to decouple your perf-tests from your main maven project by not including the \<parent\>...\</parent\> declaration in your gatling project. 

## Overriding default Gatling.conf
In some occassions you may have a requirement to override the default Gatling configuration such as the default connection timeout, default request timeout etc. You can the file `src/test/resources/gatling.conf` to modify these properties.


## Increasing the log level

You cannot increase the log level when the test is running from Frontline. However, you can increase the log level when you run the test locally by changing the values in `src/test/resources/logback-test.xml` 

Note `<logger name="io.gatling.http.engine.response" level="TRACE" />` will log the entire request and response payload. 

## Request collection

Compile all the requests which you want to use in your tests in the package `src/test/scala/com/intuit/gatling/requests` 
It is a recommended pattern to co-locate all requests.


## Configuration file

Fallback order of processing 

`<env>-test.properties -> common-test.properties --> deafult hardcoded overrides.` 

The environment specific properties file and common-test.properties file can be found in the  `src/test/resources/` directory. 

## Assertions

Assertions are mandatory for build-pipelines. Without assertoins the Gatling Jenkins pipeline will fail the perf stage. Assertions are away through which you can validate if the latency, error rate and throughput goals for the service is met. 

Example:
```
val listOfAssertions = List(
global.failedRequests.percent.lte(5),
details(FakeDocs.GET_DOCUMENT).responseTime.percentile(99).lte(1000),
details(FakeDocs.CREATE_DOCUMENT).responseTime.percentile(99).lte(2000),
details(FakeDocs.UPDATE_DOCUMENT).responseTime.percentile(99).lte(3000)
)
* For support, use the Slack channel [#ip-frontline](https://intuit-teams.slack.com/archives/CQ4TDK8TT).

