@ignore
Feature: Create User

  Background:
    * url oiiUrlBase

  Scenario: Create User
    * def getRandomUser =
    """
    function(){ return java.lang.System.nanoTime()+java.lang.Math.random() }
    """

    * def time = getRandomUser()

    * def username = 'createUser' + time + '_iamtestpass'
    * def email = username + '@sharklasers.com'
    * def password = 'Tester@123'
    * print "Creating user "+ username
    # Create a user for for testing the rest endpoints
    Given path 'v1', 'users', 'sign_up'
    And header Content-Type = 'application/json'
    And header Accept = 'application/json'
    And header intuit_originatingip = '123.45.67.89'
    And header Authorization = "Intuit_IAM_Authentication intuit_appid=" + appId + ",intuit_app_secret=" + appSecret
    And request { "user": { "username": #(username),"password": #(password),"email": {"primary": "true","address": #(email)},"phone": [{"type": "WORK","primary": "true","phoneNumber": "8293857324"}],"securityLevel": "HIGH","challengeQuestionAnswer": [{ "question": "Who was your childhood hero?","answer": "Elmo"}],"displayName": [#(username)],"fullName": [{"salutation": "Mr.","givenName": "Test","middleName": "U","surName": "Intuit","suffix": "Jr"}]}}

    When method post
    Then status 200
    * print "Sign up Response"
    * print response
    * def resp = $
    And def user = resp.user
    And def ticket = resp.iamticket
    And def username = username
    And def password = password
