Contribution Guidelines
=======================
Great to have you here. Here are a few ways you can help make our project better!

- [Expectations Before Contribution](#Expectations-Before-Contribution)
	- [Adding Functionality](#Adding-Functionality)
	- [Bug Triage](#Bug-Triage)
- [Process for Contributing Code](#Process-for-Contributing-Code)
	- [Local Setup](#Local-Setup)
		- [Fork and Clone](#Fork-and-Clone)
		- [Commit](#Commit)
		- [Rebase](#Rebase)
		- [Test](#Test)
		- [Push](#Push)
		- [Pull Request](#Pull-Request)
- [Code Quality Expectations](#Code-Quality-Expectations)
	- [Tests](#Tests)
	- [Coverage](#Coverage)
	- [Documentation](#Documentation)
	- [Code Style](#Code-Style)
- [SLA](#SLA)
- [Contact Information](#Contact-Information)
	- [Team](#Team)
	- [Contact](#Contact)

# Expectations Before Contribution

## Adding Functionality
_How to request or add in functionality.  Indicate if you want a conversation before code contribution and how to have that conversation.  JIRA issues, GitHub issues, etc._

## Bug Triage
_How to report or fix bugs. For example:_

>How to request and fix bugs:

>* You can report bugs as issues for the project, give as much detail as possible. Logs, stacktraces, versions...etc.
>* You can look through the existing bugs/issues in the project and contribute.
>* Look at existing bugs and help us understand if
> * The bug is reproducible?
> * Is it reproducible in other environments (browsers)?
> * What are the steps to reproduce?
>* You can close fixed bugs by testing old tickets to see if they are happening

# Process for Contributing Code
_Add steps for contributing code, with information about pull request summary and description expectations._

## Local Setup

Make sure git knows your real name and email address:

```text
$ git config --global user.name "Jon Doe"
$ git config --global user.email "jon.doe@example.com"
```

### Fork and Clone

From the GitHub UI, fork the project into your user space or another organization.  Following the steps below, clone locally and add the upstream remote.

```text
$ git clone git@github.intuit.com:ORG/REPOSITORY.git
$ cd <project>

## If you have SSH keys set up, then add the SSH URL as an upstream.
$ git remote add upstream git@github.intuit.com/ORG/REPOSITORY.git

## If you want to type in your password when fetching from upstream, then add the HTTPS URL as an upstream.
$ git remote add upstream https://github.intuit.com/ORG/REPOSITORY.git
```

If later you want to switch your remote upstream from `https` to `ssh` or vice versa you can edit it using the [`git remote set-url`](https://help.github.com/articles/changing-a-remote-s-url/) command.

### Commit

Writing good commit logs is important.  A commit log should describe what
changed and why.  Make sure that commit message contains the JIRA or GitHub issue associated with the change.  Make sure to use GitHubs [special syntax](https://help.github.com/articles/closing-issues-via-commit-messages/) for closing issues via commit messages.

### Rebase

Use `git rebase` (not `git merge`) to sync your work from time to time.

```text
$ git fetch upstream
$ git rebase upstream/master
```

### Test

Bug fixes and features **should come with tests** and coverage should meet or exceed 85%

```text
$ mvn clean test
```

Make sure that all tests pass.  Please, do not submit patches that fail this check.

### Push

```text
$ git push origin my-feature-branch
```

### Pull Request
Go to https://github.intuit.com/ORG/REPOSITORY and select your fork.
Click the 'Pull Request' button and fill out the form.  Be sure to @mention code owners and/or maintainers.

# Code Quality Expectations
_Add in information about tests, coverage, documentation, and code style that you expect. For example:_

>### Tests
>All new Java methods should have correlated JUnit tests.
>### Coverage
>Ensure that code coverage does not fall below 80%.
>### Documentation
>Code should be well documented. What it is doing should be self explanatory based on coding conventions, however why the >code is doing something should be documented well.
>
>* Java code should have JavaDoc
>* `pom.xml` should have comments
>* Unit tests should have comments and failure messages
>* Integration tests should have comments and failure messages
>
>### Code Style
>We try to follow [Google's Coding Standards](https://google.github.io/styleguide/javaguide.html).  The easiest way is to just format based on existing code you see.  We don't enforce this, just a guideline.

# SLA
_Add in information about pull-request and issue request SLAs.  For example:_

>The pull request review SLA is 24 hours.  If there are comments
>to address, apply your changes in a separate commit and push that to your
>feature branch.  Post a comment in the pull request afterwards; GitHub does
>not send out notifications when you add commits.

# Contact Information
_Add in information about the committers to this repository and how to contact the team. For example:_

>## Team 

>[TEAM NAME]
>
>```
>Dev Manager: [MANAGER]
>Architect: [ARCHITECT]
>Lead Engineer: [LEAD ENGINEER]
>```

>## Contact

>Here is how you can contact the our team:

>* Slack: [YOUR SLACK CHANNEL]
>* JIRA: https://jira.intuit.com/browse/[YOUR JIRA SUBMIT POINT]