## Guidance for Maintainers of this Project

These are general recommendations for keeping a healthy service.

### Stay Up-to-date with Intuit Security Practices

Intuit relies on proper handling of customer data and following security rules and best practices. Our customers' 
financial lives, and our jobs, depend on that.

Every developer should be aware of the [Intuit Security Policies](https://intuitcloud.sharepoint.com/WorkResources/Pages/information-security-policies.aspx).

There is training on security and privacy available, currently via [Degreed](https://intuitcloud.sharepoint.com/WorkResources/Pages/degreed-at-intuit.aspx), and
the [Intuit Information Security resource page](https://intuitcloud.sharepoint.com/WorkResources/Pages/Intuit_Information_Security_IIS_TOC.aspx) has
other useful resources.

#### Specifically for Spring Boot

One specific [Security/Risk/Fraud ITAD](https://github.intuit.com/intuit-tech-arch-decisions/security-risk-fraud/) 
document should be reviewed by each developer on projects like the current one: 
[0043. Spring Boot Security Standard](https://github.intuit.com/intuit-tech-arch-decisions/security-risk-fraud/blob/master/doc/adr/0043-springboot-hardening.md#0043-spring-boot-security-standard).

### Watch for DevX Technical Security Bulletins

These should be communicated to you, but from time to time — or if you're new to Intuit — you should check 
[DevX Technical Support Bulletins (TSB)](https://wiki.intuit.com/pages/viewpage.action?pageId=479779504).

### Hygiene, Hygiene, Hygiene

At any given moment, doing run-the-business updates of dependencies, docker/podman images, compilers seems to
offer little payback for the effort. But deferred maintenance builds up geometrically: updating once a year is almost 
inevitably more complex/risky, and requires more time, than updating six or twelve times a year.

And when a security vulnerability in a dependency needs to be addressed ASAP, that difference can be painfully apparent.

For Java and Kotlin projects using [`jsk-bom`](https://github.intuit.com/services-java/jsk-bom#jsk-bom) 
or [`jsk-spring-boot-starter-parent`](https://github.intuit.com/services-java/jsk-spring-boot-starter-parent#description)
— as this project was vended — most
of the busy work of finding the right versions and updating is done by the JSK team, which publishes regular
updates (announced in `#ip-jsk-help`); see the release notes for more:
* [jsk-bom](https://github.intuit.com/services-java/jsk-bom/releases)
* [jsk-spring-boot-starter-parent](https://github.intuit.com/services-java/jsk-spring-boot-starter-parent/releases)

If you're new to Maven or BOMs, be sure to read [several sections starting here](https://github.intuit.com/services-java/jsk-bom#description).

_PS: as a software engineer, you needn't be reminded to read the release notes for every update you apply._

### For support, Use StackOverflow

Before looking for help from support channels:

* If the problem seems to be with a library, check the README **and** release notes of its source repo
* If the problem is with a service you're calling, peruse its pages on DevPortal
* If those don't help, or the problem is more ambiguous, search [Intuit StackOverflow](https://stackoverflow.intuit.com/)
* And of course searching [public StackOverflow](https://stackoverflow.com/questions) and Googling are recommended
    * Be **very** careful about copying code examples of more than a couple lines:
       * Make sure the code is published with a suitable license that allows for commercial use without payment
       * Needless to say, be sure you understand every clause in the code
       * Give credit **in the code** to the author(s) and provide a permalink (or equivalent citation) to the source
       * The above applies even if you modify the code somewhat
* **Before** resorting to Slacking in a support channel
    * Read the full description at the top (most have this)
    * Read all of the pinned posts (most support channels have at least one)
    * Read and search through the past few months of questions in the channel
    * Go over [How to report a bug or issue for support](http://in/ask-for-support) carefully
        * ... to make sure you've tried the obvious
        * ... and to make sure you are supplying the info needed
        * _(this link has JSK-specific items, but most are general)_

If you've done the above due diligence to no avail, try these channels (as of July 2022)
* _There are certainly other channels, and the channels evolve over time_
* `#msaas-support` (Modern Software-as-a-Service ≈ "Paved Road")
* `#gateway-support`
* `#ibp-community` (Intuit Build Platform)
* `#svc-artifactory`
* `#iip-sup-ticket`, `#iip-sup-general`, `#iip-sup-authz-2-0` (Identity)
* `#svc-github-support`
* `#di-support` (Data Infrastructure Platform)
* `#team-aws-support`
* `#team-oim` (metrics)
* `#cmty-o11y-appd`, `#cmt-o11y-tracing` ("o˙bservabilit˙y")
* `#cmty-karate` (the testing software, not the martial art)
* `#cmty-gatling` (performance testing)
* `#ip-jsk-help` (Java Service Kit)

And please do not tag `@here` or `@channel` in **any** of those channels, as this will notify 
hundreds or even thousands of developers, but endear you to none of them.