@ignore
Feature: Sign in User

  Background:
    * url mfaUrlBase

  Scenario:
    * configure headers = read('classpath:ius-headers.js')
    Given path 'v2', 'web','tickets','sign_in'
    And request { username: '#(username)', password: '#(password)', namespaceId:'50000003' }
    When method post
    Then status 200
    * print "Sign in Response"
    * print response
    * def resp = $

    # JSK will enforce authentication even in "permitAll" profiles if auth headers are sent in the request.
    # Remove mocked auth headers returned from IUS mock to enable permitAll
    And def ticket = env=='mock'?'':resp.iamTicket